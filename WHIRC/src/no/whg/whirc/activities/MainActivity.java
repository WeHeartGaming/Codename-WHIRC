package no.whg.whirc.activities;

import java.util.concurrent.ExecutionException;

import jerklib.Channel;
import jerklib.ConnectionManager;
import jerklib.Session;
import jerklib.events.IRCEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.events.JoinCompleteEvent;
import jerklib.events.MessageEvent;
import jerklib.events.MotdEvent;
import jerklib.listeners.IRCEventListener;
import no.whg.whirc.R;
import no.whg.whirc.adapters.ConnectionPagerAdapter;
import no.whg.whirc.adapters.ConversationPagerAdapter;
import no.whg.whirc.fragments.ConversationFragment;
import no.whg.whirc.helpers.ConnectionService;
import no.whg.whirc.helpers.ConnectionServiceBinder;
import no.whg.whirc.helpers.ServerListDownload;
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
    
        try 
        {
			filePath = downloadServer.execute("http://www.mirc.com/servers.ini", null, "").get();
		}
        catch (InterruptedException e)
        {
			e.printStackTrace();
		}
        catch (ExecutionException e)
		{
			e.printStackTrace();
		}
        
        
        Log.d("ServerListDownload", filePath);
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
		//bindService(intent, this, Context.BIND_ABOVE_CLIENT);
		Log.d(TAG, "onCreate()");
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
	    Log.d(TAG, "onStart()");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Intent intent = new Intent(this, ConnectionService.class);
	    //bindService(intent, this, Context.BIND_ABOVE_CLIENT);
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
		Log.d(TAG, "onStop()");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//unbindService(this);
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
		cService = ((ConnectionServiceBinder) binder).getService();
		if (cService.getSessions().isEmpty()){
			//Session temp = cService.connect("irc.quakenet.org");
			//cService.getSessions().get(cService.getSessions().indexOf(temp)).addIRCEventListener(this);
			cService.connect("irc.quakenet.org", this);
		}
		Log.d(TAG, "onServiceConnected()");
		//cService.connect("irc.quakenet.org");

		for (Session s : cService.getSessions()){
			s.addIRCEventListener(this);
			if (!s.isConnected()){
				cService.connect(s.getServerInformation().getServerName(), this);
			}
		}
		

	    //mConversationPagerAdapter.removeFragments();
	    
	    Server s = cService.getCurrentServer();
	    if (s != null){
	    	if (s.getSession().isConnected()){
	    		generateFragments(s);
	    	} else {
	    		Log.d(TAG, "Not connected to current Server [" + s.getName() + "].");
	    	}
	    } else {
	    	Log.d(TAG, "s==null onServiceConnected()");
	    }

		// Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
//		if (s != null && s.isConnected()) {
//			mConversationPagerAdapter = new ConversationPagerAdapter(getSupportFragmentManager(), s);
//		} else {
//			Log.e("MainActivity", "ConversationPagerAdapter not started because no active connection at time of call");
//		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		for (Session s : cService.getSessions()){
			s.removeIRCEventListener(this);
		}
		cService = null;
		Log.d(TAG, "onServiceDisconnected()");
		
	}
	
	public ConnectionService getConnectionServiceObject() {
		return cService;
	}
	
	@Override
	public void receiveEvent(IRCEvent e) {
		if (e.getType() == Type.CONNECT_COMPLETE) {
			//mConversationPagerAdapter.removeFragments();
			cService.addServer(e.getSession());
		    Server s = cService.getCurrentServer();
		    if (s != null){
		    	generateFragments(s);
		    } else {
		    	Log.d(TAG, "s==null receiveEvent()");
		    }
		    
		    e.getSession().join("#whg");
		    Log.d(TAG, "Joined channel #whg.");
		} else if (e.getType() == Type.CHANNEL_MESSAGE) {
			MessageEvent me = (MessageEvent)e;
			Server s = cService.getServer(me.getSession());
			Conversation c = s.getConversation(me.getChannel().getName());
			c.addMessage(me);
//			runOnUiThread(new Runnable(){
//				public void run(){
//					mConversationPagerAdapter.notifyDataSetChanged();
//				}
//			});
			generateFragments(s);
			Log.d(TAG, "Added message to channel " + c.getChannelTitle() + ": " + me.getMessage());
			
		} else if (e.getType() == Type.MOTD) {
//			MotdEvent me = (MotdEvent)e;
//			Server s = cService.getServer(me.getSession());
//			Conversation c = s.getConversation(s.getName());
//			c.addMessage(me);
		} else if (e.getType() == Type.JOIN_COMPLETE){
			Session s = e.getSession();
			JoinCompleteEvent jce = (JoinCompleteEvent) e;
			Channel channel = jce.getChannel();
			Conversation conversation = null;
			//Server tempServ = cService.getServer(s.getServerInformation().getServerName());
			Server tempServ = cService.getCurrentServer();
			
			if (tempServ != null){
				conversation = tempServ.getConversation(channel.getName());
				if (conversation == null){
			    	conversation = new Conversation(channel);
			    	tempServ.addConversation(conversation);
			    	Log.d(TAG, "Created conversation " + s.getServerInformation().getServerName() + channel.getName() + ".");
			    	
					mConversationPagerAdapter.addFragment(new ConversationFragment(conversation, getApplicationContext()));
//					runOnUiThread(new Runnable(){
//						public void run(){
//							mConversationPagerAdapter.notifyDataSetChanged();
//						}
//					});
			    	Log.d(TAG, "Added " + s.getServerInformation().getServerName() + channel.getName() + " fragment.");
				} else {
			    	Log.d(TAG, "Conversation " + s.getServerInformation().getServerName() + channel.getName() + "already exists.");
				}
				generateFragments(tempServ);
			} else {
				Log.e(TAG, "This server does not exist");
			}
		} else {
			System.out.println(e.getType() + " : " + e.getRawEventData());
		}
	}
	
	private void generateFragments(Server s){
		if (s != null){

	        mConversationPagerAdapter = new ConversationPagerAdapter(getSupportFragmentManager());
			if (!s.getConversations().isEmpty()){
				for (Conversation c : s.getConversations()){
		        	Log.d(TAG, "Found Conversation " + c.getChannelTitle() + ", making a fragment.");
		        	mConversationPagerAdapter.addFragment(new ConversationFragment(c, getApplicationContext()));
				}
			} else {
				Log.d(TAG, "No conversations to add for Server.");
			}
			runOnUiThread(new Runnable(){
				public void run(){
					mConversationPagerAdapter.notifyDataSetChanged();
					mViewPager.setAdapter(mConversationPagerAdapter);
				}
			});
		} else {
			Log.e(TAG, "Server is null, cannot look for conversations.");
		}
	}
}
