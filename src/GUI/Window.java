package GUI;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.io.File;


public class Window extends JFrame implements ActionListener,Runnable {


    //Constants
    private static final int WIDTH = 600;
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

    public static JButton penBtn;
    public JButton fillBtn;
    public JButton clearBtn;
    public JButton customBtn;
    public JButton blackBtn, blueBtn, greenBtn, darkGrayBtn, lightGrayBtn;
    public JButton cyanBtn, redBtn, magentaBtn, whiteBtn, grayBtn, orangeBtn, yellowBtn;
    public JPanel colorPalette;

    private JFileChooser fileChooser;

    JPanel statusBar;
    JLabel coordLabel, commmandSelectedLabel, zoomLabel;

    private static String currentAction = "PEN";
    private JButton currentPenColorBtn;
    private JLabel currentPenColorLabel;
    static JCheckBox fillCheckBox;
    private JLabel checkBoxLabel;
    private JButton currentFillColorBtn;
    private JLabel currentFillColorLabel;

    //Globals that manage the state of the program
    private static Color penColor = Color.BLACK;
    private static Color fillColor = Color.WHITE;
    static boolean penClicked = true;
    static boolean fillClicked = false;
    static boolean penColorChanged = false;
    static boolean fillColorChanged = false;


    @Override
    public void run() {
        createAndDisplayGUI();
    }

