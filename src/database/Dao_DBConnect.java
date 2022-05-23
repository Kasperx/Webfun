package database;

import java.sql.Connection;
import java.util.HashMap;

public class Dao_DBConnect
{
    protected Connection connection;
    protected String serverIp;
    protected String path;
    HashMap<String, String> mapFromFile;
}
