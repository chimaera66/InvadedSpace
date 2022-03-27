public class EnemyMover
{

//-----------------------------------------------------------------------------
	//EnemyClass Matrisen
	private EnemyClass[][] enemyClass;

	//boolean som kollar vilket håll de rör sig (false = left)
	private boolean moveDir = false;

	//max och min värden för rörelse
	private int maxLeft = 12;
	private int maxRight = 542;

//-----------------------------------------------------------------------------

	public EnemyMover(EnemyClass[][] _enemyClass){

		enemyClass = _enemyClass;
	}

	//------------------------------------------------------
	public void move()
	{
		//om flytt till vänster
		if (moveDir == false)
		{
			for (int i = 0; i < enemyClass.length; i++)
			{
				for (int ii = 0; ii < 1; ii++)
				{
					if (enemyClass[i][ii] != null)
					{
						if (enemyClass[i][ii].getEnemyXPosition() > maxLeft)
						{
							for (int index = 0; index < 1; index++)
							{
								for (int index2 = 0; index2 < enemyClass.length; index2++)
								{
									if (enemyClass[index2][index] != null)
									{
										enemyClass[index2][index].setEnemyXPosition(-2);

									}
								}
							}
						}
						else
						{
							moveDir = true;
						}

						break;
					}
				}
			}
		}
		//om flytt till höger
		else
		{
			for (int i = 0; i < enemyClass.length; i++)
			{
				for (int ii = 0; ii < 1; ii++)
				{
					if (enemyClass[i][ii] != null)
					{
						if (enemyClass[i][ii].getEnemyXPosition() < maxRight)
						{
							for (int index = 0; index < 1; index++)
								{
									for (int index2 = 0; index2 < enemyClass.length; index2++)
									{
										if (enemyClass[index2][index] != null)
										{
											enemyClass[index2][index].setEnemyXPosition(+2);

										}
									}
								}
							}
							else
							{
								moveDir = false;
							}
						}

						break;

					}
				}
			}
		}

//-----------------------------------------------------------------------------

}