
package database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;  
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

public class DatabaseSQLite extends Database
{  
    static String path = System.getProperty("user.dir")+"/test.db";
    Connection connection = null;
    private final String dateFormat = "dd-MM-yyyy_HH:mm";
    
    public DatabaseSQLite()
    {
        File dbFile = new File(path);
        try
        {
            if(!dbFile.exists())
            {
                dbFile.createNewFile();
            }
            connect();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void connect()
    {
        try
        {
        	if(connection == null || connection.isClosed())
        	{
//        		Class.forName("org.sqlite.JDBC");
        		connection = DriverManager.getConnection("jdbc:sqlite:"+path);
        	}
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void close(ResultSet result)
    {
    	try
    	{
    		if(result != null && !result.isClosed())
    		{
    			result.close();
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    public ArrayList<ArrayList<String>> getData()
    {
        String sql = ""
        		+ "SELECT "
        		+ "id, "
        		+ "name, "
        		+ "lastname "
        		+ "FROM "
        		+ "person "
        		+ "where name != 'admin';";
        ArrayList<ArrayList<String>> data = getDataFromDBWithHeader(sql);
        return data;
    }
    public int getId(String name)
    {
        ResultSet resultSet = executeGet("SELECT id FROM person where name = '"+name+"';");
        try
        {
            if(resultSet.next())
            {
                return resultSet.getInt("id");
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return -1;
    }
    public ArrayList <ArrayList<String>> getAllData()
    {
        String sql = "SELECT "
                + "person.id, "
                + "person.name, "
                + "person.lastname, "
                + "login.p_password, "
                + "login.p_admin "
                + "FROM person "
                + "join login on person.id = login.p_id";
        ArrayList <ArrayList<String>> data = getDataFromDBWithHeader(sql);
//        try
//        {
//        	ResultSet resultSet = executeGet(sql);
//        	ResultSetMetaData rsmd = getMetaData(sql);
//        	ArrayList <String> temp = new ArrayList<String>();
//            for(int column=1; column <= rsmd.getColumnCount(); column++)
//            {
//        		temp.add(rsmd.getColumnName(column));
//            }
//            data.add(temp);
//            while(resultSet.next())
//            {
//            	temp = new ArrayList<String>();
//            	for(int column=1; column <= rsmd.getColumnCount(); column++)
//            	{
//        			temp.add(resultSet.getString(column));
//            	}
//            	data.add(temp);
//            }
//        }
//        catch(SQLException e)
//        {
//            e.printStackTrace();
//        }
        return data;
    }
    public boolean createDatabaseIfNotExists()
    {
//    	connect();
        executeSet("drop table if exists metadata;");
//        executeSet("drop table if exists login");
        //////////////////////////////
        executeSet("create table if not exists metadata ("
                + "id integer primary key autoincrement,"
                + "topic text,"
                + "date text"
                + ");");
//        executeSet("create table if not exists login ("
//                + "id integer primary key autoincrement,"
//                + "p_id integer,"
//                + "p_name text,"
//                + "p_lastname text,"
//                + "p_password text unique,"
//                + "p_admin default 'false',"
//                + "foreign key (p_id) references person(id)"
//                + ")");
//        close();
        return true;
    }
    public void insertData()
    {
    }
    public boolean isPermitted(String name, String password)
    {
        try
        {
//            ResultSet resultSet = executeGet("SELECT p.name, login.p_password, login.p_admin FROM person p inner join login on p.id = login.p_id where login.p_admin = 'true'");
            ResultSet resultSet = executeGet("SELECT p.id, p.name, login.p_password, login.p_admin FROM person p inner join login on p.id = login.p_id where login.p_admin = 'true'");
            if(resultSet.next())
            {
                String tempname = resultSet.getString("name");
                String temppw = resultSet.getString("p_password");
                if(name != null && password != null & name.equals(tempname) && password.equals(temppw))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            return false;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    ResultSet executeGet(String sql)
    {
        try
        {
            System.out.println(sql);
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet res = stmt.executeQuery();
//            close(res);
            return res;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    ResultSetMetaData getMetaData(String sql)
    {
    	try
    	{
//    		System.out.println(sql);
    		PreparedStatement stmt = connection.prepareStatement(sql);
    		return stmt.getMetaData();
    	}
    	catch(SQLException e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    }
    void executeSet(String sql)
    {
        try
        {
            System.out.println(sql);
            connect();
            connection.prepareStatement(sql).executeUpdate();
//            close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    ArrayList <ArrayList<String>> getDataFromDBWithoutHeader(String sql)
    {
    	ArrayList <ArrayList<String>> data = new ArrayList<ArrayList<String>>();
    	try
    	{
        	ResultSet resultSet = executeGet(sql);
        	ResultSetMetaData rsmd = resultSet.getMetaData();
        	data = getDataFromDB(sql, resultSet, rsmd);
    	}
    	catch(SQLException e)
    	{
    		e.printStackTrace();
    	}
    	return data;
    }    
    ArrayList <ArrayList<String>> getDataFromDB(String sql, ResultSet resultSet, ResultSetMetaData rsmd)
    {
    	ArrayList <ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        try
        {
        	ArrayList <String> temp = new ArrayList<String>();
            while(resultSet.next())
            {
            	temp = new ArrayList<String>();
            	for(int column=1; column <= rsmd.getColumnCount(); column++)
            	{
        			temp.add(resultSet.getString(column));
            	}
            	data.add(temp);
            }
            resultSet.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return data;
    }    
    ArrayList <ArrayList<String>> getDataFromDBWithHeader(String sql)
    {
    	ArrayList <ArrayList<String>> data = new ArrayList<ArrayList<String>>();
    	ArrayList <ArrayList<String>> header = new ArrayList<ArrayList<String>>();
    	ArrayList <ArrayList<String>> content = new ArrayList<ArrayList<String>>();
    	try
    	{
    		ResultSet resultSet = executeGet(sql);
    		// get header
    		ResultSetMetaData rsmd = getMetaData(sql);
    		ArrayList <String> temp = new ArrayList<String>();
    		for(int column=1; column <= rsmd.getColumnCount(); column++)
    		{
    			if(headerInUppercaseCharacter)
    			{
    				temp.add(rsmd.getColumnName(column).toUpperCase());
    			}
    			else
    			{
    				temp.add(rsmd.getColumnName(column).toLowerCase());
    			}
    		}
    		resultSet.close();
    		header.add(temp);
    		// get content
    		content = getDataFromDB(sql, resultSet, rsmd);
    		// migrate
    		for(ArrayList<String> migrate: header)
    		{
    			data.add(migrate);
    		}
    		for(ArrayList<String> migrate: content)
    		{
    			data.add(migrate);
    		}
    	}
    	catch(SQLException e)
    	{
    		e.printStackTrace();
    	}
    	return data;
    }
    private Date getDate()
    {
    	ResultSet resultSet = executeGet("select max(date) from metadata where topic = 'request';");
    	try
    	{
			if(resultSet.next())
			{
				String date = resultSet.getString("max(date)");
				SimpleDateFormat sdt = new SimpleDateFormat(dateFormat);
			    Date result = sdt.parse(date);
//				return new Timestamp(result.getTime());
				return result;
			}
			close(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return new Timestamp(System.currentTimeMillis());
    }
    public static void main(String[] args) {
    	DatabaseSQLite obj = new DatabaseSQLite();
    	obj.createDatabaseIfNotExists();
    	obj.insertData();
    	ArrayList<ArrayList<String>> data = obj.getData();
    	for(ArrayList<String> temp: data)
    	{
    		for(String temp1: temp)
    		{
    			System.out.print(temp1+" : ");
    		}
    		System.out.println();
    		
    	}
	}
    public double calculate(int a, int b)
    {
    	return Math.pow(a, b);
    }
    public void storeTimeOfRequest()
    {
//    	executeSet("insert into metadata (topic, date) values ('request', datetime('now', 'localtime'))");
    	Date now = new Timestamp( System.currentTimeMillis());
    	String dates = new SimpleDateFormat(dateFormat).format(now);
    	executeSet("insert into metadata (topic, date) values ('request', '"+dates+"');");
    }
    public boolean isLastTimeOfRequestMoreThanOneHourInThePast()
    {
    	ZonedDateTime now = ZonedDateTime.of(LocalDateTime.now(),ZoneId.systemDefault());
//    	LocalDateTime lastRequest = getDate().toLocalDateTime();
//    	System.out.println(new Timestamp(1653250706445l).toLocalDateTime());
    	final ZoneId id = ZoneId.systemDefault();
    	ZonedDateTime lastRequest = ZonedDateTime.ofInstant(getDate().toInstant(), id);
    	System.out.println("Last request more than one hour in past?: "+(Duration.between(now, lastRequest).toHours() > 1));
    	return Duration.between(now, lastRequest).toHours() > 1;
//    	return new Timestamp(System.currentTimeMillis()).after(getDate());
    }
    public boolean isLastTimeOfRequestMoreThanOneHourInThePast(int minimumDifferenceInHours)
    {
    	ZonedDateTime now = ZonedDateTime.of(LocalDateTime.now(),ZoneId.systemDefault());
    	final ZoneId id = ZoneId.systemDefault();
    	ZonedDateTime lastRequest = ZonedDateTime.ofInstant(getDate().toInstant(), id);
    	return Duration.between(now, lastRequest).toHours() > minimumDifferenceInHours;
//    	return new Timestamp(System.currentTimeMillis()).after(getDate());
    }
	@Override
	public ArrayList<Integer> lastRequest()
	{
		return null;
	}
}  

