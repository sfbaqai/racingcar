/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.doubles.Double2IntMap;
import it.unimi.dsi.fastutil.doubles.Double2IntOpenHashMap;
import it.unimi.dsi.fastutil.doubles.Double2IntRBTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import it.unimi.dsi.fastutil.doubles.DoubleSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
	final static int MAX_RADIUS =1000;
	TrackSegment seg = null;
	double dist = -1;
	Vector2D center = null;
	Vector2D start = null;
	Vector2D end = null;
	double length = -1;
	double arc = 0;
	int type=-2;
	double radius = 0;
	int num = 0;	
	ObjectArrayList<Vector2D> points = null;
	boolean sorted = true;
	int[] map = new int[MAX_RADIUS];
	
	public static Comparator<Vector2D> comp = new Comparator<Vector2D>(){
		public int compare(Vector2D o1, Vector2D o2) {
			return (o1.y==o2.y) ? 0 : (o1.y<o2.y) ? -1 : 1;
		};
	};
	/**
	 * @param args
	 */
	
	public final static int double2int(double r){
		return (int)(Math.round(r));
	}

	public Segment(){		
	}

	public Segment(TrackSegment ts){
		seg = ts;
		dist = ts.distanceFromLocalOrigin;
		center = new Vector2D(ts.centerx,ts.centery);
		start = new Vector2D(ts.startX,ts.startY);
		end = new Vector2D(ts.endX,ts.endY);
		length = ts.length;
		type = ts.type;
		arc = ts.arc;
		radius = ts.radius;
		map[double2int(radius)] = 1;
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
		System.arraycopy(s.map, 0, map, 0, MAX_RADIUS);		
	}

	
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
		System.arraycopy(s.map, 0, map, 0, MAX_RADIUS);		
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
		map[double2int(radius)] = 1;		
	}
	
	public static Double2IntMap joinMap(Double2IntMap m1,Double2IntMap m2){
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
	}
		
	
	public void sortPoints(){
		if (!sorted){
			Arrays.quicksort(points.elements(),0,points.size()-1,comp);
			sorted = true;
		}
	}
	
	public static void sortPoints(ObjectArrayList<Vector2D> points){		
			Arrays.quicksort(points.elements(),0,points.size()-1,comp);		
	}
	
	public int contains(Vector2D p){
		if (!sorted) {
			Vector2D[] pp = points.elements();
			int sz = points.size();
			for (int i=0;i<sz;++i)
				if (pp[i].equals(p)) return i;
			return -sz-1;
		}
		return Sorting.binarySearchFromTo(points.elements(), p, 0, points.size()-1, comp);		
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

	public void addPoints(Vector2D[] v){
		if (num==0){
			points = ObjectArrayList.wrap(new Vector2D[INIT_SIZE],0);			
		}
		for (Vector2D p : v) {
			points.add(p);
			if (points.size()>LIM) break;
		}
		num = num+v.length;
	}
	
	public final void removePoint(Vector2D p){
		if (num<=0 || points==null) return;
		
		int index = contains(p); 	
		if (index>=0){			
			points.remove(index);
			if (type==0) 
				num--;
			else num = points.size();		
			
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
					end = (points==null || points.size()==0) ? null : new Vector2D(points.get(points.size()-1));
					changed = true;
				}
				if (p.distance(start)<0.1) {
					if (!sorted) sortPoints();
					start = (points==null || points.size()==0) ? null :new Vector2D(points.get(0));
					changed = true;
				}
			} else if (p.distance(end)<0.1 || p.distance(start)<0.1){
				if (!sorted) sortPoints();
				if (points==null || points.size()==0){
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
				Vector2D point = (de<0.1) ? new Vector2D(points.get(points.size()-1)) : new Vector2D(points.get(0));
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
			points = ObjectArrayList.wrap(new Vector2D[INIT_SIZE], 0);			
		}

		if (sorted && points.size()>0 && p.y<end.y) sorted = false;
		if (points.size()<=LIM) points.add(p);
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
			if (type==0) points.add(p); 
			reCalLength();
		}
		if (type==0) 
			num++;
		else num = points.size();
	}

	public void addPoints(Vector2D[] v,int from, int to){
		if (num==0){
			points = ObjectArrayList.wrap(new Vector2D[INIT_SIZE], 0);			
		}
		for (int i = from;i<to;++i){
			Vector2D p = v[i];
			points.add(p);
			if (points.size()>LIM) break;
		}
		num = num+to-from;
	}


	public void addPoints(Collection<Vector2D> v){
		if (num==0){
			points = ObjectArrayList.wrap(new Vector2D[INIT_SIZE], 0);			
		}
		for (Vector2D p : v) {
			points.add(p);
			if (points.size()>LIM) break;
		}
		num = points.size();
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
		Segment s = (seg.seg!=null) ? new Segment(seg.seg) : new Segment();
		if (seg.seg==null) s.copy(seg);
		if (seg.type==0){									
			s.start.x += t;
			s.end.x += t;			
		} else {
			TrackSegment ts = seg.seg;
			if (ts==null)
				ts = TrackSegment.createTurnSeg(seg.center.x, seg.center.y, seg.radius, seg.start.x, seg.start.y, seg.end.x, seg.end.y);
			double rad = ts.radius + t;
			s.start = s.center.plus(s.start.minus(s.center).normalised().times(rad));
			s.end = s.center.plus(s.end.minus(s.center).normalised().times(rad));
			s.radius = rad;
			s.length = Math.abs(s.arc * rad);
			if (t>0){
				for (int i = MAX_RADIUS-1;i>=0;--i){
					int n = s.map[i]; 
					if (n!=0) {
						s.map[double2int(i+t)] = n;
						s.map[i] = 0;
					}
				}
			} else {
				for (int i = 0;i<MAX_RADIUS;++i){
					int n = s.map[i]; 
					if (n!=0) {
						s.map[double2int(i+t)] = n;
						s.map[i] = 0;
					}
				}
			}			
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
				if (s.points==null) {
					s.points = ObjectArrayList.wrap(new Vector2D[INIT_SIZE], 0);
				} else {
					if (t.points!=null) s.points.addAll(t.points);
				}
				s.num = s.points.size();
		
				if (s.type!=0){					
					s.start = new Vector2D(s.points.get(0));
					s.end = new Vector2D(s.points.get(s.points.size()-1));
					s.reCalLength();
					s.arc = s.type*s.length/s.radius;
				} else {
					double dy = s.end.y-s.start.y;
					double dx = s.end.x-s.start.x;										
					s.start = new Vector2D(s.points.get(0));
					s.end = new Vector2D(s.points.get(s.points.size()-1));
					if (Math.abs(dx)<=TrackSegment.EPSILON){
						s.start.x = (s.start.x+s.end.x)*0.5;
						s.end.x = s.start.x;
					} else {
						double tmp = dy/dx;
						Vector2D[] v = s.points.elements();
						LineFitter lf = new LineFitter(new double[]{tmp,s.start.y-tmp*s.start.x},v,0,s.points.size()-1);
						lf.fit();
						double a = lf.getA();
						double b = lf.getB();
						s.start.y = a*s.start.x+b;
						s.end.y = a*s.end.x+b;												
					}
					s.reCalLength();
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
		if (!sorted) sortPoints();
		Vector2D[] v = points.elements();		
		if (type==0){
			int len = points.size()+2;
			v[len-1] = end;
			v[len-2] = start;			
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
			copy(ts);
			return;
		}
		if (points.size()<3) return;
				
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
		int len = points.size();
		boolean isCircle = Geom.getCircle(x1, y1, x2, y2, x3, y3, result);
		double radius = (isCircle) ? Math.sqrt(result[2]) : Double.MAX_VALUE;
		result[2] = radius;		
		if (v.length>3){
			CircleFitter cf = new CircleFitter(result.clone(),v,0,len-1);
			cf.fit();
			radius = cf.getEstimatedRadius();			
//			x1 = xx;
//			x2 = v[i+1].x;			
//			y1 = yy;
//			y2 = v[i+1].y;
//			x3 = v[i+3].x;
//			y3 = v[i+3].y;
//			isCircle = Geom.getCircle(x1, y1, x2, y2, x3, y3, result);
//			result[2] = Math.sqrt(result[2]);
		}
		radius = Math.round(radius-tW)+tW;
		Vector2D t = new Vector2D(x1+x2,y1+y2).times(0.5d);
		Vector2D q = new Vector2D(result[0],result[1]);
		double d = t.distanceSq(v[i]);
		Vector2D p = t.plus(q.minus(t).normalised().times(Math.sqrt(radius*radius-d)));

		ts = TrackSegment.createTurnSeg(dist, p.x, p.y, radius, x1, y1, end.x, end.y,x2,y2);
		copy(ts);

	}



	public void transform(AffineTransform at){		
		if (start!=null) at.transform(start, start);
		if (end!=null) at.transform(end, end);
		if (type!=0 &&type!= UNKNOWN && center!=null)
			at.transform(center, center);
		if (points!=null){						
			for (Vector2D v:points){
				at.transform(v, v);
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
				Vector2D[] points = last.points.elements();
				int len = last.points.size();
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
				if (last.points.size()<=0) rs.remove(l+1);								
			}
		}
	}	
	
	public final static void checkRs(ObjectList<Segment> rs,int l,double tW){		
		if (l>=1 && l<rs.size()){
			Segment s = rs.get(l-1);
			if (l==1 && s.type==0) return;
			Segment last = rs.get(l);
			if ((s.num<=3 || s.type==Segment.UNKNOWN) && last.type!=Segment.UNKNOWN){					
				Vector2D[] points = s.points.elements();
				int len = s.points.size();
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

				if (s.points.size()<=0) rs.remove(l-1);								
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
				for (Vector2D v:next.points) s.addPoint(v);
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
				Vector2D[] l = s.points.elements();
				ObjectList<Segment> ol = segmentize(l,0,s.points.size(), tW);
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
		if (i>1){
			TrackSegment ts = TrackSegment.createStraightSeg(0, x0, v[0].y, x0, v[i-1].y);
			Segment s = new Segment(ts);
			s.addPoints(v,0,i);
			rs.add(s);
			if (i==len) return rs;
		} else i = 0;
		double xx=v[i].x;
		double yy=v[i].y;
		TrackSegment ts = null;
		while (i<=to-1){
			if (i>=to-2){
				Segment last = (rs.size()>0) ? rs.get(rs.size()-1) :null;
				if (last!=null &&  last.type==Segment.UNKNOWN) rs.remove(rs.size()-1);
				Segment s = (last!=null && last.type==Segment.UNKNOWN) ? last :  new Segment();
				for (int k=i;k<len;++k) s.addPoint(v[k]);
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
				if (rs.size()==0){					
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
				s.addPoints(v,i,j);
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
					Segment s = new Segment(ts);
					s.addPoints(v, i, len);
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
					double rr = (int)Math.round(Math.sqrt(result[2])-tW)+tW;					
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
				Segment s = new Segment(ts);
				s.addPoints(v,i,j);
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
