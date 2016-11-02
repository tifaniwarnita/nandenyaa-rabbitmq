import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tifani on 11/2/2016.
 */
public class ResponseBuilder {
    // REGISTER
    public static JSONObject buildRegisterSuccessMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.REGISTER);
        message.put(Constants.STATUS, Constants.SUCCESS);
        message.put(Constants.INFO, info);
        return message;
    }

    public static JSONObject buildRegisterFailedMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.REGISTER);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }

    // LOGIN
    public static JSONObject buildLoginSuccessMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.LOGIN);
        message.put(Constants.STATUS, Constants.SUCCESS);
        message.put(Constants.INFO, info);
        return message;
    }

    public static JSONObject buildLoginFailedMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.LOGIN);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }

    // ADD FRIEND
    public static JSONObject buildAddFriendSuccessMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.ADD_FRIEND);
        message.put(Constants.STATUS, Constants.SUCCESS);
        message.put(Constants.INFO, info);
        return message;
    }

    public static JSONObject buildAddFriendFailedMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.ADD_FRIEND);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }

    // CREATE GROUP
    public static JSONObject buildCreateGroupSuccessMessage(int groupId, String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.CREATE_GROUP);
        message.put(Constants.STATUS, Constants.SUCCESS);
        message.put(Constants.INFO, info);
        message.put(Constants.GROUP_ID, groupId);
        return message;
    }

    public static JSONObject buildCreateGroupFailedMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.CREATE_GROUP);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }

    // ADD GROUP MEMBER
    public static JSONObject buildAddGroupMembersSuccessMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.ADD_GROUP_MEMBERS);
        message.put(Constants.STATUS, Constants.SUCCESS);
        message.put(Constants.INFO, info);
        return message;
    }

    public static JSONObject buildAddGroupMembersFailedMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.ADD_GROUP_MEMBERS);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }

    // REMOVE GROUP MEMBER
    public static JSONObject buildRemoveGroupMembersSuccessMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.REMOVE_GROUP_MEMBERS);
        message.put(Constants.STATUS, Constants.SUCCESS);
        message.put(Constants.INFO, info);
        return message;
    }

    public static JSONObject buildRemoveGroupMembersFailedMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.REMOVE_GROUP_MEMBERS);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }

    // EXIT GROUP
    public static JSONObject buildExitGroupMembersSuccessMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.EXIT_GROUP);
        message.put(Constants.STATUS, Constants.SUCCESS);
        message.put(Constants.INFO, info);
        return message;
    }

    public static JSONObject buildExitGroupMembersFailedMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.EXIT_GROUP);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }

    // SEND PRIVATE CHAT
    public static JSONObject buildDeliverPrivateMessageSuccessMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.PRIVATE_MESSAGE);
        message.put(Constants.STATUS, Constants.SUCCESS);
        message.put(Constants.INFO, info);
        return message;
    }

    public static JSONObject buildDeliverPrivateMessageFailedMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.PRIVATE_MESSAGE);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }

    // SEND GROUP CHAT
    public static JSONObject buildDeliverGroupMessageSuccessMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.GROUP_MESSAGE);
        message.put(Constants.STATUS, Constants.SUCCESS);
        message.put(Constants.INFO, info);
        return message;
    }

    public static JSONObject buildDeliverGroupMessageFailedMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.GROUP_MESSAGE);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }

    // GET FRIENDS
    public static JSONObject buildGetFriendsSuccessMessage(ArrayList<String> friends, String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.GET_FRIENDS);
        message.put(Constants.STATUS, Constants.SUCCESS);
        JSONArray arr = new JSONArray();
        arr.addAll(friends);
        message.put(Constants.FRIENDS, arr);
        message.put(Constants.INFO, info);
        return message;
    }

    public static JSONObject buildGetFriendsFailedMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.GET_FRIENDS);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }

    // GET GROUPS
    public static JSONObject buildGetGroupsSuccessMessage(HashMap<Integer, String> groups, String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.GET_GROUPS);
        message.put(Constants.STATUS, Constants.SUCCESS);
        JSONArray arr = new JSONArray();
        for (Map.Entry<Integer, String> entry : groups.entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put(Constants.GROUP_ID, entry.getKey());
            obj.put(Constants.GROUP_NAME, entry.getValue());
            arr.add(obj);
        }
        message.put(Constants.GROUPS, arr);
        message.put(Constants.INFO, info);
        return message;
    }

    public static JSONObject buildGetGroupsFailedMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.GET_GROUPS);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }

    // GET GROUP MEMBERS
    public static JSONObject buildGetGroupMemberSuccessMessage(ArrayList<String> groupMembers, String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.GET_GROUP_MEMBERS);
        message.put(Constants.STATUS, Constants.SUCCESS);
        JSONArray arr = new JSONArray();
        arr.addAll(groupMembers);
        message.put(Constants.GROUP_MEMBERS, arr);
        message.put(Constants.INFO, info);
        return message;
    }

    public static JSONObject buildGetGroupMemberFailedMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.GET_GROUP_MEMBERS);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }

    public static JSONObject buildUnknownErrorMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.UNKNOWN);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }
}
