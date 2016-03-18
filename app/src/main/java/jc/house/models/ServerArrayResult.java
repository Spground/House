package jc.house.models;

import org.json.JSONArray;

/**
 * Created by hzj on 2016/3/14.
 */
public class ServerArrayResult extends ServerResult {
    public ServerArrayResult() {
        super();
        this.resultType = Type.Array;
    }
    public JSONArray array;
}
