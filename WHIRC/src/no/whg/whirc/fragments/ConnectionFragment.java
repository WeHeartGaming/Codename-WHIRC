/**
 * 
 */
package no.whg.whirc.fragments;

import java.util.ArrayList;
import java.util.Iterator;

import no.whg.whirc.R;
import no.whg.whirc.adapters.ConnectionListAdapter;
import no.whg.whirc.helpers.WhircDB;
import no.whg.whirc.models.Server;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

	public ConnectionFragment() {
		//this.cListAdapter = new ConnectionListAdapter(getActivity());

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
		cListAdapter = new ConnectionListAdapter(servers, getActivity());
		cList.setAdapter(cListAdapter);
		
		
		servers = new ArrayList<Server>();
		
		db = new WhircDB(getActivity());
		
		Log.d("ConnectionFragment", "Size: " + db.getAllServers().size() + "\nRest: " + db.getAllServers().toString());
		
		Iterator<Server> iterator = db.getAllServers().iterator();
		
		while (iterator.hasNext()) {
			servers.add(iterator.next());
		}
		

		return rootView;
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

		TextView addServer = (TextView) getActivity().findViewById(
				R.id.tv_addConnection);
		addServer.setText(R.string.addNetwork);
		addServer.setClickable(true);
		addServer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("ConnectionFragment", "TextView onclick rna");
				LayoutInflater factory = LayoutInflater.from(getActivity());

				// text_entry is an Layout XML file containing two text field to
				// display in alert dialog
				final View textEntryView = factory.inflate(
						R.layout.dialog_connect, null);

				final EditText serverName = (EditText) textEntryView
						.findViewById(R.id.et_serverName);
				final EditText host = (EditText) textEntryView
						.findViewById(R.id.et_host);
				final EditText port = (EditText) textEntryView
						.findViewById(R.id.et_port);
				final EditText nick = (EditText) textEntryView
						.findViewById(R.id.et_nick);
				final EditText nickTwo = (EditText) textEntryView
						.findViewById(R.id.et_more_nick2);
				final EditText nickThree = (EditText) textEntryView
						.findViewById(R.id.et_more_nick3);
				final EditText name = (EditText) textEntryView
						.findViewById(R.id.et_more_name);
				final TextView more = (TextView) textEntryView
						.findViewById(R.id.tv_more);
				final ImageView img = (ImageView) textEntryView
						.findViewById(R.id.iv_more);
				nickTwo.setVisibility(View.GONE);
				nickThree.setVisibility(View.GONE);
				name.setVisibility(View.GONE);
				more.setClickable(true);
				more.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (nickTwo.getVisibility() == View.GONE
								&& nickThree.getVisibility() == View.GONE
								&& name.getVisibility() == View.GONE) {
							nickTwo.setVisibility(View.VISIBLE);
							nickThree.setVisibility(View.VISIBLE);
							name.setVisibility(View.VISIBLE);
							textEntryView.invalidate();
						} else {
							nickTwo.setVisibility(View.GONE);
							nickThree.setVisibility(View.GONE);
							name.setVisibility(View.GONE);
							textEntryView.invalidate();
						}

					}
				});
				img.setClickable(true);
				img.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// lazy..
						if (nickTwo.getVisibility() == View.GONE
								&& nickThree.getVisibility() == View.GONE
								&& name.getVisibility() == View.GONE) {
							nickTwo.setVisibility(View.VISIBLE);
							nickThree.setVisibility(View.VISIBLE);
							name.setVisibility(View.VISIBLE);
							textEntryView.invalidate();
						} else {
							nickTwo.setVisibility(View.GONE);
							nickThree.setVisibility(View.GONE);
							name.setVisibility(View.GONE);
							textEntryView.invalidate();
						}

					}
				});

				final AlertDialog.Builder alert = new AlertDialog.Builder(
						getActivity());
				alert.setTitle(R.string.addNetwork)
						.setView(textEntryView)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {

										Log.i("AlertDialog", "Server name: "
												+ serverName.getText()
														.toString());
										Log.i("AlertDialog", "Host: "
												+ host.getText().toString());
										Log.i("AlertDialog", "Port: "
												+ port.getText().toString());
										Log.i("AlertDialog", "Nick: "
												+ nick.getText().toString());
										Log.i("AlertDialog", "NickTwo: "
												+ nickTwo.getText().toString());
										Log.i("AlertDialog", "NickThree: "
												+ nickThree.getText()
														.toString());
										Log.i("AlertDialog", "Name: "
												+ name.getText().toString());
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.cancel();
									}
								});
				alert.show();
			}
		});

		cList = (ListView) getActivity().findViewById(R.id.lw_connections);
		servers = new ArrayList<Server>();
		
		db = new WhircDB(getActivity());
		
		Log.d("ConnectionFragment", "Size: " + db.getAllServers().size() + "\nRest: " + db.getAllServers().toString());
		
		Iterator<Server> iterator = db.getAllServers().iterator();
		
		while (iterator.hasNext()) {
			servers.add(iterator.next());
		}
		cListAdapter = new ConnectionListAdapter(servers, getActivity());
		cList.setAdapter(cListAdapter);
		

		// msgs = new ArrayList<Messages>();
		// Messages msg;
		//
		// for (int i = 0; i < 7; i++) {
		// msg = new Messages();
		// msg.setName("Fredrik"+i);
		// msg.setMessage("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Message id: "+i);
		// msg.setTime("22:0"+i);
		// msgs.add(msg);
		// }
		//
		// msgList.setAdapter(new MessageAdapter(msgs, getActivity()));

	}
}
