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
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.net.URL;
import Listeners.*;

/**
 * This class contains all the Java Swing elements that create the GUI.
 */
public class VectorImagePlotter extends JFrame implements Runnable {


    //Constants
    private static final int MIN_WIDTH = 675;
    private static final int MIN_HEIGHT = 675;
    private static final Color PANEL_BACKGROUND = Color.WHITE;
    private static final Color CANVAS_BACKGROUND = Color.GRAY;




    private static JPanel mainPanel;
    private JPanel toolsPanel;
    private JPanel palettePanel;
    private static Canvas pnlDisplay;
    private EventsHandler eventsHandler = new EventsHandler();

    //Public Elements and Globals that manage the state of the program
    public static JMenuItem New, Open, Save, SaveAs, Exit;
    public static JButton plotBtn;
    public static JButton lineBtn;
    public static JButton rectangleBtn;
    public static JButton ellipseBtn;
    public static JButton polygonBtn;
    public static JButton undoBtn;
    public static JButton redoBtn;

    public static JButton penBtn;
    public static JButton fillBtn;
    public static JButton clearBtn;
    public static JButton customBtn;
    public static JButton blackBtn, blueBtn, greenBtn, darkGrayBtn, lightGrayBtn;
    public static JButton cyanBtn, redBtn, magentaBtn, whiteBtn, grayBtn, orangeBtn, yellowBtn;

    public JPanel statusBar;
    public static JLabel coordinatesLabel;
    public static JLabel commandSelectedLabel;

    public static String currentAction = "PEN";
    public static JButton currentPenColorBtn;
    public static JCheckBox fillCheckBox;
    public static JLabel checkBoxLabel;
    public static JButton currentFillColorBtn;

    public static Color penColor = Color.BLACK;
    public static Color fillColor = Color.WHITE;
    public static boolean penClicked = true;
    public static boolean fillClicked = false;
    static boolean penColorChanged = false;
    static boolean fillColorChanged = false;

    /**
     * Method implement from the Runnable interface that allows the GUI creation to run on a
     * separate thread.
     */
    @Override
    public void run() {
        createAndDisplayGUI();
    }

