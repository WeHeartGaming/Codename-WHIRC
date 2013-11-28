package no.whg.whirc.helpers;


import java.io.IOException;
import java.util.List;

import no.whg.whirc.activities.MainActivity;

import org.ini4j.InvalidFileFormatException;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author Peer Andreas Stange
 *
 * @param String the input for the doInBackground
 */
public class ServerListDownload extends AsyncTask<String, Integer, String>
{
	
	private MainActivity activity;
	private BroadcastReceiver downloadReceiver;
	private Context context;
	private ServerParser iniParser;
	String filePath;
	
	public ServerListDownload(Context c)
	{
		context = c;
		filePath = "none";
		iniParser = new ServerParser(context);
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
			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
			
			if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.HONEYCOMB)
			{
				request.allowScanningByMediaScanner();
				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
			}
			
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "servers.ini");
			DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
			
			Log.d("ServerListDownload", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
			manager.enqueue(request);
			filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/servers.ini";			
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
		Log.d("parserInfo", result);
		Log.d("parserInfo", filePath);
		
		try 
        {
			iniParser.parseIni(result);
		} 
        catch (InvalidFileFormatException e)
		{
			e.printStackTrace();
			Log.d("parserInfo", e.toString());
		} 
        catch (IOException e)
        {
			e.printStackTrace();
			Log.d("parserInfo", e.toString());
		}
	}
}
