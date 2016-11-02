import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tifani on 10/29/2016.
 */
public class RequestBuilder {
    public static JSONObject buildRegisterMessage(String username, String password) {
        JSONObject message = new JSONObject();
        message.put(Constants.REQUEST_TYPE, Constants.REGISTER);
        message.put(Constants.USERNAME, username);
        message.put(Constants.PASSWORD, password);
        message.put(Constants.DATE_TIME, System.currentTimeMillis());
        return message;
    }

    public static JSONObject buildLoginMessage(String username, String password) {
        JSONObject message = new JSONObject();
        message.put(Constants.REQUEST_TYPE, Constants.LOGIN);
        message.put(Constants.USERNAME, username);
        message.put(Constants.PASSWORD, password);
        message.put(Constants.DATE_TIME, System.currentTimeMillis());
        return message;
    }

    public static JSONObject buildPrivateMessage(String username, String receiver, String messageContent) {
        JSONObject message = new JSONObject();
        message.put(Constants.REQUEST_TYPE, Constants.PRIVATE_MESSAGE);
        message.put(Constants.USERNAME, username);
        message.put(Constants.RECEIVER, receiver);
        message.put(Constants.MESSAGE, messageContent);
        message.put(Constants.DATE_TIME, System.currentTimeMillis());
        return message;
    }

    public static JSONObject buildGroupMessage(String username, int groupId, String messageContent) {
        JSONObject message = new JSONObject();
        message.put(Constants.REQUEST_TYPE, Constants.GROUP_MESSAGE);
        message.put(Constants.USERNAME, username);
        message.put(Constants.GROUP_ID, groupId);
        message.put(Constants.MESSAGE, messageContent);
        message.put(Constants.DATE_TIME, System.currentTimeMillis());
        return message;
    }

    public static JSONObject buildAddFriendMessage(String username, String userToAdd) {
        JSONObject message = new JSONObject();
        message.put(Constants.REQUEST_TYPE, Constants.ADD_FRIEND);
        message.put(Constants.USERNAME, username);
        message.put(Constants.USER_TO_ADD, userToAdd);
        message.put(Constants.DATE_TIME, System.currentTimeMillis());
        return message;
    }

    public static JSONObject buildCreateGroupMessage(String username, String groupName, ArrayList<String> members) {
        JSONObject message = new JSONObject();
        message.put(Constants.REQUEST_TYPE, Constants.CREATE_GROUP);
        message.put(Constants.USERNAME, username);
        message.put(Constants.GROUP_NAME, groupName);
        if (members != null) {
            JSONArray arr = new JSONArray();
            arr.addAll(members);
            message.put(Constants.MEMBERS, arr);
        }
        message.put(Constants.DATE_TIME, System.currentTimeMillis());
        return message;
    }

    public static JSONObject buildAddGroupMembersMessage(String username, int groupId, ArrayList<String> newMembers) {
        JSONObject message = new JSONObject();
        message.put(Constants.REQUEST_TYPE, Constants.ADD_GROUP_MEMBERS);
        message.put(Constants.USERNAME, username);
        message.put(Constants.GROUP_ID, groupId);
        JSONArray arr = new JSONArray();
        arr.addAll(newMembers);
        message.put(Constants.MEMBERS, arr);
        message.put(Constants.DATE_TIME, System.currentTimeMillis());
        return message;
    }

    public static JSONObject buildRemoveGroupMembersMessage(String username, int groupId, ArrayList<String> members) {
        JSONObject message = new JSONObject();
        message.put(Constants.REQUEST_TYPE, Constants.REMOVE_GROUP_MEMBERS);
        message.put(Constants.USERNAME, username);
        message.put(Constants.GROUP_ID, groupId);
        JSONArray arr = new JSONArray();
        for (String member : members) {
            arr.add(member);
        }
        message.put(Constants.MEMBERS, arr);
        message.put(Constants.DATE_TIME, System.currentTimeMillis());
        return message;
    }

    public static JSONObject buildExitGroupMessage(String username, int groupId) {
        JSONObject message = new JSONObject();
        message.put(Constants.REQUEST_TYPE, Constants.EXIT_GROUP);
        message.put(Constants.USERNAME, username);
        message.put(Constants.GROUP_ID, groupId);
        message.put(Constants.DATE_TIME, System.currentTimeMillis());
        return message;
    }

    public static JSONObject buildGetFriendsMessage(String username) {
        JSONObject message = new JSONObject();
        message.put(Constants.REQUEST_TYPE, Constants.GET_FRIENDS);
        message.put(Constants.USERNAME, username);
        message.put(Constants.DATE_TIME, System.currentTimeMillis());
        return message;
    }

    public static JSONObject buildGetGroupsMessage(String username) {
        JSONObject message = new JSONObject();
        message.put(Constants.REQUEST_TYPE, Constants.GET_GROUPS);
        message.put(Constants.USERNAME, username);
        message.put(Constants.DATE_TIME, System.currentTimeMillis());
        return message;
    }

    public static JSONObject buildGetGroupMembersMessage(int groupId) {
        JSONObject message = new JSONObject();
        message.put(Constants.REQUEST_TYPE, Constants.GET_GROUP_MEMBERS);
        message.put(Constants.GROUP_ID, groupId);
        message.put(Constants.DATE_TIME, System.currentTimeMillis());
        return message;
    }
}
