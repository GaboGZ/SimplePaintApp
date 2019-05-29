package GUI;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
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

public class Canvas extends JComponent {

    static double canvasWidth;
    static double canvasHeight;
    private JFileChooser fileChooser;

    // Arraylists shapes information:
    //  shapes: stores Shape form either PLOT, LINE, RECTANGLE or POLYGON
    //  shapePenColor: stores Pen Color
    //  shapeFillColor: stores Fill Color
    //  shapeFilled: stores whether the shape is filled or not.
    public ArrayList<Shape> shapes = new ArrayList<Shape>();
    public ArrayList<Color> shapePenColor = new ArrayList<Color>();
    public ArrayList<Color> shapeFillColor = new ArrayList<Color>();
    public ArrayList<Boolean> shapeFilled = new ArrayList<>();
    static Point drawStart;
    static Point drawEnd;

    static boolean mousePressed = false;
    private boolean fillUsed = false;

    /**
     * Creates a Canvas for the display panel.
     * Adds a MouseListener to detect mouse events.
     */
    public Canvas() {

        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

//        canvasWidth = getSize().getWidth();
//        canvasHeight = getSize().getHeight();

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

                // Creates a shape based on the current currentAction command
                Shape s;
                String command = Window.getCurrentAction();

                 if( Window.isDrawingCommand(command)){

                     //Defines the shape to be drawn base on the current command
                     s = defineShape(command,  drawStart.x,  drawStart.y,  drawEnd.x, drawEnd.y);

                     // Add shapes, fills and colors to the ArrayLists
                     shapes.add(s);
                     shapePenColor.add(Window.getCurrentPenColor());
                     shapeFillColor.add(Window.getCurrentFillColor());

                     if(Window.fillCheckBox.isSelected()){
                         shapeFilled.add(true);
                     }else{
                         shapeFilled.add(false);
                     }

                     writeCommandToFile(command,  drawStart.x/getWidth(),  drawStart.y/getHeight(),  drawEnd.x/getWidth(), drawEnd.y/getHeight());
                 }

                 mousePressed = false;
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
                Window.coordLabel.setText("(x,y): "+ "(" + drawEnd.x + "," + drawEnd.y + ")     ");
                repaint();
            }
        });

    }// end Canvas constructor



    public void getMouseCoordinates(){

        // Detected when mouse is pressed
        int x1 = drawStart.x;
        int y1 = drawStart.y;

        // Detected when mouse is released
        int x2 = drawEnd.x;
        int y2 = drawEnd.y;

        if (mousePressed){
            Window.coordLabel.setText("(x,y): "+ "(" + x1 + "," + y1 + ")     ");
        }
        else{
            Window.coordLabel.setText("(x,y): "+ "(" + x2 + "," + y2 + ")     ");
        }
    }

    public void paint(Graphics g){

        Graphics2D g2 = (Graphics2D)g;
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

            if(fillIndicator.next() == true){   // fill shape if needed
                g2.fill(shape);
            }

        }

        // Guide shape used as drawing guide
        if (drawStart != null && drawEnd != null) {
            // Transparent guide shape
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.40f));
            g2.setPaint(Color.LIGHT_GRAY);

            // Create new shape based on current command
            Shape shape = defineShape(Window.getCurrentAction(), drawStart.x,  drawStart.y,  drawEnd.x, drawEnd.y);
            g2.draw(shape);
        }
    }//End Paint Method


    /**
     * Returns a shape based on the current command action.
     * @param action
     * @return
     */
    public Shape defineShape(String action, double x1,double y1, double x2, double y2){

        // return the guide shape if none command is selected.
        Shape s = drawRectangle(x1,y1,x2,y2);

        //return a shape if any drawing command is selected.
        if ( action == "LINE"){
            s = drawLine(x1,y1,x2,y2);
        }
        else if (action == "ELLIPSE"){
            s = drawEllipse(x1,y1,x2,y2);
        }
        else if( action == "PLOT"){
            s = drawPlot(x1,y1,x2,y2);
        }
        else if (action == "RECTANGLE"){
            s = drawRectangle(x1,y1,x2,y2);
        }
        else if (action == "POLYGON"){
//            s = drawPolygon(x1, y1, x2, y2);
//            s = drawPolyline(x1, y1, x2, y2);
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

    private Line2D.Double drawLine(double x1, double y1, double x2, double y2){
        return new Line2D.Double(x1,y1,x2,y2);
    }

    private Line2D.Double drawPlot(double x1, double y1, double x2, double y2){
        return new Line2D.Double(x1,y1,x1,y1);
    }

    //todo: finish drawPolygon();
//    private GeneralPath drawPolygon(double x1, double y1, double x2, double y2) {
//
////        double xPoints[] =  getMouseCoordinates()[0];
////        double yPoints[] = getMouseCoordinates()[1];
//        // draw GeneralPath (polygon)
//        GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
//        // How can I feed these arrays with mouse events?
//
//        polygon.moveTo(xPoints[0], yPoints[0]);
//        for (int index = 1; index < xPoints.length; index++) {
//            polygon.lineTo(xPoints[index], yPoints[index]);
//        }
//        polygon.closePath();
//
//        return polygon;
//    }

//    private GeneralPath drawPolyline(double x1, double y1, double x2, double y2) {
//
//        // draw GeneralPath (polyline)
//        int[] xPoints = {x1,x2};
//        int[] yPoints = {y1,y2};
//
////        int xPoints[] = getMouseCoordinates()[1];
////        int yPoints[] = getMouseCoordinates()[1];
//
//        GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
//        polyline.moveTo (xPoints[0], yPoints[0]);
//        for ( int index = 1; index < xPoints.length; index++ ) {
//            polyline.lineTo(xPoints[index], yPoints[index]);
//        };
//
//        return polyline;
//    }

    public ArrayList<Double> roundCoordinates(double x1, double y1, double x2, double y2){
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

    private void writeCommandToFile(String command, double x_1, double y_1, double x_2, double y_2){

        FileWriter fr = new FileWriter();
        String filename = "plotTest";

        ArrayList<Double> c = roundCoordinates(x_1,y_1,x_2,y_2);
        double x1 = c.get(0);
        double y1 = c.get(1);
        double x2 = c.get(2);
        double y2 = c.get(3);


        if( Window.fillCheckBox.isSelected() && !fillUsed){
            fr.writeToFile(filename, "FILL "+ ColorToHex(Window.getCurrentFillColor()) +"\n");
            fillUsed = true;
            Window.fillColorChanged = false;
        }
        else if(!Window.fillCheckBox.isSelected() && fillUsed){
            fr.writeToFile(filename, "FILL OFF" +"\n");
            System.out.println("FILL OFF");
            fillUsed = false;
        }
        else if(Window.fillColorChanged && Window.fillCheckBox.isSelected()) {
            fr.writeToFile(filename, "FILL "+ ColorToHex(Window.getCurrentFillColor()) +"\n");
            Window.fillColorChanged = false;
        }
        else if(Window.penColorChanged){
            fr.writeToFile(filename, "PEN "+ ColorToHex(Window.getCurrentPenColor()) +"\n");
            Window.penColorChanged = false;
        }

        if(Window.isDrawingCommand(command)) {
            if (command == "PLOT") {
                fr.writeToFile(filename, command + " " + x1 + " " + y1 + " " + "\n");
                System.out.println(command + " " + x1 + " " + y1);
            } else if (command == "POLYGON") {
                //todo:Finish file writer for polygon once drawPolygon(); is finished.
                // POLYGON [x1] [y1] [x2] [y2] [x3…] [y3…]
                fr.writeToFile(filename, command + " " + x1 + " " + y1 + " " + x2 + " " + y2 + "\n");
                System.out.println(command + " " + x1 + " " + y1 + " " + x2 + " " + y2);

            } else {//Syntax for LINE, ELLIPSE and RECTANGLE
                fr.writeToFile(filename, command + " " + x1 + " " + y1 + " " + x2 + " " + y2 + "\n");
                System.out.println(command + " " + x1 + " " + y1 + " " + x2 + " " + y2);
            }
        }
    }//end writeCommandToFile();


    /**
     * Return the HEX format of a given Color
     * @param color
     * @return
     */
    public static String ColorToHex(Color color) {
        String rgb = Integer.toHexString(color.getRGB());
        return "#"+rgb.substring(2).toUpperCase();
    }

    /**
     * Clear all drawings from the display panel.
     * <ul><li>If forcedAction == true, clears drawings.</li>
     * <li>If forcedAction == false, prompts the user to save current drawings.</li>
     * <li>Returns true if all drawing were cleared.</li>
     * </ul>
     * @param forcedAction
     * @return boolean
     */
    public boolean clearDrawings(boolean forcedAction){

        if(forcedAction){
            shapes.clear();
            shapePenColor.clear();
            shapeFillColor.clear();
            shapeFilled.clear();
            repaint();
            return true;
        }
        else{
            if(!shapes.isEmpty()){

                //Dialog
                Object[] options = {"Save", "Don't Save", "Cancel"};
                int clearDrawings = JOptionPane.showOptionDialog( Window.getDisplayPanel(),
                        "There are drawings in your current workspace. \n"
                                + "Do you want to save these?",
                        "Clear Drawings?",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[2]);
                //If "Ok" clear all drawings
                if( clearDrawings == 0){
                    int returnVal = fileChooser.showOpenDialog(Window.getDisplayPanel());
                    //todo: show Save



                    repaint();
                    return true;
                }
                else if( clearDrawings == 1){
                    shapes.clear();
                    shapePenColor.clear();
                    shapeFillColor.clear();
                    shapeFilled.clear();
                    repaint();
                    return true;
                }
                else{
                    return false;
                }

            }else{
                return true;
            }
        }
    }




}//end Canvas Class