
package database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;  
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class DatabasePostgres extends Database
{  
    public DatabasePostgres()
    {
        connect();
    }
    public void connect()
    {
        try
        {
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public ArrayList<ArrayList<String>> getData()
    {
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        return data;
    }
    public int getId(String name)
    {
        return -1;
    }
    public ArrayList <ArrayList<String>> getAllData()
    {
        ArrayList <ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        return data;
    }
    public boolean createDatabaseIfNotExists()
    {
        return true;
    }
    public void insertData()
    {
    }
    public boolean isPermitted(String name, String password)
    {
        return false;
    }
	@Override
	public void storeTimeOfRequest() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isLastTimeOfRequestMoreThanOneHourInThePast() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public ArrayList<Integer> lastRequest() {
		// TODO Auto-generated method stub
		return null;
	}
}  

