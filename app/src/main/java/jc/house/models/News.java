package jc.house.models;

public final class News extends BaseModel{
	private String picUrl;
	private String title;
	private String author;
	private String time;

	public News() {

	}

	public News(int id, String picUrl, String title, String author, String time) {
		super(id);
		this.id = id;
		this.picUrl = picUrl;
		this.title = title;
		this.author = author;
		this.time = time;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String url) {
		this.picUrl = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String date) {
		this.time = date;
	}

}
