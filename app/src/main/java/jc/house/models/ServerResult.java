package jc.house.models;

/**
 * Created by hzj on 2015/12/10.
 */
public class ServerResult {
    public enum Type {
        Array,
        Object
    }
    public static final int CODE_SUCCESS = 1;
    public static final int CODE_FAILURE = 0;
    public static final int CODE_NO_DATA = 2;
    public static final String CODE = "code";
    public static final String RESULT = "result";
    public int code;
    public boolean isSuccess;
    public Type resultType;
    public ServerResult() {
        code = -1;
        isSuccess = false;
    }
}
