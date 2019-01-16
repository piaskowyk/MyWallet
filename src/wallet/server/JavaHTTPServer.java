package wallet.server;

import wallet.server.controllers.Controller;
import wallet.server.exceptions.*;
import wallet.server.untils.ServerResponse;
import wallet.commonElements.untils.Validator;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class JavaHTTPServer implements Runnable{

    private static final int PORT = 8080;
    private static final boolean debug = true;
    private Socket connect;
    private static ArrayList<String> controllerList = new ArrayList<>();

    private JavaHTTPServer(Socket socket) {
        this.connect = socket;
    }

    public static void main(String[] args) {
        /*
        * dodaj kontrolery które dostępne są publiczne przez api, jedynie te kontrolery są możliwe do wywołąnia, zabezpiecza
        * to po części przed remote code execution
        * */
        controllerList.add("user");
        controllerList.add("wallet");
        controllerList.add("payments");
        controllerList.add("dashboard");

        /*
        * Uruchamiam serwer, który nasłuchuje na porcie 8080, każde połączenie jest uruchamiane na nowym wątku
        */
        try {
            ServerSocket serverConnect = new ServerSocket(PORT);

            if (debug) System.out.println("server started on port: " + PORT);

            while (true) {
                JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());

                if (debug) System.out.println("New connection (" + new Date() + ")");

                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("server Connection error : " + e.getMessage());
        }
    }

    /*
    * Po wykryciu połączenia, uruchamina jest funkcja run, która na postawie danych zawartych w adresie url wywołuje
    * odpowiednią metodę w kontrolerzy wykorzystując mechanizm refleksji.
    * pattern urla: /api/[nazwa kontrolera]/[akcja w kontrolerze]
    * dane przesyłane są w formacie json
    * w nagłówku requestu znajduje się token słurzący do autoryzacji urzytkonika, token generowany jest
    * podczas procesu logowania
    *
    * W na początku metody w kontrolerze tworzę obiekt, któ©y będzie zawierać dane zwracane urzytkonwnikowi,
    * potem próbuję zrzutować dane przysłąne przez urzytkoniwka w formacie json na oczekiwany objekt który ma być 
    * reprezentacją danych wejściowych dla danej metowy.
    * Następnie przeprowadzam autoryzację urzytkownika.
    * jeśi operacja się powiedzie. Wykonuję dalsze akcja specyficzne dla danej metody, uzupełniając przy tym danymi objekt
    * mający być odpowiedzią dla klienta.
    *
    * następnie rzutuję objekt z danymi do formatu json i zwracam go klientowi
    *
    * Objekty służące do dialogu user <-> serwer znajdują sę w pakiecie commonElements
    * */
    @Override
    public void run() {
        final String pathToController = "wallet.server.controllers.";
        BufferedReader inputBuffer = null;
        HashMap<String, String> headers = new HashMap<>();
        boolean flag = true;
        String requestMethod = null;
        String controllerName = null;
        String action = null;
        StringBuilder inputData = new StringBuilder();
        Object outputDataObject = null;

        try {
            //zczytywanie bufora wejściowego z danymi
            inputBuffer = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            ServerResponse serverResponse = new ServerResponse(connect.getOutputStream());

            //zapisywanie nagłówków header z requestu
            BufferedReader headerBuffer = inputBuffer;
            String urlHeader = null;
            String headerLine;
            int i = 0;
            while((headerLine = headerBuffer.readLine()).length() != 0){
                if(i == 0) urlHeader = headerLine;
                int index = headerLine.indexOf(' ');
                String key = headerLine.substring(0, index);
                String value = headerLine.substring(index + 1);
                headers.put(key, value);
                i++;
            }

            //sprawdzanie poprawności adresu url
            if(urlHeader != null && urlHeader.chars().filter(num -> num == '/').count() != 3){
                try {
                    StringTokenizer parse = new StringTokenizer(urlHeader);
                    requestMethod = parse.nextToken().toUpperCase();
                    String path = parse.nextToken();
                    path = path.substring(1);
                    String[] pathElements = path.split("/");

                    controllerName = pathElements[1];
                    if (!Validator.isValidUrlControllersPath(controllerName)) throw new UnpermittedCharsException();
                    controllerName = controllerName.toLowerCase();

                    action = pathElements[2];
                    if (!Validator.isValidUrlControllersPath(action)) throw new UnpermittedCharsException();
                    action = action.toLowerCase();

                } catch (Exception e){
                    e.printStackTrace();
                    serverResponse.badRequest();
                    flag = false;
                }

            } else {
                serverResponse.badRequest();
                flag = false;
            }

            //obsługa poprawnego zapytania
            if(flag && (requestMethod.equals("GET") || requestMethod.equals("POST"))) {
                //wczytywanie danych zapisanych w formacie json
                while(headerBuffer.ready()){
                    inputData.append((char) headerBuffer.read());
                }

                //wywołąnie akcji w kontrolerze
                try{
                    if (!controllerList.contains(controllerName)) throw new ControllerNotExistException();

                    //kontroler i akcja muszą spełniać oddpowiedni pattern nazw, po części zabezpiecza to remot code execution
                    action += "Action";
                    controllerName = controllerName.substring(0, 1).toUpperCase() + controllerName.substring(1);
                    controllerName += "Controller";

                    //próba wywołania akcji w kontrolerze
                    try{
                        Class<?> controllerClass = Class.forName(pathToController + controllerName);
                        Constructor<?> controllerConstructor = controllerClass.getConstructor();
                        Object controllerObject = controllerConstructor.newInstance();
                        if (!(controllerObject instanceof Controller)) throw new ForbiddenControllerException();

                        //wysyłanie nagłówków do konrolera
                        Method setUp = controllerClass.getMethod("setHeaders", HashMap.class);
                        setUp.invoke(controllerObject, headers);

                        Method method = controllerClass.getMethod(action, String.class);
                        outputDataObject = method.invoke(controllerObject, inputData.toString());

                    }
                    catch (InvalidInputDataException e){
                        throw new InvalidInputDataException();
                    }
                    catch (Exception e){
                        if (debug) System.out.println("Controller or action not exist.");
                        e.printStackTrace();
                        throw new ControllerNotExistException();
                    }

                }
                catch (InvalidInputDataException e){
                    if (debug) System.out.println("Invalid input data.");
                    e.printStackTrace();
                    serverResponse.incorrectInputData();
                    flag = false;
                }
                catch (ActionNotExistException | ControllerNotExistException e){
                    e.printStackTrace();
                    serverResponse.notFound();
                    flag = false;
                }
                catch (Exception e){
                    if (debug) System.out.println("Undefined server error.");
                    serverResponse.serverError();
                    e.printStackTrace();
                    flag = false;
                }

                //wysyłąnie odpowiedzi serwera
                if(flag){
                    serverResponse.setData(outputDataObject);
                    serverResponse.response();
                }
            }
            else {
                if(flag) serverResponse.notSupportedMethod();
            }

        } catch (IOException e) { //złapanie wyjątków
            e.printStackTrace();

        } finally {
            try {
                if (inputBuffer != null) inputBuffer.close();
                connect.close();

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (debug) System.out.println("Connection closed.\n");
        }
    }
}