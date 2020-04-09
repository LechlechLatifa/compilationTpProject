package sample;

import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.Arrays;

public class SyntacticAnalysis {

    //syntacticAnalysis
    public static void syntacticAnalysis(ArrayList<ArrayList<String>> wordT, ArrayList<ArrayList<String>> word, ArrayList<ArrayList<String>> wordS, TextArea textArea) {

        textArea.setText("");
        String message = "chaînne reconnue";
        for (int i = 0; i < word.size(); i++) {
            wordS.add((ArrayList<String>) (word.get(i)).clone());
        }
        for (int a = 0; a < word.size(); a++) {
            word(word.get(a).get(0), textArea, message);
            declaration(wordT.get(a), word.get(a), wordS.get(a), textArea,false);
            isComment(wordT.get(a), word.get(a), textArea, message);
            Affctation(wordT.get(a), word.get(a), wordS.get(a), textArea, message, false);
            display(wordT.get(a), word.get(a), wordS.get(a), textArea, message, false);
            conditionAction(wordT.get(a), word.get(a), wordS.get(a), textArea, message);



            System.out.println(a + "---------------------------------------------------------------------------------------------------------------------------");

          /*  for (int i = 0; i < wordS.size(); i++) {
                System.out.println(i + " ===========================================================");
                System.out.println(wordS.get(i));
            }*/
            System.out.println("wordS"+wordS);
            System.out.println("word"+word);
            System.out.println("wordt"+wordT);
        }
        isError(textArea);

    }


    //isOneWord
    public static boolean word(String word, TextArea textArea, String message) {
        boolean result = true;
        switch (word) {
            case "SP":
                textArea.setText(textArea.getText() + "Start_Program         " + message + "  Start of the program\n");
                break;

            case "ELSE":
                textArea.setText(textArea.getText() + "Else                 " + message + "   else\n");
                break;
            case "S":
                textArea.setText(textArea.getText() + "Start               " + message + "   Start of the block\n");
                break;
            case "E":
                textArea.setText(textArea.getText() + "Finish              " + message + "   end of a block\n");
                break;
            case "EP":
                textArea.setText(textArea.getText() + "End_Program         " + message + "  End of the Programme\n");
                break;
            default:
                result = false;

        }
        return result;
    }

    //is comment
    public static boolean isComment(ArrayList<String> lineT, ArrayList<String> line, TextArea textArea, String message) {              //it's work
        boolean result = false;
        if (line.get(0).matches("SC")) {
            textArea.setText(textArea.getText() + String.join(" ", lineT) + "        " + message + " comment \n");

            result = true;
        }
        return result;
    }


    //isDeclaration
    public static boolean declaration(ArrayList<String> lineT, ArrayList<String> line, ArrayList<String> lineS, TextArea textArea, Boolean condition) {
        boolean result = false;
        String type = "";
        int nbrId = 0;


        if (line.get(0).equals("INT")) {
            lineS.clear();
            type = "d'entier(s)";

            lineS.add("entier");
        } else {
            if (line.get(0).equals("REAL")) {
                type = "de reel(s)";
                lineS.clear();
                lineS.add("reel");
            } else {
                if (line.get(0).equals("STRING")) {
                    type = "chaine de caractere";
                    lineS.clear();
                    lineS.add("string");
                }
            }
        }
        if (!(type.equals(""))) {

            result = true;
            if (line.get(1).equals("COLON")) {
                if (line.get(line.size() - 1).equals("EL")) {
                    if (line.get(2).equals("IDENT")) {
                        lineS.add(lineT.get(2));
                        if (!(line.size() == 4)) {
                            int i = 3;
                            while (i < line.size() - 1) {
                                if (line.get(i).equals("COMMA")) {
                                    i++;
                                    if ((line.get(i).equals("IDENT"))) {
                                        nbrId++;
                                        lineS.add(lineT.get(i));
                                        i++;
                                    } else {
                                        textArea.setText(textArea.getText() + String.join(" ", line) + "        **Error**  nom d'une variable Expected \n");
                                        Controller.erreurSynataxic = true;
                                        i = line.size() - 1;
                                        result = false;

                                    }
                                } else {
                                    textArea.setText(textArea.getText() + String.join(" ", line) + "        **Error**  , Expected \n");
                                    Controller.erreurSynataxic = true;
                                    i = line.size() - 1;
                                    result = false;

                                }
                            }
                        }
                        if (result == true) {
                            nbrId++;
                            lineS.add(0, "Declaration");
                            lineS.add(1, "" + nbrId);
                            if (!condition)
                                textArea.setText(textArea.getText() + String.join(" ", lineT) + "         recognized channel (Declaration " + type + ") \n");

                        }
                    } else {
                        Controller.erreurSynataxic = true;
                        textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  nom d'une variable Expected \n");
                    }

                } else {
                    Controller.erreurSynataxic = true;
                    textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  ;; Expected \n");
                }
            } else {
                Controller.erreurSynataxic = true;
                textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  : Expected \n");
            }
        }


        return result;


    }

