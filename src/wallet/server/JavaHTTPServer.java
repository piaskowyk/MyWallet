package wallet.server;

import com.google.gson.Gson;
import wallet.server.Exceptions.ActionNotExist;
import wallet.server.Exceptions.ControllerNotExist;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class JavaHTTPServer implements Runnable{

    private static final int PORT = 8080;
    private static final boolean debug = true;
    private Socket connect;
    private static String[] controllerList = null;
    private String pathToController = "wallet.server.Controllers.";

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

        Tmp aaa = null;

        try {
            inputBuffer = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            ServerResponse serverResponse = new ServerResponse(connect.getOutputStream());

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

            if(urlHeader != null && urlHeader.chars().filter(num -> num == '/').count() != 3){
                try {
                    StringTokenizer parse = new StringTokenizer(urlHeader);
                    method = parse.nextToken().toUpperCase();
                    String path = parse.nextToken();
                    path = path.substring(1);
                    String[] pathElements = path.split("/");
                    controlerName = pathElements[1];
                    controlerName = controlerName.toLowerCase();
                    action = pathElements[2];
                    action = action.toLowerCase();
                } catch (Exception e){
                    serverResponse.badRequest();
                    flag = false;
                }
            } else {
                serverResponse.badRequest();
                flag = false;
            }

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
                    //if (!exist) throw new ControllerNotExist();

                    //if(!action.contains(".+action$")) throw new ActionNotExist();

                    //capitalize controller
                    controlerName = controlerName.substring(0, 1).toUpperCase() + controlerName.substring(1);

                    //Class controllerClass = Class.forName(pathToController + controlerName);

                    //Class<?> controllerClass = Class.forName(pathToController + controlerName);
                    Class<?> controllerClass = Class.forName(pathToController + "User");
                    //try{
//                        Object controllerObject = controllerClass.getConstructor().newInstance();
//                        Controller controller = (Controller) controllerObject;

                        Constructor<?> controllerConstructor = controllerClass.getConstructor();
                        Object controllerObject = controllerConstructor.newInstance(); /*new Object[] { ctorArgument }*/
                        //Controller controller = (Controller) controllerObject;

                        Method tmp = controllerClass.getMethod("indexAction");
                    System.out.println(tmp.toString());
                        Tmp a = (Tmp)tmp.invoke(controllerObject);
                    aaa = a;
                    System.out.println("bbbbbbbbbbbbbbbbbbbbbbb");
//                    }
//                    catch (Exception e){
//                        throw new ControllerNotExist();
//                    }

                }
                catch (ActionNotExist | ControllerNotExist e){
                    serverResponse.notFound();
                    flag = false;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
//                catch (Exception e){
//                    flag = false;
//                }

                //response output data
                if(flag){
                    serverResponse.setData(aaa);
                    serverResponse.response();
                }
            }

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);

        } finally {
            try {
                if (inputBuffer != null) inputBuffer.close();
                connect.close();

            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (debug) System.out.println("Connection closed.\n");
        }
    }
}