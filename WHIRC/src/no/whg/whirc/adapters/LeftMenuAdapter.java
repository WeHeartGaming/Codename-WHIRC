package no.whg.whirc.adapters;

import java.util.ArrayList;
import java.util.List;

import no.whg.whirc.R;
import no.whg.whirc.models.Conversation;
import no.whg.whirc.models.Server;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class LeftMenuAdapter extends BaseExpandableListAdapter
{
	private ArrayList<Server> serverList;
	private List<ArrayList<Conversation>> conversationList; 
	Context context;

	/**
	 * Constructor
	 * 
	 * @param servers ArrayList containing all servers
	 * @param c Application context
	 */
	public LeftMenuAdapter(ArrayList<Server> servers, Context c) {
		this.serverList = servers;
		this.context = c;
		
		conversationList = new ArrayList<ArrayList<Conversation>>();
		
		if (serverList.size() != 0) {
			for (Server server : serverList) {
				conversationList.add(server.getConversations());
			}
		}
	}
	
	@Override
	public Conversation getChild(int parentPos, int childPos)
	{
		// TODO Auto-generated method stub
		return conversationList.get(parentPos).get(childPos);
	}
	@Override
	public long getChildId(int parentPos, int childPos) 
	{
		// TODO Auto-generated method stub
		return childPos;
	}
	
	@Override
	public View getChildView(int parentPos, int childPos, boolean isLastChild, View childView, ViewGroup parentView) {
		if(childView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			childView = inflater.inflate(R.layout.list_item_left, parentView, false);
		}
		
		TextView child = (TextView) childView.findViewById(R.id.groupChild);
		child.setText(getChild(parentPos, childPos).getChannelTitle());
		
		return childView;
	}
	
	@Override
	public int getChildrenCount(int parentPos) 
	{
		// TODO Auto-generated method stub
		return conversationList.get(parentPos).size();
	}
	@Override
	public Server getGroup(int parentPos)
	{
		return serverList.get(parentPos);
	}
	@Override
	public int getGroupCount()
	{
		// TODO Auto-generated method stub
		return serverList.size();
	}
	@Override
	public long getGroupId(int parentPos) 
	{
		// TODO Auto-generated method stub
		return parentPos;
	}
	
	@Override
	public View getGroupView(int groupPos, boolean isExpanded, View groupView, ViewGroup parent) {
		if (groupView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			groupView = inflater.inflate(R.layout.list_group_left, parent, false);
		}
		
		TextView group = (TextView) groupView.findViewById(R.id.tv_more);
		group.setText(getGroup(groupPos).getSimpleName());
		
		return groupView;
	}
	
	@Override
	public boolean hasStableIds() 
	{
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isChildSelectable(int parentPos, int childPos) 
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public void addServer(Server s) {
		serverList.add(s);
		conversationList.add(s.getConversations());
	}

}
