package jc.house.models;

/**
 * Created by hzj on 2015/12/23.
 */
public class CustomerHelper extends BaseModel {
    private String name;
    private String hxID;
    private String picUrl;

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

    public String getHxID() {
        return hxID;
    }

    public void setHxID(String hxID) {
        this.hxID = hxID;
    }
}
