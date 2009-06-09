/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import cern.colt.Sorting;

import com.graphbuilder.geom.Geom;

/**
 * @author kokichi3000
 *
 */
public final class Segment {
	final static int LIM = 6;
	final static int UNKNOWN = -2;
	final static double MARGIN =3;
	final static int INIT_SIZE = 20;
	final static int MAX_RADIUS =1001;
	final static double REJECT_VALUE = 10;
	static int mark =0 ;
	static double[] temp = new double[10];
	TrackSegment seg = null;
	double dist = -1;
	public Vector2D center = null;//estimated center of the segment
	public Vector2D start = null;
	public Vector2D end = null;
	public int minR = MAX_RADIUS;
	public int maxR = 0;
	double length = -1;
	double arc = 0;
	int type=-2;
	double radius = 0;
	int num = 0;
	int size = 0;
	//	int[] keys = null;
	public Vector2D[] points = null;// only use when in a straight segment, otherwise null	
	boolean sorted = true;
	public int[] map = null;
	public int[] mapR = null;	

	public final static Comparator<Vector2D> comp = new Comparator<Vector2D>(){		
		public int compare(Vector2D o1, Vector2D o2) {
			return (Math.abs(o1.y-o2.y)<=1e-6) ? 0 : (o1.y<o2.y) ? -1 : 1;
		};
	};
	/**
	 * @param args
	 */

	public final static int double2int(double r){
		return (r>=MAX_RADIUS) ? MAX_RADIUS-1 : (r<=0) ? 0 : (int)(Math.round(r));		
	}

	public final static double int2double(int r){
		return (r==MAX_RADIUS-1) ? Double.MAX_VALUE : r;		
	}

	public Segment(){		
	}

	public Segment(TrackSegment ts){
		if (ts==null) return;
		seg = ts;
		dist = ts.distanceFromLocalOrigin;
		center = new Vector2D(ts.centerx,ts.centery);
		start = new Vector2D(ts.startX,ts.startY);
		end = new Vector2D(ts.endX,ts.endY);
		length = ts.length;
		type = ts.type;
		arc = ts.arc;
		radius = ts.radius;
		map = new int[MAX_RADIUS];		
		if (radius<MAX_RADIUS-1){
			int rr = double2int(radius);
			map[rr] = 1;
			minR = rr;
			maxR = rr;
		} else {
			map[MAX_RADIUS-1] = 1;
			minR = MAX_RADIUS-1;
			maxR = MAX_RADIUS-1;
		}
	}

	public Segment(TrackSegment ts,double offset){
		if (ts==null) return;
		seg = ts;
		dist = ts.distanceFromLocalOrigin;
		center = new Vector2D(ts.centerx,ts.centery);
		start = new Vector2D(ts.startX,ts.startY);
		end = new Vector2D(ts.endX,ts.endY);
		length = ts.length;
		type = ts.type;
		arc = ts.arc;
		radius = ts.radius;
		map = new int[MAX_RADIUS];		
		if (radius<MAX_RADIUS-1){
			if (type!=Segment.UNKNOWN){
				int rr = double2int(radius-type*offset);
				map[rr] = 1;
				minR = rr;
				maxR = rr;
			}
		} else {
			map[MAX_RADIUS-1] = 1;
			minR = MAX_RADIUS-1;
			maxR = MAX_RADIUS-1;
		}
	}


	public Segment(Segment s){
		seg = s.seg;
		dist = s.dist;
		center = (s.center==null) ? null :new Vector2D(s.center);
		start = (s.start==null) ? null :new Vector2D(s.start);
		end = (s.end==null) ? null :new Vector2D(s.end);
		length = s.length;
		type = s.type;
		arc = s.arc;
		radius = s.radius;
		minR = s.minR;
		maxR = s.maxR;
		size = s.size;
		if (s.map!=null){
			map = new int[MAX_RADIUS];
			if (minR<=maxR) System.arraycopy(s.map, minR, map, minR, maxR-minR+1);
		}
	}		


	//not copy Points
	public final void copy(Segment s){
		seg = s.seg;
		dist = s.dist;
		center = (s.center==null) ? null :new Vector2D(s.center);
		start = (s.start==null) ? null :new Vector2D(s.start);
		end = (s.end==null) ? null :new Vector2D(s.end);
		length = s.length;
		type = s.type;
		arc = s.arc;
		radius = s.radius;
		minR = s.minR;
		maxR = s.maxR;
		size = s.size;
		map = s.map;
		num = s.num;
		points = s.points;
//		if (s.map!=null){
//			if (map==null) map = new int[MAX_RADIUS];
//			if (maxR>=minR) System.arraycopy(s.map, minR, map, minR, maxR-minR+1);
//		} else map = null;
	}


	public final void copy(TrackSegment ts){
		seg = ts;
		if (ts==null) return;
		dist = ts.distanceFromLocalOrigin;
		center = new Vector2D(ts.centerx,ts.centery);
		start = new Vector2D(ts.startX,ts.startY);
		end = new Vector2D(ts.endX,ts.endY);
		length = ts.length;
		type = ts.type;
		arc = ts.arc;
		radius = ts.radius;
		if (map==null) map = new int[MAX_RADIUS];
		map[double2int(radius)]++;
		if (type!=Segment.UNKNOWN){
			int rr = (int)Math.round(radius);
			if (minR>rr) minR = rr;
			if (maxR<rr) maxR = rr;
		}
	}
	
	public final void copy(TrackSegment ts,double offset){
		seg = ts;
		if (ts==null) return;
		dist = ts.distanceFromLocalOrigin;
		center = new Vector2D(ts.centerx,ts.centery);
		start = new Vector2D(ts.startX,ts.startY);
		end = new Vector2D(ts.endX,ts.endY);
		length = ts.length;
		type = ts.type;
		arc = ts.arc;
		radius = ts.radius;
		if (map==null) map = new int[MAX_RADIUS];
		map[double2int(radius)]++;		
		if (type!=Segment.UNKNOWN){
			int rr = double2int(radius-type*offset);
			map[rr]++;
			if (minR>rr) minR = rr;
			if (maxR<rr) maxR = rr;
		} else 
			map[MAX_RADIUS-1]++;										
	}


