/**
 * 
 */
package no.whg.whirc.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jerklib.Channel;
import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
import jerklib.events.IRCEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.events.MessageEvent;
import jerklib.listeners.IRCEventListener;
import no.whg.whirc.activities.MainActivity;
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
	Session qnet = null;
	
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
		Log.d(TAG, "onBind()");
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
	
	public void connect(String server){
		connect(server, this);
	}
	
	public void connect(final String server, final ConnectionService service) {
		
		final Runnable thread = new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "Connection thread started! [connect() called]");
//				while ((qnet == null) || (!qnet.isConnected())){
//					Log.d(TAG, "Attempting to connect! [if (qnet == null) || (!qnet.isConnected())]");
//					qnet = connection.requestConnection(server);
//					qnet.addIRCEventListener(service);
//					try {
//						Log.d(TAG, "Sleeping connection thread! [if (!qnet.isConnected()) called]");
//						Thread.sleep(15000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					if ((qnet == null) || (!qnet.isConnected())){
////						Log.d(TAG, "Retrying connect! [if (qnet == null) || (!qnet.isConnected())]");
//					} 
//					else if (qnet.isConnected()) {
//						Log.d(TAG, "Connected! [(qnet.isConnected())]");
//						addServer(qnet);
//
//						//qnet.join("#whg");
//					}
//				}
				int i;
				//for (i = 0; i < 3; i++){
					if ((qnet == null) || (!qnet.isConnected())){
						qnet = connection.requestConnection(server);
						qnet.addIRCEventListener(service);
//						try {
//							Log.d(TAG, "Sleeping connection thread! [if (!qnet.isConnected()) called]");
//							Thread.sleep(15000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						if (qnet.isConnected()) {
//							Log.d(TAG, "Connected! [(qnet.isConnected())]");
//							//addServer(qnet);
//							i = 9000;
//							//qnet.join("#whg");
//						} else {
//							qnet.removeIRCEventListener(service);
//							qnet = null;
//							Log.e(TAG, "Did not connect.");
//						}
					}
				//}
			}
		};
		threads.add(thread);
		handler.postDelayed(thread, 1000);
		//notification.tickerText = "Connected to " + qnet.getConnectedHostName();
		Log.d(TAG, "Thread created! toString: " + thread.toString() + " - hash: " + thread.hashCode());
		
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
			Log.d(TAG, "Server: irc.quakenet.org [as string] - Session name: " + qnet.getConnectedHostName());
		} else if (e.getType() == Type.CHANNEL_MESSAGE) {
			MessageEvent me = (MessageEvent)e;
			
			if (me.getMessage().contains(me.getSession().getNick())) {
				//TODO: Highlight in notification
			}
			Log.d(TAG, "Name: " + me.getNick() + "\nUsername: " + me.getUserName() + "\nChannel: " + me.getChannel() + "\nMessage: " + me.getMessage());
		} else {
			System.out.println(e.getType() + " : " + e.getRawEventData());
		}
	}
	
	public Server getCurrentServer(){
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
	
	private void addServer (Session s){
		if (getServer(s.getServerInformation().getServerName()) == null){
			Server myServer = new Server (s);
			serverList.add(myServer);
		}
	}
}
