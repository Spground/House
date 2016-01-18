package jc.house.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import jc.house.global.ServerResultType;
import jc.house.models.ServerResult;

/**
 * Created by hzj on 2015/12/10.
 */
public final class ServerUtils {

    public static boolean isConnectServerSuccess(int statusCode, JSONObject response) {
        return (statusCode == HttpURLConnection.HTTP_OK && null != response);
    }

    public static ServerResult parseServerResponse(JSONObject response, ServerResultType resultType) {
        ServerResult result = new ServerResult();
        try {
            int code = response.getInt(ServerResult.CODE);
            result.code = code;
            result.isSuccess = (ServerResult.CODE_SUCCESS == code);
            if (result.isSuccess) {
                if (ServerResultType.Array == resultType) {
                    result.array = response.getJSONArray(ServerResult.RESULT);
                    result.resultType = ServerResultType.Array;
                } else {
                    result.object = response.getJSONObject(ServerResult.RESULT);
                    result.resultType = ServerResultType.Object;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
