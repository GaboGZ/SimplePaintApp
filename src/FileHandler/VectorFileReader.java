/*
    Author: Gabriel Garate Zea
    Student ID: N10023780
    Unit: CAB302 - Software Development
    Assignment: Project 2
    Due Date: 2-June-2019
    Queensland University of Technology
    Brisbane, QLD, Australia.
 */

package FileHandler;

import GUI.VectorImagePlotter;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * VectorFileReader class used to read and perform drawings
 * on the Canvas class based on a .VEC file.
 */
public class VectorFileReader {

    Charset ch;
    GUI.Canvas canvas;
    Color penColor = VectorImagePlotter.getCurrentPenColor();
    Color fillColor = VectorImagePlotter.getCurrentFillColor();
    boolean fillShape = false;

    /**
     * Reads a file by passing the source directory, name of the source file and charset.
     * If no charset is given, "US-ASCII" is used by default.
     *
     * @param directory
     * @param sourceFile
     * @param charset
     * @return
     */
    public void readFile(String directory, String sourceFile, String charset) {

        System.out.println("Source File: " + sourceFile);

        File file = new File(directory + "/" + sourceFile);
        setCharset(charset);

        canvas = VectorImagePlotter.getDisplayPanel();

        if (canvas.clearDrawings(false)) {

            // Try with resources: The BufferedReader is automatically closed after each read.
            try (BufferedReader reader = Files.newBufferedReader(file.toPath(), ch)) {
                String textLine = null;
                while ((textLine = reader.readLine()) != null) {
                    performActionOnGUI(textLine);
                }
            } catch (IOException e) {
                //File  not found
            }
        }
    }


