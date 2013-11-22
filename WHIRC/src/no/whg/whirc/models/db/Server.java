package no.whg.whirc.models.db;

public class Server {
	private long id;
	private String server;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getServer() {
		return server;
	}
	
	public void setServer(String server) {
		this.server = server;
	}
	
	@Override
	public String toString() {
		return server;
	}
}
