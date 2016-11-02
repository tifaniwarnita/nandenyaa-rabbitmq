import org.json.simple.JSONObject;

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

    public static JSONObject buildUnknownErrorMessage(String info) {
        JSONObject message = new JSONObject();
        message.put(Constants.RESPONSE_TYPE, Constants.UNKNOWN);
        message.put(Constants.STATUS, Constants.FAILED);
        message.put(Constants.INFO, info);
        return message;
    }
}
