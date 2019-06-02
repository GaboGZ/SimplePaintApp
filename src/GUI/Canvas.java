/*
    Author: Gabriel Garate Zea
    Student ID: N10023780
    Unit: CAB302 - Software Development
    Assignment: Project 2
    Due Date: 2-June-2019
    Queensland University of Technology
    Brisbane, QLD, Australia.
 */

package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import FileHandler.*;

import static GUI.VectorImagePlotter.*;

/**
 * This class provides a component to allow the display of drawings.
 */
public class Canvas extends JComponent {

    private static final double canvasW_ini = 641.0;
    private static final double canvasH_ini = 389.0;
    private static double canvasW_current;
    private static double canvasH_current;
    private static double ratioW; //for resizing purposes.
    private static double ratioH; //for resizing purposes.

    // Arraylists that store shapes information:
    //  shapes: stores Shape form either PLOT, LINE, RECTANGLE or POLYGON
    //  shapePenColor: stores Pen Color
    //  shapeFillColor: stores Fill Color
    //  shapeFilled: stores whether the shape is filled or not.
    public static ArrayList<Shape> shapes = new ArrayList<>();
    public static ArrayList<Color> shapePenColor = new ArrayList<>();
    public static ArrayList<Color> shapeFillColor = new ArrayList<>();
    public static ArrayList<Boolean> shapeFilled = new ArrayList<>();

    // Temporary Arrays to handle UNDO and REDO features
    private ArrayList<Shape> temp_shapes = new ArrayList<>();
    private ArrayList<Color> temp_shapePenColor = new ArrayList<>();
    private ArrayList<Color> temp_shapeFillColor = new ArrayList<>();
    private ArrayList<Boolean> temp_shapeFilled = new ArrayList<>();


    // Coordinates helpers
    private static Point drawStart;
    private static Point drawEnd;
    ArrayList<Point> points = new ArrayList<>();

    // Drawing helpers
    private static boolean mousePressed = false;
    private boolean fillUsed = false;


    // Polygon helpers
    private boolean newPolygon = true;
    private boolean polygonClosed = false;
    private int initialNumberOfShapes;
    private int finalNumberOfShapes;


