/**
 * 
 */
package solo;


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

import com.graphbuilder.curve.ControlPath;
import com.graphbuilder.geom.Geom;

/**
 * @author kokichi3000
 *
 */
public final class EdgeDetector {

	//	public static ObjectArrayList<Vector2D> newLeft = ObjectArrayList.wrap(new Vector2D[20], 0);
	//	public static ObjectArrayList<Vector2D> newRight = ObjectArrayList.wrap(new Vector2D[20], 0);
	//	private static IntArrayList lIndx = IntArrayList.wrap(new int[20], 0);	
	//	private static IntArrayList rIndx = IntArrayList.wrap(new int[20], 0);
	private static final int[] out = new int[]{0,0};
	private static final int UPPER_LIM = 16;
	private static final int LOWER_LIM = 6;	
	private static final double E = 0.1*TrackSegment.EPSILON;
	private final Vector2D tmpHighestPoint = new Vector2D();
	final static double steerLock=0.785398;
	public static final double CERTAIN_DIST = 0.05;
	private static final double SMALL_MARGIN = 0.002;
	public static final double MAX_DISTANCE = 99.95;
	private static final double DELTA = 0.001;
	private static final double PRECISION = 1000000.0d;
	private static final double PI_2 = Math.PI/2;
	final static double EPS = 0.02;
	public int whichE = 0;//highest point belong to which edge,0 = unknown
	public int whichEdgeAhead = 0;
	final static double[] SIN_LK = new double[]{0.0d,
		0.17364817766693041,0.3420201433256688,0.5d,0.6427876096865394,
		0.766044443118978,0.8660254037844386,0.9396926207859083,0.984807753012208,1.0,
		0.984807753012208,0.9396926207859083,0.8660254037844387,0.766044443118978,
		0.6427876096865395,0.5,0.3420201433256688,0.17364817766693064,
		0.0d};

	final static double[] COS_LK = new double[]{-1.0,-0.984807753012208,
		-0.9396926207859083,-0.8660254037844387,-0.766044443118978,-0.6427876096865393,
		-0.5,-0.34202014332566877,-0.17364817766693036,0.0,0.17364817766693036,
		0.34202014332566877,0.5,0.6427876096865394,0.7660444431189779,
		0.8660254037844387,0.9396926207859083,0.984807753012208,1.0};

	final static double[] ANGLE_LK = new double[]{0.0,0.17453292519943295,0.3490658503988659,0.5235987755982988,0.6981317007977318,
		0.8726646259971648,1.0471975511965976,1.2217304763960306,1.3962634015954636,1.5707963267948966,1.7453292519943295,
		1.9198621771937623,2.0943951023931953,2.2689280275926285,2.443460952792061,2.617993877991494,2.792526803190927,
		2.9670597283903604,3.141592653589793};


//	private static final double UNKNOWN_ANGLE = -2;
	private static final double DELTATIME = 0.002;
	private static final double MAXSTEERSPEED = Math.PI*2/3;
//	private static final double ANGLEACCURACY =100.0d; 
	public static final double MINDIST = 1;
	public static final int[] closePoint = new int[256];
	public static final Vector2D[] closePointV = new Vector2D[256];
	private static final int[] tmpIndx = new int[256];
	public static int numClosePoints = 0;
	static {
		for (int i = closePointV.length-1;i>=0;--i)
			closePointV[i] = new Vector2D();
	}
	

	//	private static final Comparator<Vector2D> Vector2DComparator = new Comparator<Vector2D>(){

	//		public final int compare(Vector2D a, Vector2D b) {
	//			// TODO Auto-generated method stub
	//			return (Math.abs(a.y-b.y)<=0.00001) ? 0 : (a.y>b.y) ? 1 : -1;
	//		}};


	//	private static final Swapper swapper = new Swapper(){
	//		@Override
	//		public void swap(int i, int j) {
	//			// TODO Auto-generated method stub
	//			double tmp = angles[i];
	//			angles[i] = angles[j];
	//			angles[j] = tmp;
	//			Vector2D v = backP[i];
	//			backP[i] = backP[j];
	//			backP[j] = v;
	//		}
	//	};

	//		private static class Swap implements Swapper{
	//			Object[] backP;
	//
	//			public Swap(Object[] b) {
	//				// TODO Auto-generated constructor stub
	//				backP = b;
	//			}
	//
	//			@Override
	//			public void swap(int i, int j) {
	//				// TODO Auto-generated method stub
	//				Object v = backP[i];
	//				backP[i] = backP[j];
	//				backP[j] = v;
	//
	//			}		
	//		}

	Vector2D currentPointAhead = new Vector2D();
	ControlPath cp=null;
	private static double[] tracks;

	public double trackWidth=0;
	double curPos=0;	
	double distRaced;	

	private static final int NUM_POINTS=500;
	double curAngle;
	double maxY;
	int pointAheadIndx = -1;

	public final Vector2D[] left = new Vector2D[NUM_POINTS];
	public final Vector2D[] right = new Vector2D[NUM_POINTS];
	public final static Vector2D[] nleft = new Vector2D[20];
	public final static Vector2D[] nright = new Vector2D[20];
	static {
		for (int i = nleft.length-1;i>=0;--i){
			nleft[i] = new Vector2D();
			nright[i] = new Vector2D();
		}
	}
	public int nLsz = 0;
	public int nRsz = 0;
	public int lSize = 0;
	public int rSize = 0;
	//		public final ObjectArrayList<Vector2D> allPoints = ObjectArrayList.wrap(backP,0);
	//	private static ObjectArrayList<Vector2D> allPointsR = null;
	//	private static double[] angles = new double[NUM_POINTS];
	//	DoubleArrayList x = null;
	//	DoubleArrayList y = null;
	int numpoint = 0;
	//	int firstIndexMax = -1,lastIndexMax = -1;

	double maxDistance=-1;
	public double leftStraight = -1;
	public double rightStraight = -1;
	public double straightDist =-1;
	Vector2D highestPoint = null;	
	int turn;
	Vector2D center = null;
	double radiusL = -1;
	double radiusR = -1;
	//	private static double[] tmpX = new double[NUM_POINTS];//x is axis along track axis
	//	private static double[] tmpY = new double[NUM_POINTS];
	//	private static double[] tmpRx = new double[NUM_POINTS];//x is axis along track axis
	//	private static double[] tmpRy = new double[NUM_POINTS];

	/**
	 * Copy Constructor
	 *
	 * @param edgeDetector a <code>EdgeDetector</code> object
	 */
	
	public final int removeElems(Vector2D[] sortedArr,int indx,int num,int[] selected){
		int k = 0;
		Vector2D v = sortedArr[indx];
		Vector2D vvv = v;
		int j = 0;
		for (k = indx-1;k>=0;--k){
			Vector2D vv = sortedArr[k];
			if (v.y-vv.y>trackWidth && vvv.y-vv.y>trackWidth) break;
			if (vv.distance(vvv)<trackWidth) {
				vvv = vv;
				selected[j++] = k;
			}
		}		
		if (j>0){
			System.arraycopy(selected, 0, selected, j, j);
			k = j;
			for (int i=j-1;i>=0;--i){
				selected[i] = selected[k++]; 
			}
		}
		selected[j++] = indx;
		
		vvv = v;
		for (k = indx+1;k<num;++k){
			Vector2D vv = sortedArr[k];
			if (vv.y-v.y>trackWidth && vv.y-vvv.y>trackWidth) break;
			if (vv.distance(vvv)<trackWidth) {
				vvv = vv;
				selected[j++] = k;
			}
		}
		
//		k++;
		
//		for (;k<num;++k){
//			Vector2D vv = sortedArr[k];
//			if (vv.distance(v)<trackWidth || k<num-1 && vv.distance(sortedArr[k+1])<trackWidth) selected[j++] = k;			
//		}
		return j;
	}
	
	public final int removeFromRightEdge(int i,Segment[] trArr,int trSz,int[] trIndx,int[] occupied){
		int n = removeElems(right, i, rSize, tmpIndx);
		if (n==0 || rSize>5 && n>rSize-5 ) {
			i = n>0 && rSize>5 && n>rSize-5 ? tmpIndx[0] : i;
			return i;
		}
		int count = 0;
		for (int j = 0;j<n;++j){
			if (right[ tmpIndx[j]].certain ) count++;
			if (count>1) break;
		}
		if (count>1) 
			return tmpIndx[0];
			
		int firstIndx = tmpIndx[0];
		for (int j = 0;j<trSz;++j){
			int indx = trIndx[j];
			Segment t = trArr[ indx ].rightSeg;
			if (t.startIndex>=firstIndx || t.endIndex>=firstIndx){
				int start = t.startIndex;
				int end = t.endIndex;
				for (int k = 0;k<n;++k){
					int idx = tmpIndx[k];
					if (idx<start) t.startIndex--;
					if (idx<=end) t.endIndex--;
					if (idx>end) break;
				}
				t.num = t.endIndex - t.startIndex+1;
				if (t.num<3 && t.opp.num<3){
					occupied[indx] = 0;
					if (trSz>j+1) System.arraycopy(trIndx, j+1, trIndx, j, trSz-j-1);
					trSz--;
					j--;
				}
			}
		}
		
		int lastIndx = tmpIndx[n-1];
		int indx = lastIndx;
		for (int j = n-1;j>=0;--j){
			int idx = tmpIndx[j];
			if (idx<i) i--;
			lSize = EdgeDetector.join(right[idx], left, lSize, trArr, trIndx, trSz, -1);
			if (indx-- == idx)
				continue;
			indx = tmpIndx[j+1];
			if (rSize>lastIndx+1) {
				for (int ii = 0,nn = rSize-lastIndx-1;ii<nn;++ii){
					right[indx+ii].copy(right[lastIndx+1+ii]);
				}
//				System.arraycopy(right, lastIndx+1, right, indx, rSize-lastIndx-1);
			}
			rSize -= lastIndx-indx+1;
			lastIndx = idx;							
			indx = lastIndx;														
		}
		indx = tmpIndx[0];
		if (rSize>lastIndx+1) {
			for (int ii = 0,nn = rSize-lastIndx-1;ii<nn;++ii){
				right[indx+ii].copy(right[lastIndx+1+ii]);
			}
//			System.arraycopy(right, lastIndx+1, right, indx, rSize-lastIndx-1);
		}
		rSize -= lastIndx-indx+1;
		CircleDriver2.trSz = trSz;
		return tmpIndx[0];
	}
	
	public final int removeFromLeftEdge(int i,Segment[] trArr,int trSz,int[] trIndx,int[] occupied){
		int n = removeElems(left, i, lSize, tmpIndx);
		if (n==0 || lSize>5 && n>lSize-5) {
			i = n>0 && n>lSize-5 ? tmpIndx[0] : i;
			return i;
		}
		int count = 0;
		for (int j = 0;j<n;++j){
			if (left[ tmpIndx[j]].certain ) count++;
			if (count>1) break;
		}
		if (count>1)
			return tmpIndx[0];
			
		int firstIndx = tmpIndx[0];
		for (int j = 0;j<trSz;++j){
			int indx = trIndx[j];
			Segment t = trArr[ indx ].leftSeg;
			if (t.startIndex>=firstIndx || t.endIndex>=firstIndx){
				int start = t.startIndex;
				int end = t.endIndex;
				for (int k = 0;k<n;++k){
					int idx = tmpIndx[k];
					if (idx<start) t.startIndex--;
					if (idx<=end) t.endIndex--;
					if (idx>end) break;
				}
				t.num = t.endIndex - t.startIndex+1;
				if (t.num<3 && t.opp.num<3){
					occupied[indx] = 0;
					if (trSz>j+1) System.arraycopy(trIndx, j+1, trIndx, j, trSz-j-1);
					trSz--;
					j--;
				}
			}
		}
		
		int lastIndx = tmpIndx[n-1];
		int indx = lastIndx;
		for (int j = n-1;j>=0;--j){
			int idx = tmpIndx[j];
			if (idx<i) i--;
			rSize = EdgeDetector.join(left[idx], right, rSize, trArr, trIndx, trSz, 1);
			if (indx-- == idx)
				continue;
			indx = tmpIndx[j+1];
			if (lSize>lastIndx+1) {
				for (int ii = 0,nn = lSize-lastIndx-1;ii<nn;++ii){
					left[indx+ii].copy(left[lastIndx+1+ii]);
				}
//				System.arraycopy(left, lastIndx+1, left, indx, lSize-lastIndx-1);
			}
			lSize -= lastIndx-indx+1;
			lastIndx = idx;							
			indx = lastIndx;														
		}
		indx = tmpIndx[0];
		if (lSize>lastIndx+1) {
			for (int ii = 0,nn = lSize-lastIndx-1;ii<nn;++ii){
				left[indx+ii].copy(left[lastIndx+1+ii]);
			}
//			System.arraycopy(left, lastIndx+1, left, indx, lSize-lastIndx-1);
		}
		lSize -= lastIndx-indx+1;
		CircleDriver2.trSz = trSz;
		return tmpIndx[0];
	}
		
