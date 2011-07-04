package solo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Feb 1, 2008
 * Time: 4:56:58 PM
 */
public class Test {
	private final static String clientID = "championship2011";
    private final static int port = 3001;
    private final static int UDP_CLIENT_TIMEOUT=50;
    private final static int BUFFER_SIZE=8192;
    private static DatagramSocket socket=null;
    private static InetAddress remote = null;
    private final static int[] chars = new int[BUFFER_SIZE];   
    private static int prevLen;
    private static long maxTime = 0;
    private static double atTime = 0;
    public static double maxX = 0;
    private static final byte[] inBuffer = new byte[BUFFER_SIZE];
    private static final DatagramPacket packet = new DatagramPacket (inBuffer, inBuffer.length);
    private static final BaseDriver sd = new TurnDriver();
    private static final DatagramPacket outPacket = new DatagramPacket(CarControl.output, 0);

    public static void identify() throws Exception{    	
    	do {    		
    		try{
    			if (socket==null) socket = new DatagramSocket();
                if (remote==null) remote = InetAddress.getByName("127.0.0.1");                
                int len = prevLen;
                byte[] send = CarControl.output;
                if (len ==0 ) 
                	len = new CarControl().toBytes();
                else send[len-1] = 1;
                
                String toSend = clientID;
                byte[] outBuffer = toSend.getBytes();
                
               
            	socket.setSoTimeout(UDP_CLIENT_TIMEOUT);        
            	socket.send(new DatagramPacket  (send, len, remote, port));	            	
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
    
    /*public static void test1(){
    	
    	long ti = System.nanoTime();
    	double tmp = 1000;
        for (int i = 0;i<1000000000;++i){
        	double t = (tmp<0) ? -tmp : tmp;
        	t = (tmp<0) ? -tmp : tmp;
        	t = (tmp<0) ? -tmp : tmp;
        }
        System.out.println("Test 1 taken: "+(System.nanoTime()-ti)/1000000);
    }
    
    public static void test2(){    	
    	long ti = System.nanoTime();  
    	double tmp = 1000;
    	double t =tmp;
        for (int i = 0;i<1000000000;++i){
        	if (tmp<0) t = -tmp;
        	if (tmp<0) t = -tmp;
        	if (tmp<0) t = -tmp;
        }
    	
        System.out.println("Test 2 taken: "+(System.nanoTime()-ti)/1000000);
    }//*/
    
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


    static final void init(){
//    	String str = "(angle -0.000272449)(curLapTime -0.982)(damage 0)(distFromStart 3174.21)(distRaced -99.9897)(fuel 80)(gear 0)" +
//    			"(lastLapTime -0.98)(opponents 100 100 100 100 100 100 100 100 100 100 100 100 100 100 100 100 100 100)(racePos 1)(rpm 2094.4)" +
//    			"(speedX 0.0626618)(speedY -0.00302766)(track 5.50211 5.58817 5.8612 6.37042 7.22456 8.66359 11.2975 17.2813 48.7607 77.6603 25.9742 " +
//    			"15.1856 10.7399 8.45764 7.13737 6.33211 5.84498 5.58155 5.49789)(trackPos -0.000377308)(wheelSpinVel 0 0 0 0)(posX 234.982)" +
//    			"(posY 413.48)(posZ 20.577)(ax 0.00844149)(ay 0.0823664)(az -0.0866334)(cx 263.365)(cy 739.248)(cz 0)(v0x 233.071)(v0y 419.179)" +
//    			"(v1x 232.035)(v1y 408.227)(v2x 236.796)(v2y 418.848)(v3x 235.887)(v3y 407.885)(length 3.80372)(radius 327)(radiusl 321.5)" +
//    			"(radiusr 332.5)(allTypes 2 2 2 2 2 2 2 2 3 3 3 3 3 3 3 3 3)(allDist 3171.77 3175.58 3179.38 3183.18 3186.99 3190.79 3194.6 3198.4 " +
//    			"3202.2 3211.2 3220.2 3229.2 3238.2 3247.2 3256.2 3265.2 0)(allX 263.365 263.259 263.168 263.091 263.03 262.984 262.954 262.938 " +
//    			"334.891 334.891 334.891 334.891 334.891 334.891 334.891 334.891 334.891)(allY 739.248 737.967 736.684 735.401 734.117 732.832 " +
//    			"731.546 730.261 406.829 406.829 406.829 406.829 406.829 406.829 406.829 406.829 406.829)(allRadiusL 321.5 320.214 318.929 317.643" +
//    			" 316.357 315.072 313.786 312.5 0 0 0 0 0 0 0 0 0)(allRadiusR 332.5 331.214 329.929 328.643 327.357 326.072 324.786 323.5 0 0 0 " +
//    			"0 0 0 0 0 0)(allArc 0.0116322 0.0116781 0.0117244 0.011771 0.011818 0.0118654 0.0119132 0.0119614 0 0 0 0 0 0 0 0 0)" +
//    			"(allLength 3.80372 3.80372 3.80372 3.80372 3.80372 3.80372 3.80372 3.80372 9 9 9 9 9 9 9 9 9.23077)" +
//    			"(allSXL 233.071 236.796 240.525 244.256 247.99 251.725 255.462 259.199 262.937 271.937 280.937 289.937 298.937 307.937" +
//    			" 316.937 325.937 334.891)(allSYL 419.179 418.848 418.561 418.317 418.117 417.961 417.85 417.783 417.761 417.761 417.761 417.761" +
//    			" 417.761 417.761 417.761 417.761 417.829)(allSXR 232.035 235.887 239.744 243.604 247.467 251.332 255.199 259.068 262.937 271.937" +
//    			" 280.937 289.937 298.937 307.937 316.937 325.937 334.891)(allSYR 408.227 407.885 407.588 407.336 407.13 406.969 406.853 406.784 " +
//    			"406.761 406.761 406.761 406.761 406.761 406.761 406.761 406.761 406.829)(allEXL 236.796 240.525 244.256 247.99 251.725" +
//    			" 255.462 259.199 262.937 271.937 280.937 289.937 298.937 307.937 316.937 325.937 334.937 344.122)" +
//    			"(allEXR 235.887 239.744 243.604 247.467 251.332 255.199 259.068 262.937 271.937 280.937 289.937 298.937 307.937" +
//    			" 316.937 325.937 334.937 344.122)(allEYL 418.848 418.561 418.317 418.117 417.961 417.85 417.783 417.761 417.761 " +
//    			"417.761 417.761 417.761 417.761 417.761 417.761 417.76 417.829)" +
//    			"(allEYR 407.885 407.588 407.336 407.13 406.969 406.853 406.784 406.761 406.761 406.761 406.761 406.761 406.761 406.761" +
//    			" 406.761 406.76 406.829)";
//    	for (int i =0;i<str.length();++i){
//    		chars[i] = str.charAt(i);
//    	}
//    	NewCarState cs = new NewCarState(chars,str.length());
//    	CarControl cc = new CarControl();
//    	State<NewCarState,CarControl> st= new State<NewCarState,CarControl>(0,cs,null,null);
//    	if (st==null);
//    	if (cc==null);
//    	if (cs==null);    	
    }

    public static void main(String[] args) throws Exception {
    	System.gc();
        System.out.println("Starting");
        init();
//        double ox = 0;
//        double oy = 4;
//        double[] rs = new double[4];
//        double x1 = 0;
//        double y1 = 0;
//        double x2 = 4;
//        double y2 = 0;
//        double x0 = 4;
//        double y0 = 2;
//        Geom.findCenter(ox, oy, x1, y1, x2, y2, x0, y0, rs);
//        double x = rs[0];
//        double y = rs[1];
//        double dx = x- x0;
//        double dy = y-y0;
//        double d = Math.sqrt(dx*dx+dy*dy);
//        double d1 = Math.sqrt(Geom.ptLineDistSq(ox, oy, x1, y1, x, y, null));
//        System.out.println(d+"  "+d1);
//        test1();
//        test2();
//        Vector2D v1 = new Vector2D(2,1);
//        AffineTransform at = AffineTransform.getRotateInstance(0.35,0,1);
//        at.transform(v1, v1);
//        Vector2D v2 = new Vector2D(2,1);
//        v2.rotate(0.35,0,1);
//        System.out.println(v1+"    "+v2);        
//        double[] rs = new double[4];
//        int sz = Geom.getCircleSpcial(0, 0, 1, 2, 2, 0, 0, rs);
//        if (sz>0){
//        	System.out.println(rs[0]+"   "+rs[1]);
//        }
//        System.exit(0);                          
//        final byte[] outBuffer = CarControl.output;
        identify();
        socket.setSoTimeout(UDP_CLIENT_TIMEOUT*20);
        outPacket.setAddress(remote);
        outPacket.setPort(port);
        socket.setSendBufferSize(1024);
        socket.setReceiveBufferSize(BUFFER_SIZE);
        while (true) {
        	try{	        		            
	            socket.setSoTimeout(UDP_CLIENT_TIMEOUT*20000);
	            socket.receive (packet);
	            	            
	            byte[] data = packet.getData();
	            int len = packet.getLength();	       
	            for (int i =0;i<len;++i){
	            	chars[i] = data[i];	            	
	            }
	            String received = new String(data,0,14);	            
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
	            long ti = System.nanoTime();	            
	            int numBytes = sd.drive(chars,len);
	            prevLen = numBytes;	          
	            
	            outPacket.setLength(numBytes);	            
	            socket.send(outPacket);
	            
	            ti = (System.nanoTime()-ti)/1000000;	            
	            
	            if (maxTime<ti) {
	            	maxTime = ti;
	            	atTime = CircleDriver2.time;
	            }
	            if (ti>=10){
	            	System.out.println("Total time : "+ti+" at "+CircleDriver2.time+" s.");
	            }
	            
	            if (CircleDriver2.debug){
	            	System.out.println("Total time : "+ti);
	            	System.out.println("Max processing time : "+maxTime+" at "+atTime+" s.");
	            }
//	            if (CircleDriver2.time>=CircleDriver2.BREAK_TIME-0.02){
//	            	sd.onShutdown();
//	            	socket.close();
//	            	break;
//	            }
	            //System.out.println(toSend);
	            
	            //System.out.println(i); // + "   " + received);
	            //MessageParser parser = new MessageParser (received);
	            //parser.printAll ();
	            //i++;	            
        	} catch (Exception e){
        		System.out.println("*****************************************");
            	System.out.println("Max processing time : "+maxTime+" at "+atTime+" s.");
        		System.out.println("Error at : "+ CircleDriver2.time );
        		e.printStackTrace();
        		sd.onShutdown();
            	socket.close();
            	System.out.println("Finished with error");
            	System.out.println("Please check at : "+ CircleDriver2.time );
            	System.out.println("*****************************************");
        		System.exit(1);
        	}        	
        }
        System.out.println("Max processing time : "+maxTime+" at "+atTime+" s.");
        System.out.println("Finished");
    }

    
}