	/*public static Double2IntMap joinMap(Double2IntMap m1,Double2IntMap m2){
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


	public final void sortPoints(){
		if (!sorted){
			Arrays.quicksort(points,0,size-1,comp);
			sorted = true;
		}
	}

	public final static void sortPoints(ObjectArrayList<Vector2D> points){		
		Arrays.quicksort(points.elements(),0,points.size()-1,comp);		
	}

	public final void insert(int index,Vector2D p){
		System.arraycopy(points, index, points, index+1, num-index);
		points[index] = p;
	}

	@SuppressWarnings("unchecked")
	public static int binarySearchFromTo(Object[] list, Object key, int from, int to, java.util.Comparator comparator) {
		Object midVal;
		while (from <= to) {
			int mid =(from + to)/2;
			midVal = list[mid];
			int cmp = comparator.compare(midVal,key);

			if (cmp < 0) from = mid + 1;
			else if (cmp > 0) to = mid - 1;
			else return mid; // key found
		}
		return -(from + 1);  // key not found.
	}


	public final int contains(Vector2D p){
		if (!sorted) {						
			for (int i=0;i<size;++i)
				if (points[i].equals(p)) return i;
			return -size-1;
		}
		return binarySearchFromTo(points, p, 0, num-1, comp);		
	}

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


	public final void removePoint(Vector2D p){
		if (num<=0 || points==null) return;

		int index = Sorting.binarySearchFromTo(points, p, 0, num-1, comp);
		if (index<0) return;		

		if (index>=0){			
			System.arraycopy(points, index+1, points, index, num-index);			
			num--;


			if (num<3){
				type = -2;
				length = -1;
				arc = 0;
				radius = 0;
				seg = null;
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
	}


	public final void reCalLength(){
		if (type==UNKNOWN) return;
		if (start.equals(end)){
			length = 0;
			return;
		}
		if (type==0){
			seg.startX = start.x;
			seg.startY = start.y;
			seg.endX = end.x;
			seg.endY = end.y;			 
			seg.length = start.distance(end);			
		} else if (type!=Segment.UNKNOWN){
			TrackSegment ts = TrackSegment.createTurnSeg(center.x, center.y, radius, start.x, start.y, end.x, end.y);
			seg = ts;				
			if (ts!=null) type = ts.type;
		}
		length = (seg==null) ? 0 : seg.length;
	}

	//Point must belong to seg
	public final void addPoint(Vector2D p){
		if (num<=0 || points==null){
			points = new Vector2D[INIT_SIZE];			
		} else if (num>=points.length){
			Vector2D[] tmp = new Vector2D[num*2];
			System.arraycopy(points, 0, tmp, 0, points.length);
			points = tmp;			
		}

		if (num>=LIM && p.y>=start.y && p.y<=end.y) return;
		if (num>=LIM && p.y<start.y){
			start = new Vector2D(p);
			points[0] = p;
			return;
		}

		if (num>=LIM && p.y>end.y){
			end = new Vector2D(p);
			points[num-1] = p;
			return;
		}

		int index = Sorting.binarySearchFromTo(points, p, 0, num-1, comp);
		if (index>=0) return;
		index = -index - 1;
		if (index>=num) {
			points[num] = p ;
		} else {
			System.arraycopy(points, index, points, index+1, num-index);
			points[index] = p;
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
				Vector2D n = new Vector2D(dx,dy).orthogonal();
				Vector2D point = null;
				double[] r = new double[3];
				if (Math.abs(dx)<=TrackSegment.EPSILON){					
					if (dy==0){
						Geom.getLineLineIntersection(start.x, start.y, end.x, start.y+1, p.x, p.y, p.x-1, p.y, r);
					} else Geom.getLineLineIntersection(start.x, start.y, end.x, end.y, p.x, p.y, p.x-1, p.y, r);					
				} else Geom.getLineLineIntersection(start.x, start.y, end.x, end.y, p.x, p.y, p.x+n.x, p.y+n.y, r);					

				if (r!=null && r.length>2) point = new Vector2D(r[0],r[1]);
				if (point!=null) {
					start = new Vector2D(point);
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
				Vector2D n = new Vector2D(dx,dy).orthogonal();
				Vector2D point = null;
				double[] r = new double[3];
				if (Math.abs(dx)<=TrackSegment.EPSILON)	{
					start.x = (start.x+end.x)/2;
					end.x = start.x;
					if (dy==0){
						Geom.getLineLineIntersection(start.x, start.y, end.x, start.y+1, p.x, p.y, p.x-1, p.y, r);
					} else Geom.getLineLineIntersection(start.x, start.y, end.x, end.y, p.x, p.y, p.x-1, p.y, r);					
				} else Geom.getLineLineIntersection(start.x, start.y, end.x, end.y, p.x, p.y, p.x+n.x, p.y+n.y, r);					

				if (r!=null && r.length>2) point = new Vector2D(r[0],r[1]);
				if (point!=null){
					if (Math.abs(dx)<=TrackSegment.EPSILON)
						start.x = end.x;
					end = new Vector2D(point);
				}
			}
		}

		if (changed){
			//			if (type==0) points.add(p); 
			reCalLength();
		}		
	}

	public boolean isPointInSegment(Vector2D point){		
		//		double minx = Math.min(start.x, end.x);
		double miny = Math.min(start.y, end.y);
		//		double maxx = Math.max(start.x, end.x);
		double maxy = Math.max(start.y, end.y);
		//		return (point.x>=minx && point.x<=maxx && point.y>=miny && point.y<=maxy);
		return (point.y>=miny && point.y<=maxy);
	}

	public final boolean isPointBelongToSeg(Vector2D point){
		if (type==Segment.UNKNOWN) return true;

		if (type==0){			
			//			if (isCirle && Math.sqrt(r[2])<TrackSegment.MAXRADIUS) return false;
			double dx = end.x - start.x;
			if (Math.abs(dx)<=4*TrackSegment.EPSILON)
				if (point.y<=end.y && point.y>=start.y) return true;

			double d = Geom.ptLineDistSq(start.x, start.y, end.x, end.y, point.x, point.y, null); 
			if (d>=16*TrackSegment.EPSILON*TrackSegment.EPSILON) return false;
		} else {
			//			double[] r = new double[3];
			//			boolean isCirle = Geom.getCircle(start.x, start.y, end.x, end.y, point.x, point.y, r);
			//			if (!isCirle) return false;
			//			if (Math.abs(Math.sqrt(r[2])-radius)>1) return false;			
			double d = Math.abs(point.distance(center)-radius);
			if (d>=4*TrackSegment.EPSILON) return false;
		}
		return true;
	}

	public final static Segment toMiddleSegment(Segment seg,int which,double tW){
		if (seg.type==Segment.UNKNOWN) return null;
		double t = (seg.type==0) ? -tW*which : tW*which*seg.type;
//		Segment s = (seg.seg!=null) ? new Segment(seg.seg,-which*tW) : new Segment();
		Segment s = new Segment();
		s.copy(seg);
		if (seg.type==0){									
			s.start.x += t;
			s.end.x += t;
			s.dist = seg.dist;
			if (seg.seg!=null){
				seg.seg.startX += t;
				seg.seg.endX += t;
			}
//			if (s.points!=null){
//				for (int i = 0;i<s.num;++i){
//					if (s.points[i]!=null) s.points[i].x+=t;					
//				}
//			}
			s.points = null;
		} else {
			TrackSegment ts = seg.seg;
			if (ts==null)
				ts = TrackSegment.createTurnSeg(seg.center.x, seg.center.y, seg.radius+t, seg.start.x, seg.start.y, seg.end.x, seg.end.y);
			int rad = (int)Math.round(seg.radius + t);
			s.start = s.center.plus(s.start.minus(s.center).normalised().times(rad));
			s.end = s.center.plus(s.end.minus(s.center).normalised().times(rad));
			s.radius = rad;
			s.arc = ts.arc;
			s.length = Math.abs(s.arc * rad);
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
			//						s.map[double2int(i+t)] = n;
			//						s.map[i] = 0;
			//					}
			//				}
			//			} else {
			//				for (int i = s.minR	;i<=s.maxR;++i){
			//					int n = s.map[i]; 
			//					if (i!=MAX_RADIUS-1 &&n!=0) {
			//						s.map[double2int(i+t)] = n;
			//						s.map[i] = 0;
			//					}
			//				}
			//			}			
			//			s.minR = (int)(s.minR+t);
			//			s.maxR = (int)(s.maxR+t);
		}
		return s;
	}

	public final static Segment toSideSegment(Segment seg,int which,double tW){
		if (seg.type==Segment.UNKNOWN) return null;
		double t = (seg.type==0) ? tW*which : -tW*which*seg.type;
//		Segment s = (seg.seg!=null) ? new Segment(seg) : new Segment();
		Segment s = new Segment();
		s.copy(seg);
		if (seg.type==0){									
			s.start.x += t;
			s.end.x += t;
			s.dist = seg.dist;
			if (seg.seg!=null){
				seg.seg.startX += t;
				seg.seg.endX += t;
			}
			s.points = null;
//			if (s.points!=null){
//				for (int i = 0;i<s.num;++i){
//					if (s.points[i]!=null) s.points[i].x+=t;					
//				}
//			}
		} else {
			TrackSegment ts = seg.seg;
			if (ts==null)
				ts = TrackSegment.createTurnSeg(seg.center.x, seg.center.y, seg.radius+t, seg.start.x, seg.start.y, seg.end.x, seg.end.y);
			double rad = Math.round(seg.radius) + t;
			s.start = s.center.plus(s.start.minus(s.center).normalised().times(rad));
			s.end = s.center.plus(s.end.minus(s.center).normalised().times(rad));
			s.radius = rad;
			s.arc = ts.arc;
			s.length = Math.abs(s.arc * rad);
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
	public final static double distance(Vector2D p1,Vector2D p2,Vector2D center,double radius){
		Vector2D v1 = new Vector2D(p1.x-center.x,p1.y-center.y);
		Vector2D v2 = new Vector2D(p2.x-center.x,p2.y-center.y);

		double angle = Vector2D.angle(v1, v2);
		if (angle<-Math.PI) 
			angle += 2*Math.PI;
		else if (angle>Math.PI) 
			angle -= 2*Math.PI;

		return radius*Math.abs(angle);
	}

	//try to combine a list of segment with the same radius
	public final static void adjust(List<Segment> l){
		if (l==null || l.size()==0) return;
		for (int i=0;i<l.size();++i){
			Segment s = l.get(i);
			Segment t = (i<l.size()-1) ? l.get(i+1) : null;
			while (t!=null && (s.type!=Segment.UNKNOWN && s.type==t.type && Math.abs(s.radius-t.radius)<1)){
				if (s.type==0 && s.end!=null && t.end!=null){
					double dx = s.end.x - s.start.x;
					double ddx = t.end.x - t.start.x;
					if (Math.abs(dx)<=TrackSegment.EPSILON && Math.abs(ddx)>TrackSegment.EPSILON) break;
					if (Math.abs(dx)>TrackSegment.EPSILON && Math.abs(ddx)<=TrackSegment.EPSILON) break;
					if (Math.abs(dx)>TrackSegment.EPSILON && Math.abs(ddx)>TrackSegment.EPSILON){
						double dy = s.end.y - s.start.y;
						double ddy = t.end.y - t.start.y;
						if (Math.abs(Math.atan2(dy, dx)-Math.atan2(ddy, ddx))>0.1) break;
					}
				}
				l.remove(i+1);		

				if (s.points==null && t.points!=null) {					
					s.points = t.points;
				} else if (s.points!=null && t.points!=null){
					int sz = s.num+t.num;
					if (sz>s.length) {
						Vector2D[] tmp = new Vector2D[sz*2];
						System.arraycopy(s.points, 0, tmp, 0, s.num);
						s.points = tmp;
					}
					System.arraycopy(t.points, 0, s.points, s.num, t.num);
					s.num += t.num;
				}				

				if (s.points!=null){
					if (s.type!=0 && s.num>0 ){					
						s.start = new Vector2D(s.points[0]);
						s.end = new Vector2D(s.points[s.num-1]);
						s.reCalLength();
						s.arc = s.type*s.length/s.radius;
					} else if (s.num>0){
						double dy = s.end.y-s.start.y;
						double dx = s.end.x-s.start.x;										
						s.start = new Vector2D(s.points[0]);
						s.end = new Vector2D(s.points[s.num-1]);
						if (Math.abs(dx)<=TrackSegment.EPSILON){
							s.start.x = (s.start.x+s.end.x)*0.5;
							s.end.x = s.start.x;
						} else {
							double tmp = dy/dx;						
							LineFitter lf = new LineFitter(new double[]{tmp,s.start.y-tmp*s.start.x},s.points,0,s.num-1);
							lf.fit();
							double a = lf.getA();
							double b = lf.getB();
							s.start.y = a*s.start.x+b;
							s.end.y = a*s.end.x+b;												
						}
						s.reCalLength();
					}
				}

				if (i==l.size()-1) return;
				t = l.get(i+1);								
			}					
		}
	}

	public final static void estimateDist(Segment s,Segment t){
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

	public final void reCalculate(double tW){
		if (type==Segment.UNKNOWN)
			return;		
		Vector2D[] v = points;		
		if (type==0){
			int len = num;						
			if (len<1) return;
			double dx = v[len-1].x-v[0].x;			
			if (Math.abs(dx)<=TrackSegment.EPSILON)	return;			
			double dy = v[len-1].y-v[0].y;
			double tmp = dy/dx;
			LineFitter lf = new LineFitter(new double[]{tmp,start.y-tmp*start.x},v,0,len-1);
			lf.fit();
			double a = lf.getA();
			double b = lf.getB();
			TrackSegment ts = TrackSegment.createStraightSeg(0, start.x, a*start.x+b, end.x, a*end.x+b);
			seg = ts;
			length = ts.length;
			arc = ts.arc;			
			if (map!=null)
				map[MAX_RADIUS-1]++;

			return;
		}
		if (num<3) return;

		double xx=v[0].x;
		double yy=v[0].y;
		TrackSegment ts = null;
		int i = 0;		

		double[] result = new double[3];
		double x1 = xx;
		double x2 = v[i+1].x;			
		double y1 = yy;
		double y2 = v[i+1].y;
		double x3 = v[i+2].x;
		double y3 = v[i+2].y;
		int len = num;
		boolean isCircle = Geom.getCircle(x1, y1, x2, y2, x3, y3, result);
		double radius = (isCircle) ? Math.sqrt(result[2]) : Double.MAX_VALUE;
		result[2] = radius;		
		if (v.length>3){
			CircleFitter cf = new CircleFitter(result.clone(),v,0,len-1);
			cf.fit();
			radius = cf.getEstimatedRadius();						
		}
		radius = Math.round(radius-type*tW)+type*tW;
		Vector2D t = new Vector2D(x1+x2,y1+y2).times(0.5d);
		Vector2D q = new Vector2D(result[0],result[1]);
		double d = t.distanceSq(v[i]);
		Vector2D p = t.plus(q.minus(t).normalised().times(Math.sqrt(radius*radius-d)));

		ts = TrackSegment.createTurnSeg(dist, p.x, p.y, radius, x1, y1, end.x, end.y,x2,y2);
		center = p;
		seg = ts;
		length = ts.length;
		arc = ts.arc;
		this.radius = radius;
		if (map!=null)
			if (radius>=MAX_RADIUS) 
				map[MAX_RADIUS-1]++;
			else map[(int)Math.round(radius-type*tW)]++;

	}



	public final void transform(final AffineTransform at){		
		if (at==null) return;
		if (start!=null) {
			at.transform(start, start);
			if (seg!=null){
				seg.startX = start.x;
				seg.startY = start.y;
			}			
		}
		if (end!=null) {
			at.transform(end, end);
			if (seg!=null){
				seg.endX = end.x;
				seg.endY = end.y;
			}
		}
		if (type!=0 &&type!= UNKNOWN && center!=null)
			at.transform(center, center);
		if (points!=null){						
			for (int i=0;i<num;++i){
				Vector2D v = points[i];
				if (v!=null) at.transform(v, v);
			}
		}
	}

	public final static ObjectList<Segment> segmentize(ObjectArrayList<Vector2D> v,double tW){		
		return segmentize(v.elements(),0,v.size(), tW);		
	}

	public final static void updateRs(ObjectList<Segment> rs,int l,double tW){		
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
					last.removePoint(point);
					if (last.num<3) last.type = Segment.UNKNOWN;					
					if (last.type==Segment.UNKNOWN) last.radius = 0;
					s.addPoint(point);		
					if (last.type!=Segment.UNKNOWN){
						double oldr = last.radius;
						last.reCalculate(tW);
						if (Math.abs(oldr-last.radius)>0.5) updateRs(rs, l+1,tW);
					}

				}
				if (last.num<=0) rs.remove(l+1);								
			}
		}
	}	

	public final static void checkRs(ObjectList<Segment> rs,int l,double tW){		
		if (l>=1 && l<rs.size()){
			Segment s = rs.get(l-1);
			if (l==1 && s.type==0) return;
			Segment last = rs.get(l);
			if ((s.num<=3 || s.type==Segment.UNKNOWN) && last.type!=Segment.UNKNOWN){					
				Vector2D[] points = s.points;
				int len = s.num;
				int index = len-1;
				while (index>=0){
					Vector2D point = points[index--];
					if (index==0 && l==1 && last.type==0) break;
					if (!last.isPointBelongToSeg(point)) break;						
					s.removePoint(point);
					if (s.num<3) s.type = Segment.UNKNOWN;					
					if (s.type==Segment.UNKNOWN) s.radius = 0;
					last.addPoint(point);							
				}
				if (s.type!=Segment.UNKNOWN){
					double oldr = s.radius;
					s.reCalculate(tW);
					if (Math.abs(oldr-last.radius)>0.5) checkRs(rs, l-1,tW);
				}

				if (s.num<=0) rs.remove(l-1);								
			}
		}	
	}

	public final static void lastCheck(ObjectList<Segment> rs,double tW){
		int i=0;
		boolean found = false;
		for (i=0;i<rs.size()-1;++i){
			Segment s = rs.get(i);

			if (s.type!=Segment.UNKNOWN)	
				continue;

			Segment next = rs.get(i+1);
			while (next!=null && next.type==Segment.UNKNOWN){							
				for (int kk = 0 ; kk<next.num;++kk) {
					s.addPoint(next.points[kk]);
				}
				rs.remove(i+1);
				found = true;
				if (i>=rs.size()-1) break;
				next = rs.get(i+1);
			}			
		}
		if (found)
			for (i=0;i<rs.size();++i){
				Segment s = rs.get(i);
				if (s.type==Segment.UNKNOWN && s.num>=3){
					Vector2D[] l = s.points;
					ObjectList<Segment> ol = segmentize(l,0,s.num, tW);
					if (ol.size()==1){
						s.copy(ol.get(0).seg);
					} else {
						rs.remove(i);
						rs.addAll(i, ol);
					}
				}
			}

	}

	public final static ObjectList<Segment> segmentize(Vector2D[] v,int from, int to,double tW){
		ObjectList<Segment> rs = new ObjectArrayList<Segment>();
		int len = to - from;
		if (v==null || len<=2){
			Segment s = new Segment();
			for (int i=from;i<to;++i) s.addPoint(v[i]);		
			rs.add(s);
			return rs;
		}		

		int i=0;
		final double allowedDist = TrackSegment.EPSILON*TrackSegment.EPSILON;
		double x0 = v[0+from].x;
		for (i = 1+from;i<to;++i){
			if (Math.abs(v[i].x-x0)>=0.1*TrackSegment.EPSILON) break;
		}
		if (i>1+from){
			TrackSegment ts = TrackSegment.createStraightSeg(0, x0, v[0].y, x0, v[i-1].y);
			Segment s = new Segment(ts);
			for (int jj=0;jj<i;++jj)
				s.addPoint(v[jj]);
			rs.add(s);
			if (i==len) return rs;
		} else i = 0+from;
		double xx=v[i].x;
		double yy=v[i].y;
		TrackSegment ts = null;
		while (i<=to-1){
			if (i>=to-2){
				Segment last = (rs.size()>0) ? rs.get(rs.size()-1) :null;
				if (last!=null &&  last.type==Segment.UNKNOWN) rs.remove(rs.size()-1);
				Segment s = (last!=null && last.type==Segment.UNKNOWN) ? last :  new Segment();
				for (int k=i;k<to;++k) s.addPoint(v[k]);
				rs.add(s);
				lastCheck(rs,tW);
				break;
			}

			double[] result = new double[3];
			double x1 = xx;
			double x2 = v[i+1].x;			
			double y1 = yy;
			double y2 = v[i+1].y;
			double x3 = v[i+2].x;
			double y3 = v[i+2].y;
			boolean isCircle = Geom.getCircle(x1, y1, x2, y2, x3, y3, result);
			double radius = (isCircle) ? Math.sqrt(result[2]) : Double.MAX_VALUE;
			int j=i+3;			
			if (!isCircle || radius>TrackSegment.MAXRADIUS){//is a straight line
				if (rs.size()==0 && len>3){					
					Segment s = new Segment();					
					s.addPoint(v[i]);
					rs.add(s);
					i++;
					xx = v[i].x;
					yy = v[i].y;	
					continue;
				}
				for (j=i+3;j<to;++j){
					double x = v[j].x;
					double y = v[j].y;

					if (Geom.ptLineDistSq(x1, y1, x2, y2, x, y, null)>allowedDist)
						break;					
				}
				if (j>to) break;

				xx = v[j-1].x;
				yy = v[j-1].y;		

				double dx = xx-x1;
				double dy = yy-y1;
				if (Math.abs(dx)<TrackSegment.EPSILON){			
					xx = x1;
				} else {
					double tmp = dy/dx;
					LineFitter lf = new LineFitter(new double[]{tmp,yy-xx*tmp},v,i,j-1);
					lf.fit();
					double a = lf.getA();
					double b = lf.getB();
					y1 = a*x1+b;
					yy = a*xx+b;
				}

				ts = TrackSegment.createStraightSeg(0, x1, y1, xx, yy);
				Segment s = new Segment(ts);
				for (int jj=i;jj<j;++jj)
					s.addPoint(v[jj]);
				rs.add(s);			
				checkRs(rs,rs.size()-1,tW);
				if (j>=to) break; 
				xx = v[j].x;
				yy = v[j].y;						
			} else {
				int r = (int)Math.round(radius-tW);

				Vector2D t = new Vector2D(x1+x2,y1+y2).times(0.5d);
				Vector2D q = new Vector2D(result[0],result[1]);
				double d = t.distanceSq(v[i]);
				if (i>to-3) return rs;
				radius = r+tW;

				if (radius*radius<d){
					Segment last = (rs.size()>0) ? rs.get(rs.size()-1) :null;
					Segment s = (last!=null && last.type==Segment.UNKNOWN) ? last :  new Segment();
					if (last!=null &&  last.type==Segment.UNKNOWN) rs.remove(rs.size()-1);
					s.addPoint(v[i]);
					rs.add(s);							
					i++;
					xx = v[i].x;
					yy = v[i].y;	
					continue;
				}

				if (i==to-3){					
					Vector2D p = t.plus(q.minus(t).normalised().times(Math.sqrt(radius*radius-d)));
					ts = TrackSegment.createTurnSeg(0, p.x, p.y, radius, x1, y1, x3, y3,x2,y2);
					Segment s = new Segment(ts,tW);
					for (int jj=i;jj<to;++jj)
						s.addPoint(v[jj]);
					rs.add(s);
					checkRs(rs,rs.size()-1,tW);
					lastCheck(rs,tW);
					return rs;
				}

				Vector2D p = t.plus(q.minus(t).normalised().times(Math.sqrt(radius*radius-d)));
				double ox = p.x;
				double oy = p.y;

				for (j=i+1;j<to;++j){
					double dx = v[j].x-ox;
					double dy = v[j].y-oy;						
					//					System.out.print(Math.abs(Math.sqrt(dx*dx+dy*dy)-radius)+"    ");
					if (Math.abs(Math.sqrt(dx*dx+dy*dy)-radius)>=TrackSegment.EPSILON)						
						break;			
				}												

				if (j<=i+2) {
					Segment last = (rs.size()>0) ? rs.get(rs.size()-1) :null;
					if (last!=null &&  last.type==Segment.UNKNOWN) rs.remove(rs.size()-1);
					Segment s = (last!=null && last.type==Segment.UNKNOWN) ? last :  new Segment();
					s.addPoint(v[i]);
					rs.add(s);
					i++;
					xx = v[i].x;
					yy = v[i].y;	
					continue;
				}

				if (j==i+3){
					if (j>to-3){
						if (v[i].distance(v[i+1])>v[i+2].distance(v[i+3])){
							Segment last = (rs.size()>0) ? rs.get(rs.size()-1) :null;
							Segment s = (last!=null && last.type==Segment.UNKNOWN) ? last :  new Segment();
							if (last!=null &&  last.type==Segment.UNKNOWN) rs.remove(rs.size()-1);
							s.addPoint(v[i]);
							rs.add(s);							
							i++;
							xx = v[i].x;
							yy = v[i].y;	
							continue;
						}							
					}
				}

				if (j>=i+4){					
					x1 = xx;
					x2 = v[i+1].x;			
					y1 = yy;
					y2 = v[i+1].y;
					x3 = v[i+3].x;
					y3 = v[i+3].y;
					isCircle = Geom.getCircle(x1, y1, x2, y2, x3, y3, result);
					double rr = Math.round(Math.sqrt(result[2])-tW)+tW;					
					if (Math.abs(radius-rr)>3){
						Segment last = (rs.size()>0) ? rs.get(rs.size()-1) :null;
						Segment s = (last!=null && last.type==Segment.UNKNOWN) ? last :  new Segment();
						if (last!=null &&  last.type==Segment.UNKNOWN) rs.remove(rs.size()-1);
						s.addPoint(v[i]);
						rs.add(s);
						i++;
						xx = v[i].x;
						yy = v[i].y;	
						continue;
					}
					if (Math.abs(radius-rr)>0.5){					
						result[0] = ox;
						result[1] = oy;
						result[2] = radius;						
						CircleFitter cf = new CircleFitter(result.clone(),v,i,j-1);
						cf.fit();
						double oldr = radius;
						radius = Math.round(cf.getEstimatedRadius()-tW)+tW;
						if (Math.abs(oldr-radius)>2){
							Segment last = (rs.size()>0) ? rs.get(rs.size()-1) :null;
							Segment s = (last!=null && last.type==Segment.UNKNOWN) ? last :  new Segment();
							if (last!=null &&  last.type==Segment.UNKNOWN) rs.remove(rs.size()-1);
							s.addPoint(v[i]);
							rs.add(s);
							i++;
							xx = v[i].x;
							yy = v[i].y;	
							continue;
						}
						if (Math.abs(oldr-radius)>0.5){
							p = t.plus(q.minus(t).normalised().times(Math.sqrt(radius*radius-d)));
							ox = p.x;
							oy = p.y;
							for (j=i+1;j<to;++j){
								double dx = v[j].x-ox;
								double dy = v[j].y-oy;						
								if (Math.abs(Math.sqrt(dx*dx+dy*dy)-radius)>=0.01d)						
									break;			
							}					
						}						
					}
				}

				xx = v[j-1].x;
				yy = v[j-1].y;
				ts = TrackSegment.createTurnSeg(0, ox, oy, radius, x1, y1, xx, yy,x2,y2);
				Segment s = new Segment(ts,tW);
				for (int jj=i;jj<j;++jj)
					s.addPoint(v[jj]);
				rs.add(s);
				checkRs(rs,rs.size()-1,tW);
				if (j<to){
					xx = v[j].x;
					yy = v[j].y;
				}
			}

			i = j;
		}//end of while
		lastCheck(rs,tW);
		return rs;
	}
	
	public final static int expandBackward(final Vector2D[] v,int from,int to,double tW,Vector2D center,double rad,Segment prev,Segment next,Segment s,int firstIndex,Vector2D last,int[] marks,int n){			
		Vector2D first = null;
		double r = 0;
		if (firstIndex>-1){
			boolean ok = false;
			for (int k=firstIndex-1;k>=from;--k){
				if (marks[k]==0){
					if (s.type!=0 && Math.abs(v[k].distance(center)-rad)<=TrackSegment.EPSILON*4){
						ok = true;
						marks[k] = n;			
						first = v[k];
						firstIndex = k;
					} else if (s.type==0 && Geom.ptLineDistSq(s.start.x, s.start.y, s.end.x, s.end.y, v[k].x, v[k].y, null)<=TrackSegment.EPSILON*16*TrackSegment.EPSILON){
						ok = true;
						marks[k] = n;					
						first = v[k];
						firstIndex = k;
					} else if (s.type!=0 && prev!=null && prev.type==0 && n==2 && last!=null){
						double x0 = prev.start.x;
						Geom.getCircle2(v[k], last, new Vector2D(x0,0), new Vector2D(x0,1), temp);					
						temp[2] = Math.sqrt(temp[2]);
						r = Math.round(temp[2]-s.type*tW)+s.type*tW;
						if (Math.round(r-s.type*tW)==Math.round(rad-s.type*tW)){
							ok = true;
							marks[k] = n;						
							first = v[k];
							firstIndex = k;
						} 
					} 							
				} else if (marks[k]<n) break;
				if (!ok) break;
				ok = false;
			}
		}
		if (first!=null && s.start.y>first.y) s.start = first;
		return firstIndex;
	}
	
	public final static int expandForward(final Vector2D[] v,int from,int to,double tW,Vector2D center,double rad,Segment prev,Segment next,Segment s,int lastIndex,Vector2D first,int[] marks,int n){
		Vector2D last = null;			
		double r = 0;
		if (lastIndex>-1){
			boolean changeNext = false;
			boolean ok = false;
			for (int k=lastIndex+1;k<to;++k){
				if (marks[k]==0){
					if (s.type!=0 && Math.abs(v[k].distance(center)-rad)<=TrackSegment.EPSILON*4){
						ok = true;
						marks[k] = n;
						last = v[k];
						if (next!=null && last.y>=next.start.y){
							changeNext = true;
							next.start = new Vector2D(last);
						}
						lastIndex = k;
					} else if (s.type==0 && Geom.ptLineDistSq(s.start.x, s.start.y, s.end.x, s.end.y, v[k].x, v[k].y, null)<=TrackSegment.EPSILON*16*TrackSegment.EPSILON){
						ok = true;
						marks[k] = n;						
						last = v[k];
						if (next!=null && last.y>=next.start.y){
							changeNext = true;
							next.start = new Vector2D(last);
						}
						lastIndex = k;
					} else if (first!=null && s.type!=0 && prev!=null && prev.type==0 && n==2 ){						
						double x0 = prev.start.x;
						Geom.getCircle2(first, v[k], new Vector2D(x0,0), new Vector2D(x0,1), temp);					
						temp[2] = Math.sqrt(temp[2]);
						r = Math.round(temp[2]-s.type*tW)+s.type*tW;
						if (Math.round(r-s.type*tW)==Math.round(rad-s.type*tW)){
							ok = true;
							marks[k] = n;							
							last = v[k];
							lastIndex = k;
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
					
		if (last!=null && s.end.y<last.y) s.end = last;		
		s.reCalLength();
		return lastIndex;
	}

	public final static int getAllPoints(final Vector2D[] v,int from,int to,double tW,Vector2D center,double rad,Segment prev,Segment next,Segment s,Vector2D[] tmp,int size,int firstIndex,int[] marks,int n){
		int lastIndex = to-1;
		if (firstIndex!=-1) {
			lastIndex = firstIndex+size-1;
//			for (int i= firstIndex;i<=lastIndex;++i){
//				marks[i] = n;
//			}
		}
		
		Vector2D first = tmp[0];
		Vector2D last = null;
		double r = -1;
		if (size>1) {
//			Arrays.quicksort(tmp, 0, size-1, comp);
		
			
			last = tmp[size-1];
			if (next!=null && next.start.y<last.y){
				next.start = new Vector2D(last);
				next.reCalLength();
			}
			int tp = 0;	
			if (prev!=null && s.type !=0 && prev.type==0 && n==2){				
				double x0 = prev.start.x;
				Geom.getCircle2(first, last, new Vector2D(x0,0), new Vector2D(x0,1), temp);					
				temp[2] = Math.sqrt(temp[2]);
				r = Math.round(temp[2]-s.type*tW)+s.type*tW;			
				
			} else if (size==2 && s.type!=0 && prev.type!=0){
				if (s.type==prev.type) 
					Geom.getCircle4(first, last, prev.center, prev.radius, temp);					
				else Geom.getCircle5(first, last, prev.center, prev.radius, temp);
				r = Math.round(temp[2]-s.type*tW)+s.type*tW;				
			} else if (size>=3 && s.type!=0){
//				if (s.type==prev.type) 
//					Geom.getCircle4(first, last, prev.center, prev.radius, temp);					
//				else Geom.getCircle5(first, last, prev.center, prev.radius, temp);
//				r = Math.round(temp[2]-s.type*tW)+s.type*tW;
				if (size==3){
					double[] temp =new double[3];
					Geom.getCircle(tmp[0].x, tmp[0].y, tmp[size-1].x, tmp[size-1].y, tmp[1].x, tmp[1].y, temp);
					temp[2] = Math.sqrt(temp[2]);
					int turn = TrackSegment.getTurn(temp[0], temp[1], temp[2], tmp[0].x, tmp[0].y, tmp[size-1].x, tmp[size-1].y);
					r = Math.round(temp[2]-turn*tW)+turn*tW;
				} else {
					CircleFitter cf = new CircleFitter(new double[]{center.x,center.y,rad},tmp,0,size-1);
					cf.fit();
					int turn = TrackSegment.getTurn(cf.getEstimatedCenterX(), cf.getEstimatedCenterY(), cf.getEstimatedRadius(), tmp[0].x, tmp[0].y, tmp[size-1].x, tmp[size-1].y);
					r = Math.round(cf.getEstimatedRadius()-turn*tW)+turn*tW;
				}
			}  else if (size==2 && s.type==0){
				if (s.start.y>first.y) s.start = first;
				if (s.end.y<last.y) s.end = last;
			} else if (size>=3 && s.type==0){
				double[] temp =new double[3];
				boolean isCircle = Geom.getCircle(tmp[0].x, tmp[0].y, tmp[size-1].x, tmp[size-1].y, tmp[1].x, tmp[1].y, temp);
				temp[2] = Math.sqrt(temp[2]);
				if (temp[2]>=MAX_RADIUS-1){
					r = MAX_RADIUS-1;
				} else if (isCircle && size>3){					
					CircleFitter cf = new CircleFitter(temp,tmp,0,size-1);
					cf.fit();					
					if (cf.getEstimatedRadius()<MAX_RADIUS-1){
						tp = TrackSegment.getTurn(cf.getEstimatedCenterX(), cf.getEstimatedCenterY(), cf.getEstimatedRadius(), tmp[0].x, tmp[0].y, 
								tmp[size-1].x, tmp[size-1].y);
						
						r = Math.round(cf.getEstimatedRadius()-tp*tW)+tp*tW;
					}					
				} else if (isCircle){
					tp = TrackSegment.getTurn(temp[0], temp[1], temp[2], tmp[0].x, tmp[0].y, tmp[size-1].x, tmp[size-1].y);
					r = Math.round(temp[2]-tp*tW)+tp*tW;
				}
				if (!isCircle || r>=MAX_RADIUS-1){				
					double dx = last.x-first.x;
					double dy = last.y-first.y;			
					double tmp1 = dy/dx;
					LineFitter lf = new LineFitter(new double[]{tmp1,last.y-last.x*tmp1},tmp,0,size-1);
					lf.fit();
					double a = lf.getA();
					double b = lf.getB();
					first.y = a*first.x+b;
					last.y = a*last.x+b;
					if (s.start.y>first.y) s.start = first;
					if (s.end.y<last.y) s.end = last;
					center = null;
					r = MAX_RADIUS-1;
				}
			}
			int rd = (rad>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(rad-s.type*tW);

			if (r!=-1){
				if (Math.abs(r-rad)<0.5){
					if (s.start.y>first.y) s.start = first;
					if (s.end.y<last.y) s.end = last;
					if (s.type!=Segment.UNKNOWN) s.map[rd]++;
				} else {
					int rr = (r>=MAX_RADIUS-1) ? MAX_RADIUS-1 : (s.type==0) ? double2int(r-tp*tW): double2int(r-s.type*tW);			
					int nr = s.map[rd];
					s.map[rr]++;
					if (s.minR>rr) s.minR = rr;
					if (s.maxR<rr) s.maxR = rr;
					if (nr<s.map[rr] && rr<MAX_RADIUS-1 && center!=null ){
						Vector2D p = first;
						Vector2D q = last;
						Vector2D m = p.plus(q).times(0.5);
						Vector2D nn = q.minus(p).orthogonal().normalised();
						double pq = p.distance(q) * 0.5;
						if (nn.dot(center.minus(m))<0) nn = nn.negated();
						center = m.plus(nn.times(Math.sqrt(r*r-pq*pq)));
						rad = r;
						s.center = center;
						s.radius = r;
						if (s.type==0) s.type = tp;
					} else if (nr<s.map[rr] && rr>=MAX_RADIUS-1){
						s.type = 0;
						s.radius = int2double(rr);
						s.reCalLength();
					}
					//			if (nr<s.map[rr]) getAllPoints(v,from,to,tW,center,r,prev,s,tmp,size,marks,n);
				}
			}
		}
		int oldLastIdx = lastIndex;
		firstIndex = expandBackward(v, from, to, tW, center, rad, prev, next, s, firstIndex, last, marks, n);
		lastIndex = expandForward(v, from, to, tW, center, rad, prev, next, s, lastIndex, first,marks, n);
		if (firstIndex!=-1){
			size = lastIndex-firstIndex+1;
			s.num = size;
			System.arraycopy(v, firstIndex, tmp, 0, size);
		} else s.num += lastIndex-oldLastIdx;		
		return (firstIndex==-1) ? 0 : size;
	}

	public final static int findGap(final Vector2D[] v,int from, int to,int[] marks,Vector2D[] temp){
		int size = 0;	
		for (int j = from;j<to;++j){
			if (marks[j]==0)
				temp[size++] = v[j];
		}
		return size;
	}

	public final static ObjectList<Segment> fillGap(final Vector2D[] v,int from, int to,double tW,Segment prev,int[] marks,Vector2D[] tmp,int size){
		if (size<2) return null;
		if (size==2 && mark==2 && prev!=null && prev.type==0){
			ObjectArrayList<Segment> rs = new ObjectArrayList<Segment>();
			double xx = prev.start.x;
			Geom.getCircle2(tmp[0], tmp[size-1], new Vector2D(xx,0), new Vector2D(xx,1), temp);					
			temp[2] = Math.sqrt(temp[2]);
			double r = Math.round(temp[2]-tW)+tW;
			for (int i=from;i<to;++i)
				marks[i] = mark;
			if (r<REJECT_VALUE) return null;
			Segment s = null;
			if (r>=MAX_RADIUS-1){
				TrackSegment ts = TrackSegment.createStraightSeg(0, tmp[1].x, tmp[0].y, tmp[1].x, tmp[1].y);
				s = new Segment(ts);
			} else{
				Vector2D center = new Vector2D(temp[0],temp[1]);
				Vector2D o = tmp[0].plus(tmp[size-1]).times(0.5);
				double d = o.distance(tmp[0]);
				center = o.plus(center.minus(o).normalised().times(Math.sqrt(r*r-d*d) ));
				TrackSegment ts = TrackSegment.createTurnSeg(center.x, center.y, r, tmp[0].x, tmp[0].y, tmp[1].x, tmp[1].y);
				s = new Segment(ts,tW);
				
			}
			s.num = size; 
			rs.add(s);

			return rs;
		}
		ObjectList<Segment> rs = Segment.segmentize(v, from, to, tW);
		if (rs.size()==1){
			Segment s = rs.get(0);
			for (int i=from;i<to;++i)
				marks[i] = mark;
			if (s.type!=UNKNOWN){				
				if (s.radius<REJECT_VALUE) return null;
				return rs;
			} else if (s.num>=2 && mark==2 && prev!=null && prev.type==0){				
				double xx = prev.start.x;
				Geom.getCircle2(tmp[0], tmp[s.num-1], new Vector2D(xx,0), new Vector2D(xx,1), temp);					
				temp[2] = Math.sqrt(temp[2]);
				double r = Math.round(temp[2]-tW)+tW;				
				if (r<REJECT_VALUE) return null;				
				if (r>=MAX_RADIUS-1){
					TrackSegment ts = TrackSegment.createStraightSeg(0, tmp[1].x, tmp[0].y, tmp[1].x, tmp[1].y);
					s.copy(ts);
				} else if (Math.abs(s.radius-r)>0.5){
					Vector2D center = new Vector2D(temp[0],temp[1]);
					Vector2D o = tmp[0].plus(tmp[size-1]).times(0.5);
					double d = o.distance(tmp[0]);
					center = o.plus(center.minus(o).normalised().times(Math.sqrt(r*r-d*d) ));
					TrackSegment ts = TrackSegment.createTurnSeg(center.x, center.y, r, tmp[0].x, tmp[0].y, tmp[1].x, tmp[1].y);
					s.copy(ts,tW);
				}
//				int rr = (int)Math.round(r-s.type*tW);
//				if (s.minR>rr) s.minR = rr;
//				if (s.maxR<rr) s.maxR = rr;
//				s.map[rr]++;
				rs.set(0, s);

				return rs;
			}							
		} else {
			int k = 0;
			int mrk = 0;
			for (int i =0;i<rs.size();++i){
				Segment s = rs.get(i);
				if (i==0 && s.num>=2 && mark==2 && prev!=null && prev.type==0 && s.type!=0){
					double xx = prev.start.x;
					Geom.getCircle2(tmp[0], tmp[s.num-1], new Vector2D(xx,0), new Vector2D(xx,1), temp);					
					temp[2] = Math.sqrt(temp[2]);
					double r = Math.round(temp[2]-tW)+tW;				
					if (r<REJECT_VALUE) continue;
					if (s.type!=Segment.UNKNOWN && Math.abs(s.radius-r)<0.5) {
						for (int j = 0;j<s.num;++j){
							marks[from+k++] = mark+mrk;					
						}
						mrk++;
						continue;
					}
					if (r>=MAX_RADIUS-1){
						TrackSegment ts = TrackSegment.createStraightSeg(0, tmp[0].x, tmp[0].y, tmp[1].x, tmp[1].y);
						s.copy(ts);
					} else {
						Vector2D center = new Vector2D(temp[0],temp[1]);
						Vector2D o = tmp[0].plus(tmp[size-1]).times(0.5);
						double d = o.distance(tmp[0]);
						center = o.plus(center.minus(o).normalised().times(Math.sqrt(r*r-d*d) ));
						TrackSegment ts = TrackSegment.createTurnSeg(center.x, center.y, r, tmp[0].x, tmp[0].y, tmp[1].x, tmp[1].y);
						s.copy(ts,tW);
					}
//					int rr = (int)Math.round(r-s.type*tW);
//					if (s.minR>rr) s.minR = rr;
//					if (s.maxR<rr) s.maxR = rr;
//					s.map[rr]++;
					rs.set(0, s);
				} else if (s.type!=Segment.UNKNOWN){															
					int rr = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : (int)Math.round(s.radius-s.type*tW);
					if (s.minR>rr) s.minR = rr;
					if (s.maxR<rr) s.maxR = rr;
//					s.map[rr]++;					
				}
				if (s.type==Segment.UNKNOWN || s.radius<REJECT_VALUE){
					for (int j = 0;j<s.num;++j){
						marks[k++] = 0;					
					}
				} else {
					for (int j = 0;j<s.num;++j){
						marks[from+k++] = mark+mrk;					
					}
					mrk++;
				}
			}
			mark += mrk-1;
		}

		return rs;
	}	
	
	public final static void mix(Segment o, Segment s,int which,double tW){				
		int rrr = (o.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(o.radius+o.type*which*tW);
		int sR = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius+s.type*which*tW);
		if (sR!=rrr && (o.type==s.type || s.type==0)) o.map[sR] += s.map[sR];
		if (o.minR>sR) o.minR=sR;
		if (o.maxR<sR) o.maxR=sR;
		if (s.type==o.type) {
			o.map[rrr] += s.map[rrr];
			if (rrr==sR){
				if (o.start.y>s.start.y) o.start = new Vector2D(s.start);
				if (o.end.y<s.end.y) o.end = new Vector2D(s.end);
				o.reCalLength();
			} else {
				o.start = new Vector2D(s.start);
				o.end = new Vector2D(s.end);
				if (o.map[rrr]<o.map[sR]) 
					o.radius = s.radius;										
				o.reCalLength();
			}
				
		} else if (o.type==0){
			o.start = new Vector2D(s.start);
			o.end = new Vector2D(s.end);
			o.map[MAX_RADIUS-1]+=s.map[MAX_RADIUS-1];														
			if (o.map[MAX_RADIUS-1]<o.map[sR]){
				o.radius = s.radius;
				o.type = s.type;
				o.reCalLength();
//				s.minR = MAX_RADIUS-1;
			}
		} else if (s.type==0){
			o.start = new Vector2D(s.start);
			o.end = new Vector2D(s.end);
			if (o.map[rrr]<o.map[sR]) {
				o.radius = s.radius;
				o.type = 0;								
			}
			o.reCalLength();
		} else {
			o.start = new Vector2D(s.start);
			o.end = new Vector2D(s.end);
			if (o.mapR==null) {
				o.mapR=s.map;
			} else o.mapR[sR] += s.map[sR];
			if (o.map[rrr]<o.mapR[sR]){
				o.radius = s.radius;
				o.type = s.type;
				o.reCalLength();
				int[] tmp = o.map;
				o.map = o.mapR;
				o.mapR = tmp;
				o.minR =1001;
				o.maxR = 0;
				for (int i=0;i<=MAX_RADIUS-1;++i){
					if (o.map[i]!=0){
						if (o.minR>i) o.minR = i;
						if (o.maxR<i) o.maxR = i;
					}
				}
			}
			o.reCalLength();
		}	
	}
	
	
	public final static void mix(ObjectList<Segment> ss, ObjectList<Segment> li,int which,double tW){
		if (li==null || li.size()==0) return;		
		int index = 0;
		int indx = 0;
		for (;index<li.size();++index){
			Segment o = li.get(index);
			if (o.type==Segment.UNKNOWN) {
				li.remove(index--);
				continue;
			}
			Segment nexto = (index<li.size()-1) ? li.get(index+1) : null;
			
			
			for (;indx<ss.size();++indx){
				Segment s = ss.get(indx);
				if (o.start.y>s.end.y && Math.abs(s.radius-o.radius)>=1) continue;
				if (o.end.y<s.start.y && Math.abs(s.radius-o.radius)>=1) break;
				Segment nexts = (indx<ss.size()-1) ? ss.get(indx+1) : null;
				int rrr = o.minR;
				int sR = (s.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(s.radius+s.type*which*tW);
//				if (sR!=rrr && (o.type==s.type || o.type==0 || s.type==0)) o.map[sR] += s.map[sR];
				if (o.minR>sR) o.minR=sR;
				if (o.maxR<sR) o.maxR=sR;
				if (s.type==o.type  || o.type==0 || s.type==0) {
					s.map[rrr] += o.map[rrr];
					if (s.map[rrr]<s.map[sR]) {						
						li.set(index, s);
						if (nexto!=null && s.end.y>nexto.start.y){
							while (nexto!=null && s.end.y>nexto.start.y){
								li.remove(index+1);
								nexto = (index<li.size()-1) ? li.get(index+1) : null;
							}
						} 						
						break;
					} else if (rrr==sR){
						if (s.start.y>o.start.y) s.start = new Vector2D(o.start);
						if (s.end.y<o.end.y) s.end = new Vector2D(o.end);
						if (nexts!=null && s.end.y>nexts.end.y){
							while (nexts!=null && s.end.y>nexts.end.y){
								ss.remove(indx+1);
								nexts = (indx<li.size()-1) ? ss.get(indx+1) : null;
							}
						} 
						if (nexts!=null && s.end.y>nexts.start.y){
							nexts.start = new Vector2D(s.end);
							nexts.reCalLength();
						}
						o.map = s.map;
						o.reCalLength();
					} else if (s.map[rrr]>s.map[sR]){
						o.map = s.map;
						s.copy(o);
						if (nexts!=null && o.end.y>nexts.end.y){
							while (nexts!=null && o.end.y>nexts.end.y){
								ss.remove(indx+1);
								nexts = (indx<li.size()-1) ? ss.get(indx+1) : null;
							}
						} 
						if (nexts!=null && o.end.y>nexts.start.y){
							int nR = (nexts.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(nexts.radius+nexts.type*which*tW);
							if (nexts.type==o.type  || o.type==0 || nexts.type==0){
								nexts.map[rrr]++;								
								if (nexts.map[rrr]>nexts.map[nR]){
									ss.remove(indx+1);
									break;
								}							
							} else {
								if (nexts.mapR==null) 
									nexts.mapR = o.map;
								else nexts.mapR[rrr]++;
								if (nexts.mapR[rrr]>nexts.map[nR]){
									ss.remove(indx+1);
									break;
								}			 
							}
							nexts.start = new Vector2D(s.end);
							nexts.reCalLength();
						}
					}
				} else {
					if (s.mapR==null) 
						s.mapR = o.map;
					else s.mapR[rrr]++;
					if (s.mapR[rrr]<=s.map[sR]){																								
						li.set(index, s);
						if (nexto!=null && s.end.y>nexto.start.y){
							while (nexto!=null && s.end.y>nexto.start.y){
								li.remove(index+1);
								nexto = (index<li.size()-1) ? li.get(index+1) : null;
							}
						} 						
						break;
					} else {
						o.map = s.mapR;
						o.mapR = s.map;
						s.copy(o);
						if (nexts!=null && s.end.y>nexts.end.y){
							while (nexts!=null && s.end.y>nexts.end.y){
								ss.remove(indx+1);
								nexts = (indx<li.size()-1) ? ss.get(indx+1) : null;
							}
						} 
						if (nexts!=null && o.end.y>nexts.start.y){
							int nR = (nexts.radius>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(nexts.radius+nexts.type*which*tW);
							if (nexts.type==o.type  || o.type==0 || nexts.type==0){
								nexts.map[rrr]++;								
								if (nexts.map[rrr]>nexts.map[nR]){
									ss.remove(indx+1);
									break;
								}							
							} else {
								if (nexts.mapR==null) 
									nexts.mapR = o.map;
								else nexts.mapR[rrr]++;
								if (nexts.mapR[rrr]>nexts.map[nR]){
									ss.remove(indx+1);
									break;
								}			 
							}
							nexts.start = new Vector2D(o.end);
							nexts.reCalLength();
						}

					}
				}
			}//end of for s:ss
		}				
	}

	public final static boolean isSameSegment(Segment s,Segment t){
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
				if (Math.abs(Math.atan2(dy, dx)-Math.atan2(ddy, ddx))>0.1) return false;
			}
		} 
		return true;
	}

	//from inclusive, to exclusive
	public final static ObjectList<Segment> segmentize(final Vector2D[] v,int from, int to,int which,double tW,ObjectList<Segment> guess,double dist){
		//		int len = to - from;		
		int i = 0;		
		double x0 = v[from].x;		
		for (i = 1+from;i<to;++i){
			if (Math.abs(v[i].x-x0)>=TrackSegment.EPSILON) break;
		}
		Segment first = (guess==null || guess.size()<1) ? null : toSideSegment(guess.get(0),which,tW);
		int j = 0;
		if (first!=null && first.type==0){
			if (first.length < dist+v[i-1].y-first.dist){
				first.length = dist+v[i-1].y-first.dist;
				first.end = v[i-1];
			} else if (first!=null && first.dist<=dist)
				for (;i<to;++i){
					if (v[i].y>first.dist+first.length-dist) break;
				}
			boolean ok = false;

			for (;i<to;++i){				
				x0 = v[i].x;					
				for (j = i+1;j<to;++j){
					if (j==i+1 && Math.abs(v[j].x-x0)<=4*TrackSegment.EPSILON){
						ok = true;						
					} else if (j==i+1) break;
					if (j<to-1 && Math.abs(v[j+1].x-x0)>4*TrackSegment.EPSILON) break;
					if (j==to-1) break;
				}
				if (ok){
					if (first!=null){
						first.end = v[j];
						first.length = v[j].y+dist - first.dist;
					}
					i = j;
					ok = false;
				} else break;				
			}			
		}

		if (first == null && i>0){
			TrackSegment ts = TrackSegment.createStraightSeg(dist+v[0].y, v[i-1].x, v[0].y, v[i-1].x, v[i-1].y);
			first = new Segment(ts);
		}
		Vector2D[] tmp = new Vector2D[v.length];
		int size = 0 ;
		int k =0;		
		int[] marks = new int[v.length];
		for (k=0;k<i;++k) marks[k] = 1;
		first.reCalLength();
		ObjectArrayList<Segment> rs = new ObjectArrayList<Segment>();
		rs.add(first);
		if (guess!=null && first!=null && guess.size()>0) {
			guess.set(0, toMiddleSegment(first, which, tW));
		} else{
			guess.add(toMiddleSegment(first, which, tW));
		}

		Segment prev = first;
		mark = 2;
		int prevLastIdx = i;

		if (guess!=null){
			ObjectList<Segment> ss =  new ObjectArrayList<Segment>(guess.size());
			Segment next = null;
			for (j=1;j<guess.size();++j){
				Segment s = toSideSegment(guess.get(j),which,tW);
				if (prev!=null && prev.end.y>s.start.y){
					if (prev.end.y>s.end.y){	
						guess.remove(j--);
						continue;
					} else s.start = new Vector2D(prev.end);					
				}
				next = (j+1>=guess.size()) ? null : toSideSegment(guess.get(j+1),which,tW);
				ss.add(s);
				size = 0;
				int firstIndex = -1;
				for (k=i;k<to;++k){
					if (v[k].y>=s.start.y && v[k].y<=s.end.y){
						marks[k] = mark;
						tmp[size++] = v[k];
						if (firstIndex==-1) firstIndex = k;
					}
					if (v[k].y>s.end.y) break;
				}
				int count = size;
				if (size>=2 && s.type!=0){
					Vector2D p = tmp[0];
					Vector2D q = tmp[size-1];
					Vector2D m = p.plus(q).times(0.5);
					Vector2D n = q.minus(p).orthogonal().normalised();
					double pq = p.distance(q) * 0.5;
					if (n.dot(new Vector2D(s.type,0))<0) n = n.negated();
					Vector2D center = m.plus(n.times(Math.sqrt(s.radius*s.radius-pq*pq)));
					count = getAllPoints(v, from, to, -which*tW, center, s.radius, prev,next, s, tmp, size,firstIndex, marks, mark);								
				} else if (size>=1){
					count = getAllPoints(v, from, to, -which*tW, s.center, s.radius, prev,next, s, tmp, size, firstIndex, marks, mark);
				} 
				/*else if (size==0){
					int index = prevLastIdx;
					for (;index<to;++index){
						Vector2D p = v[index];
						if (p.y>s.start.y) break;
					}	
					Vector2D fP = null;
					int fI = -1;
					if (index>0 && marks[index-1]==0){
						fP = v[index-1];
						fI = index-1;
						firstIndex = expandBackward(v, from, to, -which*tW, s.center, s.radius, prev,next, s, index,null,marks, mark);
						if (firstIndex!=index){ 
							count = getAllPoints(v, from, to, -which*tW, s.center, s.radius, prev,next, s, tmp, size, firstIndex, marks, mark);
							fP = v[firstIndex];
						} else {
							firstIndex = -1;
							fP = null;
						}
					}
					Vector2D lP = null;
					if (firstIndex==-1){
						for (;index<to;++index){
							Vector2D p = v[index];
							if (p.y>s.end.y) break;
						}
						if (index<to && marks[index]==0)
							if (fP==null) {
								fP = v[index];
								fI = index;
								lP = (index+1<to) ? v[index+1] :null;
							} else lP = v[index];
							
							firstIndex = expandForward(v, from, to, -which*tW, s.center, s.radius, prev,next, s, index-1,null,marks, mark);
						if (firstIndex!=index-1){ 
							count = getAllPoints(v, from, to, -which*tW, s.center, s.radius, prev,next, s, tmp, size, firstIndex, marks, mark);
							fP = v[firstIndex];
							lP = v[firstIndex+count-1];
						} else {
							firstIndex = -1;
							fP=null;
							lP=null;
						}
					}
					if (firstIndex==-1 && fP!=null && lP!=null){
						if (fP==lP && index+1<to) lP = v[index+1];
						if (fP!=lP){
							if (prev!=null && prev.type==0 && mark==2){
								double xx = prev.start.x;
								Geom.getCircle2(fP, lP, new Vector2D(xx,0), new Vector2D(xx,1), temp);					
								temp[2] = Math.sqrt(temp[2]);
								int tp = TrackSegment.getTurn(temp[0], temp[1], temp[2], fP.x, fP.y, lP.x, lP.y);
								double r = Math.round(temp[2]+tp*which*tW)-tp*which*tW;
								if (tp==s.type && Math.abs(r-s.radius)<0.5){
									tmp[size++] = fP;
									tmp[size++] = lP;
									Vector2D p = fP;
									Vector2D q = lP;
									Vector2D m = p.plus(q).times(0.5);
									Vector2D n = q.minus(p).orthogonal().normalised();
									double pq = p.distance(q) * 0.5;
									if (n.dot(new Vector2D(s.type,0))<0) n = n.negated();
									Vector2D center = m.plus(n.times(Math.sqrt(r*r-pq*pq)));
									count = getAllPoints(v, from, to, -which*tW, center, s.radius, prev,next, s, tmp, size, fI, marks, mark);
								}
							}
						}
					}
				}//*/

