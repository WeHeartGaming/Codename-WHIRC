package no.whg.whirc.dialogs;

import no.whg.whirc.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
		
		View mainView = inflater.inflate(R.layout.dialog_invite, null);
		
		inviteText = (TextView)mainView.findViewById(R.id.dialog_invite);
		inviteText.setText(String.format(String.valueOf(R.string.dialog_invite_recieved), user, channel));
		
		builder.setView(mainView)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					/* JOIN CHANNEL HERE */
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
