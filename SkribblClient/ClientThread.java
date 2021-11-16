import java.io.*;
import java.net.*;

public class ClientThread extends Thread
{
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private GUI display;
  
  public ClientThread(Socket socket, GUI Gui) throws IOException
  {
    this.socket = socket;
    display = Gui;
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
    start();  //start run method in new thread
  }
  
  public void run()
  {
    try
    {
      String line = in.readLine();
      //System.out.println("received:  " + line);
      while (line != null)
      {
        //do something with received line here
        
        display.processReceivedInformation(line);
        
        //read next line
        line = in.readLine();
        //System.out.println("received:  " + line);
      }
      
      System.out.println("client disconnected");
      closeAll();
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void send(String line)
  {
    out.println(line);
    //System.out.println("sent:  " + line);
  }
  
  public void closeAll() throws IOException
  {
    in.close();
    out.close();
    socket.close();  
  }
}