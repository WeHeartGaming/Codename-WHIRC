package no.whg.whirc.preferenceDialogs;

import no.whg.whirc.R;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

public class UserDeleteDialog extends DialogPreference {

	public UserDeleteDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		setDialogLayoutResource(R.layout.user_delete_dialog);
		
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		
		setDialogIcon(null);
	}

	@Override
	protected View onCreateDialogView() {
		// TODO Auto-generated method stub
		return super.onCreateDialogView();
	}
}
