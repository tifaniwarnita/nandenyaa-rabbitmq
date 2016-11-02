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
            System.out.println(" [x] Connecting to database...");
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
        boolean exist = false;
        try {
            String sql = "SELECT username FROM user WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exist;
    }

    public static boolean isFriend(String user1, String user2) {
        boolean friend = false;
        try {
            String sql = "SELECT user1 " +
                    "FROM friend " +
                    "WHERE ((user1 = ? AND user2 = ?) OR (user1 = ? AND user2 = ?))";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, user1);
            stmt.setString(2, user2);
            stmt.setString(3, user2);
            stmt.setString(4, user1);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                friend = true;
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friend;
    }

    public static JSONObject register(String username, String password) {
        if (isUsernameExist(username)) {
            System.out.println("    {db} REGISTER FAILED: Username has already exist");
            return ResponseBuilder.buildRegisterFailedMessage("Username has already exist");
        } else {
            try {
                String sql = "INSERT INTO user (username, password) VALUES (?, SHA(?))";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.executeUpdate();

                stmt.close();

                System.out.println("    {db} REGISTER SUCCESS: Username " + username);
                return ResponseBuilder.buildRegisterSuccessMessage("User " + username + " has been succesfully registered");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("    {db} REGISTER FAILED: Unknown error");
        return ResponseBuilder.buildRegisterFailedMessage("Error occurred. Please try again.");
    }

    public static JSONObject login(String username, String password) {
        boolean success = false;
        try {
            String sql = "SELECT username FROM user WHERE username = ? AND password = SHA(?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                success = true;
            }
            stmt.close();
            rs.close();
            if (success) {
                System.out.println("    {db} LOGIN SUCCESS: Username " + username);
                return ResponseBuilder.buildLoginSuccessMessage("Logged in");
            } else {
                System.out.println("    {db} LOGIN FAILED: Wrong username or password");
                return ResponseBuilder.buildLoginFailedMessage("Wrong username or password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("    {db} LOGIN FAILED: Unknown error");
        return ResponseBuilder.buildLoginFailedMessage("Error occurred. Please try again.");
    }

    public static JSONObject addFriend(String user1, String user2) {
        if (isFriend(user1, user2)) {
            System.out.println("    {db} ADD FRIEND FAILED: " + user1 + " has already been friend of " + user2);
            return ResponseBuilder.buildAddFriendFailedMessage(user1 + " has already been friend of " + user2);
        } else {
            try {
                String sql = "INSERT INTO friend (user1, user2) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, user1);
                stmt.setString(2, user2);
                stmt.executeUpdate();

                stmt.close();

                System.out.println("    {db} ADD FRIEND SUCCESS: " + user1 + " with " + user2);
                return ResponseBuilder.buildAddFriendSuccessMessage(user2 + " has been added");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("    {db} ADD FRIEND FAILED: Unknown error");
        return ResponseBuilder.buildAddFriendFailedMessage("Error occurred. Please try again.");
    }
}
