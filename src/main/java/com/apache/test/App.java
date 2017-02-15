package com.apache.test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

/**
 * Hello world!
 */
public class App {
    public static void main(String argv[]) {

        try {
            String filePath = argv[0];

            System.out.println("file Path "+filePath);
            //Read the input file
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            execute(filePath, doc);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String serchWord(String word, Document dictionary) {
        Boolean wordFound = false;
        String result = "";
        try {

            NodeList nList = dictionary.getElementsByTagName("entry");


            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                Element eElement = (Element) nNode;
                String findWord = eElement.getAttribute("word");


                if (findWord.equals(word)) {
                    wordFound = true;
                    result = String.valueOf(eElement.getAttribute("meaning"));
                }
            }

            if (!wordFound) {
                result = word+": word not in dictionary";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    private static String insertWord(String word, String meaning, Document doc, String filepath){

        Node company = doc.getFirstChild();
        Element newWord = doc.createElement("entry");

        newWord.setAttribute("word",word);
        newWord.setAttribute("meaning",meaning);

        company.appendChild(newWord);


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filepath));
        transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "done";
    }

    public static void execute(String fileName, Document doc){

        String options = "Please select one Option\n" +
                "  Option 1 will be search for the meaning of a word\n" +
                "  Option 2 will be add a word\n" +
                "  Option 3 will be Exit\n";

        String invalidInput = "Please enter a correct option between 1 to 3";

        String input = userInput(options);
        int choice = 0;
        try {
             choice = parseInt(input);
        }catch (Exception e){
            print(invalidInput);
            execute(fileName, doc);
        }
        if(choice <1 || choice >3){
            print(invalidInput);
            execute(fileName, doc);
        }else {
            switch (choice) {
                case 1:
                    String result= serchWord(userInput("Please enter the word:"),doc);
                    print(result+"\n");
                    break;
                case 2:
                    String userInput = userInput("Please enter the new word followed by its meaning:");

                    String[] parts = userInput.split("\\s+",2);
                    System.out.println(Arrays.toString(parts));

                    String result2= insertWord(parts[0],parts[1],doc, fileName);

                    print(result2+"\n");

                    break;
                case 3:
                    System.exit(0);
            }
            String userChoice = userInput("Please enter to continue");
            System.out.println(userChoice.equals(""));

            //when enter key press
            if(userChoice.equals("")){
                execute(fileName, doc);
            }
        }
    }

    public static String userInput(String question){
        Scanner scanner = new Scanner (System.in);

        print(question);

        String selected = scanner.nextLine(); // Get what the user types.
        return selected;
    }


    public static void print(String data){
        System.out.println(data);
    }
}


