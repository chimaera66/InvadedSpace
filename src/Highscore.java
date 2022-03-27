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

	//method för att hämta highscoren från en annan class
	public int getHighscore()
	{
		return highscore;
	}

}