    /**
     * Create the GUI with all the necessary elements.
     */
    private void createAndDisplayGUI() {

        setDefaultLookAndFeelDecorated(true);

        //Set GUI
        setTitle("CAB302 | Java Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        this.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setLayout(new BorderLayout());

        //Add components
        createMainPanel();      // Main container.
        createMenuBar();        // Top Menu
        createToolbar();        // Drawing tools
        createColorPalette();   // Color tools
        createStatusBar();


        //Add main panel to the main frame
        this.getContentPane().add(mainPanel);

        //Display the window
        repaint();
        setVisible(true);

    }

    /**
     * Creates the main panel that contains all other panels.
     */
    private void createMainPanel() {
        // Create main Panel
        mainPanel = createPanel(Color.WHITE, new BorderLayout());
        // Child Panels
        createChildPanels();
    }

    /**
     * Creates all the required child panels
     */
    private void createChildPanels() {
        // Upper Panels
        JPanel pnlUp = createPanel(PANEL_BACKGROUND, new GridLayout(2, 1));
        toolsPanel = createPanel(PANEL_BACKGROUND);
        palettePanel = createPanel(PANEL_BACKGROUND, new FlowLayout());
        pnlUp.add(toolsPanel);
        pnlUp.add(palettePanel);

        // Center Panel
        JPanel pnlCenter = createPanel(PANEL_BACKGROUND, new BorderLayout());
        pnlDisplay = new Canvas(); // Center Panel | Used to draw shapes
        JPanel pnlCenter_Up = createPanel(CANVAS_BACKGROUND);//Surrounds Canvas
        JPanel pnlCenter_Down = createPanel(CANVAS_BACKGROUND);//Surrounds Canvas
        JPanel pnlCenter_Left = createPanel(CANVAS_BACKGROUND);//Surrounds Canvas
        JPanel pnlCenter_Right = createPanel(CANVAS_BACKGROUND);//Surrounds Canvas

        pnlCenter.add(pnlDisplay, BorderLayout.CENTER);
        pnlCenter.add(pnlCenter_Up, BorderLayout.NORTH);
        pnlCenter.add(pnlCenter_Down, BorderLayout.SOUTH);
        pnlCenter.add(pnlCenter_Left, BorderLayout.WEST);
        pnlCenter.add(pnlCenter_Right, BorderLayout.EAST);

        // Button Panel
        JPanel pnlDown = createStatusBar();

        //Add child panels to the Main Panel
        mainPanel.add(pnlUp, BorderLayout.NORTH);
        mainPanel.add(pnlCenter, BorderLayout.CENTER);
        mainPanel.add(pnlDown, BorderLayout.SOUTH);

    }

    /**
     * Create the top menu bar and necessary components
     */
    private void createMenuBar() {
        //Menu Bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        menuBar.setBackground(Color.WHITE);
        menuBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        //File Menu
        JMenu fileMenu = new JMenu("File");
        New = new JMenuItem("New (Ctrl+N)");
        Open = new JMenuItem("Open (Ctrl+O)");
        Save = new JMenuItem("Save (Ctrl+S)");
        SaveAs = new JMenuItem("Save as (Ctrl+Shift+S)");
        Exit = new JMenuItem("Exit (Ctrl+Q)");

        New.addActionListener(eventsHandler);
        Open.addActionListener(eventsHandler);
        Save.addActionListener(eventsHandler);
        SaveAs.addActionListener(eventsHandler);
        Exit.addActionListener(eventsHandler);

        //Add Menu elements to the menu bar
        menuBar.add(fileMenu);
        fileMenu.add(New);
        fileMenu.add(Open);
        fileMenu.add(Save);
        fileMenu.add(SaveAs);
        fileMenu.add(Exit);

        //Add menu bar to the main window
        this.setJMenuBar(menuBar);
    }

    /**
     * Creates an instance of JToolbar containing the drawing buttons.
     */
    private void createToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout());
        toolBar.setBorder(BorderFactory.createTitledBorder("Drawing Tools"));
        toolBar.setFloatable(false);    //fixed toolBar
        toolBar.setRollover(true);      //displays info when hovering

        //Create ToolBar buttons
        clearBtn = createButton("clear-s", "CLEAR", "(Ctrl+Delete) Clear", "Clear-Alt");
        penBtn = createButton("pen-s", "PEN", "(Ctrl+D) Pen color", "Pen-Alt");
        fillBtn = createButton("fill-s", "FILL", "(Ctrl+F) Fill color", "Fill-Alt");

        fillCheckBox = new JCheckBox();
        fillCheckBox.addActionListener(eventsHandler);
        fillCheckBox.addKeyListener(eventsHandler);
        fillCheckBox.addItemListener(eventsHandler);
        checkBoxLabel = new JLabel();
        checkBoxLabel.setToolTipText("(Ctrl+Shift+F) Fill shape");
        checkBoxLabel.setLabelFor(fillCheckBox);
        checkBoxLabel.setText("Fill OFF");
        checkBoxLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkBoxLabel.addMouseListener(eventsHandler);

        plotBtn = createButton("plot-s", "PLOT", "(Ctrl+.) Plot", "Plot-Alt");
        lineBtn = createButton("line-s", "LINE", "(Ctrl+L) Line", "Line-Alt");
        rectangleBtn = createButton("rectangle-s", "RECTANGLE", "(Ctrl+R) Rectangle", "Rectangle-Alt");
        ellipseBtn = createButton("ellipse-s", "ELLIPSE", "(Ctrl+E) Ellipse", "Ellipse-Alt");
        polygonBtn = createButton("polygon-s", "POLYGON", "(Ctrl+P)Polygon", "Polygon-Alt");

