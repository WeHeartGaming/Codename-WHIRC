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
 *
 */
public class ConnectionListAdapter extends BaseAdapter {
	private ArrayList<Server> data;
	Context c;
	
	/**
	 * @param data
	 * @param c
	 */
	public ConnectionListAdapter(ArrayList<Server> data, Context c) {
		super();
		this.data = data;
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

		View v = convertView;
		
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_item_connections, null);
		}
		
		TextView name = (TextView) v.findViewById(R.id.tv_connectionName);
		TextView status = (TextView) v.findViewById(R.id.tv_connectionStatus);
		
		Server server = data.get(position);
		
		//name.setText(server.getHost());
		status.setText("TODO");
		
		
		return v;
	}
}
