package TestsUnit;

import static org.junit.jupiter.api.Assertions.*;

import FileHandler.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.NoSuchFileException;

public class Tests {

    FileReader fileReader;
    FileWriter fileWriter;

    //Ensure all object instances are cleared before each test
    @BeforeEach
    public void clearInstaces(){
        fileReader = null;
        fileWriter = null;
    }

    /**
     * Test 0: Constructing a basic FileReader object.
     * [This test obliges you to add a constructor for the FileReader Class]
     */
    @Test
    public void TestConstructFileReader(){
        fileReader = new FileReader();
        fileWriter = new FileWriter();
    }


    @Test
    public void TestReadFile(){
        fileReader = new FileReader();
        String file = "test-read.txt";
        String actual = fileReader.readFile(file);
        String exp = "hello world!";
        assertEquals(exp,actual);

        System.out.println("-------------");
        System.out.println(actual);

    }

    @Test
    public void TestReadFileNotFound(){

            fileReader = new FileReader();
            String file = "test-read1.txt";
            String charset = "US-ASCII";

            String actual = fileReader.readFile(file);
            String exp = "File not found";
            assertEquals(exp,actual);
    }

    @Test
    public void TestWriteToAFile(){
        fileWriter = new FileWriter();
        fileReader = new FileReader();

        String text = "Test 02";
        String file = "test-write2.txt";
        fileWriter.writeToFile(file,text);

        String actual = fileReader.readFile(file);
        String exp = text;
        assertEquals(exp,actual);
    }

}
