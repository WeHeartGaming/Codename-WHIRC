/**
 * 
 */
package no.whg.whirc.adapters;

import java.util.ArrayList;

import no.whg.whirc.R;
import no.whg.whirc.models.Server;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Fredrik
 * Data adapter, takes in an array, and updates the UI whenever the array adds or removes an item
 */
public class ConnectionListAdapter extends BaseAdapter {
	private ArrayList<Server> data;
	Context c;
	
	/**
	 * @param servers
	 * @param c
	 */
	public ConnectionListAdapter(ArrayList<Server> servers, Context c) {
		super();
		this.data = servers;
		this.c = c;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// set the current view object to draw on
		View v = convertView;
		
		if (v == null) {
			// inflate (draw) the view, with list_item_connection.xml as base
			LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_item_connections, null);
		}
		
		// Point variables to textview objects created through xml
		TextView name = (TextView) v.findViewById(R.id.tv_connectionName);
		TextView status = (TextView) v.findViewById(R.id.tv_connectionStatus);
		
		Server server = data.get(position);
		
		name.setText(server.getSimpleName());
		
		if (server.getSession() == null) {
			status.setText(R.string.disconnected);
		} else {
			if (server.getSession().isConnected()) {
				status.setText(R.string.connected);
			}
		}
		
		// return the inflated view
		return v;
	}
	
	/**
	 * 
	 * @param s a server
	 */
	public void addServer(Server s) {
		data.add(s);
		notifyDataSetChanged(); // this tells the UI to update itself (sometimes?)
	}
	
	/**
	 * 
	 * @param position position in array
	 */
	public void removeServer(int position) {
		data.remove(position);
		notifyDataSetChanged(); 
	}
	
	/**
	 * refreshes the array by clearing everything and then adding in new elements
	 * @param servers new servers array
	 */
	public void refreshArray(ArrayList<Server> servers) {
		data.clear();
		for (Server s : servers) {
			data.add(s);
		}
		notifyDataSetChanged();
	}
}