	public final void copy(EdgeDetector edgeDetector) 
	{
		this.cp = edgeDetector.cp;
		this.trackWidth = edgeDetector.trackWidth;
		this.curPos = edgeDetector.curPos;
		this.distRaced = edgeDetector.distRaced;
		this.curAngle = edgeDetector.curAngle;
		this.maxY = edgeDetector.maxY;
		this.pointAheadIndx = edgeDetector.pointAheadIndx;
		if (edgeDetector.center==null) 
			this.center = null;
		else if (this.center==null) 
				this.center = new Vector2D(edgeDetector.center);
			else {
				this.center.x = edgeDetector.center.x;
				this.center.y = edgeDetector.center.y;
			}		
		this.radiusL = edgeDetector.radiusL;
		this.radiusR = edgeDetector.radiusR;		
		//		this.left = edgeDetector.left;
		//		this.right = edgeDetector.right;
		//		this.firstIndexMax = edgeDetector.firstIndexMax;
		//		this.lastIndexMax = edgeDetector.lastIndexMax;
		this.maxDistance = edgeDetector.maxDistance;
		this.leftStraight = edgeDetector.leftStraight;
		this.rightStraight = edgeDetector.rightStraight;
		this.straightDist = edgeDetector.straightDist;
//		this.highestPoint = edgeDetector.highestPoint;
		if (edgeDetector.highestPoint==null){
			this.highestPoint = null;
		} else {
			this.highestPoint = tmpHighestPoint;
			this.highestPoint.copy(edgeDetector.highestPoint);
		}
		//		this.x = new DoubleArrayList(edgeDetector.x);
		//		this.y = new DoubleArrayList(edgeDetector.y);
		this.numpoint = edgeDetector.numpoint;
		this.turn = edgeDetector.turn;		
		this.currentPointAhead = edgeDetector.currentPointAhead;		
		this.whichE = edgeDetector.whichE;			
		int sL = lSize = edgeDetector.lSize;
		int sR = rSize = edgeDetector.rSize;
		if (sL>0) 
			for (int i = sL-1;i>=0;--i){
				Vector2D v = edgeDetector.left[i];
				left[i].x = v.x;
				left[i].y = v.y;
			}
		if (sR>0) 
			for (int i = sR-1;i>=0;--i){
				Vector2D v = edgeDetector.right[i];
				right[i].x = v.x;
				right[i].y = v.y;
			}
		//			int sA = allPoints.size();
		//			edgeDetector.allPoints.getElements(0, this.allPoints.elements(), 0, sA);			
	}


	public EdgeDetector() {
		for (int i = left.length-1;i>=0;--i){
			left[i] = new Vector2D();
			right[i] = new Vector2D();
		}
	}

	private static final int compare(Vector2D a,Vector2D b){
		double d = a.y-b.y;
		return (-SMALL_MARGIN<=d && d<=SMALL_MARGIN) ? 0 : (d<0) ? -1 : 1;
	}

	private static int partition(Vector2D[] a, int left, int right) {
		int i = left - 1;
		int j = right;
		while (true) {
			while (compare(a[++i],a[right])<0)      // find item on left to swap
				;                               // a[right] acts as sentinel
			while (compare(a[right],a[--j])<0)      // find item on right to swap
				if (j == left) break;           // don't go out-of-bounds
			if (i >= j) break;                  // check if pointers cross            
			Vector2D tmp = a[i];
			a[i] = a[j];
			a[j] =tmp;
		}
		Vector2D tmp = a[i];
		a[i] = a[right];
		a[right] =tmp;
		return i;
	}

	public static void quicksort(Vector2D[] a, int left, int right) {
		if (right <= left) return;
		int i = partition(a, left, right);
		quicksort(a, left, i-1);
		quicksort(a, i+1, right);
	}

	public final void init(CarState cs,double trkWidth) {
		// TODO Auto-generated constructor stub
		long ti = System.currentTimeMillis();
		tracks = cs.track;
		Vector2D[] left = this.left;
		Vector2D[] right = this.right;
		whichE = 0;		
		curPos = -Math.round(cs.trackPos*PRECISION)/PRECISION;
		//		curPos = -cs.getTrackPos();
		curAngle = Math.round(cs.angle*PRECISION)/PRECISION;
		maxDistance=-1;
		int firstIndexMax = -1;
		int lastIndexMax = -1;		
		maxY = -1000;
		distRaced = Math.round(cs.distRaced*PRECISION)/PRECISION;		
		int startIndex = 0;
		int endIndex = 19;		
		if (Math.abs(curAngle)<0.01){
			trackWidth = Math.round((tracks[0]+tracks[18])*Math.cos(curAngle));
			if (trackWidth>0 && trkWidth>0 && trackWidth!=trkWidth) {
				trackWidth = trkWidth;
				startIndex = 1;
			}
		}
		if (trackWidth<=0)
			trackWidth = trkWidth;

		leftStraight=-1;
		rightStraight=-1;			
		int j = 0;			

		double  angle = Math.PI-ANGLE_LK[9]-curAngle;
		//			double xx = tracks[i]* Math.cos(angle);
		//			double yy = tracks[i]* Math.sin(angle);			
		double yy =Math.round(tracks[9]* Math.sin(angle)*PRECISION)/PRECISION;
		int sign = (yy<0) ? -1 : 1;
//		sign *= (curAngle<-PI_2) ? -1 : 1;
		if (sign<0){			
			angle = Math.PI-ANGLE_LK[startIndex]-curAngle;					
			yy =Math.round(tracks[startIndex]* Math.sin(angle)*PRECISION)/PRECISION;
			angle = Math.PI-ANGLE_LK[1+startIndex]-curAngle;					
			double yy1 =Math.round(tracks[1+startIndex]* Math.sin(angle)*PRECISION)/PRECISION;
			if (yy<yy1){ 
				angle = Math.PI-ANGLE_LK[2+startIndex]-curAngle;					
				double yy2 =Math.round(tracks[2+startIndex]* Math.sin(angle)*PRECISION)/PRECISION;
				if (yy2<yy1) sign = 1;
			} 
//			else {
//				angle = Math.PI-ANGLE_LK[2]-curAngle;					
//				double yy2 =Math.round(tracks[2]* Math.sin(angle)*PRECISION)/PRECISION;
//				if (yy2>yy1) sign = 1;
//			}
		}
		Vector2D[] edge = (sign<0) ? right : left;
//		int startIndex = (curAngle>PI_2) ? 1 : 0;
//		int endIndex = (curAngle<-PI_2) ? 18 : 19;
		whichEdgeAhead = 0;
		pointAheadIndx = -1;
		currentPointAhead.x = 0;
		currentPointAhead.y = 0;
		firstIndexMax = -1;
		double ll = 0;

		for (int i=startIndex;i<endIndex;++i){			
			angle = Math.PI-ANGLE_LK[i]-curAngle;
			//			double xx = tracks[i]* Math.cos(angle);
			//			double yy = tracks[i]* Math.sin(angle);
			double l = tracks[i];
			if (l<=0) continue;
			double xx =Math.round(l* Math.cos(angle)*PRECISION)/PRECISION;
			yy =Math.round(l* Math.sin(angle)*PRECISION)/PRECISION;
			if (i==9)
				currentPointAhead.copy(xx,yy);			
			
			if ((i==0 || i==endIndex-1 || Math.abs(yy)<trackWidth) && (xx<-2*trackWidth || xx>2*trackWidth)) continue;
			//			angle=Math.round((Math.PI-angle)*ANGLEACCURACY)/ANGLEACCURACY;
			//			angle = Math.PI - angle;			
			//			angle = (xx==0) ? PI_2 : (yy>=0) ? Math.PI-Math.atan2(yy,xx) : Math.PI - angle;
			//			angle = Math.round(angle*PRECISION)/PRECISION;			

			if (maxDistance<l)
				maxDistance = l;
			
			double absXX = Math.abs(xx);
			if (absXX>=40 || (absXX>trackWidth*2 && absXX>Math.abs(yy))){
				System.out.println("Vo no roi");
				continue;
			}

			if (yy<-5 || (j>0 && Math.abs(yy-edge[j-1].y)<=0.15)) continue;
			double absYY = yy*sign;
			
			if (maxY<absYY || l>MAX_DISTANCE){
				if (maxY<absYY)
					maxY = absYY;
				if (l<=MAX_DISTANCE){
					firstIndexMax = j;
					lastIndexMax = firstIndexMax;		
					ll = l;
				} else {
					if (ll<=MAX_DISTANCE){
						firstIndexMax = j;
						ll = l;
					} else if (ll<l) ll = l;
					lastIndexMax = j;
				}
			} else if (maxY==absYY && lastIndexMax>=0){
				lastIndexMax =j;
				if (ll<l) ll = l;
			} else if (l>MAX_DISTANCE){				
				if (maxY<absYY && firstIndexMax>=0 && ll<=MAX_DISTANCE){
					maxY = absYY;
					firstIndexMax =j;
					lastIndexMax =j;
					ll = l;
				} else if (maxY==absYY && lastIndexMax>=0){
					lastIndexMax = j;
					if (ll<l) ll = l;
				} else {
					continue;
				}
			}			

			if (yy<-5) continue;
//			Vector2D v = edge[j];			

			if (j<=0 || Math.abs(yy-edge[j-1].y)>0.15){
				edge[j++].copy(xx, yy,true);
//				j++;
//				v.x = xx;
//				v.y = yy;
//				v.certain = true;	
				if (i==9) pointAheadIndx = j-1;
			}			
						
		}

		Vector2D[] other = (sign<0) ? left : right;
		for (int i=j-1;i>=0;--i){
//			Vector2D v = edge[j-1-i];
//			Vector2D s = other[i];
//			s.x = v.x;
//			s.y = v.y;
			other[i].copy(edge[j-1-i]);
		}
		numpoint = j;
		int lsz = (firstIndexMax>=0 && firstIndexMax<numpoint) ? firstIndexMax  : 0;
		int rsz = (lastIndexMax<numpoint-1 && lastIndexMax>=0) ? numpoint-1-lastIndexMax : 0;
		if (numpoint==0){
			if (highestPoint!=null){
				highestPoint.x = 0;
				highestPoint.y = 0;
			}
			lSize = 0;
			rSize = 0;
			nLsz = 0;
			nRsz = 0;
			whichE = 0;
			whichEdgeAhead = 0;
			currentPointAhead.x = 0;
			currentPointAhead.y = 0;
			return;
		}
		if (sign<0){			
			yy = edge[0].y;			
			int oldIndx = firstIndexMax;
			firstIndexMax = 0;
			Vector2D p;
			for (int i=1;i<numpoint;++i){
				p = edge[i];
				if (p.y>yy){
					yy = p.y;
					firstIndexMax = i;
				}
			}		
			if (firstIndexMax>=0 && firstIndexMax<oldIndx){
				sign = 1;			
				lastIndexMax = firstIndexMax;
				lsz = (firstIndexMax>=0 && firstIndexMax<numpoint) ? firstIndexMax  : 0;
				rsz = (lastIndexMax<oldIndx-1 && lastIndexMax>=0) ? oldIndx-lastIndexMax-1 : 0;				
				int oldLsz = numpoint-1-oldIndx;
//				Vector2D[] tmp = new Vector2D[lsz+1 + oldLsz];				
//				if (oldLsz>0) System.arraycopy(edge, oldIndx+1, tmp, 0, oldLsz);
//				System.arraycopy(edge, 0, tmp, oldLsz, lsz+1);
				//Use nleft as temporary buffer
				for (int i = 0;i<oldLsz;++i)
					nleft[i].copy(edge[oldIndx+1+i]);
				for (int i = 0;i<lsz+1;++i)
					nleft[i+oldLsz].copy(edge[i]);
				lsz+=oldLsz;
				
				if (rsz>0) {
					for (int i = rsz-1;i>=0;--i){
						right[i].copy(other[oldLsz+1+i]);
//						Vector2D s = other[oldLsz+1+i];
//						Vector2D d = right[i];
//						d.x = s.x;
//						d.y = s.y;
					}
//					System.arraycopy(other, oldLsz+1, right, 0, rsz);			
				}				
//				System.arraycopy(tmp, 0, left, 0, lsz+1);
				
				for (int i = lsz;i>=0;--i){
					left[i].copy(nleft[i]);
//					Vector2D s = tmp[i];
//					Vector2D d = left[i];
//					d.x = s.x;
//					d.y = s.y;
				}
				
				
				firstIndexMax = lsz;
			} else if (firstIndexMax>oldIndx){
				sign = 1;
				int oldRsz = oldIndx;
				lsz = firstIndexMax - oldIndx-1;
				rsz = (firstIndexMax<numpoint-1 && firstIndexMax>=0) ? numpoint-1-firstIndexMax : 0;
//				Vector2D[] tmp = (rsz+oldRsz>0) ? new Vector2D[Math.max(rsz+oldRsz,lsz)] : null;
//				if (tmp!=null) for (int i = tmp.length-1;i>=0;--i) tmp[i] = new Vector2D();
				if (oldRsz>0) {	
					/*if (tmp==null || numpoint-oldRsz>tmp.length){
						tmp = new Vector2D[numpoint-oldRsz];
						for (int i = tmp.length-1;i>=0;--i) tmp[i] = new Vector2D();
					}//*/
					int startIndx = numpoint-1;
					for (int i = oldRsz-1;i>=0;--i){
						nleft[i].copy(other[startIndx--]);
//						Vector2D s = other[startIndx--];
//						Vector2D d = tmp[i];
//						d.x = s.x;
//						d.y = s.y;
					}
//					System.arraycopy(other, numpoint-oldRsz, tmp, 0, oldRsz);
				}
				if (rsz>0) {
					int startIndx = oldRsz+rsz-1;
					for (int i = rsz-1;i>=0;--i){
						nleft[startIndx--].copy(other[i]);
//						Vector2D s = other[i];
//						Vector2D d = tmp[startIndx--];						
//						d.x = s.x;
//						d.y = s.y;
					}
					//System.arraycopy(other, 0, tmp, oldRsz, rsz);
				}
				rsz += oldRsz;				
				for (int i = rsz-1;i>=0;--i){
					right[i].copy(nleft[i]);
//						Vector2D s = tmp[i];
//						Vector2D d = right[i];
//						d.x = s.x;
//						d.y = s.y;
				}
//					System.arraycopy(tmp, 0, right, 0, rsz);
				
				
				if (lsz>0) {					
					int startIndx = firstIndexMax-1;
					for (int i = 0;i<lsz;++i){
						nleft[i].copy(other[startIndx--]);
//						Vector2D s = other[startIndx--];
//						Vector2D d = tmp[i];
//						d.x = s.x;
//						d.y = s.y;
					}
					
					for (int i = 0;i<lsz;++i){
//						Vector2D s = tmp[i];
//						Vector2D d = left[i];
//						d.x = s.x;
//						d.y = s.y;
						left[i].copy(nleft[i]);
					}
//					System.arraycopy(edge, oldIndx+1, left, 0, lsz+1);
				}
				
				firstIndexMax = lsz;
			}
		}
		if (firstIndexMax>=0 && numpoint>0 && sign<0){
			highestPoint = (firstIndexMax>=0 && firstIndexMax<numpoint && edge[firstIndexMax].y>=0) ? new Vector2D(edge[firstIndexMax]) : null;
			whichE = 0;
			firstIndexMax = j-1-firstIndexMax;
			lastIndexMax = j-1-lastIndexMax;
			lsz = (firstIndexMax>0 && firstIndexMax<numpoint) ? firstIndexMax  : 0;
			rsz = (lastIndexMax<numpoint-1 && lastIndexMax>=0) ? numpoint-1-lastIndexMax : 0;			
			if (lsz>0) quicksort(left, 0, lsz-1);
			if (rsz>0) quicksort(right, 0, rsz-1);			
//			int hIndx = (startIndex==1) ? 0 : 18;
//			angle = Math.PI-ANGLE_LK[hIndx]-curAngle;
			//			double xx = tracks[i]* Math.cos(angle);
			//			double yy = tracks[i]* Math.sin(angle);
//			double xx =Math.round(tracks[hIndx]* Math.cos(angle)*PRECISION)/PRECISION;
//			yy =Math.round(tracks[hIndx]* Math.sin(angle)*PRECISION)/PRECISION;
//			highestPoint = (yy>0) ? new Vector2D(xx,yy) : null;			
		} else {			
			if (pointAheadIndx>=0){
				if (lsz>pointAheadIndx){
					whichEdgeAhead = -1;
				} else if (firstIndexMax<pointAheadIndx) whichEdgeAhead = 1;
			}
			highestPoint = (firstIndexMax>=0 && firstIndexMax<numpoint && left[firstIndexMax].y>=0) ? new Vector2D(left[firstIndexMax]) : null;	
			if (highestPoint!=null && CircleDriver2.debug) System.out.println("New Highest point : "+highestPoint+"   "+highestPoint.length());
		}
				
		double dx =(highestPoint==null) ? 0: highestPoint.x;
		double dy = (highestPoint==null) ? 0 :highestPoint.y;
		double d = (highestPoint==null) ? 100 : Math.sqrt(dx*dx+dy*dy);		

		double ldv = 0;
		if (firstIndexMax>0 && left!=null && lsz>0 && d<MAX_DISTANCE){
			Vector2D v = (sign>0) ? left[lsz-1] : left[0];
			double dvx = dx - v.x;
			double dvy = dy - v.y;
			ldv = Math.sqrt(dvx*dvx+dvy*dvy);
			ldv = Math.round(ldv*1000.0d)/1000.0d;
			double absDvx = Math.abs(dvx);
			double absDvy = Math.abs(dvy);
			if (absDvx>trackWidth && absDvx<=UPPER_LIM && absDvx>=absDvy && absDvy<=LOWER_LIM ) whichE = 1;
		}
		double rdv = 0;
		if (whichE==0 && right!=null && rsz>0 && d<MAX_DISTANCE){
			Vector2D v = (sign>0) ? right[rsz-1] : right[0];
			double dvx = dx - v.x;
			double dvy = dy - v.y;
			rdv = Math.sqrt(dvx*dvx+dvy*dvy);
			rdv = Math.round(rdv*1000.0d)/1000.0d;
			double absDvx = Math.abs(dvx);
			double absDvy = Math.abs(dvy);
			if (absDvx>trackWidth && absDvx<=UPPER_LIM && absDvx>=absDvy && absDvy<=LOWER_LIM ) whichE = -1;
		}
		
		if (whichE==0 && d<MAX_DISTANCE){
			if (lsz>0 && rsz>0){				
				if (ldv<trackWidth-EPS && ldv<rdv) 
					whichE = -1;
				else if (rdv<trackWidth-EPS && rdv<ldv)
					whichE = 1;
				else if (ldv<trackWidth-EPS)
					whichE = -1;
				else if (rdv<trackWidth-EPS)
					whichE = 1;
			} else if (lsz>0 && ldv<trackWidth-EPS){
				whichE = -1;
			} else if (rsz>0 && rdv<trackWidth-EPS){
				whichE = 1;
			}
		}
		
		
		if (whichE==1){			
			if (sign>0) {
				Vector2D dest = right[rsz++];
				dest.x = dx;
				dest.y = dy;
			} else {
//				System.arraycopy(right, 0, right, 1, rsz++);
				for (int i = rsz++;i>=0;--i){
					right[i+1].copy(right[i]);
//					Vector2D s = right[i];
//					Vector2D dest = right[i+1];
//					dest.x = s.x;
//					dest.y = s.y;
				}				
				Vector2D dest = right[0];
				dest.x = dx;
				dest.y = dy;
			}
		} else if (whichE==-1){			
			if (sign>0) {
				Vector2D dest = left[lsz++];
				dest.x = dx;
				dest.y = dy;
			}else {
//				System.arraycopy(left, 0, left, 1, lsz++);
				for (int i = lsz++;i>=0;--i){
					left[i+1].copy(left[i]);
//					Vector2D s = left[i];
//					Vector2D dest = left[i+1];
//					dest.x = s.x;
//					dest.y = s.y;
				}
				Vector2D dest = left[0];
				dest.x = dx;
				dest.y = dy;
			}
		}
		
		if (whichEdgeAhead==0 && pointAheadIndx>=0){
			if (lsz>0 && binarySearchFromTo(left, currentPointAhead, 0, lsz-1)>=0) 
				whichEdgeAhead = -1;
			else if (rsz>0 && binarySearchFromTo(right, currentPointAhead, 0, rsz-1)>=0)
				whichEdgeAhead = 1;
		}
				
		
		lSize = lsz;
		rSize = rsz;
		nLsz = lsz;
		nRsz = rsz;
		if (lsz>0) {
			for (int i = lsz-1;i>=0;--i){
//				Vector2D s = left[i];
//				Vector2D dest = nleft[i];
//				dest.x = s.x;
//				dest.y = s.y;
				nleft[i].copy(left[i]);
			}
//			System.arraycopy(left, 0, nleft, 0, lsz);
		}
		if (rsz>0) {
			for (int i = rsz-1;i>=0;--i){
				nright[i].copy(right[i]);
//				Vector2D s = right[i];
//				Vector2D dest = nright[i];
//				dest.x = s.x;
//				dest.y = s.y;
			}
//			System.arraycopy(right, 0, nright, 0, rsz);
		}
		//			newLeft.size(lsz);
		//			if (left!=null && lsz>0) System.arraycopy(left.elements(), 0, newLeft.elements(), 0, lsz);
		//			newRight.size(rsz);
		//			if (right!=null && rsz>0) System.arraycopy(right.elements(), 0, newRight.elements(), 0, rsz);
		//			lIndx.size(lsz);
		//			int[] ar = lIndx.elements();
		//			for (int i=0;i<lsz;++i)ar[i] = i;
		//			rIndx.size(rsz);
		//			ar = rIndx.elements();
		//			for (int i=0;i<rsz;++i) ar[i] = i;

		double l0 = (left!=null && lsz>0) ? left[0].x : -10000;
		double l1 = (left!=null && lsz>0) ? left[lsz-1].x : -10000;
		double r0 = (right!=null && rsz>0) ? right[0].x : -10000;
		double r1 = (right!=null && rsz>0) ? right[rsz-1].x : -10000;
		boolean straight = (highestPoint!=null && d>=MAX_DISTANCE);
		boolean nlS = (l1>l0 ? l1 : l0)>TrackSegment.EPSILON;
		boolean nrS = (r1>r0?r1:r0)>TrackSegment.EPSILON;
		if (straight && ((l1>=l0 ? l1-l0 : l0-l1)<=0.1*TrackSegment.EPSILON || (r1>=r0 ? r1-r0 : r0-r1)<=TrackSegment.EPSILON*0.1)) 
			turn = MyDriver.STRAIGHT;
		else if ((nlS && l1>l0+TrackSegment.EPSILON) || (nrS && r1>r0+TrackSegment.EPSILON))
			turn = 1;
		else if ((nlS && l1+TrackSegment.EPSILON<l0) || (nrS && r1+TrackSegment.EPSILON<r0))
			turn = -1;
		else turn = 2;

		double dL = getStraightDist(left,lsz);
		double dR = getStraightDist(right,rsz);
		if (CircleDriver2.debug) System.out.println("EdgeInit : "+(System.currentTimeMillis()-ti));
		straightDist = (dL< dR) ? dR : dL;

	}	

