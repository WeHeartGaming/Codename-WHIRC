package no.whg.whirc.helpers;

import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import android.util.Log;

public class ServerParser
{
	public void parseIni(String filename) throws InvalidFileFormatException, IOException
	{
		Ini ini = new Ini(new File(filename));
		
		String channel = ini.get("servers", "n0");
		Log.d("parserInfo", channel);
	}
}
