package manage.reagent.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
* Server that maintain a database of reagents in a laboratory.
* @author Zekun Zhang
* @version 2015-01-06
*/
public class Server
{
	private static int port = 8189;
	
	/**
	* Open the port to accept requests from client
	* @param args Not to matter
	* @throws IOException Error opening server socket
	*/
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException
	{
		ServerSocket svc = new ServerSocket(port);
		int i = 1;
		while(true)
		{
			Socket incoming = svc.accept();
			System.out.println("CLIENT #" + i ++);
			Runnable conversation = new ServerEcho(incoming);
			Thread t = new Thread(conversation);
			t.start();
		}
	}
}

/**
* Thread that handle one request form a client.
* @author Zekun Zhang
* @version 2015-01-06
*/
class ServerEcho implements Runnable
{
	@SuppressWarnings("unused")
	private Socket incoming;
	private String passwd = "123456";
	private Scanner client_get;
	private PrintWriter send;
	
	/**
	* @param incoming request socket
	*/
	public ServerEcho(Socket incoming)
	{
		try
		{
			this.incoming = incoming;
			client_get = new Scanner(incoming.getInputStream());
			send = new PrintWriter(incoming.getOutputStream(), true);
		}
		catch(IOException e1)
		{
			System.out.println("Error Opening Socket.");
		}
	}
	
	/**
	* Echo the incoming stream of the socket.
	*/
	@SuppressWarnings("unused")
	public void run()
	{
		try
		{
			if(client_get.nextLine().equals(passwd))
			{
				send.println("Login. (Enter \'?\' for help)");
				send.println("CONTINUE");
				System.out.println("Login at " + new Date().toString()); 
			}
			else
			{
				send.println("Password Incorrect. Bye.");
				send.println("EXIT");
				System.out.println("Unsuccesful Login."); 
				return;
			}
			String command;
			String CAS;
			while(true)
			{
				command = client_get.nextLine();
				System.out.println("\t" + command);
				String[] seg = command.split(":");
				if(seg[0].equalsIgnoreCase("SELECT"))
					send.println(ReagentSystem.select(new int[]
						{
							Integer.parseInt(seg[1]),
							Integer.parseInt(seg[2]),
							Integer.parseInt(seg[3])
						}
						));
				else if(seg[0].equalsIgnoreCase("UPDATE"))
				{
					command = "";
					for(int i = 1; i < seg.length; i ++)
						command = command + seg[i] + ":";
					send.println(ReagentSystem.update(command));
				}
				else if(seg[0].equalsIgnoreCase("DROP"))
					send.println(ReagentSystem.drop(new int[]
						{
							Integer.parseInt(seg[1]),
							Integer.parseInt(seg[2]),
							Integer.parseInt(seg[3])
						}
						));
				else if(seg[0].equalsIgnoreCase("PRINT"))
					send.println(ReagentSystem.print());
				else
					send.println("CONTINUE");
			}
		}
		catch(NoSuchElementException e1)
		{
			System.out.println("Connection Closed by Client.");
		}
	}
}
		
