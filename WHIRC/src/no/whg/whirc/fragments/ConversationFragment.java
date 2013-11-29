package no.whg.whirc.fragments;

import java.util.ArrayList;

import jerklib.Channel;
import no.whg.whirc.R;
import no.whg.whirc.adapters.MessageAdapter;
import no.whg.whirc.models.Conversation;
import no.whg.whirc.models.Message;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;



    /**
     * A fragment representing current "section" in use by the app, which in this specific app will contain the currently selected IRC channel
     *
     */
    @SuppressLint("ValidFragment")
	public class ConversationFragment extends Fragment {
    	private static final String TAG = "ConversationFragment";
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";
        ListView messageList;
        ArrayList<Message> msgs;
        AdapterView.AdapterContextMenuInfo info;
        private MessageAdapter messageAdapter;
        private ListView messageView;
        private ImageView sendButton;
        private EditText textBox;
        
        private Conversation conversation;
		/**
		 * 
		 * @param conversation
		 * @param c
		 */
        public ConversationFragment(Conversation conversation, Context c) {
        	this.conversation = conversation;
            this.messageAdapter = new MessageAdapter(conversation.getMessages(), c);
        	//this.conversation.addMessageAdapter(messageAdapter);
        }
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);                    

    		messageView = (ListView) rootView.findViewById(R.id.lw_chat);
    		messageView.setAdapter(messageAdapter);
    		
    		sendButton = (ImageView) rootView.findViewById(R.id.ib_send);
    		textBox = (EditText) rootView.findViewById(R.id.et_send);
    		
    		sendButton.setOnClickListener(
    		        new View.OnClickListener()
    		        {
    		            public void onClick(View view)
    		            {
    		                channelSend(textBox.getText().toString());
    		                textBox.setText("");
    		                
    		            }
    		        });
            
            return rootView;
        }

		/* (non-Javadoc)
		 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
		 */
		@Override
		/**
		 * @param savedInstanceState
		 */
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			
			Bundle args = getArguments();
			//int position = args.getInt(ARG_SECTION_NUMBER);

		}
		/**
		 * 
		 * @return
		 */
		public String getName() {
			return conversation.getChannelTitle();
		}
		/**
		 * 
		 * @param string
		 */
		public void channelSend(String string){
			if (!conversation.isServ()){
				if (string.startsWith("/mode ")){
					if (string.startsWith(conversation.getChannelTitle() + " +o " , 6)) {
						string = string.replace("/mode " + conversation.getChannelTitle() + " +o ", "");
						channelOp(string, conversation.getChannel());
					} else if (string.startsWith(conversation.getChannelTitle() + " -o " , 6)) {
						string = string.replace("/mode " + conversation.getChannelTitle() + " -o ", "");
						channelDeop(string, conversation.getChannel());
					} else if (string.startsWith(conversation.getChannelTitle() + " +v " , 6)) {
						string = string.replace("/mode " + conversation.getChannelTitle() + " +v ", "");
						channelVoice(string, conversation.getChannel());
					} else if (string.startsWith(conversation.getChannelTitle() + " -v " , 6)) {
						string = string.replace("/mode " + conversation.getChannelTitle() + " -v ", "");
						channelDevoice(string, conversation.getChannel());
					}
				} else if (string.startsWith("/kick " + conversation.getChannelTitle() + " ")){
					string = string.replace("/kick " + conversation.getChannelTitle() + " ", "");
					channelKick(string, "", conversation.getChannel());
				} else if (string.startsWith("/topic " + conversation.getChannelTitle() + " ")){
					string = string.replace("/topic " + conversation.getChannelTitle() + " ", "");
					channelSetTopic(string, conversation.getChannel());
				} else if (string.startsWith("/me ")){
					string = string.replace("/me ", "");
					channelAction(string, conversation.getChannel());
				} else if (string.equals("/part")){
					channelPart(conversation.getChannel());
				} else if (string.startsWith("/join ")){
					string = string.replace("/join ", "");
					conversation.getChannel().getSession().join(string);
				} else {
					channelSay(string, conversation.getChannel());
				}
			} else if (conversation.getPriv()){
				if (string.startsWith("/me ")){
					string = string.replace("/me ", "");
					channelAction(string, conversation.getChannel());
				} else if (string.startsWith("/")){
					string = string.replace("/", "./");
					channelSay(string, conversation.getChannel());
				} else {
					channelSay(string, conversation.getChannel());
				}
			} else {
				if (string.startsWith("/join ")){
					string = string.replace("/join ", "");
					conversation.getSession().join(string);
				}
			}
		}
		
		/**
		 * 
		 * @param action
		 * @param channel
		 */
		public void channelAction(String action, Channel channel){
			channel.action(action);
		}
		/**
		 * 
		 * @param name
		 * @param channel
		 */
		public void channelDeop(String name, Channel channel){
			channel.deop(name);
			conversation.addMessage(new Message(conversation.getChannelTitle(), "TEMP DEOP STRING " + name, conversation.getTime(), 0));
		}
		/**
		 * 
		 * @param name
		 * @param channel
		 */
		public void channelDevoice(String name, Channel channel){
			channel.deVoice(name);
			conversation.addMessage(new Message(conversation.getChannelTitle(), "TEMP DEVOICE STRING " + name, conversation.getTime(), 0));
		}
		/**
		 * 
		 * @param name
		 * @param reason
		 * @param channel
		 */
		public void channelKick(String name, String reason, Channel channel){
			channel.kick(name, reason);
			conversation.addMessage(new Message(conversation.getChannelTitle(), "TEMP KICK STRING " + name, conversation.getTime(), 0));
		}
		/**
		 * 
		 * @param mode
		 * @param channel
		 */
		public void channelMode(String mode, Channel channel){
			channel.mode(mode);
		}
		/**
		 * 
		 * @param name
		 * @param channel
		 */
		public void channelOp(String name, Channel channel){
			conversation.addMessage(new Message(conversation.getChannelTitle(), "TEMP OP STRING " + name, conversation.getTime(), 0));
			channel.op(name);
		}
		/**
		 * 
		 * @param channel
		 */
		public void channelPart(Channel channel){
			channel.part("");
		}
		/**
		 * 
		 * @param say
		 * @param channel
		 */
		public void channelSay(String say, Channel channel){
			channel.say(say);
			conversation.addMessage(new Message(channel.getSession().getNick(), say, conversation.getTime(), say.hashCode()));
		}
		/**
		 * 
		 * @param topic
		 * @param channel
		 */
		public void channelSetTopic(String topic, Channel channel){
			channel.setTopic(topic);
			conversation.addMessage(new Message(conversation.getChannelTitle(), "TEMP TOPIC STRING " + topic, conversation.getTime(), 0));
		}
		/**
		 * 
		 * @param name
		 * @param channel
		 */
		public void channelVoice(String name, Channel channel){
			channel.voice(name);
			conversation.addMessage(new Message(conversation.getChannelTitle(), "TEMP VOICE STRING " + name, conversation.getTime(), 0));
		}
    }