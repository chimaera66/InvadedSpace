import java.util.*;

public class ThreadEnemyMove extends Thread
{

	private long delay;

	private GamePanel gamePanel;

	private boolean go;

	private int XPosition;
	private int YPosition;

//------------------------------------------------------

	//konstruktor, tar emot referensobjekt
	public ThreadEnemyMove (long aDelay, GamePanel t)
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
				sleep(delay);
			}

			//---------------------------

			catch(InterruptedException e)
			{
				System.out.println(e);
			}

				//anropar metod i referensobjektet
				gamePanel.setEnemyXPosition();
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