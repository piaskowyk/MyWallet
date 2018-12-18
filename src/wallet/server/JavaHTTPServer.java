package wallet.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

import com.google.gson.Gson;

public class JavaHTTPServer implements Runnable{

    private static final int PORT = 8080;
    private static final boolean debug = true;
    private Socket connect;

    private JavaHTTPServer(Socket socket) {
        this.connect = socket;
    }

    public static void main(String[] args) {
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
        // we manage our particular client connection
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {
            // we read characters from the client via input stream on the socket
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            // we get character output stream to client (for headers)
            out = new PrintWriter(connect.getOutputStream());
            // get binary output stream to client (for requested data)
            dataOut = new BufferedOutputStream(connect.getOutputStream());
            // get first line of the request from the client
            String input = in.readLine();


///////////////////////////////////////////////////////////////////////////////////
            System.out.println(input);
            BufferedReader br = in;

//code to read and print headers
            String headerLine = null;
            while((headerLine = br.readLine()).length() != 0){
                System.out.println(headerLine);
            }

//code to read the post payload data
            StringBuilder payload = new StringBuilder();
            while(br.ready()){
                payload.append((char) br.read());
            }
            System.out.println("Payload data is: "+payload.toString());
///////////////////////////////////////////////////////////////////////////////////



            // we parse the request with a string tokenizer
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
            // we get file requested
            fileRequested = parse.nextToken();
            System.out.println(fileRequested);


            if(method.equals("GET") || method.equals("POST")) {
                // GET or HEAD method
//                if (fileRequested.endsWith("/")) {
//                    fileRequested += DEFAULT_FILE;
//                }

//                File file = new File(WEB_ROOT, fileRequested);
//                int fileLength = (int) file.length();
//                String content = getContentType(fileRequested);

                String content = "";

                Gson gson = new Gson();
                ServerResponse obj = new ServerResponse(1, "mlekomleko");
                String json = gson.toJson(obj);
                content = json;


                if (method.equals("GET") || method.equals("POST")) { // GET method so we return content
                    //byte[] fileData = readFileData(file, fileLength);

                    // send HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: xdddddddddddddddddddddd");
                    out.println("Date: " + new Date());
                    out.println("Content-type: ok");
                    out.println("Content-length: " + content.length());
                    out.println(); // blank line between headers and content, very important !
                    out.flush(); // flush character output stream buffer

                    dataOut.write(content.getBytes(), 0, content.length());
                    dataOut.flush();
                }

                if (debug) System.out.println("File "  + " of type "  + " returned");

            }

        } catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(out, dataOut, fileRequested);
            } catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                connect.close(); // we close socket connection
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (debug) {
               // System.out.println("Connection closed.\n");
            }
        }


    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

    // return supported MIME Types
    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return "text/html";
        else
            return "text/plain";
    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
//        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
//        int fileLength = (int) file.length();
//        String content = "text/html";
//        byte[] fileData = readFileData(file, fileLength);
//
//        out.println("HTTP/1.1 404 File Not Found");
//        out.println("Server: Java HTTP Server from SSaurel : 1.0");
//        out.println("Date: " + new Date());
//        out.println("Content-type: " + content);
//        out.println("Content-length: " + fileLength);
//        out.println(); // blank line between headers and content, very important !
//        out.flush(); // flush character output stream buffer
//
//        dataOut.write(fileData, 0, fileLength);
//        dataOut.flush();
//
//        if (debug) {
//            System.out.println("File " + fileRequested + " not found");
//        }
    }

}