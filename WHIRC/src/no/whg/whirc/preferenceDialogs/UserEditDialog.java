package no.whg.whirc.preferenceDialogs;

import no.whg.whirc.R;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

public class UserEditDialog extends DialogPreference {
	
	public UserEditDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Set the correct layout, buttons and icon
		setDialogLayoutResource(R.layout.user_edit_dialog);
		
		setDialogTitle("TITTEL");
		
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		
		setDialogIcon(null);
	}

	@Override
	protected View onCreateDialogView() {
		return super.onCreateDialogView();
	}	
	
}
