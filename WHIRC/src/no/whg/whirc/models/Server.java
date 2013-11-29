package no.whg.whirc.models;

import java.util.ArrayList;
import java.util.List;

import jerklib.Channel;
import jerklib.Session;

public class Server {
	//private HashMap<String, Conversation> conversations;
	private Session session;
	private String nickOne;
	private String nickTwo;
	private String nickThree;
	private String name;
	private String host;
	private String port;
	private String password;
	private String simpleName;
	private ArrayList<Conversation> conversations;
	
	
	
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
	
	
	
	
	
	/**
	 * @param simpleName
	 * @param conversations
	 */
	public Server(String simpleName, ArrayList<Conversation> conversations) {
		super();
		this.simpleName = simpleName;
		this.conversations = conversations;
	}





	/**
	 * @param nickOne
	 * @param nickTwo
	 * @param nickThree
	 * @param name
	 * @param host
	 * @param port
	 * @param simpleName
	 */
	public Server(String nickOne, String nickTwo, String nickThree,
			String name, String host, String port, String simpleName) {
		super();
		this.nickOne = nickOne;
		this.nickTwo = nickTwo;
		this.nickThree = nickThree;
		this.name = name;
		this.host = host;
		this.port = port;
		this.simpleName = simpleName;
	}


	/**
	 * 
	 */
	public Server() {
		// EMPTY CONSTRUCTOR
	}
	/**
	 * 
	 * @param session
	 */
	public Server(Session session){
		this.simpleName = session.getRequestedConnection().getHostName();
		this.session = session;
		//this.conversations = new HashMap<String, Conversation>();
		this.conversations = new ArrayList<Conversation>();
		addConversation(new Conversation(this.simpleName, session));
		for (Channel c : session.getChannels()){
			addConversation(new Conversation(c));
		}
	}

	/**
	 * gets a conversation by title
	 * @param title
	 * @return
	 */
	public Conversation getConversation(String title) {
		for (Conversation c : conversations){
			if (c.getChannelTitle().equals(title)){
				return c;
			}
		}
		return null;
	}
	/**
	 * gets a conversation by index
	 * @param i
	 * @return
	 */
	public Conversation getConversation(int i) {
		return conversations.get(i);
	}
	/**
	 * adds a conversation
	 * @param c
	 */
	public void addConversation(Conversation c) {
		conversations.add(c);
	}
	/**
	 * removes a conversation by title
	 * @param title
	 */
	public void removeConversation(String title) {
		conversations.remove(title);
	}
	
	/**
	 * returns all the conversations
	 * @return
	 */
	public ArrayList<Conversation> getConversations(){
		return conversations;
	}
	/**
	 * returns the name
	 * @return the name
	 */
	public String getName(){
		return name;
	}
	/**
	 * sets the name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * gets the session
	 * @return the session
	 */
	public Session getSession(){
		return session;
	}
	/**
	 * gets nick one
	 * @return nick one
	 */
	public String getNickOne() {
		return nickOne;
	}
	/**
	 * sets nick one
	 * @param nickOne
	 */
	public void setNickOne(String nickOne) {
		this.nickOne = nickOne;
	}
	/**
	 * gets nick two
	 * @return nick two
	 */
	public String getNickTwo() {
		return nickTwo;
	}
	/**
	 * sets nick two
	 * @param nickTwo
	 */
	public void setNickTwo(String nickTwo) {
		this.nickTwo = nickTwo;
	}
	/**
	 * gets nick three
	 * @return nick three
	 */
	public String getNickThree() {
		return nickThree;
	}
	/**
	 * sets nick three
	 * @param nickThree
	 */
	public void setNickThree(String nickThree) {
		this.nickThree = nickThree;
	}
	/**
	 * gets the host
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * sets the host
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * gets the port
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	/**
	 * sets the port
	 * @param port
	 */
	public void setPort(String port) {
		this.port = port;
	}
	/**
	 * gets the simple name
	 * @return the simple name
	 */
	public String getSimpleName() {
		return simpleName;
	}
	/**
	 * sets the simple name
	 * @param simpleName
	 */
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	/**
	 * finds matching conversations
	 * @param channels
	 * @return the matched conversations
	 */
	public ArrayList<Conversation> getMatchingConversations(List<Channel> channels){
		ArrayList<Conversation> cons = new ArrayList<Conversation>();
		for (Conversation con : conversations){
			for (Channel chan : channels){
				if (con.getChannelTitle().equals(chan.getName())){
					cons.add(con);
				}
			}
		}
		return cons;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\nNick1: " + getNickOne() + "\nNick2: " + getNickTwo() + "\nNick3: " + getNickThree()
				+ "\nName: " + getName() + "\nPort: " + getPort() + "\nHost: " + getHost() + "\nSimpleName: " + getSimpleName();
	}
	
	
}