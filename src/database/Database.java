package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.github.javafaker.Faker;

public abstract class Database extends Dao_DBConnect implements DatabaseInterface
{
    protected enum Enum_Database{
        sqlite, file, mariadb, postgres
    };
    protected Connection connection;
    protected String serverIp;
    protected String path;
    boolean headerInUppercaseCharacter = true;
//    public Database()
//    {
////        int f = Enum_Database.file.ordinal();
////        if(Enum_Database.file)
////        {
////            
////        }
//        switch(Enum_Database)
//        {
//        }
//    }
    public static Database getInstance()
    {
//        return getInstance(Enum_Database.file);
        return getInstance(Enum_Database.sqlite);
    }
    public static Database getInstance(Enum_Database source)
    {
        Database data = null;
        switch(source)
        {
            case file:
                data = new DatabaseFile();
                break;
            case sqlite:
                data = new DatabaseSQLite();
                break;
            case mariadb:
                data = new DatabaseFile();
                break;
            case postgres:
                data = new DatabasePostgres();
                break;
            default:
                data = new DatabaseSQLite();
                break;
        }
        return data;
    }
    
    protected HashMap <String[], Integer> getNewData()
    {
        //////////////////////////////////////////
        // get names, but only once (-> set)
//        String[] names = { "detlef", "arnold", "ulrike", "emil", "lena", "laura", "achim", "mia", "anna", "jonas" };
//        HashMap<String, Integer> result = new HashMap<String, Integer>();
//        Set <String> unique = new HashSet<String>();
//        int temp = 0;
//        while(temp < 5)
//        {
//            String found = names[new Random().nextInt(10)];
//            if(unique.add(found))
//            {
//                result.put(found, new Random().nextInt(10000000) + 1000000);
//                temp++;
//            }
//        }
        HashMap<String[], Integer> result = new HashMap<String[], Integer>();
        Faker faker;
        int temp = 0;
        while(temp < 10)
        {
            faker = new Faker();
//            String name = faker.name().fullName();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
//            String streetAddress = faker.address().streetAddress();
//            result.put(firstName, new Random().nextInt(10000000) + 1000000);
            result.put(new String[]{firstName, lastName}, new Random().nextInt(10000000) + 1000000);
            temp++;
        }
        return result;
    }
    public abstract void connect();
    public abstract boolean createDatabaseIfNotExists();
    public abstract ArrayList<ArrayList<String>> getData();
    public abstract ArrayList<ArrayList<String>> getAllData();
    public abstract boolean isPermitted(String name, String password);
    public abstract int getId(String name);
    public abstract void insertData();

//    @Override
//    abstract ResultSet executeGet(String sql);

//    @Override
//    abstract void executeSet(String sql);
	public boolean isHeaderInUppercaseCharacter()
	{
		return headerInUppercaseCharacter;
	}
	public void setHeaderInUppercaseCharacter(boolean headerInUppercaseCharacter)
	{
		this.headerInUppercaseCharacter = headerInUppercaseCharacter;
	}
	public Map <String, String> getProperties(String filename)
    {
        mapFromFile = new HashMap<String, String>();
        if(!new File(filename).exists())
        {
            System.out.println("File '"+filename+"' does not exist");
            return mapFromFile;
        }
        try (BufferedReader br = new BufferedReader( new FileReader(filename));)
        {
                        String line = "";
                mapFromFile = new HashMap<String, String>();
                        while ((line = br.readLine()) != null)
                        {
                String[] parts = line.split("=");
                String name = parts[0].trim();
                String value = parts[1].trim();
                if(!name.equals("") && !value.equals(""))
                {
                    mapFromFile.put(name, value);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return mapFromFile;
        }
        return mapFromFile;
    }
    public String getProperty (String keyname)
    {
        return mapFromFile.get(keyname);
    }
    public abstract void storeTimeOfRequest();
    public abstract boolean isLastTimeOfRequestMoreThanOneHourInThePast();
    public abstract ArrayList<Integer> lastRequest();
}
