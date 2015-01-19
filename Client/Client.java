import java.io.*;
import java.net.*;
import java.util.*;

/**
* CUI Client of reagents database
* @author Zekun Zhang
* @version 2015-01-06
*/
public class Client
{
	private static String server_ip;
	private static int port = 8189;
	private static Socket skt;
	private static PrintWriter send;
	private static Scanner get;
	private static Scanner in;
	private static Console cons;
	
	/**
	* @param args The first argument should be the IP address of the server.
	* @throws IOException Error connecting remote server
	*/
	public static void main(String[] args) throws IOException
	{
		try
		{
			server_ip = args[0];
			skt = new Socket(server_ip, port);
			send = new PrintWriter(skt.getOutputStream(), true);
			get = new Scanner(skt.getInputStream());
			in = new Scanner(System.in);
			cons = System.console();
			char[] passwd = cons.readPassword("Enter Password (will not be displayed):");
			send.println(new String(passwd));
			passwd[0] = passwd[1] = passwd[2] = 0;
			System.out.println(get.nextLine());
			String command = get.nextLine().trim();
			while(! command.equalsIgnoreCase("EXIT"))
			{
				command = in.nextLine();
				if(command.equalsIgnoreCase("?"))
					System.out.println("Commands: SELECT UPDATE DROP PRINT EXIT");
				else if(command.equalsIgnoreCase("SELECT"))
				{
					System.out.println("Enter CAS Number (like 141 78 6): ");
					int[] cas = new int[3];
					cas[0] = in.nextInt();cas[1] = in.nextInt();cas[2] = in.nextInt();
					in.nextLine();
					send.println(command + ":" + cas[0] + ":" + cas[1] + ":" + cas[2]);
					System.out.println(get.nextLine());
				}
				else if(command.equalsIgnoreCase("UPDATE"))
				{
					System.out.println("Enter CAS Number (like 141 78 6): ");
					int[] cas = new int[3];
					cas[0] = in.nextInt();cas[1] = in.nextInt();cas[2] = in.nextInt();
					in.nextLine();
					command = command + ":" + cas[0] + ":" + cas[1] + ":" + cas[2];
					System.out.println("Enter Formula (like C4H8O2): ");
					command = command + ":" + in.nextLine().trim();
					System.out.println("Enter Name (like ethyl acetate): ");
					command = command + ":" + in.nextLine().trim();
					System.out.println("Enter Molecular Weight (like 88.11): ");
					command = command + ":" + in.nextDouble();
					in.nextLine();
					System.out.println("Enter Stock (like 375.0): ");
					command = command + ":" + in.nextDouble();
					in.nextLine();
					send.println(command);
					System.out.println(get.nextLine());
				}
				else if(command.equalsIgnoreCase("DROP"))
				{
					System.out.println("Enter CAS Number (like 141 78 6): ");
					int[] cas = new int[3];
					cas[0] = in.nextInt();cas[1] = in.nextInt();cas[2] = in.nextInt();
					in.nextLine();
					send.println(command + ":" + cas[0] + ":" + cas[1] + ":" + cas[2]);
					System.out.println(get.nextLine());
				}
				else if(command.equalsIgnoreCase("PRINT"))
				{
					send.println(command);
					System.out.println(get.nextLine());
				}
				else if(command.equalsIgnoreCase("EXIT"));
				else
					System.out.println("Check Your Input.");
			}
		}
		catch(ConnectException e1)
		{
			System.out.println("Server Offline. Exit.");
			System.exit(0);
		}
		catch(NoSuchElementException e2)
		{
			System.out.println("Server Down. Exit.");
			System.exit(0);
		}
		catch(ArrayIndexOutOfBoundsException e3)
		{
			System.out.println("USAGE:\n$ java Client ip_addr");
			System.exit(0);
		}
		send.close();
		get.close();
		skt.close();
	}
}
