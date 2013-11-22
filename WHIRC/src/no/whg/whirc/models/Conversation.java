package no.whg.whirc.models;

import java.util.ArrayList;

import jerklib.Channel;
import no.whg.whirc.R;
import no.whg.whirc.activities.MainActivity;
import no.whg.whirc.adapters.MessageAdapter;
import android.widget.ListView;


/**
 * @author Snorre
 *
 */
public class Conversation {
	ArrayList<Message> messages;
	Channel channel;
	String channelID;
	MainActivity activity;
	ListView messageView;
	MessageAdapter messageAdapter;
	
	public Conversation(Channel channel, String channelID){
		this.channel = channel;
		this.channelID = channelID;
		
		messages = new ArrayList<Message>();
		
		Message topic = new Message(channel.getTopicSetter(), channel.getTopic(), channel.getTopicSetTime().toString());
		messages.add(topic);
		
		messageAdapter = new MessageAdapter(messages, activity);

		messageView = (ListView) activity.findViewById(R.id.lw_chat);
		messageView.setAdapter(messageAdapter);
	}
	
	
	public String getChannelID(){
		return channelID;
	}
	
	public ArrayList<Message> getMessages(){
		return messages;
	}
	
	public void addMessage(Message m){
		messages.add(m);
		messageAdapter.notifyDataSetChanged();
	}
}
