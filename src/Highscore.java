import java.io.*;

public class Highscore implements Serializable
{
	//
	int highscore;


	//konstruktorn som tar emot highscoren
	public Highscore(int aHighscore)
	{
		highscore = aHighscore;
	}

	//method f�r att h�mta highscoren fr�n en annan class
	public int getHighscore()
	{
		return highscore;
	}

}