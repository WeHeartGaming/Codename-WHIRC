package no.whg.whirc.activities;

import java.util.ArrayList;
import java.util.Locale;

import jerklib.ConnectionManager;
import no.whg.whirc.R;
import no.whg.whirc.adapters.MessageAdapter;
import no.whg.whirc.helpers.ConnectionService;
import no.whg.whirc.helpers.ConnectionServiceBinder;
import no.whg.whirc.models.Messages;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements ServiceConnection {
    private DrawerLayout mDrawerLayoutLeft;
    private DrawerLayout mDrawerLayoutRight;
    private ListView mDrawerListRight;
    private ListView mDrawerListLeft;
    private ActionBarDrawerToggle mDrawerToggleLeft;

    private CharSequence mDrawerTitleLeft;
    private CharSequence mTitle;
    
    private ConnectionManager manager;
    
    public ConnectionService cService;
    private boolean mBound = false;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        
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



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        
       cService = null;
       Intent intent = new Intent(this, ConnectionService.class);
       startService(intent);
    }

    
    
    /* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}



	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}



	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a SectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new SectionFragment();
            Bundle args = new Bundle();
            args.putInt(SectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A fragment representing current "section" in use by the app, which in this specific app will contain the currently selected IRC channel
     *
     */
    public static class SectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";
        ListView msgList;
        ArrayList<Messages> msgs;
        AdapterView.AdapterContextMenuInfo info;

        public SectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            return rootView;
        }

		/* (non-Javadoc)
		 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
		 */
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			
			Bundle args = getArguments();
			int position = args.getInt(ARG_SECTION_NUMBER);
			
			switch (position) {
			case 1:
				msgList = (ListView) getActivity().findViewById(R.id.lw_chat);
				msgs = new ArrayList<Messages>();
				Messages msg;
				
				for (int i = 0; i < 7; i++) {
					msg = new Messages();
					msg.setName("Fredrik"+i);
					msg.setMessage("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Message id: "+i);
					msg.setTime("22:0"+i);
					msgs.add(msg);
				}
				
				msgList.setAdapter(new MessageAdapter(msgs, getActivity()));
				break;
			case 2:
				
				break;
				
			case 3:
				
				break;
				
	
					
			}
		}
        
        
    }

	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		cService = ((ConnectionServiceBinder) binder).getService();
		
	}



	@Override
	public void onServiceDisconnected(ComponentName name) {
//		Intent intent = new Intent(this, ConnectionService.class);
//		
//		Bundle b = new Bundle();
//		b.putSerializable("object", cService);
//		b.putString("exit", "exit");
//		
//		intent.putExtras(b);
//		startService(intent);
		cService = null;
		
	}
}