        undoBtn = createButton("undo-s", "UNDO", "(Ctrl+Z) Undo the last drawing", "Undo-Alt");
        redoBtn = createButton("redo-s", "REDO", "(Ctrl+Shift+Z) Redo the last removed drawing", "Redo-Alt");

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
        toolBar.add(undoBtn);
        toolBar.add(redoBtn);
        toolBar.add(clearBtn);
        toolsPanel.add(toolBar);
    }

    /**
     * Create a palette of basic colors.
     */
    private void createColorPalette() {

        JPanel colorPalette = createPanel(PANEL_BACKGROUND, new GridLayout(1, 3));

        JPanel pLeft = createPanel(PANEL_BACKGROUND, new GridLayout(2, 1));
        pLeft.setBorder(BorderFactory.createTitledBorder("Current Colors"));

        JPanel pLeft_up = new JPanel();
        JPanel pLeft_down = new JPanel();

        JPanel pCenter = createPanel(PANEL_BACKGROUND, new GridLayout(2, 1));
        pCenter.setBorder(BorderFactory.createTitledBorder("Color Palette"));
        JPanel pCenter_up = new JPanel();
        JPanel pCenter_down = new JPanel();

        JPanel pRight = createPanel(PANEL_BACKGROUND);
        pRight.setBorder(BorderFactory.createTitledBorder("Custom Color"));


        currentPenColorBtn = createColorButton(penColor);
        currentPenColorBtn.setEnabled(false);
        JLabel currentPenColorLabel = new JLabel("Pen");
        currentPenColorLabel.setLabelFor(currentPenColorBtn);

        currentFillColorBtn = createColorButton(fillColor);
        currentFillColorBtn.setEnabled(false);
        JLabel currentFillColorLabel = new JLabel("Fill");
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
        customBtn = createButton("custom-s", "CUSTOM", "(Ctrl+Shift+C) Custom Color", "Custom-Alt");
        customBtn.setPreferredSize(new Dimension(140, 50));
        customBtn.setMinimumSize(new Dimension(140, 50));


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

        pLeft.add(pLeft_up);
        pLeft.add(pLeft_down);
        pCenter.add(pCenter_up);
        pCenter.add(pCenter_down);
        pRight.add(customBtn);

        colorPalette.add(pLeft);
        colorPalette.add(pCenter);
        colorPalette.add(pRight);
        palettePanel.add(colorPalette);
    }

    /**
     * Creates the status bar.
     */
    private JPanel createStatusBar() {

        statusBar = createPanel(PANEL_BACKGROUND, new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        coordinatesLabel = new JLabel();
        coordinatesLabel.setText("(x,y): (0.0,0.0)     ");

        commandSelectedLabel = new JLabel();
        commandSelectedLabel.setText("Current Command: " + getCurrentAction());
        commandSelectedLabel.setHorizontalTextPosition(SwingConstants.CENTER);

        JLabel zoomLabel = new JLabel();
        zoomLabel.setText("Zoom: 100%");
        zoomLabel.setHorizontalTextPosition(SwingConstants.RIGHT);

        statusBar.add(coordinatesLabel, BorderLayout.WEST);
        statusBar.add(commandSelectedLabel, BorderLayout.CENTER);
        statusBar.add(zoomLabel, BorderLayout.EAST);

        return statusBar;

    }

    /**
     * Creates a standard JPanel and set the background color to the given color.
     * @param c the desired background color for the panel.
     * @return a JPanel
     */
    private JPanel createPanel(Color c) {
        JPanel panel = new JPanel();
        panel.setBackground(c);
        return panel;
    }

    /**
     * Creates a possible Parent JPanel. Sets the LayoutManager to the given layout.
     *
     * @param c      Color
     * @param layout LayoutManager
     * @return a Jpanel
     */
    private JPanel createPanel(Color c, LayoutManager layout) {
        JPanel panel = new JPanel();
        panel.setBackground(c);
        panel.setLayout(layout);
        return panel;
    }

    /**
     * Create buttons for the color palette
     * @param color The desired color for the button
     * @return a colored button
     */
    private JButton createColorButton(Color color) {

        JButton button = new JButton();
        button.setPreferredSize(new Dimension(20, 20));
        button.setActionCommand("CHANGE_COLOR");
        button.setBackground(color);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(eventsHandler);
        button.addKeyListener(eventsHandler);
        return button;
    }


    /**
     * @param imageName     name for the image file. Not file extension needed.
     * @param actionCommand Command to call a drawing method.
     * @param toolTipText   Text display when hovering.
     * @param altText       Text displayed if no image is found.
     * @return JButton
     */
    private JButton createButton(String imageName, String actionCommand, String toolTipText, String altText) {

        //Look for the image.
        String imgLocation = "images/" + imageName + ".png";
        URL imageURL = VectorImagePlotter.class.getResource(imgLocation);

        //Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText(toolTipText);
        button.setPreferredSize(new Dimension(50, 50));

        if (imageURL != null) { //image found
            button.setIcon(new ImageIcon(imageURL, altText));
        } else { //no image found
            button.setText(altText);
            System.err.println("Image not found: " + imgLocation);
        }

        button.addKeyListener(eventsHandler);
        button.addActionListener(eventsHandler);
        return button;
    }

    /**
     * A convenient method to show a warning message.
     * @param title The dialog title
     * @param message the message to be displayed.
     * @return a Warning message.
     */
    public static int showWarningMessage(String title, String message) {
        Object[] options = {"Ok", "Cancel"};
        int warningResponse = JOptionPane.showOptionDialog(pnlDisplay,
                message + "\nDo you want to proceed?",
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[1]);
        return warningResponse;
    }

    /**
     * A convenient method to show an information message.
     * @param title The dialog title
     * @param message the message to be displayed.
     * @return an information message.
     */
    public static int showInformationMessage(String title, String message) {
        Object[] options = {"OK"};
        int dialogResponse = JOptionPane.showOptionDialog(VectorImagePlotter.getDisplayPanel(),
                message,
                title,
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        return dialogResponse;
    }

    /**
     * Sets the color of the pen if an only the Pen Button has been clicked. Otherwise, leaves
     * the pen color as it is.
     * @param colorButtonClicked The color button that triggered the event.
     * @return pc Color of the pen.
     */
    public static Color setPenColor(JButton colorButtonClicked) {

        Color pc = getCurrentPenColor();

        if (penClicked) {
            pc = colorButtonClicked.getBackground();
            currentPenColorBtn.setBackground(pc);
            penColorChanged = true;
        } else {
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
    public static Color setFillColor(JButton colorButtonClicked) {

        Color fc = getCurrentFillColor();

        if (fillClicked) {
            fc = colorButtonClicked.getBackground();
            currentFillColorBtn.setBackground(fc);
            fillColorChanged = true;
        } else {
            fillColorChanged = false;
        }
        return fc;
    }

    /**
     * Returns the current action command.
     * @return the current action command.
     */
    public static String getCurrentAction() {
        return currentAction;
    }

    /**
     * Returns true if the current command is either "PLOT","LINE","RECTANGLE","ELLIPSE" or "POLYGON"
     * @param action an action command.
     * @return boolean - true or false
     */
    public static boolean isDrawingCommand(String action) {
        return action.contains("PLOT") || action.contains("LINE") || action.contains("ELLIPSE") || action.contains("RECTANGLE") || action.contains("POLYGON");
    }

    /**
     * Gets the current pen color
     * @return Color - the current Pen color
     */
    public static Color getCurrentPenColor() {
        return penColor;
    }

    /**
     * Gets the current fill color
     * @return Color - the current fill color
     */
    public static Color getCurrentFillColor() {
        return fillColor;
    }

    /**
     * Return the panel containing the display Canvas.
     * @return the display panel
     */
    public static Canvas getDisplayPanel() {
        return pnlDisplay;
    }

    public static JPanel getMainPanel(){
        return mainPanel;
    }




}
