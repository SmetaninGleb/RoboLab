package ru.li24robotics.ev3.robolab.cubeFinder;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.omg.CORBA.SystemException;

public class RouteFileParser {
	private static FileInputStream fileInputStream;
	private static ObjectInputStream objectInputStream;
	private static String lastFileName;
	
	public static Route getRouteFromStartCors(int[] startCors)
	{
		return getRouteFromFile("[" + startCors[0] + "][" + startCors[1] + "].route");
	}
	
	public static Route getRouteFromFile(String fileName)
	{
		RouteFileParser.lastFileName = fileName;
		try {
			fileInputStream = new FileInputStream(fileName);
			objectInputStream = new ObjectInputStream(fileInputStream);
			return (Route)objectInputStream.readObject();
		}catch(Exception e)
		{
			System.err.println("File Not Found Exception! Not find -" + fileName + "- file");
			return null;
		}
	}

}
