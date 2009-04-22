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
//        Vector2D p = new Vector2D(0,0);
//        Vector2D q1 = new Vector2D(1,1.0);
//        Vector2D q2 = new Vector2D(2,2);
//        double[] xx = new double[]{0,0.001,-0.001};
//        double[] yy = new double[]{0,2,6};
//        LineFitter lf = new LineFitter(new double[]{1,1},xx,yy);
//        lf.fit();        
//    	System.out.println(lf.getA()+"\t"+lf.getB());
//    	System.exit(0);
                
       /* Vector2D v = new Vector2D(0,10);
        Vector2D c = new Vector2D(20,20);
        Vector2D v1 = v.rotated(-0.6).plus(c);
        Vector2D v2 = v.rotated(2.5).plus(c);
        double arc = -Vector2D.angle(v2.minus(c), v1.minus(c));
        System.out.println(arc);
        double x1 = 0;
        double y1 = 0;
        double x2 = 8;
        double y2 = 10;
        TrackSegment ts = TrackSegment.createTurnSeg(0, 20, 20, 10, v2.x, v2.y, v1.x, v1.y, 20, 20);
        
    	ObjectArrayList<TrackSegment> t = new ObjectArrayList<TrackSegment>();
    	t.add(ts);
    	
    	double[] r = Geom.getLineArcIntersection(x1, y1, x2, y2, 20, 20, 10, arc, v1.x, v1.y, v2.x, v2.y);
    	if (r!=null){
	    	TrackSegment ts1 = TrackSegment.createStraightSeg(0, x1, y1, r[0],r[1]);
	    	t.add(ts1);
	    	for (int i=0;i<r.length;++i)
	    		System.out.print(r[i]+"\t");
	    	System.out.println();
    	}
		TrackSegment.drawTrack(t, "Track");
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
		} 
		System.exit(0);//*/
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
