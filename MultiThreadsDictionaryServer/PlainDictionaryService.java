/*
 *    The University of Melbourne
 *    School of Computing and Information Systems
 *    COMP90015 Distributed System
 *    Semester 01/2020
 *
 *    Author: QUANG TRUNG LE - 987445
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class PlainDictionaryService implements DictionaryService{

    private static final String SEARCHING_SUCCESS_MESSAGE = "Search completed!";
    private static final String SEARCHING_FAIL_MESSAGE = "Word does not exist!";
    private static final String ADDING_SUCCESS_MESSAGE = "Adding completed!";
    private static final String ADDING_FAIL_MESSAGE = "Word has already existed!";
    private static final String REMOVING_SUCCESS_MESSAGE = "Search completed!";
    private static final String REMOVING_FAIL_MESSAGE = "Word does not exist!";

    private JSONArray vocabListLine = new JSONArray();
    private String serverFileLocation = new DictionaryServer().getServerFileLocation();


    //==================== SERVICES ====================
    /**
     * Search for "vocabulary" from the "vocabListLine" loaded from the file
     */
    public synchronized JSONObject searching(String vocabulary) {
        System.out.print("Searching ... ");

        this.readDictionaryFile(serverFileLocation);

        // Default is "Searching not succeeds"
        String searchingMeaning = "";
        String announcement = SEARCHING_FAIL_MESSAGE;

        int vocabIndex = getVocabIndex(vocabulary, vocabListLine);
        if (vocabIndex >= 0) {
            searchingMeaning = vocabListLine.getJSONObject(vocabIndex).get(JsonKey.MEANING.value()).toString();
            announcement = SEARCHING_SUCCESS_MESSAGE;
        }

        JSONObject searchingObject = this.generateOutMessage(searchingMeaning,announcement);

        System.out.println("Done!");
        return searchingObject;
    }

    /**
     * Add a word into the "vocabListLine" loaded from the file
     */
    public synchronized JSONObject adding (String vocabulary, String meaning) {
        System.out.print("Adding ... ");

        this.readDictionaryFile(serverFileLocation);
        // Default setup is "Adding not succeeds"
        String addingMeaning = "";
        String announcement = ADDING_FAIL_MESSAGE;


        if (getVocabIndex(vocabulary, vocabListLine) < 0) {
            JSONObject newWord = new JSONObject();
            newWord.put(JsonKey.VOCABULARY.value(),vocabulary);
            newWord.put(JsonKey.MEANING.value(),meaning);

            vocabListLine.put(newWord);
            this.sortVocabulary(vocabListLine);

            this.writeDictionaryFile(serverFileLocation);

            announcement = ADDING_SUCCESS_MESSAGE;
        }

        JSONObject addingObject = this.generateOutMessage(addingMeaning,announcement);

        System.out.println("Done!");
        return addingObject;
    }

    /**
     * Remove a word from the "vocabListLine" loaded from the file
     */
    public synchronized JSONObject removing (String vocabulary) {
        System.out.print("Removing ... ");

        this.readDictionaryFile(serverFileLocation);
        // Default is "Removing not succeeds"
        String removingMeaning = "";
        String announcement = REMOVING_FAIL_MESSAGE;

        int index = getVocabIndex(vocabulary, vocabListLine);
        if (index >= 0) {
            vocabListLine.remove(index);
            this.writeDictionaryFile(serverFileLocation);

            announcement = REMOVING_SUCCESS_MESSAGE;
        }

        JSONObject removingObject = this.generateOutMessage(removingMeaning,announcement);

        System.out.println("Done!");
        return removingObject;
    }



    //==================== MESSAGE PROCESSING ====================
    /**
     * Generate outMessage from successStatus, meaning of word, announcement
     * Format {meaning, announcement}
     */
    private JSONObject generateOutMessage(String meaning,String announcement){
        JSONObject jObject = new JSONObject();

        jObject.put(JsonKey.MEANING.value(),meaning);
        jObject.put(JsonKey.ANNOUNCEMENT.value(),announcement);

        return jObject;
    }

    /**
     * @return The index of vocab in the Dictionary.
     */
    private int getVocabIndex(String vocabulary, JSONArray vocabList){
        int vocabIndex = -1;
        int numVocab = vocabList.length();

        try{
            for (int index = 0; index < numVocab; index++){
                if (vocabulary.equals(vocabList.getJSONObject(index).get(JsonKey.VOCABULARY.value()).toString())){
                    vocabIndex = index;
                    break;
                }
            }
        }
        catch (JSONException e){
            System.out.println(e.getMessage());
        }

        return vocabIndex;
    }


    //==================== EDIT DATA ON LIST ====================
    /**
     * Swap position of vocab in the list at "index01" and "index02".
     */
    private void swapVocabulary(JSONArray vocabListLine, int index01,int index02){

        try{
            JSONObject vocabulary = vocabListLine.getJSONObject(index01);

            vocabListLine.put(index01,vocabListLine.getJSONObject(index02));
            vocabListLine.put(index02,vocabulary);
        }
        catch (JSONException e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Sort vocab in the List in alphabet order of vocab.
     */
    private void sortVocabulary (JSONArray vocabListLine){

        int numVocabulary = vocabListLine.length();

        try{
            for(int minPosition = numVocabulary-1; minPosition>0; minPosition--){
                for (int index = 0; index < minPosition; index++) {
                    String vocab00 = vocabListLine.getJSONObject(index).get(JsonKey.VOCABULARY.value()).toString();
                    String vocab01 = vocabListLine.getJSONObject(index+1).get(JsonKey.VOCABULARY.value()).toString();

                    if (vocab01.compareTo(vocab00) < 0)
                        this.swapVocabulary(vocabListLine,index,index+1);
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }


    // =============== FILE PROCESSING ===============
    /**
     * Read all the information of vocabulary from file.
     */
    public synchronized void readDictionaryFile(String file){
        try{
            Scanner inputStream = new Scanner(new FileInputStream(file));

            while (inputStream.hasNextLine()){
                String dataLine = inputStream.nextLine();

                JSONObject dataLineJSON = new JSONObject(dataLine);
                vocabListLine.put(dataLineJSON);
            }
            inputStream.close();
        }
        catch (FileNotFoundException e){
            this.createOutputStream(file);
        }
    }

    /**
     * Write all the information of vocabulary to file.
     */
    public synchronized void writeDictionaryFile(String file){
        PrintWriter outputStream = this.createOutputStream(file);

        int vocabListLength = vocabListLine.length();
        for (int index = 0; index < vocabListLength; index++) {
            outputStream.println(vocabListLine.getJSONObject(index).toString());
        }

        outputStream.close();
    }

    /**
     * Create a file to store vocabulary list.
     * @return outputStream
     */
    private PrintWriter createOutputStream(String file){
        PrintWriter outputStream = null;

        try{
            outputStream = new PrintWriter(new FileOutputStream(file));
        }
        catch (FileNotFoundException e){
            System.out.println("File can not be created.");
            System.exit(0);
        }
        return outputStream;
    }

}
