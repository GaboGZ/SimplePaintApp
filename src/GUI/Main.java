package GUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Window());
        // the .invokeLater(Runnable doRun) method run the doRun.run() methods on a
        // separate thread.

    }
}
