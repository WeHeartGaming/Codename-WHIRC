/**
 * 
 */
package no.whg.whirc.fragments;

import java.util.ArrayList;
import java.util.Iterator;

import no.whg.whirc.R;
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
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case MENU_CONNECT:
			// TODO: connection logic
			break;
		case MENU_DISCONNECT:
			// TODO: disconnect logic
			break;

		case MENU_EDIT:
			
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
		Log.d("ConnectionFragment", "CREATED CONTEXTMENU OR SOMETHING");

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
				Log.d("ConnectionFragment", "TextView onclick rna");
//				LayoutInflater factory = LayoutInflater.from(getActivity());
//
//				// text_entry is an Layout XML file containing two text field to
//				// display in alert dialog
//				final View textEntryView = factory.inflate(
//						R.layout.dialog_connect, null);
//
//				final EditText serverName = (EditText) textEntryView
//						.findViewById(R.id.et_serverName);
//				final EditText host = (EditText) textEntryView
//						.findViewById(R.id.et_host);
//				final EditText port = (EditText) textEntryView
//						.findViewById(R.id.et_port);
//				final EditText nick = (EditText) textEntryView
//						.findViewById(R.id.et_nick);
//				final EditText nickTwo = (EditText) textEntryView
//						.findViewById(R.id.et_more_nick2);
//				final EditText nickThree = (EditText) textEntryView
//						.findViewById(R.id.et_more_nick3);
//				final EditText name = (EditText) textEntryView
//						.findViewById(R.id.et_more_name);
//				final TextView more = (TextView) textEntryView
//						.findViewById(R.id.tv_more);
//				final ImageView img = (ImageView) textEntryView
//						.findViewById(R.id.iv_more);
//				nickTwo.setVisibility(View.GONE);
//				nickThree.setVisibility(View.GONE);
//				name.setVisibility(View.GONE);
//				more.setClickable(true);
//				more.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						if (nickTwo.getVisibility() == View.GONE
//								&& nickThree.getVisibility() == View.GONE
//								&& name.getVisibility() == View.GONE) {
//							nickTwo.setVisibility(View.VISIBLE);
//							nickThree.setVisibility(View.VISIBLE);
//							name.setVisibility(View.VISIBLE);
//							textEntryView.invalidate();
//						} else {
//							nickTwo.setVisibility(View.GONE);
//							nickThree.setVisibility(View.GONE);
//							name.setVisibility(View.GONE);
//							textEntryView.invalidate();
//						}
//
//					}
//				});
//				img.setClickable(true);
//				img.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// lazy..
//						if (nickTwo.getVisibility() == View.GONE
//								&& nickThree.getVisibility() == View.GONE
//								&& name.getVisibility() == View.GONE) {
//							nickTwo.setVisibility(View.VISIBLE);
//							nickThree.setVisibility(View.VISIBLE);
//							name.setVisibility(View.VISIBLE);
//							textEntryView.invalidate();
//						} else {
//							nickTwo.setVisibility(View.GONE);
//							nickThree.setVisibility(View.GONE);
//							name.setVisibility(View.GONE);
//							textEntryView.invalidate();
//						}
//
//					}
//				});
//
//				final AlertDialog.Builder alert = new AlertDialog.Builder(
//						getActivity());
//				alert.setTitle(R.string.addNetwork)
//						.setView(textEntryView)
//						.setPositiveButton("OK",
//								new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog,
//											int whichButton) {
//
//										db.addServer(nick.getText().toString(),
//												nickTwo.getText().toString(),
//												nickThree.getText().toString(),
//												name.getText().toString(), host
//														.getText().toString(),
//												port.getText().toString(),
//												serverName.getText().toString());
//										cListAdapter.addServer(db
//												.getServer(serverName.getText()
//														.toString()));
//									}
//								})
//						.setNegativeButton("Cancel",
//								new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog,
//											int whichButton) {
//										dialog.cancel();
//									}
//								});
//				alert.show();
				ServerDialog dialog = new ServerDialog(cListAdapter, true);
				dialog.show(getActivity().getSupportFragmentManager(), "WHOOOOA");
			}
		});

		Log.d("ConnectionFragment", "Size: " + db.getAllServers().size()
				+ "\nRest: " + db.getAllServers().toString());

		cList.setAdapter(cListAdapter);

		// cList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position,
		// long id) {
		// Log.d("ConnectionFragment", "onItemClick pressed! [position=" +
		// position + "], [id=" + id + "]");
		// // TODO: connect logic here
		//
		// }
		// });

		// cList.setOnItemLongClickListener(new
		// AdapterView.OnItemLongClickListener() {
		//
		// @Override
		// public boolean onItemLongClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// Log.d("ConnectionFragment", "onItemLongClick pressed! [position=" +
		// position + "], [id=" + id + "]");
		// // TODO: contextmenu logic here
		// return false;
		// }
		//
		// });

		registerForContextMenu(cList);

	}
}
