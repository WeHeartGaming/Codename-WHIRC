/**
 * 
 */
package no.whg.whirc.fragments;

import java.util.ArrayList;
import java.util.Iterator;

import no.whg.whirc.R;
import no.whg.whirc.activities.MainActivity;
import no.whg.whirc.adapters.ConnectionListAdapter;
import no.whg.whirc.dialogs.ServerDialog;
import no.whg.whirc.helpers.WhircDB;
import no.whg.whirc.models.Server;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Fredrik
 * 
 */
public class ConnectionFragment extends Fragment {
	private ListView cList;
	private WhircDB db;
	private ArrayList<Server> servers;
	private ConnectionListAdapter cListAdapter;

	private static final int MENU_CONNECT = Menu.FIRST;
	private static final int MENU_DISCONNECT = Menu.FIRST + 1;
	private static final int MENU_EDIT = Menu.FIRST + 2;
	private static final int MENU_DELETE = Menu.FIRST + 3;

	public ConnectionFragment() {
		// this.cListAdapter = new ConnectionListAdapter(getActivity());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_connections,
				container, false);

		cList = (ListView) rootView.findViewById(R.id.lw_connections);
		servers = new ArrayList<Server>();

		return rootView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onContextItemSelected(android.view.MenuItem
	 * )
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		switch (item.getItemId()) {
		case MENU_CONNECT:
			// TODO: connect logic
			break;
		case MENU_DISCONNECT:
			// TODO: disconnect logic
			break;

		case MENU_EDIT:
			ServerDialog dialog = new ServerDialog(cListAdapter, false, db.getAllServers().get(info.position));
			dialog.show(getActivity().getSupportFragmentManager(), "whoa");
			break;

		case MENU_DELETE:

			break;

		}
		return super.onContextItemSelected(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateContextMenu(android.view.ContextMenu
	 * , android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.lw_connections) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.clearHeader();
			menu.add(0, MENU_CONNECT, Menu.NONE, R.string.context_connect);
			menu.add(0, MENU_DISCONNECT, Menu.NONE, R.string.context_disconnect);
			menu.add(0, MENU_EDIT, Menu.NONE, R.string.context_edit);
			menu.add(0, MENU_DELETE, Menu.NONE, R.string.context_remove);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		db = new WhircDB(getActivity());
		Iterator<Server> iterator = db.getAllServers().iterator();

		while (iterator.hasNext()) {
			servers.add(iterator.next());
		}
		cListAdapter = new ConnectionListAdapter(servers, getActivity());

		TextView addServer = (TextView) getActivity().findViewById(
				R.id.tv_addConnection);
		addServer.setText(R.string.addNetwork);
		addServer.setClickable(true);
		addServer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ServerDialog dialog = new ServerDialog(cListAdapter, true);
				dialog.show(getActivity().getSupportFragmentManager(), "WHOOOOA");
			}
		});
		cList.setAdapter(cListAdapter);
		registerForContextMenu(cList);

	}
}
