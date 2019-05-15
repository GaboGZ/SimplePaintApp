package GUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.awt.Graphics;


public class Window extends JFrame implements ActionListener,Runnable {


    //Constants
    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;

    //Elements
    private JMenuBar menuBar;
    private JMenu menuFile, menuHome, menuView;
    private JMenuItem New, Open, Save, SaveAs, Options, Close;

    public JPanel mainPanel;
    private JPanel pnlUp, pnlUp_Up, pnlUp_Down, pnlUp_Up_Up, pnlUp_Up_Down;
    private JPanel pnlLeft, pnlRight;
    private JPanel pnlDown, pnlDown_Up, pnlDown_Down;
    private JPanel pnlDisplay;
//    private DrawArea drawArea;

    private JToolBar toolBar;

    private JButton plotBtn, lineBtn, rectangleBtn, ellipseBtn, polygonBtn;

    public JButton penBtn, fillBtn, clearBtn, customBtn;
    public JButton blackBtn, blueBtn, greenBtn, darkGrayBtn, lightGrayBtn;
    public JButton cyanBtn, redBtn, magentaBtn, whiteBtn, grayBtn, orangeBtn, yellowBtn;
    public JPanel colorPalette;

    private JColorChooser colorPicker;

    private JFileChooser fileChooser;