	private static final double getStraightDist(Vector2D[] left,int sL){
		double val = 0;
		if (left!=null && sL>0){
			double l0 = -10000;
			boolean ok = false;
			int i = 0;
			int j = 0;
			for (i=0;i<sL;++i){
				Vector2D v = left[i];
				if (!ok && v.y>=0) {
					l0 = v.x;
					j = i;
					ok = true;
					continue;
				} 
				if (ok && Math.abs(v.x-l0)>E)				
					break;				
			}
			if (ok){
				if (i==sL || i>j+1)
					val = left[i-1].y;				
			}
		}
		return val;
	}

	//		private static final int compare(Segment s,Vector2D key){
	//			if (key.y<=s.end.y && key.y>=s.start.y) return 0;
	//			if (key.y>s.end.y) return -1;
	//			return 1;
	//		}

	/*private static final int binarySearchFromTo(Segment[] list, double key, int from, int to) {
		if (from>to) return -from-1;
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

	
	private static final int binarySearchFromTo(Vector2D[] list, Vector2D key, int from, int to) {
		if (from>to) return -from-1;
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


	//		@SuppressWarnings("unchecked")
	//		private static final int binarySearchFromTo(Object[] list, Object key, int from, int to, java.util.Comparator comparator) {
	//			Object midVal;
	//			int cmp = comparator.compare(list[from], key); 
	//			if (cmp==0) 
	//				return from;
	//			else if (cmp>0) return -from-1;
	//			cmp = comparator.compare(list[to], key);
	//			if (cmp==0) 
	//				return to;
	//			else if (cmp<0) return -(to+2);
	//			while (from <= to) {
	//				int mid =(from + to)/2;
	//				midVal = list[mid];
	//				cmp = comparator.compare(midVal,key);
	//
	//				if (cmp < 0) from = mid + 1;
	//				else if (cmp > 0) to = mid - 1;
	//				else return mid; // key found
	//			}
	//			return -(from + 1);  // key not found.
	//		}




	/*private static final void shift(int offset,IntArrayList s,IntArrayList e){
		int[] sr = s.elements();
		int[] se = e.elements();
		for (int i = 0;i<s.size();++i) {				
			sr[i]+=offset;
			se[i]+=offset;
			if (sr[i]<0) sr[i] = 0;
			if (se[i]<0) se[i] = -1;
		}						
	}//*/

	//		private static final void shift(int offset,IntArrayList s,IntArrayList e,int from){
	//			int[] sr = s.elements();
	//			int[] se = e.elements();
	//			for (int i = from;i<s.size();++i) {
	//				if (sr[i]>se[i] || se[i]<0) {
	//					if (i>0) se[i] = se[i-1];
	//					sr[i] = se[i]+1;
	//					continue;
	//				}
	//				sr[i]+=offset;
	//				se[i]+=offset;
	//			}						
	//		}

	/*private static final void addPoint(int segmentIndex,ObjectArrayList<Segment> ss){
		Segment[] ssArr = ss.elements();
		Segment s = ssArr[segmentIndex];
		s.endIndex++;		
		int sz = ss.size();
		for (int i = segmentIndex+1;i<sz;++i){
			s = ssArr[i];
			Segment prev = ssArr[i-1];
			if (s.startIndex>s.endIndex || s.endIndex<0){
				s.startIndex = prev.endIndex+1;
				s.endIndex = s.startIndex-1;
				continue;
			}
			if (s.startIndex<0)	s.startIndex = 0;				
			if (s.startIndex>=0 && s.startIndex<=prev.endIndex){
				do {					
					s.startIndex++;
					s.endIndex++;
				} while (s.startIndex<=prev.endIndex);
			} else if (s.startIndex<=s.endIndex && i>0 && s.startIndex>prev.endIndex+1) 
				break;
		}		
	}//*/				

