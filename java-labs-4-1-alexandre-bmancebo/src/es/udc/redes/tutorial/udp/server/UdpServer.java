package es.udc.redes.tutorial.udp.server;

import java.net.*;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String[] argv) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }
        DatagramSocket socketUdp = null;
        try {
            // get server port
            int port = Integer.parseInt(argv[0]);  // in string -> int

            // Create a server socket                                       socket = new DatagramSocket();
            socketUdp = new DatagramSocket(port);

            // Set maximum timeout to 300 secs/ 300_000 milliseconds
            socketUdp.setSoTimeout(300000);

            while (true) {
                // Prepare datagram for reception
                byte[] dataInput = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(dataInput, dataInput.length);
                // Receive the message
                socketUdp.receive(receivePacket);
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                // Get client address
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                // Console print received message
                System.out.println("SERVER: Received " + message + " from " + clientAddress + ":" + clientPort);
                // Prepare datagram to send response
                byte[] dataOutput = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(dataOutput, dataOutput.length, clientAddress, clientPort);
                // Send response
                socketUdp.send(sendPacket);
                // Console print
                System.out.println("SERVER: Sending " + message + " to " + clientAddress + ":" + clientPort);
            }
        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            //Close the socket
            if (socketUdp != null && !socketUdp.isClosed())
                socketUdp.close();
        }
    }
}
