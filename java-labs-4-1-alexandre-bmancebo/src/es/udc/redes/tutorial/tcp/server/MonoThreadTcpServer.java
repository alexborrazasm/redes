package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String[] argv) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }
        ServerSocket listeningSocket = null;
        try {
            // get server port
            int port = Integer.parseInt(argv[0]);  // in string -> int
            // Create a server socket
            listeningSocket = new ServerSocket(port);

            // Set a timeout of 300 secs/ 300_000 milliseconds
            listeningSocket.setSoTimeout(300000);
            while (true) {
                // Wait for connections
                Socket connectionSocket = listeningSocket.accept();
                // Get client address
                InetAddress clientAddress = connectionSocket.getInetAddress();
                int clientPort = connectionSocket.getPort();
                // Set the input channel
                BufferedReader inputClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                // Set the output channel
                DataOutputStream outputClient =
                        new DataOutputStream(connectionSocket.getOutputStream());
                // Receive the client message
                String message = inputClient.readLine();
                // Console print received message
                System.out.println("SERVER: Received " + message + " from " + clientAddress + ":" + clientPort);
                // Send response to the client
                outputClient.writeBytes(message);
                // Console print send message
                System.out.println("SERVER: Sending " + message + " to " + clientAddress + ":" + clientPort);
                // Close the streams
                inputClient.close();
                outputClient.close();
                //Close the socket
                connectionSocket.close();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
	        //Close the socket
            if (listeningSocket != null && !listeningSocket.isClosed()) {
                try {
                    listeningSocket.close();
                } catch (Exception e) {
                    System.err.println("Error closing server socket: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}