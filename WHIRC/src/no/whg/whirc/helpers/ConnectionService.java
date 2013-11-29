/**
 * 
 */
package no.whg.whirc.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import jerklib.Channel;
import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
import jerklib.events.AwayEvent;
import jerklib.events.CtcpEvent;
import jerklib.events.IRCEvent;
import jerklib.events.InviteEvent;
import jerklib.events.JoinCompleteEvent;
import jerklib.events.JoinEvent;
import jerklib.events.KickEvent;
import jerklib.events.MotdEvent;
import jerklib.events.NickChangeEvent;
import jerklib.events.NickInUseEvent;
import jerklib.events.NickListEvent;
import jerklib.events.PartEvent;
import jerklib.events.QuitEvent;
import jerklib.events.ServerVersionEvent;
import jerklib.events.TopicEvent;
import jerklib.events.WhoEvent;
import jerklib.events.WhoisEvent;
import jerklib.events.WhowasEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.events.modes.ModeEvent;
import jerklib.events.MessageEvent;
import jerklib.listeners.IRCEventListener;
import no.whg.whirc.activities.MainActivity;
import no.whg.whirc.models.Conversation;
import no.whg.whirc.models.Message;
import no.whg.whirc.models.Server;
import android.R;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ConnectionService extends Service implements IRCEventListener {
	private static final String TAG = "ConnectionService";
	private Handler handler;
	private final ConnectionServiceBinder binder;
	private ArrayList<Runnable> threads;
	Notification notification;
	
	
	// irc object
	ConnectionManager connection;
	//Session qnet = null;
	
	private ArrayList <Server> serverList;
	private int currentServer;
	

	public ConnectionService() {
		super();
		
		Random random = new Random();
		String name = "whirc";
		name += (String.valueOf(random.nextInt(9)) + String.valueOf(random.nextInt(9)) + String.valueOf(random.nextInt(9)));
		connection = new ConnectionManager(new Profile(name));
		
		this.handler = new Handler();
		this.threads = new ArrayList<Runnable>();
		this.binder = new ConnectionServiceBinder(this);
		Log.e(TAG, "constructor");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Service bound! [onBind() called]");
		
		// lets see if shit keeps running or not now
		startService(new Intent(this, ConnectionService.class));
		
		if (!connection.getSessions().isEmpty()) {
			for(Session s : connection.getSessions()) {
				s.removeIRCEventListener(this);
			}
		}
		return this.binder;
	}
	
	

	/* (non-Javadoc)
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		
		if (!connection.getSessions().isEmpty()) {
			for(Session s : connection.getSessions()) {
				s.addIRCEventListener(this);
			}
		}
		Log.d(TAG, "onUnbind()");
		return super.onUnbind(intent);
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Load servers from file
		super.onCreate();
		Log.d("ConnectionService", "Service created! [onCreate() called]");
		serverList = new ArrayList <Server>();
		
		//connect("irc.quakenet.org", this);
		
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        
        PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        
        notification = 
                        new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_info_details)
                        .setTicker("Starting IRC")
                        
                        .setContentTitle("WHIRC")
                        .setContentText("Not connected.")
                        .setContentIntent(pending)
                        .build();
        
        startForeground(1337, notification);
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "Service destroyed! [onDestroy() called]");
		for(Runnable t : threads) {
			handler.removeCallbacks(t);
			Log.d(TAG, "Removed callback on thread " + t.toString() + " (" + t.hashCode() + ")");
		}
		stopSelf();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Service started! [onStartCommand() called]");
		return START_STICKY;
	}
	
	// Not used!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public void connect(final String server, final ConnectionService service) {
		final Runnable thread = new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "Connection thread started! [connect() called from ConnectionService]");
				Session mySession = null;
				mySession = connection.requestConnection(server);
				mySession.addIRCEventListener(service);
			}
		};
		threads.add(thread);
		handler.postDelayed(thread, 1000);
		Log.d(TAG, "Thread created! toString: " + thread.toString() + " - hash: " + thread.hashCode());
	}
	
	public void connect(final String server, final MainActivity service) {
		final Runnable thread = new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "Connection thread started! [connect() called from MainActivity]");
				Session mySession = null;
				mySession = connection.requestConnection(server);
				mySession.addIRCEventListener(service);
					
			}
		};
		Log.d(TAG, "Thread created! toString: " + thread.toString() + " - hash: " + thread.hashCode());
		threads.add(thread);
		handler.postDelayed(thread, 1000);
	}
	
	public void shutdown() {
		for(Runnable t : threads) {
			handler.removeCallbacks(t);
		}
		stopSelf();
	}
	
	public List<Channel> getChannelsForSession(String name) {
		return connection.getSession(name).getChannels();

	}
	
	public List<Session> getSessions() {
		return connection.getSessions();
	}
	
	public boolean isSessionConnected(String name) {
		return connection.getSession(name).isConnected();
	}

	@Override
	public void receiveEvent(IRCEvent e) {
		
		if (e.getType() == Type.CONNECT_COMPLETE) {
			addServer(e.getSession());
		} else if (e.getType() == Type.TOPIC) {
			TopicEvent te = (TopicEvent)e;
			Server server = getServer(te.getSession());
			Conversation conversation = server.getConversation(te.getChannel().getName());
			conversation.addMessage(te);
		} else if (e.getType() == Type.CHANNEL_MESSAGE) {
			MessageEvent me = (MessageEvent)e;
			Server server = getServer(me.getSession());
			Conversation conversation = server.getConversation(me.getChannel().getName());
			if (!conversation.hasMessage(me.hashCode())){
				conversation.addMessage(me);
			} else {
				Log.e(TAG, "receiveEvent() CHANNEL_MESSAGE: Message already exists, did not add it to Conversation.");
			}
			if (me.getMessage().contains(me.getSession().getNick())) {
				//TODO: Highlight in notification
			}
		} else if (e.getType() == Type.JOIN){
			JoinEvent je = (JoinEvent)e;
			Server server = getServer(je.getSession());
			Conversation conversation = server.getConversation(je.getChannel().getName());
			if (!conversation.hasMessage(je.hashCode())){
				conversation.addUser(je.getNick());
				conversation.addMessage(je);
			} else {
				Log.e(TAG, "receiveEvent() JOIN: JOIN Message already exists, did not add it to Conversation.");
			}
		} else if (e.getType() == Type.PART){
			PartEvent pe = (PartEvent)e;
			Server server = getServer(pe.getSession());
			Conversation conversation = server.getConversation(pe.getChannel().getName());
			if (!conversation.hasMessage(pe.hashCode())){
				conversation.removeUser(pe.getWho());
				conversation.addMessage(pe);
			} else {
				Log.e(TAG, "receiveEvent() PART: PART Message already exists, did not add it to Conversation.");
			}
		} else if (e.getType() == Type.KICK_EVENT){
			KickEvent ke = (KickEvent)e;
			Server server = getServer(ke.getSession());
			Conversation conversation = server.getConversation(ke.getChannel().getName());
			if (!conversation.hasMessage(ke.hashCode())){
				conversation.removeUser(ke.getWho());
				conversation.addMessage(ke);
			} else {
				Log.e(TAG, "receiveEvent() KICK: KICK Message already exists, did not add it to Conversation.");
			}
		} else if (e.getType() == Type.MODE_EVENT){
			System.out.println(e.getType() + " : " + e.getRawEventData());
			ModeEvent me = (ModeEvent)e;
			Server server = getServer(me.getSession());
			Conversation conversation;
			if (me.getChannel() == null){
				conversation = server.getConversation(0); // 0 is always the position of the server conversation. 
			} else {
				conversation = server.getConversation(me.getChannel().getName());
				conversation.makeUserList();
			}
			if (!conversation.hasMessage(me.hashCode())){
				conversation.addMessage(me);
			} else {
				Log.e(TAG, "receiveEvent() MODE_EVENT: MODE_EVENT Message already exists, did not add it to Conversation.");
			}
		} else if (e.getType() == Type.AWAY_EVENT){
			AwayEvent ae = (AwayEvent)e;
			Server server = getServer(ae.getSession());
			Conversation conversation = server.getConversation(0); // 0 is always the position of the server conversation. 
			if (!conversation.hasMessage(ae.hashCode())){
				conversation.addMessage(ae);
			} else {
				Log.e(TAG, "receiveEvent() AWAY_EVENT: AWAY_EVENT Message already exists, did not add it to Conversation.");
			}
		} else if (e.getType() == Type.PRIVATE_MESSAGE){
			MessageEvent me = (MessageEvent)e;
			Server server = getServer(me.getSession());
			Conversation conversation;
			String nick = me.getNick();
			if (server.getConversation(nick) == null){
				conversation = new Conversation(me.getChannel(), nick);
				server.addConversation(conversation);
			} else {
				conversation = server.getConversation(me.getChannel().getName());
			}
			if (!conversation.hasMessage(me.hashCode())){
				conversation.addMessage(me);
			} else {
				Log.e(TAG, "receiveEvent() PRIVATE_MESSAGE: PRIVATE_MESSAGE Message already exists, did not add it to Conversation.");
			}
		} else if (e.getType() == Type.MOTD) {
			MotdEvent me = (MotdEvent)e;
			Server server = getServer(me.getSession());
			Conversation conversation = server.getConversation(0); // 0 is always the position of the server conversation. 
			if (!conversation.hasMessage(me.hashCode())){
				conversation.addMessage(me);
			} else {
				Log.e(TAG, "receiveEvent() MOTD: Message already exists, did not add it to Conversation.");
			}
		} else if (e.getType() == Type.JOIN_COMPLETE){
			Session session = e.getSession();
			JoinCompleteEvent jce = (JoinCompleteEvent) e;
			Channel channel = jce.getChannel();
			Conversation conversation = null;
			Server server = getServer(session.getRequestedConnection().getHostName());
			if (server != null){
				conversation = server.getConversation(channel.getName());
				if (conversation == null){
			    	conversation = new Conversation(channel);
			    	server.addConversation(conversation);
				} else {
			    	Log.e(TAG, "receiveEvent() JOIN_COMPLETE: Conversation " + session.getRequestedConnection().getHostName() + channel.getName() + "already exists.");
				}
			} else {
				Log.e(TAG, "receiveEvent() JOIN_COMPLETE: This server does not exist.");
			}
			Log.e(TAG, "receiveEvent() JOIN_COMPLETE.");
		} else if (e.getType() == Type.CTCP_EVENT){
			CtcpEvent ce = (CtcpEvent)e;
			String cm = ce.getCtcpString();
			String[] ctcp = cm.split(" ", 2);
			if(ctcp[0].equals("ACTION")){
				Server server = getServer(ce.getSession());
				Conversation conversation = server.getConversation(ce.getChannel().getName());
				if (!conversation.hasMessage(ce.hashCode())){
					conversation.addMessage(ce, ctcp[1]);
				} else {
					Log.e(TAG, "receiveEvent() CTCP_EVENT: CTCP ACTION already exists, did not add it to Conversation.");
				}
			}
		} else if (e.getType() == Type.SERVER_VERSION_EVENT){
			ServerVersionEvent sve = (ServerVersionEvent)e;
			Server server = getServer(sve.getSession());
			Conversation conversation = server.getConversation(0); // 0 is always the position of the server conversation. 
			if (!conversation.hasMessage(sve.hashCode())){
				conversation.addMessage(sve);
			} else {
				Log.e(TAG, "receiveEvent() SERVER_VERSION_EVENT: Message already exists, did not add it to Conversation.");
			}
		} else if (e.getType() == Type.QUIT){
			QuitEvent qe = (QuitEvent)e;
			Server server = getServer(qe.getSession());
			List<Channel> channels = qe.getChannelList();
			ArrayList<Conversation> conversations = server.getMatchingConversations(channels);
			if (conversations != null){
				for (Conversation conversation : conversations){
					if (!conversation.hasMessage(qe.hashCode())){
						conversation.removeUser(qe.getNick());
						conversation.addMessage(qe);
					} else {
						Log.e(TAG, "receiveEvent() QUIT: QUIT Message already exists, did not add it to Conversation.");
					}
				}
			}
		} else if (e.getType() == Type.NOTICE){
			Log.d(TAG, e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.WHO_EVENT){
			Log.d(TAG, e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.WHOIS_EVENT){
			Log.d(TAG, e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.WHOWAS_EVENT){
			Log.d(TAG, e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.NICK_IN_USE){
			NickInUseEvent niue = (NickInUseEvent)e;
			Random random = new Random();
			String newNick = niue.getInUseNick();
			newNick += (String.valueOf(random.nextInt(9)) + String.valueOf(random.nextInt(9)) + String.valueOf(random.nextInt(9)));
			Server server = getServer(niue.getSession());
			Conversation conversation = server.getConversation(0); // 0 is always the position of the server conversation.
			if (!conversation.hasMessage(niue.hashCode())){
				conversation.addMessage(niue, newNick);
				niue.getSession().changeNick(newNick);
			}
		} else if (e.getType() == Type.NICK_LIST_EVENT){
			NickListEvent nle = (NickListEvent)e;
			Server server = getServer(nle.getSession());
			Conversation conversation = server.getConversation(nle.getChannel().getName());
			conversation.makeUserList();
		} else if (e.getType() == Type.NICK_CHANGE){
			NickChangeEvent nce = (NickChangeEvent)e;
			Server server = getServer(nce.getSession());
			ArrayList<Conversation> conversations = server.getConversations();
			for (Conversation conversation : conversations){
				if (conversation.hasUser(nce.getOldNick())){
					if (!conversation.hasMessage(nce.hashCode())){
						conversation.makeUserList();
						conversation.addMessage(nce);
					} else {
						Log.e(TAG, "receiveEvent() NICK_CHANGE: NICK_CHANGE Message already exists, did not add it to Conversation.");
					}
				}
			}
		}
			

		// This should work, but jerklib dies if we try inviting the client somewhere
		else if (e.getType() == Type.INVITE_EVENT){
			Log.d(TAG, e.getType() + " : " + e.getRawEventData());
		}	
		// Everything under here is being ignored.
		else if (e.getType() == Type.CHANNEL_LIST_EVENT){
			// We don't need this
			Log.d(TAG, e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.UPDATE_HOST_NAME){
			// We don't need this
			Log.d(TAG, e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.SERVER_INFORMATION){
			// Fuck this
			Log.d(TAG, e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.DCC_EVENT){
			// We don't need this
			Log.d(TAG, e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.ERROR){
			// We don't need this
			Log.d(TAG, e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.EXCEPTION){
			// We don't need this
			Log.d(TAG, e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.DEFAULT){
			// We don't need this
			Log.d(TAG, e.getType() + " : " + e.getRawEventData());
		} else {
			// This is just for show
			Log.d(TAG, e.getType() + " : " + e.getRawEventData());
		}
	}
	
	public Server getCurrentServer(){
		// TODO make this not static
		if (serverList.size() > 0){
			return serverList.get(0);
		} else {
			return null;
		}
	}
	
	public ArrayList<Server> getServerList(){
		return serverList;
	}
	
	public Server getServer(int i){
		if (i >= serverList.size()){
			return null;
		} else {
			return serverList.get(i);
		}
	}
	
	public Server getServer(String s){
		if (!serverList.isEmpty()){
			for (Server serv : serverList){
				if (serv.getName().equals(s)){
					return serv;
				}
			}
		}
		return null;
	}
	
	public Server getServer(Session s){
		if (!serverList.isEmpty()){
			for (Server serv : serverList){
				if (serv.getSession() == s){
					return serv;
				}
			}
		}
		return null;
	}
	
	public void addServer (Session s){
		if (getServer(s.getServerInformation().getServerName()) == null){
			Server myServer = new Server (s);
			serverList.add(myServer);
			Log.d(TAG, "Added " + s.getConnectedHostName() + " to serverList.");
		}
	}
	
	public ConnectionManager getConnection(){
		return connection;
	}
	
	public ArrayList<Runnable> getThreads(){
		return threads;
	}
	
	public Handler getHandler(){
		return handler;
	}
}
