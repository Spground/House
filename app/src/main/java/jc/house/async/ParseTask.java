package jc.house.async;

import java.util.List;

import jc.house.global.ServerResultType;
import jc.house.models.BaseModel;
import jc.house.utils.LogUtils;

/**
 * Created by hzj on 2015/12/29.
 */
public class ParseTask extends BaseTask {
    private Object args;
    private ServerResultType resultType;
    private Class<? extends BaseModel> mClass;
    private static final String TAG = "ParseTask";

    public ParseTask(Object args, ServerResultType resultType, Class<? extends BaseModel> mClass) {
        this.args = args;
        this.resultType = resultType;
        this.mClass = mClass;
    }

    public Object getArgs() {
        return args;
    }

    public ServerResultType getResultType() {
        return resultType;
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
