import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IntroPanel extends JPanel implements KeyListener
{

//----------------------------------------------------------------
	//Grafiken

	//Svart Bakgrund
	private Image imageBackground;

	//Stj�rn Bakgrunder
	private Image imageBackgroundStars[];

	//siffror
	private Image imageHighscoreNumbers[];

	//Menytext + mark�r
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


	//boolean som h�ller koll p� om bilderna �r laddade
	private boolean imagesLoaded = false;
//--------------------------------
	//Musik komponenter

	//Skapar ett objekt av sounds klassen
	private Sounds introMusic = new Sounds();

	private Sounds menu_sfx2 = new Sounds();
	private Sounds menu_sfx1 = new Sounds();

//----------------------------------
	//Positions best�mmare

	//mark�rens start position i pixlar
	private int cursorPosition = 314;

	//bakgrundstextens(BGText) position
	private int bgTextYPosition = 600;

	//mark�rens start position p� sk�rmen (1 Start, 2 Highscores, 3 Exit)
	private int position = 1;

//-----------------------
	//highscore grejer

	//highscores x position
	final int highscoreX2 = 217;
	private int highscoreX = highscoreX2;

	//g�r object av highscore
	Highscore highscore;
	//g�r object av Po�ng Konverteraren
	ScoreConverter scoreConverter = new ScoreConverter();

	//g�r en int som inneh�ller highscore siffrorna
	private int intHighscore[] = new int[9];

//----------------------------------
	//Animerings best�mmare
	private int animateStars = 0;

//----------------------------------
	//Tr�den som hanterar text scrollningen
	private ThreadIntroPanel threadBGTextScroll;

	//Tr�den som hanterar animationen av bakgrunds stj�rnorna
	private ThreadIntroPanel threadBGStars;

	//Tr�den som hanterar InsertCoin "Animeringen"
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
		//k�r ig�ng panelen
		startPanel();

	}
	//k�r ig�ng panelen
	public void startPanel()
	{
		setVisible(true);

		setListenerChoice("BGText");

		//laddar highscore
		loadHighscore();

		//startar musiken
		introMusic.getMusic("music_Menu.wav");

		//startar text-scrollen och stj�rn animeringen
		startBGTextScroll();
		startBGStars();

		//keylistenern p�
		setListenerStatus(true);
	}

	//st�nger av panelern
	public void hidePanel()
	{
		//st�nger stj�rn animeringen
		killBGStars();

		//stoppar musiken
		introMusic.stop();

		//keylistener av
		setListenerStatus(false);

		//panelen g�md
		setVisible(false);

	}

