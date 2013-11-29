package no.whg.whirc.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jerklib.Channel;
import jerklib.events.AwayEvent;
import jerklib.events.CtcpEvent;
import jerklib.events.JoinEvent;
import jerklib.events.KickEvent;
import jerklib.events.MessageEvent;
import jerklib.events.MotdEvent;
import jerklib.events.NickChangeEvent;
import jerklib.events.NickInUseEvent;
import jerklib.events.NoticeEvent;
import jerklib.events.PartEvent;
import jerklib.events.QuitEvent;
import jerklib.events.ServerVersionEvent;
import jerklib.events.TopicEvent;
import jerklib.events.modes.ModeAdjustment;
import jerklib.events.modes.ModeEvent;
import android.util.Log;


/**
 * @author Snorre
 *
 */
public class Conversation {
	private static final String TAG = "Conversation";
    private ArrayList<Message> messages;
    private Channel channel;
    private String channelTitle;
    private ArrayList<String> userList;
    private List<String> voices;
    private List<String> ops;
    private List<String> users;
    //private char oSymbol = 'o';

    public Conversation(Channel channel){
        this.channel = channel;
        this.channelTitle = channel.getName();
        this.messages = new ArrayList<Message>();
        Message topic = new Message("none", "no topic", "never", 0);
        this.messages.add(topic);
        //this.users = channel.getNicks();
        //this.users = new List<String>();

    }

    public Conversation(Channel channel, String priv){
        this.channel = channel;
        this.channelTitle = priv;
        this.userList = new ArrayList<String>();
        this.userList.add(priv);
        messages = new ArrayList<Message>();
    }

    public Conversation(String servername){
        this.channelTitle = servername;

        messages = new ArrayList<Message>();
    }
    
    public void makeUserList(){
    	this.ops = channel.getNicksForMode(ModeAdjustment.Action.PLUS, 'o');
    	this.voices = channel.getNicksForMode(ModeAdjustment.Action.PLUS, 'v');
    	this.users = new ArrayList<String>();
    	
    	for (String s : channel.getNicks()){
		  	if (!ops.contains(s) && !voices.contains(s)){
		  		users.add(s);
		  	}
	  	}
	  	
    	updateUserList();
    }
    
    private void updateUserList(){
    	this.userList = new ArrayList<String>();
    	String opSymbol = "@ ";
    	String voiceSymbol = "+ ";
	  	
	  	for (String op : ops){
	  		userList.add(opSymbol.concat(op));
	  	}
	  	for (String voice : voices){
	  		userList.add(voiceSymbol.concat(voice));
	  	}
	  	for (String user : users){
	  		userList.add(user);
	  	}
	  	Log.d(TAG, userList.toString());
    }
    
    public void addUser(String user, String mode){
		String opSymbol = "@ ";
    	String voiceSymbol = "+ ";
    	if (mode.equals(opSymbol)){
    		ops.add(opSymbol.concat(user));
    	} else if (mode.equals(voiceSymbol)){
    		voices.add(voiceSymbol.concat(user));
    	} else {
    		users.add(user);
    	}
    	updateUserList();
    }
    
    public void addUser(String user){
    	users.add(user);
    	updateUserList();
    }
    
    public void removeUser(String user){
    	char temp = fetchMode(user);
    	switch (temp){
    	case 'o':
    		ops.remove(user);
    		break;
    	case 'v':
    		voices.remove(user);
    		break;
    	case 'u':
    		users.remove(user);
    		break;
    		}
    	updateUserList();
    }
    
    public void changeMode(String user, String mode){
    	removeUser(user);
    	addUser(user, mode);
    }
    
    private char fetchMode(String user){
    	for (String op : ops){
    		if (op.equals(user)){
    			return 'o';
    		}
    	}
    	for (String voice : voices){
    		if (voice.equals(user)){
    			return 'v';
    		}
    	}
    	return 'u';
    }
    
    public boolean hasUser(String user){
    	for (String op : ops){
    		if (op.equals(user)){
    			return true;
    		}
    	}
    	for (String voice : voices){
    		if (voice.equals(user)){
    			return true;
    		}
    	}
    	for (String u : users){
    		if (u.equals(user)){
    			return true;
    		}
    	}
    	return false;
    }
    
