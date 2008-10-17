package solo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.graphbuilder.math.func.Function;

import cern.colt.function.DoubleDoubleProcedure;
import cern.colt.function.DoubleProcedure;
import cern.colt.list.DoubleArrayList;
import cern.jet.math.Functions;
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

    public static void main(String[] args) throws Exception {
        System.out.println("Starting");
//        double[] x =new double[]{-1.5461,-1.1482,-1.5461,-1.5461,-1.4613,-1.4855,-1.5239,-1.5365,-1.169,-1.546,-1.1477,-1.5456,-1.4038,-1.4835,-1.5045,-1.5216,-1.5341,-1.5412,-1.5443,-1.1461,-1.5428,-1.5418,-1.5001,-1.517,-1.5293,-1.1646,-1.5402,-1.1431,-1.5372,-1.5367,-1.3937,-1.4718,-1.4922,-1.5087,-1.5334,-1.5269,-1.5318,-1.1376,-1.5247,-1.5237,-1.435,-1.4776,-1.4933,-1.5194,-1.5098,-1.5161,-1.1273,-1.5073,-1.5053,-1.5018,-1.3844,-1.4303,-1.4484,-1.4623,-1.4854,-1.4747,-1.481,-1.1054,-1.4577,-1.277,-1.432,-1.3801,-1.3893,-1.3929,-1.404,-1.3961,-1.0494,-1.374,-1.3421,-1.3085,-1.2734,-1.1568,-1.2368,-1.1774,-1.1989,-1.1597,-1.129,-1.1194,-0.8523,-0.7904,-0.5024,-0.4941,-0.3867,-0.3145,-0.2172,-0.1199,-0.0073,0.1119,0.3936,1.5069,2.1478,2.3712,2.7928,2.6541,3.0969,3.4584,3.77,4.1265,4.4488,4.8428,5.3653,5.9085,6.3699,7.2473,7.7487,8.4098,9.0297,9.7298,9.9598,10.5361,11.2143,11.8176,12.4938,13.1695,13.8433,14.5142,15.8737,17.3153,18.8389,20.5161,22.3444};
//        double[] y =new double[]{0.0,0.0121,0.0424,0.1545,0.1832,0.1932,0.2213,0.2392,0.1926,0.2613,0.2149,0.3653,0.4247,0.4652,0.4825,0.5021,0.5238,0.5464,0.5664,0.4309,0.7139,0.7833,0.7903,0.8155,0.8425,0.655,0.8892,0.6762,1.06,1.0805,1.0438,1.1258,1.1567,1.1899,1.2316,1.2596,1.2984,0.9753,1.553,1.5849,1.5467,1.6285,1.6738,1.7141,1.769,1.8068,1.3727,2.0326,2.0801,2.1625,2.1285,2.2374,2.2983,2.3641,2.4997,2.5057,2.5844,1.9623,2.9846,2.9707,3.3678,3.3786,3.4845,3.6005,3.7377,3.8357,2.9809,4.097,4.4449,4.7827,5.1114,4.9944,5.4316,5.437,5.744,6.0491,6.2783,6.3476,5.1519,8.3776,8.8195,9.8381,9.979,10.3648,10.8194,11.4048,11.832,10.5745,13.2344,16.4576,17.5955,18.228,19.233,18.2332,19.8895,20.6213,21.3829,22.0365,22.6058,23.277,24.1284,24.9715,25.6557,26.8873,27.5532,28.3942,29.1471,29.9818,30.1828,30.8608,31.6321,32.47,33.2758,34.0808,34.884,35.6832,37.3035,39.0221,40.8379,42.8358,45.0148};
//    	ObjectArrayList<TrackSegment> ts = TrackSegment.createSeg(x,y);
//    	ObjectList<TrackSegment> t = ts.subList(0, 12);
//    	for (TrackSegment tt:t){
//    		System.out.println(tt);
//    	}
//    	System.out.println(ts);
//    	Vector2D p = new Vector2D(1,2);
//    	Vector2D point = TrackSegment.findPointAtDistToPoint(3, p, ts);
//    	if (point!=null) {
//    		System.out.println(point+"    "+point.distance(p));    	
//    		TrackSegment t = TrackSegment.createStraightSeg(0, p.x, p.y, point.x, point.y);
//    		ts.add(t);
//    	}
//		TrackSegment.drawTrack(t, "Track");
//		try {
//			Thread.sleep(2000);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}        
//        
//        JFrame jf = new JFrame();
//    	jf.setMinimumSize(new Dimension(400,500));
//		jf.setPreferredSize(new Dimension(400,500));
//		XYSeries series = new XYSeries("Test");
//		double xx[] = new double[]{1,5,2,-1};
//		double yy[] = new double[]{0.0, 1.2678279194093556, 2.6470888406790465,7};
//		double z[] = new double[3];
//		
//		ControlPath cp = new ControlPath();		
//		cp.addCurve(new BSpline(cp,new GroupIterator("0:n-1",4)));
//		for (int i=0;i<xx.length;++i)
//			cp.addPoint(PointFactory.createPoint(xx[i], yy[i]));
//		
//		ShapeMultiPath mp = new ShapeMultiPath();
//		
//		Curve c = cp.getCurve(0);		
//		c.appendTo(mp);
//		int n = mp.getNumPoints();
//		
//		
//		for (int i=0;i<n;++i){
//			double[] p = mp.get(i);
//			series.add(p[0],p[1]);
////			System.out.println(t+"    "+f.valueAt(t));
//		}
//		
//					
//		XYDataset xyDataset = new XYSeriesCollection(series);
//
//		// Create plot and show it
//		JFreeChart chart = ChartFactory.createScatterPlot("Test", "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );
//		ChartPanel chartPanel = new ChartPanel(chart);
//		jf.setContentPane(chartPanel);
//		jf.setVisible(true);
//        int i = 0;
//        System.out.print("{");
//        for (int i=0;i<19;++i)
//        	System.out.print(i*Math.PI/18+",");
//        System.out.println("}");
//        
        identify();
        BaseDriver sd = new TurnDriver();
        socket.setSoTimeout(UDP_CLIENT_TIMEOUT*20);
        while (true) {
        	try{
	        	byte[] inBuffer = new byte[1024];
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
