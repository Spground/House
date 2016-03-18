package jc.house.async;

import java.util.List;

import jc.house.models.BaseModel;
import jc.house.models.ServerArrayResult;

/**
 * Created by hzj on 2016/3/9.
 */
public abstract class ModelsTask extends BaseTask {
    public abstract void onSuccess(List<? extends BaseModel> models, ServerArrayResult result);
}
