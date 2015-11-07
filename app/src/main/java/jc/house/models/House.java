package jc.house.models;

public final class House extends BaseModel{
	private String url;
	private String name;
	private String intro;
	private String phone;
	private double lat;
	private double lng;
	
	public House(int ID, String url, String name, String intro,
			String phone, double lat, double lng) {
		super(ID);
		this.url = url;
		this.name = name;
		this.intro = intro;
		this.phone = phone;
		this.lat = lat;
		this.lng = lng;
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
	
}
