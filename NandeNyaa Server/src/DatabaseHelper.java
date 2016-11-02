import org.json.simple.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

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

    private static boolean isUsernameExist(String username) {
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

    private static boolean isFriend(String user1, String user2) {
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

    private static boolean isAdmin(String username, int groupId) {
        boolean admin = false;
        try {
            String sql = "SELECT group_id FROM group_admin WHERE (group_id = ? AND admin = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, groupId);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                admin = true;
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
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

                System.out.println("    {db} REGISTER SUCCESS: username " + username);
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

    public static boolean addGroupAdmin(int groupId, String admin) {
        try {
            String  sql = "INSERT INTO group_admin (group_id, admin) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, groupId);
            stmt.setString(2, admin);
            stmt.executeUpdate();

            stmt.close();
            System.out.println("    {db} ADD GROUP ADMIN SUCCESS: id_group " + groupId + "; admin " + admin);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("    {db} ADD GROUP ADMIN FAILED: Unknown error");
        return false;
    }

    public static JSONObject addGroupMembers(int groupId, String username, ArrayList<String> members) {
        if (!isAdmin(username, groupId)) {
            System.out.println("    {db} ADD GROUP MEMBERS FAILED: " + username + " is not an admin");
            return ResponseBuilder.buildAddGroupMembersFailedMessage("You aren't authorized to add members");
        } else {
            try {
                String sql = "INSERT INTO group_member (group_id, member ) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                for(String member: members) {
                    stmt.setInt(1, groupId);
                    stmt.setString(2, member);
                    stmt.executeUpdate();
                    System.out.println("    {db} ADD GROUP MEMBERS SUCCESS: group_id " + groupId + "; user " + member);
                }
                stmt.close();
                System.out.println("    {db} ADD GROUP MEMBERS SUCCESS: All success");
                return ResponseBuilder.buildAddGroupMembersSuccessMessage("User(s) has been added to group");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("    {db} ADD FRIEND FAILED: Unknown error");
        return ResponseBuilder.buildAddGroupMembersFailedMessage("Error occurred. Please try again.");
    }

    public static JSONObject createGroup(String username, String groupName, ArrayList<String> members) {
        int id;
        boolean success = true;
        try {
            conn.setAutoCommit(false);

            String sql = "INSERT INTO `group` (group_name) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, groupName);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                id = rs.getInt(1);
                rs.close();
                stmt.close();

                addGroupMembers(id, username, new ArrayList<>(Arrays.asList(new String[] {username})));
                addGroupMembers(id, username, members);

                conn.commit();
                conn.setAutoCommit(true);

                System.out.println("    {db} CREATE GROUP SUCCESS: Group id " + id + "; group_name " + groupName);
                return ResponseBuilder.buildCreateGroupSuccessMessage(id, "Group has been created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("    {db} CREATE GROUP FAILED: Unknown error");
        return ResponseBuilder.buildCreateGroupFailedMessage("Error occurred. Please try again.");
    }
}
