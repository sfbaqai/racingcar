package net.sourceforge.jFuzzyLogic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * General pupose rutines
 * @author Pablo Cingolani <pcingola@users.sourceforge.net>
 */
public class Gpr {

	/** Only print this number of warnings */
	public static int MAX_NUMBER_OF_WARNINGS = 5;

	/** Print warning only N times */
	public static int warnCount = 0;

	/**
	 * Prits a debug message (prints class name, method and line number)
	 * @param debug : Only prints message if debug=true  			
	 * @param obj : Object to print
	 */
	public static void debug(boolean debug, Object obj) {
		if( debug ) debug(obj, 1, true);
	}

	/**
	 * Prits a debug message (prints class name, method and line number)
	 * @param obj : Object to print
	 */
	public static void debug(Object obj) {
		debug(obj, 1, true);
	}

	/**
	 * Prits a debug message (prints class name, method and line number)
	 * @param obj : Object to print
	 * @param offset : Offset N lines from stacktrace
	 */
	public static void debug(Object obj, int offset) {
		debug(obj, offset, true);
	}

	/**
	 * Prits a debug message (prints class name, method and line number)
	 * @param obj : Object to print
	 * @param offset : Offset N lines from stacktrace
	 * @param newLine : Print a newline char at the end ('\n')
	 */
	public static void debug(Object obj, int offset, boolean newLine) {
		StackTraceElement ste = new Exception().getStackTrace()[1 + offset];
		String steStr = ste.getClassName();
		int ind = steStr.lastIndexOf('.');
		steStr = steStr.substring(ind + 1);
		steStr += "." + ste.getMethodName() + "(" + ste.getLineNumber() + "): " + (obj == null ? null : obj.toString());
		if( newLine ) System.err.println(steStr);
		else System.err.print(steStr);
	}

	/**
	 * Equivalent to Integer.parseInt, except it returns 0 on invalid integer (NumberFormatException)
	 * @param s
	 * @return	int
	 */
	public static boolean parseBooleanSafe(String s) {
		if( s == null ) return false;
		try {
			if( s.equalsIgnoreCase("true") || s.equalsIgnoreCase("on") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("si") || s.equalsIgnoreCase("oui") || s.equalsIgnoreCase("ja") || s.equalsIgnoreCase("da") ) return true;
			return false;
		} catch(Exception e) {
			return false;
		}
	}

	/**
	 * Equivalent to Double.parseDouble, except it returns 0 on invalid value (NumberFormatException)
	 * @param s
	 * @return	double
	 */
	public static double parseDoubleSafe(String s) {
		try {
			return Double.parseDouble(s);
		} catch(Exception e) {
			return 0;
		}
	}

	/**
	 * Equivalent to Integer.parseInt, except it returns 0 on invalid integer (NumberFormatException)
	 * @param s
	 * @return	int
	 */
	public static int parseIntSafe(String s) {
		try {
			return Integer.parseInt(s);
		} catch(Exception e) {
			return 0;
		}
	}

	/**
	 * Write an object to a file 
	 * @param fileName : File to write
	 * @param obj : Object 
	 */
	public static void toFile(String fileName, Object obj) {
		BufferedWriter outFile;
		try {
			outFile = new BufferedWriter(new FileWriter(fileName));
			outFile.write(obj.toString());
			outFile.close();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Print a warning message (only a few of them) 
	 * @param warning
	 */
	public static void warn(String warning) {
		if( warnCount < MAX_NUMBER_OF_WARNINGS ) {
			warnCount++;
			Gpr.debug(warning, 2);
		}
	}

}
