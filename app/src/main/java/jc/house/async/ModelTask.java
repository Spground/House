package jc.house.async;

import jc.house.models.BaseModel;
import jc.house.models.ServerResult;

/**
 * Created by hzj on 2016/3/9.
 */
public abstract class ModelTask extends BaseTask {
    public abstract void onSuccess(BaseModel model, ServerResult result);
}
