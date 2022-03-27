public class ScoreConverter
{
	//int som inneh�ller ental, tiotal mm
	private int highscore[] = new int[9];
	//int som tar emot po�ngen som ska konverteras
	private int convertScore;
	//int som anv�nds f�r att h�lla koll p� hur m�nga
	//g�nger for-loopen ska k�ras
	private int score;

//----------------------------------------------------
	//konstruktorn
	public ScoreConverter()
	{
		for(int i = 0; i < 9; i++)
		{
			highscore[i] = 0;
		}
	}

//----------------------------------------------------
	//funktion f�r att convertera ett givet tal till ental, tiotal mm..
	public void convertScore(int aConvertScore)
	{
		convertScore = aConvertScore;

		score = score + convertScore;

		for(int i = 0; i < score; i++)
		{
			highscore[0]++;
			if(highscore[0] == 10)
			{
				highscore[1]++;
				highscore[0] = 0;
				if(highscore[1] == 10)
				{
					highscore[2]++;
					highscore[1] = 0;
					if(highscore[2] == 10)
					{
						highscore[3]++;
						highscore[2] = 0;
						if(highscore[3] == 10)
						{
							highscore[4]++;
							highscore[3] = 0;
							if(highscore[4] == 10)
							{
								highscore[5]++;
								highscore[4] = 0;
								if(highscore[5] == 10)
								{
									highscore[6]++;
									highscore[5] = 0;
									if(highscore[6] == 10)
									{
										highscore[7]++;
										highscore[6] = 0;
										if(highscore[7] == 10)
										{
											highscore[8]++;
											highscore[7] = 0;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

//----------------------------------------------------
	//funktion f�r att h�mta den konverterade po�ngen
	public int getConvertedScore(int aScore)
	{
		return highscore[aScore];
	}

//---------------------
	//funktion f�r att nollst�lla den converterade po�ngen
	public void resetScore()
	{
		score = 0;

		for(int i = 0; i < 9; i++)
		{
			highscore[i] = 0;
		}

	}

//---------------------
	//funktion f�r att skriva ut okonverterade po�ngen
	public void printScore()
	{

		System.out.println(score);

	}

//----------------------------------------------------

}