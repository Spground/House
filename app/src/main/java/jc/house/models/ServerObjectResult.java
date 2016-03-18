package jc.house.models;

import org.json.JSONObject;

/**
 * Created by hzj on 2016/3/14.
 */
public class ServerObjectResult extends ServerResult {
    public ServerObjectResult() {
        super();
        this.resultType = Type.Object;
    }
    public JSONObject object;
}
