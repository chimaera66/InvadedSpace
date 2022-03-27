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

//kör tråd, så länge som "go" är sant, med förutbestämt delay
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

			//tar emot det utav spelaren ivägskjutna skotts y-position
			YPosition2 = gamePanel.getPlayerShotY();

			//---------------------------

			//stänger skottråden om skottet passerat viss position, i detta fall y-koordinaten 1
			if (YPosition2 <= 1)
			{
				gamePanel.setShotPassed();
				kill();
			}
		}

		gamePanel=null;
	}

	//------------------------------------------------------

	//stänger tråden
	public void kill()
	{
		go=false;

		//"stänger ner" skottet
		gamePanel.killPlayerShot();
	}

//-----------------------------------------------------------------------------

}