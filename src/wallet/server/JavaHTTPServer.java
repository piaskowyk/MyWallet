package wallet.server;

import com.google.gson.Gson;
import wallet.server.Controllers.Controller;
import wallet.server.Exceptions.*;
import wallet.server.Helpers.Validator;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;

public class JavaHTTPServer implements Runnable{

    private static final int PORT = 8080;
    private static final boolean debug = true;
    private final String pathToController = "wallet.server.Controllers.";
    private Socket connect;
    private static String[] controllerList = null;

    private JavaHTTPServer(Socket socket) {
        this.connect = socket;
    }

    public static void main(String[] args) throws FileNotFoundException {
        //collect public controller list
        Gson gson = new Gson();
        FileReader reader = new FileReader("src/wallet/server/controllersList.json");
        controllerList = gson.fromJson(reader, String[].class);

        try {
            ServerSocket serverConnect = new ServerSocket(PORT);

            if (debug) System.out.println("Server started on port: " + PORT);

            while (true) {
                JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());

                if (debug) System.out.println("New connection (" + new Date() + ")");

                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        BufferedReader inputBuffer = null;
        String fileRequested = null;
        Map<String, String> headers = new HashMap<String, String>();
        boolean flag = true;
        String method = null;
        String controlerName = null;
        String action = null;
        StringBuilder inputData = new StringBuilder();
        Object outpurDataObject = null;

        try {
            inputBuffer = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            ServerResponse serverResponse = new ServerResponse(connect.getOutputStream());

            //reading data from request headers
            BufferedReader headerBuffer = inputBuffer;
            String urlHeader = null;
            String headerLine = null;
            int i = 0;
            while((headerLine = headerBuffer.readLine()).length() != 0){
                if(i == 0) urlHeader = headerLine;
                int index = headerLine.indexOf(' ');
                String key = headerLine.substring(0, index);
                String value = headerLine.substring(index + 1);
                headers.put(key, value);
                i++;
            }

            //check request url validation
            if(urlHeader != null && urlHeader.chars().filter(num -> num == '/').count() != 3){
                try {
                    StringTokenizer parse = new StringTokenizer(urlHeader);
                    method = parse.nextToken().toUpperCase();
                    String path = parse.nextToken();
                    path = path.substring(1);
                    String[] pathElements = path.split("/");

                    controlerName = pathElements[1];
                    if (!Validator.isValidUrlControllersPath(controlerName)) throw new UnpermittedCharsException();
                    controlerName = controlerName.toLowerCase();

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

            //handling request
            if(flag && (method.equals("GET") || method.equals("POST"))) {
                //reading input data
                while(headerBuffer.ready()){
                    inputData.append((char) headerBuffer.read());
                }

                //execute action from controller
                Object outData = null;
                try{
                    boolean exist = false;
                    for (String item : controllerList){
                        if(item.equals(controlerName)) exist = true;
                    }

                    if (!exist) throw new ControllerNotExistException();

                    action += "Action";
                    controlerName = controlerName.substring(0, 1).toUpperCase() + controlerName.substring(1);
                    controlerName += "Controller";

                    try{
                        Class<?> controllerClass = Class.forName(pathToController + controlerName);
                        Constructor<?> controllerConstructor = controllerClass.getConstructor();
                        Object controllerObject = controllerConstructor.newInstance();
                        if (!(controllerObject instanceof Controller)) throw new ForbiddenControllerException();

                        //set up request headers in controller
                        Method setUp = controllerClass.getMethod("setHeaders", HashMap.class);
                        setUp.invoke(controllerObject, headers);

                        Method tmp = controllerClass.getMethod(action, String.class);
                        outpurDataObject = (Object)tmp.invoke(controllerObject, inputData.toString());

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

                //response output data
                if(flag){
                    serverResponse.setData(outpurDataObject);
                    serverResponse.response();
                }
            }
            else {
                if(flag) serverResponse.notSupportedMethod();
            }

        } catch (IOException e) { //catch server errors
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