    //Constructor
    /**
     * Constructs the main window by creating a JFrame
     * @param str
     * @see JFrame
     */
    public Window(String str){

        //Set default look and feel
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create window
        JFrame Window = new JFrame(str);

        //Set action when window is closed
        Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /*  Methods */
    private void createAndDisplayGUI(){

        //Set GUI
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createMenuBar();
        createMainPanel();  // main container.
        createToolbar();    //Drawing tools
        createColorPalette();
        createFileChooser();
        //Status Bar todo


        //Add main panel to the main frame
        this.getContentPane().add(mainPanel,BorderLayout.CENTER);

        //Display the window
        repaint();
        setVisible(true);

    }

    private void createMainPanel(){

        // Create main Panel
        mainPanel = createPanel(Color.WHITE, new BorderLayout());
        // Child Panels
        createChildPanels();

        //Add child panels to the Main Panel
        mainPanel.add(pnlUp, BorderLayout.NORTH);
        mainPanel.add(pnlLeft, BorderLayout.WEST);
        mainPanel.add(pnlRight, BorderLayout.EAST);
        mainPanel.add(pnlDown, BorderLayout.SOUTH);
        mainPanel.add(pnlDisplay,BorderLayout.CENTER);
    }

    private void createMenuBar() {
        //Menu Bar
        menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        menuBar.setBackground(Color.WHITE);
//        menuBar.setPreferredSize(new Dimension(WIDTH,20));

        //File Menu
        menuFile = new JMenu("File");

        //Menu Items
        New = new JMenuItem("New");
        Open = new JMenuItem("Open");
        Save = new JMenuItem("Save");
        SaveAs = new JMenuItem("Save as");
        Options = new JMenuItem("Options");
        Close = new JMenuItem("Close");

        //Add Listeners
        New.addActionListener(this);
        Open.addActionListener(this);
        Save.addActionListener(this);
        SaveAs.addActionListener(this);
        Options.addActionListener(this);
        Close.addActionListener(this);

        //Add components to the File Menu
        menuFile.add(New);
        menuFile.add(Open);
        menuFile.add(Save);
        menuFile.add(SaveAs);
        menuFile.add(Options);
        menuFile.add(Close);

        //Home Menu | Includes Palette
        menuHome = new JMenu("Home");

        //View Menu
        menuView = new JMenu("View");

        //Add Menu elements to the menu bar
        menuBar.add(menuFile);
        menuBar.add(menuHome);
        menuBar.add(menuView);

        //Add menu bar to the main window
        this.setJMenuBar(menuBar);
    }

    /**
     * Creates all the required panels
     */
    public void createChildPanels(){
        // Upper Panels
        pnlUp = createPanel(Color.RED, new BorderLayout());

        pnlUp_Up = createPanel(Color.RED, new BorderLayout());
        pnlUp_Up_Up = createPanel(Color.WHITE);
        pnlUp_Up_Down = createPanel(Color.GREEN);
        pnlUp_Up.add(pnlUp_Up_Up, BorderLayout.NORTH);
        pnlUp_Up.add(pnlUp_Up_Down, BorderLayout.SOUTH);

        pnlUp_Down = createPanel(Color.GRAY);
        pnlUp.add(pnlUp_Up, BorderLayout.NORTH);
        pnlUp.add(pnlUp_Down, BorderLayout.SOUTH);

        // Side Panels
        pnlLeft = createPanel(Color.GRAY);
        pnlRight = createPanel(Color.GRAY);

        // Button Panel
        pnlDown = createPanel(Color.BLUE, new BorderLayout());
        pnlDown_Up = createPanel(Color.GRAY);
        pnlDown_Down = createPanel(Color.RED); //Status Bar Panel
        pnlDown.add(pnlDown_Up,BorderLayout.NORTH);
        pnlDown.add(pnlDown_Down, BorderLayout.SOUTH);

        // Center Panel | Use to draw shapes
        pnlDisplay = createPanel(Color.WHITE);
//        drawArea = new DrawArea();
//        pnlDisplay.add(drawArea);
    }

    public void createFileChooser(){

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Create filtered view
        fileChooser.addChoosableFileFilter(new FileVecFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);


    }

    public void createColorPalette(){

        colorPalette = new JPanel();

        clearBtn = createButton("Clear");
        customBtn = createButton("Custom Color");

        blackBtn = createButton(Color.BLACK);
        darkGrayBtn = createButton(Color.DARK_GRAY);
        grayBtn = createButton(Color.GRAY);
        lightGrayBtn = createButton(Color.lightGray);
        whiteBtn = createButton(Color.WHITE);
        blueBtn = createButton(Color.BLUE);
        cyanBtn = createButton(Color.CYAN);
        greenBtn = createButton(Color.GREEN);
        yellowBtn = createButton(Color.YELLOW);
        orangeBtn = createButton(Color.ORANGE);
        redBtn = createButton(Color.RED);
        magentaBtn = createButton(Color.MAGENTA);

        // add to panel
        colorPalette.add(blackBtn);
        colorPalette.add(darkGrayBtn);
        colorPalette.add(grayBtn);
        colorPalette.add(lightGrayBtn);
        colorPalette.add(whiteBtn);
        colorPalette.add(blueBtn);
        colorPalette.add(cyanBtn);
        colorPalette.add(greenBtn);
        colorPalette.add(yellowBtn);
        colorPalette.add(orangeBtn);
        colorPalette.add(redBtn);
        colorPalette.add(magentaBtn);
        colorPalette.add(clearBtn);
        colorPalette.add(customBtn);

        //todo: Custom color choose as pop-up window
        // May nod need this 3 lines as can be initialize on the event handler
//        colorPicker = new JColorChooser();
//        colorPicker.setPreferredSize(new Dimension(100,225));
//        colorPicker.setPreviewPanel(new JPanel()); //replaces the preview panel with a dimensionless panel

        //add to parent panel
        //todo: be able to paint on drawing area
//        pnlUp_Down.add(colorPicker, BorderLayout.SOUTH); //hidden until popup is implemented
        pnlUp_Up_Down.add(colorPalette,BorderLayout.SOUTH);
    }

    //BEGIN HELPER METHODS | Note: todo Create an interface

    /**
     * Creates a standard JPanel
     * @param c
     * @return
     */
    private JPanel createPanel(Color c){

        //(1) Create a JPanel object and store it in a local var
        JPanel panel = new JPanel();

        //(2) Set the background colour to that passed in c
        panel.setBackground(c);

        //(3) Return the JPanel object
        return panel;
    }

    /**
     * Creates a possible Parent JPanel
     * @param c
     * @param layout
     * @return
     */
    private JPanel createPanel(Color c, LayoutManager layout){

        //(1) Create a JPanel object and store it in a local var
        JPanel panel = new JPanel();

        //(2) Set the background colour to that passed in c
        panel.setBackground(c);

        //(3) Set layout manager
        panel.setLayout(layout);
        //(4) Return the JPanel object
        return panel;
    }


    private JButton createButton(String str) {
        //(1) Create a JButton object and store it in a local var
        JButton button = new JButton();

        //(2) Set the button text to that passed in str
        button.setText(str);

        //(3) Add the frame as an actionListener
        button.addActionListener(this);

        //(4) Return the JButton object
        return button;
    }

    private JButton createButton(Color c){
        //(1) Create a JButton object and store it in a local var
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(25,25));

        //(2) Set the button text to that passed in str
        //button.setText(str);
        button.setBackground(c);

        //(3) Add the frame as an actionListener
        button.addActionListener(this);

        //(4) Return the JButton object
        return button;
    }

    /**
     * Creates a button to be used by the Toolbar
     * @param imageName name for the image file. Not file extension needed.
     * @param actionCommand Command to call a drawing method.
     * @param toolTipText Text display when hovering.
     * @param altText Text displayed if no image is found.
     * @return JButton
     */
    private JButton createToolbarButton(String imageName,
                                        String actionCommand,
                                        String toolTipText,
                                        String altText) {
        //Look for the image.
        String imgLocation = "images/"
                + imageName
                + ".gif";
        URL imageURL = Window.class.getResource(imgLocation);

        //Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);

        if (imageURL != null) { //image found
            button.setIcon(new ImageIcon(imageURL, altText));
        } else { //no image found
            button.setText(altText);
            System.err.println("Imagen not found: "
                    + imgLocation);
        }

        return button;
    }

