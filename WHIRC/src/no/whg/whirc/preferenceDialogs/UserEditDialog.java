package no.whg.whirc.preferenceDialogs;

import no.whg.whirc.R;
import no.whg.whirc.models.Server;
import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class UserEditDialog extends DialogPreference {
	
	private String title = "null";
	private String nickOne = "null";
	private String nickTwo = "null";
	private String nickThree = "null";
	private String name = "null";
	private EditText nickOneView = null;
	private EditText nickTwoView = null;
	private EditText nickThreeView = null;
	private EditText nameView = null;
	
	/**
	 * Standard constructor for DialogPrefence
	 * 
	 * @param context The application context
	 * @param attrs Attributes from a given android-layout XML file
	 */
	public UserEditDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Set the correct layout, buttons, title and icon
		setDialogLayoutResource(R.layout.user_edit_dialog);
				
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		
		setDialogTitle(title);
		
		setDialogIcon(null);
	}
	
	/**
	 * The method that populates the dialog with information.
	 * This is implemented because DialogPreference does not have support
	 * for the putExtra method
	 * 
	 * @param title The title of the dialog
	 */
	public void setData(Server server) {
		setDialogTitle(server.getSimpleName());

		// Sets the correct information in the editboxes
		nickOne = server.getNickOne();
		nickTwo = server.getNickTwo();
		nickThree = server.getNickThree();
		name = server.getName();
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		
		if (positiveResult) {
			// CHANGE USER DATA HERE
		}
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		
		// Stores the edittexts so they can be written to and read from
		nickOneView = (EditText)view.findViewById(R.id.setDialog_edit_nickOne);
		nickTwoView = (EditText)view.findViewById(R.id.setDialog_edit_nickTwo);
		nickThreeView = (EditText)view.findViewById(R.id.setDialog_edit_nickThree);
		nameView = (EditText)view.findViewById(R.id.setDialog_edit_name);
		
		nickOneView.setText(nickOne);
		nickTwoView.setText(nickTwo);
		nickThreeView.setText(nickThree);
		nameView.setText(name);
	}
		
}
