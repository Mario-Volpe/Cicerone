package com.example.cicerone.data.control;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class ConnectDB {

    public static void main(String[] args) {

        try
        {
            ConnectDB mySqlExample = new ConnectDB();

            Connection conn = mySqlExample.getMySqlConnection();

            /* You can use the connection object to do any insert, delete, query or update action to the mysql server.*/

            /* Do not forget to close the database connection after use, this can release the database connection.*/
            conn.close();
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /* This method return java.sql.Connection object from MySQL server. */
    public Connection getMySqlConnection()
    {
        /* Declare and initialize a sql Connection variable. */
        Connection ret = null;

        try
        {

            /* Register for mysql jdbc driver class. */
            Class.forName("com.mysql.jdbc.Driver");

            /* Create mysql connection url. */
            String mysqlConnUrl = "jdbc:mysql:https://www.db4free.net:3306/cicerone";

            /* MySQL access user name. */
            String mysqlUserName = "cicerone@localhost";

            /* MySQL access password. */
            String mysqlPassword = "cicerone";

            /* Get the mysql Connection object. */
            ret = DriverManager.getConnection(mysqlConnUrl, mysqlUserName , mysqlPassword);

            /* Get related meta data for this mysql server to verify db connect successfully.. */
            DatabaseMetaData dbmd = ret.getMetaData();

            String dbName = dbmd.getDatabaseProductName();

            String dbVersion = dbmd.getDatabaseProductVersion();

            String dbUrl = dbmd.getURL();

            String userName = dbmd.getUserName();

            String driverName = dbmd.getDriverName();

            System.out.println("Database Name is " + dbName);

            System.out.println("Database Version is " + dbVersion);

            System.out.println("Database Connection Url is " + dbUrl);

            System.out.println("Database User Name is " + userName);

            System.out.println("Database Driver Name is " + driverName);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }finally
        {
            return ret;
        }
    }

}