    public ArrayList<String> getUserList(){
    	return this.userList;
    }

    public ArrayList<Message> getMessages(){
        return messages;
    }

    public void addMessage(Message m){
        messages.add(m);
    }

    public void addMessage(MessageEvent me){
        addMessage(new Message(me.getNick(), me.getMessage(), getTime(), me.hashCode()));
    }

    public void addMessage(MotdEvent me){
        addMessage(new Message(me.getHostName(), me.getMotdLine(), getTime(), me.hashCode()));
    }

    public void addMessage(TopicEvent te){
        Message topic = new Message(getChannelTitle(), te.getTopic() + "\nSet by " + te.getSetBy() + " on " + te.getSetWhen().toString(), getTime(), 0);
        messages.set(0, topic);
    }

    public void addMessage(JoinEvent je){
        addMessage(new Message(getChannelTitle(), "User " + je.getNick() + " joined the channel (" + je.getUserName() + " " + je.getHostName() + ").", getTime(), je.hashCode()));
    }

    public void addMessage(PartEvent pe){
        addMessage(new Message(getChannelTitle(), "User " + pe.getWho() + " left the channel (" + pe.getUserName() + " " + pe.getHostName() + "):\n" + pe.getPartMessage(), getTime(), pe.hashCode()));
    }

    public void addMessage(KickEvent ke){
        addMessage(new Message(getChannelTitle(), "User " + ke.getWho() + " was kicked from channel (" + ke.getUserName() + " " + ke.getHostName() + "):\n" + ke.getMessage(), getTime(), ke.hashCode()));
    }

    public void addMessage(AwayEvent ae){
        if (ae.isYou()){
            if (ae.isAway()){
                addMessage(new Message(ae.getSession().getConnectedHostName(), "You are now listed as AWAY.", getTime(), ae.hashCode()));
            } else {
                addMessage(new Message(ae.getSession().getConnectedHostName(), "You are no longer listed as AWAY.", getTime(), ae.hashCode()));
            }
        } else {
            if (ae.isAway()){
                addMessage(new Message(ae.getSession().getConnectedHostName(), ae.getNick() + " is now listed as AWAY (" + ae.getAwayMessage() + ").", getTime(), ae.hashCode()));
            } else {
                addMessage(new Message(ae.getSession().getConnectedHostName(), ae.getNick() + " is no longer listed as AWAY.", getTime(), ae.hashCode()));
            }
        }
    }

    public void addMessage(ModeEvent me){
        addMessage(new Message(me.setBy(), "Mode set to " + me.getModeAdjustments().toString(), getTime(), me.hashCode()));
	}
	
	public void addMessage(CtcpEvent ce, String ctcp){
		addMessage(new Message("", ce.getNick() + " " + ctcp, getTime(), ce.hashCode()));
    }
	
	public void addMessage(ServerVersionEvent sve){
		addMessage(new Message(sve.getHostName(), sve.getComment(), getTime(), sve.hashCode()));
	}
	
	public void addMessage(QuitEvent qe){
		addMessage(new Message(getChannelTitle(), "User " + qe.getNick() + " has quit IRC (" + qe.getUserName() + "@" + qe.getHostName() + "):\n" + qe.getQuitMessage(), getTime(), qe.hashCode()));
	}
	
	public void addMessage(NoticeEvent ne){
		addMessage(new Message(ne.byWho(), ne.getNoticeMessage(), getTime(), ne.hashCode()));
	}
	
	public void addMessage(NickInUseEvent niue, String newNick){
		addMessage(new Message(niue.getSession().getConnectedHostName(), "Your nickname " + niue.getInUseNick() + " is invalid. Changing to " + newNick + ".", getTime(), niue.hashCode()));
	}
	
	public void addMessage(NickChangeEvent nce){
		addMessage(new Message(getChannelTitle(), "User " + nce.getOldNick() + " has changed nick to " + nce.getNewNick(), getTime(), nce.hashCode()));
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

    public String getTime() {
        Calendar cal = Calendar.getInstance();

        return cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
    }
    
    public Channel getChannel(){
    	return this.channel;
    }
}
