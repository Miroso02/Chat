import java.io.IOException;
import java.net.Socket;

public class Client
{
	static Socket socket;
	static MessageGetter mGetter;
	static MessageSender mSender;
	
	public static void main(String[] args) throws IOException
	{
		socket = new Socket("localhost", 6666);
		
		mGetter = new MessageGetter(socket);
		mSender = new MessageSender(socket);
		
		mGetter.start();
		mSender.start();
	}
}
