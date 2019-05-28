package FileHandler;

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
    GUI.Canvas canvas;

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

        canvas = Window.getDisplayPanel();
        if(canvas.clearDrawings(false)){

            // Try with resources: The BufferedReader is automatically closed after each read.
            try (BufferedReader reader = Files.newBufferedReader(file.toPath(), ch)){
                String textLine = null;
                while ((textLine = reader.readLine()) != null) {
                    textFile = "" + textLine;
                    performActionOnGUI(textLine);
                }
            } catch (IOException e) {
                //todo Show a dialog indicating that the file was not found
                textFile = "File not found";
            }
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

        if(textLine.contains("PLOT")){
            doDrawing("PLOT",textLine);
        }else if(textLine.contains("LINE")){
            doDrawing("LINE",textLine);
        }
        else if(textLine.contains("RECTANGLE")){
                doDrawing("RECTANGLE",textLine);
        }
        else if(textLine.contains("ELLIPSE")){
            doDrawing("ELLIPSE",textLine);
        }
        canvas.repaint();
    }

    /**
     * Retrieves the coordinates as characters from a line of text.
     * @param command the command read from file.
     * @param textLine current file text line
     * @return
     */
    private char[] getChars(String command, String textLine) {
        String coordinates = textLine.substring(command.length() + 1);
        return coordinates.toCharArray();
    }

    /**
     * Find character spaces locations on a given array of type char.
     * @param coordinates
     * @return
     */
    private ArrayList<Integer> findSpaces(char[] coordinates) {
        ArrayList<Integer> spaces = new ArrayList<>();
        for (int i = 0; i < coordinates.length ; i++) {
            if (coordinates[i] == ' ') {
                spaces.add(i);
            }
        }
        return spaces;
    }

    /**
     * Transform a given char array to an ArrayList of type Double
     * @param coords
     * @param spaces
     * @return
     */
    private ArrayList<Double> fromCharToArrayList(char[] coords, ArrayList<Integer> spaces){

        String x_1 = "0";
        String y_1 = "0";
        String x_2 = "0";
        String y_2 = "0";
        double x1,y1,x2,y2;
        ArrayList<Double> c = new ArrayList<>();
        System.out.println("Size of chords: " + coords.length);
        System.out.println("Size of spacesAt: " + spaces.size());


        for (int i = 0; i < coords.length ; i++) {

            //PLOT
            if( spaces.size() == 1) {
                if( i < spaces.get(0) ){
                    x_1 += coords[i];
                }
                else if( i > spaces.get(0)){
                    y_1 += coords[i];
                }
//                double x1 = Double.parseDouble(x_1);
//                double y1 = Double.parseDouble(y_1);
//                canvas.add(x1);
//                canvas.add(y1);
            }

            // LINE, RECTANGLE, CIRCLE
            if(spaces.size() == 3){
                if( i < spaces.get(0) ){
                    x_1 += coords[i];
                }
                else if( i > spaces.get(0) && i < spaces.get(1) ){
                    y_1 += coords[i];
                }
                else if( i > spaces.get(1) && i < spaces.get(2)){
                    x_2 += coords[i];
                }
                else if( i > spaces.get(2) ){
                    y_2 += coords[i];
                }
            }
        }

        x1 = Double.parseDouble(x_1);
        y1 = Double.parseDouble(y_1);
        c.add(x1);
        c.add(y1);

        if( x_2 != "0" && y_2 != "0"){
            x2 = Double.parseDouble(x_2);
            y2 = Double.parseDouble(y_2);
            c.add(x2);
            c.add(y2);
        }

        return c;
    }

    public void doDrawing(String command, String textLine){

        double x1,x2,y1,y2;

        //(1) Read coordinates from text line
        char[] coords_ch = getChars(command, textLine);

        //(2) Find spaces between coordinates
        ArrayList<Integer> spaces = findSpaces(coords_ch);

        //(3) Read coordinates from array of char and store them on a ArrayLists.
        ArrayList<Double> coord_d = fromCharToArrayList(coords_ch, spaces);

        //(4) Get coordinates for canvas
        if(command == "PLOT"){
            x1 = coord_d.get(0)*canvas.getWidth();
            y1 = coord_d.get(1)*canvas.getHeight();
            x2 = x1;
            y2 = y1;
        }
//        else if(command =="POLYGON"){

//        }
        else{ //PLOT, LINE, RECTANGLE
            x1 = coord_d.get(0)*canvas.getWidth();
            y1 = coord_d.get(1)*canvas.getHeight();
            x2 = coord_d.get(2)*canvas.getWidth();
            y2 = coord_d.get(3)*canvas.getHeight();
        }

        //(5) Do drawing by adding necessary elements to the canvas.
        canvas.shapes.add(canvas.defineShape(command,x1,y1,x2,y2));
        canvas.shapePenColor.add(Color.BLACK);
        canvas.shapeFillColor.add(Window.getCurrentFillColor());
        canvas.shapeFilled.add(false);
    }

}
