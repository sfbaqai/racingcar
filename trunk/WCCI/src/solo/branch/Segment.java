/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.graphbuilder.geom.Geom;

/**
 * @author kokichi3000
 *
 */
public final class Segment {
	private static final int PRECISION_DIGIT = 6;
	private static final double SMALL_MARGIN = 0.002;
	private static final char[] MAX_DOUBLE_STRING = {'1','.','7','9','7','6','9','3','1','3','4','8','6','2','3','1','5','7','E','3','0','8'};
	private static final Segment[] tmpS = new Segment[100];
	private static final Segment[] tmpStore = new Segment[100];
	private static final Segment[] tmpCopy = new Segment[100];
	private static final int[] tmpI = new int[100];
	private static boolean isPrevNextConnected = false;
	private static boolean isNextPrevConnected = false;
	private static boolean isPossiblyConnected = false;	
	private static final Segment tmpPrev = new Segment();
	private static final Segment tmpSeg = new Segment();
//	private static final Segment tmpSeg0 = new Segment();
	private static final Vector2D tmpCenter = new Vector2D();
	private static Vector2D pt = new Vector2D();
	private static final double[] temp = new double[6];	
	private static final double[] rs = new double[3];	
	private static final double E = (CircleDriver2.inTurn) ? 0.1*TrackSegment.EPSILON : TrackSegment.EPSILON*0.1;
	private static final int tmpL = 2002;
//	private static final double[] tmpR = new double [tmpL];
	public static final int[] tmpAppear = new int[tmpL];
	private static Segment p = new Segment();
	private static Segment n = new Segment();
//	private static final int[] tmpStart = new int[tmpL];
	private static final int[] tmpStartIdx = new int[tmpL];
	private static final int[] tmpEndIdx = new int[tmpL];	
	public static final int[] tmpMp = new int[tmpL];
	public static final int[] tmpApp = new int[tmpL];
	public static final int[] tmpMap = new int[tmpL];
//	private static final int[] tmpRads = new int[tmpL];
	private static final int[] tmpAMap = new int[tmpL];
	private static final int[] tmpARads = new int[tmpL];
	private static final int[] tmpCheck = new int[tmpL];
//	private static final int[] tmpOCheck = new int[tmpL];
//	private static final double[] tmpSx = new double[tmpL];
//	private static final double[] tmpSy = new double[tmpL];
//	private static final double[] tmpEx = new double[tmpL];
//	private static final double[] tmpEy = new double[tmpL];
//	private static final double[] tmpCx = new double[tmpL];
//	private static final double[] tmpCy = new double[tmpL];
	private static final double[] tmp1 = new double[6];
	private static final Segment bkupS = new Segment();
//	private static final Segment[] tmpSegP = new Segment[30];
//	private static final Segment[] tmpSegN = new Segment[30];
	//	private static final int[] tmpTp = new int[400];
	//	private static final Vector2D[] tmpCntr = new Vector2D[400];
	//	private static final Segment[] trArr = CircleDriver2.trArr;
	public static int trSz = 0;	
	private static final int int10pow[] = {
		1,10,100,1000,10000,100000,1000000,10000000,100000000,1000000000         
	};

	private static final long long10pow[] = {
		1,
		10,
		100,
		1000,
		10000,
		100000,
		1000000,
		10000000,
		100000000,
		1000000000,
		10000000000L,
		100000000000L,
		1000000000000L,
		10000000000000L,
		100000000000000L,
		1000000000000000L,
		10000000000000000L,
		100000000000000000L,
		1000000000000000000L		
	};

	private static final double PRECISION = 1000000.0d;
//	private static final int LIM = 6;
	public static final int UNKNOWN = -2;
	//	private static final double MARGIN =3;
//	private static final int INIT_SIZE = 20;
	public static final int MAX_RADIUS =1001;
	public static final double REJECT_VALUE = 11;
	public static final double EPSILON = TrackSegment.EPSILON;
	public static final double EPS = TrackSegment.EPSILON*5;
	public static final Segment[] oalArr = new Segment[64];
	private static final Segment[] salArr = new Segment[64];
	static {
		for (int i=tmpS.length-1;i>=0;--i) tmpS[i] = new Segment();
		for (int i=oalArr.length-1;i>=0;--i) {
			Segment t= new Segment();
			Segment os = new Segment();			
			oalArr[i] = t;
			t.opp = os;
			os.opp = t;			
			t.done = true;
			os.done = true;
		}

//		for (int i=tmpSegP.length-1;i>=0;--i) {
//			Segment t= new Segment();
//			Segment os = new Segment();
//			Segment leftSeg = new Segment();
//			Segment rightSeg = new Segment();
//			tmpSegP[i] = t;
//			t.opp = os;
//			os.opp = t;
//			t.leftSeg = leftSeg;
//			t.rightSeg = rightSeg;
//			leftSeg.opp = rightSeg;
//			rightSeg.opp = leftSeg;
//		}
//
//		for (int i=tmpSegN.length-1;i>=0;--i) {
//			Segment t= new Segment();
//			Segment os = new Segment();
//			Segment leftSeg = new Segment();
//			Segment rightSeg = new Segment();
//			tmpSegN[i] = t;
//			t.opp = os;
//			os.opp = t;
//			t.leftSeg = leftSeg;
//			t.rightSeg = rightSeg;
//			leftSeg.opp = rightSeg;
//			rightSeg.opp = leftSeg;
//		}
	}
	//	public static final ObjectArrayList<Segment> oal = ObjectArrayList.wrap(oalArr,0);
	//	public static final ObjectArrayList<Segment> sal = ObjectArrayList.wrap(salArr,0);
	//	private final static int[] ialArr = new int[30]; 
	//	private final static int[] ial1Arr = new int[30];
	//	public static final IntArrayList ial = IntArrayList.wrap(ialArr,0);
	//	public static final IntArrayList ial1 = IntArrayList.wrap(ial1Arr,0);	
	double dist = -1;
	public Vector2D center = null;//estimated center of the segment
	public Vector2D start = null;
	public Vector2D end = null;	
	double length = -1;
	double arc = 0;
	int type=-2;
	double radius = 0;
	int num = 0;	
	boolean done = false;
	Vector2D upper = null;
	Vector2D lower = null;
	//	int[] keys = null;
	public Vector2D[] points = null;// only use when in a straight segment, otherwise null
	public int startIndex = 0;
	public int endIndex = 0;
	boolean sorted = true;	
	public boolean updated = true;
	private final static String TAB = "    ";
	private final static char[] buf = new char[300];
	private static final String[] headers = {"Segment (type = ",TAB+"num = ",TAB+"radius = ",TAB+"length = ",TAB+"start = ",TAB+"end = ",TAB+"center = ",TAB+"lower = ",TAB+"upper =","  )"};	
	private static final char[] tpo = headers[0].toCharArray();
	private static final char[] nm = headers[1].toCharArray();
	private static final char[] rad = headers[2].toCharArray();
	private static final char[] lngth = headers[3].toCharArray();
	private static final char[] strt = headers[4].toCharArray();
	private static final char[] en = headers[5].toCharArray();	
	private static final char[] cnt = headers[6].toCharArray();
	private static final char[] lwer = headers[7].toCharArray();
	private static final char[] uper = headers[8].toCharArray();
	private static final char[] fin = headers[9].toCharArray();
	public Segment opp = null;
	public Segment leftSeg = null;
	public Segment rightSeg = null;
	public int[] map = null;
	public int[] appearedRads = null;
	public int radCount = 0;
	public int[] mapR = null;	
	public int sz = 0;
	public int rsz = 0;
	public boolean unsafe = true;
	private static double[] pr = new double[4];								
	private static int[] pTp = new int[4];				
	private static Vector2D[] pCntr = new Vector2D[4];
	static {
		for (int i = 3;i>=0;--i) pCntr[i] = new Vector2D();
	}
	private static Segment ns = new Segment();
	static {
		System.arraycopy(tpo, 0, buf, 0, tpo.length);
	}


	//	private static final Comparator<Vector2D> comp = new Comparator<Vector2D>(){		
	//		public int compare(Vector2D o1, Vector2D o2) {
	//			return (Math.abs(o1.y-o2.y)<=1e-6) ? 0 : (o1.y<o2.y) ? -1 : 1;
	//		};
	//	};
	/**
	 * @param args
	 */
	
	private final static void arraycopy(Segment[] src,int srcPos,Segment[] dest,int destPos,int length){
		if (src==dest){
			int diff = srcPos - destPos;
			System.arraycopy(dest, destPos, tmpCopy, 0, diff);
			System.arraycopy(src, srcPos, src, destPos, length);
			System.arraycopy(tmpCopy, 0, dest, destPos+length, diff);
		} else System.arraycopy(src, srcPos, dest, destPos, length);
	}

	public void reset(){
		type = Segment.UNKNOWN;		
//		opp = null;
//		leftSeg = null;
//		rightSeg = null;
		unsafe = true;
		lower = null;
		upper = null;
//		map = null;
//		mapR = null;
//		sz = 0;
//		rsz = 0;		
//		radCount = 0;
		done = false;
//		num = 0;
//		points = null;
//		startIndex = 0;
//		endIndex = 0;				
		updated = true;
//		appearedRads = null;
	}
	
	public static final int double2int(double r){		
		return (r>=MAX_RADIUS) ? MAX_RADIUS-1 : (r<=0) ? 0 : (int)(Math.round(r));		
	}

	public static final double int2double(int r){
		return (r==MAX_RADIUS-1) ? Double.MAX_VALUE : r;		
	}

	public Segment(){		
	}

	public Segment(int type,double startX,double startY,double endX,double endY,double centerx,double centery,double rad){
		this.type = type;
		radius = rad;
		startX = Math.round(startX*PRECISION)/PRECISION;
		startY = Math.round(startY*PRECISION)/PRECISION;
		endX = Math.round(endX*PRECISION)/PRECISION;
		endY = Math.round(endY*PRECISION)/PRECISION;
		start = new Vector2D(startX,startY);
		end = new Vector2D(endX,endY);
		map = new int[MAX_RADIUS];
		if (type==0){
			arc = -1;
			double dx = endX-startX;
			double dy = endY - startY;
			length = Math.sqrt(dx*dx+dy*dy);			
			center = null;
			map[MAX_RADIUS-1] = 1;			
		} else if (type!=UNKNOWN){
			int rr = double2int(rad);
			map[rr] = 1;			
			center = new Vector2D(centerx,centery);
			Vector2D v1 = new Vector2D(startX-centerx,startY-centery);
			Vector2D v2 = new Vector2D(endX-centerx,endY-centery);

			double angle = Vector2D.angle(v1, v2);
			if (angle<-Math.PI) 
				angle += 2*Math.PI;
			else if (angle>Math.PI) 
				angle -= 2*Math.PI;

			arc = (angle>=0) ? angle : -angle;
			length = arc * rad;
		}
	}


	public Segment(double startX,double startY,double endX,double endY,double centerx,double centery,double rad,double tW){		
		radius = rad;
		startX = Math.round(startX*PRECISION)/PRECISION;
		startY = Math.round(startY*PRECISION)/PRECISION;
		endX = Math.round(endX*PRECISION)/PRECISION;
		endY = Math.round(endY*PRECISION)/PRECISION;
		start = new Vector2D(startX,startY);
		end = new Vector2D(endX,endY);
		center = new Vector2D(centerx,centery);
		Vector2D v1 = new Vector2D(startX-centerx,startY-centery);
		Vector2D v2 = new Vector2D(endX-centerx,endY-centery);

		double angle = Vector2D.angle(v1, v2);
		if (angle<-Math.PI) 
			angle += 2*Math.PI;
		else if (angle>Math.PI) 
			angle -= 2*Math.PI;
		type = (angle<0) ? -1 : 1;		
		arc = (angle>=0) ? angle : -angle;
		length = arc * rad;		
		map = new int[MAX_RADIUS];
		if (radius<MAX_RADIUS-1){
			if (type!=Segment.UNKNOWN){
				int rr = double2int(radius-type*tW);				
				map[rr] = 1;					
			}
		} else {
			map[MAX_RADIUS-1] = 1;			
		}
	}

	public void copy(double startX,double startY,double endX,double endY,double centerx,double centery,double rad,double tW){		
		radius = rad;
		startX = Math.round(startX*PRECISION)/PRECISION;
		startY = Math.round(startY*PRECISION)/PRECISION;
		endX = Math.round(endX*PRECISION)/PRECISION;
		endY = Math.round(endY*PRECISION)/PRECISION;
		if (start==null) 
			start = new Vector2D(startX,startY);
		else {
			start.x = startX;
			start.y = startY;
		}
		if (end==null) 
			end = new Vector2D(endX,endY);
		else {
			end.x = endX;
			end.y = endY;
		}
		if (center==null) 
			center = new Vector2D(centerx,centery);
		else {
			center.x = centerx;
			center.y = centery;
		}
		Vector2D v1 = new Vector2D(startX-centerx,startY-centery);
		Vector2D v2 = new Vector2D(endX-centerx,endY-centery);

		double angle = Vector2D.angle(v1, v2);
		if (angle<-Math.PI) 
			angle += 2*Math.PI;
		else if (angle>Math.PI) 
			angle -= 2*Math.PI;
		type = (angle<0) ? -1 : 1;		
		arc = (angle>=0) ? angle : -angle;
		length = arc * rad;		
//		if (radius<MAX_RADIUS-1){
//			if (type!=Segment.UNKNOWN){
//				if (map==null) map = new int[MAX_RADIUS];
//				int rr = (int)Math.round(radius-type*tW);								
//				map[rr]++;
//			}
//		} else {
//			int rr = MAX_RADIUS-1;			
//			map[rr]++;			
//		}
	}



	public Segment(TrackSegment ts){
		if (ts==null) return;		
		dist = ts.distanceFromLocalOrigin;
		center = new Vector2D(ts.centerx,ts.centery);
		start = new Vector2D(ts.startX,ts.startY);
		end = new Vector2D(ts.endX,ts.endY);
		length = ts.length;
		type = ts.type;
		arc = ts.arc;
		radius = ts.radius;			
		if (radius<MAX_RADIUS-1){
			if (type!=Segment.UNKNOWN){
				int rr = (int)Math.round(radius);								
				map[rr]++;
			}
		} else {
			int rr = MAX_RADIUS-1;			
			map[rr]++;			
		}
	}

	public Segment(TrackSegment ts,double tW){
		if (ts==null) return;		
		dist = ts.distanceFromLocalOrigin;
		center = new Vector2D(ts.centerx,ts.centery);
		start = new Vector2D(ts.startX,ts.startY);
		end = new Vector2D(ts.endX,ts.endY);
		length = ts.length;
		type = ts.type;
		arc = ts.arc;
		radius = ts.radius;			
		if (radius<MAX_RADIUS-1){
			if (type!=Segment.UNKNOWN){
				int rr = (int)Math.round(radius-type*tW);						
				map[rr]++;
			}
		} else {
			int rr = MAX_RADIUS-1;			
			map[rr]++;			
		}
	}


	public Segment(Segment s){		
		updated = s.updated;
		dist = s.dist;
		center = (s.type==0 || s.type==Segment.UNKNOWN || s.center==null) ? null :new Vector2D(s.center);
		start = (s.start==null) ? null :new Vector2D(s.start);
		end = (s.end==null) ? null :new Vector2D(s.end);
		length = s.length;
		type = s.type;
		arc = s.arc;
		radius = s.radius;		
		num = s.num;
		upper = (s.upper==null) ? null : new Vector2D(s.upper);
		lower = (s.lower==null) ? null : new Vector2D(s.lower);
		startIndex = s.startIndex;
		endIndex = s.endIndex;
		points = s.points;
		radCount = s.radCount;
		appearedRads = s.appearedRads;
		unsafe = s.unsafe;
		//		opp = s.opp;
		//		leftSeg = s.leftSeg;
		//		rightSeg = s.rightSeg;		
		map = s.map;
	}		

	//not copy Points
	public final void copy(Segment s){
		if (this==s) return;
		dist = s.dist;
		if (s.type!=0){
			if (s.center!=null){
				if (center==null) 
					center = new Vector2D(s.center);
				else {
					center.x = s.center.x;
					center.y = s.center.y;
				}
			}
		};
		
		if (start==null) 
			start = new Vector2D(s.start);
		else {
			start.x = s.start.x;
			start.y = s.start.y;
		}
		
		unsafe = s.unsafe;
		if (end==null) 
			end = new Vector2D(s.end);
		else {
			end.x = s.end.x;
			end.y = s.end.y;
		}
			
		if (s.upper!=null){
			if (upper==null) 
				 upper = new Vector2D(s.upper);
			else {
				upper.x = s.upper.x;
				upper.y = s.upper.y;
			}
		} else upper = null;
		
		if (s.lower!=null){
			if (lower==null) 
				lower = new Vector2D(s.lower);
			else {
				lower.x = s.lower.x;
				lower.y = s.lower.y;
			}
		} else lower = null;
		
//		if (s.upper!=null) upper = new Vector2D(s.upper);
//		if (s.lower!=null) lower = new Vector2D(s.lower);
		
//		center = (s.center==null) ? null : new Vector2D(s.center);
//		start = (s.start==null) ? null :new Vector2D(s.start);
//		end = (s.end==null) ? null :new Vector2D(s.end);
		length = s.length;
		type = s.type;
		arc = s.arc;
		radius = s.radius;
		done = s.done;
		num = s.num;
		points = s.points;
		startIndex = s.startIndex;
		endIndex = s.endIndex;				
		updated = s.updated;
		
//		radCount = s.radCount;
//		if (map!=null){
//			for (int i = radCount-1;i>=0;--i){
//				int rr = appearedRads[i]; 
//				map[rr] = 0;
//			}
//		}
//		if (radCount>0 && appearedRads!=null && s.appearedRads!=null && s.appearedRads!=appearedRads) {
//			System.arraycopy(s.appearedRads, 0, appearedRads, 0, radCount);
//			for (int i = radCount-1;i>=0;--i){
//				int rr = appearedRads[i]; 
//				map[rr] = s.map[rr];
//			}
//		}
//		if (opp!=null) opp.radCount = radCount;
//		if (s.map!=null) map = s.map;
	}


	public final void copy(Segment s,double tW){		
		dist = s.dist;
		if (s.type!=0){
			if (s.center!=null){
				if (center==null) 
					center = new Vector2D(s.center);
				else {
					center.x = s.center.x;
					center.y = s.center.y;
				}
			}
		}
		if (start==null) 
			start = new Vector2D(s.start);
		else {
			start.x = s.start.x;
			start.y = s.start.y;
		}
		
		if (end==null) 
			end = new Vector2D(s.end);
		else {
			end.x = s.end.x;
			end.y = s.end.y;
		}
			
		if (s.upper!=null){
			if (upper==null) 
				 upper = new Vector2D(s.upper);
			else {
				upper.x = s.upper.x;
				upper.y = s.upper.y;
			}
		} else upper = null;
		
		if (s.lower!=null){
			if (lower==null) 
				lower = new Vector2D(s.lower);
			else {
				lower.x = s.lower.x;
				lower.y = s.lower.y;
			}
		} else lower = null;
//		center = (s.center==null) ? null :new Vector2D(s.center);
//		start = (s.start==null) ? null :new Vector2D(s.start);
//		end = (s.end==null) ? null :new Vector2D(s.end);
//		if (s.upper!=null) upper = new Vector2D(s.upper);
//		if (s.lower!=null) lower = new Vector2D(s.lower);
		unsafe = s.unsafe;
		length = s.length;
		type = s.type;
		arc = s.arc;
		radius = s.radius;				
		num = s.num;
		points = s.points;
		done = s.done;
		startIndex = s.startIndex;
		endIndex = s.endIndex;				
		updated = s.updated;		
		if (s.map!=null) map = s.map;

		if (type!=Segment.UNKNOWN && map!=null){			
			if (type==0 || radius>=MAX_RADIUS){
				int er = MAX_RADIUS-1;
				if (map[er]==0) 					
					if (appearedRads!=null) appearedRads[radCount++] = er;
				map[er]++;
			} else {
				int er = (int)Math.round(radius-type*tW);
				if (er>=MAX_RADIUS) er = MAX_RADIUS-1;
				if (er<MAX_RADIUS && er>=0 && map[er]==0) {					
					if (appearedRads!=null) appearedRads[radCount++] = er;
				}	
				if (er>=0) map[er]++;
			}
			if (opp!=null) opp.radCount = radCount;
		}
		//		if (s.map!=null && map!=s.map){
		//			if (map==null) map = new int[MAX_RADIUS];
		//			System.arraycopy(s.map, 0, map, 0, map.length);
		//		};		
	}


	public static final void copy(Segment src,Segment dest){
		if (dest==null || dest==src) return;
		src.copy(dest);
		if (dest.opp!=null && src.opp!=null) src.opp.copy(dest.opp);
		if (dest.leftSeg!=null && src.leftSeg!=null) src.leftSeg.copy(dest.leftSeg);
		if (dest.rightSeg!=null && src.rightSeg!=null) src.rightSeg.copy(dest.rightSeg);
	}

	public static final void copy(Segment src,Segment dest,double tW){
		if (dest==null || dest==src) return;
		src.copy(dest,tW);
		if (dest.opp!=null && src.opp!=null) src.opp.copy(dest.opp);
		if (dest.leftSeg!=null && src.leftSeg!=null) src.leftSeg.copy(dest.leftSeg);
		if (dest.rightSeg!=null && src.rightSeg!=null) src.rightSeg.copy(dest.rightSeg);
	}


	public final void copy(TrackSegment ts){		
		if (ts==null) return;
		dist = ts.distanceFromLocalOrigin;
		center = new Vector2D(ts.centerx,ts.centery);
		start = new Vector2D(ts.startX,ts.startY);
		end = new Vector2D(ts.endX,ts.endY);
		length = ts.length;
		type = ts.type;
		arc = ts.arc;
		radius = ts.radius;		

		if (type!=Segment.UNKNOWN && type!=0){
			int rr = (int)Math.round(radius);			
			map[rr]++;
		} else if (type==0){
			int rr = MAX_RADIUS-1;			
			map[rr]++;
		}
	}

	public final void copy(TrackSegment ts,double offset){
		if (ts==null) return;
		dist = ts.distanceFromLocalOrigin;
		center = new Vector2D(ts.centerx,ts.centery);
		start = new Vector2D(ts.startX,ts.startY);
		end = new Vector2D(ts.endX,ts.endY);
		length = ts.length;
		type = ts.type;
		arc = ts.arc;
		radius = ts.radius;		
		//		map[double2int(radius)]++;		
		if (type!=Segment.UNKNOWN){
			int rr = double2int(radius-type*offset);					
			map[rr]++;		
		} else { 
			int rr = MAX_RADIUS-1;			
			map[rr]++;
		}
	}


	/*private static Double2IntMap joinMap(Double2IntMap m1,Double2IntMap m2){
		if (m1==null) return m2;
		if (m2==null) return m1;
		Double2IntMap m = new Double2IntOpenHashMap(m1);

		for (double d:m2.keySet()){
			int n1 = m1.get(d);
			int n2 = m2.get(d);
			if (n1==m1.defaultReturnValue()) n1=0;
			if (n2==m2.defaultReturnValue()) n2=0;
			m.put(d, n1+n2);
		}
		return m;
	}//*/


	//	public final void sortPoints(){
	//		if (!sorted){
	//			Arrays.quicksort(points,0,size-1,comp);
	//			sorted = true;
	//		}
	//	}
	//
	//	private static final void sortPoints(ObjectArrayList<Vector2D> points){		
	//		Arrays.quicksort(points.elements(),0,points.size()-1,comp);		
	//	}



	private static final int binarySearchFromTo(Vector2D[] list, double key, int from, int to) {
		Vector2D midVal = list[from];
		double d = midVal.y-key;
		if (d<0) d = -d;
		int cmp = (d<=SMALL_MARGIN) ? 0 : (midVal.y>key) ? 1 : -1; 
		if (cmp==0){
			if (from<to){
				midVal = list[from+1];
				double de = midVal.y-key;
				if (de<0) de = -de;
				if (de<d) return from+1;
			}
			return from;
		}
		else if (cmp>0) return -from-1;
		midVal = list[to];
		d = midVal.y-key;
		if (d<0) d = -d;
		cmp = (d<=SMALL_MARGIN) ? 0 : (midVal.y>key) ? 1 : -1;
		if (cmp==0) {
			if (from<to){
				midVal = list[to-1];
				double de = midVal.y-key;
				if (de<0) de = -de;
				if (de<d) return to-1;
			}
			return to;
		}
		else if (cmp<0) return -(to+2);
		while (from <= to) {
			int mid =(from + to)/2;
			midVal = list[mid];
			d = midVal.y-key;
			if (d<0) d = -d;
			cmp = (d<=SMALL_MARGIN) ? 0 : (midVal.y>key) ? 1 : -1;

			if (cmp < 0) from = mid + 1;
			else if (cmp > 0) to = mid - 1;
			else {
				if (midVal.y>key){
					if (mid>from){
						midVal = list[mid-1];
						double de = midVal.y-key;
						if (de<0) de = -de;
						if (de<d) return mid-1;
					}					
				} else if (midVal.y<key){
					midVal = list[mid+1];
					double de = midVal.y-key;
					if (de<0) de = -de;
					if (de<d) return mid+1;
				}
				return mid; // key found
			}
		}
		return -(from + 1);  // key not found.
	}



	private static final int binarySearchFromTo(Vector2D[] list, Vector2D key, int from, int to) {
		Vector2D midVal = list[from];
		double d = midVal.y-key.y;
		if (d<0) d = -d;
		int cmp = (d<=SMALL_MARGIN) ? 0 : (midVal.y>key.y) ? 1 : -1; 
		if (cmp==0){
			if (from<to){
				midVal = list[from+1];
				double de = midVal.y-key.y;
				if (de<0) de = -de;
				if (de<d) return from+1;
			}
			return from;
		}
		else if (cmp>0) return -from-1;
		midVal = list[to];
		d = midVal.y-key.y;
		if (d<0) d = -d;
		cmp = (d<=SMALL_MARGIN) ? 0 : (midVal.y>key.y) ? 1 : -1;
		if (cmp==0) {
			if (from<to){
				midVal = list[to-1];
				double de = midVal.y-key.y;
				if (de<0) de = -de;
				if (de<d) return to-1;
			}
			return to;
		}
		else if (cmp<0) return -(to+2);
		while (from <= to) {
			int mid =(from + to)/2;
			midVal = list[mid];
			d = midVal.y-key.y;
			if (d<0) d = -d;
			cmp = (d<=SMALL_MARGIN) ? 0 : (midVal.y>key.y) ? 1 : -1;

			if (cmp < 0) from = mid + 1;
			else if (cmp > 0) to = mid - 1;
			else {
				if (midVal.y>key.y){
					if (mid>from){
						midVal = list[mid-1];
						double de = midVal.y-key.y;
						if (de<0) de = -de;
						if (de<d) return mid-1;
					}					
				} else if (midVal.y<key.y){
					midVal = list[mid+1];
					double de = midVal.y-key.y;
					if (de<0) de = -de;
					if (de<d) return mid+1;
				}
				return mid; // key found
			}
		}
		return -(from + 1);  // key not found.
	}



	//	public final int contains(Vector2D p){
	//		if (!sorted) {						
	//			for (int i=0;i<size;++i)
	//				if (points[i].equals(p)) return i;
	//			return -size-1;
	//		}
	//		return binarySearchFromTo(points, p, 0, num-1);		
	//	}

	/*public final ObjectList<Segment> combine(Segment s){
		if (s.dist<dist) return s.combine(this);
		double e = Math.max(dist+length, s.dist+s.length);		
		double ss = Math.min(dist, s.dist);
//		double l = Math.min(dist+length, s.dist+s.length)-Math.max(dist, s.dist);
		ObjectList<Segment> ol = new ObjectArrayList<Segment>();
		if (s.type==type && s.radius==radius){
			int nr = map.get(radius);			
			map.put(radius, Math.max(nr,s.map.get(radius))+1);
			length = e-ss;
			if (s.dist+s.length>dist+length)
				end = s.end;
			arc = type*length/(radius+0.0d);
			ol.add(this);
			return ol;
		}
		if (s.dist>dist+length) return null;


		if (s.type==type){			
			int nr = map.get(s.radius);
			if (nr==map.defaultReturnValue()) nr = 0;
			map.put(s.radius, ++nr);
			if (Math.abs(s.radius-radius)<MARGIN){//probably the same radius
				dist = ss;
				if (nr>map.get(radius))
					radius = s.radius;													
				length = e-ss;					
				arc = type*length/radius;
				ol.add(this);
				return ol;
			} else if (s.dist+s.length>dist+length){
				Segment t = new Segment();
				t.copy(s);				
				t.length = s.dist+s.length -dist-length;
				t.dist = dist+length;				
				if (t.type!=0) t.arc = t.type * t.length / t.radius;
				this.map = joinMap(this.map, s.map);
				int max =-1;
				double dmax = 0;
				for (double d:this.map.keySet()){
					int n = this.map.get(d);
					if (max<n){
						max = n;
						dmax = d;
					}
				}
				if (dmax!=radius){
					radius = dmax;
					arc = type*length/radius;
				}
				ol.add(this);

				ol.add(t);
				return ol;
			}  
		} else  {		
			double em = Math.min(dist+length, s.dist+s.length);
			length = s.dist-ss;
			dist = ss;
			arc = type*length/radius;
			ol.add(this);
			Segment ns = new Segment();
			ns.copy(this);
			ns.dist = s.dist;
			ns.length = em - s.dist;
			if (ns.type!=0) ns.arc = ns.type * ns.length / ns.radius;

			if (s.type!=0) {
				for (double d:s.map.keySet())
					ns.map.put(-d, s.map.get(d));
			} else ns.map = joinMap(ns.map, s.map);

			int max =-1;
			double dmax = 0;
			for (double d:ns.map.keySet()){
				int n = ns.map.get(d);
				if (max<n){
					max = n;
					dmax = d;
				}
			}
			if (dmax!=radius){
				ns.radius = (dmax<0) ? -dmax : dmax;
				if (dmax<0 || Double.isInfinite(dmax)) ns.type = s.type;
				ns.arc = ns.type*ns.length/ns.radius;
			}

			ol.add(ns);
			return ol;
		}

		return null;
	}*/

	/*public final ObjectList<Segment> mix(Segment s){
		if (start.y>s.end.y || end.y<s.start.y)
			return null;
		Vector2D ss = (start.y<s.start.y) ? start : s.start;
		Vector2D ee = (end.y<s.end.y) ? s.end : end;

		if (Math.abs(radius-s.radius)<0.5 && s.type==type){
			Segment rs = new Segment();
			rs.copy(this);
			rs.start = ss;
			rs.end = ee;
			rs.reCalLength();
			ObjectList<Segment> ol = new ObjectArrayList<Segment>();
			ol.add(rs);
			return ol;
		}
		Double2ObjectRBTreeMap<Vector2D> m = new Double2ObjectRBTreeMap<Vector2D>();
		m.put(start.y,start);
		m.put(s.start.y,s.start);
		m.put(end.y,end);
		m.put(s.end.y,s.end);
		ObjectCollection<Vector2D> tmp = m.values();
		Vector2D[] v = new Vector2D[4];
		tmp.toArray(v);
		ObjectList<Segment> ol = new ObjectArrayList<Segment>();
		Segment rs = new Segment();
		if (start.y<s.start.y)			
			rs.copy(this);			
		else rs.copy(s);		
		rs.start = v[0];
		rs.end = v[1];
		rs.reCalLength();
		ol.add(rs);
		rs = new Segment();
		rs.copy(this);
		rs.start = v[1];
		rs.end = v[2];
		rs.reCalLength();
		rs.map.put(s.radius, 1);
		ol.add(rs);
		rs = new Segment();
		if (start.y<s.start.y)			
			rs.copy(s);			
		else rs.copy(this);
		rs.start = v[2];
		rs.end = v[3];
		rs.reCalLength();		
		ol.add(rs);
		return ol;
	}//*/

	/*public final Segment combine(TrackSegment s){
		if (s==null) return this;
		double e = Math.max(dist+length, s.distanceFromLocalOrigin+s.length);		
		double ss = Math.min(dist, s.distanceFromLocalOrigin);
		double l = Math.min(dist+length, s.distanceFromLocalOrigin+s.length)-Math.max(dist, s.distanceFromLocalOrigin);
		int rr = (int)Math.round(s.radius);
		if (s.type==type && rr==radius){
			int nr = map.get(radius);
			if (nr==map.defaultReturnValue()) nr = 0;
			nr++;
			map.put(radius, nr);
			arc = type*(e-ss)/(radius+0.0d);
			if (s.distanceFromLocalOrigin+s.length>=dist+length)
				end = center.plus(start.minus(center).rotated(-arc));
			length = e-ss;
			dist = ss;


			return this;
		}

		if (s.distanceFromLocalOrigin+s.length<dist){
			dist = s.distanceFromLocalOrigin;
			length = s.length;
			radius = rr;
			arc = s.arc;
			start = new Vector2D(s.startX,s.startY);
			center = new Vector2D(s.centerx,s.centery);
			end = new Vector2D(s.endX,s.endY);
			map.clear();
			map.put(radius, 1);
			return this;
		}
//		if (s.distanceFromLocalOrigin>dist+length)
//		return this;

		else if ((s.distanceFromLocalOrigin+s.length<dist+length || l/length>=0.2) && s.type==type){
//			if (Math.abs(s.radius-radius)<5){//probably the same radius
			int nr = map.get(rr);
			if (nr==map.defaultReturnValue()) nr = 0;
			nr++;
			map.put(rr, nr);
			if (nr>map.get(radius)){	
				dist = s.distanceFromLocalOrigin;
				radius = rr;
				Vector2D p = start.plus(end).times(0.5d);
//				length = e-ss;
				length = s.length;
				center = p.plus(center.minus(p).normalized().times(radius));
				arc = type*length/radius;
			}
//			}
		} else if (s.distanceFromLocalOrigin+s.length<dist+length && s.type!=type){
			return this;
		}

		return null;
	}//*/


	/*public final void removePoint(Vector2D p){
		if (num<=0 || points==null) return;

		int index = binarySearchFromTo(points, p, 0, num-1);
		if (index<0) return;		

		if (index>=0){			
			System.arraycopy(points, index+1, points, index, num-index);			
			num--;


			if (num<3){
				type = -2;
				length = -1;
				arc = 0;
				radius = 0;				
			}
			boolean changed = false;
			if (type!=0){
				if (p.distance(end)<0.1) {
					if (!sorted) sortPoints();
					end = (points==null || num==0) ? null : new Vector2D(points[num-1]);
					changed = true;
				}
				if (p.distance(start)<0.1) {
					if (!sorted) sortPoints();
					start = (points==null || num==0) ? null :new Vector2D(points[0]);
					changed = true;
				}
			} else if (p.distance(end)<0.1 || p.distance(start)<0.1){
				if (!sorted) sortPoints();
				if (points==null || num==0){
					if (start!=null && end!=null && start.distance(end)==0){
						start = null;
						end = null;
					} 
					if (start!=null && p.distance(start)<0.1){
						start = (end==null) ? null : new Vector2D(end);
					}
					if (end!=null && p.distance(end)<0.1)
						end = (start==null) ? null : new Vector2D(start);					
					return;
				}
				changed = true;
				double de = p.distance(end);
				double dy = end.y-start.y;
				double dx = end.x-start.x;
				Vector2D n = new Vector2D(dx,dy).orthogonal();
				Vector2D point = (de<0.1) ? new Vector2D(points[num-1]) : new Vector2D(points[0]);
				double[] r = new double[3];
				if (Math.abs(dx)<=TrackSegment.EPSILON)					
					Geom.getLineLineIntersection(start.x, start.y, end.x, end.y, point.x, point.y, point.x-1, point.y, r);					
				else Geom.getLineLineIntersection(start.x, start.y, end.x, end.y, point.x, point.y, point.x+n.x, point.y+n.y, r);					

				if (r!=null && r.length>2) {
					point = new Vector2D(r[0],r[1]);
					if (de<0.1) 
						end = point;
					else start = point;
				}

			}
			if (changed) reCalLength();
		}
	}//*/


	public final void reCalLength(){
		if (type==UNKNOWN) return;
		if (start.equals(end)){
			length = 0;
			return;
		}
		if (type==0 && start!=null && end!=null){
			length = end.distance(start.x, start.y);
		} else if (type!=Segment.UNKNOWN && start!=null && end!=null && center!=null){
			Vector2D v1 = new Vector2D(start.x-center.x,start.y-center.y);
			Vector2D v2 = new Vector2D(end.x-center.x,end.y-center.y);

			double angle = Vector2D.angle(v1, v2);
			if (angle<-Math.PI) 
				angle += 2*Math.PI;
			else if (angle>Math.PI) 
				angle -= 2*Math.PI;

			arc = (angle>=0) ? angle : -angle;			
			length = angle * radius;
		}

	}

	//Point must belong to seg
	/*public final void addPoint(Vector2D p){		
		if (num<=0 || points==null){
			points = new Vector2D[INIT_SIZE];			
		} else if (num>=points.length){
			Vector2D[] tmp = new Vector2D[num*2];
			System.arraycopy(points, 0, tmp, 0, points.length);
			points = tmp;			
		}

		Vector2D point = new Vector2D(p);
		if (num>=LIM && p.y>=start.y && p.y<=end.y) return;
		if (num>=LIM && p.y<start.y){
			start = new Vector2D(p);
			start.x = Math.round(start.x*PRECISION)/PRECISION;
			start.y = Math.round(start.y*PRECISION)/PRECISION;
			points[0] = point;
			return;
		}

		if (num>=LIM && p.y>end.y){
			end = new Vector2D(p);
			end.x = Math.round(end.x*PRECISION)/PRECISION;
			end.y = Math.round(end.y*PRECISION)/PRECISION;
			points[num-1] = point;
			return;
		}

		int index = binarySearchFromTo(points, p, 0, Math.min(num-1,LIM-1));
		if (index>=0) return;
		index = -index - 1;
		if (index>=num) {
			points[num] = point ;
		} else {
			System.arraycopy(points, index, points, index+1, num-index);
			points[index] = point;
		}
		num++;

		boolean changed = false;
		if (start==null){
			start = new Vector2D(p);
		} else if (p.y<start.y){
			changed = true;
			if (type!=0)
				start = new Vector2D(p);
			else {
				double dy = end.y-start.y;
				double dx = end.x-start.x;										
				if (Math.abs(dx)<=TrackSegment.EPSILON){					
					point = p;					
				} else if (points!=null){
					double a = dy/dx;
					double b = end.y-a*end.x;
					QuickLineFitter lf = new QuickLineFitter(new double[]{a,b},points,0,num);
					lf.fit();
					a = lf.getA();
					b = lf.getB();
					point = p;
					point.x = (p.y-b)/a;
				}

				if (point!=null) {
					start = new Vector2D(point);
					start.x = Math.round(start.x*PRECISION)/PRECISION;
					start.y = Math.round(start.y*PRECISION)/PRECISION;
					if (Math.abs(dx)<=TrackSegment.EPSILON)
						end.x = start.x;									
				}
			}

		}

		if (end==null){
			end = new Vector2D(p);
		} else if (p.y>end.y){
			changed = true;
			if (type!=0)
				end = new Vector2D(p);
			else {
				double dy = end.y-start.y;
				double dx = end.x-start.x;						
				if (Math.abs(dx)<=TrackSegment.EPSILON)	{					
					end.x = start.x;
					point = p;					
				} else if (points!=null){
					double a = dy/dx;
					double b = end.y-a*end.x;
					QuickLineFitter lf = new QuickLineFitter(new double[]{a,b},points,0,num);
					lf.fit();
					a = lf.getA();
					b = lf.getB();
					point = p;
					point.x = (p.y-b)/a;
				}					

				if (point!=null){
					end = new Vector2D(point);
					end.x = Math.round(end.x*PRECISION)/PRECISION;
					end.y = Math.round(end.y*PRECISION)/PRECISION;
					if (Math.abs(dx)<=TrackSegment.EPSILON)
						end.x = start.x;					
				}
			}
		}

		if (changed){
			//			if (type==0) points.add(p); 
			reCalLength();
		}		
	}//*/

	/*public boolean isPointInSegment(Vector2D point){		
		//		double minx = Math.min(start.x, end.x);
		double miny = Math.min(start.y, end.y);
		//		double maxx = Math.max(start.x, end.x);
		double maxy = Math.max(start.y, end.y);
		//		return (point.x>=minx && point.x<=maxx && point.y>=miny && point.y<=maxy);
		return (point.y>=miny && point.y<=maxy);
	}//*/

	public final boolean isPointBelongToSeg(Vector2D point){
		if (type==Segment.UNKNOWN) return true;

		if (type==0){			
			//			if (isCirle && Math.sqrt(r[2])<TrackSegment.MAXRADIUS) return false;
			double dx = end.x - start.x;
			if (Math.abs(dx)<=EPSILON*0.5)
				if (point.y<=end.y && point.y>=start.y) return true;

			double d = Geom.ptLineDistSq(start.x, start.y, end.x, end.y, point.x, point.y, null); 
			if (d>=4*TrackSegment.EPSILON*TrackSegment.EPSILON) return false;
		} else {
			double dx = point.x - center.x;
			double dy = point.y - center.y;
			double dd =  Math.sqrt(dx*dx+dy*dy);
			double d = dd-radius;
			if (d<0) d = -d;
			if (d>=EPSILON*0.5) return false;
		}
		return true;
	}

	public static final Segment toMiddleSegment(Segment seg,int which,double tW){
		if (seg.type==Segment.UNKNOWN) return null;
		double t = (seg.type==0) ? -tW*which : tW*which*seg.type;
		//		Segment s = (seg.seg!=null) ? new Segment(seg.seg,-which*tW) : new Segment();
		Segment s = new Segment(seg);
//		s.copy(seg);
		Vector2D first = seg.start;
		Vector2D last = seg.end;
		if (seg.type==0 && Math.abs(s.start.x-s.end.x)<=TrackSegment.EPSILON){									
			s.start.x += t;
			s.end.x += t;
			s.start.x = Math.round(s.start.x*PRECISION)/PRECISION;			
			s.end.x = Math.round(s.end.x*PRECISION)/PRECISION;
			s.start.x = Math.round(s.start.x*PRECISION)/PRECISION;
			s.start.y = Math.round(s.start.y*PRECISION)/PRECISION;
			s.end.x = Math.round(s.end.x*PRECISION)/PRECISION;
			s.end.y = Math.round(s.end.y*PRECISION)/PRECISION;
			if (s.upper!=null) {
				s.upper.x += t;
				s.upper.x = Math.round(s.upper.x*PRECISION)/PRECISION;
				s.upper.y = Math.round(s.upper.y*PRECISION)/PRECISION;
			}
			if (s.lower!=null) {
				s.lower.x += t;
				s.lower.x = Math.round(s.lower.x*PRECISION)/PRECISION;
				s.lower.y = Math.round(s.lower.y*PRECISION)/PRECISION;
			}
			s.dist = seg.dist;			
			s.points = null;
		} else if (seg.type==0){
			double sx = s.start.x;
			double sy = s.start.y;
			double ex = s.end.x;
			double ey = s.end.y;
			double nx = sy-ey;
			double ny = ex-sx;
			double d = -t/Math.sqrt(nx*nx+ny*ny);
			nx*=d;
			ny*=d;						
			s.start.x = Math.round((sx+nx)*PRECISION)/PRECISION;
			s.start.y = Math.round((sy+ny)*PRECISION)/PRECISION;
			s.end.x = Math.round((ex+nx)*PRECISION)/PRECISION;
			s.end.y = Math.round((ey+ny)*PRECISION)/PRECISION;
			if (s.upper!=null) {				
				s.upper.x = Math.round((s.upper.x+nx)*PRECISION)/PRECISION;
				s.upper.y = Math.round((s.upper.y+ny)*PRECISION)/PRECISION;
			}
			if (s.lower!=null) {				
				s.lower.x = Math.round((s.lower.x+nx)*PRECISION)/PRECISION;
				s.lower.y = Math.round((s.lower.y+ny)*PRECISION)/PRECISION;
			}	
		} else if (s.center!=null){
			double rad = seg.radius + t;
			Vector2D center = s.center;			
			double ox = center.x;
			double oy = center.y;
			double d = rad/seg.radius;
			if (oy!=0){
				double cx = center.x;
				double cy = center.y;
				ox = (first.x+last.x)*0.5;
				oy = (first.y+last.y)*0.5;
				double dx = ox - first.x;
				double dy = oy - first.y;
				double dd = dx*dx+dy*dy;

				double nx = -dy;
				double ny = dx;		
				double dn = nx*nx+ny*ny;

				cx -= ox;
				cy -= oy;
				if (nx*cx+ny*cy<0) {
					nx = -nx;
					ny = -ny;
				}
				double r = seg.radius;
				if (r*r<dd){
					s.type = Segment.UNKNOWN;
					seg.type= Segment.UNKNOWN;
					return s;
				}
				double dt = Math.sqrt((r*r-dd)/dn);
				ox += nx * dt;
				oy += ny * dt;									
			}			//			s.start.x = Math.round(s.start.x*PRECISION)/PRECISION;
			//			s.start.y = Math.round(s.start.y*PRECISION)/PRECISION;
			//			s.end.x = Math.round(s.end.x*PRECISION)/PRECISION;
			//			s.end.y = Math.round(s.end.y*PRECISION)/PRECISION;
			double nsx = first.x - ox;
			double nsy = first.y - oy;
			double nex = last.x - ox;
			double ney = last.y - oy;										
			if (s.start==null) 
				s.start = new Vector2D(ox+nsx*d,oy+nsy*d);
			else s.start.copy(ox+nsx*d,oy+nsy*d);
			if (s.end==null)
				s.end = new Vector2D(ox+nex*d,oy+ney*d);
			else s.end.copy(ox+nex*d,oy+ney*d);
			center.x = ox;
			center.y = oy;

			s.radius = rad;
			s.arc = arc(s.start,s.end,center,rad);
			s.length = s.arc * rad;
			s.dist = seg.dist;			
			if (s.upper!=null) {
				double ux = s.upper.x - ox;
				double uy = s.upper.y - oy;
				s.upper = new Vector2D(ox+ux*d,oy+uy*d);
				s.upper.x = Math.round(s.upper.x*PRECISION)/PRECISION;
				s.upper.y = Math.round(s.upper.y*PRECISION)/PRECISION;
			}
			if (s.lower!=null) {
				double lx = s.lower.x - ox;
				double ly = s.lower.y - oy;
				s.lower = new Vector2D(ox+lx*d,oy+ly*d);
				s.lower.x = Math.round(s.lower.x*PRECISION)/PRECISION;
				s.lower.y = Math.round(s.lower.y*PRECISION)/PRECISION;
			}
			s.points = null;
		}

		return s;
	}

	public static final Segment toMiddleSegment(Segment seg,Segment s,int which,double tW){
		if (seg.type==Segment.UNKNOWN) return null;
		double t = (seg.type==0) ? -tW*which : tW*which*seg.type;
		//		Segment s = (seg.seg!=null) ? new Segment(seg.seg,-which*tW) : new Segment();	
		Vector2D first = seg.start;
		if (seg.type==0) s.center = null;
		s.type = seg.type;
		s.unsafe = seg.unsafe;
		if (s.start==null) s.start = new Vector2D();
		if (s.end==null) s.end = new Vector2D();
		if (seg.upper==null) 
			s.upper = null;
		else if (seg.upper!=null && s.upper==null) s.upper = new Vector2D();
		if (seg.lower==null){
			s.lower = null;
		} else if (seg.lower!=null && s.lower==null) s.lower = new Vector2D();
		if (seg.center==null)
			s.center = null;
		else if (seg.type!=0 && s.center==null) s.center = new Vector2D();
		Vector2D last = seg.end;
		if (seg.type==0  && Math.abs(seg.start.x-seg.end.x)<=TrackSegment.EPSILON){
			seg.radius = 0;
			s.radius = 0;
			s.start.x = seg.start.x + t;
			s.end.x = seg.end.x + t;
			s.start.x = Math.round(s.start.x*PRECISION)/PRECISION;
			s.start.y = Math.round(seg.start.y*PRECISION)/PRECISION;
			s.end.x = Math.round(s.end.x*PRECISION)/PRECISION;
			s.end.y = Math.round(seg.end.y*PRECISION)/PRECISION;
			s.dist = seg.dist;
			s.points = null;
			if (seg.upper!=null) {
				s.upper.x = seg.upper.x+ t;
				s.upper.x = Math.round(s.upper.x*PRECISION)/PRECISION;
				s.upper.y = Math.round(seg.upper.y*PRECISION)/PRECISION;
			}
			if (seg.lower!=null) {
				s.lower.x = seg.lower.x + t;
				s.lower.x = Math.round(s.lower.x*PRECISION)/PRECISION;
				s.lower.y = Math.round(seg.lower.y*PRECISION)/PRECISION;
			}
		}  else if (seg.type==0){
			seg.radius = 0;
			s.radius = 0;
			double sx = seg.start.x;
			double sy = seg.start.y;
			double ex = seg.end.x;
			double ey = seg.end.y;
			double nx = sy-ey;
			double ny = ex-sx;
			double d = -t/Math.sqrt(nx*nx+ny*ny);
			nx*=d;
			ny*=d;						
			s.start.x = Math.round((sx+nx)*PRECISION)/PRECISION;
			s.start.y = Math.round((sy+ny)*PRECISION)/PRECISION;
			s.end.x = Math.round((ex+nx)*PRECISION)/PRECISION;
			s.end.y = Math.round((ey+ny)*PRECISION)/PRECISION;
			if (seg.upper!=null) {				
				s.upper.x = Math.round((seg.upper.x+nx)*PRECISION)/PRECISION;
				s.upper.y = Math.round((seg.upper.y+ny)*PRECISION)/PRECISION;
			}
			if (seg.lower!=null) {				
				s.lower.x = Math.round((seg.lower.x+nx)*PRECISION)/PRECISION;
				s.lower.y = Math.round((seg.lower.y+ny)*PRECISION)/PRECISION;
			}
		} else if (seg.center!=null){
			double rad = seg.radius + t;
			Vector2D center = seg.center;
			double ox = center.x;
			double oy = center.y;
			double d = rad/seg.radius;
			if (oy!=0){
				double cx = center.x;
				double cy = center.y;
				ox = (first.x+last.x)*0.5;
				oy = (first.y+last.y)*0.5;
				double dx = ox - first.x;
				double dy = oy - first.y;
				double dd = dx*dx+dy*dy;

				double nx = -dy;
				double ny = dx;		
				double dn = nx*nx+ny*ny;

				cx -= ox;
				cy -= oy;
				if (nx*cx+ny*cy<0) {
					nx = -nx;
					ny = -ny;
				}
				double r = seg.radius;
				if (r*r<dd){
					s.type = Segment.UNKNOWN;
					seg.type= Segment.UNKNOWN;
					return s;
				}
				double dt = Math.sqrt((r*r-dd)/dn);
				ox += nx * dt;
				oy += ny * dt;					

			}
			double nsx = first.x - ox;
			double nsy = first.y - oy;
			double nex = last.x - ox;
			double ney = last.y - oy;										
			s.start.x = ox+nsx*d;
			s.start.y = oy+nsy*d;
			s.end.x = ox+nex*d;
			s.end.y = oy+ney*d;	
			s.center.x = ox;
			s.center.y = oy;
			//			s.start.x = Math.round(s.start.x*PRECISION)/PRECISION;
			//			s.start.y = Math.round(s.start.y*PRECISION)/PRECISION;
			//			s.end.x = Math.round(s.end.x*PRECISION)/PRECISION;
			//			s.end.y = Math.round(s.end.y*PRECISION)/PRECISION;
			if (seg.upper!=null) {
				double ux = seg.upper.x - ox;
				double uy = seg.upper.y - oy;
				s.upper.x = ox+ux*d;
				s.upper.y = oy+uy*d;
				s.upper.x = Math.round(s.upper.x*PRECISION)/PRECISION;
				s.upper.y = Math.round(s.upper.y*PRECISION)/PRECISION;
			}
			if (seg.lower!=null) {
				double lx = seg.lower.x - ox;
				double ly = seg.lower.y - oy;
				s.lower.x = ox+lx*d;
				s.lower.y = oy+ly*d;
				s.lower.x = Math.round(s.lower.x*PRECISION)/PRECISION;
				s.lower.y = Math.round(s.lower.y*PRECISION)/PRECISION;
			}
			s.radius = rad;
			s.arc = arc(s.start,s.end,center,rad);
			s.length = s.arc * rad;
			s.dist = seg.dist;			
			s.points = null;
		}
		return s;
	}


	public static final Segment toSideSegment(Segment seg,int which,double tW){
		if (seg!=null && seg.type==Segment.UNKNOWN) return null;
		double t = (seg.type==0) ? tW*which : -tW*which*seg.type;
		//		Segment s = (seg.seg!=null) ? new Segment(seg) : new Segment();
		Segment s = new Segment(seg);
//		s.copy(seg);
		Vector2D first = seg.start;
		Vector2D last = seg.end;
		if (seg.type==0  && Math.abs(s.start.x-s.end.x)<=TrackSegment.EPSILON){									
			s.start.x += t;
			s.end.x += t;
			s.start.x = Math.round(s.start.x*PRECISION)/PRECISION;
			s.start.y = Math.round(s.start.y*PRECISION)/PRECISION;
			s.end.x = Math.round(s.end.x*PRECISION)/PRECISION;
			s.end.y = Math.round(s.end.y*PRECISION)/PRECISION;
			s.dist = seg.dist;
			s.points = null;
			if (s.upper!=null) {
				s.upper.x += t;
				s.upper.x = Math.round(s.upper.x*PRECISION)/PRECISION;
				s.upper.y = Math.round(s.upper.y*PRECISION)/PRECISION;
			}
			if (s.lower!=null) {
				s.lower.x += t;
				s.lower.x = Math.round(s.lower.x*PRECISION)/PRECISION;
				s.lower.y = Math.round(s.lower.y*PRECISION)/PRECISION;
			}
		}  else if (seg.type==0){
			double sx = s.start.x;
			double sy = s.start.y;
			double ex = s.end.x;
			double ey = s.end.y;
			double nx = sy-ey;
			double ny = ex-sx;
			double d = -t/Math.sqrt(nx*nx+ny*ny);
			nx*=d;
			ny*=d;						
			s.start.x = Math.round((sx+nx)*PRECISION)/PRECISION;
			s.start.y = Math.round((sy+ny)*PRECISION)/PRECISION;
			s.end.x = Math.round((ex+nx)*PRECISION)/PRECISION;
			s.end.y = Math.round((ey+ny)*PRECISION)/PRECISION;
			if (s.upper!=null) {				
				s.upper.x = Math.round((s.upper.x+nx)*PRECISION)/PRECISION;
				s.upper.y = Math.round((s.upper.y+ny)*PRECISION)/PRECISION;
			}
			if (s.lower!=null) {				
				s.lower.x = Math.round((s.lower.x+nx)*PRECISION)/PRECISION;
				s.lower.y = Math.round((s.lower.y+ny)*PRECISION)/PRECISION;
			}
		} else if (s.center!=null){
			double rad = seg.radius + t;
			Vector2D center = s.center;
			double ox = center.x;
			double oy = center.y;
			double d = rad/seg.radius;
			if (oy!=0){
				double cx = center.x;
				double cy = center.y;
				ox = (first.x+last.x)*0.5;
				oy = (first.y+last.y)*0.5;
				double dx = ox - first.x;
				double dy = oy - first.y;
				double dd = dx*dx+dy*dy;

				double nx = -dy;
				double ny = dx;		
				double dn = nx*nx+ny*ny;

				cx -= ox;
				cy -= oy;
				if (nx*cx+ny*cy<0) {
					nx = -nx;
					ny = -ny;
				}
				double r = seg.radius;
				if (r*r<dd){
					s.type = Segment.UNKNOWN;
					seg.type= Segment.UNKNOWN;
					return s;
				}
				double dt = Math.sqrt((r*r-dd)/dn);
				ox += nx * dt;
				oy += ny * dt;					

			}
			double nsx = first.x - ox;
			double nsy = first.y - oy;
			double nex = last.x - ox;
			double ney = last.y - oy;		
			if (s.start==null) 
				s.start = new Vector2D(ox+nsx*d,oy+nsy*d);
			else s.start.copy(ox+nsx*d,oy+nsy*d);
			if (s.end==null)
				s.end = new Vector2D(ox+nex*d,oy+ney*d);
			else s.end.copy(ox+nex*d,oy+ney*d);			
			center.x = ox;
			center.y = oy;
			//			s.start.x = Math.round(s.start.x*PRECISION)/PRECISION;
			//			s.start.y = Math.round(s.start.y*PRECISION)/PRECISION;
			//			s.end.x = Math.round(s.end.x*PRECISION)/PRECISION;
			//			s.end.y = Math.round(s.end.y*PRECISION)/PRECISION;
			if (s.upper!=null) {
				double ux = s.upper.x - ox;
				double uy = s.upper.y - oy;
				s.upper = new Vector2D(ox+ux*d,oy+uy*d);
				s.upper.x = Math.round(s.upper.x*PRECISION)/PRECISION;
				s.upper.y = Math.round(s.upper.y*PRECISION)/PRECISION;
			}
			if (s.lower!=null) {
				double lx = s.lower.x - ox;
				double ly = s.lower.y - oy;
				s.lower = new Vector2D(ox+lx*d,oy+ly*d);
				s.lower.x = Math.round(s.lower.x*PRECISION)/PRECISION;
				s.lower.y = Math.round(s.lower.y*PRECISION)/PRECISION;
			}
			s.radius = rad;
			s.arc = arc(s.start,s.end,center,rad);
			s.length = s.arc * rad;
			s.dist = seg.dist;			
			s.points = null;
			//			if (s.points!=null){
			//				for (int i = 0;i<s.num;++i){					
			//					if (s.points[i]!=null) s.points[i]=s.center.plus(s.points[i].minus(s.center).normalised().times(rad));					
			//				}
			//			}
			//			if (t>0){
			//				for (int i = s.maxR;i>=s.minR;--i){
			//					int n = s.map[i];					
			//					if (i!=MAX_RADIUS-1 && n!=0) {
			//						s.map[(int)(Math.round(i+t))] = n;
			//						s.map[i] = 0;
			//					}
			//				}
			//			} else {
			//				for (int i = s.minR	;i<=s.maxR;++i){
			//					int n = s.map[i]; 
			//					if (i!=MAX_RADIUS-1 &&n!=0) {
			//						s.map[(int)(Math.round(i+t))] = n;
			//						s.map[i] = 0;
			//					}
			//				}
			//			}			
			//			s.minR = (int)Math.round(s.minR+t);
			//			s.maxR = (int)Math.round(s.maxR+t);
		}
		return s;
	}
	
	public static final void mapPoint(Vector2D p,Segment seg ,int which,double tW,Vector2D q){
		double t = (seg.type==0) ? 2*tW*which : -2*tW*which*seg.type;
		if (seg.type==0 && Math.abs(seg.start.x-seg.end.x)<=TrackSegment.EPSILON){
			q.x = p.x+t;
			q.y = p.y;
		} else if (seg.type==0){
			double sx = seg.start.x;
			double sy = seg.start.y;
			double ex = seg.end.x;
			double ey = seg.end.y;
			double nx = sy-ey;
			double ny = ex-sx;
			double d = -t/Math.sqrt(nx*nx+ny*ny);
			nx*=d;
			ny*=d;
			q.x = p.x+nx;
			q.y = p.y+ny;
		} else {
			double rad = seg.radius + t;
			Vector2D center = seg.center;
			double ox = center.x;
			double oy = center.y;
			double d = rad/seg.radius;
			Vector2D first = seg.start;
			Vector2D last = seg.end;
			if (oy!=0){
				double cx = center.x;
				double cy = center.y;
				ox = (first.x+last.x)*0.5;
				oy = (first.y+last.y)*0.5;
				double dx = ox - first.x;
				double dy = oy - first.y;
				double dd = dx*dx+dy*dy;

				double nx = -dy;
				double ny = dx;		
				double dn = nx*nx+ny*ny;

				cx -= ox;
				cy -= oy;
				if (nx*cx+ny*cy<0) {
					nx = -nx;
					ny = -ny;
				}
				double r = seg.radius;				
				double dt = Math.sqrt((r*r-dd)/dn);
				ox += nx * dt;
				oy += ny * dt;					

			}
			double nsx = p.x - ox;
			double nsy = p.y - oy;				
			q.x = ox+nsx*d;
			q.y = oy+nsy*d;
		}
	}

	public static final void toSideSegment(Segment seg,Segment s,int which,double tW){
		if (seg!=null && seg.type==Segment.UNKNOWN) return;
		double t = (seg.type==0) ? tW*which : -tW*which*seg.type;
		//		Segment s = (seg.seg!=null) ? new Segment(seg) : new Segment();				
		Vector2D first = seg.start;
		if (seg.type==0) s.center = null;
		s.type = seg.type;
		if (s.start==null) s.start = new Vector2D();
		if (s.end==null) s.end = new Vector2D();
		if (seg.upper==null) 
			s.upper = null;
		else if (seg.upper!=null && s.upper==null) s.upper = new Vector2D();
		if (seg.lower==null){
			s.lower = null;
		} else if (seg.lower!=null && s.lower==null) s.lower = new Vector2D();
		if (seg.center==null)
			s.center = null;
		else if (seg.type!=0 && s.center==null) s.center = new Vector2D();			
		Vector2D last = seg.end;
		if (seg.type==0  && Math.abs(seg.start.x-seg.end.x)<=TrackSegment.EPSILON){
			seg.radius = 0;
			s.radius = 0;
			s.start.x = seg.start.x + t;
			s.end.x = seg.end.x + t;
			s.start.x = Math.round(s.start.x*PRECISION)/PRECISION;
			s.start.y = Math.round(seg.start.y*PRECISION)/PRECISION;
			s.end.x = Math.round(s.end.x*PRECISION)/PRECISION;
			s.end.y = Math.round(seg.end.y*PRECISION)/PRECISION;
			s.dist = seg.dist;
			s.points = null;
			if (seg.upper!=null) {
				s.upper.x = seg.upper.x+ t;
				s.upper.x = Math.round(s.upper.x*PRECISION)/PRECISION;
				s.upper.y = Math.round(seg.upper.y*PRECISION)/PRECISION;
			}
			if (seg.lower!=null) {
				s.lower.x = seg.lower.x + t;
				s.lower.x = Math.round(s.lower.x*PRECISION)/PRECISION;
				s.lower.y = Math.round(seg.lower.y*PRECISION)/PRECISION;
			}
		}  else if (seg.type==0){
			seg.radius = 0;
			s.radius = 0;
			double sx = seg.start.x;
			double sy = seg.start.y;
			double ex = seg.end.x;
			double ey = seg.end.y;
			double nx = sy-ey;
			double ny = ex-sx;
			double d = -t/Math.sqrt(nx*nx+ny*ny);
			nx*=d;
			ny*=d;						
			s.start.x = Math.round((sx+nx)*PRECISION)/PRECISION;
			s.start.y = Math.round((sy+ny)*PRECISION)/PRECISION;
			s.end.x = Math.round((ex+nx)*PRECISION)/PRECISION;
			s.end.y = Math.round((ey+ny)*PRECISION)/PRECISION;
			if (seg.upper!=null) {				
				s.upper.x = Math.round((seg.upper.x+nx)*PRECISION)/PRECISION;
				s.upper.y = Math.round((seg.upper.y+ny)*PRECISION)/PRECISION;
			}
			if (seg.lower!=null) {				
				s.lower.x = Math.round((seg.lower.x+nx)*PRECISION)/PRECISION;
				s.lower.y = Math.round((seg.lower.y+ny)*PRECISION)/PRECISION;
			}
		} else if (seg.center!=null){
			double rad = seg.radius + t;
			Vector2D center = seg.center;
			double ox = center.x;
			double oy = center.y;
			double d = rad/seg.radius;
			if (oy!=0){
				double cx = center.x;
				double cy = center.y;
				ox = (first.x+last.x)*0.5;
				oy = (first.y+last.y)*0.5;
				double dx = ox - first.x;
				double dy = oy - first.y;
				double dd = dx*dx+dy*dy;

				double nx = -dy;
				double ny = dx;		
				double dn = nx*nx+ny*ny;

				cx -= ox;
				cy -= oy;
				if (nx*cx+ny*cy<0) {
					nx = -nx;
					ny = -ny;
				}
				double r = seg.radius;
				if (r*r<dd){
					s.type = Segment.UNKNOWN;
					seg.type= Segment.UNKNOWN;
					return;
				}
				double dt = Math.sqrt((r*r-dd)/dn);
				ox += nx * dt;
				oy += ny * dt;					

			}
			double nsx = first.x - ox;
			double nsy = first.y - oy;
			double nex = last.x - ox;
			double ney = last.y - oy;										
			s.start.x = ox+nsx*d;
			s.start.y = oy+nsy*d;
			s.end.x = ox+nex*d;
			s.end.y = oy+ney*d;	
			s.center.x = ox;
			s.center.y = oy;
			//			s.start.x = Math.round(s.start.x*PRECISION)/PRECISION;
			//			s.start.y = Math.round(s.start.y*PRECISION)/PRECISION;
			//			s.end.x = Math.round(s.end.x*PRECISION)/PRECISION;
			//			s.end.y = Math.round(s.end.y*PRECISION)/PRECISION;
			if (seg.upper!=null) {
				double ux = seg.upper.x - ox;
				double uy = seg.upper.y - oy;
				s.upper.x = ox+ux*d;
				s.upper.y = oy+uy*d;
				s.upper.x = Math.round(s.upper.x*PRECISION)/PRECISION;
				s.upper.y = Math.round(s.upper.y*PRECISION)/PRECISION;
			}
			if (seg.lower!=null) {
				double lx = seg.lower.x - ox;
				double ly = seg.lower.y - oy;
				s.lower.x = ox+lx*d;
				s.lower.y = oy+ly*d;
				s.lower.x = Math.round(s.lower.x*PRECISION)/PRECISION;
				s.lower.y = Math.round(s.lower.y*PRECISION)/PRECISION;
			}
			s.radius = rad;
			s.arc = arc(s.start,s.end,center,rad);
			s.length = s.arc * rad;
			s.dist = seg.dist;			
			s.points = seg.points;			
		}		
	}


	/*private static final double getTurn(Vector2D p1,Vector2D p2,Vector2D center,double radius){
		Vector2D v1 = new Vector2D(p1.x-center.x,p1.y-center.y);
		Vector2D v2 = new Vector2D(p2.x-center.x,p2.y-center.y);

		double angle = Vector2D.angle(v1, v2);
		if (angle<-Math.PI) 
			angle += 2*Math.PI;
		else if (angle>Math.PI) 
			angle -= 2*Math.PI;

		return (angle<0) ? -1 : 1;		
	}//*/


	private static final double arc(Vector2D p1,Vector2D p2,Vector2D center,double radius){		
		double ax = p1.x-center.x;
		double ay = p1.y-center.y;
		double bx = p2.x-center.x;
		double by = p2.y-center.y;
		double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);		
		if (angle<-Math.PI) 
			angle += 2*Math.PI;
		else if (angle>Math.PI) 
			angle -= 2*Math.PI;

		return (angle>=0) ? angle : -angle;
	}


	private static final double distance(Vector2D p1,Vector2D p2,Vector2D center,double radius){
		double ax = p1.x-center.x;
		double ay = p1.y-center.y;
		double bx = p2.x-center.x;
		double by = p2.y-center.y;
		double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);		
		if (angle<-Math.PI) 
			angle += 2*Math.PI;
		else if (angle>Math.PI) 
			angle -= 2*Math.PI;

		return radius*Math.abs(angle);
	}

	//try to combine a list of segment with the same radius
	/*private static final void adjust(ObjectArrayList<Segment> l){
		int sz = l.size();
		Segment[] lArr = l.elements();
		//		int[] startArr = starts.elements();
		//		int[] endArr = ends.elements();
		if (l==null || sz==0) return;
		for (int i=0;i<sz;++i){
			Segment s = lArr[i];
			if (s==null){
				if ((sz-=i+1)>0)System.arraycopy(lArr, i+1, lArr, i, sz);
				sz+=i;
				--i;
				continue;
			}
			Segment t = (i<sz-1) ? lArr[i+1] : null;
			if (s.type==UNKNOWN && t!=null && t.type==UNKNOWN){
				while (t!=null && t.type==UNKNOWN){
					s.end = t.end;
					s.num += t.num;
					s.endIndex = t.endIndex;
					if (--sz>i+1){
						System.arraycopy(lArr, i+2, lArr, i+1, sz-i-1);
						//						System.arraycopy(startArr, i+2, startArr, i+1, sz-i-1);
						//						System.arraycopy(endArr, i+2, endArr, i+1, sz-i-1);
					}	
					s.updated = true;
					t = (i<sz-1) ? lArr[i+1] : null;
				}
				continue;
			}

			double E = TrackSegment.EPSILON*0.1;

			while (t!=null && (s.type!=Segment.UNKNOWN && s.type==t.type && s.num+t.num>0 && (s.type==0 || (Math.abs(s.radius-t.radius)<1) && s.center.distance(t.center)<2) )){
				if (s.type==0 && s.end!=null && t.end!=null){
					double dx = s.end.x - s.start.x;
					double ddx = t.end.x - t.start.x;
					if (Math.abs(dx)<=E && Math.abs(ddx)>E) break;
					if (Math.abs(dx)>E && Math.abs(ddx)<=E) break;
					if (Math.abs(dx)>E && Math.abs(ddx)>E){
						double d = Math.max(s.start.y, t.start.y);
						double ll = Math.min(s.end.y, t.end.y);
						if (ll-d<0){
							double dy = s.end.y - s.start.y;
							double ddy = t.end.y - t.start.y;
							if (Math.abs(Math.atan2(dy, dx)-Math.atan2(ddy, ddx))>0.5) break;
						}
					}						
					if (Math.abs(t.end.x-s.start.x)>E) break;
				}

				if (--sz>i+1){
					System.arraycopy(lArr, i+2, lArr, i+1, sz-i-1);
					//										System.arraycopy(startArr, i+2, startArr, i+1, sz-i-1);
					//										System.arraycopy(endArr, i+2, endArr, i+1, sz-i-1);
				}	
				double dx = s.end.x - s.start.x;
				double ddx = t.end.x - t.start.x;
				if (dx<0) dx = -dx;
				if (ddx<0) ddx = -ddx;				
				s.updated = true;

				s.end = new Vector2D(t.end);
				if (t.upper!=null) 
					s.upper = t.upper;
				else s.upper = null;
				if (t.lower!=null && s.lower==null && s.num==0) s.lower = t.lower;				

				if (s.type==0 && ddx<E && dx<E && Math.abs(s.start.x-t.start.x)<TrackSegment.EPSILON){
					double x0= t.end.x;
					s.start.x = x0;
					s.end.x = x0;					
					s.endIndex = t.endIndex;
					if (s.opp!=null && t.opp!=null){
						s.opp.end = new Vector2D(t.opp.end);
						x0 = t.opp.end.x;
						s.opp.start.x = x0;
						s.opp.end.x = x0;
						s.opp.endIndex = t.opp.endIndex;
						s.opp.num += t.opp.num;
					}
					s.num += t.num;
					t.type = Segment.UNKNOWN;
					if (t.opp!=null) t.opp.type = Segment.UNKNOWN;
					if (t.lower!=null) {
						t.lower = null;
						s.upper = null;
					}
					t.upper = null;			
				} else {
					s.endIndex = t.endIndex;
					if (s.opp!=null && t.opp!=null){
						s.opp.end = new Vector2D(t.opp.end);
						s.opp.endIndex = t.opp.endIndex;
						s.opp.num += t.opp.num;
					}
					s.num += t.num;				
				}



				//				if (s.points==null && t.points!=null) {					
				//					s.points = t.points;
				//					s.num = t.num;
				//				} else if (s.points!=null && t.points!=null){
				//					int sz = s.num+t.num;
				//					if (sz>s.length) {
				//						Vector2D[] tmp = new Vector2D[sz*2];
				//						System.arraycopy(s.points, 0, tmp, 0, s.num);
				//						s.points = tmp;
				//					}
				//					System.arraycopy(t.points, 0, s.points, s.num, t.num);
				//					s.num += t.num;
				//				}				
				//
				//				if (s.points!=null && s.points.length>s.num){
				//					if (s.type!=0 && s.num>0 ){					
				//						s.start = new Vector2D(s.points[0]);
				//						s.end = new Vector2D(s.points[s.num-1]);
				//						s.reCalLength();
				//						s.arc = s.type*s.length/s.radius;
				//					} else if (s.num>0 && s.points!=null && s.points.length>=s.num){
				//						double dy = s.end.y-s.start.y;
				//						double dx = s.end.x-s.start.x;										
				//						s.start = (s.points[0]==null) ? null : new Vector2D(s.points[0]);
				//						s.end = (s.points[s.num-1]==null) ? null : new Vector2D(s.points[s.num-1]);
				//						if (Math.abs(dx)<=TrackSegment.EPSILON){
				//							s.start.x = (s.start.x+s.end.x)*0.5;
				//							s.end.x = s.start.x;
				//						} else if (s.start!=null && s.end!=null && s.points!=null && s.num<=s.points.length){
				//							double tmp = dy/dx;						
				//							QuickLineFitter lf = new QuickLineFitter(new double[]{tmp,s.start.y-tmp*s.start.x},s.points,0,s.num);
				//							lf.fit();
				//							double a = lf.getA();
				//							double b = lf.getB();
				//							s.start.y = a*s.start.x+b;
				//							s.end.y = a*s.end.x+b;												
				//						}
				//						s.reCalLength();
				//					}
				//				}
				if (s.map==null ){
					s.map = t.map;
				} else if (t.map!=null) 
					for (int ii=MAX_RADIUS-1;ii>=0;--ii) s.map[ii]+=t.map[ii];				

				if (s.radius!=t.radius && s.type!=0){
					int sr = (int)s.radius;
					int tr = (int)t.radius;
					if (s.map!=null && t.map!=null && s.map[sr]<t.map[tr]) s.radius = t.radius; 
					//					s.end = t.end;
					s.reCalLength();
				}

				if (i==sz-1) break;
				t = (i<sz-1) ? lArr[i+1] : null;								
			}					
		}
		l.size(sz);
		//		starts.size(sz);
		//		ends.size(sz);
	}//*/


	//try to combine a list of segment with the same radius
	private static final int adjust(Segment[] lArr,int sz){				
		//		int[] startArr = starts.elements();
		//		int[] endArr = ends.elements();
		if (lArr==null || sz==0) return 0;
		for (int i=0;i<sz;++i){
			Segment s = lArr[i];			
			Segment t = (i<sz-1) ? lArr[i+1] : null;
			if (s.type==UNKNOWN && t!=null && t.type==UNKNOWN){
				int oldI = i;
				while (t!=null && t.type==UNKNOWN){					
					i++;
					t = (i<sz-1) ? lArr[i+1] : null;
				}
				if ((sz-=oldI)>0) System.arraycopy(lArr, oldI,lArr, i, sz);
				sz+=i;
				continue;
			}

			double E = TrackSegment.EPSILON*0.1;

			while (t!=null && (s.type!=Segment.UNKNOWN && s.type==t.type && s.num+t.num>0 && (s.type==0 || (Math.abs(s.radius-t.radius)<1) && s.center.distance(t.center)<2) )){
				if (s.type==0 && s.end!=null && t.end!=null){
					double dx = s.end.x - s.start.x;
					double ddx = t.end.x - t.start.x;
					if (Math.abs(dx)<=E && Math.abs(ddx)>E) break;
					if (Math.abs(dx)>E && Math.abs(ddx)<=E) break;
					if (Math.abs(dx)>E && Math.abs(ddx)>E){
						double d = Math.max(s.start.y, t.start.y);
						double ll = Math.min(s.end.y, t.end.y);
						if (ll-d<0){
							double dy = s.end.y - s.start.y;
							double ddy = t.end.y - t.start.y;
							if (Math.abs(Math.atan2(dy, dx)-Math.atan2(ddy, ddx))>0.5) break;
						}
					}						
					if (Math.abs(t.end.x-s.start.x)>E) break;
				}

				double dx = s.end.x - s.start.x;
				double ddx = t.end.x - t.start.x;
				if (dx<0) dx = -dx;
				if (ddx<0) ddx = -ddx;				
				s.updated = true;

				s.end.copy(t.end);
				if (t.upper!=null) 
					s.upper = t.upper;
				else s.upper = null;
				if (s!=null && t!=null && t.lower!=null && s.lower==null && s.num==0) s.lower = t.lower;				

				if (s.type==0 && ddx<E && dx<E && Math.abs(s.start.x-t.start.x)<TrackSegment.EPSILON){
					double x0= t.end.x;
					s.start.x = x0;
					s.end.x = x0;					
					s.endIndex = t.endIndex;
					if (s.opp!=null && t.opp!=null){
						s.opp.end.copy(t.opp.end);
						x0 = t.opp.start.x;
						s.opp.start.x = x0;
						s.opp.end.x = x0;
						s.opp.endIndex = t.opp.endIndex;
						s.opp.num += t.opp.num;
					}
					s.num += t.num;
					t.type = Segment.UNKNOWN;
					if (t.opp!=null) t.opp.type = Segment.UNKNOWN;
					if (t.lower!=null) {
						t.lower = null;
						s.upper = null;
					}
					t.upper = null;			
				} else {
					s.endIndex = t.endIndex;
					if (s.opp!=null && t.opp!=null){
						s.opp.end.copy(t.opp.end);
						s.opp.endIndex = t.opp.endIndex;
						s.opp.num += t.opp.num;
					}
					s.num += t.num;			
					if (s.num>1){
						circle(s.points[s.startIndex], s.points[s.endIndex], s.center.x,s.center.y, s.radius,s.center);
					}
				}
				
				if (s.map!=null && t.map!=null){
					for (int ii=t.radCount-1;ii>=0;--ii) {
						int rr = t.appearedRads[ii];
						if (s.map[rr]==0)
							s.appearedRads[s.radCount++] = rr;
						
						int sc = t.map[rr];
						if (s.map[rr]<sc) s.map[rr] = sc;				
					}
					if (s.opp!=null) s.opp.radCount = s.radCount;
				} else if (s.map == null && t.map!=null) {
					t.copy(s);
					if (t.opp!=null) t.opp.copy(s.opp);
					int idx = i+1;
					if ((sz-=idx)>0) System.arraycopy(lArr,idx,lArr,idx-1,sz);
					sz+=idx-1;
					break;
				}

				if (s.radius!=t.radius && s.type!=0){
					int sr = (int)Math.round(s.radius);
					int tr = (int)Math.round(t.radius);
					if (s.map!=null && t.map!=null && s.map[sr]<t.map[tr]) s.radius = t.radius; 
					//					s.end = t.end;
					s.reCalLength();
				}

				int idx = i+2;
				if ((sz-=idx)>0) arraycopy(lArr,idx,lArr,idx-1,sz);
				sz+=idx-1;				

				if (i==sz-1) break;
				t = (i<sz-1) ? lArr[i+1] : null;								
			}					
		}
		return sz;
	}


	public static final void estimateDist(Segment s,Segment t){
		if (s.type==0 && t.type==0){
			double[] r = new double[3];
			double dx = s.end.x - s.start.x;
			double ddx = t.end.x - t.start.x;
			if (Math.abs(dx)<TrackSegment.EPSILON && Math.abs(ddx)<TrackSegment.EPSILON){
				t.dist = s.dist + t.start.y - s.start.y;
				return;
			}
			Geom.getLineLineIntersection(s.start.x, s.start.y, s.end.x, s.end.y, t.start.x, t.start.y, t.end.x, t.end.y, r);
			Vector2D p = new Vector2D(r[0],r[1]);
			t.dist = s.dist+s.length + p.distance(s.end)+p.distance(t.start);			
		} else if (s.type==0){
			double[] r = new double[3];
			Vector2D n  = s.end.minus(s.start).orthogonal(); 
			Geom.getLineLineIntersection(s.start.x, s.start.y, s.end.x, s.end.y, t.center.x, t.center.y, t.center.x+n.x, t.center.y+n.y, r);
			Vector2D p = new Vector2D(r[0],r[1]);
			double d = s.dist + p.distance(s.start);
			t.dist = d + distance(p,t.start,t.center,t.radius);
		} else if (t.type==0){
			double[] r = new double[3];
			Vector2D n  = t.end.minus(t.start).orthogonal(); 
			Geom.getLineLineIntersection(t.start.x, t.start.y, t.end.x, t.end.y, s.center.x, s.center.y, s.center.x+n.x, s.center.y+n.y, r);
			Vector2D p = new Vector2D(r[0],r[1]);
			double d = s.dist + distance(p,s.start,s.center,s.radius);
			t.dist = d + p.distance(t.start);
		} else { 
			double[] r = Geom.getCircleCircleIntersection(s.center.x, s.center.y, s.radius, t.center.x, t.center.y, t.radius);
			if (r==null || r.length==0) {
				t.dist=s.dist+s.length+s.end.distance(t.start);
				return;
			}
			Vector2D p1 = new Vector2D(r[0],r[1]);
			if (r.length==2){
				double d = s.dist + distance(p1,s.start,s.center,s.radius);
				t.dist = d + distance(p1,t.start,t.center,t.radius);
			} else {
				Vector2D p2 = new Vector2D(r[2],r[3]);
				Vector2D p = (p1.y<s.start.y && p2.y>s.start.y) ? p2 : (p2.y<s.start.y && p1.y>s.start.y) ? p1 : (Math.abs(p1.y)>Math.abs(p2.y)) ? p2 : p1;
				double d = s.dist + distance(p,s.start,s.center,s.radius);
				t.dist = d + distance(p,t.start,t.center,t.radius);
			}
		}
	}

	public final boolean reCalculate(final Vector2D[] v,int from,int to,double tW){
		if (type==Segment.UNKNOWN || to-from<1)
			return false;		
		Vector2D first = v[from];
		Vector2D last = v[to-1];
		Vector2D center = this.center;
		int len = to-from;		
		if (type==0){						
			if (len<1) return false;
			double dx = last.x-first.x;
			if (dx<0) dx = -dx;
			if (dx<=0.5*TrackSegment.EPSILON) {
				int rr = MAX_RADIUS-1;
				if (map[rr]==0) {
					appearedRads[radCount++] = rr;
					if (opp!=null) opp.radCount++;
				}

				map[rr]++;
				if (start.y>first.y){
					start.copy(first);					
				}


				if (end.y<last.y){
					end.copy(last);					
				}
				startIndex = from;
				endIndex = to-1;

				num = len;
				return true;						
			}
			double[] coef = new double[2];			
			double total = 1;			
			double a = (first.y-last.y)/(first.x-last.x);
			double b = first.y - a*first.x;
			double x1 = (first.y-b)/a;
			double x2 = (last.y-b)/a;						

			double tot = 0;
			for (int kk = from;kk<to;++kk){
				Vector2D vv = v[kk];
				tot += Math.sqrt(Geom.ptLineDistSq(x1, first.y, x2, last.y, vv.x, vv.y, null));							
			}
			tot /= len;
			if (len>3){							
				total = bestFitLine(v, from,to, coef);
				double t = (total<tot) ? total : tot;
				if (t>EPSILON && !(lower!=null && upper!=null) && (map==null || map[MAX_RADIUS-1]<3)){								
					return false;
				}
				if (tot>total){
					a = coef[0];
					b = coef[1];
				}
			} 
			if (len>=3){
				Vector2D vv =  (len==3) ? v[from+1] : v[(from+to)/2];
				double[] temp = new double[3];
				boolean isCircle = Geom.getCircle(first, last, vv, temp);
				int rr = (isCircle) ? (int)Math.sqrt(temp[2]) : MAX_RADIUS-1;

				if (rr>MAX_RADIUS-1) rr = MAX_RADIUS-1;
				
				if (rr>=MAX_RADIUS-1) {
					if (map[rr]==0) {
						appearedRads[radCount++] = rr;
						if (opp!=null) opp.radCount++;
					}
					map[rr]++;
				} else if (rr>REJECT_VALUE && rr<MAX_RADIUS-1){
					if (map[rr]==0) {
						if (appearedRads!=null) appearedRads[radCount++] = rr;
						if (opp!=null) opp.radCount++;
					}
					map[rr]++;					
					if (map[MAX_RADIUS-1]>0) map[MAX_RADIUS-1]--;
					if (map[MAX_RADIUS-1]==0){						
						type = UNKNOWN;
						radius = -1;
						for (int i =sz-1;i>=0;--i) {							
							map[rr] = 0;
						}
						sz = 0;
					}
				}
			}
			num =  len;									

			if (start.y>first.y){
				start.copy(first);				
			}

			if (end.y<last.y){
				end.copy(last);				
			}
			if (!Double.isInfinite(a)){
				start.x = (start.y-b)/a;
				end.x = (end.y-b)/a;
			}
			startIndex = from;
			endIndex = to-1;
			reCalLength();
			return true;
		}

		if (len<3) {
			if (len==2 && type!=0){
				center = tmpCenter; 
				circle(first, last, this.center.x,this.center.y, radius,center);				
				double tx = center.x;
				double ty = center.y;
				if (ty<SMALL_MARGIN && ty>-SMALL_MARGIN) {
					tx = (tx<0) ? -Math.sqrt(tx*tx+ty*ty) : Math.sqrt(tx*tx+ty*ty);
					ty = 0;
				}//first segment
				double d = this.center.y-ty;					
				if ((this.center.y==0 && (ty>SMALL_MARGIN || ty<-SMALL_MARGIN)) || (this.num>1 && (d>EPSILON*10 || d<-EPSILON*10))) 
					return false;				
				this.center.x = tx;
				this.center.y = ty;				
			}
			if (start.y>first.y){
				start.copy(first);				
			}

			if (end.y<last.y){
				end.copy(last);				
			}
			num = len;
			startIndex = from;
			endIndex = to-1;
			reCalLength();			
			return true;
		}

		Vector2D st = null;
		Vector2D en = null;
		int k = 0;
		if (points!=null){
			for (int i = from;i<to;++i){
				if (points[i].certain) {
					++k;
					if (st==null) 
						st = points[i];
					else en = points[i];
				}
			}
			if (k<2){
				st = first;
				en = last;
			}
		}
		double xx=first.x;
		double yy=first.y;		
		int i = from;		

		double[] result = new double[3];
		double x1 = xx;
		double x2 = v[i+1].x;
		double x3 = v[to-1].x;
		double y1 = yy;
		double y2 = v[i+1].y;		
		double y3 = v[to-1].y;

		boolean isCircle = Geom.getCircle(x1, y1, x2, y2, x3, y3, result);
		double oldr = this.radius;
		double radius = (isCircle) ? Math.sqrt(result[2]) : Double.MAX_VALUE;
		radius = Math.sqrt(result[2]);		
		radius = Math.round(radius-type*tW)+type*tW;		
		int sR = (this.type==0 || this.radius>=MAX_RADIUS) ? MAX_RADIUS-1 : (int)(this.radius-this.type*tW);
		/*if (len>3){
			double tx = result[0];
			double ty = result[1];			
			if (center.y==0 && to>endIndex+1) return false; 
			QuickCircleFitter cf = new QuickCircleFitter(new double[]{tx,ty},v,from,from+len);
			cf.fit();
			double rad = cf.getEstimatedRadius();
			rad = Math.round(rad-type*tW)+type*tW;
			//			if (radius!=this.radius && radius!=rad && rad<MAX_RADIUS-1){				
			//				if (map!=null)
			//					if (radius>=MAX_RADIUS) 
			//						map[MAX_RADIUS-1]++;
			//					else map[(int)Math.round(radius-type*tW)]++;
			//			}
			radius = rad;

		}//*/			
		int rr = (radius>=MAX_RADIUS) ? MAX_RADIUS-1 : (int)Math.round(radius-type*tW);
		double de = check(v,from,to,center,oldr);
		if (de<-0.1) return false;
		boolean wasOk = de>=0;

		if (map!=null && rr>0 && rr<=Segment.MAX_RADIUS-1){
			if (map[rr]==0) {
				if (appearedRads==null) appearedRads = new int[MAX_RADIUS];
				appearedRads[radCount++] = rr;
				if (opp!=null) opp.radCount++;
			}
			map[rr]++;
		}
		if (rr!=sR && sR>=0 && rr>=0 && rr<=Segment.MAX_RADIUS-1 && sR<=MAX_RADIUS-1 && map!=null && map[rr]<map[sR]) radius = oldr;
		if (rr>=MAX_RADIUS-1 && sR<MAX_RADIUS-1 && map[MAX_RADIUS-1]<2) radius = oldr;
		if (st!=null && en!=null && radius<Segment.MAX_RADIUS-1){
			double tx = (st.x+en.x)*0.5;
			double ty = (st.y+en.y)*0.5;
			double qx = result[0];
			double qy = result[1];

			double ddx = st.x-tx;
			double ddy = st.y-ty;
			double d = ddx*ddx+ddy*ddy;

			double nx = -ddy;
			double ny = ddx;		
			double dn = nx*nx+ny*ny;

			qx -= tx;
			qy -= ty;
			if (nx*qx+ny*qy<0) {
				nx = -nx;
				ny = -ny;
			}

			double dt = Math.sqrt((radius*radius-d)/dn);
			tx += nx * dt;
			ty += ny * dt;						

			if (center.y==0 && (ty>=SMALL_MARGIN || ty<=-SMALL_MARGIN)) return false;

			if (ty<SMALL_MARGIN && ty>-SMALL_MARGIN) {
				tx = (tx<0) ? -Math.sqrt(tx*tx+ty*ty) : Math.sqrt(tx*tx+ty*ty);
				ty = 0;
			}//first segment

			if (check(v,from,to,new Vector2D(tx,ty),radius)<0){ 				
				if (ty==0 || (upper!=null && last.y>upper.y) || (lower!=null && lower.y>first.y) || (wasOk && radius==oldr)) return false;			
			}
			if (center.y==0 && ty!=0) return false;
			center.x = tx;
			center.y = ty;
		}

		arc = arc(new Vector2D(x1,y1),end,center,radius);
		length = arc*radius;
		num = len;
		this.radius = radius;
		if (start.y>first.y){
			start.copy(first);			
		}
		if (end.y<last.y){
			end.copy(last);			
		}
		startIndex = from;
		endIndex = to-1;
		return true;
	}	

	private static final double bestFitLine(Vector2D[] tmp,int from,int to,double[] rs){		
		Vector2D last = new Vector2D(tmp[to-1]);
		Vector2D fst = new Vector2D(tmp[from]);
		double dx = last.x-fst.x;
		double dy = last.y-fst.y;			

		if (Math.abs(dx)<E){
			double total = 0;
			for (int kk = from;kk<to;++kk){
				Vector2D vv = tmp[kk];
				total += Math.sqrt(Geom.ptLineDistSq(fst.x, fst.y, fst.x, last.y, vv.x, vv.y, null));							
			}
			if (rs!=null) rs[0] = Double.POSITIVE_INFINITY;
			return total/(to-from+0.0d);
		}
		double a = dy/dx;
		double b = last.y-last.x*a;
//		if (!CircleDriver2.inTurn){			
//			QuickLineFitter lf = new QuickLineFitter(new double[]{a,b},tmp,from,to);
//			lf.fit();
//			a = lf.getA();
//			b = lf.getB();		
//		} 

		double x1 = (fst.y-b)/a;
		double x2 = (last.y-b)/a;
		if (rs!=null && rs.length>=2){
			rs[0] = a;
			rs[1] = b;
		}

		double total = 0;
		for (int kk = from;kk<to;++kk){
			Vector2D vv = tmp[kk];
			total += Math.sqrt(Geom.ptLineDistSq(x1, fst.y, x2, last.y, vv.x, vv.y, null));							
		}
		return total/(to-from+0.0d);
	}

	
	public final void translate(double dx,double dy){				
		if (start!=null) {
			start.x+=dx;
			start.y+=dy;
		}
		if (end!=null) {
			end.x+=dx;
			end.y+=dy;
		}
		if (type!=0 &&type!= UNKNOWN && center!=null){
			center.x+=dx;
			center.y+=dy;
		}

		if (upper!=null) {
			upper.x+=dx;
			upper.y+=dy;
		}
		if (lower!=null){
			lower.x+=dx;
			lower.y+=dy;
		}
	}
	
	public final void scale(double scale){
		if (scale==1.0) return;
		if (start!=null) {
			start.x*=scale;			
		}
		if (end!=null) {
			end.x*=scale;			
		}
		if (type!=0 &&type!= UNKNOWN && center!=null){
			center.x*=scale;			
		}

		if (upper!=null) {
			upper.x*=scale;			
		}
		if (lower!=null){
			lower.x*=scale;			
		}
	}

	
	/*
	 * * Rotating by a positive angle theta rotates points on the positive
     * X axis toward the positive Y axis.
     * Note also the discussion of
	 */
	public final void rotate(double theta,double cx,double cy){
		double sin = Math.sin(theta);
		double cos = (sin==1.0 || sin==-1.0) ? 0.0 : Math.cos(theta);
		double x = 0;
		double y = 0;
		double tx = 0;
		if (start!=null){
			x = start.x-cx;
			y = start.y-cy;
			tx = x;
			if (cos==-1.0) {
				x = -x;
				y = -y;
			} else if (cos!=1){
				x = x * cos - y * sin;
				y = tx * sin + y * cos;
			}
			x+=cx;
			y+=cy;
			start.x = x;
			start.y = y;
		}
		
		if (end!=null){
			x = end.x-cx;
			y = end.y-cy;
			tx = x;
			if (cos==-1.0) {
				x = -x;
				y = -y;
			} else if (cos!=1){
				x = x * cos - y * sin;
				y = tx * sin + y * cos;
			}
			x+=cx;
			y+=cy;
			end.x = x;
			end.y = y;
		}
		
		if (type!=0 && type!= UNKNOWN && center!=null){
			x = center.x-cx;
			y = center.y-cy;
			tx = x;
			if (cos==-1.0) {
				x = -x;
				y = -y;
			} else if (cos!=1){
				x = x * cos - y * sin;
				y = tx * sin + y * cos;
			}
			x+=cx;
			y+=cy;
			center.x = x;
			center.y = y;
		}
		
		if (upper!=null){
			x = upper.x-cx;
			y = upper.y-cy;
			tx = x;
			if (cos==-1.0) {
				x = -x;
				y = -y;
			} else if (cos!=1){
				x = x * cos - y * sin;
				y = tx * sin + y * cos;
			}
			x+=cx;
			y+=cy;
			upper.x = x;
			upper.y = y;
		}
		
		if (lower!=null){
			x = lower.x-cx;
			y = lower.y-cy;
			tx = x;
			if (cos==-1.0) {
				x = -x;
				y = -y;
			} else if (cos!=1){
				x = x * cos - y * sin;
				y = tx * sin + y * cos;
			}
			x+=cx;
			y+=cy;
			lower.x = x;
			lower.y = y;
		}

	}


	
//	public final void transform(final AffineTransform at){		
//		if (at==null) return;
//		if (start!=null) {
//			at.transform(start, start);						
//		}
//		if (end!=null) {
//			at.transform(end, end);			
//		}
//		if (type!=0 &&type!= UNKNOWN && center!=null)
//			at.transform(center, center);
//
//		if (upper!=null) at.transform(upper, upper);
//		if (lower!=null) at.transform(lower, lower);		
//	}

	public static final ObjectArrayList<Segment> segmentize(ObjectArrayList<Vector2D> v,double tW){		
		return segmentize(v.elements(),0,v.size(), tW,null);		
	}

	/*private static final void updateRs(final Vector2D[] v,ObjectList<Segment> rs,int l,double tW,IntArrayList starts,IntArrayList ends){		
		if (l<rs.size()-1){
			Segment s = rs.get(l);
			Segment last = rs.get(l+1);
			if (last.num<=4 && s.type!=Segment.UNKNOWN){					
				Vector2D[] points = last.points;
				int len = last.num;
				int index = 0;
				while (index<len){
					Vector2D point = points[index++];
					if (!s.isPointBelongToSeg(point)) break;						
//					last.removePoint(point);
					if (last.num<3) last.type = Segment.UNKNOWN;					
					if (last.type==Segment.UNKNOWN) last.radius = 0;
					s.addPoint(new Vector2D(point));		
					if (last.type!=Segment.UNKNOWN){
						double oldr = last.radius;
						last.reCalculate(v,starts.get(l+1),ends.get(l+1),tW);
						if (Math.abs(oldr-last.radius)>0.5) updateRs(v,rs, l+1,tW,starts,ends);
					}

				}
				if (last.num<=0) rs.remove(l+1);								
			}
		}
	}//*/	

	public static final boolean isConnected(Segment s,Segment last,double tW,Vector2D point){
		if (s==null || last==null || s.type==Segment.UNKNOWN || last.type==Segment.UNKNOWN) return false;
		double[] temp = new double[6];
		isPrevNextConnected = false;
		isNextPrevConnected = false;
		isPossiblyConnected = false;
		boolean isCntd = false;
		Vector2D sF = null;
		Vector2D sL = null;
		Vector2D lF = null;
		Vector2D lL = null;
		Vector2D lCenter = last.center;
		Vector2D sCenter = s.center;
		int k = 0;
		boolean isStraight = Math.abs(s.start.x-s.end.x)<0.5*TrackSegment.EPSILON;
		if (s.points!=null || last.points!=null){

			Vector2D[] points = (s.points!=null) ? s.points : last.points;
			if (s.num>1 && !isStraight && s.endIndex>0 && points!=null){
				int start = s.startIndex;
				int end = s.endIndex;

				for (int i = start;i<=end;++i){
					Vector2D p = points[i];
					if (p==null) continue;
					if (p.certain) {
						++k;
						if (sF==null) 
							sF = p;
						else sL = p;
					}
				}
				if (k<2){
					if (s.num<2) {
						sF = s.start;
						sL = s.end;
					} else {
						sF = points[start];
						sL = points[end];
					}
				}

			} else if (!isStraight){
				sF = s.start;
				sL = s.end;
			} else {
				double xx = s.start.x;
				sF = new Vector2D(xx,0);
				sL = new Vector2D(xx,1);
			}


			if (last.num>1 && points!=null){				
				k = 0;
				int start = last.startIndex;
				int end = last.endIndex;				
				for (int i = start;i<=end;++i){
					Vector2D p = points[i];
					if (p==null) continue;
					if (p.certain) {
						++k;
						if (lF==null) 
							lF = p;
						else lL = p;
					}
				}
				if (k<2){								
					if (last.num<2) {
						lF = last.start;
						lL = last.end;
					} else {
						lF = points[start];
						lL = points[end];
					}	
				}			
			} else {
				lF = last.start;
				lL = last.end;
			}
		} else {
			lF = last.start;
			lL = last.end;
			sF = s.start;
			sL = s.end;
			if (isStraight)							
				sL.x = sF.x;
		}




		if (s.type!=Segment.UNKNOWN && last.type!=Segment.UNKNOWN){						
			double r = 0;
			if (s.type==0 && last.type!=0){							
				Geom.getCircle2(lF, lL, sF, sL, temp);					
				r = Math.sqrt(temp[2]);
				double cx = temp[0];
				double cy = temp[1];
				double ax = lF.x-cx;
				double ay = lF.y-cy;
				double bx = lL.x-cx;
				double by = lL.y-cy;
				double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);			
				if (angle<-Math.PI) 
					angle += 2*Math.PI;
				else if (angle>Math.PI) 
					angle -= 2*Math.PI;
							
				int tp = (angle<0) ? -1 : 1;	
				r = Math.round(r-tp*tW)+tp*tW;
				if (r<=REJECT_VALUE) return false;
				double xx = sF.x;
				double yy = sF.y;
				double dx = sL.x - xx;
				double dy = sL.y - yy;
				double nx = -dy;
				double ny = dx;								
				double x0 = last.center.x;
				double y0 = last.center.y;
				Geom.getLineLineIntersection(xx, yy, sL.x, sL.y, x0, y0, x0+nx, y0+ny, temp);
				double ddx = x0 - temp[0];
				double ddy = y0 - temp[1];
				double dd = Math.round(Math.sqrt(ddx*ddx+ddy*ddy)*100)/100.0d;
				double de = dd-r;
				if (de<0) de = -de;
				isPossiblyConnected = (de<=0.1);
				if (r==last.radius || (de<=2 && dd==last.radius) || (s.upper!=null && last.lower!=null && Math.abs(s.start.x-last.start.x)<TrackSegment.EPSILON && dd==last.radius)) {
					if (point!=null) {
						if (isStraight) {					
							point.x = xx;
							point.y = y0;
						} else {							
							point.x = temp[0];
							point.y = temp[1];
						}
					}
					return true;
				}

			} else if (s.type!=0 && last.type==0){							
				Geom.getCircle2(sF, sL, lF, lL, temp);					
				r = Math.sqrt(temp[2]);								
				double cx = temp[0];
				double cy = temp[1];
				double ax = sF.x-cx;
				double ay = sF.y-cy;
				double bx = sL.x-cx;
				double by = sL.y-cy;
				double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);			
				if (angle<-Math.PI) 
					angle += 2*Math.PI;
				else if (angle>Math.PI) 
					angle -= 2*Math.PI;
							
				int tp = (angle<0) ? -1 : 1;		
				r = Math.round(r-tp*tW)+tp*tW;
				if (r>0 && r<=REJECT_VALUE) return false;
				double xx = lF.x;
				double yy = lF.y;
				double dx = lL.x - xx;
				double dy = lL.y - yy;
				double nx = -dy;
				double ny = dx;											
				double x0 = s.center.x;
				double y0 = s.center.y;
				Geom.getLineLineIntersection(xx, yy, lL.x, lL.y, x0, y0, x0+nx, y0+ny, temp);
				double ddx = x0 - temp[0];
				double ddy = y0 - temp[1];
				double dd = Math.round(Math.sqrt(ddx*ddx+ddy*ddy)*100)/100.0d;
				double de = dd-r;
				if (de<0) de = -de;
				isPossiblyConnected = (de<=0.1);
				if (r==s.radius || (dd == s.radius && de<=2) || (s.upper!=null && last.lower!=null && Math.abs(s.start.y-last.start.y)<0.5 && dd==last.radius)) {
					if (point!=null) {						
						point.x = temp[0];
						point.y = temp[1];
					}
					return true;
				}

				//				double d = Math.sqrt(Geom.ptLineDistSq(sF.x, sF.y, sL.x, sL.y, s.center.x, s.center.y, temp));
				//				d = Math.round(d-tW)+tW;
				//				if (d==s.radius){
				//					if (point!=null){
				//						point.x = temp[0];
				//						point.y = temp[1];
				//					}
				//					return true;
				//				}

			} else if (s.type!=0 && last.type!=0){									
				double dx = lCenter.x - sCenter.x;
				double dy = lCenter.y - sCenter.y;
				double d = Math.sqrt(dx*dx+dy*dy);
				double sum = (s.type==last.type) ? (last.radius-s.radius) : last.radius + s.radius;
				if (sum<0) sum = -sum;
				if (Math.round(d)!=sum) return false;
				d = Math.round(d*100)/100.0d;
				double de = d-sum;
				if (de<0) de = -de;
				isPossiblyConnected = (de<=0.1);
				int no = 0;

				boolean ok1 = false;
				if (s.type==last.type) 
					no = Geom.getCircle4(sF, sL, lCenter, last.radius, temp);					
				else no = Geom.getCircle5(sF, sL, lCenter, last.radius, temp);

				for (k=no-1;k>=0;--k){
					r = Math.round(temp[k*3+2]-tW)+tW;
					ok1 = (ok1 || Math.abs(r-s.radius)<2);
					if (Math.abs(r-s.radius)<=0.5) {
						isCntd = true;
						isNextPrevConnected = true;
						break;
					}
				}
				if (!isCntd && s.num>1){
					//					if (lCenter!=null) lCenter = circle(lF, lL, lCenter, last.radius);
					no = 0;
					if (s.type==last.type) 
						no = Geom.getCircle4(s.start, s.end, lCenter, last.radius, temp);					
					else no = Geom.getCircle5(s.start, s.end, lCenter, last.radius, temp);

					for (k=no-1;k>=0;--k){
						r = Math.round(temp[k*3+2]-tW)+tW;
						ok1 = (ok1 || Math.abs(r-s.radius)<2);
						if (Math.abs(r-s.radius)<=0.5) {
							isCntd = true;
							isNextPrevConnected = true;
							break;
						}
					}
				}

				boolean ok2 = false;

				no = 0;					
				if (s.type==last.type) 
					no = Geom.getCircle4(lF, lL, sCenter, s.radius, temp);					
				else no = Geom.getCircle5(lF, lL, sCenter, s.radius, temp);

				for (k=no-1;k>=0;--k){
					r = Math.round(temp[k*3+2]-tW)+tW;
					ok2 = (ok2 || Math.abs(r-last.radius)<2);
					if (Math.abs(r-last.radius)<=0.5) {
						isCntd = true;	
						isPrevNextConnected = true;
						break;
					}
				}

				if (!isPrevNextConnected && last.num>1){
					//					if (sCenter!=null) sCenter = circle(sF, sL, sCenter, s.radius);
					no = 0;					
					if (s.type==last.type) 
						no = Geom.getCircle4(last.start, last.end, sCenter, s.radius, temp);					
					else no = Geom.getCircle5(last.start, last.end, sCenter, s.radius, temp);

					for (k=no-1;k>=0;--k){
						r = Math.round(temp[k*3+2]-tW)+tW;
						ok2 = (ok2 || Math.abs(r-last.radius)<2);
						if (Math.abs(r-last.radius)<=0.5) {
							isCntd = true;	
							isPrevNextConnected = true;
							break;
						}
					}
				}
				isCntd = ((isPrevNextConnected && isNextPrevConnected)|| (isNextPrevConnected && no==0) || (isPrevNextConnected && !ok1));				


				if ((isCntd || isPrevNextConnected || isNextPrevConnected) && point!=null){
					//					lCenter = circle(lF, lL, lCenter, last.radius);
					//					sCenter = circle(sF, sL, sCenter, s.radius);
					double sx = sCenter.x;
					double sy = sCenter.y;					
					double rad = s.radius;
					dx = lCenter.x - sCenter.x;
					dy = lCenter.y - sCenter.y;
					d = Math.round(Math.sqrt(dx*dx+dy*dy));
					if (s.type==last.type && dy<0){
						dx = - dx;
						dy = -dy;
					}
					double t = rad/d;
					sx += dx*t;
					sy += dy*t;
					point.x = sx;
					point.y = sy;
				}
				return isCntd;				
			}


		}
		return false;
	}

	private final static boolean isBelong(Segment s,Vector2D[] v,int from,int to,Segment rs,int which,double tW){
//		long ti = System.nanoTime();
		int n = 0;
		int size = to - from;
		if (size<2) return false;
		int sNum = s.num;

		Vector2D fst = v[from];
		Vector2D lst = v[to-1];		

		Storage storage = (which==1) ? CircleDriver2.rMap : CircleDriver2.lMap;
		if (size>=3){
			int mr = storage.getMaxRad(from, to-1);
			if (storage.isFound(from,to-1)){									
				int tp = (mr>=Segment.MAX_RADIUS) ? 1 : (mr==0) ? 0 : -1;
				if (tp==1) mr-=Segment.MAX_RADIUS;
				double rad = mr-tp*which*tW;
				if (tp==s.type && rad==s.radius){
					rs.type = tp;
					if (rs.start==null) 
						rs.start = new Vector2D(fst);
					else rs.start.copy(fst);
					
					if (rs.end==null)
						rs.end = new Vector2D(lst);
					else rs.end.copy(lst);
					rs.num = size;
					rs.points = v;
					rs.radius = rad;
					if (tp!=0) {
						if (rs.center==null) rs.center = new Vector2D();					
						circle(fst, lst, tp, rad,rs.center);
					}
					rs.startIndex = from;
					rs.endIndex = to-1;
					return true;
				}
			}
			if (storage.isChecked(from, to-1)) {
				int[] map = storage.getMap(from,to-1);				
				if (s.type!=UNKNOWN){
					int sr = (s.type==0) ? 0 : (int)Math.round(s.radius+s.type*which*tW);
					if (s.type==1) sr+=Segment.MAX_RADIUS;
					if (map[mr]>1 && (sr==mr || map!=null && map[sr]>0 && map[sr]>=map[mr])){
						rs.type = s.type;
						if (rs.start==null) 
							rs.start = new Vector2D(fst);
						else rs.start.copy(fst);
						
						if (rs.end==null)
							rs.end = new Vector2D(lst);
						else rs.end.copy(lst);
						rs.num = size;
						rs.points = v;
						rs.radius = s.radius;
						if (s.type!=0) {
							if (rs.center==null) rs.center = new Vector2D();					
							circle(fst, lst, s.center.x,s.center.y, s.radius,rs.center);
						}
						rs.startIndex = from;
						rs.endIndex = to-1;
						return true;
					}
				}
								
				return false;
			}
		}
		if (sNum>=size){
			if (rs==null) return true;
			rs.type = s.type;
			if (rs.start==null) 
				rs.start = new Vector2D(fst);
			else rs.start.copy(fst);
			
			if (rs.end==null)
				rs.end = new Vector2D(lst);
			else rs.end.copy(lst);
			rs.points = v;
			rs.num = size;
			rs.radius = s.radius;
			rs.startIndex = from;
			rs.endIndex = to-1;
			if (rs.type!=0){
//				rs.center = circle(fst, lst, s.center, rs.radius);
				if (rs.center==null) rs.center = new Vector2D();					
				circle(fst, lst, s.center.x,s.center.y, s.radius,rs.center);
			}
			return true;
		}

		if (s.type==0){
			double de = s.start.x-s.end.x;
			if (de<0) de = -de;
			if (de<E){
				double e = fst.x-lst.x;
				if (e<0) e = -e;
				if (e<E){
					if (rs!=null){
						rs.type = 0;
						if (rs.start==null) 
							rs.start = new Vector2D(fst);
						else rs.start.copy(fst);
						
						if (rs.end==null)
							rs.end = new Vector2D(lst);
						else rs.end.copy(lst);
						rs.points = v;
						rs.num = size;
						rs.radius = 0;
						rs.startIndex = from;
						rs.endIndex = to-1;
					}
					return true;
				} 
				return false;
			}

			if (s.isPointBelongToSeg(fst) && s.isPointBelongToSeg(lst)){								
				double total = 1;
				double[] coef = new double[2];
				double a = (fst.y-lst.y)/(fst.x-lst.x);
				double b = fst.y - a*fst.x;
				double x1 = (fst.y-b)/a;
				double x2 = (lst.y-b)/a;						

				double tot = 0;
				for (int kk = from;kk<to;++kk){
					Vector2D vv = v[kk];
					tot += Math.sqrt(Geom.ptLineDistSq(x1, fst.y, x2, lst.y, vv.x, vv.y, null));							
				}
				tot /= size;
				if (size>3){							
					total = bestFitLine(v, from,to, coef);
					double t = (total<tot) ? total : tot;
					if ((t>EPSILON && !(s.lower!=null && s.upper!=null) && (s.map==null || s.map[MAX_RADIUS-1]<3)) || (t>=0.5 && tot>=0.5)){								
						return false;
					}
					if (tot>total){
						a = coef[0];
						b = coef[1];
					}
				}


				if (s!=null && s.type!=UNKNOWN) {
					if (size>=3){
						int indx  = (size==3) ? from+1 : (from+to)>>1;
						Vector2D vv = v[indx];
						double[] temp = new double[3];
						boolean isCircle = Geom.getCircle(fst, lst, vv, temp);
						int rr = (isCircle) ? (int)Math.sqrt(temp[2]) : MAX_RADIUS-1;						
						if (rr>MAX_RADIUS-1) rr = MAX_RADIUS-1;						

						if (rr>=MAX_RADIUS-1 && s.map!=null){							
							s.map[rr]++;
							storage.store(from,indx, to-1, 0, 0);
						} else if (rr>REJECT_VALUE && rr<MAX_RADIUS-1){
							double cx = temp[1];
							double cy = temp[2];
							double ax = fst.x-cx;
							double ay = fst.y-cy;
							double bx = lst.x-cx;
							double by = lst.y-cy;
							double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);			
							if (angle<-Math.PI) 
								angle += 2*Math.PI;
							else if (angle>Math.PI) 
								angle -= 2*Math.PI;
										
							int tp = (angle<0) ? -1 : 1;
							storage.store(from,indx, to-1, tp, rr);
							if (s.map==null || s.map[MAX_RADIUS-1]<3){
								return false;
							}							
						}
					}						
				}
				if (rs!=null){
					rs.type = 0;
					if (rs.start==null) 
						rs.start = new Vector2D(fst);
					else rs.start.copy(fst);
					
					if (rs.end==null)
						rs.end = new Vector2D(lst);
					else rs.end.copy(lst);						
					rs.num = size;					
					rs.radius = 0;
					rs.points = v;
					rs.startIndex = from;
					rs.endIndex = to-1;

					if (!Double.isInfinite(a)){
						rs.start.x = (fst.y-b)/a;
						rs.end.x = (lst.y-b)/a;
					}
				}				
				return true;
			}
		} else if (size==2){
			if (rs!=null){
				rs.type = s.type;
				if (rs.start==null) 
					rs.start = new Vector2D(fst);
				else rs.start.copy(fst);
				
				if (rs.end==null)
					rs.end = new Vector2D(lst);
				else rs.end.copy(lst);
				rs.num = size;
				rs.radius = s.radius;
				rs.points = v;
				rs.startIndex = from;
				rs.endIndex = to-1;
				if (rs.type!=0){
					if (rs.center==null) rs.center = new Vector2D();					
					circle(fst, lst, s.center.x,s.center.y, s.radius,rs.center);
//					rs.center = circle(fst, lst, s.center, rs.radius);
				}
			}
			return false;
		} else {	
			int maxPossible = size-2;
			//double sx = (s.type==0 || s.center==null) ? 0 : s.center.x;
			double sy = (s.type==0 || s.center==null) ? 0 : s.center.y;
			boolean isFirst = (s.type==0 && Math.abs(s.start.x-s.end.x)<=E) || (s.type!=0 && s.type!=Segment.UNKNOWN && s.center!=null && sy==0);
			int[] map = tmpAMap;
			int[] allRads = tmpARads;
			int[] check = tmpCheck;
			int count = 0;
			int endIndx = to-1;
			boolean inTurn = CircleDriver2.inTurn;
//			double[][][] allRadius = (inTurn) ? (which==1) ? CircleDriver2.allRadiusRight : CircleDriver2.allRadiusLeft : null;
//			double[][][] allCntrx = (inTurn) ? (which==1) ? CircleDriver2.allCntrxR : CircleDriver2.allCntrxL : null;
//			double[][][] allCntry = (inTurn) ? (which==1) ? CircleDriver2.allCntryR : CircleDriver2.allCntryL : null;
//			double[][] bestRad = (inTurn) ? (which==1) ? CircleDriver2.bestFitRadR : CircleDriver2.bestFitRadL : null;
//			int[][] bestIntRad = (inTurn) ? (which==1) ? CircleDriver2.bestIntFitRadR : CircleDriver2.bestIntFitRadL : null;
//			int[][] maxAppear = (inTurn) ? (which==1) ? CircleDriver2.maxAppearR : CircleDriver2.maxAppearL : null;
//			int[][] tps = (inTurn) ? (which==1) ? CircleDriver2.bestTpR : CircleDriver2.bestTpL : null;
//			int[][][] allTurn = (inTurn) ? (which==1) ? CircleDriver2.allTpR : CircleDriver2.allTpL : null;
			int tp = s.type;
			boolean found = false;
			double r = 0;
			int er = 0;			
			Vector2D center = tmpCenter;
			final double MARGIN = 0.006;
			double dd1 = 0;
			double dd = 0;
			//double ignoreRad = -1;
			/*if (inTurn){
				tp = tps[from][endIndx];
				if (tp!=s.type && s.type!=Segment.UNKNOWN) return false;
				r = Math.round(bestRad[from][endIndx]-tW)+tW;
				er = (int)r;
				double cy = 0;
				if (tp!=0) {
					Segment.circle(fst, lst, sx,sy, r,center);
					cy = center.y;
					if (isFirst && Math.abs(cy)<MARGIN){						
						double de = which*tp*tW;
						center.x = (tp==-1) ? CircleDriver2.toMiddle-r-de : r+de+CircleDriver2.toMiddle;
						center.y = 0;
						cy = 0;
					}
				}
				if (tp!=0 && r==s.radius && tp==s.type && Math.abs(cy-sy)<1){
					if (isFirst){
						if (Math.abs(cy)>MARGIN || Segment.check(v, from, to, center, r)<0) {
							return false;
						} else {
							Vector2D point = (which==-1) ? CircleDriver2.leftMost : CircleDriver2.rightMost;
							double ox = (point.x+lst.x)*0.5d;
							double oy = (point.y+lst.y)*0.5d;
							double nx = point.y - oy  ;
							double ny = ox - point.x;
							Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, 1, 0, tmp1);
							double dx = tmp1[0]-point.x;
							double dy = tmp1[1];
							dd1 = Math.sqrt(dx*dx+dy*dy);
							dd1 = Math.round(dd1*100.0d)/100.0d;
							if (Math.abs(dd1-r)<=0.5) 
								found = true;
							else {
								ox = (fst.x+lst.x)*0.5d;
								oy = (fst.y+lst.y)*0.5d;
								nx = fst.y - oy  ;
								ny = ox - fst.x;
								Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, 1, 0, tmp1);
								dx = tmp1[0]-fst.x;
								dy = tmp1[1];
								dd = Math.sqrt(dx*dx+dy*dy);
								dd = Math.round(dd*100.0d)/100.0d;
								if (Math.abs(dd-r)<=0.5)
									found = true;
							}
						}
					} else found = true;					
				} else if (tp!=0 && r>REJECT_VALUE){
					check[er] = (Segment.check(v, from, to, center, r)<0) ? -1 : 1;
					int max = maxAppear[from][endIndx];
					found = (max==maxPossible && check[er]>0);
					if (found || s.type==Segment.UNKNOWN){						
						if (!found && max<2) return false;
						if (found || (check[er]>0 && (max>2 || (max>1 && maxPossible<=3)))){
							if (rs!=null){
								rs.type = tp;
								rs.start = new Vector2D(fst);
								rs.end = new Vector2D(lst);
								rs.num = size;
								rs.points = v;
								rs.radius = r;
								if (tp!=0) rs.center = center;
								rs.startIndex = from;
								rs.endIndex = to-1;					
							}
							return false;
						} else if (rs!=null){							
							if (check[er]<0 && er>0){						
								if (!isFirst){
									circle(fst, lst, center.x,center.y,r, center);
								} else {
									double de = which*tp*tW;
									center.x = (tp==-1) ? CircleDriver2.toMiddle-r-de : r+de+CircleDriver2.toMiddle;
									center.y = 0;
								}
								boolean ok = (!inTurn);
								if (ok){
									for (int i=from;i<to;++i){
										Vector2D p = v[i];
										if (p.certain){
											double dx = p.x - center.x;
											double dy = p.y - center.y;
											double d = Math.sqrt(dx*dx+dy*dy)-r;
											if (d<0) d = -d;
											if (d>=EPSILON) {
												ok = false;
												break;
											}
										}
									}
								}
								if (ok || max>2 || (max>1 && maxPossible<=3)){
									rs.type = tp;
									rs.start = new Vector2D(fst);
									rs.end = new Vector2D(lst);
									rs.num = size;
									rs.points = v;
									rs.radius = r;
									rs.center = center;
									rs.startIndex = from;
									rs.endIndex = to-1;
									return false;
								}								
								ignoreRad = r;
							}							
						}//end of else
						if (maxPossible<max+max) return false;
					}//end of if
					if (maxPossible==max) return false;
				}
				
			}//*/

			if (!found){
				if (isFirst && (dd==0 || dd1==0)){
					if (dd1==0){
						Vector2D point = (which==-1) ? CircleDriver2.leftMost : CircleDriver2.rightMost;
						double ox = (point.x+lst.x)*0.5d;
						double oy = (point.y+lst.y)*0.5d;
						double nx = point.y - oy  ;
						double ny = ox - point.x;
						Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, 1, 0, tmp1);
						double dx = tmp1[0]-point.x;
						double dy = tmp1[1];
						dd1 = Math.sqrt(dx*dx+dy*dy);
						dd1 = Math.round(dd1*100.0d)/100.0d;
					}
					if (dd==0){
						double ox = (fst.x+lst.x)*0.5d;
						double oy = (fst.y+lst.y)*0.5d;
						double nx = fst.y - oy  ;
						double ny = ox - fst.x;
						Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, 1, 0, tmp1);
						double dx = tmp1[0]-fst.x;
						double dy = tmp1[1];
						dd = Math.sqrt(dx*dx+dy*dy);
						dd = Math.round(dd*100.0d)/100.0d;
					}
				}
				double cx = 0;
				double cy = 0;
				for (int i = from+1;i<endIndx;++i){					
					Vector2D mid = v[i];
					n++;
					boolean isCircle = Geom.getCircle(fst, mid,lst, tmp1);					
					if (isCircle){						
						r = Math.sqrt(tmp1[2]);							
						cx = tmp1[0];
						cy = tmp1[1];						
						r = Math.round(r-tW)+tW;
//						if (r==ignoreRad) continue;
						
//						if (inTurn){
//							tp = allTurn[from][i][endIndx];
//						} else 
						if (r<MAX_RADIUS-1){						
							double ax = fst.x-cx;
							double ay = fst.y-cy;
							double bx = lst.x-cx;
							double by = lst.y-cy;
							double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);			
							if (angle<-Math.PI) 
								angle += 2*Math.PI;
							else if (angle>Math.PI) 
								angle -= 2*Math.PI;
										
							tp = (angle<0) ? -1 : 1;	
						} else {
							tp = 0;
							r = 0;
						}

																		
						circle(fst, lst, cx,cy, r,center);
						cx = center.x;
						cy = center.y;
						if (r<=REJECT_VALUE || r+tp*which*tW*2<=REJECT_VALUE) continue;
						double de = which*tp*tW;
						if (isFirst){
							if (Math.abs(cy)>MARGIN) continue;
							if (Math.abs(r-dd1)>0.5 && Math.abs(r-dd)>0.5) continue;
							cx = (tp==-1) ? CircleDriver2.toMiddle-r-de : r+de+CircleDriver2.toMiddle;
							cy = 0;		
							center.x = cx;
							center.y = 0;
						}
						
						er = (int)Math.round(r);
						if (er>=Segment.MAX_RADIUS) {
							er = 0;
							tp = 0;
						}
						if (r>=tmpL){												
							er = 0;
							tp = 0;
						}
	
						storage.store(from,i, endIndx, tp, r);
						
						if (map[er]==0){
							allRads[count++] = er;
						}
						map[er]++;															
					} else {//else !isCircle
						if (map[0]==0){
							allRads[count++] = 0;
						}
						map[0]++;
						er = 0;
						tp = 0;
					}
					if (map[er]>2) {					
						found = (tp==s.type && r==s.radius && Math.abs(cy-sy)<1);
						if (found){							
							double EE = EPSILON*3;
							boolean ok = true;
							for (int ii = from;ii<to;++ii){								
								Vector2D vv = v[ii];
								double dx = vv.x-cx;
								double dy = vv.y-cy;			
								double e = Math.sqrt(dx*dx+dy*dy)-r;
								if (e<0) e = -e;
								if (e>EE) {
									ok = false;
									break;
								}
							}
							if (ok) break;
						}			
						if (check[er]>0) break;
					}
					if (map[er]>1) {
						if (er>0 && tp==s.type && r==s.radius && check[er]>0 && Math.abs(cy-sy)<1) {
							found = true;
							break;
						}
						continue;
					}
					if (check[er]==0){
						if (er>0) {														
							if (check(v,from,to,center,r)<0) 
								check[er] = -1;
							else check[er] = 1;
						} else {
							double total = bestFitLine(v, from, to, null);
							if (total>EPSILON){ 
								check[er] = -1;							
							} else check[er] = 1;
						}
						if (check[er]>0 && tp==s.type && s.radius==r && tp!=0 && Math.abs(cy-sy)<1) {
							found = true;
							break;
						}
					}
				}//end of for			
			}//end of if

			if (found || check[er]>0 && (map[er]>2 || (map[er]>1 && maxPossible<=3) ) ){
				if (rs!=null){
					rs.type = tp;
					if (rs.start==null) 
						rs.start = new Vector2D(fst);
					else rs.start.copy(fst);
					
					if (rs.end==null)
						rs.end = new Vector2D(lst);
					else rs.end.copy(lst);
					rs.num = size;
					rs.points = v;
					rs.radius = r;
					if (tp!=0) {
						if (rs.center==null) 
							rs.center = new Vector2D(center);
						else rs.center.copyValue(center);
					}
					rs.startIndex = from;
					rs.endIndex = to-1;					
				}		
				if (s.map!=null) {
					int sr = (tp==0) ? MAX_RADIUS-1 : (int)Math.round(r+tp*which*tW);
					if (sr>=MAX_RADIUS) sr = MAX_RADIUS-1;
					if (sr>=0){
						if (s.map[sr]==0 && s.appearedRads!=null){
							s.appearedRads[s.radCount++] = sr;
							s.opp.radCount++;
						}
						s.map[sr]+=map[er];
					}
				}
			} else if (rs!=null){
				int max = map[er];
				int maxEr = (int)Math.round(r);
				if (max+max<maxPossible){
					for (int i = count-1;i>=0;--i){
						er = allRads[i];
						if (max<map[er]){
							max = map[er];
							maxEr = er;
							r = (er-Math.round(tW))+tW;
						}
					}	
				}

				if (max>1){
					tp = storage.getType(v, from,to-1);
					if (maxEr>0 && check[maxEr]<0 ){						
						if (!isFirst){
							circle(fst, lst, center.x,center.y, r,center);
						} else {
							double de = which*tp*tW;
							center.x = (tp==-1) ? CircleDriver2.toMiddle-r-de : r+de+CircleDriver2.toMiddle;
							center.y = 0;
						}
						boolean ok = (!inTurn);
						if (ok){
							for (int i=from;i<to;++i){
								Vector2D p = v[i];
								if (p.certain){
									double dx = p.x - center.x;
									double dy = p.y - center.y;
									double d = Math.sqrt(dx*dx+dy*dy)-r;
									if (d<0) d = -d;
									if (d>=EPSILON) {
										ok = false;
										break;
									}
								}
							}
						}
						if (ok || max>2){
							rs.type = tp;
							if (rs.start==null) 
								rs.start = new Vector2D(fst);
							else rs.start.copy(fst);
							
							if (rs.end==null)
								rs.end = new Vector2D(lst);
							else rs.end.copy(lst);
							rs.num = size;
							rs.points = v;
							rs.radius = r;
//							rs.center = center;
							if (tp!=0){
								if (rs.center==null) 
									rs.center = new Vector2D(center);
								else rs.center.copyValue(center);
							}
							rs.startIndex = from;
							rs.endIndex = to-1;		
						}
					}
				}
			}
			for (int i = count-1;i>=0;--i){
				er = allRads[i];
				map[er] = 0;
				check[er] = 0;
			}
			//long endTime = (System.nanoTime()-ti)/1000000;
//			if (CircleDriver2.debug || endTime>=1) System.out.println("End isBelong : "+endTime+"   at "+CircleDriver2.time+" s.    "+n);
			return found;
		}//end of else


//		long endTime = (System.nanoTime()-ti)/1000000;
//		if (CircleDriver2.debug || endTime>=1) System.out.println("End isBelong : "+endTime+"   at "+CircleDriver2.time+" s.    "+n);
		return false;
	}
	
	
	private final static boolean isBelongToEither(Segment s,Segment s1,Vector2D[] v,int from,int to,Segment rs,int which,double tW){
//		long ti = System.nanoTime();
		int n = 0;
		int size = to - from;
		if (size<2) return false;		

		Vector2D fst = v[from];
		Vector2D lst = v[to-1];
		Storage storage = (which==1) ? CircleDriver2.rMap : CircleDriver2.lMap;
		if (size>=3){
			int mr = storage.getMaxRad(from, to-1);
			if (storage.isFound(from,to-1)){									
				int tp = (mr>=Segment.MAX_RADIUS) ? 1 : (mr==0) ? 0 : -1;
				if (tp==1) mr-=Segment.MAX_RADIUS;
				double rad = mr-tp*which*tW;
				rs.type = tp;
				if (rs.start==null) 
					rs.start = new Vector2D(fst);
				else rs.start.copy(fst);
				
				if (rs.end==null)
					rs.end = new Vector2D(lst);
				else rs.end.copy(lst);
				rs.num = size;
				rs.points = v;
				rs.radius = rad;
				if (tp!=0) {
					if (rs.center==null) rs.center = new Vector2D();					
					circle(fst, lst, tp, rad,rs.center);
				}
				rs.startIndex = from;
				rs.endIndex = to-1;
				return true;
			}
			if (storage.isChecked(from, to-1)) {
				int[] map = storage.getMap(from,to-1);				
				if (s.type!=UNKNOWN){
					int sr = (s.type==0) ? 0 : (int)Math.round(s.radius+s.type*which*tW);
					if (sr>=Segment.MAX_RADIUS) sr = Segment.MAX_RADIUS-1;
					if (s.type==1) sr+=Segment.MAX_RADIUS;					
					if (map[mr]>1 && (sr==mr || map!=null && map[sr]>0 && map[sr]>=map[mr])){
						rs.type = s.type;
						if (rs.start==null) 
							rs.start = new Vector2D(fst);
						else rs.start.copy(fst);
						
						if (rs.end==null)
							rs.end = new Vector2D(lst);
						else rs.end.copy(lst);
						rs.num = size;
						rs.points = v;						
						rs.radius = s.radius;
						if (s.type!=0) {
							if (rs.center==null) rs.center = new Vector2D();					
							circle(fst, lst, s.center.x,s.center.y, s.radius,rs.center);
						}
						rs.startIndex = from;
						rs.endIndex = to-1;
						return true;
					}
				}
				
				if (s1.type!=UNKNOWN){
					int sr = (s1.type==0) ? 0 : (int)Math.round(s1.radius+s1.type*which*tW);
					if (sr>=Segment.MAX_RADIUS) sr = Segment.MAX_RADIUS-1;
					if (s1.type==1) sr+=Segment.MAX_RADIUS;					
					if (sr==mr || map!=null && sr>=0 && mr>=0 && map[sr]>0 && map[sr]>=map[mr]){
						rs.type = s1.type;
						if (rs.start==null) 
							rs.start = new Vector2D(fst);
						else rs.start.copy(fst);
						
						if (rs.end==null)
							rs.end = new Vector2D(lst);
						else rs.end.copy(lst);
						rs.num = size;
						rs.points = v;
						rs.radius = s1.radius;
						if (s1.type!=0) {
							if (rs.center==null) rs.center = new Vector2D();					
							circle(fst, lst, s1.center.x,s1.center.y, s1.radius,rs.center);
						}
						rs.startIndex = from;
						rs.endIndex = to-1;
						return true;
					}
				}
				return false;
			}
		}
		
		if (s.type==0){
			double de = s.start.x-s.end.x;
			if (de<0) de = -de;
			if (de<E){
				double e = fst.x-lst.x;
				if (e<0) e = -e;
				if (e<E){
					if (rs!=null){
						rs.type = 0;
						if (rs.start==null) 
							rs.start = new Vector2D(fst);
						else rs.start.copy(fst);
						
						if (rs.end==null)
							rs.end = new Vector2D(lst);
						else rs.end.copy(lst);
						rs.points = v;
						rs.num = size;
						rs.radius = 0;
						rs.startIndex = from;
						rs.endIndex = to-1;
					}
					return true;
				}			
				return false;
			}

			if (s.isPointBelongToSeg(fst) && s.isPointBelongToSeg(lst)){								
				double total = 1;
				double[] coef = new double[2];
				double a = (fst.y-lst.y)/(fst.x-lst.x);
				double b = fst.y - a*fst.x;
				double x1 = (fst.y-b)/a;
				double x2 = (lst.y-b)/a;						

				double tot = 0;
				for (int kk = from;kk<to;++kk){
					Vector2D vv = v[kk];
					tot += Math.sqrt(Geom.ptLineDistSq(x1, fst.y, x2, lst.y, vv.x, vv.y, null));							
				}
				tot /= size;
				if (size>3){							
					total = bestFitLine(v, from,to, coef);
					double t = (total<tot) ? total : tot;
					if ((t>EPSILON && !(s.lower!=null && s.upper!=null) && (s.map==null || s.map[MAX_RADIUS-1]<3)) || (t>=0.5 && tot>=0.5)){								
						return false;
					}
					if (tot>total){
						a = coef[0];
						b = coef[1];
					}
				}


				if (s!=null && s.type!=UNKNOWN) {
					if (size>=3){
						int indx  = (size==3) ? from+1 : (from+to)>>1;
						Vector2D vv =  v[indx];
						double[] temp = new double[3];
						boolean isCircle = Geom.getCircle(fst, lst, vv, temp);
						int rr = (isCircle) ? (int)Math.sqrt(temp[2]) : MAX_RADIUS-1;
						double cx = temp[1];
						double cy = temp[2];
						if (rr>MAX_RADIUS-1) rr = MAX_RADIUS-1;						

						if (rr>=MAX_RADIUS-1 && s.map!=null){							
							s.map[rr]++;
							storage.store(from,indx, to-1, 0, 0);
						} else if (rr>REJECT_VALUE && rr<MAX_RADIUS-1){
							double ax = fst.x-cx;
							double ay = fst.y-cy;
							double bx = lst.x-cx;
							double by = lst.y-cy;
							double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);			
							if (angle<-Math.PI) 
								angle += 2*Math.PI;
							else if (angle>Math.PI) 
								angle -= 2*Math.PI;
										
							int tp = (angle<0) ? -1 : 1;
							storage.store(from,indx, to-1, tp, rr);
							if (s.map==null || s.map[MAX_RADIUS-1]<3){
								return false;
							}							
						}
					}						
				}
				if (rs!=null){
					rs.type = 0;
					if (rs.start==null) 
						rs.start = new Vector2D(fst);
					else rs.start.copy(fst);
					
					if (rs.end==null)
						rs.end = new Vector2D(lst);
					else rs.end.copy(lst);						
					rs.num = size;					
					rs.radius = 0;
					rs.points = v;
					rs.startIndex = from;
					rs.endIndex = to-1;

					if (!Double.isInfinite(a)){
						rs.start.x = (fst.y-b)/a;
						rs.end.x = (lst.y-b)/a;
					}
				}				
				return true;
			}
		} else if (size==2){
			if (rs!=null){
				rs.type = s.type;
				if (rs.start==null) 
					rs.start = new Vector2D(fst);
				else rs.start.copy(fst);
				
				if (rs.end==null)
					rs.end = new Vector2D(lst);
				else rs.end.copy(lst);
				rs.num = size;
				rs.radius = s.radius;
				rs.points = v;
				rs.startIndex = from;
				rs.endIndex = to-1;
				if (rs.type!=0){
					if (rs.center==null) rs.center = new Vector2D();					
					circle(fst, lst, s.center.x,s.center.y, s.radius,rs.center);
//					rs.center = circle(fst, lst, s.center, rs.radius);
				}
			}
			return false;
		} else {								
			int maxPossible = size-2;
			int[] map = tmpAMap;
			int[] allRads = tmpARads;
			int[] check = tmpCheck;			
			double sy = (s!=null && s.type!=Segment.UNKNOWN && s.type!=0 ) ? s.center.y : 0;
			double s1y = (s1!=null && s1.type!=Segment.UNKNOWN && s1.type!=0 ) ? s1.center.y : 0;
			int count = 0;
			int endIndx = to-1;
			boolean inTurn = CircleDriver2.inTurn;
//			double[][][] allRadius = (inTurn) ? (which==1) ? CircleDriver2.allRadiusRight : CircleDriver2.allRadiusLeft : null;
//			double[][][] allCntrx = (inTurn) ? (which==1) ? CircleDriver2.allCntrxR : CircleDriver2.allCntrxL : null;
//			double[][][] allCntry = (inTurn) ? (which==1) ? CircleDriver2.allCntryR : CircleDriver2.allCntryL : null;
//			double[][] bestRad = (inTurn) ? (which==1) ? CircleDriver2.bestFitRadR : CircleDriver2.bestFitRadL : null;			
//			int[][] bestIntRad = (inTurn) ? (which==1) ? CircleDriver2.bestIntFitRadR : CircleDriver2.bestIntFitRadL : null;
//			int[][] maxAppear = (inTurn) ? (which==1) ? CircleDriver2.maxAppearR : CircleDriver2.maxAppearL : null;
//			int[][] tps = (inTurn) ? (which==1) ? CircleDriver2.bestTpR : CircleDriver2.bestTpL : null;
//			int[][][] allTurn = (inTurn) ? (which==1) ? CircleDriver2.allTpR : CircleDriver2.allTpL : null;
			boolean isFirst = s!=null && ((s.type==0 && Math.abs(s.start.x-s.end.x)<=E) || (s.type!=0 && s.type!=Segment.UNKNOWN && s.center!=null && sy==0));
			isFirst = isFirst || (s1!=null && ((s1.type==0 && Math.abs(s1.start.x-s1.end.x)<=E) || (s1.type!=0 && s1.type!=Segment.UNKNOWN && s1.center!=null && s1y==0)));
			int tp = 0;
			boolean found = false;
			double r = 0;
			int er = 0;			
			Vector2D center = tmpCenter;
			final double MARGIN = 0.006;
			double dd1 = 0;
			double dd = 0;
//			double ignoreRad = -1;
			/*if (inTurn){
				tp = tps[from][endIndx];
				if (s!=null && s1!=null && s.type!=Segment.UNKNOWN && s1.type!=Segment.UNKNOWN && tp!=s.type && tp!=s1.type) return false;
				r = Math.round(bestRad[from][endIndx]-tW)+tW;
				er = (int)r;				
				double cy = 0;
				if (tp!=0) {
					center = (s!=null && s.type!=Segment.UNKNOWN && s.type!=0 ) ? Segment.circle(fst, lst, s.center, r) : (s1!=null && s1.type!=Segment.UNKNOWN && s1.type!=0 ) ? Segment.circle(fst, lst, s1.center, r) 										
						: Segment.circle(fst, lst, allCntrx[from][from+1][endIndx],allCntry[from][from+1][endIndx], r);
					cy = center.y;
					if (isFirst && Math.abs(cy)<MARGIN){
						double de = which*tp*tW;
						center.x = (tp==-1) ? CircleDriver2.toMiddle-r-de : r+de+CircleDriver2.toMiddle;
						center.y = 0;
						cy = 0;
					}
				}
				if (tp!=0 && s!=null && ((r==s.radius && tp==s.type && Math.abs(cy-sy)<1) || (tp==s1.type && r==s1.radius && Math.abs(cy-s1y)<1)) ){
					if (s!=null && isFirst){
						if (Math.abs(cy)>MARGIN && Segment.check(v, from, to, center, r)<0) {
							return false;
						} else {
							Vector2D point = (which==-1) ? CircleDriver2.leftMost : CircleDriver2.rightMost;
							double ox = (point.x+lst.x)*0.5d;
							double oy = (point.y+lst.y)*0.5d;
							double nx = point.y - oy  ;
							double ny = ox - point.x;
							Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, 1, 0, tmp1);
							double dx = tmp1[0]-point.x;
							double dy = tmp1[1];
							dd1 = Math.sqrt(dx*dx+dy*dy);
							dd1 = Math.round(dd1*100.0d)/100.0d;
							if (Math.abs(dd1-r)<=0.5) 
								found = true;
							else {
								ox = (fst.x+lst.x)*0.5d;
								oy = (fst.y+lst.y)*0.5d;
								nx = fst.y - oy  ;
								ny = ox - fst.x;
								Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, 1, 0, tmp1);
								dx = tmp1[0]-fst.x;
								dy = tmp1[1];
								dd = Math.sqrt(dx*dx+dy*dy);
								dd = Math.round(dd*100.0d)/100.0d;
								if (Math.abs(dd-r)<=0.5)
									found = true;
							}
						}
					} else found = true;					
				} else if (tp!=0 && r>REJECT_VALUE){
					check[er] = (Segment.check(v, from, to, center, r)<0) ? -1 : 1;
					int max = maxAppear[from][endIndx];
					found = (max==maxPossible && check[er]>0);
					if (found || (s.type==Segment.UNKNOWN && s1.type==Segment.UNKNOWN)){						
						if (!found && max<2) return false;
						if (found || (check[er]>0 && (max>2 || (max>1 && maxPossible<=3)))){
							if (rs!=null){
								rs.type = tp;
								rs.start = new Vector2D(fst);
								rs.end = new Vector2D(lst);
								rs.num = size;
								rs.points = v;
								rs.radius = r;
								if (tp!=0) {
									if (rs.center==null) 
										rs.center = new Vector2D(center);
									else rs.center.copyValue(center);
//									rs.center = center;
								}
								rs.startIndex = from;
								rs.endIndex = to-1;					
							}
							return false;
						} else if (rs!=null){							
							if (check[er]<0 && er>0){						
								if (!isFirst){
									circle(fst, lst, center.x,center.y,r, center);
								} else {
									double de = which*tp*tW;
									center.x = (tp==-1) ? CircleDriver2.toMiddle-r-de : r+de+CircleDriver2.toMiddle;
									center.y = 0;
								}
								boolean ok = (!inTurn);
								if (ok){
									for (int i=from;i<to;++i){
										Vector2D p = v[i];
										if (p.certain){
											double dx = p.x - center.x;
											double dy = p.y - center.y;
											double d = Math.sqrt(dx*dx+dy*dy)-r;
											if (d<0) d = -d;
											if (d>=EPSILON) {
												ok = false;
												break;
											}
										}
									}
								}
								if (ok || max>2 || (max>1 && maxPossible<=3)){
									rs.type = tp;
									rs.start = new Vector2D(fst);
									rs.end = new Vector2D(lst);
									rs.num = size;
									rs.points = v;
									rs.radius = r;
									rs.center = center;
									rs.startIndex = from;
									rs.endIndex = to-1;
									return false;
								}								
								ignoreRad = r;
							}
							
						}//end of else
						if (maxPossible<max+max) return false;
					}//end of if
					if (maxPossible==max) return false;
				}
				

				
				if (!found && tp!=0 && s1!=null && r==s1.radius && tp==s1.type && Math.abs(center.y-s1y)<2){
					if (s1!=null && ((s1.type==0 && Math.abs(s1.start.x-s1.end.x)<=E) || (s1.type!=0 && s1.type!=Segment.UNKNOWN && s1.center!=null && s1y==0))){
						if (Segment.check(v, from, to, s1.center, s1.radius)<0 || Math.abs(center.y)>MARGIN) {
							found = false;
						}
					} else found = true;					
				} 							
			}//*/
			
//			if (center==null) center = new Vector2D();

			if (!found){				
				if (isFirst && (dd==0 || dd1==0)){
					if (dd1==0){
						Vector2D point = (which==-1) ? CircleDriver2.leftMost : CircleDriver2.rightMost;
						double ox = (point.x+lst.x)*0.5d;
						double oy = (point.y+lst.y)*0.5d;
						double nx = point.y - oy  ;
						double ny = ox - point.x;
						Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, 1, 0, tmp1);
						double dx = tmp1[0]-point.x;
						double dy = tmp1[1];
						dd1 = Math.sqrt(dx*dx+dy*dy);
						dd1 = Math.round(dd1*100.0d)/100.0d;
					}
					if (dd==0){
						double ox = (fst.x+lst.x)*0.5d;
						double oy = (fst.y+lst.y)*0.5d;
						double nx = fst.y - oy  ;
						double ny = ox - fst.x;
						Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, 1, 0, tmp1);
						double dx = tmp1[0]-fst.x;
						double dy = tmp1[1];
						dd = Math.sqrt(dx*dx+dy*dy);
						dd = Math.round(dd*100.0d)/100.0d;
					}
				}
				double cx = 0;
				double cy = 0;
				for (int i = from+1;i<endIndx;++i){					
					Vector2D mid = v[i];
					n++;
					boolean isCircle = Geom.getCircle(fst, mid,lst, tmp1);					
					if (isCircle){
				
						r = Math.sqrt(tmp1[2]);							
						cx = tmp1[0];
						cy = tmp1[1];
					
						r = Math.round(r-tW)+tW;
//						if (r==ignoreRad) continue;
//						if (inTurn){
//							tp = allTurn[from][i][endIndx];
//						} else 
						if (r<MAX_RADIUS-1){						
							double ax = fst.x-cx;
							double ay = fst.y-cy;
							double bx = lst.x-cx;
							double by = lst.y-cy;
							double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);			
							if (angle<-Math.PI) 
								angle += 2*Math.PI;
							else if (angle>Math.PI) 
								angle -= 2*Math.PI;
										
							tp = (angle<0) ? -1 : 1;	
						} else {
							r = 0;
							tp = 0;
						}
						
						circle(fst, lst, cx,cy, r,center);
						cx = center.x;
						cy = center.y;
						if (r<=REJECT_VALUE || r+tp*which*tW*2<=REJECT_VALUE) continue;
						double de = which*tp*tW;
						if (isFirst){
							if (Math.abs(cy)>MARGIN) continue;
							if (Math.abs(r-dd1)>0.5 && Math.abs(r-dd)>0.5) continue;
							cx = (tp==-1) ? CircleDriver2.toMiddle-r-de : r+de+CircleDriver2.toMiddle;
							cy = 0;
							center.x = cx;
							center.y = cy;
						}
						
						
						er = (int)Math.round(r);
						if (er>=Segment.MAX_RADIUS) {
							er = 0;
							tp = 0;
						}
						if (r>=tmpL){												
							er = 0;
							tp = 0;
						}
	
						storage.store(from,i, endIndx, tp, r);
	
						if (map[er]==0){
							allRads[count++] = er;
						}
						map[er]++;															
					} else {//else !isCircle
						if (map[0]==0){
							allRads[count++] = 0;
						}
						map[0]++;
						er = 0;
						tp = 0;
					}
					if (map[er]>2) {					
						found = ((s!=null && tp==s.type && tp!=0 && r==s.radius && Math.abs(cy-sy)<2) || (s1!=null && tp==s1.type && tp!=0 && r==s1.radius && Math.abs(cy-s1y)<2) || (tp==0 && ((s!=null && s.type==0) || (s1!=null && s1.type==0))));
						if (found){							
							double EE = EPSILON*3;
							boolean ok = true;
							for (int ii = from;ii<to;++ii){								
								Vector2D vv = v[ii];
								double dx = vv.x-cx;
								double dy = vv.y-cy;			
								double e = Math.sqrt(dx*dx+dy*dy)-r;
								if (e<0) e = -e;
								if (e>EE) {
									ok = false;
									break;
								}
							}
							if (ok) break;
						}			
						if (check[er]>0) break;
					}
					if (map[er]>1) {
						if (er>0 && check[er]>0) {
							found = (s!=null && tp==s.type && tp!=0 && r==s.radius && Math.abs(cy-sy)<2) || (s1!=null && tp==s1.type && tp!=0 && r==s1.radius && Math.abs(cy-s1y)<2) || (tp==0 && ((s!=null && s.type==0) || (s1!=null && s1.type==0)));
							if (found) break;
						}
						continue;
					}
					if (check[er]==0){
						if (er>0) {							
							if (check(v,from,to,center,r)<0) 
								check[er] = -1;
							else check[er] = 1;
						} else {
							double total = bestFitLine(v, from, to, null);
							if (total>EPSILON){ 
								check[er] = -1;							
							} else check[er] = 1;
						}
						if (check[er]>0 && (s!=null && tp==s.type && tp!=0 && r==s.radius && Math.abs(cy-sy)<2) || (s1!=null && tp==s1.type && tp!=0 && r==s1.radius && Math.abs(cy-s1y)<2) || (tp==0 && ((s!=null && s.type==0) || (s1!=null && s1.type==0)))) {
							found = true;
							break;
						} 
					}
				}//end of for
			}//end of if

			if (found || (map[er]>2 && check[er]>0)){
				if (rs!=null){
					rs.type = tp;
					if (rs.start==null) 
						rs.start = new Vector2D(fst);
					else rs.start.copy(fst);
					
					if (rs.end==null)
						rs.end = new Vector2D(lst);
					else rs.end.copy(lst);
					rs.num = size;
					rs.points = v;
					rs.radius = r;
					if (tp!=0) {
//						rs.center = center;
						if (rs.center==null) 
							rs.center = new Vector2D(center);
						else rs.center.copyValue(center);
					}
					rs.startIndex = from;
					rs.endIndex = to-1;					
				}				
				if (s.map!=null) {
					int sr = (tp==0) ? MAX_RADIUS-1 : (int)Math.round(r+tp*which*tW);
					if (sr>=MAX_RADIUS) sr = MAX_RADIUS-1;
					if (sr>=0){
						if (s.map[sr]==0){
							s.appearedRads[s.radCount++] = sr;
							s.opp.radCount++;
						}
						s.map[sr]+=map[er];
					}
				}
			} else if (rs!=null){
				int max = map[er];
				int maxEr = (int)Math.round(r);
				if (max+max<maxPossible){
					for (int i = count-1;i>=0;--i){
						er = allRads[i];
						if (max<map[er]){
							max = map[er];
							maxEr = er;
							r = (er-Math.round(tW))+tW;
						}
					}	
				}	

				if (max>1){
					if (to>from) tp = storage.getType(v, from,to-1);
					if (maxEr>0 && check[maxEr]<0){						
						if (!isFirst){
							circle(fst, lst, center.x,center.y, r,center);
						} else {
							double de = which*tp*tW;
							center.x = (tp==-1) ? CircleDriver2.toMiddle-r-de : r+de+CircleDriver2.toMiddle;
							center.y = 0;
						}
						boolean ok = (!inTurn);
						if (ok){
							for (int i=from;i<to;++i){
								Vector2D p = v[i];
								if (p.certain){
									double dx = p.x - center.x;
									double dy = p.y - center.y;
									double d = Math.sqrt(dx*dx+dy*dy)-r;
									if (d<0) d = -d;
									if (d>=EPSILON) {
										ok = false;
										break;
									}
								}
							}
						}
						if (ok || max>2){
							rs.type = tp;
							if (rs.start==null) 
								rs.start = new Vector2D(fst);
							else rs.start.copy(fst);
							
							if (rs.end==null)
								rs.end = new Vector2D(lst);
							else rs.end.copy(lst);
							rs.num = size;
							rs.points = v;
							rs.radius = r;
//							rs.center = center;
							if (tp!=0){
								if (rs.center==null) 
									rs.center = new Vector2D(center);
								else rs.center.copyValue(center);
							}
							rs.startIndex = from;
							rs.endIndex = to-1;															
						}
					}
				}
			}
			for (int i = count-1;i>=0;--i){
				er = allRads[i];
				map[er] = 0;
				check[er] = 0;
			}
//			long endTime = (System.nanoTime()-ti)/1000000;
//			if (CircleDriver2.debug || endTime>=1) System.out.println("End isBelongToEither : "+endTime+"   at "+CircleDriver2.time+" s.    "+n);
			return found;
		}//end of else
//		long endTime = (System.nanoTime()-ti)/1000000;
//		if (CircleDriver2.debug || endTime>=1) System.out.println("End isBelongToEither : "+endTime+"   at "+CircleDriver2.time+" s.    "+n);
		return false;
	}


	private final static boolean testConnect(Segment prev,Segment s,Vector2D point,double tW,int cutIndx){
		int pStartIndx = prev.startIndex;
		int endIndx = s.endIndex;
		Segment op = prev.opp;
		Segment os = s.opp;
		tmpPrev.reset();
		tmpSeg.reset();
		int which = (tW<0) ? 1 : -1;
		Vector2D[] v = (which==1) ? CircleDriver2.edgeDetector.right : CircleDriver2.edgeDetector.left;
		Vector2D[] opoints = (which==1) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;		
		double tw = tW;
		if (tw<0) tw = -tw;		
		int pNum = cutIndx - pStartIndx;
		Vector2D pCenter = prev.center;		
		double px = point.x;
		double py = point.y;
		boolean pOk = false;
		boolean sOk = false;
		boolean isPrevConfirmed = prev!=null && ((prev.type==0 && Math.abs(prev.start.x-prev.end.x)<E) || (prev.type!=0 && prev.center!=null && prev.center.y==0)); 
		int i = -1;
		if (pNum<2){
			if (op.num<2){ 
				pOk = false;
			} else {	
				double sx = 0;
				double sy = 0;
				int opStartIndx = op.startIndex;										
				if (prev.type==0){
					sx = prev.start.x-op.start.x;
					sy = prev.start.y-op.start.y;
					px -= sx;
					py -= sy;										
				} else if (prev.type!=Segment.UNKNOWN){
					pCenter = prev.center;
					double cx = pCenter.x;
					double cy = pCenter.y;								
					double r = op.radius/prev.radius;
					px = cx + (px-cx)*r;
					py = cy + (py-cy)*r;					
				}
				i = (opStartIndx>os.endIndex) ? opStartIndx : binarySearchFromTo(opoints, py, opStartIndx, os.endIndex);
				if (i<0) 
					i = -i-1;
				else if (py>opoints[i].y) i++;
				if (i-opStartIndx<2) {
					pOk = false;
				} else {					
					pOk = isBelong(op, opoints, opStartIndx, i, tmpPrev, -which, tw);
					if (tmpPrev.type!=Segment.UNKNOWN){
						if (tmpPrev.type!=0){
							pCenter = tmpPrev.center;
							double cx = pCenter.x;
							double cy = pCenter.y;
							double oRadius = tmpPrev.radius;
							tmpPrev.radius += tmpPrev.type*tW*2;
							double r = tmpPrev.radius/oRadius;
							tmpPrev.start.x = cx+(tmpPrev.start.x-cx)*r;
							tmpPrev.start.y = cy+(tmpPrev.start.y-cy)*r;
							tmpPrev.end.x = cx+(tmpPrev.end.x-cx)*r;
							tmpPrev.end.y = cy+(tmpPrev.end.y-cy)*r;
						} else {
							tmpPrev.start.x += sx;
							tmpPrev.start.y += sy;
							tmpPrev.end.x += sx;
							tmpPrev.end.y += sy;
						}
						tmpPrev.points = prev.points;
						tmpPrev.startIndex = pStartIndx;
						int prevIndx = cutIndx-1;
						tmpPrev.endIndex = prevIndx;
						if (prevIndx>=0 && tmpPrev.end.y<v[prevIndx].y-SMALL_MARGIN) tmpPrev.end.copy(v[prevIndx]);
						tmpPrev.num = cutIndx-pStartIndx;
					}
				}
				px = point.x;
				py = point.y;
			}
		} else {					
			pOk = isBelong(prev, v, pStartIndx, cutIndx, tmpPrev, which, tw);
			if (pOk && isPrevConfirmed && !((tmpPrev.type==0 && Math.abs(tmpPrev.start.x-tmpPrev.end.x)<E) || (tmpPrev.type!=0 && tmpPrev.center!=null && tmpPrev.center.y==0))){
				pOk = false;
				return false;
			}
		}


		int sNum = endIndx +1 - cutIndx;
		Vector2D sCenter = s.center;		
		if (sNum<2){
			if (s.num<2) return false;		
			int osEndIndx = os.endIndex;
			double sx = 0;
			double sy = 0;
			if (i<0){
				if (s.type==0){				
					sx = s.start.x-os.start.x;
					sy = s.start.y-os.start.y;
					px -= sx;
					py -= sy;

				} else if (s.type!=Segment.UNKNOWN){
					sCenter = s.center;
					double cx = sCenter.x;
					double cy = sCenter.y;																									
					double r = os.radius/s.radius;
					px = cx + (px-cx)*r;
					py = cy + (py-cy)*r;								
				}
				i = (op.startIndex>osEndIndx) ? op.startIndex : binarySearchFromTo(opoints, py, op.startIndex, osEndIndx);
				if (i<0) 
					i = -i-1;
				else if (py>opoints[i].y) i++;
			}

			if (osEndIndx+1-i<2){ 
				sOk = false;
			} else {				
				sOk = isBelong(os, opoints, i, osEndIndx+1, tmpSeg, -which, tw);
				if (tmpSeg.type!=Segment.UNKNOWN){
					if (tmpSeg.type!=0){
						sCenter = tmpSeg.center;
						double cx = sCenter.x;
						double cy = sCenter.y;
						double oRadius = tmpSeg.radius;						
						tmpSeg.radius += tmpSeg.type*tW*2;
						double r = tmpSeg.radius/oRadius;
						tmpSeg.start.x = cx+(tmpSeg.start.x-cx)*r;
						tmpSeg.start.y = cy+(tmpSeg.start.y-cy)*r;
						tmpSeg.end.x = cx+(tmpSeg.end.x-cx)*r;
						tmpSeg.end.y = cy+(tmpSeg.end.y-cy)*r;
					} else {
						tmpSeg.start.x += sx;
						tmpSeg.start.y += sy;
						tmpSeg.end.x += sx;
						tmpSeg.end.y += sy;
					}
					tmpSeg.points = s.points;
					tmpSeg.startIndex = cutIndx;
					tmpSeg.endIndex = endIndx;
					if (tmpSeg.start.y>v[cutIndx].y+SMALL_MARGIN) tmpSeg.start.copy(v[cutIndx]);
					tmpSeg.num = endIndx+1-cutIndx;
				}
			}
		} else {			
			sOk = isBelong(s, v, cutIndx, s.endIndex+1, tmpSeg, which, tw);						
		}		
		
		if (tmpPrev!=null && tmpSeg!=null && tmpPrev.type!=Segment.UNKNOWN && tmpSeg.type!=Segment.UNKNOWN){
			boolean isConnect = isConnected(tmpPrev, tmpSeg, tW, pt);
			boolean ok = false;

			if (isConnect && pt.y>=tmpPrev.end.y && pt.y<=tmpSeg.start.y){
				ok = true;								
			} else if (isConnect || isPossiblyConnected){
				i = binarySearchFromTo(v, pt.y, pStartIndx, endIndx);
				if (i<0) 
					i = -i-1;
				else if (pt.y>v[i].y) i++;

				if (pt.y<tmpPrev.start.y || pt.y>tmpSeg.end.y || i-pStartIndx<2 || endIndx-i<1){
					if (!isPrevConfirmed && isBelong(tmpSeg, v, pStartIndx, endIndx+1, tmpSeg, which, tw)){
						s.copy(tmpSeg);				
						if (s.map!=null){
							int sr = (s.type==0) ? Segment.MAX_RADIUS-1 : (int)Math.round(s.radius+s.type*which*tw);
							if (sr>=Segment.MAX_RADIUS) sr = Segment.MAX_RADIUS-1;
							if (s.map!=null){
								if (s.map[sr]==0){
									s.appearedRads[s.radCount++] =sr;
									if (s.opp!=null) s.opp.radCount = s.radCount;
								}
								s.map[sr]++;
							}
						}
						prev.type = Segment.UNKNOWN;
					} else if (isBelong(tmpPrev, v, pStartIndx, endIndx+1, tmpPrev, which, tw)){
						if (!isPrevConfirmed || (tmpPrev.type==0 && Math.abs(tmpPrev.start.x-tmpPrev.end.x)<E) || (tmpPrev.type!=0 && tmpPrev.center!=null && tmpPrev.center.y==0)){
							prev.copy(tmpPrev);
							prev.updated = true;
							if (prev.map!=null){
								int pr = (prev.type==0) ? Segment.MAX_RADIUS-1 : (int)Math.round(prev.radius+prev.type*which*tw);
								if (pr>=Segment.MAX_RADIUS) pr = Segment.MAX_RADIUS-1;
								if (prev.map!=null){
									if (prev.map[pr]==0){
										prev.appearedRads[prev.radCount++] =pr;
										if (prev.opp!=null) prev.opp.radCount = prev.radCount;
									}
									prev.map[pr]++;
								}
							}
							s.type = Segment.UNKNOWN;
						}
					} 
					ok = false;
				} 
				if (!ok && isConnect){
					if (!isBelong(tmpSeg, v, i, endIndx+1, tmpSeg, which, tw) || !isBelong(tmpPrev, v, pStartIndx, i, tmpPrev, which, tw)){
						ok = false;
					} else if (isConnected(tmpPrev, tmpSeg, tW, pt) && pt.y>=tmpPrev.end.y && pt.y<=tmpSeg.start.y){
						ok = true;					
					} 				
				}				
			} 

			if (ok){
				if (s.num>=tmpSeg.num && s.radius==tmpSeg.radius && s.type==tmpSeg.type){
					if (tmpSeg.type!=0 && tmpSeg.center!=null){
						s.center.copy(tmpSeg.center);
						os.center.copy(tmpSeg.center);
					}
					Segment.removeFirstPoint(s, os, pt.y);					
				} else {	
//					if (s.radius!=tmpSeg.radius){
//						s.start = new Vector2D(tmpSeg.start);
//						s.end = new Vector2D(tmpSeg.end);
//					}
					Segment.apply(s, tW, tmpSeg.type,tmpSeg.start, tmpSeg.end, tmpSeg.center, tmpSeg.radius);
					if (s.radius==tmpSeg.radius){
						s.startIndex = tmpSeg.startIndex;
						s.endIndex = tmpSeg.endIndex;
						s.num = tmpSeg.num;			
					}
					if (s.type!=0) circle(s.start, s.end, s.center.x,s.center.y, s.radius,s.center);						
						
					if (s.map!=null){
						int sr = (s.type==0) ? Segment.MAX_RADIUS-1 : (int)Math.round(s.radius+s.type*which*tw);
						if (sr>=Segment.MAX_RADIUS) sr = Segment.MAX_RADIUS-1;	
						if (s.map!=null){
							if (s.map[sr]==0){
								s.appearedRads[s.radCount++] =sr;
								if (s.opp!=null) s.opp.radCount = s.radCount;
							}
							s.map[sr]++;
						}
					}
				}

				if (prev.num>=tmpPrev.num && prev.radius==tmpPrev.radius && prev.type==tmpPrev.type){
					if (tmpPrev.type!=0 && tmpPrev.center!=null){
						prev.center.copy(tmpPrev.center);
						op.center.copy(tmpPrev.center);
					}
					Segment.removeLastPoint(prev, op, pt.y);
					prev.updated = true;
				} else {					
//					if (prev.radius!=tmpPrev.radius){
//						prev.start = new Vector2D(tmpPrev.start);
//						prev.end = new Vector2D(tmpPrev.end);
//					}
					Segment.apply(prev, tW, tmpPrev.type,tmpPrev.start, tmpPrev.end, tmpPrev.center, tmpPrev.radius);
					if (prev.radius==tmpPrev.radius){
						prev.startIndex = tmpPrev.startIndex;
						prev.endIndex = tmpPrev.endIndex;
						prev.num = tmpPrev.num;
						prev.updated = true;
					}
					if (prev.type!=0) circle(prev.start, prev.end, prev.center.x,prev.center.y, prev.radius,prev.center);						
				
					if (prev.map!=null){
						int pr = (prev.type==0) ? Segment.MAX_RADIUS-1 : (int)Math.round(prev.radius+prev.type*which*tw);
						if (pr>=Segment.MAX_RADIUS) pr = Segment.MAX_RADIUS-1;	
						if (prev.map!=null){
							if (prev.map[pr]==0){
								prev.appearedRads[prev.radCount++] =pr;
								if (prev.opp!=null) prev.opp.radCount = prev.radCount;
							}
							prev.map[pr]++;
						}
					}
				}								
			}
			return true;

		}


		if (pOk && sOk || (!pOk && tmpPrev!=null && tmpPrev.type!=Segment.UNKNOWN) || (!sOk && tmpSeg!=null && tmpSeg.type!=Segment.UNKNOWN)){					
			if (tmpPrev!=null && tmpSeg!=null && prev.num<=tmpPrev.num && s.num<=tmpSeg.num){
				if (sOk && isConnected(prev, tmpSeg, tW, pt)){
					s.copy(tmpSeg);
					s.opp = os;	
					int sr = (s.type==0) ? Segment.MAX_RADIUS-1 : (int)Math.round(s.radius+s.type*which*tw);
					if (sr>=Segment.MAX_RADIUS) sr = Segment.MAX_RADIUS-1;
//					if (s.map==null) s.map = new int[MAX_RADIUS];
					if (s.map!=null){
						if (s.map[sr]==0){
							s.appearedRads[s.radCount++] =sr;
							if (s.opp!=null) s.opp.radCount = s.radCount;
						}
						s.map[sr]++;
					}
				} else if (pOk && isConnected(tmpPrev, s, tW, pt)){
					prev.copy(tmpPrev);
					prev.opp = op;
					prev.updated = true;
					int pr = (prev.type==0) ? Segment.MAX_RADIUS-1 : (int)Math.round(prev.radius+prev.type*which*tw);
					if (pr>=Segment.MAX_RADIUS) pr = Segment.MAX_RADIUS-1;
//					if (prev.map==null) prev.map = new int[MAX_RADIUS];
					if (prev.map!=null){
						if (prev.map[pr]==0){
							prev.appearedRads[prev.radCount++] =pr;
							if (prev.opp!=null) prev.opp.radCount = prev.radCount;
						}
						prev.map[pr]++;
					}
				} 					
			} 
			return false;
		} else {
			if (pOk && !isConfirmed(s, which, tw) && isBelong(prev, v, pStartIndx, endIndx+1, tmpPrev, which, tw)){
				prev.copy(tmpPrev);
				prev.opp = op;
				prev.updated = true;
				int pr = (prev.type==0) ? Segment.MAX_RADIUS-1 : (int)Math.round(prev.radius+prev.type*which*tw);
				if (pr>=Segment.MAX_RADIUS) pr = Segment.MAX_RADIUS-1;
//				if (prev.map==null) prev.map = new int[MAX_RADIUS];
				if (prev.map!=null){
					if (prev.map[pr]==0){
						prev.appearedRads[prev.radCount++] =pr;
						if (prev.opp!=null) prev.opp.radCount = prev.radCount;
					}
					prev.map[pr]++;
				}
				s.type = Segment.UNKNOWN;		
				os.type = Segment.UNKNOWN;
			} else if (sOk && !isConfirmed(prev, which, tw) && isBelong(s, v, pStartIndx, endIndx+1, tmpSeg, which, tw)){
				s.copy(tmpSeg);
				s.opp = os;
				int sr = (s.type==0) ? Segment.MAX_RADIUS-1 : (int)Math.round(s.radius+s.type*which*tw);
				if (sr>=Segment.MAX_RADIUS) sr = Segment.MAX_RADIUS-1;
//				if (s.map==null) s.map = new int[MAX_RADIUS];
				if (s.map!=null){
					if (s.map[sr]==0){
						s.appearedRads[s.radCount++] =sr;
						if (s.opp!=null) s.opp.radCount = s.radCount;
					}
					s.map[sr]++;
				}
				prev.type = Segment.UNKNOWN;
				op.type = Segment.UNKNOWN;
			}
		}				
		return false;
	}

	private final static void connect(Segment prev,Segment s,Vector2D point,double tW){
		//		if (prev.type!=0 && prev.center!=null && prev.center.y==0) return; 
		long ti = System.nanoTime();
		Vector2D pLast = prev.end;
		Vector2D start = s.start;
		Vector2D[] v = s.points;
		double tw = tW+tW;		
		Vector2D[] o = s.opp.points;
		int pStartIndex = prev.startIndex;
		int pEndIndex = prev.endIndex;
		int startIndex = s.startIndex;
		int endIndex = s.endIndex;		

		double yy = point.y;
		if (yy<prev.start.y || yy>s.end.y) return;		
		int otherNums = (tW<0) ? CircleDriver2.edgeDetector.lSize : CircleDriver2.edgeDetector.rSize;
		Segment os = s.opp;		
		if (s!=null && s.type!=UNKNOWN && os==null) {
			os = new Segment();			
			s.opp = os;
			os.points = o;
			reSynchronize(s, os, 0, otherNums, 1, tw);
		}

		Segment op = prev.opp;
		if (prev!=null && prev.type!=UNKNOWN && op==null) {
			op = new Segment();			
			prev.opp = op;
			op.points = o;
			reSynchronize(prev, op, 0, otherNums, 1, tw);
		}
		//		boolean notAllowed = prev.type!=0 && prev.type!=UNKNOWN && prev.center!=null && prev.center.y==0 && (s.type==0 || s.center.y!=0);		
		if (pLast.y>yy || start.y<yy || pEndIndex+1<startIndex){
			int i = binarySearchFromTo(v, yy, pStartIndex, endIndex);
			if (i<0) 
				i = -i-1;
			else if (yy>v[i].y) i++;
			//			if (notAllowed && i-1<prev.endIndex) return;
			if (testConnect(prev, s, point, tW, i)){
				reSynchronize(s, os, 0, otherNums, 1, tw);
				reSynchronize(prev, op, 0, otherNums, 1, tw);
				boolean isFirstS = (s!=null && s.type!=Segment.UNKNOWN && ((s.type==0 && Math.abs(s.start.x-s.end.x)<E) || (s.type!=0 && s.center.y==0)));
				if (s.type!=UNKNOWN && ((!isFirstS && s.end.y-s.start.y<=1) || (s.num<2 && os.num<2 && !CircleDriver2.inTurn))){
					s.type = Segment.UNKNOWN;
					os.type = Segment.UNKNOWN;
				}
				
				boolean isFirstP = (prev!=null && prev.type!=Segment.UNKNOWN && ((prev.type==0 && Math.abs(prev.start.x-prev.end.x)<E) || prev.type!=0 && prev.center.y==0));
				if (prev.type!=UNKNOWN && ((!isFirstP && prev.end.y-prev.start.y<=1) ||(prev.num<2 && op.num<2 && !CircleDriver2.inTurn))){
					prev.type = Segment.UNKNOWN;
					op.type = Segment.UNKNOWN;
				}
				if (os.type!=Segment.UNKNOWN && op.type!=Segment.UNKNOWN && op.end.y>=os.start.y-SMALL_MARGIN){
					Segment.removeFirstPoint(os, s, op.end.y+SMALL_MARGIN);
					reSynchronize(os, s, 0, s.endIndex+1, -1, tw);
				}
			} else {
				prev.upper = null;
				s.lower = null;				
			}
			long endTime = (System.nanoTime()-ti)/1000000;
			if (CircleDriver2.debug || endTime>=1) System.out.println("End connect : "+endTime+"   at "+CircleDriver2.time+" s.");
			return;
		}
	}

	/*private final static void calibrateConnected(Segment s,int index,Segment last,int lIndex,Vector2D[] v,double tW,int[] startArr,int[] endArr,Vector2D point){
		double yy = point.y;
		int start = (index>=0) ? s.startIndex : -1;
		int end = (index>=0) ? s.endIndex : -1;
		int nStart =(lIndex>=0) ? last.startIndex : -1;
		int nEnd =(lIndex>=0) ? last.endIndex : -1;

		if (s.end.y>yy+SMALL_MARGIN && index>=0){	
			Segment bkupS = new Segment(s);
			Segment bkupL = new Segment(last);
			Segment.removeLastPoint(s, s.opp, null, point);
			int i = s.endIndex+1;
			if (!last.reCalculate(v, i, nEnd+1, tW)) {
				s.copy(bkupS);
				return;			
			}
			if (s.reCalculate(v, start, i, tW) && isConnected(s, last, tW, point)){
				yy = point.y;
				end = i-1;
				nStart = i;
				s.updated = true;
				last.updated = true;							
			} else {				
				s.copy(bkupS);
				last.copy(bkupL);
			}
		} else if (last.start.y<yy-SMALL_MARGIN && lIndex>=0){			
			Segment bkupS = new Segment(s);
			Segment bkupL = new Segment(last);
			Segment.removeFirstPoint(last, last.opp, null, point);
			int i = last.startIndex;
			if (!s.reCalculate(v, start, i, tW)) {
				last.copy(bkupL);
				return;			
			}
			if (last.reCalculate(v, i, nEnd, tW) && isConnected(s, last, tW, point)){
				yy = point.y;
				nStart = i;
				end = i-1;
				s.updated = true;
				last.updated = true;							
			} else {			
				s.copy(bkupS);
				last.copy(bkupL);
			}
		} else {//end of if
			s.updated = false;
			last.updated = false;
		}

		if (index>=0 && lIndex>index+1){
			Segment bk = new Segment();
			for (int i=end+1;i<nStart;++i){
				Vector2D vv = v[i];				
				if (vv.y<=yy){
					if (s.upper!=null || s.isPointBelongToSeg(vv)){
						bk.copy(last);
						double sR = s.radius;
						double lR = last.radius;
						Vector2D en = s.end;
						//						s.end = new Vector2D(vv);
						//						s.num++;																		
						if (i+1 != last.startIndex && !last.reCalculate(v, i+1, last.endIndex, tW)) continue;
						if (!s.reCalculate(v, start, i+1, tW)) {
							last.copy(bk);
							continue;	
						}
						if (isConnected(s, last, tW, point)){
							yy = point.y;
							end = i;
							s.updated = true;
							if (i+1!=nStart){
								nStart = i+1;
								last.updated = true;
							} else last.updated = false;
							break;
						}  else if (sR!=s.radius){
							if (lR!=last.radius){
								double nR = last.radius;
								Vector2D nCentr = (last.center==null) ? null : new Vector2D(last.center);
								last.radius = lR;
								last.center = circle(last.start, last.end, last.center, lR);
								if (isConnected(s, last, tW, point)){
									yy = point.y;
									nStart = i;		
									s.updated = true;	
									if (i+1!=nStart){
										nStart = i+1;
										last.updated = true;
									} else last.updated = false;
									break;
								} else {
									s.radius = sR;								
									s.center = circle(s.start, s.end, s.center, sR);
									if (isConnected(s, last, tW, point)){
										yy = point.y;
										nStart = i;		
										s.updated = true;	
										if (i+1!=nStart){
											nStart = i+1;
											last.updated = true;
										} else last.updated = false;
										break;
									} else {
										last.radius = nR;
										last.center= nCentr;
										if (isConnected(s, last, tW, point)){
											yy = point.y;
											nStart = i;		
											s.updated = true;	
											if (i+1!=nStart){
												nStart = i+1;
												last.updated = true;
											} else last.updated = false;
											break;
										}
									}
								}
							} else {					
								if (isConnected(s, last, tW, point)){
									yy = point.y;
									nStart = i;		
									s.updated = true;	
									if (i+1!=nStart){
										nStart = i+1;
										last.updated = true;
									} else last.updated = false;
									break;
								}
							}
						} 	
						//Restore now
						s.num = end-start+1;
						s.end = en;
						int[] map = s.map;
						if (map!=null)
							if (s.radius>=MAX_RADIUS || s.type==0) 
								map[MAX_RADIUS-1]--;
							else map[(int)Math.round(s.radius-s.type*tW)]--;

						s.radius = sR;		
						if (i+1!=nStart) last.copy(bk);

					}
				} else {
					if (last.lower!=null || last.isPointBelongToSeg(vv)){
						bk.copy(s);
						double sR = s.radius;
						Vector2D sCntr = (s.center==null) ? null : new Vector2D(s.center);
						double lR = last.radius;		
						Vector2D st = last.start;
						//						last.start = new Vector2D(vv);
						//						last.num++;																			
						if (i!=s.endIndex+1 && !s.reCalculate(v, start, i, tW)) continue;
						if (!last.reCalculate(v, i, nEnd+1, tW)) {
							last.copy(bk);
							continue;
						}

						if (isConnected(s, last, tW, point)){
							yy = point.y;
							nStart = i;		
							last.updated = true;
							if (i!=end+1){
								s.endIndex = i-1;
								s.updated = true;
							} else s.updated = false;
							break;
						} else if (lR!=last.radius){
							double nR = last.radius;
							Vector2D nCentr = (last.center==null) ? null : new Vector2D(last.center);
							last.radius = lR;
							last.center = circle(last.start, last.end, last.center, lR);
							if (isConnected(s, last, tW, point)){
								yy = point.y;
								nStart = i;		
								last.updated = true;
								if (i!=end+1){
									s.endIndex = i-1;
									s.updated = true;
								} else s.updated = false;
								break;
							} else if (sR!=s.radius){
								s.radius = sR;
								s.center = sCntr;
								if (isConnected(s, last, tW, point)){
									yy = point.y;
									nStart = i;		
									last.updated = true;
									if (i!=end+1){
										s.endIndex = i-1;
										s.updated = true;
									} else s.updated = false;
									break;
								} else {
									last.radius = nR;
									last.center = nCentr;
									if (isConnected(s, last, tW, point)){
										yy = point.y;
										nStart = i;		
										last.updated = true;
										if (i!=end+1){
											s.endIndex = i-1;
											s.updated = true;
										} else s.updated = false;
										break;
									}
								}

							}
						} if (sR!=s.radius){
							s.radius = sR;
							s.center = sCntr;
							if (isConnected(s, last, tW, point)){
								yy = point.y;
								nStart = i;		
								last.updated = true;
								if (i!=end+1){
									s.endIndex = i-1;
									s.updated = true;
								} else s.updated = false;
								break;
							}
						}
						//Restore now
						last.num = nEnd-nStart+1;
						last.start = st;
						int[] map = last.map;
						if (map!=null)
							if (last.radius>=MAX_RADIUS || last.type==0) 
								map[MAX_RADIUS-1]--;
							else map[(int)Math.round(last.radius-last.type*tW)]--;

						last.radius = lR;																			
						if (i!=end+1)
							s.copy(bk);						
					}
				}
			}
		}
		if (s.upper==null ) 
			s.upper = new Vector2D(point);
		else {
			s.upper.x = point.x;
			s.upper.y = point.y;
		}
		last.lower = point;
		if (index>=0) startArr[index] = s.startIndex = start;
		if (index>=0) endArr[index] = s.endIndex = end;
		if (lIndex>=0) startArr[lIndex] = last.startIndex = nStart;
		if (lIndex>=0) endArr[lIndex] = last.endIndex = nEnd;
	}//*/

	/*private static final int checkPN(final Vector2D[] v,Segment[] rsArr,int l,double tW,int sz,ObjectArrayList<Segment> other){		
		if (l>=1 && l<sz){			
			Segment s = rsArr[l-1];
			//			if (l==1 && s.type==0) return;
			Segment last = rsArr[l];
			int which = (tW<0) ? 1 : -1;
			Vector2D[] opoints = (which==1) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;
			int otherTo = (which==1) ? CircleDriver2.edgeDetector.lSize:  CircleDriver2.edgeDetector.rSize;
			Vector2D pt = new Vector2D();
			int indx = l-1;
			//			int which = (tW<0) ? 1 : -1;
			while (s.type==UNKNOWN){
				if (indx>0) 
					s = rsArr[--indx];
				else break;
			}
			if (s.type!=UNKNOWN){
				if (s.type==last.type && s.radius==last.radius && (s.type==0 || s.center.distance(last.center)<2)){
					s.end = last.end;
					s.endIndex = last.endIndex;
					s.num = s.endIndex - s.startIndex + 1;
					Vector2D fst = (s.num<2) ? s.start : s.points[s.startIndex];
					Vector2D lst = (s.num<2) ? s.end : s.points[s.endIndex];
					if (s.type!=0 && s.center!=null) s.center = circle(fst, lst, s.center, s.radius);
					int sR = (s.type==0) ? MAX_RADIUS-1 : (int)(s.radius-s.type*tW);
					int max = sR+10;
					if (max>=MAX_RADIUS-1) max = MAX_RADIUS-1;
					int[] map = s.map;
					int[] lmap = last.map;					
					int val;
					if (map==null && lmap==null){ 
						map = new int[MAX_RADIUS];
						map[sR]+=2;
						s.map = map;
					} else if (map==null){
						map = lmap;
						map[sR]++;
						s.map = map;
					} else if (lmap==null){
						map[sR]++;
					} else {
						for (int i=20;i>=0;--i){
							int idx = max-i; 
							if (map[idx] < (val = lmap[idx])) map[idx] = val;
						}
					}															
					if (last.upper!=null) s.upper = last.upper;
					s.updated = true;					
					if ((sz-=l+1)>0)
						System.arraycopy(rsArr, l+1, rsArr, indx+1, sz);											
					sz += indx+1;
					l = indx;
					last = s;
					if (l>=1 && l<sz){
						s = rsArr[l-1];
						//						if (l==1 && s.type==0) return;
						indx = l-1;
						while (s.type==UNKNOWN){
							if (indx>0) 
								s = rsArr[--indx];
							else break;
						}
					}
					if (s.type==UNKNOWN) return sz;
				}

				boolean isConnected = (s!=null && last!=null && isConnected(s, last, tW, pt));				
				if (isConnected) {
					if (s.opp==null) {
						s.opp = new Segment();
						s.opp.points = opoints;
						reSynchronize(s,s.opp,0,otherTo,1,tW*2);
					}					
					if (last.opp==null) {
						last.opp = new Segment();
						last.opp.points = opoints;
						reSynchronize(last,last.opp,0,otherTo,1,tW*2);
					}

					connect(s, last, pt.y, tW);
					//					calibrateConnected(s, indx, last, l, v, tW, startArr, endArr, pt);
					//					if (s.updated && s.type!=UNKNOWN){						
					//						if (s.opp==null) {
					//							s.opp = new Segment();
					//							s.opp.points = opoints;
					//						}
					//						reSynchronize(s,s.opp,0,otherTo,1,tW*2);														
					//					}
					//					if (last.updated && last.type!=UNKNOWN){						
					//						if (last.opp==null) {
					//							last.opp = new Segment();
					//							last.opp.points = opoints;
					//						}
					//						reSynchronize(last,last.opp,0,otherTo,1,tW*2);														
					//					}
					//					s.upper = pt;
					//					last.lower = pt;				
					int sI = s.endIndex+1;
					int eI = last.startIndex-1;
					int size = l-indx-1;
					if (sI>eI && indx<l-1){
						if ((sz-=size)>0)
							System.arraycopy(rsArr, l, rsArr, indx+1, size);									
						l = indx+1;
					} else if (sI<=eI){
						if (indx<l-2){
							size = l-indx-2;
							int np = indx+2;
							if ((sz-=size)>0)
								System.arraycopy(rsArr, l, rsArr, np, size);														
							l = np;
						}
						int pos = indx+1;
						if (l==indx+1){
							Segment ns = new Segment();
							ns.start = new Vector2D(v[sI]);
							ns.end = new Vector2D(v[eI]);
							ns.num = eI-sI+1;						
							int np = pos +1;
							if ((sz-=pos)>0)							
								System.arraycopy(rsArr, pos, rsArr, np, sz);

							sz+=np;
							ns.startIndex = sI;
							ns.endIndex = eI;
							rsArr[pos] = ns;										
						} else {						
							Segment ns = rsArr[pos];
							ns.start = new Vector2D(v[sI]);
							ns.end = new Vector2D(v[eI]);
							ns.startIndex = sI;
							ns.endIndex = eI;
							ns.num = eI-sI+1;						
						}						
					}
				} else if (isPossiblyConnected && (isPrevNextConnected || isNextPrevConnected)) {
					//					int kn = l+1;
					//					if (kn<sz){
					//						for (;kn<sz;++kn) 
					//							if (rsArr[kn].type!=UNKNOWN) break;
					//					}
					//					Segment next = (kn<sz) ? rsArr[kn] : null;
					//					ObjectArrayList<Segment> oal = ObjectArrayList.wrap(rsArr,sz);
					//					IntArrayList starts = IntArrayList.wrap(startArr,sz);
					//					IntArrayList ends = IntArrayList.wrap(endArr,sz);
					//					expand(v, startArr[indx], endArr[indx], s, indx, last, l, next, kn, oal, starts,ends, tW, true,other);
				} else {
					s.upper = null;
					last.lower = null;
				}
			} 
		} 
		return sz;
	}//*/

	private static final int checkRs(final Vector2D[] v,Segment[] rsArr,int l,double tW,int sz){		
		if (l>=1 && l<sz){
			int which = (tW<0) ? 1 : -1;
			Vector2D[] opoints = (which==1) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;
			int otherTo = (which==1) ? CircleDriver2.edgeDetector.lSize:  CircleDriver2.edgeDetector.rSize;
			Segment s = rsArr[l-1];
			//			if (l==1 && s.type==0) return;
			Segment last = rsArr[l];			
			Vector2D pt = new Vector2D();
			int indx = l-1;
			while (s==null || s.type==UNKNOWN){
				if (indx>0) 
					s = rsArr[--indx];
				else break;
			}
			if (s.type!=UNKNOWN){
				if (s.type==last.type && s.radius==last.radius && (s.type==0 || s.center.distance(last.center)<2)){
					s.end = last.end;
					s.endIndex = last.endIndex;
					s.num = s.endIndex - s.startIndex+1;					
					int sR = (s.type==0) ? MAX_RADIUS-1 : (int)(s.radius-s.type*tW);
					int max = sR+10;
					if (max>=MAX_RADIUS-1) max = MAX_RADIUS-1;
					int[] map = s.map;
					int[] lmap = last.map;					
					int val;
					if (map==null && lmap==null){ 
						map = new int[MAX_RADIUS];
						map[sR]+=2;
						s.map = map;
					} else if (map==null){
						map = lmap;
						map[sR]++;
						s.map = map;
					} else if (lmap==null){
						map[sR]++;
					} else {
						for (int i=20;i>=0;--i){
							int idx = max-i; 
							if (map[idx] < (val = lmap[idx])) map[idx] = val;
						}
					}
					if (!s.reCalculate(v, s.startIndex, s.endIndex+1, tW)){
						System.out.println("Check out reCalculate************************************************************");
					}
					if (last.lower!=null && s.lower==null) s.lower = last.lower;
					if (last.upper!=null) s.upper = last.upper;
					s.updated = true;
					int size = l-indx;					
					if ((sz-=size)>0 && rsArr!=null){
//						if (l+1<0 || indx+1<0 || l+1>rsArr.length || indx+1>rsArr.length || l+1+size>rsArr.length || indx+1+size>rsArr.length)
//							System.out.println();
						System.arraycopy(rsArr, l+1, rsArr, indx+1, size);
					}

					l = indx;
					last = s;
					if (l>=1 && l<sz){
						s = rsArr[l-1];
						//						if (l==1 && s.type==0) return;
						indx = l-1;
						while (s.type==UNKNOWN){
							if (indx>0) 
								s = rsArr[--indx];
							else break;
						}
					}
					if (s.type==UNKNOWN) return sz;
				}

				boolean isConnected = (s!=null && last!=null && isConnected(s, last, tW, pt));
				if (isConnected) {
					if (s.opp==null) {
						s.opp = new Segment();
						s.opp.points = opoints;
						reSynchronize(s,s.opp,0,otherTo,1,tW*2);
					}					
					if (last.opp==null) {
						last.opp = new Segment();
						last.opp.points = opoints;
						reSynchronize(last,last.opp,0,otherTo,1,tW*2);
					}
					Vector2D pLast = s.end;
					Vector2D start = last.start;
					double yy = pt.y;
					if (pLast.y>yy || start.y<yy || s.endIndex+1<last.startIndex)
						connect(s, last, pt, tW);
					//					calibrateConnected(s, indx, last, l, v, tW, startArr, endArr, pt);
					s.upper = pt;
					last.lower = pt;				
					int sI = s.endIndex+1;
					int eI = last.startIndex-1;
					int size = l-indx-1;
					if (sI>eI && indx<l-1){
						if ((sz-=size)>0)
							System.arraycopy(rsArr, l, rsArr, indx+1, size);										
						l = indx+1;
					} else if (sI<=eI){
						if (indx<l-2){
							size = l-indx-2;
							int np = indx+2;
							if ((sz-=size)>0)
								System.arraycopy(rsArr, l, rsArr, np, size);														
							l = np;
						}
						int pos = indx+1;
						if (l==indx+1){
							Segment ns = new Segment();
							ns.start = new Vector2D(v[sI]);							
							ns.end = new Vector2D(v[eI]);							
							ns.num = eI-sI+1;						
							int np = pos +1;
							if ((sz-=pos)>0)							
								System.arraycopy(rsArr, pos, rsArr, np, sz);								
							sz+=np;
							ns.startIndex = sI;
							ns.endIndex = eI;
							rsArr[pos] = ns;										
						} else {						
							Segment ns = rsArr[pos];
							ns.start = new Vector2D(v[sI]);
							ns.end = new Vector2D(v[eI]);
							ns.num = eI-sI+1;			
							ns.startIndex = sI;
							ns.endIndex = eI;
						}
						Segment ns = rsArr[pos];
						ns.startIndex = sI;
						ns.endIndex = eI;

					}
				}
			}

			if (l<1) return sz; 
			s = rsArr[l-1];
			int sR = (s==null || s.type==UNKNOWN) ? 0 : (s.type==0) ? MAX_RADIUS-1 : (int)Math.round(s.radius-s.type*tW);
			boolean ok = false;
			if (s!=null && (s.type==Segment.UNKNOWN || (ok = (s.num<=3 && (s.map==null || s.map[sR]<3))) ) && last.type!=Segment.UNKNOWN){									
				int len = s.num;
				int startIndex = s.startIndex; 
				int index = startIndex+len-1;

				while (index>=startIndex){
					Vector2D point = v[index--];
					if (index==0 && l==1 && last.type==0) break;
					if (!last.isPointBelongToSeg(point)) break;					
					s.endIndex = index;
					s.end = (index>=0) ? new Vector2D(v[index]) : null;
					s.num--;					
					last.startIndex = index+1;
					last.start = new Vector2D(v[index+1]);
					last.num++;
					s.updated = true;
					last.updated = true;
					if (ok && s.num<3) s.type = Segment.UNKNOWN;					
					if (s.type==Segment.UNKNOWN) {
						s.radius = -1;
						//						s.map = null;
					}

				}
				boolean changed = (len!=s.num);
				if (s.type!=Segment.UNKNOWN && changed && s.num>0){
					double oldr = s.radius;
					if (!s.reCalculate(v,s.startIndex,s.endIndex+1,tW)){
						System.out.println("Check out reCalculate************************************************************");
					}
					if (l-1>=1 && Math.abs(oldr-s.radius)>0.5) sz = checkRs(v,rsArr, l-1,tW,sz);
				}

				if (changed && s.num<=0) {			
					if ((sz-=l)>0)
						System.arraycopy(rsArr, l, rsArr, l-1, sz);						
					sz+=l-1;				
				}
			}
		}	
		return sz;
	}

	private static final int lastCheck(final Vector2D[] v,Segment[] rsArr,double tW,int sz){
		int i=0;		
		boolean found = false;
		Vector2D center = new Vector2D();
		for (i = sz-1;i>=0;--i){
			Segment s = rsArr[i];

			if (s==null || s.type==Segment.UNKNOWN)	
				continue;		
			if (s.type!=0 && s.center!=null && s.center.y==0 && i>0){
				int endIdx = s.startIndex;
				int indx = 0;
				for (int j=0;j<endIdx;++j){
					Vector2D p = v[j];
					if (p.y<0) continue;
					if (s.isPointBelongToSeg(p)){
						s.startIndex = j;
						s.num = s.endIndex+1-s.startIndex;
						s.start = new Vector2D(p);
						circle(p, s.end, s.center.x,s.center.y, s.radius,center);
						if (center.y==0) s.center.x = center.x;
						if (j>0){
							Segment ns = new Segment();
							ns.start = new Vector2D(v[0]);
							ns.end = new Vector2D(v[j-1]);
							ns.startIndex = 0;
							ns.center = center;
							ns.endIndex = j-1;
							ns.num = j;
							rsArr[0] = ns;								
							indx = 1;
						}
						break;
					}
				}
				if (indx<i){
					System.arraycopy(rsArr, i, rsArr, indx, sz-=i);
					sz+=indx;
				}
				i = indx;
				break;
			}
		}
		for (i=0;i<sz-1;++i){
			Segment s = rsArr[i];

			if (s==null || s.type!=Segment.UNKNOWN)	
				continue;			
			Segment next = rsArr[i+1];
			while (next!=null && next.type==Segment.UNKNOWN){							
				//				for (int kk = 0 ; kk<next.num;++kk) {
				//					s.addPoint(next.points[kk]);
				//				}				
				s.end = (next.end!=null) ? new Vector2D(next.end) : null;
				s.num += next.num;
				s.endIndex = next.endIndex;
				if (s.end==null && s.num>0) s.end = new Vector2D(v[s.endIndex]);
				if (sz>i+2)
					System.arraycopy(rsArr, i+2, rsArr, i+1, sz-i-2);

				sz--;				
				found = true;
				if (i>=sz-1) break;
				next = rsArr[i+1];
			}			
		}
		if (found && sz>1)
			for (i=0;i<sz;++i){
				Segment s = rsArr[i];
				if (s!=null && s.type==Segment.UNKNOWN && s.num>=3){										
					Segment[] olArr = new Segment[20];					
					ObjectArrayList<Segment> ol = segmentize(v,s.startIndex,s.endIndex+1, tW,ObjectArrayList.wrap(olArr, 0));					
					int olSz = ol.size();
					if (olSz==1){
						s.copy(olArr[0]);						
						if (s.type!=UNKNOWN){
							int oldSz = sz;
							sz = checkRs(v, rsArr, i, tW, sz);
							if (oldSz!=sz)
								i -= oldSz - sz;								
						}
					} else {
						if (sz>i+1)
							System.arraycopy(rsArr, i+1, rsArr, i, sz-i-1);							
						sz--;						
						if (sz>i+1)							
							System.arraycopy(rsArr, i+1, rsArr, i+1+olSz, sz-i-1);							
						System.arraycopy(olArr, 0, rsArr, i, olSz);						
						sz+=olSz;
						for (int j = i;j<olSz+i;++j){
							s = rsArr[j];
							if (s==null) continue;
							if (s.type!=UNKNOWN){
								int oldSz = sz;
								sz = checkRs(v, rsArr, j, tW,sz);
								if (oldSz!=sz) {
									j -= oldSz - sz;
									olSz -= oldSz-sz;
								}
							}
						}
					}
				} 								
			}
		return sz;
	}


	public static final double guessRadius(Vector2D l,Vector2D r,double d,Vector2D center){
		double c = r.y;
		double b = l.y;
		double a = r.x - l.x;
		double a2 = a*a;
		double b2 = b*b;
		double c2 = c*c;
		double d2 = d*d;
		double E = (a2+b2+c2-d2)/2;
		double A = a2-d2;
		double B = a*(a2+b2-c2-d2);
		double C =  E*E-c2*(a2+b2);
		double delta = B*B-4*A*C;
		if (delta<0) return -1;
		double X1 = 0;
		double X2 = 0;
		if (a==d){
			X1 = X2 = -C/a/(b2-c2);
		} else if (delta==0){
			X1 = X2 = -B*0.5d/A;
		} else {
			double deltaSqrt = Math.sqrt(delta);
			X1 = (-B-deltaSqrt)*0.5d/A;
			X2 = (-B+deltaSqrt)*0.5d/A;
		}
		if (center!=null){
			center.x = X1+r.x;
			center.y = 0;
			//			double rr1 = 0.5d*(center.distance(l)+center.distance(r));
			center.x = X2+r.x;
			//			double rr2 = 0.5d*(center.distance(l)+center.distance(r));
			//			System.out.println();
		}
		return 0;
	}

	public static final ObjectArrayList<Segment> segmentize(final Vector2D[] v,int from, int to,double tW,ObjectArrayList<Segment> rs){
		//		long ti = System.nanoTime();
		if (CircleDriver2.debug) System.out.println("start seg time : "+((System.nanoTime()-CircleDriver2.ti)/1000000)+" ms.");
		if (rs==null) rs = ObjectArrayList.wrap(new Segment[30],0);
		int len = to - from;
		int count = 0;
		double[] coef = new double[2];
//		boolean inTurn = CircleDriver2.inTurn;		
//		int which = (tW<0) ? 1 : -1;
		//		Vector2D[] opoints = (which==1) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;
		//		int otherTo = (which==1) ? CircleDriver2.edgeDetector.lSize:  CircleDriver2.edgeDetector.rSize;
//		double[][][] allRadius = (inTurn) ? (which==1) ? CircleDriver2.allRadiusRight : CircleDriver2.allRadiusLeft : null;
//		double[][][] allCntrx = (inTurn) ? (which==1) ? CircleDriver2.allCntrxR : CircleDriver2.allCntrxL : null;
//		double[][][] allCntry = (inTurn) ? (which==1) ? CircleDriver2.allCntryR : CircleDriver2.allCntryL : null;
		//		int[][][] allTurn = (inTurn) ? (which==1) ? CircleDriver2.allTpR : CircleDriver2.allTpL : null;

		if (v==null || len<=2){
			Segment s = new Segment();
			s.start = new Vector2D(v[from]);
			s.startIndex = from;
			s.end = new Vector2D(v[to-1]);
			s.endIndex = to - 1;
			s.num = len;
			s.points = v;
			rs.add(s);
			return rs;
		}		

		int sz = rs.size();
		Segment[] rsArr = rs.elements();
		Segment prev = null;
		int i=0;
		final double allowedDist = TrackSegment.EPSILON*TrackSegment.EPSILON;
		final double E = (CircleDriver2.inTurn) ? 0.1*TrackSegment.EPSILON : TrackSegment.EPSILON*0.5;
		double x0 = v[0+from].x;
		for (i = 1+from;i<to;++i){
			if (Math.abs(v[i].x-x0)>=0.1*TrackSegment.EPSILON) break;
		}
		if (i>1+from){			
			Segment s = new Segment(0, x0, v[from].y, x0, v[i-1].y,0,0,Double.MAX_VALUE);
			s.startIndex = from;
			s.endIndex = i-1;			
			rsArr[sz++] = s;
			s.num = i-from;
			s.points = v;
			prev = s;
			if (i==to) {
				rs.size(sz);
				return rs;
			}
		} else i = 0+from;
		double xx=v[i].x;
		double yy=v[i].y;		
		while (i<=to-1){
			if (i>=to-2){
				/*Segment last = (sz>0) ? rsArr[sz-1] :null;				
				Segment s = (last!=null && last.type==Segment.UNKNOWN) ? last :  new Segment();
				if (s==last){					
					if (s.startIndex>i) {
						s.startIndex = i;
						s.start = new Vector2D(v[i]);
					}
					if (s.endIndex<to-1) s.endIndex = to-1;
					s.end = new Vector2D(v[to-1]);
					s.num = to-s.startIndex;
				} else {
					s.start = new Vector2D(v[i]);
					s.end = new Vector2D(v[to-1]);
					s.startIndex = i;
					s.endIndex = to-1;					
					s.num = to-i;
					rsArr[sz++] = s;
				}								
				//				for (int k=i;k<to;++k) s.addPoint(v[k]);

				s.points =v;
				sz = lastCheck(v,rsArr,tW,sz);//*/				
				break;
			}

			double[] result = new double[3];
			double x1 = xx;
			double x2 = v[i+1].x;			
			double y1 = yy;
			double y2 = v[i+1].y;
			double x3 = v[i+2].x;
			double y3 = v[i+2].y;
			boolean isCircle =  Geom.getCircle(x1, y1, x2, y2, x3, y3, result);
			double radius = (isCircle) ?  Math.sqrt(result[2]) : Double.MAX_VALUE;			


			int j=i+3;			
			if (!isCircle || radius>=Segment.MAX_RADIUS){//is a straight line				
				Segment ns = new Segment();
				ns.type = 0;
				ns.start = new Vector2D(x1,y1);
				ns.end = new Vector2D(x3,y3);
				ns.num = 3;
				ns.radius = Double.MAX_VALUE;	
				ns.points = v;

				if (count>0){
					boolean ok = false;
					for (int k = count-1;k>=0;--k){
						Segment t = tmpS[k];
						if (t.type==0 && isSameSegment(t, ns)){
							int indx = tmpI[k];								
							for (j=i+3;j>=i;--j){
								double total = bestFitLine(v, indx, j, coef);
								if (total>=EPSILON) continue;
								ok = true;
								break;
							}
							if (!ok) continue;
							if (ns.map==null) ns.map = new int[MAX_RADIUS];						
							ns.map[MAX_RADIUS-1]++;																									
							double a = coef[0];
							double b = coef[1];			
							i = indx;
							Vector2D vv = v[indx];
							x1 = vv.x;
							y1 = vv.y;
							vv = v[indx+1];
							x2 = vv.x;
							y2 = vv.y;
							vv = v[j-1];
							xx = vv.x;
							yy = vv.y;

							if (!Double.isInfinite(a)){
								x1 = (y1-b)/a;
								xx = (yy-b)/a;
							}

							for (;j<to;++j){
								vv = v[j];
								double x = vv.x;
								double y = vv.y;					
								if (Geom.ptLineDistSq(x1, y1, xx, yy, x, y, null)>allowedDist)
									break;		
								boolean isCir = Geom.getCircle(x1, y1, x2, y2, x, y, result);
								double r = Math.sqrt(result[2]);
								if (isCir && r>REJECT_VALUE && r<MAX_RADIUS-1) break;
								xx = x;
								yy = y;
								if (j-i>=3){
									coef = new double[2];						
									double total = bestFitLine(v, i, j, coef);
									if (total>=EPSILON) break;												
									a = coef[0];
									b = coef[1];																					
									if (!Double.isInfinite(a)){
										x1 = (y1-b)/a;
										xx = (yy-b)/a;
									}
								}
							}

							ns.num = j-i;
							ns.start.x = x1;
							ns.start.y = y1;
							ns.end.x = xx;
							ns.end.y = yy;							
							ns.points = v;

							//							Segment last = (sz>0) ? rsArr[sz-1] :null;
							//							if (last!=null && last.type==UNKNOWN){
							//								if ((last.num = indx-last.startIndex)>0){
							//									last.end = new Vector2D(v[indx-1]);
							//									last.endIndex = indx-1;
							//								} else sz--;									
							//							}
							ns.startIndex = i;
							ns.endIndex = j-1;
							rsArr[sz++] = ns;
							sz = checkRs(v, rsArr, sz-1, tW, sz);
							prev = rsArr[sz-1];
							ok = true;
							xx = v[j].x;
							yy = v[j].y;
							i = j;
							count = 0;
							break;
						}
					}
					if (ok) continue;
				}

				j = i+3;
				//				double total = bestFitLine(v, i, j, coef);
				//				if (total>=EPSILON) {
				//					Segment s = new Segment();					
				//					s.start = new Vector2D(v[i]);
				//					s.end = new Vector2D(v[i]);
				//					s.num = 1;
				//					startArr[sz] = i;
				//					endArr[sz] = i;
				//					rsArr[sz++] = s;
				//					tmpS[count].copy(ns);					
				//					tmpI[count++] = i;
				//					i++;
				//					xx = v[i].x;
				//					yy = v[i].y;
				//					continue;
				//				}
				Vector2D vv = v[j-1];
				xx = vv.x;
				yy = vv.y;
				//				double a = coef[0];
				//				double b = coef[1];
				//				if (!Double.isInfinite(a)){
				//					x1 = (y1-b)/a;
				//					xx = (yy-b)/a;
				//				}


				for (j=i+3;j<to;++j){
					vv = v[j];
					double x = vv.x;
					double y = vv.y;					
					if (Geom.ptLineDistSq(x1, y1, xx, yy, x, y, null)>allowedDist)
						break;		
					boolean isCir = Geom.getCircle(x1, y1, x2, y2, x, y, result);
					double r;
					if (isCir && (r = Math.sqrt(result[2]))>REJECT_VALUE && r<MAX_RADIUS-1) break;
					xx = x;
					yy = y;
					if (j-i>3){											
						double total = bestFitLine(v, i, j, coef);
						if (total>=EPSILON) break;												
						double a = coef[0];
						double b = coef[1];																			
						if (!Double.isInfinite(a)){
							x1 = (y1-b)/a;
							xx = (yy-b)/a;
						}
					}
				}

				if (j-i<=3 && j!=to){
					/*Segment s = new Segment();					
					//					s.addPoint(v[i]);
					s.start = new Vector2D(v[i]);
					s.end = new Vector2D(v[i]);
					s.num = 1;
					s.points = v;
					s.startIndex = i;
					s.endIndex = i;
					rsArr[sz++] = s;//*/
					tmpS[count].copy(ns);					
					tmpI[count++] = i;
					i++;
					xx = v[i].x;
					yy = v[i].y;

					continue;
				}
				//				xx = v[j-1].x;
				//				yy = v[j-1].y;		

				//				double dx = xx-x1;
				//				double dy = yy-y1;
				//				if ((dx<0 ? -dx : dx)<E){			
				//					xx = x1;
				//				} else if (j-i-3>0){
				//					double tmp = dy/dx;					
				//					QuickLineFitter lf = new QuickLineFitter(new double[]{tmp,yy-xx*tmp},v,i,j);					
				//					lf.fit();					
				//					double a = lf.getA();
				//					double b = lf.getB();
				//					x1 = (y1-b)/a;
				//					xx = (yy-b)/a;					
				//				}
				if (j-i==3) {
					ns.map = new int[MAX_RADIUS];
					ns.map[MAX_RADIUS-1]++;
					ns.startIndex = i;
					ns.endIndex = j-1;					
					rsArr[sz++] = ns;
					ns.points = v;
					count = 0;
					if (j>=to) break; 
					xx = v[j].x;
					yy = v[j].y;						
					i = j;
					continue;
				}
				Segment s = new Segment(0, x1, y1, xx, yy,0,0,Double.MAX_VALUE);				
				//				if (s.start.y>v[i].y) s.start = new Vector2D(v[i]);
				//				if (s.end.y<v[j-1].y) s.end = new Vector2D(v[j-1]);
				//				for (int jj=i;jj<j;++jj)
				//					s.addPoint(v[jj]);
				s.startIndex = i;
				s.endIndex = j-1;
				s.num = j-i;
				rsArr[sz++] = s;
				s.points = v;
				sz = checkRs(v,rsArr,sz-1,tW,sz);
				prev = rsArr[sz-1];
				count = 0;
				if (j>=to) break; 
				xx = v[j].x;
				yy = v[j].y;						
				i = j;
				continue;
			} else {						
				if (i>to-3) {					
					rs.size(sz);
					return rs;
				}
				int r = (int)Math.round(radius-tW);
				radius = r+tW;

				if (radius <REJECT_VALUE){
					/*Segment last = (sz>0) ? rsArr[sz-1] :null;				
					if (last!=null &&  last.type==Segment.UNKNOWN) {
						last.end = new Vector2D(v[i]);
						last.num++;
						last.endIndex = i;												
					} else {
						Segment s = new Segment();
						s.points = v;
						s.start = new Vector2D(v[i]);
						s.end = new Vector2D(v[i]);
						s.num = 1;					
						s.startIndex = i;
						s.endIndex = i;				
						rsArr[sz++] = s;
					}//*/
					i++;
					xx = v[i].x;
					yy = v[i].y;	
					continue;
				}
				double tx = (x1+x2)*0.5;
				double ty = (y1+y2)*0.5;

				double ddx = x1-tx;
				double ddy = y1-ty;
				double d = ddx*ddx+ddy*ddy;											
				double qx = result[0];
				double qy = result[1];
				double dx = qx-tx;
				double dy = qy-ty;				
				double dr = radius*radius-d;
				double dd = dx*dx+dy*dy;
				double dt =  Math.sqrt(dr/dd);
				tx += dx*dt;
				ty += dy*dt;

				if (ty<SMALL_MARGIN && ty>-SMALL_MARGIN) {
					tx = (tx<0) ? -Math.sqrt(tx*tx+ty*ty) : Math.sqrt(tx*tx+ty*ty);
					ty = 0;
				}//first segment

				if (radius*radius <d){
					/*Segment last = (sz>0) ? rsArr[sz-1] :null;
					Segment s = new Segment();
					if (last!=null &&  last.type==Segment.UNKNOWN) {
						last.end = new Vector2D(v[i]);
						last.num++;
						last.endIndex = i;					
					} else {
						s.points = v;
						s.start = new Vector2D(v[i]);
						s.end = new Vector2D(v[i]);
						s.num = 1;					
						s.startIndex = i;
						s.endIndex = i;				
						rsArr[sz++] = s;
					}//*/					
					i++;
					xx = v[i].x;
					yy = v[i].y;	
					continue;
				}

				for (j=i+1;j<to;++j){
					Vector2D vv = v[j];
					double dx0 = vv.x-tx;
					double dy0 = vv.y-ty;						
					double dd0 = Math.sqrt(dx0*dx0+dy0*dy0)-radius;
					if ((dd0<0 ? -dd0 : dd0)>=E)						
						break;

					if (j>=i+3){
						double ttx = (x1+vv.x)*0.5;
						double tty = (y1+vv.y)*0.5;

						ddx = x1-ttx;
						ddy = y1-tty;
						d = ddx*ddx+ddy*ddy;																

						double nx = -ddy;
						double ny = ddx;		
						double dn = nx*nx+ny*ny;

						qx = tx-ttx;
						qy = ty-tty;
						if (nx*qx+ny*qy<0) {
							nx = -nx;
							ny = -ny;
						}

						dt = Math.sqrt((radius*radius-d)/dn);
						ttx += nx * dt;
						tty += ny * dt;																					

						double de = tty-ty;
						if (de>EPSILON*10 || de<-EPSILON*10) break;
						if (ty==0 && (tty>=SMALL_MARGIN || tty<=-SMALL_MARGIN)) break;
						tx = ttx;
						ty = tty;
						if (ty<SMALL_MARGIN && ty>-SMALL_MARGIN) {
							tx = (tx<0) ? -Math.sqrt(tx*tx+ty*ty) : Math.sqrt(tx*tx+ty*ty);
							ty = 0;
						}
					}
				}												

				if (j<=i+2) {
					/*Segment last = (sz>0) ? rsArr[sz-1] :null;
					//					if (last!=null &&  last.type==Segment.UNKNOWN) rs.remove(rs.size()-1);
					Segment s = (last!=null && last.type==Segment.UNKNOWN) ? last :  new Segment();
					if (s==last){
						//						int index = sz-1;
						if (s.startIndex>i) s.startIndex = i;
						if (s.endIndex <i) s.endIndex = i;
						s.num = s.endIndex-s.startIndex+1;
					} else {
						s.start = new Vector2D(v[i]);
						s.end = new Vector2D(v[i]);
						s.startIndex = i;
						s.endIndex = i;
						s.num++;
						s.points = v;
						rsArr[sz++] = s;
					}//*/

					//					s.addPoint(v[i]);

					i++;
					xx = v[i].x;
					yy = v[i].y;	
					continue;
				}

				if (i==to-3){																				
					Segment s = new Segment(x1, y1, x3, y3,tx, ty, radius,tW);
					//					if (s.start.y>v[i].y) s.start = new Vector2D(v[i]);
					//					if (s.end.y<v[to-1].y) s.end = new Vector2D(v[to-1]);
					s.startIndex = i;
					s.endIndex = to-1;
					s.num = to-i;
					s.points = v;
					//					for (int jj=i;jj<to;++jj)
					//						s.addPoint(v[jj]);
					rsArr[sz++]= s;										
					sz = checkRs(v,rsArr,sz-1,tW,sz);					
					sz = lastCheck(v,rsArr,tW,sz);
					rs.size(sz);					
					return rs;
				}

				if (j==i+3){					
					Segment s = new Segment(x1, y1, x3, y3,tx, ty, radius,tW);
					s.num = j - i;
					s.points = v;
					if (prev!=null){												
						if ((s.type==prev.type && s.radius==prev.radius) || isConnected(prev, s, tW, null)){							
							int sR = (s.type==0 || s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : (int)(s.radius-s.type*tW);
							s.map[sR]++;
							xx = v[j-1].x;
							yy = v[j-1].y;				
							s.startIndex = i;
							s.endIndex = j-1;							
							rsArr[sz++] = s;
							sz = checkRs(v,rsArr,sz-1,tW,sz);
							prev = rsArr[sz-1];
							if (j<to){
								xx = v[j].x;
								yy = v[j].y;
							}						
							i = j;
							continue;
						}
					}
					if (count>0){
						boolean ok = false;
						for (int k = count-1;k>=0;--k){
							Segment t = tmpS[k];
							if (t.type==s.type && t.radius==s.radius && t.center.distance(s.center)<2){
								int indx = tmpI[k];
								if (s.map==null) s.map = new int[MAX_RADIUS];
								int sR = (s.type==0 || s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : (int)(s.radius-s.type*tW);
								if (!s.reCalculate(v, indx, j, tW)) continue;
								s.map[sR]++;																								
								s.num = j-indx;

								/*Segment last = (sz>0) ? rsArr[sz-1] :null;
								if (last.type==UNKNOWN){
									if ((last.num = indx-last.endIndex)>0){
										last.end = new Vector2D(v[indx-1]);
										last.endIndex = indx-1;
									} else sz--;									
								}//*/
								s.startIndex = indx;
								s.endIndex = j-1;
								rsArr[sz++] = s;
								sz = checkRs(v, rsArr, sz-1, tW, sz);
								prev = rsArr[sz-1];
								ok = true;
								xx = v[j].x;
								yy = v[j].y;
								i = j;
								count = 0;
								break;
							}
						}
						if (ok) continue;
					}
					tmpS[count].copy(s);					
					tmpI[count++] = i;
					if (s.center!=null && ty==0){
						s.startIndex = i;
						s.endIndex = j-1;
						rsArr[sz++] = s;
						i = j;
						xx = v[i].x;
						yy = v[i].y;	
						continue;
					}
					/*Segment last = (sz>0) ? rsArr[sz-1] :null;
					s.map = null;
					s.type = UNKNOWN;
					s.radius = -1;
					if (last!=null &&  last.type==Segment.UNKNOWN) {								
						last.end = new Vector2D(v[i]);
						last.num++;
						last.endIndex = i;								
					} else {
						s.start = new Vector2D(v[i]);
						s.end = new Vector2D(v[i]);
						s.num = 1;					
						s.startIndex = i;
						s.endIndex = i;
						s.points = v;
						rsArr[sz++] = s;
					}//*/
					i++;
					xx = v[i].x;
					yy = v[i].y;	
					continue;

				}

				if (j>=i+4){					
					double rr =radius;
					count = 0;
					xx = v[j-1].x;
					yy = v[j-1].y;
					if (ty!=0){						
						isCircle = Geom.getCircle(x2, y2, x3, y3, xx, yy, result);
						rr =  Math.round(Math.sqrt(result[2])-tW)+tW;						
					}
					if (ty==0 || radius==rr){
						Segment s = new Segment(x1, y1, xx, yy,tx, ty, radius,tW);
						int sR = (rr>=MAX_RADIUS-1) ? MAX_RADIUS : (int)(rr-s.type*tW);						
						s.map[sR]++;
						s.startIndex = i;
						s.endIndex = j-1;
						s.num = j-i;
						rsArr[sz++] = s;		
						s.points = v;
						sz = checkRs(v,rsArr,sz-1,tW,sz);	
						prev = rsArr[sz-1];
						if (j<to){
							xx = v[j].x;
							yy = v[j].y;
						}
						i = j;
						continue;
					}
					if (Math.abs(radius-rr)>3){
						/*Segment last = (sz>0) ? rsArr[sz-1] :null;
						Segment s = new Segment();
						if (last!=null &&  last.type==Segment.UNKNOWN) {
							last.end = new Vector2D(v[i]);
							last.num++;
							last.endIndex = i;								
						} else {
							s.start = new Vector2D(v[i]);
							s.end = new Vector2D(v[i]);
							s.num = 1;					
							s.points = v;
							s.startIndex = i;
							s.endIndex = i;
							rsArr[sz++] = s;
						}//*/
						i++;
						xx = v[i].x;
						yy = v[i].y;	
						continue;
					}

					/*if (Math.abs(radius-rr)>0.5){					
						result[0] = tx;
						result[1] = ty;						
						QuickCircleFitter cf = new QuickCircleFitter(new double[]{result[0],result[1]},v,i,j);
						cf.fit();
						double oldr = radius;
						radius = Math.round(cf.getEstimatedRadius()-tW)+tW;
						Vector2D center = cf.getEstimatedCenter();						
						double tmp = oldr-radius;
						if ((tmp<0?-tmp:tmp)>3){
							Segment last = (sz>0) ? rsArr[sz-1] :null;
							Segment s = new Segment();
							if (last!=null &&  last.type==Segment.UNKNOWN) {
								last.end = new Vector2D(v[i]);
								last.num++;
								last.endIndex = i;								
							} else {
								s.start = new Vector2D(v[i]);							
								s.end = new Vector2D(v[i]);
								s.num = 1;					
								s.startIndex = i;
								s.endIndex = i;
								rsArr[sz++] = s;
								s.points = v;
							}
							i++;
							xx = v[i].x;
							yy = v[i].y;	
							continue;
						}

						center = circle(v[i],v[j-1],center,radius);
						tx = center.x;
						ty = center.y;
						if (ty==0 || radius==rr || tmp==0 || (tmp<0?-tmp:tmp)>0.5){							
							if (ty<SMALL_MARGIN && ty>-SMALL_MARGIN) {
								tx = (tx<0) ? -Math.sqrt(tx*tx+ty*ty) : Math.sqrt(tx*tx+ty*ty);
								ty = 0;
							}
							Segment s = new Segment(x1, y1, xx, yy,tx, ty, radius,tW);
							int sR = (rr>=MAX_RADIUS-1) ? MAX_RADIUS : (int)(rr-s.type*tW);
							if (sR>MAX_RADIUS-1) sR = MAX_RADIUS-1;
							s.map[sR]++;
							sR = (oldr>=MAX_RADIUS-1) ? MAX_RADIUS : (int)(oldr-s.type*tW);
							if (sR>MAX_RADIUS-1) sR = MAX_RADIUS-1;
							s.map[sR]++;
							s.points = v;
							tx = (x1+xx)*0.5;
							ty = (y1+yy)*0.5;
							ddx = x1-tx;
							ddy = y1-ty;
							d = ddx*ddx+ddy*ddy;

							qx = center.x;
							qy = center.y;


							double nx = -ddy;
							double ny = ddx;		
							double dn = nx*nx+ny*ny;

							qx -= tx;
							qy -= ty;
							if (nx*qx+ny*qy<0) {
								nx = -nx;
								ny = -ny;
							}

							dt = Math.sqrt((radius*radius-d)/dn);
							tx += nx * dt;
							ty += ny * dt;	
							if (ty<SMALL_MARGIN && ty>-SMALL_MARGIN) {
								tx = (tx<0) ? -Math.sqrt(tx*tx+ty*ty) : Math.sqrt(tx*tx+ty*ty);
								ty = 0;
							}

							for (j=i+1;j<to;++j){
								Vector2D vv = v[j];
								double dx0 = vv.x-tx;
								double dy0 = vv.y-ty;						
								double dd0 = Math.sqrt(dx0*dx0+dy0*dy0)-radius;
								if ((dd0<0 ? -dd0 : dd0)>=E)						
									break;				
							}			
							s.start = new Vector2D(v[i]);
							s.end = new Vector2D(v[j-1]);
							s.center.x = tx;
							s.center.y = ty;
							s.startIndex = i;
							s.endIndex = j-1;
							s.num = j-i;
							rsArr[sz++] = s;				
							sz = checkRs(v,rsArr,sz-1,tW,sz);
							prev = rsArr[sz-1];
							if (j<to){
								xx = v[j].x;
								yy = v[j].y;
							}
							i = j;
							continue;
						}										
					}//*/
				}
				xx = v[j-1].x;
				yy = v[j-1].y;				
				Segment s = new Segment(x1, y1, xx, yy,tx, ty, radius,tW);
				//				if (s.start.y>v[i].y) s.start = new Vector2D(v[i]);
				//				if (s.end.y<v[j-1].y) s.end = new Vector2D(v[j-1]);
				//				for (int jj=i;jj<j;++jj)
				//					s.addPoint(v[jj]);					
				s.startIndex = i;
				s.endIndex = j-1;
				s.num = j-i;
				s.points = v;
				rsArr[sz++] = s;				
				sz = checkRs(v,rsArr,sz-1,tW,sz);
				prev = rsArr[sz-1];
				if (j<to){
					xx = v[j].x;
					yy = v[j].y;
				}
			}

			i = j;
		}//end of while

		sz = lastCheck(v,rsArr,tW,sz);			
		rs.size(sz);
		if (CircleDriver2.debug)System.out.println("seg time : "+(System.nanoTime()-CircleDriver2.ti)/1000000+" ms.");
		return rs;
	}

	/*private static final int expandBackward(final Vector2D[] v,int from,int to,double tW,Vector2D center,double rad,Segment prev,Segment next,Segment s,Vector2D[] tmp,int size,int firstIndex,Vector2D last,int[] marks,int n){			
		Vector2D first = null;
		double r = 0;
		double E = (CircleDriver2.inTurn) ? 0.1*TrackSegment.EPSILON : EPSILON;
		Vector2D endSeg = null;
		if (s.type!=0 && prev!=null && prev.type!=0 && prev.center!=null){
			double angle = Vector2D.angle(prev.start.minus(prev.center), s.center.minus(prev.center));
			if (angle<-Math.PI) 
				angle += 2*Math.PI;
			else if (angle>Math.PI) 
				angle -= 2*Math.PI;

			angle = Math.round(Math.abs(angle)*PRECISION)/PRECISION;

			endSeg = prev.center.plus(prev.start.minus(prev.center).rotated(-angle*s.type));			
		}

		Vector2D c = new Vector2D(center);
		if (firstIndex>-1){
			boolean ok = false;
			for (int k=firstIndex-1;k>=from;--k){
				if (endSeg!=null && v[k].y<=endSeg.y) break;
				if (marks[k]==0){					
					if (s.type!=0 && s.type!=UNKNOWN && (n>2 || prev==null || prev.type!=0) && Math.abs(v[k].distance(center)-rad)<=E){												
						first = new Vector2D(v[k]);
						if (size>0) {
							c = circle(first, tmp[size-1], c, rad);
							if (check(tmp, size, c, rad)<0) break;
						}			
						marks[k] = n;		
						if (tmp!=null && size>0) System.arraycopy(tmp, 0, tmp, 1, size);
						if (tmp!=null) tmp[0] = first;
						ok = true;
						size++;						
						s.start = first;
						firstIndex = k;
						//						if (tmp!=null && size>s.num) {
						//							s.num = size;
						//							update(v,from,to,tW, center, rad, prev, next, s, tmp, size, firstIndex, marks, n);							
						//						}						
					} else if (s.type==0 && Geom.ptLineDistSq(s.start.x, s.start.y, s.end.x, s.end.y, v[k].x, v[k].y, null)<=E*E){
						ok = true;
						marks[k] = n;					
						first = new Vector2D(v[k]);
						firstIndex = k;
						if (tmp!=null && size>0) System.arraycopy(tmp, 0, tmp, 1, size);
						if (tmp!=null) tmp[0] = first;
						size++;
						s.start = first;
						if (tmp!=null && size>s.num) {
							s.num = size;
							update(v,from,to,tW, center, rad, prev, next, s, n);						
						}
					} else if (s.type!=0 && s.type!=UNKNOWN && prev!=null && prev.type==0 && n==2 && last!=null){
						double x0 = prev.start.x;
						Geom.getCircle2(v[k], last, new Vector2D(x0,0), new Vector2D(x0,1), temp);					
						r = Math.sqrt(temp[2]);
						r = Math.round(r-s.type*tW)+s.type*tW;
						if (Math.round(r-s.type*tW)==Math.round(rad-s.type*tW)){
							ok = true;
							marks[k] = n;						
							first = new Vector2D(v[k]);
							firstIndex = k;
							if (tmp!=null && size>0) System.arraycopy(tmp, 0, tmp, 1, size);
							if (tmp!=null) tmp[0] = first;
							size++;
							s.start = first;
							s.map[double2int(r-s.type*tW)]++;


						} 
					}					
				} else if (marks[k]<n) break;
				if (!ok) break;
				ok = false;
			}
		}
		if (first!=null && s.start.y>first.y) s.start = new Vector2D(first);		
		return firstIndex;
	}//*/

	/*private static final int expandForward(final Vector2D[] v,int from,int to,double tW,Vector2D center,double rad,Segment prev,Segment next,Segment s,Vector2D[] tmp,int size,int lastIndex,Vector2D first,int[] marks,int n){
		Vector2D last = null;
		int firstIndex = lastIndex - size+1;
		double E = (CircleDriver2.inTurn) ? 0.1*TrackSegment.EPSILON : EPSILON;
		double r = 0;
		Vector2D endSeg = null;
		if (s.type!=0 && next!=null && next.type!=0 && next.center!=null && s.center!=null){
			double angle = Vector2D.angle(s.start.minus(s.center), next.center.minus(s.center));
			if (angle<-Math.PI) 
				angle += 2*Math.PI;
			else if (angle>Math.PI) 
				angle -= 2*Math.PI;

			angle = Math.round(Math.abs(angle)*PRECISION)/PRECISION;

			endSeg = s.center.plus(s.start.minus(s.center).rotated(-angle*s.type));			
		}

		Vector2D c = new Vector2D(center);
		if (lastIndex>-1){
			boolean changeNext = false;
			boolean ok = false;
			for (int k=lastIndex+1;k<to;++k){
				if (endSeg!=null && v[k].y>=endSeg.y) break;
				if (marks[k]==0){					
					if (s.type!=0 && s.type!=UNKNOWN && (n>2 || prev==null || prev.type!=0) && Math.abs(v[k].distance(center)-rad)<=E){																					
						last = new Vector2D(v[k]);
						if (size>0){
							c = circle(tmp[0], last, c, rad);
							if (check(tmp, size, c, rad)<0) break;
						}
						marks[k] = n;		
						if (tmp!=null && size>=0) tmp[size++] = last;
						ok = true;
						s.end = last;
						lastIndex = k;						
						//						if (tmp!=null && size>s.num){
						//							s.num = size;
						//							update(v,from,to,tW, center, rad, prev, next, s, tmp, size, firstIndex, marks, n);
						//						}
						if (next!=null && last.y>=next.start.y){
							changeNext = true;
							next.start = new Vector2D(last);
						}

					} else if (s.type==0 && Geom.ptLineDistSq(s.start.x, s.start.y, s.end.x, s.end.y, v[k].x, v[k].y, null)<=E*E){
						ok = true;
						marks[k] = n;						
						last = new Vector2D(v[k]);
						if (tmp!=null && size>=0) tmp[size++] = last;
						if (next!=null && last.y>=next.start.y){
							changeNext = true;
							next.start = new Vector2D(last);
						}
						if (tmp!=null && size>s.num) {
							s.num = size;
							update(v,from,to,tW, center, rad, prev, next, s, n);						
						}
						lastIndex = k;
					} else if (first!=null && s.type!=UNKNOWN && s.type!=0 && prev!=null && prev.type==0 && n==2 ){						
						double x0 = prev.start.x;
						Geom.getCircle2(first, v[k], new Vector2D(x0,0), new Vector2D(x0,1), temp);					
						r = Math.sqrt(temp[2]);
						r = Math.round(r-s.type*tW)+s.type*tW;
						if (Math.round(r-s.type*tW)==Math.round(rad-s.type*tW)){
							ok = true;
							marks[k] = n;							
							last = new Vector2D(v[k]);
							if (tmp!=null && size>=0) tmp[size++] = last;						
							s.map[double2int(r-s.type*tW)]++;
							lastIndex = k;
							s.end = last;
							if (next!=null && last.y>=next.start.y){
								changeNext = true;
								next.start = new Vector2D(last);
							}
						}
					} 
				} else if (marks[k]>n) break;
				if (!ok) break;
				ok = false;
			}

			if (changeNext && next.start.y<next.end.y) next.reCalLength();
		}

		if (last!=null && s.end.y<last.y) s.end = new Vector2D(last);		
		s.reCalLength();
		return lastIndex;
	}//*/

	/*private static final double check(Vector2D[] tmp,int size,Vector2D center,double r){
		double d = 0;
		boolean ok = true;
		double cx = center.x;
		double cy = center.y;
		for (int i=0;i<size;++i){
			double dx = tmp[i].x-cx;
			double dy = tmp[i].y-cy;			
			double e = Math.sqrt(dx*dx+dy*dy)-r;
			if (e<0) e = -e;
			if (e>EPSILON) ok = false;						
			d+=e;
		}
		return (ok) ? d : -d;
	}//*/

	public static final double check(Vector2D[] tmp,int from,int to,Vector2D center,double r){
		double d = 0;
		boolean ok = true;
		double cx = center.x;
		double cy = center.y;
		Vector2D s = tmp[from];
		double dx = s.x-cx;
		double dy = s.y-cy;			
		double e = Math.sqrt(dx*dx+dy*dy)-r;
		if (e<0) e = -e;
		if (e>EPSILON) return -1;
		s = tmp[to-1];
		dx = s.x-cx;
		dy = s.y-cy;			
		e = Math.sqrt(dx*dx+dy*dy)-r;
		if (e<0) e = -e;
		if (e>EPSILON) return -1;
		to--;
		boolean prevGood = true;
		for (int i=from+1;i<to;++i){
			s = tmp[i];
			dx = s.x-cx;
			dy = s.y-cy;			
			e = Math.sqrt(dx*dx+dy*dy)-r;
			if (e<0) e = -e;
			if (e>EPSILON) {
				ok = false;		
				prevGood = false;
				if (s.certain) break;
				if (i>0 && prevGood && s.distance(tmp[i-1])<1) {
					ok = true;
					continue;
				}
				break;
			}
			d+=e;
			prevGood = true;
		}
		return (ok) ? d : -d;
	}

	public static final Vector2D circle(Vector2D first,Vector2D last, Vector2D center,double r){
		if (r<=0) return null;
		double cx = center.x;
		double cy = center.y;
		double ox = (first.x+last.x)*0.5;
		double oy = (first.y+last.y)*0.5;
		double dx = ox - first.x;
		double dy = oy - first.y;
		double d = dx*dx+dy*dy;

		double nx = -dy;
		double ny = dx;		
		double dn = nx*nx+ny*ny;

		cx -= ox;
		cy -= oy;
		if (nx*cx+ny*cy<0) {
			nx = -nx;
			ny = -ny;
		}

		double dt = Math.sqrt((r*r-d)/dn);
		ox += nx * dt;
		oy += ny * dt;		
		center = new Vector2D(ox,oy);		
		if (oy<SMALL_MARGIN && oy>-SMALL_MARGIN) {
			ox = (ox<0) ? -Math.sqrt(ox*ox+oy*oy) : Math.sqrt(ox*ox+oy*oy);
			oy = 0;
			center.x = ox;
			center.y = oy;
		}
		return center;
	}
	
	public static final Vector2D circle(Vector2D first,Vector2D last, double cx,double cy,double r){
		if (r<=0) return null;		
		double ox = (first.x+last.x)*0.5;
		double oy = (first.y+last.y)*0.5;
		double dx = ox - first.x;
		double dy = oy - first.y;
		double d = dx*dx+dy*dy;

		double nx = -dy;
		double ny = dx;		
		double dn = nx*nx+ny*ny;

		cx -= ox;
		cy -= oy;
		if (nx*cx+ny*cy<0) {
			nx = -nx;
			ny = -ny;
		}

		double dt = Math.sqrt((r*r-d)/dn);
		ox += nx * dt;
		oy += ny * dt;		
		Vector2D center = new Vector2D(ox,oy);		
		if (oy<SMALL_MARGIN && oy>-SMALL_MARGIN) {
			ox = (ox<0) ? -Math.sqrt(ox*ox+oy*oy) : Math.sqrt(ox*ox+oy*oy);
			oy = 0;
			center.x = ox;
			center.y = oy;
		}
		return center;
	}
	
	public static final void circle(Vector2D first,Vector2D last, double cx,double cy,double r,Vector2D center){
		if (r<=0) return;		
		double ox = (first.x+last.x)*0.5;
		double oy = (first.y+last.y)*0.5;
		double dx = ox - first.x;
		double dy = oy - first.y;
		double d = dx*dx+dy*dy;

		double nx = -dy;
		double ny = dx;		
		double dn = nx*nx+ny*ny;

		cx -= ox;
		cy -= oy;
		if (nx*cx+ny*cy<0) {
			nx = -nx;
			ny = -ny;
		}

		double dt = Math.sqrt((r*r-d)/dn);
		ox += nx * dt;
		oy += ny * dt;						
		if (oy<SMALL_MARGIN && oy>-SMALL_MARGIN) {
			ox = (ox<0) ? -Math.sqrt(ox*ox+oy*oy) : Math.sqrt(ox*ox+oy*oy);
			oy = 0;		
		}
		if (center!=null){
			center.x = ox;
			center.y = oy;
		}
	}

	public static final void circle(Vector2D first,Vector2D last, int tp,double r,Vector2D center){
		if (r<=0) return;		
		double ox = (first.x+last.x)*0.5;
		double oy = (first.y+last.y)*0.5;
		double dx = ox - first.x;
		double dy = oy - first.y;
		double d = dx*dx+dy*dy;

		double nx = (tp==1) ? dy : -dy;
		double ny = -tp*dx;		
		double dn = nx*nx+ny*ny;
		

		double dt = Math.sqrt((r*r-d)/dn);
		ox += nx * dt;
		oy += ny * dt;						
		if (oy<SMALL_MARGIN && oy>-SMALL_MARGIN) {
			ox = (ox<0) ? -Math.sqrt(ox*ox+oy*oy) : Math.sqrt(ox*ox+oy*oy);
			oy = 0;		
		}
		if (center!=null){
			center.x = ox;
			center.y = oy;
		}
	}



	public static final void apply(Segment s, double tW,int tp,Vector2D first,Vector2D last,Vector2D center,double r){		
		int rr = (r>=MAX_RADIUS || r==0) ? MAX_RADIUS-1 : (int)Math.round(r-tp*tW);		
		if (rr>=MAX_RADIUS) rr = MAX_RADIUS-1;
		if (s.type==UNKNOWN || s.map==null) {			
			if (s.start==null) 
				s.start = new Vector2D(first);
			else s.start.copy(first);								
			if (s.end==null)
				s.end = new Vector2D(last);
			else s.end.copy(last);			
			s.type = tp;			
			if (tp!=0 && center!=null) {				
				if (s.center==null) 
					s.center = new Vector2D(center);
				else s.center.copy(center);				
			}
			s.radius = r;				
			if (s.map!=null) {
				s.appearedRads[s.radCount++] = rr;
				if (s.opp!=null) s.opp.radCount = s.radCount;
				s.map[rr]++;
			}
						 
			return;
		}
		int rd = (s.type==0 || s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : (int)Math.round(s.radius-s.type*tW);
		double rad = s.radius;

		if (Math.abs(r-rad)<0.5 || (s.type==0 && (rr>=MAX_RADIUS-1 || center==null || r==0)) || (rr>=MAX_RADIUS-1 && rd>=MAX_RADIUS-1)){
			if (s.start.y>first.y) {
				s.start.x = first.x;
				s.start.y = first.y;
			}
			s.radius = r;
			if (s.end.y<last.y) {
				s.end.x = last.x;
				s.end.y = last.y;
			}
			if (s.type!=Segment.UNKNOWN && s.map!=null) {
				if (s.map[rd]==0) {
					s.appearedRads[s.radCount++] = rd;
					if (s.opp!=null) s.opp.radCount = s.radCount;
				}
				s.map[rd]++;			
			}
		} else if (s.map!=null){
			int score = 1;
			if (rr>=0 && (tp==s.type || s.type==0 || tp==0)){
				if (s.map[rr]==0) {
					s.appearedRads[s.radCount++] = rr;
					if (s.opp!=null) s.opp.radCount = s.radCount;
				}
				s.map[rr]++;
				score = s.map[rr];
			} else if (s.map[rd]>0) s.map[rd]--;
			if (s.map!=null && s.map[rd]==0){
				for (int i = s.radCount-1;i>=0;--i){
					int rads = s.appearedRads[i];
					if (rads==rd){
						if (s.radCount-i-1>0) System.arraycopy(s.appearedRads, i+1, s.appearedRads, i, s.radCount-i-1);
						s.radCount--;
						if (s.opp!=null) s.opp.radCount = s.radCount;
						break;
					}
				}
			}
			int nr = s.map[rd];	
																		
			if (nr<=score && rr<MAX_RADIUS-1 && center!=null ){
				double cx = center.x;
				double cy = center.y;
				double ox = (first.x+last.x)*0.5;
				double oy = (first.y+last.y)*0.5;
				double dx = ox - first.x;
				double dy = oy - first.y;
				double d = dx*dx+dy*dy;

				double nx = -dy;
				double ny = dx;		
				double dn = nx*nx+ny*ny;

				cx -= ox;
				cy -= oy;
				if (nx*cx+ny*cy<0) {
					nx = -nx;
					ny = -ny;
				}

				double dt = Math.sqrt((r*r-d)/dn);
				ox += nx * dt;
				oy += ny * dt;		
				
				center.x = ox;
				center.y = oy;
				
				rad = r;
				if (s.center!=null)
					s.center.copy(center);
				else s.center = new Vector2D(center);
				s.start.x = first.x;
				s.start.y = first.y;
				s.end.x = last.x;
				s.end.y = last.y;
				s.type = tp;
				s.radius = r;
				s.type = tp;
				s.reCalLength();					
			} else if (s.map!=null && rr>=0 && rr>=MAX_RADIUS-1 && nr<=s.map[rr] ){				
				//				s.start = new Vector2D(first);
				//				s.end = new Vector2D(last);
				s.type = 0;
				s.radius = Double.MAX_VALUE;				
				s.reCalLength();
			}

		}


	}

	/*private static final ObjectArrayList<Vector2D> reFillSeg(final Vector2D[] v,int from,int to,double tW,Segment prev,Vector2D center,double r,int n){
		int size = to - from;
		if (size<0) return null;
		//		prev.end = tmp[size-1];

		Vector2D[] temp = new Vector2D[Math.max(size,6)+2];		
		int sz = 0;
		for (int i = from;i<to;++i){
			if ((v[i].y>=prev.start.y-SMALL_MARGIN && v[i].y<=prev.end.y+SMALL_MARGIN) ) temp[sz++] = v[i]; 
			if (v[i].y>prev.end.y) break;
		}
		Vector2D first = null;
		Vector2D last = null;
		if (sz>=2){
			first = temp[0];
			last = temp[sz-1];			
		} else {
			first = prev.start;
			last = prev.end;
		}
		if (prev.type!=0){
			Vector2D o = first.plus(last).times(0.5);
			double d = o.distance(first);
			Vector2D nn = last.minus(first).orthogonal().normalised();				
			if (nn.dot(center.minus(o))<0) nn = nn.negated();
			center = o.plus(nn.times(Math.sqrt(r*r-d*d) ));
			prev.center = center;
		} else {
			temp[sz++] = prev.start;
			temp[sz++] = prev.end;
			double dx = prev.end.x-prev.start.x;
			double dy = prev.end.y-prev.start.y;
			double xx = prev.start.x;
			double yy = prev.start.y;
			if (Math.abs(dx)<TrackSegment.EPSILON){			
				prev.end.x = prev.start.x;
			} else {
				double tp = dy/dx;
				QuickLineFitter lf = new QuickLineFitter(new double[]{tp,yy-xx*tp},temp,0,sz);
				lf.fit();
				double a = lf.getA();
				double b = lf.getB();				
				prev.start.x = (prev.start.y-b)/a;
				prev.end.x = (prev.end.y-b)/a;
			}

		}
		prev.reCalLength();
		return ObjectArrayList.wrap(temp, sz);
	}//*/

	/*private static final boolean guessFromPrevNext(final Vector2D[] v,int from,int to,double tW,Segment prev,Segment next,Segment s,int n){
		int size = to- from;
		if (size<=1) return false;
		Vector2D first = v[from];
		Vector2D last = v[to-1];
		Vector2D center = null;
		double[] rs = new double[6];
		double r = -1;
		int nr = (next==null) ? 0: (next.radius>=MAX_RADIUS-1) ? MAX_RADIUS : double2int(next.radius-next.type*tW);
		int pr = (prev==null) ? 0: (prev.radius>=MAX_RADIUS-1) ? MAX_RADIUS : double2int(prev.radius-prev.type*tW);
		int rad = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius-s.type*tW);

		if (s!=null && prev!=null && prev.type==0 && s.type!=0 && s.center!=null && n>2){
			double d = Math.sqrt(Geom.ptLineDistSq(prev.start.x, prev.start.y, prev.end.x, prev.end.y, s.center.x, s.center.y, null));
			d = Math.round(d-s.type*tW)+s.type*tW;
			if (d==s.radius){
				if (Geom.getCircle2(first, last, prev.start, prev.end, rs)!=null){
					d = Math.sqrt(rs[2]);
					d = Math.round(d-s.type*tW)+s.type*tW;
					if (Math.abs(d-s.radius)>5)
						System.out.println(d);
				}
				double dd = Math.sqrt(Geom.ptLineDistSq(prev.start.x, prev.start.y, prev.end.x, prev.end.y, s.start.x, s.start.y, null));
				if (dd<=TrackSegment.EPSILON){																				
					if (s.num<=3){
						prev.end= new Vector2D(s.start);
						s.type = UNKNOWN;
						prev.reCalLength();
						return true;
					} else {
						double dy = prev.end.y - prev.start.y;
						double dx = prev.end.x - prev.start.x;
						double a = dy/dx;
						double b = prev.start.y - a*prev.start.x;
						double y = Math.max(prev.end.y, s.start.y-1);
						double x = (y-b)/a;
						prev.end = new Vector2D(x,y);
						prev.reCalLength();
					}
				}
			}
		}

		if (s!=null && next!=null && next.type==0 && s.type!=0 && s.center!=null && n>2){
			double d = Math.sqrt(Geom.ptLineDistSq(next.start.x, next.start.y, next.end.x, next.end.y, s.center.x, s.center.y, null));
			d = Math.round(d-s.type*tW)+s.type*tW;
			if (d==s.radius){
				double dd = Math.sqrt(Geom.ptLineDistSq(next.start.x, next.start.y, next.end.x, next.end.y, s.end.x, s.end.y, null));
				if (dd<=TrackSegment.EPSILON){					
					if (s.num<=3){
						next.start = new Vector2D(s.end);
						next.reCalLength();
						s.type = UNKNOWN;
						return true;
					} else {
						double dy = next.end.y - next.start.y;
						double dx = next.end.x - next.start.x;
						double a = dy/dx;
						double b = next.start.y - a*next.start.x;
						double y = Math.min(next.start.y, s.end.y+1);
						double x = (y-b)/a;
						next.start = new Vector2D(x,y);
						next.reCalLength();
					}
				}
			}
		}

		if (s!=null && next!=null && s.type==next.type && Math.abs(s.radius-next.radius)<0.5 && next.type!=UNKNOWN && next.center!=null && (!next.start.equals(s.start) || !next.end.equals(s.end))){			
			if (next.type!=0 && s.center.distance(next.center)<=CENTER_DIST_E){
				next.map[rad]++;
				if (next.start.y>s.start.y) next.start = s.start;
				if (next.end.y<s.end.y) next.end = s.end;
				next.reCalLength();
				s.type = UNKNOWN;
				return true;
			} else if (next.type==0){
				double dd = Math.sqrt(Geom.ptLineDistSq(s.start.x, s.start.y, s.end.x, s.end.y, next.start.x, next.start.y, null));
				double dd1 = Math.sqrt(Geom.ptLineDistSq(s.start.x, s.start.y, s.end.x, s.end.y, next.end.x, next.end.y, null));
				if (dd<4*TrackSegment.EPSILON && dd1<4*TrackSegment.EPSILON){
					next.map[rad]++;
					if (next.start.y>s.start.y) next.start = s.start;
					if (next.end.y<s.end.y) next.end = s.end;
					next.reCalLength();
					s.type = UNKNOWN;
					return true;
				}
			}
		}
		if (s!=null && prev!=null && s.type==prev.type && Math.abs(s.radius-prev.radius)<0.5 && prev.type!=UNKNOWN && prev.center!=null
				&& (!prev.start.equals(s.start) || !prev.end.equals(s.end))){			
			if (prev.type!=0 && s.center.distance(prev.center)<=CENTER_DIST_E){
				prev.map[rad]++;
				if (prev.start.y>s.start.y) prev.start = new Vector2D(s.start);
				if (prev.end.y<s.end.y) prev.end = new Vector2D(s.end);
				prev.reCalLength();
				reFillSeg(v, 0, to, tW, prev, prev.center, prev.radius, n-1);
				s.type = UNKNOWN;
				return true;
			} else if (prev.type==0){
				double dd = Math.sqrt(Geom.ptLineDistSq(prev.start.x, prev.start.y, prev.end.x, prev.end.y, s.start.x, s.start.y, null));
				double dd1 = Math.sqrt(Geom.ptLineDistSq(prev.start.x, prev.start.y, prev.end.x, prev.end.y, s.end.x, s.end.y, null));
				if (dd<4*TrackSegment.EPSILON && dd1<4*TrackSegment.EPSILON){
					prev.map[rad]++;
					if (prev.start.y>s.start.y) prev.start = new Vector2D(s.start);
					if (prev.end.y<s.end.y) prev.end = new Vector2D(s.end);
					prev.reCalLength();
					reFillSeg(v, 0, to, tW, prev, prev.center, prev.radius, n-1);
					s.type = UNKNOWN;
					return true;
				}
			}
		}



		if (next!=null && next.type!=0 && prev!=null && prev.type!=0 && next.num>=3 && prev.num>=3 && next.type!=UNKNOWN && next.map[nr]>3 && prev.map[pr]>3 && s.map[rad]<=3){
			Vector2D[] temp = new Vector2D[prev.num+size+10];
			int sz = 0;
			for (int i = 0;i<to;++i){
				if (v[i].y<=prev.end.y+0.00001 && v[i].y>=prev.start.y-0.00001) temp[sz++] = v[i]; 
				if (v[i].y>prev.end.y+0.00001) break;
			}
			if (sz>=2){
				first = temp[0];
				last = temp[sz-1];			
			} else {
				first = new Vector2D(prev.start);
				last = new Vector2D(prev.end);
			}

			center = null;			
			int no = 0;
			if (prev.type==next.type) 
				no = Geom.getCircle4(first, last, next.center, next.radius, rs);					
			else no = Geom.getCircle5(first, last, next.center, next.radius, rs);
			if (no>0){
				for (int k=0;k<no;++k){
					r = Math.round(rs[k*3+2]-prev.type*tW)+prev.type*tW;
					if (r==prev.radius) {
						s.type = UNKNOWN;
						break;
					}
				}
			}
			if (s.type==UNKNOWN) return true;
		}

		if (s.type!=0 && prev!=null && prev.type!=0 && size>=2 && prev.center!=null && prev.type!=UNKNOWN && prev.map[pr]>3){			
			center = null;			
			int no =0;
			if (s.type==prev.type) 
				no = Geom.getCircle4(first, last, prev.center, prev.radius, temp);					
			else no = Geom.getCircle5(first, last, prev.center, prev.radius, temp);
			for (int k =0;k<no;++k){
				r = Math.round(temp[k*3+2]-s.type*tW)+s.type*tW;
				center = new Vector2D(temp[3*k],temp[3*k+1]);
				boolean good = true;
				if (r<MAX_RADIUS-1 && r>REJECT_VALUE){
					Vector2D o = first.plus(last).times(0.5);
					double d = o.distance(first);
					Vector2D nn = last.minus(first).orthogonal().normalised();				
					if (nn.dot(center.minus(o))<0) nn = nn.negated();
					center = o.plus(nn.times(Math.sqrt(r*r-d*d) ));				
					for (int i=from;i<to;++i){
						if (Math.abs(v[i].distance(center)-r)>EPSILON) {						
							good = false;
							break;
						}
					}
				} else if (r>=MAX_RADIUS-1) 
					r = Double.MAX_VALUE;
				else  good = false;														
				if (good){
					if (r==prev.radius && prev.center.distance(center)<=CENTER_DIST_E){
						ObjectArrayList<Vector2D> temp = reFillSeg(v, 0, to, tW, prev, center, r, n-1);						
						if (prev.end.y>=s.end.y || s.num<=3) {
							s.type=Segment.UNKNOWN;
						} else if (prev.type!=0 && next!=null && next.type!=0 && size>=2 && temp!=null && temp.size()>0 && next.map[nr]>3){
							first = temp.get(0);
							last = temp.get(temp.size()-1);
							center = null;							
							int numb = 0;
							if (prev.type==next.type) 
								numb = Geom.getCircle4(first, last, next.center, next.radius, rs);					
							else numb = Geom.getCircle5(first, last, next.center, next.radius, rs);
							if (numb>0){
								for (int kk =0;kk<numb;++kk){
									double rr = Math.round(rs[2+3*kk]-prev.type*tW)+prev.type*tW;
									if (rr==prev.radius) {
										s.type = UNKNOWN;
										return true;
									}
								}
							}
						}

						s.start = new Vector2D(v[to-1]);
						if (s.start.y>=s.end.y) {
							s.type = UNKNOWN;
							return true;
						}
						s.reCalLength();
						return true;
					} else if (next!=null && r==next.radius && next.center.distance(center)<=CENTER_DIST_E && s.type==next.type){
						next.start = new Vector2D(first);
						next.reCalLength();						
						s.type=Segment.UNKNOWN;						
						return true;
					} else {
						int rr = (r>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(r-s.type*tW);						
						if (rr==rad){										
							s.map[rr]++;
							break;
						} else if (Math.abs(rr-rad)<5 && rr<MAX_RADIUS-1){						
							s.map[rr]++;
							if (s.map[rr]>s.map[rad]){																
								Vector2D m = first.plus(last).times(0.5);
								Vector2D nn = last.minus(first).orthogonal().normalised();
								double pq = last.distance(first) * 0.5;
								if (nn.dot(center.minus(m))<0) nn = nn.negated();
								center = m.plus(nn.times(Math.sqrt(r*r-pq*pq)));								
								s.center = center;
								s.start = new Vector2D(first);
								s.end = new Vector2D(last);								
								s.radius = r;
								if (rr>=MAX_RADIUS-1) s.type = 0;
								s.reCalLength();
							} else if (!CircleDriver2.inTurn && s.map[rad]>3){
								int numb = 0;
								if (s.type==prev.type && mark!=3) 
									numb = Geom.getCircle4(prev.start, prev.end, s.center, s.radius, rs);					
								else numb = Geom.getCircle5(prev.start, prev.end, s.center, s.radius, rs);
								if (numb>0){
									for (int kk=0;kk<numb;++kk){
										double rrr = Math.round(rs[2+3*kk]-prev.type*tW)+prev.type*tW;
										Vector2D c = new Vector2D(rs[3*kk],rs[1+3*kk]);
										c = circle(prev.start, prev.end, c, rrr);
										if (Math.abs(rrr-prev.radius)<=5)
											apply(prev, tW, prev.start, prev.end, c, rrr);
									}
								}
							}
							break;
						}//end of if
					}


				}
			}
		}
		if (s.type!=0 && next!=null && next.type!=0 && next.center!=null && size>=2 && next.type!=UNKNOWN && next.map[nr]>3){			
			center = null;
			//			XYSeries series = new XYSeries("Curve");
			//			TrackSegment.circle(next.center.x, next.center.y, next.radius, series);
			//			EdgeDetector.drawEdge(series, "Curve0");
			//			center = Geom.getCircle4(first, last, next.center, next.radius, temp);			
			//			TrackSegment.circle(temp[0], temp[1], temp[2], series);
			//			EdgeDetector.drawEdge(series, "Curve1");
			//			center = Geom.getCircle5(first, last, next.center, next.radius, temp);			
			//			TrackSegment.circle(temp[0], temp[1], temp[2], series);
			//			EdgeDetector.drawEdge(series, "Curve2");
			//			if (s.type!=next.type) 
			//				center = Geom.getCircle4(first, last, next.center, next.radius, temp);					
			//			else center = Geom.getCircle5(first, last, next.center, next.radius, temp);
			int no = 0;
			if (s.type==next.type) 
				no = Geom.getCircle4(first, last, next.center, next.radius, temp);					
			else no = Geom.getCircle5(first, last, next.center, next.radius, temp);
			for (int k =0;k<no;++k){
				r = Math.round(temp[k*3+2]-s.type*tW)+s.type*tW;
				center = new Vector2D(temp[3*k],temp[3*k+1]);	
				boolean good = true;
				if (r<MAX_RADIUS-1 && r>REJECT_VALUE){
					Vector2D o = first.plus(last).times(0.5);
					double d = o.distance(first);
					Vector2D nn = last.minus(first).orthogonal().normalised();				
					if (nn.dot(center.minus(o))<0) nn = nn.negated();
					center = o.plus(nn.times(Math.sqrt(r*r-d*d) ));				
					for (int i=from;i<to;++i){
						if (Math.abs(v[i].distance(center)-r)>EPSILON) {						
							good = false;
							break;
						}
					}
				} else if (r>=MAX_RADIUS-1) 
					r = Double.MAX_VALUE;
				else {
					good = false;
				}
				if (good){
					if (r==next.radius && next.center.distance(center)<=CENTER_DIST_E){
						next.start = new Vector2D(first);
						next.reCalLength();
						if (next.start.y<=s.start.y || s.num<=3) 
							s.type=Segment.UNKNOWN;					

						if (s.type==UNKNOWN) return true;
						s.end = new Vector2D(first);
						if (s.start.y>=s.end.y) {
							s.type =UNKNOWN;
							return true;
						}
						s.reCalLength();
						return true;
					} else if (prev!=null && r==prev.radius && prev.center.distance(center)<=CENTER_DIST_E && s.type==prev.type){
						reFillSeg(v, 0, to, tW, prev, center, r, n-1);						
						s.type = UNKNOWN;
						return true;
					} else {
						int rr = (r>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(r-s.type*tW);						
						if (rr==rad){										
							s.map[rr]++;
							break;
						} else if (Math.abs(rr-rad)<5 && rr<MAX_RADIUS-1){						
							s.map[rr]++;
							if (s.map[rr]>s.map[rad]){																
								Vector2D m = first.plus(last).times(0.5);
								Vector2D nn = last.minus(first).orthogonal().normalised();
								double pq = last.distance(first) * 0.5;
								if (nn.dot(center.minus(m))<0) nn = nn.negated();
								center = m.plus(nn.times(Math.sqrt(r*r-pq*pq)));								
								s.center = center;
								s.start = new Vector2D(first);
								s.end = new Vector2D(last);								
								s.radius = r;
								if (rr>=MAX_RADIUS-1) s.type = 0;
								s.reCalLength();
							} else if (!CircleDriver2.inTurn && s.map[rad]>3){							
								int numb = 0;
								if (s.type==next.type) 
									numb = Geom.getCircle4(next.start, next.end, s.center, s.radius, rs);					
								else numb = Geom.getCircle5(next.start, next.end, s.center, s.radius, rs);
								if (numb>0){
									for (int kk =0;kk<numb;++kk){
										double rrr = Math.round(rs[2+3*kk]-next.type*tW)+next.type*tW;
										Vector2D c = new Vector2D(rs[3*kk],rs[1+3*kk]);
										c = circle(next.start, next.end, c, rrr);
										if (Math.abs(rrr-next.radius)<=5)
											apply(next, tW, next.start, next.end, c, rrr);
									}
								}
							}
							break;
						}
					}													
				}  
			}
		}

		//		if (s.center!=null && s.type!=0 && prev!=null && prev.type!=0 && prev.center!=null && prev.num<=3 && prev.type!=UNKNOWN){
		//			int numb = 0;
		//			center = new Vector2D();
		//			if (s.type==prev.type) 
		//				numb = Geom.getCircle4(prev.start, prev.end, s.center, s.radius, rs);					
		//			else numb = Geom.getCircle5(prev.start, prev.end, s.center, s.radius, rs);
		//			if (numb>0){
		//				double rrr = selectRadius(tW, s, prev, prev.start, prev.end, center);
		//				if (rrr==s.radius && s.center.distance(center)<=CENTER_DIST_E){
		//					prev.end = s.end;
		//					prev.map = s.map;
		//					prev.radius = s.radius;
		//					prev.reCalLength();
		//					reFillSeg(v, 0, to, tW, prev, center, rrr, tmp, size, firstIndex, marks, n-1);
		//					s.type = UNKNOWN;
		//					return true;
		//				}
		//			}
		//		}

		if (s.center!=null && s.type!=0 && next!=null && next.type!=0 && next.center!=null && next.map[nr]<=3 && s.map[rad]>3){
			int numb = 0;
			if (s.type==next.type) 
				numb = Geom.getCircle4(next.start, next.end, s.center, s.radius, rs);					
			else numb = Geom.getCircle5(next.start, next.end, s.center, s.radius, rs);
			if (numb>0){
				for (int kk =0;kk<numb;++kk){
					double rrr = Math.round(rs[2+3*kk]-next.type*tW)+next.type*tW;
					Vector2D c = new Vector2D(rs[3*kk],rs[1+3*kk]);
					c = circle(next.start, next.end, c, rrr);
					if (rrr==s.radius && s.center.distance(center)<=CENTER_DIST_E && check(v, from,to, c, rrr)>=0){
						next.start = new Vector2D(s.start);
						next.map = s.map;
						next.radius = s.radius;
						next.reCalLength();
						s.type = UNKNOWN;
						return true;
					}
					if (Math.abs(rrr-next.radius)<=5)
						apply(next, tW, next.start, next.end, c, rrr);
				}
			}			
		}

		return false;
	}//*/

	/*private static final double selectRadius(double tW,Segment prev,Segment s,Vector2D first,Vector2D last,Vector2D center){
		double[] temp = new double[6];
		int no = 0;
		if (s.type==prev.type && prev.center!=null) 
			no = Geom.getCircle4(first, last, prev.center, prev.radius, temp);					
		else if (prev.center!=null) no = Geom.getCircle5(first, last, prev.center, prev.radius, temp);
		if (no<1) return 0;
		if (no==1) {
			center.x = temp[0];
			center.y = temp[1];
			return Math.round(temp[2]-s.type*tW)+s.type*tW;
		}
		double r1 = Math.round(temp[2]-s.type*tW)+s.type*tW;
		Vector2D center1 = new Vector2D(temp[0],temp[1]);
		center1 = circle(first, last, center1, r1);
		double r2 = Math.round(temp[5]-s.type*tW)+s.type*tW;
		Vector2D center2 = new Vector2D(temp[3],temp[4]);
		center2 = circle(first, last, center2, r2);
		int rr1 = (r1>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(r1-s.type*tW);
		int rr2 = (r2>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(r2-s.type*tW);
		int prr = (prev.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(prev.radius-prev.type*tW);
		int rr = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius-s.type*tW);
		if (r1<=REJECT_VALUE && r2<=REJECT_VALUE) return 0;
		if (r1<=REJECT_VALUE || rr2==rr || rr2==prr) {
			center.copy(center2);
			return r2;
		}
		if (r2<=REJECT_VALUE || rr1==rr || rr1==prr) {
			center.copy(center1);
			return r1;
		}

		if (Math.abs(rr1-rr)<=5){
			center.copy(center1);
			return r1;
		}
		if (Math.abs(rr2-rr)<=5){
			center.copy(center2);
			return r2;
		}
		if (Math.abs(rr1-prr)<=5){
			center.copy(center1);
			return r1;
		}
		if (Math.abs(rr2-prr)<=5){
			center.copy(center2);
			return r2;
		}
		if (Math.abs(rr-rr1)<Math.abs(rr-rr2)){
			center.copy(center1);
			return r1;
		} else {
			center.copy(center2);
			return r2;
		}
	}//*/

	/*private static final double guessNewRad(final Vector2D[] v,int from,int to,double tW,Segment prev,Segment next,Segment s){
		Vector2D first = v[from];
		Vector2D last = v[to-1];
		int size = to - from;
		if (size<1) return s.radius;
		double[] temp = new double[6];
		int tp = 0;	
		Vector2D center = (s.center==null) ? new Vector2D() :new Vector2D(s.center);
		double r = -1;
		if (prev!=null && s.type !=0 && prev.type==0 && !CircleDriver2.inTurn && Math.abs(prev.end.x-prev.start.x)<TrackSegment.EPSILON){				
			double x0 = prev.start.x;
			Geom.getCircle2(first, last, new Vector2D(x0,0), new Vector2D(x0,1), temp);					
			r = Math.sqrt(temp[2]);
			tp = TrackSegment.getTurn(temp[0], temp[1], r, first.x, first.y, last.x, last.y);
			r = Math.round(r-s.type*tW)+s.type*tW;
			if (r<REJECT_VALUE) {				
				return -1;
			}
			Vector2D m = first.plus(last).times(0.5);
			Vector2D nn = last.minus(first).orthogonal().normalised();
			double pq = last.distance(first) * 0.5;
			if (nn.dot(center.minus(m))<0) nn = nn.negated();
			center = m.plus(nn.times(Math.sqrt(r*r-pq*pq)));
			boolean ok = true;
			if (size<4) 
				ok = true;
			else {
				ObjectList<Segment> li = Segment.segmentize(v, from, to, tW,null);
				ok = (li.size()==1 && li.get(0).radius==r);
			}
			if (!ok || check(v,from,to, center, r)<0){				
				for (int i = to-2;i>=from;--i){					
					if (i>1){
						last = v[i];
						Geom.getCircle2(first, last, new Vector2D(x0,0), new Vector2D(x0,1), temp);					
						r = Math.sqrt(temp[2]);
						tp = TrackSegment.getTurn(temp[0], temp[1], r, first.x, first.y, last.x, last.y);
						r = Math.round(r-s.type*tW)+s.type*tW;
						int rr2 = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius-s.type*tW);
						if (r==s.radius && s.map[rr2]<3) continue;
						center = circle(first, last, center, r);
						if (i<3) 
							ok = true;
						else {
							ObjectList<Segment> li = Segment.segmentize(v, from, i+1, tW,null);
							ok = (li.size()==1 && li.get(0).radius==r);
						}
						if (ok || check(v,from, i+1, center, r)>=0){
							s.end = new Vector2D(last);
							if (r!=s.radius){
								s.map = null;
								s.map = new int[MAX_RADIUS];
							}
							s.radius = r;													
							s.map[rr2]++;
							s.num = i+1;
							s.reCalLength();
							return r;
						}
					}
				}
				s.type = Segment.UNKNOWN;
				return -1;				
			}

		} else if (size==2 && s.type!=0 && prev!=null && prev.type!=0){
			center = new Vector2D();
			r = selectRadius(tW, prev, s, first, last, center);
			tp = s.type;
			if (r==0) r=-1;			
		} else if (size==2 && s.type!=0 && next!=null && next.type!=0){
			r = selectRadius(tW, next, s, first, last, center);
			tp = s.type;
			if (r==0) r=-1;							
		} else if (size>=3 && s.type!=0){
			if (size==3){														
				Geom.getCircle(first.x, first.y, last.x, last.y, v[from+1].x, v[from+1].y, temp);
				r = Math.sqrt(temp[2]);
				center = new Vector2D(temp[0],temp[1]);					
				tp = TrackSegment.getTurn(temp[0], temp[1], r, first.x, first.y, last.x, last.y);				
				r = Math.round(r-tp*tW)+tp*tW;
				center = circle(first, last, center, r);
			} else {
				ObjectList<Segment> li = segmentize(v, from, to, tW,null);
				//				int rt = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius-s.type*tW);								
				Segment tt = null;
				//				int no = 0;
				int fr = -1;
				int t0 = -1;
				for (int kk = 0;kk<li.size();++kk){
					tt = li.get(kk);					
					//					if (tt!=null && tt.type!=Segment.UNKNOWN && tt.radius>REJECT_VALUE && tt.length>0.5 && (qq==null || Math.abs(qq.radius-tt.radius)>=5)) ++no;
					if (tt!=null && tt.type!=Segment.UNKNOWN) {
						fr = tt.startIndex;
						t0 = tt.endIndex;
						break;
					}
				}								
				if (tt==null ||  tt.type==Segment.UNKNOWN) {
					s.type = -2;
					return -1;
				}
				int rr = (tt.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(tt.radius-tt.type*tW);
				int rd = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius-s.type*tW);
				double d1 = check(v,fr,t0,tt.center,tt.radius);
				if (rr==rd) {
					s.map[rd]++;
					r = tt.radius;						
					double d = check(v, fr,t0, s.center, s.radius);
					if ((d<0 || d>d1) && li.size()==1) s.center = tt.center;								
					tp = tt.type;
					if (li.size()>1){						
						s.num = tt.num;																						
						if (tt==li.get(0)){
							s.end = new Vector2D(tt.end);
							if (d<0 || d>d1) s.center = tt.center;
							s.reCalLength();							
						} else {
							s.start = new Vector2D(tt.start);
							if (d<0 || d>d1) s.center = tt.center;
							s.reCalLength();							
						}						
						s.map[rd]++;						
						return r;
					}
				}

				QuickCircleFitter cf = new QuickCircleFitter(new double[]{center.x,center.y},v,from,to);
				cf.fit();
				Vector2D c = cf.getEstimatedCenter();
				tp = TrackSegment.getTurn(c.x, c.y, cf.getEstimatedRadius(), first.x, first.y, last.x, last.y);
				center = cf.getEstimatedCenter();					
				r = Math.round(cf.getEstimatedRadius()-tp*tW)+tp*tW;
				Vector2D p = first;
				Vector2D q = last;
				Vector2D m = p.plus(q).times(0.5);
				Vector2D nn = q.minus(p).orthogonal().normalised();				
				double pq = p.distance(q) * 0.5;
				if (nn.dot(center.minus(m))<0) nn = nn.negated();
				center = m.plus(nn.times(Math.sqrt(r*r-pq*pq)));
				center.x = Math.round(center.x*PRECISION)/PRECISION;
				center.y = Math.round(center.y*PRECISION)/PRECISION;
				double d2 = check(v, from,to, center, r);
				int rr2 = (r>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(r-tp*tW);				
				//				if (rr2!=rd && li.size()>1 && Math.abs(rr-rd)<2 && (d1<0 || Math.abs(rr2-rd)>2)){
				//					s.type = UNKNOWN;
				//					return 0;
				//				}
				double dd;
				if (rr==rr2){
					//					s.map[rr]++;
					dd = (d1<0) ? d2 : (d2<0) ? d1 : (d1<d2) ? d1 : d2;
					if (dd==d1) center = tt.center;
				} else if (rr2==rd) {
					//					s.map[rd]++;
					dd = d2;														
				}else if (d2<0 && d1>=0){
					r = tt.radius;
					tp = tt.type;
					center = tt.center;
					dd = d1;
				} else if (d1>=0 && d2>=0 && d2>d1){
					//					s.map[rr2]++;
					r = tt.radius;
					tp = tt.type;
					center = tt.center;
					dd = d1;
				} else if (d1>=0 && d2>=0 && d1>d2){
					//					s.map[rr]++;
					dd = d1;
				} else if (d1<0 && d2<0 && d1>d2){					
					r = tt.radius;
					tp = tt.type;
					center = tt.center;
					dd = d1;
				} 
			}
		}  else if (size==2 && s.type==0){
			if (s.start.y>first.y) s.start = new Vector2D(first);
			if (s.end.y<last.y) s.end = new Vector2D(last);
			r = MAX_RADIUS-1;
		} else if (size>=3 && s.type==0){			
			boolean isCircle = Geom.getCircle(first.x, first.y, last.x, last.y, v[1+from].x, v[1+from].y, temp);
			r = Math.sqrt(temp[2]);
			center= new Vector2D(temp[0],temp[1]);
			if (r>=MAX_RADIUS-1){
				r = MAX_RADIUS-1;
			} else if (isCircle && size>3){
				ObjectList<Segment> li = segmentize(v, from, to, tW,null);
				if (li.size()>1 && s.num<=3){
					s.type = -2;
					return 0;
				}
				Segment tt = null;
				for (int kk = 0;kk<li.size();++kk){
					tt = li.get(kk);
					if (tt!=null && tt.type!=Segment.UNKNOWN) break;
				}
				if (tt==null ||  tt.type==Segment.UNKNOWN) {
					s.type = -2;
					return 0;
				}

				tp = tt.type;					
				r = Math.round(tt.radius-tp*tW)+tp*tW;
				if (r<REJECT_VALUE){
					r = MAX_RADIUS-1;
					tp = 0;
				}

			} else if (isCircle){
				tp = TrackSegment.getTurn(temp[0], temp[1], r, first.x, first.y, last.x, last.y);
				r = Math.round(r-tp*tW)+tp*tW;
				if (r<REJECT_VALUE){
					r = MAX_RADIUS-1;
					tp = 0;
				}
			}
			if (!isCircle || r>=MAX_RADIUS-1){				
				double dx = last.x-first.x;
				double dy = last.y-first.y;			
				double tmp1 = dy/dx;
				QuickLineFitter lf = new QuickLineFitter(new double[]{tmp1,last.y-last.x*tmp1},v,from,to);
				lf.fit();
				double a = lf.getA();
				double b = lf.getB();				
				first.x = (first.y-b)/a;
				last.x = (last.y-b)/a;
				if (s.start.y>first.y) s.start = new Vector2D(first);
				if (s.end.y<last.y) s.end = new Vector2D(last);
				center = null;
				r = MAX_RADIUS-1;
			}
		}
		return r;
	}//*/

	/*private static final int update(final Vector2D[] v,int from,int to,double tW,Vector2D center,double rad,Segment prev,Segment next,Segment s,int n){
		Vector2D first = v[from];
		Vector2D last = v[to-1];
		int size = to - from;
		int rd = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius-s.type*tW);
		double d = 0;
		double dd = 0;
		double r = -1;
		if (next!=null && last!=null && next.start.y<last.y){
			if (next.num<3) {
				next.type = UNKNOWN;				
			} else {
				next.start = new Vector2D(last);
				next.num--;
				next.reCalLength();
			}
		}

		//		if ((n>2 || (prev!=null && prev.type!=0)) && guessFromPrevNext(v, from, to, tW, prev, next, s, tmp, size, firstIndex, marks, n)) return 0;
		int tp = 0;	
		if (prev!=null && s.type !=0 && prev.type==0 && Math.abs(prev.start.x-prev.end.x)<TrackSegment.EPSILON*0.5){				
			double x0 = prev.start.x;
			Geom.getCircle2(first, last, new Vector2D(x0,0), new Vector2D(x0,1), temp);					
			r = Math.sqrt(temp[2]);
			tp = TrackSegment.getTurn(temp[0], temp[1], r, first.x, first.y, last.x, last.y);
			r = Math.round(r-s.type*tW)+s.type*tW;
			//			if (r<REJECT_VALUE) {
			//				s.type = UNKNOWN;
			//				s.radius = -1;				
			//				return 0;
			//
			//			}			
			double cx = temp[0];
			double cy = temp[1];
			double ox = (first.x+last.x)*0.5;
			double oy = (first.y+last.y)*0.5;
			double dx = ox - first.x;
			double dy = oy - first.y;
			double dxy = dx*dx+dy*dy;
			double vx = cx - ox;
			double vy = cy - oy;
			double dv = vx*vx+vy*vy;
			double dt = Math.sqrt((r*r-dxy)/dv); 
			ox += vx * dt;
			oy += vy * dt;
			center = new Vector2D(ox,oy);
			boolean ok = (s.radius==r);
			int[] map = s.map;
			double de = s.radius-r;
			if (de<0) de = -de;
			if (de>=1 && size>=3){				
				oal.size(0);
				ObjectList<Segment> li = bestGuess(v, from,to, tW, prev, next,oal);
				int sz = (li==null) ? 0 : li.size();
				Segment[] liArr = oalArr;
				if (sz>0){
					from = liArr[0].startIndex;
					to = liArr[sz-1].endIndex+1;
					size = to-from;
					first = v[from];
					last = v[to-1];
				}								

				if (li==null || (sz>1 && (d<0 || (s.num<=3 && s.map[rd]<=3)))){
					s.type = -2;
					s.map = null;
					s.radius = 0;
					return 0;
				}
				Segment t = null;

				for (int i = 0;i<sz;++i){
					t = liArr[i];				
					if (t!=null && t.type!=UNKNOWN) break;
				}				

				ok = (t!=null && t.type==tp && (t.radius==r || t.radius==s.radius));
				if (ok && t.radius==s.radius) {
					center = circle(first, t.end, center, s.radius);		
					r = t.radius;
					s.map[rd]++;
					if (prev!=null && s.startIndex<=prev.endIndex) {
						s.startIndex = t.startIndex;
						s.start = t.start;
					}

					if (next!=null && s.endIndex>=next.startIndex){
						s.endIndex = next.startIndex-1;
						s.end = t.end;
					}
					s.num = s.endIndex+1-s.startIndex;					
					s.center = center;						
				}
				if (s.start.y<=prev.end.y){
					Segment.removeFirstPoint(s, s.opp, null, prev.end);
					//					reSynchronize(s, s.opp, s.opp.startIndex, s.opp.endIndex+1, 1, tW+tW);
				}

				if (next!=null && s.end.y>=next.start.y){
					Segment.removeLastPoint(s, s.opp, null, next.start);
				}
			}
			if (!ok && check(v, from,to, center, r)<0){
				if (map[rd]>=3){
					for (int i = to-2;i>=from+1;--i){						
						last = v[i];
						Geom.getCircle2(first, last, new Vector2D(x0,0), new Vector2D(x0,1), temp);					
						r = Math.sqrt(temp[2]);
						tp = TrackSegment.getTurn(temp[0], temp[1], r, first.x, first.y, last.x, last.y);
						r = Math.round(r-s.type*tW)+s.type*tW;						
						if (r!=s.radius || check(v,from,i+1, center, r)<0) continue;
						center = circle(first, last, center, r);
						s.end = new Vector2D(last);														
						s.radius = r;													
						s.map[rd]++;
						ok = true;
						s.num = i+1-from;
						s.center = center;							
						s.reCalLength();	

					}///end of if map[rd]>3
				} else{
					s.type = Segment.UNKNOWN;
					s.map = null;
					s.radius = -1;
					return 0;
				}
			}
		} else if (size==2 && s.type!=0 && prev!=null && prev.type!=0){
			center = new Vector2D();
			r = selectRadius(tW, prev, s, first, last, center);
			tp = s.type;
			if (r==0) r=-1;				
		} else if (size==2 && s.type!=0 && next!=null && next.type!=0){
			r = selectRadius(tW, next, s, first, last, center);
			tp = s.type;
			if (r==0) r=-1;							
		} else if (size>=3 && s.type!=0){
			if (size==3){
				double[] temp =new double[3];										
				Geom.getCircle(first.x, first.y, last.x, last.y, v[1+from].x, v[1+from].y, temp);
				r = Math.sqrt(temp[2]);
				center = new Vector2D(temp[0],temp[1]);					
				tp = TrackSegment.getTurn(temp[0], temp[1], r, first.x, first.y, last.x, last.y);				
				r = Math.round(r-tp*tW)+tp*tW;
				center = circle(first, last, center, r);
			} else {			
				oal.size(0);
				ObjectList<Segment> li = bestGuess(v, from,to, tW, prev, next,oal);
				int sz = (li==null) ? 0 : li.size();
				Segment[] liArr = oalArr;
				if (sz>0){
					from = liArr[0].startIndex;
					to = liArr[sz-1].endIndex+1;
					size = to-from;
					first = v[from];
					last = v[to-1];
				}														
				int rt = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius-s.type*tW);				
				if (li==null || (sz>1 && (d<0 || (s.num<=3 && s.map[rt]<=3)))){
					s.type = -2;
					s.map = null;
					s.radius = 0;
					return 0;
				}
				Segment tt = null;
				//				int no = 0;
				int fr = -1;
				int t0 = -1;
				for (int kk = 0;kk<sz;++kk){
					tt = liArr[kk];					
					//					if (tt!=null && tt.type!=Segment.UNKNOWN && tt.radius>REJECT_VALUE && tt.length>0.5 && (qq==null || Math.abs(qq.radius-tt.radius)>=5)) ++no;
					if (tt!=null && tt.type!=Segment.UNKNOWN) {
						fr = tt.startIndex;
						t0 = tt.endIndex;
						break;
					}
				}

				//				if (no>1 || (no==1 && li.size()>1 && Math.abs(qq.radius-s.radius)>=5)){
				//					s.type = UNKNOWN;
				//					return true;
				//				}				
				if (tt==null ||  tt.type==Segment.UNKNOWN) {
					if (s.num<=3 && s.map[rt]<=3) s.type = -2;
					return 0;
				}
				if (prev.type!=Segment.UNKNOWN && s.start.y<=prev.end.y){
					//					s.start = new Vector2D(tt.start);
					//					s.startIndex = tt.startIndex;
					//					s.num = s.endIndex+1-s.startIndex;
					Segment.removeFirstPoint(s, s.opp, null, prev.end);
				}

				if (next!=null && s.end.y>=next.start.y){
					Segment.removeLastPoint(s, s.opp, null, next.start);
				}
				int rr = (tt.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(tt.radius-tt.type*tW);				
				double d1 = (tt.type!=0) ? check(v,fr,t0+1,tt.center,tt.radius) : 0;
				if (rr==rd) {
					s.map[rd]++;
					r = tt.radius;						
					d = check(v, fr,t0+1, s.center, s.radius);
					if ((d<0 || d>d1) && li.size()==1) s.center = tt.center;								
					tp = tt.type;
					if (li.size()>1){						
						if (s.num!=tt.num) {
							s.num = tt.num;
							s.end = tt.end;
						}						
						size = 0;						
						boolean ok = false;
						if (tt==li.get(0)){							
							if (d<0 || d>d1) s.center = tt.center;
							s.reCalLength();
							ok = true;
						} else {							
							if (d<0 || d>d1) s.center = tt.center;
							s.reCalLength();
							ok = true;
						}
						if (ok){
							size = 0;							
						}											
						return size;
					}
					return size;
				} else if (rr==MAX_RADIUS-1 || rr==0){
					r = MAX_RADIUS-1;
				} else {
					QuickCircleFitter cf = new QuickCircleFitter(new double[]{center.x,center.y},v,from,to);
					cf.fit();				
					Vector2D c = cf.getEstimatedCenter();
					r = cf.getEstimatedRadius();
					tp = TrackSegment.getTurn(c.x, c.y, r, first.x, first.y, last.x, last.y);
					center = cf.getEstimatedCenter();					
					r = Math.round(r-tp*tW)+tp*tW;
					Vector2D p = first;
					Vector2D q = last;
					Vector2D m = p.plus(q).times(0.5);
					Vector2D nn = q.minus(p).orthogonal().normalised();				
					double pq = p.distance(q) * 0.5;
					if (nn.dot(center.minus(m))<0) nn = nn.negated();
					center = m.plus(nn.times(Math.sqrt(r*r-pq*pq)));
					center.x = Math.round(center.x*PRECISION)/PRECISION;
					center.y = Math.round(center.y*PRECISION)/PRECISION;
					double d2 = check(v, from,to, center, r);
					int rr2 = (r>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(r-tp*tW);				
					//				if (rr2!=rd && li.size()>1 && Math.abs(rr-rd)<2 && (d1<0 || Math.abs(rr2-rd)>2)){
					//					s.type = UNKNOWN;
					//					return 0;
					//				}
					if (rr==rr2){
						//					s.map[rr]++;
						dd = (d1<0) ? d2 : (d2<0) ? d1 : (d1<d2) ? d1 : d2;
						if (dd==d1) center = tt.center;
					} else if (rr2==rd) {
						//					s.map[rd]++;
						dd = d2;														
					}else if (d2<0 && d1>=0){
						r = tt.radius;
						tp = tt.type;
						center = tt.center;
						dd = d1;
					} else if (d1>=0 && d2>=0 && d2>d1){
						//					s.map[rr2]++;
						r = tt.radius;
						tp = tt.type;
						center = tt.center;
						dd = d1;
					} else if (d1>=0 && d2>=0 && d1>d2){
						//					s.map[rr]++;
						dd = d1;
					} else if (d1<0 && d2<0 && d1>d2){					
						r = tt.radius;
						tp = tt.type;
						center = tt.center;
						dd = d1;
					} 
				}
			}
		}  else if (size==2 && s.type==0){
			if (s.start.y>first.y) s.start = new Vector2D(first);
			if (s.end.y<last.y) s.end = new Vector2D(last);
			r = MAX_RADIUS-1;
		} else if (size>=3 && s.type==0){
			double[] temp =new double[3];
			boolean isCircle = Geom.getCircle(first.x, first.y, last.x, last.y, v[1+from].x, v[1+from].y, temp);
			r = Math.sqrt(temp[2]);
			center= new Vector2D(temp[0],temp[1]);
			if (r>=MAX_RADIUS-1){
				r = MAX_RADIUS-1;
			} else if (isCircle && size>3){
				ObjectList<Segment> li = segmentize(v, from, to, tW,null);
				if (li.size()>1 && s.num<=3){
					s.type = -2;					
					return 0;
				}
				Segment tt = null;
				for (int kk = 0;kk<li.size();++kk){
					tt = li.get(kk);
					if (tt!=null && tt.type!=Segment.UNKNOWN) break;
				}
				if (tt==null ||  tt.type==Segment.UNKNOWN) {
					s.type = -2;
					return 0;
				}

				tp = tt.type;					
				r = Math.round(tt.radius-tp*tW)+tp*tW;
				if (r<REJECT_VALUE){
					r = MAX_RADIUS-1;
					tp = 0;
				}

			} else if (isCircle){
				tp = TrackSegment.getTurn(temp[0], temp[1], r, first.x, first.y, last.x, last.y);
				r = Math.round(r-tp*tW)+tp*tW;
				if (r<20){
					r = MAX_RADIUS-1;
					tp = 0;
				}
			}
			if (!isCircle || r>=MAX_RADIUS-1){				
				double dx = last.x-first.x;
				double dy = last.y-first.y;			
				double tmp1 = dy/dx;
				QuickLineFitter lf = new QuickLineFitter(new double[]{tmp1,last.y-last.x*tmp1},v,from,to);
				lf.fit();
				double a = lf.getA();
				double b = lf.getB();				
				first.x = (first.y-b)/a;
				last.x = (last.y-b)/a;
				if (s.start.y>first.y) s.start = new Vector2D(first);
				if (s.end.y<last.y) s.end = new Vector2D(last);
				center = null;
				r = MAX_RADIUS-1;
			}
		}		

		if (r!=-1){
			if (Math.abs(r-rad)<0.5 || (r>=MAX_RADIUS-1 && rd>=MAX_RADIUS-1)){
				if (s.start.y>first.y) s.start = new Vector2D(first);
				if (s.end.y<last.y) s.end = new Vector2D(last);
				if (s.type!=Segment.UNKNOWN && s.map!=null) s.map[rd]++;

				if (dd==0 && center!=null) dd = check(v, from,to, center, r);
				if (d==0) d = check(v, from,to, s.center, r);
				if (d<0 || d>dd){
					s.center = center;
				}
				return size;
			} else {
				int rr = (r>=MAX_RADIUS-1) ? MAX_RADIUS-1 : (s.type==0) ? double2int(r-tp*tW): double2int(r-tp*tW);
				//				s.map[rd]--;
				int nr = (s.map==null) ? 1 : s.map[rd];
				if (nr==1 && size<=2 &&  s.type!=0 && (prev!=null && prev.type!=0 || mark!=2) ) {
					s.type = -2;
					return 0;
				}
				if (s.map!=null) s.map[rr]++;
				if (s.minR>rr) s.minR = rr;
				if (s.maxR<rr) s.maxR = rr;
				if (s.map!=null && nr<s.map[rr] && rr<MAX_RADIUS-1 && center!=null ){
					/*if (size>2 && ((prev!=null && prev.type!=0) || mark!=2)){
						ObjectList<Segment> li = segmentize(v, from, to, tW,null,null,null);
						if (li.size()>1 && s.num<=3){
							s.type = -2;
							return 0;
						}
						Segment tt = null;
						for (int kk = 0;kk<li.size();++kk){
							tt = li.get(kk);
							if (tt!=null && tt.type!=Segment.UNKNOWN) break;
						}
						if (tt==null ||  tt.type==Segment.UNKNOWN) {
							s.type = -2;
							return 0;
						}
					}
					if (s.map[rr]==1){
						s.type = UNKNOWN;
						s.radius = 0;
						s.map = null;
						return 0;
					}

					Vector2D m = first.plus(last).times(0.5);
					Vector2D nn = last.minus(first).orthogonal().normalised();
					double pq = last.distance(first) * 0.5;
					if (nn.dot(center.minus(m))<0) nn = nn.negated();
					center = m.plus(nn.times(Math.sqrt(r*r-pq*pq)));
					rad = r;
					s.center = center;
					s.start = new Vector2D(first);
					s.end = new Vector2D(last);
					s.type = tp;
					s.radius = r;
					if (s.type==0) s.type = tp;
					s.reCalLength();					
				} else if (s.map!=null && nr<s.map[rr] && rr>=MAX_RADIUS-1){
					ObjectList<Segment> li = segmentize(v, from, to, tW,null);
					if (li.size()>1 && s.num<=3){
						s.type = -2;
						s.radius = 0;
						s.map = null;
						return 0;
					}
					Segment tt = null;
					for (int kk = 0;kk<li.size();++kk){
						tt = li.get(kk);
						if (tt!=null && tt.type!=Segment.UNKNOWN) break;
					}
					if (tt==null ||  tt.type==Segment.UNKNOWN) {
						s.type = -2;
						s.radius = 0;
						s.map = null;
						return 0;
					}
					s.copy(tt);					
				} else if (s.map!=null && nr==s.map[rr]){
					s.type = UNKNOWN;
					s.radius = 0;
					s.map = null;
					return 0;
				}
				//			if (nr<s.map[rr]) getAllPoints(v,from,to,tW,center,r,prev,s,tmp,size,marks,n);
			}
		}

		return size;
	}//*/

	/*private static final int getAllPoints(final Vector2D[] v,int from,int to,double tW,Vector2D center,double rad,Segment prev,Segment next,Segment s,int n){
		//		int lastIndex = to-1;		
		int size = to - from;

		//		Vector2D first = (size<=0) ? s.start : tmp[0];
		//		Vector2D last = null;
		//		double r = -1;
		boolean ok = true;
		//		if (size>=3 && (n>2 || (prev!=null && prev.type!=0))){
		//			if (check(tmp, size, s.center, s.radius)<0) {
		//				s.type = Segment.UNKNOWN;
		//				return 0;
		//			}
		//		}

		//		if (size>1 && !CircleDriver2.inTurn){
		//			for (int i=0;i<size;++i){
		//				if (tmp[i].certain) {
		//					ok = false;
		//					break;
		//				}
		//			}
		//		} else ok = false; 
		ok = false;
		if (!ok && size>1) {
			int count = update(v,from,to,tW, center, rad, prev, next, s,n); 			
			if (count!=size){
				if (count==0) return 0;
				//				int indx = 0;
				//				for (int k = firstIndex;k<firstIndex+size;++k)
				//					if (marks[k]==n) {
				//						indx = k;
				//						break;
				//					}
				//				
				//				
				//				first = (size<=0) ? null : tmp[0];
				//				last = (size<=0) ? null : tmp[count-1];
				//				if (firstIndex==indx){//firstIndex unchanged
				//					indx = expandBackward(v, from, to, tW, s.center, s.radius, prev, next, s,tmp,size, firstIndex, last, marks, n);					
				//					size = count + firstIndex-indx;
				//					s.num = Math.max(s.num,size);
				//					if (firstIndex!=indx) s.reCalLength();
				//					return size;
				//				} else if (v[firstIndex+size-1]==tmp[count-1]){								
				//					int oldLastIdx = indx+size-1;
				//					lastIndex = expandForward(v, from, to, tW, s.center, s.radius, prev, next, s,tmp,size, oldLastIdx, first,marks, n);
				//					size = count+lastIndex-oldLastIdx;
				//					s.num = Math.max(s.num,size);
				//					return size;
				//				}
				return count;
			} else {
				if (s.type==Segment.UNKNOWN) return 0;
				ok = true;
			}
		}
		//		int oldLastIdx = lastIndex;
		//		if (ok){
		//			int idx = expandBackward(v, from, to, tW, s.center, s.radius, prev, next, s,tmp,size, firstIndex, last, marks, n);
		//			if (firstIndex != idx){
		//				size += firstIndex-idx;
		//				firstIndex = idx;
		//				first = v[idx];
		//			}
		//			lastIndex = expandForward(v, from, to, tW, s.center, s.radius, prev, next, s,tmp,size, lastIndex, first,marks, n);			
		//		}

		size = to-from;			
		s.num = Math.max(s.num,size);
		//			System.arraycopy(v, firstIndex, tmp, 0, size);
		if (size==2 && s.type!=0 && (prev==null || mark!=2 || prev.type!=0)){
			boolean good = true;
			double E = (CircleDriver2.inTurn) ? TrackSegment.EPSILON : EPSILON;
			for (int i=from;i<to;++i){
				if (s.center==null || Math.abs(v[i].distance(s.center)-s.radius)>E){
					good = false;
					break;
				}
			}
			if (!good){
				//				int rd = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius-s.type*tW);
				//				if (s.map!=null) s.map[rd]++;
				//			} else {
				s.type = Segment.UNKNOWN;
			}
		}


		return size;
	}//*/

	/*private static final int findGap(final Vector2D[] v,int from, int to,int[] marks,Vector2D[] temp){
		int size = 0;	
		for (int j = from;j<to;++j){
			if (marks[j]==0)
				temp[size++] = v[j];
		}
		return size;
	}//*/

	private static final boolean guessPN(Segment prev,Segment next,double tW,Vector2D first,Vector2D last,double[] rs){
		p.type = UNKNOWN;
		n.type = UNKNOWN;
		radiusFrom2Points(prev, first, last, tW, p);
		if (p.type==UNKNOWN) return false;
		radiusFrom2Points(next, first, last, tW, n);
		if (n.type==UNKNOWN || n.type!=p.type) return false;
		if (p.type==0 || p.radius==n.radius) {			
			if (p.type!=0 && p.center!=null) {
				rs[0] = p.center.x;
				rs[1] = p.center.y;
			} else {
				rs[0] = 0;
				rs[1] = 0;
			}
			rs[2] = (p.type==0) ? 0 : p.radius;
			rs[3] = p.type;
			return true;
		}
		return false;
	}

	private static double radiusFrom2Points(Segment prev,Vector2D first,Vector2D last,double tW,Segment s){
//		long ti = System.nanoTime();
		if (prev==null || prev.type==UNKNOWN || prev.end!=null && prev.end.distance(first)<1) return -1;				
		double r = 0;
		double tw = tW+tW;
		double startX = first.x;
		double startY = first.y;
		double endX = last.x;
		double endY = last.y;
		if (tw<0) tw = -tw;
		//		if (s!=null && s.map==null) {
		//			s.map = new int[MAX_RADIUS];
		//			if (s.type!=UNKNOWN){
		//				int rd = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius-s.type*tW);
		//				s.map[rd]++;
		//			}
		//		}
		if (prev.type==0){
			double sx = 0;
			double sy = 0;
			double lx = 0;
			double ly = 0;
			boolean isStraight = Math.abs(prev.start.x-prev.end.x)<E; 
			if (isStraight){//isStraight
				double xx = prev.start.x;
				sx = xx;
				lx = xx;
				sy = 0;				
				ly = 1;									
			} else {
				Vector2D sF = prev.start;
				Vector2D sL = prev.end;
				sx = sF.x;
				sy = sF.y;
				lx = sL.x;
				ly = sL.y;				
			}
			Geom.getCircle2(startX,startY, endX,endY, sx,sy, lx,ly, temp);
			
								
			r = Math.sqrt(temp[2]);
			int tp = 0;
			if (r<MAX_RADIUS-1){
				double cx = temp[0];
				double cy = temp[1];
				double ax = startX-cx;
				double ay = startY-cy;
				double bx = endX-cx;
				double by = endY-cy;
				double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);			
				if (angle<-Math.PI) 
					angle += 2*Math.PI;
				else if (angle>Math.PI) 
					angle -= 2*Math.PI;
	
				tp = (angle<0) ? -1 : 1;
			}
			double ox = temp[0];
			double oy = temp[1];
			r = Math.round(r-tp*tW)+tp*tW;						
			if (r<=REJECT_VALUE) return -1;
			if (r>=MAX_RADIUS-1){
				if (s!=null) apply(s, tW,tp, first, last, null, Double.MAX_VALUE);
//				long endTime = (System.nanoTime()-ti)/1000000;
//				if (CircleDriver2.debug || endTime>=1) System.out.println("End radiuFrom2Points : "+endTime+"   at "+CircleDriver2.time+" s.    ");
				return Double.MAX_VALUE;
			}
			
			double d = Math.sqrt(Geom.ptLineDistSq(sx, sy, lx, ly, ox, oy, null));
			d = Math.round(d-tW)+tW;
			if (d==r){
				Vector2D center = circle(first, last, ox,oy, r);
				if (s!=null) apply(s, tW,tp, first, last, center, r);
//				long endTime = (System.nanoTime()-ti)/1000000;
//				if (CircleDriver2.debug || endTime>=1) System.out.println("End radiuFrom2Points : "+endTime+"   at "+CircleDriver2.time+" s.    ");
				return d;
			}


		} else if (prev.type!=0){			
			int no = 0;
			int prL = 0;
			double sx = prev.center.x;
			double sy = prev.center.y;
			for (int tp = -1;tp<2;tp+=2){		
				no = 0;				
				if (prev.type==tp) 
					no = Geom.getCircle4(startX,startY, endX,endY, sx,sy, prev.radius, temp);					
				else if (prev.type!=0 && prev.center!=null) no = Geom.getCircle5(startX,startY, endX,endY, sx,sy, prev.radius, temp);							
				for (int kk =no-1;kk>=0;--kk){
					double rr = Math.round(temp[2+3*kk]-tp*tW)+tp*tW;						
					if (rr>=MAX_RADIUS-1){
						double d = Math.sqrt(Geom.ptLineDistSq(startX, startY, endX, endY, sx, sy, tmp1));
						d = Math.round(d-tW)+tW;
						if (d==prev.radius && endY-startY>1 && tmp1[1]<startY && prev.type*(endX-startX)>0){
							if (s!=null) apply(s, tW,0, first, last, null, Double.MAX_VALUE);	
//							long endTime = (System.nanoTime()-ti)/1000000;
//							if (CircleDriver2.debug || endTime>=1) System.out.println("End radiuFrom2Points : "+endTime+"   at "+CircleDriver2.time+" s.    ");
							return (s!=null) ? s.radius : MAX_RADIUS-1;
						}
//						long endTime = (System.nanoTime()-ti)/1000000;
//						if (CircleDriver2.debug || endTime>=1) System.out.println("End radiuFrom2Points : "+endTime+"   at "+CircleDriver2.time+" s.    ");
						return 0;
					}
					if (rr<=REJECT_VALUE) continue;					
					Vector2D center = pCntr[prL]; 
					circle(first, last, temp[3*kk],temp[1+3*kk], rr,center);
					//					if (CircleDriver2.time>=CircleDriver2.BREAK_TIME){														
					double dx = center.x - sx;
					double dy = center.y - sy;
					double dl = Math.round(Math.sqrt(dx*dx+dy*dy));
					if (dl!=0){
						if (prev.type==tp && dy<0){
							dx = - dx;
							dy = -dy;
						}					
						double d = prev.radius/dl;
//						double ssx = sx + dx*d;
						double ssy = sy + dy*d;
						if (prev.end.y<startY){
							if (ssy<=prev.end.y-0.5 || ssy>startY+0.2) continue;
						} else if (prev.start.y>endY){
							if (ssy>=prev.start.y+0.2 || ssy<endY-0.5) continue;
						}
//						if ((sy<prev.start.y && startY>prev.start.y) || (sy>endY && endY>prev.end.y) || (sy<startY && startY<prev.start.y) || (sy>prev.end.y && prev.end.y>endY)) continue;
					}
					//					}

					if ((rr==prev.radius && tp==prev.type) || (s!=null && s.type==tp && rr==s.radius)){
						if (s!=null) apply(s, tW,tp, first, last, center, rr);
						return (s!=null) ? s.radius : rr;
					}					
					pr[prL] = rr;										
//					pCntr[prL] = center;
					pTp[prL] = tp;
					prL++;
				}				
			}

			if (prL==0 && prev.type!=0  && endY-startY>1) {				
				double d = Math.sqrt(Geom.ptLineDistSq(startX, startY, endX, endY, sx, sy, rs));
				d = Math.round((d-tW)*100)/100.0d+tW;
				if (d==prev.radius && rs[1]<=startY){
					if (s!=null) {
						apply(s, tW,0, first, last, null, Double.MAX_VALUE);
//						long endTime = (System.nanoTime()-ti)/1000000;
//						if (CircleDriver2.debug || endTime>=1) System.out.println("End radiuFrom2Points : "+endTime+"   at "+CircleDriver2.time+" s.    ");
						return Double.MAX_VALUE;
					}
//					long endTime = (System.nanoTime()-ti)/1000000;
//					if (CircleDriver2.debug || endTime>=1) System.out.println("End radiuFrom2Points : "+endTime+"   at "+CircleDriver2.time+" s.    ");
					return Double.MAX_VALUE;
				}
//				long endTime = (System.nanoTime()-ti)/1000000;
//				if (CircleDriver2.debug || endTime>=1) System.out.println("End radiuFrom2Points : "+endTime+"   at "+CircleDriver2.time+" s.    ");
				return -1;
			}

			if (prL==0 && prev.type==0 && s!=null) {
				double d = Math.sqrt(Geom.ptLineDistSq(prev.start.x, prev.start.y, prev.end.x, prev.end.y, s.center.x, s.center.y, rs));
				d = Math.round((d-tW)*100)/100.0d+tW;
				if (d==s.radius && rs[1]>=prev.end.y){
					apply(s, tW,0, first, last, null, Double.MAX_VALUE);
//					long endTime = (System.nanoTime()-ti)/1000000;
//					if (CircleDriver2.debug || endTime>=1) System.out.println("End radiuFrom2Points : "+endTime+"   at "+CircleDriver2.time+" s.    ");
					return Double.MAX_VALUE;
				}
//				long endTime = (System.nanoTime()-ti)/1000000;
//				if (CircleDriver2.debug || endTime>=1) System.out.println("End radiuFrom2Points : "+endTime+"   at "+CircleDriver2.time+" s.    ");
				return -1;
			}

			if (s==null || s.type==UNKNOWN){
				double dmin = Double.MAX_VALUE;
				int index = -1;
				for (int i=prL-1;i>=0;--i){
					double rr = pr[i];					
					if (rr<REJECT_VALUE) continue;
					if (prev.type==0 || prev.radius>30){
						if (rr<20) continue;
					}
					double d = rr-prev.radius;
					if (d<0) d=-d;
					int tp = pTp[i];
					if (dmin>d) {
						dmin = d;
						index = i;
					} else if (dmin==d && tp==prev.type){
						index = i;
					}
				}
				if (index>=0 && s!=null){
					apply(s,tW,pTp[index],first,last,pCntr[index],pr[index]);
//					long endTime = (System.nanoTime()-ti)/1000000;
//					if (CircleDriver2.debug || endTime>=1) System.out.println("End radiuFrom2Points : "+endTime+"   at "+CircleDriver2.time+" s.    ");
					return pr[index];
				}
				if (index>=0) return pr[index];
			} else if (prL>0){
				double dmin = Double.MAX_VALUE;
				int index = -1;
				double sr = s.radius;
				Vector2D point = new Vector2D();
				for (int i=prL-1;i>=0;--i){
					double rr = pr[i];
					Vector2D center = pCntr[i];
					if (rr<=REJECT_VALUE) continue;	
					if (prev.type==0 || prev.radius>30){
						if (rr<20) continue;
					}
					ns.type = pTp[i];
					ns.center = center;
					ns.start = first;
					ns.end = last;
					ns.num = s.num;
					ns.radius = rr;					
					if (isConnected(prev, ns, tW, point)){
						if (s!=null) {
							apply(s,tW,ns.type,first,last,center,rr);						
							if (s.radius==rr){
								s.lower = point;
								prev.upper = point;
							}							
						};
					}
					double d = rr-sr;
					if (d<0) d =-d;
					if (dmin>d) {
						dmin = d;
						index = i;
					}			
				}//end of for

				if (index>=0 && s!=null){
					apply(s,tW,pTp[index],first,last,pCntr[index],pr[index]);					
				}
//				long endTime = (System.nanoTime()-ti)/1000000;
//				if (CircleDriver2.debug || endTime>=1) System.out.println("End radiuFrom2Points : "+endTime+"   at "+CircleDriver2.time+" s.    ");
				if (index>=0) return pr[index];
			}
		}

//		long endTime = (System.nanoTime()-ti)/1000000;
//		if (CircleDriver2.debug || endTime>=1) System.out.println("End radiuFrom2Points : "+endTime+"   at "+CircleDriver2.time+" s.    ");
		return r;
	}
	
	private static boolean isReject(Segment prev,Segment s,Segment next,double tW){
		if (s==null || s.type==Segment.UNKNOWN) return false;
		Vector2D first = s.start;
		Vector2D last = s.end;
		double r = s.radius;
		if (s.type!=0 && (!CircleDriver2.inTurn && (s.type*(first.x-last.x)>=0 || last.y-first.y<=1 || s.end.y>99) || last.y-first.y>=r+2*tW || last.y-Math.max(0,first.y)>=r || s.radius<=REJECT_VALUE || s.radius>=MAX_RADIUS-1 || s.radius-2*s.type*tW>=MAX_RADIUS-1 || s.radius-2*s.type*tW<=REJECT_VALUE*0.5))
			return true;
		
		if (!CircleDriver2.inTurn && s.type!=0){
			if (prev!=null && prev.type!=0 && s.end.y-s.start.y<5 && first.distance(prev.end)<1 && (prev.type!=s.type || Math.abs(s.radius-prev.radius)>tW))
				return true;
		}
		return false;
	}

	/*private final static boolean tryConnect(Segment prev,Segment s,double tW,Vector2D point){
		if (prev.type==Segment.UNKNOWN || s.type==Segment.UNKNOWN) return false;
		Vector2D pt = (point==null) ? new Vector2D() : point;
		int which = (tW<0) ? 1 : -1;
		Vector2D[] opoints = (which==1) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;
		int otherTo = (which==1) ? CircleDriver2.edgeDetector.lSize:  CircleDriver2.edgeDetector.rSize;
		double tw = tW+tW;
		if (tw<0) tw = -tw;
		
		
		
		//		Segment pr = null;
		if (isConnected(prev, s, tW, pt) && pt.y>prev.start.y && pt.y<s.end.y) {
			if (s.endIndex<1 || pt.y>s.points[s.endIndex-1].y) return false;
			Segment op = (s.opp==null) ? new Segment() : s.opp;
			op.points = (s.opp!=null) ? s.opp.points : opoints;
			reSynchronize(s, op, 0, otherTo, -which, tw);
			s.opp = op;
			Vector2D pLast = prev.end;
			Vector2D start = s.start;
			double yy = pt.y;
			if ((prev.type!=s.type || prev.radius!=s.radius)&& (pLast.y>=yy || start.y<=yy || prev.endIndex+1<s.startIndex))				
				connect(prev, s, pt, tW);												
			return true;
		}
		Segment tmp = new Segment();
		tmp.points = s.points;	
		 
		if (((prev.type!=0 && prev.center.y!=0) || (prev.type==0 && Math.abs(prev.start.x-prev.end.x)>E)) && radiusFrom2Points(prev, s.start, s.end, tW, tmp)!=s.radius && tmp.type!=UNKNOWN && (tmp.type==0 || check(tmp.points, s.startIndex, s.endIndex+1, tmp.center, tmp.radius)>=0) && isConnected(prev, tmp, tW, pt) &&  pt.y>prev.start.y && pt.y<tmp.end.y){
			if (s.endIndex<1 || pt.y>s.points[s.endIndex-1].y) return false;
			tmp.startIndex = s.startIndex;
			tmp.endIndex = s.endIndex;
			tmp.num = s.num;
			Segment op = new Segment();
			op.points = (s.opp!=null) ? s.opp.points : opoints;
			reSynchronize(tmp, op, 0, otherTo, -which, tw);
			//			s.opp = op;
			tmp.opp = op;
			Vector2D pLast = prev.end;
			Vector2D start = tmp.start;
			double yy = pt.y;
			if (prev.type!=Segment.UNKNOWN && tmp.type!=Segment.UNKNOWN && (prev.type!=tmp.type || prev.radius!=tmp.radius)&& (pLast.y>=yy || start.y<=yy || prev.endIndex+1<s.startIndex)){			
				connect(prev, tmp, pt, tW);
				if (prev.type==Segment.UNKNOWN) {
					op.type = Segment.UNKNOWN;
					prev.updated = true;
					op.updated = true;
					return true;
				}
				if (tmp.type==Segment.UNKNOWN){
					s.type = Segment.UNKNOWN;
					if (s.opp!=null) s.opp.type = Segment.UNKNOWN;
					return true;
				}
			}

			if ((prev.type==Segment.UNKNOWN && s.type!=Segment.UNKNOWN) || (prev.endIndex+1==tmp.startIndex && prev.upper!=null)){
				Segment.apply(s, tW,tmp.type, tmp.start, tmp.end, tmp.center, tmp.radius);
				if (prev.type==Segment.UNKNOWN && s.start.y>=prev.end.y){
					s.start = tmp.start;
					s.num = tmp.num;
					s.startIndex = tmp.startIndex;
				} 
				if (s.radius==tmp.radius && prev.type!=Segment.UNKNOWN && pt.y>=prev.end.y && pt.y<=s.start.y){
					s.lower = pt;
					prev.upper = new Vector2D(pt);
				} else if (s.radius!=tmp.radius && s.type!=0 && check(s.points,s.startIndex,s.endIndex+1,s.center,s.radius)<0){
					s.radius = tmp.radius;
					s.map = tmp.map;
					if (s.opp == null) 
						s.opp = op;
					else reSynchronize(s, s.opp, 0, otherTo, -which, tw);
				}
				return true;
			}
		} else if (tmp.type!=0 && tmp.type==prev.type && tmp.radius==prev.radius){
			Vector2D[] points = s.points;
			Vector2D fst = (prev.num>0) ? points[prev.startIndex] : prev.start;
			Vector2D lst = (s.num>0) ? points[s.endIndex] : s.end;
			Vector2D center = prev.center;
			center = circle(fst, lst, center, tmp.radius);
			double d = 0;
			boolean ok = true;
			double cx = center.x;
			double cy = center.y;
			int startIndex = (prev.num>0) ? prev.startIndex : s.startIndex;
			int endIndex = s.endIndex+1;
			double E = EPSILON*2;			
			for (int i=startIndex;i<endIndex;++i){
				Vector2D ss = points[i];
				double dx = ss.x-cx;
				double dy = ss.y-cy;			
				double e = Math.sqrt(dx*dx+dy*dy)-tmp.radius;
				if (e<0) e = -e;
				if (e>E) {
					ok = false;
					break;
				}
				d+=e;
				//				center.x = cx;
				//				center.y = cy;
				//				if (fst!=ss){
				//					center = circle(fst, ss, center, tmp.radius);
				//					if (center.y!=0){
				//						ok = false;
				//						break;
				//					}
				//				}
			}			
			if (ok){
				if (s.endIndex<1 || pt.y>s.points[s.endIndex-1].y) return false;
				tmp.startIndex = s.startIndex;
				tmp.endIndex = s.endIndex;
				tmp.num = s.num;
				Segment op = new Segment();
				op.points = (s.opp!=null) ? s.opp.points : opoints;
				reSynchronize(tmp, op, 0, otherTo, -which, tw);
				//				s.opp = op;
				tmp.opp = op;
				Vector2D pLast = prev.end;
				Vector2D start = tmp.start;
				double yy = pt.y;
				if ((prev.type!=tmp.type || prev.radius!=tmp.radius)&& (pLast.y>=yy-SMALL_MARGIN || start.y<=yy+SMALL_MARGIN || prev.endIndex+1<s.startIndex))					
					connect(prev, tmp, pt, tW);

				if (prev.endIndex+1==tmp.startIndex){
					Segment.apply(s, tW,tmp.type, tmp.start, tmp.end, tmp.center, tmp.radius);
					if (s.radius==tmp.radius && pt.y>=prev.end.y && pt.y<=s.start.y){
						s.lower = pt;
						prev.upper = new Vector2D(pt);
					} else if (s.radius!=tmp.radius && check(s.points,s.startIndex,s.endIndex+1,s.center,s.radius)<0){
						s.radius = tmp.radius;
						s.map = tmp.map;
						if (s.opp == null){
							s.opp = op;
						} else reSynchronize(s, s.opp, 0, otherTo, -which, tw);
					}					
					return true;
				}
			}
		}
		//		if (prev.type==Segment.UNKNOWN){
		//			prev.copy(pr);
		//			if (prev.type!=Segment.UNKNOWN) {
		//				if (prev.opp==null) prev.opp = new Segment();
		//				reSynchronize(prev, prev.opp, 0, otherTo, -which, tw);
		//			}
		//		}
		return false;
	}


	private static void forwardOnly(final Vector2D[] v,Segment prev,Segment s,int to,double tW){
		int from = s.endIndex+1;
		Vector2D vv;		
		vv = (s.num>1) ? v[s.startIndex] : s.start;			
		double x1 = vv.x;
		double y1 = vv.y;
		vv = (s.num>1) ? v[s.endIndex] : s.end;
		double xx = vv.x;
		double yy = vv.y;
		Vector2D point = s.lower;
		if (s.type==0){			
			final double allowedDist = TrackSegment.EPSILON*TrackSegment.EPSILON;			
			double[] result = new double[3];
			double[] coef = new double[2];
			double r;
			int j = s.startIndex;
			double a;
			double b;			
			for (int i = from;i<to;++i){
				vv = v[i];
				double x = vv.x;
				double y = vv.y;					
				if (Geom.ptLineDistSq(x1, y1, xx, yy, x, y, null)>allowedDist)
					break;		
				boolean isCir = Geom.getCircle(x1, y1, xx, yy, x, y, result);				
				if (isCir && (r = Math.sqrt(result[2]))>REJECT_VALUE && r<MAX_RADIUS-1) break;
				xx = x;
				yy = y;
				if (i-j>2){											
					double total = bestFitLine(v, j, i, coef);
					if (total>=EPSILON) break;												
					a = coef[0];
					b = coef[1];																			
					if (!Double.isInfinite(a)){
						x1 = (y1-b)/a;
						xx = (yy-b)/a;
					}
					s.num++;
					Vector2D oldStart = s.start;
					Vector2D oldEnd = s.end;
					s.endIndex = i;
					s.start = new Vector2D(x1,y1);
					s.end = new Vector2D(xx,yy);
					if (!isConnected(prev, s, tW, point)){
						s.num--;
						s.endIndex--;
						s.start = oldStart;
						s.end = oldEnd;
						break;
					}
				}
			}
		} else {
			Vector2D center = s.center;
			double tx = center.x;
			double ty = center.y;
			double radius = s.radius;
			final double E = (CircleDriver2.inTurn) ? 0.1*TrackSegment.EPSILON : TrackSegment.EPSILON*0.5;
			double EE = E+E;
			int i = s.startIndex;
			for (int j=from;j<to;++j){
				vv = v[j];
				double dx0 = vv.x-tx;
				double dy0 = vv.y-ty;						
				double dd0 = Math.sqrt(dx0*dx0+dy0*dy0)-radius;
				dd0 = (dd0<0 ? -dd0 : dd0);
				if (dd0>=EE)						
					break;								

				if (j>=i+3){
					double ttx = (x1+vv.x)*0.5;
					double tty = (y1+vv.y)*0.5;

					double ddx = x1-ttx;
					double ddy = y1-tty;
					double d = ddx*ddx+ddy*ddy;																

					double nx = -ddy;
					double ny = ddx;		
					double dn = nx*nx+ny*ny;

					double qx = tx-ttx;
					double qy = ty-tty;
					if (nx*qx+ny*qy<0) {
						nx = -nx;
						ny = -ny;
					}

					double dt = Math.sqrt((radius*radius-d)/dn);
					ttx += nx * dt;
					tty += ny * dt;																					

					double de = tty-ty;
					if (de>EPSILON*10 || de<-EPSILON*10) break;
					tx = ttx;
					ty = tty;
					if (ty<SMALL_MARGIN && ty>-SMALL_MARGIN) {
						tx = (tx<0) ? -Math.sqrt(tx*tx+ty*ty) : Math.sqrt(tx*tx+ty*ty);
						ty = 0;
					}
					Vector2D oldStart = s.start;
					Vector2D oldEnd = s.end;
					s.center = new Vector2D(tx,ty);
					s.num++;
					s.endIndex = j;
					s.end = new Vector2D(vv);
					if (prev!=null && s!=null && prev.type!=Segment.UNKNOWN && s.type!=Segment.UNKNOWN && !isConnected(prev, s, tW, point)){
						s.num--;
						s.endIndex--;
						s.start = oldStart;
						s.end = oldEnd;
						break;
					}
				}
			}												

		}
	}//*/

	/*private static final void adjust(Segment prev,ObjectArrayList<Segment> ols,Segment next,double tW){
		int osz = ols.size();
		int oldOsz = osz;
		Segment[] olsArr = ols.elements();
		Vector2D pupper = new Vector2D();
		Vector2D nlower = new Vector2D();
		int which = (tW<0) ? 1 : -1;
		Vector2D[] opoints = (which==1) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;
		Vector2D[] v = (which==-1) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;
		int otherTo = (which==1) ? CircleDriver2.edgeDetector.lSize:  CircleDriver2.edgeDetector.rSize;
		double abstW = (tW<0) ? -tW : tW;
		double tw = abstW*2;


		if (osz<1) return;
		for (int i=0;i<osz;++i){
			Segment s = olsArr[i];			
//			if (s!=null && s.type!=0 && s.type!=UNKNOWN) break;
			if (s!=null && s.type!=UNKNOWN ){
				if (prev!=null && prev.type!=Segment.UNKNOWN && ((prev.type==s.type && (prev.radius==s.radius || s.type==0) ) || (isConnected(prev, s, tW, pupper)))) {
					boolean ok = false;
					if (prev.type==s.type && s.type==0){
						ok = true;
						double dx = prev.end.x - prev.start.x;
						double ddx = s.end.x - s.start.x;
						if (dx<0) dx = -dx;
						if (ddx<0) ddx = -ddx;
						if (dx<=TrackSegment.EPSILON && ddx>TrackSegment.EPSILON) continue;
						if (dx>TrackSegment.EPSILON && ddx<=TrackSegment.EPSILON) continue;
						if (dx>TrackSegment.EPSILON && ddx>TrackSegment.EPSILON){
							double d = Math.max(prev.start.y, s.start.y);
							double ll = Math.min(prev.end.y, s.end.y);
							if (ll-d<0){
								double dy = prev.end.y - prev.start.y;
								double ddy = s.end.y - s.start.y;
								if (Math.abs(Math.atan2(dy, dx)-Math.atan2(ddy, ddx))>0.5) continue;
							}
						}
						if (i>0) {
							osz -= i+1;				
							System.arraycopy(olsArr, i+1, olsArr, 0, osz);							
						}						
						//						if (s.startIndex!=prev.endIndex+1){
						//							s.startIndex = prev.endIndex+1;
						//							s.start = new Vector2D(v[s.startIndex]);
						//							if (ddx<=TrackSegment.EPSILON && dx<=TrackSegment.EPSILON) s.start.x = s.end.x;
						//							s.num = s.endIndex+1-s.startIndex;
						//						}
					} else if (prev.type==s.type && prev.radius==s.radius && prev.center!=null && s.center!=null && Math.abs(prev.center.y-s.center.y)<2){
						if (i>0) {
							osz -= i+1;				
							System.arraycopy(olsArr, i+1, olsArr, 0, osz);							
						}	
						prev.endIndex = s.endIndex;
						prev.end = new Vector2D(s.end);
						prev.num = prev.endIndex+1-prev.startIndex;
						prev.center = circle(prev.start, prev.end, prev.center, prev.radius);						
						ok = true;
						i = 0 ;
						continue;
					}

					Segment op = (s.opp==null) ? new Segment() : s.opp;
					op.points = (s.opp!=null) ? s.opp.points : opoints;
					s.opp = op;
					reSynchronize(s, op, 0, otherTo, -which, tw);
					if (!ok){
						Vector2D pLast = prev.end;
						Vector2D start = s.start;
						double yy = pupper.y;					
						if ((s.center==null || s.center.y!=0) && (yy<prev.start.y || yy>s.end.y)) continue;
						if ((prev.type!=s.type || prev.radius!=s.radius)&& (pLast.y>=yy || start.y<=yy || prev.endIndex+1<s.startIndex)){
							int prevNum = prev.num;
							int sNum = s.num;
							connect(prev, s, pupper.y, tW);
							if (prevNum!=prev.num || sNum!=s.num){
								if ((osz-=i)>0) System.arraycopy(olsArr, i, olsArr, 0, osz);
								if (i>0) ols.size(osz);
								i = 0;							
							} else if ((start.y<=yy && s.num+op.num<=3) || (prev!=null && prev.type!=Segment.UNKNOWN && prev.opp!=null && prev.start!=null && yy<prev.start.y && prev.num+prev.opp.num<=3)) continue;							
						}
						prev.upper = new Vector2D(pupper);
						s.lower=pupper;
					}
					//					if (i>0 && s.startIndex==prev.endIndex+1 && (osz-=i)>0) System.arraycopy(olsArr, i, olsArr, 0, osz);

				}
				if (next!=null && next.type!=Segment.UNKNOWN &&((next.type==s.type && (next.radius==s.radius && s.type!=0) ) || (isConnected(next,s, tW, nlower)))) {
					if (next.type==s.type && next.radius==s.radius && next.center!=null && s.center!=null && Math.abs(next.center.y-s.center.y)<2){
						if (i>0) {
							osz = i;																	
						}	
						next.startIndex = s.startIndex;
						next.start = new Vector2D(s.start);
						next.num = next.endIndex+1-next.startIndex;
						next.center = circle(next.start, next.end, next.center, next.radius);																		
						break;
					}
					Segment op = (s.opp==null) ? new Segment() : s.opp;
					op.points = (s.opp!=null) ? s.opp.points : opoints;
					s.opp = op;
					reSynchronize(s, op, 0, otherTo, -which, tw);										
					Vector2D pLast = s.end;
					Vector2D start = next.start;
					double yy = nlower.y;
					if ((next.type!=s.type || next.radius!=s.radius)&& (pLast.y>=yy || start.y<=yy || s.endIndex+1<next.startIndex)){
						int nextNum = next.num;
						int sNum = s.num;
						if ((next.center==null || next.center.y!=0) && yy<0 || yy<s.start.y || yy>next.end.y) continue;
						connect(next, s, nlower.y, tW);
						if (nextNum!=next.num || sNum!=s.num) {
							osz = i+1;
							ols.size(osz);
						}
					}
					s.upper=nlower;
					next.lower = new Vector2D(nlower);
					//					if (i>0 && s.endIndex==next.startIndex-1) osz = i;

				}			
			}			
		}
		if (osz!=oldOsz) ols.size(osz);
	}//*/
	
	public static final int getCircleType(Vector2D first,Vector2D mid,Vector2D last){		
		boolean isCircle = Geom.getCircle(first, mid,last, tmp1);	
		if (isCircle){			
			double r = Math.sqrt(tmp1[2]);							
			double cx = tmp1[0];
			double cy = tmp1[1];									
//			if (r==ignoreRad) continue;
			
//			if (inTurn){
//				tp = allTurn[from][i][endIndx];
//			} else 
			if (r<MAX_RADIUS-1){						
				double ax = first.x-cx;
				double ay = first.y-cy;
				double bx = last.x-cx;
				double by = last.y-cy;
				double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);			
				if (angle<-Math.PI) 
					angle += 2*Math.PI;
				else if (angle>Math.PI) 
					angle -= 2*Math.PI;
							
				return (angle<0) ? -1 : 1;
											
			} 
		}
		return 0;
	}

	public static final int bestGuess(final Vector2D[] v,int from,int to,double tW,Segment prev,Segment next,Segment[] oalArr,int indx){
		long ti = System.nanoTime();
		int size = to - from;
						
		int which = (tW<0) ? 1 : -1;
		Storage storage = (which==1) ? CircleDriver2.rMap : CircleDriver2.lMap;
		
		Vector2D[] opoints = (which==1) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;
		int otherTo = (which==1) ? CircleDriver2.edgeDetector.lSize:  CircleDriver2.edgeDetector.rSize;
		double x = Math.abs(2*tW);
		
//		boolean inTurn = CircleDriver2.inTurn;
//		double[][][] allRadius = (inTurn) ? (which==1) ? CircleDriver2.allRadiusRight : CircleDriver2.allRadiusLeft : null;
//		double[][][] allCntrx = (inTurn) ? (which==1) ? CircleDriver2.allCntrxR : CircleDriver2.allCntrxL : null;
//		double[][][] allCntry = (inTurn) ? (which==1) ? CircleDriver2.allCntryR : CircleDriver2.allCntryL : null;
//		int[][][] allTurn = (inTurn) ? (which==1) ? CircleDriver2.allTpR : CircleDriver2.allTpL : null;
		double abstW = (tW<0) ? -tW : tW;
		double tw = abstW*2;
		if (size<2) return indx;
		Vector2D first = v[from];
		Vector2D last = v[to-1];
		double[] rs = new double[4];
		Segment s = oalArr[indx];
		Segment os = s.opp;		
		os.points = opoints;		
		s.points = v;
		s.type = Segment.UNKNOWN;
		s.done = true;
		s.map = null;
		os.map = null;		
		s.unsafe = true;
		os.unsafe = true;
		Segment op = (prev==null) ? null : prev.opp;		
		Segment on = (next==null) ? null : next.opp;
		
		double x0 = first.x;
		if (Math.abs(last.x-x0)<=E && last.y-first.y>1){
			s.type = 0;										
			s.num = to-from;
			s.endIndex = to-1;
			s.startIndex = from;
			if (s.end==null) 
				s.end = new Vector2D(last);
			else s.end.copy(last);
			if (s.start==null) 
				s.start = new Vector2D(first);
			else s.start.copy(first);
			s.end.x = first.x;
			s.radius = 0;			
			reSynchronize(s, os, 0, otherTo, -which, tw);
			return indx+1;
		}
		
		for (int i = 1+from;i<to;++i){
			if (Math.abs(v[i].x-x0)>=0.1*TrackSegment.EPSILON) {
				if (i>from+1 && v[i-1].y-first.y>1){
					s.type = 0;										
					s.num = i-from;
					s.endIndex = i-1;
					s.startIndex = from;
					if (s.end==null) 
						s.end = new Vector2D(v[i-1]);
					else s.end.copy(v[i-1]);
					
					if (s.start==null) 
						s.start = new Vector2D(first);
					else s.start.copy(first);
					s.radius = 0;
					s.end.x = first.x;
					reSynchronize(s, os, 0, otherTo, -which, tw);
					indx++;
					return (to-i)>1 ? bestGuess(v, i, to, tW, s, next,oalArr,indx) : indx;
				}
				break;
			}
		}
		double EE = EPSILON*0.5;
		if (Math.abs(first.x-last.x)<EE){			
			boolean willchanged = false;
			for (int i = to-1;i>from;--i){
				Vector2D p = v[i];
				if (Math.abs(first.x-p.x)<E && p.y-first.y>1){
					s.type = 0;
					s.startIndex = from;
					s.num = i+1-s.startIndex;
					s.endIndex = i;
					s.radius = 0;
					if (s.end==null) 
						s.end = new Vector2D(p);
					else s.end.copy(p);
					if (s.start==null) 
						s.start = new Vector2D(first);
					else s.start.copy(first);
					s.end.x = first.x;
					reSynchronize(s, os, 0, otherTo, -which, tw);
					indx++;
					return (to-i-1>1) ? bestGuess(v, i+1, to, tW, s, next,oalArr,indx) :indx;					
				}  else if ((Math.abs(last.x-p.x)<E && last.y-p.y>1)){
					willchanged = true;											
				}
			}//end of for

			if (willchanged){									
				for (int i = from+1;i<to-1;++i){
					Vector2D p = v[i];
					if (Math.abs(last.x-p.x)<E && last.y-p.y>1){
						s.type = 0;
						s.endIndex = to-1;
						s.startIndex = i;
						s.num = to-i;			
						s.radius = 0;
						if (s.end==null) 
							s.end = new Vector2D(last);
						else s.end.copy(last);
						if (s.start==null) 
							s.start = new Vector2D(p);
						else s.start.copy(p);
						s.end.x = p.x;		
						reSynchronize(s, os, 0, otherTo, -which, tw);						
						return indx+1;
					}
				}//end of for
			}
		}//end of if	

		
		//		if (size>2) segmentize(v, from, to, tW,ols);					

		if (size==2){
			double e = first.x - last.x;
			if (e<0) e=-e;
			if (prev!=null && prev.type==0 && Math.abs(prev.start.x-prev.end.x)<=E) {
				if (Math.abs(prev.start.x-last.x)<=E){
					prev.end.copy(last);
					prev.end.x = prev.start.x;
					prev.endIndex = to-1;
					prev.num = to - prev.startIndex;
					reSynchronize(prev, op, 0, otherTo, -which, tw);
					return indx;
				} else if (Math.abs(prev.start.x-first.x)<=E){
					prev.end.copy(first);
					prev.end.x = prev.start.x;
					prev.endIndex = from;
					prev.num = from+1- prev.startIndex;
					reSynchronize(prev, op, 0, otherTo, -which, tw);
					return indx;
				}				
			}
			
			if (last.y-first.y<3 && prev!=null && !CircleDriver2.isFirstSeg(prev)) return indx;
			
			if (prev!=null && e>E){				
				radiusFrom2Points(prev, first, last, tW, s);		
//				if (CircleDriver2.time>=CircleDriver2.BREAK_TIME && prev.num>0){
//					double[] r = new double[6];
//					Geom.getCircle(first, last, prev.points[prev.endIndex], r);
//					r[2] = Math.sqrt(r[2]);
//					System.out.println();
//				}
				if (prev.type==0 && s.type==0){										
					return indx;
				}
				if (last.y-first.y<2 && s.type!=Segment.UNKNOWN && s.type!=0 && s.radius-s.type*tW<=15)
					return indx;
			}
			
			if (s.type==Segment.UNKNOWN || (s.type!=0 && (s.end.y-s.start.y>=s.radius+1 || s.radius<=REJECT_VALUE || s.radius>=MAX_RADIUS-1 || s.radius-s.type*tW>=MAX_RADIUS-1))) {									
				return indx;
			}
			if (s.type!=Segment.UNKNOWN){
				s.startIndex = from;
				s.endIndex = to-1;
				s.num = 2;
				s.unsafe = false;
				reSynchronize(s, os, 0, otherTo, -which, tw);				
				if ((os.type!=0 && ((os.num>=2 && check(opoints, os.startIndex, os.endIndex+1, os.center, os.radius)<0))) || (op!=null && op.type!=Segment.UNKNOWN && (os.startIndex<=op.endIndex || os.start.y<=op.end.y)) || (on!=null && on.type!=Segment.UNKNOWN && (os.endIndex>=on.startIndex ||  os.end.y>=on.start.y))) {								
					return indx;
				}
				if (s.type==0){ 
					if (s.end.y-s.start.y<3 || os.end.y-os.start.y<3) return indx;
					double total = 0;
					for (int kk = os.startIndex;kk<=os.endIndex;++kk){
						Vector2D vv = opoints[kk];
						total += Math.sqrt(Geom.ptLineDistSq(os.start.x, os.start.y, os.end.x, os.end.y, vv.x, vv.y, null));							
					}
					total /= (double)os.num;
					if (total>=0.1) return indx;
				}
				s.done = true;
				os.unsafe = false;
				return 1+indx;
			} 
			return indx;
		}
		int numb = 0;		
		int index = 0;

		if (prev!=null || next!=null){			
			Segment tmpSeg = new Segment();	
			int n = 0;
			Segment s1 = oalArr[indx+1];
			s1.points = s.points;
			s1.unsafe = true;
			Vector2D pt = new Vector2D();
			final double EPS = EPSILON*3.5;
			int m = 0;			
			int SIZE_N = storage.SIZE_N;
			int[] totalRad_N = storage.totalRad_N;
			int[] mapIndx = storage.mapIndx;
			int[] appearRad_N = storage.appearRad_N;
			int[] maxRad = storage.maxRad;
			int[][] allMaps = Storage.allMaps;
			int[][] allTypes = Storage.allTypes;
			int[][] allRadius = Storage.allRadius;
			int[] aMap = tmpMp;
			int[] aRadius = tmpApp;
			int[] aFstIndx = tmpStartIdx;
			int[] aLstIndx = tmpEndIdx;
			int aNum = 0;
			int maxAppear = 0;
			int maxR = 0;
			int maxFstIndx = 0;
			int maxLstIndx = 0;
			for (index=from;index<=to-3;++index){
				Vector2D fst = v[index];
				double startX = fst.x;
				double startY = fst.y;	
				int r_index = index<<SIZE_N;
				if (r_index>=totalRad_N.length) continue;
				for (int k=to-1;k>index+1;--k){
					int m_indx = r_index + k;					
					int total = totalRad_N[m_indx];
					boolean donePrev = total>=k-index-1;
					boolean isFound = false;
					int map_indx = mapIndx[m_indx];
					int[] aTypes = (map_indx>=0) ? allTypes[map_indx] : null;
					int[] aRads = (map_indx>=0) ? allRadius[map_indx] : null;
					int[] map = (map_indx>=0) ? allMaps[map_indx] : null;
					if (map_indx>=0 && appearRad_N[m_indx]>0){
						int mr = maxRad[m_indx];
						
						if (map!=null && (map[mr]>2 || map[mr]>1 && k-index<3)) isFound = true;
						if (!isFound){							
							int tp = aTypes[0];
							if (tp>Segment.UNKNOWN){
								int rad = aRads[0];
								if (map[rad]>1) isFound = true;
							}
						}
					}
										
					boolean ok = false;
					boolean isGPN = false;
					if (isFound){
						storage.toSegment(v, index, k, s);
						isGPN = true;
						ok = true;						
					}
					Vector2D lst = v[k];
					double endX = lst.x;
					double endY = lst.y;
					if (!ok){
						n++;						
						int num = k+1-index;					
						s.type = Segment.UNKNOWN;
						s.startIndex = index;
						s.endIndex = k;
						s.num = num;						
						s1.type = Segment.UNKNOWN;
						s1.startIndex = index;
						s1.endIndex = k;
						s1.num = num;					
								
																										
						if (prev!=null && prev.type!=Segment.UNKNOWN){
							s.type = Segment.UNKNOWN;
//							if (prev.updated || aTypes==null || aTypes[0]<Segment.UNKNOWN){ 
//								radiusFrom2Points(prev, fst, lst, tW, s);					
//								storage.store(index, index, k, s.type, s.radius);
//							} else {
//								storage.toSegment(v, index, index, k, s);
//								radiusFrom2Points(prev, fst, lst, tW, s1);
//								if (s.type!=s1.type || s.type!=0 && s.type!=Segment.UNKNOWN && s.radius!=s1.radius)
//									System.out.println();
//								s1.type = Segment.UNKNOWN;
//							}
							radiusFrom2Points(prev, fst, lst, tW, s);							
							if (s.type!=Segment.UNKNOWN ){
								if (donePrev && map!=null){
									int er = (s.type==0) ? 0 : (int)Math.round(s.radius+which*s.type*abstW);
									if (er>=Segment.MAX_RADIUS-1) {
										er = 0;
										s.type = 0;
									}
									if (s.type==1) er+=Segment.MAX_RADIUS;
									if (er>=0 && map[er]>0) {
										ok = true;
										isGPN = true;
									}
								} else {
									boolean good = true;									
									if (s.type!=0){
										if (isSameSegment(prev, s) || next!=null && isSameSegment(s, next)){
											ok = true;											
											isGPN = true;
										} else {
											double cnx = s.center.x;
											double cny = s.center.y;	
//											double maxE = 0;
											for (int ii = index;ii<=k;++ii){								
												Vector2D vv = v[ii];
												m++;
												double dx = vv.x-cnx;
												double dy = vv.y-cny;			
												double e = Math.sqrt(dx*dx+dy*dy)-s.radius;
												if (e<0) e = -e;
//												if (maxE<e) maxE = e;
												if (e>EPS) {
													good = false;
													break;
												}
											}
//											if (!good && maxE<1 && num>3) storage.store(index, index, k, s.type, s.radius);
										}
									} else {																		
										double tot = 0;
										for (int ii = index;ii<=k;++ii){								
											Vector2D vv = v[ii];
											m++;
											double e = Math.sqrt(Geom.ptLineDistSq(startX, startY, endX, endY, vv.x, vv.y, null));
											if (e<0) e = -e;
											tot+=e;
											if (e>EPS) {
												good = false;
												break;
											}
											
										}
		
										tot/=num;
										if (tot>E) good = false;
									}
									if (!good){
										s.type = Segment.UNKNOWN;									
									}
								}
							}
						}//end of if prev
	
						if (!ok && next!=null && next.type!=Segment.UNKNOWN){
							s1.type = Segment.UNKNOWN;						 
							radiusFrom2Points(next, fst, lst, tW, s1);						
							if (s1.type!=Segment.UNKNOWN ){
								if (donePrev && map!=null){
									int er = (s1.type==0) ? 0 : (int)Math.round(s1.radius+which*s1.type*abstW);
									if (er>=Segment.MAX_RADIUS-1) {
										er = 0;
										s1.type = 0;
									}
									if (s1.type==1) er+=Segment.MAX_RADIUS;
									if (er>=0 && map[er]>0){ 
										ok = true;
										s.copy(s1);
										isGPN = true;
									}
								} else {
									boolean good = true;
									if (s1.type!=0){
										if (prev!=null && isSameSegment(prev, s1) || isSameSegment(s1, next)){
											ok = true;
											isGPN = true;
											s.copy(s1);
										} else {
											double cnx = s1.center.x;
											double cny = s1.center.y;		
//											double maxE = 0;
											for (int ii = index;ii<=k;++ii){
												m++;
												Vector2D vv = v[ii];
												double dx = vv.x-cnx;
												double dy = vv.y-cny;			
												double e = Math.sqrt(dx*dx+dy*dy)-s1.radius;												
												if (e<0) e = -e;
//												if (maxE<e) maxE = e;
												if (e>EPS) {
													good = false;
													break;
												}
											}
											
//											if (!good && maxE<1 && num>3) storage.store(index, index, k, s1.type, s1.radius);
										}
									} else {																		
										double tot = 0;
										for (int ii = index;ii<=k;++ii){								
											Vector2D vv = v[ii];
											m++;
											double e = Math.sqrt(Geom.ptLineDistSq(startX, startY, endX, endY, vv.x, vv.y, null));
											if (e<0) e = -e;
											tot+=e;
											if (e>EPS) {
												good = false;
												break;
											}
										}
		
										tot/=num;
										if (tot>E) good = false;
									}
									if (!good){
										s1.type = Segment.UNKNOWN;									
									}
								}
							}
						}//end of if next						
						if (!ok && prev!=null && next!=null && s.type==s1.type && s.type!=Segment.UNKNOWN && (s.type==0 || s.radius==s1.radius)){												
							ok = true;
							isGPN = true;
						}
	
	
						if (!ok && !donePrev){						
							tmpSeg.type = Segment.UNKNOWN;
							ok = (isBelongToEither(s,s1,v, index, k+1, tmpSeg, which, abstW) || tmpSeg.type!=Segment.UNKNOWN);							
						}//end of if normal
						
						if (!ok){
							int appear_n = appearRad_N[m_indx];
							map_indx = mapIndx[m_indx];
							map = (map_indx>=0) ? allMaps[map_indx] : null;
							int[] appearRad = (map_indx>=0) ? Storage.allAppear[map_indx] : null;
							if (appearRad!=null){
								for (int i = appear_n-1;i>=0;--i){
									int r = appearRad[i];
									if (aMap[r]==0){
										aRadius[aNum++] = r;
										aFstIndx[r] = index;
										aLstIndx[r] = k;
									}
									aMap[r]+=map[r];
									if (aMap[r]>maxAppear){
										maxAppear = aMap[r];
										if (r!=maxR){
											maxR = r;
											maxFstIndx = aFstIndx[r];
											maxLstIndx = aLstIndx[r];
										}
									}
								}
								
							}
						}
					}
					
					if (ok){
						boolean isPossiblePrevConnected = (s.type!=UNKNOWN && s.type==tmpSeg.type && (s.type==0 || s.radius==tmpSeg.radius));
						boolean isPossibleNextConnected = (s1.type!=UNKNOWN && s1.type==tmpSeg.type && (s1.type==0 || s1.radius==tmpSeg.radius));
						if (!isGPN) s.copy(tmpSeg);			
						storage.store(index, index, k, s.type, s.radius);
						if (s.radius>=MAX_RADIUS-1 || os.radius>=MAX_RADIUS-1){
							s.type = 0;
							s.radius = 0;
//							s.center = null;							
						}
//						if (s.type!=0 && (s.end.y-s.start.y>=x+s.radius || s.radius<=REJECT_VALUE || s.radius>=MAX_RADIUS-1 || s.radius-s.type*tW<=REJECT_VALUE || s.radius-s.type*tW>=MAX_RADIUS-1)) continue;
						if (isReject(prev, s, next, tW)) continue;
						reSynchronize(s,os,0,otherTo,-which,tw);													
						if (prev!=null && isPossiblePrevConnected && isConnected(prev, s, tW, pt) && pt.y>prev.start.y && pt.y<s.end.y) {																														
							if ((prev.type!=s.type || prev.radius!=s.radius))				
								connect(prev, s, pt, tW);
						}

						if (next!=null && isPossibleNextConnected && s.type!=Segment.UNKNOWN && next.type!=Segment.UNKNOWN && isConnected(s, next, tW, pt) && pt.y>s.start.y && pt.y<next.end.y) {																														
							if ((next.type!=s.type || next.radius!=s.radius))				
								connect(s, next, pt, tW);
						}						
					}

					if (ok  || size==3){
						for (int i = aNum-1;i>=0;--i){
							int r = aRadius[i];
							aMap[r] = 0;
							aFstIndx[r] = 0;
							aLstIndx[r] = 0;
							aRadius[i] = 0;
						}
						aNum = 0;
						
						if (!ok){
							Vector2D mid = v[from+1];					
							boolean isCircle = Geom.getCircle(first, mid,last, tmp1);	
							if (isCircle){
								if (s.start==null) 
									s.start = new Vector2D(first);
								else s.start.copy(first);
								if (s.end==null) 
									s.end = new Vector2D(last);
								else s.end.copy(last);
								double r = Math.sqrt(tmp1[2]);							
								double cx = tmp1[0];
								double cy = tmp1[1];						
								r = Math.round(r-tW)+tW;
//								if (r==ignoreRad) continue;
								
//								if (inTurn){
//									tp = allTurn[from][i][endIndx];
//								} else 
								if (r<MAX_RADIUS-1){						
									double ax = first.x-cx;
									double ay = first.y-cy;
									double bx = last.x-cx;
									double by = last.y-cy;
									double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);			
									if (angle<-Math.PI) 
										angle += 2*Math.PI;
									else if (angle>Math.PI) 
										angle -= 2*Math.PI;
												
									s.type = (angle<0) ? -1 : 1;
									s.radius = r; 
									if (s.center==null) s.center = new Vector2D();
									circle(first, last, cx,cy, r,s.center);
									if (isReject(prev, s, next, tW)){
										s.type = Segment.UNKNOWN;
										os.type = Segment.UNKNOWN;
									}									
								} else {
									s.type = 0;
									s.radius = 0;
								}								
								s.num = 3;
							}
						}
						if (s.type!=Segment.UNKNOWN && s.type!=0){		
							fst = v[s.startIndex];
							lst = v[s.endIndex];
							Vector2D py = (prev!=null && prev.type==0 && Math.abs(prev.start.x-prev.end.x)<E) ? prev.end : null;							
							if (Math.abs(fst.x-lst.x)<EE || (py!=null && (Math.abs(fst.x-py.x)<EE || Math.abs(py.x-lst.x)<EE))){
								boolean changed = false;
								boolean willchanged = false;
								for (int i = s.endIndex;i>s.startIndex;--i){
									Vector2D p = v[i];
									m++;
									if (py!=null && Math.abs(py.x-p.x)<E && p.y-py.y>1){
										s.type = UNKNOWN;										
										prev.endIndex = i;
										prev.num = i+1-prev.startIndex;
										prev.end.copy(p);										
										prev.end.x = py.x;
										reSynchronize(prev,op,0,otherTo,-which,tw);
										changed = true;
										break;
									} else if (Math.abs(fst.x-p.x)<E && p.y-fst.y>1){
										s.type = 0;										
										s.num = i+1-s.startIndex;
										s.endIndex = i;
										s.end.copy(p);
										s.radius = 0;
										s.end.x = fst.x;
										changed = true;
										break;
									} else if ((Math.abs(last.x-p.x)<E && last.y-p.y>1) || (lst!=last && Math.abs(lst.x-p.x)<E && lst.y-p.y>1)){
										willchanged = true;											
									}
								}
								
								if (!changed && willchanged){									
									for (int i = s.startIndex+1;i<s.endIndex;++i){
										Vector2D p = v[i];
										m++;
										if (Math.abs(lst.x-p.x)<E && lst.y-p.y>1) {
											s.type = 0;																					
											s.startIndex = i;
											s.num = s.endIndex+1-i;
											s.start.copy(p);
											s.radius = 0;
											s.end.copy(p.x,lst.y);
											changed = true;
											break;
										}								
									}
								}
							}
						}

						if (s.type!=Segment.UNKNOWN) {
							if (s.type==0 || !ok) reSynchronize(s,os,0,otherTo,-which,tw);
							if (s!=null && op!=null && s.type!=Segment.UNKNOWN && os.num>0 && (op.endIndex>=os.startIndex || op.end.y>os.start.y-SMALL_MARGIN)){
								s.type = Segment.UNKNOWN;
								os.type = Segment.UNKNOWN;
							} else if (s.type!=Segment.UNKNOWN && s.num>1 && os.num>1){
								if ((s.points[s.startIndex].x-s.points[s.endIndex].x)*(os.points[os.startIndex].x-os.points[os.endIndex].x)<0){
									s.type = Segment.UNKNOWN;
									os.type = Segment.UNKNOWN;
								} else if ((s.points[s.startIndex].x-s.points[s.endIndex].x)*s.type>0 && os.num>2 && s.type*getCircleType(os.points[os.startIndex], os.points[(os.startIndex+os.endIndex)/2], os.points[os.endIndex])<0){
									s.type = Segment.UNKNOWN;
									os.type = Segment.UNKNOWN;
								}
							}
							if (s.type!=Segment.UNKNOWN){
								if (prev!=null && prev.type!=Segment.UNKNOWN && (prev.endIndex>s.startIndex || prev.end.y>=s.start.y-SMALL_MARGIN)){																
									reCalibrate(prev, s, which, tw);							
								}
								if (op!=null && op.type!=Segment.UNKNOWN && (op.endIndex>os.startIndex || op.end.y>=os.start.y-SMALL_MARGIN)){								
									reCalibrate(prev, s, which, tw);							
								}
								
								if (next!=null && next.type!=Segment.UNKNOWN && (s.endIndex>next.startIndex || s.end.y>=next.start.y-SMALL_MARGIN)){
									reCalibrate(s, next, which, tw);
								} 
								if (on!=null && on.type!=Segment.UNKNOWN && (os.endIndex>on.startIndex || os.end.y>=on.start.y-SMALL_MARGIN)){								
									reCalibrate(s, next, which, tw);							
								}
							}
						}
						
						for (int i = aNum-1;i>=0;--i){
							int r = aRadius[i];
							aMap[r] = 0;
							aFstIndx[r] = 0;
							aLstIndx[r] = 0;
							aRadius[i] = 0;
						}
						aNum = 0;
						
						if (s!=null && s.type!=Segment.UNKNOWN && s.type==0 && s.end.y-s.start.y<2) return indx;																						
						if (s.type!=Segment.UNKNOWN)
							indx++;								

						if (s.endIndex<to-1  && s.type!=Segment.UNKNOWN){																														
//							if (s.type!=0) forwardOnly(v, prev, s, to, tW);
							if (to-s.endIndex>2 && s.num>0)																																	
								indx = bestGuess(v, s.endIndex+1, to, tW, s, next,oalArr,indx);									

						} else if (s.type==Segment.UNKNOWN){
							os.type = Segment.UNKNOWN;
							if (prev!=null && prev.endIndex+1>from && to-prev.endIndex-1>2 && (next==null || to<=next.startIndex))																								
								indx = bestGuess(v, s.endIndex+1, to, tW, s, next,oalArr,indx);								
						}

						long endTime = (System.nanoTime()-ti)/1000000;
						if (CircleDriver2.debug || endTime>=1) System.out.println("End bestGuess : "+endTime+"   at "+CircleDriver2.time+" s.    "+n+"   "+m);
						s.done = true;
						if (os.num==0) os.done =  true;
						return indx;														
					}//end of if ok				
				}//end of for				
			}//end of for

			//not found using radiusFrom2Points
			
			if (prev!=null && prev.type==0 && Math.abs(prev.start.x-prev.end.x)<TrackSegment.EPSILON*0.5){
				Vector2D start = v[from];
				for (int i = size-1;i>=1;--i){				
					Vector2D end = v[i+from];
					x0 = prev.start.x;
					Geom.getCircle2(start.x,start.y, end.x,end.y, x0,0, x0,1, tmp1);					
					double r = Math.sqrt(tmp1[2]);								 				
					double cx = tmp1[0];
					double cy = tmp1[1];
					double ax = start.x-cx;
					double ay = start.y-cy;
					double bx = end.x-cx;
					double by = end.y-cy;
					double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);			
					if (angle<-Math.PI) 
						angle += 2*Math.PI;
					else if (angle>Math.PI) 
						angle -= 2*Math.PI;

					int tp = (angle<0) ? -1 : 1;	
					r = Math.round(r-tp*tW)+tp*tW;
					if (r<=REJECT_VALUE || r>=MAX_RADIUS-1) continue;				
					Vector2D center = (s.center==null) ? circle(start, end, cx,cy, r) : s.center;
					if (s.center!=null){
						circle(start, end, cx,cy, r,center);
					} else s.center = center;
									
					if (check(v, from, from+i+1, center, r)>=0 && s!=null) {																						
						if (s.start!=null) 
							s.start.copy(start);
						else s.start = new Vector2D(start);
						if (s.end!=null) 
							s.end.copy(end);
						else s.end = new Vector2D(end);
						s.type = tp;
						s.radius = r;
						s.startIndex = from;
						s.endIndex = from+i;
						s.num = i+1;
						
						if (isConnected(prev, s, tW, pt) && pt.y>prev.end.y && pt.y<s.start.y){
							s.lower = pt;	
							reSynchronize(s, os, 0, otherTo, -which, tw);																										
							if (next!=null && size-i-1>=2){
								start = v[from+i+1];
								end = v[from+size-1];
								if (guessPN(s, next, tW, start, end, rs)){
									Segment ns = oalArr[++indx];
									if (ns.start==null)
										ns.start = new Vector2D(start);
									else ns.start.copy(start);
									
									if (ns.end==null)
										ns.end = new Vector2D(end);
									else ns.end.copy(end);	
									if (ns.center==null) 
										ns.center = new Vector2D(rs[0],rs[1]);
									else {
										ns.center.x = rs[0];
										ns.center.y = rs[1];
									}
									ns.num = size-i-1;
									ns.points = v;								
									ns.type = (int)(rs[3]);															
									ns.radius = rs[2];
									ns.done = true;																								
									ns.startIndex = from+i+1;					
									ns.endIndex = from+size-1;
									Segment other = ns.opp;
									other.points = opoints;								
									reSynchronize(ns, other, 0, otherTo, -which, tw);
								}																										
							}
							for (i = aNum-1;i>=0;--i){
								int rr = aRadius[i];
								aMap[rr] = 0;
								aFstIndx[rr] = 0;
								aLstIndx[rr] = 0;
								aRadius[i] = 0;
							}
							return indx+1;
						}					
					}
				}
			}

			if (size==4 && prev!=null && prev.type!=UNKNOWN && prev.type!=0 && prev.center!=null && (next==null || (next.type!=UNKNOWN && next.type!=0  && next.center!=null))){
				Vector2D start = first;
				Vector2D end = v[1+from];
				if (end.y-start.y>1){
					for (int tp =-1;tp<2;tp+=2){
						numb = 0;
						if (prev!=null && prev.type==tp) 
							numb = Geom.getCircle4(start, end, prev.center, prev.radius, tmp1);					
						else numb = Geom.getCircle5(start, end, prev.center, prev.radius, tmp1);
						for (int kk =0;kk<numb;++kk){
							double rr = Math.round(tmp1[2+3*kk]-tp*tW)+tp*tW;						
							if (tp!=0 && (rr<=REJECT_VALUE || rr-tp*tW<=REJECT_VALUE )) continue;						
							if (rr<MAX_RADIUS-1){							
								double cx = tmp1[3*kk];
								double cy = tmp1[1+3*kk];
								Vector2D center = (s.center==null) ? circle(start, end, cx,cy, rr) : s.center;
								if (s.center!=null){
									circle(start, end, cx,cy, rr,center);
								} else s.center = center;												
								if (s.start==null){
									s.start = new Vector2D(start);
								} else s.start.copy(start);											
								if (s.end==null){
									s.end = new Vector2D(end);
								} else s.end.copy(end);													
								s.type = tp;
								s.radius = rr;
								s.startIndex = from;
								s.endIndex = from+1;					
//								if (end.y-start.y>=x+rr || end.y-start.y>=rr || rr<=REJECT_VALUE || rr-tW*2*tp<=REJECT_VALUE || center.y>start.y || rr>=MAX_RADIUS-1) continue;
								if (isReject(prev, s, next, tW)) continue;
							} else if (prev.type*(end.x-start.x)>0){																		
								if (s.start==null){
									s.start = new Vector2D(start);
								} else s.start.copy(start);											
								if (s.end==null){
									s.end = new Vector2D(end);
								} else s.end.copy(end);													
								s.type = 0;
								s.radius = 0;
								s.startIndex = from;
								s.endIndex = from+1;						
							} else continue;					
							s.num =2;
							s.unsafe = false;
							reSynchronize(s, os, 0, otherTo, -which, tw);				
							if (isReject(op, os, on, tW)) continue;
//							if ((os.type!=0 && (os.end.y-os.start.y>=os.radius ||os.radius<=REJECT_VALUE || (os.num>=2 && check(opoints, os.startIndex, os.endIndex+1, os.center, os.radius)<0))) || (op!=null && op.type!=Segment.UNKNOWN && (os.startIndex<=op.endIndex || os.start.y<=op.end.y))) {								
//								continue;
//							}
							if (s.type==0){ 
								if (s.end.y-s.start.y<3 || os.end.y-os.start.y<3) continue;
								double total = 0;
								for (int k = os.startIndex;k<=os.endIndex;++k){
									Vector2D vv = opoints[k];
									total += Math.sqrt(Geom.ptLineDistSq(os.start.x, os.start.y, os.end.x, os.end.y, vv.x, vv.y, null));							
								}
								total /= (double)os.num;
								if (total>=0.1) continue;
							}						
							
							if ((next==null || guessPN(s, next, tW, v[2+from], v[to-1], rs))){
								Vector2D fst = v[2+from];
								Vector2D lst = v[to-1];
								if (lst.y-fst.y<=1) continue;
								
								Segment s2 = oalArr[indx+1];
								if (next!=null) {
									int tmptp = (int)rs[3];
									double tmpr = rs[2];																
									if (tmptp!=0 && (lst.y-Math.max(0,fst.y)>=tmpr+1.5+tW*which*tmptp || lst.y-fst.y<=1 || tmpr<=REJECT_VALUE || tmpr>=MAX_RADIUS-1 || tmpr-tmptp*tW<=REJECT_VALUE || tmpr-tmptp*tW>=MAX_RADIUS-1)) continue;
									
									if (s2.start==null){
										s2.start = new Vector2D(fst);
									} else s2.start.copy(fst);											
									if (s2.end==null){
										s2.end = new Vector2D(lst);
									} else s2.end.copy(lst);															
									if (s2.center==null)
										s2.center = new Vector2D(rs[0],rs[1]);
									else {
										s2.center.x = rs[0];
										s2.center.y = rs[1];
									}
									s2.type = tmptp;
									s2.radius = tmpr;
									s2.startIndex = from+2;
									s2.endIndex = to-1;
									s2.num = 2;
									s2.done = true;
									s2.points = v;
								} else {							
									s2.type = Segment.UNKNOWN;
									s2.startIndex = from + 2;
									s2.endIndex = to-1;
									s2.num = 2;
									s2.done = true;
									s2.points = v;
									radiusFrom2Points(s, fst, lst, tW, s2);
//									if (s2.type==Segment.UNKNOWN || (s2.type!=0 && lst.y-fst.y>=x+s2.radius) ||(s2.type!=0 && (s2.radius<=REJECT_VALUE || s2.radius-tW*2*s2.type<=REJECT_VALUE || s2.end.y-s2.start.y>=s2.radius))) continue;
									if (isReject(s, s2, next, tW)) continue;
									double d = 0;
									if (Math.abs(s.radius-s2.radius)>3 && ((d = radiusFrom2Points(s2, start, end, tW, null))<0 || Math.abs(s.radius-d) > 3)) continue;
								}
								if (s2!=null && s2.type!=Segment.UNKNOWN){																										
//									reSynchronize(s, os, 0, otherTo, -which, tw);	
									s2.unsafe = false;
									Segment other = s2.opp;									
									other.points = opoints;							
									reSynchronize(s2, other, 0, otherTo, -which, tw);
//									if (os.type!=0 && os.type!=Segment.UNKNOWN && os.radius<=REJECT_VALUE || (os.center==null || (op!=null && op.type!=Segment.UNKNOWN && (os.startIndex<=op.endIndex || os.start.y<=op.end.y)) || (other!=null && (os.endIndex>=other.startIndex ||  os.end.y>=other.start.y)))) {
//										continue;
//									}
									
									
//									if (other.type!=0 && other.type!=Segment.UNKNOWN && (other.end.y-other.start.y>=other.radius || other.radius<=REJECT_VALUE || (other.center==null || other.startIndex<=os.endIndex || other.start.y<=os.end.y || (on!=null && on.type!=Segment.UNKNOWN && (other.endIndex>=on.startIndex ||  other.end.y>=on.start.y))))) {
//										continue;
//									}
									
									if (isReject(os, other, next, tW)) continue;
									
									if (s2.type==0){ 
										if (s2.end.y-s2.start.y<3 || other.end.y-other.start.y<3 || s2.end.y-s2.start.y>=s2.radius+1.5) continue;
										double total = 0;
										for (int k = other.startIndex;k<=other.endIndex;++k){
											Vector2D vv = opoints[k];
											total += Math.sqrt(Geom.ptLineDistSq(other.start.x, other.start.y, other.end.x, other.end.y, vv.x, vv.y, null));							
										}
										total /= (double)os.num;
										if (total>=0.1) continue;
									}						
									
									for (int i = aNum-1;i>=0;--i){
										int r = aRadius[i];
										aMap[r] = 0;
										aFstIndx[r] = 0;
										aLstIndx[r] = 0;
										aRadius[i] = 0;
									}
									other.unsafe = false;
									os.unsafe = false;
									return indx+2;
								}
							}
						}				
					}
				}//end of if
			}

			
			if (maxAppear>0){						
				double avg = 0;
				int num = 0;
				boolean noStraight = true;
				int[] marks = new int[aNum];
				int idx = -1;
				boolean found = false;
				for (int i = 0;i<aNum;++i){
					int er = aRadius[i];
					if (aMap[er]==maxAppear){
						found = true;
						int tp = er==0 ? 0: er<Segment.MAX_RADIUS ? -1: 1;
						maxFstIndx = aFstIndx[er];
						maxLstIndx = aLstIndx[er];
						if (tp==1) er-=Segment.MAX_RADIUS;
						Vector2D fst = v[maxFstIndx];
						Vector2D lst = v[maxLstIndx];
						if (!CircleDriver2.inTurn && tp*(fst.x-lst.x)>=0 || tp!=0 && lst.y-Math.max(0,fst.y)>=er+1.5+tp*which*tW || er<=REJECT_VALUE || er-tW<=REJECT_VALUE){
							continue;														
						}
						marks[i] = 1;
						if (noStraight && tp!=0){
							avg+=er*tp;
							num++;
							if (maxAppear==1) break;
						} else if (tp==0) noStraight = false;
					}
				}
				
				if (noStraight && !found && aNum>3){
					System.out.println();
				}
				
				if (noStraight && num>0) {
					avg/=num;
					double minDist = 100000;
					for (int i = 0;i<aNum;++i){
						if (marks[i]==0) continue;
						int er = aRadius[i];													
						int tp = er==0 ? 0: er<Segment.MAX_RADIUS ? -1: 1;								
						if (tp==1) er-=Segment.MAX_RADIUS;																
						if (minDist>Math.abs(tp*er-avg)){
							minDist = Math.abs(tp*er-avg);
							idx = i;
							if (num<3 || maxAppear==1) break;
						}							
					}

				}
			
				for (int i = 0;i<aNum;++i){
					if (marks[i]==0) continue;
					if (noStraight && num>0 && idx!=-1 && i!=idx) continue;
					int er = aRadius[i];
					if (aMap[er]==maxAppear){						
						int tp = er==0 ? 0: er<Segment.MAX_RADIUS ? -1: 1;
						maxFstIndx = aFstIndx[er];
						maxLstIndx = aLstIndx[er];
						if (tp==1) er-=Segment.MAX_RADIUS;
						Vector2D fst = v[maxFstIndx];
						Vector2D lst = v[maxLstIndx];
//						if (!CircleDriver2.inTurn && tp*(fst.x-lst.x)>=0 || tp!=0 && lst.y-fst.y>=x+er || tp!=0 && lst.y-Math.max(0,fst.y)>=er+tW || er<=REJECT_VALUE || er-tW<=REJECT_VALUE){
//							continue;														
//						}
						
						tmpSeg.type = tp;
						tmpSeg.startIndex = maxFstIndx;
						tmpSeg.endIndex = maxLstIndx;
						tmpSeg.num = maxLstIndx-maxFstIndx+1;
						if (tmpSeg.start==null) 
							tmpSeg.start = new Vector2D(fst);
						else tmpSeg.start.copy(fst);
						if (tmpSeg.end==null) 
							tmpSeg.end = new Vector2D(lst);
						else tmpSeg.end.copy(lst);
						
						tmpSeg.radius = er + tW*tp;				
						if (tmpSeg.type!=0){
							if (tmpSeg.center==null) tmpSeg.center = new Vector2D();
							Segment.circle(tmpSeg.start, tmpSeg.end, tp, tmpSeg.radius, tmpSeg.center);
						}
										
						Segment tmpSegOp = tmpSeg.opp;
						if (tmpSegOp==null) {
							tmpSegOp = new Segment();
							tmpSeg.opp = tmpSegOp;
						}
						tmpSegOp.points = opoints;
						tmpSeg.points = v;
						reSynchronize(tmpSeg,tmpSegOp,0,otherTo,-which,tw);								
						if (prev!=null && prev.type!=Segment.UNKNOWN && (prev.endIndex>tmpSeg.startIndex || prev.end.y>=tmpSeg.start.y-SMALL_MARGIN)){																
							reCalibrate(prev, tmpSeg, which, tw);							
						}
						if (tmpSeg.type!=Segment.UNKNOWN && op!=null && op.type!=Segment.UNKNOWN && (op.endIndex>tmpSegOp.startIndex || op.end.y>=tmpSegOp.start.y-SMALL_MARGIN)){								
							reCalibrate(prev, tmpSeg, which, tw);							
						}
						
						if (tmpSeg.type!=Segment.UNKNOWN && next!=null && next.type!=Segment.UNKNOWN && (tmpSeg.endIndex>next.startIndex || tmpSeg.end.y>=next.start.y-SMALL_MARGIN)){
							reCalibrate(tmpSeg, next, which, tw);
						} 
						if (tmpSeg.type!=Segment.UNKNOWN && on!=null && on.type!=Segment.UNKNOWN && (tmpSegOp.endIndex>on.startIndex || tmpSegOp.end.y>=on.start.y-SMALL_MARGIN)){								
							reCalibrate(tmpSeg, next, which, tw);							
						}
										
						if (tmpSeg!=null && tmpSeg.type!=Segment.UNKNOWN && tmpSeg.type==0 && tmpSeg.end.y-tmpSeg.start.y<2) return indx;
														
					
						if (tmpSeg.type!=Segment.UNKNOWN){
							for (int ii = aNum-1;ii>=0;--ii){
								int r = aRadius[ii];
								aMap[r] = 0;
								aFstIndx[r] = 0;
								aLstIndx[r] = 0;
								aRadius[ii] = 0;
							}
							aNum = 0;
							if (maxFstIndx-from>=2){
								indx = bestGuess(v, from, maxFstIndx, tW, prev, tmpSeg, oalArr, indx);
							}
							s = oalArr[indx];
							os = s.opp;
							s.copy(tmpSeg);
							os.copy(tmpSegOp);
							s.done = true;
							if (os.num==0) 
								os.done = true;
							else os.done = false;
							++indx;
							
							if (to-maxLstIndx-1>=2){
								return bestGuess(v, maxLstIndx+1, to, tW, s, next, oalArr, indx);
							}
							return indx;
						} else {
							idx = -1;
						}
					}
				}
			}				
			
			for (int i = aNum-1;i>=0;--i){
				int r = aRadius[i];
				aMap[r] = 0;
				aFstIndx[r] = 0;
				aLstIndx[r] = 0;
				aRadius[i] = 0;
			}
			aNum = 0;
		} else {//end of if			
			Segment[] tmpStore = new Segment[30];
			ObjectArrayList<Segment> ols = ObjectArrayList.wrap(tmpStore,0);
			segmentize(v, from, to, tW,ols);
			int osz = ols.size();
			
			for (int i = 0;i<osz;++i){
				Segment ss = tmpStore[i];				
				if (ss==null || ss.type==Segment.UNKNOWN || ss.end.y<0) continue;												
				Segment t = oalArr[indx++];
				t.copy(ss);
				t.done = true;
				t.opp.points = opoints;
				t.opp.done = true;
				t.points = v;
				reSynchronize(t, t.opp, 0, otherTo, -which, tw);
				
			}
			return indx;
		}

		
		if (size<=4 && size>2 && prev!=null && prev.type!=Segment.UNKNOWN && prev.type!=0){
			Vector2D start = v[1+from];
			Vector2D end = v[2+from];
			for (int tp =-1;tp<2;tp+=2){
				numb = 0;
				if (prev!=null && prev.type==tp) 
					numb = Geom.getCircle4(start, end, prev.center, prev.radius, tmp1);					
				else numb = Geom.getCircle5(start, end, prev.center, prev.radius, tmp1);
				for (int kk =0;kk<numb;++kk){
					double rr = Math.round(tmp1[2+3*kk]-tp*tW)+tp*tW;
					if (rr<=REJECT_VALUE || rr-tp*2*tW<=REJECT_VALUE) continue;					
					if (rr<MAX_RADIUS-1){
						Vector2D center = (s.center==null) ? new Vector2D() : s.center;
						circle(start, end, tmp1[3*kk],tmp1[1+3*kk], rr,center);
						s.center = center;
						//						tp = TrackSegment.getTurn(center.x, center.y, rr, start.x, start.y, end.x, end.y);
						if (rr<=REJECT_VALUE || rr>=MAX_RADIUS-1 || center.y>start.y) continue;																			
						if (s.start==null){
							s.start = new Vector2D(start);
						} else s.start.copy(start);											
						if (s.end==null){
							s.end = new Vector2D(end);
						} else s.end.copy(end);														
						s.type = tp;
						s.radius = rr;						
					} else if (prev.type*(end.x-start.x)>0){																		
						if (s.start==null){
							s.start = new Vector2D(start);
						} else s.start.copy(start);											
						if (s.end==null){
							s.end = new Vector2D(end);
						} else s.end.copy(end);									
//						s.center = null;
						s.type = 0;
						s.radius = 0;						
					} else continue;					
					s.num = 2;
					s.startIndex = from+1;
					s.endIndex = from+2;
					s.unsafe = false;
					reSynchronize(s, os, 0, otherTo, -which, tw);				
					if ((os.type!=0 && ((os.num>=2 && check(opoints, os.startIndex, os.endIndex+1, os.center, os.radius)<0))) || (op!=null && op.type!=Segment.UNKNOWN && (os.startIndex<=op.endIndex || os.start.y<=op.end.y))) {								
						continue;
					}
					if (s.type==0){ 
						if (s.end.y-s.start.y<3 || os.end.y-os.start.y<3) continue;
						double total = 0;
						for (int k = os.startIndex;k<=os.endIndex;++k){
							Vector2D vv = opoints[k];
							total += Math.sqrt(Geom.ptLineDistSq(os.start.x, os.start.y, os.end.x, os.end.y, vv.x, vv.y, null));							
						}
						total /= (double)os.num;
						if (total>=0.1) continue;
					}
					
					Vector2D pt = new Vector2D();
					boolean ok = false;					
					if (next==null || ((ok = (s.type==next.type && s.type!=0 && s.radius==next.radius && Math.abs(s.center.y-next.center.y)<1)) || isConnected(s, next, tW, pt)) ){
						if (ok) {
//							if (next!=null && next.radius==s.radius) 
//								radiusFrom2Points(next, start, end, tW, null);
							s.endIndex = next.startIndex-1;
							s.end.copy(v[s.endIndex]);
							s.num = s.endIndex+1-s.startIndex;
							if (s.num<3) s.unsafe = false;
							reSynchronize(s, os, 0, otherTo, -which, tw);
							return 1+indx;
						} else if (next==null && isConnected(prev, s, tW, pt)){
							connect(prev, s, pt, tW);
							if (s.type!=Segment.UNKNOWN && s.startIndex==prev.endIndex+1){
								reSynchronize(s, os, 0, otherTo, -which, tw);
								s.done = true;
								return 1+indx;
							}														
						} else if (next!=null){							
							connect(next, s, pt, tW);
							if (s.type!=Segment.UNKNOWN && s.endIndex+1==next.startIndex ){
								reSynchronize(s, os, 0, otherTo, -which, tw);
								s.done = true;
								return 1+indx;
							}	
						}
					}
				}				
			}
		}
		return indx;
	}


	/*private static final boolean checkSeg(final Vector2D[] edge,Segment s,int start,int end){
		if (end<start) return true;
		double d = check(edge, start, end+1, s.center, s.radius);
		if (end<start+2) return (d>=0);
		Vector2D first = edge[start];
		Vector2D last = edge[end];
		Vector2D center = new Vector2D(s.center);
		center = circle(first, last, center, s.radius);
		double d1 = check(edge, start, end+1, center, s.radius);
		if (d<0 && d1<0) return false;
		if (d<0 || d>d1) s.center = center;
		return true;
	}//*/
	//	private static final void updateSegment(final Vector2D[] left,final Vector2D[] right,ObjectArrayList<Segment> l,ObjectArrayList<Segment> tr,ObjectArrayList<Segment> r,IntArrayList lS,IntArrayList lE,IntArrayList rS,IntArrayList rE,double tW){
	//		int j =0;
	//		int k =0;
	//		Segment sL = null;
	//		Segment sR = null;
	//		Segment prevL = l.get(0);
	//		Segment prevR = r.get(0);
	//		Segment nextL = null;
	//		Segment nextR = null;
	//		for (int i=1;i<tr.size();++i){
	//			Segment mid = tr.get(i);
	//			double rad = mid.radius;
	//			double rL = (mid.type==0 || mid.type==Segment.UNKNOWN) ? 0 : mid.radius+mid.type*tW;			
	//			double rR = (mid.type==0 || mid.type==Segment.UNKNOWN) ? 0 : mid.radius-mid.type*tW;
	//			for (;j<l.size();++j){
	//				sL = l.get(j);
	//				if (Math.abs(sL.radius-rL)<1) break;
	//			}
	//			for (int jj = j+1;jj<l.size();++jj){
	//				nextL = l.get(jj);
	//				if (nextL.type!=Segment.UNKNOWN) break;
	//			}
	//			
	//			for (;k<r.size();++k){
	//				sR = r.get(k);
	//				if (Math.abs(sR.radius-rR)<1) break;
	//			}
	//			for (int jj = k+1;jj<r.size();++jj){
	//				nextR = r.get(jj);
	//				if (nextR.type!=Segment.UNKNOWN) break;
	//			}
	//			if (sL!=null && sR!=null && !sL.updated && !sR.updated) continue;
	//			int[] map = mid.map;
	//			int startL = (j<lS.size()) ? lS.getInt(j) : lS.get(lS.size()-1)+1;
	//			int endL = (j<lS.size()) ? lE.getInt(j) : startL-1;
	//			int startR = (k<rS.size()) ? rS.getInt(k) : rS.get(rS.size()-1)+1;
	//			int endR = (k<rS.size()) ? rE.getInt(k) : startR-1;
	//			if (!checkSeg(left, sL, startL, endL) || !checkSeg(right, sR, startR, endR)) {
	//				sL.type = Segment.UNKNOWN;
	//				sR.type = Segment.UNKNOWN;
	//				tr.remove(i--);
	//				continue;
	//			}
	//			double r1 = guessNewRad(left, startL, endL, tW, prevL, nextL, sL);
	//			double r2 = guessNewRad(right, startR, endR, tW, prevR, nextR, sR);
	//			System.out.println(r1+"    "+r2);
	//		}
	//	}

	public final static double findBestRadius(Vector2D[] v,int from,int to,Vector2D center,Vector2D start,Vector2D end){
		if (from>to-3) return -1;
		double r = -1;
		double max = 100;
		double rx = 0;
		double ry = 0;
		int sI = 0;
		int eI = 0;		

		for (int i = from;i<to-2;++i){
			Vector2D p1 = v[i];
			double x1 = p1.x;
			double y1 = p1.y;
			for (int j = i+1;j<to-1;++j){
				Vector2D p2 = v[j];
				double x2 = p2.x;
				double y2 = p2.y;				
				for (int k = j+1;k<to;++k){
					Vector2D p3 = v[k];
					double x3 = p3.x;
					double y3 = p3.y;

					double ax = x2 - x1;  // first compute vectors
					double ay = y2 - y1;  // a and c
					double cx = x1 - x3;
					double cy = y1 - y3;

					double aPerpDOTc = ax * cy - ay * cx;

					if (aPerpDOTc == 0)
						continue;

					double bx = x3 - x2;
					double by = y3 - y2;
					double bDOTc = bx * cx + by * cy;

					double qo = bDOTc / aPerpDOTc;
					double sx = x1 + (ax - qo * ay) / 2.0d; // found center of circle
					double sy = y1 + (ay + qo * ax) / 2.0d; // (sx, sy)					

					double absSy = (sy<0) ? -sy : sy;
					//					double ddx = x1 - sx;
					//					double ddy = y1 - sy;
					//					double rSquared = dx * dx + dy * dy; // radius of the circle squared
					//					double rr = Math.sqrt(ddx * ddx + ddy * ddy);
					//					System.out.println(rr+"   "+i+"   "+j+"   "+k+"   "+sx+"   "+sy);
					if (absSy<max){
						max = absSy;
						double dx = x1 - sx;
						double dy = y1 - sy;
						//						double rSquared = dx * dx + dy * dy; // radius of the circle squared
						r = Math.sqrt(dx * dx + dy * dy);
						rx = sx;
						ry = sy;
						sI = i;
						eI = k;
						//						System.out.println(r+"   "+i+"   "+j+"   "+k+"   "+sx+"   "+sy);
					}
				}
			}
		}

		if (center!=null){
			center.x = rx;
			center.y = ry;
		}
		Vector2D p = v[sI];
		if (start!=null){
			start.x = p.x;
			start.y = p.y;
		}

		p = v[eI];
		if (end!=null){
			end.x = p.x;
			end.y = p.y;
		}
		return r;
	}

	/*private static final ObjectArrayList<Segment> fillGap(final Vector2D[] v,int from, int to,double tW,Segment prev,Segment nextSeg,ObjectArrayList<Segment> rs,ObjectArrayList<Segment> other){		
		if (rs==null) rs = ObjectArrayList.wrap(new Segment[30],0);
		int size = to - from;
		Vector2D first = v[from];
		Vector2D last = v[to-1];		
		int which = (tW<0) ? 1 : -1;
		Vector2D[] opoints = (which==1) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;
		int otherTo = (which==1) ? CircleDriver2.edgeDetector.lSize:  CircleDriver2.edgeDetector.rSize;
		double abstW = (tW<0) ? -tW : tW;
		double tw = abstW*2;
		int nr = (nextSeg==null) ? 0: (nextSeg.radius>=MAX_RADIUS-1) ? MAX_RADIUS : double2int(nextSeg.radius-nextSeg.type*tW);
		if (size<2) return null;
		if (size==2 && prev!=null && prev.type==0){			
			double xx = prev.start.x;
			Geom.getCircle2(first, last, prev.start, prev.end, temp);					
			double r = Math.sqrt(temp[2]);
			int tp = TrackSegment.getTurn(temp[0], temp[1], temp[2], first.x, first.y, last.x, last.y);
			r = Math.round(r-tp*tW)+tp*tW;
			double cx = temp[0];
			double cy = temp[1];
			double ox = (first.x+last.x)*0.5;
			double oy = (first.y+last.y)*0.5;
			double dx = ox - first.x;
			double dy = oy - first.y;
			double d = dx*dx+dy*dy;
			double vx = cx - ox;
			double vy = cy - oy;
			double dv = vx*vx+vy*vy;			
			double dt = Math.sqrt((r*r-d)/dv); 
			ox += vx * dt;
			oy += vy * dt;
			Vector2D center = new Vector2D(ox,oy);
			//			Vector2D o = first.plus(last).times(0.5);
			//			double d = o.distance(first);
			//			center = o.plus(center.minus(o).normalised().times(Math.sqrt(r*r-d*d) ));

			if (r<=REJECT_VALUE || oy<0 || oy>first.y) return null;
			Segment s = null;			
			int rr = (nextSeg==null) ? 0 : (nextSeg.type==0 || nextSeg.radius>=MAX_RADIUS-1) ? MAX_RADIUS : double2int(nextSeg.radius-nextSeg.type*tW);
			if (nextSeg!=null && tp==nextSeg.type && nextSeg.type!=0 && r==nextSeg.radius ){
				//				nextSeg.start = new Vector2D(first);				
				nextSeg.map[rr]++;
				//				nextSeg.reCalLength();
				s = new Segment();
				s.start = new Vector2D(first);
				s.end = new Vector2D(last);
				s.type = tp;
				s.radius = r;
				s.center = center;
				s.num = size;
				s.points = v;
				s.startIndex = from;
				s.endIndex = to-1;
				rs.add(s);
				return rs;
			}
			if (r>=MAX_RADIUS-1){				
				s = new Segment(0, first.x, first.y, last.x, last.y, 0, 0, Double.MAX_VALUE);				
			} else {															
				s = new Segment(first.x, first.y, last.x, last.y,center.x, center.y, r,tW);				
			}			
			s.num = size;
			s.points = v;

			if (nextSeg!=null && nextSeg.type!=0){				
				if (nextSeg.type==s.type && nextSeg.radius!=s.radius){
					if (Math.abs(nextSeg.radius-s.radius)<=2){
						last = nextSeg.end;
						Geom.getCircle2(first, last, new Vector2D(xx,0), new Vector2D(xx,1), temp);					
						r = Math.sqrt(temp[2]);
						tp = TrackSegment.getTurn(temp[0], temp[1], r, first.x, first.y, last.x, last.y);
						r = Math.round(r-tp*tW)+tp*tW;
						cx = temp[0];
						cy = temp[1];
						ox = (first.x+last.x)*0.5;
						oy = (first.y+last.y)*0.5;
						dx = ox - first.x;
						dy = oy - first.y;
						d = dx*dx+dy*dy;
						vx = cx - ox;
						vy = cy - oy;
						dv = vx*vx+vy*vy;
						dt = Math.sqrt((r*r-d)/dv);
						ox += vx * dt;
						oy += vy * dt;
						center = new Vector2D(ox,oy);
						if (r==nextSeg.radius){
							s.radius = r;	
						} else {
							apply(nextSeg, tW, first, last, center, r);
							nextSeg.map[nr]--;
							s.radius =r;
						}						
						s.map = null;
					} else if (nextSeg.map!=null && nextSeg.map[nr]<3){
						nextSeg.map = null;
						nextSeg.type = UNKNOWN;
						nextSeg.radius = -1;
					}
				}				
			}
			s.startIndex = from;
			s.endIndex = to-1;
			rs.add(s);

			return rs;
		}
		int oldPrev = (prev==null) ? 0 : prev.num;
		int oldNext = (nextSeg==null) ? 0 : nextSeg.num;		
		//		int pr = (prev==null) ? 0: (prev.radius>=MAX_RADIUS-1) ? MAX_RADIUS : double2int(prev.radius-prev.type*tW);
		//				if (CircleDriver2.time>=CircleDriver2.BREAK_TIME){
		//					ObjectArrayList<Segment> list = bestGuess(v, from,to, tW, prev, nextSeg,rs);
		//					System.out.println();
		//					rs.size(0);					
		//				}
		//		rs = (size==0) ? null : (size>=6) ? Segment.segmentize(v, from, to, tW,starts,ends,rs) : bestGuess(v, from,to, tW, prev, nextSeg,starts,ends,rs);		
		rs = bestGuess(v, from,to, tW, prev, nextSeg,rs);
		if (rs==null) return null;

		int sz = rs.size();		
		Segment[] rsArr = rs.elements();				
		if ((prev!=null && oldPrev!=prev.num) || (nextSeg!=null && oldNext!=nextSeg.num)){
			from = (sz>0) ? rsArr[0].startIndex : from;
			to = (sz>0) ? rsArr[sz-1].endIndex+1 : to;
			size = to - from;
			first = v[from];
			last = v[to-1];		
		}
		//		if (size>=6 && sz==1 && rsArr[0].type==UNKNOWN){
		//			starts.size(0);
		//			ends.size(0);
		//			rs.size(0);			
		//			bestGuess(v, from,from+5, tW, prev, nextSeg,starts,ends,rs);
		//			sz = rs.size();
		//			if (sz>0){				
		//				Segment s = rsArr[0];
		//				if (s.type==UNKNOWN){
		//					for (int i = 1;i<sz;++i){
		//						s = rsArr[i];
		//						if (s!=null && s.type!=UNKNOWN) break;
		//					}
		//				}
		//				if (s!=null && (s.type==UNKNOWN || (s.lower==null && s.upper==null))){
		//					s.type = UNKNOWN;
		//					s.radius = 0;
		//					s.map = null;
		//					s.start = new Vector2D(first);
		//					s.end = new Vector2D(last);
		//					startArr[0] = s.startIndex = from;
		//					endArr[0] = s.endIndex = to-1;
		//					s.num = size;
		//					rsArr[0] = s;
		//					starts.size(1);
		//					ends.size(1);
		//					rs.size(1);
		//					sz = 1;
		//				} else if (s!=null && (s.lower!=null || s.upper!=null)){
		//					Segment l = rsArr[sz-1];
		//					if (l.type==UNKNOWN){
		//						l.end = new Vector2D(last);
		//						endArr[sz-1] = l.endIndex = to - 1;
		//						l.num = to - l.startIndex;
		//					} else {
		//						Segment tmp = new Segment();
		//						tmp.start = new Vector2D(v[from+5]);
		//						tmp.end = new Vector2D(v[to-1]);
		//						tmp.num = to - from -5;
		//						startArr[sz] = tmp.startIndex = from+5;
		//						endArr[sz] = tmp.endIndex = to -1;
		//						rsArr[sz] = tmp;
		//						++sz;
		//						starts.size(sz);
		//						ends.size(sz);
		//						rs.size(sz);
		//					}					
		//				}				
		//			} 
		//		}
		if (rs!=null && sz==1){
			Segment s = rsArr[0];
			//			s.startIndex = from;
			//			s.endIndex = to-1;
			s.points = v;
			if (s.type!=UNKNOWN){				
				if (s.radius<REJECT_VALUE) {
					s.lower = null;
					if (prev!=null) prev.upper = null;					
					return null;				
				}
				if (s.type!=0 && s.num>=2 && prev!=null && prev.type==0){				
					double xx = prev.start.x;
					first = v[s.startIndex];
					last = v[s.endIndex];
					Geom.getCircle2(first, last, new Vector2D(xx,0), new Vector2D(xx,1), temp);					
					double r = Math.sqrt(temp[2]);
					int tp = TrackSegment.getTurn(temp[0], temp[1], r, first.x, first.y, last.x, last.y);
					r = Math.round(r-tp*tW)+tp*tW;					

					if (r<REJECT_VALUE) return rs;

					if (nextSeg!=null && tp==nextSeg.type && nextSeg.type!=0 && r==nextSeg.radius && nextSeg.type==tp){
						//						nextSeg.start = new Vector2D(first);						
						nextSeg.map[nr]++;
						//						nextSeg.reCalLength();
						s.start = new Vector2D(first);
						s.end = new Vector2D(last);						
						s.type = tp;
						s.radius=r;						
						return rs;
					}
					int rr = (r>=MAX_RADIUS-1) ? MAX_RADIUS : double2int(r-tp*tW);
					boolean ok = false;
					//					s.map[s.minR] = 0;
					if (r>=MAX_RADIUS-1){						
						s.type = 0;
						s.start.x = first.x;
						s.end.x = last.x;
						s.start.y = first.y;
						s.end.y = last.y;
						int[] map = (s.map==null) ? new int[MAX_RADIUS] :s.map;						
						map[MAX_RADIUS-1]++;
						if (s.map==null) s.map = map;
						s.radius = Double.MAX_VALUE;
					} else if (tp==s.type && r==s.radius){
						s.map[rr]++;
					} else if (tp==s.type && Math.abs(s.radius-r)>0.5){
						Vector2D point = new Vector2D();
						boolean isConnected = isConnected(prev, s, tW, point);
						if (isConnected){
							if (s.opp==null) {
								s.opp = new Segment();
								s.opp.points = opoints;
								reSynchronize(s,s.opp,0,otherTo,1,tW*2);
							}					
							if (prev.opp==null) {
								prev.opp = new Segment();
								prev.opp.points = opoints;
								reSynchronize(prev,prev.opp,0,otherTo,1,tW*2);
							}
							connect(prev, s, point.y, tW);
							int start = s.startIndex;
							int end = s.endIndex;
							for (int i=end;i>=start+1;--i){
								last = v[i];
								Geom.getCircle2(first, last, new Vector2D(xx,0), new Vector2D(xx,1), temp);					
								r = Math.sqrt(temp[2]);
								tp = TrackSegment.getTurn(temp[0], temp[1], r, first.x, first.y, last.x, last.y);
								r = Math.round(r-tp*tW)+tp*tW;
								if (r<REJECT_VALUE) continue;

								if (r==s.radius){
									Segment ns = new Segment();
									ns.start = new Vector2D(v[ns.startIndex = i+1]);
									ns.end = s.end;
									ns.endIndex = end;
									ns.num = end-i;
									s.end = new Vector2D(last);									
									s.endIndex = i;
									s.num = i+1-start;
									rs.add(ns);
									return rs;
								}								
							}
							return rs;
						}

						double cx = temp[0];
						double cy = temp[1];
						double ox = (first.x+last.x)*0.5;
						double oy = (first.y+last.y)*0.5;
						double dx = ox - first.x;
						double dy = oy - first.y;
						double d = dx*dx+dy*dy;

						double nx = -dy;
						double ny = dx;		
						double dn = nx*nx+ny*ny;

						cx -= ox;
						cy -= oy;
						if (nx*cx+ny*cy<0) {
							nx = -nx;
							ny = -ny;
						}

						double dt = Math.sqrt((r*r-d)/dn);
						ox += nx * dt;
						oy += ny * dt;		
						Vector2D center = new Vector2D(ox,oy);

						if (oy<0) return rs;
						if (check(v, from,to, center, r)>=0){							
							s.type = tp;
							s.center.x = ox;
							s.center.y = oy;
							s.radius = r;
							if (s.start.y>first.y){
								s.start = new Vector2D(first);
								s.startIndex = from;
							}

							if (s.end.y<last.y){
								s.end = new Vector2D(last);
								s.endIndex = to-1;
							}
							int[] map = (s.map==null) ? new int[MAX_RADIUS] : s.map;							
							if (s.type!=Segment.UNKNOWN){								
								map[rr]++;								
								if (s.map==null) s.map = map;
								ok = true;
							} else if (s.map!=null) s.map[rr] = -1;
						} else if (tp==s.type){			
							if (s.map!=null) s.map[rr]++;						
							return rs;
						} 
						//					int nr = (nextSeg==null || nextSeg.type==0) ? 0 : (nextSeg.radius>=MAX_RADIUS-1) ? MAX_RADIUS : double2int(nextSeg.radius-nextSeg.type*tW);
						if (ok && nextSeg!=null && nextSeg.type!=0){	
							if (nextSeg.type==s.type && nextSeg.radius!=s.radius){
								if (Math.abs(nextSeg.radius-s.radius)<=2){
									last = nextSeg.end;
									Geom.getCircle2(first, last, new Vector2D(xx,0), new Vector2D(xx,1), temp);					
									r = Math.sqrt(temp[2]);
									tp = TrackSegment.getTurn(temp[0], temp[1], r, first.x, first.y, last.x, last.y);
									r = Math.round(r-tp*tW)+tp*tW;
									cx = temp[0];
									cy = temp[1];
									ox = (first.x+last.x)*0.5;
									oy = (first.y+last.y)*0.5;
									dx = ox - first.x;
									dy = oy - first.y;
									d = dx*dx+dy*dy;

									nx = -dy;
									ny = dx;		
									dn = nx*nx+ny*ny;

									cx -= ox;
									cy -= oy;
									if (nx*cx+ny*cy<0) {
										nx = -nx;
										ny = -ny;
									}

									dt = Math.sqrt((r*r-d)/dn);
									ox += nx * dt;
									oy += ny * dt;	
									center = new Vector2D(ox,oy);

									if (r==nextSeg.radius){
										s.radius = r;	
									} else {
										apply(nextSeg, tW, first, last, center, r);
										nextSeg.map[nr]--;
										s.radius =r;
									}	
									s.map = null;
								} else if (nextSeg.map!=null && nextSeg.map[nr]<3){
									nextSeg.map = null;
									nextSeg.type = UNKNOWN;
									nextSeg.radius = -1;
								}
							}
						}
					}												
				}

				//				 && !guessFromPrevNext(v, from, to, tW, prev, nextSeg, s, tmp, size, firstIndex, marks, mark)							

				return rs;
			}							

		} else if (rs!=null){			
			int mrk = 0;
			//			if (size<6) return rs;
			for (int i =0;i<sz;++i){
				Segment s = rsArr[i];
				if (s==null) continue;
				s.points = v;
				if (i==0 && s.num>=2 && prev!=null && prev.type==0 && s.type!=0 && s.type!=Segment.UNKNOWN){					
					double xx = prev.start.x;
					first = v[s.startIndex];
					Vector2D lst = v[s.endIndex];
					Geom.getCircle2(first, lst, new Vector2D(xx,0), new Vector2D(xx,1), temp);					
					double r = Math.sqrt(temp[2]);
					int tp = TrackSegment.getTurn(temp[0], temp[1], r, first.x, first.y, lst.x, lst.y);
					r = Math.round(r-tp*tW)+tp*tW;				
					if (r<REJECT_VALUE) continue;
					int rr = (r>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(r-tp*tW);
					//					int nr = (nextSeg==null || nextSeg.type==0) ? 0 : (nextSeg.radius>=MAX_RADIUS-1) ? MAX_RADIUS : double2int(nextSeg.radius-nextSeg.type*tW);
					//					if (nextSeg!=null && nextSeg.type!=0){						
					//						nextSeg.copy(s);						
					//					}
					if (s.type!=Segment.UNKNOWN && tp==s.type && Math.abs(s.radius-r)<0.5) {
						if (s.map!=null) s.map[rr]++;						
						mrk++;
						continue;
					}
					if (r>=MAX_RADIUS-1){						
						TrackSegment ts = TrackSegment.createStraightSeg(0, first.x, first.y, lst.x, lst.y);
						s.copy(ts);
					} else {																		
						Vector2D center = new Vector2D(temp[0],temp[1]);						
						center = circle(first, lst, center, r);

						if (check(v,from,from+s.num,center,r)>=0){
							TrackSegment ts = TrackSegment.createTurnSeg(center.x, center.y, r, first.x, first.y, lst.x, lst.y);
							if (s.map!=null) s.map[rr]++;
							s.copy(ts,tW);
							s.startIndex = from;
							s.endIndex = from+s.num-1;
						}
					}
					Segment next = rsArr[1];
					//					expandForward(v, from, to, tW, s.center, s.radius, prev, rs.get(1), s, tmp,size,from+s.num-1, tmp[0], marks, mark);
					if (next!=null && next.type!=Segment.UNKNOWN && next.start!=null && next.end!=null && next.start.y>=next.end.y){
						while (next!=null && next.start.y>=next.end.y){
							if (i<rs.size()) rs.remove(i+1);
							next = (i+1<rs.size()) ? rs.get(i+1) : null;
						} 
					} 

				} else if (s.type!=Segment.UNKNOWN){															
					int rr = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : (int)Math.round(s.radius-s.type*tW);
					if (rr>REJECT_VALUE){
						if (s.minR>rr) s.minR = rr;
						if (s.maxR<rr) s.maxR = rr;
						//						s.map[rr]++;		
					}
				}

				if (s.type==Segment.UNKNOWN && s.num>=2 && s.start!=null && s.end!=null){
					Segment p = (i==0) ? prev : rsArr[i-1];
					Segment n = (i==sz-1) ? nextSeg : rsArr[i+1];
					if (p!=null && n!=null && p.type!=Segment.UNKNOWN && n.type!=Segment.UNKNOWN){
						if (guessPN(p, n, tW, s.start, s.end, temp)){
							s.type = (int)(temp[3]);
							s.radius = temp[2];
							s.center = new Vector2D(temp[0],temp[1]);
							int[] map = (s.map==null) ? new int[MAX_RADIUS] : s.map;
							int rr = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : (int)Math.round(s.radius-s.type*tW);
							map[rr]++;
							if (s.map==null) s.map = map;
							int oldSz = sz;
							sz = checkPN(v, rsArr, i, tW, sz,other);
							i -= oldSz -sz;							
							sz = checkPN(v, rsArr, i+1, tW, sz,other);							
						}
					}
				}
				//				 || guessFromPrevNext(v, from, to, tW, prev, nextSeg, s, tmp, size, firstIndex, marks, mark)
				if (s.type==Segment.UNKNOWN || s.radius<REJECT_VALUE){					
				} else {					
					mrk++;
				}
			}//end of for
			mark += mrk;
			rs.size(sz);
		}		
		return rs;
	}//*/	

	/*private static final void mix(Segment o, Segment s,int which,double tW){				
		int rrr = (o.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(o.radius+o.type*which*tW);
		int sR = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius+s.type*which*tW);
		//		if (sR!=rrr && (o.type==s.type || s.type==0 || o.type==0)) o.map[sR] += s.map[sR];		
		if (o.minR>rrr) o.minR=rrr;
		if (o.maxR<rrr) o.maxR=rrr;

		if (s.type==o.type || s.type==0 || o.type==0) {		
			if (rrr==sR){
				if (o.start.y>s.start.y) o.start = new Vector2D(s.start);
				if (o.end.y<s.end.y) o.end = new Vector2D(s.end);				
				o.reCalLength();
			} else if (o.map[rrr]<o.map[sR]){				
				o.start = new Vector2D(s.start);
				o.end = new Vector2D(s.end);				
				o.radius = s.radius;
				o.type = s.type;
				o.reCalLength();
			}					
		} else {			
			if (o.mapR==null) {
				o.mapR=s.map;
			} 
			//			else o.mapR[sR] += s.map[sR];
			if (o.map[rrr]<=o.mapR[sR]){
				o.start = new Vector2D(s.start);
				o.end = new Vector2D(s.end);				
				o.radius = s.radius;
				o.type = s.type;
				int[] tmp = o.map;
				o.map = o.mapR;
				o.mapR = tmp;
				o.reCalLength();				
			}
		}	
	}//*/

	// ss = previous segs, li = new segs, return in li
	//Need good algorithm
	/*private static final void mix(ObjectArrayList<Segment> ss, ObjectArrayList<Segment> li,int which,double tW){
		if (li==null || li.size()==0) return;		
		Segment[] ssArr = ss.elements();
		Segment[] liArr = li.elements();
		int sz = ss.size();
		int lz = li.size();
		//		EdgeDetector edge = CircleDriver2.edgeDetector;
		//		Vector2D[] v = (which==1) ? edge.right : edge.left;
		//		Vector2D[] otherPoints = (which==-1) ? edge.right : edge.left;
		//		int otherTo = (which==1) ? edge.lSize : edge.rSize;		
		//		int to = (which==-1) ? edge.lSize : edge.rSize;
		Segment end = liArr[0];
		int startIndex = (end.num==0) ? end.endIndex : end.startIndex;
		if (sz>0){
			end = ssArr[0];
			if (end.num>0 && startIndex>end.startIndex) 
				startIndex = end.startIndex;
			else if (end.num==0 && startIndex>end.endIndex) 
				startIndex = end.endIndex;
		}
		end = (lz<1) ? null : liArr[lz-1];
		int endIndex = (end==null) ? startIndex-1 : (end.num>0) ?  end.endIndex : end.startIndex;
		end = (sz<1) ? null : ssArr[sz-1];
		if (sz>0){
			if (end.num==0 && endIndex<end.startIndex) 
				endIndex = end.startIndex;
			else if (end.num>0 && endIndex<end.endIndex)
				endIndex = end.endIndex;
		}
		int size = endIndex+1-startIndex;
		int[] mark = new int[size];


		for (int i = 0;i<sz;++i){
			Segment s = ssArr[i];
			int endIndx = s.endIndex;
			if (s.type!=UNKNOWN && s.map!=null)
				for (int j = s.startIndex;j<=endIndx;++j) {
					double score = score(s,which,tW);
					if (j-startIndex<0 || score>MAX_RADIUS-1 || score<0)
						System.out.println();
					mark[j-startIndex] = s.map[score(s,which,tW)];
				}
		}

		int oldLz = lz;
		int k = 0;		
		int jj = 0;
		Segment s = null;
		Segment l = null;
		for (int i = 0;i<lz;++i){
			l = liArr[i];
			if (l==null) continue;
			int endIndx = l.endIndex;
			int startIndx = l.startIndex;
			if (l.type!=UNKNOWN && l.map!=null){
				boolean ok = true;
				int lr = score(l,which,tW);
				int score = l.map[lr];
				if (l.type==0) score--;
				s = (k<sz) ? ssArr[k] : null;
				for (int j = startIndx;j<=endIndx;++j){
					if (j>=startIndex && j-startIndex<size && mark[j-startIndex]>=score) {
						while (k<sz){
							s = ssArr[k];
							if (s.endIndex<j || (jj>0 && liArr[jj-1].endIndex>=s.startIndex)) 
								k++;
							else break;
						}													

						if (s!=null && (s.type!=l.type || (s.type!=0 && s.radius!=l.radius))){
							ok = false;							
							break;
						} else if (s!=null && s.radius==l.radius){
							boolean changed = false;
							if (l.endIndex>=s.endIndex && s.end.y>l.end.y) {
								l.end = s.end;
								changed = true;
							}
							if (l.startIndex<=s.startIndex && s.start.y<l.start.y) {
								l.start = s.start;
								changed = true;
							}
							if (s.map!=null) {
								if (s.map[lr]<l.map[lr]) s.map[lr] = l.map[lr];
								if (changed && s.map!=null) {
									s.map[lr]++;
									l.updated = true;
								}
								if (s.map!=null) l.map = s.map;
							}
						}
					}
				}//end of for

				if (!ok && s!=null && s.map!=null){
					s.map[lr] += score;				
					int sr = score(s,which,tW);
					if (s.type==0) s.map[sr]--;
					if (s.map!=null && sr>0 && lr>0 && s.map[lr]>=s.map[sr]) {														
						if (s.type!=UNKNOWN && s.map!=null)
							for (int j = s.startIndex;j<=s.endIndex;++j) mark[j-startIndex] = 0;
						if (s.map!=null) l.map = s.map;
						l.updated = true;
						liArr[jj++] = l;
					} else if (jj>0){
						Segment tmp = liArr[jj-1];
						if (tmp.endIndex>=s.startIndex ){
							int tr = score(tmp,which,tW);							
							if (tr==sr){
								tmp.endIndex = s.endIndex;
								tmp.end = new Vector2D(tmp.points[tmp.endIndex]);
								tmp.num = tmp.endIndex - tmp.startIndex +1;
								tmp.updated = true;
							} else if (tmp.map[tr]<=s.map[sr]){
								s.map[tr] += tmp.map[tr];
								liArr[jj-1] = s;
							}
						}
					} else liArr[jj++] = s;
				} else if (!ok){ 
					liArr[jj++] = s;
				} else liArr[jj++] = l;									
			} else if (l.type==UNKNOWN){					
				Segment t = (jj==0) ? null : liArr[jj-1];
				while (k<sz){
					s = ssArr[k];
					if (s.endIndex<startIndx || (jj>0 && t.endIndex>=s.startIndex)) 
						k++;
					else break;
				}
				if (s!=null && s.type!=UNKNOWN && s.endIndex<=endIndx && (t==null || s.start.y>t.end.y)){
					liArr[jj++] = s;					
				}					
			}

			if (jj>0 && i<lz-1){
				Segment t = (jj==0) ? null : liArr[jj-1];
				while (k<sz){
					s = ssArr[k];
					if (s.endIndex<t.startIndex || (jj>0 && t.endIndex>=s.startIndex)) 
						k++;
					else break;
				}
				while (i<lz-1){
					l = liArr[i+1];
					if (l==null || t==null || l.endIndex<t.startIndex || (jj>0 && t.endIndex>=l.startIndex)) 
						i++;
					else break;
				}
			}
		}

		if (oldLz!=jj){
			li.size(jj);
		}
		return;

	}//*/


	private static final int mix(Segment[] ssArr,int sz, Segment[] liArr,int lz,int which,double tW,Segment[] rs){
		if (liArr==null || lz==0) return 0;		
		Segment end = liArr[0];
		int startIndex = (end.num==0) ? end.endIndex : end.startIndex;
		if (sz>0){
			end = ssArr[0];
			if (end.num>0 && startIndex>end.startIndex) 
				startIndex = end.startIndex;
			else if (end.num==0 && startIndex>end.endIndex) 
				startIndex = end.endIndex;
		}
		end = (lz<1) ? null : liArr[lz-1];
		int endIndex = (end==null) ? startIndex-1 : (end.num>0) ?  end.endIndex : end.startIndex;
		end = (sz<1) ? null : ssArr[sz-1];
		if (sz>0){
			if (end.num==0 && endIndex<end.startIndex) 
				endIndex = end.startIndex;
			else if (end.num>0 && endIndex<end.endIndex)
				endIndex = end.endIndex;
		}
		int size = endIndex+1-startIndex;
		int[] mark = new int[size];


		for (int i = 0;i<sz;++i){
			Segment s = ssArr[i];
			int endIndx = s.endIndex;
			if (s.type!=UNKNOWN && s.startIndex>=startIndex)
				for (int j = s.startIndex;j<=endIndx;++j) {
					int score = score(s,which,tW);
//					if (j-startIndex<0 || score>MAX_RADIUS-1 || score<0)
//						System.out.println();
					mark[j-startIndex] = (s.map!=null) ? s.map[score] : 1;
				}
		}

		int k = 0;		
		int jj = 0;
		Segment s = null;
		Segment l = null;
		for (int i = 0;i<lz;++i){
			l = liArr[i];
			if (l==null || l.type==Segment.UNKNOWN) continue;
			int endIndx = l.endIndex;
			int startIndx = l.startIndex;
			if (l.type!=UNKNOWN){
				boolean ok = true;				
				int lr = (l.type==0 || l.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : (int)Math.round(l.radius+l.type*tW*which);
				if (lr>MAX_RADIUS-1) lr = MAX_RADIUS-1;
				int score = (l.map==null) ? 1 : l.map[lr];
//				if (l.type==0) score--;
				s = (k<sz) ? ssArr[k] : null;
				Segment t = (jj==0) ? null : rs[jj-1];
				while (l!=null && s!=null && l.start.y>s.end.y && (t==null || t.end.y<s.start.y)){
					if (i==jj && rs==liArr){
						if (lz-i>0) System.arraycopy(liArr, i, liArr, i+1, lz-i);
						++lz;
						++i;								
					} 
					rs[jj++] = s;
					k++;
					s = (k<sz) ? ssArr[k] : null;
				}

				if (s==null && i<lz){
					if (lz-i>0) System.arraycopy(liArr, i, rs, jj, lz-i);
					jj+=lz-i;
					break;
				}

				for (int j = startIndx;j<=endIndx;++j){
					if (j>=startIndex && j-startIndex<size && mark[j-startIndex]>=score) {
						while (k<sz){
							s = ssArr[k];
							if (s.type==Segment.UNKNOWN || s.endIndex<j || (jj>0 && rs[jj-1].endIndex>=s.startIndex)) 
								k++;
							else break;
						}					
						if (k>=sz) s = null;						
						if (s!=null && (s.type!=l.type || (s.type!=0 && s.radius!=l.radius))){
							ok = false;							
							break;
						} else if (s!=null && s.type==l.type && ((s.type==0 && isSameSegment(s, l)) || s.radius==l.radius)){
							if (!l.unsafe) l.unsafe = s.unsafe;
							boolean changed = false;							
							if (l.endIndex>s.endIndex || l.end.y>s.end.y) {
								if (l.type!=0 || (l.type==0 && (Math.abs(s.start.x-s.end.x)<E || Math.abs(l.start.x-l.end.x)>E)  )){
									s.end.copy(l.end);
									s.endIndex = l.endIndex;
									changed = true;
								}
							}
							if (l.startIndex<s.startIndex || l.start.y<s.start.y) {
								if (l.type!=0 || (l.type==0 && (Math.abs(s.start.x-s.end.x)<E || Math.abs(l.start.x-l.end.x)>E)  )){
									s.start.copy(l.start);
									s.startIndex = l.startIndex;
									changed = true;
								}
							}
							int sR = (s.type==0) ? MAX_RADIUS-1 : (int)Math.round(s.radius+s.type*tW*which);
							if (s.map==null && l.map==null){
								s.map = new int[MAX_RADIUS];
								s.radCount = 1;								
								if (s.appearedRads==null) s.appearedRads = new int[MAX_RADIUS];
								s.appearedRads[0] = sR;
								if (s.opp!=null) {
									s.opp.appearedRads = s.appearedRads;
									s.opp.map = s.map;
									s.opp.radCount = 1;
								}
								s.map[sR]++;
							} else if (s.map==null) {
								s.map = l.map;
								s.radCount = l.radCount;
								s.appearedRads = l.appearedRads;
								if (s.map[sR]==0){
									s.appearedRads[s.radCount++] = sR;
									l.radCount++;
								}
								if (s.opp!=null) {
									s.opp.radCount = s.radCount;
									s.opp.map = s.map;
									s.opp.appearedRads = s.appearedRads;
								}
								s.map[sR]++;
							}
							
							if (s.map[lr]==0){
								s.appearedRads[s.radCount++] = lr;
								s.radCount++;
								s.opp.radCount++;
							}
							s.map[lr] += score;							
							if (s.map!=null){
								if (changed) {
									s.num = s.endIndex+1-s.startIndex;
//									if (s.map[lr]==0){
//										s.appearedRads[s.radCount++] = lr;
//										s.radCount++;
//										s.opp.radCount++;
//										s.map[lr]=1;
//									}
//									s.map[lr]++;
									
									s.updated = true;								
									int indx = l.opp.endIndex;
									if (indx<s.opp.endIndex) indx = s.opp.endIndex;
									if (!s.unsafe && (s.num>2 || s.map[lr]>3)) s.unsafe = true;
									Segment.reSynchronize(s, s.opp, 0,indx+1, -which, tW*2);
								} else if ((s.map[lr]<3 && s.upper==null && s.lower==null)&& s.num>l.num){
									s.map[lr]++;																										
									s.copy(l);									
									Segment os = s.opp;
									Segment ol = l.opp;									
									s.num = l.num;
									boolean oppDone = os.done;
									os.copy(ol);
									if (!s.unsafe && (s.num>2 || s.map[lr]>3)) {
										s.unsafe = true;
										os.unsafe = true;
									}
									os.done = oppDone;
								}								
							}
//							rs[jj++] = s;							
							break;
						}
					}
				}//end of for
				
				if (!ok && s!=null){
					int sr = (s.type==0) ? MAX_RADIUS-1 : (int)(s.radius+s.type*tW*which);
					if (sr>=MAX_RADIUS) sr = MAX_RADIUS-1;
					if (s.map==null && l.map==null){
						s.map = new int[MAX_RADIUS];
						s.radCount = 1;
						if (s.appearedRads==null) s.appearedRads = new int[MAX_RADIUS];
						s.appearedRads[0] = sr;
						if (s.opp!=null) {
							s.opp.map = s.map;
							s.opp.appearedRads = s.appearedRads;
							s.opp.radCount = s.radCount;
						}
						s.map[sr]++;
					} else if (s.map==null){
						s.map = l.map;						
						s.radCount = l.radCount;
						s.appearedRads = l.appearedRads;
						if (s.map[sr]==0) {
							s.appearedRads[s.radCount++] = sr;
							l.radCount++;
						}
						if (s.opp!=null) {
							s.opp.map = s.map;
							s.opp.radCount = s.radCount;
							s.opp.appearedRads = l.appearedRads;
						}
						s.map[sr]++;
					}
					if (s.type==l.type || l.type==0) {
						if (s.map[lr]==0) {
							s.appearedRads[s.radCount++] = lr;
							if (s.opp!=null) s.opp.radCount = s.radCount;							
						}
						s.map[lr] += score;									
					} else if (s.map[sr]>0) s.map[sr]--;					
					if (s.type==0 && s.map!=null && s.map[sr]>1) s.map[sr]--;
					if (s.map!=null && s.map[sr]==0){
						for (int ii = s.radCount-1;ii>=0;--ii){
							int rads = s.appearedRads[ii];
							if (rads==sr){
								if (s.radCount-ii-1>0) System.arraycopy(s.appearedRads, ii+1, s.appearedRads, ii, s.radCount-ii-1);
								s.radCount--;
								if (s.opp!=null) s.opp.radCount = s.radCount;
								break;
							}
						}
					}
					if (s.map!=null && sr>0 && lr>0 && (score>=s.map[sr] || s.map[lr]>=s.map[sr]) ) {														
						if (s.type!=UNKNOWN && s.map!=null)
							for (int j = s.startIndex;j<=s.endIndex;++j) mark[j-startIndex] = 0;
						
						if (s.map[lr]==0) {
							s.appearedRads[s.radCount++] = lr;
							if (s.opp!=null) s.opp.radCount = s.radCount;
							s.map[lr] = 1;
						}
						
						s.copy(l);
						Segment os = s.opp;						
						os.copy(l.opp);
						os.done = false;
						if (!s.unsafe && (s.num>2 || s.map[lr]>3)) {
							s.unsafe = true;
							os.unsafe = true;
						}
						rs[jj++] = s;
					} else if (jj>0){
						Segment tmp = rs[jj-1];
						if (tmp.endIndex>=s.startIndex ){							
							int tr = (tmp.type==0 || tmp.radius>=Segment.MAX_RADIUS) ? MAX_RADIUS-1 : (int)(tmp.radius+tmp.type*tW*which);								
							if (tr==sr && s.type==tmp.type){
								boolean changed = (tmp.endIndex>s.endIndex || tmp.end.y>s.end.y);
								if (changed){
									s.end.copy(tmp.end);
									s.endIndex = tmp.endIndex;
								}
								s.startIndex = tmp.startIndex;
								s.num = s.endIndex+1-s.startIndex;
								s.updated = true;
								if (s.start==tmp.start || s.start.x!=tmp.start.x || s.start.y!=tmp.start.y) {
									s.start.copy(tmp.start);
									reSynchronize(s, s.opp, tmp.opp.startIndex, s.opp.endIndex+1,-which, tW*2);
								} else if (changed)	reSynchronize(s, s.opp, tmp.opp.startIndex, s.opp.endIndex+1,-which, tW*2);															
								if (s.map==null){
									s.map=tmp.map;
								} else 	if (tmp.map!=null && s.map!=null && tmp.map[tr]>s.map[tr]) 
									s.map[tr] = tmp.map[tr];																
								s.opp.done = false;
								if (!s.unsafe && (s.num>2 || s.map[sr]>3)) {
									s.unsafe = true;
									s.opp.unsafe = true;
								}
								rs[jj-1] = s;
								
							} else if (tmp.map!=null && tmp.map[tr]<=s.map[sr]){
								s.map[tr] += tmp.map[tr];
								rs[jj-1] = s;
							} else if (tmp.map==null && s.map[sr]>=1){
								s.map[tr]++;
								rs[jj-1] = s;
							}
						}
					} else rs[jj++] = s;
				} else if (!ok){ 
					rs[jj++] = s;
				} else {
//					if (s!=null && s.map!=null){
//						l.map = s.map;
//						l.appearedRads = s.appearedRads;
//						l.radCount = s.radCount;
//						
//						if (l.map[lr]==0){ 
//							l.appearedRads[l.radCount++] = lr;
//							s.radCount++;
//							if (s.opp!=null) s.opp.radCount++;
//						}
//						l.map[lr]++;
//						if (l.opp!=null) {
//							l.opp.radCount = l.radCount;
//							l.opp.appearedRads = l.appearedRads;
//							l.opp.map = l.map;
//						}
//					}
					if (s.type!=l.type || s.type!=0 && s.radius!=l.radius){
						s.copy(l,-which*tW);
						s.opp.copy(l.opp);
						if (s.map!=null && l.map!=null && l.map[lr]>1)
							s.map[lr]+=l.map[lr]-1;						
					}

					rs[jj++] = s;									
				}
			} else if (l.type==UNKNOWN){					
				Segment t = (jj==0) ? null : liArr[jj-1];
				if (jj>0){
					while (k<sz){
						s = ssArr[k];
						if (s.type==Segment.UNKNOWN || s.endIndex<startIndx || (jj>0 && (t.endIndex>=s.startIndex || t.end.y>=s.start.y))) 
							k++;
						else break;
					}				
					if (k>=sz) s = null;
				}
				if (s!=null && s.type!=UNKNOWN && s.endIndex<=endIndx && (t==null || s.start.y>t.end.y)){
					rs[jj++] = s;					
				}
			}

			Segment t = (jj==0) ? null : rs[jj-1];
			if (i==lz-1){
				if (t!=null){
					while (k<sz){
						s = ssArr[k];
						if (s==null || s.type==Segment.UNKNOWN || s.endIndex<t.startIndex || (jj>0 && (t.endIndex>=s.startIndex || t.end.y>=s.start.y))) 
							k++;
						else break;
					}
					if (k>=sz) s = null;
				}
				if (k<sz-1){
					System.arraycopy(ssArr, k, rs, jj, sz-k);
					jj += sz-k;
				}
				break;
			} else if (jj>0){						
				while (k<sz){
					s = ssArr[k];
					if (s.type==Segment.UNKNOWN || s.endIndex<t.startIndex || (jj>0 && (t.endIndex>=s.startIndex || t.end.y>=s.start.y))) 
						k++;
					else break;
				}
				if (k>=sz) s = null;
				if (s!=null && s.type!=UNKNOWN && s.endIndex<=endIndx && (t==null || s.start.y>t.end.y)){
					rs[jj++] = s;					
				}

				if (i<lz-1){
					while (i<lz-1){
						l = liArr[i+1];
						if (l==null || t==null || l.type==Segment.UNKNOWN || l.endIndex<t.startIndex || (jj>0 && (t.endIndex>=l.startIndex || t.end.y>=l.start.y))) 
							i++;
						else break;
					}
				}
			}
		}

		return jj;

	}


	private static final boolean isSameSegment(Segment s,Segment t){
		double d = Math.max(s.start.y, t.start.y);
		double l = Math.min(s.end.y, t.end.y);
		if (l-d>0) return true;
		if (s.type!=t.type || Math.abs(s.radius-t.radius)>0.5) return false;
		if (s.type==0){
			double dx = s.end.x - s.start.x;
			double ddx = t.end.x - t.start.x;
			if (Math.abs(dx)<=TrackSegment.EPSILON && Math.abs(ddx)>TrackSegment.EPSILON) return false;
			if (Math.abs(dx)>TrackSegment.EPSILON && Math.abs(ddx)<=TrackSegment.EPSILON) return false;
			if (Math.abs(dx)>TrackSegment.EPSILON && Math.abs(ddx)>TrackSegment.EPSILON){
				double dy = s.end.y - s.start.y;
				double ddy = t.end.y - t.start.y;
				if (Math.abs(Math.atan2(dy, dx)-Math.atan2(ddy, ddx))>0.5) return false;
			}
		} 		
		return s.type==0 || s.center.distance(t.center)<2.5;
	}

	public static boolean isConfirmed(Segment s,int which,double tW){
		if (s.type==0 && Math.abs(s.start.x-s.end.x)<TrackSegment.EPSILON) return true;
		if (s.type!=0 && s.type!=UNKNOWN && s.center!=null && s.center.y==0) return true;		
		int sR = (s.type==0 || s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : (int)(Math.round(s.radius+s.type*which*tW));
		if (sR<0) sR = 0;
		return ((s.map!=null && sR>=0 && sR<s.map.length && s.map[sR]>3) || (s.type!=0 && s.center!=null && s.center.y==0) || ((s.num>=3 || (s.num>=2 && CircleDriver2.inTurn)) && s.lower!=null && s.upper!=null) || (s.num>4 && s.lower!=null || s.upper!=null) );
	}

	/*private static void removeFirstPoint(Segment s,Segment other){//remove the first point of s		
		if (s.num==0 || s.points==null) return;		
		Vector2D[] vv = s.points;
		Vector2D point = (s.startIndex>=0) ? vv[s.startIndex] : null;

		s.startIndex++;			
		s.updated = true;
		s.num--;
		if (s.num<0) {
			s.num = 0;
			s.endIndex = s.startIndex-1;
		}
		Vector2D v = (s.num>0 && s.startIndex>=0) ? s.points[s.startIndex] : s.start;
		if (s.num>0 && v.y>=s.end.y){
			s.type = Segment.UNKNOWN;
			s.opp.type = Segment.UNKNOWN;
			return;
		}
		if (s.num>0 && v!=null) s.start = new Vector2D(v);

		if (other==null || other.num==0 || v==null){			
			if (s.num>1 && v!=null) {				
				if (s.type!=0 && s.center!=null && s.center.y!=0) s.center = circle(v, s.end, s.center, s.radius);				
			} else if (s.num==0 || (s.type!=0 && s.center.y!=0)) s.type = UNKNOWN;
			return;
		}

		Vector2D end = (s.num>0) ? s.points[s.endIndex] : s.end;
		int endIndx = other.endIndex;
		boolean empty = true;
		if (s.type!=0 && s.type!=UNKNOWN){
			Vector2D center = s.center;
			double cx = center.x;
			double cy = center.y;
			double oy = v.y;
			double r = s.radius/other.radius;			
			if (s.num>0){
				Vector2D lst = other.points[endIndx];
				if (other!=null && other.num>0){					
					for (int i =other.startIndex;i<=endIndx;++i){
						Vector2D n = other.points[i];
						int num = endIndx - i +1;
						if (num>1) circle(n, lst, center.x,center.y, other.radius,center);
						cx = center.x;
						cy = center.y;
						double nx = n.x-cx;
						double ny = n.y-cy;								
						double ovy = cy + ny*r;
						if (ovy<=point.y) continue;
						empty = false;						
						if (oy<=ovy){						
							if (s.num>1) 
								if (s.center.y!=0) circle(v, end, center.x,center.y, s.radius,s.center) ;
								else if (num>1){								
									if (s.center.y!=0) s.center  =center;
								} else {
									s.type = Segment.UNKNOWN;								
								}
							break;
						} else {	
							if (num>1 || s.center.y==0){
								if (s.center.y!=0){													
									s.center = center;	
								}
								cx = center.x;
								cy = center.y;							
								s.start = new Vector2D(cx+nx*r,cy+ny*r);
							}  else if (s.num>1){
								s.start = new Vector2D(cx+nx*r,cy+ny*r);								
							} else {															
								s.type = UNKNOWN; 								
							} 
							break;
						}
					}
				} else {					
					if (s.num>1){
						if (s.center.y!=0) circle(v, end, s.center.x,s.center.y, s.radius,s.center);
					} else {
						s.start = null;
						s.end = null;
						s.type = UNKNOWN;
					}
				}
			} else if (s.num==0){
				if (other!=null && other.num>0){					
					for (int i =other.startIndex;i<=endIndx;++i){
						Vector2D n = other.points[i];
						double nx = n.x-cx;
						double ny = n.y-cy;					
						double ovy = cy + ny*r;
						if (ovy<=point.y) continue;
						empty = false;
						int num = endIndx - i +1;						
						if (num>1 || s.center.y==0){
							if (s.center.y!=0) {
								Vector2D lst = other.points[endIndx];
								circle(n, lst, center.x,center.y, other.radius,center);
								s.center = center;
							}
							cx = center.x;
							cy = center.y;						
							s.start = new Vector2D(cx+nx*r,cy+ny*r);
						} else {
							s.start = null;
							s.end = null;
							s.type = UNKNOWN;
						}
						break;						
					}
				} else {
					s.start = null;
					s.end = null;
					s.type = UNKNOWN;
				}
			}//end of if			
		} else if (s.type==0){//s.type == 0
			double vx = s.start.x - other.start.x;
			double vy = s.start.y - other.start.y;			
			double sy = v.y;						
			if (s.num>0 && other!=null){			
				for (int i =other.startIndex;i<=endIndx;++i){
					Vector2D n = other.points[i];
					double nx = n.x+vx;
					double ny = n.y+vy;	
					if (ny<=point.y) continue;
					empty = false;
					int num = endIndx - i +1;
					if (ny<sy){	
						if (num>1 || s.num>1){ 
							if (Math.abs(s.start.x-s.end.x)>E)
								s.start.x = nx;																				
							s.start.y = ny;
						} else {							
							s.type = UNKNOWN;
						}
						break;
					}
				}
			} else if (s.num==0){
				if (other!=null && other.num>0){
					for (int i =other.startIndex;i<=endIndx;++i){
						Vector2D n = other.points[i];
						double nx = n.x+vx;
						double ny = n.y+vy;		
						if (ny<=point.y) continue;
						empty = false;
						int num = endIndx - i +1;						
						if (num>1) {
							if (Math.abs(s.start.x-s.end.x)>E)
								s.start.x = nx;																				
							s.start.y = ny;
						} else {
							s.start = null;
							s.end = null;
							s.type = UNKNOWN;
						}
						break;						
					}
				} else {
					s.start = null;
					s.end = null;
					s.type = UNKNOWN;
				}
			}//end of if
		}	
		if (empty){
			if (s.num>0) {				
				if (s.type!=0 && s.center!=null && s.center.y!=0) circle(v, s.end, s.center.x,s.center.y, s.radius,s.center);				
			} else if (s.num==0 || (s.type!=0 && s.center.y!=0)) {
				s.type = UNKNOWN;
				if (s.opp!=null) s.opp.type = Segment.UNKNOWN;
			}
		}
	}//*/


	private static void removeFirstPoint(Segment s,Segment other,double pointy){//remove the first point of s
		if (pointy<s.start.y-SMALL_MARGIN && (s.num==0 || s.points[s.startIndex].y>pointy)) return;		
		Vector2D[] vv = s.points;
		int fst = (s.num==0) ? s.startIndex : binarySearchFromTo(vv, pointy, 0, s.endIndex);
		if (s.num>0){
			if (fst<0) 
				fst = -fst-1;
			else fst++;
			if (fst>=0 && fst<s.endIndex && vv[fst].y<=pointy+SMALL_MARGIN) fst++;
		}

		s.startIndex = fst;			
		s.updated = true;
		s.num = s.endIndex-fst+1;
		if (s.num<0) {
			s.num = 0;
			s.endIndex = fst-1;
		}
		Vector2D v = (s.num>0) ? s.points[s.startIndex] : s.start;
		if (s.num>0 && v.y>s.end.y+SMALL_MARGIN){
			s.type = Segment.UNKNOWN;
			s.opp.type = Segment.UNKNOWN;
			return;
		}
		if (s.num>0 && s.type!=0) s.start.copy(v);

		if (other==null || other.num==0){			
			if (s.num>1) {
				s.start.copy(v);
				if (s.type!=0 && s.center!=null && s.center.y!=0) circle(v, s.end, s.center.x,s.center.y, s.radius,s.center);				
			} else if (s.num==0 || (s.type!=0 && s.center.y!=0)) {
				s.type = UNKNOWN;
				if (s.opp!=null) s.opp.type = Segment.UNKNOWN;
			}
			return;
		}

		Vector2D end = (s.num>0) ? s.points[s.endIndex] : s.end;
		int endIndx = other.endIndex;
		boolean empty = true;
		if (s.type!=0 && s.type!=UNKNOWN){
			Vector2D center = s.center;
			double cx = center.x;
			double cy = center.y;
			double oy = v.y;
			double r = s.radius/other.radius;			
			if (s.num>0){
				if (other!=null && other.num>0){
					Vector2D lst = other.points[endIndx];
					for (int i =other.startIndex;i<=endIndx;++i){
						Vector2D n = other.points[i];	
						int num = endIndx - i +1;
						if (num>1 && cy!=0){
							circle(n, lst, center.x,center.y, other.radius,center);
							cx = center.x;
							cy = center.y;
						}						
						double nx = n.x-cx;
						double ny = n.y-cy;								
						double ovy = cy + ny*r;
						if (ovy<=pointy) continue;
						empty = false;
						
						if (oy<=ovy){						
							if (s.num>1 && cy!=0){ 
								circle(v, end, cx,cy, s.radius,s.center);								
							} else if (num<=1 && cy!=0){																
								s.type =Segment.UNKNOWN;
//								s.start = null;
//								s.end = null;
							}
							break;
						} else if (ovy<oy){	
							if (num>1 || cy==0){																	
								s.start.copy(cx+nx*r,cy+ny*r);
							}  else if (s.num>1){
								s.start.copy(cx+nx*r,cy+ny*r);								
							} else {															
								s.type = UNKNOWN;
								if (s.opp!=null) s.opp.type = Segment.UNKNOWN;
							} 
							break;
						}
					}
				} else {					
					if (s.num>1){
						if (cy!=0) circle(v, end, s.center.x,s.center.y, s.radius,s.center);
					} else {						
						s.type = UNKNOWN;
						if (s.opp!=null) s.opp.type = Segment.UNKNOWN;
					}
				}
			} else if (s.num==0){
				if (other!=null && other.num>0){
					Vector2D lst = other.points[endIndx];
					for (int i =other.startIndex;i<=endIndx;++i){
						Vector2D n = other.points[i];
						int num = endIndx - i +1;
						if (num>1 && cy!=0) {
							circle(n, lst, center.x,center.y, other.radius,center);
							cx = center.x;
							cy = center.y;
						}						
						double nx = n.x-cx;
						double ny = n.y-cy;					
						double ovy = cy + ny*r;
						if (ovy<=pointy) continue;
						empty = false;
								
						if (num>1 || cy==0){							
							cx = center.x;
							cy = center.y;						
							s.start.copy(cx+nx*r,cy+ny*r);
						} else {
//							s.start = null;
//							s.end = null;
							s.type = UNKNOWN;
							if (s.opp!=null) s.opp.type = Segment.UNKNOWN;
						}
						break;						
					}
				} else {
//					s.start = null;
//					s.end = null;
					s.type = UNKNOWN;
					if (s.opp!=null) s.opp.type = Segment.UNKNOWN;
				}
			}//end of if			
		} else if (s.type==0){//s.type == 0
			double vx = s.start.x - other.start.x;
			double vy = s.start.y - other.start.y;			
			double sy = v.y;
			s.start.copy(v);
			if (s.num>0 && other!=null){			
				for (int i =other.startIndex;i<=endIndx;++i){
					Vector2D n = other.points[i];					
					double nx = n.x+vx;
					double ny = n.y+vy;	
					if (ny<=pointy) continue;
					empty = false;
					int num = endIndx - i +1;
					if (ny<sy){	
						if (num>1 || s.num>1){
							if (Math.abs(s.start.x-s.end.x)>E)
								s.start.x = nx;																				
							s.start.y = ny;
						} else {							
							s.type = UNKNOWN;
							if (s.opp!=null) s.opp.type = Segment.UNKNOWN;
						}
						break;
					}
				}
			} else if (s.num==0){
				if (other!=null && other.num>0){
					for (int i =other.startIndex;i<=endIndx;++i){
						Vector2D n = other.points[i];
						double nx = n.x+vx;
						double ny = n.y+vy;		
						if (ny<=pointy) continue;
						empty = false;
						int num = endIndx - i +1;					
						if (num>1){ 
							if (Math.abs(s.start.x-s.end.x)>E)
								s.start.x = nx;																				
							s.start.y = ny;
						} else {
//							s.start = null;
//							s.end = null;
							s.type = UNKNOWN;
						}
						break;

					}
				} else {
//					s.start = null;
//					s.end = null;
					s.type = UNKNOWN;
				}
			}//end of if
		}

		if (empty){
			if (s.num>0 && s.start.y<s.end.y-SMALL_MARGIN) {				
				if (s.type!=0 && s.center!=null && s.center.y!=0) circle(v, s.end, s.center.x,s.center.y, s.radius,s.center);				
			} else if (s.num==0 || (s.type!=0 && s.center.y!=0) || s.start.y>=s.end.y-SMALL_MARGIN) {
				s.type = UNKNOWN;
				if (s.opp!=null) s.opp.type = Segment.UNKNOWN;
			}
		}
	}

	/*private static void removeLastPoint(Segment s,Segment other){//remove the last point of s		
		if (s.num==0) return;			

		Vector2D[] vv = s.points;
		Vector2D point = (s.endIndex>=0) ? vv[s.endIndex] : null;
		s.updated = true;
		s.endIndex--;
		s.num--;		
		//		if (s.endIndex<0) {
		//			s.start = null;
		//			s.end = null;
		//			s.center = null;
		//			return;
		//		}
		Vector2D v = (s.num>0 && s.endIndex>=0) ? s.points[s.endIndex] : point;
		if (s.num>0 && v.y<=s.start.y){
			s.type = Segment.UNKNOWN;
			s.opp.type = Segment.UNKNOWN;
			return;
		}
		if (s.num>0 && v!=null) s.end = new Vector2D(v);

		if (other==null || other.num==0 || v==null){			
			if (s.num>0 && v!=null) {				
				if (s.type!=0 && s.center!=null && s.center.y!=0) 
					circle(s.start, v, s.center.x,s.center.y, s.radius,s.center);				
			} else if (s.num==0 || (s.type!=0 && s.center.y!=0)) s.type = UNKNOWN;
			return;
		}		
		Vector2D start = (s.num>0) ? s.points[s.startIndex] : s.start;
		int startIndx =other.startIndex;
		boolean empty = true;
		if (s.type!=0 && s.type!=UNKNOWN){			
			Vector2D center = s.center;
			double cx = center.x;
			double cy = center.y;								
			double r = s.radius/other.radius;
			double oy = v.y;

			if (s.num>0){
				Vector2D fst = other.points[startIndx];
				if (other!=null && other.num>0){
					for (int i =other.endIndex;i>=startIndx;--i){
						Vector2D n = other.points[i];
						int num = i - startIndx +1;
						if (num>1 && cy!=0){
							circle(fst, n, center.x,center.y, other.radius,center) ;
							cx = center.x;
							cy = center.y;
						}						
						double nx = n.x-cx;
						double ny = n.y-cy;								
						double ovy = cy + ny*r;
						if (ovy>=point.y) continue;
						empty = false;						
						if (oy>=ovy){							
							if (cy!=0) {
								if (s.num>1) {
									circle(start, v, cx,cy, s.radius,s.center) ;								
								} else if (num<=1){
									s.type =Segment.UNKNOWN;
									s.start = null;
									s.end = null;
								}
							}
							break;
						} else if (oy<ovy){
							if (num>1 || cy==0){								
								cx = center.x;
								cy = center.y;							
								s.end = new Vector2D(cx+nx*r,cy+ny*r);
							} else if (s.num>1){
								s.end = new Vector2D(cx+nx*r,cy+ny*r);								
							} else {
								s.type =Segment.UNKNOWN;
								s.start = null;
								s.end = null;							}
							break;
						} 

					}
				} else {					
					if (s.num>1 && s.center!=null && cy!=0) {
						circle(start, s.end, s.center.x,s.center.y, s.radius,s.center);					
					} else {
						s.start = new Vector2D(start);
						s.type = UNKNOWN; 
					}
				}
			} else if (s.num==0){
				Vector2D fst = other.points[startIndx];
				if (other!=null && other.num>0){
					for (int i =other.endIndex;i>=startIndx;--i){
						Vector2D n = other.points[i];
						int num = i - startIndx +1;
						if (num>1 && cy!=0){
							circle(fst, n, center.x,center.y, other.radius,center) ;
							cx = center.x;
							cy = center.y;
						}						
						double nx = n.x-cx;
						double ny = n.y-cy;					
						double ovy = cy + ny*r;
						if (ovy>=point.y) continue;
						empty = false;																					
						if (num>1 || cy==0){							
							cx = center.x;
							cy = center.y;							
							s.end = new Vector2D(cx+nx*r,cy+ny*r);
						} else {
							s.start = null;
							s.end = null;
							s.type = UNKNOWN;
						}
						break;					
					}
				} else {					
					s.start = null;
					s.end = null;
					s.type = UNKNOWN;					
				}
			}//end of if			
		} else if (s.type==0){//s.type == 0		
			double vx = s.end.x - other.end.x;
			double vy = s.end.y - other.end.y;			
			double sy = v.y;									
			if (s.num>0 && other!=null && other.points!=null){			
				for (int i =other.endIndex;i>=startIndx;--i){
					Vector2D n = other.points[i];
					double nx = n.x+vx;
					double ny = n.y+vy;
					if (ny>=point.y) continue;
					empty = false;
					int num = i - startIndx +1;
					if (ny>sy){										
						if (num>1 || s.num>1) {
							if (Math.abs(s.start.x-s.end.x)>E)
								s.end.x = nx;																				
							s.end.y = ny;
						} else {						
							s.type = UNKNOWN;
						}
						break;
					}
				}
			} else if (s.num==0 && other!=null && other.num>0 && other.points!=null){
				for (int i =other.endIndex;i>=startIndx+1;--i){
					Vector2D n = other.points[i];
					double nx = n.x+vx;
					double ny = n.y+vy;
					if (ny>=point.y) continue;
					empty = false;
					int num = i - startIndx +1;																					
					if (num>1) {
						if (Math.abs(s.start.x-s.end.x)>E)
							s.end.x = nx;																				
						s.end.y = ny;
					} else {
						s.start = null;
						s.end = null;
						s.type = UNKNOWN;
					}
					break;					
				}
			} else if (other==null || other.num<2){
				//end of if
				s.start = null;
				s.end = null;
				s.type = UNKNOWN;
			}
		}

		if (empty){
			if (s.num>0) {				
				if (s.type!=0 && s.center!=null && s.center.y!=0) circle(s.start, v, s.center.x,s.center.y, s.radius,s.center);				
			} else if (s.num==0 || (s.type!=0 && s.center.y!=0)) {
				s.type = UNKNOWN;
				if (s.opp!=null) s.opp.type = Segment.UNKNOWN;
			}
		}
	}//*/


	private static void removeLastPoint(Segment s,Segment other,double pointy){//remove the last point of s
		if (s.type==Segment.UNKNOWN || pointy>s.end.y+SMALL_MARGIN && (s.num==0 || pointy>s.points[s.endIndex].y)) return;					
		Vector2D[] vv = s.points;
		int j = (s.num==0 || s.endIndex<=0) ? s.startIndex : binarySearchFromTo(vv, pointy, 0, s.endIndex);
		j = (j<0) ? -j-1 : j;

		//		if (j<=s.endIndex && vv[j].y<=point.y) j++;
		s.updated = true;
		s.endIndex = j-1;
		s.num = j-s.startIndex;
		//		if (s.endIndex<0) {
		//			s.start = null;
		//			s.end = null;
		//			s.center = null;
		//			return;
		//		}
		Vector2D v = (s.num>0) ? s.points[s.endIndex] : s.end;
		if (s.num>0 && v.y<s.start.y-SMALL_MARGIN){
			s.type = Segment.UNKNOWN;
			s.opp.type = Segment.UNKNOWN;
			return;
		}
		if (s.num>0 && s.type!=0) s.end.copy(v);				

		if (other==null || other.num==0){			
			if (s.num>1) {
				s.end.copy(v);
				if (s.type!=0 && s.center!=null && s.center.y!=0) 
					circle(s.start, v, s.center.x,s.center.y, s.radius,s.center);				
			} else if (s.num==0 || (s.type!=0 && s.center.y!=0)) s.type = UNKNOWN;
			return;
		}	
		boolean empty = true;
		Vector2D start = (s.num>0) ? s.points[s.startIndex] : s.start;
		int startIndx = other.startIndex;
		if (s.type!=0 && s.type!=UNKNOWN){			
			Vector2D center = s.center;
			double cx = center.x;
			double cy = center.y;								
			double r = s.radius/other.radius;
			double oy = v.y;

			if (s.num>0){				
				if (other!=null && other.num>0){
					Vector2D fst = other.points[startIndx];
					for (int i =other.endIndex;i>=startIndx;--i){
						Vector2D n = other.points[i];
						int num = i - startIndx +1;
						if (num>1 && cy!=0) {
							circle(fst, n, center.x,center.y, other.radius,center) ;
							cx = center.x;
							cy = center.y;
						}						
						double nx = n.x-cx;
						double ny = n.y-cy;								
						double ovy = cy + ny*r;
						if (ovy>=pointy) continue;
						empty = false;						
						if (oy>=ovy){							
							if (s.center.y!=0) {
								if (s.num>1 && cy!=0) 
									circle(start, v, center.x,center.y, s.radius,s.center) ;
								else if (num>1 || cy==0){								
									if (s.center.y!=0) s.center = center;
								} else {
									s.type =Segment.UNKNOWN;
//									s.start = null;
//									s.end = null;
								}
							}
							break;
						} else if (oy<ovy){
							if (num>1 || cy==0){								
								cx = center.x;
								cy = center.y;							
								s.end.copy(cx+nx*r,cy+ny*r);
							} else if (s.num>1){
								s.end.copy(cx+nx*r,cy+ny*r);								
							} else {
								s.type =Segment.UNKNOWN;
//								s.start = null;
//								s.end = null;
							}
							break;
						} 

					}
				} else {					
					if (s.num>1 && s.center!=null && cy!=0) {
						circle(start, s.end, s.center.x,s.center.y, s.radius,s.center);					
					} else {
						s.type =Segment.UNKNOWN;
//						s.start = null;
//						s.end = null; 
					}
				}
			} else if (s.num==0){
				if (other!=null && other.num>0 && other.points!=null){
					Vector2D fst = other.points[startIndx];
					for (int i =other.endIndex;i>=startIndx;--i){
						Vector2D n = other.points[i];
						int num = i - startIndx +1;
						if (num>1 && cy!=0){
							circle(fst, n, center.x,center.y, other.radius,center) ;
							cx = center.x;
							cy = center.y;
						}						
						double nx = n.x-cx;
						double ny = n.y-cy;					
						double ovy = cy + ny*r;
						if (ovy>=pointy) continue;
						empty = false;											
						if (ovy<oy || num==0){								
							if (num>1){								
								cx = center.x;
								cy = center.y;							
								s.end.copy(cx+nx*r,cy+ny*r);
							} else {
//								s.start = null;
//								s.end = null;
								s.type = UNKNOWN;
							}
							break;
						}
					}
				} else {					
//					s.start = null;
//					s.end = null;
					s.type = UNKNOWN;					
				}
			}//end of if			
		} else if (s.type==0){//s.type == 0		
			double vx = s.end.x - other.end.x;
			double vy = s.end.y - other.end.y;			
			double sy = v.y;
			s.end.copy(v);
			if (s.num>0 && other!=null && other.points!=null){			
				for (int i =other.endIndex;i>=startIndx;--i){
					Vector2D n = other.points[i];
					double nx = n.x+vx;
					double ny = n.y+vy;
					if (ny>=pointy) continue;
					empty = false;
					int num = i - startIndx +1;
					if (ny>sy){										
						if (num>1 || s.num>1){ 
							if (Math.abs(s.start.x-s.end.x)>E)
								s.end.x = nx;																				
							s.end.y = ny;														
						} else {						
							s.type = UNKNOWN;
						}
						break;
					}
				}
			} else if (s.num==0 && other!=null && other.num>0 && other.points!=null){
				for (int i =other.endIndex;i>=startIndx+1;--i){
					Vector2D n = other.points[i];
					double nx = n.x+vx;
					double ny = n.y+vy;
					if (ny>=pointy) continue;
					empty = false;
					int num = i - startIndx +1;
					if (ny<sy || num==0){																		
						if (num>1){ 
							if (Math.abs(s.start.x-s.end.x)>E)
								s.end.x = nx;																				
							s.end.y = ny;
						} else {
//							s.start = null;
//							s.end = null;
							s.type = UNKNOWN;
						}
						break;
					}
				}
			} else if (other==null || other.num<2){
				//end of if
//				s.start = null;
//				s.end = null;
				s.type = UNKNOWN;
			}
		}

		if (empty){
			if (s.num>0 && s.start.y<s.end.y-SMALL_MARGIN) {				
				if (s.type!=0 && s.center!=null && s.center.y!=0) circle(s.start, v, s.center.x,s.center.y, s.radius,s.center);				
			} else if (s.num==0 || (s.type!=0 && s.center!=null && s.center.y!=0) || s.start.y>=s.end.y-SMALL_MARGIN) {
				s.type = UNKNOWN;
				if (s.opp!=null) s.opp.type = Segment.UNKNOWN;
			}
		}
	}


	/*private static final void reMerge(final Vector2D[] v,int from, int to,IntArrayList starts,IntArrayList ends,ObjectArrayList<Segment> ss){
		int sz = ss.size();
		Segment[] ssArr = ss.elements();
		int[] startArr = starts.elements();
		int[] endArr = ends.elements();
		for (int i=0;i<sz;++i){
			Segment s = ssArr[i];
			if (!s.updated || s.type!=Segment.UNKNOWN) continue;
			Segment next = (i<sz-1) ? ssArr[i+1] : null;
			if (next==null || next.type!=Segment.UNKNOWN) continue;
			s.updated = true;
			if (next!=null){
				do{
					endArr[i] =  endArr[i+1];
					s.end = next.end;
					s.num += next.num;
					if ((sz-=i+2)>0){
						System.arraycopy(ssArr, i+2, ssArr, i+1, sz);
						System.arraycopy(startArr, i+2, startArr, i+1, sz);
						System.arraycopy(endArr, i+2, endArr, i+1, sz);
					}
					sz+=i+1;					
					next = (i<sz-1) ? ssArr[i+1] : null;
				} while (next!=null && next.type==Segment.UNKNOWN);
				//				if (next!=null && next.type!=Segment.UNKNOWN) i++;
			}
		}
		ss.size(sz);
		starts.size(sz);
		ends.size(sz);
	}//*/

	/*private static final int binarySearchFromTo(Segment[] list, double key, int from, int to) {			
		Segment val = list[from];
		int cmp = (key<=val.end.y+SMALL_MARGIN && key>=val.start.y-SMALL_MARGIN) ? 0 : (key>val.end.y) ? -1 : 1; 
		if (cmp==0) 
			return from;
		else if (cmp>0) return -from-1;
		val = list[to];
		cmp = (key<=val.end.y+SMALL_MARGIN && key>=val.start.y-SMALL_MARGIN) ? 0 : (key>val.end.y) ? -1 : 1;
		if (cmp==0) 
			return to;
		else if (cmp<0) return -(to+2);
		while (from <= to) {
			int mid =(from + to)/2;
			val = list[mid];
			cmp = (key<=val.end.y+SMALL_MARGIN && key>=val.start.y-SMALL_MARGIN) ? 0 : (key>val.end.y) ? -1 : 1;

			if (cmp < 0) from = mid + 1;
			else if (cmp > 0) to = mid - 1;
			else return mid; // key found
		}
		return -(from + 1);  // key not found.
	}//*/

	/*private static int expand(final Vector2D[] v,int st,int en,Segment prev,int kp,Segment s ,int j,Segment next,int kn,ObjectArrayList<Segment> ss,IntArrayList starts,IntArrayList ends,double tW,boolean firstBreak,ObjectArrayList<Segment> other){
		int sz = ss.size();
		Segment[] ssArr = ss.elements();
		int[] startArr = starts.elements();
		int[] endArr = ends.elements();

		double rad = s.radius;
		double oldRad = rad;								
		Vector2D point = new Vector2D();
		Vector2D nPoint = new Vector2D();
		boolean good = true;

		boolean isConfirm = (s.type==0 && Math.abs(s.start.x-s.end.x)<TrackSegment.EPSILON) ;
		int sR = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius-s.type*tW);		
		isConfirm = isConfirm || ((s.map!=null && s.map[sR]>3) || (s.type!=0 && s.center!=null && s.center.y==0));
		boolean retest = false;

		Segment[] oalArr = new Segment[30];
		ObjectArrayList<Segment> oal = ObjectArrayList.wrap(oalArr,0);
		Segment bkupS = new Segment();
		Segment bkupNext = (next==null) ? null : new Segment();
		Segment bkupPrev = (prev==null) ? null : new Segment();

		Segment os = (s==null) ? null : s.opp;
		Segment op = (prev==null) ? null : prev.opp; 
		Segment on = (next==null) ? null : next.opp;		

		//		int which = (tW<0) ? 1 : -1;
		Vector2D[] opoints = (op!=null) ? op.points : (os!=null) ? os.points : (on!=null) ? on.points : (tW<0) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;
		int otherTo = (on!=null) ? on.endIndex+1 : (tW<0) ? CircleDriver2.edgeDetector.lSize:  CircleDriver2.edgeDetector.rSize;
		if (op==null && prev!=null && prev.type!=UNKNOWN){
			op = new Segment();
			op.points = otherPoints;
			reSynchronize(prev, op, 0, otherTo, 1, tW);
			prev.opp = op;
		}

		if (on==null && next!=null && next.type!=UNKNOWN){
			on = new Segment();
			on.points = otherPoints;
			reSynchronize(next, on, 0, otherTo, 1, tW);
			next.opp = on;
		}

		for (int k = en;k>=st;--k){
			if (!good && firstBreak) break;
			int begin = s.startIndex;
			int end = s.endIndex;
			Vector2D  first = (s.num==0) ? s.start : v[begin];
			Vector2D last = (s.num==0) ? s.end : v[end];
			bkupS.copy(s);
			if (next!=null) bkupNext.copy(next);
			if (prev!=null) bkupPrev.copy(prev);
			int bksI = startArr[j];
			int bkeI = endArr[j];		
			int bkPs = (j>0) ? startArr[j-1] : 0;
			int bkPe = (j>0) ? endArr[j-1] : 0;
			int bkNs = (j<sz-1) ? startArr[j+1] : 0;
			int bkNe = (j<sz-1) ? endArr[j+1] : 0;

			Vector2D vv = v[k];		
			retest = false;
			int sIndx = (firstBreak) ? k : begin;
			int eIndx = (firstBreak) ? end : k;		
			boolean deletePrev = false;
			boolean deleteNext = false;

			if (!isConfirm && s.num>0 && s.num<6) {				
				oal.size(0);
				if (firstBreak && prev!=null && kp==j-1){
					removeLastPoint(prev, op, null);							
					deletePrev = true;
					if (prev.type!=Segment.UNKNOWN && prev.num+op.num<3 && (prev.lower!=null || prev.upper!=null || isConfirmed(prev, 1, tW))){
						prev.copy(bkupPrev);								
						continue;
					}							
				} else if (!firstBreak && next!=null && kn==j+1){
					removeFirstPoint(next, on, null);							
					deleteNext = true;
					if (next.type!=Segment.UNKNOWN && next.num+on.num<3 && (next.lower!=null || next.upper!=null || isConfirmed(next, 1, tW))){
						next.copy(bkupNext);								
						continue;
					}
				}
				ObjectArrayList<Segment> ol =  bestGuess(v, sIndx, eIndx+1, tW, prev, next, oal);
				int osz = (ol==null) ? 0 : ol.size();
				if (ol!=null && osz>1){
					Segment tmp = oalArr[0];
					if (!firstBreak && tmp.type==UNKNOWN){
						int ii = 0;
						while (tmp.type==UNKNOWN && ii<osz)
							tmp = oalArr[++ii];
						if (ii==osz) tmp = null;
						if (tmp!=null && tmp.lower!=null){
							s.type = UNKNOWN;
							s.map = null;
							return sz;
						}
					}

				} else if (ol!=null && osz==1) {					
					Segment tmp = oalArr[0];
					Vector2D pPoint = new Vector2D();										
					if (tmp.type==s.type && tmp.radius==s.radius)
						retest = true;
					else if (tmp.size<=2 && s.lower!=null && s.upper!=null){
						if (prev!=null && deletePrev) prev.copy(bkupPrev);							
						if (next!=null && deleteNext) next.copy(bkupNext);	
						break;
					} else if (prev!=null && tmp.type!=UNKNOWN && isConnected(prev, tmp, tW, pPoint)) {
						Segment.apply(s, tW, s.start, s.end, tmp.center, tmp.radius);
						if (s.radius==tmp.radius) {
							s.lower = pPoint;							
							rad = s.radius;
							prev.upper = new Vector2D(pPoint);
						}
						retest = true;
					} else if (next!=null && tmp.type!=UNKNOWN  && isConnected(tmp, next, tW, pPoint)) {
						Segment.apply(s, tW, s.start, s.end, tmp.center, tmp.radius);
						if (s.radius==tmp.radius) {
							s.upper = pPoint;							
							rad = s.radius;
							next.lower = new Vector2D(pPoint);
						}
						retest = true;
					}

				}
			}
			if (retest || s.isPointBelongToSeg(vv)){				
				Vector2D center = (s.type==0) ? null : s.center;
				good = false;
				if (center!=null) {					
					center = (firstBreak) ? circle(vv, last, center, rad) : circle(first, vv, center, rad);					
					if (check(v, sIndx, eIndx+1, center, rad)<0) {
						if (prev!=null && deletePrev) prev.copy(bkupPrev);												
						if (next!=null && deleteNext) next.copy(bkupNext);							
						continue;
					}
					double tx = center.x;
					double ty = center.y;
					if (rad==oldRad){
						double d = center.y-s.center.y;					
						if (d>EPSILON*10 || d<-EPSILON*10 || (s.center.y==0 && (ty>SMALL_MARGIN || ty<-SMALL_MARGIN))) {
							if (prev!=null && deletePrev) prev.copy(bkupPrev);					
							if (next!=null && deleteNext) next.copy(bkupNext);								
							continue;
						}
					}

					if (ty<SMALL_MARGIN && ty>-SMALL_MARGIN) {						
						tx = (tx<0) ? -Math.sqrt(tx*tx+ty*ty) : Math.sqrt(tx*tx+ty*ty);
						ty = 0;						
						center.x = tx;
						center.y = ty;
					}//first segment
					s.center = center;

				}
				//				s.end = new Vector2D(vv);
				//				s.num = k+1-begin;
				if (s.num>=2){					
					if (!s.reCalculate(v, sIndx, eIndx+1, tW)) {
						if (prev!=null && deletePrev) prev.copy(bkupPrev);
						if (next!=null && deleteNext) next.copy(bkupNext);							
						continue;																
					}
					if (s.type!=0 && rad!=s.radius && s.type!=UNKNOWN){
						boolean nok = (prev!=null && prev.type!=UNKNOWN && prev.type!=0 && prev.center.y>s.center.y && prev.radius>s.radius);
						nok = (nok || (next!=null && next.type!=UNKNOWN && next.type!=0 && next.center.y<s.center.y && s.radius>next.radius));
						if (nok){
							s.copy(bkupS);								
							if (prev!=null && deletePrev) prev.copy(bkupPrev);														
							if (next!=null && deleteNext) next.copy(bkupNext);																					
							startArr[j] = bksI;
							endArr[j] = bkeI;
							if (j>0){
								startArr[j-1] = bkPs;
								endArr[j-1] = bkPe;
							}
							if (j<sz-1){
								startArr[j+1] = bkNs;
								endArr[j+1] = bkNe;
							}
							continue;
						}
						rad = s.radius;
					}
				} else {					
					if (s.start.y>vv.y){
						s.start = new Vector2D(vv);
						s.startIndex = k;
					}

					if (s.end.y<vv.y){
						s.end = new Vector2D(vv);
						s.endIndex = k;
					}

					s.num++;	
				}
				s.updated = true;


				if (!retest){
					if (!deletePrev && firstBreak && prev!=null && kp==j-1){
						removeLastPoint(prev, op, null);
						deletePrev = true;
					} else if (!deleteNext && !firstBreak && next!=null && kn==j+1){
						removeFirstPoint(next, on, null);
						deleteNext = true;
					}
				}
				boolean nIsConn = false;

				if (next!=null && next.type!=UNKNOWN){
					nIsConn = isConnected(s, next, tW, nPoint);
					if (!nIsConn && s.upper!=null){
						s.copy(bkupS);												
						if (prev!=null && deletePrev) prev.copy(bkupPrev);							
						if (next!=null && deleteNext) next.copy(bkupNext);																	
						startArr[j] = bksI;
						endArr[j] = bkeI;		
						if (j>0){
							startArr[j-1] = bkPs;
							endArr[j-1] = bkPe;
						}
						if (j<sz-1){
							startArr[j+1] = bkNs;
							endArr[j+1] = bkNe;
						}
						continue;
					} else if (nIsConn){
						s.upper = new Vector2D(nPoint.x,nPoint.y);
						next.lower = nPoint;
						s.updated = true;						
					}
				}

				good = true;
				boolean pIsConn = false;
				if (prev!=null && prev.type!=UNKNOWN){
					pIsConn = isConnected(prev, s, tW, point);
					if (!pIsConn && s.lower!=null){
						s.copy(bkupS);										
						if (prev!=null && deletePrev) prev.copy(bkupPrev);							
						if (next!=null && deleteNext) next.copy(bkupNext);													
						startArr[j] = bksI;
						endArr[j] = bkeI;
						if (j>0){
							startArr[j-1] = bkPs;
							endArr[j-1] = bkPe;
						}
						if (j<sz-1){
							startArr[j+1] = bkNs;
							endArr[j+1] = bkNe;
						}
						good = false;
						continue;
					}  					
					if (!pIsConn && isPossiblyConnected && (isPrevNextConnected || isNextPrevConnected)){
						Segment tmp = new Segment();						
						double radius = s.radius;						
						radiusFrom2Points(prev, v[sIndx], v[eIndx], tW, tmp);
						if (radius!=tmp.radius){
							if (prev!=null && (pIsConn = isConnected(prev, tmp, tW, point))) {
								Segment.apply(s, tW, tmp.start, tmp.end, tmp.center, tmp.radius);
								s.lower = point;
								s.upper = null;
								if (next!=null) next.lower = null;
								rad = s.radius;
								prev.upper = new Vector2D(point);
							} else if (next!=null && isConnected(tmp, next, tW, point)) {
								Segment.apply(s, tW, tmp.start, tmp.end, tmp.center, tmp.radius);										
								s.upper = point;
								s.lower = null;
								if (prev!=null) prev.upper = null;
								rad = s.radius;
								next.lower = new Vector2D(point);																				
							}
						}
						if (!pIsConn){							
							radiusFrom2Points(s, prev.start, prev.end, tW, prev);
						}

					}

					if (pIsConn){
						//Remerge with prev here. Copy and paste from above. Ugly
						s.updated = true;
						startArr[j] = s.startIndex = sIndx;
						endArr[j] = s.endIndex = eIndx;
						int oldnum = s.num;
						if (prev!=null && deletePrev)
							reSynchronize(prev,op,0,otherTo,1,tW*2);						
						reSynchronize(s,s.opp,0,otherTo,1,tW*2);	
						calibrateConnected(prev, kp, s, j, v, tW, startArr, endArr, point);
						if (prev!=null && prev.updated && prev.type!=UNKNOWN){						
							if (prev.opp==null) {
								prev.opp = new Segment();
								prev.opp.points = os.points;
							}
							reSynchronize(prev,prev.opp,0,otherTo,1,tW*2);														
						}
						if (s.updated && s.type!=UNKNOWN){						
							if (s.opp==null) {
								s.opp = new Segment();
								s.opp.points = opoints;
							}
							reSynchronize(s,s.opp,0,otherTo,1,tW*2);	
							s.updated = false;
						}
						if (oldnum!=s.num){
							good = false;
							if (next!=null){
								nIsConn = isConnected(s, next, tW, nPoint);
								if (!nIsConn && s.upper!=null){
									s.copy(bkupS);		
									reSynchronize(s, os, 0, otherTo, 1, 2*tW);
									if (prev!=null && deletePrev) {
										prev.copy(bkupPrev);
										reSynchronize(prev, op, op.startIndex, otherTo, 1, 2*tW);
									}
									if (next!=null && deleteNext) next.copy(bkupNext);									
									startArr[j] = bksI;
									endArr[j] = bkeI;		
									if (j>0){
										startArr[j-1] = bkPs;
										endArr[j-1] = bkPe;
									}
									if (j<sz-1){
										startArr[j+1] = bkNs;
										endArr[j+1] = bkNe;
									}
									continue;
								} else if (nIsConn){
									s.upper = new Vector2D(nPoint.x,nPoint.y);
									next.lower = nPoint;
									s.updated = true;						
								}
							}							
						}
						int sI = prev.endIndex+1;
						int eI = s.startIndex-1;
						if (sI>eI && kp<j-1){					
							int idx = kp+1;
							int size = j-idx;
							if ((sz-=j)>0){
								System.arraycopy(ssArr, j, ssArr, idx, size);
								System.arraycopy(startArr, j, startArr, idx, size);
								System.arraycopy(endArr, j, endArr, idx, size);
							}
							sz += j-size;						
							kn -= j-idx;
							j = idx;
						} else if (sI<=eI){
							if (kp<j-2){
								int idx = kp+2;
								int size = j-idx;
								if ((sz-=j)>0){
									System.arraycopy(ssArr, j, ssArr, idx, size);
									System.arraycopy(startArr, j, startArr, idx, size);
									System.arraycopy(endArr, j, endArr, idx, size);
								}
								sz += j-size;						
								kn -= j-idx;
								j = idx;
							}

							if (j==kp+1){
								Segment ns = new Segment();							
								ns.start = new Vector2D(v[sI]);
								ns.end = new Vector2D(v[eI]);
								ns.num = eI-sI+1;
								int idx = kp+1;
								if ((sz-=idx)>0){
									System.arraycopy(ssArr, idx, ssArr, idx+1, sz);
									System.arraycopy(startArr, idx, startArr, idx+1, sz);
									System.arraycopy(endArr, idx, endArr, idx+1, sz);
								}
								sz+= idx+1;
								ssArr[idx] = ns;
								startArr[idx] = ns.startIndex = sI;
								endArr[idx] = ns.endIndex = eI;							
								j++;
								kn++;
							} else {
								int idx = kp+1;
								Segment ns = ssArr[idx];
								ns.start = new Vector2D(v[sI]);
								ns.end = new Vector2D(v[eI]);
								ns.num = eI-sI+1;
								startArr[idx] = ns.startIndex = sI;
								endArr[idx] = ns.endIndex = eI;
							}
							//End of paste
						}
					}
					begin = startArr[j];									
				}//end of if prev


				if (!firstBreak){
					Segment n = ssArr[j+1];
					if (k==en){
						int idx = j+1;
						if ((sz-=idx+1)>0){
							System.arraycopy(ssArr, idx+1, ssArr, idx, sz);
							System.arraycopy(startArr, idx+1, startArr, idx, sz);
							System.arraycopy(endArr, idx+1, endArr, idx, sz);
						}
						kn--;
						sz+=idx;					
					} else {
						n.start = new Vector2D(v[k+1]);
						if (n.type!=0 && n.type!=UNKNOWN){
							Vector2D e = (en>k+2) ? v[en] : n.end;
							n.center = circle(n.start,e,n.center,n.radius);
						}
						n.num = en-k; 
						startArr[j+1] = n.startIndex = k+1;
					}
					startArr[j] = s.startIndex = sIndx;
					endArr[j] = s.endIndex = eIndx;	
				} else {
					Segment n = ssArr[j-1];
					int sI = startArr[j-1];
					if ((n.type==UNKNOWN && k==sI) || k==0){
						int idx = j-1;
						if ((sz-=idx+1)>0){
							System.arraycopy(ssArr, idx+1, ssArr, idx, sz);
							System.arraycopy(startArr, idx+1, startArr, idx, sz);
							System.arraycopy(endArr, idx+1, endArr, idx, sz);
						}
						j--;
						sz+=idx;					
					} else {
						if (n.end.y<v[k-1].y) {
							n.end = new Vector2D(v[k-1]);
							if (n.type!=0 && n.type!=UNKNOWN){
								Vector2D strt = (k>sI+2) ? v[sI] : n.start;
								n.center = circle(strt,n.end,n.center,n.radius);
							}
						}
						endArr[j-1] = n.endIndex = k-1;
						n.num = k-sI; 
					}
					startArr[j] = s.startIndex = sIndx;
					endArr[j] = s.endIndex = eIndx;					
				}

				if (s.updated && s.type!=UNKNOWN){						
					if (s.opp==null) {
						s.opp = new Segment();
						s.opp.points = opoints;
					}
					reSynchronize(s,s.opp,0,otherTo,1,tW*2);														
				}
				if (deleteNext) reSynchronize(next,on,0,otherTo,1,tW*2);								
				if (deletePrev && prev!=null && prev.upper!=null && !pIsConn) reSynchronize(prev,op,0,otherTo,1,tW*2);
				if (!firstBreak || pIsConn) break;				
			} else {
				good = false;//end of if ss.isBelongPoint(vv)
				if (deletePrev || deleteNext){
					if (deletePrev && firstBreak && prev!=null && kp==j-1){
						prev.copy(bkupPrev);						
					} else if (deleteNext && !firstBreak && next!=null && kn==j+1){
						next.copy(bkupNext);						
					}
				}
			}
		}//end of for

		starts.size(sz);
		ends.size(sz);
		ss.size(sz);
		return sz;

	}//*/

	/*private static int expandFw(final Vector2D[] v,int st,int en,Segment prev,int kp,Segment s ,int j,Segment next,int kn,ObjectArrayList<Segment> ss,IntArrayList starts,IntArrayList ends,double tW,boolean firstBreak,ObjectArrayList<Segment> other){
		int sz = ss.size();
		Segment[] ssArr = ss.elements();
		int[] startArr = starts.elements();
		int[] endArr = ends.elements();

		double rad = s.radius;
		double oldRad = rad;								
		Vector2D point = new Vector2D();
		Vector2D nPoint = new Vector2D();
		boolean good = true;

		boolean isConfirm = (s.type==0 && Math.abs(s.start.x-s.end.x)<TrackSegment.EPSILON) ;
		int sR = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius-s.type*tW);		
		isConfirm = isConfirm || ((s.map!=null && s.map[sR]>3) || (s.type!=0 && s.center!=null && s.center.y==0));
		boolean retest = false;

		Segment[] oalArr = new Segment[30];
		ObjectArrayList<Segment> oal = ObjectArrayList.wrap(oalArr,0);
		Segment bkupS = new Segment();
		Segment bkupNext = (next==null) ? null : new Segment();
		Segment bkupPrev = (prev==null) ? null : new Segment();

		int oz = other.size();
		Segment os = (s==null) ? null : s.opp;
		Segment op = (prev==null) ? null : prev.opp; 
		Segment on = (next==null) ? null : next.opp;
		//		int which = (tW<0) ? 1 : -1;
		Vector2D[] opoints =  (tW<0) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;
		int otherTo = (on!=null) ? on.endIndex+1 : (tW<0) ? CircleDriver2.edgeDetector.lSize:  CircleDriver2.edgeDetector.rSize;
		if (op==null && prev!=null && prev.type!=UNKNOWN){
			op = new Segment();
			op.points = otherPoints;
			reSynchronize(prev, op, 0, otherTo, 1, tW);
			prev.opp = op;
		}

		if (on==null && next!=null && next.type!=UNKNOWN){
			on = new Segment();
			on.points = otherPoints;
			reSynchronize(next, on, 0, otherTo, 1, tW);
			next.opp = on;
		}

		for (int k = st;k<=en;++k){
			if (!good && firstBreak) break;
			int begin = s.startIndex;
			int end = s.endIndex;
			Vector2D  first = (s.num==0) ? s.start : v[begin];
			Vector2D last = (s.num==0) ? s.end : v[end];
			boolean deletePrev = false;
			boolean deleteNext = false;
			bkupS.copy(s);			
			if (next!=null) bkupNext.copy(next);
			if (prev!=null) bkupPrev.copy(prev);
			int bksI = startArr[j];
			int bkeI = endArr[j];		
			int bkPs = (j>0) ? startArr[j-1] : 0;
			int bkPe = (j>0) ? endArr[j-1] : 0;
			int bkNs = (j<sz-1) ? startArr[j+1] : 0;
			int bkNe = (j<sz-1) ? endArr[j+1] : 0;

			Vector2D vv = v[k];		
			retest = false;
			int sIndx = (firstBreak) ? begin : k;
			int eIndx = (firstBreak) ? k : end;		

			if (!isConfirm && s.num>0 && s.num<6) {				
				oal.size(0);
				if (!firstBreak && prev!=null && kp==j-1){
					removeLastPoint(prev, op, null);
					deletePrev = true;
					if (prev.type!=Segment.UNKNOWN && prev.num+op.num<3 && (prev.lower!=null || prev.upper!=null || isConfirmed(prev, 1, tW))){
						prev.copy(bkupPrev);								
						continue;
					}
				} else if (firstBreak && next!=null && kn==j+1){
					deleteNext = true;
					removeFirstPoint(next, on, null);
					if (next.type!=Segment.UNKNOWN && next.num+on.num<3 && (next.lower!=null || next.upper!=null || isConfirmed(next, 1, tW))){
						next.copy(bkupNext);								
						continue;
					}
				}
				ObjectArrayList<Segment> ol =  bestGuess(v, sIndx, eIndx+1, tW, prev, next, oal);
				int osz = (ol==null) ? 0 : ol.size();
				if (ol!=null && osz>1){
					Segment tmp = oalArr[0];
					if (!firstBreak && tmp.type==UNKNOWN){
						int ii = 0;
						while (tmp.type==UNKNOWN && ii<osz)
							tmp = oalArr[++ii];
						if (ii==osz) tmp = null;
						if (tmp!=null && tmp.lower!=null){
							s.type = UNKNOWN;
							s.map = null;
							return sz;
						}
					}

				} else if (ol!=null && osz==1) {					
					Segment tmp = oalArr[0];
					Vector2D pPoint = new Vector2D();					
					if (tmp.type==s.type && tmp.radius==s.radius)
						retest = true;
					else if (tmp.size<=2 && s.lower!=null && s.upper!=null){
						if (prev!=null && deletePrev) prev.copy(bkupPrev);							
						if (next!=null && deleteNext) next.copy(bkupNext);	
						break;
					} else if (prev!=null && tmp.type!=UNKNOWN && isConnected(prev, tmp, tW, pPoint)) {
						Segment.apply(s, tW, s.start, s.end, tmp.center, tmp.radius);
						if (s.radius==tmp.radius) {
							s.lower = pPoint;							
							rad = s.radius;
							prev.upper = new Vector2D(pPoint);
						}
						retest = true;
					} else if (next!=null && tmp.type!=UNKNOWN  && isConnected(tmp, next, tW, pPoint)) {
						Segment.apply(s, tW, s.start, s.end, tmp.center, tmp.radius);
						if (s.radius==tmp.radius) {
							s.upper = pPoint;							
							rad = s.radius;
							next.lower = new Vector2D(pPoint);
						}
						retest = true;
					}										
				}
			}
			if (retest || s.isPointBelongToSeg(vv)){				
				Vector2D center = (s.type==0) ? null : s.center;
				good = false;
				if (center!=null) {					
					center = (firstBreak) ? circle(first, vv, center, rad) : circle(vv, last, center, rad);					
					if (check(v, sIndx, eIndx+1, center, rad)<0) {
						if (prev!=null && deletePrev) prev.copy(bkupPrev);													
						if (next!=null && deleteNext) next.copy(bkupNext);							
						continue;
					}
					double tx = center.x;
					double ty = center.y;
					if (rad==oldRad){
						double d = center.y-s.center.y;					
						if (d>EPSILON*10 || d<-EPSILON*10 || (s.center.y==0 && (ty>SMALL_MARGIN || ty<-SMALL_MARGIN))) {
							if (prev!=null && deletePrev) prev.copy(bkupPrev);								
							if (next!=null && deleteNext) next.copy(bkupNext);								
							continue;
						}
					}

					if (ty<SMALL_MARGIN && ty>-SMALL_MARGIN) {						
						tx = (tx<0) ? -Math.sqrt(tx*tx+ty*ty) : Math.sqrt(tx*tx+ty*ty);
						ty = 0;						
						center.x = tx;
						center.y = ty;
					}//first segment
					s.center = center;

				}
				//				s.end = new Vector2D(vv);
				//				s.num = k+1-begin;
				if (s.num>=2){					
					if (!s.reCalculate(v, sIndx, eIndx+1, tW)) {						
						if (prev!=null && deletePrev) prev.copy(bkupPrev);							
						if (next!=null && deleteNext) next.copy(bkupNext);							
						continue;																
					}
					if (s.type!=0 && rad!=s.radius && s.type!=UNKNOWN){
						boolean nok = (prev!=null && prev.type!=UNKNOWN && prev.type!=0 && prev.center.y>s.center.y);
						nok = (nok || (next!=null && next.type!=UNKNOWN && next.type!=0 && next.center.y<s.center.y));
						if (nok){
							s.copy(bkupS);								
							if (prev!=null && deletePrev) prev.copy(bkupPrev);								
							if (next!=null && deleteNext) next.copy(bkupNext);																				
							startArr[j] = bksI;
							endArr[j] = bkeI;
							if (j>0){
								startArr[j-1] = bkPs;
								endArr[j-1] = bkPe;
							}
							if (j<sz-1){
								startArr[j+1] = bkNs;
								endArr[j+1] = bkNe;
							}
							continue;
						}
						rad = s.radius;
					}
				} else {					
					if (s.start.y>vv.y){
						s.start = new Vector2D(vv);
						s.startIndex = k;
					}

					if (s.end.y<vv.y){
						s.end = new Vector2D(vv);
						s.endIndex = k;
					}

					s.num++;	
				}
				s.updated = true;
				if (!retest){
					if (!deletePrev && !firstBreak && prev!=null && kp==j-1){
						removeLastPoint(prev, op, null);
						deletePrev = true;
					} else if (!deleteNext && firstBreak && next!=null && kn==j+1){
						removeFirstPoint(next, on, null);
						deleteNext = true;
					}
				}
				boolean nIsConn = false;
				if (next!=null && next.type!=UNKNOWN){
					nIsConn = isConnected(s, next, tW, nPoint);
					if (!nIsConn && s.upper!=null){
						s.copy(bkupS);							
						if (prev!=null && deletePrev) prev.copy(bkupPrev);					
						if (next!=null && deleteNext) next.copy(bkupNext);																
						startArr[j] = bksI;
						endArr[j] = bkeI;		
						if (j>0){
							startArr[j-1] = bkPs;
							endArr[j-1] = bkPe;
						}
						if (j<sz-1){
							startArr[j+1] = bkNs;
							endArr[j+1] = bkNe;
						}
						continue;
					} else if (nIsConn){
						s.upper = new Vector2D(nPoint.x,nPoint.y);
						next.lower = nPoint;
						s.updated = true;						
					}
				}

				good = true;
				boolean pIsConn = false;
				if (prev!=null && prev.type!=UNKNOWN){
					pIsConn = isConnected(prev, s, tW, point);
					if (!pIsConn && s.lower!=null){
						s.copy(bkupS);								
						if (prev!=null && deletePrev) prev.copy(bkupPrev);							
						if (next!=null && deleteNext) next.copy(bkupNext);													
						startArr[j] = bksI;
						endArr[j] = bkeI;
						if (j>0){
							startArr[j-1] = bkPs;
							endArr[j-1] = bkPe;
						}
						if (j<sz-1){
							startArr[j+1] = bkNs;
							endArr[j+1] = bkNe;
						}
						good = false;
						continue;
					}  					
					if (!pIsConn && isPossiblyConnected && (isPrevNextConnected || isNextPrevConnected)){
						Segment tmp = new Segment();						
						double radius = s.radius;						
						radiusFrom2Points(prev, v[sIndx], v[eIndx], tW, tmp);
						if (radius!=tmp.radius){
							if (prev!=null && (pIsConn = isConnected(prev, tmp, tW, point))) {
								Segment.apply(s, tW, tmp.start, tmp.end, tmp.center, tmp.radius);
								s.lower = point;
								s.upper = null;
								if (next!=null) next.lower = null;
								rad = s.radius;
								prev.upper = new Vector2D(point);
							} else if (next!=null && isConnected(tmp, next, tW, point)) {
								Segment.apply(s, tW, tmp.start, tmp.end, tmp.center, tmp.radius);										
								s.upper = point;
								s.lower = null;
								if (prev!=null) prev.upper = null;
								rad = s.radius;
								next.lower = new Vector2D(point);																				
							}
						}
						if (!pIsConn){							
							radiusFrom2Points(s, prev.start, prev.end, tW, prev);
						}

					}

					if (pIsConn){
						//Remerge with prev here. Copy and paste from above. Ugly
						s.updated = true;
						startArr[j] = s.startIndex = sIndx;
						endArr[j] = s.endIndex = eIndx;
						int oldnum = s.num;
						if (prev!=null && deletePrev) reSynchronize(prev,prev.opp,0,otherTo,1,tW*2);
						reSynchronize(s,os,0,otherTo,1,tW*2);
						calibrateConnected(prev, kp, s, j, v, tW, startArr, endArr, point);
						if (prev!=null && prev.updated && prev.type!=UNKNOWN){						
							if (prev.opp==null) {
								prev.opp = new Segment();
								prev.opp.points = os.points;
							}
							reSynchronize(prev,prev.opp,0,otherTo,1,tW*2);														
						}
						if (s.updated && s.type!=UNKNOWN){						
							if (s.opp==null) {
								s.opp = new Segment();
								s.opp.points = opoints;
							}
							reSynchronize(s,s.opp,0,otherTo,1,tW*2);	
							s.updated = false;
						}
						if (oldnum!=s.num){
							good = false;
							if (next!=null){
								nIsConn = isConnected(s, next, tW, nPoint);
								if (!nIsConn && s.upper!=null){
									s.copy(bkupS);				
									reSynchronize(s, os, 0, otherTo, 1, 2*tW);
									if (prev!=null && deletePrev) {
										prev.copy(bkupPrev);
										reSynchronize(prev, op, op.startIndex, otherTo, 1, 2*tW);
									}
									if (next!=null && deleteNext) next.copy(bkupNext);																							
									startArr[j] = bksI;
									endArr[j] = bkeI;		
									if (j>0){
										startArr[j-1] = bkPs;
										endArr[j-1] = bkPe;
									}
									if (j<sz-1){
										startArr[j+1] = bkNs;
										endArr[j+1] = bkNe;
									}
									continue;
								} else if (nIsConn){
									s.upper = new Vector2D(nPoint.x,nPoint.y);
									next.lower = nPoint;
									s.updated = true;						
								}
							}							
						}
						int sI = prev.endIndex+1;
						int eI = s.startIndex-1;
						if (sI>eI && kp<j-1){					
							int idx = kp+1;
							int size = j-idx;
							if ((sz-=j)>0){
								System.arraycopy(ssArr, j, ssArr, idx, size);
								System.arraycopy(startArr, j, startArr, idx, size);
								System.arraycopy(endArr, j, endArr, idx, size);
							}
							sz += j-size;						
							kn -= j-idx;
							j = idx;
						} else if (sI<=eI){
							if (kp<j-2){
								int idx = kp+2;
								int size = j-idx;
								if ((sz-=j)>0){
									System.arraycopy(ssArr, j, ssArr, idx, size);
									System.arraycopy(startArr, j, startArr, idx, size);
									System.arraycopy(endArr, j, endArr, idx, size);
								}
								sz += j-size;						
								kn -= j-idx;
								j = idx;
							}

							if (j==kp+1){
								Segment ns = new Segment();							
								ns.start = new Vector2D(v[sI]);
								ns.end = new Vector2D(v[eI]);
								ns.num = eI-sI+1;
								int idx = kp+1;
								if ((sz-=idx)>0){
									System.arraycopy(ssArr, idx, ssArr, idx+1, sz);
									System.arraycopy(startArr, idx, startArr, idx+1, sz);
									System.arraycopy(endArr, idx, endArr, idx+1, sz);
								}
								sz+= idx+1;
								ssArr[idx] = ns;
								startArr[idx] = ns.startIndex = sI;
								endArr[idx] = ns.endIndex = eI;							
								j++;
								kn++;
							} else {
								int idx = kp+1;
								Segment ns = ssArr[idx];
								ns.start = new Vector2D(v[sI]);
								ns.end = new Vector2D(v[eI]);
								ns.num = eI-sI+1;
								startArr[idx] = ns.startIndex = sI;
								endArr[idx] = ns.endIndex = eI;
							}
							//End of paste
						}
					}
					begin = startArr[j];									
				}//end of if prev


				if (firstBreak){
					Segment n = ssArr[j+1];
					if (k==en){
						int idx = j+1;
						if ((sz-=idx+1)>0){
							System.arraycopy(ssArr, idx+1, ssArr, idx, sz);
							System.arraycopy(startArr, idx+1, startArr, idx, sz);
							System.arraycopy(endArr, idx+1, endArr, idx, sz);
						}
						kn--;
						sz+=idx;					
					} else {
						n.start = new Vector2D(v[k+1]);
						if (n.type!=0 && n.type!=UNKNOWN){
							Vector2D e = (en>k+2) ? v[en] : n.end;
							n.center = circle(n.start,e,n.center,n.radius);
						}
						n.num = en-k; 
						startArr[j+1] = n.startIndex = k+1;
					}
					startArr[j] = s.startIndex = sIndx;
					endArr[j] = s.endIndex = eIndx;	
				} else {
					Segment n = ssArr[j-1];
					int sI = startArr[j-1];
					if ((n.type==UNKNOWN && k==sI) || k==0){
						int idx = j-1;
						if ((sz-=idx+1)>0){
							System.arraycopy(ssArr, idx+1, ssArr, idx, sz);
							System.arraycopy(startArr, idx+1, startArr, idx, sz);
							System.arraycopy(endArr, idx+1, endArr, idx, sz);
						}
						j--;
						sz+=idx;					
					} else {
						if (n.end.y<v[k-1].y) {
							n.end = new Vector2D(v[k-1]);
							if (n.type!=0 && n.type!=UNKNOWN){
								Vector2D strt = (k>sI+2) ? v[sI] : n.start;
								n.center = circle(strt,n.end,n.center,n.radius);
							}
						}
						endArr[j-1] = n.endIndex = k-1;
						n.num = k-sI; 
					}
					startArr[j] = s.startIndex = sIndx;
					endArr[j] = s.endIndex = eIndx;					
				}

				if (s.updated && s.type!=UNKNOWN){						
					if (s.opp==null) {
						s.opp = new Segment();
						s.opp.points = opoints;
					}
					reSynchronize(s,s.opp,0,otherTo,1,tW*2);														
				}
				if (deleteNext) reSynchronize(next,on,0,otherTo,1,tW*2);								
				if (deletePrev && prev!=null && prev.upper!=null && !pIsConn) reSynchronize(prev,op,0,otherTo,1,tW*2);
				if (!firstBreak || nIsConn) break;

			} else {
				good = false;//end of if ss.isBelongPoint(vv)				
				if (deletePrev && !firstBreak && prev!=null && kp==j-1){
					prev.copy(bkupPrev);					
				} else if (deleteNext && firstBreak && next!=null && kn==j+1){
					next.copy(bkupNext);					
				}				
			}
		}//end of for

		starts.size(sz);
		ends.size(sz);
		ss.size(sz);
		return sz;

	}//*/


	public static final void reSynchronize(Segment s,Segment o,int from,int to,int which,double tW){
		if (s==null || s.type==Segment.UNKNOWN) {
			if (o!=null) o.type=Segment.UNKNOWN;
			return;
		}
		Vector2D[] v = o.points;
		if (v==null) return;
//		double t = (s.type==0) ? tW*which : -tW*which*s.type;		
		Segment.toSideSegment(s,o,which,tW);		
		o.points = v;		
		o.unsafe = s.unsafe;
		if (to>0){
			int fst = (from>=to) ? from : binarySearchFromTo(v, o.start, from, to-1);
			if (fst<0) fst = -fst-1;
			if (fst>0 && v[fst-1].y>o.start.y-SMALL_MARGIN) fst--;

			int j = (fst>=to-1) ? fst-1 :binarySearchFromTo(v, o.end, fst, to-1);			
			j = (j<0) ? -j-1 : j+1;
			if (j<to && v[j].y<o.end.y+SMALL_MARGIN) j++;
			o.startIndex = fst;
			o.endIndex = j-1;		
			o.num = j - fst;
		} else {
			o.num = 0;
			o.startIndex = 0;
			o.endIndex = -1;
		}
		if (s.updated) o.updated = true;
		o.opp = s;
		o.map = s.map;
		if (which==1){			
			o.leftSeg = s;
		} else o.rightSeg = s;
	}

	/*private static final int grow(Segment prev,Segment s,Segment next,int which, double tW,IntArrayList starts,IntArrayList ends,ObjectArrayList<Segment> tr,ObjectArrayList<Segment> ss){
		Vector2D[] opoints = (which==1) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;
		int otherTo = (which==1) ? CircleDriver2.edgeDetector.lSize:  CircleDriver2.edgeDetector.rSize;				
		Vector2D[] v = s.points;

		int[] startArr = starts.elements();
		int[] endArr = ends.elements();
		Segment[] ssArr = ss.elements();
		int sz = ss.size();
		int j = 0;
		int kp = 0;
		int kn = 0;		
		boolean ok1 = (prev==null);
		boolean ok2 = false;
		boolean ok3 = (next==null);

		for (int i = 0;i<sz;++i){
			if (ok1 && ok2 && ok3) break;
			Segment tmp = ssArr[i];
			if (!ok1 && tmp==prev){ 
				kp = i;
				ok1 = true;
			} else if (!ok2 && tmp==s){
				j = i;
				ok2 = true;
			} else if (!ok3 && tmp==next){
				kn = i;
				ok3 = true;
				break;
			}
		}
		if (s.updated && s.type!=UNKNOWN && s.num>1) {
			if (s.opp==null) {
				s.opp = new Segment();
				s.opp.points = opoints;
			}
			reSynchronize(s,s.opp,0,otherTo,-which,tW*2);								
		}

		boolean expandDown = false;		
		if (prev!=null && s.type!=UNKNOWN){
			Vector2D point = new Vector2D();
			boolean isConn = isConnected(prev, s, -which*tW, point);
			if (isConn && prev.type==0 && prev.end.y>point.y+2) {
				prev.upper = null;
				s.lower = null;
				isConn = false;
			} else if (!isConn && (isPrevNextConnected || isNextPrevConnected) ){
				System.out.println();
			}
			if (s.lower!=null && !isConn && s.num>2){
				boolean isStraight = (prev.type==0 && Math.abs(prev.start.x-prev.end.x)<TrackSegment.EPSILON*0.5);
				int st = s.startIndex;
				int en = s.endIndex;
				double t =0;
				if (s.num>0){
					Vector2D e = v[en];						
					if ((t=e.distance(s.end))<EdgeDetector.MINDIST && t>0){
						s.end.x = e.x;
						s.end.y = e.y;
						isConn = isConnected(prev, s, -which*tW, point);
					}						
					if (!isConn){
						e = v[st];						
						if ((t = e.distance(s.start))<EdgeDetector.MINDIST && t>0){
							s.start.x = e.x;
							s.start.y = e.y;
							isConn = isConnected(prev, s, -which*tW, point);
						}
						if (!isConn && prev.num>2){
							st = prev.startIndex;
							en = prev.endIndex;
							e = v[st];						
							if ((t = e.distance(prev.start))<EdgeDetector.MINDIST && t>0){
								prev.start.x = e.x;
								prev.start.y = e.y;	
								if (isStraight) prev.end.x = prev.start.x;
								isConn = isConnected(prev, s, -which*tW, point);
							}
							if (!isConn){
								e = v[en];						
								if ((t = e.distance(prev.end))<EdgeDetector.MINDIST && t>0){
									prev.end.x = e.x;
									prev.end.y = e.y;
									if (isStraight) prev.start.x = prev.end.x;
									isConn = isConnected(prev, s, -which*tW, point);
								}
							}
						}
					}
				} else {
					prev.upper = null;
					s.lower = null;
				}
			}
			if (isConn){					
				calibrateConnected(prev, kp, s, j, v, -which*tW, startArr, endArr, point);
				if (prev!=null && prev.updated && prev.type!=UNKNOWN){						
					if (prev.opp==null) {
						prev.opp = new Segment();
						prev.opp.points = opoints;
					}
					reSynchronize(prev,prev.opp,0,otherTo,-which,tW*2);														
				}
				if (s.updated && s.type!=UNKNOWN){						
					if (s.opp==null) {
						s.opp = new Segment();
						s.opp.points = opoints;
					}
					reSynchronize(s,s.opp,0,otherTo,-which,tW*2);		
					s.updated = false;
				}
				expandDown = false;
				int sI = prev.endIndex+1;
				int eI = s.startIndex-1;
				if (sI>eI && kp<j-1){
					ss.removeElements(kp+1, j);
					starts.removeElements(kp+1, j);
					ends.removeElements(kp+1, j);
					kn -= j-kp-1;
					j = kp+1;
				} else if (sI<=eI){
					if (kp<j-2){
						ss.removeElements(kp+2, j);
						starts.removeElements(kp+2, j);
						ends.removeElements(kp+2, j);
						kn -= j-kp-2;
						j = kp+2;
					}

					if (j==kp+1){
						Segment ns = new Segment();
						ss.add(kp+1, ns);
						ns.start = new Vector2D(v[sI]);
						ns.end = new Vector2D(v[eI]);
						ns.num = eI-sI+1;
						ns.startIndex = sI;
						ns.endIndex = eI;
						starts.add(kp+1, sI);
						ends.add(kp+1, eI);
						j++;
						kn++;
					} else {
						Segment ns = ss.get(kp+1);
						ns.start = new Vector2D(v[sI]);
						ns.end = new Vector2D(v[eI]);
						ns.num = eI-sI+1;
						ns.startIndex = sI;
						ns.endIndex = eI;
						starts.set(kp+1, sI);
						ends.set(kp+1, eI);
					}
				}				
			} else if (isPossiblyConnected){
				if (prev.type!=0 && s.type!=0 && s.num>1){
					if (prev.lower!=null){
						Segment tmp = new Segment();
						radiusFrom2Points(prev, s.points[s.startIndex], s.points[s.endIndex], -which*tW, tmp);
						if (tmp.type!=0 && tmp.type!=UNKNOWN && check(v, s.startIndex, s.endIndex+1, tmp.center, tmp.radius)>=0 && isConnected(prev, tmp, -which*tW, point)){
							double rad = s.radius;
							int rr = score(s, which, tW);
							if (s.map!=null) s.map[rr]--;
							Segment.apply(s, tW, s.start, s.end, tmp.center, tmp.radius);
							if (rad!=s.radius){
								s.lower = new Vector2D(point);
								prev.upper = new Vector2D(point);
								if (s.opp==null) {
									s.opp = new Segment();
									s.opp.points = opoints;
								}
								reSynchronize(s,s.opp,0,otherTo,-which,tW*2);
							} 
						} else {
							s.lower = null;
							prev.upper = null;
						}
					} else {
						s.lower = null;
						prev.upper = null;
					}
				} //end of if					
				Segment p = ssArr[j-1];
				if (p.num==0) 
					expandDown = false;
				else {
					Vector2D pp = v[p.endIndex];
					if (pp.y<point.y-1) expandDown = false;
				}				
			} else {
				s.lower = null;//end of else
				prev.upper = null;
			}

			if (!isConn && s.lower!=null) expandDown = false;								
		}



		if (expandDown && s.type!=UNKNOWN && s.type!=0 && s.center!=null && prev!=null && prev.type==0 && Math.abs(prev.start.x-prev.end.x)<TrackSegment.EPSILON*0.5){
			Segment p = ssArr[j-1];
			if (p.num==0) 
				expandDown = false;
			else {
				Vector2D pp = v[p.endIndex];
				if (pp.y<s.center.y-1) expandDown = false;
			}
		}

		//		if (expandUp && next!=null && j<ss.size()-1  && s.type!=UNKNOWN ){
		//			int st = startArr[j+1] ;
		//			int en = (kn-1>j || (next!=null && next.num>0)) ? endArr[j+1] : endArr[kn]+1;
		//			if (j<kn-1)
		//				szs = expand(v,st,en,prev, kp, s, j, next, kn, ss, starts, ends, -which*tW,false,other);
		//			else if (next!=null && next.num>0) szs = expandFw(v,st,en,prev, kp, s, j, next, kn, ss, starts, ends, -which*tW,true,other);
		//		}
		//		
		//		if (szs!=ssz){
		//			int idx = j+1;
		//			if ((sz-=idx+1)>0) System.arraycopy(trArr, idx+1, trArr, idx, sz);
		//			sz+=idx;
		//			kn -= ssz-szs;
		//			for (int jj = ii+1;jj<sz;++jj) indexArr[jj]--;
		//			ssz = szs;
		//		}
		//
		//		if (expandDown && j>0 && s.type!=UNKNOWN){
		//			int en = startArr[j]-1;
		//			int st = (prev!=null && prev.num>0) ? startArr[kp] : startArr[j-1];
		//			if (kp+1<j || (prev!=null && prev.num>0)) szs = expand(v,st,en,prev, kp, s, j, next, kn, ss, starts, ends, -which*tW,true,other);
		//		}



		return 0;

	}//*/

	/*public static void copySegment(Segment[] src,int startIndx,Segment[] dest,int offset,int size){
		if (src==dest && startIndx==offset) return;
		if (src==dest && startIndx<offset){
			startIndx+=size-1;
			offset += size-1;
			for (int i = size-1;i>=0;--i){
				Segment s = dest[startIndx--];
				Segment d = dest[offset--];
				copy(d,s);
			}
		} else {
			for (int i = size-1;i>=0;--i){
				Segment s = src[startIndx++];
				Segment d = dest[offset++];
				copy(d,s);
			}
		}
	}

	public static void copySegment(Segment[] src,int startIndx,Segment[] dest,int offset,int size,double tW){
		if (src==dest && startIndx==offset) return;
		if (src==dest && startIndx<offset){
			startIndx+=size-1;
			offset += size-1;
			for (int i = size-1;i>=0;--i){
				Segment s = dest[startIndx--];
				Segment d = dest[offset--];
				copy(d,s,tW);
			}
		} else {
			for (int i = size-1;i>=0;--i){
				Segment s = src[startIndx++];
				Segment d = dest[offset++];
				copy(d,s,tW);
			}
		}
	}//*/

	/*public static final int insertElems(Segment[] src,int from,int to,Segment[] dest,int posIns,int size){
		int sz = to-from;
		if (sz<=0) return size;
		if ((size-=posIns)>0) copySegment(dest, posIns, dest, posIns+sz, size);
		copySegment(src, from, dest, posIns, sz);		
		return size+posIns+sz;
	}


	public static final int replaceElems(Segment[] src,int from,int to,Segment[] dest,int startIndx,int endIndx,int size){
		int sz = to-from;
		int dSz = endIndx-startIndx;
		if (sz==dSz){
			copySegment(src,from,dest,startIndx,sz);
		} else {
			int newIndx = startIndx+sz;
			if ((size-=endIndx)>0) copySegment(dest,endIndx,dest,newIndx,size);
			if (sz>0) copySegment(src, from, dest, startIndx, sz);
			size+=endIndx+sz-dSz;
		}
		return size;
	}//*/

	/*public static final int replaceSideElems(Segment[] src,int from,int to,Segment[] dest,int[] trIndx,int startIndx,int endIndx,int size,int which){
		int sz = to-from;
		int dSz = endIndx-startIndx;
		int[] occupied = CircleDriver2.occupied;
		if (sz==dSz){
			copySideSegment(src,from,dest,trIndx,startIndx,sz,which);
			if (endIndx>size){
				int j = 0;
				for (int i = size;i<endIndx;++i){
					while (occupied[j]!=0) j++;
					trIndx[i] = j;
					occupied[j] = 1;
				}
				return endIndx;
			}			
			return size;
		} else {
			int newIndx = startIndx+sz;
			
			if (endIndx>newIndx){
				for (int i = newIndx;i<endIndx;++i){
					occupied[ trIndx[i] ] = 0;
				}
			}
																
			if (size-endIndx>0 && endIndx!=newIndx) {
				System.arraycopy(trIndx,endIndx,trIndx,newIndx,size-endIndx);
			}
																		
			int j = 0;
			if (sz>0) {
				for (int i=sz-1;i>=0;--i){
					if (startIndx>=endIndx || startIndx>=size){
						while (occupied[j]!=0) j++;
						trIndx[startIndx] = j;
						occupied[j] = 1;
					}
					Segment s = src[from++];
					Segment t = dest[ trIndx[startIndx++] ];
					Segment seg = (which==-1) ? t.leftSeg : t.rightSeg;
					if (seg!=s){
						seg.copy(s);
						seg.opp.copy(s.opp);
					}					
				}		
				if (endIndx>size) return startIndx;
			}						
		}
		return size+sz-dSz;
	}//*/



	public static final int replaceSideElems(Segment[] src,int from,int to,Segment[] dest,int[] trIndx,int startIndx,int endIndx,int size,int which,double tW){
		int sz = to-from;
		int dSz = endIndx-startIndx;
		int[] occupied = CircleDriver2.occupied;
		if (size<0) return size;
		if (sz==dSz){
			if (endIndx>size) {
				int j = 0;
				for (int i = size;i<endIndx;++i){
					while (occupied[j]!=0) j++;
					trIndx[i] = j;
					occupied[j] = 1;
//					Segment t = dest[j];
//					t.leftSeg.done = false;
//					t.rightSeg.done = false;
				}				
			}
			copySideSegment(src,from,dest,trIndx,startIndx,sz,which,tW);
			if (endIndx>size) return endIndx;						
			return size;
		} else {
			double tw = -which*tW;
//			int newIndx = startIndx+sz;
//			if (endIndx>newIndx){
//				for (int i = newIndx;i<endIndx;++i){
//					occupied[ trIndx[i] ] = 0;
//				}
//			}
			int j =0;
			
//			if ((size-endIndx)>0 && endIndx!=newIndx) {
//				System.arraycopy(trIndx,endIndx,trIndx,newIndx,size-endIndx);
//			}
									
			
			if (sz>0) {
				for (int i=sz-1;i>=0;--i){
//					if (startIndx>=endIndx || startIndx>=size){
//						while (occupied[j]!=0) j++;
//						trIndx[startIndx] = j;
//						occupied[j] = 1;
//					}
					Segment t = (startIndx>=size) ? null : dest[ trIndx[startIndx] ];
					Segment seg = (t==null) ? null : (which==-1) ? t.leftSeg : t.rightSeg;
					Segment s = src[from++];
					if (s.type==Segment.UNKNOWN) continue;
					if (seg==null || (seg.type!=Segment.UNKNOWN &&  s.end.y<seg.start.y) ){						
						if (seg==null || seg.type!=Segment.UNKNOWN){
							while (occupied[j]!=0) j++;		
							if (size-startIndx>0) System.arraycopy(trIndx, startIndx, trIndx, startIndx+1, size-startIndx);
							size++;
						} else j = trIndx[ startIndx++ ];
						occupied[j] = 1;
						
						t = 	dest[ j ];
						seg = (which==-1) ? t.leftSeg : t.rightSeg;							
						seg.opp.done = false;							
						trIndx[ startIndx ] = j;
						startIndx++;
														
					} else startIndx++;
					if (seg!=s){
						if (s.map==null || seg.map!=s.map){
							int[] map = seg.map;
							int[] appearedRads = seg.appearedRads;								
							for (int k = seg.radCount-1;k>=0;--k){
								int rr = appearedRads[k];
								map[rr] = 0;
								appearedRads[k] = 0;
							}
							if (s.map!=null){
								for (int k = s.radCount-1;k>=0;--k){
									int rr = s.appearedRads[k];
									map[rr] = s.map[rr];
									appearedRads[k] = s.appearedRads[k];
								}					
							}
							seg.radCount = s.radCount;
							seg.opp.radCount = seg.radCount;							 
						}
						
						if (s.map==null){
							seg.copy(s,tw);																	
						} else {
							seg.copy(s);							
						}
//						boolean oldDone = seg.opp.done;
						seg.opp.copy(s.opp);
						seg.opp.done = false;
//						seg.opp.done = oldDone;
					}					
					t.radCount = seg.radCount;
				} // end of for				
			}		
		}
	
		return size;
	}


	public static void copySide(Segment src,int which,Segment dest){
		Segment seg = (which==-1) ? dest.leftSeg : dest.rightSeg;
		if (seg!=src){
			seg.copy(src);
			seg.opp.copy(src.opp);
		}		
	}

	public static void copySideSegment(Segment[] src,int startIndx,Segment[] dest,int[] trIndx,int offset,int size,int which){		
		for (int i = size-1;i>=0;--i){
			Segment s = src[startIndx++];
			Segment t = dest[ trIndx[offset++] ];
			Segment seg = (which==-1) ? t.leftSeg : t.rightSeg;
			if (seg!=s){
				seg.copy(s);
				seg.opp.copy(s.opp);
			}
		}
	}

	public static void copySide(Segment src,int which,Segment dest,double tW){
		Segment seg = (which==-1) ? dest.leftSeg : dest.rightSeg;
		if (seg!=src){
			seg.copy(src,tW);
			seg.opp.copy(src.opp,tW);
		}			
	}

	public static void copySideSegment(Segment[] src,int startIndx,Segment[] dest,int[] trIndx,int offset,int size,int which,double tW){
		double tw = -which*tW;
		for (int i = size-1;i>=0;--i){
			Segment s = src[startIndx++];
			if (s==null || s.type==Segment.UNKNOWN) continue;
			int index = trIndx[offset++];
			Segment t = dest[ index ];
			
			Segment seg = (which==-1) ? t.leftSeg : t.rightSeg;			
			if (s.map==null || seg.map!=s.map){
				int[] map = seg.map;
				int[] appearedRads = seg.appearedRads;								
				for (int j = seg.radCount-1;j>=0;--j){
					int rr = seg.appearedRads[j];
					seg.map[rr] = 0;
					seg.appearedRads[j] = 0;
				}
				if (s.map!=null){
					for (int j = s.radCount-1;j>=0;--j){
						int rr = s.appearedRads[j];
						map[rr] = s.map[rr];
						appearedRads[j] = s.appearedRads[j];
					}					
				}
				seg.radCount = s.radCount;				
				seg.opp.radCount = seg.radCount;				
			}
			if (seg!=s){
				if (s.map==null){ 
					seg.copy(s,tw);															
				} else {
					seg.copy(s);					
				}				
//				boolean oldDone = seg.opp.done;
				seg.opp.copy(s.opp);
				seg.opp.done = false;
//				seg.opp.done = oldDone;
			}
			t.radCount = seg.radCount;
		}
	}
	
	public static int remove(Segment s,int which,int currentIndx,int trSz){
		int[] occupied = CircleDriver2.occupied;
		int[] trIndx = CircleDriver2.trIndx;
		Segment[] trArr = CircleDriver2.trArr;
		Segment os = s.opp;
		s.type = Segment.UNKNOWN;							
		if (os!=null) os.type = Segment.UNKNOWN;
		occupied[ trIndx[currentIndx] ] = 0;
		if (currentIndx==trSz-1) return trSz-1;
//		if (!s.unsafe){
//			Segment next = null;
			int i = 0;
			for (i = currentIndx+1;i<trSz;++i){
				int j = trIndx[i]; 
				Segment t = trArr[ j ];
				if (t.unsafe) {
					/*if (!t.unsafe) {
						t.leftSeg.type = Segment.UNKNOWN;
						t.rightSeg.type = Segment.UNKNOWN;
						t.type = Segment.UNKNOWN;
						occupied[j] = 0;
						i++;
					}//*/
					if (i-currentIndx>0) {
						if (trSz-i>0) System.arraycopy(trIndx, i, trIndx, currentIndx, trSz-i);							
						trSz-=i-currentIndx;
					}
					/*if (trSz>currentIndx){
						next = trArr[trIndx[currentIndx]];
						next = (which==1) ? next.rightSeg : next.leftSeg;
					} else next = null;//*/
					return trSz;
				}
				t.leftSeg.type = Segment.UNKNOWN;
				t.rightSeg.type = Segment.UNKNOWN;
				t.type = Segment.UNKNOWN;
				occupied[j] = 0;
			}
			if (i==trSz) return currentIndx;
//		}
		return trSz-1;
	}
	

	public static final void reCheckSegment(Segment prev,Segment s,Segment next,int which, double tW,Segment[] trArr,int currentIndx){		
		int[] occupied = CircleDriver2.occupied;
		int[] trIndx = CircleDriver2.trIndx;
		int[] currentMap = (s==null) ? null : s.map;
		Segment os = (s!=null) ? s.opp : null;
		Segment on = (next==null) ? null : next.opp;
		Segment op = (prev==null) ? null : prev.opp;
		EdgeDetector edge = CircleDriver2.edgeDetector;
		Vector2D[] v = (which==1) ? edge.right : edge.left;
		Vector2D[] nv = (which==1) ? EdgeDetector.nright : EdgeDetector.nleft;
		int nSz = (which==-1) ? edge.nLsz:  edge.nRsz;
		Vector2D[] otherPoints = (which==-1) ? edge.right : edge.left;
		int otherTo = (which==1) ? edge.lSize : edge.rSize;
		int to = (which==-1) ? edge.lSize:  edge.rSize;
		//		if (s.updated){
		int firstIndex = (s==null) ? (prev==null) ? 0 : prev.endIndex+1: s.startIndex;
		int lastIndex = (s==null) ? to : s.endIndex+1;
		int size = (s==null) ? lastIndex-firstIndex : s.num;		
		//		int sr = (s==null) ? 0 :(s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius+s.type*tW*which);


		if (s!=null && currentMap==null) {
			int sR = (s.type==0 || s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : (s.type==Segment.UNKNOWN) ? -1 :  (int)Math.round(s.radius+s.type*tW*which);
			if (sR>=MAX_RADIUS) sR = MAX_RADIUS-1;
			currentMap = new int[MAX_RADIUS];
			s.map = currentMap;
			if (sR>=0) currentMap[sR]++;
			if (os!=null) os.map = currentMap;
		}
		//		int[] map = (s==null) ? null : s.map;				
		boolean inTurn = CircleDriver2.inTurn;
//		double[][][] allRadius = (inTurn) ? (which==1) ? CircleDriver2.allRadiusRight : CircleDriver2.allRadiusLeft : null;
//		double[][][] allCntrx = (inTurn) ? (which==1) ? CircleDriver2.allCntrxR : CircleDriver2.allCntrxL : null;
//		double[][][] allCntry = (inTurn) ? (which==1) ? CircleDriver2.allCntryR : CircleDriver2.allCntryL : null;
//		int[][][] allTurn = (inTurn) ? (which==1) ? CircleDriver2.allTpR : CircleDriver2.allTpL : null;
//		double[] result = new double[6];		

		//Update from here
		Vector2D fst = null;
		Vector2D lst = null;
//		int fi = 0;
//		int li = 0;
		if (s!=null && (s.start==null || s.end==null)) {
			s.type = Segment.UNKNOWN;
			os.type = Segment.UNKNOWN;
		}
		int oldTp = (s==null) ? Segment.UNKNOWN : s.type;
		if (s!=null && s.type!=Segment.UNKNOWN && s.updated)
			for (int i = 0;i<nSz;++i){
				Vector2D p = nv[i];
				if (p.y>=s.start.y-SMALL_MARGIN && p.y<=s.end.y+SMALL_MARGIN){
					lst = p;
//					li = i;
					if (fst==null){
						fst = lst;
//						fi = i;
					}
				}
				if (p.y>s.end.y) break;
			}

		if (prev==null && s!=null && s.type==0 && Math.abs(s.start.x-s.end.x)>=E){
			if (fst!=null && lst!=fst && Math.abs(lst.x-fst.x)<E){
				s.start.x = fst.x;
				s.end.x = fst.x;
			} else if (s.endIndex>firstIndex){
				Vector2D first = v[firstIndex];
				Vector2D last = v[s.endIndex];
				if (Math.abs(last.x-first.x)<E){
					if (fst!=null){
						s.start.x = fst.x;
						s.end.x = fst.x;
					} else {
						s.start.x = first.x;
						s.end.x = first.x;
					}
				}
			}
		}
		if (prev ==null && s!=null && ((s.type!=0 && s.center!=null && s.center.y==0) || (s.type==0 && Math.abs(s.start.x-s.end.x)<E))){
			if (s!=null && s.type!=Segment.UNKNOWN) {					
				if (s.type!=0 && s.updated){
					boolean ok = true;
					boolean deleted = false;
					int num = s.num;
					int endIdx = s.endIndex+1;
					int startIdx = s.startIndex;
					while (ok && num>0){
						double d = check(v, startIdx,endIdx, s.center, s.radius);
						ok = (d<0);						
						if (ok){
							endIdx--;
							num--;
							deleted = true;
						} else break;
					}

					if (deleted){						
						if (os==null) {
							os = new Segment();
							os.points = otherPoints;						
							s.opp = os;							
						}	

						Segment.removeLastPoint(s, os, v[endIdx].y-SMALL_MARGIN);																		
						if (s.type!=Segment.UNKNOWN) 
							reSynchronize(s,os,0,otherTo,-which,tW*2);
						else {
							os.type = Segment.UNKNOWN;							
							Segment l0 = CircleDriver2.l0;
							Segment r0 = CircleDriver2.r0;
							l0.type = Segment.UNKNOWN;
							r0.type = Segment.UNKNOWN;
							double firstRad = CircleDriver2.verify(edge.left,edge.lSize, edge.right, edge.rSize, null, which, CircleDriver2.toMiddle, tW,CircleDriver2.nraced);							
							int tp = (firstRad<0) ? -1 : (firstRad==0) ? 0 : 1;
							firstRad = (tp<0) ? -firstRad : firstRad;	
							if (firstRad>=MAX_RADIUS-1){
								firstRad = 0;
								tp = 0;
							}
							CircleDriver2.getFirstSegment(edge.left,edge.lSize, edge.right, edge.rSize, tp, CircleDriver2.toMiddle, firstRad, l0, r0);															
							
							ok = (l0.type!=Segment.UNKNOWN);
							if (ok){
								if (which==-1) {
									s.copy(l0,-which*tW);
									os.copy(r0);
								} else {
									s.copy(r0,-which*tW);
									os.copy(l0);
								}								
							}
						}
					} else if (s!=null){
						reExpand(null, s, null, which, tW);
					}
				} else if (s.type==0){							
					Vector2D first = null;
					Vector2D last = null;
					boolean changed = false;
					for (firstIndex=0;firstIndex<to;++firstIndex){
						first = v[firstIndex];
						if (first.y>=0) break;
					}
					double strtDist =  (first!=null && s.type==0) ? s.end.y : 0;						
					double x0 = v[firstIndex].x;												
					double upper = (strtDist>0) ?  (s.upper!=null) ? s.upper.y : (os.upper!=null) ? os.upper.y : (s.type==UNKNOWN && s!=null && s.upper!=null) ? s.upper.y : 100 : 100;
					for (lastIndex= firstIndex+1;lastIndex<to;++lastIndex){
						last = v[lastIndex];
						double dx = last.x-x0;
						double yy = last.y;
						dx = (dx<0) ? -dx : dx;
						if (dx>2*E || (dx>E && yy>strtDist) || dx>E || yy>upper) break;
					}					
					if (lastIndex<to) last = v[lastIndex-1];
					
					if (last==null && first!=null && first.y>s.end.y && Math.abs(s.start.x-first.x)<=E){
						s.end.copy(first);
						changed = true;
						s.endIndex = firstIndex;
						s.num = firstIndex+1-s.startIndex;
					}
					
					if (last!=null && last.y>0 && last!=first && (last.y>s.end.y || (lastIndex<to && v[lastIndex].y<s.end.y)) ){
						s.end.copy(last);		
						changed = true;
						s.endIndex = lastIndex-1;
						s.num = lastIndex - s.startIndex;					
					} 

					if (first!=null && first.y>=0 && first.y<=s.end.y) {
						s.end.x = x0;
						s.start.x = x0;
					}

					if (first!=null && last!=first && first.y>=0 && s.start.y>first.y){
						s.start.copy(first);					
						s.startIndex = firstIndex;
						s.num = s.endIndex+1-s.startIndex;
						changed = true;
					}
					
					if (s.type!=Segment.UNKNOWN && changed){
						s.updated = true;					
						if (os==null) {				
							os = new Segment();
							os.points = otherPoints;								
							s.opp = os;
						}
						reSynchronize(s,os,0,otherTo,-which,tW*2);
						os.updated = true;
					} else if (s.type==Segment.UNKNOWN) occupied[ trIndx[currentIndx] ] = 0;
				}
				
				if (s!=null && s.type!=Segment.UNKNOWN && next!=null && next.type!=Segment.UNKNOWN &&  next.start.y<=s.end.y+SMALL_MARGIN && next.end.y>s.end.y+SMALL_MARGIN){
					if (next.unsafe) 
						removeFirstPoint(next, on,s.end.y+SMALL_MARGIN);
					else {
						occupied[ trIndx[currentIndx+1] ] = 0; 
						trSz = remove(next, which, currentIndx+1, trSz);
						if (trSz>currentIndx+1){
							next = trArr[trIndx[currentIndx+1]];
							next = (which==1) ? next.rightSeg : next.leftSeg;
						} else next = null;
						on = (next==null) ? null : next.opp;
					}
					if (next!=null){
						next.updated = true;
						if (next.type!=Segment.UNKNOWN)
							reSynchronize(next,on,0,otherTo,-which,tW*2);
						else if (trSz>currentIndx+1){
							int idx = currentIndx+2;
							occupied[ trIndx[currentIndx+1] ] = 0; 
							if ((trSz-=idx)>0) System.arraycopy(trIndx, idx, trIndx, currentIndx+1, trSz);
							trSz+=currentIndx+1;
							on.type = Segment.UNKNOWN;
							if (trSz>currentIndx+1){
								next = trArr[trIndx[currentIndx+1]];
								next = (which==1) ? next.rightSeg : next.leftSeg;
							} else next = null;
							on = (next==null) ? null : next.opp;
						}
					}
					
				} else if (s!=null && s.type!=Segment.UNKNOWN && next!=null && next.type!=Segment.UNKNOWN && next.end.y<=s.end.y+SMALL_MARGIN){
					int idx = currentIndx+1;
					while (next!=null && next.type!=Segment.UNKNOWN && next.end.y<=s.end.y+SMALL_MARGIN){														
						if (idx<trSz){
							occupied[ trIndx[idx] ] = 0;
							idx++;
							next = (idx>=trSz) ? null : (which==1) ? trArr[ trIndx[idx] ].rightSeg : trArr[ trIndx[idx] ].leftSeg;
						} else next = null;
					}						
					if ((trSz-=idx)>0 && idx!=currentIndx+1) System.arraycopy(trIndx, idx, trIndx, currentIndx+1, trSz);
					trSz+=currentIndx+1;
					idx = currentIndx+1;
					next = (idx>=trSz) ? null : (which==1) ? trArr[ trIndx[idx] ].rightSeg : trArr[ trIndx[idx] ].leftSeg;
					on = (next==null) ? null : next.opp;
					
					if (next!=null && next.type!=Segment.UNKNOWN && (next.endIndex<s.startIndex || (next.start.y<=s.end.y+SMALL_MARGIN && next.end.y>s.end.y+SMALL_MARGIN))){
						if (next.unsafe) 
							removeFirstPoint(next, on, s.end.y+SMALL_MARGIN);
						else {
							occupied[ trIndx[idx] ] = 0; 
							trSz = remove(next, which, idx, trSz);
							next = (idx>=trSz) ? null : (which==1) ? trArr[ trIndx[idx] ].rightSeg : trArr[ trIndx[idx] ].leftSeg;
							on = (next==null) ? null : next.opp;
						}
						if (next!=null){
							next.updated = true;
							if (next.type!=Segment.UNKNOWN)
								reSynchronize(next,on,0,otherTo,-which,tW*2);
							else {
								idx = currentIndx+2;
								occupied[ trIndx[currentIndx+1] ] = 0;
								if ((trSz-=idx)>0) System.arraycopy(trIndx, idx, trIndx, currentIndx+1, trSz);
								trSz+=currentIndx+1;
								on.type = Segment.UNKNOWN;	
								if (trSz>currentIndx+1){
									next = trArr[trIndx[currentIndx+1]];
									next = (which==1) ? next.rightSeg : next.leftSeg;
								} else next = null;
								on = (next==null) ? null : next.opp;
							}
						}
					}
				}
				//Now check opp
				if (os!=null && os.type!=Segment.UNKNOWN && on!=null && on.type!=Segment.UNKNOWN && on.start.y<=os.end.y+SMALL_MARGIN && on.end.y>os.end.y+SMALL_MARGIN){
					if (on.unsafe) 
						removeFirstPoint(on, next,os.end.y+SMALL_MARGIN);
					else {
						occupied[ trIndx[currentIndx+1] ] = 0; 
						trSz = remove(on, -which, currentIndx+1, trSz);
						if (trSz>currentIndx+1){
							next = trArr[trIndx[currentIndx+1]];
							next = (which==1) ? next.rightSeg : next.leftSeg;
						} else next = null;
						on = (next==null) ? null : next.opp;
					}
					if (on!=null){
						on.updated = true;
						if (on.type!=Segment.UNKNOWN)
							reSynchronize(on,next,0,to,which,tW*2);
						else {
							int idx = currentIndx+2;
							occupied[ trIndx[currentIndx+1] ] = 0; 
							if ((trSz-=idx)>0) System.arraycopy(trIndx, idx, trIndx, currentIndx+1, trSz);
							trSz+=currentIndx+1;
							next.type = Segment.UNKNOWN;
							if (trSz>currentIndx+1){
								next = trArr[trIndx[currentIndx+1]];
								next = (which==1) ? next.rightSeg : next.leftSeg;
							} else next = null;
							on = (next==null) ? null : next.opp;
						}
					}
				} else if (os!=null && os.type!=Segment.UNKNOWN && on!=null && on.type!=Segment.UNKNOWN && on.end.y<=os.end.y+SMALL_MARGIN){
					int idx = currentIndx+1;
					while (on!=null && on.type!=Segment.UNKNOWN && on.end.y<=os.end.y+SMALL_MARGIN){														
						if (idx<trSz){
							occupied[ trIndx[idx] ] = 0;
							idx++;
							on = (idx>=trSz) ? null : (which==-1) ? trArr[ trIndx[idx] ].rightSeg : trArr[ trIndx[idx] ].leftSeg;
						} else on = null;
					}						
					if ((trSz-=idx)>0 && idx!=currentIndx+1) System.arraycopy(trIndx, idx, trIndx, currentIndx+1, trSz);
					trSz+=currentIndx+1;
					idx = currentIndx+1;
					next = (idx>=trSz) ? null : (which==1) ? trArr[ trIndx[idx] ].rightSeg : trArr[ trIndx[idx] ].leftSeg;
					on = (next==null) ? null : next.opp;

					if (on!=null && on.type!=Segment.UNKNOWN && (on.endIndex<os.startIndex || (on.start.y<=os.end.y+SMALL_MARGIN && on.end.y>os.end.y+SMALL_MARGIN))){
						if (on.unsafe) 
							removeFirstPoint(on, next,os.end.y+SMALL_MARGIN);
						else {
							occupied[ trIndx[currentIndx+1] ] = 0; 
							trSz = remove(on, -which, currentIndx+1, trSz);
							next = (idx>=trSz) ? null : (which==1) ? trArr[ trIndx[idx] ].rightSeg : trArr[ trIndx[idx] ].leftSeg;
							on = (next==null) ? null : next.opp;
						}
						if (on!=null){
							on.updated = true;
							if (on.type!=Segment.UNKNOWN)
								reSynchronize(on,next,0,to,which,tW*2);
							else {
								idx = currentIndx+2;
								occupied[ trIndx[currentIndx+1] ] = 0;
								if ((trSz-=idx)>0) System.arraycopy(trIndx, idx, trIndx, currentIndx+1, trSz);
								trSz+=currentIndx+1;
								next.type = Segment.UNKNOWN;
								if (trSz>currentIndx+1){
									next = trArr[trIndx[currentIndx+1]];
									next = (which==1) ? next.rightSeg : next.leftSeg;
								} else next = null;
								on = (next==null) ? null : next.opp;
							}
						}
					}
				}


				
				if (next==null && s.type!=Segment.UNKNOWN && to>s.endIndex+2){
					boolean ok = s.updated || inTurn;
					if (!ok){
						for (int i = s.endIndex+1;i<to;++i){
							if (v[i].certain) {
								ok = true;
								break;
							}
						}
					}

					if (ok){												
						int liSz = bestGuess(v, s.endIndex+1, to, -which*tW, s, null, oalArr,0);												
						if (liSz>0){																																	
							int nextIndx = currentIndx+1;							
							trSz = replaceSideElems(oalArr, 0, liSz, trArr, trIndx,currentIndx+1, nextIndx, trSz, which,tW);							
						}//end of if ok
						os.updated = s.updated;
						s.updated = false;
					}					
				}				
				return;
			}
		}
		
		if (prev!=null && s!=null && prev.type!=Segment.UNKNOWN){
			if (prev.end.y>=s.start.y-SMALL_MARGIN){		
				if (s.unsafe) {
					Segment.removeFirstPoint(s, os, prev.end.y+SMALL_MARGIN);
					s.updated = true;
				} else {
//					occupied[ trIndx[currentIndx] ] = 0; 
					trSz = remove(s, which, currentIndx, trSz);
					if (trSz>currentIndx+1){
						next = trArr[trIndx[currentIndx+1]];
						next = (which==1) ? next.rightSeg : next.leftSeg;
					} else next = null;
					on = (next==null) ? null : next.opp;
					if (trSz>currentIndx){
						s = trArr[trIndx[currentIndx]];
						s = (which==1) ? s.rightSeg : s.leftSeg;
					} else s = null;
					os = (s==null) ? null : s.opp;
				}
				reSynchronize(s,os,0,otherTo,-which,tW*2);
			} else if (op!=null && op.end.y>=os.start.y-SMALL_MARGIN){
				if (os.unsafe) {
					Segment.removeFirstPoint(os, s,op.end.y+SMALL_MARGIN);
					s.updated = true;
				} else {
//					occupied[ trIndx[currentIndx] ] = 0; 
					trSz = remove(os, -which, currentIndx, trSz);
					if (trSz>currentIndx+1){
						next = trArr[trIndx[currentIndx+1]];
						next = (which==1) ? next.rightSeg : next.leftSeg;
					} else next = null;
					on = (next==null) ? null : next.opp;
					if (trSz>currentIndx){
						s = trArr[trIndx[currentIndx]];
						s = (which==1) ? s.rightSeg : s.leftSeg;
					} else s = null;
					os = (s==null) ? null : s.opp;
				}
				reSynchronize(os,s,0,to,which,tW*2);
			}
		}  

		if (s!=null && size>1 && s.type==0 && s.updated){
			Vector2D first = v[firstIndex];
			Vector2D last = v[lastIndex-1];
			double d = first.x-last.x;					
			if ((d<0 ? - d : d ) <=E) {
				//				s.start.x = first.x;
				//				s.end.x = first.x;
				double tot = 0;
				for (int kk = firstIndex;kk<lastIndex;++kk){
					Vector2D vv = v[kk];
					tot += Math.sqrt(Geom.ptLineDistSq(first.x, first.y, last.x, last.y, vv.x, vv.y, null));							
				}
				tot /= (lastIndex-firstIndex+0.0d);
				if (tot>E){
					s.type = Segment.UNKNOWN;
					if (os!=null) os.type = Segment.UNKNOWN;
				} else	{
					s.start.x = first.x;
					double x0 = first.x;					
					last = null;
					for (lastIndex= s.endIndex+1;lastIndex<to;++lastIndex){
						last = v[lastIndex];
						double dx = last.x-x0;						
						dx = (dx<0) ? -dx : dx;
						if (dx>E) break;
					}					
					last = v[lastIndex-1];
															
					if (last!=null && last.y>0 && last!=first && (last.y>s.end.y || (lastIndex<to && v[lastIndex].y<s.end.y)) ){
						s.end.copy(last);								
						s.endIndex = lastIndex-1;
						s.num = lastIndex - s.startIndex;					
					} 
					
										
					if (next!=null && next.type!=Segment.UNKNOWN && next.start.y<=s.end.y+SMALL_MARGIN && next.end.y>s.end.y+SMALL_MARGIN){
						if (next.unsafe) 
							removeFirstPoint(next, on,s.end.y+SMALL_MARGIN);
						else {
							occupied[ trIndx[currentIndx+1] ] = 0; 
							trSz = remove(next, which, currentIndx+1, trSz);
							if (trSz>currentIndx+1){
								next = trArr[trIndx[currentIndx+1]];
								next = (which==1) ? next.rightSeg : next.leftSeg;
							} else next = null;
							on = (next==null) ? null : next.opp;
						}
						if (next!=null){
							next.updated = true;
							if (next.type!=Segment.UNKNOWN)
								reSynchronize(next,on,0,otherTo,-which,tW*2);
							else {
								int idx = currentIndx+2;
								occupied[ trIndx[currentIndx+1] ] = 0; 
								if ((trSz-=idx)>0) System.arraycopy(trIndx, idx, trIndx, currentIndx+1, trSz);
								trSz+=currentIndx+1;
								on.type = Segment.UNKNOWN;
								if (trSz>currentIndx+1){
									next = trArr[trIndx[currentIndx+1]];
									next = (which==1) ? next.rightSeg : next.leftSeg;
								} else next = null;
								on = (next==null) ? null : next.opp;
							}
						}
					} else if (next!=null && next.type!=Segment.UNKNOWN && (next.endIndex<s.startIndex || next.end.y<=s.end.y+SMALL_MARGIN)){
						int idx = currentIndx+1;
						while (next!=null && next.type!=Segment.UNKNOWN && next.end.y<=s.end.y+SMALL_MARGIN){														
							if (idx<trSz){
								occupied[ trIndx[idx] ] = 0;
								idx++;
								next = (idx>=trSz) ? null : (which==1) ? trArr[ trIndx[idx] ].rightSeg : trArr[ trIndx[idx] ].leftSeg;
							} else next = null;
						}						
						if ((trSz-=idx)>0 && idx!=currentIndx+1) System.arraycopy(trIndx, idx, trIndx, currentIndx+1, trSz);
						trSz+=currentIndx+1;
						idx = currentIndx+1;
						next = (idx>=trSz) ? null : (which==1) ? trArr[ trIndx[idx] ].rightSeg : trArr[ trIndx[idx] ].leftSeg;
						on = (next==null) ? null : next.opp;
						
						if (next!=null && next.type!=Segment.UNKNOWN && (next.endIndex<s.startIndex || (next.start.y<=s.end.y+SMALL_MARGIN && next.end.y>s.end.y+SMALL_MARGIN))){
							if (next.unsafe) 
								removeFirstPoint(next, on, s.end.y+SMALL_MARGIN);
							else {
								occupied[ trIndx[currentIndx+1] ] = 0; 
								trSz = remove(next, which, currentIndx+1, trSz);
								if (trSz>currentIndx+1){
									next = trArr[trIndx[currentIndx+1]];
									next = (which==1) ? next.rightSeg : next.leftSeg;
								} else next = null;
								on = (next==null) ? null : next.opp;
							}
							if (next!=null){
								next.updated = true;
								if (next.type!=Segment.UNKNOWN)
									reSynchronize(next,on,0,otherTo,-which,tW*2);
								else {
									idx = currentIndx+2;
									occupied[ trIndx[currentIndx+1] ] = 0;
									if ((trSz-=idx)>0) System.arraycopy(trIndx, idx, trIndx, currentIndx+1, trSz);
									trSz+=currentIndx+1;
									on.type = Segment.UNKNOWN;		
									if (trSz>currentIndx+1){
										next = trArr[trIndx[currentIndx+1]];
										next = (which==1) ? next.rightSeg : next.leftSeg;
									} else next = null;
									on = (next==null) ? null : next.opp;
								}
							}
						}
					}
														
					s.end.x = first.x;
					reSynchronize(s,os,0,otherTo,-which,tW*2);
					os.updated = true;	
					if (next==null && s.type!=Segment.UNKNOWN && to>s.endIndex+2){
						boolean ok = s.updated || inTurn;
						if (!ok){
							for (int i = s.endIndex+1;i<to;++i){
								if (v[i].certain) {
									ok = true;
									break;
								}
							}
						}

						if (ok){												
							int liSz = bestGuess(v, s.endIndex+1, to, -which*tW, s, null, oalArr,0);												
							if (liSz>0){																																	
								int nextIndx = currentIndx+1;							
								trSz = replaceSideElems(oalArr, 0, liSz, trArr, trIndx,currentIndx+1, nextIndx, trSz, which,tW);							
							}//end of if ok
							os.updated = s.updated;
							s.updated = false;
						}					
					}			
					return;
				}
			}
			if (size>=2 && s.type==0){									
				double total = 1;
				double[] coef = new double[2];
				double a = (first.y-last.y)/d;
				double b = first.y - a*first.x;
				double x1 = (first.y-b)/a;
				double x2 = (last.y-b)/a;						

				double tot = 0;
				for (int kk = lastIndex-1;kk>firstIndex;--kk){
					Vector2D vv = v[kk];
					double e = vv.x-first.x;
					if (e<0) e = -e;
					if (e<E && vv.y-first.y>=1){
						s.type = 0;
						s.start.x = first.x;
						s.end.x = first.x;
						s.end.y = vv.y;
						s.startIndex = firstIndex;
						s.endIndex = kk;
						s.num = kk+1-firstIndex;
						reSynchronize(s,os,0,otherTo,-which,tW*2);
						os.updated = true;
						if (next==null && s.type!=Segment.UNKNOWN && to>s.endIndex+2){
							boolean ok = s.updated || inTurn;
							if (!ok){
								for (int i = s.endIndex+1;i<to;++i){
									if (v[i].certain) {
										ok = true;
										break;
									}
								}
							}

							if (ok){												
								int liSz = bestGuess(v, s.endIndex+1, to, -which*tW, s, null, oalArr,0);												
								if (liSz>0){																																	
									int nextIndx = currentIndx+1;							
									trSz = replaceSideElems(oalArr, 0, liSz, trArr, trIndx,currentIndx+1, nextIndx, trSz, which,tW);							
								}//end of if ok
								os.updated = s.updated;
								s.updated = false;
							}					
						}				
						return;					
					}
					tot += Math.sqrt(Geom.ptLineDistSq(x1, first.y, x2, last.y, vv.x, vv.y, null));							
				}
				tot /= (lastIndex-firstIndex+0.0d);
				if (size>3){							
					total = bestFitLine(v, firstIndex,lastIndex+1, coef);
					double t = (total<tot) ? total : tot;
					if ((t>EPSILON && !(s.lower!=null && s.upper!=null) && currentMap[MAX_RADIUS-1]<3) || (t>=0.5 && tot>=0.5)){								
						s.type = Segment.UNKNOWN;						
						s.radius = -1;
					}
					if (tot>total){
						a = coef[0];
						b = coef[1];
					}
				}


				if (s!=null && s.type!=UNKNOWN) {
					if (size>=3){
						Vector2D vv =  (size==3) ? v[firstIndex+1] : v[(firstIndex+lastIndex)/2];					
						boolean isCircle = Geom.getCircle(first, last, vv, tmp1);
						double rd =  (isCircle) ? Math.sqrt(tmp1[2]) : MAX_RADIUS-1;						
						int rr = (int)rd;
						if (rr>=MAX_RADIUS) rr = MAX_RADIUS-1;						
						if (rr>=MAX_RADIUS-1){
							if (currentMap[rr]==0){
								s.appearedRads[s.radCount++] = rr;
								os.radCount++;
							}
							currentMap[rr]++;
						}
						else if (rr>REJECT_VALUE && rr<MAX_RADIUS-1){							
							if (currentMap[MAX_RADIUS-1]>1) currentMap[MAX_RADIUS-1]--;
							if (currentMap[MAX_RADIUS-1]<=2){
								//								currentMap = null;
								s.type = UNKNOWN;
								if (os!=null) os.type = Segment.UNKNOWN;								
							}							
						}
					}
					s.num =  size;									
					if (s.type==0 && !Double.isInfinite(a)){
						bkupS.copy(s);
						s.start.x = (s.start.y-b)/a;
						s.end.x = (s.end.y-b)/a;
						reSynchronize(s,os,0,otherTo,-which,tW*2);
						//check for intersection with prev seg						
						if (op!=null && op.type!=Segment.UNKNOWN && (op.type!=s.type || s.type==0 || (s.type!=0 && s.radius!=prev.radius)) && op.end.y>=os.start.y-SMALL_MARGIN){
							if (isConfirmed(prev, which, tW)){								
								Segment.removeFirstPoint(os, s, op.end.y+SMALL_MARGIN);
								if (os.type!=Segment.UNKNOWN) 
									reSynchronize(os, s, 0, to, which, tW*2);
								else {				
									s.copy(bkupS);
									reSynchronize(s,os,0,otherTo,-which,tW*2);									
								}
							} else {
								oldTp = prev.type;
								Segment.removeLastPoint(op, prev, os.start.y-SMALL_MARGIN);
								if (op.type!=Segment.UNKNOWN) 
									reSynchronize(op, prev, 0, to, which, tW*2);
								else {
									prev.type = oldTp;
									reSynchronize(prev,op,0,otherTo,-which,tW*2);
									Segment.removeFirstPoint(os, s, op.end.y+SMALL_MARGIN);
									reSynchronize(os, s, 0, to, which, tW*2);
									if (os.type==Segment.UNKNOWN || s.type==Segment.UNKNOWN){
										s.copy(bkupS);
										reSynchronize(s,os,0,otherTo,-which,tW*2);										
									}
								}
							}
						}
					}

				}
			}
		}

//		boolean done = false;		
		if (prev!=null && s!=null && s.type!=Segment.UNKNOWN && s.num>1){			
			int sR = (s.type==0 || s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : (s.type==Segment.UNKNOWN) ? -1 :  (int)Math.round(s.radius+s.type*tW*which);
			if (sR>=MAX_RADIUS) sR = MAX_RADIUS-1;
			if (s.type!=0 && s.type!=Segment.UNKNOWN){
				boolean ok = !s.updated;			
				if (s.updated && s.center!=null){					
					double d = check(v, firstIndex,lastIndex, s.center, s.radius);
					ok = (d>=0);
					if (s.num>2){
						if (!ok && fst!=null && lst!=fst){
//							Vector2D center = tmpCenter; 
//							circle(fst, lst, s.center.x,s.center.y, s.radius,center);
							Vector2D center = circle(fst,lst, s.center, s.radius);
							double d1 = check(v, firstIndex,lastIndex, center, s.radius);
							ok = (d1>=0);
							if (ok) {
//								s.center.copyValue(center);
								s.center = center;
								s.updated = true;
							}
						} else if (!ok){
//							Vector2D center = tmpCenter;
//							circle(v[s.startIndex], v[s.endIndex], center.x,center.y, s.radius,center);
							Vector2D center = circle(v[s.startIndex], v[s.endIndex], s.center, s.radius);
							double d1 = check(v, firstIndex,lastIndex, center, s.radius);
							ok = (d1>=0);
							if (ok) {
//								s.center.copyValue(center);
								s.center = center;
								s.updated = true;
							}
						}
					}

					if (!ok){
						if (currentMap[sR]>1) currentMap[sR]--;
						if (!isConfirmed(s, which, tW) && currentMap[sR]<1){							
							trSz = remove(s, which, currentIndx, trSz);
							if (trSz>currentIndx+1){
								next = trArr[trIndx[currentIndx+1]];
								next = (which==1) ? next.rightSeg : next.leftSeg;
							} else next = null;
							on = (next==null) ? null : next.opp;
							if (trSz>currentIndx){
								s = trArr[trIndx[currentIndx]];
								s = (which==1) ? s.rightSeg : s.leftSeg;
							} else s = null;
							os = (s==null) ? null : s.opp;
						} 													
					} 
//					else bkupS.copy(s);
				}

				/*if (s.type!=Segment.UNKNOWN){
					boolean changed = false;
					double oldR = s.radius;
					Vector2D center = new Vector2D();
					if (s.lower!=null && fst!=null && lst!=fst && prev!=null){
						double rd = radiusFrom2Points(prev, fst, lst, -tW*which, null);
						done = (rd==oldR);
						if (s.type!=0 && (prev.type==s.type && rd==prev.radius) || (next!=null && s.type==next.type && rd==next.radius)){
							circle(fst, lst, s.center.x,s.center.y, rd,center);
							if (check(v,s.startIndex,s.endIndex+1,center,rd)>=0) {
								Segment.apply(s, -which*tW,s.type, s.start, s.end, center, rd);
							} else {
								Segment.apply(s, -which*tW,s.type, fst, lst, center, rd);
								if (s.radius==rd){
									s.start = new Vector2D(fst);
									s.end = new Vector2D(lst);									
								}
							}
							oldR = s.radius;
							changed = true;
						}
					} 
					if (!done && s.upper!=null && fst!=null && lst!=fst && next!=null){
						double rd = radiusFrom2Points(next, fst, lst, -tW*which, null);
						done = (rd==oldR);
						if (s.type!=0 && (prev!=null && prev.type==s.type && rd==prev.radius) || (next!=null && s.type==next.type && rd==next.radius)){
							circle(fst, lst, s.center.x,s.center.y, rd,center);
							if (check(v,s.startIndex,s.endIndex+1,center,rd)>=0) {
								Segment.apply(s, -which*tW,s.type, s.start, s.end, center, rd);
							} else {
								Segment.apply(s, -which*tW,s.type, fst, lst, center, rd);
								if (s.radius==rd){
									s.start = new Vector2D(fst);
									s.end = new Vector2D(lst);									
								}
							}							
							changed = true;
						}
					} 										

					if (done){
						currentMap[sR]++;
					} else if (li>=fi+2){						
						double[] mp = tmpR;
						int[] appear = tmpAppear;
						int[] map = tmpAMap;
						int[] startIndx = tmpOStartIdx;
						int[] endIndx = tmpOEndIdx;
						int sz = 0;						
						boolean good = false;						
						for (int i = fi;i<li-1;++i){
							Vector2D p1 = nv[i];
							for (int k = li;k>=i+2;--k){
								Vector2D p3 = nv[k];
								for (int j = i+1;j<k;++j){
									Vector2D p2 = nv[j];

									boolean isCircle = (inTurn) ? allRadius[i][j][k]>0: Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, result);
									double radius = (isCircle) ? (inTurn) ? allRadius[i][j][k] : Math.sqrt(result[2]) : Double.MAX_VALUE;									
									int r = 0;
									if (radius>=MAX_RADIUS) {
										radius = 0;
										r = 0;
										continue;
									} else {
										r = (int)Math.round(radius-tW);
										radius = r+tW;
										r = (int)radius;
									}
									if (map[r]==0){
										appear[sz] = r; 
										startIndx[sz] = i;
										endIndx[sz] = k;
										mp[sz++] = radius;
									}
									map[r]++;
									
									if (map[r]>1 && radius==s.radius) {
										done = true;
										break;
									}
									
									double cx = (inTurn) ? allCntrx[i][j][k] : result[0] ;
									double cy = (inTurn) ? allCntry[i][j][k] : result[1] ;									
									int tp = 0;
									if (inTurn){
										tp = allTurn[i][j][k];
									} else if (r<MAX_RADIUS-1){										
										double ax = p1.x-cx;
										double ay = p1.y-cy;
										double bx = p3.x-cx;
										double by = p3.y-cy;
										double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);			
										if (angle<-Math.PI) 
											angle += 2*Math.PI;
										else if (angle>Math.PI) 
											angle -= 2*Math.PI;

										tp = (angle<0) ? -1 : 1;										
									} else {
										r = 0;
										tp = 0;
									}
									int rr = (tp==0) ?  MAX_RADIUS-1 : (int)Math.round(r+tp*tW*which);
									if (rr>=MAX_RADIUS) {
										rr = MAX_RADIUS-1;
										tp = 0;
									} else circle(p1, p3, cx,cy, radius,center);
									
									if (map[r]>1){
										boolean found = false;
										for (int ii=sz-1;ii>=0;--ii){
											int rd = appear[ii];
											if (rd==r){
												if (i==startIndx[ii] && k==endIndx[ii]){
													found = true;
													break;
												}
											}
										}
										if (found) continue;
									}
									
									if (tp!=s.type)
										if (currentMap[sR]>3) currentMap[sR]--;
									else if (map[r]>2) {	
										if (r!=s.radius && map[r]>3) 
											if (currentMap[sR]>3) currentMap[sR]--;
//										Segment.apply(s, -which*tW, p1, p3, center, r);
										continue;
									}

									if (r<MAX_RADIUS-1){																				
										if (radius==s.radius){
											if (s!=null && currentMap[sR]==0){
												s.appearedRads[s.radCount++] = sR;
												if (os!=null) os.radCount = s.radCount;
											}
											currentMap[sR]++;
											good = true;
										}
										boolean isPConnected = (prev!=null && radiusFrom2Points(prev, p1, p3, -tW*which, null)==radius);
										boolean isNConnected = (next!=null && radiusFrom2Points(prev, p1, p3, -tW*which, null)==radius);
										if (isPConnected || isNConnected){
											good = true;
											if (s.radius==radius){
												if (isPConnected){
													done = true;
													break;
												} else if (isNConnected){
													done = true;
													break;
												}
											} else {
												if (isPConnected && isNConnected){
													if (currentMap[sR]>0) currentMap[sR]--;
													if (currentMap!=null && currentMap[sR]==0){
														for (int ii = s.radCount-1;ii>=0;--ii){
															int rads = s.appearedRads[ii];
															if (rads==sR){
																if (s.radCount-ii-1>0) System.arraycopy(s.appearedRads, ii+1, s.appearedRads, ii, s.radCount-ii-1);
																s.radCount--;
																if (os!=null) os.radCount = s.radCount;
																break;
															}
														}
													}
												}
												if (map[r]>2 || map[sR]==0) {
													Segment.apply(s, -which*tW,s.type, s.start, s.end, center, radius);
													changed = true;
												}
											}
										} 
									}
									if (done) break;
								}
								if (done) break;
							}
							if (done) break;
						}// end of for

						if (!good){
							int max = -1;
							double rd = 0;

							for (int ii = sz-1;ii>=0;--ii){
								int er = appear[ii];
								if (max<map[er]){
									max = map[er];									
									rd = mp[ii];
								}
								map[er] = 0;
							}
							if (rd!=0 && rd<Segment.MAX_RADIUS-1){
								circle(fst, lst, s.center.x,s.center.y, rd,center);															
								if (max>1) {
									if (currentMap[sR]>2)currentMap[sR]--;									
								}
								if (max>2){
									if (currentMap[sR]>2) currentMap[sR] = 2;
									Segment.apply(s, -which*tW,s.type, s.start, s.end, center, rd);
									changed = true;
								}																	
							}
						} else {
							for (int ii = sz-1;ii>=0;--ii){
								int er = appear[ii];								
								map[er] = 0;
							}
						}
						
						if (changed){
							int indx = (s.startIndex>=s.endIndex+1) ? s.startIndex : binarySearchFromTo(v, s.start, s.startIndex, s.endIndex);
							if (indx<0) indx = -indx-1;
							if (indx>0 && v[indx-1].y>s.start.y-SMALL_MARGIN) indx--;

							int j = (indx>=s.endIndex) ? indx-1 :binarySearchFromTo(v, s.end, indx,s.endIndex);			
							j = (j<0) ? -j-1 : j+1;
							if (j<=s.endIndex && v[j].y<s.end.y+SMALL_MARGIN) j++;
							s.startIndex = indx;
							s.endIndex = j-1;		
							s.num = j - indx;

							reSynchronize(s,os,0,otherTo,-which,tW*2);
														
							if (op!=null && op.type!=Segment.UNKNOWN && (op.type!=s.type || s.type==0 || (s.type!=0 && s.radius!=prev.radius)) && (op.endIndex>os.startIndex || op.end.y>=os.start.y-SMALL_MARGIN)){
								if (isConfirmed(prev, which, tW)){								
									Segment.removeFirstPoint(os, s, op.end.y+SMALL_MARGIN);
									if (os.type!=Segment.UNKNOWN) 
										reSynchronize(os, s, 0, to, which, tW);
									else {				
										s.copy(bkupS);
										reSynchronize(s,os,0,otherTo,-which,tW);									
									}
								} else {
									oldTp = prev.type;
									Segment.removeLastPoint(op, prev, os.start.y-SMALL_MARGIN);
									if (op.type!=Segment.UNKNOWN) 
										reSynchronize(op, prev, 0, to, which, tW);
									else {
										prev.type = oldTp;
										reSynchronize(prev,op,0,otherTo,-which,tW);
										Segment.removeFirstPoint(os, s, op.end.y+SMALL_MARGIN);
										reSynchronize(os, s, 0, to, which, tW);
										if (os.type==Segment.UNKNOWN || s.type==Segment.UNKNOWN){
											s.copy(bkupS);
											reSynchronize(s,os,0,otherTo,-which,tW);										
										}
									}
								}
							}

							os.done = false;
						}
					}// end of if
				}
		//*/
			}
		}//End of update

		
		if (s!=null && s.type==Segment.UNKNOWN){
//			for (int i = s.radCount-1;i>=0;--i){
//				int rd = s.appearedRads[i];
//				currentMap[rd] = 0;
//				s.appearedRads[i] = 0;
//			}
//			s.radCount = 0;
//			os.radCount = 0;			
			if (s.upper!=null){				
				s.upper = null;
				os.upper = null;				
			}
			if (next!=null) {
				next.lower = null;
				on.lower = null;
			}
			if (s.lower!=null){				
				s.lower = null;
				os.lower = null;
			}
			if (prev!=null) {
				prev.upper = null;
				op.upper = null;
			}
			//			s.start = new Vector2D(v[s.startIndex]);
			//			s.end = new Vector2D(v[s.endIndex]);
			if (os!=null) os.type = Segment.UNKNOWN;		
//			if (prev!=null && prev.num>0 && prev.type!=0 && prev.endIndex+1==s.startIndex) {
//				Segment prevprev = (currentIndx>1) ? trArr[ trIndx[currentIndx-2] ] : null;
//				Segment seg = (prevprev==null) ? null : (which==1) ? prevprev.rightSeg : prevprev.leftSeg;
//				reExpand(seg, prev, next, which, tW);
//				if (seg!=null && seg.type==Segment.UNKNOWN){
//					occupied[ trIndx[currentIndx-2] ] = 0;
//					System.arraycopy(trIndx, currentIndx-1, trIndx, currentIndx-2, trSz-currentIndx+1);
//					trSz--;					
//				}
//			}
//			
//			if (next!=null && next.num>0 && next.type!=0 && s.endIndex+1==next.startIndex) {
//				Segment nextNext = (currentIndx<trSz-2) ? trArr[ trIndx[currentIndx+2] ] : null;
//				Segment seg = (nextNext==null) ? null : (which==1) ? nextNext.rightSeg : nextNext.leftSeg;
//				reExpand(prev, next,seg, which, tW);
//			}
		} 
		boolean isConfirm = s!=null && s.type!=Segment.UNKNOWN && isConfirmed(s, which, tW);
		if (s!=null && s.type!=0 && isConfirm) reExpand(prev, s, next, which, tW);
		
		////////////////////////////////FILL GAP FROM HERE //////////////////////////////////
		int prevIndx = currentIndx-1;
		while (prev!=null && prev.type==Segment.UNKNOWN){
			prev = (prevIndx<=0) ? null : (which==1) ? trArr[ trIndx[--prevIndx] ].rightSeg : trArr[ trIndx[--prevIndx] ].leftSeg; 
		}
		op = (prev==null) ? null : prev.opp;
		int nextIndx = currentIndx+1;
		while (next!=null && next.type==Segment.UNKNOWN){
			next = (nextIndx>=trSz-1) ? null : (which==1) ? trArr[ trIndx[++nextIndx] ].rightSeg : trArr[ trIndx[++nextIndx] ].leftSeg; 
		}
		on = (next==null) ? null : next.opp;
		if (isConfirm && s!=null && s.type!=Segment.UNKNOWN){			
			int sz = 0;			
			salArr[0] = s;

			int psz = 0;
			Segment[] tmp = tmpStore;
			if (prev!=null && prev.endIndex<s.startIndex-2 && s.lower==null && !(prev.type==s.type && prev.radius==s.radius)){
				boolean ok = inTurn || s.updated || prev.updated;
				if (!ok){
					for (int i = prev.endIndex+1;i<s.startIndex;++i){
						if (v[i].certain) {
							ok = true;
							break;
						}
					}
				}
				if (ok){									
					psz = bestGuess(v, prev.endIndex+1, s.startIndex, -which*tW, prev, s, oalArr,0);
					if (prev!=null && prev.type!=Segment.UNKNOWN){
						if (prev.end.y>=s.start.y-SMALL_MARGIN){		
							Segment.removeFirstPoint(s, os, prev.end.y+SMALL_MARGIN);
							reSynchronize(s,os,0,otherTo,-which,tW*2);
						} else if (op.end.y>=os.start.y-SMALL_MARGIN){
							Segment.removeFirstPoint(os, s,op.end.y+SMALL_MARGIN);
							reSynchronize(os,s,0,to,which,tW*2);
						}
					}
					if (psz>0){																	
						System.arraycopy(oalArr, 0, tmp, 0, psz);
						sz+=psz;
					} 					
				}
			}

			tmp[sz++] = s;

			int nsz = 0;
			if (((next==null && s.endIndex<to-2) || (next!=null && s.endIndex<next.startIndex-2 && next.lower==null && next.type!=Segment.UNKNOWN && Segment.isConfirmed(next,which,tW) && !(next.type==s.type && next.radius==s.radius)))){
				boolean ok = inTurn || s.updated || (next!=null && next.updated);
				int endIndx = (next==null) ? to : next.startIndex;
				if (!ok){					
					for (int i = s.endIndex+1;i<endIndx;++i){
						if (v[i].certain) {
							ok = true;
							break;
						}
					}
				}
				if (ok){													
					nsz = bestGuess(v, s.endIndex+1, endIndx, -which*tW, s, next, oalArr,sz);					
					if (nsz>sz){												
						System.arraycopy(oalArr, sz, tmp, sz, nsz-sz);
						sz = nsz;												
					} 					
				}
			}						

			int oldSz = sz;					
			if (sz>1){
				sz = adjust(tmp,sz);			
				trSz = replaceSideElems(tmp, 0, sz, trArr, trIndx, prevIndx+1, nextIndx, trSz, which,tW);
				for (int ii = oldSz-1;ii>=0;--ii){
					Segment ss = oalArr[ii];
					ss.map = null;
					ss.radCount = 0;
					ss.appearedRads = null;
					ss.opp.map = null;
					ss.opp.radCount = 0;
					ss.opp.appearedRads = null;
				}
			}
			return;						
		} else if (s!=null && s.type!=Segment.UNKNOWN){//s is not confirmed
			if (prev==null && next==null && !CircleDriver2.inTurn && s.endIndex<to-2){				
				int liSz = bestGuess(v, s.endIndex+1, to, -which*tW, s, null, oalArr,0);				
				if (liSz>1) liSz = adjust(oalArr,liSz);						
				if (liSz>0)	trSz = replaceSideElems(oalArr, 0, liSz, trArr,trIndx,currentIndx+1, nextIndx, trSz, which,tW);
				return;
			} else if (s.lower!=null || s.upper!=null){
				firstIndex = s.startIndex;
				if (s.lower!=null && firstIndex>0){
					if (s.points!=null){
						while (firstIndex>0 && s.points[firstIndex-1].y>=s.lower.y) firstIndex--;
					}
				} else if (firstIndex>0){
					if (prev!=null) 
						firstIndex = prev.endIndex+1;
					else firstIndex = 0;
				}
				lastIndex = s.endIndex;
				if (s.upper!=null && lastIndex<to-1){
					if (s.points!=null){
						while (lastIndex<to-1 && s.points[lastIndex+1].y<=s.upper.y) lastIndex++;
					}
				} else if (lastIndex<to-1){
					if (next!=null){
						lastIndex = next.startIndex-1;
					} else lastIndex = to-1;
				}
			}

		}


		int salSz = 0;					

		if (s==null || s.type==Segment.UNKNOWN || (s.lower==null && s.upper==null)){
//			if (s!=null && s.num<2 && os.num<2){
//				firstIndex = (prev==null) ? 0 : prev.endIndex+1;
//				lastIndex = s.endIndex;				
//				if (s!=null && s.type!=Segment.UNKNOWN) salArr[salSz++] = s;
//			} else {
				if (s!=null && s.type!=Segment.UNKNOWN) salArr[salSz++] = s;
				firstIndex = (prev==null) ? (s==null) ? 0 : s.startIndex : prev.endIndex+1;
				lastIndex = (next==null || s==null) ? to-1 : next.startIndex-1;			
//				if (!CircleDriver2.inTurn && next!=null && !isConfirmed(next, which, tW) && next.lower==null && next.upper==null) {
//					salArr[salSz++] = next;
//					Segment nnext = (nextIndx+1<trSz) ? trArr[ trIndx[nextIndx+1] ] : null;  
//					next = (nnext==null) ? null : (which==1) ? nnext.rightSeg : nnext.leftSeg;
//					lastIndex = (next==null) ? to - 1: next.startIndex-1;
//					nextIndx++;				
//				}
//			}
		} else if (s!=null && s.type!=Segment.UNKNOWN) salArr[salSz++] = s;

		if (lastIndex-firstIndex+1<2) return;
		int liSz = bestGuess(v, firstIndex, lastIndex+1, -which*tW, prev, next, oalArr,0);
//		if (prev!=null && s!=null && prev.type!=Segment.UNKNOWN && prev.updated &&  (prev.endIndex>=s.startIndex || prev.end.y>=s.start.y)){
//			removeFirstPoint(s, s.opp, null, prev.end);
//			if (s.type==Segment.UNKNOWN && s.opp!=null) s.opp.type = Segment.UNKNOWN;
//		}
		if (prev!=null && s!=null && prev.type!=Segment.UNKNOWN && (prev.endIndex>=s.startIndex || prev.end.y>=s.start.y)){
			if (liSz>0){
				Segment oalFirst = oalArr[0];
				if (oalFirst.type==Segment.UNKNOWN){
					for (int ii=1;ii<liSz;++ii)
						if (oalArr[ii].type!=Segment.UNKNOWN){
							oalFirst = oalArr[ii];
							break;
						}
				}
				if (oalFirst.type!=Segment.UNKNOWN){
					for (int ii = s.radCount-1;ii>=0;--ii){
						int rr = s.appearedRads[ii];
						currentMap[rr] = 0;
						s.appearedRads[ii] = 0;
					}
					s.radCount = 0;
					os.radCount = 0;
					s.copy(oalFirst,-which*tW);
					os.copy(oalFirst.opp);
				}
			} else {
				s.type = Segment.UNKNOWN;
				os.type = Segment.UNKNOWN;
			}
		} else if (next!=null && s!=null && next.type!=Segment.UNKNOWN && (s.endIndex>=next.startIndex || s.end.y>=next.start.y)){
			if (liSz>0){
				Segment oalFirst = oalArr[0];
				if (oalFirst.type==Segment.UNKNOWN){
					for (int ii=1;ii<liSz;++ii)
						if (oalArr[ii].type!=Segment.UNKNOWN){
							oalFirst = oalArr[ii];
							break;
						}
				}
				if (oalFirst.type!=Segment.UNKNOWN){
					for (int ii = s.radCount-1;ii>=0;--ii){
						int rr = s.appearedRads[ii];
						currentMap[rr] = 0;
						s.appearedRads[ii] = 0;
					}
					s.radCount = 0;
					os.radCount = 0;
					s.copy(oalFirst,-which*tW);
					os.copy(oalFirst.opp);
				}
			} else {
				s.type = Segment.UNKNOWN;
				os.type = Segment.UNKNOWN;
			}
		} else if (liSz==0 && s!=null && s.type==Segment.UNKNOWN && os.done){
			s.type = oldTp;
			os.type = oldTp;
			s.unsafe = false;
			os.unsafe = false;
		}

		int oldSz = liSz;
		if (liSz>0){ 
			if (salSz>0) 				
				liSz = mix(salArr,salSz, oalArr,liSz, which, tW,tmpStore);
			else System.arraycopy(oalArr, 0, tmpStore, 0, liSz);										
			if (liSz>1) liSz = adjust(tmpStore,liSz);	
			if (trSz>=0) trSz = replaceSideElems(tmpStore, 0, liSz, trArr, trIndx, prevIndx+1, nextIndx, trSz, which,tW);
			for (int ii = oldSz-1;ii>=0;--ii){
				Segment ss = oalArr[ii];
				ss.map = null;
				ss.radCount = 0;
				ss.appearedRads = null;
				ss.opp.map = null;
				ss.opp.radCount = 0;
				ss.opp.appearedRads = null;
			}			
		}				
	}
	
	private static final void reExpand(Segment prev,Segment s,Segment next,int which, double tW){
		long ti = System.nanoTime();
		int n = 0;
		if (CircleDriver2.debug) System.out.println("Start reExpand : "+(System.nanoTime()-ti)/1000000);		
		int from = (prev==null) ? 0 : prev.endIndex+1;
//		int from = s.startIndex;
		EdgeDetector edge = CircleDriver2.edgeDetector;
		int maxTo = (which==-1) ? edge.lSize : edge.rSize;
		int to = (next==null) ? maxTo : next.startIndex;
//		int to = s.endIndex+1;
		int otherTo = (which==1) ? edge.lSize : edge.rSize;
		Segment os = s.opp;
		Segment op = (prev==null) ? null : prev.opp;
		Segment on = (next==null) ? null : next.opp;
		Storage storage = (which==1) ? CircleDriver2.rMap : CircleDriver2.lMap;
		double tw = tW+tW;
		double applyTW = -which*tW;
		Vector2D[] v = s.points;		
		if (prev==null) {
			s.lower = null;
			os.lower = null;
		}
		if (next==null) {
			s.upper = null;
			os.upper = null;
		}		
//		Segment tmpSeg = new Segment();		
		tmpSeg.points = v;
//		Segment tmpSeg1 = new Segment();
		Segment tmpSeg1 = tmpPrev;
		tmpSeg1.points = v;
		if (s!=null){			
			if (prev!=null && prev.upper==null && prev.num>0 && prev.type!=0 && prev.type!=Segment.UNKNOWN && prev.center.y!=0 && s.type!=0 && s.type!=Segment.UNKNOWN){
				double dx = s.center.x - prev.start.x;
				double dy = s.center.y - prev.start.y;
				double d = Math.sqrt(dx*dx+dy*dy);
				d-=s.radius;
				if (d<0) d=-d;
				if (d<=EPS && Segment.check(v, prev.startIndex, s.endIndex+1, s.center, s.radius)>=0){
					tmpSeg.type = Segment.UNKNOWN;
					boolean good = false;
					if ((good = isBelong(s, v, prev.startIndex, s.endIndex+1, tmpSeg, which, tW)) || tmpSeg.type!=Segment.UNKNOWN){
						if (good || (tmpSeg.type==prev.type && tmpSeg.type!=0 && tmpSeg.radius==prev.radius && Math.abs(tmpSeg.center.y-prev.center.y)<1)){
							Segment.apply(s, applyTW, tmpSeg.type, tmpSeg.start, tmpSeg.end, tmpSeg.center, tmpSeg.radius);
							if (s.radius==tmpSeg.radius){
								s.startIndex = tmpSeg.startIndex;
								s.endIndex = tmpSeg.endIndex;
								s.num = tmpSeg.num;
								reSynchronize(s,os,0,otherTo,-which,tw);
								s.lower = null;
								os.lower = null;										
								prev.type = Segment.UNKNOWN;
								op.type = Segment.UNKNOWN;
							}															
							
						}
					}
				}
			}
			
			if (next!=null && next.lower==null && next.num>0 && next.type!=0 && next.type!=Segment.UNKNOWN && s.type!=0 && s.type!=Segment.UNKNOWN){
				double dx = s.center.x - next.end.x;
				double dy = s.center.y - next.end.y;
				double d = Math.sqrt(dx*dx+dy*dy);
				d-=s.radius;
				if (d<0) d=-d;
				if (d<=EPS && Segment.check(v, s.startIndex, next.endIndex+1, s.center, s.radius)>=0){
					tmpSeg.type = Segment.UNKNOWN;
					boolean good = false;
					if ((good = isBelong(s, v, s.startIndex, next.endIndex+1, tmpSeg, which, tW)) || tmpSeg.type!=Segment.UNKNOWN){
						if (good || (tmpSeg.type==next.type && tmpSeg.type!=0 && tmpSeg.radius==next.radius && Math.abs(tmpSeg.center.y-next.center.y)<1)){
							Segment.apply(s, applyTW, tmpSeg.type, tmpSeg.start, tmpSeg.end, tmpSeg.center, tmpSeg.radius);
							if (s.radius==tmpSeg.radius){
								s.startIndex = tmpSeg.startIndex;
								s.endIndex = tmpSeg.endIndex;
								s.num = tmpSeg.num;
								reSynchronize(s,os,0,otherTo,-which,tw);
								s.upper = null;
								os.upper = null;							
								next.type = Segment.UNKNOWN;
								on.type = Segment.UNKNOWN;
							}									
						}
					}
				}
			}
		}		
		if (prev!=null && prev.type!=Segment.UNKNOWN && s.lower!=null && s.lower.y>=prev.end.y){
			int fst = (from>=to) ? from : binarySearchFromTo(v, s.lower, from, to-1);
			if (fst<0) fst = -fst-1;
			if (fst>0 && v[fst-1].y>s.lower.y-SMALL_MARGIN) fst--;
			from = fst;			
		} else from = (prev==null) ? 0 : (prev.type==Segment.UNKNOWN) ? s.startIndex : prev.endIndex+1;
		
		if (next!=null && next.type!=Segment.UNKNOWN && s.upper!=null && s.upper.y<=next.start.y){
			int j = (from>=to-1) ? from-1 :binarySearchFromTo(v, s.upper, from, to-1);			
			j = (j<0) ? -j-1 : j+1;
			if (j<to && v[j].y<s.upper.y+SMALL_MARGIN) j++;
			to = j;			
		} else to = (next==null) ? maxTo : (next.type==Segment.UNKNOWN) ? s.endIndex+1 : next.startIndex;
		
		boolean found = true;		
		if ((from<s.startIndex || to>s.endIndex+1) && to-from>1){
			found = false;
//			Vector2D pt = new Vector2D();
			Segment rs = new Segment();
			int endIndex = s.endIndex;
			if (endIndex<=s.startIndex) endIndex = s.startIndex;
			boolean isConfirm = s.type!=0 && isConfirmed(s, which, tW);
			boolean isPrevConfirm = prev!=null && isConfirmed(prev, which, tW);
			bkupS.copy(s);
			int SIZE_N = storage.SIZE_N;
			int[] totalRad_N = storage.totalRad_N;
			int[] mapIndx = storage.mapIndx;			
			int[][] allMaps = Storage.allMaps;			
			for (int i = from;i<=s.startIndex;++i){
				Vector2D first = v[i];
				double startX = first.x;
				double startY = first.y;
				if (prev!=null && startY-prev.end.y<=SMALL_MARGIN) continue;				
				boolean isPrevFTCn = false;
				boolean ok = true;
				if (isConfirm){
					double dx = s.center.x - first.x;
					double dy = s.center.y - first.y;
					double d = Math.sqrt(dx*dx+dy*dy);
					d-=s.radius;
					if (d<0) d=-d;					
					if (prev!=null && (isPrevConfirm || prev.upper!=null)){
						tmpSeg1.type = Segment.UNKNOWN;
						radiusFrom2Points(prev, first, v[endIndex], applyTW, tmpSeg);						
						if (tmpSeg.type==s.type && (tmpSeg.type==0 || tmpSeg.radius==s.radius)){
							ok = false;							
							tmpSeg.type = Segment.UNKNOWN;
						} 
						if (to-1>endIndex && ok){
							radiusFrom2Points(prev, first, v[to-1], applyTW, tmpSeg1);
							if (tmpSeg1.type==s.type && (tmpSeg1.type==0 || tmpSeg1.radius==s.radius)){
								ok = false;
								isPrevFTCn = true;								
							} else if (tmpSeg1.type==tmpSeg.type && tmpSeg1.type==s.type && (tmpSeg1.type==0 || tmpSeg1.radius==tmpSeg.radius)){
								Segment.apply(s, applyTW, s.type, s.start, s.end, tmpSeg.center, tmpSeg.radius);
								if (s.radius==tmpSeg.radius){
									ok = false;
									isPrevFTCn = true;
								}
							}							
						}
						tmpSeg.type = Segment.UNKNOWN;
					}					
					if (ok && (d>EPS || (s.center.y==0 && d>E)) ) continue;
				}
				int r_index = i<<SIZE_N;
				for (int j = to;j>endIndex;--j){
					Vector2D last = v[j-1];
					double endX = last.x;
					double endY = last.y;
					if (isConfirm){
						double dx = s.center.x - last.x;
						double dy = s.center.y - last.y;
						double d = Math.sqrt(dx*dx+dy*dy);
						d-=s.radius;
						if (d<0) d=-d;
						if (!ok && !isPrevFTCn){
							radiusFrom2Points(prev, first, last, applyTW, tmpSeg);
							if (tmpSeg.type==s.type && (tmpSeg.type==0 || tmpSeg.radius==s.radius)){
								ok = false;								
								tmpSeg.type = Segment.UNKNOWN;
							} else if (tmpSeg1.type==tmpSeg.type && tmpSeg1.type==s.type && (tmpSeg1.type==0 || tmpSeg1.radius==tmpSeg.radius)){
								Segment.apply(s, applyTW, s.type, s.start, s.end, tmpSeg.center, tmpSeg.radius);
								if (s.radius==tmpSeg.radius){
									ok = false;
									isPrevFTCn = true;
								}
							} else ok = true;
						}						
						if (ok && (d>EPS || (s.center.y==0 && d>E)) ) continue;
					}
					
					int m_indx = r_index + j-1;
					int total = totalRad_N[m_indx];
					boolean donePrev = total>=j-i-2;					
					int map_indx = mapIndx[m_indx];					
					int[] map = (map_indx>=0) ? allMaps[map_indx] : null;															
					
					n++;
					int num = j-i;
					if (i==s.startIndex && j==s.endIndex+1) break;
					rs.type = Segment.UNKNOWN;
					rs.startIndex = i;
					rs.endIndex = j-1;
					rs.num = num;
					if (donePrev && map!=null){
						int er = (s.type==0) ? 0 : (int)Math.round(s.radius+which*s.type*tW);
						if (er>=Segment.MAX_RADIUS-1) {
							er = 0;
							s.type = 0;
						}
						if (s.type==1) er+=Segment.MAX_RADIUS;
						if (map[er]>0) {
							found = true;
							rs.type = s.type;
							rs.radius = s.radius;
							rs.start = first;
							rs.end = last;
							if (rs.type!=0){
								if (rs.center==null) 
									rs.center = new Vector2D(); 
								Segment.circle(rs.start, rs.end, s.center.x,s.center.y, rs.radius,rs.center);
							}
						}
					}
					found =  !found && !donePrev && num>2 && isBelong(s,v, i, j, rs, which, tW) || rs.type!=Segment.UNKNOWN;
					tmpSeg.type = Segment.UNKNOWN;
					tmpSeg1.type = Segment.UNKNOWN;
					
					if (num>2 && !found){
						if (prev!=null && prev.type!=Segment.UNKNOWN){
//							if (prev.updated || storage.getRadiusFrom2Point(i, j-1)<0){ 
//								radiusFrom2Points(prev, first, last, applyTW, tmpSeg);								
//								storage.store(i, i, j-1, tmpSeg.type, tmpSeg.radius);
//							} else {
//								storage.toSegment(v, i, i, j-1, tmpSeg);
//								radiusFrom2Points(prev, first, last, applyTW, tmpSeg1);
//								if (tmpSeg.type!=tmpSeg1.type || tmpSeg.radius!=tmpSeg1.radius)
//									System.out.println();
//								tmpSeg1.type = Segment.UNKNOWN;
//							}							
							radiusFrom2Points(prev, first, last, applyTW, tmpSeg);
							
							if (tmpSeg.type!=Segment.UNKNOWN ){
								if (donePrev && map!=null){
									int er = (tmpSeg.type==0) ? 0 : (int)Math.round(tmpSeg.radius+which*tmpSeg.type*tW);
									if (er>=Segment.MAX_RADIUS-1) {
										er = 0;
										tmpSeg.type = 0;
									}
									if (tmpSeg.type==1) er+=Segment.MAX_RADIUS;
									if (map[er]>0) {
										found = true;										
									}
								} else {
									boolean good = true;
									if (tmpSeg.type!=0){
										double cnx = tmpSeg.center.x;
										double cny = tmpSeg.center.y;																
										for (int ii = i;ii<j;++ii){								
											Vector2D vv = v[ii];
											double dx = vv.x-cnx;
											double dy = vv.y-cny;			
											double e = Math.sqrt(dx*dx+dy*dy)-tmpSeg.radius;
											if (e<0) e = -e;
											if (e>EPS) {
												good = false;
												break;
											}
										}
									} else {																		
										double tot = 0;
										for (int ii = i;ii<j;++ii){								
											Vector2D vv = v[ii];										
											double e = Math.sqrt(Geom.ptLineDistSq(startX, startY, endX, endY, vv.x, vv.y, null));
											if (e<0) e = -e;
											tot+=e;
											if (e>EPS) {
												good = false;
												break;
											}
										}
		
										tot/=num;
										if (tot>E) good = false;
									}
									if (!good){
										tmpSeg.type = Segment.UNKNOWN;									
									}
								}
							}
						}//end of if prev
	
						if (!found && next!=null && next.type!=Segment.UNKNOWN){							
							radiusFrom2Points(next, first, last, applyTW, tmpSeg1);
							if (tmpSeg1.type!=Segment.UNKNOWN ){
								if (donePrev && map!=null){
									int er = (tmpSeg1.type==0) ? 0 : (int)Math.round(tmpSeg1.radius+which*tmpSeg1.type*tW);
									if (er>=Segment.MAX_RADIUS-1) {
										er = 0;
										tmpSeg1.type = 0;
									}
									if (tmpSeg1.type==1) er+=Segment.MAX_RADIUS;
									if (map[er]>0) {
										found = true;										
									}
								} else {
									boolean good = true;
									if (tmpSeg1.type!=0){
										double cnx = tmpSeg1.center.x;
										double cny = tmpSeg1.center.y;																
										for (int ii = i;ii<j;++ii){								
											Vector2D vv = v[ii];
											double dx = vv.x-cnx;
											double dy = vv.y-cny;			
											double e = Math.sqrt(dx*dx+dy*dy)-tmpSeg1.radius;
											if (e<0) e = -e;
											if (e>EPS) {
												good = false;
												break;
											}
										}
									} else if (s.type!=0){																		
										double tot = 0;
										for (int ii = i;ii<j;++ii){								
											Vector2D vv = v[ii];										
											double e = Math.sqrt(Geom.ptLineDistSq(startX, startY, endX, endY, vv.x, vv.y, null));
											if (e<0) e = -e;
											tot+=e;
											if (e>EPS) {
												good = false;
												break;
											}
										}
		
										tot/=num;
										if (tot>E) good = false;
									}
									if (!good){
										tmpSeg1.type = Segment.UNKNOWN;									
									}
								}
							}
						}//end of if next
	
						if (!found && (tmpSeg.type!=Segment.UNKNOWN || tmpSeg1.type!=Segment.UNKNOWN) ){						
							rs.type = Segment.UNKNOWN;
							found =  isBelongToEither(tmpSeg,tmpSeg1,v, i, j, rs, which, tW) || rs.type!=Segment.UNKNOWN;						
						}//end of if normal
					}
					
					if (num==2 && !found){
						if (prev!=null && s.type!=0){
							radiusFrom2Points(prev, first, last, applyTW, rs);
							if (rs.type==s.type && rs.radius==s.radius && Math.abs(rs.center.y-s.center.y)<1){								
								found = true;
							}
						}
						
						if (!found && next!=null && s.type!=0){
							rs.type = Segment.UNKNOWN;
							radiusFrom2Points(next, first, last, applyTW, rs);
							if (rs.type==s.type && rs.radius==s.radius && Math.abs(rs.center.y-s.center.y)<1){								
								found = true;
							}
						}
						
					}

					if (found){
						boolean isPossiblePrevConnected = tmpSeg.type==Segment.UNKNOWN || (tmpSeg.type==rs.type && (tmpSeg.type==0 || tmpSeg.radius==rs.radius));
						boolean isPossibleNextConnected = tmpSeg1.type==Segment.UNKNOWN || (tmpSeg1.type==rs.type && (tmpSeg1.type==0 || tmpSeg1.radius==rs.radius));
//						if (s.radius!=rs.radius){
//							s.start = new Vector2D(first);
//							s.end = new Vector2D(last);
//						}
						Segment.apply(s, applyTW, rs.type, first, last, rs.center, rs.radius);
						if (s.radius==rs.radius){
							s.startIndex = i;
							s.endIndex = j-1;
							s.num = num;
						}
						storage.store(i, i, j-1, s.type, s.radius);
						reSynchronize(s,os,0,otherTo,-which,tw);
												
						if (prev!=null && isPossiblePrevConnected && isConnected(prev, s, applyTW, pt) && pt.y>prev.start.y && pt.y<s.end.y) {																														
							if ((prev.type!=s.type || prev.radius!=s.radius))				
								connect(prev, s, pt, applyTW);
						}

						if (next!=null && isPossibleNextConnected && s.type!=Segment.UNKNOWN && next.type!=Segment.UNKNOWN && isConnected(s, next, applyTW, pt) && pt.y>s.start.y && pt.y<next.end.y) {																														
							if ((next.type!=s.type || next.radius!=s.radius))				
								connect(s, next, pt, applyTW);
						}
						
						if (op!=null && op.type!=Segment.UNKNOWN && s.type!=Segment.UNKNOWN && (op.endIndex>os.startIndex || op.end.y>=os.start.y-SMALL_MARGIN)){
							if (isConfirmed(prev, which, tW)){								
								Segment.removeFirstPoint(os, s, op.end.y+SMALL_MARGIN);
								if (os.type!=Segment.UNKNOWN) 
									reSynchronize(os, s, 0, maxTo, which, tw);
								else {				
									s.copy(bkupS);
									reSynchronize(s,os,0,otherTo,-which,tw);
									break;
								}
							} else {
								int oldTp = prev.type;
								Segment.removeLastPoint(op, prev, os.start.y-SMALL_MARGIN);
								if (op.type!=Segment.UNKNOWN) 
									reSynchronize(op, prev, 0, maxTo, which, tw);
								else {
									prev.type = oldTp;
									reSynchronize(prev,op,0,otherTo,-which,tw);
									Segment.removeFirstPoint(os, s, op.end.y+SMALL_MARGIN);
									reSynchronize(os, s, 0, maxTo, which, tw);
									if (os.type==Segment.UNKNOWN || s.type==Segment.UNKNOWN || op.endIndex>os.startIndex || op.end.y>=os.start.y-SMALL_MARGIN){
										s.copy(bkupS);
										reSynchronize(s,os,0,otherTo,-which,tw);
										break;
									}
								}
							}
						}
						
						if (on!=null && s.type!=Segment.UNKNOWN && on.type!=Segment.UNKNOWN && (os.endIndex>on.startIndex || os.end.y>=on.start.y-SMALL_MARGIN)){
							if (isConfirmed(next, which, tW)){
								Segment.removeLastPoint(os, s,on.start.y-SMALL_MARGIN);
								if (os.type!=Segment.UNKNOWN) 
									reSynchronize(os, s, 0, maxTo, which, tw);
								else s.type = Segment.UNKNOWN;
							} else {
								Segment.removeFirstPoint(on, next, os.end.y+SMALL_MARGIN);
								if (on.type!=Segment.UNKNOWN) 
									reSynchronize(on, next, 0, maxTo, which, tw);
								else next.type = Segment.UNKNOWN;
							}
						}
						break;
					}//end of if found																										
				}//end of for
				if (found) break;
			}
		}
		int sr = (s.type==0 || s.radius>=MAX_RADIUS) ? MAX_RADIUS-1 : (int)Math.round(s.radius+s.type*tW*which);
		if (sr>=MAX_RADIUS) sr = MAX_RADIUS-1;
		if (s.map!=null && sr>=0 && s.map[sr]==0){
			s.appearedRads[s.radCount++] = sr;
			s.opp.radCount++;
			s.map[sr]++;
		}
		long endTime = (System.nanoTime()-ti)/1000000;
		if (CircleDriver2.debug || endTime>=1) System.out.println("End reExpand : "+endTime+"   at "+CircleDriver2.time+" s.    "+n);
	}

	/*private static final void reExpand(Segment prev,Segment t,Segment next,double tW){
		EdgeDetector edge = CircleDriver2.edgeDetector;
		Vector2D[] lV = edge.left;
		int lN = edge.lSize;
		Vector2D[] rV = edge.right;
		int rN = edge.rSize;
		Segment l = t.leftSeg;
		Segment r = t.rightSeg;
		Segment nl = (next==null) ? null : next.leftSeg;
		Segment nr = (next==null) ? null : next.rightSeg;
		Segment pl = (prev==null) ? null : prev.leftSeg;
		Segment pr = (prev==null) ? null : prev.rightSeg;

		if (r==null || (l!=null && l.num>=r.num)){
			if (l!=null && l.updated) reCheckSegment(pl, l, nl, -1, tW,tr,i);			
			if (l!=null && l.type!=UNKNOWN && (l.updated || (pl==null && l.startIndex>0) || (nl==null && l.endIndex<lN-1) || (pl!=null && pl.updated) || (nl!=null && nl.updated))) reExpand(pl, l, nl, -1, tW);
			if (r!=null && r.type!=UNKNOWN && (l.updated || r.updated)) reCheckSegment(pr, r, nr, 1, tW,oal);
			if (r!=null && r.type!=UNKNOWN && (r.updated || (pr==null && r.startIndex>0) || (nr==null && r.endIndex<rN-1) || (pr!=null && pr.updated) || (nr!=null && nr.updated))) reExpand(pr, r, nr, 1, tW);							
		} else {
			if (r!=null && r.updated) reCheckSegment(pr, r, nr, 1, tW,oal);
			if (r!=null && r.type!=UNKNOWN && (r.updated || (pr==null && r.startIndex>0) || (nr==null && r.endIndex<rN-1) || (pr!=null && pr.updated) || (nr!=null && nr.updated))) reExpand(pr, r, nr, 1, tW);
			if (l!=null && l.type!=UNKNOWN && (l.updated || r.updated)) reCheckSegment(pl, l, nl, -1, tW,oal);
			if (l!=null && l.type!=UNKNOWN && (l.updated || (pl==null && l.startIndex>0) || (nl==null && l.endIndex<lN-1) || (pl!=null && pl.updated) || (nl!=null && nl.updated))) reExpand(pl, l, nl, -1, tW);
		}

		if ((pl!=null && pl.updated) || (pr!=null && pr.updated)){
			if (pl!=null && pl.type!=UNKNOWN) {
				toMiddleSegment(pl,prev, -1, tW);
				pl.points = lV;
				prev.leftSeg = pl;
				prev.rightSeg = pr;					
			} else prev.type = UNKNOWN;
		}

//		if (pl!=null) pl.updated = false;			
//		if (pr!=null) pr.updated = false;
//		if (prev!=null) prev.updated = false;

		if (l!=null){
			if (l.type!=UNKNOWN) {
				toMiddleSegment(l,t, -1, tW);
				l.points = lV;
				t.leftSeg = l;
				t.rightSeg = r;
//				l.updated = false;
//				if (r!=null) r.updated = false;
//				t.updated = false;
			} 
		} else if (r!=null){
			if (r.type!=UNKNOWN) {
				toMiddleSegment(r,t, 1, tW);
				r.points = rV;
				t.leftSeg = l;
				t.rightSeg = r;
//				r.updated = false;
//				if (l!=null) l.updated = false;
//				t.updated = false;
			} 
		}
	}//*/
	
	public static final void reCalibrate(Segment prev,Segment s,int which,double tw){
		Segment op = prev.opp;
		Segment os = s.opp;
		EdgeDetector edge = CircleDriver2.edgeDetector;
		int maxTo = (which==-1) ? edge.lSize : edge.rSize;		
		int otherTo = (which==1) ? edge.lSize : edge.rSize;
		
		if (prev!=null && prev.type!=Segment.UNKNOWN && s.type!=Segment.UNKNOWN && (prev.endIndex>s.startIndex || prev.end.y>=s.start.y-SMALL_MARGIN)){
			if (isConfirmed(prev, which, tw*0.5)){				
				if (s.unsafe) 
					Segment.removeFirstPoint(s, os, prev.end.y+SMALL_MARGIN);
				else {
					s.type = Segment.UNKNOWN;
					os.type = Segment.UNKNOWN;
				}
				if (s.type!=Segment.UNKNOWN) 
					reSynchronize(s, os, 0, otherTo, -which, tw);
				else os.type = Segment.UNKNOWN;
			} else {
				int oldTp = prev.type;
				if (prev.unsafe){
					Segment.removeLastPoint(prev, op, s.start.y-SMALL_MARGIN);
				} else {
					prev.type = Segment.UNKNOWN;
					op.type = Segment.UNKNOWN;
				}
				if (prev.type!=Segment.UNKNOWN) 
					reSynchronize(prev, op, 0, otherTo, -which, tw);
				else {
					prev.type = oldTp;
					reSynchronize(op,prev,0,maxTo,which,tw);
					Segment.removeFirstPoint(s, os, prev.end.y+SMALL_MARGIN);
					reSynchronize(s, os, 0, otherTo, -which, tw);					
				}
			}
		}
		
		if (op!=null && op.type!=Segment.UNKNOWN && s.type!=Segment.UNKNOWN && (op.endIndex>os.startIndex || op.end.y>=os.start.y-SMALL_MARGIN)){
			if (isConfirmed(prev, which, tw*0.5)){
				if (os.unsafe)
					Segment.removeFirstPoint(os, s, op.end.y+SMALL_MARGIN);
				else {
					s.type = Segment.UNKNOWN;
					os.type = Segment.UNKNOWN;
				}
				if (os.type!=Segment.UNKNOWN) 
					reSynchronize(os, s, 0, maxTo, which, tw);
				else s.type = Segment.UNKNOWN;
			} else {
				int oldTp = prev.type;
				if (op.unsafe){
					Segment.removeLastPoint(op, prev, os.start.y-SMALL_MARGIN);
				} else {
					prev.type = Segment.UNKNOWN;
					op.type = Segment.UNKNOWN;
				}
				if (op.type!=Segment.UNKNOWN) 
					reSynchronize(op, prev, 0, maxTo, which, tw);
				else {
					prev.type = oldTp;
					reSynchronize(prev,op,0,otherTo,-which,tw);
					Segment.removeFirstPoint(os, s, op.end.y+SMALL_MARGIN);
					reSynchronize(os, s, 0, maxTo, which, tw);					
				}
			}
		}
	}
	
	private static void joinSegment(Segment prev,Segment s,double tW){
		Segment pl = prev.leftSeg;
		Segment pr = prev.rightSeg;
		Segment l = s.leftSeg;
		Segment r = s.rightSeg;
		Vector2D[] lV = l.points;
		Vector2D[] rV = r.points;
		pl.end.copy(l.end);
		pl.endIndex = l.endIndex;
		pr.end.copy(r.end);
		pr.endIndex = r.endIndex;
		pl.num = pl.endIndex+1-pl.startIndex;
		pr.num = pr.endIndex+1-pr.startIndex;
		pl.upper = l.upper;
		pr.upper = r.upper;
		if (pl.center!=null && pl.center.y!=0){
			if (pl.num>2){
				circle(lV[pl.startIndex], lV[pl.endIndex], pl.center.x,pl.center.y, pl.radius,pl.center);
			} else circle(pl.start,pl.end, pl.center.x,pl.center.y, pl.radius,pl.center);

			if (pr.num>2){
				circle(rV[pr.startIndex], rV[pr.endIndex], pr.center.x,pr.center.y, pr.radius,pr.center);
			} else circle(pr.start,pr.end, pr.center.x,pr.center.y, pr.radius,pr.center);
		}
		l.type = Segment.UNKNOWN;
		r.type = Segment.UNKNOWN;		
		s.radCount = l.radCount;								
		toMiddleSegment(pl,prev, -1, tW);
		int prvRad = (prev.type==0) ? MAX_RADIUS-1 : (int)Math.round(prev.radius); 
		prev.map[prvRad] += s.map[prvRad];
		pl.done = true;
		pr.done = true;
		pl.updated = true;
		pr.updated = true;
		prev.updated = true;
		prev.radCount = pl.radCount;

	}
	
	public static final int reUpdate(Segment[] trArr,int sz,double tW){
		return reUpdate(trArr, sz, tW,0);
	}
	
	private static boolean reject3(Segment pr,Segment r,Segment nr,Segment pl,Segment l,Segment nl,int edge,double tW){		
		if (CircleDriver2.inTurn && r!=null && pr!=null && r.type!=0 &&  !isConfirmed(r, edge, tW) && r.num<=2 && l.num<=2 
				 && pr.type!=r.type && (r.startIndex>pr.endIndex+1 || l.startIndex>pl.endIndex+1)){
			Vector2D v = (l.points==null) ? null : r.startIndex>pr.endIndex+1 ? r.points[r.startIndex-1] : l.points[l.startIndex-1];
//			Vector2D pv = (v==null) ? null : r.startIndex>pr.endIndex+1 ? pr.end : pl.end;
			
			if (v!=null){
				if (true){
					Vector2D point = new Vector2D();
					Vector2D[] vs = null,others = null;
					int num = 0;
					int otherNum = 0;
					Segment s = null;
					Segment prev = null;
					boolean ok = false;
					if (isConnected(pr, r, tW, pt)){
						ok = true;
						mapPoint(pt, r, -1, tW, point);
						num = CircleDriver2.edgeDetector.rSize;
						otherNum = CircleDriver2.edgeDetector.lSize;
						vs = r.points;
						others = l.points;
						s = r;
						prev = pr;
					} else if (isConnected(pl, l, tW, pt)){
						ok  =true;
						mapPoint(pt, l, 1, tW, point);
						num = CircleDriver2.edgeDetector.lSize;
						otherNum = CircleDriver2.edgeDetector.rSize;
						vs = l.points;
						others = r.points;
						s = l;
						prev = pl;
					}
					if (ok){
						int i = 0;
						if (num>0){
							i = binarySearchFromTo(vs, pt.y, 0, num-1);
							if (i<0) 
								i = -i-1;
							else if (pt.y>vs[i].y) i++;
							if (s.startIndex>i){
								v = vs[i];
								if ((s.start.x-v.x)*s.type<0) 
									return true;
							} 
//							else if (i-1>prev.endIndex){
//								v = vs[i-1];
//								if ((v.x-prev.end.x)*prev.type<0) 
//									return true;
//							}
						}
						
						if (otherNum>0){
							i = binarySearchFromTo(others, point.y, 0, otherNum-1);
							if (i<0) 
								i = -i-1;
							else if (point.y>others[i].y) i++;
							s = s.opp;
							prev = prev.opp;					
							if (s.startIndex>i){
								v = others[i];
								if ((s.start.x-v.x)*s.type<0) 
									return true;
							} 
//							else if (i-1>prev.endIndex){
//								v = others[i-1];
//								if ((v.x-prev.end.x)*prev.type<0) 
//									return true;
//							}
						}
					}
				}
//				return (v.x-pv.x)*l.type<0;
			}
		}
		return false;	
			
	}
	
	private static boolean reject2(Segment pr,Segment r,Segment nr,int edge,int rN,Vector2D[] rV,double tW){		
		if (r.type==0){
			if (pr!=null && pr.type==0 || nr!=null && nr.type==0) return false;
		} else if (pr!=null && pr.type==r.type && Math.abs(pr.radius-r.radius)<5) 
			return false;
		else if (nr!=null && nr.type==r.type && Math.abs(nr.radius-r.radius)<5)
			return false;
		if (r.type!=0 && r.map!=null && r.num>1 && (r.num<=2 || r.lower==null && r.upper==null) && r.endIndex<rN-1 && rV[r.endIndex+1].y-r.end.y<=2 && (r.startIndex==0 || r.start.y - rV[r.startIndex-1].y<=2) && (!isConfirmed(r, edge, tW) || r.end.y-r.start.y<=3 || r.num>=3 && r.end.y-r.start.y<5) && (pr==null || nr==null || pr.endIndex<r.startIndex-1 || nr.startIndex-1>r.endIndex))
			return true;
		boolean notIsconfirmed = !isConfirmed(r, edge, tW); 
		if (r.num>=3 && notIsconfirmed && r.lower==null && r.upper==null && r.end.y-r.start.y<=5 && r.endIndex<rN-1 && rV[r.endIndex+1].y-r.end.y<=2 && (pr==null || r.start.y-pr.end.y<=3)) return true;		
		if (r.num>=3 && notIsconfirmed && r.lower==null && r.upper==null && r.end.y-r.start.y<=5 && (pr==null || r.start.y-pr.end.y<=2)) return true;
		return false;
	}

	public static final int reUpdate(Segment[] trArr,int sz,double tW,int fromSeg){
		if (CircleDriver2.debug) System.out.println("Start reUpdate : "+(System.nanoTime()-CircleDriver2.ti)/1000000);
		int[] trIndx = CircleDriver2.trIndx;
		int[] occupied = CircleDriver2.occupied;
		trSz = sz;		
		EdgeDetector edge = CircleDriver2.edgeDetector;
		Vector2D[] lV = edge.left;
		int lN = edge.lSize;
		Vector2D[] rV = edge.right;
		int rN = edge.rSize;
		Segment prev = (fromSeg<=0) ? null : trArr[trIndx[fromSeg-1]];
		Segment pl = (prev==null) ? null : prev.leftSeg;
		Segment pr = (prev==null) ? null : prev.rightSeg;
		Segment next = null;		
		int oldTrSz = 0;
		int oldIndx = 0;		
		int count = 0;		
		int prevLStartIndx = 0;
		int prevRStartIndx = 0;
//		int nextLEndIndx = 0;
//		int nextREndIndx = 0;
		double tw = tW+tW;
		for (int i = fromSeg;i<trSz;++i){			
			if (i<0) continue;
			Segment t = trArr[ trIndx[i] ];
			if (oldIndx>=i) 
				count++;
			else count = 0;
			if (oldIndx>=i && (oldTrSz==trSz || count>1)){
//				prev = (i>0) ? trArr[ trIndx[i-1] ] : null;
				toMiddleSegment(t.leftSeg,t, -1, tW);
				t.radCount = t.leftSeg.radCount;
				prev = t;
				i++;
				oldIndx = i;
				t = trArr[ trIndx[i] ];
				if (i>=trSz) break;
			} else {
				oldTrSz = trSz;
				if (oldIndx<i) oldIndx = i;				
			}			
			oldTrSz = trSz;
			next = (i<trSz-1) ? trArr[ trIndx[i+1] ] : null;
			Segment l = t.leftSeg;
			Segment r = t.rightSeg;
			if (i==0 && t.lower!=null){
				t.lower = null;
				l.lower = null;
				r.lower = null;
			}
			if (i==trSz-1 && t.upper!=null){
				t.upper = null;
				l.upper = null;
				r.upper = null;
			}
			Segment nl = (next==null) ? null : next.leftSeg;
			Segment nr = (next==null) ? null : next.rightSeg;
			pl = (prev==null) ? null : prev.leftSeg;
			pr = (prev==null) ? null : prev.rightSeg;			
//			if (pl!=null) pl.updated = false;
//			if (pr!=null) pr.updated = false;
			if (i>1 && prev!=null && prev.type!=UNKNOWN){
				prevLStartIndx = pl.startIndex;
				prevRStartIndx = pr.startIndex;
			}
//			if (i<trSz-2 && next.type!=UNKNOWN){
//				nextLEndIndx = nl.endIndex;
//				nextREndIndx = nr.endIndex;
//			}

			boolean isFirstL = (l!=null && l.type!=Segment.UNKNOWN && ((l.type==0 && Math.abs(l.start.x-l.end.x)<E) || (l.type!=0 && l.center!=null && l.center.y==0)));
			if (l!=null && l.type!=Segment.UNKNOWN && ((l.end.y<0 && r.end.y<0)|| Double.isNaN(l.start.y) || Double.isNaN(l.end.y) || (!isFirstL && l.end.y-l.start.y<=1) || (!CircleDriver2.inTurn && l.num<2 && r.num<2))){				
				occupied[ trIndx[i] ] = 0;
				if ((trSz-=i+1)>0) System.arraycopy(trIndx, i+1, trIndx, i, trSz);									
				trSz+=i--;				
				oldIndx = i;
//				prev = null;
				continue;
			}

			if (r==null || (l!=null && l.type!=Segment.UNKNOWN && l.num>=r.num)){
				if (!l.done){
					if (lN>0) reCheckSegment(pl, l, nl, -1, tW,trArr,i);
					t = trArr[ trIndx[i] ];
					next = (i<trSz-1) ? trArr[ trIndx[i+1] ] : null;
					l = t.leftSeg;
					r = t.rightSeg;			
					nl = (next==null) ? null : next.leftSeg;
					nr = (next==null) ? null : next.rightSeg;
					if (l.type!=Segment.UNKNOWN){
						if (l.lower!=null && pl!=null && (pl.upper==null || pr.upper==null)) {
							pl.upper = new Vector2D(l.lower);
							if (r.lower==null)
								reSynchronize(l, r, 0, rN, 1, tw);
							pr.upper = new Vector2D(r.lower);																
						} else if (nl!=null && l.upper!=null && (nl.lower==null || nr.lower==null)){
							nl.lower = new Vector2D(l.upper);
							if (r.upper==null)
								reSynchronize(l, r, 0, rN, 1, tw);
							nr.lower = new Vector2D(r.upper);
						}
					}
					while (pl!=null && pl.type==UNKNOWN){
						if (i>0){
							occupied[ trIndx[i-1] ] = 0;					
							if ((trSz-=i)>0) System.arraycopy(trIndx, i, trIndx, i-1, trSz);
							trSz+=--i;					
							prev.radCount = pl.radCount;
						}
						prev = (i>0) ? trArr[ trIndx[i-1] ] : null;
						pl = (prev==null) ? null : prev.leftSeg;
						pr = (prev==null) ? null : prev.rightSeg;					
						if (lN>0 && l.type!=UNKNOWN && (pl==null || pl.type!=l.type || pl.radius!=l.radius) && (l.updated || l.lower==null || (pl==null && l.startIndex>0) || (nl==null && l.endIndex<lN-1) || (pl!=null && (pl.updated || pl.endIndex+1<l.startIndex)) || (nl!=null && (nl.updated || l.endIndex+1<nl.startIndex)))) reExpand(pl, l, nl, -1, tW);					
					}
				}
				if (!r.done){
					if (rN>0 && r.type!=UNKNOWN) reCheckSegment(pr, r, nr, 1, tW,trArr,i);								
					t = trArr[ trIndx[i] ];
					next = (i<trSz-1) ? trArr[ trIndx[i+1] ] : null;
					l = t.leftSeg;
					r = t.rightSeg;			
					nl = (next==null) ? null : next.leftSeg;
					nr = (next==null) ? null : next.rightSeg;
					if (l.type!=Segment.UNKNOWN){
						if (l.lower!=null && pl!=null && (pl.upper==null || pr.upper==null)) {
							pl.upper = new Vector2D(l.lower);
							if (r.lower==null)
								reSynchronize(l, r, 0, rN, 1, tw);
							pr.upper = new Vector2D(r.lower);																
						} else if (nl!=null && l.upper!=null && (nl.lower==null || nr.lower==null)){
							nl.lower = new Vector2D(l.upper);
							if (r.upper==null)
								reSynchronize(l, r, 0, rN, 1, tw);
							nr.lower = new Vector2D(r.upper);
						}
					}
					while (pr!=null && pr.type==UNKNOWN){
						if (i>0){
							occupied[ trIndx[i-1] ] = 0;					
							if ((trSz-=i)>0) System.arraycopy(trIndx, i, trIndx, i-1, trSz);
							trSz+=--i;
							prev.radCount = pr.radCount;
						}
						prev = (i>0) ? trArr[ trIndx[i-1] ] : null;					
						pl = (prev==null) ? null : prev.leftSeg;
						pr = (prev==null) ? null : prev.rightSeg;
						if (prev!=null){
							prev.upper = null;
							pl.upper = null;
							pr.upper = null;
						}
						if (rN>0 && r.type!=UNKNOWN && (pr==null || pr.type!=r.type || pr.radius!=r.radius) && (r.updated || r.lower==null || (pr==null && r.startIndex>0) || (nr==null && r.endIndex<rN-1) || (pr!=null && (pr.updated || pr.endIndex+1<r.startIndex)) || (nr!=null && (nr.updated || r.endIndex+1<nr.startIndex)))) reExpand(pr, r, nr, 1, tW);				
					}
				}
			} else if (r.type!=Segment.UNKNOWN){
				if (!r.done){
					if (rN>0) reCheckSegment(pr, r, nr, 1, tW,trArr,i);				
					t = trArr[ trIndx[i] ];			
					next = (i<trSz-1) ? trArr[  trIndx[i+1] ] : null;
					l = t.leftSeg;
					r = t.rightSeg;						
					nl = (next==null) ? null : next.leftSeg;
					nr = (next==null) ? null : next.rightSeg;
					if (l.type!=Segment.UNKNOWN){
						if (l.lower!=null && pl!=null && (pl.upper==null || pr.upper==null)) {
							pl.upper = new Vector2D(l.lower);
							if (r.lower==null)
								reSynchronize(l, r, 0, rN, 1, tw);
							pr.upper = new Vector2D(r.lower);																
						} else if (nl!=null && l.upper!=null && (nl.lower==null || nr.lower==null)){
							nl.lower = new Vector2D(l.upper);
							if (r.upper==null)
								reSynchronize(l, r, 0, rN, 1, tw);
							nr.lower = new Vector2D(r.upper);
						}
					}
					while (pr!=null && pr.type==UNKNOWN){
						if (i>0){
							occupied[ trIndx[i-1] ] = 0;					
							if ((trSz-=i)>0) System.arraycopy(trIndx, i, trIndx, i-1, trSz);
							trSz+=--i;					
							prev.radCount = pr.radCount;
						}
						prev = (i>0) ? trArr[ trIndx[i-1] ] : null;
						pl = (prev==null) ? null : prev.leftSeg;
						pr = (prev==null) ? null : prev.rightSeg;		
						if (prev!=null){
							prev.upper = null;
							pl.upper = null;
							pr.upper = null;
						}
						if (rN>0 && r.type!=UNKNOWN && (pr==null || pr.type!=l.type || pr.radius!=r.radius) && (r.updated || r.lower==null || (pr==null && r.startIndex>0) || (nr==null && r.endIndex<rN-1) || (pr!=null && (pr.updated || pr.endIndex+1<r.startIndex)) || (nr!=null && (nr.updated || r.endIndex+1<nr.startIndex)))) reExpand(pr, r, nr, 1, tW);					
					}
				}
				if (!l.done){
					if (lN>0 && l.type!=UNKNOWN) reCheckSegment(pl, l, nl, -1, tW,trArr,i);				
					t = trArr[ trIndx[i] ];
					next = (i<trSz-1) ? trArr[  trIndx[i+1] ] : null;
					l = t.leftSeg;
					r = t.rightSeg;					
					nl = (next==null) ? null : next.leftSeg;
					nr = (next==null) ? null : next.rightSeg;
					if (l.type!=Segment.UNKNOWN){
						if (l.lower!=null && pl!=null && (pl.upper==null || pr.upper==null)) {
							pl.upper = new Vector2D(l.lower);
							if (r.lower==null)
								reSynchronize(l, r, 0, rN, 1, tw);
							pr.upper = new Vector2D(r.lower);																
						} else if (nl!=null && l.upper!=null && (nl.lower==null || nr.lower==null)){
							nl.lower = new Vector2D(l.upper);
							if (r.upper==null)
								reSynchronize(l, r, 0, rN, 1, tw);
							nr.lower = new Vector2D(r.upper);
						}
					}
					while (pl!=null && pl.type==UNKNOWN){
						if (i>0){
							occupied[ trIndx[i-1] ] = 0;					
							if ((trSz-=i)>0) System.arraycopy(trIndx, i, trIndx, i-1, trSz);
							trSz+=--i;
							prev.radCount = pl.radCount;
						}
						prev = (i>0) ? trArr[ trIndx[i-1] ] : null;
						pl = (prev==null) ? null : prev.leftSeg;
						pr = (prev==null) ? null : prev.rightSeg;			
						if (prev!=null){
							prev.upper = null;
							pl.upper = null;
							pr.upper = null;
						}
						if (lN>0 && l.type!=UNKNOWN && (pl==null || pl.type!=l.type || pl.radius!=l.radius) && (l.updated || l.lower==null || (pl==null && l.startIndex>0) || (nl==null && l.endIndex<lN-1) || (pl!=null && (pl.updated || pl.endIndex+1<l.startIndex)) || (nl!=null && (nl.updated || l.endIndex+1<nl.startIndex)))) reExpand(pl, l, nl, -1, tW);
					}
				}
			}
			
			if (i>0 && pl!=null && pl.type!=UNKNOWN){
				if (i>1 && (pl.startIndex<prevLStartIndx || pr.startIndex<prevRStartIndx)){
					Segment prPrevL = trArr[ trIndx[i-2] ].leftSeg;
					Segment prPrevR = prPrevL.opp;
					if (prPrevL.endIndex>=pl.startIndex || prPrevR.endIndex>=pr.startIndex){
						prevLStartIndx = pl.num;
						prevRStartIndx = pr.num;
						reCalibrate(prPrevL, pl, -1, tw);
						if (pl.num!=prevLStartIndx || pr.num!=prevRStartIndx){
							pl.updated = true;
							pr.updated = true;
							prev.updated = true;
						}
					}
				}		
				
				if (pl.endIndex>=l.startIndex || pr.endIndex>=r.startIndex || pl.end.y>=l.start.y-SMALL_MARGIN || pr.end.y>=r.start.y-SMALL_MARGIN){
					prevLStartIndx = pl.num;
					prevRStartIndx = pr.num;
					reCalibrate(pl, l, -1, tw);
					if (pl.num!=prevLStartIndx || pr.num!=prevRStartIndx){
						pl.updated = true;
						pr.updated = true;
						prev.updated = true;
					}
				}
			}
//			if (i<trSz-2 && next.type!=UNKNOWN){
//				if (nl.endIndex>nextLEndIndx || nr.endIndex>nextREndIndx){
//					Segment nxNextL = trArr[ trIndx[i+2] ].leftSeg;
//					Segment nxNextR = nxNextL.opp;
//					if (nxNextL.startIndex<=nl.endIndex || nxNextR.startIndex<=nr.endIndex){
//						reCalibrate(nl, nxNextL, -1, tw);
//					}
//				}	
//				nextLEndIndx = nl.endIndex;
//				nextREndIndx = nr.endIndex;
//			}

			if (i>0 && pl!=null && l!=null && l.type!=Segment.UNKNOWN && pl.type==l.type && l.type!=0 && (pl.radius==l.radius && (pl.center==null || l.center==null || pl.center.distance(l.center)<2.5) )){
				joinSegment(prev, t, tW);
				int idx = i+1;
				occupied [ trIndx[i] ] = 0;
				if ((trSz-=idx)>0) System.arraycopy(trIndx, idx, trIndx, i, trSz);
				trSz+=i--;												
				continue;
			} else if (i>0 && pl!=null && nl!=null && nl.type!=Segment.UNKNOWN && pl.type==nl.type && pl.type!=0 && pl.radius==nl.radius && pl.center.distance(nl.center)<2.5 ){
				joinSegment(prev, next, tW);
				int idx = i+2;
				occupied[ trIndx[i+1] ] = 0;
				occupied[ trIndx[i] ] = 0;
				if ((trSz-=idx)>0) System.arraycopy(trIndx, idx, trIndx, i, trSz);
				trSz+=i--;								
				continue;
			} 
			if (i>0 && l!=null && nl!=null && l.type!=Segment.UNKNOWN && l.type==nl.type && l.type!=0 && isSameSegment(l, nl)){
				joinSegment(t, next, tW);
				occupied[ trIndx[i+1] ] = 0;
				if (trSz>i+2) System.arraycopy(trIndx, i+2, trIndx, i+1, trSz-i-2);
				trSz--;	
				prev = t;
				continue;
			}

			if ((pl!=null && pl.updated) || (pr!=null && pr.updated)){
				if (pl.type!=UNKNOWN) {
					toMiddleSegment(pl,prev, -1, tW);					
//					pl.updated = false;
//					pr.updated = false;
					prev.radCount = pl.radCount;
				} else prev.type = UNKNOWN;
			}
			if (prev!=null && pl!=null) prev.radCount = pl.radCount;
			if (CircleDriver2.inTurn && pl!=null && reject3(pr, r, nr, pl, l, nl, 1,tW) || !CircleDriver2.inTurn && (trSz>2 || trSz>1 && pl!=null && pl.type!=0) && i>0 && l.type!=Segment.UNKNOWN && r.type!=Segment.UNKNOWN && (reject2(pl, l, nl,-1, lN, lV, tW)
					|| reject2(pr, r, nr, 1,rN, rV, tW))){
				l.type = Segment.UNKNOWN;
				r.type = Segment.UNKNOWN;
			}
						
			if (l!=null && lN>0){
				if (l.type!=UNKNOWN) {
					toMiddleSegment(l,t, -1, tW);
					t.radCount = l.radCount;					
				} else {													
					t.type = UNKNOWN;
					if (pl!=null) pl.upper = null;
					if (nl!=null) nl.lower = null;
					if (pr!=null) pr.upper = null;
					if (nr!=null) nr.lower = null;
					if (prev!=null) prev.upper = null;
					if (next!=null) next.lower = null;
					if (t.rightSeg!=null) {
						t.rightSeg.type = Segment.UNKNOWN;
						t.rightSeg.radius = 0;
					}				
					t.radCount = l.radCount;
					occupied[ trIndx[i] ] = 0;
					int idx = i+1;					
					if ((trSz-=idx)>0) System.arraycopy(trIndx, idx, trIndx, i, trSz);
					trSz+=i;					
					if (oldTrSz==trSz){
						i--;	//*/
						continue;
					} else i -= oldTrSz-trSz;
					t = prev;
				} 
			} else if (r!=null && rN>0){
				if (r.type!=UNKNOWN) {
					toMiddleSegment(r,t, 1, tW);
					t.radCount = r.radCount;					
					//					r.updated = false;
					//					if (l!=null) l.updated = false;
					//					t.updated = false;
				} else {																			
					t.type = UNKNOWN;
					if (pl!=null) pl.upper = null;
					if (nl!=null) nl.lower = null;
					if (pr!=null) pr.upper = null;
					if (nr!=null) nr.lower = null;
					if (prev!=null) prev.upper = null;
					if (next!=null) next.lower = null;
					if (t.leftSeg!=null) {
						t.leftSeg.type = Segment.UNKNOWN;
						t.leftSeg.radius = 0;
					}
					t.radCount = r.radCount;
					occupied[ trIndx[i] ] = 0;
					int idx = i+1;						
					if ((trSz-=idx)>0) System.arraycopy(trIndx, idx, trIndx, i, trSz);
					trSz+=i;
					if (oldTrSz==trSz){
						i--;	//*/
						continue;
					} else i -= oldTrSz-trSz;
				} 
			}			

			Segment firstSeg = (trSz<=0) ? null : trArr[ trIndx[0] ];
			if (firstSeg!=null && t!= null && t.type!=Segment.UNKNOWN && (prev!=null && (t.type==0 && Math.abs(t.end.x-t.start.x)<=E && t.end.y-t.start.y>3 && firstSeg.type!=0) || (t.type!=0 && t.center!=null && t.center.y==0  && firstSeg.end.y<1 && (firstSeg.type==0 || firstSeg.radius!=t.radius)))){
				boolean good = true;
				if (t.type!=0){
					for (int j = 0;j<l.startIndex;++j){
						Vector2D p = lV[i];
						if (p.y<0) continue;
						if (!l.isPointBelongToSeg(p)) {
							good = false;
							break;
						}
					}
					
					if (good){
						for (int j = 0;j<r.startIndex;++j){
							Vector2D p = rV[i];
							if (p.y<0) continue;
							if (!r.isPointBelongToSeg(p)) {
								good = false;
								break;
							}
						}
					}
				}
				if (good){
					for (int j = 0;j<i;++j)	occupied[ trIndx[j] ] = 0;
					if ((trSz-=i)>0) System.arraycopy(trIndx, i, trIndx, 0, trSz);
					i = 0;
				}											
			}
			
			prev = t;			
		}						
		
		
		if (trSz>0 && fromSeg<trSz) 
			prev = trArr[ trIndx[ trSz-1] ];						
		
		pl = (prev==null) ? null : prev.leftSeg;
		pr = (prev==null) ? null : prev.rightSeg;
		int numL = (pl==null) ? lN : lN - pl.endIndex-1;
		int numR = (pr==null) ? rN : rN - pr.endIndex-1;
		
		if (trSz==0 || fromSeg==trSz || numL>=2 || numR>=2 ){			
			int currentIndx = (trSz==0) ? 0 : trSz;			
			if (numL>=numR)
				reCheckSegment(pl, null, null, -1, tW,trArr,currentIndx);
			else reCheckSegment(pr, null, null, 1, tW,trArr,currentIndx);
			Segment t = null;
			Segment l = null;
			Segment r = null;
			Segment nl = null;
			Segment nr = null; 
			if (currentIndx<trSz){
				t = trArr[ trIndx[currentIndx] ];
				next = (currentIndx<trSz-1) ? trArr[  trIndx[currentIndx+1] ] : null;
				l = t.leftSeg;
				r = t.rightSeg;
				nl = (next==null) ? null : next.leftSeg;
				nr = (next==null) ? null : next.rightSeg;				
			}
			if (numL>=numR) 
				reCheckSegment(pr, r, nr, 1, tW,trArr,currentIndx);
			else reCheckSegment(pl, l, nl, -1, tW,trArr,currentIndx);
			
			if (l!=null && l.type!=Segment.UNKNOWN){
				if (l.lower!=null && pl!=null && (pl.upper==null || pr.upper==null)) {
					pl.upper = new Vector2D(l.lower);
					if (r.lower==null)
						reSynchronize(l, r, 0, rN, 1, tw);
					pr.upper = new Vector2D(r.lower);																
				} else if (nl!=null && l.upper!=null && (nl.lower==null || nr.lower==null)){
					nl.lower = new Vector2D(l.upper);
					if (r.upper==null)
						reSynchronize(l, r, 0, rN, 1, tw);
					nr.lower = new Vector2D(r.upper);
				}
			}
			
			if (CircleDriver2.inTurn && pl!=null && reject3(pr, r, nr, pl, l, nl, 1,tW) || !CircleDriver2.inTurn && l!=null && r!=null && l.type!=Segment.UNKNOWN && r.type!=Segment.UNKNOWN && (reject2(pl, l, nl,-1, lN, lV, tW)
					|| reject2(pr, r, nr, 1,rN, rV, tW))){
				l.type = Segment.UNKNOWN;
				r.type = Segment.UNKNOWN;
			}
			
			if (pl!=null && l!=null && l.type!=Segment.UNKNOWN && pl.type==l.type && l.type!=0 && (pl.radius==l.radius && (pl.center==null || l.center==null || Math.abs(pl.center.y-l.center.y)<1) )){
				joinSegment(prev, t, tW);		
				occupied [ trIndx[trSz-1] ] = 0;
//				if (trSz>0) System.arraycopy(trIndx, trSz-1, trIndx, trSz-2, 1);
				trSz--;								
			}
			
			if (trSz>0){
				int minSeg = (fromSeg>0) ? fromSeg-1 : 0;
				for (int i = trSz-1;i>=minSeg;--i){					
					t = trArr[ trIndx[i] ];	
					Segment s = t.leftSeg;
					if (s.type==Segment.UNKNOWN){
						occupied[ trIndx[i] ] = 0;
						if (trSz>i+1) System.arraycopy(trIndx, i+1, trIndx, i, trSz-i-1);
						trSz--;
						continue;
					}
					toMiddleSegment(s,t, -1, tW);
					prev = (i>0) ? trArr[ trIndx[i-1] ].leftSeg : null;
					
					if (prev!=null && prev.type!=Segment.UNKNOWN && (prev.endIndex>s.startIndex || prev.end.y>=s.start.y-SMALL_MARGIN)){																
						reCalibrate(prev, s, -1, tw);							
					}
					Segment op = (prev==null) ? null : prev.opp;
					Segment os = (s==null) ? null : s.opp;
					if (op!=null && op.type!=Segment.UNKNOWN && (op.endIndex>os.startIndex || op.end.y>=os.start.y-SMALL_MARGIN)){								
						reCalibrate(prev, s, 1, tw);							
					}
					t.radCount = s.radCount;
				}
			}
		}		
		
		if (CircleDriver2.debug) System.out.println("End reUpdate : "+(System.nanoTime()-CircleDriver2.ti)/1000000);
		return trSz;
	}

	/*private static final void reUpdate(final Vector2D[] v,int from, int to,int which,double tW,IntArrayList starts,IntArrayList ends,ObjectArrayList<Segment> tr,ObjectArrayList<Segment> ss,ObjectArrayList<Segment> other,int[] indexArr,int[] indexArrO){		
		if (CircleDriver2.time>=CircleDriver2.BREAK_TIME)
			System.out.println();
		//		double[] temp = new double[6];
		Segment prev = (ss.size()>0) ? ss.get(0) : null;
		Segment next = null;
		int[] startArr = starts.elements();
		int[] endArr = ends.elements();
		Segment[] ssArr = ss.elements();		
		Segment[] trArr = tr.elements();
		int start =  (prev!=null && prev.type==0 && Math.abs(prev.start.x-prev.end.x)<TrackSegment.EPSILON*0.5) ? 1 : 0;		
		int size = 0;
		int kp = 0;
		int kn = 0;						
		Vector2D[] opoints = (which==1) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;
		int otherTo = (which==1) ? CircleDriver2.edgeDetector.lSize:  CircleDriver2.edgeDetector.rSize;		
		int sz = tr.size();
		int szs = 0;
		if (prev!=null && start!=0) prev.updated = false; 

		for (int ii = start;ii<sz;++ii){
			int j = indexArr[ii];
			int ssz = ss.size();
			Segment s = (which==1) ? trArr[ii].rightSeg : trArr[ii].leftSeg;
			boolean expandUp = true;
			boolean expandDown = true;			
			if (s==null || s.type==Segment.UNKNOWN) continue;

			kp = (ii<=0) ? 0 : (ii>=sz) ? sz : indexArr[ii-1];
			prev = (ii<=0 || ii>=sz )? null : (which==1) ? trArr[ii-1].rightSeg : trArr[ii-1].leftSeg;;

			kn = (ii<0) ? sz : (ii>=sz-1) ? sz : indexArr[ii+1];
			next = (ii<0 || ii>=sz-1 )? null : (which==1) ? trArr[ii+1].rightSeg : trArr[ii+1].leftSeg;

			if (s.lower!=null && s.start.y<s.lower.y){
				System.out.println();
				if (prev!=null){
					//					Vector2D point = new Vector2D();
					//					point.x = s.lower.x;
					//					point.y = s.lower.y;
					//					calibrateRemove(prev, kp, s, j, v, -which*tW, startArr, endArr, point);
				}
			}

			if (s.upper!=null && s.end.y>s.upper.y){
				System.out.println();
			}
			if (!s.updated && (prev==null || (kp==j-1 && !prev.updated))) continue;
			if (s.updated){
				int firstIndex = s.startIndex;
				int lastIndex = s.endIndex+1;
				size = s.num;
				int sr = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius+s.type*tW*which);
				if (s.map==null) s.map = new int[MAX_RADIUS];
				int[] map = s.map;
				int num = s.num;
				if (j==0 || prev ==null){
					Vector2D ft = v[firstIndex];
					if (s.center!=null  && size>1){
						Vector2D center = circle(ft, v[lastIndex-1], s.center, s.radius);
						center.x = center.length()*s.type;
						center.y = 0;
						double d = check(v, firstIndex,lastIndex, s.center, s.radius);
						double d1 = check(v, firstIndex,lastIndex, center, s.radius);
						boolean ok = true;
						double[] rr = new double[3];
						if (d<0){													
							Vector2D o = ft.plus(v[lastIndex-1]).times(0.5);
							Vector2D n = o.minus(ft).orthogonal();							
							Geom.getLineLineIntersection(o.x, o.y, o.x+n.x, o.y+n.y, 0, 0, 1, 0, rr);	
							double dx = rr[0]-ft.x;
							double dy = rr[1]-ft.y;
							double dd = Math.sqrt(dx*dx+dy*dy);
							double r = Math.round(dd*100.0d)/100.0d;															
							ok = (Math.abs(r-s.radius)<0.5);
							if (ok) map[sr]++; 
						}
						if (!ok || (d<0 && d1<0)){
							int kk = lastIndex-1;
							for (;kk>firstIndex+1;--kk){
								Vector2D lst = v[kk-1];
								Vector2D o = ft.plus(lst).times(0.5);
								Vector2D n = o.minus(ft).orthogonal();										
								Geom.getLineLineIntersection(o.x, o.y, o.x+n.x, o.y+n.y, 0, 0, 1, 0, rr);
								if (rr[1]<-SMALL_MARGIN) continue;
								double dx = rr[0]-ft.x;
								double dy = rr[1]-ft.y;
								double dd = Math.sqrt(dx*dx+dy*dy);
								double r = Math.round(dd*100.0d)/100.0d;																
								double de = r-s.radius;
								if (de<0) de = -de;
								if (de<0.5){
									if (map[sr]>3) r = s.radius;									
									center =  circle(ft, lst, center, r);
									center.x = center.length()*s.type;
									center.y = 0;
									if ((d1 = check(v, firstIndex,kk, center, r))>=0){									
										s.end = new Vector2D(lst);				
										s.endIndex = kk-1;
										s.center = center;									
										s.reCalLength();													
										s.num = kk-firstIndex;
										s.radius = r;
										map[sr]++;
										break;
									}
								}
							}

							if (kk==firstIndex+1) {
								if (!isConfirmed(s, which, tW)) {
									s.type = Segment.UNKNOWN;
									s.radius = -1;
									s.map = null;
								} else {								
									s.map[sr]--;
								}
							}
						} else if (d<0 || d>d1){ 							
							s.center = center;
						} 
						//							Vector2D fst = (size>0) ? tmp[0] : null;
						//							int lastIndex = expandForward(v, from, to, -tW*which, s.center, s.radius, prev, next, s, tmp,size,firstIndex+size-1, fst, marks, 1);
						//							count = lastIndex - firstIndex +1;

					} else if (s.center!=null){
						//						Vector2D fst = (size>0) ? tmp[0] : null;
						//						int lastIndex = expandForward(v, from, to, -tW*which, s.center, s.radius, prev, next, s, tmp,size,firstIndex+size-1, fst, marks, 1);
						//						count = lastIndex - firstIndex +1;
					}
				} else if (size>=2 && s.type!=0 && j>0){
					Vector2D p = v[firstIndex];
					Vector2D q = v[lastIndex-1];
					Vector2D m = p.plus(q).times(0.5);
					Vector2D n = q.minus(p).orthogonal().normalised();
					double pq = p.distance(q) * 0.5;
					if (n.dot(new Vector2D(s.type,0))<0) n = n.negated();
					Vector2D center = m.plus(n.times(Math.sqrt(s.radius*s.radius-pq*pq)));
					double d = check(v,firstIndex, lastIndex, center, s.radius);
					double d1 = (s.center!=null) ? check(v, firstIndex,lastIndex, s.center, s.radius) : -1;
					if (d1<0 && d<0 && (mark!=2 || prev==null || prev.type!=0)){
						s.type = UNKNOWN;
						s.radius = -1;
						s.map = null;
					} else {
						if (d1<0 || d1>d) s.center = new Vector2D(center);					
						getAllPoints(v, firstIndex, lastIndex, -which*tW, center, s.radius, prev,next, s, mark);					
					}
				} else if (size>0 && s.type!=0){
					if (size>1) 
						getAllPoints(v, firstIndex, lastIndex, -which*tW, s.center, s.radius, prev,next, s, mark);
					else if (CircleDriver2.inTurn && prev!=null && prev.type!=0 && isConfirmed(prev, which, tW) && prev.upper!=null){
						double r = prev.radius;
						double R = s.radius;
						Vector2D cntr = prev.center;
						double cx = cntr.x;
						double cy = cntr.y;
						Vector2D first = v[firstIndex];
						double dx = cx - first.x;
						double dy = cy - first.y;
						double d = dx*dx+dy*dy;
						double a = (prev.type==s.type) ?  r * (R+R-r) : r * (R+R+r);
						double l = a/d;														
						double px = (prev.type==s.type) ? cx + l*dx : cx - l*dx;
						double py = (prev.type==s.type) ? cy + l*dy : cy - l*dy;
						Vector2D point = new Vector2D(px,py);
						s.center = circle(first, point, s.center, R);						
					}
				} else if (size>1 && s.type==0){
					Vector2D first = v[firstIndex];
					Vector2D last = v[lastIndex-1];
					double d = first.x-last.x;					
					if ((d<0 ? - d : d ) <=0.1*TrackSegment.EPSILON) continue;
					if (size>=2){						
						//						Vector2D[] tmp = new Vector2D[size+2];
						//						tmp[0] = s.start;
						//						int sz = s.start.equals(first) ? 0 : 1;					
						//						System.arraycopy(v, firstIndex, tmp, sz, size);
						//						sz+=size;
						//						if (s!=null && !last.equals(s.end)) tmp[sz++] =s.end;
						double total = 1;
						double[] coef = new double[2];
						double a = (first.y-last.y)/d;
						double b = first.y - a*first.x;
						double x1 = (first.y-b)/a;
						double x2 = (last.y-b)/a;						

						double tot = 0;
						for (int kk = firstIndex;kk<lastIndex;++kk){
							Vector2D vv = v[kk];
							tot += Math.sqrt(Geom.ptLineDistSq(x1, first.y, x2, last.y, vv.x, vv.y, null));							
						}
						tot /= (lastIndex-firstIndex+0.0d);
						if (size>3){							
							total = bestFitLine(v, firstIndex,lastIndex+1, coef);
							double t = (total<tot) ? total : tot;
							if (t>EPSILON && !(s.lower!=null && s.upper!=null) && s.map[MAX_RADIUS-1]<3){								
								s.type = Segment.UNKNOWN;							
								s.radius = -1;
							}
							if (tot>total){
								a = coef[0];
								b = coef[1];
							}
						} 

						if (s.type!=UNKNOWN) {
							if (size>=3){
								Vector2D vv =  (size==3) ? v[firstIndex+1] : v[(firstIndex+lastIndex)/2];
								double[] temp = new double[3];
								boolean isCircle = Geom.getCircle(first, last, vv, temp);
								int rr = (isCircle) ? (int)Math.sqrt(temp[2]) : MAX_RADIUS-1;
								if (rr>MAX_RADIUS-1) rr = MAX_RADIUS-1;
								if (rr>=MAX_RADIUS-1) 
									s.map[rr]++;
								else if (rr>REJECT_VALUE && rr<MAX_RADIUS-1){
									s.map[rr]++;
									s.map[MAX_RADIUS-1]--;
									if (s.map[MAX_RADIUS-1]==0){
										s.map = null;
										s.type = UNKNOWN;
										s.radius = -1;
									}
								}
							}
							s.num =  size;									
							if (!Double.isInfinite(a)){
								s.start.x = (s.start.y-b)/a;
								s.end.x = (s.end.y-b)/a;
							}

						}
					}
				}

				if (s.type!=UNKNOWN && s.num<num){
					s.updated = true;					
					expandUp = false; 
					Segment ns = (j<ssz-1) ? ssArr[j+1] : null;
					int strt = startArr[j]+s.num;
					s.endIndex = endArr[j] =  strt-1;
					int idx = j+1;
					expandUp = false;
					if (ns==null || ns.type!=UNKNOWN){
						ns = new Segment();						
						int end = endArr[j];
						ns.start = new Vector2D(v[strt]);
						ns.end = new Vector2D(v[end]);
						ns.num = num-s.num;

						if ((ssz-=idx)>0){
							System.arraycopy(ssArr, idx, ssArr, idx+1, ssz);
							System.arraycopy(startArr, idx, startArr, idx+1, ssz);
							System.arraycopy(endArr, idx, endArr, idx+1, ssz);
						}
						ssz+=idx+1;	
						ns.points = v;
						ns.startIndex = startArr[idx] = strt;
						ns.endIndex = endArr[idx]= end;
						ssArr[idx] = ns;
						for (int ji=ii+1;ji<sz;++ji) indexArr[ji]++;
						kn++;
					} else {
						ns.points = v;
						ns.start = new Vector2D(v[strt]);
						ns.num += num-s.num;
						ns.startIndex = startArr[idx] = strt;
						ns.endIndex = endArr[idx];
					}								
				}
				starts.size(ssz);
				ends.size(ssz);
				ss.size(ssz);
				szs = ssz;
				if (s.type==Segment.UNKNOWN){
					if (s.upper!=null){
						if (next!=null) next.lower = null;
						s.upper = null;
					}
					if (s.lower!=null){
						if (prev!=null) prev.upper = null;
						s.lower = null;
					}
					//					if (prev!=null){
					//						int st = startArr[j];
					//						int en = endArr[j];
					//						int kk = kp-1;
					//						Segment prevPrev = null;
					//						if (kp>0) {
					//							kk = indexArr[kp-1];
					//							prevPrev = ssArr[kk];
					//							expand(v,st,en,prevPrev, kk, prev, kp, null, j, ss, starts, ends, -which*tW,false,other);
					//						}						
					//					}
				}
			}

			if (s.updated && s.type!=UNKNOWN && s.num>1) {
				if (s.opp==null) {
					s.opp = new Segment();
					s.opp.points = opoints;
				}
				reSynchronize(s,s.opp,0,otherTo,-which,tW*2);								
			}

			if (prev!=null && s.type!=UNKNOWN){
				Vector2D point = new Vector2D();
				boolean isConn = isConnected(prev, s, -which*tW, point);
				if (isConn && prev.type==0 && prev.end.y>point.y+2) {
					prev.upper = null;
					s.lower = null;
					isConn = false;
				} else if (!isConn && (isPrevNextConnected || isNextPrevConnected) ){
					System.out.println();
				}
				if (s.lower!=null && !isConn && s.num>2){
					boolean isStraight = (prev.type==0 && Math.abs(prev.start.x-prev.end.x)<TrackSegment.EPSILON*0.5);
					int st = startArr[j];
					int en = endArr[j];
					double t =0;
					if (s.num>0){
						Vector2D e = v[en];						
						if ((t=e.distance(s.end))<EdgeDetector.MINDIST && t>0){
							s.end.x = e.x;
							s.end.y = e.y;
							isConn = isConnected(prev, s, -which*tW, point);
						}						
						if (!isConn){
							e = v[st];						
							if ((t = e.distance(s.start))<EdgeDetector.MINDIST && t>0){
								s.start.x = e.x;
								s.start.y = e.y;
								isConn = isConnected(prev, s, -which*tW, point);
							}
							if (!isConn && prev.num>2){
								st = startArr[kp];
								en = endArr[kp];
								e = v[st];						
								if ((t = e.distance(prev.start))<EdgeDetector.MINDIST && t>0){
									prev.start.x = e.x;
									prev.start.y = e.y;	
									if (isStraight) prev.end.x = prev.start.x;
									isConn = isConnected(prev, s, -which*tW, point);
								}
								if (!isConn){
									e = v[en];						
									if ((t = e.distance(prev.end))<EdgeDetector.MINDIST && t>0){
										prev.end.x = e.x;
										prev.end.y = e.y;
										if (isStraight) prev.start.x = prev.end.x;
										isConn = isConnected(prev, s, -which*tW, point);
									}
								}
							}
						}
					} else {
						prev.upper = null;
						s.lower = null;
					}
				}
				if (isConn){					
					calibrateConnected(prev, kp, s, j, v, -which*tW, startArr, endArr, point);
					if (prev!=null && prev.updated && prev.type!=UNKNOWN){						
						if (prev.opp==null) {
							prev.opp = new Segment();
							prev.opp.points = opoints;
						}
						reSynchronize(prev,prev.opp,0,otherTo,-which,tW*2);														
					}
					if (s.updated && s.type!=UNKNOWN){						
						if (s.opp==null) {
							s.opp = new Segment();
							s.opp.points = opoints;
						}
						reSynchronize(s,s.opp,0,otherTo,-which,tW*2);		
						s.updated = false;
					}
					expandDown = false;
					int sI = prev.endIndex+1;
					int eI = s.startIndex-1;
					if (sI>eI && kp<j-1){
						ss.removeElements(kp+1, j);
						starts.removeElements(kp+1, j);
						ends.removeElements(kp+1, j);
						kn -= j-kp-1;
						j = kp+1;
					} else if (sI<=eI){
						if (kp<j-2){
							ss.removeElements(kp+2, j);
							starts.removeElements(kp+2, j);
							ends.removeElements(kp+2, j);
							kn -= j-kp-2;
							j = kp+2;
						}

						if (j==kp+1){
							Segment ns = new Segment();
							ss.add(kp+1, ns);
							ns.start = new Vector2D(v[sI]);
							ns.end = new Vector2D(v[eI]);
							ns.num = eI-sI+1;
							ns.startIndex = sI;
							ns.endIndex = eI;
							starts.add(kp+1, sI);
							ends.add(kp+1, eI);
							j++;
							kn++;
						} else {
							Segment ns = ss.get(kp+1);
							ns.start = new Vector2D(v[sI]);
							ns.end = new Vector2D(v[eI]);
							ns.num = eI-sI+1;
							ns.startIndex = sI;
							ns.endIndex = eI;
							starts.set(kp+1, sI);
							ends.set(kp+1, eI);
						}
					}				
				} else if (isPossiblyConnected){
					if (prev.type!=0 && s.type!=0 && s.num>1){
						if (prev.lower!=null){
							Segment tmp = new Segment();
							radiusFrom2Points(prev, s.points[s.startIndex], s.points[s.endIndex], -which*tW, tmp);
							if (tmp.type!=0 && tmp.type!=UNKNOWN && check(v, s.startIndex, s.endIndex+1, tmp.center, tmp.radius)>=0 && isConnected(prev, tmp, -which*tW, point)){
								double rad = s.radius;
								int rr = score(s, which, tW);
								if (s.map!=null) s.map[rr]--;
								Segment.apply(s, tW, s.start, s.end, tmp.center, tmp.radius);
								if (rad!=s.radius){
									s.lower = new Vector2D(point);
									prev.upper = new Vector2D(point);
									if (s.opp==null) {
										s.opp = new Segment();
										s.opp.points = opoints;
									}
									reSynchronize(s,s.opp,0,otherTo,-which,tW*2);
								} 
							} else {
								s.lower = null;
								prev.upper = null;
							}
						} else {
							s.lower = null;
							prev.upper = null;
						}
					} //end of if					
					Segment p = ssArr[j-1];
					if (p.num==0) 
						expandDown = false;
					else {
						Vector2D pp = v[p.endIndex];
						if (pp.y<point.y-1) expandDown = false;
					}				
				} else {
					s.lower = null;//end of else
					prev.upper = null;
				}

				if (!isConn && s.lower!=null) expandDown = false;								
			}



			if (expandDown && s.type!=UNKNOWN && s.type!=0 && s.center!=null && prev!=null && prev.type==0 && Math.abs(prev.start.x-prev.end.x)<TrackSegment.EPSILON*0.5){
				Segment p = ssArr[j-1];
				if (p.num==0) 
					expandDown = false;
				else {
					Vector2D pp = v[p.endIndex];
					if (pp.y<s.center.y-1) expandDown = false;
				}
			}

			if (expandUp && next!=null && j<ss.size()-1  && s.type!=UNKNOWN ){
				int st = startArr[j+1] ;
				int en = (kn-1>j || (next!=null && next.num>0)) ? endArr[j+1] : endArr[kn]+1;
				if (j<kn-1)
					szs = expand(v,st,en,prev, kp, s, j, next, kn, ss, starts, ends, -which*tW,false,other);
				else if (next!=null && next.num>0) szs = expandFw(v,st,en,prev, kp, s, j, next, kn, ss, starts, ends, -which*tW,true,other);
			}

			if (szs!=ssz){
				int idx = j+1;
				if ((sz-=idx+1)>0) System.arraycopy(trArr, idx+1, trArr, idx, sz);
				sz+=idx;
				kn -= ssz-szs;
				for (int jj = ii+1;jj<sz;++jj) indexArr[jj]--;
				ssz = szs;
			}

			if (expandDown && j>0 && s.type!=UNKNOWN){
				int en = startArr[j]-1;
				int st = (prev!=null && prev.num>0) ? startArr[kp] : startArr[j-1];
				if (kp+1<j || (prev!=null && prev.num>0)) szs = expand(v,st,en,prev, kp, s, j, next, kn, ss, starts, ends, -which*tW,true,other);
			}

			if (szs!=ssz){
				int idx = j-1;
				if ((sz-=j)>0) System.arraycopy(trArr, j, trArr, idx, sz);
				sz+=idx;
				kn -= ssz-szs;
				j -= ssz-szs;
				for (int jj = ii;jj<sz;++jj) indexArr[jj]--;
				ssz = szs;
			}

		}//end of for
		reMerge(v, from, to, starts, ends, ss);
	}//*/

	/*private static final void reReFillGap(ObjectArrayList<Segment> tr,double tW){
		Segment[] trArr = tr.elements();
		int trSz = tr.size();
		Segment prev = null;
		Segment next = null;
		int sL = CircleDriver2.edgeDetector.lSize;
		int sR = CircleDriver2.edgeDetector.rSize;
		int startIndxL = 0;
		int endIndxL = 0;
		int startIndxR = 0;
		int endIndxR = 0;
		Vector2D[] left = CircleDriver2.edgeDetector.left;
		Vector2D[] right = CircleDriver2.edgeDetector.right;
		int prevIndex = 0;
		int nextIndex = 0;
		for (int i = 0;i<trSz;++i){
			Segment t = trArr[i];
			prevIndex = i-1;
			nextIndex = i+1;
			next = (i<trSz-1) ? trArr[i+1] : null;
			Segment l = t.leftSeg;
			Segment r = t.rightSeg;
			Segment nl = (next==null) ? null : next.leftSeg;
			Segment nr = (next==null) ? null : next.rightSeg;
			Segment pl = (prev==null) ? null : prev.leftSeg;
			Segment pr = (prev==null) ? null : prev.rightSeg;			
			if (prev==null && t.start.y<=0){
				prev = t;
				continue;
			} 

			startIndxL = (pl==null) ? 0 : pl.endIndex+1;
			startIndxR = (pr==null) ? 0 : pr.endIndex+1;
			endIndxL = (l==null) ? sL : l.startIndex;
			endIndxR = (r==null) ? sR : r.startIndex;
			int numL = endIndxL-startIndxL;
			int numR = endIndxR - startIndxR; 
			if (prev!=null && prev.type==t.type && prev.type!=0 && Math.abs(prev.radius-t.radius)<3){
				if (prev.radius==t.radius){
					int sr = (l.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(l.radius-l.type*tW);
					if (numL>0){
						l.startIndex = startIndxL;
						l.num += numL;
						l.start = new Vector2D(left[startIndxL]);
						if (l.num>1) l.center = circle(l.start, l.end, l.center, l.radius);
						if (l.map[sr]<pl.map[sr]) l.map[sr] = pl.map[sr];
						reSynchronize(l, r, startIndxR, endIndxR, 1, tW);
						endIndxR = r.startIndex;
						numR = endIndxR - startIndxR;
					}
					if (numR>0){
						r.startIndex = startIndxR;
						r.num += numR;
						r.start = new Vector2D(right[startIndxR]);
						if (r.num>1) r.center = circle(r.start, r.end, r.center, r.radius);
						if (r.map[sr]<pr.map[sr]) r.map[sr] = r.map[sr];
						reSynchronize(r, l, startIndxL, endIndxL, -1, tW);
						endIndxL = l.startIndex;
						numL = endIndxL - startIndxL;
					}
					continue;
				}

				int sr = (pl.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(pl.radius-pl.type*tW);
				if ((pl.num<3 && pr.num<3) || pl.map[sr]<=3){
					startIndxL = pl.startIndex;
					startIndxR = pr.startIndex;
					numL = endIndxL-startIndxL;
					numR = endIndxR - startIndxR;					
					prev = (i>1) ? trArr[i-2] : null;
					prevIndex = i-2;
					pl = (prev==null) ? null : prev.leftSeg;
					pr = (prev==null) ? null : prev.rightSeg;
					if (numL>0){
						reExpand(pl, l, nl, -1, tW);
						endIndxL = l.startIndex;
						endIndxR = r.startIndex;
						numL = endIndxL-startIndxL;
						numR = endIndxR - startIndxR;
					} 
					if (numR>0){
						reExpand(pr, r, nr, 1, tW);
						endIndxL = l.startIndex;
						endIndxR = r.startIndex;
						numL = endIndxL-startIndxL;
						numR = endIndxR - startIndxR;
					} 
				}
			}

			int sr = (l.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(l.radius-l.type*tW);
			if (nl!=null && numL+numR>0 && (l.num<3 && r.num<3 && l.upper==null) || l.map[sr]<=3){
				endIndxL = (nl==null) ? nl.startIndex-1 : l.endIndex+1;
				endIndxR = (nr==null) ? nr.startIndex-1 : r.endIndex+1;
				numL = endIndxL-startIndxL;
				numR = endIndxR - startIndxR;					
				Segment nNext= (i<trSz-2) ? trArr[i+2] : null;
				Segment nNl = (nNext==null) ? null : nNext.leftSeg;
				Segment nNr = (nNext==null) ? null : nNext.rightSeg;
				if (numL>0){
					reExpand(pl, nl, nNl, -1, tW);
					endIndxL = nl.startIndex;
					endIndxR = nr.startIndex;
					numL = endIndxL-startIndxL;
					numR = endIndxR - startIndxR;
				} 
				if (numR>0){
					reExpand(pr, nr, nNr, 1, tW);
					endIndxL = nl.startIndex;
					endIndxR = nr.startIndex;
					numL = endIndxL-startIndxL;
					numR = endIndxR - startIndxR;
				} 
			}
			if (numL==0 && numR==0) continue;
			if (numL>=numR){
				if (numL>1){

				}
			}
		}
	}//*/


	/*private static final void reFillGap(final Vector2D[] v,int from, int to,int which,double tW,ObjectArrayList<Segment> tr,ObjectArrayList<Segment> ss,ObjectArrayList<Segment> other){
		Segment[] elems = ss.elements();
		Segment tmp;
		int sz = ss.size();
		Segment prev = null;
		int minFromSeg = 0;
		int osz = other.size();
		Segment l = (other==null || osz<1) ? null : other.get(osz-1);
		Vector2D[] opoints = (which==1) ? CircleDriver2.edgeDetector.left : CircleDriver2.edgeDetector.right;
		int otherTo = (which==1) ? CircleDriver2.edgeDetector.lSize:  CircleDriver2.edgeDetector.rSize;		

		//		if (CircleDriver2.time>=CircleDriver2.BREAK_TIME){
		//			int start = startArr[sz-2];
		//			Vector2D[] vv = new Vector2D[to-start];
		//			for (int i=start;i<to;++i){
		//				vv[to-1-i] = v[i];
		//			}
		//			if (to-start>0){
		//				ObjectArrayList<Segment> tt = segmentize(vv, 0, to-start, tW, starts, ends, null);
		//				System.out.println();
		//			}
		//		}
		for (int i = 0;i<sz;++i){
			mark = i+1;
			Segment s = elems[i];
			if (CircleDriver2.time>=CircleDriver2.BREAK_TIME && i>0 && s.startIndex-1>elems[i-1].endIndex){
				System.out.println();				
			} else if (s!=null && s.type!=Segment.UNKNOWN) {				
				continue;			
			}						
			int fromSeg = i;
			int toSeg = i+1;
			while (fromSeg>minFromSeg && (tmp=elems[fromSeg-1])!=null && tmp.num>0 && (tmp.type==UNKNOWN || !isConfirmed(tmp, which, tW))) fromSeg--;
			while (toSeg<sz && (tmp=elems[toSeg])!=null && tmp.num>0 && (tmp.type==UNKNOWN || !isConfirmed(tmp, which, tW))) toSeg++;
			boolean ok = false;
			//			for (int k=fromSeg;k<toSeg;++k){ 
			//				if (elems[k].updated){
			//					ok = true;
			//					break;
			//				}
			//			}
			//			if (!ok){
			//				if (fromSeg>0 && elems[fromSeg-1].updated) 
			//					ok = true;
			//				else if (toSeg<sz && elems[toSeg].updated) ok = true;
			//				if (!ok){
			//					System.out.println();
			//					continue;
			//				}
			//			}
			int firstIndex = (fromSeg==0) ? elems[fromSeg].startIndex : (elems[fromSeg-1].endIndex+1);
			int lastIndex = elems[toSeg-1].endIndex+1;
			int size = lastIndex - firstIndex;

			if (size>1 && ss!=null){				
				sal.size(toSeg-fromSeg);
				ss.getElements(fromSeg, sal.elements(), 0, toSeg-fromSeg);
				int kn = (toSeg<sz) ? toSeg : sz;
				int kp = (fromSeg>0) ? fromSeg-1 : -1;
				Segment nextSeg = (toSeg<sz) ? elems[toSeg] : null;
				prev = (fromSeg>0) ? elems[fromSeg-1] : null;				
				oal.size(0);				
				ObjectArrayList<Segment> li = bestGuess(v, firstIndex, lastIndex, -which*tW, prev, nextSeg, oal);
				int liSz = (li==null) ? 0 : li.size();
				Segment[] liArr = (liSz>0) ? li.elements() : null;
				if ((li==null && prev!=null && prev.endIndex>=firstIndex) || (liSz==1 && liArr[0].type==UNKNOWN)) {
					if ((sz-=toSeg)>0)
						System.arraycopy(elems, toSeg, elems, fromSeg, sz);

					sz+=fromSeg;
					minFromSeg = fromSeg+1;
					continue;
				}				

				Segment[] salArr = sal.elements();
				if (liSz>0) {					
					int ols = sal.size();
					if (prev!=null && prev.endIndex>=firstIndex){
						int score = (prev.map!=null) ? prev.map[score(prev, which, tW)] : 1;
						int oldSz = ols;
						for (int ii = 0;ii<ols;){
							Segment t = salArr[ii];
							if (t.start.y>prev.end.y) break;
							int tScore = (t.type==Segment.UNKNOWN) ? 0 : (t.map!=null) ? score(t, which, tW) : 0;							
							int sc = (t.type==Segment.UNKNOWN) ? 0 : (t.map!=null) ? t.map[tScore] : 1;
							if (sc<=score && --ols>=0){
								System.arraycopy(salArr, 1, salArr, 0, ols);
							} else break;
						}
						if (ols!=oldSz) sal.size(ols);
					}
					if (ols==liSz && ols==1){
						Segment t = liArr[0];
						t.points = v;						
						if (salArr[0].type==UNKNOWN && t.type!=UNKNOWN){												
							if (t.opp==null) 
								t.opp = (elems[i]==null || elems[i].opp==null) ? new Segment() : elems[i].opp;
								t.opp.points = opoints;							
								reSynchronize(t,t.opp,0,otherTo,-which,tW*2);
								elems[i] = t;		
								if (prev!=null && prev.type!=Segment.UNKNOWN && (prev.type!=t.type || (prev.type!=0 && prev.radius!=t.radius))) 
									if (prev.type!=0 && prev.center.y!=0) reExpand(prev, t, nextSeg, which,tW);							
						} else {
							apply(s, tW, t.start, t.end, t.center, t.radius);
							if (s.opp==null) 
								s.opp = new Segment();
							s.opp.points = opoints;							
							reSynchronize(s,s.opp,0,otherTo,-which,tW*2);	
							if (prev!=null && prev.type!=Segment.UNKNOWN && (prev.type!=t.type || (prev.type!=0 && prev.radius!=t.radius)))
								reExpand(prev, s, nextSeg, which,tW);

						}

						minFromSeg = fromSeg+1;
						continue;
					}
					if (ols==1 && salArr[0].type==Segment.UNKNOWN) ols = 0;
					if (ols!=0) mix(sal,li,which,tW);
					liSz = li.size();						
					for (int k = liSz-1;k>=0;--k) if (liArr[k]!=null && liArr[k].type==UNKNOWN) --liSz;

					if (li!=null){
						if ((sz-=toSeg)>0)
							System.arraycopy(elems, toSeg, elems, fromSeg, sz);							
						sz+=fromSeg;												
						if ((sz-=fromSeg)>0)
							System.arraycopy(elems, fromSeg, elems, fromSeg+liSz, sz);

						sz+=fromSeg+liSz;
						toSeg -= (toSeg-fromSeg-liSz);
						if (liArr!=null){
							for (int j = 0,k=0;k<liSz;++j){
								if (liArr[j]==null){ 
									k++;
								} else if (liArr[j].type!=UNKNOWN) {
									elems[fromSeg+k++] = liArr[j];
								}
							}
							//							System.arraycopy(liArr, 0, elems, fromSeg, liSz);
							//						System.arraycopy(ial.elements(), 0, startArr, fromSeg, liSz);
							//						System.arraycopy(ial1.elements(), 0, endArr, fromSeg, liSz);

							int oldSz = sz;
							boolean fst = true;
							for (int k = 0;k<liSz;++k){
								int idx = fromSeg+k;
								Segment t = elems[idx];

								if (t.type!=UNKNOWN) {
									for (int j = idx+1;j<sz;++j){
										nextSeg = (j>=sz) ? null : elems[j];
										if (nextSeg==null || nextSeg.type!=UNKNOWN) break;
									}
									if (nextSeg!=null && nextSeg.type==UNKNOWN) 
										nextSeg = null;
									else if (nextSeg!=null && nextSeg.opp==null){ 
										nextSeg.opp = new Segment();
										nextSeg.opp.points = opoints;								
										reSynchronize(nextSeg,nextSeg.opp,0,otherTo,-which,tW*2);
									}							
									t.points = v;
									if (t.opp==null) 
										t.opp = new Segment();
									t.opp.points = opoints;								
									reSynchronize(t,t.opp,0,otherTo,-which,tW*2);	
									if (fst) {
										i = idx;
										fst = false;
									}
									if (prev!=null && prev.type!=Segment.UNKNOWN && (prev.type!=t.type || (prev.type!=0 && prev.radius!=t.radius))) 
										reExpand(prev, t, nextSeg, which,tW);
									if (!CircleDriver2.inTurn && t.upper==null && t.lower==null && t.num<=2 && t.opp.num<=2) {
										t.type = Segment.UNKNOWN;
										t.opp.type = Segment.UNKNOWN;
									}
									if (t.type!=Segment.UNKNOWN) prev = t;
								}

							}
						}
						fromSeg = i;							
						minFromSeg = fromSeg+1;
					}
				}
			}
		}//end of for		
		ss.size(sz);
	}//*/


	//from inclusive, to exclusive
	public static final void segmentize(final Vector2D[] v,int from, int to,int which,double tW,ObjectArrayList<Segment> guess,double dist,ObjectArrayList<Segment> ss,ObjectArrayList<Segment> other){
		//		int len = to - from;
		//		long ti = System.nanoTime();
		//		Segment[] ssArr = ss.elements();
		//		Segment[] oArr = other.elements();		
		//		Segment[] trArr = guess.elements();
		//		int osz = other.size();
		//			if (sz<=1) return;
		//		reUpdate(v, from, to, which, tW, starts, ends, guess,ss,other,indexArr,indexArrO);		
		reUpdate(guess.elements(),guess.size(), tW);			

		//		reFillGap(v, from, to, which, tW,guess,ss,other);


		if (CircleDriver2.debug) System.out.println("Segmentize took : "+(System.nanoTime()-CircleDriver2.ti)/1000000+" ms.");
	}


	static void drawTrack(ObjectList<Segment> ts,final String title){
		XYSeries series = new XYSeries("Curve");
		if (ts==null) return;

		for (Segment t : ts){
			if (t.type==TrackSegment.STRT){
				TrackSegment.line(t.start.x, t.start.y, t.end.x, t.end.y, series);
			} else {
				TrackSegment.arc(t.center.x, t.center.y, t.radius, t.start.x,t.start.y,t.arc,series);								
			}
		}

		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );		
		chart.getXYPlot().getDomainAxis().setRange(-60.0,60.0);
		chart.getXYPlot().getRangeAxis().setRange(-10.0,110.0);

		Thread p = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					BufferedImage image = chart.createBufferedImage(500, 500);
					ImageIO.write(image, "png", new File(title+".png"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});

		p.start();

	}

	public final static int score(Segment s,int which,double tW){
		if (s.type==UNKNOWN) return -1;
		if (s.map==null) return 0;
		if (s.type==0) return MAX_RADIUS-1;
		int sr = (s.type==0 || s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : (int)Math.round(s.radius+s.type*tW*which);
		if (sr>=MAX_RADIUS) sr = MAX_RADIUS-1;
		return sr;
	}

	public static void drawTrack(ObjectList<Segment> ts,final String title,double dist){			
		XYSeries series = new XYSeries("Curve");
		if (ts==null) return;
		for (Segment tt : ts){
			if (tt.dist+tt.length<dist)
				continue;			
			if (tt.type==TrackSegment.STRT){
				TrackSegment.line(tt.start.x, tt.start.y, tt.end.x, tt.end.y, series);
			} else {
				TrackSegment.arc(tt.center.x, tt.center.y, tt.radius, tt.start.x,tt.start.y,tt.arc,series);								
			}
		}

		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );		
		chart.getXYPlot().getDomainAxis().setRange(-60.0,60.0);
		chart.getXYPlot().getRangeAxis().setRange(-10.0,110.0);

		Thread p = new Thread(new Runnable(){
			@Override
			public void run() {				
				try{
					BufferedImage image = chart.createBufferedImage(500, 500);
					ImageIO.write(image, "png", new File(title+".png"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});

		p.start();


	}

	@Override
	public String toString() {
		int len = tpo.length;	
		len = int2string(this.type, buf, len);
		System.arraycopy(nm, 0, buf, len, nm.length);
		len+=nm.length;
		len = int2string(num, buf, len);

		System.arraycopy(rad, 0, buf, len, rad.length);
		len+=rad.length;
		len = double2string(this.radius,buf,len);		

		System.arraycopy(strt, 0, buf, len, strt.length);
		len+=strt.length;
		if (start!=null){
			buf[len++]='(';
			len = double2string(this.start.x,buf,len);
			buf[len++]=',';
			len = double2string(this.start.y,buf,len);
			buf[len++]=')';
		} else {
			buf[len++] = 'n';
			buf[len++] = 'u';
			buf[len++] = 'l';
			buf[len++] = 'l';
		}

		System.arraycopy(en, 0, buf, len, en.length);
		len+=en.length;
		if (end!=null){
			buf[len++]='(';
			len = double2string(this.end.x,buf,len);
			buf[len++]=',';
			len = double2string(this.end.y,buf,len);
			buf[len++]=')';
		} else {
			buf[len++] = 'n';
			buf[len++] = 'u';
			buf[len++] = 'l';
			buf[len++] = 'l';
		}

		System.arraycopy(cnt, 0, buf, len, cnt.length);
		len+=cnt.length;		
		if (center!=null){
			buf[len++]='(';
			len = double2string(this.center.x,buf,len);
			buf[len++]=',';
			len = double2string(this.center.y,buf,len);
			buf[len++]=')';
		} else {
			buf[len++] = 'n';
			buf[len++] = 'u';
			buf[len++] = 'l';
			buf[len++] = 'l';
		}

		System.arraycopy(lngth, 0, buf, len, lngth.length);
		len+=lngth.length;
		len = double2string(this.length,buf,len);

		System.arraycopy(lwer, 0, buf, len, lwer.length);
		len+=lwer.length;
		if (lower!=null){
			buf[len++]='(';
			len = double2string(this.lower.x,buf,len);
			buf[len++]=',';
			len = double2string(this.lower.y,buf,len);
			buf[len++]=')';
		} else {
			buf[len++] = 'n';
			buf[len++] = 'u';
			buf[len++] = 'l';
			buf[len++] = 'l';
		}
		
		System.arraycopy(uper, 0, buf, len, uper.length);
		len+=uper.length;
		if (upper!=null){
			buf[len++]='(';
			len = double2string(this.upper.x,buf,len);
			buf[len++]=',';
			len = double2string(this.upper.y,buf,len);
			buf[len++]=')';
		} else {
			buf[len++] = 'n';
			buf[len++] = 'u';
			buf[len++] = 'l';
			buf[len++] = 'l';
		}

		System.arraycopy(fin, 0, buf, len, fin.length);
		len+=fin.length;				
		return new String(buf,0,len);		
	}
	
	
	public void toBuffer(StringBuffer sb){
		int len = tpo.length;	
		len = int2string(this.type, buf, len);
		System.arraycopy(nm, 0, buf, len, nm.length);
		len+=nm.length;
		len = int2string(num, buf, len);

		System.arraycopy(rad, 0, buf, len, rad.length);
		len+=rad.length;
		len = double2string(this.radius,buf,len);		

		System.arraycopy(strt, 0, buf, len, strt.length);
		len+=strt.length;
		if (start!=null){
			buf[len++]='(';
			len = double2string(this.start.x,buf,len);
			buf[len++]=',';
			len = double2string(this.start.y,buf,len);
			buf[len++]=')';
		} else {
			buf[len++] = 'n';
			buf[len++] = 'u';
			buf[len++] = 'l';
			buf[len++] = 'l';
		}

		System.arraycopy(en, 0, buf, len, en.length);
		len+=en.length;
		if (end!=null){
			buf[len++]='(';
			len = double2string(this.end.x,buf,len);
			buf[len++]=',';
			len = double2string(this.end.y,buf,len);
			buf[len++]=')';
		} else {
			buf[len++] = 'n';
			buf[len++] = 'u';
			buf[len++] = 'l';
			buf[len++] = 'l';
		}

		System.arraycopy(cnt, 0, buf, len, cnt.length);
		len+=cnt.length;		
		if (center!=null){
			buf[len++]='(';
			len = double2string(this.center.x,buf,len);
			buf[len++]=',';
			len = double2string(this.center.y,buf,len);
			buf[len++]=')';
		} else {
			buf[len++] = 'n';
			buf[len++] = 'u';
			buf[len++] = 'l';
			buf[len++] = 'l';
		}

		System.arraycopy(lngth, 0, buf, len, lngth.length);
		len+=lngth.length;
		len = double2string(this.length,buf,len);

		System.arraycopy(lwer, 0, buf, len, lwer.length);
		len+=lwer.length;
		if (lower!=null){
			buf[len++]='(';
			len = double2string(this.lower.x,buf,len);
			buf[len++]=',';
			len = double2string(this.lower.y,buf,len);
			buf[len++]=')';
		} else {
			buf[len++] = 'n';
			buf[len++] = 'u';
			buf[len++] = 'l';
			buf[len++] = 'l';
		}
		
		System.arraycopy(uper, 0, buf, len, uper.length);
		len+=uper.length;
		if (upper!=null){
			buf[len++]='(';
			len = double2string(this.upper.x,buf,len);
			buf[len++]=',';
			len = double2string(this.upper.y,buf,len);
			buf[len++]=')';
		} else {
			buf[len++] = 'n';
			buf[len++] = 'u';
			buf[len++] = 'l';
			buf[len++] = 'l';
		}

		System.arraycopy(fin, 0, buf, len, fin.length);
		len+=fin.length;				
		sb.append(buf,0,len);		
	}

	
	

	private static final int int2string(int ivalue,char[] s,int from){
		if (ivalue==0){
			s[from++]='0';
			return from;
		}
		int ndigits = 0;	               
		if (ivalue<0) {
			s[from++] ='-';
			ivalue = -ivalue;
		}
		while (ndigits<int10pow.length && ivalue>=int10pow[ndigits]) ndigits++;
		int digitno = from+ndigits-1;
		int c =ivalue%10;       
		while ( ivalue != 0){
			s[digitno--] = (char)(c+'0');              
			ivalue /= 10;
			c = ivalue%10;

		}          
		return from+ndigits;
	}

	private static final int double2string(double val,char[] s,int from){		
		if (val==Double.MAX_VALUE){
			System.arraycopy(MAX_DOUBLE_STRING, 0, s, from, MAX_DOUBLE_STRING.length);
			return from+MAX_DOUBLE_STRING.length;
		}
		long lval = Math.round(val*PRECISION);
		if (lval<0){
			s[from++] = '-';
			lval = -lval;
		}
		if (lval==0) {
			s[from++]='0';
			return from;
		}
		int ndigits = 0;
		int rt = 0;
		int dotIndex;
		if (lval<=1000000000){
			int ivalue = (int)lval;
			while (ndigits<int10pow.length && ivalue>=int10pow[ndigits] ) ndigits++;
			dotIndex = ndigits-PRECISION_DIGIT;
			if (dotIndex<=0) {
				s[from++]='0';
				s[from++]='.';
				while (dotIndex++<0) s[from++]='0';				
				int digitno = from+ndigits-1;
				rt = digitno;
				rt++;
				int c =ivalue%10;       								
				while ( c == 0 ){
					digitno--;
					rt--;
					ivalue /= 10;
					c = ivalue%10;
				}
				while ( ivalue != 0){
					s[digitno--] = (char)(c+'0');              
					ivalue /= 10;
					c = ivalue%10;
				}   
			} else {								
				int c =ivalue%10;       					
				while ( c == 0 && ndigits>dotIndex){
					ndigits--;				
					ivalue /= 10;
					c = ivalue%10;

				}				
				int digitno = from+ndigits;
				rt = digitno+1;
				if (ndigits==dotIndex) {					
					digitno--;
					rt--;
					s[digitno--] = (char)(c+'0');
					ivalue /= 10;
					c = ivalue%10;
					while ( ivalue != 0){						
						s[digitno--] = (char)(c+'0');              
						ivalue /= 10;
						c = ivalue%10;
					}   
					return rt;
				}
				while ( ivalue != 0){
					if (ndigits==dotIndex) {
						s[digitno--] = '.';	
						ndigits--;
					}
					ndigits--;
					s[digitno--] = (char)(c+'0');              
					ivalue /= 10;
					c = ivalue%10;

				}   
			}
		} else {
			while (ndigits<long10pow.length && lval>=long10pow[ndigits]) ndigits++;
			dotIndex = ndigits-PRECISION_DIGIT;
			if (dotIndex<=0) {
				s[from++]='0';
				s[from++]='.';
				while (dotIndex++<0) s[from++]='0';				
				int digitno = from+ndigits-1;
				rt = digitno;
				rt++;
				int c =(int)(lval%10L);       								
				while ( c == 0 ){
					digitno--;
					rt--;
					lval /= 10;
					c =(int)(lval%10L);
				}
				while ( lval != 0){
					s[digitno--] = (char)(c+'0');              
					lval /= 10L;
					c =(int)(lval%10L);
				}   
			} else {								
				int c =(int)(lval%10L);      					
				while ( c == 0 && ndigits>dotIndex){
					ndigits--;				
					lval /= 10L;
					c =(int)(lval%10L);

				}				
				int digitno = from+ndigits;
				rt = digitno+1;

				if (lval<=Integer.MAX_VALUE){
					int ivalue = (int)lval;
					if (ndigits==dotIndex) {					
						digitno--;
						rt--;
						s[digitno--] = (char)(c+'0');
						ivalue /= 10;
						c = ivalue%10;
						while ( ivalue != 0){						
							s[digitno--] = (char)(c+'0');              
							ivalue /= 10;
							c = ivalue%10;
						}   
						return rt;
					}
					while ( ivalue != 0){
						if (ndigits==dotIndex) {
							s[digitno--] = '.';	
							ndigits--;
						}
						ndigits--;
						s[digitno--] = (char)(c+'0');              
						ivalue /= 10;
						c = ivalue%10;

					}   

				} else {
					if (ndigits==dotIndex) {					
						digitno--;
						rt--;
						s[digitno--] = (char)(c+'0');
						lval /= 10;
						c =(int)(lval%10L);
						while ( lval != 0){						
							s[digitno--] = (char)(c+'0');              
							lval /= 10;
							c =(int)(lval%10L);
						}   
						return rt;
					}
					while ( lval != 0){
						if (ndigits==dotIndex) {
							s[digitno--] = '.';	
							ndigits--;
						}
						ndigits--;
						s[digitno--] = (char)(c+'0');              
						lval /= 10;
						c =(int)(lval%10L);

					}

				}
			}

		}
		return rt;
	}


}
