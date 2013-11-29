package no.whg.whirc.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import no.whg.whirc.models.Server;

import org.ini4j.InvalidFileFormatException;

import android.content.Context;

public class ServerParser
{
	Random randGenerator;
	private String temp;
	private String[] iniString;
	private String[] splitString;
	private Server insertServer;
	private Context context;
	private WhircDB dbObject;
	
	public ServerParser(Context c)
	{
		context = c;
		dbObject = new WhircDB(context);
		randGenerator = new Random(2345345);
	}
	public void parseIni(String filename) throws InvalidFileFormatException, IOException
	{
		iniString = new String[220];
		splitString = new String[5];
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		
		int i = 0;
		while((temp = reader.readLine()) != null) //reading in the file	
		{
			iniString[i++] = temp;
		}
		
		for(int j = 18; j < 220; j++)		//hardcoded shit FUCK OFF
		{
			splitString = iniString[j].split(":");
			
			if(splitString[0].contains("Random server"))
			{
				String userName = "ph" + randGenerator.nextInt(1000);
				dbObject.addServer(userName, "", "", "", splitString[1], "6667", splitString[3]);
				//insert splitString[1] and [4] and push them to the db with ports and generic usernames
			}
			
		}
		
		dbObject.close();
	}
}
