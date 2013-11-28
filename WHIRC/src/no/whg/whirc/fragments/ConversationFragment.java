package no.whg.whirc.fragments;

import java.util.ArrayList;

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
        
        private Conversation conversation;

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
            
            return rootView;
        }

		/* (non-Javadoc)
		 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
		 */
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			
			Bundle args = getArguments();
			//int position = args.getInt(ARG_SECTION_NUMBER);

		}
		
		public String getName() {
			return conversation.getChannelTitle();
		}
    }