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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DictionaryClientGUI {
    // GUI variables
    private JPanel panelMain;
    private JPanel panelWest;
    private JPanel panelNorthEast;
    private JPanel panelSouthEast;
    private JPanel panelEast;
    private JPanel panelNELeft;
    private JPanel panelNERight;
    private JButton okButton;
    private JButton clearButton;
    private JLabel announcementLabel;
    private JLabel wordLabel;
    private JLabel meaningLabel;
    private JTextField wordTextfield;
    private JTextField meaningTextfield;
    private JComboBox commandComboBox;

    private String command = commandComboBox.getItemAt(commandComboBox.getSelectedIndex()).toString().toLowerCase();
    private String vocabulary = "";
    private String meaning = "";

    private static final String ANNOUNCEMENT_DEFAULT = "Welcome! Please choose the command.";


    // ==============================
    public DictionaryClientGUI() {
        commandComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                command = commandComboBox.getItemAt(commandComboBox.getSelectedIndex()).toString().toLowerCase();
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{

                    DictionaryClient client = new DictionaryClient();

                    vocabulary = wordTextfield.getText();
                    meaning = meaningTextfield.getText();

                    client.initSocket();

                    if (!client.isSocketConnect())
                        announcementLabel.setText("Server not connected");
                    else {
                        if (checkParameters()) {
                            String outMessage = generateOutMessage();

                            String incomingMessage = client.execute(outMessage);

                            String meaning = getMeaning(incomingMessage);
                            String announcement = getAnnouncement(incomingMessage);

                            meaningTextfield.setText(meaning);
                            announcementLabel.setText(announcement);
                        }
                        else
                            client.closeSocket();
                    }
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                wordTextfield.setText("");
                meaningTextfield.setText("");
                announcementLabel.setText(ANNOUNCEMENT_DEFAULT);
            }
        });
    }

    /**
     * Generate a GUI
     */
    void generateGUI(){
        JFrame frame = new JFrame();

        frame.setTitle("Dictionary Service");
        frame.setLayout(new FlowLayout());
        frame.setSize(450,300);
        frame.setLocation(150,150);
        frame.setContentPane(new DictionaryClientGUI().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        announcementLabel.setText(ANNOUNCEMENT_DEFAULT);
    }


    // =============== DATA PROCESSING ===============
    /**
     * Generate the outMessage to send to server, including command,vocabulary, meaning
     */
    private String generateOutMessage(){
        JSONObject jOutMessage = new JSONObject();

        jOutMessage.put(JsonKey.COMMAND.value(),command);
        jOutMessage.put(JsonKey.VOCABULARY.value(),vocabulary);
        jOutMessage.put(JsonKey.MEANING.value(),meaning);

        return jOutMessage.toString();
    }

    /**
     * Check if the number of parameters for each command correct
     */
    private boolean checkParameters() {
        String exception = "";

        if (command.isEmpty())
            exception = "Illegal command!";
        else if ((command.equals(CmdType.SEARCH_VOCAB.value()) || command.equals(CmdType.REMOVE_VOCAB.value()))
                && vocabulary.isEmpty())
            exception = "Word is empty!";
        else if (command.equals(CmdType.ADD_VOCAB.value()) && (vocabulary.isEmpty() || meaning.isEmpty()))
            exception = "Word and/or Meaning is empty!";

        if (exception.isEmpty())
            return true;
        else {
            announcementLabel.setText(exception);
            return false;
        }
    }


    // =============== JSON Decode ===============
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

    /**
     * Extract "announcement" from the incoming message
     */
    private String getAnnouncement(String incomingMessage) {
        String announcement = "";

        try {
            JSONObject jsonObject = new JSONObject(incomingMessage);
            announcement = jsonObject.get(JsonKey.ANNOUNCEMENT.value()).toString();
        }
        catch(JSONException e) {
            e.getMessage();
        }
        return announcement;
    }

}
