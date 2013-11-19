package no.whg.whirc.fragments;

import java.util.ArrayList;

import no.whg.whirc.R;
import no.whg.whirc.models.Messages;
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
    public class ConversationFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";
        ListView msgList;
        ArrayList<Messages> msgs;
        AdapterView.AdapterContextMenuInfo info;

        public ConversationFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);
            
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
			int position = args.getInt(ARG_SECTION_NUMBER);
			
//			switch (position) {
//			case 1:
//				msgList = (ListView) getActivity().findViewById(R.id.lw_chat);
//				msgs = new ArrayList<Messages>();
//				Messages msg;
//				
//				for (int i = 0; i < 7; i++) {
//					msg = new Messages();
//					msg.setName("Fredrik"+i);
//					msg.setMessage("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Message id: "+i);
//					msg.setTime("22:0"+i);
//					msgs.add(msg);
//				}
//				
//				msgList.setAdapter(new MessageAdapter(msgs, getActivity()));
//				break;
//			case 2:
//				
//				break;
//				
//			case 3:
//				
//				break;
//				
//	
//					
//			}
		}
        
        
    }