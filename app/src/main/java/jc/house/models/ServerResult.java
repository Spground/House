package jc.house.models;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by hzj on 2015/12/10.
 */
public class ServerResult {
    public static final int CODE_SUCCESS = 1;
    public static final int CODE_FAILURE = 0;
    public static final int CODE_NO_DATA = 2;
    public static final String CODE = "code";
    public static final String RESULT = "result";
    public int code;
    public boolean isSuccess;
    public JSONArray array;
    public JSONObject object;

    public ServerResult() {
        code = -1;
    }
}
