/**
 * 
 */
package no.whg.whirc.fragments;

import java.util.ArrayList;

import no.whg.whirc.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
		addServer.setText("cliclckckcicliclc");
		addServer.setClickable(true);
		addServer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("ConnectionFragment", "TextView onclick rna");
				LayoutInflater factory = LayoutInflater.from(getActivity());

				//text_entry is an Layout XML file containing two text field to display in alert dialog
				final View textEntryView = factory.inflate(R.layout.dialog_connect, null);

				final EditText input1 = (EditText) textEntryView.findViewById(R.id.et_host);
				final EditText input2 = (EditText) textEntryView.findViewById(R.id.et_port);
				final EditText input3 = (EditText) textEntryView.findViewById(R.id.et_nick);


//				input1.setText("DefaultValue", TextView.BufferType.EDITABLE);
//				input2.setText("DefaultValue", TextView.BufferType.EDITABLE);
//				input3.setText("DefaultValue", TextView.BufferType.EDITABLE);

				final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				alert.setTitle(R.string.addNetwork).setView(textEntryView).setPositiveButton("OK",
				  new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog,
				     int whichButton) {

				    Log.i("AlertDialog","TextEntry 1 Entered "+input1.getText().toString());
				    Log.i("AlertDialog","TextEntry 2 Entered "+input2.getText().toString());
				    Log.i("AlertDialog","TextEntry 3 Entered "+input3.getText().toString());
				   }
				  }).setNegativeButton("Cancel",
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
