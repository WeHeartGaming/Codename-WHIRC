package no.whg.whirc.helpers;


import no.whg.whirc.R;

import java.util.List;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import no.whg.whirc.activities.MainActivity;

/**
 * 
 * @author Peer Andreas Stange
 *
 * @param String the input for the doInBackground
 */
public class ServerListDownload extends AsyncTask<String, Integer, String>
{
	
	private MainActivity activity;
	private Context context;
	String filePath;
	
	public ServerListDownload(Context c)
	{
		context = c;
		filePath = "none";
	}
	
	public static boolean isDownloadManagerAvailable(Context context)
	{
		
		try
		{
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD)
			{
				return false;
			}
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setClassName("com.android.providers.download.ui", "com.android.providers.download.ui.DownloadList");
			List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			
			return list.size() > 0;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	@Override
	protected String doInBackground(String... arg0)
	{
		try
		{
			String url = "http://www.mirc.com/servers.ini";
			Log.d("ServerListDownload", url);
			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
			
			if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.HONEYCOMB)
			{
				request.allowScanningByMediaScanner();
				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
			}
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "servers.ini");
			DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
			manager.enqueue(request);
			filePath = Environment.DIRECTORY_DOWNLOADS + "/servers.ini";			
			
		}
		catch(Exception E)
		{
			Log.d("ServerListDownload", E.toString());
		}
			
		// TODO Auto-generated method stub
		
		return filePath;
	}
	
	@Override
	protected void onPostExecute(String result)
	{
		
	}
}
