//TODO: Implement clock and slow letter reveal on client side

import java.awt.event.*;  //for ActionListener, ActionEvent
import javax.swing.*;  //for JFrame, BoxLayout, JLabel, JTextField, JButton
import java.awt.*;  //for Graphics
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

public class GUI implements ActionListener
{
    private JTextField field;
    private JTextField guesses;
    private JTextArea chat;
    private JLabel score;
    private JTextField enterPlayerName;
    private Draw draw;
    private String playerName;

    private JButton black;
    private JButton blue;
    private JButton red;
    private JButton green;
    private JButton yellow;
    private JButton erase;

    private int displayWidth;   
    private int displayHeight;

    private JLabel hint;
    private JFrame frame;
    private String currentWord;
    private int scoreNum;
    public GUI() throws IOException
    {
        scoreNum = 0;

        draw = new Draw();
        //make a window

        frame = new JFrame();

        //frame.setTitle("My Window");

        //tell window to place each new component under the previous ones
        //frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        //frame.getContentPane().add(draw);        
        //5/19 reworking the layout
        /* Starting from innermost embedded component (the gridLayout for color buttons)
         * 
         * 
         */
        //1 row, 5 buttons
        GridLayout g = new GridLayout(1, 6);

        //frame.setLayout(g);

        JButton black = new JButton("   ");
        JButton blue = new JButton("   ");
        JButton red = new JButton("   ");
        JButton green = new JButton("   ");
        JButton yellow = new JButton("   ");
        JButton erase = new JButton(" erase ");

        black.addActionListener(this);
        blue.addActionListener(this);
        red.addActionListener(this);
        green.addActionListener(this);
        yellow.addActionListener(this);
        erase.addActionListener(this);

        black.setActionCommand("black");
        blue.setActionCommand("blue");
        red.setActionCommand("red");
        green.setActionCommand("green");
        yellow.setActionCommand("yellow");
        erase.setActionCommand("erase");

        black.setBackground(Color.BLACK);
        blue.setBackground(Color.BLUE);
        red.setBackground(Color.RED);
        green.setBackground(Color.GREEN);
        yellow.setBackground(Color.YELLOW);
        erase.setBackground(Color.WHITE);            

        // black.setPreferredSize(new Dimension(50, 20));
        // blue.setPreferredSize(new Dimension(50, 20));
        // red.setPreferredSize(new Dimension(50, 20));
        // green.setPreferredSize(new Dimension(50, 20));
        // yellow.setPreferredSize(new Dimension(50, 20));
        // erase.setPreferredSize(new Dimension(90, 50));

        //frame.setLayout(new GridLayout(0,2));
        // frame.add(black);
        // frame.add(blue);
        // frame.add(red);
        // frame.add(green);
        // frame.add(yellow);
        // frame.add(erase);

        JPanel colorBar = new JPanel(g);
        colorBar.add(black);
        colorBar.add(blue);
        colorBar.add(red);
        colorBar.add(green);
        colorBar.add(yellow);
        colorBar.add(erase);

        black.setOpaque(true);
        blue.setOpaque(true);
        red.setOpaque(true);
        green.setOpaque(true);
        yellow.setOpaque(true);
        erase.setOpaque(true);
        //pink outline on erase button
        Border b = BorderFactory.createLineBorder(Color.PINK, 3);
        erase.setBorder(b);

        //drawing area and colros
        JPanel middleSection = new JPanel();
        middleSection.setLayout(new BoxLayout(middleSection, BoxLayout.PAGE_AXIS));
        //System.out.println("Here in middle section");
        //middleSection.setLayout(new GridBagLayout());
        //left off here 5/19 delete the line below
        //GridBagConstraints gbc = new GridBagConstraints();

        //hint = new JLabel();
        //hint.setFont(new Font("Impact", Font.PLAIN, 50));
        //middleSection.add(hint);

        middleSection.add(draw);
        middleSection.add(colorBar);

        JPanel rightSection = new JPanel();
        rightSection.setLayout(new BoxLayout(rightSection, BoxLayout.PAGE_AXIS));
        chat = new JTextArea(30,1);
        chat.setEditable(false);
        for (int x = 0; x < 30; x++)
        {
            chat.append("\n");
            updateScrollPane();
        }
        guesses = new JTextField(35);
        //guesses.setSize(150,20);    
        guesses.setSize(20,1);    
        Font font1 = new Font("SansSerif", Font.BOLD, 13);
        Font chatFont = new Font("Times New Roman", Font.BOLD, 13);
        //Color darkBlue = new Color(0, 7, 201);
        //chat.setBackground(Color.CYAN);
        chat.setFont(chatFont);
        //chat.append("Turn " + parsed[2]);

        guesses.setFont(font1);
        //guesses.setHorizontalAlignment(JTextField.CENTER);
        guesses.addActionListener(this);
        
        // DefaultCaret caret = (DefaultCaret)chat.getCaret();
        // caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); 
        JScrollPane j = new JScrollPane(chat);
        rightSection.add(j);
        rightSection.add(guesses);

        JPanel leftSection = new JPanel();
        score = new JLabel("Score: " + scoreNum);
        //score.setBounds(100,100,displayWidth,displayHeight);
        //label.setBounds(150, 100, size.width, size.height); 

        score.setFont(new Font("Impact", Font.PLAIN, 30));
        leftSection.add(score);

        JPanel screen = new JPanel();
        screen.add(leftSection);
        screen.add(middleSection);
        screen.add(rightSection);

        hint = new JLabel();
        hint.setFont(new Font("Courier New", Font.BOLD, 50));
        frame.getContentPane().add(hint);
        frame.getContentPane().add(screen);

        //score.setBounds(100,4000,100,1000);

        //5/19 reworking the layout

        //add some text
        // frame.getContentPane().add(new JLabel("Send your guess:"));

        // //experiment with menus
        // JMenuBar menuBar = new JMenuBar();
        // JMenu menu = new JMenu("Colors Menu");
        // menu.add(new JRadioButtonMenuItem("Black"));
        // menu.add(new JRadioButtonMenuItem("Blue"));
        // menu.add(new JRadioButtonMenuItem("Red"));
        // menu.add(new JRadioButtonMenuItem("Green"));
        // menu.add(new JRadioButtonMenuItem("Yellow"));

        // menuBar.add(menu);
        // frame.setJMenuBar(menuBar);

        //--commented out here with new layout

        // //frame.getContentPane().addLayoutComponent("Score", new Component());

        // //         frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));   
        // //add a text field

        // field = new JTextField(25);

        // frame.getContentPane().add(field);

        //--commented out here with new layout

        //tell button to call this object's actionPerformed method when pressed

        //field.addActionListener(this);

        //store text in button that can be retrieved in actionPerformed method

        frame.pack();  //determine best size for window

        frame.setVisible(true);  //show the window      

    }

