package manage.reagent.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import manage.reagent.server.Reagent;

/**
* StructureImage fetch images of specified compound structure from Internet.<br>
* All methods are static.<br>
* Image directory: ./image/<br>
* Data source: <a href="http://www.chemicalbook.com/">Chemical Book</a>.<br>
* System must install programs such as imagemagick to enable <i>convert</i> command.
* @author Zekun Zhang
* @version 2015-01-06
*/
public class StructureImage
{
	/**
	* Fetch sturture image and convert it to PNG.
	* @param substance The Reagent Object which it's structure image been fetched.
	*/
	public static void getImage(Reagent substance)
	{
		try
		{
			String urlstr = new String("http://www.chemicalbook.com/CAS/GIF/" +
				substance.getCASNo() + ".gif");
			URL url = new URL(urlstr);
			new File("image").mkdir();
			String imgpath = new String("./image/" + substance.getCASNo() + ".gif");
			File imagefile = new File(imgpath);
			DataInputStream imginput = new DataInputStream(url.openStream());
			FileOutputStream imgoutput = new FileOutputStream(imagefile);
			byte[] buff = new byte[1024];
			int length;
			while((length = imginput.read(buff)) > 0)
				imgoutput.write(buff, 0, length);
			imginput.close();
			imgoutput.close();
			Process process = Runtime.getRuntime().exec("convert ./image/" +
				substance.getCASNo() +".gif ./image/" + substance.getCASNo() + ".png");
			process.waitFor();
		}
		catch(MalformedURLException e1)
		{
			System.out.println("Cannot download image, try again.");
		}
		catch(FileNotFoundException e2)
		{
			System.out.println("Cannot open file, try again.");
		}
		catch(IOException e3)
		{
			System.out.println("Error with image file, try again");
		}
		catch(InterruptedException e4)
		{
			System.out.println("Error converting image format, try again");
		}
	}
	
	/**
	* Remove sturture specified image.
	* @param CASNo The CAS Number in int[3] which it's structure image been removed.
	*/
	public static void removeImage(int[] CASNo)
	{
		File imagefile = new File("./image/" + CASNo + ".gif");
		imagefile.delete();
		imagefile = new File("./image/" + CASNo + ".png");
		imagefile.delete();
	}
}
		
