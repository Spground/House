package jc.house.async;

import java.util.List;

import jc.house.models.BaseModel;
import jc.house.utils.LogUtils;

/**
 * Created by hzj on 2015/12/29.
 */
public class ParseTask {
    private static final String TAG = "ParseTask";

    public void onStart() {
    }

    public void onSuccess(List<? extends BaseModel> models) {
    }

    public void onSuccess(BaseModel model) {
    }

    public void onFail(String msg) {
        LogUtils.debug(TAG, msg);
    }
}
