import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MessageSender extends Thread
{
	BufferedReader in;
	BufferedWriter out;
	
	public MessageSender(Socket socket) throws IOException
	{
		in = new BufferedReader(new InputStreamReader(System.in));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	@Override 
	public void run()
	{
		try
		{
			while (true)
			{
				String message = in.readLine();
				if (message == "exit")
					break;
				out.write(message + "\n");
				out.flush();
			}
			
			in.close();
			out.close();
		}
		catch (IOException e) {}
	}
}
