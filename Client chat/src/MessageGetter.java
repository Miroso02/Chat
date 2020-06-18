import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageGetter extends Thread
{
	BufferedReader in;
	
	public MessageGetter(Socket socket) throws IOException
	{
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	@Override
	public void run()
	{
		String answer;
		try
		{
			while (true)
			{
				answer = in.readLine();
				System.out.println(answer);
			}
		} 
		catch (IOException e) {}
	}
}
