package com.sahiti.phototocalendar;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;


public class App {
    public static void main(String[] args) {
        Scanner myScanner = new Scanner(System.in);
        String path = "";
        String text = "";
        boolean cont = false;
        boolean end = false; 


        

        do {
            try {

                cont = false;

                System.out.println("Enter the path to the file containing event information:");
                path = myScanner.nextLine();
                path = path.trim(); 

                File file = new File(path);
                if (file.exists() && file.isFile()) {
                    cont = false;
                } else {
                    if (askYesNo(myScanner, "Invalid path. Retry?")) {
                        cont = true;
                    } else {
                        System.out.println("Exited program.");
                        cont = false;     
                        end = true;    
                        
                    }
                    

                }

                if (!cont && !end) {
                    try {
                        text = readImage(path);
                        if (text.isEmpty() || text.isBlank() || text.equals("")) {
                            if (askYesNo(myScanner, "Image appears to be blank: Retry?")) {
                                cont = true; 
                            } else {
                                System.out.println("Exited program.");
                                cont = false;     
                                end = true;
                            }
                        }
                    } catch (TesseractException e) {
                        if (askYesNo(myScanner, "Unable to read image text: Retry?")) {
                            cont = true; 
                        } else {
                            System.out.println("Exited program.");
                            cont = false;     
                            end = true;
                        }

                    }
                }

            } catch (NoSuchElementException e) {
                System.out.println("Exited program.");
                end = true;


            }

            



        } while (cont);

        
    }

    static boolean askYesNo(Scanner input, String question) {
        System.out.println(question + " y/n");
        return input.nextLine().trim().toLowerCase().equals("y");
    }

    static String readImage(String pathName) throws TesseractException {
        String text = "";
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/opt/homebrew/opt/tesseract/share/tessdata");
        tesseract.setLanguage("eng");

        File image = new File(pathName);
        text = tesseract.doOCR(image);
        

        return text;

    }
}
