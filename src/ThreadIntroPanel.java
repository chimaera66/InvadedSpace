import java.util.*;

public class ThreadIntroPanel extends Thread
{

	private long delay;

	//Ska tr�den k�ras?
	//false = nej
	//true = ja
	private boolean go;

	//Inneh�ller info om vilken tr�d som ska visas,
	//skickas fr�n MenuMain
	//BGText = BGText Scrollen
	//InsertCoin = InsertCoin "Blinken"
	private String thread;

	//Objekt
	private IntroPanel introPanel;

//------------------------------------------------------

	//Inneh�ller info om InsertCoin ska visas eller inte
	//0 = Visa inte
	//1 = Visa
	private int show;

//--------------------

	//Y Positionen f�r BGText
	private int bgTextYPosition;

	//Y Positionen f�r bakgrunds stj�rnorna
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
				// metod �rvd fr�n Thread, tar emot en long
				// som bet�mmer hur m�nga millisekunder den skall
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
						//Uppdaterar positionen f�r BGText
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
							//Skriver ut i dosf�nstret(f�r debugging)
							System.out.println();
							System.out.println("Hide InsertCoin");

							//Ritar ut bakgrunden samn BGTexten
							introPanel.paintScreen("BGText");
							introPanel.repaint();

							//�ndrar variabeln f�r om InsertCoin ska visas eller inte
							show = 1;
							break;

						//Om variabeln show = 1, visa InsertCoin
						case 1:
							//Skriver ut i dosf�nstret(f�r debugging)
							System.out.println();
							System.out.println("Show InsertCoin");

							//Ritar ut bakgrunden, BGTexten & InsertCoin
							introPanel.paintScreen("InsertCoin");
							introPanel.repaint();

							//�ndrar variabeln f�r om InsertCoin ska visas eller inte
							show = 0;
							break;
					}
				}

				//-= Animerar stj�rnbakgrunden =-
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

	//St�nger Tr�den
	public void kill()
	{
		go = false;
	}

//------------------------------------------------------

}