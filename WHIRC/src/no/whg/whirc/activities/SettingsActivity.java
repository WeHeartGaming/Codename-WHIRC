package no.whg.whirc.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;

import no.whg.whirc.R;
import no.whg.whirc.preferenceDialogs.UserEditDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;


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
	
	
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		// MAKE SURE STUFF IS SAVED HERE
	}



	public static class SettingsFragment extends PreferenceFragment {
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			addPreferencesFromResource(R.xml.preferences);
			populateServerList();
		}
		
		/**
		 * Populates the list of usernames based on which servers the user has visited.
		 */
		private void populateServerList() {
			PreferenceGroup testGroup = (PreferenceGroup)findPreference("settings_userEditCat");

			List<UserEditDialog> dialogList = new ArrayList<UserEditDialog>();
			
			// Extracts the resources which allows generation of new UserEditDialog objects
			Resources resources = this.getResources();
		    XmlPullParser parser = resources.getXml(R.layout.user_edit_dialog);
		    AttributeSet attributes = Xml.asAttributeSet(parser);
			
		    // TEMPORAY
			for (int i = 0; i < 6; i++) {
				UserEditDialog temp = new UserEditDialog(getActivity(), attributes);
				
				temp.setTitle("Nummer" + String.valueOf(i));
				temp.setKey("key" + String.valueOf(i));
				temp.setSummary("summary" + String.valueOf(i));
				temp.setData("Tittel" + String.valueOf(i));
				temp.setOrder(i);
				
				dialogList.add(temp);
			}
			
			for (int j = 0; j < dialogList.size(); j++) {
				testGroup.addPreference(dialogList.get(j));
			}
		}
	}
}
