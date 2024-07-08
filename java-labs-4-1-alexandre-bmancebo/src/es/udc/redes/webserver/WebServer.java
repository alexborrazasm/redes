package es.udc.redes.webserver;

import java.net.ServerSocket;
import java.net.*;

public class WebServer {
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Format: es.udc.redes.webserver.WebServer <port>");
            System.exit(-1);
        }
        ServerSocket listeningSocket = null;
        // get server port
        int port = Integer.parseInt(args[0]);
        try {
            // Create a ServerSocket bound to the specified port
            listeningSocket = new ServerSocket(port);
            System.out.println("Web server started on port " + port);
            // Set a timeout of 300 secs/ 300_000 milliseconds
            listeningSocket.setSoTimeout(300000);
            // Listen for incoming connections
            while (true) {
                // Accept incoming connection
                Socket connectionSocket = listeningSocket.accept();
                // Create a new thread to handle the client connection
                ServerThread serverThread = new ServerThread(connectionSocket);
                // Start the thread
                serverThread.start();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            //Close the socket
            try {
                if (listeningSocket != null && !listeningSocket.isClosed())
                    listeningSocket.close();
            } catch (Exception e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}