    //isAffectation
    public static boolean Affctation(ArrayList<String> lineT, ArrayList<String> line, ArrayList<String> lineS, TextArea textArea, String Message, Boolean condition) {
        boolean result = false;
        String type = "";

        if (AffectationVar(lineT, line, lineS, textArea, Message)) {
            lineS.add(0, "AffectationVar");
            result = true;
        } else {
            if (result != true) {
                if (AffectationVal(lineT, line, lineS, textArea, Message, condition)) {
                    lineS.add(0, "AffectationVal");
                    result = true;
                }
            }
        }
        return result;
    }

    //is AffectationVal
    public static boolean AffectationVal
    (ArrayList<String> lineT, ArrayList<String> line, ArrayList<String> lineS, TextArea textArea, String Message, Boolean condition) {//--------------------------------------------------------
        boolean result = false;
        String type = "";
        if (line.get(0).equals("GIVE")) {
            result = true;
            lineS.clear();
            if (line.get(1).equals("IDENT")) {
                lineS.add(lineT.get(1));
                if (line.get(2).equals("COLON")) {
                    if (line.get(3).equals("VINT")) {
                        type = "entier";
                        lineS.add("entier");
                        lineS.add(lineT.get(3));
                    } else {
                        if (line.get(3).equals("VREAL")) {
                            type = "de reel(s)";
                            lineS.add("reel");
                            lineS.add(lineT.get(3));
                        } else {
                            if (line.get(3).equals("VSTRING")) {
                                type = "chaine de caractere";
                                lineS.add("chaine de caractere");
                                lineS.add(lineT.get(3));
                            } else {
                                Controller.erreurSynataxic = true;
                                textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  value Expected \n");
                            }
                        }
                    }
                    if (!(type.equals(""))) {

                        if (line.get(4).equals("EL")) {
                            if (!condition) {
                                textArea.setText(textArea.getText() + String.join(" ", lineT) + "        " + Message + " Assigning value to a variable\n");
                            }//*********************************************************************************************************************************************************************************

                        } else {
                            Controller.erreurSynataxic = true;
                            textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  ;; Expected \n");
                        }
                    }
                } else {
                    Controller.erreurSynataxic = true;
                    textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  : Expected \n");
                }
            } else {
                Controller.erreurSynataxic = true;
                textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  name of variable Expected \n");
            }
        }

        return result;

    }

    //is AffectationVar
    public static boolean AffectationVar(ArrayList<String> lineT, ArrayList<String> line, ArrayList<String> lineS, TextArea textArea, String Message) {
        boolean result = false;

        if (line.get(0).equals("AFFECT")) {
            result = true;
            lineS.clear();
            if (line.get(1).equals("IDENT")) {
                lineS.add(lineT.get(1));
                if (line.get(2).equals("TO")) {
                    if (line.get(3).equals("IDENT")) {
                        lineS.add(lineT.get(3));

                        if (line.get(4).equals("EL")) {
                            textArea.setText(textArea.getText() + String.join(" ", lineT) + "        " + Message + "  Assigning value to a variable\n");

                        } else {
                            Controller.erreurSynataxic = true;
                            textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  ;; Expected \n");
                        }

                    } else {
                        Controller.erreurSynataxic = true;
                        textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  name of variable Expected \n");
                    }
                } else {
                    Controller.erreurSynataxic = true;
                    textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  to Expected \n");
                }
            }
        }

        return result;
    }


    //Affichage
    public static boolean display(ArrayList<String> lineT, ArrayList<String> line, ArrayList<String> lineS, TextArea textArea, String Message, Boolean condition) {
        boolean result = false;

        if (displayMessage(lineT, line, lineS, textArea, Message, condition)) {
            result = true;
        } else {
            if (result != true) {
                if (displayVal(lineT, line, lineS, textArea, Message, condition)) {
                    result = true;
                }
            }
        }
        return result;
    }


