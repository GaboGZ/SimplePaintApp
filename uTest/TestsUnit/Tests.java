package TestsUnit;

import static org.junit.jupiter.api.Assertions.*;

import FileHandler.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Tests {

    VectorFileReader vectorFileReader;
    VectorFileWriter vectorFileWriter;

    //Ensure all object instances are cleared before each test
    @BeforeEach
    public void clearInstaces(){
        vectorFileReader = null;
        vectorFileWriter = null;
    }

    /**
     * Test 0: Constructing a basic VectorFileReader object.
     * [This test obliges you to add a constructor for the VectorFileReader Class]
     */
    @Test
    public void TestConstructFileReader(){
        vectorFileReader = new VectorFileReader();
        vectorFileWriter = new VectorFileWriter();
    }

    @Test
    public void TestWritePolygonToFile(){

        int[] x = {1,2,3,4,5};
        int[] y = {6,7,8,9,10};
        Polygon p = new Polygon();
        p.xpoints = x;
        p.ypoints = y;

        String file = "temp";

        GUI.Canvas.writeCommandToFile("POLYGON", x,y);

        VectorFileReader reader = new VectorFileReader();
        String folder = "C:/cab302/project/files";

        String exp = "POLYGON 1.0 6.0 2.0 7.0 3.0 8.0 4.0 9.0 5.0 10.0";
        String actual = reader.readWholeFile(folder,file+".vec");

        assertEquals(exp,actual);

    }


    @Test
    public void TestReadFile(){
        vectorFileReader = new VectorFileReader();
        String file = "LINE.VEC";
        String folder = "C:/cab302/project/files";
        String actual = vectorFileReader.readWholeFile(folder, file);
        String exp = "LINE 100 100 200 200";
        assertEquals(exp,actual);

        System.out.println("-------------");
        System.out.println(actual);

    }

    @Test
    public void TestReadFileNotFound(){

        vectorFileReader = new VectorFileReader();
        String file = "test-read1.txt";
        String folder = "C:/cab302/project/files";

        String actual = vectorFileReader.readWholeFile(folder, file);
        String exp = "File not found";
        assertEquals(exp,actual);
    }

    @Test
    public void TestWriteToAFile(){
        vectorFileWriter = new VectorFileWriter();
        vectorFileReader = new VectorFileReader();

        String text = "BLAH";
        String fileName = "test-write3";
        String folder = "C:/cab302/project/files";
        vectorFileWriter.writeToFile(fileName,text);


        String exp = text;
        String actual = vectorFileReader.readWholeFile(folder,fileName+".vec");
        assertEquals(exp,actual);
    }

    @Test
    public void TestIdentifyCommand(){
        double x1,x2,y1,y2;
        x1 = 10.0;
        y1 = 20.0;
        x2 = 30.0;
        y2 = 40.0;
        int[] xpoints = {1,2,3};
        int[] ypoints = {1,2,3};

        Shape s1 = new Line2D.Double(x1,y1,x2,y2);
        String exp1 = "LINE";
        String actual1 = GUI.Canvas.identifyCommand(s1);
        assertEquals(exp1,actual1);

        Shape s2 = new Rectangle2D.Double(x1,y1,x2,y2);
        String exp2 = "RECTANGLE";
        String actual2 = GUI.Canvas.identifyCommand(s2);
        assertEquals(exp2,actual2);

        Shape s3 = new Ellipse2D.Double(x1,y1,x2,y2);
        String exp3 = "ELLIPSE";
        String actual3 = GUI.Canvas.identifyCommand(s3);
        assertEquals(exp3,actual3);

        Shape s4 = new Polygon(xpoints,ypoints,xpoints.length);
        String exp4 = "POLYGON";
        String actual4 = GUI.Canvas.identifyCommand(s4);
        assertEquals(exp4,actual4);

        Shape s5 = new Line2D.Double(x1,y1,x1,y1);
        String exp5 = "PLOT";
        String actual5 = GUI.Canvas.identifyCommand(s5);
        assertEquals(exp5,actual5);
    }
}
