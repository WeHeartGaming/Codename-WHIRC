package no.whg.whirc.preferenceDialogs;

import no.whg.whirc.R;
import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

public class UserEditDialog extends DialogPreference {
	
	private String title;
	
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

	@Override
	protected View onCreateDialogView() {
		return super.onCreateDialogView();
	}	
	
	/*
	 * Initializes the data to make sure they hold some information
	 */
	private void initData() {
		title = "null";
	}
	
	
	public void setData(String title) {
		this.title = title;
		setDialogTitle(title);
	}
}