    public static boolean displayMessage(ArrayList<String> lineT, ArrayList<String> line, ArrayList<String> lineS, TextArea textArea, String Message, boolean condition) {
        boolean result = false;

        if (line.get(0).equals("SHOWMES")) {
            result = true;

            if (line.get(1).equals("COLON")) {
                if (line.get(2).equals("QUOTE")) {
                    if (line.get(line.size() - 2).equals("QUOTE")) {
                        if (line.get(line.size() - 1).equals("EL")) {
                            if (!condition)
                                textArea.setText(textArea.getText() + String.join(" ", lineT) + "        " + Message + " Displaying a message on the screen\n");

                        } else {
                            Controller.erreurSynataxic = true;
                            textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  ;; Expected \n");
                        }
                    } else {
                        Controller.erreurSynataxic = true;
                        textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  \" Expected in the end of the message \n");
                    }
                } else {
                    Controller.erreurSynataxic = true;
                    textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  \" Expected in the beginning of the message \n");
                }
            } else {
                Controller.erreurSynataxic = true;
                textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  : Expected \n");
            }
        }

        return result;
    }


    public static boolean displayVal(ArrayList<String> lineT, ArrayList<String> line, ArrayList<String> lineS, TextArea textArea, String Message, boolean condition) {
        boolean result = false;

        if (line.get(0).equals("SHOWVAL")) {
            result = true;
            lineS.clear();
            lineS.add("SHOWVAL");
            if (line.get(1).equals("COLON")) {

                if (line.get(2).equals("IDENT")) {
                    lineS.add(lineT.get(2));
                    if (line.get(3).equals("EL")) {
                        if (!condition)
                            textArea.setText(textArea.getText() + String.join(" ", lineT) + "        " + Message + " variable display\n");

                    } else {
                        Controller.erreurSynataxic = true;
                        textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  ;; Expected \n");
                    }
                } else {
                    Controller.erreurSynataxic = true;
                    textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  name of variable Expected \n");
                }
            } else {
                Controller.erreurSynataxic = true;
                textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  : Expected \n");
            }
        }
        return result;
    }


    public static boolean conditionAction(ArrayList<String> lineT, ArrayList<String> line, ArrayList<String> lineS, TextArea textArea, String Message) {
        boolean result = false;

        if (line.get(0).equals("IF")) {
            result = true;
            //lineS.clear();
            if (line.get(1).equals("HYPHEN")) {
                if (line.get(2).equals("IDENT")) {
                    if (line.get(3).equals("L") || line.get(3).equals("LE") || line.get(3).equals("S") || line.get(3).equals("SE") || line.get(3).equals("NE") || line.get(3).equals("EQ")) {
                        if (line.get(4).equals("IDENT")) {
                            if (line.get(5).equals("HYPHEN")) {

                                ArrayList<String> lineT1 = new ArrayList<String>();
                                for (int i = 6; i < lineT.size() - 1; i++) {
                                    lineT1.add(lineT.get(i));
                                }

                                for (int j = 0; j < 6; j++) {
                                    lineS.remove(0);
                                    line.remove(0);
                                }


                                if ((SyntacticAnalysis.declaration(lineT1, line, lineS, textArea, true)) || (SyntacticAnalysis.Affctation(lineT1, line, lineS, textArea, "", true)) || (SyntacticAnalysis.display(lineT1, line, lineS, textArea, "", true))) {
                                    lineS.add("actionOfCondition");
                                    textArea.setText(textArea.getText() + String.join(" ", lineT) + "        " + Message + "  Condition and action\n");

                                }
                            } else {
                                textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  -- Expected \n");
                                Controller.erreurSynataxic = true;
                            }
                        } else {
                            textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  name of variable Expected \n");
                            Controller.erreurSynataxic = true;
                        }
                    } else {
                        textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  <,<=,>,>= Expected \n");
                        Controller.erreurSynataxic = true;
                    }
                } else {
                    textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  name of variable Expected \n");
                    Controller.erreurSynataxic = true;
                }
            } else {
                textArea.setText(textArea.getText() + String.join(" ", lineT) + "        **Error**  -- Expected \n");
                Controller.erreurSynataxic = true;
            }

        }
        return result;
    }

    public static void isError(TextArea textArea) {

        if (LexicalAnalysis.de == 1) {
            textArea.setText(textArea.getText() + "Error -----------------------------------------------------------\n");
            textArea.setText(textArea.getText() + "You have forgotten End_Program\n");
            Controller.erreurSynataxic = true;
        } else {
            if (LexicalAnalysis.de == 2) {
                textArea.setText(textArea.getText() + "Error-----------------------------------------------------------\n");
                textArea.setText(textArea.getText() + "You have forgotten Start_Program\n");
                Controller.erreurSynataxic = true;
            }
        }
        //si de == 3 c'est juste


        if (LexicalAnalysis.ce == 2) {
            textArea.setText(textArea.getText() + "Error-----------------------------------------------------------\n");
            textArea.setText(textArea.getText() + "You have forgotten If\n");
            Controller.erreurSynataxic = true;
        } else {
            if (LexicalAnalysis.ce == 3) {
                //verifier le bloc
                switch (LexicalAnalysis.sf) {
                    case 1:
                        textArea.setText(textArea.getText() + "Error -----------------------------------------------------------\n");
                        textArea.setText(textArea.getText() + "You have forgotten Finish\n");
                        Controller.erreurSynataxic = true;
                        break;
                    case 2:
                        textArea.setText(textArea.getText() + "Error -----------------------------------------------------------\n");
                        textArea.setText(textArea.getText() + "You have forgotten Srart\n");
                        Controller.erreurSynataxic = true;
                        break;
                    default:
                        break;

                }

            }
        }
    }
}

