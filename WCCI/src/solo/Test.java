package solo;

import gnu.trove.THashMap;
import gnu.trove.TIntDoubleHashMap;
import gnu.trove.TObjectDoubleHashMap;
import gnu.trove.decorator.TIntDoubleHashMapDecorator;
import it.unimi.dsi.fastutil.doubles.Double2DoubleMap;
import it.unimi.dsi.fastutil.doubles.Double2DoubleRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleRBTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cern.colt.list.DoubleArrayList;
import cern.colt.list.IntArrayList;
import cern.colt.map.AbstractIntDoubleMap;
import cern.colt.map.OpenIntDoubleHashMap;
/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Feb 1, 2008
 * Time: 4:56:58 PM
 */
public class Test {

    final static int port = 3001;
    final static int UDP_CLIENT_TIMEOUT=50;
    public static DatagramSocket socket=null;
    static InetAddress remote = null;

    public static void identify() throws Exception{    	
    	do {    		
    		try{
    			if (socket==null) socket = new DatagramSocket();
                if (remote==null) remote = InetAddress.getByName("127.0.0.1");
                byte[] send = new CarControl().toString().getBytes();
                
                String toSend = "wcci2008";
                byte[] outBuffer = toSend.getBytes();
                
               
            	socket.setSoTimeout(UDP_CLIENT_TIMEOUT);
            	socket.send(new DatagramPacket  (send, send.length, remote, port));	
    			socket.send(new DatagramPacket  (outBuffer, outBuffer.length, remote, port));
	    		byte[] inBuffer = new byte[1024];
	            DatagramPacket packet = new DatagramPacket (inBuffer, inBuffer.length);
	            socket.receive (packet);
	            String received = new String(packet.getData(), 0, packet.getLength());                
	            if (received.startsWith("***identified***")){                	
	            	break;
	            }
    		 } catch (Exception e){
    			 	//e.printStackTrace();    			 	
    	     }
            
    	} while(true);                   
    }
    
    public static void test1(String name, Map map,int[] keys,double[] values){
    	long ti = System.currentTimeMillis();
    	for (int j= 0;j<10000;++j)
        for (int i=0;i<keys.length;++i)
        	map.put(keys[i], values[i]);
        System.out.println("Test "+name +"  Time taken: "+(System.currentTimeMillis()-ti));
    }
    
    public static void quicksort(double[] a) {
//        shuffle(a);                        // to guard against worst-case
        quicksort(a, 0, a.length - 1);
    }

    // quicksort a[left] to a[right]
    public static void quicksort(double[] a, int left, int right) {
        if (right <= left) return;
        int i = partition(a, left, right);
        quicksort(a, left, i-1);
        quicksort(a, i+1, right);
    }

    // partition a[left] to a[right], assumes left < right
    private static int partition(double[] a, int left, int right) {
        int i = left - 1;
        int j = right;
        while (true) {
            while (a[++i]< a[right])      // find item on left to swap
                ;                               // a[right] acts as sentinel
            while (a[right]< a[--j])      // find item on right to swap
                if (j == left) break;           // don't go out-of-bounds
            if (i >= j) break;                  // check if pointers cross            
            double tmp = a[i];
            a[i] = a[j];
            a[j] =tmp;
        }
        double tmp = a[i];
        a[i] = a[right];
        a[right] =tmp;
        return i;
    }