	private static final boolean insertPoint(Vector2D[] elems,int sz,Vector2D v,int from,int to,Segment s){		
		int indx = (to<=from) ? -from-1: binarySearchFromTo(elems, v, from, to-1);
		if (indx>=0){ 
			Vector2D old = elems[indx];
			if (old.x==s.start.x && old.y==s.start.y) {
				s.start.copy(v);
			}
			if (old.x==s.end.x && old.y==s.end.y) {
				s.end.copy(v); 
			}
			old.copy(v);
//			elems[indx].certain = true;
			return false;
		}

		if (indx<0) indx = -indx-1;
		boolean ok1 = false;
		boolean ok2 = false;
		Vector2D p1 = null;
		Vector2D p2 = null;
		double d1 = 0;
		double d2 = 0;
		boolean isStart = false;
		boolean isEnd = false;
		if (indx>from){				
			p1 = elems[indx-1];
			double dx = p1.x-v.x;
			double dy = p1.y - v.y;
			d1 = Math.sqrt(dx*dx+dy*dy);
			if (d1<MINDIST && !p1.certain){
				isStart = (p1.x==s.start.x && p1.y==s.start.y);
				ok1 = true;					
			}
		}

		if (indx<to){				
			p2 = elems[indx];
			double dx = p2.x-v.x;
			double dy = p2.y - v.y;
			d2 = Math.sqrt(dx*dx+dy*dy);
			if (d2<MINDIST && !p2.certain) {	
				isStart = !isStart && (p2.x==s.start.x && p2.y==s.start.y);
				isEnd = p2.x==s.end.x && p2.y==s.end.y;
				ok2 = true;					
			}
		}
		if (ok1 && ok2){
			if (d1<=d2){ 
				p1.copy(v);
				if (isStart) {
//					s.start.x = v.x;
//					s.start.y = v.y;
					s.start.copy(v);
				}
			} else {
//				p2.x = v.x;
//				p2.y = v.y;
				p2.copy(v);
				if (isStart) {
//					s.start.x = v.x;
//					s.start.y = v.y;
					s.start.copy(v);
				}
				if (isEnd) {
//					s.end.x = v.x;
//					s.end.y = v.y;
					s.end.copy(v);
				}
			}
			return false;
		} else if (ok1){ 
//			p1.x = v.x;
//			p1.y = v.y;
			p1.copy(v);
			if (isStart) {
//				s.start.x = v.x;
//				s.start.y = v.y;
				s.start.copy(v);
			}	
			return false;
		} else if (ok2) {
//			p2.x = v.x;
//			p2.y = v.y;
			p2.copy(v);
			if (isStart) {
//				s.start.x = v.x;
//				s.start.y = v.y;
				s.start.copy(v);
			}
			if (isEnd) {
//				s.end.x = v.x;
//				s.end.y = v.y;
				s.end.copy(v);
			}
			return false;
		}
		if ((sz-indx)>0) {
			for (int ii = sz-indx-1;ii>=0;--ii){
				elems[indx+1+ii].copy(elems[indx+ii]);
//				Vector2D src = elems[indx+ii];
//				Vector2D dest = elems[indx+1+ii];
//				dest.x = src.x;
//				dest.y = src.y;
			}
//			System.arraycopy(elems, indx, elems, indx+1, sz);
		}
		sz++;
		p2 = elems[indx];
//		p2.x = v.x;
//		p2.y = v.y;
		p2.copy(v);
		return true;	
	}


	public static final int join(Vector2D v,Vector2D[] elems,int sz,Segment[] trArr,int[] trIndx,int trSz,int which){			
		//			boolean ok1 =false;
		//			boolean ok2 = false;
		//			double d1 = 0;
		//			double d2 = 0;
//		Vector2D p = null;
		//	boolean changed = false;		
		int index = 0;
		double vy = v.y;
//		int lastIndex = (trSz==0) ? 0 : (which==-1) ? trArr[trSz-1].leftSeg.endIndex : trArr[trSz-1].rightSeg.endIndex;
		Segment s = null;
		Vector2D p = null;
		Vector2D p1 = null;
		Vector2D p2 = null;
		for (int i = 0;i<trSz;++i){
			s = (which==-1) ? trArr[ trIndx[i] ].leftSeg : trArr[ trIndx[i] ].rightSeg;
			if (s==null) continue;
			if (s.start.y-SMALL_MARGIN<=vy && vy<=s.end.y+SMALL_MARGIN){
				index = i;
				break;
			}
			if (s.start.y>vy){
				index = -i-1;
				break;
			}
			if (i==trSz-1 && s.end.y+SMALL_MARGIN<vy){
				index = -trSz-1;
			}
		}									
		if (index>=0 && s!=null){						
			int start = s.startIndex;
			int end = s.endIndex;						
			if (start<0) {
				start = 0;
				s.startIndex = 0;
			}
			if (end<0 || end<start) {
				end = start;
				s.endIndex = end;
				if (sz-start>0) {
					for (int ii = sz-start-1;ii>=0;--ii)
						elems[start+1+ii].copy(elems[start+ii]);
						
//					System.arraycopy(elems, start, elems, start+1, sz);
				}
				sz++;
				p = elems[start];
				p.x = v.x;
				p.y = v.y;
//				elems[start] = v;																	 								
				for (int ii=index+1;ii<trSz;++ii){					
					Segment ss = (which==-1) ? trArr[ trIndx[ii] ].leftSeg : trArr[ trIndx[ii] ].rightSeg;
					ss.startIndex++;
					ss.endIndex++;					
				}
				s.num++;
				s.updated = true;
				return sz;
			}					

			
			boolean inserted = (insertPoint(elems,sz, v, start, end+1,s));
			if (inserted) {
				sz++;
				s.updated = true;
				s.endIndex++;
				s.num++;
				for (int ii=index+1;ii<trSz;++ii){
					Segment ss = (which==-1) ? trArr[ trIndx[ii] ].leftSeg : trArr[ trIndx[ii] ].rightSeg;
					ss.startIndex++;
					ss.endIndex++;					
				}			
			}


		} else if (s!=null){//if index<0
			index = -index-1;						
			Segment prev = (index>0) ? (which==-1) ? trArr[ trIndx[index-1] ].leftSeg : trArr[ trIndx[index-1] ].rightSeg : null;
			Segment next = (index<trSz) ? (which==-1) ? trArr[ trIndx[index] ].leftSeg : trArr[ trIndx[index] ].rightSeg : null;
			int start = (prev==null || prev.endIndex<0) ? 0 : prev.endIndex+1;			
			int end = (index<0 || index>=trSz) ? sz : (which==-1) ? trArr[ trIndx[index] ].leftSeg.startIndex : trArr[trIndx[index] ].rightSeg.startIndex;
			//						int insertionIndx = binarySearchFromTo(elems, v, start, sz-1, Vector2DComparator);						
			//						boolean inserted = insertPoint(oldEdge, v, start, end);
			//						if (inserted) sz++;

			
			int indx = binarySearchFromTo(elems, v, start, end-1);
			if (indx>=0){ 
				p = elems[indx];
				p.x = v.x;
				p.y = v.y;
				p.certain = v.certain;
//				elems[indx] = v;					
				return sz;
			}
			if (indx<0) indx = -indx-1;
			
			boolean ok1 = false;
			boolean ok2 = false;
			double d1 = 0;
			double d2 = 0;			
			if (indx>start){				
				p1 = elems[indx-1];
				double dx = p1.x-v.x;
				double dy = p1.y - v.y;
				d1 = Math.sqrt(dx*dx+dy*dy);
				if (d1<MINDIST && !p1.certain && (prev==null || indx-1!=prev.endIndex)) 								
					ok1 = true;							
			}

			if (indx<end){				
				p2 = elems[indx];
				double dx = p2.x-v.x;
				double dy = p2.y - v.y;
				d2 = Math.sqrt(dx*dx+dy*dy);
				if (d2<MINDIST && !p2.certain && (next==null || indx!=next.startIndex)) 															
					ok2 = true;							
			}
			if (ok1 && ok2){
				if (d1<=d2){ 					
					p1.x = v.x;
					p1.y = v.y;
					p1.certain = v.certain;
				} else {					
					p2.x = v.x;
					p2.y = v.y;
					p2.certain = v.certain;
				}
			} else if (ok1){ 				
				p1.x = v.x;
				p1.y = v.y;
				p1.certain = v.certain;
			} else if (ok2) {				
				p2.x = v.x;
				p2.y = v.y;
				p2.certain = v.certain;
			} else {
				if ((sz-indx)>0) {
					for (int ii = sz-indx-1;ii>=0;--ii)
						elems[indx+1+ii].copy(elems[indx+ii]);
						
//					System.arraycopy(elems, indx, elems, indx+1, sz);
				}
				sz++;
				p = elems[indx];
				p.x = v.x;
				p.y = v.y;
//				elems[indx] = v;
				for (int ii=index;ii<trSz;++ii){
					Segment ss = (which==-1) ? trArr[ trIndx[ii] ].leftSeg : trArr[ trIndx[ii] ].rightSeg;
					ss.startIndex++;
					ss.endIndex++;					
				}
			}																							
		}//end of if

		//	if (changed && edge!=null && edge.size()>0)
		//		Arrays.quicksort(edge.elements(), 0,edge.size()-1,new Swap(edge.elements()), Vector2DComparator);
		return sz;
	}

	/*private static final int add(Vector2D[] elems,int sz,int index,Vector2D point){
		if (index<0 || index>sz)throw new ArrayIndexOutOfBoundsException( "Array index "+index+" out of bound" );
		if (index==sz) {
			elems[sz++] = point;
			return sz;
		}
		System.arraycopy(elems, index, elems, index+1, sz++ -index);
		elems[index] = new Vector2D(point);
		return sz;
	}//*/

	/*private static final int remove(Vector2D[] elems,int sz,int index){
		if (index<0 || index>=sz)throw new ArrayIndexOutOfBoundsException( "Array index "+index+" out of bound" );
		if (index==sz-1) {
			elems[--sz] = null;
			return sz;
		}
		System.arraycopy(elems, index+1, elems, index, --sz-index);			
		return sz;
	}//*/

	/*private static final int insert(Vector2D[] right,int sz,int index,Vector2D lower){
		if (lower.certain){
			sz = add(right,sz,index,new Vector2D(lower));
			Vector2D p = null;
			if (index>0){				
				p = right[index-1];
				if (p!=null && !p.certain && Math.hypot(p.x-lower.x,p.y-lower.y)<MINDIST){
					sz = remove(right,sz,--index);
				}
			}

			if (index<sz-1){				
				p = right[index+1];
				if (p!=null && !p.certain && Math.hypot(p.x-lower.x,p.y-lower.y)<MINDIST) sz = remove(right,sz,index+1);					
			}
		} else {
			sz = add(right,sz,index,new Vector2D(lower));
			Vector2D p = null;
			boolean ok = true;
			if (index>0){				
				p = right[index-1];
				if (p!=null && p.certain && Math.hypot(p.x-lower.x,p.y-lower.y)<MINDIST){
					sz = remove(right,sz,index);
					ok = false;
				}
			}

			if (ok && index<sz-1){				
				p = right[index+1];
				if (p!=null && p.certain && Math.hypot(p.x-lower.x,p.y-lower.y)<MINDIST) sz = remove(right,sz,index+1);					
			}
		}
		return sz;
	}//*/

