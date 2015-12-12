package jc.house.async;

import java.util.List;

import jc.house.global.FetchType;
import jc.house.models.BaseModel;

/**
 * Created by hzj on 2015/12/11.
 */
public interface IParseData {
    void onParseDataTaskCompleted(List<BaseModel> lists, FetchType fetchType);
}
