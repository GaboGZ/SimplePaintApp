package FileHandler;

import java.nio.file.*;
import java.io.*;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileWriter {

    String str;
    byte data[];
    Path p;
    static final String sourceFolder = "files/";

    public void writeToFile(String fileName, String text) {
        this.str = text;
        this.data = text.getBytes();
        Path p = Paths.get(sourceFolder + fileName + ".VEC");

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }
    }
}
