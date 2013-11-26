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
		if(isDownloadManagerAvailable(activity))
		{
			String url = arg0.toString();
			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
			
			if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.HONEYCOMB)
			{
				request.allowScanningByMediaScanner();
				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
			}
			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "servers.ini");
		
			DownloadManager manager = (DownloadManager)activity.getSystemService(Context.DOWNLOAD_SERVICE);
			manager.enqueue(request);
		}
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onPostExecute(String result)
	{
		
	}
}
