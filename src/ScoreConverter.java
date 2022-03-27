public class ScoreConverter
{
	//int som innehåller ental, tiotal mm
	private int highscore[] = new int[9];
	//int som tar emot poängen som ska konverteras
	private int convertScore;
	//int som används för att hålla koll på hur många
	//gånger for-loopen ska köras
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
	//funktion för att convertera ett givet tal till ental, tiotal mm..
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
	//funktion för att hämta den konverterade poängen
	public int getConvertedScore(int aScore)
	{
		return highscore[aScore];
	}

//---------------------
	//funktion för att nollställa den converterade poängen
	public void resetScore()
	{
		score = 0;

		for(int i = 0; i < 9; i++)
		{
			highscore[i] = 0;
		}

	}

//---------------------
	//funktion för att skriva ut okonverterade poängen
	public void printScore()
	{

		System.out.println(score);

	}

//----------------------------------------------------

}