package jc.house.models;

public final class News extends BaseModel{
	private String url;
	private String title;
	private String author;
	private String date;

	public News() {

	}

	public News(int ID, String url, String title, String author, String date) {
		super(ID);
		this.url = url;
		this.title = title;
		this.author = author;
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