				if (firstIndex!=-1) {
					int gap = findGap(v, prevLastIdx, firstIndex, marks, tmp);
					if (gap>=1){
						int kkk = ss.size();
						for (kkk = ss.size()-1;kkk>=0;--kkk){
							Segment tt = ss.get(kkk);
							if (tt.end.y<tmp[0].y) break;
						}
						size = gap;
						
						if (prev!=null && prev.type==0 && mark==2 && gap>=2){							
							double xx = prev.start.x;
							Geom.getCircle2(tmp[0], tmp[gap-1], new Vector2D(xx,0), new Vector2D(xx,1), temp);					
							temp[2] = Math.sqrt(temp[2]);
							int tp = TrackSegment.getTurn(temp[0], temp[1], temp[2], tmp[0].x, tmp[0].y, tmp[gap-1].x, tmp[gap-1].y);
							double r = Math.round(temp[2]+tp*which*tW)-tp*which*tW;							
							Vector2D p = tmp[0];
							Vector2D q = tmp[gap-1];
							Vector2D m = p.plus(q).times(0.5);
							Vector2D n = q.minus(p).orthogonal().normalised();
							double pq = p.distance(q) * 0.5;
							if (n.dot(new Vector2D(tp,0))<0) n = n.negated();
							Vector2D center = m.plus(n.times(Math.sqrt(r*r-pq*pq)));
							s.start = new Vector2D(tmp[0]);
							s.end = new Vector2D(tmp[gap-1]);
							s.radius = r;
							s.center = center;
							s.type = tp;
							s.reCalLength();
							s.map = new int[MAX_RADIUS+1];							
							int sR = (r>=MAX_RADIUS-1) ? MAX_RADIUS-1 : double2int(r+tp*which*tW);
							s.map[sR]++;
							
							for (int kk=prevLastIdx+gap;kk<prevLastIdx+gap+count;++kk){
								marks[kk] = 0;
							}
							int nIndex = expandForward(v, from, to, -which*tW, center, r, prev,next, s, prevLastIdx+gap,tmp[0],marks, mark);
							for (int kk=prevLastIdx;kk<nIndex;++kk){
								marks[kk] = 2;
							}
							s.num = nIndex-prevLastIdx;
							guess.set(j, toMiddleSegment(s, which, tW));
							prev = s;							
							prevLastIdx = nIndex;							
							mark++;
							if (next!=null && next.start.y>=next.end.y){
								while (next!=null && next.start.y>=next.end.y){
									guess.remove(j+1);
									next = (j+1<guess.size()) ? toSideSegment(guess.get(j+1),which,tW) : null;
										
								} 
							} else if (next!=null && j+1<guess.size()){
								guess.set(j+1, toMiddleSegment(next, which, tW));
							}
							continue;
						}
												
						ObjectList<Segment> ol = null;
						if (count<4) {							
							for (int kk = prevLastIdx+gap;kk<prevLastIdx+gap+count;++kk){
								tmp[size++] = v[kk];
								marks[kk] = 0;
							}			
							ol = ss.subList(kkk+1, ss.size());
						} else ol = ss.subList(kkk+1, ss.size()-1);						
						
						if (prev!=null && prev.type==0 && size>1 && s.type!=0 && mark==2){ 
							double xx = prev.start.x;
							Geom.getCircle2(tmp[0], tmp[size-1], new Vector2D(xx,0), new Vector2D(xx,1), temp);					
							temp[2] = Math.sqrt(temp[2]);							
							int tp = TrackSegment.getTurn(temp[0], temp[1], temp[2], tmp[0].x, tmp[0].y, tmp[size-1].x, tmp[size-1].y);
							double r = Math.round(temp[2]+tp*which*tW)-tp*which*tW;
							
							if (s.type==tp && Math.abs(r-s.radius)<0.5){
								if (s.start.y>tmp[0].y)	{
									s.start.copy(tmp[0]);																		
								}
								if (s.end.y<tmp[size-1].y) {
									s.end.copy(tmp[size-1]);									
								}
								s.reCalLength();
								
								for (int kk = prevLastIdx;kk<prevLastIdx+gap+count;++kk){									
									marks[kk] = mark;
								}
								Vector2D p = tmp[0];
								Vector2D q = tmp[size-1];
								Vector2D m = p.plus(q).times(0.5);
								Vector2D n = q.minus(p).orthogonal().normalised();
								double pq = p.distance(q) * 0.5;
								if (n.dot(new Vector2D(tp,0))<0) n = n.negated();
								Vector2D center = m.plus(n.times(Math.sqrt(s.radius*s.radius-pq*pq)));
								prevLastIdx = expandForward(v, from, to, -which*tW, center, r, prev,next, s, prevLastIdx+gap+count,p,marks, mark);
								guess.set(j, toMiddleSegment(s, which, tW));
								prev = s;
							} else {
								ObjectList<Segment> li = fillGap(v, prevLastIdx, prevLastIdx+size, -which*tW, prev, marks, tmp, size);
								if (li!=null && li.size()>0) {
									int ols = ol.size();
									mix(ol,li,which,tW);
									
									if (li!=null && li.size()>0){										
										Segment tt = li.get(li.size()-1);
										if (tt!=null && tt.type!=Segment.UNKNOWN)
											prevLastIdx = expandForward(v, from, to, -which*tW, tt.center, tt.radius, prev,next, s, prevLastIdx+size,tt.start,marks, mark);											
									}
									rs.addAll(li);
									guess.removeElements(kkk+2, kkk+2+ols);
//									ss.removeElements(kkk+1, kkk+1+ols);
									j = kkk+2;
									for (int idx =0;idx<ol.size();++idx){
										Segment tt = ol.get(idx);
										if (tt!=null && tt.type!=Segment.UNKNOWN){
											if (j<=guess.size()) {
												Segment sss = toMiddleSegment(tt, which, tW);												
												Segment exist= guess.get(j-1);
												if (!isSameSegment(exist, sss)) {
													guess.add(j++,sss);
//													ss.add(sss);
												} else {
													mix(exist,sss,which,tW);
													if (ol!=null && ol.size()>0) ol.set(idx, toSideSegment(exist, which, tW));
												}																							
												
											}
											prev = tt;						
										}
									}								
								}
							}
						} else {
							ObjectList<Segment> li = fillGap(v, prevLastIdx, prevLastIdx+size, -which*tW, prev, marks, tmp, size);						
							if (li!=null && li.size()>0) {
								int ols = ol.size();
								mix(ol,li,which,tW);
								if (li!=null && li.size()>0){
									Segment tt = li.get(li.size()-1);
									if (tt!=null && tt.type!=Segment.UNKNOWN)
										prevLastIdx = expandForward(v, from, to, -which*tW, tt.center, tt.radius, prev,next, s, prevLastIdx+size,tt.start,marks, mark);										
								}
								rs.addAll(li);
								guess.removeElements(kkk+2, kkk+2+ols);
//								ss.removeElements(kkk+1, kkk+1+ols);
								j = kkk+2;
								for (int idx =0;idx<ol.size();++idx){
									Segment tt = ol.get(idx);						
									if (tt!=null && tt.type!=Segment.UNKNOWN){
										Segment sss = toMiddleSegment(tt, which, tW);								
										Segment exist= guess.get(j-1);
										if (!isSameSegment(exist, sss)) {
											guess.add(j++,sss);											
										} else {
											mix(exist,sss,which,tW);
											if (ol!=null && ol.size()>0) ol.set(idx, toSideSegment(exist, which, tW));
										}		
										prev = tt;						
									}
								}								
							}
						}
					} else {
						rs.add(s);
						prev = s;
						prevLastIdx += gap+count-1;
						guess.set(j, toMiddleSegment(s, which, tW));
					} 			
				} else {
//					for (;prevLastIdx<to;++prevLastIdx){
//						if (v[prevLastIdx].y>s.end.y) break;
//					}
					prev = s;
				}
				
