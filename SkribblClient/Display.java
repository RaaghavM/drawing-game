import java.awt.*;  //for Graphics
import javax.swing.*;  //for JComponent, JFrame
import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.net.*;
import java.io.*;
public class Display extends Canvas
{

    public Display() throws IOException
    {
        new Draw();
        
        
        JFrame frame = new JFrame("My Drawing");
        Canvas canvas = new Display();
        canvas.setSize(400, 400);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

}
