/*
 *    The University of Melbourne
 *    School of Computing and Information Systems
 *    COMP90015 Distributed System
 *    Semester 01/2020
 *
 *    Author: QUANG TRUNG LE - 987445
 */


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadsDictionaryServer extends DictionaryServer implements Runnable {


    /**
     * Override the method run() of Runnable
     */
    public void run() {
        System.out.println("Client connecting ...");
        this.execute();
        System.out.println("Client disconnect!\n");
    }


    public static void main(String[] args) throws IOException {
        MultiThreadsDictionaryServer main = new MultiThreadsDictionaryServer();

        main.extractPortFile(args); // extract port and fileLocation

        System.out.println("Dictionary Server is running...");

        ServerSocket serverSocket = new ServerSocket(main.getServerPort());


        // Loop connection
        while (true) {
            // waiting for client connection
            Socket socket = serverSocket.accept();

            socket.setSoTimeout(15000); //waiting time for connection

            MultiThreadsDictionaryServer server = new MultiThreadsDictionaryServer();
            server.setDictionaryService(new PlainDictionaryService());
            server.setSocket(socket);

            // start a new server thread...
            Thread thread = new Thread(server);
            thread.start();

        }
    }


    /**
     * Extract server Port and File Location from the input parameters
     */
    private void extractPortFile(String[] inputParameter) {

        try {
            if (inputParameter.length == 2) {
                this.setServerPort(Integer.parseInt(inputParameter[0]));
                this.setServerFileLocation(inputParameter[1]);
            } else
                throw new IllegalArgumentException("Illegal Inputs - Follow <host> <dictionaryFile>");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

}
