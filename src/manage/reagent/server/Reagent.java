package manage.reagent.server;

import java.io.Serializable;

/**
* Certian reagent item stored in a laboratory.
* @author Zekun Zhang
* @version 2015-01-06
*/
@SuppressWarnings("serial")
public class Reagent implements Serializable
{
	/**
	* Initialize an empty reagent item.
	*/
	public Reagent()
	{
	}
	
	/**
	* Initialize a reagent item.
	* @param CASNo CAS Number of reagent, three segments of number in array int[]
	* @param Formula chemical formula of reagent
	* @param Name name of reagent
	* @param Weight molecular weight of reagent
	* @param Stock stock of reagent in lab, in <i>gram</i>
	*/
	public Reagent(int[] CASNo, String Formula, String Name, double Weight, double Stock)
	{
		this.CASNo = CASNo;
		this.Formula = Formula;
		this.Name = Name;
		this.Weight = Weight;
		this.Stock = Stock;
	}
	
	/**
	* Initialize a reagent item.
	* @param descript a description string of the reagent. 
	* Each attribute should be separated by ":". For example<br>
	* <i>141:78:6:C4H8O2:ethyl acetate:88.11:500.0</i>
	*/
	public Reagent(String descript)
	{
		String[] seg = descript.split(":");
		CASNo = new int[]
		{
			Integer.parseInt(seg[0]),
			Integer.parseInt(seg[1]),
			Integer.parseInt(seg[2])
		};
		Formula = seg[3];
		Name = seg[4];
		Weight = Double.parseDouble(seg[5]);
		Stock = Double.parseDouble(seg[6]);
	}
	
	/**
	* @return CAS Number of reagent. Such as <i>141-78-6</i>
	*/
	public String getCASNo()
	{
		return CASNo[0] + "-" + CASNo[1] + "-" + CASNo[2];
	}
	
	/**
	* @return formula of reagent.
	*/
	public String getFormula()
	{
		return Formula;
	}
	
	/**
	* @return name of reagent.
	*/
	public String getName()
	{
		return Name;
	}
	
	/**
	* @return mulecular weight of reagent.
	*/
	public double getWeight()
	{
		return Weight;
	}
	
	/**
	* @return stock of reagent in <i>gram</i>.
	*/
	public double getStock()
	{
		return Stock;
	}
	
	/**
	* @return a compacted description of reagent. For example<br>
	* <i>141-78-6:C4H8O2:ethyl acetate:88.11:500.0 g</i>
	*/
	public String toCompactString()
	{
		return CASNo[0] + "-" + CASNo[1] + "-" + CASNo[2] + ":" + Formula + ":" +
			Name + ":" + Weight + ":" + Stock + " g";
	}
	
	/**
	* @return a colon seperated description of reagent. For example<br>
	* <i>141:78:6:C4H8O2:ethyl acetate:88.11:500.0</i>
	*/
	public String toColonDevidedString()
	{
		return CASNo[0] + ":" + CASNo[1] + ":" + CASNo[2] + ":" + Formula + ":" +
			Name + ":" + Weight + ":" + Stock;
	}
	
	/**
	* @return a one-line-one-attribute description of reagent. For example<br>
	* <i>141<br>78<br>6<br>C4H8O2<br>ethyl acetate<br>88.11<br>500.0</i>
	*/
	public String toFileString()
	{
		return CASNo[0] + "\n" + CASNo[1] + "\n" + CASNo[2] + "\n" + Formula + "\n" +
			Name + "\n" + Weight + "\n" + Stock + "\n";
	}
	
	/**
	* @return a detailed description of reagent. For example<br>
	* <i>CAS Number: 141-78-6<br>
	* Formula: C4H8O2<br>
	* Name: ethyl acetate<br>
	* Molecular Weight: 88.11<br>
	* Stock: 500.0 g</i>
	*/
	public String toString()
	{
		return "CAS Number: " + CASNo[0] + "-" + CASNo[1] + "-" + CASNo[2] + "\n" +
			"Formula: " + Formula + "\n" + "Name: " + Name + "\n" +
			"Molecular Weight: " + Weight + "\n" + "Stock: " + Stock + " g";
	}
	
	private int[] CASNo; //CAS number
	private String Formula;
	private String Name;
	private double Weight; //molecular weight
	private double Stock; //in g
}
