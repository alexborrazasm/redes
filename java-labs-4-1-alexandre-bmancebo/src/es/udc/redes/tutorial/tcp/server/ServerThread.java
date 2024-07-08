package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * Thread that processes an echo server connection.
 */

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
            // Set the output channel
            DataOutputStream outputClient =
                    new DataOutputStream(socket.getOutputStream());
            // Receive the message from the client
            String message = inputClient.readLine();
            // Console print received message
            System.out.println("SERVER: Received " + message + " from " + clientAddress + ":" + clientPort);
            // Sent the echo message to the client
            outputClient.writeBytes(message);
            // Console print send message
            System.out.println("SERVER: Sending " + message + " to " + clientAddress + ":" + clientPort);
            // Close the streams
            inputClient.close();
            outputClient.close();
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Close the socket
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
