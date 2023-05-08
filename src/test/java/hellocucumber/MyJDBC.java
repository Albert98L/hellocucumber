package hellocucumber;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyJDBC {

    private static Connection connection;

    public static void main(String[] args) {

        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3006/gorestdb1", "root", "albert122298++");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}