    public static void main(String[] args) throws Exception {
        System.out.println("Starting");
        /*int[] keys = new int[10000];
        double[] values = new double[10000];
        int[] k = new int[10000];
        long ti =0;
        double[] v = new double[10000];        
      
        Int2DoubleMap map1 = new Int2DoubleRBTreeMap();
        HashMap<Integer,Double> map3 = new HashMap<Integer, Double>();
        Int2DoubleMap map4 = new Int2DoubleAVLTreeMap();        
        Int2DoubleMap map6 = new Int2DoubleLinkedOpenHashMap();
        Hashtable<Integer, Double> map7 = new Hashtable<Integer, Double>();
        Object2ObjectMap<Integer, Double> map8 = new Object2ObjectOpenHashMap<Integer, Double>();
        THashMap<Integer, Double> map9 = new THashMap<Integer, Double>();
        TObjectDoubleHashMap<Integer> map11 = new TObjectDoubleHashMap<Integer>();                        
        IntArrayList coltK = new IntArrayList();
        DoubleArrayList coltV = new DoubleArrayList();             
        for (int i=0;i<keys.length;++i){
        	keys[i] = i;
        	values[i] = Math.random()*(keys.length-i);
        }
                
        
        FastMap<Integer, Double> fm = new FastMap<Integer, Double>();
        
        
//        List<String> names = java.util.Arrays.asList("FastUtil RBTreeMap","Java util HashMap","FastUtil AVLTreeMap","FastUtil OpenHashMap","FastUtil linked OpenHashmap","HashTable","FastUtil object2object OpenHashMap","Trove HashMap","Trove IntDouble Hashmap decorator");
//        List<Map<Integer, Double>> maps = java.util.Arrays.asList(map1,map3,map4,map5,map6,map7,map8,map12);
               
//        for (int i=0;i<keys.length;++i){        
//        	map10.put(keys[i], values[i]);
//        	map.put(keys[i], values[i]);
//        	fm.put(keys[i],values[i]);
//        }
        int N = 1000;
        double d1 =0;
        double d2 = 0;
        double d3 = 0;
        double d4 = 0;
        for (int kk =0;kk<1;++kk){
        	
//        	ti = System.currentTimeMillis();
//	        for (int j= 0;j<N;++j){
//		        for (int i=0;i<keys.length;++i){
////		        	fm.put(keys[i], values[i]);
////		        	fm.get(keys[i]);
//		        	fm.containsKey(keys[i]);
//		        }		      
//	        }
//	        d4 += (System.currentTimeMillis()-ti);
//        	
	        
	        ti = System.currentTimeMillis();
	        for (int j= 0;j<N;++j){
	        	  AbstractIntDoubleMap map = new OpenIntDoubleHashMap();
		        for (int i=0;i<keys.length;++i){
//		        	map.put(keys[i], values[i]);
//		        	map.get(keys[i]);
		        	map.containsKey(keys[i]);
		        }
		        k = map.keys().elements();
		        v = map.values().elements();
	        }
	        d2 += (System.currentTimeMillis()-ti);
	        
	        
	     
	        ti = System.currentTimeMillis();
	        for (int j= 0;j<N;++j){
	        	TIntDoubleHashMap map10 = new TIntDoubleHashMap();
		        for (int i=0;i<keys.length;++i){
//		        	map10.put(keys[i], values[i]);
//		        	map10.get(keys[i]);
		        	map10.containsKey(keys[i]);
		        }
		        k = map10.keys();
		        v = map10.getValues();
	        }
	        d3 += (System.currentTimeMillis()-ti);
	        
	        ti = System.currentTimeMillis();
	        for (int j= 0;j<N;++j){
	        	Int2DoubleMap map5 = new Int2DoubleOpenHashMap();
		        for (int i=0;i<keys.length;++i){
//		        	map5.put(keys[i], values[i]);
//		        	map5.get(keys[i]);
		        	map5.containsKey(keys[i]);
		        }
		        map5.keySet().toArray(k);
		        map5.values().toArray(v);
	        }
	        d1 += (System.currentTimeMillis()-ti);	        
	        
	        
        }    
        System.out.println("Test FastUtil Int Double HashMap  Time taken: "+d1);
        System.out.println("Test Colt Int Double HashMap  Time taken: "+d2);
        System.out.println("Test Trove Object Double HashMap  Time taken: "+d3);
        System.out.println("Test Primitive long map  Time taken: "+d4);
        
      
       
       
//        ti = System.currentTimeMillis();
//        for (int j= 0;j<10000;++j)
//        for (int i=0;i<keys.length;++i)
//        	map10.put(keys[i], values[i]);
//        System.out.println("Test Trove IntDouble OpenHashMap  Time taken: "+(System.currentTimeMillis()-ti));
//        for (int i=0;i<names.size();++i){
//        	String s = names.get(i);
//        	Map m = (Map)(maps.get(i));
//        	test1(s,m,keys,values);
//        }
        
              
//        ti = System.currentTimeMillis();
//        for (int j= 0;j<10000;++j){
//		    for (int i=0;i<keys.length;++i)
//		    	map1.put(keys[i], values[i]);
//
//		    
//		    map1.keySet().toArray(k);
////		    Arrays.sort(k);
//		    map1.values().toArray(v);
//        }
//        System.out.println("Test FastUtil IntDoubleRB sort  Time taken: "+(System.currentTimeMillis()-ti));
        
//        ti = System.currentTimeMillis();
//        for (int j= 0;j<10000;++j){
//	        for (int i=0;i<keys.length;++i)
//	        	map10.put(keys[i], values[i]);	        
//	    	map10.keys(k);	    	
//	    	Arrays.sort(k);
//        	v = map10.getValues();
//        }
//        System.out.println("Test Trove IntDouble HashMap  Time taken: "+(System.currentTimeMillis()-ti));
//        
//               
//        ti = System.currentTimeMillis();
//        for (int j= 0;j<10000;++j){
//		    for (int i=0;i<keys.length;++i)
//		    	map5.put(keys[i], values[i]);
//
//		    
//		    map5.keySet().toArray(k);
//		    Arrays.sort(k);
//		    map5.values().toArray(v);
//        }
//        System.out.println("Test FastUtil IntDoubleHashMap sort  Time taken: "+(System.currentTimeMillis()-ti));
//        
//        ti = System.currentTimeMillis();
//        for (int j= 0;j<10000;++j){
//        	for (int i=0;i<keys.length;++i)
//        		map.put(keys[i], values[i]);
//        	map.values().sort();
//        	map.keys().elements(k);
//        	map.values().elements(v);
//        	
//        }
//        System.out.println("Test Colt OpenHashMap sort Time taken: "+(System.currentTimeMillis()-ti));
        
        
                
        System.out.println("******** Test insertion*************");
        
		System.exit(0);//*/
        identify();
        BaseDriver sd = new TurnDriver();
        socket.setSoTimeout(UDP_CLIENT_TIMEOUT*20);
        while (true) {
        	try{
	        	byte[] inBuffer = new byte[8192];
	            DatagramPacket packet = new DatagramPacket (inBuffer, inBuffer.length);
	            socket.setSoTimeout(UDP_CLIENT_TIMEOUT*200);
	            socket.receive (packet);
	            
	            String received = new String(packet.getData(), 0, packet.getLength());            
	            if (received.startsWith("***shutdown***")){
	            	sd.onShutdown();
	            	socket.close();
	            	break;
	            }
	            
	            if (received.startsWith("***restart***")){
	            	sd.onRestart();
	            	identify();
	            	continue;
	            }	        	
	        	String toSend = sd.drive(received);
	                    	
	            byte[] outBuffer = toSend.getBytes();
	            socket.send(new DatagramPacket  (outBuffer, outBuffer.length, remote, port));
	            //System.out.println(toSend);
	            
	            //System.out.println(i); // + "   " + received);
	            //MessageParser parser = new MessageParser (received);
	            //parser.printAll ();
	            //i++;	            
        	} catch (Exception e){
        		//e.printStackTrace();
        	}        	
        }
        System.out.println("Finished");
    }

    
}
