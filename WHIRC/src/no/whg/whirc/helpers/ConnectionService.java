/**
 * 
 */
package no.whg.whirc.helpers;

import java.io.Serializable;
import java.util.ArrayList;

import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
import jerklib.events.IRCEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.events.JoinCompleteEvent;
import jerklib.listeners.IRCEventListener;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class ConnectionService extends Service implements IRCEventListener {
	private Handler handler;
	private final ConnectionServiceBinder binder;
	private ArrayList<Runnable> threads;
	
	
	// irc object
	ConnectionManager connection = new ConnectionManager(new Profile("whirc"));
	Session qnet;
	

	public ConnectionService() {
		super();
		
		this.handler = new Handler();
		this.threads = new ArrayList<Runnable>();
		this.binder = new ConnectionServiceBinder(this);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("ConnectionService::onBind ran");
		return this.binder;
	}
	
	

	/* (non-Javadoc)
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("Service created");
		connect("irc.quakenet.org", this);
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(this, "service stopping", Toast.LENGTH_SHORT).show();
		for(Runnable t : threads) {
			handler.removeCallbacks(t);
		}
		stopSelf();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		System.out.println("service started");
		return START_STICKY;
	}
	
	public void connect(final String server, final ConnectionService service) {
		
		final Runnable thread = new Runnable() {
			@Override
			public void run() {
				System.out.println("Thread created, and run function has run");
				qnet = connection.requestConnection(server);
				qnet.addIRCEventListener(service);

			}
		};
		threads.add(thread);
		handler.postDelayed(thread, 1000);

		
	}
	
	public void shutdown() {
		for(Runnable t : threads) {
			handler.removeCallbacks(t);
		}
		stopSelf();
	}

	@Override
	public void receiveEvent(IRCEvent e) {
		
		if (e.getType() == Type.CONNECT_COMPLETE) {
			e.getSession().join("#whg");
		} else if (e.getType() == Type.JOIN_COMPLETE) {
			JoinCompleteEvent jce = (JoinCompleteEvent) e;
			jce.getChannel().say("I just joined " + jce.getChannel().getName());
		} else {
			System.out.println(e.getType() + " : " + e.getRawEventData());
		}
	}

}
