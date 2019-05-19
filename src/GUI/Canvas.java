package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

public class Canvas extends JComponent {

    // ArrayLists that contain each shape drawn along with
    // that shapes stroke and fill

    ArrayList<Shape> shapes = new ArrayList<Shape>();
    ArrayList<Color> shapePen = new ArrayList<Color>();
    ArrayList<Color> shapeFill = new ArrayList<Color>();
    Point drawStart, drawEnd;

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
                repaint();
            }

            /**
             * Gets coordinates when mouse is released.
             * @param e
             */
            public void mouseReleased(MouseEvent e) {

                // Creates a shape based on the current currentAction command
                Shape s;
                String command = Window.getCurrentAction();
                if ( command != "CLEAR" && command != "PEN" && command != "FILL"){
                    s = defineShape(command, drawStart.x, drawStart.y, e.getX(), e.getY());
                    shapes.add(s);
                }

                // Add shapes, fills and colors to there ArrayLists
                shapePen.add(Window.getCurrentPenColor());
                shapeFill.add(Window.getCurrentFillColor());
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
//                coor.setText("(x,y): " + e.getX() +", "+ e.getY() + "      ");
                repaint();
            }
        });

    }// end Canvas constructor

    public void paint(Graphics g){

        // Class used to define the shapes to be drawn
        Graphics2D graphSettings = (Graphics2D)g;

        // Antialiasing cleans up the jagged lines and defines rendering rules
        graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Defines the line width of the stroke
        graphSettings.setStroke(new BasicStroke(3));

        // Iterators created to cycle through strokes and fills
        Iterator<Color> strokeCounter = shapePen.iterator();
        Iterator<Color> fillCounter = shapeFill.iterator();

        // Eliminates transparent setting below
        graphSettings.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        for (Shape shape : shapes) {

            // Grabs the next stroke from the color ArrayList
            graphSettings.setPaint(strokeCounter.next());
            graphSettings.draw(shape);

            // Grabs the next fill from the color ArrayList
            graphSettings.setPaint(fillCounter.next());

//                graphSettings.fill(s);

        }

        // Guide shape used for drawing
        if (drawStart != null && drawEnd != null) {
            // Transparent guide shape
            graphSettings.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.40f));

            graphSettings.setPaint(Color.LIGHT_GRAY);

            // Create new shape base on current command
            Shape shape = defineShape(Window.getCurrentAction(),drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
            graphSettings.draw(shape);
        }
    }//End Pain Method


    /**
     * Returns a shape based on the current command action.
     * @param action
     * @return
     */
    public Shape defineShape(String action, int x1,int y1, int x2, int y2){

        // return the guide shape if none command is selected.
        Shape s = drawRectangle(x1, y1, x2, y2);

        //return the apropiatte shape if any drawing command is selected.
        if ( action == "LINE"){
            s = drawLine(x1, y1, x2, y2);
        }else if (action == "ELLIPSE"){
            s = drawEllipse(x1, y1, x2, y2);
        }else if( action == "PLOT"){
            s = drawPlot(x1, y1, x2, y2);
        }else if (action == "RECTANGLE"){
            s = drawRectangle(x1, y1, x2, y2);
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

}//end Canvas Class