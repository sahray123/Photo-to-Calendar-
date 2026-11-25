package com.sahiti.phototocalendar;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;




public class App {
    public static void main(String[] args) {
        Scanner myScanner = new Scanner(System.in);
        String path = "";
        String text = "";
        boolean cont = false;
        boolean end = false; 

        String time = "";
        String date = "";
        String location = "";
        String event = "";
        String zone = "";
        String zoneID = "";


        

        do {
            try {

                cont = false;
                time = "";
                date = "";
                location = "";
                event = "";
                zone = "";
                zoneID = "";

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
                        } else {
                            System.out.print("Event name: ");
                            if (myScanner.hasNextLine()) {
                                event = myScanner.nextLine().trim();
                            }
                        }

                        if (!cont && !end) {
                            Pattern range = Pattern.compile("(?i)\\b\\d{1,2}\\s*(:\\d{2}\\s*)?(a\\.?m\\.?|p\\.?m\\.?)?\\s*(-|–|—|to)\\s*\\d{1,2}\\s*(:\\d{2}\\s*)?(a\\.?m\\.?|p\\.?m\\.?)");
                            Pattern single = Pattern.compile("(?i)\\b\\d{1,2}\\s*(:\\d{2}\\s*)?(a\\.?m\\.?|p\\.?m\\.?)");
                            Matcher m = range.matcher(text);
                            Matcher m2 = single.matcher(text);
                            if (m.find()) {
                                time = m.group();
                                text = text.substring(0, m.start()) + " " + text.substring(m.end());
                            } else if (m2.find()) {
                                time = m2.group();
                                text = text.substring(0, m2.start()) + " " + text.substring(m2.end());
                            } 

                            Pattern d1 = Pattern.compile("\\b\\d{1,2}\\s*/\\s*\\d{1,2}(\\s*/\\s*\\d{1,4})?\\b");
                            Pattern d2 = Pattern.compile("\\b\\d{1,2}\\s*-\\s*\\d{1,2}\\s*-\\s*\\d{1,4}\\b");
                            Pattern d3 = Pattern.compile("(?i)\\b\\d{1,2}(st|nd|rd|th)?\\s*(January|February|March|April|May|June|July|August|September|October|November|December|Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Sep|Oct|Nov|Dec)\\.?(\\s*\\d{1,4})?\\b");
                            Pattern d4 = Pattern.compile("(?i)\\b(January|February|March|April|May|June|July|August|September|October|November|December|Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Sep|Oct|Nov|Dec)\\.?\\s*\\d{1,2}(st|nd|rd|th)?(\\s*,?\\s*\\d{1,4})?\\b");

                            Matcher md1 = d1.matcher(text);
                            Matcher md2 = d2.matcher(text);
                            Matcher md3 = d3.matcher(text);
                            Matcher md4 = d4.matcher(text);

                            if (md1.find()) {
                                date = md1.group();
                                text = text.substring(0, md1.start()) + " " + text.substring(md1.end());
                                date = date.replaceAll("\\s+", " ");
                            } else if (md2.find()) {
                                date = md2.group();
                                text = text.substring(0, md2.start()) + " " + text.substring(md2.end());
                                date = date.replaceAll("\\s+", " ");
                            } else if (md3.find()) {
                                date = md3.group();
                                text = text.substring(0, md3.start()) + " " + text.substring(md3.end());
                                date = date.replaceAll("\\s+", " ");
                            } else if (md4.find()){
                                date = md4.group();
                                text = text.substring(0, md4.start()) + " " + text.substring(md4.end());
                                date = date.replaceAll("\\s+", " ");
                            } 

                            Pattern l1 = Pattern.compile("(?i)\\b(location|where|at|venue|place|loc)\\s*:\\s*(.+)");
                            Pattern l2 = Pattern.compile("@\\s*([^_\\n]+)");
                            Pattern l3 = Pattern.compile("(?im)^.*\\b(room|hall|rm|lab|building|bldg|center|auditorium|lounge|floor|union|library|st|street|ave|avenue|rd|road|blvd|commons|lobby|atrium|theater|theatre|gallery|cafeteria|gym|studio|suite|ste|plaza|quad|courtyard|stadium|dr|drive|way|ln|lane|ct|court)\\b.*$");

                            Matcher ml1 = l1.matcher(text);
                            Matcher ml2 = l2.matcher(text);
                            Matcher ml3 = l3.matcher(text);

                            if (ml1.find()) {
                                location = ml1.group(2).trim();
                            } else if (ml2.find()) {
                                location = ml2.group(1).trim();
                            } else if (ml3.find() && ml3.group().trim().length() < 60) {
                                location = ml3.group().trim();
                            } 

                            Event info = new Event(event, date, time, location);
                            zoneID = info.getZone();
                            zone = info.getAbb();                            

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
        File image = new File(pathName);
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/opt/homebrew/opt/tesseract/share/tessdata");
        tesseract.setLanguage("eng");
        tesseract.setVariable("user_defined_dpi", "300");

        String text = tesseract.doOCR(image);
        return text;

        
        

    }
}
