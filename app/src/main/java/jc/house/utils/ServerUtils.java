package jc.house.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import jc.house.models.ServerArrayResult;
import jc.house.models.ServerObjectResult;
import jc.house.models.ServerResult;

/**
 * Created by hzj on 2015/12/10.
 */
public final class ServerUtils {

    public static boolean isConnectServerSuccess(int statusCode, JSONObject response) {
        return (statusCode == HttpURLConnection.HTTP_OK && null != response);
    }

//    private static ServerResult parseServerResponse(JSONObject response, ServerResult.Type resultType) {
//        try {
//            int code = response.getInt(ServerResult.CODE);
//            boolean success = (ServerResult.CODE_SUCCESS == code);
//            if (success) {
//                if (ServerResult.Type.Array == resultType) {
//                    ServerArrayResult result = new ServerArrayResult();
//                    result.array = response.optJSONArray(ServerResult.RESULT);
//                    result.isSuccess = (null != result.array);
//                    return result;
//                } else {
//                    ServerObjectResult result = new ServerObjectResult();
//                    result.object = response.optJSONObject(ServerResult.RESULT);
//                    result.isSuccess = (null != result.object);
//                    return result;
//                }
//            } else {
//                ServerResult result = new ServerResult();
//                result.isSuccess = false;
//                return result;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     *  初步解析JSON数据
     * @param response response from the server.
     * @return ServerArrayResult
     */
    public static ServerArrayResult parseServerArrayResponse(JSONObject response) {
        ServerArrayResult result = new ServerArrayResult();
        int code = response.optInt(ServerResult.CODE);
        if (ServerResult.CODE_SUCCESS == code) {
            JSONArray array = response.optJSONArray(ServerResult.RESULT);
            result.array = array;
            result.code = code;
            result.isSuccess = (null != array);
        } else {
            result.code = code;
            result.isSuccess = false;
        }
        return result;
    }

    /**
     * 同上
     */
    public static ServerObjectResult parseServerObjectResponse(JSONObject response) {
        ServerObjectResult result = new ServerObjectResult();
        int code = response.optInt(ServerResult.CODE);
        if (ServerResult.CODE_SUCCESS == code) {
            JSONObject object = response.optJSONObject(ServerResult.RESULT);
            result.object = object;
            result.code = code;
            result.isSuccess = (null != object);
        } else {
            result.code = code;
            result.isSuccess = false;
        }
        return result;
    }
}
