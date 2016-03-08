package jc.house.async;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jc.house.global.Constants;
import jc.house.global.ServerResultType;
import jc.house.models.ServerResult;
import jc.house.utils.ServerUtils;

/**
 * Created by hzj on 2016/3/8.
 */
public class FetchServer {
    private static AsyncHttpClient client;
    private static AsyncHttpClient share() {
        if (null == client) {
            synchronized (FetchServer.class) {
                if (null == client) {
                    client = new AsyncHttpClient();
                }
            }
        }
        return client;
    }
    public static void fetchCompanyInfo(final StringTask task) {
        FetchServer.share().get(Constants.SERVER_URL + "introduction/images", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                handleStringTask(statusCode, response, task, "url");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                task.onFail(responseString);
            }
        });
    }

    private static void handleStringTask(int statusCode, JSONObject response, StringTask task, String key) {
        if (ServerUtils.isConnectServerSuccess(statusCode, response)) {
            ServerResult result = ServerUtils.parseServerResponse(response, ServerResultType.Object);
            if (result.isSuccess) {
                try {
                    task.onSuccess(result.object.getString(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                task.onCode(result.code);
            }
        } else {
            task.onFail("网络连接失败，statusCode is " + statusCode);
        }
    }

}
