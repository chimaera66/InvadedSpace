import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IntroPanel extends JPanel implements KeyListener
{

//----------------------------------------------------------------
	//Grafiken

	//Svart Bakgrund
	private Image imageBackground;

	//Stjärn Bakgrunder
	private Image imageBackgroundStars[];

	//siffror
	private Image imageHighscoreNumbers[];

	//Menytext + markör
	private Image imageMainCursor;
	private Image imageStartGame;
	private Image imageExit;

	//Bakgrundstext
	private Image imageR_Highscore;
	private Image imageBodySoftware;
	private Image imageAllRightsReserved;

	//InsertCoin
	private Image imageInsertCoin;

	//Buffert bild
	private Image bufferImage;

	//Graphics
	Graphics bufferGraphics;


	//boolean som håller koll på om bilderna är laddade
	private boolean imagesLoaded = false;
//--------------------------------
	//Musik komponenter

	//Skapar ett objekt av sounds klassen
	private Sounds introMusic = new Sounds();

	private Sounds menu_sfx2 = new Sounds();
	private Sounds menu_sfx1 = new Sounds();

//----------------------------------
	//Positions bestämmare

	//markörens start position i pixlar
	private int cursorPosition = 314;

	//bakgrundstextens(BGText) position
	private int bgTextYPosition = 600;

	//markörens start position på skärmen (1 Start, 2 Highscores, 3 Exit)
	private int position = 1;

//-----------------------
	//highscore grejer

	//highscores x position
	final int highscoreX2 = 217;
	private int highscoreX = highscoreX2;

	//gör object av highscore
	Highscore highscore;
	//gör object av Poäng Konverteraren
	ScoreConverter scoreConverter = new ScoreConverter();

	//gör en int som innehåller highscore siffrorna
	private int intHighscore[] = new int[9];

//----------------------------------
	//Animerings bestämmare
	private int animateStars = 0;

//----------------------------------
	//Tråden som hanterar text scrollningen
	private ThreadIntroPanel threadBGTextScroll;

	//Tråden som hanterar animationen av bakgrunds stjärnorna
	private ThreadIntroPanel threadBGStars;

	//Tråden som hanterar InsertCoin "Animeringen"
	private ThreadIntroPanel threadInsertCoin;

//----------------------------------
	//laddar objekt
	MenuMain menuMain;

//----------------------------------------------------------------

	//konstruktor
	public IntroPanel(MenuMain aMenuMain)
	{
		menuMain = aMenuMain;
		setBounds(0, 0, 600, 576);
	}

//------------------------------------------------------
	//initierar panelen
	public void initPanel()
	{
		//skapar bild-bufferten
		bufferImage = createImage(600,576);
		bufferGraphics = bufferImage.getGraphics();

		//laddar bilder
		loadImages();
		//kör igång panelen
		startPanel();

	}
	//kör igång panelen
	public void startPanel()
	{
		setVisible(true);

		setListenerChoice("BGText");

		//laddar highscore
		loadHighscore();

		//startar musiken
		introMusic.getMusic("music_Menu.wav");

		//startar text-scrollen och stjärn animeringen
		startBGTextScroll();
		startBGStars();

		//keylistenern på
		setListenerStatus(true);
	}

	//stänger av panelern
	public void hidePanel()
	{
		//stänger stjärn animeringen
		killBGStars();

		//stoppar musiken
		introMusic.stop();

		//keylistener av
		setListenerStatus(false);

		//panelen gömd
		setVisible(false);

	}

//------------------------------------------------------

	//Läser in bilder
	public void loadImages()
	{

		//läser in bakgrundsbildern
		ImageIcon tempIcon = new ImageIcon("images/background.gif");
		imageBackground = tempIcon.getImage();

		//läser in stjärnbakgrunder
		imageBackgroundStars = new Image[3];


		//laddar siffror
		imageHighscoreNumbers = new Image[10];

		for(int i = 0; i < 10; i++)
		{
			tempIcon = new ImageIcon("images/fonts/" + i + ".gif");
			imageHighscoreNumbers[i] = tempIcon.getImage();
		}

		//laddar bakgrundsstjärnor
		for (int picnr = 0; picnr < imageBackgroundStars.length; picnr++)
		{
			tempIcon = new ImageIcon("images/mainmenu_stars01_" + picnr + ".gif");
			imageBackgroundStars[picnr] = tempIcon.getImage();
		}

		//läser in menytexten + markören
		tempIcon = new ImageIcon("images/mainmenu_cursor.gif");
		imageMainCursor = tempIcon.getImage();

		tempIcon = new ImageIcon("images/mainmenu_start.gif");
		imageStartGame = tempIcon.getImage();

		tempIcon = new ImageIcon("images/mainmenu_exit.gif");
		imageExit = tempIcon.getImage();

		//läser in bakgrundstexten
		tempIcon = new ImageIcon("images/mainmenu_r_highscore.gif");
		imageR_Highscore = tempIcon.getImage();

		tempIcon = new ImageIcon("images/mainmenu_2002bodysoftware.gif");
		imageBodySoftware = tempIcon.getImage();

		tempIcon = new ImageIcon("images/mainmenu_allrightsreserved.gif");
		imageAllRightsReserved = tempIcon.getImage();

		//läser in InsertCoin Texten
		tempIcon = new ImageIcon("images/mainmenu_insertcoin.gif");
		imageInsertCoin = tempIcon.getImage();


		//allting laddat
		imagesLoaded = true;

		//debugging
		if(imagesLoaded)
		{
			System.out.println("IntroPanel Images Loaded");
		}

	}

	//------------------------------------------------------
	//Sträng som innehåller information om vilken grafikdel
	//som ska ritas ut på skärmen
	String screen;

	//Ritar ut alla bilder som lästes in i föregående metod
	public void paintScreen(String aScreen)
	{
		screen = aScreen;

		//om bilderna är laddade, rita ut bilderna
		if(imagesLoaded)
		{
			//Bakgrundstexten
			if (screen == "BGText")
			{
				//bakgrunden
				bufferGraphics.drawImage(imageBackground, 0, 0, this);
				//bakgrundsstjärnorna
				bufferGraphics.drawImage(imageBackgroundStars[animateStars], 0, 0, this);
				//röd highscore text
				bufferGraphics.drawImage(imageR_Highscore, 217, bgTextYPosition, this);

				//Highscore poängen
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[8]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[7]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[6]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[5]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[4]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[3]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[2]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[1]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[0]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;

				highscoreX = highscoreX2;

				//texten längst ner
				bufferGraphics.drawImage(imageBodySoftware, 104, bgTextYPosition + 427, this);
				bufferGraphics.drawImage(imageAllRightsReserved, 142, bgTextYPosition + 465, this);
			}
			//Insert Coin skärmen
			else if (screen == "InsertCoin")
			{
				//bakgrunden
				bufferGraphics.drawImage(imageBackground, 0, 0, this);
				//bakgrundsstjärnorna
				bufferGraphics.drawImage(imageBackgroundStars[animateStars], 0, 0, this);
				//röd highscore text
				bufferGraphics.drawImage(imageR_Highscore, 217, bgTextYPosition, this);

				//Highscore poängen
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[8]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[7]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[6]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[5]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[4]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[3]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[2]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[1]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[0]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;

				highscoreX = highscoreX2;

				//texten längst ner
				bufferGraphics.drawImage(imageBodySoftware, 104, bgTextYPosition + 427, this);
				bufferGraphics.drawImage(imageAllRightsReserved, 142, bgTextYPosition + 465, this);
				//InsertCoin blinken
				bufferGraphics.drawImage(imageInsertCoin, 198, 303, this);
			}
			//Menyvalen
			else if (screen == "Choices")
			{
				//bakgrunden
				bufferGraphics.drawImage(imageBackground, 0, 0, this);
				//bakgrundsstjärnorna
				bufferGraphics.drawImage(imageBackgroundStars[animateStars], 0, 0, this);
				//röd highscore text
				bufferGraphics.drawImage(imageR_Highscore, 217, bgTextYPosition, this);

				//Highscore
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[8]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[7]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[6]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[5]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[4]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[3]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[2]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[1]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;
				bufferGraphics.drawImage(imageHighscoreNumbers[intHighscore[0]], highscoreX, bgTextYPosition + 20, this);
				highscoreX += 19;

				highscoreX = highscoreX2;

				//texten längst ner
				bufferGraphics.drawImage(imageBodySoftware, 104, bgTextYPosition + 427, this);
				bufferGraphics.drawImage(imageAllRightsReserved, 142, bgTextYPosition + 465, this);
				//menyn
				bufferGraphics.drawImage(imageStartGame, 255, 314, this);
				bufferGraphics.drawImage(imageExit, 255, 354, this);
				//markören
				bufferGraphics.drawImage(imageMainCursor, 217, cursorPosition, this);
			}

			//rensa skärmen
			else if (screen == "ClearScreen")
			{
				bufferGraphics.drawImage(imageBackground, 0, 0, this);
			}

		}

	}

	//Ritar ut allt i bufferten till skärmen
	public void paint(Graphics g)
	{
		super.paint(g);
		if(imagesLoaded)
		{
			g.drawImage(bufferImage, 0, 0, this);
		}
	}

//---------------------
	//laddar highscore poängen
	public void loadHighscore()
	{
		//nollställer scoreConvertern
		scoreConverter.resetScore();

		//laddar highscore poängen från fil till ett Highscore objekt
		highscore = (Highscore) DiskObjectIO.readFromFile("hiscore.hsc");

		//converterar highscore poängen till ental, tiotal osv.
		scoreConverter.convertScore(highscore.getHighscore());
		scoreConverter.printScore();

		//lägger highscoren i en array så att man kan
		//använda den för att skriva ut fonterna
		for(int i = 0; i < 9; i++)
		{
			intHighscore[i] = scoreConverter.getConvertedScore(i);
		}

	}

//----------------------------------------------------------------

	//Ändrar den bild i stjärnbakgrundens animation som visas
	public void animateBGStars()
	{
		if(animateStars == 0)
		{
			animateStars = 1;
		}
		else if(animateStars == 1)
		{
			animateStars = 2;
		}
		else if(animateStars == 2)
		{
			animateStars = 0;
		}

		//om menylyssnaren är Choices så ritas
		//stjärnbakgrunden ut kontinuerligt
		if(listenerChoice == "Choices")
		{
			paintScreen("Choices");
			repaint();
		}

		//skriver ut vilken stjärnanimations bild som visas(debugging)
		System.out.println("Stars Animation Frame: " + animateStars);

	}

	//------------------------------------------------------
	//Hämtar BGText's position
	public int getBGTextY()
	{
		return bgTextYPosition;
	}

	//Ändrar positionen på BGText
	public void setBGTextY(int y)
	{
		bgTextYPosition = y;

		System.out.println("BGText Y Position: " + bgTextYPosition);

		//Om positionen är 30, stäng av scrollningen och
		//starta InsertCoin tråden
		if (bgTextYPosition <= 30)
		{
			//stänger text scrollen
			killBGTextScroll();
			//Ändrar keylistenern
			setListenerChoice("InsertCoin");
			//startar insertcoin tråden
			startInsertCoin();
		}
	}

	//------------------------------------------------------

	//Hämtar markörens position
	public int getCursorPosition()
	{
		return cursorPosition;
	}

	//bestämmer markörens position
	public void setCursorPosition(int y)
	{
		//Position 1, Start
		if (y == 1)
		{
			cursorPosition = 314;
		}
		//Position 2, Exit
		else if (y == 2)
		{
			cursorPosition = 354;
		}

		repaint();

	}

	//------------------------------------------------------
	//startar textscroll tråden
	public void startBGTextScroll()
	{
		//nollställer bakgrundstextens position
		bgTextYPosition = 600;

		threadBGTextScroll = new ThreadIntroPanel(0, "BGText", this);
		threadBGTextScroll.start();
	}
	//stänger textscroll tråden
	public void killBGTextScroll()
	{
		threadBGTextScroll.kill();
	}

	//--------------------------
	//startar stjärnanimeringen
	public void startBGStars()
	{
		threadBGStars = new ThreadIntroPanel(300, "AnimateStars", this);
		threadBGStars.start();
	}
	//stänger stjärnanimeringen
	public void killBGStars()
	{
		threadBGStars.kill();
	}

	//--------------------------
	//startar insertcoin blinken
	public void startInsertCoin()
	{
		threadInsertCoin = new ThreadIntroPanel(350, "InsertCoin", this);
		threadInsertCoin.start();
	}
	//stänger insertcoin blinken
	public void killInsertCoin()
	{
		threadInsertCoin.kill();
	}

//----------------------------------------------------------------
//----------------------------------------------------------------
//----------------------------------------------------------------
	//sätter vilken keylistener som ska användas
	public void setListenerChoice(String aListenerChoice)
	{
		listenerChoice = aListenerChoice;
	}
	//sätter om keylistenern ska vara av eller på
	public void setListenerStatus(boolean aGo)
	{
		go = aGo;
	}
	//-------------------------------

	//Ska keylistenern vara av eller på?
	boolean go = false;

	//Bestämmer vilken lyssnare som ska användas
	private String listenerChoice = "BGText";

	//tangentbords lyssnare
	public void keyPressed(KeyEvent event)
	{
		if(go)
		{
			//--------------------

			//KeyListener 1, stänger textscrollningen med
			//Enter
			if(listenerChoice == "BGText")
			{
				//Enter
				if(event.getKeyCode() == 10)
				{
					//När användaren trycker på Enter
					//stängs tråden ner och KeyListenern byter
					//lyssnare
					setListenerChoice("InsertCoin");

					//Sätter BGText's position till 60
					setBGTextY(30);

					//Ritar ut bakgrunden och BGText
					paintScreen("BGText");
					repaint();
				}
			}
			//InsertCoin menyn

			//--------------------
			//KeyListener 2, stänger av InsertCoin blinken
			//och laddar menyn med
			//Enter
			else if(listenerChoice == "InsertCoin")
			{
				//Enter
				if(event.getKeyCode() == 10)
				{
					//Stänger tråden med InsertCoin "blinken"
					killInsertCoin();
					setListenerChoice("Choices");

					//Ritar ut bakgrunden, BGText
					//Meny valen samnt markören
					paintScreen("Choices");
					repaint();
				}
			}

			//--------------------

			//KeyListener 3, flyttar markören och markerar
			//ett val samt stänger ner introPanel och
			//startar spelet/stänger av programmet med
			//Enter
			else if(listenerChoice == "Choices")
			{
				//Uppåt-Pil
				if(event.getKeyCode() == 38)
				{
					menu_sfx1.getSFX("menu_sfx1.wav");

					//Om markören är på position 1,
					//flytta till position 3
					if(position == 1)
					{
						position = 2;
					}
					//Om markören är på position 2,
					//flytta till position 1
					else if(position == 2)
					{
						position = 1;
					}

					//Skicka nya markör positionen så markören
					//byter Y position
					setCursorPosition(position);
					//Ritar ut nya positionen i bufferten
					paintScreen("Choices");
					//Ritar ut på skärmen
					repaint();
				}
				//Neråt-Pil
				else if(event.getKeyCode() == 40)
				{
					menu_sfx1.getSFX("menu_sfx1.wav");

					//Om markören är på position 1,
					//flytta till position 2
					if(position == 1)
					{
						position = 2;
					}
					//Om markören är på position 2,
					//flytta till position 3
					else if(position == 2)
					{
						position = 1;
					}

					//Skicka nya markör positionen så markören
					//byter Y position
					setCursorPosition(position);
					//Ritar ut nya positionen i bufferten
					paintScreen("Choices");
					//Ritar ut på skärmen
					repaint();
				}

				//Enter
				else if(event.getKeyCode() == 10)
				{
					//Om positionen är 1 när användare trycker Enter,
					//Starta spelet
					if(position == 1)
					{

						//Spelar ljudeffekt
						menu_sfx2.getSFX("menu_sfx2.wav");

						//gömmer panelen
						hidePanel();
						menuMain.startGame();

						System.out.println();
						System.out.println("Start Game");

					}
					//Om positionen är 2 när användare trycker Enter,
					//Avsluta programmet
					else if(position == 2)
					{
						//gömmer panelen
						hidePanel();

						System.out.println();
						System.out.println("Exit");
						System.out.println();
						System.out.println("InvadedSpace v1.05 beta");
						System.out.println("Av: Richard Bengtsson, riba01");
						System.out.println("& : Peter Brunni, pebr01");

						System.exit(0);
					}
				}

				//--------------------
			}

			//Skriver ut KeyCode samt markörens YPosition
			System.out.println();
			System.out.println("KeyCode:           " + event.getKeyCode());
			//Skriver ut Markörens position samt Y position
			System.out.println("Cursor Position:   " + position);
			System.out.println("Cursor Y Position: " + getCursorPosition());

			//KeyCode 37 = VänsterPil
			//KeyCode 39 = HögerPil
			//KeyCode 38 = UppåtPil
			//KeyCode 40 = NeråtPil
			//KeyCode 10 = Enter
			//KeyCode 32 = Space

		}

	}

	//------------------------------------------------------

	public void keyReleased(KeyEvent event)
	{
		if(go)
		{

		}
	}

	//------------------------------------------------------

	public void keyTyped(KeyEvent event)
	{
		if(go)
		{

		}
	}

//----------------------------------------------------------------

}