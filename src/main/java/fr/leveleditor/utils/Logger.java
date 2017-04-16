package fr.leveleditor.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private static SimpleDateFormat formater = new SimpleDateFormat("[HH:mm:ss]");
	
	public static void info(Object o) {
		Date date = new Date();
		System.out.println(formater.format(date) + " [INFO] " + o);
	}
	
	public static void warning(Object o) {
		Date date = new Date();
		System.out.println(formater.format(date) + " [WARNING] " + o);
	}
	
	public static void error(Object o) {
		Date date = new Date();
		System.err.println(formater.format(date) + " [ERROR] " + o);
	}
}
