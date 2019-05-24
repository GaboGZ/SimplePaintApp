package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import FileHandler.*;

public class Canvas extends JComponent {

    // ArrayLists that contain each shape drawn along with
    // that shapes stroke and fill

    static ArrayList<Shape> shapes = new ArrayList<Shape>();
    static ArrayList<Color> shapePen = new ArrayList<Color>();
    static ArrayList<Color> shapeFill = new ArrayList<Color>();
    static  ArrayList<Boolean> shapeFilled = new ArrayList<>();
    static Point drawStart;
    static Point drawEnd;

    static boolean mousePressed = false;

    /**
     * Creates a Canvas for the display panel.
     * Adds a MouseListener to detect mouse events.
     */
    public Canvas() {
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

//                drawEnd = new Point(e.getX(), e.getY());

                // Creates a shape based on the current currentAction command
                Shape s;
                String command = Window.getCurrentAction();

                 if( Window.isDrawingCommand(command)){

                     //Defines the shape to be drawn base on the current command
                     s = defineShape(command, drawStart.x, drawStart.y, e.getX(), e.getY());

                     // Add shapes, fills and colors to the ArrayLists
                     shapes.add(s);
                     shapePen.add(Window.getCurrentPenColor());
                     shapeFill.add(Window.getCurrentFillColor());

                     if(Window.fillCheckBox.isSelected()){
                         shapeFilled.add(true);
                     }else{
                         shapeFilled.add(false);
                     }

                     writeCommandtoFile(command, drawStart.x, drawStart.y, e.getX(), e.getY());
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
                repaint();
            }
        });

    }// end Canvas constructor


    /**
     * Detects coordinates when mouse is pressed and when the mouse is released
     * @return points - A 2D array containing x points and y points.
     */

    public static int[][] getMouseCoordinates(){
        // todo: finish getMouseCoordinates();
        // Detected when mouse is pressed
        int x1 = drawStart.x;
        int y1 = drawStart.y;
        // Detected when mouse is released
        int x2 = drawEnd.x;
        int y2 = drawEnd.y;

        int[] xpoints = {x1,x2};
        int[] ypoints = {y1,y2};
        int[][] points = {xpoints,ypoints};

        if (mousePressed){
            System.out.println("Mouse pressed at " + "(" + x1 + "," + y1 + ")");
        }else{
            System.out.println("Mouse released at " + "(" + x2 + "," + y2 + ")");
        }

        return points;


    }

    public void paint(Graphics g){

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));

        // Iterators created to cycle through strokes and fills
        Iterator<Color> strokeCounter = shapePen.iterator();
        Iterator<Color> fillCounter = shapeFill.iterator();
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
            Shape shape = defineShape(Window.getCurrentAction(),drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
            g2.draw(shape);
        }
    }//End Paint Method


    /**
     * Returns a shape based on the current command action.
     * @param action
     * @return
     */
    public Shape defineShape(String action, int x1,int y1, int x2, int y2){

        // return the guide shape if none command is selected.
        Shape s = drawRectangle(x1, y1, x2, y2);

        //return a shape if any drawing command is selected.
        if ( action == "LINE"){
            s = drawLine(x1, y1, x2, y2);

        }else if (action == "ELLIPSE"){
            s = drawEllipse(x1, y1, x2, y2);
        }else if( action == "PLOT"){
            s = drawPlot(x1, y1, x2, y2);
        }else if (action == "RECTANGLE"){
            s = drawRectangle(x1, y1, x2, y2);
        }else if (action == "POLYGON"){
//            s = drawPolygon(x1, y1, x2, y2);
            s = drawPolyline(x1, y1, x2, y2);
        }
        return s;
    }

    private Rectangle2D.Float drawRectangle(int x1, int y1, int x2, int y2) {
        // Get the top left hand corner for the shape
        // Math.min returns the points closest to 0

        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);

        // Gets the difference between the coordinates and

        int width = Math.abs(x1 - x2);
        int height = Math.abs(y1 - y2);

        return new Rectangle2D.Float(
                x, y, width, height);
    }

    private Ellipse2D.Float drawEllipse(int x1, int y1, int x2, int y2) {
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int width = Math.abs(x1 - x2);
        int height = Math.abs(y1 - y2);

        return new Ellipse2D.Float(x, y, width, height);
    }

    private Line2D.Float drawLine(int x1, int y1, int x2, int y2){
        return new Line2D.Float(x1,y1,x2,y2);
    }

    private Line2D.Float drawPlot(int x1, int y1, int x2, int y2){
        return new Line2D.Float(x1,y1,x1,y1);
    }

    //todo: finish drawPolygon();
    private GeneralPath drawPolygon(int x1, int y1, int x2, int y2) {

        int xPoints[] = getMouseCoordinates()[0];
        int yPoints[] = getMouseCoordinates()[1];
        // draw GeneralPath (polygon)
        GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
        // How can I feed these arrays with mouse events?

        polygon.moveTo(xPoints[0], yPoints[0]);
        for (int index = 1; index < xPoints.length; index++) {
            polygon.lineTo(xPoints[index], yPoints[index]);
        }
        polygon.closePath();

        return polygon;
    }

    private GeneralPath drawPolyline(int x1, int y1, int x2, int y2) {

        // draw GeneralPath (polyline)
        int[] xPoints = {x1,x2};
        int[] yPoints = {y1,y2};

//        int xPoints[] = getMouseCoordinates()[1];
//        int yPoints[] = getMouseCoordinates()[1];

        GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
        polyline.moveTo (xPoints[0], yPoints[0]);
        for ( int index = 1; index < xPoints.length; index++ ) {
            polyline.lineTo(xPoints[index], yPoints[index]);
        };

        return polyline;
    }

    private void writeCommandtoFile(String command, int x1, int y1, int x2, int y2){
        FileWriter fr = new FileWriter();
        String filename = "plotTest";

        //todo: THIS ONE WORKS
        if (Window.isDrawingCommand(command)){
            if(command == "PLOT"){
                fr.writeToFile(filename, command +" "+ x1 +" "+ y1 +" "+"\n");
            }else if (command == "POLYGON"){
                fr.writeToFile(filename, command +" "+ x1 +" "+ y1 +" "+ x2 +" "+ y2 + "\n");
            }else{
                fr.writeToFile(filename, command +" "+ x1 +" "+ y1 +" "+ x2 +" "+ y2 + "\n");
            }
        }
        //todo: this if statement if being skipped
        if( command == "PEN" || command == "FILL"){
            fr.writeToFile(filename, command +" "+ Window.getCurrentPenColor() +"\n");
        }

        //todo: THIS ONE WORKS!
        if(!Window.fillClicked){
            fr.writeToFile(filename, "FILL OFF" +"\n");
        }


        //LINE [x1] [y1] [x2] [y2]
        //RECTANGLE [x1] [y1] [x2] [y2]
        //ELLIPSE [x1] [y1] [x2] [y2]
        //PLOT [x1] [y1]
        //PEN #FF0000
        //FILL #FFFF00
        //FILL OFF
        // POLYGON [x1] [y1] [x2] [y2] [x3…] [y3…]

    }


}//end Canvas Class