				if (next!=null && next.start.y>=next.end.y){
					while (next!=null && next.start.y>=next.end.y){
						guess.remove(j+1);
						next = (j+1<guess.size()) ? toSideSegment(guess.get(j+1),which,tW) : null;
							
					} 
				}else if (next!=null && j+1<guess.size()){
					guess.set(j+1, toMiddleSegment(next, which, tW));
				}
				
//				for (int kk=to;kk>=i;--kk)
//					if (marks[kk]==mark) {
//						prevLastIdx = kk+1;
//						break;
//					}
				if (firstIndex!=-1) prevLastIdx++;
				mark++;

			}
		}
		size = 0;
		k = 1;
		prev = first;
		i = prevLastIdx;
		prevLastIdx = -1;
		for (j = i;j<to;++j){
			if (marks[j]!=0 || j==to-1){
				if (marks[j]==0)
					tmp[size++] = v[j];
				if (size>2){
					int indx = 0;
					for (;indx<guess.size();++indx){
						Segment g = toSideSegment(guess.get(indx),which,tW);
						if (g.start.y>tmp[0].y) break;
					}
					
					mark = Math.max(mark, indx+1);
					ObjectList<Segment> list = fillGap(v, prevLastIdx, j+1, -which*tW, prev, marks, tmp, size);
					if (list!=null && list.size()>0) {
						rs.addAll(list);
					
						for (int idx =0;idx<list.size();++idx){
							Segment tt = list.get(idx);			
							if (tt!=null && tt.type!=Segment.UNKNOWN){
								Segment sss = toMiddleSegment(tt, which, tW);
								indx = guess.size()-1;
								Segment exist = null;
								boolean test = false;
								for (;indx>=0;--indx){
									exist = guess.get(indx);
									test = isSameSegment(exist, sss);
									if (exist.end.y<sss.start.y || test) break;
								}
								
								if (!test) {
									indx++;
									boolean ok = indx<guess.size(); 
									if (ok) exist = guess.get(indx);
									if (!ok || !test){
										if (indx==1 && guess!=null && guess.size()>1) guess.remove(indx);
										guess.add(indx,sss);
									} else {
										mix(exist,sss,which,tW);											
									}
								} else {
									mix(exist,sss,which,tW);
								}
								prev = tt;						
							}
						}//end of for
					}
				}
				k = marks[j];
				size = 0;
				if (k>=1 && rs.size()>k-1) prev = rs.get(k-1);
			} else if (marks[j]==0){
				tmp[size++] = v[j];
				if (prevLastIdx==-1) prevLastIdx = j;
			}
		}	
		boolean ok = false;

		/*prev = guess.get(0);
		for (i = 1;i<rs.size();++i){
			Segment s = rs.get(i);			
			if (s.type==Segment.UNKNOWN) continue;			
			Segment t = toMiddleSegment(s, which, tW);
			estimateDist(prev, t);
			prev = t;

			ok = false;
			if (guess!=null && guess.size()>1)
			for (j=1;j<guess.size();++j){
				Segment g = guess.get(j);			
				if (g.type==t.type && Math.abs(g.radius-t.radius)<0.5){
					ok = true;
					if (g.start.y>t.start.y){
						g.start = t.start;						
						g.length += g.dist - t.dist;
						g.dist = t.dist;						
					}

					if (g.end.y<t.end.y){
						g.end = t.end;
						if (g.dist+g.length<t.dist+t.length) 
							g.length = t.dist+t.length - g.dist;													
					}
				} else {
					double dd = Math.max(g.dist, t.dist);
					double ee = Math.min(g.dist+g.length, t.dist+t.length);
					double ll = ee-dd;
					if (g.type==t.type && Math.abs(g.radius-t.radius)>0.5 && ll>0 && ll>=0.6*g.length){
						ok = true;
						int tRad = double2int(t.radius);
						int gRad = double2int(g.radius);
						g.map[tRad]++;
						int nr = g.map[tRad];
						if (g.minR>tRad) g.minR = tRad;
						if (g.maxR<tRad) g.maxR = tRad;
						if (nr>g.map[gRad]){
							g.radius = t.radius;
							if (prev!=null && prev.type==g.type && Math.abs(prev.radius-g.radius)<0.5){
								if (prev.start.y>g.start.y) {
									prev.start = g.start;
									prev.dist = g.dist;
								}
								if (prev.end.y<g.end.y) prev.end = g.end;
								prev.reCalLength();
								for (int jj = g.minR;jj<=g.maxR;++jj){
									if (g.map[jj]!=0) prev.map[jj]+=g.map[jj];
								}
								if (prev.minR>g.minR) prev.minR=g.minR;
								if (prev.maxR<g.maxR) prev.maxR=g.maxR;
							}
							guess.remove(j);
						}
						
					} else if (g.type!=t.type && ll>0 && ll>=0.6*g.length){
						if (g.mapR==null) g.mapR = new int[MAX_RADIUS+1];
						int tRad = double2int(t.radius);
						int gRad = double2int(g.radius);
						g.mapR[tRad]++;
						int nr = g.mapR[tRad];
						if (nr>g.map[gRad]){
							g.radius = t.radius;
							g.type = t.type;
							int[] tmp0 = g.map;
							g.map = g.mapR;
							g.mapR = tmp0;
							g.minR = 1001;
							g.minR = 0;
							for (int kk=0;kk<MAX_RADIUS;++kk){
								int vv = g.map[kk];
								if (vv!=0){
									if (g.minR>kk) g.minR=kk;
									if (g.maxR<kk) g.maxR=kk;
								}
							}
						}
						
					}
				}
			}//end of for
			if (!ok){
				int kk = 0;
				if (guess!=null){
					for (kk = 0;kk<guess.size();++kk){
						Segment g = guess.get(kk);
						if (g.start.y>t.end.y) 						
							break;					
					}
					guess.add(kk, t);
				} 
			}
		}//*/
		return rs;
	}


	public static void drawTrack(ObjectList<Segment> ts,final String title){
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

	public static void drawTrack(ObjectList<Segment> ts,final String title,double dist){			
		XYSeries series = new XYSeries("Curve");
		if (ts==null) return;
		for (Segment tt : ts){
			if (tt.dist+tt.length<dist)
				continue;
			TrackSegment t = tt.seg;
			if (t.type==TrackSegment.STRT){
				TrackSegment.line(t.startX, t.startY, t.endX, t.endY, series);
			} else {
				TrackSegment.arc(t.centerx, t.centery, t.radius, t.startX,t.startY,t.arc,series);								
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

	@Override
	public String toString()
	{
		final String TAB = "    ";

		String retValue = "";

		retValue = "TrackSegment ( "
			+ super.toString() + TAB
			+ "type = " + this.type + TAB
			+ "num = " + this.num + TAB
			+ "radius = " + this.radius + TAB
			+ "length = " + this.length + TAB
			+ "start = " + this.start + TAB			
			+ "end = " + this.end + TAB
			+ "center = " + this.center + TAB						
			+ "dist = " + this.dist + TAB			
			+ "arc = " + this.arc + TAB
			+ "Points = " + this.points + TAB			
			+ " )";

		return retValue;
	}
}
