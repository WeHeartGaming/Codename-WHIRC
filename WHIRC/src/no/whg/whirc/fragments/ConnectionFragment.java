/**
 * 
 */
package no.whg.whirc.fragments;

import java.util.ArrayList;

import no.whg.whirc.R;
import no.whg.whirc.models.db.Server;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Fredrik
 *
 */
public class ConnectionFragment extends Fragment {
	private ListView cList;
	
	public ConnectionFragment() {
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_connections, container, false);
        
        return rootView;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		TextView addServer = (TextView) getActivity().findViewById(R.id.tv_addConnection);
		addServer.setText(R.string.joinNetwork);
		addServer.setClickable(true);
		addServer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				alert.setView(getActivity().findViewById(R.layout.dialog_connect));
				alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO: add servern til db osv
					}
				});
				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						
					}
				});
			}
		});
		
		cList = (ListView) getActivity().findViewById(R.id.lw_connections);
		ArrayList<Server> servers = new ArrayList<Server>();
		
//		msgs = new ArrayList<Messages>();
//		Messages msg;
//		
//		for (int i = 0; i < 7; i++) {
//			msg = new Messages();
//			msg.setName("Fredrik"+i);
//			msg.setMessage("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Message id: "+i);
//			msg.setTime("22:0"+i);
//			msgs.add(msg);
//		}
//		
//		msgList.setAdapter(new MessageAdapter(msgs, getActivity()));
		
	}
	
	

}
