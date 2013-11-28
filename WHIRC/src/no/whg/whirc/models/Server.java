package no.whg.whirc.models;

import java.util.HashMap;

import jerklib.Channel;
import jerklib.Session;

public class Server {
	private HashMap<String, Conversation> conversations;
	private Session session;
	private String nickOne;
	private String nickTwo;
	private String nickThree;
	private String name;
	private String host;
	private String port;
	private String password;
	private String simpleName;
	
	
	
	/**
	 * @param session
	 * @param nickOne
	 * @param nickTwo
	 * @param nickThree
	 * @param name
	 * @param host
	 * @param port
	 * @param password
	 */
	public Server(Session session, String nickOne, String nickTwo,
			String nickThree, String name, String host, String port,
			String password) {
		this.session = session;
		this.nickOne = nickOne;
		this.nickTwo = nickTwo;
		this.nickThree = nickThree;
		this.name = name;
		this.host = host;
		this.port = port;
		this.password = password;
	}
	
	public Server() {
		// EMPTY CONSTRUCTOR
	}
	
	public Server(Session session){
		this.simpleName = session.getServerInformation().getServerName();
		this.session = session;
		this.conversations = new HashMap<String, Conversation>();
		addConversation(new Conversation(this.simpleName));
		for (Channel c : session.getChannels()){
			addConversation(new Conversation(c));
		}
	}


	public Conversation getConversation(String title) {
		return conversations.get(title);
	}
	
	public void addConversation(Conversation c) {
		conversations.put(c.getChannelTitle(), c);
	}
	
	public void removeConversation(String title) {
		conversations.remove(title);
	}
	
	public HashMap<String, Conversation> getConversations() {
		return conversations;
	}
	
	public String getName(){
		return simpleName;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Session getSession(){
		return session;
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	
	
}