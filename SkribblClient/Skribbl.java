import java.net.*;
import java.io.*;
import javax.swing.JOptionPane; 
public class Skribbl
{
    static private ClientThread clientThread;
    private String playerName;
    
    public static void main(String[] args) throws IOException
    {
        Skribbl s = new Skribbl();
    }
    
    public Skribbl() throws IOException
    {
        // initialise instance variables
        //Draw display = new Draw();        
        GUI display = new GUI(); //GUI contains a draw
        Socket socket = new Socket("localhost", 9001); //start server on port 9000        
        clientThread = new ClientThread(socket, display); //start thread for listening to client        
        playerName = JOptionPane.showInputDialog("Enter your name:"); 
        display.setPlayerName(playerName);
        Skribbl.prepareInformation("NAME " + playerName);
        //clientThread.send("hello"); //send friendly greeting to client
        //System.out.println("hereee");
        
    }   
    
    public void play()
    {
        
    }
    
    public int sampleMethod(int y)
    {
        // put your code here
        return 0;
    }
    
    public static void prepareInformation(String s)
    {
        clientThread.send(s);
    }

}
