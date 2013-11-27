package no.whg.whirc.activities;

import no.whg.whirc.R;
import no.whg.whirc.preferenceDialogs.UserEditDialog;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;


/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Allows the user to go back home via the top-left button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Sets the correct fragment
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
	}
	
	public static class SettingsFragment extends PreferenceFragment {
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			addPreferencesFromResource(R.xml.preferences);
			populateServerList();
		}
		
		private void populateServerList() {
			PreferenceGroup testGroup = (PreferenceGroup)findPreference("settings_userEditCat");
			
			// Extracting the temp dialog and removing it from the view
			UserEditDialog testDialog = (UserEditDialog)getPreferenceScreen().findPreference("settings_userEditDialogTemp");
			testGroup.removePreference(findPreference("settings_userEditDialogTemp"));
			
			// Modifying dialog and putting it back in
			testDialog.setTitle("TEST");
			testDialog.setKey("TESTKEY");
			testDialog.setSummary("summary of shit");
			
			testGroup.addPreference(testDialog);
		}
	}
}
