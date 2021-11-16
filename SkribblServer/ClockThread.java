public class ClockThread extends Thread
{
    //private long startTime;
    private long maxTime;
    private int numLetters;
    private long noLetterTime;
    private boolean stop;
    private boolean allLettersRevealed;

    public ClockThread(int numLetters, long maxTime, long noLetterTime)
    {
        this.numLetters = numLetters;
        this.maxTime = maxTime;
        this.noLetterTime = noLetterTime;
        this.allLettersRevealed = false;
        this.stop = false;
        start();
    }

    public void end()
    {
        this.stop = true;
    }

    public void run()
    {
        //startTime = System.currentTimeMillis();
        System.out.println("Clock Thread started");
        long timeBeforeLetterRevealed = maxTime/numLetters;
        try {
            System.out.println("Sleeping for " + noLetterTime + " ms");
            Thread.sleep(noLetterTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < numLetters; i++)
        {
            if (stop)
            {
                System.out.println("Stopping clock thread");
                break;
            }
            try {
                System.out.println("Sleeping for " + timeBeforeLetterRevealed + " ms");
                Thread.sleep(timeBeforeLetterRevealed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!stop)
            {
                Server.revealLetter();
                if (i == numLetters - 1) allLettersRevealed = true;
            }
        }
        if (!stop || allLettersRevealed)
            Server.endRound();
    }
}
