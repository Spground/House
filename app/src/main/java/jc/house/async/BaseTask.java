package jc.house.async;

import jc.house.utils.LogUtils;

/**
 * Created by hzj on 2015/12/29.
 */
public abstract class BaseTask {

    public void onStart() {}

    public void onSuccess() {}

    public void onCode(int code) {}

    public void onFail(String msg) {
        LogUtils.debug(msg);
    }
}
