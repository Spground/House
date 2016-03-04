package jc.house.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hzj on 2015/11/9.
 */
public final class HouseDetail extends House {
    private String houseType;
    private String forceType;
    private String address;
    private String recReason;
    private String trafficLines;
    private String designIdea;

    private CustomerHelper helper;

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

    public CustomerHelper getHelper() {
        return helper;
    }

    public void setHelper(CustomerHelper helper) {
        this.helper = helper;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(houseType);
        dest.writeString(forceType);
        dest.writeString(address);
        dest.writeString(recReason);
        dest.writeString(trafficLines);
        dest.writeString(designIdea);
    }

    private HouseDetail(Parcel origin) {
        super(origin);
        this.houseType = origin.readString();
        this.forceType = origin.readString();
        this.address = origin.readString();
        this.recReason = origin.readString();
        this.trafficLines = origin.readString();
        this.designIdea = origin.readString();
    }

    public static final Parcelable.Creator<HouseDetail> CREATOR = new Parcelable.Creator<HouseDetail>() {
        @Override
        public HouseDetail createFromParcel(Parcel source) {
            return new HouseDetail(source);
        }

        @Override
        public HouseDetail[] newArray(int size) {
            return new HouseDetail[size];
        }
    };
}
