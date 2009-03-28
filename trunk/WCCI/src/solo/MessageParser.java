package solo;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Feb 22, 2008
 * Time: 6:17:32 PM
 */
public class MessageParser {
    // parses the message from the serverbot, and creates a table of
    // associated names and values of the readings

    private Hashtable<String, Object> table = new Hashtable<String, Object>();

    public MessageParser(String message) {
//    	System.out.println(message);
        StringTokenizer mt = new StringTokenizer(message, "(");
        while (mt.hasMoreElements()) {
            // process each reading
            String reading = mt.nextToken();
            reading = reading.substring(0, reading.indexOf(")"));
            StringTokenizer rt = new StringTokenizer(reading, " ");
            if (rt.countTokens() < 2) {
                System.out.println("Reading not recognized: " + reading);
            } else {
                String readingName = rt.nextToken();
                Object readingValue;
                if (readingName.equals("opponents") || readingName.equals("track") ||
                        readingName.equals("wheelSpinVel") || readingName.startsWith("all")) {
                    // these readings have multiple values
                    readingValue = new DoubleArrayList(rt.countTokens() - 1);                    
                    while (rt.hasMoreElements()) {
                        ((DoubleArrayList)readingValue).add(Double.parseDouble(rt.nextToken()));
                    }
                } else {
                    readingValue = rt.nextToken();
                }
                table.put(readingName, readingValue);
            }
        }
    }

    public void printAll() {
        Enumeration<String> keys = table.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            System.out.print(key + ":  ");
            System.out.println(table.get(key));
        }
    }

    public Object getReading(String key) {
        return table.get(key);
    }
}
