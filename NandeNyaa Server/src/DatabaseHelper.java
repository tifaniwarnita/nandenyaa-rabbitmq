import org.json.simple.JSONObject;

import java.sql.*;

/**
 * Created by Tifani on 11/1/2016.
 */
public class DatabaseHelper {
    private static Connection conn = null;

    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/nandenyaa";

    //  Database credentials
    private static final String USER = "root";
    private static final String PASS = "";

    public static void openConnection() {
        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try{
            if(conn!=null)
                conn.close();
        }catch(SQLException se){
            se.printStackTrace();
        }
    }

    public static boolean isUsernameExist(String username) {
        boolean success = false;
        try {
            String sql = "SELECT username FROM user WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                success = true;
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static JSONObject register(String username, String password) {
        if (isUsernameExist(username)) {
            System.out.println(" <DB> Register failed: username has already exist");
            return ResponseBuilder.buildRegisterFailedMessage("Username has already exist");
        } else {
            try {
                String sql = "INSERT INTO user (username, password) VALUES (?, SHA(?))";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.executeUpdate();

                stmt.close();

                System.out.println(" <DB> Register success: username " + username);
                return ResponseBuilder.buildRegisterSuccessMessage("User " + username + " has been succesfully registered");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ResponseBuilder.buildRegisterFailedMessage("Error occurred. Please try again.");
    }
}