	public static final void guessHighestPointEdge(Vector2D lower,int whichL,Vector2D higher,int whichH,Vector2D[] left,int sL,Vector2D[] right,int sR,double trackWidth,int[] out){
		long ti = System.currentTimeMillis();
		if (whichL!=0 && whichH!=0) return;
		double lx = (lower==null) ? 0 :lower.x;
		double ly = (lower==null) ? 0 :lower.y;		
		double hx = (higher==null) ? 0 :higher.x;
		double hy = (higher==null) ? 0 :higher.y;
		double ll = (lower==null) ? 100 : Math.sqrt(lx*lx+ly*ly);
		double hl = Math.sqrt(hx*hx+hy*hy);
		double dx = hx-lx;
		double dy = hy-ly;
		double ldh = (lower==null) ? 100 : Math.sqrt(dx*dx+dy*dy);
		ldh = Math.round(ldh*1000)/1000.0d;
		if (lower!=null && higher!=null && ldh>0){		
			if (whichL==0 && lower!=null && ll<MAX_DISTANCE){
				if (whichH!=0 && hl<MAX_DISTANCE && ldh<trackWidth-CERTAIN_DIST){
					whichL = whichH;
				} else {
					double ldv = 100;
					if (sL>0){
						Vector2D v = left[sL-1];
						if (v.y<=lower.y){
							double dvx = lx - v.x;
							double dvy = ly - v.y;
							ldv = Math.sqrt(dvx*dvx+dvy*dvy);
							ldv = Math.round(ldv*1000.0d)/1000.0d;
							double absDvx = Math.abs(dvx);
							double absDvy = Math.abs(dvy);
							if (absDvx>trackWidth && absDvx<=UPPER_LIM && absDvx>=absDvy && absDvy<=LOWER_LIM ) whichL = 1;
						} else {							
							v = new Vector2D();
							findNearestPoint(lower, left,sL, v);
							double dvx = lx - v.x;
							double dvy = ly - v.y;
							ldv = Math.sqrt(dvx*dvx+dvy*dvy);
							ldv = Math.round(ldv*1000.0d)/1000.0d;			
							double absDvx = Math.abs(dvx);
							double absDvy = Math.abs(dvy);
							if (absDvx>trackWidth && absDvx<=UPPER_LIM && absDvx>=absDvy && absDvy<=LOWER_LIM ) whichL = 1;
						}						
					}
					double rdv = 100;
					if (sR>0 && whichL==0){
						Vector2D v = right[sR-1];
						if (v.y<=lower.y){
							double dvx = lx - v.x;
							double dvy = ly - v.y;
							rdv = Math.sqrt(dvx*dvx+dvy*dvy);
							rdv = Math.round(rdv*1000.0d)/1000.0d;
							double absDvx = Math.abs(dvx);
							double absDvy = Math.abs(dvy);
							if (absDvx>trackWidth && absDvx<=UPPER_LIM && absDvx>=absDvy && absDvy<=LOWER_LIM ) whichL = -1;
						} else {	
							v = new Vector2D();
							findNearestPoint(lower, right,sR, v);
							double dvx = lx - v.x;
							double dvy = ly - v.y;
							rdv = Math.sqrt(dvx*dvx+dvy*dvy);
							rdv = Math.round(rdv*1000.0d)/1000.0d;	
							double absDvx = Math.abs(dvx);
							double absDvy = Math.abs(dvy);
							if (absDvx>trackWidth && absDvx<=UPPER_LIM && absDvx>=absDvy && absDvy<=LOWER_LIM ) whichL = -1;
						}																		
					}
									
					if (whichL==0){
						if (sL>0 && sR>0){				
							if (ldv<trackWidth-EPS && ldv<rdv) 
								whichL = -1;
							else if (rdv<trackWidth-EPS && rdv<ldv)
								whichL = 1;
							else if (ldv<trackWidth-EPS)
								whichL = -1;
							else if (rdv<trackWidth-EPS)
								whichL = 1;
						} else if (sL>0 && ldv<trackWidth-EPS){
							whichL = -1;
						} else if (sR>0 && rdv<trackWidth-EPS){
							whichL = 1;
						}
					}
				} 				

				if (hl<MAX_DISTANCE && ldh<trackWidth) whichH = whichL;
			}			
		}

		if (whichH==0 && higher!=null && hl<MAX_DISTANCE){
			if (whichL!=0 && ll<MAX_DISTANCE && ldh<trackWidth-CERTAIN_DIST){ 
				whichH = whichL;
			} else {
				double ldv = 100;
				if (sL>0){
					Vector2D v = left[sL-1];
					if (v.y<=higher.y){
						double dvx = hx - v.x;
						double dvy = hy - v.y;
						ldv = Math.sqrt(dvx*dvx+dvy*dvy);
						ldv = Math.round(ldv*1000.0d)/1000.0d;
						double absDvx = Math.abs(dvx);
						double absDvy = Math.abs(dvy);
						if (absDvx>trackWidth && absDvx<=UPPER_LIM && absDvx>=absDvy && absDvy<=LOWER_LIM ) whichH = 1;
					}
				}
				double rdv = 100;
				if (sR>0 && whichH==0){
					Vector2D v = right[sR-1];
					if (v.y<=higher.y){
						double dvx = hx - v.x;
						double dvy = hy - v.y;
						rdv = Math.sqrt(dvx*dvx+dvy*dvy);
						rdv = Math.round(rdv*1000.0d)/1000.0d;
						double absDvx = Math.abs(dvx);
						double absDvy = Math.abs(dvy);
						if (absDvx>trackWidth && absDvx<=UPPER_LIM && absDvx>=absDvy && absDvy<=LOWER_LIM ) whichH = -1;
					} 																	
				}
								
				if (whichH==0){
					if (sL>0 && sR>0){				
						if (ldv<trackWidth-EPS && ldv<rdv) 
							whichH = -1;
						else if (rdv<trackWidth-EPS && rdv<ldv)
							whichH = 1;
						else if (ldv<trackWidth-EPS)
							whichH = -1;
						else if (rdv<trackWidth-EPS)
							whichH = 1;
					} else if (sL>0 && ldv<trackWidth-EPS){
						whichH = -1;
					} else if (sR>0 && rdv<trackWidth-EPS){
						whichH = 1;
					}
				}
			}
			
			if (whichH!=0 && whichL==0 && ldh<trackWidth-EPS) whichL = whichH;
		}
		Segment[] trArr = CircleDriver2.trArr;
		int trSz = CircleDriver2.trSz;
		int[] trIndx = CircleDriver2.trIndx;
		if (whichL==0 && ll<MAX_DISTANCE){
			double tx = 0;
			double ty = 0;
			double dl = 0;
			double dr = 0;
			for (int i = trSz-1;i>=0;--i){
				Segment t = trArr[ trIndx[i] ];
				if (t.type==Segment.UNKNOWN) continue;
				Segment lSeg = t.leftSeg;
				Segment rSeg = t.rightSeg;
				
				int tp = t.type;
				if (ly>=lSeg.start.y || ly>=rSeg.start.y){
					tx = lx - lSeg.end.x;
					ty = ly - lSeg.end.y;
					dl = Math.sqrt(tx*tx+ty*ty);
					tx = lx - rSeg.end.x;
					ty = ly - rSeg.end.y;
					dr = Math.sqrt(tx*tx+ty*ty);
					if (dl<trackWidth && dr<trackWidth){
						whichL = (dl<dr) ? -1 : 1;
						break;
					} else if (dl<trackWidth-EPS) {
						whichL = -1;
						break;
					} else if (dr<trackWidth-EPS) {
						whichL = 1;
						break;
					}
				}
				
				if (ly<=lSeg.end.y || ly<=rSeg.end.y){
					tx = lx - lSeg.start.x;
					ty = ly - lSeg.start.y;
					dl = Math.sqrt(tx*tx+ty*ty);
					tx = lx - rSeg.start.x;
					ty = ly - rSeg.start.y;
					dr = Math.sqrt(tx*tx+ty*ty);
					if (dl<trackWidth && dr<trackWidth){
						whichL = (dl<dr) ? -1 : 1;
						break;
					} else if (dl<trackWidth-EPS) {
						whichL = -1;
						break;
					} else if (dr<trackWidth-EPS) {
						whichL = 1;
						break;
					}
				}
				
				if (i==trSz-1 && ly>lSeg.end.y && ly>rSeg.end.y){																				
					int turnLeft = 0;
					int turnRight = 0;
					int numL = sL - lSeg.endIndex-1; 
					int numR = sR - rSeg.endIndex-1;
					if (numL>=1){
						Vector2D v = left[lSeg.endIndex+1];
						turnLeft = (lSeg.type==0 && CircleDriver2.isFirstSeg(lSeg) && Math.abs(lSeg.end.x-v.x)<E) ? 0 : (v.x>lSeg.end.x) ? 1 : -1;
					} 
					
					if (numR>=1){
						Vector2D v = right[rSeg.endIndex+1];
						turnRight = (rSeg.type==0 && CircleDriver2.isFirstSeg(rSeg) && Math.abs(rSeg.end.x-v.x)<E) ? 0 : (v.x>rSeg.end.x) ? 1 : -1;
					} 
					
					if (turnLeft!=0 && turnRight!=0 && turnLeft!=turnRight)
						System.out.println("Check immediately");
					else if (turnLeft!=0 && turnRight!=0 && turnLeft==turnRight)
						whichL = turnLeft;
				}

				if (tp!=0 && (ly>=lSeg.end.y || ly>=rSeg.end.y)){
					tx = lx-lSeg.center.x;
					ty = ly-lSeg.center.y;
					dl = Math.sqrt(tx*tx+ty*ty) - lSeg.radius;					
					tx = lx-rSeg.center.x;
					ty = ly-rSeg.center.y;
					dr = Math.sqrt(tx*tx+ty*ty) - rSeg.radius;
					if (dl<0) dl = -dl;
					if (dr<0) dr = -dr;
					if (dl<trackWidth && dr<trackWidth){
						whichL = (dl<dr) ? -1 : 1;						
					} else if (dl<=trackWidth-EPS){ 
						whichL = -1;
					} else if (dr<=trackWidth-EPS) 
						whichL = 1;					
				} else if (tp==0){
					dl = Math.sqrt(Geom.ptSegDistSq(lSeg.start.x, lSeg.start.y, lSeg.end.x, lSeg.end.y, lx, ly, null));
					dr = Math.sqrt(Geom.ptSegDistSq(rSeg.start.x, rSeg.start.y, rSeg.end.x, rSeg.end.y, lx, ly, null));
					if (dl<trackWidth && dr<trackWidth){
						whichL = (dl<dr) ? -1 : 1;						
					} else if (dl<=trackWidth-EPS){ 
						whichL = -1;
					} else if (dr<=trackWidth-EPS) 
						whichL = 1;					
				}

				if (whichL!=0) break;
				if (ly>lSeg.end.y && ly>rSeg.end.y || ly>lSeg.start.y && ly>rSeg.start.y) break;
			}//end of for			

		}//end of if
		
		if (whichL!=0 && hl<MAX_DISTANCE && ll<MAX_DISTANCE && ldh<trackWidth-CERTAIN_DIST){ 
			whichH = whichL;
		} else if (whichH==0 && hl<MAX_DISTANCE){
			double tx = 0;
			double ty = 0;
			double dl = 0;
			double dr = 0;
			for (int i = trSz-1;i>=0;--i){
				Segment t = trArr[ trIndx[i] ];
				if (t.type==Segment.UNKNOWN) continue;
				Segment lSeg = t.leftSeg;
				Segment rSeg = t.rightSeg;
				int tp = t.type;
				if (hy>=lSeg.start.y || hy>=rSeg.start.y){
					tx = hx - lSeg.end.x;
					ty = hy - lSeg.end.y;
					dl = Math.sqrt(tx*tx+ty*ty);
					tx = hx - rSeg.end.x;
					ty = hy - rSeg.end.y;
					dr = Math.sqrt(tx*tx+ty*ty);
					if (dl<trackWidth && dr<trackWidth){
						whichH = (dl<dr) ? -1 : 1;
						break;
					} else if (dl<trackWidth-EPS) {
						whichH = -1;
						break;
					} else if (dr<trackWidth-EPS) {
						whichH = 1;
						break;
					}
				}
				
				if (hy<=lSeg.end.y || hy<=rSeg.end.y){
					tx = hx - lSeg.start.x;
					ty = hy - lSeg.start.y;
					dl = Math.sqrt(tx*tx+ty*ty);
					tx = hx - rSeg.start.x;
					ty = hy - rSeg.start.y;
					dr = Math.sqrt(tx*tx+ty*ty);
					if (dl<trackWidth && dr<trackWidth){
						whichH = (dl<dr) ? -1 : 1;
						break;
					} else if (dl<trackWidth-EPS) {
						whichH = -1;
						break;
					} else if (dr<trackWidth-EPS) {
						whichH = 1;
						break;
					}
				}
				
				if (i==trSz-1 && hy>lSeg.end.y && hy>rSeg.end.y){																				
					int turnLeft = 0;
					int turnRight = 0;
					int numL = sL - lSeg.endIndex-1; 
					int numR = sR - rSeg.endIndex-1;
					if (numL>=1){
						Vector2D v = left[lSeg.endIndex+1];
						turnLeft = (lSeg.type==0 && CircleDriver2.isFirstSeg(lSeg) && Math.abs(lSeg.end.x-v.x)<E) ? 0 : (v.x>lSeg.end.x) ? 1 : -1;
					} 
					
					if (numR>=1){
						Vector2D v = right[rSeg.endIndex+1];
						turnRight = (rSeg.type==0 && CircleDriver2.isFirstSeg(rSeg) && Math.abs(rSeg.end.x-v.x)<E) ? 0 : (v.x>rSeg.end.x) ? 1 : -1;
					} 
					
					if (turnLeft!=0 && turnRight!=0 && turnLeft!=turnRight)
						System.out.println("Check immediately");
					else if (turnLeft!=0 && turnRight!=0 && turnLeft==turnRight)
						whichH = turnLeft;
				}

				
				if (tp!=0 && (hy>=lSeg.end.y || hy>=rSeg.end.y)){
					tx = hx-lSeg.center.x;
					ty = hy-lSeg.center.y;
					dl = Math.sqrt(tx*tx+ty*ty) - lSeg.radius;					
					tx = hx-rSeg.center.x;
					ty = hy-rSeg.center.y;
					dr = Math.sqrt(tx*tx+ty*ty) - rSeg.radius;
					if (dl<0) dl = -dl;
					if (dr<0) dr = -dr;
					if (dl<trackWidth && dr<trackWidth){
						whichH = (dl<dr) ? -1 : 1;						
					} else if (dl<=trackWidth-EPS){ 
						whichH = -1;
					} else if (dr<=trackWidth-EPS) 
						whichH = 1;					
				} else if (tp==0){
					dl = Math.sqrt(Geom.ptSegDistSq(lSeg.start.x, lSeg.start.y, lSeg.end.x, lSeg.end.y, hx, hy, null));
					dr = Math.sqrt(Geom.ptSegDistSq(rSeg.start.x, rSeg.start.y, rSeg.end.x, rSeg.end.y, hx, hy, null));
					if (dl<trackWidth && dr<trackWidth){
						whichH = (dl<dr) ? -1 : 1;						
					} else if (dl<=trackWidth-EPS){ 
						whichH = -1;
					} else if (dr<=trackWidth-EPS) 
						whichH = 1;					
				}

				if (whichH!=0) break;
				if (hy>lSeg.end.y && hy>rSeg.end.y || hy>lSeg.start.y && hy>rSeg.start.y) break;
			}//end of for				

		}//end of if

						
		
		if (whichL==0 && whichH==0 && hl<MAX_DISTANCE && ll<MAX_DISTANCE && ldh<trackWidth && ldh>=MINDIST){
			double  angleH = (higher.x==0) ? PI_2 : Math.PI-Math.atan2(higher.y,higher.x);	
			angleH=Math.round(angleH*PRECISION)/PRECISION;
			double  angleL = (lower.x==0) ? PI_2 : Math.PI-Math.atan2(lower.y,lower.x);	
			angleL=Math.round(angleL*PRECISION)/PRECISION;
			if (angleL<angleH && left!=null && higher!=null && hl<MAX_DISTANCE) {							
				whichL = -1;
			} else if (angleL>angleH && right!=null && higher!=null && hl<MAX_DISTANCE){							
				whichL = 1;
			}					
		}
										
		if (CircleDriver2.debug) System.out.println("End of guess Highest Point "+(System.currentTimeMillis()-ti)+" ms.");
		if (out!=null){
			out[0] = whichL;
			out[1] = whichH;
		}			
	}

