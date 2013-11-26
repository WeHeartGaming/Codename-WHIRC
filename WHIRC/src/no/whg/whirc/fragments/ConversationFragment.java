package no.whg.whirc.fragments;

import java.util.ArrayList;

import no.whg.whirc.R;
import no.whg.whirc.models.Conversation;
import no.whg.whirc.models.Message;
import android.annotation.SuppressLint;
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
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";
        ListView messageList;
        ArrayList<Message> msgs;
        AdapterView.AdapterContextMenuInfo info;
        
        private Conversation conversation;

        public ConversationFragment(Conversation c) {
        	this.conversation = c;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);
            
            messageList = conversation.getView();
            
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

            messageList = conversation.getView();
		}
    }