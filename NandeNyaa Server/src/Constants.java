/**
 * Created by Tifani on 10/29/2016.
 */
public class Constants {
    public static final String SERVER_ADDRESS = "localhost";
    public static final String SERVER_QUEUERE_NAME = "rpc_nandenyaa";
    public static final String EXCHANGE_NAME = "nandenyaa_exchange";
    public static final String DEAD_LETTER_EXCHANGE_NAME = "dead_letter_nandenyaa_exchange";
    public static final String REQUEST_TYPE = "request_type";
    public static final String RESPONSE_TYPE = "response_type";

    // Request type
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String PRIVATE_MESSAGE = "private_message";
    public static final String GROUP_MESSAGE = "group_message";
    public static final String ADD_FRIEND = "add_friend";
    public static final String CREATE_GROUP = "create_group";
    public static final String ADD_GROUP_MEMBERS = "add_group_members";
    public static final String REMOVE_GROUP_MEMBERS = "remove_group_members";
    public static final String EXIT_GROUP = "exit_group";
    public static final String GET_FRIENDS = "get_friends";
    public static final String GET_GROUPS = "get_groups";
    public static final String GET_GROUP_MEMBERS = "get_group_members";

    // Response type
    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";
    public static final String UNKNOWN = "unknown";

    // Params
    public static final String DATE_TIME = "date_time";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String RECEIVER = "receiver";
    public static final String MESSAGE = "message";
    public static final String USER_TO_ADD = "user_to_add";
    public static final String GROUP_ID = "group_id";
    public static final String GROUP_NAME = "group_name";
    public static final String MEMBERS = "members";
    public static final String STATUS = "status";
    public static final String INFO = "info";
    public static final String FRIENDS = "friends";
    public static final String GROUPS = "groups";
    public static final String GROUP_MEMBERS = "group_members";
}
