package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ServerThread extends Thread {

    private final Socket socket;

    public ServerThread(Socket s) {
        // Store the socket s
        this.socket = s;
    }

    public void run() {
        try {
            // Get client address
            InetAddress clientAddress = socket.getInetAddress();
            int clientPort = socket.getPort();
            // Set the input channel
            BufferedReader inputClient =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // This code processes HTTP requests and generates
            String request = inputClient.readLine();
            System.out.println("SERVER: Received " + request + " from " + clientAddress + ":" + clientPort);
            if (request != null) {
                String[] requestParts = request.split(" ");
                String method = requestParts[0];
                String requestResource = requestParts[1];
                String serverPath = "p1-files";

                if (!method.equals("HEAD") && !method.equals("GET")) {
                    String filePath = serverPath + File.separator + "error400.html";
                    File errorFile = new File(filePath);
                    handleBadRequest(socket, errorFile);
                } else {
                    File resource = new File(serverPath + requestResource);
                    File error404File = new File(serverPath + File.separator + "error404.html");
                    if (method.equals("GET") && resource.exists()) {
                        handleGet(socket, inputClient, resource);
                    } else if (method.equals("HEAD") && resource.exists()) {
                        handleHead(socket, resource, "200 OK");
                    } else if (method.equals("GET") && !resource.exists()) {
                        handleNotFound(socket, error404File);
                    } else if (method.equals("HEAD") && !resource.exists()) {
                        handleHead(socket, error404File, "404 Not Found");
                    }
                }
                inputClient.close();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Close the client socket
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
    public void handleGet(Socket clientSocket, BufferedReader inputClient, File resource) throws IOException {
        boolean modifiedSince = true;
        String lastModified = getDateModified(resource);
        String response;
        // Read HTTP client request
        String request = inputClient.readLine();
        while(!request.isEmpty()) {
            String[] parts = request.split(": ");
            if (parts[0].equals("If-Modified-Since")) {
                modifiedSince = isModifiedSince(lastModified, parts[1]);
                System.out.println(request);
            }
            request = inputClient.readLine();
        }
        if (modifiedSince) {
            response = getHTTPResponse("200 OK", resource, resource.toPath());
            sendHTTPResponse(socket, response);
            sendResource(clientSocket, resource);
        } else {
            response = getHTTPResponse("304 Not Modified", resource, null);
            sendHTTPResponse(socket, response);
        }
    }
    public void handleHead(Socket socket, File resource, String code) throws IOException {
        String response = getHTTPResponse(code, resource, resource.toPath());
        sendHTTPResponse(socket, response);
    }
    public void handleBadRequest(Socket socket, File resource) throws IOException {
        String response = getHTTPResponse("400 Bad Request", resource, resource.toPath());
        sendHTTPResponse(socket, response);
        sendResource(socket, resource);
    }
    public void handleNotFound(Socket socket, File resource) throws IOException {
        String response = getHTTPResponse("404 Not Found", resource, resource.toPath());
        sendHTTPResponse(socket, response);
        sendResource(socket, resource);
    }
    private String getDate(){ // Returns actual date
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        return format.format(date);
    }
    private String getDateModified(File file){ // Return last modified file date
        Date dateModified = new Date(file.lastModified());
        DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        return format.format(dateModified);
    }
    private String getHTTPResponse(String code, File resource, Path path) throws IOException {
        String nL = "\r\n";
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("HTTP/1.0 ").append(code).append(nL);
        responseBuilder.append("Date: ").append(getDate()).append(nL);
        responseBuilder.append("Server: ficServer/0.0.1 (Java)").append(nL);
        if(path != null) {
            responseBuilder.append("Last-Modified: ").append(getDateModified(resource)).append(nL);
            long bitsLength = Files.size(path);
            responseBuilder.append("Content-Length: ").append(bitsLength).append(nL);
            String contentType = Files.probeContentType(path);
            responseBuilder.append("Content-Type: ").append(contentType).append(nL);
        }
        responseBuilder.append(nL);
        return responseBuilder.toString();
    }
    private boolean isModifiedSince(String serverDate, String clientDate) {
        DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

        try {
            // String -> Date
            Date lastModifiedDate = format.parse(serverDate);
            Date modifiedSinceDate = format.parse(clientDate);

            // Compare
            return lastModifiedDate.after(modifiedSinceDate);
        } catch (ParseException e) {
            System.err.println("Error parsing dates: " + e.getMessage());
            return false;
        }
    }
    private void sendHTTPResponse(Socket socket, String response) throws IOException {
        BufferedWriter outputClient =
                new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        System.out.println("SERVER: Sending response from "
                + socket.getLocalAddress() + ":" + socket.getPort()
                + "\nRenponse:\n" + response);
        outputClient.write(response);
        outputClient.flush();
    }
    private void sendResource(Socket clientSocket, File resource) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(resource);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        fileInputStream.close();
        outputStream.close();
    }
}