import java.io.*;
import java.util.*;

/**
* Handle the database of reagents. All methods are static.<br>
* @author Zekun Zhang
* @version 2015-01-06
*/

public class ReagentSystem
{
	private static String list = "./list/list.txt";
	private static int i;
	private static ArrayList<Reagent> reagent;
	private static boolean isConcluded;
	private static ObjectInputStream data_in;
	private static ObjectOutputStream data_out;
	
	/**
	* Get detailed information of specified reagent in database.
	* @param CASNo CAS Number of reagent, three segments of number in array int[]
	* @return a compacted description of reagent. For example<br>
	* <i>141-78-6:C4H8O2:ethyl acetate:88.11:500.0 g</i><br>
	* if such reagent is not recorded, would return<br><i>No Such Record.</i>
	*/
	public static String select(int[] CASNo)
	{
		try
		{
			File data_file = new File(list);
			reagent = new ArrayList<Reagent>();
			if(data_file.length() > 0)
			{
				data_in = new ObjectInputStream(new FileInputStream(data_file));
				reagent = (ArrayList<Reagent>)data_in.readObject();
				data_in.close();
			}
			for(i = 0; i < reagent.size(); i ++)
				if(reagent.get(i).getCASNo().equals(CASNo[0] + "-" +
							CASNo[1] + "-" + CASNo[2]))
					break;
		}
		catch(FileNotFoundException e1)
		{
			System.out.println("File not found, try again.");
		}
		catch(IOException e2)
		{
			e2.printStackTrace();
			System.out.println("Error reading/writing file, try again.");
		}
		catch(ClassNotFoundException e3)
		{
			System.out.println("Undefined class, try again.");
		}
		finally
		{
			if(i < reagent.size())
				return reagent.get(i).toCompactString();
			else
				return "No Such Record.";
		}
	}
	
	/**
	* Add a reagent item to the database.
	* @param descript a description string of the reagent. 
	* Each attribute should be separated by ":". For example<br>
	* <i>141:78:6:C4H8O2:ethyl acetate:88.11:500.0</i>
	* @return if database is updated successfully, would return<br>
	* <i>Data Updated.</i><br>
	* if item with the same CAS Number already exists in database, return<br>
	* <i>Already Exists.</i><br>and the database would not be changed.
	*/
	public static String update(String descript)
	{
		try
		{
			File data_file = new File(list);
			ArrayList<Reagent> reagent = new ArrayList<Reagent>();
			if(data_file.length() > 0)
			{
				data_in = new ObjectInputStream(new FileInputStream(data_file));
				reagent = (ArrayList<Reagent>)data_in.readObject();
				data_in.close();
			}
			
			Reagent rgnt = new Reagent(descript);
			isConcluded = false;
			for(i = 0; i < reagent.size(); i ++)
				if(reagent.get(i).getCASNo().equals(rgnt.getCASNo()))
					isConcluded = true;
			if(isConcluded == false)
			{
				reagent.add(rgnt);
				StructureImage.getImage(rgnt);
				data_out = new ObjectOutputStream(new FileOutputStream(data_file));
				data_out.writeObject(reagent);
			}
			data_out.close();
		}
		catch(FileNotFoundException e1)
		{
			System.out.println("File not found, try again.");
		}
		catch(IOException e2)
		{
			System.out.println("Error reading/writing file, try again.");
		}
		catch(ClassNotFoundException e3)
		{
			System.out.println("Undefined class, try again.");
		}
		finally
		{
			if(isConcluded == false)
				return "Data Updated.";
			else
				return "Already Exists.";
		}
	}
	
	/**
	* Remove the record of specified reagent in database.
	* @param CASNo CAS Number of reagent, three segments of number in array int[]
	* @return if the item is removed successfully, would return<br>
	* <i>Data Removed.</i><br>
	* if the reagent is not recorded in database, return<br>
	* <i>No Such Record.</i><br>and the database would not be changed.
	*/
	public static String drop(int[] CASNo)
	{
		try
		{
			File data_file = new File(list);
			ArrayList<Reagent> reagent = new ArrayList<Reagent>();
			if(data_file.length() > 0)
			{
				data_in = new ObjectInputStream(new FileInputStream(data_file));
				reagent = (ArrayList<Reagent>)data_in.readObject();
				data_in.close();
			}
			for(int i = 0; i < reagent.size(); i ++)
				if(reagent.get(i).getCASNo().equals(
					CASNo[0] + "-" + CASNo[1] + "-" + CASNo[2]))
				{
					reagent.remove(i);
					StructureImage.removeImage(CASNo);
					data_out = new ObjectOutputStream(new FileOutputStream(data_file));
					data_out.writeObject(reagent);
					data_out.close();
					break;
				}
					
		}
		catch(FileNotFoundException e1)
		{
			System.out.println("File not found, try again.");
		}
		catch(IOException e2)
		{
			System.out.println("Error reading/writing file, try again.");
		}
		catch(ClassNotFoundException e3)
		{
			System.out.println("Undefined class, try again.");
		}
		finally
		{
			if(i < reagent.size())
				return "Data Removed.";
			else
				return "No Such Record.";
		}
	}
	
	/**
	* Generate PDF file of reagents list according to the database.
	* PDF could be visited at <u>http://<i>server_ip</i>/PDF/list.pdf</u>
	* @return would return<br><i>PDF File Generated</i>if completed.
	*/
	public static String print()
	{
		GeneratePDF.generateTeX();
		GeneratePDF.generatePDF();
		return "PDF File Generated";
	}
}
