package no.whg.whirc.activities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import no.whg.whirc.R;
import no.whg.whirc.helpers.WhircDB;
import no.whg.whirc.models.Server;
import no.whg.whirc.preferenceDialogs.UserEditDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.util.AttributeSet;
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
		super.onStop();
		
		// MAKE SURE STUFF IS SAVED HERE
	}

	public static class SettingsFragment extends PreferenceFragment {

		public static WhircDB db = null;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			// Initialize the DB
			db = new WhircDB(getActivity());
			
			addPreferencesFromResource(R.xml.preferences);
			populateServerList();
		}		
		
		@Override
		public void onStop() {
			super.onStop();
			
			// Closing isn't vital
			db.close();
		}

		/**
		 * Populates the list of usernames based on which servers the user has visited.
		 */
		private void populateServerList() {
			// Fetches the group where items will be added. removeAll() makes sure the list is purged each time it is populated
			PreferenceGroup testGroup = (PreferenceGroup)findPreference("settings_userEditCat");
			testGroup.removeAll();
			
			// Prepares lists
			List<UserEditDialog> dialogList = new ArrayList<UserEditDialog>();
			List<Server> servers = new LinkedList<Server>();
			servers = db.getAllServers();
			
			// Extracts the resources which allows generation of new UserEditDialog objects
			Resources resources = this.getResources();
		    XmlPullParser parser = resources.getXml(R.layout.user_edit_dialog);
		    AttributeSet attributes = Xml.asAttributeSet(parser);
			
			// Populates preferences with stored servers if there are any in the database
			if (!servers.isEmpty()) {
				// Adds servers into the dialoglist and populates their data
				for (Server server : servers) {
					UserEditDialog temp = new UserEditDialog(getActivity(), attributes);
					
					temp.setTitle(server.getSimpleName());
					temp.setKey("settings_" + server.getSimpleName());
					temp.setSummary(server.getHost());
					temp.setOrder(servers.indexOf(server));
					
					temp.setData(server);
					
					dialogList.add(temp);
				}
				
				// Adds the dialog elements into the preference group
				for (UserEditDialog dialog : dialogList)
					testGroup.addPreference(dialog);

			}
		}
	}
}
