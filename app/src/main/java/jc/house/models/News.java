package jc.house.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class News extends BaseModel implements Serializable{
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

	public boolean isNew() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(time);
			long timeStamp = date.getTime();
			if (System.currentTimeMillis() - timeStamp <= 7 * 24 * 3600 * 1000) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

}
