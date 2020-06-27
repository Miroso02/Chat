import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread extends Thread
{
	BufferedReader in;
	BufferedWriter out;
	Socket client;
	int name;
	String userName;
	
	DBConnector dbc;
	
	public ServerThread(Socket client) throws IOException
	{
		this.name = (int)(Math.random() * 100);
		this.client = client;
		dbc = new DBConnector();
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		start();
	}
	
	@Override
	public void run()
	{	
		if (!logIn())
		{
			send("Connection broken");
			return;
		}
		chat();
	}
	
	private void chat() 
	{
		String message;
		try
		{
			while (true)
			{
				message = in.readLine();
				if (message.equals("exit"))
					break;
				for (ServerThread client : Server.clients)
				{
					if (client != this)
						client.send("<" + userName + ">: " + message);
				}
			}
		}
		catch (Exception e) {}
	}
	
	private void send(String message)
	{
		try 
		{
			out.write(message + "\n");
			out.flush();
		}
		catch (IOException e) {}
	}
	
	private boolean logIn() 
	{
		String login;
		String password;
		
		try
		{
			send("Enter login:");
			login = in.readLine();
			if (dbc.hasUser(login))
			{
				boolean authorised = false;
				send("Enter password:");
				
				while (!authorised)
				{
					password = in.readLine();
					if (password.equals("exit"))
						return false;
					authorised = dbc.authorise(login, password);
					if (!authorised) 
						send("Wrong password. Try again: ");
				}
			}
			else 
			{
				send("Missing user with login '" + login + "'. Would you like to register? (Y, N)");
				String message = in.readLine();
				if (!message.equals("y") && !message.equals("Y"))
					return false;
				
				send("Creating new user... \nEnter a password: ");
				password = in.readLine();
				dbc.addUser(login, password);
			}
			
			send("Authorization succeed");
			System.out.println(login + " joined");
			this.userName = login;
			return true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}
}
