/**
 * 
 */
package no.whg.whirc.adapters;

import no.whg.whirc.fragments.ConnectionFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author Fredrik
 *
 */
public class ConnectionPagerAdapter extends FragmentPagerAdapter {

	public ConnectionPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		return new ConnectionFragment();
	}

	@Override
	public int getCount() {
		return 1;
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
        return "Networks";
    }
	
	@Override
	public int getItemPosition(Object object) {
			return POSITION_NONE;
	}
	
	

}
