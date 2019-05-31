package Listeners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static GUI.Window.*;
import static java.awt.event.InputEvent.*;

public class EventsHandler extends MouseAdapter implements ActionListener, ItemListener, KeyListener {


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
        if ((key == KeyEvent.VK_Z) && ((mod & CTRL_DOWN_MASK) != 0) && ((mod & SHIFT_DOWN_MASK) != 0)) {
            try {
                getDisplayPanel().redo();
            } catch (Exception ex) {
                showInformationMessage("Redo", "There are no drawings to redo");
            }
        }
        if ((key == KeyEvent.VK_PERIOD) && ((mod & CTRL_DOWN_MASK) != 0)) {
            plotBtn.doClick();
        }
        if ((key == KeyEvent.VK_L) && ((mod & CTRL_DOWN_MASK) != 0)) {
            lineBtn.doClick();
        }
        if ((key == KeyEvent.VK_R) && ((mod & CTRL_DOWN_MASK) != 0)) {
            rectangleBtn.doClick();
        }
        if ((key == KeyEvent.VK_E) && ((mod & CTRL_DOWN_MASK) != 0)) {
            ellipseBtn.doClick();
        }
        if ((key == KeyEvent.VK_P) && ((mod & CTRL_DOWN_MASK) != 0)) {
            polygonBtn.doClick();
        }
        if ((key == KeyEvent.VK_F) && ((mod & CTRL_DOWN_MASK) != 0) && ((mod & SHIFT_DOWN_MASK) == 0)) {
            fillBtn.doClick();
        }
        if ((key == KeyEvent.VK_F) && ((mod & CTRL_DOWN_MASK) != 0) && ((mod & SHIFT_DOWN_MASK) != 0)) {
            fillCheckBox.doClick();
        }
        if ((key == KeyEvent.VK_D) && ((mod & CTRL_DOWN_MASK) != 0)) {
            penBtn.doClick();
        }
        if ((key == KeyEvent.VK_DELETE) && ((mod & CTRL_DOWN_MASK) != 0)) {
            clearBtn.doClick();
        }
        if ((key == KeyEvent.VK_C) && ((mod & CTRL_DOWN_MASK) != 0) && ((mod & SHIFT_DOWN_MASK) != 0)){
            customBtn.doClick();
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
                commmandSelectedLabel.setText("Command: " + getCurrentAction());
            }

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
            } else if (src == customBtn) {
                // Display JColorChooser
                Color newColor = JColorChooser.showDialog(mainPanel, "Pick a color", mainPanel.getBackground());

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
                    pnlDisplay.clearDrawings(true);
                    pnlDisplay.repaint();
                }
            } else if (src == undoBtn) {
                try {
                    pnlDisplay.undo();
                } catch (Exception ex) {
                    showInformationMessage("Undo", "There are no drawings to undo");
                }
            } else if (src == redoBtn) {
                try {
                    pnlDisplay.redo();
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
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object src = e.getItemSelectable();
        if (src == fillCheckBox) {
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
}