//------------------------------------------------------

	//L�ser in bilder
	public void loadImages()
	{

		//l�ser in bakgrundsbildern
		ImageIcon tempIcon = new ImageIcon("images/background.gif");
		imageBackground = tempIcon.getImage();

		//l�ser in stj�rnbakgrunder
		imageBackgroundStars = new Image[3];


		//laddar siffror
		imageHighscoreNumbers = new Image[10];

		for(int i = 0; i < 10; i++)
		{
			tempIcon = new ImageIcon("images/fonts/" + i + ".gif");
			imageHighscoreNumbers[i] = tempIcon.getImage();
		}

		//laddar bakgrundsstj�rnor
		for (int picnr = 0; picnr < imageBackgroundStars.length; picnr++)
		{
			tempIcon = new ImageIcon("images/mainmenu_stars01_" + picnr + ".gif");
			imageBackgroundStars[picnr] = tempIcon.getImage();
		}

		//l�ser in menytexten + mark�ren
		tempIcon = new ImageIcon("images/mainmenu_cursor.gif");
		imageMainCursor = tempIcon.getImage();

		tempIcon = new ImageIcon("images/mainmenu_start.gif");
		imageStartGame = tempIcon.getImage();

		tempIcon = new ImageIcon("images/mainmenu_exit.gif");
		imageExit = tempIcon.getImage();

		//l�ser in bakgrundstexten
		tempIcon = new ImageIcon("images/mainmenu_r_highscore.gif");
		imageR_Highscore = tempIcon.getImage();

		tempIcon = new ImageIcon("images/mainmenu_2002bodysoftware.gif");
		imageBodySoftware = tempIcon.getImage();

		tempIcon = new ImageIcon("images/mainmenu_allrightsreserved.gif");
		imageAllRightsReserved = tempIcon.getImage();

		//l�ser in InsertCoin Texten
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
	//Str�ng som inneh�ller information om vilken grafikdel
	//som ska ritas ut p� sk�rmen
	String screen;

	//Ritar ut alla bilder som l�stes in i f�reg�ende metod
	public void paintScreen(String aScreen)
	{
		screen = aScreen;

		//om bilderna �r laddade, rita ut bilderna
		if(imagesLoaded)
		{
			//Bakgrundstexten
			if (screen == "BGText")
			{
				//bakgrunden
				bufferGraphics.drawImage(imageBackground, 0, 0, this);
				//bakgrundsstj�rnorna
				bufferGraphics.drawImage(imageBackgroundStars[animateStars], 0, 0, this);
				//r�d highscore text
				bufferGraphics.drawImage(imageR_Highscore, 217, bgTextYPosition, this);

				//Highscore po�ngen
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

				//texten l�ngst ner
				bufferGraphics.drawImage(imageBodySoftware, 104, bgTextYPosition + 427, this);
				bufferGraphics.drawImage(imageAllRightsReserved, 142, bgTextYPosition + 465, this);
			}
			//Insert Coin sk�rmen
			else if (screen == "InsertCoin")
			{
				//bakgrunden
				bufferGraphics.drawImage(imageBackground, 0, 0, this);
				//bakgrundsstj�rnorna
				bufferGraphics.drawImage(imageBackgroundStars[animateStars], 0, 0, this);
				//r�d highscore text
				bufferGraphics.drawImage(imageR_Highscore, 217, bgTextYPosition, this);

				//Highscore po�ngen
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

				//texten l�ngst ner
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
				//bakgrundsstj�rnorna
				bufferGraphics.drawImage(imageBackgroundStars[animateStars], 0, 0, this);
				//r�d highscore text
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

				//texten l�ngst ner
				bufferGraphics.drawImage(imageBodySoftware, 104, bgTextYPosition + 427, this);
				bufferGraphics.drawImage(imageAllRightsReserved, 142, bgTextYPosition + 465, this);
				//menyn
				bufferGraphics.drawImage(imageStartGame, 255, 314, this);
				bufferGraphics.drawImage(imageExit, 255, 354, this);
				//mark�ren
				bufferGraphics.drawImage(imageMainCursor, 217, cursorPosition, this);
			}

			//rensa sk�rmen
			else if (screen == "ClearScreen")
			{
				bufferGraphics.drawImage(imageBackground, 0, 0, this);
			}

		}

	}

	//Ritar ut allt i bufferten till sk�rmen
	public void paint(Graphics g)
	{
		super.paint(g);
		if(imagesLoaded)
		{
			g.drawImage(bufferImage, 0, 0, this);
		}
	}

//---------------------
	//laddar highscore po�ngen
	public void loadHighscore()
	{
		//nollst�ller scoreConvertern
		scoreConverter.resetScore();

		//laddar highscore po�ngen fr�n fil till ett Highscore objekt
		highscore = (Highscore) DiskObjectIO.readFromFile("hiscore.hsc");

		//converterar highscore po�ngen till ental, tiotal osv.
		scoreConverter.convertScore(highscore.getHighscore());
		scoreConverter.printScore();

		//l�gger highscoren i en array s� att man kan
		//anv�nda den f�r att skriva ut fonterna
		for(int i = 0; i < 9; i++)
		{
			intHighscore[i] = scoreConverter.getConvertedScore(i);
		}

	}

//----------------------------------------------------------------

	//�ndrar den bild i stj�rnbakgrundens animation som visas
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

		//om menylyssnaren �r Choices s� ritas
		//stj�rnbakgrunden ut kontinuerligt
		if(listenerChoice == "Choices")
		{
			paintScreen("Choices");
			repaint();
		}

		//skriver ut vilken stj�rnanimations bild som visas(debugging)
		System.out.println("Stars Animation Frame: " + animateStars);

	}

	//------------------------------------------------------
	//H�mtar BGText's position
	public int getBGTextY()
	{
		return bgTextYPosition;
	}

	//�ndrar positionen p� BGText
	public void setBGTextY(int y)
	{
		bgTextYPosition = y;

		System.out.println("BGText Y Position: " + bgTextYPosition);

		//Om positionen �r 30, st�ng av scrollningen och
		//starta InsertCoin tr�den
		if (bgTextYPosition <= 30)
		{
			//st�nger text scrollen
			killBGTextScroll();
			//�ndrar keylistenern
			setListenerChoice("InsertCoin");
			//startar insertcoin tr�den
			startInsertCoin();
		}
	}

	//------------------------------------------------------

	//H�mtar mark�rens position
	public int getCursorPosition()
	{
		return cursorPosition;
	}

	//best�mmer mark�rens position
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
	//startar textscroll tr�den
	public void startBGTextScroll()
	{
		//nollst�ller bakgrundstextens position
		bgTextYPosition = 600;

		threadBGTextScroll = new ThreadIntroPanel(0, "BGText", this);
		threadBGTextScroll.start();
	}
	//st�nger textscroll tr�den
	public void killBGTextScroll()
	{
		threadBGTextScroll.kill();
	}

	//--------------------------
	//startar stj�rnanimeringen
	public void startBGStars()
	{
		threadBGStars = new ThreadIntroPanel(300, "AnimateStars", this);
		threadBGStars.start();
	}
	//st�nger stj�rnanimeringen
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
	//st�nger insertcoin blinken
	public void killInsertCoin()
	{
		threadInsertCoin.kill();
	}

