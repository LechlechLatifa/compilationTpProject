package sample;

import javafx.scene.control.TextArea;

import java.util.ArrayList;

public class LexicalAnalysis {

    String id = "[a-z|A-Z]+(_?[[a-z|A-Z]|[0-9]]+)*";
    // this variable it use to check the syntactic analysis
    static int de = 0; //debut_Prgm End_prgm
    static int sf = 0; //Start Finish
    static int ce = 0; //condition else


    //----------------------------------------------Line----------------------------------------------------------------
    //isComment
    public static boolean isComment(ArrayList<String> line, TextArea textArea) {                                        //it's work
        boolean r = false;
        if (line.get(0).matches("//.")) {
            textArea.setText(textArea.getText() + "//.      word refers to the start of a comment \n");
            line.set(0, "SC");
            for (int i = 1; i < line.size(); i++) {
                textArea.setText(textArea.getText() + line.get(i) + "       un comment \n");
                line.set(i, "C");
            }
            r = true;
        }
        return r;
    }

    //isMessage
    // [ShowMes, :, ", ceci, est, un, message, ";;] contains("\"");
    public static boolean isMessage(ArrayList<String> line,ArrayList<String> lineS, TextArea textArea) {                                        //it's work
        boolean r = false;
        if (line.contains("\"")) { // line contain "
            if (line.get(0).matches("ShowMes")) {
                textArea.setText(textArea.getText() + line.get(0) + "       reserved word for message display\n\n");
                line.set(0, "SHOWMES");
                if (line.get(1).matches(":")) {
                    textArea.setText(textArea.getText() + line.get(1) + "      reserved character\n");
                    line.set(1, "COLON");
                    if (line.get(2).matches("\"")) {
                        textArea.setText(textArea.getText() + line.get(2) + "      reserved character\n");
                        line.set(2, "QUOTE");
                        //la fin
                        // if la fin contain ";; ou ;; alors
                        if (line.get(line.size() - 1).matches("\";;") || line.get(line.size() - 1).matches(";;")) {

                            for (int i = 3; i <= line.size() - 2; i++) { //le reste c'est le message
                                if (line.get(i).matches("[a-z|A-Z](_?([a-z|A-Z|0-9])+)")) {
                                    textArea.setText(textArea.getText() + line.get(i) + "      string\n");
                                    line.set(i, "VSTRING");
                                } else {
                                    textArea.setText(textArea.getText() + line.get(i) + "       Lexical error\n");
                                    line.set(i, "ERROR");
                                    Controller.erreurLexical = true;
                                }

                            }
                            r = true;
                        }

                      if (line.get(line.size() - 1).matches(";;")) {
                            textArea.setText(textArea.getText() + line.get(line.size() - 1) + "       mot reserve\n");
                            line.set(line.size() - 1, "EL");
                            if (line.get(line.size() - 2).matches("\"")) {
                                textArea.setText(textArea.getText() + line.get(line.size() - 2) + "       reserved character\n");
                                line.set(line.size() - 1, "QUOTE");
                            }
                        } else {
                            if (line.get(line.size() - 1).matches("\";;")) {
                                String t[] = line.get(line.size() - 1).split(";;");
                                line.set(line.size() - 1, "\"");
                                line.add(";;");
                                textArea.setText(textArea.getText() + line.get(line.size() - 2) + "       reserved character\n");
                                textArea.setText(textArea.getText() + line.get(line.size() - 1) + "       reserved word\n");
                                line.set(line.size() - 2, "QUOTE");
                                line.set(line.size() - 1, "EL");

                            }
                        }

                    }
                }
            }
        }
        return r;
    }

