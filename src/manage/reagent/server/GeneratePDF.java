package manage.reagent.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import manage.reagent.server.Reagent;

/**
* Generate PDF file of the list of all reagents.<br>
* All methods are static.<br>
* System must install programs such as texlive to enable <i>pdflatex</i> command.<br>
* PDF file would be moved to /var/www/PDF/ for visiting. A http server is required. 
* See ./pdf/compile.sh for more detials.
* @author Zekun Zhang
* @version 2015-01-06
*/
public class GeneratePDF
{
	/**
	* Generate <i>.tex</i> file of reagents list to be compiled by pdfLaTeX.
	*/
	@SuppressWarnings("unchecked")
	public static void generateTeX()
	{
		new File("pdf").mkdir();
		File data_file = new File("./list/list.txt");
		try
		{
			ObjectInputStream in =
				new ObjectInputStream(new FileInputStream(data_file));
			PrintWriter out = new PrintWriter(new File("./pdf/list.tex"));
			ArrayList<Reagent> reagent = (ArrayList<Reagent>)in.readObject();
			Reagent substance = new Reagent();
			for(int i = 0; i < reagent.size(); i ++)
			{
				substance = reagent.get(i);
				out.print("\\item \\textbf{CAS }" + substance.getCASNo() + "\\\\$\\rm ");
				String formula = substance.getFormula();
				char c;
				for(int j = 0; j < formula.length(); j ++)
				{
					c = formula.charAt(j);
					if(c >= '0' && c <= '9')
						out.print("_" + c + "{}");
					else
						out.print(c);
				}
				out.print("$\\\\" + substance.getName() + "\\\\");
				out.print("\\textbf{FW }" + substance.getWeight() + "\\\\");
				out.print(substance.getStock() + " g\\\\");
				out.print("{\\centering \\includegraphics[width=0.23\\textwidth]{../image/"
					+ substance.getCASNo() + "}}\n");
			}
			in.close();
			out.close();
		}
		catch(FileNotFoundException e1)
		{
			System.out.println("Cannot open file, try again.");
		}
		catch(IOException e2)
		{
			System.out.println("Error writing file, try again.");
		}
		catch(ClassNotFoundException e3)
		{
			System.out.println("Error reading file, try again.");
		}
	}
	
	/**
	* Compile <i>.tex</i> files to get PDF file using pdfLaTeX. 
	* See ./pdf/compile.sh for more
	*/
	public static void generatePDF()
	{
		try
		{
			Process process = Runtime.getRuntime().exec("/bin/bash ./pdf/compile.sh");
			process.waitFor();
			System.out.println("Compilation Complete.");
		}
		catch(InterruptedException e1)
		{
			System.out.println("Compilation terminated.");
		}
		catch(IOException e2)
		{
			System.out.println("Error writing file, try again.");
		}
	}
}
