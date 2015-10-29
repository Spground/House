package jc.house.models;

public class ChatUser extends BaseModel {
	private String name;
	private String msg;
	private int imageResId;
	private String time;

	public ChatUser(int ID, String name, String msg, int imageResId, String time) {
		super(ID);
		this.name = name;
		this.msg = msg;
		this.imageResId = imageResId;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getImageResId() {
		return imageResId;
	}

	public void setImageResId(int imageResId) {
		this.imageResId = imageResId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}