package no.whg.whirc.models;

import java.util.ArrayList;
import java.util.Date;

import jerklib.Channel;
import jerklib.events.MessageEvent;
import jerklib.events.MotdEvent;
import jerklib.events.TopicEvent;
import no.whg.whirc.activities.MainActivity;
import no.whg.whirc.adapters.MessageAdapter;
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
	private String channelTitle;
	
	public Conversation(Channel channel){
		this.channel = channel;
		this.channelTitle = channel.getName();
		messages = new ArrayList<Message>();
		Message topic = new Message("none", "no topic", "never", 0);
		messages.add(topic);
//		if (!channel.getTopic().equals("")){
//			Message topic = new Message(channel.getTopicSetter(), channel.getTopic(), channel.getTopicSetTime().toString());
//			messages.add(topic);
//		}
	}
	
	public Conversation(String servername){
		this.channelTitle = servername;
		
		messages = new ArrayList<Message>();
	}
	
	public ArrayList<Message> getMessages(){
		return messages;
	}
	
	public void addMessage(Message m){
		messages.add(m);
	}
	
	public void addMessage(MessageEvent me){
		addMessage(new Message(me.getNick(), me.getMessage(), (new Date().toString()), me.hashCode()));
	}

	public void addMessage(MotdEvent me){
		addMessage(new Message(me.getHostName(), me.getMotdLine(), (new Date().toString()), me.hashCode()));
	}
	
	public void addTopic(TopicEvent te){
		Message topic = new Message(te.getSetBy(), te.getTopic(), te.getSetWhen().toString(), 0);
		messages.set(0, topic);
	}
	
	public String getChannelTitle(){
		return channelTitle;
	}
	
	public boolean hasMessage(int hash){
		for (Message m : messages){
			if (m.getHashcode() == hash){
				return true;
			}
		}
		
		return false;
	}
}
