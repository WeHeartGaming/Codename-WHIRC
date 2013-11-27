package no.whg.whirc.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
	
	public Server(Session session){
		this.simpleName = session.getServerInformation().getServerName();
		this.session = session;
	}


	public Conversation getConversation(String title) {
		return conversations.get(title);
	}
	
	public void addConversation(Conversation c) {
		conversations.put(c.getChannelID(), c);
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
	
	
}