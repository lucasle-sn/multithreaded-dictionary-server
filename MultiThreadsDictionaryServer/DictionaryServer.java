/*
 *    The University of Melbourne
 *    School of Computing and Information Systems
 *    COMP90015 Distributed System
 *    Semester 01/2020
 *
 *    Author: QUANG TRUNG LE - 987445
 */


import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class DictionaryServer implements Runnable {

    private int serverPort = 2020;    // Default port
    private String serverFileLocation = "dictionary.txt";

    private DictionaryService dictionaryService;
    private Socket socket;


//    private JSONArray vocabListLine = new JSONArray();

    // Override the method run() of Runnable
    public void run() {
        this.execute();
    }


    // =============== EXECUTION ===============
    /**
     * Create the connection, get inputStream and push outputStream
     */
    public void execute() {
        try {
            // get the result from the server
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            String incomingMessage = (dataInputStream.readUTF());

            // DATA PROCESS on returnedString
            String outMessage = this.parseExecution(incomingMessage);

            // push the returned result to client
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(outMessage);

            // close all the streams
            dataInputStream.close();
            inputStream.close();
            dataOutputStream.close();
            outputStream.close();

            socket.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }
    }

    /**
     * Process the incoming Message to return the outMessage
     */
    private String parseExecution(String incomingMessage) {
        String command = getCommand(incomingMessage);
        String vocabulary = getVocabulary(incomingMessage);
        String meaning = getMeaning(incomingMessage);

        int messageLen = getIncomingMessageLength(command,vocabulary,meaning);

        JSONObject returnedObject = new JSONObject();

        //  getDictionaryServiceResult
        try{
            if (command.equals(CmdType.SEARCH_VOCAB.value())){
                checkArgument(messageLen,CmdType.SEARCH_VOCAB.getMinLength(),CmdType.SEARCH_VOCAB.getMaxLength());
                returnedObject = dictionaryService.searching(vocabulary);
            }
            else if (command.equals(CmdType.ADD_VOCAB.value())) {
                checkArgument(messageLen, CmdType.ADD_VOCAB.getMinLength(), CmdType.ADD_VOCAB.getMaxLength());
                returnedObject = dictionaryService.adding(vocabulary, meaning);
            }
            else if (command.equals(CmdType.REMOVE_VOCAB.value())) {
                checkArgument(messageLen, CmdType.REMOVE_VOCAB.getMinLength(), CmdType.REMOVE_VOCAB.getMaxLength());
                returnedObject = dictionaryService.removing(vocabulary);
            }
            else{
                returnedObject.put(JsonKey.ANNOUNCEMENT.value(),"Invalid command!");
                throw new IllegalArgumentException("Invalid command!");
            }
        }
        catch (IllegalArgumentException | JSONException e){
            System.out.println(e.getMessage());
        }

        return returnedObject.toString();
    }

    /**
     * Check the validity of the argument and throw an exception if the argument is invalid.
     * @throws IllegalArgumentException when syntax length < min syntax length of command type.
     */
    private void checkArgument(int length, int minLength, int maxLength) throws IllegalArgumentException{
        if (length < minLength || length > maxLength)
            throw new IllegalArgumentException("Invalid number of input parameters!");
    }

    /**
     * Get the Length of incoming Message (# parameters)
     */
    private int getIncomingMessageLength(String command,String vocabulary,String meaning){
        int messageLen = 3;

        if (command.isEmpty()) messageLen--;
        if (vocabulary.isEmpty()) messageLen--;
        if (meaning.isEmpty()) messageLen--;

        return messageLen;
    }



    // =============== JSON Decode ===============
    /**
     * Extract "command" from the incoming message
     */
    private String getCommand(String incomingMessage) {
        String command = "";

        try {
            JSONObject jsonObject = new JSONObject(incomingMessage);
            command = jsonObject.get(JsonKey.COMMAND.value()).toString();
        }
        catch(JSONException e) {
            e.getMessage();
        }
        return command;
    }

    /**
     * Extract "vocabulary" from the incoming message
     */
    private String getVocabulary(String incomingMessage) {
        String vocabulary = "";

        try {
            JSONObject jsonObject = new JSONObject(incomingMessage);
            vocabulary = jsonObject.get(JsonKey.VOCABULARY.value()).toString();
        }
        catch(JSONException e) {
            e.getMessage();
        }
        return vocabulary;
    }

    /**
     * Extract "meaning" from the incoming message
     */
    private String getMeaning(String incomingMessage) {
        String meaning = "";

        try {
            JSONObject jsonObject = new JSONObject(incomingMessage);
            meaning = jsonObject.get(JsonKey.MEANING.value()).toString();
        }
        catch(JSONException e) {
            e.getMessage();
        }

        return meaning;
    }


     // =============== GETTERS & SETTERS ===============
    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerFileLocation() {
        return serverFileLocation;
    }

    public void setServerFileLocation(String serverFileLocation) {
        this.serverFileLocation = serverFileLocation;
    }

}
