//import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;

//In background:
//When the time is equal to something, stop and proceed to next round
//Split time into chunks depending on the currentword's length
//after the time reaches the first step, broadcast a random letter from currentword that's not already revealed

//may be infinite loop on the two word things
//may need to fix dimensions

public class Server
{
  static ArrayList<ServerThread> threads;
  static ArrayList<String> wordsList;
  static String currentWord;
  static String messageSig;
  static String broadcastSig;
  static String lineSig;
  static String nameSig;
  static String clearSig;
  static String wordSig;
  static String letterSig;
  static String scoreSig;
  static String clockSig;
  static int noLetterTime;
  static ArrayList<String> finishedPlayers;
  static int turnNum;
  static int currentPlayer;
  static long startTime;
  static int maxTime;
  static Set<Integer> revealedLetterIndices;
  static int wordsCount;
  static ClockThread clockThread;


  public static void main(String[] args) throws IOException
  {
    wordsCount = 231;
    threads = new ArrayList<ServerThread>();
    wordsList = new ArrayList<String>();
    createListOfWords();
    currentWord = wordsList.get((int)(Math.random()*wordsList.size()));
    finishedPlayers = new ArrayList<String>();
    revealedLetterIndices = new HashSet<Integer>();
    turnNum = 1;
    currentPlayer = 0;
    maxTime = 60000;
    noLetterTime = 30000;


    ServerSocket server = new ServerSocket(9001); //start server on port 9001
    messageSig = "MESSAGE ";
    broadcastSig = "BROADCAST ";
    lineSig = "LINE ";
    nameSig = "NAME ";
    clearSig = "CLEAR ";
    wordSig = "WORD "; //WORD BUTTERFLY
    letterSig = "LETTER "; //LETTER A 3
    scoreSig = "SCORE "; //SCORE 500
    clockSig = "CLOCK "; //CLOCK 30
    int i = 0;

    while (true)
    {
      Socket socket = server.accept();
      System.out.println("Found client");
      ServerThread serverThread = new ServerThread(socket, i);
      threads.add(serverThread);

      while (threads.get(i).getPlayerName() == null) {

        try {
          Thread.sleep(1);
        }
        catch (InterruptedException e)
        {
          break;
        }
      }

      //System.out.println(threads.get(currentPlayer).getPlayerName() + ": Name not null now");
      serverThread.send("BROADCAST TURN " + turnNum + " " + threads.get(currentPlayer).getPlayerName());
      if (i == currentPlayer) {
        threads.get(currentPlayer).send(wordSig + currentWord);
        startTime = System.currentTimeMillis();
        startClockThread(true); //will start clock when first player joins, giving less time to other players
      }
      i++;
      serverThread.send("BROADCAST LETTERCOUNT " + currentWord);
      serverThread.send(scoreSig + 0);


    }
  }

  public static void createListOfWords() throws IOException
  {

    try
    {
      Scanner in = new Scanner(new File("SkribblWords.txt"));
      for (int x = 0; x < wordsCount; x++)
      {
        wordsList.add(in.nextLine().toLowerCase());
      }
    }
    catch(IOException e)
    {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  public static void processInfo(String sig, String message, String from)
  {
    if (sig.equals(messageSig)) {
      System.out.println("processing message sig...");
      ServerThread correctPlayer = threads.get(Integer.parseInt(from));
      if (!finishedPlayers.contains(Integer.toString(correctPlayer.getPlayerNum()))) {
        if (message.equals(currentWord)) {
          sendAll(broadcastSig + "CORRECT " + correctPlayer.getPlayerName());
          finishedPlayers.add(Integer.toString(correctPlayer.getPlayerNum()));
          long timeElapsed = System.currentTimeMillis() - startTime;
          System.out.println(timeElapsed);
          System.out.println(maxTime);
          int score = (int)(((double)(maxTime+noLetterTime-timeElapsed)/(maxTime+noLetterTime))*1000);
          System.out.println("Score: " + score);
          correctPlayer.addPlayerScore(score);
          correctPlayer.send(scoreSig + correctPlayer.getPlayerScore());
          if (finishedPlayers.size() == threads.size()-1) //everyone finished guessing
          {
            endRound();
          }
        }
        else {
          sendAll(messageSig + correctPlayer.getPlayerName() + ": " + message);
        }
      }

    }
    else if (sig.equals(lineSig)) {
        System.out.println("processing line sig...");
        sendAll(lineSig + message);
    } else if (sig.equals(broadcastSig)) {
        System.out.println("received broadcast signal, but clients cannot broadcast");
        //do nothing for now because this case shouldn't arise
    } else if (sig.equals(nameSig)) {
      System.out.println("Name received");
      ServerThread playerThread = threads.get(Integer.parseInt(from));
      playerThread.updatePlayerName(message);
      sendAll(broadcastSig + "HELLO " + playerThread.getPlayerName());
    } else if (sig.equals(clearSig))
    {
      System.out.println("received clear sig...");
      sendAll(clearSig);
    }
  }

  public static void endRound()
  {
    clockThread.end();
    wordsList.remove(currentWord);
    currentWord = wordsList.get((int) (Math.random() * wordsList.size()));
    turnNum++;
    updateCurrentPlayer();
    sendAll("BROADCAST TURN " + turnNum + " " + threads.get(currentPlayer).getPlayerName());
    sendAll("BROADCAST LETTERCOUNT " + currentWord);
    finishedPlayers = new ArrayList<String>();
    revealedLetterIndices = new HashSet<Integer>();
    threads.get(currentPlayer).send(wordSig + currentWord);
    startTime = System.currentTimeMillis();
    startClockThread(false);
  }

  private static void startClockThread(boolean firstTurn)
  {
    int time;
    //if (firstTurn)
      //time = maxTime+30000;
    //else
      time = maxTime;
    clockThread = new ClockThread(currentWord.length(), time, noLetterTime);
  }

  public static void revealLetter()
  {
    int index = (int)(Math.random()*currentWord.length());
    while (revealedLetterIndices.contains(index) || currentWord.substring(index, index+1).equals(" "))
      index = (int)(Math.random()*currentWord.length());
    sendAll(letterSig + currentWord.substring(index, index+1) + " " + index);
    revealedLetterIndices.add(index);
  }

  private static void updateCurrentPlayer()
  {
    currentPlayer++;
    if (currentPlayer == threads.size())
      currentPlayer = 0;
  }

  public static void sendAll(String message)
  {
    for (ServerThread s : threads)
    {
      s.send(message);
    }
  }
}