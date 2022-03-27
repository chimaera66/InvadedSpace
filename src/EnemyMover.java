public class EnemyMover
{

//-----------------------------------------------------------------------------
	//EnemyClass Matrisen
	private EnemyClass[][] enemyClass;

	//boolean som kollar vilket h�ll de r�r sig (false = left)
	private boolean moveDir = false;

	//max oxh min v�rden f�r r�relse
	private int maxLeft = 12;
	private int maxRight = 542;

//-----------------------------------------------------------------------------

	public EnemyMover(EnemyClass[][] _enemyClass){

		enemyClass = _enemyClass;
	}

	//------------------------------------------------------
	public void move()
	{
		//om flytt till v�nster
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
		//om flytt till h�ger
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