	public static final int findNearestPoint(final Vector2D point, final Vector2D[] elems, int sz,Vector2D nearest){			
		int index = binarySearchFromTo(elems, point, 0, sz-1);
		if (index<0) index = -index-1;
		Vector2D p1 = (index>0) ? elems[index-1] : null;
		Vector2D p;
		if (p1==null) {
			if (sz>=0) {
				p = elems[0];
				nearest.x = p.x;
				nearest.y = p.y;
			}
			return 0;
		}
		Vector2D p2 = (index<sz) ? elems[index] : null;
		if (p2==null) {
			if (sz>=0) {
				p = elems[sz-1];
				nearest.x = p.x;
				nearest.y = p.y;
			}
			return index;
		}
		double dx1 = point.x - p1.x;
		double dy1 = point.y - p1.y;
		double dx2 = point.x - p2.x;
		double dy2 = point.y - p2.x;
		p = (dx1*dx1+dy1*dy1<dx2*dx2+dy2*dy2) ? p1 : p2;
		nearest.x = p.x;
		nearest.y = p.y;
		return  index;
	}

	private static final int join(double scale,double tx,double ty,Vector2D[] oldElems,int os,Vector2D[] elems,int sz,Segment[] trArr,int[] trIndx,int trSz,int which){									
		int i = os-1;
		int[] occupied = CircleDriver2.occupied;
		for (;i>=0;--i) {
			Vector2D v = oldElems[i];
			if (scale!=1) v.x *= scale;
			v.x+=tx;
			v.y+=ty;
			v.certain = false;
		}

		for (i=trSz-1;i>=0;--i){
			Segment s = (which==-1) ? trArr[ trIndx[i] ].leftSeg : trArr[ trIndx[i] ].rightSeg;
			if (scale!=1.0) s.scale(scale);
			s.translate(tx, ty);
		}

		if (sz==0){
			for (i = os-1;i>=0;--i){
//				Vector2D src = oldElems[i];
//				Vector2D dest = elems[i];
//				dest.x = src.x;
//				dest.y = src.y;
				elems[i].copy(oldElems[i]);
			}
//			System.arraycopy(oldElems, 0, elems, 0, os);
			return os;
		}

		Segment s;
		Segment prev = null;
		int prevIndex = -1;
		Vector2D key = null;
		int j = 0;
		Vector2D p1 = null;
		Vector2D p2 = null;
		int oldEndIndx = (trSz==0) ? 0 : (which==-1) ? trArr[ trIndx[trSz-1] ].leftSeg.endIndex+1 : trArr[ trIndx[trSz-1] ].rightSeg.endIndex+1;
		int k = sz;
		boolean done = false;
		for (i = 0;i<trSz;++i){
			if (j>=sz){
				done = true;
				break;
			}
			s = (which==-1) ? trArr[ trIndx[i] ].leftSeg : trArr[ trIndx[i] ].rightSeg;
			if (s.type==Segment.UNKNOWN){
				occupied[ trIndx[i] ] = 0;
				int idx = i+1;
				if (trSz-idx>0) System.arraycopy(trIndx, idx, trIndx, i, trSz-idx);
				trSz--;
				continue;
			}						

			key = s.start;
			int index = binarySearchFromTo(elems, key, j, sz-1);
			if (index<0) index = -index-1;
			if (prev==null){										
				if (index>0){
					for (int ii = index-1;ii>=0;--ii){
//						Vector2D src = elems[ii];
//						Vector2D dest = elems[k+ii];
//						dest.x = src.x;
//						dest.y = src.y;
						elems[k+ii].copy(elems[ii]);
					}
//					System.arraycopy(elems, 0, elems, k, index);
					k += index;
					j = index;
				}
			} else {
				int size = 0;				
				int start = prevIndex+1;					 
				int end = s.startIndex;
				size = end - start;
				if (size>0) {
					for (int ii = size-1;ii>=0;--ii){
//						Vector2D src = oldElems[start+ii];
//						Vector2D dest = elems[k+ii];
//						dest.x = src.x;
//						dest.y = src.y;
						elems[k+ii].copy(oldElems[start+ii]);
					}
//					System.arraycopy(oldElems, start, elems, k,size);				
				}

				for (int t=j;t<index;++t){
					Vector2D point = elems[t];
					int pos = (size>0) ? binarySearchFromTo(elems, point, k, k+size-1) : -(k+1);
					if (pos>=0){ 
						p2 = elems[pos];
						p2.x = point.x;
						p2.y = point.y;
						p2.certain = point.certain;
					} else {
						pos = -pos - 1;
						boolean ok1 = false;
						boolean ok2 = false;
						double d1 = 0;
						double d2 = 0;						
						if (pos>k){				
							p1 = elems[pos-1];
							double dx = p1.x-point.x;
							double dy = p1.y-point.y;
							d1 = Math.sqrt(dx*dx+dy*dy);
							if (d1<MINDIST && !p1.certain) 								
								ok1 = true;							
						}

						if (pos<k+size){				
							p2 = elems[pos];
							double dx = p2.x-point.x;
							double dy = p2.y-point.y;
							d2 = Math.sqrt(dx*dx+dy*dy);							
							if (d2<MINDIST && !p2.certain) 															
								ok2 = true;							
						}
						if (ok1 && ok2){
							if (d1<=d2){ 
								p1.x = point.x;
								p1.y = point.y;
								p1.certain = point.certain;
							} else {
								p2.x = point.x;
								p2.y = point.y;
								p2.certain = point.certain;
							}
						} else if (ok1){ 
							p1.x = point.x;
							p1.y = point.y;
							p1.certain = point.certain;
						} else if (ok2) {
							p2.x = point.x;
							p2.y = point.y;				
							p2.certain = point.certain;
						} else {
							if (k+size-pos>0) {
								for (int ii = k+size-pos-1;ii>=0;--ii){
									elems[pos+1+ii].copy(elems[pos+ii]);
//									Vector2D src = elems[pos+ii];
//									Vector2D dest = elems[pos+1+ii];
//									dest.x = src.x;
//									dest.y = src.y;
								}
//								System.arraycopy(elems, pos, elems, pos+1, k+size-pos);
							}
//							elems[pos]= new Vector2D(point);
							p2 = elems[pos];
							p2.x = point.x;
							p2.y = point.y;
							p2.certain = point.certain;
							size++;
						}

					}//end of else
				}//end of for				
				if (size>0){					
					k+=size;
				}
				j = index;
			}//end of gap

			int start = s.startIndex;
			int end = s.endIndex+1;
			int size = end-start;
			prevIndex = s.endIndex;
			if (size>0 && start>=0) {
				if (size>2 && s.type==0 && s.end.x==s.start.x){
					size = 2;
					p2 = elems[k];
					p1 = oldElems[start];
					p2.copy(p1);
//					elems[k] = new Vector2D(oldElems[start]);
					p2 = elems[k+1];
					p1 = oldElems[end-1];
					p2.copy(p1);
//					p2.x = p1.x;
//					p2.y = p1.y;
//					elems[k+1] = new Vector2D(oldElems[end-1]);
				} else {
					for (int ii = size-1;ii>=0;--ii){
						elems[k+ii].copy(oldElems[start+ii]);
//						Vector2D src = oldElems[start+ii];
//						Vector2D dest = elems[k+ii];
//						dest.x = src.x;
//						dest.y = src.y;
					}
//					System.arraycopy(oldElems, start, elems, k, size);
				}
			}

			key = s.end;
			int endIndex = binarySearchFromTo(elems, key, j, sz-1);
			if (endIndex<0) 
				endIndex = -endIndex-1;
			else endIndex++;
			j = endIndex;

			for (int t=index;t<endIndex;++t){
				Vector2D point = elems[t];
				int pos = (size>0) ? binarySearchFromTo(elems, point, k, k+size-1) : -(k+1);
				if (pos>=0){									
					p2 = elems[pos];
					p2.x = point.x;
					p2.y = point.y;		
					p2.certain = point.certain;
				} else {
					pos = -pos - 1;
					boolean ok1 = false;
					boolean ok2 = false;				
					double d1 = 0;
					double d2 = 0;					
					if (pos>k){				
						p1 = elems[pos-1];
						double dx = p1.x-point.x;
						double dy = p1.y-point.y;
						d1 = Math.sqrt(dx*dx+dy*dy);
						if (d1<MINDIST && !p1.certain) 								
							ok1 = true;							
					}

					if (pos<k+size){				
						p2 = elems[pos];
						double dx = p2.x-point.x;
						double dy = p2.y-point.y;
						d2 = Math.sqrt(dx*dx+dy*dy);							
						if (d2<MINDIST && !p2.certain) 															
							ok2 = true;							
					}
					if (ok1 && ok2){
						if (d1<=d2){ 
							p1.x = point.x;
							p1.y = point.y;
							p1.certain = point.certain;
						} else {
							p2.x = point.x;
							p2.y = point.y;
							p2.certain = point.certain;
						}
					} else if (ok1){ 
						p1.x = point.x;
						p1.y = point.y;	
						p1.certain = point.certain;
					} else if (ok2) {
						p2.x = point.x;
						p2.y = point.y;
						p2.certain = point.certain;
					} else {
						if (k+size-pos>0) {
							for (int ii = k+size-pos-1;ii>=0;--ii){
								elems[pos+1+ii].copy( elems[pos+ii]);
//								Vector2D src = elems[pos+ii];
//								Vector2D dest = elems[pos+1+ii];
//								dest.x = src.x;
//								dest.y = src.y;
							}
//							System.arraycopy(elems, pos, elems, pos+1, k+size-pos);
						}
//						elems[pos]= new Vector2D(point);
						p2 = elems[pos];
						p2.x = point.x;
						p2.y = point.y;	
						p2.certain = point.certain;
						size++;
					}
				}//end of else
			}//end of for				
//			segs[newIndex++] = s;
			s.num = size;
			s.updated = (index<endIndex);
			int sI = k - sz;
			s.startIndex = sI;
			s.endIndex = sI+size-1;
			k+=size;
			prev = s;			
		}

		
		if (!done){
			//Do the last bit
			int size = 0;
			if (oldEndIndx<os){										 							
				size = os - oldEndIndx;
				if (size>0) {
					for (int ii = size-1;ii>=0;--ii){
						elems[k+ii].copy(oldElems[oldEndIndx+ii]);
//						Vector2D src = oldElems[oldEndIndx+ii];
//						Vector2D dest = elems[k+ii];
//						dest.x = src.x;
//						dest.y = src.y;
					}
	//				System.arraycopy(oldElems, oldEndIndx, elems, k,size);				
				}
			}
	
			for (int t=j;t<sz;++t){
				Vector2D point = elems[t];
				int pos = (size>0) ? binarySearchFromTo(elems, point, k, k+size-1) : -(k+1);
				if (pos>=0){									
					p2 = elems[pos];
					p2.x = point.x;
					p2.y = point.y;
					p2.certain = point.certain;
				} else {
					pos = -pos - 1;
					boolean ok1 = false;
					boolean ok2 = false;				
					double d1 = 0;
					double d2 = 0;					
					if (pos>k){				
						p1 = elems[pos-1];
						double dx = p1.x-point.x;
						double dy = p1.y-point.y;
						d1 = Math.sqrt(dx*dx+dy*dy);
						if (d1<MINDIST && !p1.certain) 								
							ok1 = true;							
					}
	
					if (pos<k+size){				
						p2 = elems[pos];
						double dx = p2.x-point.x;
						double dy = p2.y-point.y;
						d2 = Math.sqrt(dx*dx+dy*dy);							
						if (d2<MINDIST && !p2.certain) 															
							ok2 = true;							
					}
					if (ok1 && ok2){
						if (d1<=d2){ 
							p1.x = point.x;
							p1.y = point.y;
							p1.certain = point.certain;
						} else {
							p2.x = point.x;
							p2.y = point.y;
							p2.certain = point.certain;
						}
					} else if (ok1){ 
						p1.x = point.x;
						p1.y = point.y;
						p1.certain = point.certain;
					} else if (ok2) {
						p2.x = point.x;
						p2.y = point.y;		
						p2.certain = point.certain;
					} else {
						if (k+size-pos>0) {
							for (int ii = k+size-pos-1;ii>=0;--ii){
								elems[pos+1+ii].copy(elems[pos+ii]);
//								Vector2D src = elems[pos+ii];
//								Vector2D dest = elems[pos+1+ii];
//								dest.x = src.x;
//								dest.y = src.y;
							}
	//						System.arraycopy(elems, pos, elems, pos+1, k+size-pos);
						}
	//					elems[pos]= new Vector2D(point);
						p2 = elems[pos];
						p2.x = point.x;
						p2.y = point.y;	
						p2.certain = point.certain;
						size++;
					}
				}//end of else
			}//end of for
			
			
			if (size>0){			
				k+=size;
			}
		}
		k -= sz;
		for (int ii = 0;ii<k;++ii){
//			Vector2D src = elems[sz+ii];
//			Vector2D dest = elems[ii];
//			dest.x = src.x;
//			dest.y = src.y;
			elems[ii].copy(elems[sz+ii]);
		}
		
		if (done){
			prevIndex++;
			int diff = k-prevIndex;											 							
			int size = os - prevIndex;
			if (size>0) {
				for (int ii = size-1;ii>=0;--ii){
//					Vector2D src = oldElems[prevIndex+ii];
//					Vector2D dest = elems[k+ii];
//					dest.x = src.x;
//					dest.y = src.y;
					elems[k+ii].copy(oldElems[prevIndex+ii]);
				}
//				System.arraycopy(oldElems, oldEndIndx, elems, k,size);
				k+=size;
			}
			
			for (;i<trSz;++i){
				s = (which==-1) ? trArr[ trIndx[i] ].leftSeg : trArr[ trIndx[i] ].rightSeg;
				if (s.type==Segment.UNKNOWN){
					occupied[ trIndx[i] ] = 0;
					int idx = i+1;
					if (trSz-idx>0) System.arraycopy(trIndx, idx, trIndx, i, trSz-idx);
					trSz--;
					continue;
				}
				s.startIndex+=diff;
				s.endIndex+=diff;
			}
		}
//		System.arraycopy(elems, sz, elems, 0, k-sz);
		CircleDriver2.trSz = trSz;
		return k;
	}
	
