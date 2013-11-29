package no.whg.whirc.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import no.whg.whirc.models.Server;

import org.ini4j.InvalidFileFormatException;

import android.content.Context;

/**
 * 
 * @author Peer Andreas
 *
 */
public class ServerParser
{
	Random randGenerator;
	private String temp;
	private String[] iniString;
	private String[] splitString;
	private Server insertServer;
	private Context context;
	private WhircDB dbObject;
	
	/**
	 * 
	 * @param c The apps context
	 */
	public ServerParser(Context c)
	{
		context = c;
		dbObject = new WhircDB(context);
		randGenerator = new Random(2345345);
	}
	
	/**
	 * 
	 * @param filename full filepath to the file
	 * @throws InvalidFileFormatException
	 * @throws IOException
	 */
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
				String userName = "ph" + (randGenerator.nextInt(10000) + 1000);
				dbObject.addServer(userName, "", "", "", splitString[1], "6667", splitString[3]);
				//insert splitString[1] and [4] and push them to the db with ports and generic usernames
			}
			
		}
		
		dbObject.close();
	}
}
