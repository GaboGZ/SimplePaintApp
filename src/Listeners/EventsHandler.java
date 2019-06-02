/*
    Author: Gabriel Garate Zea
    Student ID: N10023780
    Unit: CAB302 - Software Development
    Assignment: Project 2
    Due Date: 2-June-2019
    Queensland University of Technology
    Brisbane, QLD, Australia.
 */

package Listeners;

import FileHandler.VectorFileReader;
import FileHandler.FileVecFilter;
import GUI.VectorImagePlotter;

import static GUI.VectorImagePlotter.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static java.awt.event.InputEvent.*;


public class EventsHandler extends MouseAdapter implements ActionListener, ItemListener, KeyListener {

    private File newfile;
    private Path tempFile;
    private String newFileDirectory;

    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    @Override
    public void keyPressed(KeyEvent e) {

        // Get extended modifiers
        int mod = e.getModifiersEx();
        int key = e.getKeyCode();


        if ((key == KeyEvent.VK_Z) && ((mod & CTRL_DOWN_MASK) != 0 && ((mod & SHIFT_DOWN_MASK) == 0))) {

            try {
                getDisplayPanel().undo();
            } catch (Exception ex) {
                showInformationMessage("Undo", "There are no drawings to undo");
            }
        }
        else if ((key == KeyEvent.VK_Z) && ((mod & CTRL_DOWN_MASK) != 0) && ((mod & SHIFT_DOWN_MASK) != 0)) {
            try {
                getDisplayPanel().redo();
            } catch (Exception ex) {
                showInformationMessage("Redo", "There are no drawings to redo");
            }
        }
        else if ((key == KeyEvent.VK_PERIOD) && ((mod & CTRL_DOWN_MASK) != 0)) {
            plotBtn.doClick();
        }
        else if ((key == KeyEvent.VK_L) && ((mod & CTRL_DOWN_MASK) != 0)) {
            lineBtn.doClick();
        }
        else if ((key == KeyEvent.VK_R) && ((mod & CTRL_DOWN_MASK) != 0)) {
            rectangleBtn.doClick();
        }
        else if ((key == KeyEvent.VK_E) && ((mod & CTRL_DOWN_MASK) != 0)) {
            ellipseBtn.doClick();
        }
        else if ((key == KeyEvent.VK_P) && ((mod & CTRL_DOWN_MASK) != 0)) {
            polygonBtn.doClick();
        }
        else if ((key == KeyEvent.VK_F) && ((mod & CTRL_DOWN_MASK) != 0) && ((mod & SHIFT_DOWN_MASK) == 0)) {
            fillBtn.doClick();
        }
        else if ((key == KeyEvent.VK_F) && ((mod & CTRL_DOWN_MASK) != 0) && ((mod & SHIFT_DOWN_MASK) != 0)) {
            fillCheckBox.doClick();
        }
        else if ((key == KeyEvent.VK_D) && ((mod & CTRL_DOWN_MASK) != 0)) {
            penBtn.doClick();
        }
        else if ((key == KeyEvent.VK_DELETE) && ((mod & CTRL_DOWN_MASK) != 0)) {
            clearBtn.doClick();
        }
        else  if ((key == KeyEvent.VK_C) && ((mod & CTRL_DOWN_MASK) != 0) && ((mod & SHIFT_DOWN_MASK) == 0)) {
            customBtn.doClick();
        }
        else if ((key == KeyEvent.VK_N) && ((mod & CTRL_DOWN_MASK) != 0) && ((mod & SHIFT_DOWN_MASK) == 0)){
            New.doClick();
        }
        else if((key == KeyEvent.VK_O) && ((mod & CTRL_DOWN_MASK) != 0) && ((mod & SHIFT_DOWN_MASK) == 0)){
            Open.doClick();
        }
        else if((key == KeyEvent.VK_S) && ((mod & CTRL_DOWN_MASK) != 0) && ((mod & SHIFT_DOWN_MASK) == 0)){
            Save.doClick();
        }
        else if((key == KeyEvent.VK_S) && ((mod & CTRL_DOWN_MASK) != 0) && ((mod & SHIFT_DOWN_MASK) != 0)){
            SaveAs.doClick();
        }
        else if((key == KeyEvent.VK_Q) && ((mod & CTRL_DOWN_MASK) != 0) && ((mod & SHIFT_DOWN_MASK) == 0)){
            Exit.doClick();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        //
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            Object src = e.getSource();

            if (isDrawingCommand(((JButton) src).getActionCommand())) {
                currentAction = ((JButton) src).getActionCommand();
                commandSelectedLabel.setText("Command: " + getCurrentAction());
            }

            // Detecting color button events
            if (((((JButton) src).getActionCommand() == "CHANGE_COLOR"))) {
                if (src == blackBtn) {
                    penColor = setPenColor(blackBtn);
                    fillColor = setFillColor(blackBtn);
                } else if (src == darkGrayBtn) {
                    penColor = setPenColor(darkGrayBtn);
                    fillColor = setFillColor(darkGrayBtn);
                } else if (src == grayBtn) {
                    penColor = setPenColor(grayBtn);
                    fillColor = setFillColor(grayBtn);
                } else if (src == lightGrayBtn) {
                    penColor = setPenColor(lightGrayBtn);
                    fillColor = setFillColor(lightGrayBtn);
                } else if (src == whiteBtn) {
                    penColor = setPenColor(whiteBtn);
                    fillColor = setFillColor(whiteBtn);
                } else if (src == blueBtn) {
                    penColor = setPenColor(blueBtn);
                    fillColor = setFillColor(blueBtn);
                } else if (src == cyanBtn) {
                    penColor = setPenColor(cyanBtn);
                    fillColor = setFillColor(cyanBtn);
                } else if (src == greenBtn) {
                    penColor = setPenColor(greenBtn);
                    fillColor = setFillColor(greenBtn);
                } else if (src == yellowBtn) {
                    penColor = setPenColor(yellowBtn);
                    fillColor = setFillColor(yellowBtn);
                } else if (src == orangeBtn) {
                    penColor = setPenColor(orangeBtn);
                    fillColor = setFillColor(orangeBtn);
                } else if (src == redBtn) {
                    penColor = setPenColor(redBtn);
                    fillColor = setFillColor(redBtn);
                } else if (src == magentaBtn) {
                    penColor = setPenColor(magentaBtn);
                    fillColor = setFillColor(magentaBtn);
                }
            }
            // Detecting tools events
            if (src == customBtn) {
                // Display JColorChooser
                Color newColor = JColorChooser.showDialog(getMainPanel(), "Pick a color", getMainPanel().getBackground());

                // if a color is picked newColor is set to the picked color otherwise is set to null.
                if (newColor != null) {
                    if (penClicked) { //check if pen or fill is being used.
                        penColor = newColor;
                        currentPenColorBtn.setBackground(penColor);
                    }
                    if (fillClicked) {
                        fillColor = newColor;
                        currentFillColorBtn.setBackground(fillColor);
                    }
                }
            } else if (src == clearBtn) {

                //Dialog
                int Response = showWarningMessage("Clear Drawings",
                        "This action will clear all the drawings and cannot be undone.");

                //If "Ok" clear all drawings
                if (Response == 0) {
                    getDisplayPanel().clearDrawings(true);
                    clearTempFile();
                    getDisplayPanel().repaint();
                }
            } else if (src == undoBtn) {
                try {
                    getDisplayPanel().undo();
                } catch (Exception ex) {
                    showInformationMessage("Undo", "There are no drawings to undo");
                }
            } else if (src == redoBtn) {
                try {
                    getDisplayPanel().redo();
                } catch (Exception ex) {
                    showInformationMessage("Redo", "There are no drawings to redo");
                }
            } else if (src == penBtn) {
                penClicked = true;
                fillClicked = false;
            } else if (src == fillBtn) {
                penClicked = false;
                fillClicked = true;
            }

        } catch (ClassCastException e1) {
//            e1.printStackTrace();
//            System.out.println("JCheckBox cannot be cast to class JButton");
        }

        try {

            // Detecting menu items events
            Object source = e.getSource();
            JMenuItem src = (JMenuItem) source;

            // Get a fileChoose on open selection mode
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            // Filter the view to vector files and directories only.
            fc.addChoosableFileFilter(new FileVecFilter());
            fc.setAcceptAllFileFilterUsed(false);


            if (src == Open) {

                fc.setDialogTitle("Open a file");
                int returnVal = fc.showOpenDialog(VectorImagePlotter.getDisplayPanel());

                if (returnVal == JFileChooser.APPROVE_OPTION) {

                    // Open a file and store its name and directory location
                    String directory = fc.getCurrentDirectory().toString();
                    File loaded = fc.getSelectedFile();
                    VectorFileReader fr = new VectorFileReader();
                    fr.readFile(directory, loaded.getName());

                    // Copy the content of the loaded file into the temporary file.
                    try{
                        // Copy loaded file content into the temp file for editing.
                        tempFile = Paths.get("C:/cab302/project/files/temp.vec");
                        Path loadedFile = Paths.get(directory, loaded.getName());
                        // .copy(source,target);
                        Files.copy(loadedFile,tempFile, StandardCopyOption.REPLACE_EXISTING);
                        Desktop.getDesktop().open(tempFile.toFile());
                    }catch(Exception ex){ }
                }

            } else if (src == New) {

                // Indicate user that drawing will be cleared if they are now saved
                if (getDisplayPanel().clearDrawings(false)) {
                    clearTempFile();
                    //Save As is called from clearDrawings();

                    // Get a fileChooser on open selection mode.
                    fc.setDialogTitle("Create a new file");
                    int choice = fc.showSaveDialog(VectorImagePlotter.getDisplayPanel());

                    if (choice == JFileChooser.APPROVE_OPTION) {
                        newfile = fc.getSelectedFile();
                        newFileDirectory = fc.getCurrentDirectory().toString();

                        VectorImagePlotter.getFrames()[0].setTitle("CAB302 | " + newfile.getName());


                        if (newfile == null) {
                            return;
                        }

                        //validate vector file
                        if (!newfile.getName().toLowerCase().endsWith(".vec")) {
                            newfile = new File(newfile.getParentFile(), newfile.getName() + ".vec");
                        }

                        try {
                            PrintWriter writer = new PrintWriter(newfile);
                            writer.print("I AM A NEW FILE");
                            writer.close();
                        } catch (Exception e2) {
//                            e2.printStackTrace();
                        }
                    }

                }


            } else if (src == Save) {
                try {

                    if(newfile != null){

                        // Copy the content of the temporary file into the user new file.
                        tempFile = Paths.get("C:/cab302/project/files/temp.vec");
                        Path targetFile = Paths.get(newFileDirectory, newfile.getName());
                        Files.copy(tempFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                        Desktop.getDesktop().open(newfile);

                    }else{
                        System.out.println("Creating a new file.");
                        New.doClick();
                    }

                } catch (Exception e2) {
                    e2.printStackTrace();
                }

            } else if (src == SaveAs) {

                fc.setDialogTitle("Save a file as...");
                int choice = fc.showSaveDialog(VectorImagePlotter.getDisplayPanel());
                if (choice == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String directory = fc.getCurrentDirectory().toString();
                    if (file == null) {
                        return;
                    }
                    if (!file.getName().toLowerCase().endsWith(".vec")) {
                        file = new File(file.getParentFile(), file.getName() + ".vec");
                    }
                    try {

                        // Copy the content of the temporary file into the user file.
                        Path tempFile = Paths.get("C:/cab302/project/files/temp.vec");
                        Path targetFile = Paths.get(directory, file.getName());
                        Files.copy(tempFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                        Desktop.getDesktop().open(file);

                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            } else if (src == Exit) {

                if(getDisplayPanel().clearDrawings(false)){
                    //Clear temporary file for future use.
                    clearTempFile();
                    System.exit(0);
                }else{
                    //do nothing
                }
            }
        } catch (ClassCastException e2) {
//            e1.printStackTrace();
        }
    }


    @Override
    public void itemStateChanged(ItemEvent e) {
        Object s = e.getItemSelectable();
        if (s == fillCheckBox) {
            if (fillCheckBox.isSelected())
                checkBoxLabel.setText("Fill ON ");
            else {
                checkBoxLabel.setText("Fill OFF");
            }
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        fillCheckBox.doClick();
    }

    public void clearTempFile() {
        Path tempFile = Paths.get("C:/cab302/project/files/temp.vec");
        try {
            PrintWriter writer = null;
            writer = new PrintWriter(tempFile.toFile());
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
