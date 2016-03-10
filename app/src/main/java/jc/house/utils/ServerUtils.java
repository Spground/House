package jc.house.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import jc.house.models.ServerResult;

/**
 * Created by hzj on 2015/12/10.
 */
public final class ServerUtils {

    public static boolean isConnectServerSuccess(int statusCode, JSONObject response) {
        return (statusCode == HttpURLConnection.HTTP_OK && null != response);
    }

    /**
     *  初步解析JSON数据
     * @param response response from the server.
     * @param resultType array or object
     * @return ServerResult
     */
    public static ServerResult parseServerResponse(JSONObject response, ServerResult.Type resultType) {
        ServerResult result = new ServerResult();
        try {
            int code = response.getInt(ServerResult.CODE);
            result.code = code;
            boolean success = (ServerResult.CODE_SUCCESS == code);
            if (success) {
                if (ServerResult.Type.Array == resultType) {
                    result.array = response.optJSONArray(ServerResult.RESULT);
                    result.isSuccess = (null != result.array);
                    result.resultType = ServerResult.Type.Array;
                } else {
                    result.object = response.optJSONObject(ServerResult.RESULT);
                    result.isSuccess = (null != result.object);
                    result.resultType = ServerResult.Type.Object;
                }
            } else {
                result.isSuccess = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
