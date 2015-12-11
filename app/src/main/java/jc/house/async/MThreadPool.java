package jc.house.async;

import android.util.Log;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jc.house.global.FetchType;
import jc.house.models.BaseModel;
import jc.house.utils.ParseJson;

/**
 * Created by hzj on 2015/12/11.
 */
public class MThreadPool {
    private static MThreadPool instance = null;
    private ExecutorService executorService = null;
    private static final int THREAD_NUM = 3;

    private MThreadPool() {
        executorService = Executors.newFixedThreadPool(THREAD_NUM);
    }

    public static MThreadPool getInstance() {
        if (null == instance) {
            synchronized (MThreadPool.class) {
                if (null == instance) {
                    instance = new MThreadPool();
                }
            }
        }
        return instance;
    }

    public void submitParseDataTask(JSONArray array, Class<? extends BaseModel> mClass, FetchType fetchType, IParseData praseData) {
        this.executorService.submit(new ParseDataThread(array, mClass, fetchType,praseData));
    }

    private class ParseDataThread implements Runnable {
        private JSONArray array;
        private Class<? extends BaseModel> mClass;
        private FetchType fetchType;
        private IParseData parseData;

        public ParseDataThread(JSONArray array, Class<? extends BaseModel> mClass, FetchType fetchType, IParseData parseData) {
            this.array = array;
            this.mClass = mClass;
            this.fetchType = fetchType;
            this.parseData = parseData;
        }

        @Override
        public void run() {
            List<BaseModel> lists = ParseJson.jsonArray2ModelList(array, mClass);
            parseData.onTaskCompleted(lists, fetchType);
        }
    }
}
