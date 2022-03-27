import java.util.*;

public class ThreadIntroPanel extends Thread
{

	private long delay;

	//Ska tråden köras?
	//false = nej
	//true = ja
	private boolean go;

	//Innehåller info om vilken tråd som ska visas,
	//skickas från MenuMain
	//BGText = BGText Scrollen
	//InsertCoin = InsertCoin "Blinken"
	private String thread;

	//Objekt
	private IntroPanel introPanel;

//------------------------------------------------------

	//Innehåller info om InsertCoin ska visas eller inte
	//0 = Visa inte
	//1 = Visa
	private int show;

//--------------------

	//Y Positionen för BGText
	private int bgTextYPosition;

	//Y Positionen för bakgrunds stjärnorna
	private int bgStarsPosY;

//------------------------------------------------------

	//Konstruktorn
	public ThreadIntroPanel(long aDelay, String aThread, IntroPanel t)
	{
		delay = aDelay;
		introPanel = t;
		thread = aThread;
	}

//------------------------------------------------------

	public void run()
	{
		go = true;
		show = 1;

		while(go)
		{
			try
			{
				// metod ärvd från Thread, tar emot en long
				// som betämmer hur många millisekunder den skall
				// sova. 1000millisekunder = 1sekund
				sleep(delay);
			}

			//---------------------------

			catch(InterruptedException e)
			{
				System.out.println(e);
			}

			//---------------------------
			if (go)
			{
				//-= BGText Scrollningen =-
				if (thread == "BGText")
				{

					if (go)
					{
						//Uppdaterar positionen för BGText
						//Samt ritar ut den

						bgTextYPosition = introPanel.getBGTextY();

						introPanel.setBGTextY(bgTextYPosition -= 3);

						introPanel.paintScreen("BGText");
						introPanel.repaint();
					}

				}

				//-= InsertCoin "blinken" =-
				else if (thread == "InsertCoin")
				{
					switch (show)
					{
						//Om variabeln show = 0, visa inte InsertCoin
						case 0:
							//Skriver ut i dosfönstret(för debugging)
							System.out.println();
							System.out.println("Hide InsertCoin");

							//Ritar ut bakgrunden samn BGTexten
							introPanel.paintScreen("BGText");
							introPanel.repaint();

							//ändrar variabeln för om InsertCoin ska visas eller inte
							show = 1;
							break;

						//Om variabeln show = 1, visa InsertCoin
						case 1:
							//Skriver ut i dosfönstret(för debugging)
							System.out.println();
							System.out.println("Show InsertCoin");

							//Ritar ut bakgrunden, BGTexten & InsertCoin
							introPanel.paintScreen("InsertCoin");
							introPanel.repaint();

							//ändrar variabeln för om InsertCoin ska visas eller inte
							show = 0;
							break;
					}
				}

				//-= Animerar stjärnbakgrunden =-
				else if(thread == "AnimateStars")
				{
					introPanel.animateBGStars();
				}


			}

			//---------------------------
		}

		introPanel = null;
	}

//------------------------------------------------------

	//Stänger Tråden
	public void kill()
	{
		go = false;
	}

//------------------------------------------------------

}