package jc.house.models;

import android.os.Parcel;
import android.os.Parcelable;

import jc.house.utils.StringUtils;

public class House extends BaseModel implements Parcelable {
	private String url;
	private String name;
	private String intro;
	private String phone;
	private int stars;
	private String labelContent;
	private double lat;
	private double lng;
	private String[] labelsResult;

	public House() {}

	public House(int id, String url, String name, String intro,
			String phone, double lat, double lng) {
		super(id);
		this.id = id;
		this.url = url;
		this.name = name;
		this.intro = intro;
		this.phone = phone;
		this.lat = lat;
		this.lng = lng;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(url);
		dest.writeString(name);
		dest.writeString(intro);
		dest.writeString(phone);
		dest.writeDouble(lat);
		dest.writeDouble(lng);
		dest.writeInt(stars);
		dest.writeString(labelContent);
	}

	private House(Parcel origin) {
		this.id = origin.readInt();
		this.url = origin.readString();
		this.name = origin.readString();
		this.intro = origin.readString();
		this.phone = origin.readString();
		this.lat = origin.readDouble();
		this.lng = origin.readDouble();
		this.stars = origin.readInt();
		this.labelContent = origin.readString();
	}

	public static final Parcelable.Creator<House> CREATOR = new Parcelable.Creator<House>() {
		@Override
		public House createFromParcel(Parcel source) {
			return new House(source);
		}

		@Override
		public House[] newArray(int size) {
			return new House[size];
		}
	};

	public String[] getLabelsResult() {
		if (null == labelsResult) {
			labelsResult = StringUtils.parseHouseLables(labelContent);
		}
		return labelsResult;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public void setLabelContent(String labelContent) {
		this.labelContent = labelContent;
	}
	public String getLabelContent() {
		return labelContent;
	}
	public void setStars(int stars) {
		this.stars = stars;
	}
	public int getStars() {
		return stars;
	}
}
