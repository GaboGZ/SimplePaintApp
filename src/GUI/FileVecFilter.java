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
        return "just '.vec' files";
    }
}


