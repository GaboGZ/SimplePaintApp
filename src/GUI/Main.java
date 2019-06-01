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

public class Main {

    /**
     * Entry program point.
     * Create a new instance of a VectorImagePlotter class.
     * The VectorImagePlotter class implements the Runnable
     * interface.
     * @param args
     */
    public static void main(String[] args){

        SwingUtilities.invokeLater(new VectorImagePlotter());
        // the SwingUtilities.invokeLater(Runnable doRun) method
        // calls the doRun.run() method of the class passed as
        // parameter on a separate thread.
    }
}
