package es.udc.redes.tutorial.tcp.server;
import java.net.*;

/**
 * Multithread TCP echo server.
 */

public class TcpServer {

    public static void main(String[] argv) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
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
                // Create a ServerThread object, with the new connection as parameter
                ServerThread serverThread = new ServerThread(connectionSocket);
                // Initiate thread using the start() method
                serverThread.start();
            }
            // Uncomment next catch clause after implementing the logic
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