//----------------------------------------------------------------
//----------------------------------------------------------------
//----------------------------------------------------------------
	//s�tter vilken keylistener som ska anv�ndas
	public void setListenerChoice(String aListenerChoice)
	{
		listenerChoice = aListenerChoice;
	}
	//s�tter om keylistenern ska vara av eller p�
	public void setListenerStatus(boolean aGo)
	{
		go = aGo;
	}
	//-------------------------------

	//Ska keylistenern vara av eller p�?
	boolean go = false;

	//Best�mmer vilken lyssnare som ska anv�ndas
	private String listenerChoice = "BGText";

	//tangentbords lyssnare
	public void keyPressed(KeyEvent event)
	{
		if(go)
		{
			//--------------------

			//KeyListener 1, st�nger textscrollningen med
			//Enter
			if(listenerChoice == "BGText")
			{
				//Enter
				if(event.getKeyCode() == 10)
				{
					//N�r anv�ndaren trycker p� Enter
					//st�ngs tr�den ner och KeyListenern byter
					//lyssnare
					setListenerChoice("InsertCoin");

					//S�tter BGText's position till 60
					setBGTextY(30);

					//Ritar ut bakgrunden och BGText
					paintScreen("BGText");
					repaint();
				}
			}
			//InsertCoin menyn

			//--------------------
			//KeyListener 2, st�nger av InsertCoin blinken
			//och laddar menyn med
			//Enter
			else if(listenerChoice == "InsertCoin")
			{
				//Enter
				if(event.getKeyCode() == 10)
				{
					//St�nger tr�den med InsertCoin "blinken"
					killInsertCoin();
					setListenerChoice("Choices");

					//Ritar ut bakgrunden, BGText
					//Meny valen samnt mark�ren
					paintScreen("Choices");
					repaint();
				}
			}

			//--------------------

			//KeyListener 3, flyttar mark�ren och markerar
			//ett val samt st�nger ner introPanel och
			//startar spelet/st�nger av programmet med
			//Enter
			else if(listenerChoice == "Choices")
			{
				//Upp�t-Pil
				if(event.getKeyCode() == 38)
				{
					menu_sfx1.getSFX("menu_sfx1.wav");

					//Om mark�ren �r p� position 1,
					//flytta till position 3
					if(position == 1)
					{
						position = 2;
					}
					//Om mark�ren �r p� position 2,
					//flytta till position 1
					else if(position == 2)
					{
						position = 1;
					}

					//Skicka nya mark�r positionen s� mark�ren
					//byter Y position
					setCursorPosition(position);
					//Ritar ut nya positionen i bufferten
					paintScreen("Choices");
					//Ritar ut p� sk�rmen
					repaint();
				}
				//Ner�t-Pil
				else if(event.getKeyCode() == 40)
				{
					menu_sfx1.getSFX("menu_sfx1.wav");

					//Om mark�ren �r p� position 1,
					//flytta till position 2
					if(position == 1)
					{
						position = 2;
					}
					//Om mark�ren �r p� position 2,
					//flytta till position 3
					else if(position == 2)
					{
						position = 1;
					}

					//Skicka nya mark�r positionen s� mark�ren
					//byter Y position
					setCursorPosition(position);
					//Ritar ut nya positionen i bufferten
					paintScreen("Choices");
					//Ritar ut p� sk�rmen
					repaint();
				}

				//Enter
				else if(event.getKeyCode() == 10)
				{
					//Om positionen �r 1 n�r anv�ndare trycker Enter,
					//Starta spelet
					if(position == 1)
					{

						//Spelar ljudeffekt
						menu_sfx2.getSFX("menu_sfx2.wav");

						//g�mmer panelen
						hidePanel();
						menuMain.startGame();

						System.out.println();
						System.out.println("Start Game");

					}
					//Om positionen �r 2 n�r anv�ndare trycker Enter,
					//Avsluta programmet
					else if(position == 2)
					{
						//g�mmer panelen
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

			//Skriver ut KeyCode samt mark�rens YPosition
			System.out.println();
			System.out.println("KeyCode:           " + event.getKeyCode());
			//Skriver ut Mark�rens position samt Y position
			System.out.println("Cursor Position:   " + position);
			System.out.println("Cursor Y Position: " + getCursorPosition());

			//KeyCode 37 = V�nsterPil
			//KeyCode 39 = H�gerPil
			//KeyCode 38 = Upp�tPil
			//KeyCode 40 = Ner�tPil
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