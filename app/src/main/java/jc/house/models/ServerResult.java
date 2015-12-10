package jc.house.models;

import org.json.JSONArray;

/**
 * Created by hzj on 2015/12/10.
 */
public class ServerResult {
    public static final int CODE_SUCCESS = 1;
    public static final int CODE_FAILED = 0;
    public static final int CODE_NO_DATA = 2;
    public int code;
    public boolean isSuccess;
    public JSONArray array;

    public ServerResult() {
        code = -1;
    }
}
