package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;


public class Controller {

    @FXML
    public TextArea textArea;

    private String filePath;
    private static File file;

    static String text;  //global variable that contains the contents of the file in the form of string
    static ArrayList<String> lines = new ArrayList<String>();  //all lines
    static ArrayList<ArrayList<String>> word;
    static ArrayList<ArrayList<String>> wordT ;//original word
    static ArrayList<ArrayList<String>> wordS ;//original word

    static boolean erreurLexical ;
    static boolean erreurSynataxic ;

    //event method -----------------------------------------------------------------------------------------------------



    //method to chose path of file
    public void getPath(javafx.event.ActionEvent actionEvent) {

        filePath = null;
        System.out.println("file uri " + filePath);
        try {
            FileChooser fileChooser = new FileChooser();
            file = fileChooser.showOpenDialog(null); //file name
            filePath = file.toURI().toString();

            String l[] = null;
            System.out.println("file uri" + filePath);

            if (getFileExtension(file).equals("compila")) {

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = "";
                text = "";
                int i = 0;
                erreurLexical = false;
                erreurSynataxic = false;
                word = new ArrayList<ArrayList<String>>();
                wordT = new ArrayList<ArrayList<String>>();
                wordS = new ArrayList<ArrayList<String>>();


                while ((line = br.readLine()) != null) {
                    text += line + "\n";
                    lines.add(line); // add to lines line
                }
                //initialize

                textArea.setText("");

                textArea.setText(text);

            } else {
                //dialog box
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                // alert.setHeaderText("Error");
                alert.setContentText("Please chose a file with extension .compila");

                alert.showAndWait();
            }
        } catch (Exception e) {

        }

    }


    //method to get file extension
    public String getFileExtension(File file) {
        String extension = "";

        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                int i = name.lastIndexOf('.');
                if (i >= 0) {
                    extension = name.substring(i + 1);
                }
            }
        } catch (Exception e) {
            extension = "";
        }

        return extension;

    }

    //Lexical analysis -------------------------------------------------------------------------------------------------
    //we analyse all the text word by word
    public void word(ActionEvent actionEvent) {
        LexicalAnalysis.lexicalAnalyse(lines ,word,wordT ,textArea);
    }

    //Syntactic analysis -----------------------------------------------------------------------------------------------
    @FXML
    void Syntactic(ActionEvent event) {
        if(!erreurLexical){
            SyntacticAnalysis.syntacticAnalysis(wordT,word,wordS,textArea);
        }
        else {
            textArea.setText("Please correct the lexical error\n ");
        }
    }

    //semantic analysis
    @FXML
    void Semantic(ActionEvent event) {
        if(!erreurLexical) {
           if(!erreurSynataxic) {
                SemanticAnalysis.semanticAnalyse(wordT, word, wordS, textArea);
            }
            else {
                textArea.setText("Please correct the Syntax error\n");
            }
        }
        else {
            textArea.setText("Please correct the lexical error\n ");
        }
    }

}
