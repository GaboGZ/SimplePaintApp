package GUI;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class Window extends JFrame implements ActionListener,Runnable {


    //Constants
    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;

    //Elements
    private JMenuBar menuBar;
    private JMenu menuFile, menuHome, menuView;
    private JMenuItem New, Open, Save, SaveAs, Options, Close;

    public JPanel mainPanel;
    private JPanel pnlUp, pnlUp_Up, pnlUp_Down, toolsPanel, palettePanel;
    private JPanel pnlLeft, pnlRight;
    private JPanel pnlDown, pnlDown_Up, pnlDown_Down;
    private JComponent pnlDisplay;
//    private DrawArea drawArea;

    private JToolBar toolBar;

    private JButton plotBtn, lineBtn, rectangleBtn, ellipseBtn, polygonBtn;

    public JButton penBtn, fillBtn, clearBtn, customBtn;
    public JButton blackBtn, blueBtn, greenBtn, darkGrayBtn, lightGrayBtn;
    public JButton cyanBtn, redBtn, magentaBtn, whiteBtn, grayBtn, orangeBtn, yellowBtn;
    public JPanel colorPalette;

    private JFileChooser fileChooser;

    JPanel statusBar;
    JLabel coor, comSelected, zoom;

    private static String currentAction = "PEN";
    private JButton currentPenColorBtn;
    private JLabel currentPenColorLabel;
    private JRadioButton fillRadioButton;
    private JButton currentFillColorBtn;
    private JLabel currentFillColorLabel;
    private static Color penColor = Color.BLACK;
    private static Color fillColor = Color.WHITE;
    boolean penClicked = true;
    boolean fillClicked = false;




    public static void main(String[] args){

        SwingUtilities.invokeLater(new Window());
        // the .invokeLater(Runnable doRun) method run the doRun.run() methods on a
        // separate thread.

    }

    @Override
    public void run() {
        createAndDisplayGUI();
    }

    /**
     * Constructs the main window by using a JFrame
     */
    public Window(){

        //Set default look and feel
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create window
        JFrame Window = new JFrame();
        Window.setTitle("CAB302 | Java Project");

        //Set currentAction when window is closed
        Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private void createAndDisplayGUI(){

        //Set GUI
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Add components
        createMenuBar();
        createMainPanel();      // main container.
        createToolbar();        //Drawing tools
        createColorPalette();
        createStatusBar();


        //Add main panel to the main frame
        this.getContentPane().add(mainPanel);

        //Display the window
        repaint();
        setVisible(true);

    }

    private void createMainPanel(){

        // Create main Panel
        mainPanel = createPanel(Color.WHITE, new BorderLayout());
        // Child Panels
        createChildPanels();
    }

    /**
     * Creates all the required panels
     */
    public void createChildPanels(){
        // Upper Panels
        pnlUp = createPanel(Color.WHITE, new BorderLayout());

        pnlUp_Up = createPanel(Color.WHITE, new BorderLayout());
        toolsPanel = createPanel(Color.WHITE);
        palettePanel = createPanel(Color.WHITE);
        pnlUp_Up.add(toolsPanel, BorderLayout.NORTH);
        pnlUp_Up.add(palettePanel, BorderLayout.SOUTH);

        pnlUp_Down = createPanel(Color.GRAY);
        pnlUp.add(pnlUp_Up, BorderLayout.NORTH);
        pnlUp.add(pnlUp_Down, BorderLayout.SOUTH);

        // Side Panels
        pnlLeft = createPanel(Color.GRAY);
        pnlRight = createPanel(Color.GRAY);

        // Button Panel
        pnlDown = createPanel(Color.GRAY, new BorderLayout());
        pnlDown_Up = createPanel(Color.GRAY);
        pnlDown_Down = createPanel(Color.WHITE, new BorderLayout()); //Status Bar Panel
        pnlDown.add(pnlDown_Up,BorderLayout.NORTH);
        pnlDown.add(pnlDown_Down, BorderLayout.SOUTH);

        pnlDisplay = new Canvas(); // Center Panel | Use to draw shapes


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

        //Add Listeners //todo: can I implement listeners here?
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

        //Set up file chooser
        createFileChooser();
    }


    public void createFileChooser(){

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Create filtered view
        fileChooser.addChoosableFileFilter(new FileVecFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
    }

    /**
     * Creates a Toolbar containing the drawing buttons.
     */
    private void createToolbar(){
        toolBar = new JToolBar();
        toolBar.setFloatable(false);    //fixed toolBar
        toolBar.setRollover(true);      //displays info when hovering

        //Create ToolBar
        clearBtn = createButton("clear-s","CLEAR","Clear","Clear-Alt");
        penBtn = createButton("pen-s","PEN","Pen color","Pen-Alt");
        fillBtn = createButton("fill-s","FILL","Fill color","Fill-Alt");
        fillRadioButton = new JRadioButton();
        plotBtn = createButton("plot-s","PLOT","Plot","Plot-Alt");
        lineBtn =  createButton("line-s","LINE","Line","Line-Alt");
        rectangleBtn =  createButton("rectangle-s","RECTANGLE","Rectangle","Rectangle-Alt");
        ellipseBtn =  createButton("ellipse-s","ELLIPSE","Ellipse","Ellipse-Alt");
        polygonBtn =  createButton("polygon-s","POLYGON","Polygon","Polygon-Alt");

        //Add button to the toolbar
        toolBar.add(penBtn);
        toolBar.add(fillBtn);
        toolBar.add(fillRadioButton);

        toolBar.add(plotBtn);
        toolBar.add(lineBtn);
        toolBar.add(rectangleBtn);
        toolBar.add(ellipseBtn);
        toolBar.add(polygonBtn);
        toolBar.add(clearBtn);
        toolsPanel.add(toolBar);
    }


    public void createColorPalette(){

        colorPalette = new JPanel();

        currentPenColorBtn = createColorButton(penColor);
        currentPenColorLabel = new JLabel("Current Pen Color");
        currentPenColorLabel.setLabelFor(currentPenColorBtn);

        currentFillColorBtn = createColorButton(fillColor);
        currentFillColorLabel = new JLabel("Current Fill Color");
        currentFillColorLabel.setLabelFor(currentFillColorBtn);

        blackBtn = createColorButton(Color.BLACK);
        darkGrayBtn = createColorButton(Color.DARK_GRAY);
        grayBtn = createColorButton(Color.GRAY);
        lightGrayBtn = createColorButton(Color.lightGray);
        whiteBtn = createColorButton(Color.WHITE);
        blueBtn = createColorButton(Color.BLUE);
        cyanBtn = createColorButton(Color.CYAN);
        greenBtn = createColorButton(Color.GREEN);
        yellowBtn = createColorButton(Color.YELLOW);
        orangeBtn = createColorButton(Color.ORANGE);
        redBtn = createColorButton(Color.RED);
        magentaBtn = createColorButton(Color.MAGENTA);
        customBtn = createButton("custom-s","CUSTOM","Custom Color","Custom-Alt");

        // add to panel
        colorPalette.add(currentPenColorBtn);
        colorPalette.add(currentPenColorLabel);
        colorPalette.add(currentFillColorBtn);
        colorPalette.add(currentFillColorLabel);
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
        colorPalette.add(customBtn);
        palettePanel.add(colorPalette,BorderLayout.SOUTH);
    }


    public void createStatusBar(){

        statusBar = createPanel(Color.WHITE, new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        coor = new JLabel();
        coor.setText("(x,y) = ");

        comSelected = new JLabel();
        comSelected.setText("Current Command: " + currentAction);
        comSelected.setHorizontalTextPosition(SwingConstants.CENTER);

        zoom = new JLabel();
        zoom.setText("Zoom: 100%");
        zoom.setBackground(Color.WHITE);

        statusBar.add(coor, BorderLayout.WEST);
//        statusBar.add(new JSeparator(SwingConstants.VERTICAL));
        statusBar.add(comSelected,BorderLayout.CENTER);
//        statusBar.add(new JSeparator(SwingConstants.VERTICAL));
        statusBar.add(zoom, BorderLayout.EAST);

        pnlDown_Down.add(statusBar);

    }

    //BEGIN HELPER METHODS | Note: todo Create an interface

    /**
     * Creates a standard JPanel
     * @param c
     * @return
     */
    private JPanel createPanel(Color c){
        JPanel panel = new JPanel();
        panel.setBackground(c);
        return panel;
    }

    /**
     * Creates a possible Parent JPanel
     * @param c
     * @param layout
     * @return
     */
    private JPanel createPanel(Color c, LayoutManager layout){
        JPanel panel = new JPanel();
        panel.setBackground(c);
        panel.setLayout(layout);
        return panel;
    }



    /**
     * Create buttons for the color palette
     * @param color
     * @return a colored button
     */
    private JButton createColorButton(Color color){

        JButton button = new JButton();
        button.setPreferredSize(new Dimension(25,25));
        button.setBackground(color);

        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if(e.getSource() == blackBtn) {
                    penColor = setPenColor(blackBtn);
                    fillColor = setFillColor(blackBtn);
                }else if( e.getSource() == darkGrayBtn){
                    penColor = setPenColor(darkGrayBtn);
                    fillColor = setFillColor(darkGrayBtn);
                }else if( e.getSource() == grayBtn){
                    penColor = setPenColor(grayBtn);
                    fillColor = setFillColor(grayBtn);
                }else if( e.getSource() == lightGrayBtn) {
                    penColor = setPenColor(lightGrayBtn);
                    fillColor = setFillColor(lightGrayBtn);
                }else if( e.getSource() == whiteBtn){
                    penColor = setPenColor(whiteBtn);
                    fillColor = setFillColor(whiteBtn);
                }else if( e.getSource() == blueBtn){
                    penColor = setPenColor(blueBtn);
                    fillColor = setFillColor(blueBtn);
                }else if( e.getSource() == cyanBtn) {
                    penColor = setPenColor(cyanBtn);
                    fillColor = setFillColor(cyanBtn);
                }else if( e.getSource() == greenBtn){
                    penColor = setPenColor(greenBtn);
                    fillColor = setFillColor(greenBtn);
                }else if( e.getSource() == yellowBtn){
                    penColor = setPenColor(yellowBtn);
                    fillColor = setFillColor(yellowBtn);
                }else if(e.getSource() == orangeBtn){
                    penColor = setPenColor(orangeBtn);
                    fillColor = setFillColor(orangeBtn);
                }else if( e.getSource() == redBtn){
                    penColor = setPenColor(redBtn);
                    fillColor = setFillColor(redBtn);
                }else if(e.getSource() == magentaBtn) {
                    penColor = setPenColor(magentaBtn);
                    fillColor = setFillColor(magentaBtn);
                }else if(e.getSource() == currentPenColorBtn){
                    penClicked = true;
                    fillClicked = false;
                }else if(e.getSource() == currentFillColorBtn){
                    penClicked = false;
                    fillClicked = true;
                }
            }
        });

        return button;
    }



    /**
     * @param imageName name for the image file. Not file extension needed.
     * @param actionCommand Command to call a drawing method.
     * @param toolTipText Text display when hovering.
     * @param altText Text displayed if no image is found.
     * @return JButton
     */
    private JButton createButton(String imageName,String actionCommand, String toolTipText, String altText) {
        //Look for the image.
        String imgLocation = "images/" + imageName + ".gif";
        URL imageURL = Window.class.getResource(imgLocation);

        //Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);

        if (imageURL != null) { //image found
            button.setIcon(new ImageIcon(imageURL, altText));
        } else { //no image found
            button.setText(altText);
            System.err.println("Image not found: " + imgLocation);
        }

        //Add listener and event handlers
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentAction = actionCommand;
                comSelected.setText("Command: " + currentAction);
                System.out.println("Current Command: " + currentAction);

                if( e.getSource() == customBtn) {
                    // Display modal color picker
                    Color newColor = JColorChooser.showDialog(mainPanel, "Pick a color", mainPanel.getBackground());

                    // if a color is picked newColor is set to the picked color
                    // otherwise is set to null.
                    if (newColor != null) {
                        //check if pen or fill is being used.
                        if ( penClicked){
                            penColor = newColor;
                            currentPenColorBtn.setBackground(penColor);
                        }
                        if ( fillClicked){
                            fillColor = newColor;
                            currentFillColorBtn.setBackground(fillColor);
                        }
                    }
                }
                if( e.getSource() == clearBtn) {
                    // todo: Clear drawing
                }

                if( e.getSource() == penBtn){
                    penClicked = true;
                    fillClicked = false;
                }
                if (e.getSource() == fillBtn){
                    penClicked = false;
                    fillClicked = true;
                }


            }
        });

        return button;
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
    }

    /**
     * Sets the color of the pen if an only the Pen Button has been clicked. Otherwise, leaves
     * the pen color as it is.
     * @param buttonClicked The color button that triggered the event.
     * @return pc Color of the pen.
     */
    private Color setPenColor(JButton buttonClicked){

        Color pc = penColor;

        if(penClicked && (!fillClicked)){
            penColor = buttonClicked.getBackground();
            currentPenColorBtn.setBackground(penColor);
            return pc;
        }
        return pc;
    }

    /**
     * Sets the color of the fill if an only the Fill Button has been clicked. Otherwise, leaves
     * the fill color as it is.
     * @param buttonClicked The color button that triggered the event.
     * @return fc - Color of the fill.
     */
    private Color setFillColor(JButton buttonClicked) {

        Color fc = fillColor;

        if (!penClicked && fillClicked){
            fillColor = buttonClicked.getBackground();
            currentFillColorBtn.setBackground(fillColor);
        }

        return fc;
    }

    public static String getCurrentAction(){
        return currentAction;
    }

    public static Color getCurrentPenColor(){
        return penColor;
    }

    public static Color getCurrentFillColor(){
        return fillColor;
    }
}
