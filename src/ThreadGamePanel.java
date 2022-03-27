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
	//startar s� l�nge "go" �r sant

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

	//st�nger tr�den
	public void kill()
	{

		go=false;

	}

//-----------------------------------------------------------------------------

}