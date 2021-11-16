import java.awt.*;  //for Graphics
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import javax.imageio.*;
import javax.swing.*;  //for JComponent, JFrame
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.Border;
import java.util.*;

public class Draw extends JComponent implements MouseListener, MouseMotionListener
{
    private JFrame frame;
    private int lastKeyPressed;
    private Color lineColor;
    private static Color drawingColor; 

    private static BufferedImage whiteboard;
    private int displayWidth;
    private int displayHeight;

    private int lastX;
    private int lastY;
    private Map<Color, String> colorMap;

    private boolean myTurn;
    //use boolean to determine whose turn it is (but the server tells whose turn it is)

    public Draw() throws IOException
    {

        //g is the main foundational graphics
        //pen is another graphics    
        displayHeight = 650;  
        displayWidth = (int)(450*1.69);
              
        whiteboard = new BufferedImage(displayWidth, displayHeight,BufferedImage.TYPE_INT_RGB);         
        Graphics pen = whiteboard.getGraphics();
        pen.setColor(Color.WHITE);
        pen.fillRect(0,0,displayWidth,displayHeight);
        pen.setColor(Color.BLACK);

        //pen.drawLine(50,60, 500, 600);

        JFrame frame = new JFrame();
        setPreferredSize(new Dimension(displayWidth, displayHeight));
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
        addMouseListener(this);
        addMouseMotionListener(this);

        initializeColorMap();

        drawingColor = Color.BLACK;
        //can we change the mouse when we hold it over?
        //do something with right click?
        myTurn = false;
        //myTurn = server.getTurn()

    }
    
    public int displayWidth()
    {
      return displayWidth;   
    }
    public int displayHeight()
    {
      return displayHeight;   
    }
    
    public void setTurn(boolean newTurn)
    {
        myTurn = newTurn;
    }
    
    public boolean isMyTurn()
    {
     return myTurn;   
    }

    public void initializeColorMap()
    {
        colorMap = new HashMap<Color,String>();
        colorMap.put(Color.BLACK, "BLACK");
        colorMap.put(Color.BLUE, "BLUE");
        colorMap.put(Color.RED, "RED");
        colorMap.put(Color.YELLOW, "YELLOW");
        colorMap.put(Color.GREEN, "GREEN");
    }

    public void paintComponent(Graphics g)
    {
        g.drawImage(whiteboard,0,0,null);
    }
    public void mousePressed(MouseEvent e)
    {

        //person who is drawing
        if (myTurn)
        {
            //System.out.println("mousePressed was called");
            //Graphics pen = whiteboard.getGraphics();

            Graphics pen = whiteboard.getGraphics();
            pen.setColor(drawingColor);
            Graphics2D g2 = (Graphics2D)pen;
            pen.setColor(drawingColor);
            g2.setStroke(new BasicStroke(5.0f));
            pen.drawLine(e.getX(), e.getY(), e.getX(), e.getY());
            lastX = e.getX();
            lastY = e.getY();

            //xDrawCoords.add(e.getX());
            //yDrawCoords.add(e.getY());
            //mousemotion listener  --> 2 more methods when mouse is pressed smoeone is strating to draw when mouse dragged the moues is down and they are dragging and mouse released is done
            Skribbl.prepareInformation(new String("LINE " + lastX + " " + lastY + " " + e.getX() + " " + e.getY() + " " + colorMap.get(drawingColor)));
            repaint();
        }
        else
        {
            //person who is guessing   

        }
    }

    public void mouseMoved(MouseEvent e)
    {
        //Graphics pen = whiteboard.getGraphics();   
        //pen.drawLine(e.getX(), e.getY(), e.getX()+1, e.getY()+1);
    }

    public void mouseDragged(MouseEvent e)
    {
        //person who is drawing
        if (myTurn)
        {
            // System.out.println(e.getX());
            // System.out.println(e.getY());
            Graphics pen = whiteboard.getGraphics();
            pen.setColor(drawingColor);
            Graphics2D g2 = (Graphics2D)pen;
            //whiteboard.getGraphics();
            g2.setStroke(new BasicStroke(5.0f)); //changes the thickness
            pen.drawLine(lastX, lastY, e.getX(), e.getY());

            // pen.drawLine(e.getX(), e.getY(), e.getX()+2, e.getY());
            // pen.drawLine(e.getX(), e.getY(), e.getX()+3, e.getY());
            // pen.drawLine(e.getX(), e.getY(), e.getX(), e.getY()+1);
            // pen.drawLine(e.getX(), e.getY(), e.getX(), e.getY()+2);
            // pen.drawLine(e.getX(), e.getY(), e.getX(), e.getY()+3);
            //can make it like (if player selected this wifth of the drawing, do x)

            // pen.drawOval(e.getX(), e.getY(), 3,3);
            // pen.fillOval(e.getX(),e.getY(),3,3);

            // pen.drawOval(e.getX()+1, e.getY(), 3,3);
            // pen.fillOval(e.getX()+1,e.getY(),3,3);

            // pen.drawOval(e.getX()-1, e.getY(), 3,3);
            // pen.fillOval(e.getX()-1,e.getY(),3,3);

            // pen.drawOval(e.getX(), e.getY()+1, 3,3);
            // pen.fillOval(e.getX(),e.getY()+1,3,3);

            // pen.drawOval(e.getX(), e.getY()-1, 3,3);
            // pen.fillOval(e.getX(),e.getY()-1,3,3);
            //pen.drawLine(e.getX(), e.getY(), e.getX()+1, e.getY());

            Skribbl.prepareInformation(new String("LINE " + lastX + " " + lastY + " " + e.getX() + " " + e.getY() + " " + colorMap.get(drawingColor)));
            //Skribbl.receiveCoordinates(new String);
            lastX = e.getX();
            lastY = e.getY();
            /*information to send to the other computers:
             * e.getX()
             * e.getY()
             * whiteboard
             * method(e.getX(), e.getY()) --> sends the whiteboard internally
             */

            repaint();
            //xDrawCoords.add(e.getX());
            //yDrawCoords.add(e.getY());
            //last point is null --> end drawing

        }
        else
        {
            //person who is guessing

        }
    }

    public void setWhiteboardColor(String s)
    {
        //Graphics g = whiteboard.getGraphics();

        if (s.equals("black"))
        {
            drawingColor = Color.BLACK;
        }
        if (s.equals("blue"))
        {
            drawingColor = Color.BLUE;
        }
        if (s.equals("red"))
        {
            drawingColor = Color.RED;
        }
        if (s.equals("green"))
        {
            drawingColor = Color.GREEN;
        }
        if (s.equals("yellow"))
        {
            drawingColor = Color.YELLOW;
        }
        if (s.equals("erase"))
        {
            Graphics pen = whiteboard.getGraphics();
            pen.setColor(Color.WHITE);
            pen.fillRect(0,0,displayWidth,displayHeight);                      
            Skribbl.prepareInformation("CLEAR ");
            repaint();  
        }

    }

    public BufferedImage getWhiteboard()
    {
        return whiteboard;
    }

    public void mouseReleased(MouseEvent e)
    {

        //ignore

    }

    public void mouseClicked(MouseEvent e)
    {
        //ignore
    }

    public void mouseEntered(MouseEvent e)
    {
        //ignore
    }

    public void mouseExited(MouseEvent e)
    {
        //ignore
    }
}