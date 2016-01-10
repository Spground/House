package jc.house.models;

public class BaseModel {
	public int id;

	public BaseModel() {

	}
	public BaseModel(int id) {
		this.id = id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
