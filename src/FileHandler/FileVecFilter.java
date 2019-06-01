/*
    Author: Gabriel Garate Zea
    Student ID: N10023780
    Unit: CAB302 - Software Development
    Assignment: Project 2
    Due Date: 2-June-2019
    Queensland University of Technology
    Brisbane, QLD, Australia.
 */

package FileHandler;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * This class provide a filter so that only files ending with '.vec'
 * are shown anytime a JFilechooser is used.
 */
public class FileVecFilter extends FileFilter {


    @Override
    public boolean accept(File f) {
        //Show directories
        if(f.isDirectory()){
            return true;
        }

        //Filter vec files
        String file = f.getName();

        if(file != null) {
            if(file.contains(".VEC") || file.contains(".vec")){
                return true;
            }
        }else{
            return false;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "just vector files ending with '.vec'";
    }
}


