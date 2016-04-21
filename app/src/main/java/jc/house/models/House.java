package jc.house.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import jc.house.global.Constants;
import jc.house.utils.StringUtils;

public class House extends BaseModel implements Parcelable {
	public static final int MAX_INTRO_LENGTH = 25;
	private String url;
	private String hxUrl;
	private String videoUrl;
	private String otherUrl;
	private String circleUrl;
	private String name;
	private String intro;
	private String phone;
	private int stars;
	private String labelContent;
	private double lat;
	private double lng;
	protected String avgPrice;
	private String[] labelsResult;
	private String[] imageUrls;
	private String[] circleUrls;
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
		dest.writeString(avgPrice);
		dest.writeString(hxUrl);
		dest.writeString(videoUrl);
		dest.writeString(otherUrl);
		dest.writeString(circleUrl);
	}

	protected House(Parcel origin) {
		this.id = origin.readInt();
		this.url = origin.readString();
		this.name = origin.readString();
		this.intro = origin.readString();
		this.phone = origin.readString();
		this.lat = origin.readDouble();
		this.lng = origin.readDouble();
		this.stars = origin.readInt();
		this.labelContent = origin.readString();
		this.avgPrice = origin.readString();
		this.hxUrl = origin.readString();
		this.videoUrl = origin.readString();
		this.otherUrl = origin.readString();
		this.circleUrl = origin.readString();
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

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(name + "-").append(url + "-").append(id + "-").append(lat + "-").append(labelContent + "-").append(avgPrice)
		.append(videoUrl + "-");
		return stringBuilder.toString();
	}

	public boolean isValid() {
		return (null != name) && (null != labelContent) && (null != url) && (null != intro) && (null != avgPrice);
	}

	public String[] getLabelsResult() {
		if (null == labelsResult) {
			labelsResult = StringUtils.parseHouseLables(labelContent);
		}
		return labelsResult;
	}

	public String[] getCircleUrls() {
		if (null == circleUrls) {
			circleUrls = StringUtils.parseImageUrlsOrigin(circleUrl);
		}
		return circleUrls;
	}

	public String[] getImageUrls() {
		if (null == imageUrls) {
			List<String> list = new ArrayList<>();
			if (!StringUtils.strEmpty(hxUrl) && !hxUrl.equalsIgnoreCase("null")) {
				list.add(originImageUrl(url));
				list.add(originImageUrl(hxUrl));
//				imageUrls = new String[2];
//				imageUrls[0] = Constants.IMAGE_URL_ORIGIN + url;
//				imageUrls[1] = Constants.IMAGE_URL_ORIGIN + hxUrl;
			} else {
//				imageUrls = new String[1];
//				imageUrls[0] = Constants.IMAGE_URL_ORIGIN + url;
				list.add(originImageUrl(url));
			}
			if (!StringUtils.strEmpty(otherUrl) && !otherUrl.equalsIgnoreCase("null")) {
				String[] urls = StringUtils.parseImageUrlsOrigin(otherUrl);
				if (null != urls && urls.length > 0) {
					for (int i = 0; i < urls.length; i++) {
						list.add(urls[i]);
					}
				}
			}
			String[] array = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				array[i] = list.get(i);
			}
			imageUrls = array;
		}
		return imageUrls;
	}

	private String originImageUrl(String url) {
		return Constants.IMAGE_URL_ORIGIN + url;
	}

	private String smallImageUrl(String url) {
		return Constants.IMAGE_URL_THUMBNAIL + url;
	}

	public String getLabelString() {
		if (null != this.getLabelsResult()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < labelsResult.length; i++) {
				sb.append(labelsResult[i]);
				if (i == 0) {
					sb.append("  ");
				}
			}
			return sb.toString();
		}
		return null;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public void setHxUrl(String hxUrl) {
		this.hxUrl = hxUrl;
	}

	public void setOtherUrl(String otherUrl) {
		this.otherUrl = otherUrl;
	}

	public void setCircleUrl(String circleUrl) {
		this.circleUrl = circleUrl;
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

	public String getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(String avgPrice) {
		this.avgPrice = avgPrice;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
}