    /**
     *
     * @param directory
     * @param sourceFile
     * @return
     */
    public String readWholeFile(String directory, String sourceFile) {

        System.out.println("Source File: " + sourceFile);
        File file = new File(directory + "/" + sourceFile);
        String textFile = "";
        setCharset("US-ASCII");

        // Try with resources: The BufferedReader is automatically closed after each read.
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), ch)) {
            String textLine = null;
            while ((textLine = reader.readLine()) != null) {
                textFile += textLine;
            }
        } catch (IOException e) {
            //todo Show a dialog indicating that the file was not found
            textFile = "File not found";
        }

        return textFile;
    }

    /**
     * A convenient methods within the VectorFileReader class. Sets the VectorFileReader charset to the given charset. Otherwise,
     * the charset is set to "US-ASCII" by default.
     *
     * @param charset - The charset to be used.
     */
    private void setCharset(String charset) {
        if (charset == null) {
            this.ch = Charset.forName("US-ASCII");
        } else {
            this.ch = Charset.forName(charset);
        }
    }

    /**
     * A convenient method within the VectorFileReader class. Reads a file given a file name and the directory where it is
     * located.
     *
     * @param directory name of directory where file is located
     * @param fileName  the name of the file
     * @return
     */
    public void readFile(String directory, String fileName) {
        String ch = "US-ASCII";
        readFile(directory, fileName, ch);
    }

    /**
     * Perform different actions on the GUI.Canvas based on the current text line.
     *
     * @param textLine the current line of text.
     */
    private void performActionOnGUI(String textLine) {

        if (textLine.contains("PEN")) {
            String HexColor = textLine.substring("PEN".length() + 1);
            penColor = Color.decode(HexColor);
        }

        if (textLine.contains("FILL #")) {
            String HexColor = textLine.substring("FILL".length() + 1);
            fillColor = Color.decode(HexColor);
            fillShape = true;
        }

        if (textLine.contains("FILL OFF")) {
            fillShape = false;
        }

        if (VectorImagePlotter.isDrawingCommand(textLine)) {
            if (textLine.contains("PLOT")) {
                addShapeToCanvas("PLOT", textLine);
            } else if (textLine.contains("LINE")) {
                addShapeToCanvas("LINE", textLine);
            } else if (textLine.contains("RECTANGLE")) {
                addShapeToCanvas("RECTANGLE", textLine);
            } else if (textLine.contains("ELLIPSE")) {
                addShapeToCanvas("ELLIPSE", textLine);
            } else if (textLine.contains("POLYGON")) {
                addShapeToCanvas("POLYGON", textLine);
            }
            canvas.shapePenColor.add(penColor);
            canvas.shapeFillColor.add(fillColor);
            canvas.shapeFilled.add(fillShape);
        }
        canvas.repaint();
    }

    /**
     * Retrieves the coordinates as characters from a line of text.
     *
     * @param command  the command read from file.
     * @param textLine current file text line
     * @return
     */
    private char[] getChars(String command, String textLine) {
        String coordinates = textLine.substring(command.length() + 1);
        return coordinates.toCharArray();
    }

    /**
     * Find character spaces locations on a given array of type char.
     *
     * @param coordinates
     * @return
     */
    private ArrayList<Integer> findSpaces(char[] coordinates) {
        ArrayList<Integer> spaces = new ArrayList<>();
        for (int i = 0; i < coordinates.length; i++) {
            if (coordinates[i] == ' ') {
                spaces.add(i);
            }
        }
        return spaces;
    }

    /**
     * Transform a given char array to an ArrayList of type Double
     *
     * @param coords
     * @param spaces
     * @return
     */
    private ArrayList<Double> fromCharToArrayList(char[] coords, ArrayList<Integer> spaces) {

        String x_1 = "0";
        String y_1 = "0";
        String x_2 = "0";
        String y_2 = "0";
        double x1, y1, x2, y2;
        ArrayList<Double> c = new ArrayList<>();

        // Iterate over the array of characters
        for (int i = 0; i < coords.length; i++) {

            //PLOT [x1]'0'[y1]
            if (spaces.size() == 1) {
                if (i < spaces.get(0)) {
                    x_1 += coords[i];
                } else if (i > spaces.get(0)) {
                    y_1 += coords[i];
                }
            }//COMMAND [x1]'0'[y1]'1'[x2]'2'[y2]
            else if (spaces.size() == 3) { // LINE, RECTANGLE, CIRCLE
                if (i < spaces.get(0)) {
                    x_1 += coords[i];
                } else if (i > spaces.get(0) && i < spaces.get(1)) {
                    y_1 += coords[i];
                } else if (i > spaces.get(1) && i < spaces.get(2)) {
                    x_2 += coords[i];
                } else if (i > spaces.get(2)) {
                    y_2 += coords[i];
                }
            } else {//POLYGON [x1]'0'[y1]'1'[x2]'2'[y2]'2'[x2]'3'[y2]...
                //[0.01]'0'[0.01]'1'[x2]'2'[y2]'2'[x2]'3'[y2]...
                if (coords[i] != ' ') {
                    x_1 += coords[i];
                } else if (coords[i] == ' ') {
                    c.add(Double.parseDouble(x_1));
                    x_1 = "0";
                }
            }

        }

        if (x_1 != "0") {
            x1 = Double.parseDouble(x_1);
            c.add(x1);
        }

        if (y_1 != "0") {
            y1 = Double.parseDouble(y_1);
            c.add(y1);
        }

        if (x_2 != "0" && y_2 != "0") {
            x2 = Double.parseDouble(x_2);
            y2 = Double.parseDouble(y_2);
            c.add(x2);
            c.add(y2);
        }

        return c;
    }

    /**
     * Adds a shape to the canvas based on the command retrieved from the current line of text.
     *
     * @param command  a Drawing command.
     * @param textLine the current line of text.
     */
    private void addShapeToCanvas(String command, String textLine) {

        double x1, x2, y1, y2;

        //(1) Read coordinates from text line
        char[] coords_ch = getChars(command, textLine);

        //(2) Find spaces between coordinates
        ArrayList<Integer> spaces = findSpaces(coords_ch);

        //(3) Read coordinates from array of char and store them on a ArrayLists.
        ArrayList<Double> coordinates = fromCharToArrayList(coords_ch, spaces);

        //(4) Get coordinates for canvas
        if (command == "PLOT") {
            x1 = coordinates.get(0) * canvas.getWidth();
            y1 = coordinates.get(1) * canvas.getHeight();
            x2 = x1;
            y2 = y1;
            //(5) Do drawing by adding necessary elements to the canvas.
            canvas.shapes.add(canvas.drawShape(command, x1, y1, x2, y2));
        } else if (command == "POLYGON") {

            ArrayList x_points = new ArrayList();
            ArrayList y_points = new ArrayList();

            // Get x and y points from the ArrayList of coordinates
            for (int i = 0; i < coordinates.size(); i++) {
                if (i % 2 == 0) {     // if even index, add to x points.
                    x_points.add(coordinates.get(i));
                } else {              // if odd index, add to x points.
                    y_points.add(coordinates.get(i));
                }
            }
            
            int capacity = coordinates.size() / 2;
            int[] xPoints_arr = new int[capacity];
            int[] yPoints_arr = new int[capacity];

            for (int i = 0; i < x_points.size(); i++) {
                xPoints_arr[i] = (int)((double)x_points.get(i)*canvas.getWidth());
            }
            for (int i = 0; i < y_points.size(); i++) {
                yPoints_arr[i] = (int) ((double)y_points.get(i)*canvas.getHeight());
            }

            canvas.shapes.add(canvas.drawPolygon(xPoints_arr, yPoints_arr, xPoints_arr.length));

        } else { //PLOT, LINE, RECTANGLE
            x1 = coordinates.get(0) * canvas.getWidth();
            y1 = coordinates.get(1) * canvas.getHeight();
            x2 = coordinates.get(2) * canvas.getWidth();
            y2 = coordinates.get(3) * canvas.getHeight();
            //(5) Do drawing by adding necessary elements to the canvas.
            canvas.shapes.add(canvas.drawShape(command, x1, y1, x2, y2));
        }

    }

}
