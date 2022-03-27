import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuMain extends JFrame implements KeyListener
{

//----------------------------------------------------------------
	//g�r ett objekt av klassen IntroPanel
	private IntroPanel introPanel;
	private GamePanel gamePanel;

//----------------------------------------------------------------

	//Konstruktorn
	public MenuMain()
	{
		//initierar F�nstret
		initFrame();
		setVisible(true);
		getContentPane().setLayout(null);

		//l�gger till en KeyListener
		addKeyListener(introPanel);

		//initierar introPanelen
		introPanel.initPanel();

	}

	//------------------------------------------------------
	//Initierar f�nstret och dess komponenter
	public void initFrame()
	{
		//initiera f�nstret
		setBounds(200, 100, 600, 576);
		setResizable(false);
		setTitle("InvadedSpace");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		//L�gger till en contentPane till IntroPanelen
		introPanel = new IntroPanel(this);
		getContentPane().add(introPanel);
		introPanel.setVisible(true);

		//L�gger till en annan contentPane till GamePanelen

		gamePanel = new GamePanel(this);
		getContentPane().add(gamePanel);
		gamePanel.setVisible(false);



	}

	//startar Introt/Menyn
	public void startIntroMenu()
	{
		introPanel.startPanel();

	}
	//startar Spelet och tar bort Introt/Menyn
	public void startGame()
	{
		getContentPane().remove(introPanel);

		gamePanel.initPanel();
		addKeyListener(gamePanel);
		gamePanel.startPanel();
	}

//----------------------------------------------------------------
//----------------------------------------------------------------
//----------------------------------------------------------------

	//tangentbords lyssnare
	public void keyPressed(KeyEvent event)
	{

	}

	//------------------------------------------------------

	public void keyReleased(KeyEvent event)
	{

	}

	//------------------------------------------------------

	public void keyTyped(KeyEvent event)
	{

	}

//----------------------------------------------------------

}