import java.io.*;
import java.net.*;

public class Main {

    public static void main(String[] args) throws IOException
    {
        int numPlayers = 3;
        for (int i = 0; i < 3; i++)
        {
            Thread thread = new Thread("Player" + i);
            thread.start();
        }
            ServerSocket server = new ServerSocket(9000);  //start server on port 9000
            Socket socket = server.accept();  //wait for client to connect
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String line = in.readLine();  //wait for message from client
                out.println(line.toUpperCase());  //send message to client
                if (line.equals("exit"))
                    break;
            }



            //disconnect from client

            in.close();
            out.close();
            socket.close();
    }
}

class ConnectionThread implements Runnable
{
    private String playerName;

    public ConnectionThread(String playerName)
    {
        this.playerName = playerName;
    }

    @Override
    public void run()
    {
        System.out.println("Connecting to player " + playerName + "...");

        try {
            ServerSocket server = new ServerSocket(9000);  //start server on port 9000
            Socket socket = server.accept();  //wait for client to connect
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                String line = in.readLine();  //wait for message from client
                out.println(line.toUpperCase());  //send message to client
                if (line.equals("exit"))
                    break;
            }
            in.close();
            out.close();
            socket.close();
        }
        catch(IOException e)
        {
            System.out.println("Cannot connect to player " + playerName + " :(");
        }
    }
}
