import java.util.*;

public class ThreadGamePanel extends Thread
{

	private long delay;
	private GamePanel gamePanel;
	private boolean go;

//------------------------------------------------------
	//konstruktor, tar emot referensobjekt

	public ThreadGamePanel (long aDelay, GamePanel t)
	{
		delay = aDelay;
		gamePanel=t;
	}

//------------------------------------------------------
	//startar så länge "go" är sant

	public void run()
	{
		go=true;
		while(go)
		{
			try
			{
				sleep(35);
			}

			//---------------------------

			catch(InterruptedException e)
			{
				System.out.println(e);
			}

			//anropar metoder i referensobjektet

			gamePanel.checker();

			gamePanel.gameOver();

			gamePanel.paintScreen();
			gamePanel.repaint();
		}

		gamePanel=null;
	}

	//------------------------------------------------------

	//stänger tråden
	public void kill()
	{

		go=false;

	}

//-----------------------------------------------------------------------------

}