package no.whg.whirc.models.db;

public class User {
	private long id;
	private String nickOne;
	private String nickTwo;
	private String nickThree;
	private String name;
	private String address;
	private String password;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getNickOne() {
		return nickOne;
	}
	
	public void setNickOne(String nickOne) {
		this.nickOne = nickOne;
	}
	
	public String getNickTwo() {
		return nickTwo;
	}
	
	public void setNickTwo(String nickTwo) {
		this.nickTwo = nickTwo;
	}
	
	public String getNickThree() {
		return nickThree;
	}
	
	public void setNickThree(String nickThree) {
		this.nickThree = nickThree;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
