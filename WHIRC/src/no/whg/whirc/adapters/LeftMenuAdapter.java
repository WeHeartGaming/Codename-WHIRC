package no.whg.whirc.adapters;

import java.util.ArrayList;

import no.whg.whirc.models.Server;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LeftMenuAdapter extends BaseAdapter
{
	private ArrayList<Server> data;
	Context context;

	public LeftMenuAdapter(ArrayList<Server> servers, Context c)
	{
		this.data = servers;
		this.context = c;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
