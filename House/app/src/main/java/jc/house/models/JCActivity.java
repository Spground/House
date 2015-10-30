package jc.house.models;

import jc.house.utils.StringUtils;

/**
 * Created by hzj on 2015/10/30.
 */
public class JCActivity extends BaseModel {
    private String picUrl;
    private String name;
    public JCActivity(int ID, String picUrl, String name) {
        super(ID);
        this.picUrl = picUrl;
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
