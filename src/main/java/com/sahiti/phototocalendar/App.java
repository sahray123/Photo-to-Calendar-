package com.sahiti.phototocalendar;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;



public class App {
    public static void main(String[] args) {
        Scanner myScanner = new Scanner(System.in);
        String path = "";
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
}
