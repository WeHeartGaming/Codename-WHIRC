package no.whg.whirc.preferenceDialogs;

import no.whg.whirc.R;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

public class UserDeleteDialog extends DialogPreference {

	/**
	 * Standard constructor for DialogPrefence
	 * 
	 * @param context The application context
	 * @param attrs Attributes from a given android-layout XML file
	 */
	public UserDeleteDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		setDialogLayoutResource(R.layout.user_delete_dialog);
		
		setPositiveButtonText(android.R.string.yes);
		setNegativeButtonText(android.R.string.cancel);
		
		setDialogIcon(null);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		
		if (positiveResult) {
			// DELETE USER DATA HERE MANG
		}
	}
}