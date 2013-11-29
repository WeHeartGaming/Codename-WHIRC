package no.whg.whirc.dialogs;

import no.whg.whirc.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * @author Inge
 *
 */

public class InviteDialog extends DialogFragment {

	private String user;
	private String channel;
	private TextView inviteText;
	
	/**
	 * Constructor. Sets the initial data fields.
	 * 
	 * @param user Sender of the invite
	 * @param channel Channel the user has been invited to
	 */
	public InviteDialog(String user, String channel) {		
		this.user = user;
		this.channel = channel;		
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		// Retrieve main element in dialog_invite.xml
		View mainView = inflater.inflate(R.layout.dialog_invite, null);
		
		// Retrieve TextView in mainView and set the correct text
		inviteText = (TextView)mainView.findViewById(R.id.dialog_invite);
		inviteText.setText(String.format(String.valueOf(R.string.dialog_invite_recieved), user, channel));
		
		// Setup onclick listeners for both buttons in the dialog
		builder.setView(mainView)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					/* JOIN CHANNEL HERE */
					Log.d("join","join!");
					// We ran across a bug in the library that causes a crash whenever an
					//  invitation event is recieved. So we decided to leave this class as is
					//  even though it doesn't do anything other than crash the application
				}
			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		
		return builder.create();
	}
}
