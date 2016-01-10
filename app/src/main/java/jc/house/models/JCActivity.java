package jc.house.models;

/**
 * Created by hzj on 2015/10/30.
 */
public final class JCActivity extends BaseModel {
    private String title;
    private String picUrl;
    private long postTime;

    public JCActivity(int id, String picUrl, String title) {
        super(id);
        this.picUrl = picUrl;
        this.title = title;
    }

    public JCActivity() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }
}
