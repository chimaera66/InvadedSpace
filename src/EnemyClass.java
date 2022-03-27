import java.awt.Image;

public class EnemyClass {

	//Fiende Bild
	private Image imageEnemy;

	//Fiendevariabler

	private int enemyXPosition;
	private int enemyYPosition;


	//konstruktorn
	//tar emot en bild och fiendevariabler
	public EnemyClass(Image _imageEnemy, int _enemyXPosition, int _enemyYPosition){

		imageEnemy = _imageEnemy;
		enemyXPosition = _enemyXPosition;
		enemyYPosition = _enemyYPosition;

	}

	//returnerar fiendebilden
	public Image getImageEnemy()
	{
		return imageEnemy;
	}

	//returnerar fiendens x-position
	public int getEnemyXPosition()
	{
		return enemyXPosition;
	}

	//returnerar fiendens y-position
	public int getEnemyYPosition()
	{
		return enemyYPosition;
	}

	//sätter fiendens x-position
	public void setEnemyXPosition(int _enemyXPosition)
	{
		enemyXPosition += _enemyXPosition;

	}

	// sätter fiendens y-position
	public void setEnemyYPosition(int _enemyYPosition)
	{
		enemyYPosition += _enemyYPosition;
	}




}