    //----------------------------------------------Word----------------------------------------------------------------
    //other
    public static void lexicalAnalyse(ArrayList<String> lines, ArrayList<ArrayList<String>> word, ArrayList<ArrayList<String>> wordT, TextArea textArea) {
        String id = "[a-z|A-Z]+(_?[[a-z|A-Z]|[0-9]]+)*";
        textArea.setText("");
        //line is lines
        //it is word
        for (int j = 0; j < lines.size(); j++) {
            ArrayList<String> t = new ArrayList<String>();
            String[] t1 = lines.get(j).split("\\s+");//**********************************************************
            //put result of t1 in array list
            for (int k1 = 0; k1 < t1.length; k1++) {
                if (!(t1[k1].matches("")))
                    t.add(t1[k1]);
            }
            word.add(j, t);
            //wordT.add(j, t);


        }

        for (int i = 0; i < word.size(); i++) {
            wordT.add((ArrayList<String>) (word.get(i)).clone());
        }

        // wordT.get(0).set(0,"GREAT");
        System.out.println(word);
        System.out.println(wordT);

        System.out.println("=========================================================================================");
        textArea.clear();
        for (int a = 0; a < word.size(); a++) {
            textArea.setText(textArea.getText() + a + "----------------------------------------------------------------\n");
            if (!(isComment(word.get(a), textArea))) {
                if (!(isMessage(word.get(a),wordT.get(a), textArea))) {


                    //if is not a comant

                    int b = 0;
                    while (b < word.get(a).size()) {

                        switch (word.get(a).get(b)) {
                            case "Start_Program":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved word for start of a program\n \n");
                                word.get(a).set(b, "SP");
                                //wordT.get(a).set(b)
                                de = 1;
                                b++;
                                break;

                            case "End_Program":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved word for end of a program\n");
                                word.get(a).set(b, "EP");
                                de +=2;
                                b++;
                                break;

                            case "Int_Number":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved word for declaration of an integer\n");
                                word.get(a).set(b, "INT");
                                b++;
                                break;

                            case "Real_Number":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   word reserved for declaration of a real\n");
                                word.get(a).set(b, "REAL");
                                b++;
                                break;
                            case "String":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   word reserved for declaration of a string \n");
                                word.get(a).set(b, "STRING");
                                b++;
                                break;

                            case "If":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "  reserved word for condition\n");
                                word.get(a).set(b, "IF");
                                ce =1;
                                b++;
                                break;
                            case "--":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved word for condition\n");
                                word.get(a).set(b, "HYPHEN");
                                b++;
                                break;
                            case "Else":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved word for condition\n");
                                word.get(a).set(b, "ELSE");
                                ce +=2;
                                b++;
                                break;
                            case "Start":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved word for start of block\n");
                                word.get(a).set(b, "S");
                                sf = 1;
                                b++;
                                break;
                            case "Finish":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved word for end of block\n");
                                word.get(a).set(b, "E");
                                sf +=2;
                                b++;
                                break;

                            case "Affect":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved word for assignment\n");
                                word.get(a).set(b, "AFFECT");
                                b++;
                                break;
                            case "to":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "  reserved word for assignment\n");
                                word.get(a).set(b, "TO");
                                b++;
                                break;
                            case "ShowMes":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "  reserved word for display a message\n");
                                word.get(a).set(b, "SHOWMES");
                                b++;
                                break;

                            case "ShowVal":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved word for display a variable\n");
                                word.get(a).set(b, "SHOWVAL");
                                b++;
                                break;
                            case "Give":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved word \n");
                                word.get(a).set(b, "GIVE");
                                b++;
                                break;

                            case ";;":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved word\n");
                                word.get(a).set(b, "EL");
                                b++;
                                break;

                            case ":":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved character\n");
                                word.get(a).set(b, "COLON");
                                b++;
                                break;
                            case ",":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved character\n");
                                word.get(a).set(b, "COMMA");
                                b++;
                                break;

                            case "==":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   Equality symbol\n");
                                word.get(a).set(b, "EQ");
                                b++;
                                break;

                            case "!=":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   Difference symbol\n");
                                word.get(a).set(b, "NE");
                                b++;
                                break;

                            case "<=":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   less than or equal symbol\n");
                                word.get(a).set(b, "LE");
                                b++;
                                break;
                            case "<":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   less symbol\n");
                                word.get(a).set(b, "L");
                                b++;
                                break;
                            case ">=":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   Greater than or equal symbol\n");
                                word.get(a).set(b, "SE");
                                b++;
                                break;
                            case ">":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   greater symbol\n");
                                word.get(a).set(b, "S");
                                b++;
                                break;
                            case "\"":
                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   reserved symbol \n");
                                word.get(a).set(b, "QUOTE");
                                b++;
                                break;


                            default:

                                if (word.get(a).get(b).matches(id)) {
                                    textArea.setText(textArea.getText() + word.get(a).get(b) + "       identifier\n");


                                    word.get(a).set(b, "IDENT");
                                    b++;


                                } else {
                                    if (word.get(a).get(b).matches(id + "(," + id + ")+")) {  //id,id,id...

                                        String l1[] = word.get(a).get(b).split(",");
                                        ArrayList<String> subList = new ArrayList<String>();
                                        ArrayList<String> subList1 = new ArrayList<String>();
                                        textArea.setText(textArea.getText() + l1[0] + "       identifier\n");
                                        subList.add("IDENT");
                                        subList1.add(l1[0]);
                                        for (int i = 1; i < l1.length; i++) {
                                            textArea.setText(textArea.getText() + ",       reserved character\n");
                                            textArea.setText(textArea.getText() + l1[i] + "       identifier\n");
                                            subList.add("COMMA");
                                            subList.add("IDENT");

                                            subList1.add(",");
                                            subList1.add(l1[i]);

                                        }
                                        word.get(a).remove(b);
                                        word.get(a).addAll(b, subList);
                                        wordT.get(a).remove(b);
                                        wordT.get(a).addAll(b, subList1);
                                        b = l1.length * 4 - 1;

                                    } else {
                                        if (word.get(a).get(b).matches("(," + id + ")+")) { //,id,id,id
                                            String l1[] = word.get(a).get(b).split(",");
                                            ArrayList<String> subList = new ArrayList<String>();
                                            ArrayList<String> subList1 = new ArrayList<String>();
                                            for (int i = 0; i < l1.length; i++) {
                                                textArea.setText(textArea.getText() + ",       reserved character\n");
                                                textArea.setText(textArea.getText() + l1[i] + "       identifier\n");
                                                subList.add("COMMA");
                                                subList.add("IDENT");
                                                subList1.add(",");
                                                subList1.add(l1[i]);
                                            }
                                            word.get(a).remove(b);
                                            word.get(a).addAll(b, subList);
                                            wordT.get(a).remove(b);
                                            wordT.get(a).addAll(b, subList1);
                                            b = l1.length * 4;
                                        } else {
                                            if (word.get(a).get(b).matches("(" + id + ",)+")) { //id,id,id.......

                                                String l1[] = word.get(a).get(b).split(",");
                                                ArrayList<String> subList = new ArrayList<String>();
                                                ArrayList<String> subList1 = new ArrayList<String>();
                                                for (int i = 0; i < l1.length; i++) {
                                                    textArea.setText(textArea.getText() + l1[i] + "       identifier\n");
                                                    textArea.setText(textArea.getText() + ",       reserved character\n");
                                                    subList.add("IDENT");
                                                    subList.add("COMMA");
                                                    subList1.add(l1[i]);
                                                    subList1.add(",");
                                                }
                                                word.get(a).remove(b);
                                                word.get(a).addAll(b, subList);
                                                wordT.get(a).remove(b);
                                                wordT.get(a).addAll(b, subList1);
                                                b = l1.length * 4;
                                            } else {
                                                if (word.get(a).get(b).matches("[0-9]+")) {
                                                    textArea.setText(textArea.getText() + word.get(a).get(b) + "   Integer value\n");
                                                    word.get(a).set(b, "VINT");
                                                    b++;
                                                } else {
                                                    if (word.get(a).get(b).matches("[0-9]+\\.[0-9]+")) {
                                                        textArea.setText(textArea.getText() + word.get(a).get(b) + "   real value\n");
                                                        word.get(a).set(b, "VREAL");
                                                        b++;
                                                    } else {
                                                        if (word.get(a).get(b).matches("[a-z|A-Z]*|[0-9]*|'*]+")) {
                                                            textArea.setText(textArea.getText() + word.get(a).get(b) + "   String\n");
                                                            word.get(a).set(b, "VSTRING");
                                                            b++;
                                                        } else {
                                                            if (word.get(a).get(b).matches(id + "<" + id)) {
                                                                ArrayList<String> subList = new ArrayList<String>();
                                                                ArrayList<String> subList1 = new ArrayList<String>();
                                                                String t[] = word.get(a).get(b).split("<");
                                                                textArea.setText(textArea.getText() + t[0] + "       identifier\n");
                                                                textArea.setText(textArea.getText() + "<      less symbol\n");
                                                                textArea.setText(textArea.getText() + t[1] + "       identifier\n");
                                                                subList.add("IDENT");
                                                                subList.add("L");
                                                                subList.add("IDENT");
                                                                subList1.add(t[0]);
                                                                subList1.add("<");
                                                                subList1.add(t[1]);
                                                                word.get(a).remove(b);
                                                                word.get(a).addAll(b, subList);
                                                                wordT.get(a).remove(b);
                                                                wordT.get(a).addAll(b, subList1);
                                                                b = b + t.length * 2 - 1;
                                                            } else {
                                                                if (word.get(a).get(b).matches(id + "<=" + id)) {
                                                                    ArrayList<String> subList = new ArrayList<String>();
                                                                    ArrayList<String> subList1 = new ArrayList<String>();
                                                                    String t[] = word.get(a).get(b).split("<=");
                                                                    textArea.setText(textArea.getText() + t[0] + "       identifier\n");
                                                                    textArea.setText(textArea.getText() + "<=        less than or equal symbol\n");
                                                                    textArea.setText(textArea.getText() + t[1] + "       identifier\n");
                                                                    subList.add("IDENT");
                                                                    subList.add("LE");
                                                                    subList.add("IDENT");
                                                                    subList1.add(t[0]);
                                                                    subList1.add("<=");
                                                                    subList1.add(t[1]);
                                                                    word.get(a).remove(b);
                                                                    word.get(a).addAll(b, subList);
                                                                    wordT.get(a).remove(b);
                                                                    wordT.get(a).addAll(b, subList1);

                                                                    b = b + t.length * 2 - 1;
                                                                } else {

                                                                    if (word.get(a).get(b).matches(id + ">" + id)) {
                                                                        ArrayList<String> subList = new ArrayList<String>();
                                                                        ArrayList<String> subList1 = new ArrayList<String>();

                                                                        String t[] = word.get(a).get(b).split(">");
                                                                        textArea.setText(textArea.getText() + t[0] + "       identifier\n");
                                                                        textArea.setText(textArea.getText() + ">       Greater symbol \n");
                                                                        textArea.setText(textArea.getText() + t[1] + "       identifier\n");
                                                                        subList.add("IDENT");
                                                                        subList.add("S");
                                                                        subList.add("IDENT");
                                                                        subList1.add(t[0]);
                                                                        subList1.add(">");
                                                                        subList1.add(t[1]);
                                                                        word.get(a).remove(b);
                                                                        word.get(a).addAll(b, subList);
                                                                        wordT.get(a).remove(b);
                                                                        wordT.get(a).addAll(b, subList1);
                                                                        b = b + t.length * 2 - 1;
                                                                    } else {

                                                                        if (word.get(a).get(b).matches(id + ">=" + id)) {
                                                                            ArrayList<String> subList = new ArrayList<String>();
                                                                            ArrayList<String> subList1 = new ArrayList<String>();
                                                                            String t[] = word.get(a).get(b).split(">=");
                                                                            textArea.setText(textArea.getText() + t[0] + "       identifier\n");
                                                                            textArea.setText(textArea.getText() + ">=      Greater than or equal symbol\n");
                                                                            textArea.setText(textArea.getText() + t[1] + "       identifier\n");
                                                                            subList.add("IDENT");
                                                                            subList.add("SE");
                                                                            subList.add("IDENT");
                                                                            subList1.add(t[0]);
                                                                            subList1.add(">=");
                                                                            subList1.add(t[1]);
                                                                            word.get(a).remove(b);
                                                                            word.get(a).addAll(b, subList);
                                                                            wordT.get(a).remove(b);
                                                                            wordT.get(a).addAll(b, subList1);
                                                                            b = b + t.length * 2 - 1;
                                                                        } else {

                                                                            if (word.get(a).get(b).matches(id + "==" + id)) {
                                                                                ArrayList<String> subList = new ArrayList<String>();
                                                                                ArrayList<String> subList1 = new ArrayList<String>();
                                                                                String t[] = word.get(a).get(b).split("==");
                                                                                ;
                                                                                textArea.setText(textArea.getText() + t[0] + "       identifier\n");
                                                                                textArea.setText(textArea.getText() + "==       Equality symbol\n");
                                                                                textArea.setText(textArea.getText() + t[1] + "       identifier\n");
                                                                                subList.add("IDENT");
                                                                                subList.add("EQ");
                                                                                subList.add("IDENT");
                                                                                subList1.add(t[0]);
                                                                                subList1.add("==");
                                                                                subList1.add(t[1]);
                                                                                word.get(a).remove(b);
                                                                                word.get(a).addAll(b, subList);
                                                                                wordT.get(a).remove(b);
                                                                                wordT.get(a).addAll(b, subList1);
                                                                                b = b + t.length * 2 - 1;
                                                                            } else {

                                                                                if (word.get(a).get(b).matches(id + "=!" + id)) {
                                                                                    ArrayList<String> subList = new ArrayList<String>();
                                                                                    ArrayList<String> subList1 = new ArrayList<String>();
                                                                                    String t[] = word.get(a).get(b).split("=!");
                                                                                    ;
                                                                                    textArea.setText(textArea.getText() + t[0] + "       identifier\n");
                                                                                    textArea.setText(textArea.getText() + "=!       Difference symbol\n");
                                                                                    textArea.setText(textArea.getText() + t[1] + "       identifier\n");
                                                                                    subList.add("IDENT");
                                                                                    subList.add("NE");
                                                                                    subList.add("IDENT");
                                                                                    subList1.add(t[0]);
                                                                                    subList1.add("=!");
                                                                                    subList1.add(t[1]);
                                                                                    word.get(a).remove(b);
                                                                                    word.get(a).addAll(b, subList);
                                                                                    wordT.get(a).remove(b);
                                                                                    wordT.get(a).addAll(b, subList1);
                                                                                    b = b + t.length * 2 - 1;
                                                                                }

                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                textArea.setText(textArea.getText() + word.get(a).get(b) + "   " + "Lexical error\n");
                                                                Controller.erreurLexical = true;
                                                                b++;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }


                        }
                    }
                }
            }
        }
        System.out.println("*******************************************************************************************************");
        System.out.println(word);
        System.out.println("*******************************************************************************************************");
        System.out.println(wordT);
    }
}
