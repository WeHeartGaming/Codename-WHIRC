package no.whg.whirc.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import no.whg.whirc.R;
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
import android.content.Context;
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
    private static Context context;
    private boolean isServer = false;
    private boolean isPriv = false;
    /**
     * 
     * @param channel
     */
    public Conversation(Channel channel){
        this.channel = channel;
        this.channelTitle = channel.getName();
        this.messages = new ArrayList<Message>();
        Message topic = new Message("none", "no topic", "never", 0);
        this.messages.add(topic);
        this.ops = new ArrayList<String>();
        this.voices = new ArrayList<String>();
        this.users = new ArrayList<String>();
        this.userList = new ArrayList<String>();
        makeUserList();

    }
    /**
     * 
     * @param channel
     * @param priv
     */
    public Conversation(Channel channel, String priv){
        this.channel = channel;
        this.channelTitle = priv;
        this.isPriv = true;
        this.users = new ArrayList<String>();
        this.userList = new ArrayList<String>();
        this.userList.add(priv);
        messages = new ArrayList<Message>();
    }
    /**
     * 
     * @param servername
     */
    public Conversation(String servername){
        this.channelTitle = servername;

        messages = new ArrayList<Message>();
        this.isServer = true;
    }
    /**
     * 
     * @param c
     */
    public static void getContext(Context c)
    {
    	context = c;
    }
    /**
     * 
     */
    public void makeUserList(){
    	if(!isPriv){
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
    }
    /**
     * 
     */
    private void updateUserList(){
    	this.userList = new ArrayList<String>();
    	if(!isPriv){
	    	String opSymbol = "@ ";
	    	String voiceSymbol = "+ ";
		  	for (String op : ops){
		  		userList.add(opSymbol.concat(op));
		  	}
		  	for (String voice : voices){
		  		userList.add(voiceSymbol.concat(voice));
		  	}
    	}
	  	for (String user : users){
	  		userList.add(user);
	  	}
    }
    /**
     * 
     * @param user
     * @param mode
     */
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
    /**
     * 
     * @param user
     */
    public void addUser(String user){
    	users.add(user);
    	updateUserList();
    }
    /**
     * 
     * @param user
     */
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
    /**
     * 
     * @param user
     * @param mode
     */
    public void changeMode(String user, String mode){
    	removeUser(user);
    	addUser(user, mode);
    }
    /**
     * 
     * @param user
     * @return
     */
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
    
    public boolean getPriv(){
    	return isPriv;
    }
    
    public void changePrivNick(String user){
    	userList.remove(0);
    	userList.add(user);
    	this.channelTitle = user;
    }
    /**
     * 
     * @param user
     * @return
     */
    public boolean hasUser(String user){
    	if (!isServer){
    		if (!isPriv){
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
    		} else {
    			for (String s : userList){
    				if (s.equals(user)){
    					return true;
    				}
    			}
    		}
    	}
    	return false;
    }
    /**
     * 
     * @return
     */
    public ArrayList<String> getUserList(){
    	return this.userList;
    }
    /**
     * 
     * @return
     */
    public ArrayList<Message> getMessages(){
        return messages;
    }
    /**
     * 
     * @param m
     */
    public void addMessage(Message m){
        messages.add(m);
    }
    /**
     * 
     * @param me
     */
    public void addMessage(MessageEvent me){
        addMessage(new Message(me.getNick(), me.getMessage(), getTime(), me.hashCode()));
    }
    /**
     * 
     * @param me
     */
    public void addMessage(MotdEvent me){
        addMessage(new Message(me.getHostName(), me.getMotdLine(), getTime(), me.hashCode()));
    }
    /**
     * 
     * @param te
     */
    public void addMessage(TopicEvent te){
        Message topic = new Message(getChannelTitle(), te.getTopic() + context.getString(R.string.setBy) + te.getSetBy() + context.getString(R.string.on) + te.getSetWhen().toString(), getTime(), 0);
        messages.set(0, topic);
    }
    /**
     * 
     * @param je
     */
    public void addMessage(JoinEvent je){
        addMessage(new Message(getChannelTitle(), context.getString(R.string.user) + je.getNick() + context.getString(R.string.userJoinChannel) +" (" + je.getUserName() + " " + je.getHostName() + ").", getTime(), je.hashCode()));
    }
    /**
     * 
     * @param pe
     */
    public void addMessage(PartEvent pe){
        addMessage(new Message(getChannelTitle(), context.getString(R.string.user) + pe.getWho() + context.getString(R.string.userLeftChannel)+ " (" + pe.getUserName() + " " + pe.getHostName() + "):\n" + pe.getPartMessage(), getTime(), pe.hashCode()));
    }
    /**
     * 
     * @param ke
     */
    public void addMessage(KickEvent ke){
        addMessage(new Message(getChannelTitle(), context.getString(R.string.user) + ke.getWho() + context.getString(R.string.userKicked) + " (" + ke.getUserName() + " " + ke.getHostName() + "):\n" + ke.getMessage(), getTime(), ke.hashCode()));
    }
    /**
     * 
     * @param ae
     */
    public void addMessage(AwayEvent ae){
        if (ae.isYou()){
            if (ae.isAway()){
                addMessage(new Message(ae.getSession().getConnectedHostName(), context.getString(R.string.awayStatus), getTime(), ae.hashCode()));
            } else {
                addMessage(new Message(ae.getSession().getConnectedHostName(), context.getString(R.string.noLongerAway), getTime(), ae.hashCode()));
            }
        } else {
            if (ae.isAway()){
                addMessage(new Message(ae.getSession().getConnectedHostName(), ae.getNick() + context.getString(R.string.othersAwayStatus) + ae.getAwayMessage() + ").", getTime(), ae.hashCode()));
            } else {
                addMessage(new Message(ae.getSession().getConnectedHostName(), ae.getNick() + context.getString(R.string.othersNoLongerAway), getTime(), ae.hashCode()));
            }
        }
    }
    /**
     * 
     * @param me
     */
    public void addMessage(ModeEvent me){
        addMessage(new Message(me.setBy(), context.getString(R.string.modeSet) + me.getModeAdjustments().toString(), getTime(), me.hashCode()));
	}
	/**
	 * 
	 * @param ce
	 * @param ctcp
	 */
	public void addMessage(CtcpEvent ce, String ctcp){
		addMessage(new Message("", ce.getNick() + " " + ctcp, getTime(), ce.hashCode()));
    }
	/**
	 * 
	 * @param sve
	 */
	public void addMessage(ServerVersionEvent sve){
		addMessage(new Message(sve.getHostName(), sve.getComment(), getTime(), sve.hashCode()));
	}
	/**
	 * 
	 * @param qe
	 */
	public void addMessage(QuitEvent qe){
		addMessage(new Message(getChannelTitle(), context.getString(R.string.user) + qe.getNick() + context.getString(R.string.userQuit) + " (" + qe.getUserName() + "@" + qe.getHostName() + "):\n" + qe.getQuitMessage(), getTime(), qe.hashCode()));
	}
	/**
	 * 
	 * @param ne
	 */
	public void addMessage(NoticeEvent ne){
		addMessage(new Message(ne.byWho(), ne.getNoticeMessage(), getTime(), ne.hashCode()));
	}
	/**
	 * 
	 * @param niue
	 * @param newNick
	 */
	public void addMessage(NickInUseEvent niue, String newNick){
		addMessage(new Message(niue.getSession().getConnectedHostName(), context.getString(R.string.yourNick) + niue.getInUseNick() + context.getString(R.string.isInvalid) + newNick + ".", getTime(), niue.hashCode()));
	}
	/**
	 * 
	 * @param nce
	 */
	public void addMessage(NickChangeEvent nce){
		addMessage(new Message(getChannelTitle(), context.getString(R.string.user) + nce.getOldNick() + context.getString(R.string.changedNick) + nce.getNewNick(), getTime(), nce.hashCode()));
	}
	/**
	 * 
	 * @return
	 */
    public String getChannelTitle(){
        return channelTitle;
    }
    /**
     * 
     * @param hash
     * @return
     */
    public boolean hasMessage(int hash){
        for (Message m : messages){
            if (m.getHashcode() == hash){
                return true;
            }
        }

        return false;
    }
	/**
	 * 
	 * @return
	 */
    public String getTime() {
        Calendar cal = Calendar.getInstance();

        return cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
    }
    /**
     * 
     * @return
     */
    public Channel getChannel(){
    	return this.channel;
    }
    
    public void setChannel(Channel channel){
    	this.channel = channel;
    }
}