    /**
     * Creates a Toolbar containing the drawing buttons.
     */
    private void createToolbar(){
        toolBar = new JToolBar();
        toolBar.setFloatable(false);    //fixed toolBar
        toolBar.setRollover(true);      //displays info when hovering

        //Create ToolBar
        penBtn = createToolbarButton("pen-s","PEN","Pen color","Pen-Alt");
        fillBtn = createToolbarButton("fill-s","FILL","Fill color","Fill-Alt");
        plotBtn = createToolbarButton("plot-s","PLOT","Plot","Plot-Alt");
        lineBtn =  createToolbarButton("line-s","LINE","Line","Line-Alt");
        rectangleBtn =  createToolbarButton("rectangle-s","RECTANGLE","Rectangle","Rectangle-Alt");
        ellipseBtn =  createToolbarButton("elipse-s","ELIPSE","Elipse","Elipse-Alt");
        polygonBtn =  createToolbarButton("polygon-s","POLYGON","Polygon","Polygon-Alt");

        //Add button to the toolbar
        toolBar.add(penBtn);
        toolBar.add(fillBtn);
        toolBar.add(plotBtn);
        toolBar.add(lineBtn);
        toolBar.add(rectangleBtn);
        toolBar.add(ellipseBtn);
        toolBar.add(polygonBtn);
//        toolBar.addSeparator();
        pnlUp_Up_Up.add(toolBar);
    }

    //END HELPER METHODS

    /*      EVENT LISTENERS     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == Open) {
            int returnVal = fileChooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                //todo: Use FileReader
                //This is where a real application would open the file.
                System.out.println("Opening: " + file.getName() + "." + "\n");

            } else {
                System.out.println("Open command cancelled by user." + "\n");
            }
        }

        if( e.getSource() == New) {
            System.out.println( "Menu Item 'New' was pressed. ");
            //todo: Create new File
        }
        if( e.getSource() == Save) {
            System.out.println( "Menu Item  'Save' was pressed. ");
            //todo: Save File

        }
        if( e.getSource() == SaveAs) {
            System.out.println( "Menu Item 'Save as' was pressed. ");
            //todo: Save file as 'filename'
        }
        if( e.getSource() == Options) {
            System.out.println( "Menu Item 'Options' was pressed. ");
            //todo: check out specifications before attempting this.
        }
        if( e.getSource() == Close) {
            System.out.println( "Menu Item s'Close' was pressed. ");
            //todo: Close program.
            //Show dialog to save the file
        }
        if(e.getSource() == plotBtn){
            System.out.println( "'Plot' was pressed. ");
            //todo: implement drawPlot();
        }
        if( e.getSource() == lineBtn) {
            System.out.println( "'Line' was pressed. ");
            //todo: implement drawLine();


        }
        if( e.getSource() == ellipseBtn) {
            System.out.println( "'Ellipse' was pressed. ");
            //todo: implement drawEllipse();
        }
        if( e.getSource() == rectangleBtn) {
            System.out.println( "'Rectangle' was pressed. ");
            //todo: implement drawRectangle();
        }
        if( e.getSource() == polygonBtn) {
            System.out.println( "'Polygon' was pressed. ");
            //todo: implement drawPolygon();
        }
            //todo: pallette handlers
        if( e.getSource() == polygonBtn) {
            System.out.println( "'Polygon' was pressed. ");
            //todo: implement drawPolygon();
        }



        if( e.getSource() == blackBtn) {
            // todo: penColor.setColor(blackBtn.getBackground());
        }


        if( e.getSource() == customBtn) {
            // New modal color chooser
            Color newColor = JColorChooser.showDialog(this, "Pick a color", this.getBackground());

            // if a color is picked newColor is set to the color
            // otherwise is set to null.
            if (newColor != null) {
                pnlDisplay.setBackground(newColor);
            }
        }
        if( e.getSource() == clearBtn) {
            pnlDisplay.setBackground(Color.WHITE);//Clear display panel
        }
    }

    public void stateChanged(ChangeEvent e) {
//        Color newColor = colorPicker.getColor();
//        this.setForeground(newColor);
    }

    @Override
    public void run() {
        createAndDisplayGUI();
    }

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Window("Vector Plotter"));
        // the .invokeLater(Runnable doRun) method run the doRun.run() methods on a
        // separate thread.

    }
}
