/*
 *    The University of Melbourne
 *    School of Computing and Information Systems
 *    COMP90015 Distributed System
 *    Semester 01/2020
 *
 *    Author: QUANG TRUNG LE
 */


import java.io.*;
import java.net.Socket;

public class DictionaryClient{
    //Default
    private String serverAddress = "127.0.0.1";
    private int serverPort = 2020;

    private Socket socket;
    private boolean socketConnect = false;

    public static void main(String[] args) {
        DictionaryClient main = new DictionaryClient();

        main.extractPortAddress(args); // Extract Port and Address from inputted Argument

        System.out.println("Client is running ...");

        DictionaryClientGUI window = new DictionaryClientGUI();
        window.generateGUI();
//        main.execute();

    }

    /**
     * Connect to server, send the outMessage and receive the incomingMessage
     */
    String execute(String outMessage){
        String returnedString = "";

        try {
            // send request to server
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(outMessage);

            // get the result from the server
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            returnedString = dataInputStream.readUTF();

            // Close all streams
            dataOutputStream.close();
            outputStream.close();
            dataInputStream.close();
            inputStream.close();

            socket.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }
        return returnedString;
    }


    /**
     * Extract Port and Address from inputted Argument
     */
    private void extractPortAddress(String[] inputArgument) throws IllegalArgumentException{
        try {
            if (inputArgument.length == 2) {
                serverAddress = inputArgument[0];
                serverPort = Integer.parseInt(inputArgument[1]);
            }
            else
                throw new IllegalArgumentException("Illegal Inputs - Follow <server Address> <server Port>");
        }
        catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

    }


    /**
     * Initialize socket
     */
    public void initSocket(){
        try{
            socket = new Socket(serverAddress, serverPort);
            socketConnect = true;
        }
        catch (Exception e) {
            socketConnect = false;
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }
    }

    public boolean isSocketConnect() {
        return socketConnect;
    }

    public void closeSocket() throws IOException {
        socket.close();
    }

}