	public void reset(){
		lSize = nLsz;
		for (int ii = lSize-1;ii>=0;--ii)
			left[ii].copy(nleft[ii]);
		rSize = nRsz;
		for (int ii = rSize-1;ii>=0;--ii)
			right[ii].copy(nright[ii]);
		CircleDriver2.inTurn = true;
		for (int ii = CircleDriver2.trSz-1;ii>=0;--ii)
			CircleDriver2.occupied[CircleDriver2.trIndx[ii]] = 0;
		CircleDriver2.trSz = 0;
	}
	
	public void double_check(Vector2D highest){
		Segment[] trArr = CircleDriver2.trArr;
		int[] trIndx = CircleDriver2.trIndx;
		int trSz = CircleDriver2.trSz;
		int isL = 0;
		numClosePoints = 0;
		for (int i = lSize-1;i>=0;--i){
			Vector2D v = left[i];
			if (v.y<=0) break;
			double angle = Vector2D.angle(highest.x, highest.y, v.x, v.y);
			double dv = v.distance(highest);			
			if (highest.length() > MAX_DISTANCE ||dv>trackWidth+EPS){
				if (angle>=0){
					i = removeFromLeftEdge(i, trArr, trSz, trIndx, CircleDriver2.occupied);
					trSz = CircleDriver2.trSz;
				}				
			} 
		}
		
		for (int i = lSize-1;i>=0;--i){
			Vector2D v = left[i];
			if (v.y<=0) break;
			double angle = Vector2D.angle(highest.x, highest.y, v.x, v.y);
			double dv = v.distance(highest);			
			if (highest.length() < MAX_DISTANCE && dv<=trackWidth-EPS){
				isL = 1;
				closePointV[numClosePoints].copy(v);
				closePoint[numClosePoints++] = dv<0.1 || angle<=0 ? -1 : 1;
			}
		}
						
		
		int isR = 0;
		for (int i = rSize-1;i>=0;--i){
			Vector2D v = right[i];
			if (v.y<=0) break;			
			double angle = Vector2D.angle(highest.x, highest.y, v.x, v.y);
			double dv = v.distance(highest);
			if (highest.length() > MAX_DISTANCE || dv>trackWidth+EPS){
				if (angle<0){
					i = removeFromRightEdge(i, trArr, trSz, trIndx, CircleDriver2.occupied);
					trSz = CircleDriver2.trSz;
				}
			} 
		}
		
		for (int i = rSize-1;i>=0;--i){
			Vector2D v = right[i];
			if (v.y<=0) break;			
			double angle = Vector2D.angle(highest.x, highest.y, v.x, v.y);
			double dv = v.distance(highest);
			if (highest.length() < MAX_DISTANCE && dv<=trackWidth-EPS){				
				isR = 1;
				closePointV[numClosePoints].copy(v);
				closePoint[numClosePoints++] = dv>0.1 && angle<0 ? -1 : 1;								
			} 
		}
				
		
		if (isL==1 || isR==1){
			//sort vector of closepoints
			for (int i = numClosePoints-1;i>=1;--i){
				for (int j = i-1;j>=0;--j){
					if (closePointV[j].y>closePointV[i].y){
						Vector2D tmp = closePointV[i];
						closePointV[i] = closePointV[j];
						closePointV[j] = tmp;
						int tmpI = closePoint[i];
						closePoint[i] = closePoint[j];
						closePoint[j] = tmpI;
					}
				}
			}
			
			int firstGuess = closePoint[0];
			int n = removeElems(closePointV, 0, numClosePoints, tmpIndx);
			
			if (n!=numClosePoints || !(firstGuess==1 && isL==0 || firstGuess==-1 && isR==0)){ 
				for (int i = n-1;i>=0;--i){
					Vector2D v = closePointV[ tmpIndx[i] ];
					int j = binarySearchFromTo(left, v, 0, lSize-1);
					if (j>=0 && firstGuess==1){					
						removeFromLeftEdge(j, trArr, trSz, trIndx, CircleDriver2.occupied);
						trSz = CircleDriver2.trSz;
					} else if (j<0 && firstGuess==-1){
						j = binarySearchFromTo(right, v, 0, rSize-1);
						if (j>=0){
							removeFromRightEdge(j, trArr, trSz, trIndx, CircleDriver2.occupied);
							trSz = CircleDriver2.trSz;
						}
					}
				}
			}
		}
		CircleDriver2.trSz = trSz;
		CircleDriver2.edgeDetector.lSize = lSize;
		CircleDriver2.edgeDetector.rSize = rSize;
	}

	public final void combine(EdgeDetector ed,double distRaced,Segment[] trArr,int[] trIndx,int trSz){	
		long ti = System.currentTimeMillis();
		//	if (distRaced>ed.straightDist)
		//		return null;
		if (trackWidth<=0) {
			trackWidth = ed.trackWidth;
		}	

		double tW = Math.round(trackWidth)*0.5d;
		double prevTW = Math.round(ed.trackWidth)*0.5d;
		double toMiddle = Math.round((-tW*curPos)*PRECISION)/PRECISION;
		double prevToMiddle = Math.round((-prevTW*ed.curPos)*PRECISION)/PRECISION;
		double ax = toMiddle-prevToMiddle;


		double scale = tW/prevTW;		

		if (straightDist<ed.straightDist -distRaced) straightDist = ed.straightDist -distRaced;		
//		Segment[] rArr = r.elements();
//		Segment[] lArr = l.elements();	
		Vector2D[] left = this.left;
		Vector2D[] right = this.right;
		//			int len=ed.numpoint;
		//			
		//			int j = 0;
				

		if (lSize<0) lSize = 0;
		if (left!=null){ 			
		
			lSize = join(scale,ax,-distRaced, ed.left,ed.lSize,left,lSize,trArr,trIndx,trSz,-1);
			trSz= CircleDriver2.trSz;
			for (int i = 0;i<trSz;++i){
				Segment lSeg = trArr[ trIndx[i] ].leftSeg;
				if (lSeg.num==0) continue;
				if (lSeg.startIndex>=0 && lSeg.start.y>lSeg.points[lSeg.startIndex].y+SMALL_MARGIN || lSeg.endIndex>=0 && lSeg.end.y<lSeg.points[lSeg.endIndex].y-SMALL_MARGIN){
					CircleDriver2.inTurn = true;
					System.out.println();
				}
				if (lSeg.endIndex<lSize-1 && lSeg.end.y>lSeg.points[lSeg.endIndex+1].y+SMALL_MARGIN){
					System.out.println();
					CircleDriver2.inTurn = true;
				}
				
				if (lSeg.startIndex>0 && lSeg.start.y<lSeg.points[lSeg.startIndex-1].y-SMALL_MARGIN ){
					System.out.println();
					CircleDriver2.inTurn = true;
				}
			}
			for (int i=1;i<lSize;++i){
				if (left[i].y-left[i-1].y==0)
					System.out.println();
				if (left[i].y<left[i-1].y){
					System.out.println();
					turn = Segment.UNKNOWN;
					reset();
					return;
				}
			}				
		}

		if (rSize<0) rSize = 0;
		if (right!=null){					
			rSize = join(scale,ax,-distRaced,ed.right,ed.rSize, right,rSize, trArr,trIndx,trSz,1);
			trSz= CircleDriver2.trSz;
			for (int i = 0;i<trSz;++i){
				Segment rSeg = trArr[ trIndx[i] ].rightSeg;
				if (rSeg.num==0) continue;
				if (rSeg.startIndex>=0 && rSeg.start.y>rSeg.points[rSeg.startIndex].y+SMALL_MARGIN || rSeg.endIndex>=0 && rSeg.end.y<rSeg.points[rSeg.endIndex].y-SMALL_MARGIN){
					System.out.println();
					CircleDriver2.inTurn = true;
				}
			}
			for (int i=1;i<rSize;++i){
				if (right[i].y-right[i-1].y==0)
					System.out.println();
				if (right[i].y<right[i-1].y){
					System.out.println();
					reset();
					return;
				}
			}		
		}		
		
								
		Segment first = (trSz>0) ? trArr[ trIndx[0] ] : null;
		if (left!=null && right!=null && rSize>0 && lSize>0 && first.type==0 && Math.abs(first.start.x-first.end.x)<=TrackSegment.EPSILON*0.1){
			for (int j = 0;j<trSz;++j){
				Segment t = trArr[ trIndx[j] ];
				if (t.type!=0 || Math.abs(t.start.x-t.end.x)>=TrackSegment.EPSILON) break;
				Segment r0 = t.rightSeg;
				Segment l0 = t.leftSeg;
				double x0 = r0.start.x;
				double xx = l0.start.x;
				int end = l0.endIndex+1;
				for (int i = l0.startIndex;i<end;++i){
					double x = left[i].x;
					if (Math.abs(x-x0)<trackWidth-1)
						System.out.println();
					
					if (left[i].certain && Math.abs(x-xx)>=TrackSegment.EPSILON){
						reset();
						return;
					}
				}
				
				x0 = l0.start.x;
				xx = r0.start.x;
				end = r0.endIndex+1;
				if (r0.startIndex>=0){
					for (int i = r0.startIndex;i<end;++i){
						double x = right[i].x;
						if (Math.abs(x-x0)<trackWidth-1)
							System.out.println();
						
						if (right[i].certain && Math.abs(x-xx)>=TrackSegment.EPSILON){
							reset();
							return;
						}
					}
				}
			}
		}		

		Vector2D edH = ed.highestPoint;		
		if (ed!=null && edH!=null && edH.length()<MAX_DISTANCE){
			if (scale!=1.0) edH.x*=scale;
			edH.x+=ax;
			edH.y+=-distRaced;
//			at.transform(edH, edH);
		}
				
		
		Vector2D lower = (highestPoint !=null && edH!=null && highestPoint.y<edH.y) ? highestPoint : (highestPoint==null || edH==null) ? null : edH;	
		int whichL = (highestPoint !=null && edH!=null && highestPoint.y<edH.y) ? whichE : (lower==null) ? 0 : ed.whichE;
		Vector2D higher = (highestPoint !=null && edH!=null && highestPoint.y<edH.y) ? edH : (highestPoint==null) ? edH : highestPoint;
		int whichH = (highestPoint !=null && edH!=null && highestPoint.y<edH.y) ? ed.whichE : (highestPoint==null) ? ed.whichE : whichE;
		if (whichH==0 || whichL==0){
			if (lower!=null && higher!=null ) guessHighestPointEdge(lower, whichL, higher, whichH, left, lSize, right, rSize, trackWidth,out);		
			int newWhichL = out[0];
			int newWhichH = out[1];			
						
			if (whichL==0 && newWhichL!=0 && lower!=null){
				double dx = lower.x - higher.x;
				double dy = lower.y - higher.y;
				if (newWhichH!=newWhichL || (higher!=null && Math.sqrt(dx*dx+dy*dy)>MINDIST) || higher==null){
					switch (newWhichL){
					case 1:								
						rSize = join(lower, right, rSize, trArr,trIndx,trSz,1);					
						break;
					default:
						lSize = join(lower,left,lSize, trArr,trIndx,trSz,-1);
						break;
					}			
				}
			}
	
			if (whichH==0 && newWhichH!=0 && higher!=null){
				switch (newWhichH){
				case 1:
					rSize = join(higher, right, rSize, trArr,trIndx,trSz,1);
					break;
				default:
					lSize = join(higher,left,lSize, trArr,trIndx,trSz,-1);
					break;
				}
			}
		
			if (CircleDriver2.debug)System.out.println("Left num : "+lSize);
			if (CircleDriver2.debug)System.out.println("Right num : "+rSize);
			whichL = newWhichL;
			whichH = newWhichH;
			
			if (whichEdgeAhead==0){
				if (whichL!=0 && lower!=null && currentPointAhead.x==lower.x && currentPointAhead.y==lower.y)
					whichEdgeAhead = whichL;
				else if (whichH!=0 && higher!=null && currentPointAhead.x==higher.x && currentPointAhead.y==higher.y)
					whichEdgeAhead = whichH;
				else if (currentPointAhead.y!=0 || currentPointAhead.x!=0){
					if (lSize>0 && binarySearchFromTo(left, currentPointAhead, 0, lSize-1)>=0) 
						whichEdgeAhead = -1;
					else if (rSize>0 && binarySearchFromTo(right, currentPointAhead, 0, rSize-1)>=0)
						whichEdgeAhead = 1;
				}
			}
			double dx = (higher==null) ? 0 : higher.x;
			double dy = (higher==null) ? 0 : higher.y;
			if (higher!=null && Math.sqrt(dx*dx+dy*dy)<MAX_DISTANCE){
				highestPoint = higher;
				whichE = whichH;
			} else if (lower!=null && lower.length()<MAX_DISTANCE){
				highestPoint = lower;
				whichE = whichL;
			}
		}
		
		Vector2D highest = (lSize>0) ? left[lSize-1] : null;
		if (rSize>0){
			if (highest==null || highest.y<right[rSize-1].y) highest = right[rSize-1];
		}
		if (highestPoint!=null && highest.y<highestPoint.y) 
			highest = highestPoint;
		else if (highestPoint==null && ed.highestPoint!=null && highest.y<ed.highestPoint.y) highest = ed.highestPoint;
		/*if (trSz>0){
			Segment lSeg = trArr[trIndx[trSz-1]].leftSeg;
			if (lSeg.end.y>highest.y) highest = lSeg.end;
			lSeg = trArr[trIndx[trSz-1]].rightSeg;
			if (lSeg.end.y>highest.y) highest = lSeg.end;
		}//*/
		
		double_check(highest);					
		
		if (CircleDriver2.debug && highestPoint!=null)System.out.println("Highest Point : "+highestPoint+"    "+highestPoint.length());
		int nlS = lSize;
		int nrS = rSize;
		numpoint = (whichE==0 && highestPoint!=null && highestPoint.length()<MAX_DISTANCE) ? nlS+nrS+1 : nlS+nrS;
		//			Vector2D[] newArr = new Vector2D[numpoint];
		//			j = nlS;
		//			if (left!=null)
		//				System.arraycopy(left.elements(), 0, newArr, 0, j);
		//			if (whichE==0 && highestPoint!=null && highestPoint.length()<MAX_DISTANCE)
		//				newArr[j++] = highestPoint;
		//
		//			if (right!=null)
		//				System.arraycopy(right.elements(), 0, newArr, j, nrS);

		//			allPoints = ObjectArrayList.wrap(newArr, numpoint);

		//	double l0 = (left!=null && left.size()>0) ? left.get(0).x : -10000;
		//	double l1 = (left!=null && left.size()>0) ? left.get(left.size()-1).x : -10000;
		//	double r0 = (right!=null && right.size()>0) ? right.get(0).x : -10000;
		//	double r1 = (right!=null && right.size()>0) ? right.get(right.size()-1).x : -10000;
		//	boolean straight = (highestPoint!=null && highestPoint.length()>=MAX_DISTANCE);
		//	boolean nlS = Math.abs(l1-l0)>TrackSegment.EPSILON;
		//	boolean nrS = Math.abs(r1-r0)>TrackSegment.EPSILON;
		//	if (straight && (Math.abs(l1-l0)<=TrackSegment.EPSILON || Math.abs(r1-r0)<=TrackSegment.EPSILON)) 
		//		turn = MyDriver.STRAIGHT;
		//	else if ((nlS && l1>l0) || (nrS && r1>r0))
		//		turn = 1;
		//	else if ((nlS && l1<l0) || (nrS && r1<r0))
		//		turn = -1;
		//	else turn = 2;
		
		
		

		double dL = getStraightDist(left,lSize);
		double dR = getStraightDist(right,rSize);
		straightDist = Math.max(dL, dR);
		if (CircleDriver2.debug) System.out.println("K : "+(System.currentTimeMillis()-ti));
//		AffineTransform at = new AffineTransform();
//		at.scale(scale, 1);
//		at.translate(ax, -distRaced);
//		combine(scale,ax,-distRaced,ed,distRaced,trArr,trIndx,trSz);		
	}



