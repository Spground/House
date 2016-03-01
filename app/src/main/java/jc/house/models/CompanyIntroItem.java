package jc.house.models;

/**
 * Created by hzj on 2016/2/29.
 */
public class CompanyIntroItem extends BaseModel {
    private String name;
    private String url;
    public CompanyIntroItem() {}

    public CompanyIntroItem(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
