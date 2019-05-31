package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import FileHandler.*;

import static GUI.Window.*;

public class Canvas extends JComponent {

    static double canvasWidth;
    static double canvasHeight;
    private JFileChooser fileChooser;

    // Arraylists that store shapes information:
    //  shapes: stores Shape form either PLOT, LINE, RECTANGLE or POLYGON
    //  shapePenColor: stores Pen Color
    //  shapeFillColor: stores Fill Color
    //  shapeFilled: stores whether the shape is filled or not.
    public ArrayList<Shape> shapes = new ArrayList<>();
    public ArrayList<Color> shapePenColor = new ArrayList<>();
    public ArrayList<Color> shapeFillColor = new ArrayList<>();
    public ArrayList<Boolean> shapeFilled = new ArrayList<>();

    // Temporary Arrays to handle UNDO and REDO features
    ArrayList<Shape> temp_shapes = new ArrayList<>();
    ArrayList<Color> temp_shapePenColor = new ArrayList<>();
    ArrayList<Color> temp_shapeFillColor = new ArrayList<>();
    ArrayList<Boolean> temp_shapeFilled = new ArrayList<>();


    //
    static Point drawStart;
    static Point drawEnd;
    ArrayList<Point> points = new ArrayList<>();

    static boolean mousePressed = false;
    private boolean fillUsed = false;

    int initialNumberOfShapes;
    int finalNumberOfShapes;


    boolean newPolygon = true;
    boolean polygonClosed = false;


