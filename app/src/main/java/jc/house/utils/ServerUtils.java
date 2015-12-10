package jc.house.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import cz.msebera.android.httpclient.HttpResponse;
import jc.house.global.Constants;
import jc.house.models.ServerResult;

/**
 * Created by hzj on 2015/12/10.
 */
public final class ServerUtils {
    public static boolean isConnectServerSuccess(int statusCode, JSONObject response) {
        return (statusCode == HttpURLConnection.HTTP_OK && null != response);
    }
    public static ServerResult parseServerResponse(JSONObject response) {
        ServerResult result = new ServerResult();
        try {
            int code = response.getInt("code");
            result.code = code;
            result.isSuccess = (ServerResult.CODE_SUCCESS == code);
            if (result.isSuccess) {
                result.array = response.getJSONArray("result");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
