import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements KeyListener
{

//------------------------------------------------------------------------

//Grafiken

	//Svart Bakgrund
	private Image imageBackground;

	//siffror
	private Image imageNumbers[];

	//text
	private Image imageGameOver;

	//Skeppen
	private Image imagePlayer;
	private Image imageEnemy;
	private EnemyClass[][] enemyClass;

	//Skott
	private Image imagePlayerShot;
	private Image imageEnemyShot;


	//Buffert bild
	private Image bufferImage;

	//Graphics
	Graphics bufferGraphics;


	//boolean som h�ller koll p� om bilderna �r laddade
	private boolean imagesLoaded = false;
//---------------------------------
	//Musikkomponenter

	//Skapar ett objekt av sounds klassen
	private Sounds gameMusic = new Sounds();

	private Sounds game_sfx1 = new Sounds();
	private Sounds game_sfx2 = new Sounds();

	private Sounds game_over = new Sounds();

//---------------------------------
	//Laddar objekt
	MenuMain menuMain;

	private ThreadGamePanel repaintThread;
	private ThreadEnemyMove enemyThread;
	private ThreadPlayerShot playerShot;
	private EnemyMover enemyMover;

//--------------------------------------------------------------------------
	//highscore grejer

	private int highscoreYPosition = 500;

	//highscores x position
	final int highscoreX2 = 20;
	private int highscoreX = highscoreX2;

	//g�r object av highscore
	Highscore highscore;
	//new highscore
	Highscore newHighscore;
	//g�r object av Po�ng Konverteraren
	ScoreConverter scoreConverter = new ScoreConverter();

	//g�r en int som inneh�ller highscore siffrorna
	private int intHighscore[] = new int[9];

	//Po�ngvariabler
	private int score = 0;

//-----------------------
	//ammo grejer

	private int ammoYPosition = 500;
	private boolean shotPassed = true;

	//highscore x position
	final int ammoXPosition2 = 534;
	private int ammoXPosition = ammoXPosition2;

	//g�r object av Po�ng Konverteraren
	ScoreConverter ammoConverter = new ScoreConverter();

	//g�r en int som inneh�ller highscore siffrorna
	private int intAmmo[] = new int[2];

//-----------------------

	//Kollisionsvariabler
	private int playerXPosition = 268;
	private int enemyXPosition;
	private int enemyYPosition;

	//Skottvariabler
	private boolean hit = false;
	private int playerShotX;
	private int playerShotY = 470;
	private int ritaSkott= 0;
	private int shotsLeft;
	private boolean ableToShoot = true;

//--------------------------------------------------------------------------
	//konstruktor
	public GamePanel(MenuMain aMenuMain)
	{
		menuMain = aMenuMain;
		setBounds(0, 0, 600, 576);
	}

//------------------------------------------------------
	//initierar panelen
	public void initPanel()
	{
		//skapar bild-bufferten
		bufferImage = createImage(600,600);
		bufferGraphics = bufferImage.getGraphics();

		//laddar bilder
		loadImages();

		//skapar EnemyClassobjektet
		enemyClass = new EnemyClass[4][1];

		for (int i = 0; i < 1; i++)
		{
			for (int ii = 0; ii < enemyClass.length; ii++)
			{
				enemyClass[ii][i] = new EnemyClass(imageEnemy, 100+(70*ii), 40);
			}
		}

		//skapar enemyMoverobjektet
		enemyMover = new EnemyMover(enemyClass);

	}
	//k�r ig�ng panelen
	public void startPanel()
	{
		setVisible(true);

		//laddar vapen
		shotsLeft = 15;
		ammoConverter.resetScore();
		ammoConverter.convertScore(shotsLeft);

		//startar musiken
		gameMusic.getMusic("music_InGame.wav");

		//startar tr�dar
		startEnemyThread();
		startRepaintThread();

		//keylistenern p�
		setListenerStatus(true);

	}
	//st�nger av panelern
	public void hidePanel()
	{
		//St�nger Enemy tr�den
		killEnemyThread();
		//St�nger Repaint tr�den
		killRepaintThread();

		//stoppar musiken
		gameMusic.stop();

		//keylistener av
		setListenerStatus(false);

		//panelen g�md
		setVisible(false);
	}

//--------------------------------------------------------
	//l�ser in bilder
	public void loadImages()
	{

		//l�ser in bakgrundsbilden
		ImageIcon tempIcon = new ImageIcon("images/background.gif");
		imageBackground = tempIcon.getImage();

		//laddar siffror
		imageNumbers = new Image[10];

		for(int i = 0; i < 10; i++)
		{
			tempIcon = new ImageIcon("images/fonts/" + i + ".gif");
			imageNumbers[i] = tempIcon.getImage();
		}

		//laddar texten GameOver
		tempIcon = new ImageIcon("images/game_gameover.gif");
		imageGameOver = tempIcon.getImage();

		//tar hand om inl�sning av spelare
		tempIcon = new ImageIcon("images/game_player.gif");
		imagePlayer = tempIcon.getImage();

		//tar hand om inl�sning av fienden
		tempIcon = new ImageIcon("images/game_enemy.gif");
		imageEnemy = tempIcon.getImage();

		//tar hand om inl�sning av spelarens skott
		tempIcon = new ImageIcon("images/game_player_laser.gif");
		imagePlayerShot = tempIcon.getImage();


		//allting laddat
		imagesLoaded = true;

		if(imagesLoaded)
		{
			System.out.println("Image loadingstatus " +imagesLoaded);
		}

	}

//---------------------------------------------------------
	//Ritar ut alla bilder som l�stes in i f�reg�ende metod

	public void paintScreen()
	{
		if (imagesLoaded)
		{
			//bakgrunden
			bufferGraphics.drawImage(imageBackground, 0, 0, this);

			//skepp
			bufferGraphics.drawImage(imagePlayer, playerXPosition, 480, this);

			//skott
			if (ritaSkott == 1)
			{
				bufferGraphics.drawImage(imagePlayerShot, playerShotX, playerShotY, this);
			}

			for (int i = 0; i < 1; i++)
			{
				for (int ii = 0; ii < enemyClass.length; ii++)
				{
					if (enemyClass[ii][i]!=null)
					{
						bufferGraphics.drawImage(enemyClass[ii][i].getImageEnemy(), enemyClass[ii][i].getEnemyXPosition(), enemyClass[ii][i].getEnemyYPosition(), this);
					}
				}
			}

			for(int ii = 0; ii < 9; ii++)
			{
				intHighscore[ii] = scoreConverter.getConvertedScore(ii);
			}

			//Highscore
			bufferGraphics.drawImage(imageNumbers[intHighscore[8]], highscoreX, highscoreYPosition + 20, this);
			highscoreX += 19;
			bufferGraphics.drawImage(imageNumbers[intHighscore[7]], highscoreX, highscoreYPosition + 20, this);
			highscoreX += 19;
			bufferGraphics.drawImage(imageNumbers[intHighscore[6]], highscoreX, highscoreYPosition + 20, this);
			highscoreX += 19;
			bufferGraphics.drawImage(imageNumbers[intHighscore[5]], highscoreX, highscoreYPosition + 20, this);
			highscoreX += 19;
			bufferGraphics.drawImage(imageNumbers[intHighscore[4]], highscoreX, highscoreYPosition + 20, this);
			highscoreX += 19;
			bufferGraphics.drawImage(imageNumbers[intHighscore[3]], highscoreX, highscoreYPosition + 20, this);
			highscoreX += 19;
			bufferGraphics.drawImage(imageNumbers[intHighscore[2]], highscoreX, highscoreYPosition + 20, this);
			highscoreX += 19;
			bufferGraphics.drawImage(imageNumbers[intHighscore[1]], highscoreX, highscoreYPosition + 20, this);
			highscoreX += 19;
			bufferGraphics.drawImage(imageNumbers[intHighscore[0]], highscoreX, highscoreYPosition + 20, this);
			highscoreX += 19;

			highscoreX = highscoreX2;

			for(int ii = 0; ii < 2; ii++)
			{
				intAmmo[ii] = ammoConverter.getConvertedScore(ii);
			}

			//Ammo
			bufferGraphics.drawImage(imageNumbers[intAmmo[1]], ammoXPosition, ammoYPosition + 20, this);
			ammoXPosition +=19;
			bufferGraphics.drawImage(imageNumbers[intAmmo[0]], ammoXPosition, ammoYPosition + 20, this);
			ammoXPosition +=19;

			ammoXPosition = ammoXPosition2;

			if(gameOver==true)
			{
				bufferGraphics.drawImage(imageGameOver, 228, 253, this);
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

//------------------------------------------------------

//Metoder med diverse funktioner


	//initierar och startar skottr�den vid tryck p� spacetangent (se keylistener)
	public void playerShot()
	{
		playerShot = new ThreadPlayerShot(25, this);
		playerShot.start();

	}

	//returnerar y-koordinaten f�r spelarens iv�gskjutna skott
	public int getPlayerShotY()
	{
		return playerShotY;
	}

	//returnerar x-koordinaten f�r spelarens iv�gskjutna skott
	public int getPlayerShotX()
		{
			return playerShotX;
	}

	//flyttar, med hj�lp av skottr�den, p� skottet i y-led
	public void setPlayerShotY(int shotMove)
	{
		playerShotY += shotMove;
	}

	//st�nger av utritningen av skott n�r kraven i skottr�den uppfyllts
	public void killPlayerShot()
	{
		ritaSkott = 0;
		playerShotY = 470;
	}

	//�ndrar hit-booleanen efter kollision som intr�ffat upph�r
	public void setHit()
	{
		hit = false;
	}

	//s�tter "skott har passerat"-booleanen till sant
	public void setShotPassed()
	{
		shotPassed = true;
	}

	//flyttar fienden

	public void setEnemyXPosition()
	{
		enemyMover.move();
	}

	//returnerar fiendens x-position
	public int getEnemyXPosition()
	{
		return enemyXPosition;
	}

	//debugmetod som skriver ut i promten n�r kollision mellan skott och fiende intr�ffar
	public void hit()
	{
		System.out.println("Hit");
		setHit();
	}

	//metod som kontrollerar ifall alla fienden �r skjutna och, om det �r sant och spelaren har skott kvar, bel�nar spelaren nya skott
	public void checker()
	{
		if (shotsLeft>=0)
		{

			if (enemyClass[0][0]==null && enemyClass[1][0]==null && enemyClass[2][0]==null && enemyClass[3][0]==null)
			{
				shotsLeft += 5;

				ammoConverter.resetScore();
				ammoConverter.convertScore(shotsLeft);

				System.out.println();
				System.out.println("Ammo1 = " + shotsLeft);
				System.out.print("Ammo2 = ");
				ammoConverter.printScore();

				for (int i = 0; i < 1; i++)
				{
					for (int ii = 0; ii < enemyClass.length; ii++)
					{
						enemyClass[ii][i] = new EnemyClass(imageEnemy, 100+(70*ii), 40);
					}
				}

			}
		}
		else
		{
			ableToShoot = false;
		}
	}

	//metod som s�tter po�ngen
	public void setScore(int aScore)
	{
		score = aScore;
	}

	//returnerar po�ngen
	public int getScore()
	{
		return score;
	}

//---------------------------------------------------------

	//kollisionshantering som kontrollerar ifall skott och fiende befinner sig inom samma omr�de. n�r detta intr�ffar r�knas det som tr�ff och d� utf�rs de d� l�mpliga handlingarna.
	public void collisionDetection()
	{
		if(hit!=true)
		{
			for (int i = 0; i < enemyClass.length; i++)
			{
				if (enemyClass[i][0]!=null)
				{
					if(playerShotX<enemyClass[i][0].getEnemyXPosition()+48 && enemyClass[i][0].getEnemyXPosition()<playerShotX)
					{
						if(enemyClass[i][0].getEnemyYPosition()+30>playerShotY && enemyClass[i][0].getEnemyYPosition()<playerShotY)
						{
							hit=true;
							hit();
							enemyClass[i][0]=null;
							score += 100;

							game_sfx2.getSFX("game_sfx2.wav");

							scoreConverter.resetScore();
							scoreConverter.convertScore(score);

							System.out.println("Score1 = " + score);
							System.out.print("Score2 = ");
							scoreConverter.printScore();

						}
					}
				}
			}
		}
	}

//---------------------------------------------------------


	//kontrollerar vid "game over" om spelaren har uppn�tt en h�gre po�ng �n tidigare h�gsta ihopspelade summa.
	//om det st�mmer s� skriver programmet in den nya "high score":n i en fil som h�ller po�ngen �ven vid programavslut
	public void checkHighscore()
	{

		highscore = (Highscore) DiskObjectIO.readFromFile("hiscore.hsc");

		if(score > highscore.getHighscore())
		{
			newHighscore = new Highscore(score);

			DiskObjectIO.writeToFile("hiscore.hsc", newHighscore);

			System.out.println();
			System.out.println("Highscore     = " + highscore.getHighscore());
			System.out.println("New Highscore = " + newHighscore.getHighscore());
		}

	}


//---------------------------------------------------------

	//boolean som h�ller reda p� ifall spelaren har m�jlighet att forts�tta spelet
	boolean gameOver = false;

	//kontrollerar ifall spelaren f�r forts�tta, om inte s� avslutas spelet
	public void gameOver()
	{
		if(shotsLeft==0 && shotPassed!=false)
		{
			gameMusic.stop();

			if(!gameOver)
			{
				game_over.getSFX("game_over.wav");

				checkHighscore();

				listenerChoice = "GameOver";

				gameOver = true;
			}

		}
	}

//---------------------------------------------------------
	//startar "repaint"-tr�den
	public void startRepaintThread()
	{
		repaintThread = new ThreadGamePanel(0, this);
		repaintThread.start();
	}

	//st�nger "repaint"-tr�den
	public void killRepaintThread()
	{
		repaintThread.kill();
	}

	//--------------------------
	//Startar EnemyThread
	public void startEnemyThread()
	{
		enemyThread = new ThreadEnemyMove(35, this);
		enemyThread.start();
	}

	//st�nger enemythread
	public void killEnemyThread()
	{
		enemyThread.kill();
	}

//----------------------------------------------------------------
//----------------------------------------------------------------
//----------------------------------------------------------------
	//s�tter om keylistenern ska vara av eller p�
	public void setListenerStatus(boolean aGo)
	{
		go = aGo;
	}
	//-------------------------------
	//Metod f�r att �ndra KeyListener
	public void setKeyListener(String aListener)
	{
		listenerChoice = aListener;
	}

	//Vilken keylistener anv�nds?
	private String listenerChoice = "Game";

	//Ska keylistenern vara av eller p�?
	boolean go = false;

	//Keylistener
	public void keyPressed(KeyEvent event)
	{
		if(go)
		{
			if(listenerChoice == "Game")
			{
				// v�nsterpil kod 37
				if (event.getKeyCode() == 37)
				{
					if (ableToShoot!=false)
					{
						if(playerXPosition > 3)
						{
							playerXPosition-=10;
						}
						else if(playerXPosition < 3)
						{
							playerXPosition=3;
						}
					}
				}

				//h�gerpil kod 39
				else if(event.getKeyCode() == 39)
				{
					if (ableToShoot!=false)
					{
						if(playerXPosition < 537)
						{
							playerXPosition+=10;
						}
						else if (playerXPosition > 537)
						{
							playerXPosition=537;
						}
					}

				}

				//spacetangent kod 32
				else if (event.getKeyCode() == 32)
				{
					if (ableToShoot!=false && shotPassed==true)
					{

						playerShotX = playerXPosition + 22;
						ritaSkott = 1;
						shotsLeft -= 1;

						game_sfx1.getSFX("game_sfx1.wav");

						ammoConverter.resetScore();
						ammoConverter.convertScore(shotsLeft);

						System.out.println();
						System.out.println("Ammo1 = " + shotsLeft);
						System.out.print("Ammo2 = ");
						ammoConverter.printScore();

						shotPassed = false;

						playerShot();
					}
				}

			}

			if(listenerChoice == "GameOver")
			{

				//Enter, 10
				if (event.getKeyCode() == 10)
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

			//skriver ut i promten, i debugsyfte, vilken tangent som trycks ner
			System.out.println("KeyPressed          " + event.getKeyCode());

		}
	}


//----------------------------------------------------------------

	public void keyReleased(KeyEvent event)
	{

	}

	//------------------------------------------------------

	public void keyTyped(KeyEvent event)
	{

	}

//----------------------------------------------------------

}