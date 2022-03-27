import java.io.*;

/** class that can be used to load and save java objects to file */
public class DiskObjectIO
{


	/**
		writes the sent object to a file with the sent filename
		@param fileName			the name of the file, must also ocntain the path
		@param object				the object to save to file
	*/
	public static void writeToFile(String fileName, Object object)
	{
     	try
     	{
			FileOutputStream outStream = new FileOutputStream(fileName);
     		ObjectOutputStream out = new ObjectOutputStream(outStream);
			out.writeObject(object);
          	outStream.close();
          }
          catch(IOException e)
          {
			System.out.println(e);
          }
	}

	/**
		reads the sent file and convets it to a java object and returns it
		@param fileName			the name of the file, must also ocntain the path
		@return obj				the loaded and converted file
	*/
     public static Object readFromFile(String fileName)
     {
	     try
	     {
	     	FileInputStream inStream = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(inStream);
			Object obj = in.readObject();
			inStream.close();
			return obj;
	     }
         catch(IOException e)
         {
          	System.out.println(e);
         }
		 catch(ClassNotFoundException e)
		 {
          	System.out.println(e);
         }
		 return null;
	}
}