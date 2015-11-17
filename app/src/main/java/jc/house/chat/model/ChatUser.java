package jc.house.chat.model;

import jc.house.models.BaseModel;

public final class ChatUser extends BaseModel {
	private String name;
	private String msg;
	private String url;
	private String time;

	public ChatUser(int ID, String name, String msg, String url, String time) {
		super(ID);
		this.name = name;
		this.msg = msg;
		this.url = url;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}