import java.io.*;
import java.net.*;

public class ServerThread extends Thread
{
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private int playerNum;
  private String playerName;
  private int playerScore;
  
  public ServerThread(Socket socket, int playerNum) throws IOException
  {
    this.socket = socket;
    this.playerNum = playerNum;
    this.playerName = null;
    this.playerScore = 0;
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
    start();  //start run method in new thread
  }

  public int getPlayerNum() {
    return playerNum;
  }

  public String getPlayerName() {
    return playerName;
  }

  public int getPlayerScore() { return playerScore; }

  public void addPlayerScore(int toAdd) { this.playerScore += toAdd; }

  public void updatePlayerName(String newName) {
    this.playerName = newName;
  }

  public void run()
  {
    try
    {
      String line = in.readLine();

      while (line != null)
      {
        //send info to server for processing
        String sig = line.substring(0, line.indexOf(" "));
        String toSend = line.substring(line.indexOf(" ") + 1);
        System.out.println("received:  '" + toSend + "' from player " + playerNum + ", sig=" + sig + " ");
        Server.processInfo(sig + " ", toSend, Integer.toString(playerNum));
        //read next line
        line = in.readLine();
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
    System.out.println("sent:  " + line);
  }
  
  public void closeAll() throws IOException
  {
    in.close();
    out.close();
    socket.close();  
  }
}