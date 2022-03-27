import java.applet.AudioClip;
import java.applet.Applet;
import java.io.*;

public class Sounds
{

//----------------------------------------------------------------
	//Tar emot strängar från de klasser som använder sig av ljud
	private String music;
	private String SFX;

//----------------------------------------------------------------
	//skapar objekt
	private AudioClip audioClip, sfxClip;

//----------------------------------------------------------------

	//konstruktorn
	public Sounds()
	{


	}

	//------------------------------------------------------

	//Spelar upp musik
	public void getMusic(String aMusic)
	{
		music = aMusic;

		File f = new File("sounds/" +music);
		try
		{
		  audioClip = Applet.newAudioClip(f.toURL());
		}
		catch(java.net.MalformedURLException e)
		{
			audioClip = null;
		}
		if(audioClip != null) {
			audioClip.loop();
		}

	}

	//------------------------------------------------------

	//Spelar upp ljudeffekter
	public void getSFX (String aSFX)
	{
			SFX = aSFX;

			File f = new File("sounds/" +SFX);
			try
			{
			  sfxClip = Applet.newAudioClip(f.toURL());
			}
			catch(java.net.MalformedURLException e)
			{
				sfxClip = null;
			}
			if(sfxClip != null)
			{
				sfxClip.play();
			}

	}

	//------------------------------------------------------

	//Stoppar allt ljud
	public void stop()
	{

		audioClip.stop();
	}

	//------------------------------------------------------

}