import java.io.*;
import java.net.*;
import java.util.*;

/**
* ClientGUI handles the connection to the server.
* @author Zekun Zhang
* @version 2015-01-06
*/
public class ClientGUI
{
	/**
	* set the port to 8189
	*/
	public ClientGUI()
	{
		service_port = 8189;
	}
	
	/**
	* set the server IP address
	* @param ip IP address of the server. for example<br><i>192.168.1.105</i>
	*/
	public void setServerIP(String ip)
	{
		server_ip = ip;
	}
	
	/**
	* set the password required for login
	* @param passwd Password to visit the server
	*/
	public void setPasswd(char[] passwd)
	{
		this.passwd = passwd;
	}
	
	/**
	* This method should be called after setting the correct server IP and password 
	* to connect the remote server.
	* @return <b><i>true</i></b> if server connected, <b><i>false</i></b> if not
	*/
	public boolean login()
	{
		try
		{
			skt = new Socket(server_ip, service_port);
			send = new PrintWriter(skt.getOutputStream(), true);
			get = new Scanner(skt.getInputStream());
			send.println(new String(passwd));
			get.nextLine();
		}
		catch(Exception e)
		{
			return false;
		}
		finally
		{
			clearPasswd();
			if(get.nextLine().equalsIgnoreCase("CONTINUE"))
				return true;
			else
				return false;
		}
	}
	
	/**
	* This method should be called after connection to remote server has been established 
	* to send one line of command to server.
	* @param command one line of command to be sent to server
	*/
	public void sendTo(String command)
	{
		send.println(command);
	}
	
	/**
	* This method should be called after connection to remote server has been established 
	* to get one line of echo from server.
	* @return echo line from server
	*/
	public String getFrom()
	{
		return get.nextLine();
	}
	
	/**
	* This method should be called after connection to remote server has been established 
	* to download PDF file of reagents list from server.
	* @param file the file that the PDF would be ssaved to
	* @return <b><i>true</i></b> if PDF downloaded, <b><i>false</i></b> if not
	*/
	public boolean downloadPDF(File file)
	{
		try
		{
			send.println("PRINT");
			if(! get.nextLine().equalsIgnoreCase("PDF File Generated"))
				return false;
			URL url = new URL("http://" + server_ip + "/PDF/list.pdf");
			DataInputStream in = new DataInputStream(url.openStream());
			FileOutputStream out = new FileOutputStream(file);
			byte[] buff = new byte[1024];
			int length;
			while((length = in.read(buff)) > 0)
				out.write(buff, 0, length);
			in.close();
			out.close();
			return true;
		}
		catch(Exception e1)
		{
			return false;
		}
	}
	
	/**
	* clear the array that contains password, for safty
	*/
	private void clearPasswd()
	{
		for(char c : passwd)
			c = '\0';
	}
	
	/**
	* @return server IP and port. For example<br>
	* <i>192.168.1.105:8189</i>
	*/
	public String toString()
	{
		return server_ip + ":" + service_port;
	}
	
	private String server_ip;
	private int service_port;
	private char[] passwd;
	
	private Socket skt;
	private PrintWriter send;
	private Scanner get;
}