	public final int estimateTurn(){						
		boolean all=true;
		double toRight = toRightEdge();

		Vector2D[] lArr = left;
		Vector2D[] rArr = right;
		int sL = lSize;
		int sR= rSize;
		double sum=0;
		for (int i=sL-1;i>=0;--i){
			Vector2D v = lArr[i];
			sum += v.x;
			double r = v.x+toRight;
			if (!(Math.abs(r)<=DELTA || Math.abs(r-trackWidth)<=DELTA)){
				all = false;
			} else if (r>0){
				return MyDriver.TURNRIGHT;			
			} else if (r<-trackWidth)
				return MyDriver.TURNLEFT;
		}
		double meanL = Math.round(sum/sL*10000)/10000.0d;
		sum = 0;
		for (int i=sR-1;i>=0;--i){
			Vector2D v = rArr[i];
			sum+=v.x;
			double r = v.x+toRight;
			if (!(Math.abs(r)<=DELTA || Math.abs(r-trackWidth)<=DELTA)){
				all = false;
			} else if (r>0){
				return MyDriver.TURNRIGHT;			
			} else if (r<-trackWidth)
				return MyDriver.TURNLEFT;
		}
		double meanR = Math.round(sum/sR*10000.0)/10000.0d;
		if (all)
			//			if (maxDistance>=99)
			return MyDriver.STRAIGHT;
		//		else return MyDriver.UNKNOWN;
		if (sL<=0 || sR<=0)
			return MyDriver.UNKNOWN;
		double rl = lArr[sL-1].x;
		double rr = rArr[sR-1].x;

		if (Math.abs(rl-meanL)<=DELTA && Math.abs(rr-meanR)<=DELTA){
			if (maxDistance>=99) {		
				return MyDriver.STRAIGHT;
			}
			return MyDriver.UNKNOWN;
		}
		if (meanR<=0 || rl<meanL-DELTA || meanR>rr+DELTA)
			return MyDriver.TURNLEFT;

		return MyDriver.TURNRIGHT;
	}



	public double toMiddle(){
		//	if (trackWidth<=0 || curPos<-1 || curPos>1) return Double.NaN;
		return Math.round(trackWidth/2*curPos*10000.0d)/10000.0d;
	}

	public double toLeftEdge(){
		//	if (trackWidth<=0 || curPos<-1 || curPos>1) return Double.NaN;
		return Math.round(trackWidth/2*(1+curPos)*10000.0d)/10000.0d;
	}

	public double toRightEdge(){
		//	if (trackWidth<=0 || curPos<-1 || curPos>1) return Double.NaN;
		return -Math.round(trackWidth/2*(1-curPos)*10000.0d)/10000.0d;
	}

	/**
	 * @return the trackWidth
	 */
	public double getTrackWidth() {
		return trackWidth;
	}

	/**
	 * @param trackWidth the trackWidth to set
	 */
	public void setTrackWidth(double trackWidth) {
		this.trackWidth = trackWidth;
	}

	/**
	 * @return the maxDistance
	 */
	public double getMaxDistance() {
		return maxDistance;
	}

	/**
	 * @param maxDistance the maxDistance to set
	 */
	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	double SimSteerUpdate(double oldSteer,double steer){		
		/* input control */		
		steer *= steerLock;
		double stdelta = steer - oldSteer;
		double sign = (stdelta<0) ?-1:1;

		if ( Math.abs(stdelta/DELTATIME) > MAXSTEERSPEED ){
			steer = sign * MAXSTEERSPEED * DELTATIME + oldSteer;
		};


		return steer;		
	}


	public void drawEdge(double[] x,double[] y,final String title){			
		XYSeries series = new XYSeries("Curve");

		//		for (int i=0;i<numPointLeft;++i){
		//		series.add(leftEgdeX[i], leftEgdeY[i]);
		//		}

		for (int i=0;i<x.length;++i){			
			series.add(x[i],y[i]);
		}

		//		for (int i=0;i<numPointRight;++i){
		//		series.add(rightEgdeX[i], rightEgdeY[i]);
		//		}

		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );
		chart.getXYPlot().getDomainAxis().setRange(-20.0,90.0);
		chart.getXYPlot().getRangeAxis().setRange(-20.0,100.0);

		Thread p = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					BufferedImage image = chart.createBufferedImage(600, 400);
					ImageIO.write(image, "png", new File(title+".png"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});

		p.start();

		//		ChartPanel chartPanel = new ChartPanel(chart);
		//		jf.setContentPane(chartPanel);
		//		jf.setPreferredSize(new Dimension(600,400));
		//		jf.setMinimumSize(new Dimension(600,400));
		//		jf.setVisible(true);

	}


	public static void drawEdge(ObjectList<Vector2D> vs,final String title){			
		XYSeries series = new XYSeries("Curve");

		for (Vector2D v:vs){			 
			if (v!=null) series.add(v.x,v.y);
		}

		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );
		//	chart.getXYPlot().getDomainAxis().setRange(-50.0,50.0);
		//	chart.getXYPlot().getRangeAxis().setRange(-20.0,100.0);
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

	public static void drawEdge(Vector2D[] vs,final String title){			
		XYSeries series = new XYSeries("Curve");

		for (int i=0;i<vs.length;++i){			
			series.add(vs[i].x,vs[i].y);
		}

		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );
		//	chart.getXYPlot().getDomainAxis().setRange(-50.0,50.0);
		//	chart.getXYPlot().getRangeAxis().setRange(-20.0,100.0);
		chart.getXYPlot().getDomainAxis().setRange(-60.0,60.0);
		chart.getXYPlot().getRangeAxis().setRange(-10.0,110.0);

		Thread p = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					BufferedImage image = chart.createBufferedImage(600, 400);
					ImageIO.write(image, "png", new File(title+".png"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});

		p.start();
	}

	public static void drawEdge(EdgeDetector ed,final String title){			
		XYSeries series = new XYSeries("Curve");
		Vector2D[] lArr = ed.left;
		Vector2D[] rArr = ed.right;
		int sL = ed.lSize;
		int sR= ed.rSize;			

		for (int i=sL-1;i>=0;--i){
			Vector2D v = lArr[i];
			series.add(v.x,v.y);
		}
		if (ed.whichE==0) series.add(ed.highestPoint.x,ed.highestPoint.y);
		for (int i=sR-1;i>=0;--i){
			Vector2D v = rArr[i];
			series.add(v.x,v.y);
		}
		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );
		chart.getXYPlot().getDomainAxis().setRange(-50.0,50.0);
		chart.getXYPlot().getRangeAxis().setRange(-20.0,100.0);

		Thread p = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					BufferedImage image = chart.createBufferedImage(600, 400);
					ImageIO.write(image, "png", new File(title+".png"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});

		p.start();
	}

	public static void drawEdge(XYSeries series,final String title){					
		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );		
		//	chart.getXYPlot().getDomainAxis().setRange(-200.0,200.0);
		//	chart.getXYPlot().getRangeAxis().setRange(-200.0,200.0);
		chart.getXYPlot().getDomainAxis().setRange(-60.0,60.0);
		chart.getXYPlot().getRangeAxis().setRange(-10.0,110.0);
		//		chart.getXYPlot().getDomainAxis().setRange(-5.0,5.0);
		//		chart.getXYPlot().getRangeAxis().setRange(-5.0,5.0);


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




	//		public ObjectArrayList<Vector2D> getLeftEdge(){
	//			return left;
	//		}
	//
	//		public ObjectArrayList<Vector2D> getRightEdge(){
	//			return right;
	//		}



	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
		final String TAB = "    ";

		String retValue = "";

		retValue = "EdgeDetector ( "
			+ super.toString() + TAB
			+ "whichE = " + this.whichE + TAB
			+ "currentPointAhead = " + this.currentPointAhead + TAB
			+ "cp = " + this.cp + TAB
			+ "trackWidth = " + this.trackWidth + TAB
			+ "curPos = " + this.curPos + TAB
			+ "distRaced = " + this.distRaced + TAB
			+ "curAngle = " + this.curAngle + TAB
			+ "maxY = " + this.maxY + TAB
			+ "left = " + this.left + TAB
			+ "right = " + this.right + TAB		        		        
			+ "numpoint = " + this.numpoint + TAB
			+ "maxDistance = " + this.maxDistance + TAB
			+ "leftStraight = " + this.leftStraight + TAB
			+ "rightStraight = " + this.rightStraight + TAB
			+ "straightDist = " + this.straightDist + TAB
			+ "highestPoint = " + this.highestPoint + TAB
			+ "turn = " + this.turn + TAB
			+ "center = " + this.center + TAB
			+ "radiusL = " + this.radiusL + TAB
			+ "radiusR = " + this.radiusR + TAB
			+ " )";

		return retValue;
	}


}
