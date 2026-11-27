package com.sahiti.phototocalendar;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.LocalTime;
import java.time.DateTimeException;
import java.util.ArrayList;





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
        Event info = null;

        ArrayList<Event> events = new ArrayList<>(); 
        boolean more = false;


        do {
            end = false;
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

                                info = new Event(event, date, time, location);
                                zoneID = info.getZone();
                                zone = info.getAbb();    
                                dateValidation(info);
                                timeValidation(info);                        

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

       

            if (!end) {
                System.out.println(info.toString());
                System.out.println("You may now edit. Make sure to fill in empty fields.");
                try {
                
                    do {
                        cont = true;
                        System.out.println("Enter 1, 2, 3, 4, 5 or x to confirm and exit: ");
                        String num = myScanner.nextLine().toLowerCase().trim();
                        if (num.equals("x")) {
                            String s = emptyValidation(info);
                            if (s.equals("")) {
                                s = dateValidation(info);
                            }
                            if (s.equals("")) {
                                s = timeValidation(info);
                            }

                            if (!s.equals("")) {
                                if (askYesNo(myScanner, s + " Back to editing?")) {
                                    cont = true;
                                
                                } else {
                                    cont = false;
                                    end = true;
                                    System.out.println("Exited program.");
                                }
                            } else {
                                cont = false;
                            }
                        } else if (!num.equals("1") && !num.equals("2") && !num.equals("3") && !num.equals("4") && !num.equals("5")) {
                            System.out.println("Must enter a number from 1-5.");
                        } else if (num.equals("1")) {
                            System.out.print("Event: ");
                            event = myScanner.nextLine();
                            if (event.isBlank()) {
                                if (askYesNo(myScanner, "Event can't be empty. Back to editing?")) {
                                    cont = true;
                                
                                } else {
                                    cont = false;
                                    end = true;
                                    System.out.println("Exited program.");
                                }
                            } else {
                                cont = true;
                                info.setEvent(event);
                            }
                        } else if (num.equals("2")) {
                            System.out.print("Location: ");
                            location = myScanner.nextLine();
                            if (location.isBlank()) {
                                if (askYesNo(myScanner, "Location can't be empty. Back to editing?")) {
                                    cont = true;
                                
                                } else {
                                    cont = false;
                                    end = true;
                                    System.out.println("Exited program.");
                                }
                            } else {
                                cont = true;
                                info.setLocation(location);
                            }
                        } else if (num.equals("3")) {
                            System.out.print("Date: ");
                            date = myScanner.nextLine();
                            String oldDate = info.getDate();
                            info.setDate(date);
                            String s = dateValidation(info);
                            if (!s.equals("")) {
                                info.setDate(oldDate);
                                if (askYesNo(myScanner, s + " Back to editing?")) {
                                    cont = true;
                                
                                } else {
                                    cont = false;
                                    end = true;
                                    System.out.println("Exited program.");
                                }
                            } else {
                                cont = true;
                            }
                        } else if (num.equals("4")) {
                            System.out.print("Time: ");
                            time = myScanner.nextLine();
                            String oldTime = info.getTime();
                            info.setTime(time);
                            String s = timeValidation(info);
                            if (!s.equals("")) {
                                info.setTime(oldTime);
                                if (askYesNo(myScanner, s + " Back to editing?")) {
                                    cont = true;
                                
                                } else {
                                    cont = false;
                                    end = true;
                                    System.out.println("Exited program.");
                                }
                            } else {
                                cont = true;
                            }
                        } else if (num.equals("5")) {
                            System.out.print("Time Zone: ");
                            zone = myScanner.nextLine().trim().toUpperCase();
                            if (zone.isBlank()) {
                                if (askYesNo(myScanner, "Timezone can't be empty. Back to editing?")) {
                                    cont = true;
                                
                                } else {
                                    cont = false;
                                    end = true;
                                    System.out.println("Exited program.");
                                }
                            } else {
                                cont = true;
                                info.setAbb(zone);
                            }                        
                        }
                        if (cont) {
                            System.out.println(info.toString());
                        }
                    
                    } while (cont);
                } catch (NoSuchElementException e) {
                    System.out.println("Exited program.");
                    cont = false;
                    end = true;

                }
            
            }

            if (!end) {
                events.add(info);
                System.out.println("Added \"" + info.getEvent() + "\" (" + events.size() + " event(s) ready).");
            } 

            more = askYesNo(myScanner, "Would you like to add another image?");
        } while (more);

        if (!events.isEmpty()) {
            try {
                CalendarExport exporter = new CalendarExport(events);

                String fileName = events.get(0).getEvent().trim().replaceAll("[^a-zA-Z0-9\\-_]+", "_");
                if (events.size() > 1) {
                    fileName += "_and_" + (events.size() - 1) + "_more";
                }

                String downloadsFolder = System.getProperty("user.home") + "/Downloads/" + fileName + ".ics";
                exporter.exportCalendar(downloadsFolder);
                
                System.out.println("Event(s) successfully exported to: " + downloadsFolder);
               
            } catch (Exception e) {
                System.out.println("Failed to export calendar: " + e.getMessage());
            }
        } else {
            System.out.println("Exited program. No events exported.");
        }


        

        
    

        
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

    static String emptyValidation(Event info) {
        if (info.getEvent().isBlank()) {
            return "Event title can't be empty.";
        } else if (info.getDate().isBlank()) {
            return "Date can't be empty";
        }  else if (info.getTime().isBlank()) {
            return "Time can't be empty.";
        } else if (info.getLocation().isBlank()) {
            return "Location can't be empty.";
        } else if (info.getAbb().isBlank()) {
            return "Timezone can't be empty.";
        }

        return "";

    }

    static String dateValidation(Event info) {
        String fixedDate = info.getDate().replaceAll("(?i)(\\d)(th|st|nd|rd)\\b", "$1");
        fixedDate = fixedDate.replaceAll("[,\\.]", "");
        fixedDate = fixedDate.replaceAll("\\s+", " ").trim();

        
        Pattern date1 = Pattern.compile(".*\\d{4}$");
        Pattern date2 = Pattern.compile(".*\\d\\d$");
        Pattern date3 = Pattern.compile("^([a-zA-Z]+\\.?\\s*\\d{1,2}|\\d{1,2}/\\d{1,2}|\\d{1,2}\\s*[a-zA-Z]+)$");

        if (!date1.matcher(fixedDate).matches()) {
            if (date3.matcher(fixedDate).matches()) {
                if (fixedDate.contains("/")) {
                    fixedDate += "/" + LocalDate.now().getYear();
                } else {
                    fixedDate += " " + LocalDate.now().getYear();
                }
            } else if (date2.matcher(fixedDate).matches()) {
                fixedDate = fixedDate.substring(0, fixedDate.length() - 2) + "20" + fixedDate.substring(fixedDate.length() - 2);
            } else {
                return "Date format is invalid";
            }
            
        } 

        LocalDate date = null;
        String[] formats = {"M/d/yyyy", "M-d-yyyy", "MMMM d yyyy", "MMM d yyyy", "d MMMM yyyy", "d MMM yyyy"};
        for (String f : formats) {
            try {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern(f).toFormatter();
                date = LocalDate.parse(fixedDate, formatter);
                break;
            } catch (DateTimeParseException e) {

            }
        } 

        if (date == null) {
            return "Date format is invalid";
        } else {
            info.setParsedDate(date);
            info.setDate(date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));

        }

        return "";

    }

    static String timeValidation(Event info){
        String fixedTime = info.getTime().toUpperCase().replaceAll("\\.", "");
        fixedTime = fixedTime.replaceAll("\\s+", " ").trim();

        String[] parts = fixedTime.split("\\s*(-|–|—|TO)\\s*");
        
        if (parts.length != 1 && parts.length != 2) {
            return "Time format is invalid";
        } 

        //adding am/pm to first element if missing
        if (parts.length == 2) {
            if (!parts[0].endsWith("AM") && !parts[0].endsWith("PM")) {
                if (!parts[1].endsWith("AM") && !parts[1].endsWith("PM")) {
                    return "Time must specify indicators: AM/PM";
                } else if (parts[1].endsWith("AM")) {
                    parts[0] = parts[0] + "AM";
                } else if (parts[1].endsWith("PM")) {
                    parts[0] = parts[0] + "PM";
                }
            }
        } else {
            if (!parts[0].endsWith("AM") && !parts[0].endsWith("PM")) {
                return "Time must specify indicators: AM/PM";
            }
        }

        //adding space before AM/PM if missing
        if (parts[0].endsWith("AM") && !parts[0].endsWith(" AM")) {
            int len = parts[0].length();
            parts[0] = parts[0].substring(0, len - 2) + " AM";
            
        } else if (parts[0].endsWith("PM") && !parts[0].endsWith(" PM")) {
            int len = parts[0].length();
            parts[0] = parts[0].substring(0, len - 2) + " PM";
            
        }

        if (parts.length == 2) {
            
            if (parts[1].endsWith("AM") && !parts[1].endsWith(" AM")) {
                int len = parts[1].length();
                parts[1] = parts[1].substring(0, len - 2) + " AM";
            
            } else if (parts[1].endsWith("PM") && !parts[1].endsWith(" PM")) {
                int len = parts[1].length();
                parts[1] = parts[1].substring(0, len - 2) + " PM";
            
            }

            if (!parts[1].endsWith(" AM") && !parts[1].endsWith(" PM")) {
                return "Time must specify indicators: AM/PM";
            }
        }
        

        

        //parsing into date time format 
        //adding :00 for single times like 10pm
        try {
            
            Pattern digits = Pattern.compile(":\\d{2}"); 
            Matcher m = digits.matcher(parts[0]);
        
            if (!m.find()) {
                int len = parts[0].length();
                parts[0] = parts[0].substring(0, len - 3) + ":00" + parts[0].substring(len - 3);
            }

            if (parts.length == 2) {
                Matcher m2 = digits.matcher(parts[1]);
                if (!m2.find()) {
                    int len = parts[1].length();
                    parts[1] = parts[1].substring(0, len - 3) + ":00" + parts[1].substring(len - 3);
                }
            }

            DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
            LocalTime start = LocalTime.parse(parts[0], format);
            LocalTime end;
            if (parts.length == 1) {
                end = start.plusHours(1);
            } else {
                end = LocalTime.parse(parts[1], format);
                if (end.equals(start)) {
                    return "End time must be different from start time.";
                }
            }

           
            info.setStartTime(start);
            info.setEndTime(end);
            info.setTime(start.format(format) + " - " + end.format(format));
        } catch (DateTimeException e) {
            return "Time format is invalid";
        } catch (IndexOutOfBoundsException e) {
            return "Time format is invalid";
        }

        return "";
    }
}


