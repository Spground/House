package jc.house.models;

/**
 * Created by hzj on 2016/1/4.
 */
public class Slideshow extends BaseModel {
    private String picUrl;

    public Slideshow(){}

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }
}
