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

    static ArrayList<Shape> shapes = new ArrayList<Shape>();
    static ArrayList<Color> shapePen = new ArrayList<Color>();
    static ArrayList<Color> shapeFill = new ArrayList<Color>();
    static  ArrayList<Boolean> shapeFilled = new ArrayList<>();
    Point drawStart;
    static Point drawEnd;

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

                 if( Window.isDrawingCommand(command)){
                    s = defineShape(command, drawStart.x, drawStart.y, e.getX(), e.getY());
                    // Add shapes, fills and colors to there ArrayLists
                    shapes.add(s);
                    shapePen.add(Window.getCurrentPenColor());
                    shapeFill.add(Window.getCurrentFillColor());

                    if(Window.fillCheckBox.isSelected()){
                        shapeFilled.add(true);
                    }else{
                        shapeFilled.add(false);
                    }
                 }

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

    public static String getMouseCoordinates(){
        String coord = "(0,0)";
        if(drawEnd != null){
            coord = "("+ drawEnd.x + drawEnd.y+")";
        }
        return coord;
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

        // Guide shape used for drawing
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

        //return the appropiate shape if any drawing command is selected.
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