    /**
     * Creates a Canvas for the display panel.
     * This canvas has a ComponentListener to detect whether the canvas is resized.
     * This canvas has a MouseListener to detect and handle all mouse event over the canvas.
     */
    public Canvas() {

        canvasW_current = getWidth();
        canvasH_current = getHeight();

        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        //todo: Resizing images | Issue: Images not rendering well after
        // being resized.
        // Uncomment this section to test the resizing of images if you wish.
//        this.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                super.componentResized(e);
//
//
//                    System.out.println("----------------------------------------------------------");
//
//                    canvasW_current = getWidth();
//                    canvasH_current = getHeight();
//
//                    ratioW = canvasW_current / canvasW_ini;
//                    ratioH = canvasH_current / canvasH_ini;
//
//                    System.out.println("Initial Canvas Width: " + canvasW_ini);
//                    System.out.println("Initial Canvas Height: " + canvasH_ini);
//                    System.out.println("Current Canvas Width: " + canvasW_current);
//                    System.out.println("Current Canvas Height: " + canvasH_current);
//                    System.out.println("Change of width ratio: " + ratioW);
//                    System.out.println("Change of height ratio: " + ratioH);
//
//                ArrayList<Shape> shapes_copy = new ArrayList<>();
//                ArrayList<Shape> resizedShapes = new ArrayList<>();
//
//                // Copy all shapes to a temporary arrayList and clear the shapes array.
//                for (Shape s : shapes) {
//                    shapes_copy.add(s);
//                }
//                shapes.clear();
//
//                // Resize shapes on the temporary arrayList.
//                for (Shape s : shapes_copy) {
//                    String shapeName = s.getClass().getCanonicalName();
//                    if (shapeName.contains("Polygon")) {
//                        System.out.println("Resizing for polygon not implemented");
//                    } else {
//                        String command = identifyCommand(s);
//                        System.out.println("Resizing shapes...");
////                        if( ratioW > 1){
//                        double x1 = s.getBounds2D().getX() * ratioW;
//                        double x2 = s.getBounds2D().getWidth()* ratioW;
//                        double y1 = s.getBounds2D().getY() * ratioH;
//                        double y2 = s.getBounds2D().getHeight() * ratioH;
////                        }
//                        Shape resizedShape = drawShape(command, x1, y1, x2, y2);
//                        resizedShapes.add(resizedShape);
//                    }
//                }
//
//                // Copy the resized shapes in to the shapes array and clear the temporary array.
//                for (Shape s : resizedShapes) {
//                    shapes.add(s);
//                }
//                resizedShapes.clear();
//
//                repaint();
//            }
//        });// End Component Adapter

        this.addMouseListener(new MouseAdapter() {

            //Gets coordinates when mouse is pressed.
            public void mousePressed(MouseEvent e) {
//                System.out.println("Width: " + canvasW_ini);
//                System.out.println("Height: " + canvasH_ini);
                drawStart = new Point(e.getX(), e.getY());
                drawEnd = drawStart;
                coordinatesLabel.setText("(x,y): " + "(" + drawEnd.x + "," + drawEnd.y + ")     ");
                mousePressed = true;
                repaint();
            }

            //Gets coordinates when mouse is released.
            public void mouseReleased(MouseEvent e) {

//                canvasW_ini = getSize().getWidth();
//                canvasH_ini = getSize().getHeight();
                canvasW_current = getWidth();
                canvasH_current = getHeight();

                drawEnd = new Point(e.getX(), e.getY());
                coordinatesLabel.setText("(x,y): " + "(" + drawEnd.x + "," + drawEnd.y + ")     ");

                // Creates a shape based on the current n command
                Shape s = null;
                String command = getCurrentAction();

                if (isDrawingCommand(command)) {

                    //Defines the shape to be drawn base on the current command
                    if (command == "POLYGON") {
                        // (1) If it is a new polygon start by drawing a line and
                        // (2) collect the coordinates
                        if (newPolygon) { //(1)

                            //Tracking the initial number of shapes before drawing a polygon
                            initialNumberOfShapes = shapes.size();

                            s = drawShape("LINE", drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
                            newPolygon = false;

                        } else if (!newPolygon) { // If no, start the line from the last x,y point.

                            // Get the last x,y coordinates
                            drawStart.x = points.get(points.size() - 1).x;
                            drawStart.y = points.get(points.size() - 1).y;

                            // Draw line from the last x,y coordinates
                            s = drawShape("LINE", drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);

                            // Detect if user intends to close the polygon
                            int first_x = points.get(0).x;
                            int first_y = points.get(0).y;
                            int offset = 10;
                            if ((drawEnd.x >= (first_x - offset) && drawEnd.x <= first_x + offset) && (drawEnd.y >= (first_y - offset) && drawEnd.y <= (first_y + offset))) {
                                polygonClosed = true;
                            }
                        }

                        //(2) Collect coordinates
                        int[] coords = collectCoordinates();

                        //(3) Add points to the ArrayList
                        points.add(new Point(coords[0], coords[1]));
                        points.add(new Point(coords[2], coords[3]));

                    } else {
                        // Get the shape to be added to the canvas
                        // It is either PLOT, ELLIPSE, LINE or RECTANGLE
                        s = drawShape(command, drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
                    }

                    // Add shape to array lists so that it can be displayed
                    // (1) Add shapes
                    shapes.add(s);

                    // (2) Add pen color
                    Color pen = (command == "POLYGON" ? Color.LIGHT_GRAY : getCurrentPenColor());
                    shapePenColor.add(pen);

                    // (3) Add shape fill color
                    shapeFillColor.add(getCurrentFillColor());

                    // (4) Add shape fill boolean
                    shapeFilled.add(fillCheckBox.isSelected());


                    if (polygonClosed) {
                        //tracking how many line were used to construct the polygon
                        finalNumberOfShapes = shapes.size();
                        ArrayList<Integer> xpoints = new ArrayList<>();
                        ArrayList<Integer> ypoints = new ArrayList<>();


                        //Delete the lines that were used to construct the polygon
                        for (int i = finalNumberOfShapes - 1; i >= initialNumberOfShapes; i--) {
                            shapes.remove(i);
                            shapePenColor.remove(i);
                            shapeFillColor.remove(i);
                            shapeFilled.remove(i);
                        }

                        //Prepare points to be passed to drawPolygon(int[] x1, int[] y1, points);
                        // based on the line coordinates.
                        for (int i = 0; i < points.size(); i++) {
                            if (i == points.size() - 1) {
                                xpoints.add(points.get(0).x);
                                ypoints.add(points.get(0).y);
                            } else {
                                xpoints.add(points.get(i).x);
                                ypoints.add(points.get(i).y);
                            }
                        }

                        int[] xpoints_arr = new int[points.size()];
                        int[] ypoints_arr = new int[points.size()];

                        for (int i = 0; i < xpoints.size(); i++) {
                            xpoints_arr[i] = xpoints.get(i);
                        }
                        for (int i = 0; i < ypoints.size(); i++) {
                            ypoints_arr[i] = ypoints.get(i);
                        }

                        // Get a polygon
                        s = drawPolygon(xpoints_arr, ypoints_arr, xpoints_arr.length);

                        // Add polygon to array lists so that it can be displayed.
                        shapes.add(s);
                        shapePenColor.add(getCurrentPenColor());
                        shapeFillColor.add(getCurrentFillColor());
                        shapeFilled.add(fillCheckBox.isSelected());


                        //Write command to file
                        writeCommandToFile(command, xpoints_arr, ypoints_arr);

                        // Clearing globals for future use
                        newPolygon = true;
                        points.clear();

                    } else {

                        //Write other shape to the temporary file
                        if (command != "POLYGON") {
                            writeCommandToFile(command,
                                    (double) drawStart.x / getWidth(),
                                    (double) drawStart.y / getHeight(),
                                    (double) drawEnd.x / getWidth(),
                                    (double) drawEnd.y / getHeight());
                        }

                    }

                }

                mousePressed = false;
                polygonClosed = false;
                drawStart = null;
                drawEnd = null;

                repaint(); // IMPORTANT! -> repaint the drawing area
            }
        }); // End mouseReleased();


        // Gets coordinates when mouse is dragged.
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                drawEnd = new Point(e.getX(), e.getY());
                coordinatesLabel.setText("(x,y): " + "(" + drawEnd.x + "," + drawEnd.y + ")     ");
                repaint();
            }
        });

    }// end Canvas constructor

    /**
     * This methods identifies the command that was used to create a shape.
     * @param s a Shape
     * @return the command used to create the shape.
     */
    public static String identifyCommand(Shape s) {

        String name = s.getClass().getCanonicalName();
        String command = "";

        if (name.contains("Line")) {
            if (s.getBounds().width == 0) {
                command = "PLOT";
            } else {
                command = "LINE";
            }
        } else if (name.contains("Rectangle")) {
            command = "RECTANGLE";
        } else if (name.contains("Ellipse")) {
            command = "ELLIPSE";
        } else if (name.contains("Polygon")) {
            command = "POLYGON";
        }
        return command;
    }

    /**
     * Detects x and y when mouse is pressed and when mouse is released.
     *
     * @return int[] c = {x1, y1, x2, y2}
     */
    private int[] collectCoordinates() {
        // Detected when mouse is pressed
        int x1 = drawStart.x;
        int y1 = drawStart.y;

        // Detected when mouse is released
        int x2 = drawEnd.x;
        int y2 = drawEnd.y;
        int[] c = {x1, y1, x2, y2};
        return c;
    }

    /**
     * This is the main method to help drawing and rendering shapes on the canvas.
     * @param g
     */
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));

        // Iterators created to cycle through strokes and fills
        Iterator<Color> strokeCounter = shapePenColor.iterator();
        Iterator<Color> fillCounter = shapeFillColor.iterator();
        Iterator<Boolean> fillIndicator = shapeFilled.iterator();

        // Eliminates transparent setting below
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        for (Shape shape : shapes) {

            g2.setPaint(strokeCounter.next());  // Color of the border
            g2.draw(shape);                     // Shape of the drawing
            g2.setPaint(fillCounter.next());    // Color of the fill if any.

            if (fillIndicator.next()) {   // fill shape if needed
                g2.fill(shape);
            }

        }

        // Guide shape used as drawing guide
        if (drawStart != null && drawEnd != null) {
            // Transparent guide shape
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.40f));
            g2.setPaint(Color.LIGHT_GRAY);

            Shape shape;

            // Create new shape based on current command
            if (getCurrentAction() == "POLYGON") {
                if (newPolygon) {
                    shape = drawShape("LINE", drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
                } else {
                    drawStart.x = points.get(points.size() - 1).x;
                    drawStart.y = points.get(points.size() - 1).y;
                    shape = drawShape("LINE", drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
                }

            } else {
                shape = drawShape(getCurrentAction(), drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
            }
            g2.draw(shape);
        }
    }//End Paint Method


    /**
     * Returns a shape based on the current command action.
     * @param action Drawing command
     * @return Shape to be drawn
     */
    public Shape drawShape(String action, double x1, double y1, double x2, double y2) {

        // return the guide shape if none command is selected.
        Shape s = drawRectangle(x1, y1, x2, y2);

        //return a shape if any drawing command is selected.
        if (action == "LINE") {
            s = drawLine(x1, y1, x2, y2);
        } else if (action == "ELLIPSE") {
            s = drawEllipse(x1, y1, x2, y2);
        } else if (action == "PLOT") {
            s = drawPlot(x1, y1, x2, y2);
        } else if (action == "RECTANGLE") {
            s = drawRectangle(x1, y1, x2, y2);
        }
        return s;
    }

    /**
     * This methods returns a Rectangle2D.Double given the coordinates.
     * @param x1 coordinate.
     * @param y1 coordinate.
     * @param x2 coordinate.
     * @param y2 coordinate.
     * @return a Rectangle2D.Double
     */
    private Rectangle2D.Double drawRectangle(double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double width = Math.abs(x1 - x2);
        double height = Math.abs(y1 - y2);
        return new Rectangle2D.Double(x, y, width, height);
    }

    /**
     * This methods returns a Rectangle2D.Double given the coordinates.
     * @param x1 coordinate.
     * @param y1 coordinate.
     * @param x2 coordinate.
     * @param y2 coordinate.
     * @return a Ellipse2D.Double
     */
    private Ellipse2D.Double drawEllipse(double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double width = Math.abs(x1 - x2);
        double height = Math.abs(y1 - y2);
        return new Ellipse2D.Double(x, y, width, height);
    }

    /**
     * This methods returns a Rectangle2D.Double given the coordinates.
     * @param x1 coordinate.
     * @param y1 coordinate.
     * @param x2 coordinate.
     * @param y2 coordinate.
     * @return a Line2D.Double
     */
    private Line2D.Double drawLine(double x1, double y1, double x2, double y2) {
        return new Line2D.Double(x1, y1, x2, y2);
    }

    /**
     * This methods returns a Line2D.Double representing a plot.
     * Only the parameters x1 and y1 are used. The two other parameters are dummies
     * to help the method drawShape(String action, double x1, double y1, double x2, double y2);
     * @param x1 coordinate.
     * @param y1 coordinate.
     * @param x2 a dummy coordinate. This parameter will be ignored.
     * @param y2 a dummy coordinate. This parameter will be ignored.
     * @return a Line2D.Double of width 0.
     */
    private Line2D.Double drawPlot(double x1, double y1, double x2, double y2) {
        return new Line2D.Double(x1, y1, x1, y1);
    }

    /**
     * This methods return a Polygon class given the necessary x and y points.
     * @param xPoints
     * @param yPoints
     * @param sides
     * @return a Polygon
     */
    public Polygon drawPolygon(int[] xPoints, int[] yPoints, int sides) {
        Polygon p = new Polygon();
        p.xpoints = xPoints;
        p.ypoints = yPoints;
        p.npoints = sides;
        return p;
    }

    /**
     * This methods returns the given parameters rounded to 5 digits.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return the given coordinates rounded to 5 digits.
     */
    public ArrayList<Double> roundCoordinates(double x1, double y1, double x2, double y2) {
        ArrayList<Double> roundedCoordinates = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.#####");

        try {
            double x_1 = Double.parseDouble(df.format(x1));
            double y_1 = Double.parseDouble(df.format(y1));
            double x_2 = Double.parseDouble(df.format(x2));
            double y_2 = Double.parseDouble(df.format(y2));
            roundedCoordinates.add(x_1);
            roundedCoordinates.add(y_1);
            roundedCoordinates.add(x_2);
            roundedCoordinates.add(y_2);
        } catch (NumberFormatException e) {
            //e.printStackTrace();
        }
        return roundedCoordinates;

    }

    /**
     * Given an arrayList of double numbers, this method return a new array with those numbers rounded to 5 digits.
     *
     * @param arrayList
     * @return roundedArrayList
     */
    public static ArrayList<Double> roundCoordinates(ArrayList<Double> arrayList) {

        DecimalFormat df = new DecimalFormat("#.#####");
        ArrayList<Double> roundedCoordinates = new ArrayList<>();

        try {
            for (double c : arrayList) {
                roundedCoordinates.add(Double.parseDouble(df.format(c)));
            }
//            double x_1 = Double.parseDouble(df.format(x1));

        } catch (NumberFormatException e) {
            //e.printStackTrace();
        }
        return roundedCoordinates;

    }

    /**
     * Write command for Polygon use.
     *
     * @param command
     * @param xPoints
     * @param yPoints
     */
    public static void writeCommandToFile(String command, int[] xPoints, int[] yPoints) {

        VectorFileWriter fr = new VectorFileWriter();

        String filename = "temp";

        ArrayList<Double> coordinates = new ArrayList<>();

        //Prepare arrays for writing
        for (int i = 0; i < xPoints.length; i++) {
            coordinates.add(xPoints[i] / canvasW_current);
            coordinates.add(yPoints[i] / canvasH_current);
        }

        ArrayList<Double> roundedCoordinates = roundCoordinates(coordinates);

        String c_srt = "";
        for (int i = 0; i < roundedCoordinates.size(); i++) {
            double c = roundedCoordinates.get(i);

            if (i == roundedCoordinates.size() - 1) {
                c_srt += c;
            } else {
                c_srt += c + " ";
            }
        }

        fr.writeToFile(filename, command + " " + c_srt + "\n");
    }

    /**
     * Write method for any given command with the exception of the Polygon command
     *
     * @param command
     * @param x_1
     * @param y_1
     * @param x_2
     * @param y_2
     */
    private void writeCommandToFile(String command, double x_1, double y_1, double x_2, double y_2) {

        VectorFileWriter fr = new VectorFileWriter();
        String filename = "temp";

        ArrayList<Double> c = roundCoordinates(x_1, y_1, x_2, y_2);
        double x1 = c.get(0);
        double y1 = c.get(1);
        double x2 = c.get(2);
        double y2 = c.get(3);


        if (fillCheckBox.isSelected() && !fillUsed) {
            fr.writeToFile(filename, "FILL " + ColorToHex(VectorImagePlotter.getCurrentFillColor()) + "\n");
            fillUsed = true;
            VectorImagePlotter.fillColorChanged = false;
        } else if (!fillCheckBox.isSelected() && fillUsed) {
            fr.writeToFile(filename, "FILL OFF" + "\n");
            System.out.println("FILL OFF");
            fillUsed = false;
        } else if (VectorImagePlotter.fillColorChanged && fillCheckBox.isSelected()) {
            fr.writeToFile(filename, "FILL " + ColorToHex(VectorImagePlotter.getCurrentFillColor()) + "\n");
            VectorImagePlotter.fillColorChanged = false;
        } else if (VectorImagePlotter.penColorChanged) {
            fr.writeToFile(filename, "PEN " + ColorToHex(VectorImagePlotter.getCurrentPenColor()) + "\n");
            VectorImagePlotter.penColorChanged = false;
        }

        if (isDrawingCommand(command)) {
            if (command == "PLOT") {
                //Syntax for PLOT
                fr.writeToFile(filename, command + " " + x1 + " " + y1 + " " + "\n");
                System.out.println(command + " " + x1 + " " + y1);
            } else {
                //Syntax for LINE, ELLIPSE and RECTANGLE
                fr.writeToFile(filename, command + " " + x1 + " " + y1 + " " + x2 + " " + y2 + "\n");
                System.out.println(command + " " + x1 + " " + y1 + " " + x2 + " " + y2);
            }
        }
    }//end writeCommandToFile();


    /**
     * Return the HEX format of a given Color
     *
     * @param color
     * @return
     */
    private static String ColorToHex(Color color) {
        String rgb = Integer.toHexString(color.getRGB());
        return "#" + rgb.substring(2).toUpperCase();
    }

    /**
     * @return
     */
    private int getLastDrawingIndex(ArrayList arr) {
        int indexOfLastDrawing = arr.size() - 1;
        return indexOfLastDrawing;
    }

    /**
     * Removes the mos recent drawing.
     */
    public void undo() throws Exception {

        if (!shapes.isEmpty()) {
            //Remove last drawing
            int lastDrawing = getLastDrawingIndex(shapes);

            //Add deleted elements
            temp_shapes.add(shapes.get(getLastDrawingIndex(shapes)));
            temp_shapePenColor.add(shapePenColor.get(getLastDrawingIndex(shapePenColor)));
            temp_shapeFillColor.add(shapeFillColor.get(getLastDrawingIndex(shapeFillColor)));
            temp_shapeFilled.add(shapeFilled.get(getLastDrawingIndex(shapeFilled)));


            shapes.remove(lastDrawing);
            shapePenColor.remove(lastDrawing);
            shapeFillColor.remove(lastDrawing);
            shapeFilled.remove(lastDrawing);
            repaint();
        } else {
            throw new Exception();
        }

    }

    /**
     * Redraws the most recent deleted drawing.
     */
    public void redo() throws Exception {

        if (!temp_shapes.isEmpty()) {
            int lastDrawing = getLastDrawingIndex(temp_shapes);
            //Redraw the most recent deleted drawing
            shapes.add(temp_shapes.get(getLastDrawingIndex(temp_shapes)));
            shapePenColor.add(temp_shapePenColor.get(getLastDrawingIndex(temp_shapePenColor)));
            shapeFillColor.add(temp_shapeFillColor.get(getLastDrawingIndex(temp_shapeFillColor)));
            shapeFilled.add(temp_shapeFilled.get(getLastDrawingIndex(temp_shapeFilled)));

            temp_shapes.remove(lastDrawing);
            temp_shapePenColor.remove(lastDrawing);
            temp_shapeFillColor.remove(lastDrawing);
            temp_shapeFilled.remove(lastDrawing);

            repaint();
        } else {
            throw new Exception();
        }

    }

    /**
     * Clear all drawings from the display panel.
     * <ul><li>If forcedAction == true, clears drawings.</li>
     * <li>If forcedAction == false, prompts the user to save current drawings.</li>
     * <li>Returns true if all drawing were cleared.</li>
     * </ul>
     *
     * @param forcedAction
     * @return boolean
     */
    public boolean clearDrawings(boolean forcedAction) {

        // Clear drawings it doesn't matter what
        if (forcedAction) {
            shapes.clear();
            shapePenColor.clear();
            shapeFillColor.clear();
            shapeFilled.clear();
            repaint();
            return true;
        } else {
            // Prompt user to save current work.
            if (!shapes.isEmpty()) {

                //Dialog
                Object[] options = {"Save", "Don't Save", "Cancel"};
                int clearDrawings = JOptionPane.showOptionDialog(VectorImagePlotter.getDisplayPanel(),
                        "There are drawings in your current workspace. \n"
                                + "Do you want to save these?",
                        "Clear Drawings?",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[2]);
                //If "Ok" clear all drawings
                if (clearDrawings == 0) {
                    Save.doClick();
                    repaint();
                    return true;
                } else if (clearDrawings == 1) {
                    shapes.clear();
                    shapePenColor.clear();
                    shapeFillColor.clear();
                    shapeFilled.clear();
                    repaint();
                    return true;
                } else {
                    return false;
                }

            } else {
                return true;
            }
        }
    }


}//end Canvas Class