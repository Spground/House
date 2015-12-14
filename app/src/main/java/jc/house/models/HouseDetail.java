package jc.house.models;

/**
 * Created by hzj on 2015/11/9.
 */
public final class HouseDetail extends House {
    private String houseType;
    private String forceType;
    private String avgPrice;
    private String address;
    private String recReason;
    private String trafficLines;
    private String designIdea;

    public HouseDetail() {}

    public HouseDetail(int id, String url, String name, String intro,
                       String phone, double lat, double lng, String houseType, String forceType, String avgPrice, String address, String recReason, String trafficLines, String designIdea) {
        super(id, url, name, intro, phone, lat, lng);
        this.houseType = houseType;
        this.forceType = forceType;
        this.avgPrice = avgPrice;
        this.address = address;
        this.recReason = recReason;
        this.trafficLines = trafficLines;
        this.designIdea = designIdea;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setForceType(String forceType) {
        this.forceType = forceType;
    }

    public String getForceType() {
        return forceType;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setRecReason(String recReason) {
        this.recReason = recReason;
    }

    public String getRecReason() {
        return recReason;
    }

    public void setTrafficLines(String trafficLines) {
        this.trafficLines = trafficLines;
    }

    public String getTrafficLines() {
        return trafficLines;
    }

    public void setDesignIdea(String designIdea) {
        this.designIdea = designIdea;
    }

    public String getDesignIdea() {
        return designIdea;
    }
}
