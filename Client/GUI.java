import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.text.SimpleDateFormat;

/**
* GUI runs the main framework of the program interface.
* @author Zekun Zhang
* @version 2015-01-06
*/
public class GUI
{
	/**
	* @param args not to matter
	*/
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				ReagentSystemFrame frame = new ReagentSystemFrame();
				
				frame.setLocationByPlatform(true);
				frame.setVisible(true);
			}
		});
	}
}

/**
* Framework of the interface window
* @author Zekun Zhang
* @version 2015-01-06
*/
class ReagentSystemFrame extends JFrame
{
	/**
	* initialize the window framework, exit when closed<br>
	* The appearance of the window would be set to local system defualt.
	*/
	public ReagentSystemFrame()
	{
		client = new ClientGUI();
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		}
		catch(Exception e)
		{
			System.out.println("No Such Appearance.");
		}
		
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.out.println("EXIT");
			}
		});
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		int screenWidth = kit.getScreenSize().width;
		int screenHeight = kit.getScreenSize().height;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Reagent System");
		setIconImage(kit.getImage("icon.png"));
		setSize(300, 200);
		
		/**
		* main operation panel to access the database
		*/
		JPanel operate = new JPanel();
		operate.setLayout(new GridLayout(9, 1));
		
		JPanel cas_panel = new JPanel();
		cas_panel.add(new JLabel("CAS Number: "));
		JTextField cas_seg1 = new JTextField("141", 8);
		JTextField cas_seg2 = new JTextField("78", 8);
		JTextField cas_seg3 = new JTextField("6", 4);
		cas_panel.add(cas_seg1);cas_panel.add(new JLabel("-"));
		cas_panel.add(cas_seg2);cas_panel.add(new JLabel("-"));
		cas_panel.add(cas_seg3);
		operate.add(cas_panel); //line for CAS Number
		
		JPanel formula_panel = new JPanel();
		formula_panel.add(new JLabel("         Formula: "));
		JTextField formula = new JTextField("C4H8O2", 25);
		formula_panel.add(formula);
		operate.add(formula_panel); //line for formula
		
		JPanel name_panel = new JPanel();
		name_panel.add(new JLabel("            Name: "));
		JTextField name = new JTextField("ethyl acetate", 25);
		name_panel.add(name);
		operate.add(name_panel); //line for name
		
		JPanel mw_panel = new JPanel();
		mw_panel.add(new JLabel("Molecular Weight: "));
		JTextField weight = new JTextField("88.11", 25);
		mw_panel.add(weight);
		operate.add(mw_panel); //line for molecular weight
		
		JPanel stock_panel = new JPanel();
		stock_panel.add(new JLabel("           Stock: "));
		JTextField stock = new JTextField("500", 22);
		stock_panel.add(stock);
		stock_panel.add(new JLabel(" g"));
		operate.add(stock_panel); //line for stock
		
		operate.add(new JLabel(" ")); //seperate line
		
		JPanel cmd_panel = new JPanel();
		cmd_panel.setLayout(new FlowLayout());
		
		JButton search_button = new JButton("SEARCH");
		search_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.out.println("SELECT");
				client.sendTo("SELECT:" + cas_seg1.getText() + ":" + cas_seg2.getText() + ":" + cas_seg3.getText());
				String[] rgn_str = client.getFrom().split(":");
				if(rgn_str[0].equalsIgnoreCase("No Such Record."))
				{
					formula.setText("No Record");
					name.setText("No Record");
					weight.setText("");
					stock.setText("");
				}
				else
				{
					String[] cas_seg = rgn_str[0].split("-");
					cas_seg1.setText(cas_seg[0]);
					cas_seg2.setText(cas_seg[1]);
					cas_seg3.setText(cas_seg[2]);
					formula.setText(rgn_str[1]);
					name.setText(rgn_str[2]);
					weight.setText(rgn_str[3]);
					stock.setText(rgn_str[4].split(" ")[0]);
				}
			}
		});
		cmd_panel.add(search_button); //search/select button
		
		JButton delete_button = new JButton("DELETE");
		delete_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.out.println("DROP");
				client.sendTo("DROP:" + cas_seg1.getText() + ":" + cas_seg2.getText() + ":" + cas_seg3.getText());
				String echo = client.getFrom();
				if(echo.equalsIgnoreCase("Data Removed."))
					echo = "Removed";
				else
					echo = "No Record";
				formula.setText(echo);
				name.setText(echo);
				weight.setText("");
				stock.setText("");
			}
		});
		cmd_panel.add(delete_button); //delete/drop button
		
		JButton add_button = new JButton("ADD");
		add_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if(formula.getText().equalsIgnoreCase("No Record"))
					return;
				if(name.getText().equalsIgnoreCase("No Record"))
					return;
				System.out.println("UPDATE");
				client.sendTo("UPDATE:" + cas_seg1.getText() + ":" + cas_seg2.getText() + ":" + cas_seg3.getText() + ":" + formula.getText() + ":" + name.getText() + ":" + weight.getText() + ":" + stock.getText());
				String echo = client.getFrom();
				if(echo.equalsIgnoreCase("Data Updated."));
				else
				{
					JDialog dialog = new JDialog(ReagentSystemFrame.this,
						"Add Item", true);
					dialog.add(new JLabel(
						"<html><b><font size=\"6\">Item Already Exists</font></b><html>", SwingConstants.CENTER));
					dialog.setSize(400,200);
					dialog.setVisible(true);
				}
			}
		});
		cmd_panel.add(add_button); //add/update button
		
		operate.add(cmd_panel); //line for 3 command
		
		operate.add(new JLabel("Get List of Reagents in PDF:", SwingConstants.CENTER));
		
		JPanel save_panel = new JPanel();
		
		JFileChooser dir_chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF files", "pdf");
		dir_chooser.setFileFilter(filter);
		dir_chooser.setCurrentDirectory(new File("."));
		dir_chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd(hh-mm-ss)");
		String pdf_append = formater.format(new Date());
		JTextField pdf_path = new JTextField("list-" + pdf_append + ".pdf", 40);
		save_panel.add(pdf_path);
		
		JButton chooser_button = new JButton("...");
		chooser_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				int result = dir_chooser.showOpenDialog(ReagentSystemFrame.this);
				if(result == JFileChooser.APPROVE_OPTION)
					pdf_path.setText(dir_chooser.getSelectedFile().getPath());
			}
		});
		save_panel.add(chooser_button);
		
		JButton save_button = new JButton("SAVE");
		save_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				File file_path = new File(pdf_path.getText());
				try
				{
					System.out.println("SAVE PDF TO" + file_path.getCanonicalPath());
					if(file_path.isDirectory())
						file_path = new File(file_path, "list-" + pdf_append + ".pdf");
					String download_echo;
					if(client.downloadPDF(file_path))
						download_echo = "PDF File Downloaded";
					else
						download_echo = "Error Downloading PDF";
					JDialog dialog = new JDialog(ReagentSystemFrame.this, "Download PDF", true);
					dialog.add(new JLabel(
						"<html><b><font size=\"6\">" + download_echo + "</font></b><html>", SwingConstants.CENTER));
					dialog.setSize(400,200);
					dialog.setVisible(true);
				}
				catch(Exception e)
				{
					System.out.println("Cannot Open File");
				}
			}
		});
		save_panel.add(save_button);
		
		operate.add(save_panel);
		
		this.ui = new JPanel();
		ui.setLayout(new BorderLayout());
		ui.add(operate, BorderLayout.CENTER);
		
		/**
		* login panel to read server IP and password
		*/
		this.connection_panel = new JPanel();
		connection_panel.setLayout(new BorderLayout());
		
		connection_panel.add(new JLabel(
			"<html><b><font size=\"5\">Connect to Remote Server</font></b></html>", SwingConstants.CENTER), BorderLayout.NORTH);
		
		JPanel login_panel = new JPanel();
		login_panel.add(new JLabel("ServerIP:", SwingConstants.RIGHT));
		JTextField ip = new JTextField("192.168.", 30);
		login_panel.add(ip);
		login_panel.add(new JLabel("Password:", SwingConstants.RIGHT));
		JPasswordField passwd = new JPasswordField(30);
		login_panel.add(passwd);
		connection_panel.add(login_panel, BorderLayout.CENTER);
		
		JButton login = new JButton("Login");
		login.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					client.setServerIP(ip.getText());
					client.setPasswd(passwd.getPassword());
					if(client.login())
					{
						System.out.println("CONNECTION ESTABLISHED");
						connection_panel.setVisible(false);
						setSize(400, 500);
						add(ui);
					}
					else
						System.out.println("CONNECTION REFUSED");
				}
				catch(NullPointerException e)
				{
					System.out.println("CONNECTION REFUSED");
				}
			}
		});
		JPanel button_panel = new JPanel();
		button_panel.setLayout(new FlowLayout());
		button_panel.add(login);
		connection_panel.add(button_panel, BorderLayout.SOUTH);
		
		add(connection_panel); //add panel for connection
	}
	
	private JPanel ui;
	private JPanel connection_panel;
	
	private ClientGUI client = new ClientGUI();
}
