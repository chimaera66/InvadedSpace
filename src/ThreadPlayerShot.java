import java.util.*;

public class ThreadPlayerShot extends Thread
{

	private long delay;
	private GamePanel gamePanel;
	private boolean go;
	private int YPosition2;
	private int YPosition = 0;

//------------------------------------------------------

//konstruktor som tar emot referensobjekt
	public ThreadPlayerShot (long aDelay, GamePanel t)
	{
		delay = aDelay;
		gamePanel=t;
	}

//------------------------------------------------------

//k�r tr�d, s� l�nge som "go" �r sant, med f�rutbest�mt delay
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


			//anropar metoder i referensobjekt
			gamePanel.collisionDetection();
			gamePanel.setPlayerShotY(YPosition -= 1);

			//tar emot det utav spelaren iv�gskjutna skotts y-position
			YPosition2 = gamePanel.getPlayerShotY();

			//---------------------------

			//st�nger skottr�den om skottet passerat viss position, i detta fall y-koordinaten 1
			if (YPosition2 <= 1)
			{
				gamePanel.setShotPassed();
				kill();
			}
		}

		gamePanel=null;
	}

	//------------------------------------------------------

	//st�nger tr�den
	public void kill()
	{
		go=false;

		//"st�nger ner" skottet
		gamePanel.killPlayerShot();
	}

//-----------------------------------------------------------------------------

}