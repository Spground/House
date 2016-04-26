package jc.house.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzj on 2016/4/25.
 */
public class HouseHelpers extends BaseModel {
    private String name;
    private List<CustomerHelper> helpers;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setHelpers(List<CustomerHelper> helpers) {
        this.helpers = helpers;
    }

    public List<CustomerHelper> getHelpers() {
        return (helpers == null ? new ArrayList<CustomerHelper>(0) : helpers);
    }
}
