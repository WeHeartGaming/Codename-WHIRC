package no.whg.whirc.models.db;

public class Server {
	private long id;
	private String server;
	private String host;
	private String password;
	private String channelServer;
	private String channelName;
	private int port;
	
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
	
	public void setHost(String host)
	{
		this.host = host;
	}
	
	public String getHost()
	{
		return host;
	}
	
	public void setPasswordf(String password)
	{
		this.password = password;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setChannelServer(String channelServer)
	{
		this.channelServer = channelServer;
	}
	
	public String getChannelServer()
	{
		return channelServer;
	}
	
	public void setChannelName(String channelName)
	{
		this.channelName = channelName;
	}
	
	public String getChannelName()
	{
		return channelName;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
	
	public int getPort()
	{
		return port;
	}
}
