package no.whg.whirc.models.db;

public class Channel {
	private long id;
	private String channel;
	private String server;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getServer() {
		return server;
	}
	
	public void setServer(String server) {
		this.server = server;
	}
	
	@Override
	public String toString() {
		String tempString = channel + " on " + server;
		return tempString;
	}
}
