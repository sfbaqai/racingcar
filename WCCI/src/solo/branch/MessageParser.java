package solo;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Feb 22, 2008
 * Time: 6:17:32 PM
 */
public final class MessageParser {
    // parses the message from the serverbot, and creates a table of
    // associated names and values of the readings

    private final static Object2ObjectOpenHashMap<String, Object> table = new Object2ObjectOpenHashMap<String, Object>();

    public MessageParser(String message) {
    	System.out.println(table);
    }
    
    public static final void fromString(String message){
//    	System.out.println(message);
    	Object2ObjectOpenHashMap<String, Object> table = MessageParser.table;
        StringTokenizer mt = new StringTokenizer(message, "(");
        int i;
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
                if (readingName.equals("allTypes")){
                	Object exist = table.get(readingName);                    
                    int[] tmp = (exist==null) ? new int[50] : (int[])exist;
                    i = 0;
                    while (rt.hasMoreElements()) {
                        tmp[i++] = Integer.parseInt(rt.nextToken());
                    }
                    table.put("sz", i);
                    if (exist==null) table.put(readingName, tmp);
                } else if (readingName.equals("opponents") || readingName.equals("track") ||
                        readingName.equals("wheelSpinVel") || readingName.startsWith("all")) {
                    // these readings have multiple values                	                    
                    Object exist = table.get(readingName);
                    int size = readingName.equals("opponents") ? CarState.OPPONENTS_SENSORS_NUM : readingName.equals("track") ? 19 :
                    	readingName.equals("wheelSpinVel") ? 4 : 50;
                    double[] tmp = (exist==null) ? new double[size] : (double[])exist;
                    i = 0;
                    while (rt.hasMoreElements()) {
                        tmp[i++] = Double.parseDouble(rt.nextToken());
                    }
                    if (readingName.equals("allLength")){
                    	table.put("sz", i);
                    }
                    if (exist==null) table.put(readingName, tmp);
                } else {
                    readingValue = rt.nextToken();
                    table.put(readingName, readingValue);
                }                
            }
        }

    }

    public final void printAll() {
        System.out.println(table);
    }

    public static final Object getReading(String key) {
        return table.get(key);
    }
}
