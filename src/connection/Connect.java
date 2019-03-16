package connection;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connect {


    private static Properties dbInfo()  {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("connection.properties");

            // load a properties file
            prop.load(input);


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return prop;
    }



    public static Connection getConnection() throws ClassNotFoundException, SQLException {

        // Get connection parameters from properties file
        Properties props = dbInfo();

        // Initialize connection variables.
        String host = props.getProperty("db.host");
        String database = props.getProperty("db.database");
        String user = props.getProperty("db.username");
        String password = props.getProperty("db.password");


        // check that the driver is installed
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("MySQL JDBC driver NOT detected in library path.", e);
        }

        System.out.println("MySQL JDBC driver detected in library path.");

        Connection connection = null;

        // Initialize connection object
        try {
            String url = String.format("jdbc:mysql://%s/%s", host, database);

            // Set connection properties.
            Properties properties = new Properties();
            properties.setProperty("user", user);
            properties.setProperty("password", password);
            properties.setProperty("useSSL", "true");
            properties.setProperty("verifyServerCertificate", "true");
            properties.setProperty("requireSSL", "false");
            properties.setProperty("serverTimezone", "UTC"); //IMPORTANT

            // getItem connection
            connection = DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            throw new SQLException("Failed to create connection to database", e);
        }

        return connection;

    }

}
