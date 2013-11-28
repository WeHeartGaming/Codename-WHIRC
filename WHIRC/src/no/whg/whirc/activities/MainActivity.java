package no.whg.whirc.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jerklib.Channel;
import jerklib.ConnectionManager;
import jerklib.Session;
import jerklib.events.AwayEvent;
import jerklib.events.ChannelListEvent;
import jerklib.events.CtcpEvent;
import jerklib.events.IRCEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.events.InviteEvent;
import jerklib.events.JoinCompleteEvent;
import jerklib.events.JoinEvent;
import jerklib.events.KickEvent;
import jerklib.events.MessageEvent;
import jerklib.events.MotdEvent;
import jerklib.events.NickChangeEvent;
import jerklib.events.NickInUseEvent;
import jerklib.events.NickListEvent;
import jerklib.events.NoticeEvent;
import jerklib.events.PartEvent;
import jerklib.events.QuitEvent;
import jerklib.events.ServerVersionEvent;
import jerklib.events.TopicEvent;
import jerklib.events.WhoEvent;
import jerklib.events.WhoisEvent;
import jerklib.events.WhowasEvent;
import jerklib.events.modes.ModeEvent;
import jerklib.listeners.IRCEventListener;
import no.whg.whirc.R;
import no.whg.whirc.adapters.ConnectionPagerAdapter;
import no.whg.whirc.adapters.ConversationPagerAdapter;
import no.whg.whirc.dialogs.InviteDialog;
import no.whg.whirc.dialogs.WhoDialog;
import no.whg.whirc.fragments.ConversationFragment;
import no.whg.whirc.helpers.ConnectionService;
import no.whg.whirc.helpers.ConnectionServiceBinder;
import no.whg.whirc.helpers.ServerListDownload;
import no.whg.whirc.helpers.WhircDB;
import no.whg.whirc.models.Conversation;
import no.whg.whirc.models.Server;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements ServiceConnection, IRCEventListener {
	private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayoutLeft;
    private DrawerLayout mDrawerLayoutRight;
    private ListView mDrawerListRight;
    private ListView mDrawerListLeft;
    private ActionBarDrawerToggle mDrawerToggleLeft;
    private String filePath;
    
    private CharSequence mDrawerTitleLeft;
    private CharSequence mTitle;
    
    private ConnectionManager manager;
    private WhircDB server;
    
    private ServerListDownload downloadServer;
    public ConnectionService cService;
    private boolean mBound = false;
    
    //private Session s = null;
    //private ArrayList <Session> sessions;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    ConversationPagerAdapter mConversationPagerAdapter;
    ConnectionPagerAdapter mConnectionPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        downloadServer = new ServerListDownload(this.getApplicationContext());
        server = new WhircDB(this.getApplicationContext());
        int l = server.getSize();
        
        String t = "" + l;
        Log.d("ServerListDownload", t );
        if(server.getSize() == 0)
        {
	        try 
	        {
				filePath = downloadServer.execute("http://www.mirc.com/servers.ini", null, "").get();
				Log.d("ServerListDownload", filePath);
			}
	        catch (InterruptedException e)
	        {
				e.printStackTrace();
			}
	        catch (ExecutionException e)
			{
				e.printStackTrace();
			}
        }
        
        
        
        // Set placeholder title
        mTitle = mDrawerTitleLeft = getTitle();
        mDrawerLayoutLeft = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayoutRight = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListLeft = (ListView) findViewById(R.id.left_drawer);
        mDrawerListRight = (ListView) findViewById(R.id.right_drawer);

        // set shadow to overlay main content when we pull out drawer
        mDrawerLayoutLeft.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayoutRight.setDrawerShadow(R.drawable.drawer_shadow_right, GravityCompat.END);
        // set up the drawers  list view with items and click listener
        mDrawerListLeft.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, new String[]{"List 1", "List 2", "List 3", "List 4"}));
        mDrawerListLeft.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerListRight.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, new String[]{"lol 1", "lol 2", "lol 3", "lol 4"}));
        mDrawerListRight.setOnItemClickListener(new DrawerItemClickListener());

        // enable actionbar up button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // actionbardrawertoggle ties together the proper interactions between the sliding drawer and the action bar app icon
        mDrawerToggleLeft = new ActionBarDrawerToggle(
                this,
                mDrawerLayoutLeft,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayoutLeft.setDrawerListener(mDrawerToggleLeft);

        if (savedInstanceState == null) {
            selectItem(0);
        }


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        //mConversationPagerAdapter = new ConversationPagerAdapter(getSupportFragmentManager());
        mConnectionPagerAdapter = new ConnectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mConnectionPagerAdapter);
	    
		cService = null;
		Intent intent = new Intent(this, ConnectionService.class);
		startService(intent);
		Log.d(TAG, "onCreate() has been run.");
    }


	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Intent intent = new Intent(this, ConnectionService.class);
	    bindService(intent, this, Context.BIND_ABOVE_CLIENT);
	    Log.d(TAG, "onStart(): Added IRCEventListeners.");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume()");
	}
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		unbindService(this);
		for (Session s : cService.getSessions()){
			s.removeIRCEventListener(this);
		}
		Log.d(TAG, "onStop(): Decoupled IRCEventListeners.");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause()");
	}



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayoutLeft.isDrawerOpen(mDrawerListLeft);
        // Since we are (probably) going to move the settings button to the navigation drawer while it's open,
        //  lets hide the settings button when we open the drawer
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggleLeft.onOptionsItemSelected(item)) {
            return true;
        }
        // tell program what to do when you press a certain actionbar button, e.g. settings button
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        /* TODO Selection of menu item logic goes here, since we use sections we probably need an array/list of fragments
          for every network, while when we select e.g. "options" we need to start a new activity. */
        // FragmentManager, beginTransaction, setItemChecked, setTitle, mDrawerLayout.closeDrawer etc goes here.
        // Bundle with args, ARG_FRAGMENT_NUMBER etc etc
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggleLeft.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggleLeft.onConfigurationChanged(newConfig);
    }


	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		Log.d(TAG, "onServiceConnected()");
		cService = ((ConnectionServiceBinder) binder).getService();
		if (cService.getSessions().isEmpty()){
			//Session temp = cService.connect("irc.quakenet.org");
			//cService.getSessions().get(cService.getSessions().indexOf(temp)).addIRCEventListener(this);
			//cService.connect("irc.quakenet.org", this);
			Log.d(TAG, "onServiceConnected(): Forced a connection to quakenet for debug purposes.");
		}
		
		for (Session s : cService.getSessions()){
			s.addIRCEventListener(this);
			if (!s.isConnected()){
				cService.connect(s.getServerInformation().getServerName(), this);
			}
		}
	    
	    Server s = cService.getCurrentServer();
	    if (s != null){
	    	if (s.getSession().isConnected()){
	    		generateFragments(s);
	    	} else {
	    		Log.d(TAG, "Not connected to current Server [" + s.getName() + "].");
	    	}
	    } else {
	    	Log.d(TAG, "onServiceConnected(): There is no current Server.");
	    }
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		for (Session s : cService.getSessions()){
			s.removeIRCEventListener(this);
		}
		cService = null;
		Log.e(TAG, "onServiceDisconnected(): Unexpected UnBind. Decoupled IRCEventListeners. This is not supposed to happen, you know.");
		
	}
	
	public ConnectionService getConnectionServiceObject() {
		return cService;
	}
	
	@Override
	public void receiveEvent(IRCEvent e) {
		if (e.getType() == Type.CONNECT_COMPLETE) {
			cService.addServer(e.getSession());
		    Server server = cService.getCurrentServer();
		    if (server != null){
		    	if (server == cService.getCurrentServer()){
					generateFragments(server);
				}
		    } else {
		    	Log.e(TAG, "receiveEvent() CONNECT_COMPLETE: There is no server object. If this shows up, we're fucked.");
		    }
		    
		    e.getSession().join("#whg");
		    Log.e(TAG, "receiveEvent() CONNECT_COMPLETE: Forced a join on channel #whg for debugging purposes.");
		} else if (e.getType() == Type.TOPIC) {
			TopicEvent te = (TopicEvent)e;
			Server server = cService.getServer(te.getSession());
			Conversation conversation = server.getConversation(te.getChannel().getName());
			conversation.addMessage(te);
			if (server == cService.getCurrentServer()){
				generateFragments(server);
			}
		} else if (e.getType() == Type.CHANNEL_MESSAGE) {
			MessageEvent me = (MessageEvent)e;
			Server server = cService.getServer(me.getSession());
			Conversation conversation = server.getConversation(me.getChannel().getName());
			if (!conversation.hasMessage(me.hashCode())){
				conversation.addMessage(me);
			} else {
				Log.e(TAG, "receiveEvent() CHANNEL_MESSAGE: Message already exists, did not add it to Conversation.");
			}
			if (server == cService.getCurrentServer()){
				generateFragments(server);
			}
		} else if (e.getType() == Type.JOIN){
			JoinEvent je = (JoinEvent)e;
			Server server = cService.getServer(je.getSession());
			Conversation conversation = server.getConversation(je.getChannel().getName());
			if (!conversation.hasMessage(je.hashCode())){
				conversation.addMessage(je);
			} else {
				Log.e(TAG, "receiveEvent() JOIN: JOIN Message already exists, did not add it to Conversation.");
			}
			if (server == cService.getCurrentServer()){
				generateFragments(server);
			}
		} else if (e.getType() == Type.PART){
			PartEvent pe = (PartEvent)e;
			Server server = cService.getServer(pe.getSession());
			Conversation conversation = server.getConversation(pe.getChannel().getName());
			if (!conversation.hasMessage(pe.hashCode())){
				conversation.addMessage(pe);
			} else {
				Log.e(TAG, "receiveEvent() PART: PART Message already exists, did not add it to Conversation.");
			}
			if (server == cService.getCurrentServer()){
				generateFragments(server);
			}
		} else if (e.getType() == Type.KICK_EVENT){
			KickEvent ke = (KickEvent)e;
			Server server = cService.getServer(ke.getSession());
			Conversation conversation = server.getConversation(ke.getChannel().getName());
			if (!conversation.hasMessage(ke.hashCode())){
				conversation.addMessage(ke);
			} else {
				Log.e(TAG, "receiveEvent() KICK: KICK Message already exists, did not add it to Conversation.");
			}
			if (server == cService.getCurrentServer()){
				generateFragments(server);
			}
		} else if (e.getType() == Type.MODE_EVENT){
			ModeEvent me = (ModeEvent)e;
			Server server = cService.getServer(me.getSession());
			Conversation conversation;
			if (me.getChannel() == null){
				conversation = server.getConversation(0); // 0 is always the position of the server conversation. 
			} else {
				conversation = server.getConversation(me.getChannel().getName());
			}
			if (!conversation.hasMessage(me.hashCode())){
				conversation.addMessage(me);
			} else {
				Log.e(TAG, "receiveEvent() MODE_EVENT: MODE_EVENT Message already exists, did not add it to Conversation.");
			}
			if (server == cService.getCurrentServer()){
				generateFragments(server);
			}
		} else if (e.getType() == Type.AWAY_EVENT){
			AwayEvent ae = (AwayEvent)e;
			Server server = cService.getServer(ae.getSession());
			Conversation conversation = server.getConversation(0); // 0 is always the position of the server conversation. 
			if (!conversation.hasMessage(ae.hashCode())){
				conversation.addMessage(ae);
			} else {
				Log.e(TAG, "receiveEvent() AWAY_EVENT: AWAY_EVENT Message already exists, did not add it to Conversation.");
			}
			if (server == cService.getCurrentServer()){
				generateFragments(server);
			}
		} else if (e.getType() == Type.PRIVATE_MESSAGE){
			MessageEvent me = (MessageEvent)e;
			Server server = cService.getServer(me.getSession());
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
			if (server == cService.getCurrentServer()){
				generateFragments(server);
			}
		} else if (e.getType() == Type.MOTD) {
			MotdEvent me = (MotdEvent)e;
			Server server = cService.getServer(me.getSession());
			Conversation conversation = server.getConversation(0); // 0 is always the position of the server conversation. 
			if (!conversation.hasMessage(me.hashCode())){
				conversation.addMessage(me);
			} else {
				Log.e(TAG, "receiveEvent() MOTD: Message already exists, did not add it to Conversation.");
			}
			if (server == cService.getCurrentServer()){
				generateFragments(server);
			}
		} else if (e.getType() == Type.JOIN_COMPLETE){
			Session session = e.getSession();
			JoinCompleteEvent jce = (JoinCompleteEvent) e;
			Channel channel = jce.getChannel();
			Conversation conversation = null;
			Server server = cService.getServer(session.getRequestedConnection().getHostName());
			if (server != null){
				conversation = server.getConversation(channel.getName());
				if (conversation == null){
			    	conversation = new Conversation(channel);
			    	server.addConversation(conversation);
				} else {
			    	Log.e(TAG, "receiveEvent() JOIN_COMPLETE: Conversation " + session.getRequestedConnection().getHostName() + channel.getName() + "already exists.");
				}
				if (server == cService.getCurrentServer()){
					generateFragments(server);
				}
			} else {
				Log.e(TAG, "receiveEvent() JOIN_COMPLETE: This server does not exist.");
			}
		} else if (e.getType() == Type.CTCP_EVENT){
			CtcpEvent ce = (CtcpEvent)e;
			String cm = ce.getCtcpString();
			String[] ctcp = cm.split(" ", 2);
			if(ctcp[0].equals("ACTION")){
				Server server = cService.getServer(ce.getSession());
				Conversation conversation = server.getConversation(ce.getChannel().getName());
				if (!conversation.hasMessage(ce.hashCode())){
					conversation.addMessage(ce, ctcp[1]);
				} else {
					Log.e(TAG, "receiveEvent() CTCP_EVENT: CTCP ACTION already exists, did not add it to Conversation.");
				}
				if (server == cService.getCurrentServer()){
					generateFragments(server);
				}
			}
		} else if (e.getType() == Type.SERVER_VERSION_EVENT){
			ServerVersionEvent sve = (ServerVersionEvent)e;
			Server server = cService.getServer(sve.getSession());
			Conversation conversation = server.getConversation(0); // 0 is always the position of the server conversation. 
			if (!conversation.hasMessage(sve.hashCode())){
				conversation.addMessage(sve);
			} else {
				Log.e(TAG, "receiveEvent() SERVER_VERSION_EVENT: Message already exists, did not add it to Conversation.");
			}
			if (server == cService.getCurrentServer()){
				generateFragments(server);
			}
		} else if (e.getType() == Type.CHANNEL_LIST_EVENT){
			ChannelListEvent cle = (ChannelListEvent)e;
			System.out.println(e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.NICK_CHANGE){
			NickChangeEvent nce = (NickChangeEvent)e;
			System.out.println(e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.NICK_IN_USE){
			NickInUseEvent niue = (NickInUseEvent)e;
			System.out.println(e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.NICK_LIST_EVENT){
			NickListEvent nle = (NickListEvent)e;
			System.out.println(e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.NOTICE){
			NoticeEvent ne = (NoticeEvent)e;
			System.out.println(e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.QUIT){
			QuitEvent qe = (QuitEvent)e;
			Server server = cService.getServer(qe.getSession());
			List<Channel> channels = qe.getChannelList();
			//Conversation conversation = server.getConversation(qe.getChannel().getName());
			ArrayList<Conversation> conversations = server.getMatchingConversations(channels);
			if (conversations != null){
				for (Conversation conversation : conversations){
					if (!conversation.hasMessage(qe.hashCode())){
						conversation.addMessage(qe);
					} else {
						Log.e(TAG, "receiveEvent() QUIT: QUIT Message already exists, did not add it to Conversation.");
					}
				}
				if (server == cService.getCurrentServer()){
					generateFragments(server);
				}
			}
		} else if (e.getType() == Type.WHO_EVENT){
			WhoEvent we = (WhoEvent)e;
			System.out.println(e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.WHOIS_EVENT){
			WhoisEvent we = (WhoisEvent)e;
			System.out.println(e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.WHOWAS_EVENT){
			WhowasEvent we = (WhowasEvent)e;
			System.out.println(e.getType() + " : " + e.getRawEventData());
		}
			

		// This should work, but jerklib dies if we try inviting the client somewhere
		else if (e.getType() == Type.INVITE_EVENT){
			InviteEvent ie = (InviteEvent)e;
			Log.d(TAG, "receiveEvent() INVITE_EVENT: Calling INVITE_EVENT dialog for channel " + ie.getChannelName() + ": " + ie.getRawEventData());
			inviteDialog(ie.getChannelName(), ie.getNick());
		}	
		// Everything under here is being ignored.
		else if (e.getType() == Type.UPDATE_HOST_NAME){
			// We don't need this
			System.out.println(e.getType() + " : " + e.getRawEventData());
		} else if (e.getType() == Type.SERVER_INFORMATION){
			// Fuck this
		} else if (e.getType() == Type.DCC_EVENT){
			// We don't need this
		} else if (e.getType() == Type.ERROR){
			// We don't need this
		} else if (e.getType() == Type.EXCEPTION){
			// We don't need this
			// TODO: I don't even pretend to know
		} else if (e.getType() == Type.DEFAULT){
			// TODO: Nothing to do, really.
		} else {
			// This is just for show
		}
	}
	
	private void inviteDialog(final String channel, final String nick){
		runOnUiThread(new Runnable(){
			public void run(){
				InviteDialog invite = new InviteDialog(nick, channel);
				invite.show(getSupportFragmentManager(), "invite_dialog");
			}
		});
	}
	
	private void whoDialog(final String[] channels, final String nick, final String name,
			final String server, final String serverInfo, final String signedOn,
			final boolean idle, final boolean away) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				WhoDialog who = new WhoDialog(channels, nick, name, server, serverInfo, signedOn, idle, away);
				who.show(getSupportFragmentManager(), "who_dialog");
			}
		});
	}
	
	private void generateFragments(Server s){
		if (s != null){
	        mConversationPagerAdapter = new ConversationPagerAdapter(getSupportFragmentManager());
			if (!s.getConversations().isEmpty()){
				for (Conversation c : s.getConversations()){
		        	Log.d(TAG, "generateFragments(): Found Conversation " + c.getChannelTitle() + ", making a fragment.");
		        	mConversationPagerAdapter.addFragment(new ConversationFragment(c, getApplicationContext()));
				}
			} else {
				Log.d(TAG, "generateFragments(): No conversations to add for Server.");
			}
			runOnUiThread(new Runnable(){
				public void run(){
					mConversationPagerAdapter.notifyDataSetChanged();
					mViewPager.setAdapter(mConversationPagerAdapter);
					Log.d(TAG, "generateFragments(): Forced an update to the adapter.");
				}
			});
		} else {
			Log.e(TAG, "generateFragments(): Server is null, cannot look for conversations.");
		}
	}
}
