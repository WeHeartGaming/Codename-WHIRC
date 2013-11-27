package no.whg.whirc.preferenceDialogs;

import no.whg.whirc.R;
import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

public class UserEditDialog extends DialogPreference {
	
	private String title;
	
	/**
	 * Standard constructor for DialogPrefence
	 * 
	 * @param context The application context
	 * @param attrs Attributes from a given android-layout XML file
	 */
	public UserEditDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initData();
		
		// Set the correct layout, buttons, title and icon
		setDialogLayoutResource(R.layout.user_edit_dialog);
				
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		
		setDialogTitle(title);
		
		setDialogIcon(null);
	}

	/**
	 * Initializes the data to make sure they hold some information
	 */
	private void initData() {
		title = "null";
	}
	
	/**
	 * The method that populates the dialog with information.
	 * This is implemented because DialogPreference does not have support
	 * for the putExtra method
	 * 
	 * @param title The title of the dialog
	 */
	public void setData(String title) {
		this.title = title;
		setDialogTitle(title);
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		
		if (positiveResult) {
			// CHANGE USER DATA HERE
		}
	}
}
