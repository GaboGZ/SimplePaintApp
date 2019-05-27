package FileHandler;

import GUI.Ratio;
import GUI.Window;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * FileReader class used to read and perform drawings based on a .VEC file.
 */
public class FileReader {

    String textFile;
    Charset ch;

    /**
     * Reads a file by passing the source directory, name of the source file and charset.
     * If no charset is given, "US-ASCII" is used by default.
     * @param directory
     * @param sourceFile
     * @param charset
     * @return
     */
    public String readFile(String directory, String sourceFile,String charset){

        System.out.println("Source File: "+ sourceFile);

        File file = new File(directory + "/"+ sourceFile);
        setCharset(charset);


        // TRY with resources
        // The BufferedReader is automatically closed after each read.
        try (
                BufferedReader reader = Files.newBufferedReader(file.toPath(), ch)
        ){
            String textLine = null;

            while ((textLine = reader.readLine()) != null) {
                textFile = "" + textLine;
                performActionOnGUI(textLine);

            }//end while loop

        } catch (IOException e) {
            //todo Show a dialog indicating that the file was not found
            textFile = "File not found";
        }

        return textFile;
    }

    /**
     * A convenient methods within the FileReader class. Sets the FileReader charset to the given charset. Otherwise,
     * the charset is set to "US-ASCII" by default.
     * @param charset - The charset to be used.
     */
    private void setCharset(String charset) {
        if(charset == null){
            this.ch = Charset.forName("US-ASCII");
        }else{
            this.ch = Charset.forName(charset);
        }
    }

    /**
     * A convenient method within the FileReader class. Reads a file given file name and the directory where it is
     * located.
     * @param directory name of directory where file is located
     * @param fileName the name of the file
     * @return
     */
    public String readFile(String directory, String fileName){
        String ch = "US-ASCII";
        return readFile(directory, fileName,ch);
    }

    private void performActionOnGUI(String textLine) {

        String coordinates = "";

        GUI.Canvas c = Window.getDisplayPanel();

        //todo:
        // if Canvas has drawings:
        //      prompt user to save current drawings
        // if Canvas empty
        //      clearDrawings();
//        if(!c.shapes.isEmpty()){
//          System.out.println("The canvas contains drawings.");
//
//        }else{
            c.clearDrawings();
//        }


        ArrayList<Integer> spacesAt = new ArrayList<>();
        String x_1 = "";
        String y_1 = "";
        String x_2 = "";
        String y_2 = "";
        String command ="";


        if(textLine.contains("PLOT")){
            command = "PLOT";

            //read coordinates

            //(1) get everything after "comand_" and store data in a char[] array.
            coordinates = textLine.substring(command.length()+1);
            System.out.println("String coordinates: "+ coordinates);
            char[] coords = coordinates.toCharArray();

            //(2)Locate spaces between coordinates
            for (int i = 0; i < coords.length ; i++) {
                if (coords[i] == ' ') {
                    spacesAt.add(i);
                }
            }
            System.out.println("Spaces at: "+ spacesAt); // at 3

            //(3) Use the spacesAt index to read coordinates storing then into the appropriate arrayLists.
            for (int i = 0; i < coords.length ; i++) {
                if( i < spacesAt.get(0) ){
                    x_1 = x_1 + coords[i];
                }
                else{
                    y_1 = y_1 + coords[i];
                }
            }

            double x1 = Double.parseDouble(x_1);//* Ratio.MEDIUM.getRatio();
            double y1 = Double.parseDouble(y_1);//* Ratio.MEDIUM.getRatio();
            System.out.println("x1: " + x1);
            System.out.println("y1: " + y1);

//
//
//            //draw PLOT by adding the necessary values to the arraylists
            c.shapes.add(c.defineShape(command,x1,y1,x1,y1));
            c.shapePenColor.add(Color.BLACK);
            c.shapeFillColor.add(Window.getCurrentFillColor());
            c.shapeFilled.add(false);

            //draw PLOT
            System.out.println("Calling drawPlot()");
        }
        else if(textLine.contains("LINE")){
            command = "LINE";

            //read coordinates
            coordinates = textLine.substring(5);
            System.out.println("String coordinates: "+coordinates);

            char[] coords = coordinates.toCharArray();

            //Locating spaces between coordinates
            for (int i = 0; i < coords.length ; i++) {
                if (coords[i] == ' ') {
                    spacesAt.add(i);
                }
            }

            //Reading coordinates and storing then in arrayLists.
            for (int i = 0; i < coords.length ; i++) {
                if( i < spacesAt.get(0) ){
                    x_1 = x_1 + coords[i];
                }
                else if( i > spacesAt.get(0) && i < spacesAt.get(1) ){
                    y_1 = y_1 + coords[i];
                }
                else if( i > spacesAt.get(1) && i < spacesAt.get(2)){
                    x_2 = x_2 + coords[i];
                }
                else if( i > spacesAt.get(2) ){
                    y_2 = y_2 + coords[i];
                }
            }

            double x1 = Double.parseDouble(x_1);//* Ratio.MEDIUM.getRatio();
            double y1 = Double.parseDouble(y_1);//* Ratio.MEDIUM.getRatio();
            double x2 = Double.parseDouble(x_2);//* Ratio.MEDIUM.getRatio();
            double y2 = Double.parseDouble(y_2);//* Ratio.MEDIUM.getRatio();


            //draw LINE by adding the necessary values to the arraylists
//            c.shapes.add(c.drawLine(x1,y1,x2,y2));
            c.shapes.add(c.defineShape(command,x1,y1,x2,y2));
            c.shapePenColor.add(Color.BLACK);
            c.shapeFillColor.add(Window.getCurrentFillColor());
            c.shapeFilled.add(false);
        }
//                else if(textLine.contains("RECTANGLE")){
//                    //read coordinates
//                    //draw RECTANGLE
//                    System.out.println("Calling drawRectangle()");
//                }
//                else if(textLine.contains("POLYGON")){
//                    //read coordinates
//                    //draw POLYGON
//                    System.out.println("Calling drawPolygon()");
//                }
//                else if((textLine.contains("PEN"))){
//                    // Read color
//                     Window.setPenColor(Color.BLUE);
//                    // Set pen color
//                    System.out.println("Pen Color set to: " + Window.getCurrentPenColor());
//                }
//                else if(textLine.contains("FILL")){
//                    if(textLine.contains("OFF")){
//                        //Deselect fill checkbox
//                    }else{
//                        //read color
//                        //set fill color
//                    }
//                }
        c.repaint();
    }

}
