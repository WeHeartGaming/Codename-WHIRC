/**
 * 
 */
package no.whg.whirc.helpers;

import android.os.Binder;

/**
 * @author Fredrik
 *
 */
public class ConnectionServiceBinder extends Binder {
	private ConnectionService cService;

	/**
	 * @param connection
	 */
	public ConnectionServiceBinder(ConnectionService cService) {
		this.cService = cService;
	}
	
	public ConnectionService getService() {
		return this.cService;
	}


}