    private void createAndDisplayGUI(){

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame Window = new JFrame();

        //Set GUI
        setTitle("CAB302 | Java Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
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
        palettePanel = createPanel(Color.WHITE, new BorderLayout());
        pnlUp_Up.add(toolsPanel, BorderLayout.NORTH);
        pnlUp_Up.add(palettePanel, BorderLayout.SOUTH);

        pnlUp_Down = createPanel(Color.GRAY); //Surrounds Canvas
        pnlUp.add(pnlUp_Up, BorderLayout.NORTH);
        pnlUp.add(pnlUp_Down, BorderLayout.SOUTH);

        // Side Panels
        pnlLeft = createPanel(Color.GRAY);//Surrounds Canvas
        pnlRight = createPanel(Color.GRAY);//Surrounds Canvas

        // Button Panel
        pnlDown = createPanel(Color.GRAY, new BorderLayout());
        pnlDown_Up = createPanel(Color.GRAY);//Surrounds Canvas
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
        toolBar.setBorder(BorderFactory.createTitledBorder("Drawing Tools"));
        toolBar.setFloatable(false);    //fixed toolBar
        toolBar.setRollover(true);      //displays info when hovering

        //Create ToolBar
        clearBtn = createButton("clear-s","CLEAR","Clear","Clear-Alt");
        penBtn = createButton("pen-s","PEN","Pen color","Pen-Alt");
        fillBtn = createButton("fill-s","FILL","Fill color","Fill-Alt");
        fillCheckBox = new JCheckBox();

        checkBoxLabel = new JLabel();
        checkBoxLabel.setLabelFor(fillCheckBox);
        checkBoxLabel.setText("Fill Shape?");
        checkBoxLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        plotBtn = createButton("plot-s","PLOT","Plot","Plot-Alt");
        lineBtn =  createButton("line-s","LINE","Line","Line-Alt");
        rectangleBtn =  createButton("rectangle-s","RECTANGLE","Rectangle","Rectangle-Alt");
        ellipseBtn =  createButton("ellipse-s","ELLIPSE","Ellipse","Ellipse-Alt");
        polygonBtn =  createButton("polygon-s","POLYGON","Polygon","Polygon-Alt");

        //Add button to the toolbar
        toolBar.add(penBtn);
        toolBar.add(fillBtn);
        toolBar.add(fillCheckBox);
        toolBar.add(checkBoxLabel);

        toolBar.add(plotBtn);
        toolBar.add(lineBtn);
        toolBar.add(rectangleBtn);
        toolBar.add(ellipseBtn);
        toolBar.add(polygonBtn);
        toolBar.add(clearBtn);
        toolsPanel.add(toolBar);
    }


    public void createColorPalette(){

        colorPalette = createPanel(Color.WHITE, new BorderLayout());

        JPanel pLeft = createPanel(Color.WHITE, new BorderLayout());
        pLeft.setBorder(BorderFactory.createTitledBorder("Current Colors"));
        pLeft.setPreferredSize(new Dimension(150,100));

        JPanel pLeft_up = new JPanel();
        JPanel pLeft_down = new JPanel();

        JPanel pCenter = createPanel(Color.WHITE, new BorderLayout());
        pCenter.setBorder(BorderFactory.createTitledBorder("Color Palette"));
        pCenter.setPreferredSize(new Dimension(400,100));
        JPanel pCenter_up = new JPanel();
        JPanel pCenter_down = new JPanel();

        JPanel pRight = createPanel(Color.WHITE, new BorderLayout());
        pRight.setBorder(BorderFactory.createTitledBorder("Custom Color"));
        pRight.setPreferredSize(new Dimension(150,100));

        currentPenColorBtn = createColorButton(penColor);
        currentPenColorBtn.setEnabled(false);
        currentPenColorLabel = new JLabel("Pen");
        currentPenColorLabel.setLabelFor(currentPenColorBtn);

        currentFillColorBtn = createColorButton(fillColor);
        currentFillColorBtn.setEnabled(false);
        currentFillColorLabel = new JLabel("Fill");
        currentFillColorLabel.setLabelFor(currentFillColorBtn);

        // Colored Buttons
        blackBtn = createColorButton(Color.BLACK);
        darkGrayBtn = createColorButton(Color.DARK_GRAY);
        grayBtn = createColorButton(Color.GRAY);
        lightGrayBtn = createColorButton(Color.LIGHT_GRAY);
        whiteBtn = createColorButton(Color.WHITE);
        blueBtn = createColorButton(Color.BLUE);
        cyanBtn = createColorButton(Color.CYAN);
        greenBtn = createColorButton(Color.GREEN);
        yellowBtn = createColorButton(Color.YELLOW);
        orangeBtn = createColorButton(Color.ORANGE);
        redBtn = createColorButton(Color.RED);
        magentaBtn = createColorButton(Color.MAGENTA);
        customBtn = createButton("custom-s","CUSTOM","Custom Color","Custom-Alt");
        customBtn.setPreferredSize(new Dimension(50,50));

        // Add components to panel
        pLeft_up.add(currentPenColorBtn);
        pLeft_up.add(currentPenColorLabel);
        pLeft_down.add(currentFillColorBtn);
        pLeft_down.add(currentFillColorLabel);

        pCenter_up.add(blackBtn);
        pCenter_up.add(darkGrayBtn);
        pCenter_up.add(grayBtn);
        pCenter_up.add(lightGrayBtn);
        pCenter_up.add(whiteBtn);
        pCenter_up.add(blueBtn);
        pCenter_down.add(cyanBtn);
        pCenter_down.add(greenBtn);
        pCenter_down.add(yellowBtn);
        pCenter_down.add(orangeBtn);
        pCenter_down.add(redBtn);
        pCenter_down.add(magentaBtn);

        pLeft.add(pLeft_up, BorderLayout.NORTH);
        pLeft.add(pLeft_down, BorderLayout.SOUTH);
        pCenter.add(pCenter_up, BorderLayout.NORTH);
        pCenter.add(pCenter_down, BorderLayout.SOUTH);
        pRight.add(customBtn);

        colorPalette.add(pLeft, BorderLayout.WEST);
        colorPalette.add(pCenter, BorderLayout.CENTER);
        colorPalette.add(pRight, BorderLayout.EAST);
        palettePanel.add(colorPalette, BorderLayout.CENTER);
    }


    public void createStatusBar(){

        statusBar = createPanel(Color.WHITE, new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        coordLabel = new JLabel();

        coordLabel.setText("(x,y)= " );

        commmandSelectedLabel = new JLabel();
        commmandSelectedLabel.setText("Current Command: " + getCurrentAction());
        commmandSelectedLabel.setHorizontalTextPosition(SwingConstants.CENTER);

        zoomLabel = new JLabel();
        zoomLabel.setText("Zoom: 100%");
        zoomLabel.setBackground(Color.WHITE);

        statusBar.add(coordLabel, BorderLayout.WEST);
//        statusBar.add(new JSeparator(SwingConstants.VERTICAL));
        statusBar.add(commmandSelectedLabel,BorderLayout.CENTER);
//        statusBar.add(new JSeparator(SwingConstants.VERTICAL));
        statusBar.add(zoomLabel, BorderLayout.EAST);

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
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
    private JButton createButton(String imageName,String actionCommand, String toolTipText,String altText) {
        //Look for the image.
        String imgLocation = "images/" + imageName + ".png";
        URL imageURL = Window.class.getResource(imgLocation);

        //Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText(toolTipText);
        button.setMaximumSize(new Dimension(75,75));

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

                if( isDrawingCommand(actionCommand)){
                    currentAction = actionCommand;
                }

                commmandSelectedLabel.setText("Command: " + getCurrentAction());
//                System.out.println("Current Command: " + getCurrentAction());

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

                    Object[] options = {"Ok", "Cancel"};
                    int clearDrawings = JOptionPane.showOptionDialog( pnlDisplay,
                            "This action will clear all the drawings and cannot be undone.\n"
                                    + "Do you want to proceed?",
                            "Clear Drawings",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            options,
                            options[1]);

                    if( clearDrawings == 0){
                        Canvas.shapes.clear();
                        Canvas.shapePenColor.clear();
                        Canvas.shapeFillColor.clear();
                        Canvas.shapeFilled.clear();
                        repaint();
                    }
                }

                if( e.getSource() == penBtn ){
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
     * @param colorButtonClicked The color button that triggered the event.
     * @return pc Color of the pen.
     */
    private Color setPenColor(JButton colorButtonClicked){

        Color pc = getCurrentPenColor();

        if(penClicked){
            pc = colorButtonClicked.getBackground();
            currentPenColorBtn.setBackground(pc);
            penColorChanged = true;
        }else{
            penColorChanged = false;
        }
        return pc;
    }

    /**
     * Sets the color of the fill if an only the Fill Button has been clicked. Otherwise, leaves
     * the fill color as it is.
     * @param colorButtonClicked The color button that triggered the event.
     * @return fc - Color of the fill.
     */
    private Color setFillColor(JButton colorButtonClicked) {

        Color fc = getCurrentFillColor();

        if (fillClicked){
            fc = colorButtonClicked.getBackground();
            currentFillColorBtn.setBackground(fc);
            fillColorChanged = true;
        }else{
            fillColorChanged = false;
        }
        return fc;
    }

    public static String getCurrentAction(){
        return currentAction;
    }

    /**
     * Returns true if the current command is either "PLOT","LINE","RECTANGLE","ELLIPSE" or "POLYGON"
     * @param action
     * @return boolean - true or false
     */
    public static boolean isDrawingCommand(String action){
        if (action != "CLEAR" && action != "PEN" && action != "FILL"){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Gets the current pen color
     * @return Color - the current Pen color
     */
    public static Color getCurrentPenColor(){
        return penColor;
    }

    /**
     * Gets the current fill color
     * @return Color - the current fill color
     */
    public static Color getCurrentFillColor(){
        return fillColor;
    }

}
