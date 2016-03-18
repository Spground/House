package jc.house.async;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jc.house.models.BaseModel;
import jc.house.models.ServerArrayResult;
import jc.house.models.ServerObjectResult;
import jc.house.utils.ParseJson;
import jc.house.utils.SP;
import jc.house.utils.StringUtils;

/**
 * Created by hzj on 2016/3/12.
 */
public class FetchLocal {
    private static FetchLocal instance;
    private SP sp;
    private Handler mHandler;
    private FetchLocal(Context context) {
        this.sp = SP.with(context);
        this.mHandler = new Handler(Looper.myLooper());
    }
    public static FetchLocal share(Context context) {
        if (null == instance) {
            synchronized (FetchLocal.class) {
                if (null == instance) {
                    instance = new FetchLocal(context);
                }
            }
        }
        return instance;
    }

    public void fetchModelsFromLocal(final Class<? extends BaseModel> cls, final ModelsTask task) {
        String content = sp.getJsonString(cls);
        if (!StringUtils.strEmpty(content)) {
            try {
                final JSONArray array = new JSONArray(content);
                final ServerArrayResult result = new ServerArrayResult();
                result.array = array;
                MThreadPool.getInstance().submit(new Runnable() {
                    @Override
                    public void run() {
                        final List<? extends BaseModel> models = ParseJson.jsonArray2ModelList(array, cls);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                task.onSuccess(models, result);
                            }
                        });
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void fetchModelFromLocal(final Class<? extends BaseModel> cls, final ModelTask task) {
        String content = sp.getJsonString(cls);
        if (!StringUtils.strEmpty(content)) {
            try {
                final JSONObject object = new JSONObject(content);
                final ServerObjectResult result = new ServerObjectResult();
                result.object = object;
                MThreadPool.getInstance().submit(new Runnable() {
                    @Override
                    public void run() {
                        final BaseModel model = ParseJson.jsonObj2Model(object, cls);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                task.onSuccess(model, result);
                            }
                        });
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
