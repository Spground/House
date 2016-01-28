package jc.house.async;

import java.util.List;

import jc.house.global.ServerResultType;
import jc.house.models.BaseModel;
import jc.house.models.ServerResult;
import jc.house.utils.LogUtils;

/**
 * Created by hzj on 2015/12/29.
 */
public class ParseTask extends BaseTask {
    private ServerResult result;
    private Class<? extends BaseModel> mClass;
    private static final String TAG = "ParseTask";

    public ParseTask(ServerResult result, Class<? extends BaseModel> mClass) {
        this.result = result;
        this.mClass = mClass;
    }

    public ServerResultType getResultType() {
        return result.resultType;
    }

    public ServerResult getResult() {
        return result;
    }

    public Class<? extends BaseModel> getMClass() {
        return mClass;
    }

    public void onSuccess(List<? extends BaseModel> models) {
    }

    public void onSuccess(BaseModel model) {
    }

    public void onFail(String msg) {
        LogUtils.debug(TAG, msg);
    }
}
