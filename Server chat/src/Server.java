import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Server
{
	static final int PORT = 6666;
	static ServerSocket server;
	static ArrayList<ServerThread> clients = new ArrayList<>();
	
	
	public static void main(String[] args) throws IOException
	{
		server = new ServerSocket(PORT);
		System.out.println("Server started!");
		
		try
		{
			while (true)
			{
				Socket client = server.accept();
				
				try
				{
					clients.add(new ServerThread(client));
				} 
				catch (IOException e)
				{
					client.close();
				}
			}
		}
		finally 
		{
			server.close();
		}
	}
}
