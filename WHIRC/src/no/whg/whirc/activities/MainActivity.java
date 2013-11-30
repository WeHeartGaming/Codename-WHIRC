package no.whg.whirc.activities;

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
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import no.whg.whirc.R;
import no.whg.whirc.adapters.LeftMenuAdapter;
import no.whg.whirc.dialogs.WhoDialog;
import no.whg.whirc.helpers.ConnectionService;
import no.whg.whirc.helpers.ConnectionServiceBinder;
import no.whg.whirc.helpers.ServerListDownload;
import no.whg.whirc.helpers.WhircDB;
import no.whg.whirc.models.Conversation;
import no.whg.whirc.models.Server;

public class MainActivity extends FragmentActivity implements ServiceConnection {
	private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayoutLeft;
    private DrawerLayout mDrawerLayoutRight;
    private ListView mDrawerListRight;
    private ListView mDrawerListLeft;
    private ActionBarDrawerToggle mDrawerToggleLeft;
    private String filePath;
    private Conversation convers;
    
    private CharSequence mDrawerTitleLeft;
    private CharSequence mTitle;

    private WhircDB server;
    
    private ServerListDownload downloadServer;
    public ConnectionService cService;
    private boolean mBound = false;
    
    private LeftMenuAdapter elwAdapter;
    private ExpandableListView elw;
    
    private ArrayList<Server> servers;
    
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


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        convers.getContext(this.getApplicationContext());
        
        
        servers = new ArrayList<Server>();
        downloadServer = new ServerListDownload(this.getApplicationContext());
        server = new WhircDB(this.getApplicationContext());
        int l = server.getSize();
        
        
        if(server.getSize() == 0)
        {
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
        }   
        
        // Set placeholder title
        mTitle = mDrawerTitleLeft = getTitle();
        mDrawerLayoutLeft = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayoutRight = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mDrawerListLeft = (ListView) findViewById(R.id.left_drawer);
        elw = (ExpandableListView) findViewById(R.id.left_drawer);
        mDrawerListRight = (ListView) findViewById(R.id.right_drawer);

        // set shadow to overlay main content when we pull out drawer
        mDrawerLayoutLeft.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayoutRight.setDrawerShadow(R.drawable.drawer_shadow_right, GravityCompat.END);
        // set up the drawers  list view with items and click listener
        mDrawerListLeft.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, new String[]{"lol 1", "lol 2", "lol 3", "lol 4"}));
        mDrawerListLeft.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerListRight.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, new String[]{"Empty"}));
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
        //mViewPager.setAdapter(mConnectionPagerAdapter);

	    
		cService = null;
		Intent intent = new Intent(this, ConnectionService.class);
		startService(intent);
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
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		unbindService(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //boolean drawerOpen = mDrawerLayoutLeft.isDrawerOpen(elw);
        // Since we are (probably) going to move the settings button to the navigation drawer while it's open,
        //  lets hide the settings button when we open the drawer
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
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
                return super.onOptionsItemSelected(item);
            case R.id.action_servers:
            	return super.onOptionsItemSelected(item);
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
    /**
     * sets action bar title
     * @param title
     */
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

    /**fires when the accompanying service has been bound to the app, and takes over event responses
     * @param name
     * @param binder
     */
	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		Log.d(TAG, "onServiceConnected()");
		cService = ((ConnectionServiceBinder) binder).getService();

	}
	/**fires when service is disconnected, shuts down all event responses
	 * @param name
	 */
	@Override
	public void onServiceDisconnected(ComponentName name) {
		cService = null;
		
	}
	/**
	 * gets teh connectionservice object
	 * @return the connectionservice object
	 */
	public ConnectionService getConnectionServiceObject() {
		return cService;
	}

	/**
	 * opens a dialog for WHO, WHOIS and WHOWAS information
	 * @param channels
	 * @param nick
	 * @param name
	 * @param server
	 * @param serverInfo
	 * @param signedOn
	 * @param idle
	 * @param away
	 */
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
	
	public void setrightadapter(Conversation conversation){
		//runOnUiThread(new Runnable(){
			//public void run(){
				mDrawerListRight.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, conversation.getUserList()));
			//}
		//});
	}
}