    public String getUnderlined(String word)
    {
        int length = word.length();
        String underline = "";
        for (int x = 0; x < length; x++)
        {
            if (!word.substring(x, x+1).equals(" "))
            {
                underline += "_ ";
            }
            else
            {
                underline += "  ";
            }
        }
        underline = underline.substring(0, underline.length()-1);
        return underline;
    }

    public void setPlayerName(String name)
    {
        playerName = name;   
    }

    public void changeHint(String newHint)
    {
        hint.setText(newHint);
    }

    
    public void updateScrollPane()
    {
     chat.setCaretPosition(chat.getDocument().getLength());   
    }
    
    public void processReceivedInformation(String info)
    {        

        String[] parsed = info.split(" ");

        if (parsed[0].equals("LINE"))
        {
            //LINE previousX previousY currentX currentY

            Graphics pen = draw.getWhiteboard().getGraphics();

            //pen.setColor(Color.BLACK);
            if (parsed[5].equals("BLACK"))
                pen.setColor(Color.BLACK);
            if (parsed[5].equals("BLUE"))
                pen.setColor(Color.BLUE);
            if (parsed[5].equals("RED"))
                pen.setColor(Color.RED);
            if (parsed[5].equals("GREEN"))
                pen.setColor(Color.GREEN);
            if (parsed[5].equals("YELLOW"))
                pen.setColor(Color.YELLOW);

            Graphics2D g2 = (Graphics2D)pen;
            //whiteboard.getGraphics();
            g2.setStroke(new BasicStroke(5.0f)); //changes the thickness

            pen.drawLine(Integer.parseInt(parsed[1]), Integer.parseInt(parsed[2]), Integer.parseInt(parsed[3]), Integer.parseInt(parsed[4]));

            draw.repaint();
        }
        else if (parsed[0].equals("MESSAGE"))
        {
            //System.out.println("GOT TO MESSAGE");
            String received = "";
            for (int x = 1; x < parsed.length; x++)
            {
                received += parsed[x] + " ";
            }
            received = received.substring(0, received.length()-1);
            chat.append(received + "\n");
            updateScrollPane();
        }
        else if (parsed[0].equals("BROADCAST"))
        {

            if (parsed[1].equals("CORRECT"))
            {
                String name = "";
                for (int x = 2; x < parsed.length; x++)
                {
                    name += parsed[x] + " ";
                }
                name = name.substring(0, name.length()-1);
                if (name.equals(playerName))
                    chat.append("You got it right!\n");
                else
                    chat.append(name + " got it right!\n");
                updateScrollPane();
                if (name.equals(playerName))
                {
                    guesses.setEditable(false);
                }
                
            }
            if (parsed[1].equals("HELLO"))
            {
                String name = "";
                for (int x = 2; x < parsed.length; x++)
                {
                    name += parsed[x] + " ";
                }
                name = name.substring(0, name.length()-1);
                chat.append(name + " joined the game\n");
                updateScrollPane();
            }
            if (parsed[1].equals("TURN"))
            {
                String name = "";
                for (int x = 3; x < parsed.length; x++)
                {
                    name += parsed[x] + " ";
                }
                name = name.substring(0, name.length()-1);
                //System.out.println(parsed[3]);
                //System.out.println(playerName);
                chat.append("Turn " + parsed[2] + "\n");
                updateScrollPane();
                if (name.equals(playerName))
                    chat.append("It is now your turn!\n");
                else
                    chat.append("It is now " + name + "'s turn\n");
                updateScrollPane();
                guesses.setText("");
                if (name.equals(playerName)) //problem with 2 people having same name?
                    draw.setTurn(true);
                else
                    draw.setTurn(false);
                if (draw.isMyTurn())
                {
                    guesses.setEditable(false);
                }
                else
                {
                    guesses.setEditable(true);
                }
                Graphics pen = draw.getWhiteboard().getGraphics();
                Graphics2D g2 = (Graphics2D)pen;   
                g2.setColor(Color.WHITE);
                g2.fillRect(0,0,draw.displayWidth(),draw.displayHeight());
                draw.repaint();
            }
            if (parsed[1].equals("LETTERCOUNT") && !draw.isMyTurn())
            {
                String received = "";
                for (int x = 2; x < parsed.length; x++)
                {
                    received += parsed[x] + " ";
                }
                received = received.substring(0, received.length()-1);
                changeHint(getUnderlined(received));
            }
        }
        else if (parsed[0].equals("CLEAR"))
        {
            Graphics pen = draw.getWhiteboard().getGraphics();
            Graphics2D g2 = (Graphics2D)pen;   
            g2.setColor(Color.WHITE);
            g2.fillRect(0,0,draw.displayWidth(),draw.displayHeight());
            draw.repaint();
        }
        else if (parsed[0].equals("WORD"))
        {
            String received = "";
            for (int x = 1; x < parsed.length; x++)
            {
                received += parsed[x] + " ";
            }
            received = received.substring(0, received.length()-1);
            chat.append("Your word: " + received + "\n");
            updateScrollPane();
            changeHint(received);
            currentWord = received;           
        }
        else if (parsed[0].equals("LETTER") && !draw.isMyTurn())
        {
            int index = Integer.parseInt(parsed[2]);
            // parsed[1] letter
            // parsed[2] = index
            String currentDisplayedString = hint.getText();
            int underlineNum = 0;
            int letterIndex = -1;
            boolean done = false;
            System.out.println(currentDisplayedString);
            for (int i = 0; i < currentDisplayedString.length(); i++)
            {
                if (!currentDisplayedString.substring(i, i+1).equals(" "))
                    underlineNum++;
                else if (currentDisplayedString.substring(i-1, i).equals(" ") && currentDisplayedString.substring(i+1, i+2).equals(" "))
                    underlineNum++;
                if (index + 1 == underlineNum && !done)
                {
                    letterIndex = i;
                    done = true;
                }
            }
            String newString = currentDisplayedString.substring(0, letterIndex) + parsed[1] + currentDisplayedString.substring(letterIndex+1);
            changeHint(newString);
        }

        else if (parsed[0].equals("SCORE"))
        {
            scoreNum = Integer.parseInt(parsed[1]);
            score.setText("Score: " + scoreNum);
        }

        //if its a message 
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("black") && draw.isMyTurn())
        {
            draw.setWhiteboardColor("black");
        }
        else if (e.getActionCommand().equals("blue") && draw.isMyTurn())
        {
            draw.setWhiteboardColor("blue");
        }
        else if (e.getActionCommand().equals("red") && draw.isMyTurn())
        {
            draw.setWhiteboardColor("red");
        }
        else if (e.getActionCommand().equals("green") && draw.isMyTurn())
        {
            draw.setWhiteboardColor("green");
        }
        else if (e.getActionCommand().equals("yellow") && draw.isMyTurn())
        {
            draw.setWhiteboardColor("yellow");
        }
        else if (e.getActionCommand().equals("erase") && draw.isMyTurn())
        {
            draw.setWhiteboardColor("erase");
        }

        //reaches these lines when enter is pressed (not button1)
        else        {

            String toSend = "MESSAGE" + " " + guesses.getText().toLowerCase();
            guesses.setText("");
            Skribbl.prepareInformation(toSend);

        }
    }

    public void keyPressed(MouseEvent e)
    {
        // if (e.getKeyCode() == KeyEvent.VK_ENTER)
        // {

        // }
    }

    public void keyReleased(KeyEvent e)
    {
        //ignored
    }

    public void keyTyped(KeyEvent e)
    {
        //ignored
    }
}