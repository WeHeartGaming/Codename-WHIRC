package no.whg.whirc.adapters;

import java.util.List;

import no.whg.whirc.models.Conversation;
import no.whg.whirc.models.Server;
import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class LeftMenuAdapter extends BaseExpandableListAdapter
{
	private List<Server> serverList;
	private List<List<Conversation>> conversationList; 
	Context context;

	public LeftMenuAdapter(List<Server> servers, List<List<Conversation>> conversations, Context c)
	{
		this.serverList = servers;
		this.conversationList = conversations;
		this.context = c;
	}
	
	@Override
	public Object getChild(int parentPos, int childPos)
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
	public View getChildView(int parentPos, int childPos, boolean isLastChild, View childView, ViewGroup parentView)
	{
		if(childView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
		}
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getChildrenCount(int parentPos) 
	{
		// TODO Auto-generated method stub
		return conversationList.get(parentPos).size();
	}
	@Override
	public Object getGroup(int parentPos)
	{
		// TODO Auto-generated method stub
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
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3)
	{
		// TODO Auto-generated method stub
		return null;
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

}
