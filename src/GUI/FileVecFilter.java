package GUI;

import javax.swing.filechooser.FileFilter;
import java.io.File;

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
            if(file.contains(".txt")){
                return true;
            }
        }else{
            return false;
        }

        return false;
    }

    @Override
    public String getDescription() {
        // todo: Should be changed to .vec files
        return "just '.txt' file";
    }
}


