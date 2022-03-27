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


	//boolean som håller koll på om bilderna är laddade
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

	//gör object av highscore
	Highscore highscore;
	//new highscore
	Highscore newHighscore;
	//gör object av Poäng Konverteraren
	ScoreConverter scoreConverter = new ScoreConverter();

	//gör en int som innehåller highscore siffrorna
	private int intHighscore[] = new int[9];

	//Poängvariabler
	private int score = 0;

//-----------------------
	//ammo grejer

	private int ammoYPosition = 500;
	private boolean shotPassed = true;

	//highscore x position
	final int ammoXPosition2 = 534;
	private int ammoXPosition = ammoXPosition2;

	//gör object av Poäng Konverteraren
	ScoreConverter ammoConverter = new ScoreConverter();

	//gör en int som innehåller highscore siffrorna
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
	//kör igång panelen
	public void startPanel()
	{
		setVisible(true);

		//laddar vapen
		shotsLeft = 15;
		ammoConverter.resetScore();
		ammoConverter.convertScore(shotsLeft);

		//startar musiken
		gameMusic.getMusic("music_InGame.wav");

		//startar trådar
		startEnemyThread();
		startRepaintThread();

		//keylistenern på
		setListenerStatus(true);

	}
	//stänger av panelern
	public void hidePanel()
	{
		//Stänger Enemy tråden
		killEnemyThread();
		//Stänger Repaint tråden
		killRepaintThread();

		//stoppar musiken
		gameMusic.stop();

		//keylistener av
		setListenerStatus(false);

		//panelen gömd
		setVisible(false);
	}

//--------------------------------------------------------
	//löser in bilder
	public void loadImages()
	{

		//löser in bakgrundsbilden
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

		//tar hand om inläsning av spelare
		tempIcon = new ImageIcon("images/game_player.gif");
		imagePlayer = tempIcon.getImage();

		//tar hand om inläsning av fienden
		tempIcon = new ImageIcon("images/game_enemy.gif");
		imageEnemy = tempIcon.getImage();

		//tar hand om inläsning av spelarens skott
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
	//Ritar ut alla bilder som lästes in i föregående metod

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

	//Ritar ut allt i bufferten till skärmen
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


	//initierar och startar skottråden vid tryck på spacetangent (se keylistener)
	public void playerShot()
	{
		playerShot = new ThreadPlayerShot(25, this);
		playerShot.start();

	}

	//returnerar y-koordinaten för spelarens ivägskjutna skott
	public int getPlayerShotY()
	{
		return playerShotY;
	}

	//returnerar x-koordinaten för spelarens ivägskjutna skott
	public int getPlayerShotX()
		{
			return playerShotX;
	}

	//flyttar, med hjälp av skottråden, på skottet i y-led
	public void setPlayerShotY(int shotMove)
	{
		playerShotY += shotMove;
	}

	//stänger av utritningen av skott när kraven i skottråden uppfyllts
	public void killPlayerShot()
	{
		ritaSkott = 0;
		playerShotY = 470;
	}

	//Ändrar hit-booleanen efter kollision som inträffat upphör
	public void setHit()
	{
		hit = false;
	}

	//sätter "skott har passerat"-booleanen till sant
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

	//debugmetod som skriver ut i promten när kollision mellan skott och fiende inträffar
	public void hit()
	{
		System.out.println("Hit");
		setHit();
	}

	//metod som kontrollerar ifall alla fienden är skjutna och, om det är sant och spelaren har skott kvar, belönar spelaren nya skott
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

	//metod som sätter poängen
	public void setScore(int aScore)
	{
		score = aScore;
	}

	//returnerar poängen
	public int getScore()
	{
		return score;
	}

//---------------------------------------------------------

	//kollisionshantering som kontrollerar ifall skott och fiende befinner sig inom samma område. när detta inträffar räknas det som träff och då utförs de lämpliga handlingarna.
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


	//kontrollerar vid "game over" om spelaren har uppnått en högre poäng än tidigare högsta ihopspelade summa.
	//om det stämmer så skriver programmet in den nya "high score":n i en fil som håller poängen även vid programavslut
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

	//boolean som håller reda på ifall spelaren har möjlighet att fortsätta spelet
	boolean gameOver = false;

	//kontrollerar ifall spelaren får fortsätta, om inte så avslutas spelet
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
	//startar "repaint"-tråden
	public void startRepaintThread()
	{
		repaintThread = new ThreadGamePanel(0, this);
		repaintThread.start();
	}

	//stänger "repaint"-tråden
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

	//stänger enemythread
	public void killEnemyThread()
	{
		enemyThread.kill();
	}

//----------------------------------------------------------------
//----------------------------------------------------------------
//----------------------------------------------------------------
	//sätter om keylistenern ska vara av eller på
	public void setListenerStatus(boolean aGo)
	{
		go = aGo;
	}
	//-------------------------------
	//Metod för att ändra KeyListener
	public void setKeyListener(String aListener)
	{
		listenerChoice = aListener;
	}

	//Vilken keylistener används?
	private String listenerChoice = "Game";

	//Ska keylistenern vara av eller på?
	boolean go = false;

	//Keylistener
	public void keyPressed(KeyEvent event)
	{
		if(go)
		{
			if(listenerChoice == "Game")
			{
				// vänsterpil kod 37
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

				//högerpil kod 39
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