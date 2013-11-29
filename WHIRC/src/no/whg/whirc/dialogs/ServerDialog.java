/**
 * 
 */
package no.whg.whirc.dialogs;

import java.util.ArrayList;

import no.whg.whirc.R;
import no.whg.whirc.adapters.ConnectionListAdapter;
import no.whg.whirc.helpers.WhircDB;
import no.whg.whirc.models.Server;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Fredrik
 * 
 */
@SuppressLint("ValidFragment")
public class ServerDialog extends DialogFragment {
	private View textEntryView;
	private EditText serverName;
	private EditText host;
	private EditText port;
	private EditText nick;
	private EditText nickTwo;
	private EditText nickThree;
	private EditText name;
	private TextView more;
	private ImageView img;
	private AlertDialog.Builder builder;
	private ConnectionListAdapter cListAdapter;
	private WhircDB db;
	private boolean isNew;
	private Server server;

	public ServerDialog() {

	}

	/**
     * 
     */
	public ServerDialog(ConnectionListAdapter cListAdapter, boolean isNew) {
		this.cListAdapter = cListAdapter;
		this.db = new WhircDB(getActivity());
		this.isNew = isNew;

	}
	
	/**
     * 
     */
	public ServerDialog(ConnectionListAdapter cListAdapter, boolean isNew, Server s) {
		this.cListAdapter = cListAdapter;
		this.db = new WhircDB(getActivity());
		this.isNew = isNew;
		this.server = s;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater factory = LayoutInflater.from(getActivity());

		// text_entry is an Layout XML file containing two text field to
		// display in alert dialog
		textEntryView = factory.inflate(R.layout.dialog_connect, null);

		serverName = (EditText) textEntryView.findViewById(R.id.et_serverName);
		host = (EditText) textEntryView.findViewById(R.id.et_host);
		port = (EditText) textEntryView.findViewById(R.id.et_port);
		nick = (EditText) textEntryView.findViewById(R.id.et_nick);
		nickTwo = (EditText) textEntryView.findViewById(R.id.et_more_nick2);
		nickThree = (EditText) textEntryView.findViewById(R.id.et_more_nick3);
		name = (EditText) textEntryView.findViewById(R.id.et_more_name);
		more = (TextView) textEntryView.findViewById(R.id.tv_more);
		img = (ImageView) textEntryView.findViewById(R.id.iv_more);
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

		builder = new AlertDialog.Builder(getActivity());
		if (isNew) {
			builder.setTitle(R.string.addNetwork);
		} else {
			builder.setTitle(R.string.editNetwork);
		}
		if (!isNew) {
			
			Log.d("ServerDialog", server.getNickOne());
			nick.setText(server.getNickOne());
			nickTwo.setText(server.getNickTwo());
			nickThree.setText(server.getNickThree());
			name.setText(server.getName());
			host.setText(server.getHost());
			port.setText(server.getPort());
			serverName.setText(server.getSimpleName());
		}
		builder.setView(textEntryView);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				if (isNew) {
					Log.d("ServerDialog", "isNew is true");
					db.addServer(nick.getText().toString(), nickTwo.getText()
							.toString(), nickThree.getText().toString(), name
							.getText().toString(), host.getText().toString(),
							port.getText().toString(), serverName.getText()
									.toString());
					cListAdapter.addServer(db.getServer(serverName.getText()
							.toString()));
				} else {
					Log.d("ServerDialog", "equals: " + nickTwo.getText().toString().equals(""));
					Log.d("ServerDialog", "isNew is false");
					
					Server s = new Server(nick.getText().toString(), nickTwo.getText()
							.toString(), nickThree.getText().toString(), name
							.getText().toString(), host.getText().toString(),
							port.getText().toString(), serverName.getText()
									.toString());
					Log.i("ServerDialog", s.toString());
					db.updateServerUser(s);
					cListAdapter.refreshArray((ArrayList<Server>) db.getAllServers());
					
				}
			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});

		return builder.create();
	}

}