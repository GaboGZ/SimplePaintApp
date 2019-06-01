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

import java.nio.file.*;
import java.io.*;


import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * This class provides all the resources to write into a file.
 */
public class VectorFileWriter {

    String str;
    byte data[];
    Path p;
    static final String sourceFolder = "files/";

    public void writeToFile(String fileName, String text) {
        this.str = text;
        this.data = text.getBytes();
        Path p = Paths.get(sourceFolder + fileName + ".vec");

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }
    }

}
