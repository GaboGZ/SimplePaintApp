package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.io.*;
import FileHandler.*;

public class Window extends JFrame implements ActionListener,Runnable {


    //Constants
    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;

    //Elements
    private JMenuBar menuBar;
    private JMenu menuFile, menuHome, menuView;
    private JMenuItem New, Open, Save, SaveAs, Options, Close;

    private JPanel pnlUp, pnlUpChildUp, pnlUpChildDown;
    private JPanel pnlLeft, pnlRight;
    private JPanel pnlDown, pnlDownChildUp, pnlBtnChildDown;
    private JPanel pnlDisplay;

    private JToolBar toolBar;

    private JButton btnPlot, btnLine, btnRectangle, btnEllipse, btnPolygon;

    private JColorChooser colorChooser;

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

        /*  Components  */

        /*  Top Menu Bar    */
        JMenuBar menuBar = createMenuBar();

        /*  Panels  */

        // Create main Panel
        JPanel mainPanel = createPanel(Color.WHITE, new BorderLayout());
        // Child Panels
        createChildPanels();

        /*      ToolBar | Drawing tools */
        createToolbar();

        //Color Chooser
        colorChooser = new JColorChooser();
        colorChooser.setPreferredSize(new Dimension(100,225));
        colorChooser.setPreviewPanel(new JPanel()); //replaces the preview panel with a dimensionless panel
        pnlUp.add(colorChooser, BorderLayout.CENTER);

        //File Chooser
        createFileChooser();

        //Status Bar todo

        //AddChild Panel to the Main Panel
        mainPanel.add(pnlUp, BorderLayout.NORTH);
        mainPanel.add(pnlLeft, BorderLayout.WEST);
        mainPanel.add(pnlRight, BorderLayout.EAST);
        mainPanel.add(pnlDown, BorderLayout.SOUTH);
        mainPanel.add(pnlDisplay,BorderLayout.CENTER);

        //Add main panel to the main frame
        this.getContentPane().add(mainPanel,BorderLayout.CENTER);
        //Add window components to the window content pane
        this.setJMenuBar(menuBar);
//        this.getContentPane().add(mainPanel);

        //Display the window
        repaint();
        setVisible(true);

    }



    private JMenuBar createMenuBar() {
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
        return menuBar;
    }

    /**
     * Creates all the required panels
     */
    public void createChildPanels(){
        // Upper Panels
        pnlUp = createPanel(Color.RED, new BorderLayout());
        pnlUpChildUp = createPanel(Color.RED);
        pnlUpChildDown = createPanel(Color.GRAY);
        pnlUp.add(pnlUpChildUp, BorderLayout.NORTH);
        pnlUp.add(pnlUpChildDown, BorderLayout.SOUTH);

        // Side Panels
        pnlLeft = createPanel(Color.GRAY);
        pnlRight = createPanel(Color.GRAY);

        // Button Panel
        pnlDown = createPanel(Color.BLUE, new BorderLayout());
        pnlDownChildUp = createPanel(Color.GRAY);
        pnlBtnChildDown = createPanel(Color.RED);
        pnlDown.add(pnlDownChildUp,BorderLayout.NORTH);
        pnlDown.add(pnlBtnChildDown, BorderLayout.SOUTH);

        // Center Panel | Use to draw shapes
        pnlDisplay = createPanel(Color.WHITE);
    }

    public void createFileChooser(){

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Create filtered view
        fileChooser.addChoosableFileFilter(new FileVecFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);


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
//        toolBar.setOrientation(SwingConstants.VERTICAL);

        //Create ToolBar
        btnPlot = createToolbarButton("plot-m","PLOT","Plot","Plot-Alt");
        btnLine =  createToolbarButton("line-m","LINE","Line","Line-Alt");
        btnRectangle =  createToolbarButton("rectangle-m","RECTANGLE","Rectangle","Rectangle-Alt");
        btnEllipse =  createToolbarButton("elipse-m","ELIPSE","Elipse","Elipse-Alt");
        btnPolygon =  createToolbarButton("polygon-m","POLYGON","Polygon","Polygon-Alt");

        //Add Action Listeners
        btnPlot.addActionListener(this);
        btnEllipse.addActionListener(this);
        btnRectangle.addActionListener(this);
        btnEllipse.addActionListener(this);
        btnPolygon.addActionListener(this);

        //Add button to the toolbar
        toolBar.add(btnPlot);
        toolBar.add(btnLine);
        toolBar.add(btnRectangle);
        toolBar.add(btnEllipse);
        toolBar.add(btnPolygon);
//        toolBar.addSeparator();
        pnlUp.add(toolBar,BorderLayout.NORTH);
    }

    //END HELPER METHODS

    //todo: Impletement abstraction for this event listeners
    @Override
    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == Open) {
            int returnVal = fileChooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
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
        if(e.getSource() == btnPlot){
            System.out.println( "'Plot' was pressed. ");
            //todo: implement drawPlot();
        }
        if( e.getSource() == btnLine) {
            System.out.println( "'Line' was pressed. ");
            //todo: implement drawLine();
        }
        if( e.getSource() == btnEllipse) {
            System.out.println( "'Ellipse' was pressed. ");
            //todo: implement drawEllipse();
        }
        if( e.getSource() == btnRectangle) {
            System.out.println( "'Rectangle' was pressed. ");
            //todo: implement drawRectangle();
        }
        if( e.getSource() == btnPolygon) {
            System.out.println( "'Polygon' was pressed. ");
            //todo: implement drawPolygon();
        }


    }

    @Override
    public void run() {
        createAndDisplayGUI();
    }

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Window("New Plotter"));
        // the .invokeLater(Runnable doRun) method run the doRun.run() methods on a
        // separate thread.

    }
}
