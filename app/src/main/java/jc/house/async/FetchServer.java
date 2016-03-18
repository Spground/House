package jc.house.async;

import android.os.Handler;
import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import jc.house.global.Constants;
import jc.house.models.BaseModel;
import jc.house.models.ServerArrayResult;
import jc.house.models.ServerObjectResult;
import jc.house.models.ServerResult;
import jc.house.utils.ParseJson;
import jc.house.utils.ServerUtils;

/**
 * Created by hzj on 2016/3/8.
 */
public class FetchServer {
    private static FetchServer instance;
    private AsyncHttpClient client;
    private Handler mHandler = null;

    private FetchServer() {
        this.client = new AsyncHttpClient();
        this.mHandler = new Handler(Looper.myLooper());
    }

    public static FetchServer share() {
        if (null == instance) {
            synchronized (FetchServer.class) {
                if (null == instance) {
                    instance = new FetchServer();
                }
            }
        }
        return instance;
    }

    public void fetchCompanyImages(final StringTask task) {
        client.get(Constants.SERVER_URL + "introduction/images", new JsonHttpResponseHandler() {
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

    private void handleStringTask(int statusCode, JSONObject response, final StringTask task, final String key) {
        if (ServerUtils.isConnectServerSuccess(statusCode, response)) {
            final ServerObjectResult result = ServerUtils.parseServerObjectResponse(response);
            if (result.isSuccess) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        task.onSuccess(result.object.optString(key));
                    }
                });
            } else {
                task.onCode(result.code);
            }
        } else {
            task.onFail("网络连接失败，statusCode is " + statusCode);
        }
    }

    public void postModelsFromServer(String url, Map<String, String> params, final Class<? extends BaseModel> cls, final ModelsTask task) {
        client.post(url, new RequestParams(params), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                handleModelsTask(statusCode, response, cls, task);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                task.onFail(responseString);
            }
        });
    }

    private void handleModelsTask(int statusCode, JSONObject response, final Class<? extends BaseModel> cls, final ModelsTask task) {
        if (ServerUtils.isConnectServerSuccess(statusCode, response)) {
            final ServerArrayResult result = ServerUtils.parseServerArrayResponse(response);
            if (result.isSuccess) {
                MThreadPool.getInstance().submit(new Runnable() {
                    @Override
                    public void run() {
                        final List<? extends BaseModel> models = ParseJson.jsonArray2ModelList(result.array, cls);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                task.onSuccess(models, result);
                            }
                        });
                    }
                });
            } else {
                task.onCode(result.code);
            }
        } else {
            task.onFail("网络连接失败，statusCode is " + statusCode);
        }
    }

    public void postModelFromServer(String url, Map<String, String> params, final Class<? extends BaseModel> cls, final ModelTask task) {
        client.post(url, new RequestParams(params), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                handleModelTask(statusCode, response, cls, task);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                task.onFail(responseString);
            }
        });
    }

    private void handleModelTask(int statusCode, JSONObject response, final Class<? extends BaseModel> cls, final ModelTask task) {
        if (ServerUtils.isConnectServerSuccess(statusCode, response)) {
            final ServerObjectResult result = ServerUtils.parseServerObjectResponse(response);
            if (result.isSuccess) {
                MThreadPool.getInstance().submit(new Runnable() {
                    @Override
                    public void run() {
                        final BaseModel model = ParseJson.jsonObj2Model(result.object, cls);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                task.onSuccess(model, result);
                            }
                        });
                    }
                });
            } else {
                task.onCode(result.code);
            }
        } else {
            task.onFail("网络连接失败，statusCode is " + statusCode);
        }
    }

}
