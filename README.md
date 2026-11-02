# Photo-to-Calendar

 
## Project Goal

Build a Java command-line application that extracts event details from an image and exports an .ics calendar file.

Users can review the extracted information, correct errors, fill in missing details and confirm the event before exporting it.

## MVP Scope 
 - Accept an image file path (through terminal)
 - Check whether the file exists, and allow the user to retry or cancel if input is invalid
 - Extract text from image using Tess4J
 - Handle image-processing errors and allow the user to select another image or cancel
 - Identify the event title, date, time, and location
 - Leave missing details blank for the user to fill in
 - Display extracted details and the computer's default time zone
 - Allow the user to correct the values, change time zone and fill in missing information
 - Validate the required fields and explain any errors
 - Allow the user to return to editing or cancel
 - Ask the user to confirm valid event details
 - Export the confirmed event as an .ics file using iCal4j

(During testing, the exported file will be imported into Google Calendar manually to check that the details show up as expected) 

## Future Plans 
 - Giving the program several images at once, the user reviews each event in one session
 - Several reviewed events combined into one calendar file instead of individual exports for each event
 - Direct integration with Google Calendar
 - Graphical interface instead of command-line program 


## Tech Stack 
- Java: command-line application, event detail reading/review, and export
- Tess4J (text extraction from user’s image using Tesseract OCR)
- iCal4j (generating .ics file from confirmed event details)
- Scanner/IO.readln() (java terminal input tools)
- java.time classes, ZoneId, ZonedDateTime (for default time zone and event date/time handling)

## Workflow and Data
- Extracted event details are stored temporarily while the program runs. Only the confirmed event details will be saved into an .ics file
- User submits local image (enter its path through terminal)
- Program extracts image info and lets the user correct/complete the details
- Users manually import exported .ics file into Google Calendar
- No external API planned for the MVP

## Project Structure: 
- src/: Java source code
- tests/: test code
- docs/: Eisenhower Matrix and system diagram

## Design Documents 
The docs folder contains the project's Eisenhower Matrix and system diagram

## Current Status 
The project is in planning and set-up stage. Implementation has not yet started.




