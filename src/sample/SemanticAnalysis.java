package sample;

import javafx.scene.control.TextArea;

import java.util.ArrayList;

public class SemanticAnalysis {

    public static void semanticAnalyse(ArrayList<ArrayList<String>> wordT, ArrayList<ArrayList<String>> word, ArrayList<ArrayList<String>> wordS, TextArea textArea) {
        String message = "";
        textArea.setText("");
        ArrayList<ArrayList<String>> variable = new ArrayList<ArrayList<String>>();    //line = type / colone = nom variable
        variable.add(new ArrayList<String>());
        variable.add(new ArrayList<String>());
        variable.add(new ArrayList<String>());
        for (int a = 0; a < word.size(); a++) {
            SyntacticAnalysis.word(word.get(a).get(0), textArea, "");
            declaration(wordT.get(a), wordS.get(a), variable, textArea);
            SyntacticAnalysis.isComment(wordT.get(a), word.get(a), textArea, "Example of a ");
            affectation(wordT.get(a), wordS.get(a), variable, textArea);
            SyntacticAnalysis.displayMessage(wordT.get(a), word.get(a), wordS.get(a), textArea, "", false);
            displayVar(wordT.get(a), word.get(a), textArea);
            SyntacticAnalysis.conditionAction(wordT.get(a), word.get(a), wordS.get(a), textArea, message);

        }
    }


    //line 0 -> entier
    //line 1 -> reel
    //line 2 -> String
    public static boolean declaration(ArrayList<String> lineT, ArrayList<String> lineS, ArrayList<ArrayList<String>> variabe, TextArea textArea) {

        boolean result = false;
        if (lineS.get(0).equals("Declaration")) {
            textArea.setText(textArea.getText() + String.join(" ", lineT) + "         Declaration of " + lineS.get(1) + " type variable(s)  " + lineS.get(2) + " \n");
            for (int i = 0; i < Integer.parseInt(lineS.get(1)); i++) {
                switch (lineS.get(2)) {
                    case "entier":
                        variabe.get(0).add(lineS.get(lineS.size() - 1 - i));
                        break;
                    case "reel":
                        variabe.get(1).add(lineS.get(lineS.size() - 1 - i));
                        break;
                    case "string":
                        variabe.get(2).add(lineS.get(lineS.size() - 1 - i));
                        break;
                }
            }
            System.out.println("****************************************************************************************" + variabe);
        }

        return result;
    }

    public static boolean affectation(ArrayList<String> lineT, ArrayList<String> lineS, ArrayList<ArrayList<String>> variable, TextArea textArea) {
        boolean result = false;
        if (lineS.get(lineS.size() - 1).equals("actionOfCondition")){
            textArea.setText(textArea.getText() + String.join(" ", lineT) + "        " +  "  Condition and action\n");
        }
        else{//**********************************************************************************************************************************************
            if (lineS.get(0).equals("AffectationVal")) {
                //si ident a le type du val
                boolean exist = false;
                switch (lineS.get(2)) {
                    case "entier":result = variable.get(0).contains(lineS.get(1)) ;
                        exist = result || exist;
                        break;
                    case "reel":
                        result = (variable.get(0).contains(lineS.get(1)) || variable.get(1).contains(lineS.get(1)));
                        exist = result || exist;
                        break;
                    case "string":
                        result = variable.get(2).contains(lineS.get(1));
                        exist = result || exist;
                        break;
                }
              if(exist){
                if (result)
                    textArea.setText(textArea.getText() + String.join(" ", lineT) + "         Assigning  value" + lineS.get(3) + " to  " + lineS.get(1) + " \n");
                else {
                    result = true;
                    textArea.setText(textArea.getText() + String.join(" ", lineT) + "        type semantic error of value is different to variable  " + lineS.get(1) + " \n");
                }
              }
              else {
                  textArea.setText(textArea.getText() + String.join(" ", lineT) + "        Semantic error variable  " + lineS.get(1) + " is not declare  \n");
              }
            } else {
                if (lineS.get(0).equals("AffectationVar")) {
                    //result = true;
                    //si ident a le type du ident
                result = ((variable.get(2).contains(lineS.get(1)) && variable.get(2).contains(lineS.get(2)))
                        || (variable.get(0).contains(lineS.get(1)) && variable.get(1).contains(lineS.get(2)))
                        || (variable.get(1).contains(lineS.get(1)) && variable.get(1).contains(lineS.get(2)))
                        || (variable.get(0).contains(lineS.get(1)) && variable.get(0).contains(lineS.get(2))));

                    if (result)
                        textArea.setText(textArea.getText() + String.join(" ", lineT) + "        Affectation la variable  " + lineS.get(2) + " a " + lineS.get(1) + " \n");
                    else {
                        result = true;

                        textArea.setText(textArea.getText() + String.join(" ", lineT) + "        semantic error type of variable " + lineS.get(1) + " is different to variable  " + lineS.get(2) + " \n");
                    }
                }

            }
        }
        return result;
    }

    public static boolean displayVar(ArrayList<String> lineT, ArrayList<String> lineS, TextArea textArea) {
        boolean result = false;
        if (lineS.get(0).equals("SHOWVAL")) {
            result = true;
            textArea.setText(textArea.getText() + String.join(" ", lineT) + "       value display   " + /*lineS.get(1) +*/ " \n");

        }
        return result;
    }
}