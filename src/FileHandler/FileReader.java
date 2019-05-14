package FileHandler;

import java.io.*;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

/**
 * This FileReader accepts a text format such as US-ASCII format.
 */
public class FileReader {

    Charset ch;
    final String sourceFolder = "files/";
    String sourceFile;
    String textFile;


    /**
     * Reads a file by passing the sourceFile and charset as paramenters
     * @param sourceFile Name of the file.
     * @param charset charset type.
     * @return
     */
    public String readFile(String sourceFile,String charset){

        File file = new File(sourceFolder + sourceFile);
        Charset ch = Charset.forName("US-ASCII");

        // TRY with resources
        // The BufferedReader is automatically closed after each read.

        try {
            BufferedReader reader = Files.newBufferedReader(file.toPath(), ch);
            String textLine = null;

            while ((textLine = reader.readLine()) != null) {
                System.out.println(textLine);
                textFile = "" + textLine;
            }

        } catch (IOException e) {
            //todo Show a dialog indicating that the file was not found
            textFile = "File not found";
        }

        return textFile;
    }

    /**
     * A convenient method to read files with Charset "US-ASCII"
     * @param sourceFile Name of the file
     * @return
     */
    public String readFile(String sourceFile){
        String ch = "US-ASCII";
        return readFile(sourceFile,ch);
    }

}
