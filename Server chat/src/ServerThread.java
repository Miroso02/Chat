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
	
	public ServerThread(Socket client) throws IOException
	{
		this.name = (int)(Math.random() * 100);
		this.client = client;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		start();
	}
	
	@Override
	public void run()
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
						client.send("<" + name + ">: " + message);
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
}
