package database;

import java.sql.ResultSet;
import java.util.ArrayList;

public interface DatabaseInterface
{
    public void connect();
    public boolean createDatabaseIfNotExists();
    public ArrayList <ArrayList<String>> getData();
    public ArrayList <ArrayList<String>> getAllData();
    public boolean isPermitted(String name, String password);
    public int getId(String name);
//    private ResultSet executeGet(String sql);
//    private void executeSet(String sql);
}