    /**
     * Creates a Canvas for the display panel.
     * This canvas has a MouseListener to detect and handle all mouse event over the canvas.
     */
    public Canvas() {

        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        this.addMouseListener(new MouseAdapter() {

            /**
             * Gets coordinates when mouse is pressed.
             * @param e
             */
            public void mousePressed(MouseEvent e) {
                drawStart = new Point(e.getX(), e.getY());
                drawEnd = drawStart;
                mousePressed = true;
                getMouseCoordinates();
                repaint();
            }

            /**
             * Gets coordinates when mouse is released.
             * @param e
             */
            public void mouseReleased(MouseEvent e) {

                canvasWidth = getSize().getWidth();
                canvasHeight = getSize().getHeight();

                drawEnd = new Point(e.getX(), e.getY());

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

                        // Clearing globals for future use
                        newPolygon = true;
                        points.clear();

                    } else {
                        writeCommandToFile(command, (double) drawStart.x / getWidth(), (double) drawStart.y / getHeight(), (double) drawEnd.x / getWidth(), (double) drawEnd.y / getHeight());
                    }

                }

                mousePressed = false;
                polygonClosed = false;
                getMouseCoordinates();
                drawStart = null;
                drawEnd = null;

                repaint(); // IMPORTANT! -> repaint the drawing area
            }
        });

        /**
         * Gets coordinates when mouse is dragged.
         */
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                drawEnd = new Point(e.getX(), e.getY());
                Window.coordLabel.setText("(x,y): " + "(" + drawEnd.x + "," + drawEnd.y + ")     ");
                repaint();
            }
        });

    }// end Canvas constructor

    /**
     * Detects x and y when mouse is pressed and when mouse is released.
     *
     * @return int[] c = {x1, y1, x2, y2}
     */
    public int[] collectCoordinates() {
        // Detected when mouse is pressed
        int x1 = drawStart.x;
        int y1 = drawStart.y;

        // Detected when mouse is released
        int x2 = drawEnd.x;
        int y2 = drawEnd.y;
        int[] c = {x1, y1, x2, y2};
        return c;
    }

    public void getMouseCoordinates() {

        // Detected when mouse is pressed
        int x1 = drawStart.x;
        int y1 = drawStart.y;

        // Detected when mouse is released
        int x2 = drawEnd.x;
        int y2 = drawEnd.y;

        if (mousePressed) {
            Window.coordLabel.setText("(x,y): " + "(" + x1 + "," + y1 + ")     ");
        } else {
            Window.coordLabel.setText("(x,y): " + "(" + x2 + "," + y2 + ")     ");
        }
    }

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

            if (fillIndicator.next() == true) {   // fill shape if needed
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
     *
     * @param action
     * @return
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

    private Rectangle2D.Double drawRectangle(double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double width = Math.abs(x1 - x2);
        double height = Math.abs(y1 - y2);
        return new Rectangle2D.Double(x, y, width, height);
    }

    private Ellipse2D.Double drawEllipse(double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double width = Math.abs(x1 - x2);
        double height = Math.abs(y1 - y2);
        return new Ellipse2D.Double(x, y, width, height);
    }

    private Line2D.Double drawLine(double x1, double y1, double x2, double y2) {
        return new Line2D.Double(x1, y1, x2, y2);
    }

    private Line2D.Double drawPlot(double x1, double y1, double x2, double y2) {
        return new Line2D.Double(x1, y1, x1, y1);
    }

    public Polygon drawPolygon(int[] xPoints, int[] yPoints, int sides) {
        Polygon p = new Polygon();
        p.xpoints = xPoints;
        p.ypoints = yPoints;
        p.npoints = sides;
        return p;
    }

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

    private void writeCommandToFile(String command, double x_1, double y_1, double x_2, double y_2) {

        FileWriter fr = new FileWriter();
        String filename = "plotTest";

        ArrayList<Double> c = roundCoordinates(x_1, y_1, x_2, y_2);
        double x1 = c.get(0);
        double y1 = c.get(1);
        double x2 = c.get(2);
        double y2 = c.get(3);


        if (fillCheckBox.isSelected() && !fillUsed) {
            fr.writeToFile(filename, "FILL " + ColorToHex(Window.getCurrentFillColor()) + "\n");
            fillUsed = true;
            Window.fillColorChanged = false;
        } else if (!fillCheckBox.isSelected() && fillUsed) {
            fr.writeToFile(filename, "FILL OFF" + "\n");
            System.out.println("FILL OFF");
            fillUsed = false;
        } else if (Window.fillColorChanged && fillCheckBox.isSelected()) {
            fr.writeToFile(filename, "FILL " + ColorToHex(Window.getCurrentFillColor()) + "\n");
            Window.fillColorChanged = false;
        } else if (Window.penColorChanged) {
            fr.writeToFile(filename, "PEN " + ColorToHex(Window.getCurrentPenColor()) + "\n");
            Window.penColorChanged = false;
        }

        if (isDrawingCommand(command)) {
            if (command == "PLOT") {
                fr.writeToFile(filename, command + " " + x1 + " " + y1 + " " + "\n");
                System.out.println(command + " " + x1 + " " + y1);
            } else if (command == "POLYGON") {
                //todo:Finish file writer for polygon once drawPolygon(); is finished.
                // POLYGON [x1] [y1] [x2] [y2] [x3…] [y3…]
                fr.writeToFile(filename, command + " " + x1 + " " + y1 + " " + x2 + " " + y2 + "\n");
//                System.out.println(command + " " + x1 + " " + y1 + " " + x2 + " " + y2);

            } else {//Syntax for LINE, ELLIPSE and RECTANGLE
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
    public static String ColorToHex(Color color) {
        String rgb = Integer.toHexString(color.getRGB());
        return "#" + rgb.substring(2).toUpperCase();
    }

    /**
     * @return
     */
    public int getLastDrawingIndex(ArrayList arr) {
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

        if (forcedAction) {
            shapes.clear();
            shapePenColor.clear();
            shapeFillColor.clear();
            shapeFilled.clear();
            repaint();
            return true;
        } else {
            if (!shapes.isEmpty()) {

                //Dialog
                Object[] options = {"Save", "Don't Save", "Cancel"};
                int clearDrawings = JOptionPane.showOptionDialog(Window.getDisplayPanel(),
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
                    int returnVal = fileChooser.showOpenDialog(Window.getDisplayPanel());
                    //todo: show Save


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