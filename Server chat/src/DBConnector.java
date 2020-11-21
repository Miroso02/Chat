import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector
{
	private PreparedStatement hasUser;
    private PreparedStatement checkUser;
    private PreparedStatement addUser;
    private PreparedStatement userCount;
    
    public DBConnector() 
    {
    	try (InputStream in = Files.newInputStream(Paths.get("database.properties")))
    	{
    		Properties props = new Properties();
    		props.load(in);
			String url = props.getProperty("url");
			String user = props.getProperty("user");
			String password = props.getProperty("password");
			Connection con = DriverManager.getConnection(url, user, password);
    		
    		hasUser = con.prepareStatement("select count(*) from users where login = ?;");
    		checkUser = con.prepareStatement("select * from users where login = ? && password = ?;");
    		addUser = con.prepareStatement("insert into users (id, login, password) values (?, ?, ?);");
    		userCount = con.prepareStatement("select count(*) from users;");
    	}
    	catch (SQLException | IOException e)
		{
			e.printStackTrace();
		}
    }
    
    public boolean hasUser(String login) 
    {
    	try 
    	{
    		hasUser.setString(1, login);
    		ResultSet user = hasUser.executeQuery();
    		int count = 0;
    		if (user.next())
    		{
    			count = user.getInt(1);
    		}
    		user.close();
    		return count > 0;
    	}
    	catch (SQLException e) 
    	{
			e.printStackTrace();
			return false;
		}
    }
    
    public boolean authorise(String login, String password) 
    {
    	try 
    	{
    		checkUser.setString(1, login);
    		checkUser.setString(2, password);
    		ResultSet user = checkUser.executeQuery();
    		boolean contains = false;
    		if (user.next())
    			contains = true;
    		user.close();
    		return contains;
    	}
    	catch (SQLException e) 
    	{
			e.printStackTrace();
			return false;
		}
    }
    
    public void addUser(String login, String password)
    {
    	try 
    	{
    		ResultSet nOfUsers = userCount.executeQuery();
    		int usersN = 0;
    		if (nOfUsers.next())
    			usersN = nOfUsers.getInt(1);
    		nOfUsers.close();
    		addUser.setInt(1, usersN);
    		addUser.setString(2, login);
    		addUser.setString(3, password);
    		addUser.executeUpdate();
    	}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
