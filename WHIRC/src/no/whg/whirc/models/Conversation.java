package no.whg.whirc.models;

import java.util.ArrayList;
import java.util.Date;

import jerklib.Channel;
import jerklib.events.MessageEvent;
import no.whg.whirc.activities.MainActivity;
import no.whg.whirc.adapters.MessageAdapter;
import android.text.format.Time;
import android.widget.ListView;


/**
 * @author Snorre
 *
 */
public class Conversation {
	private ArrayList<Message> messages;
	private Channel channel;
	private ListView messageView;
	private MainActivity activity;
	private MessageAdapter messageAdapter;
	private String channelID;
	private String channelTitle;
	
	public Conversation(Channel channel/*, String channelID*/){
		this.channel = channel;
		//this.channelID = channelID;
		this.channelTitle = channel.getName();
		messages = new ArrayList<Message>();
		
		if (!channel.getTopic().equals("")){
			Message topic = new Message(channel.getTopicSetter(), channel.getTopic(), channel.getTopicSetTime().toString());
			messages.add(topic);
		}
		//messageAdapter = new MessageAdapter(messages, activity);

		//messageView = (ListView) activity.findViewById(R.id.lw_chat);
		//messageView.setAdapter(messageAdapter);
	}
	
	
	//public String getChannelID(){
	//	return channelID;
	//}
	
	public ArrayList<Message> getMessages(){
		return messages;
	}
	
	public void addMessage(Message m){
		messages.add(m);
	}
	
	public void addMessage(MessageEvent me){
		addMessage(new Message(me.getNick(), me.getMessage(), (new Date().toString())));
	}
	
	public void addMessageAdapter(MessageAdapter ma){
		messageAdapter = ma;
	}
	
	public ListView getView(){
		return messageView;
	}
	
	public String getChannelTitle(){
		return channelTitle;
	}
}
