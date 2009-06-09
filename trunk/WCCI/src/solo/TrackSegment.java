package solo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.graphbuilder.geom.Geom;

public class TrackSegment {
	final static int STRT = 0;
	final static int LFT = -1;
	final static int RGT = 1;
	final static int LEFTSTART = 0;
	final static int LEFTEND=1;
	final static int RIGHTSTART = 2;
	final static int RIGHTEND=3;
	final static double MAXRADIUS = 1000;
	final static double EPSILON = 0.001;
	final static double MINLENGTH = 0.01;

	int type;

	double centerx;
	double centery;
	double length;/**< Length in meters of the middle of the track */
	double distanceFromLocalOrigin;	
	double radius;		/**< Radius in meters of the middle of the track (>0) */
	double arc;			/**< Arc in rad of the curve (>0) */
	double startX,startY,endX,endY; 



	/**
	 * Copy Constructor
	 *
	 * @param trackSegment a <code>TrackSegment</code> object
	 */
	public TrackSegment(TrackSegment trackSegment) 
	{
		this.type = trackSegment.type;
		this.centerx = trackSegment.centerx;
		this.centery = trackSegment.centery;
		this.length = trackSegment.length;
		this.distanceFromLocalOrigin = trackSegment.distanceFromLocalOrigin;
		this.radius = trackSegment.radius;
		this.arc = trackSegment.arc;
		this.startX = trackSegment.startX;
		this.startY = trackSegment.startY;
		this.endX = trackSegment.endX;
		this.endY = trackSegment.endY;
	}


	public TrackSegment() {
	}


	public final static TrackSegment createStraightSeg(double dist,double startX,double startY,double endX,double endY){
		double radius = Double.MAX_VALUE;
		double arc = -1;
		double dx = endX-startX;
		double dy = endY - startY;
		double length = Math.sqrt(dx*dx+dy*dy);
		double centerx = (startX+endX)/2.0d;
		double centery = (startY+endY)/2.0d;		
		int type = STRT;
		return new TrackSegment(type,centerx,centery,length,dist,radius,arc,startX,startY,endX,endY);
	}

	public final static TrackSegment createTurnSeg(double dist,double centerx,double centery,double radius,double startX,double startY,double endX,double endY,double x2,double y2){		
		Vector2D v1 = new Vector2D(startX-centerx,startY-centery);
		Vector2D v2 = new Vector2D(endX-centerx,endY-centery);
		Vector2D v3 = new Vector2D(x2-centerx,y2-centery);
		double angle = Vector2D.angle(v1, v2);
		if (angle<-Math.PI) 
			angle += 2*Math.PI;
		else if (angle>Math.PI) 
			angle -= 2*Math.PI;
//		double a = v1.angle(v3);				
		//System.out.println("   "+v1.length()+"   "+v2.length()+"    "+v3.length());
//		System.out.println(a+"    "+angle);
//		if (angle*a>0 && angle/a<1) 
//			angle = (-Math.PI*2+angle)%(Math.PI*2);
//		else if (angle*a<0)
//			angle = (-Math.PI*2+angle)%(Math.PI*2);
		double arc = Math.abs(angle);		
		double length = radius*arc;
		int type = (angle<0) ? LFT : RGT;		
		return new TrackSegment(type,centerx,centery,length,dist,radius,angle,startX,startY,endX,endY);
	}
	
	public final static TrackSegment createTurnSeg(double centerx,double centery,double radius,double startX,double startY,double endX,double endY){		
		Vector2D v1 = new Vector2D(startX-centerx,startY-centery);
		Vector2D v2 = new Vector2D(endX-centerx,endY-centery);		
		double angle = Vector2D.angle(v1, v2);	
//		angle = (-Math.PI*2+angle)%(Math.PI*2);
		if (angle<-Math.PI) 
			angle += 2*Math.PI;
		else if (angle>Math.PI) 
			angle -= 2*Math.PI;
		double arc = Math.abs(angle);		
		double length = radius*arc;
		int type = (angle<0) ? LFT : RGT;		
		return new TrackSegment(type,centerx,centery,length,0,radius,angle,startX,startY,endX,endY);
	}
	
	public final static int getTurn(double centerx,double centery,double radius,double startX,double startY,double endX,double endY){		
		Vector2D v1 = new Vector2D(startX-centerx,startY-centery);
		Vector2D v2 = new Vector2D(endX-centerx,endY-centery);		
		double angle = Vector2D.angle(v1, v2);	
//		angle = (-Math.PI*2+angle)%(Math.PI*2);
		if (angle<-Math.PI) 
			angle += 2*Math.PI;
		else if (angle>Math.PI) 
			angle -= 2*Math.PI;
		
		return (angle<0) ? LFT : RGT;				
	}

	
	public final static double distance(double x1,double y1,double x2,double y2){
		double dx = x1-x2;
		double dy = y1-y2;
		return Math.sqrt(dx*dx+dy*dy);
	}	


	public static ObjectArrayList<TrackSegment> createSeg(double[] ex,double[] ey){
		if (ex==null || ey==null || ex.length!=ey.length || ex.length<2)
			return null;
		ObjectArrayList<TrackSegment> rs = new ObjectArrayList<TrackSegment>();
		int len = ex.length;
		int i=0;
		double dist=0;
		double xx=ex[0];
		double yy=ey[0];
		while (i<len-1){
			double[] result = new double[3];
			double x1 = xx;
			double x2 = ex[i+1];			
			double y1 = yy;
			double y2 = ey[i+1];
			if (i==len-2){
				TrackSegment ts = TrackSegment.createStraightSeg(dist, x1, y1, x2, y2);
				rs.add(ts);
				dist+=ts.length;
				break;
			}
			double x3 = ex[i+2];
			double y3 = ey[i+2];
			boolean isCircle = Geom.getCircle(x1, y1, x2, y2, x3, y3, result);
			double radius = (isCircle) ? Math.sqrt(result[2]) : Double.MAX_VALUE;
			int j=i+3;
			if (!isCircle || radius>MAXRADIUS){//is a straight line
				double allowedDist = Math.max(EPSILON, Geom.ptLineDistSq(x1, y1, x2, y2, x3, y3, null));
				double dt2 = distance(x1, y1, x2, y2);
				double dt3 = distance(x1, y1, x3, y3);
				double maxd = 0;			
				if (dt2>dt3){
					maxd = dt2;
					xx = x2;
					yy = y2;
				} else {
					maxd = dt3;
					xx = x3;
					yy = y3;
				}

				for (j=i+3;j<len;++j){
					double x = ex[j];
					double y = ey[j];
					if (Geom.ptLineDistSq(x1, y1, x2, y2, x, y, null)>allowedDist)
						break;
					double dt = distance(x1, y1, x, y);
					if (dt>maxd){
						maxd = dt;
						xx = x;
						yy = y;
					}
				}
				j--;
				if (distance(x1, y1, xx, yy)>MINLENGTH){
					TrackSegment ts = TrackSegment.createStraightSeg(dist, x1, y1, xx, yy);
					rs.add(ts);
					dist+=ts.length;
				} else {
					xx = x1;
					yy = y1;
				}
			} else {
				double ox = result[0];
				double oy = result[1];
				
				double maxa = -100000;
								
				Vector2D start = new Vector2D(x1-ox,y1-oy); 				
				double a2 = new Vector2D(x2-ox,y2-oy).angle(start);
				double a3 = new Vector2D(x3-ox,y3-oy).angle(start);				
				
				if (maxa<Math.abs(a2)){
					maxa = Math.abs(a2);
					xx = x2;
					yy = y2;
				};
				
				if (maxa<Math.abs(a3)){
					maxa = Math.abs(a3);
					xx = x3;
					yy = y3;
				};
				
				 
				for (j=i+3;j<len;++j){
					double dx = ex[j]-ox;
					double dy = ey[j]-oy;
					if (Math.abs(Math.sqrt(dx*dx+dy*dy)-radius)>EPSILON)
						break;
					double a = new Vector2D(dx,dy).angle(start);					
						
					if (maxa<Math.abs(a)){
						maxa = a;
						xx = ex[j];
						yy = ey[j];
					};
				}
				j--;				
				TrackSegment ts = TrackSegment.createTurnSeg(dist, ox, oy, radius, x1, y1, xx, yy,x2,y2);
				if (ts.length>MINLENGTH){
					rs.add(ts);
					dist += ts.length;
				} else {
					xx = x1;
					yy = y1;
				}
			}
			i = j;
		}//end of while

		return rs;
	}
	
	public static Vector2D findPointInSegAtDistToPoint(double dist,Vector2D p,TrackSegment ts){
		if (ts==null || p==null || dist<=0) return null;
		double d1 = distance(ts.startX, ts.startY, p.x, p.y);
		double d2 = distance(ts.endX, ts.endY, p.x, p.y);
		if ((d1>=dist-EPSILON) && (d1<=dist+EPSILON))
			return new Vector2D(ts.startX,ts.startY);
		
		if ((d2>=dist-EPSILON) && (d2<=dist+EPSILON))
			return new Vector2D(ts.endX,ts.endY);
				
		double x1 = ts.startX;
		double y1 = ts.startY;
		double x2 = ts.endX;
		double y2 = ts.endY;
		
		if (ts.type==STRT){
			if (d1<dist && d2<dist) return null;
			double[] rs = Geom.getLineSegCircleIntersection(x1, y1, x2, y2, p.x, p.y, dist);
			if (rs==null) return null;
			return new Vector2D(rs[0],rs[1]);
		};
		
		double d = distance(ts.centerx, ts.centery, p.x, p.y); 
		if (d>dist+ts.radius)
			return null;
		
		double[] rs = Geom.getArcCircleIntersection(ts.centerx, ts.centery,x1,y1,ts.arc, p.x, p.y, dist);
		if (rs==null) return null;		
		
		return new Vector2D(rs[0],rs[1]);
	}
	
	public static Vector2D findPointAtDistToPoint(double dist, Vector2D p,ObjectList<TrackSegment> v){
		if (v==null || v.size()==0) return null;
		for (TrackSegment t:v){
			Vector2D rs=findPointInSegAtDistToPoint(dist,p,t); 
			if (rs!=null) return rs;				
		}
		return null;
	}
		
	
	public static ObjectList<TrackSegment> createSeg(ObjectList<Vector2D> vo,double dist){
		if (vo==null || vo.size()<1)
			return null;
		Vector2D[] v = new Vector2D[vo.size()];
		int i=0;
		for (Vector2D vv:vo){
			v[i++] = new Vector2D(vv.x,vv.y);
		}
		return createSeg(v,dist);
	}
	
	public static ObjectList<TrackSegment> createSeg(Vector2D[] v,double dist){
		if (v==null || v.length<2)
			return null;
		ObjectArrayList<TrackSegment> rs = new ObjectArrayList<TrackSegment>();
		int len = v.length;
		int i=0;		
		double xx=v[0].x;
		double yy=v[0].y;
		while (i<len-1){
			double[] result = new double[3];
			double x1 = xx;
			double x2 = v[i+1].x;			
			double y1 = yy;
			double y2 = v[i+1].y;
			if (i==len-2){
				TrackSegment ts = TrackSegment.createStraightSeg(dist, x1, y1, x2, y2);
				rs.add(ts);
				dist+=ts.length;
				break;
			}
			double x3 = v[i+2].x;
			double y3 = v[i+2].y;
			boolean isCircle = Geom.getCircle(x1, y1, x2, y2, x3, y3, result);
			double radius = (isCircle) ? Math.sqrt(result[2]) : Double.MAX_VALUE;
			int j=i+3;
			if (!isCircle || radius>MAXRADIUS){//is a straight line
				double allowedDist = Math.max(EPSILON, Geom.ptLineDistSq(x1, y1, x2, y2, x3, y3, null));
				double dt2 = distance(x1, y1, x2, y2);
				double dt3 = distance(x1, y1, x3, y3);
				double maxd = 0;				
				if (dt2>dt3){
					maxd = dt2;
					xx = x2;
					yy = y2;
				} else {
					maxd = dt3;
					xx = x3;
					yy = y3;
				}

				for (j=i+3;j<len;++j){
					double x = v[j].x;
					double y = v[j].y;
					if (Geom.ptLineDistSq(x1, y1, x2, y2, x, y, null)>allowedDist)
						break;
					double dt = distance(x1, y1, x, y);
					if (dt>maxd){
						maxd = dt;
						xx = x;
						yy = y;
					}
				}
				j--;			
				if (distance(x1, y1, xx, yy)>MINLENGTH){
					TrackSegment ts = TrackSegment.createStraightSeg(dist, x1, y1, xx, yy);
					rs.add(ts);
					dist+=ts.length;
				} else {
					xx = x1;
					yy = y1;
				}
			} else {
				double ox = result[0];
				double oy = result[1];
				double maxa = -100000;
				
				Vector2D start = new Vector2D(x1-ox,y1-oy); 				
				double a2 = new Vector2D(x2-ox,y2-oy).angle(start);
				double a3 = new Vector2D(x3-ox,y3-oy).angle(start);				
				
				if (maxa<Math.abs(a2)){
					maxa = Math.abs(a2);
					xx = x2;
					yy = y2;
				};
				
				if (maxa<Math.abs(a3)){
					maxa = Math.abs(a3);
					xx = x3;
					yy = y3;
				};
				
				for (j=i+3;j<len;++j){
					double dx = v[j].x-ox;
					double dy = v[j].y-oy;
					if (Math.abs(Math.sqrt(dx*dx+dy*dy)-radius)>EPSILON)
						break;
					double a = new Vector2D(dx,dy).angle(start);					
					
					if (maxa<Math.abs(a)){
						maxa = a;
						xx = v[j].x;
						yy = v[j].y;
					};
				}
				j--;				
				TrackSegment ts = TrackSegment.createTurnSeg(dist, ox, oy, radius, x1, y1, xx, yy,x2,y2);
				if (ts.length>MINLENGTH){
					rs.add(ts);
					dist += ts.length;
				} else {
					xx = x1;
					yy = y1;
				}
			}
			i = j;
		}//end of while

		return rs;
	}
	
	public static ObjectList<TrackSegment> segmentize(Collection<Vector2D> v,double dist){
		Vector2D[] vv = new Vector2D[v.size()];
		v.toArray(vv);
		return segmentize(vv, dist);
	}
	
	public static ObjectList<TrackSegment> segmentize(Vector2D[] v,double dist){
		if (v==null || v.length<2)
			return null;
		ObjectArrayList<TrackSegment> rs = new ObjectArrayList<TrackSegment>();
		int len = v.length;
		int i=0;
		double allowedDist = EPSILON*EPSILON;
		double xx=v[0].x;
		double yy=v[0].y;
		TrackSegment ts = null;
		while (i<len-1){
			double[] result = new double[3];
			double x1 = xx;
			double x2 = v[i+1].x;			
			double y1 = yy;
			double y2 = v[i+1].y;
			if (i==len-2){
//				TrackSegment s = TrackSegment.createStraightSeg(0, x1, y1, x2, y2);
//				if (ts!=null)
//					s.distanceFromLocalOrigin = getDistNextSegment(ts, s);
//				ts = s;
//				rs.add(ts);
//				dist+=ts.length;
				break;
			}
			double x3 = v[i+2].x;
			double y3 = v[i+2].y;
			boolean isCircle = Geom.getCircle(x1, y1, x2, y2, x3, y3, result);
			double radius = (isCircle) ? Math.sqrt(result[2]) : Double.MAX_VALUE;
			int j=i+3;
			if (!isCircle || radius>MAXRADIUS){//is a straight line								
				for (j=i+3;j<len;++j){
					double x = v[j].x;
					double y = v[j].y;
					
					if (Geom.ptLineDistSq(x1, y1, x2, y2, x, y, null)>allowedDist)
						break;					
				}
				if (j>len) break;
				xx = v[j-1].x;
				yy = v[j-1].y;		
				TrackSegment s = TrackSegment.createStraightSeg(dist, x1, y1, xx, yy);
				if (ts!=null) s.distanceFromLocalOrigin = getDistNextSegment(ts, s);
				ts = s;
				rs.add(ts);						
				if (j>=len) break; 
				xx = v[j].x;
				yy = v[j].y;						
			} else {
				int r = (int)Math.round(radius);
				int maxj = -1;
				int maxr = r;
				double maxk = -1;
				Vector2D pp = null;
				Vector2D t = new Vector2D(x1+x2,y1+y2).times(0.5d);
				Vector2D q = new Vector2D(result[0],result[1]);
				double d = t.distanceSq(v[i]);
				if (i>len-3) return rs;
				if (i==len-3){
					radius = r;
					Vector2D p = t.plus(q.minus(t).normalised().times(Math.sqrt(radius*radius-d)));
					TrackSegment s = TrackSegment.createTurnSeg(dist, p.x, p.y, radius, x1, y1, x3, y3,x2,y2);
					if (ts!=null) s.distanceFromLocalOrigin = getDistNextSegment(ts, s);
					ts = s;
					rs.add(ts);			
					return rs;
				}
				int lim = 5;
				for (int rr = r-1;rr<=r+1;++rr){
					radius = rr;									
					Vector2D p = t.plus(q.minus(t).normalised().times(Math.sqrt(radius*radius-d)));
					double ox = p.x;
					double oy = p.y;
									
					boolean ok = false;
					int k = (rr==r) ? 1 : 0;
					int ti = 0;
					for (j=i+1;j<len;++j){
						double dx = v[j].x-ox;
						double dy = v[j].y-oy;						
//						System.out.print(Math.abs(Math.sqrt(dx*dx+dy*dy)-radius)+"    ");
						if (Math.abs(Math.sqrt(dx*dx+dy*dy)-radius)>=0.01d || j==len-1){
							if (j<3) break;
							double a1 = v[j-3].x;
							double a2 = v[j-2].x;
							double a3 = v[j-1].x;
							double b1 = v[j-3].y;
							double b2 = v[j-2].y;
							double b3 = v[j-1].y;
							double[] rrr = new double[3];
							Geom.getCircle(a1, b1, a2, b2, a3, b3, rrr);
							rrr[2] = Math.sqrt(rrr[2]);
							int tr = (int)Math.round(rrr[2]);
							if (Math.abs(rr-tr)<=1) ok = true;
							if (ti<lim){
								ti++;
								if (tr==rr) k++;
							}
							break;			
						}
						if (j>i+3 && ti<lim){
							ti++;
							double a1 = v[j-3].x;
							double a2 = v[j-2].x;
							double a3 = v[j-1].x;
							double b1 = v[j-3].y;
							double b2 = v[j-2].y;
							double b3 = v[j-1].y;
							double[] rrr = new double[3];
							Geom.getCircle(a1, b1, a2, b2, a3, b3, rrr);
							rrr[2] = Math.sqrt(rrr[2]);
							int tr = (int)Math.round(rrr[2]);
							if (Math.abs(rr-tr)>3) {
								ok = false;
								break;
							}
							if (tr==rr) k++;
							
						}
					}
//					System.out.println();
										
					if (j<i+3) continue;
					if (j>len) break;
					if (ok){
						if (j>maxj){
							maxj = j;
							maxr = rr;
							pp = p;
							maxk = (k+0.0d)/(ti+0.0d);
						} else if (j==maxj && maxk<(k+0.0d)/(ti+0.0d)){
							maxr = rr;
							pp = p;
							maxk = (k+0.0d)/(ti+0.0d);
						}
					}
				}
				if (j<i+3 || maxj<0) {
					i++;
					xx = v[i].x;
					yy = v[i].y;	
					continue;
				}
				
				j = maxj;
				radius = maxr;
				
				if (j==i+3){
					if (j>len-3){
						if (v[i].distance(v[i+1])>v[i+2].distance(v[i+3])){
							i++;
							xx = v[i].x;
							yy = v[i].y;	
							continue;
						}							
					}
				}
				xx = v[j-1].x;
				yy = v[j-1].y;
				TrackSegment s = TrackSegment.createTurnSeg(dist, pp.x, pp.y, radius, x1, y1, xx, yy,x2,y2);
				if (ts!=null) s.distanceFromLocalOrigin = getDistNextSegment(ts, s);
				ts = s;
				rs.add(ts);				
				if (j>=len) break;
				xx = v[j].x;
				yy = v[j].y;				
			}
			i = j;
		}//end of while

		return rs;
	}
	
	public static boolean isMiddle(Vector2D p, Vector2D p1, Vector2D p2){
		return (p.x<= Math.max(p1.x, p2.x) && p.x>= Math.min(p1.x, p2.x) && (p.y<=Math.max(p1.y, p2.y)) && p.y >= Math.min(p1.y, p2.y));
	}
	
	
	public static double getDistNextSegment(TrackSegment ts,TrackSegment n){
		double dist = ts.length+ts.distanceFromLocalOrigin;
		double[] r = new double[3];
		Vector2D s = new Vector2D(ts.endX,ts.endY);
		Vector2D e = new Vector2D(n.startX,n.startY);
		if (ts.type==0 && n.type == 0 ){
			Geom.getLineLineIntersection(ts.startX, ts.startY, ts.endX, ts.endY, n.startX, n.startY, n.endX, n.endY, r);
			Vector2D p = new Vector2D(r[0],r[1]);
			dist += p.distance(ts.endX, ts.endY)+p.distance(n.startX, n.startY); 
		} else if (ts.type==0){
			r = Geom.getLineCircleIntersection(ts.startX, ts.startY, ts.endX, ts.endY, n.centerx, n.centery, n.radius);
			if (r==null) {
				r = new double[3];
				Geom.ptLineDistSq(ts.startX, ts.startY, ts.endX, ts.endY, n.centerx, n.centery, r);				
			}
			Vector2D p = new Vector2D(r[0],r[1]);				
			if (r.length>=4 && !isMiddle(p, s, e)) p = new Vector2D(r[2],r[3]);
			Vector2D c = new Vector2D(n.centerx,n.centery);
			double angle = Vector2D.angle(p.minus(c), new Vector2D(n.startX-n.centerx,n.startY-n.centery));
			if (angle>Math.PI) 
				angle -= Math.PI*2;
			else if (angle<-Math.PI)
				angle += Math.PI*2;
			
			dist += p.distance(ts.endX, ts.endY)+Math.abs(angle)*n.radius;
		} else if (n.type==0){
			r = Geom.getLineCircleIntersection(n.startX, n.startY, n.endX, n.endY, ts.centerx, ts.centery, ts.radius);
			if (r==null) {
				r = new double[3];
				Geom.ptLineDistSq(n.startX, n.startY, n.endX, n.endY, ts.centerx, ts.centery, r);				
			}
			Vector2D p = new Vector2D(r[0],r[1]);
			if (r.length>=4 && !isMiddle(p, s, e)) p = new Vector2D(r[2],r[3]);
			Vector2D c = new Vector2D(ts.centerx,ts.centery);
			double angle = Vector2D.angle(p.minus(c), new Vector2D(ts.startX-ts.centerx,ts.startY-ts.centery));
			if (angle>Math.PI) 
				angle -= Math.PI*2;
			else if (angle<-Math.PI)
				angle += Math.PI*2;
			
			dist += p.distance(e)+Math.abs(angle)*ts.radius;
									
		} else {
			r = Geom.getCircleCircleIntersection(ts.centerx, ts.centery, ts.radius, n.centerx, n.centery, n.radius);
			if (r!=null){
				Vector2D p = new Vector2D(r[0],r[1]);
				if (r.length>=4 && !isMiddle(p, s, e)) p = new Vector2D(r[2],r[3]);
				Vector2D c = new Vector2D(n.centerx,n.centery);
				double angle = Vector2D.angle(p.minus(c), new Vector2D(n.startX-n.centerx,n.startY-n.centery));
				if (angle>Math.PI) 
					angle -= Math.PI*2;
				else if (angle<-Math.PI)
					angle += Math.PI*2;
				
				dist += Math.abs(angle)*n.radius;
				
				c = new Vector2D(ts.centerx,ts.centery);
				angle = Vector2D.angle(p.minus(c), new Vector2D(ts.startX-ts.centerx,ts.startY-ts.centery));
				if (angle>Math.PI) 
					angle -= Math.PI*2;
				else if (angle<-Math.PI)
					angle += Math.PI*2;
				
				dist += Math.abs(angle)*ts.radius;
			}
		}

		return dist;
	}
	
	public static ObjectList<TrackSegment> segmentize(Vector2D[] v,double dist,ObjectList<Segment> track){
		if (v==null || v.length<2)
			return null;
		ObjectArrayList<TrackSegment> rs = new ObjectArrayList<TrackSegment>();
		int len = v.length;
		int i=0;
		double allowedDist = EPSILON*EPSILON;
		double xx=v[0].x;
		double yy=v[0].y;
		TrackSegment ts = null;
		while (i<len-1){
			double[] result = new double[3];
			double x1 = xx;
			double x2 = v[i+1].x;			
			double y1 = yy;
			double y2 = v[i+1].y;
			if (i==len-2){
				TrackSegment s = TrackSegment.createStraightSeg(0, x1, y1, x2, y2);
				if (ts!=null)
					s.distanceFromLocalOrigin = getDistNextSegment(ts, s);
				rs.add(ts);
				dist+=ts.length;
				break;
			}
			double x3 = v[i+2].x;
			double y3 = v[i+2].y;
			boolean isCircle = Geom.getCircle(x1, y1, x2, y2, x3, y3, result);
			double radius = (isCircle) ? Math.sqrt(result[2]) : Double.MAX_VALUE;
			int j=i+3;
			if (!isCircle || radius>MAXRADIUS){//is a straight line								
				for (j=i+3;j<len;++j){
					double x = v[j].x;
					double y = v[j].y;
					
					if (Geom.ptLineDistSq(x1, y1, x2, y2, x, y, null)>allowedDist)
						break;					
				}
				if (j>len) break;
				xx = v[j-1].x;
				yy = v[j-1].y;		
				TrackSegment s = TrackSegment.createStraightSeg(0, x1, y1, xx, yy);
				if (ts!=null) s.distanceFromLocalOrigin = getDistNextSegment(ts, s);
				ts = s;
				rs.add(ts);
				dist+=ts.length;				
				if (j>=len) break; 
				xx = v[j].x;
				yy = v[j].y;						
			} else {
				int r = (int)Math.round(radius);
				int maxj = -1;
				int maxr = r;
				Vector2D pp = null;
				for (int rr = r-1;rr<=r-1;++rr){
					radius = rr;				
					Vector2D p = new Vector2D(x1+x3,y1+y3).times(0.5d);
					Vector2D q = new Vector2D(result[0],result[1]);
					p = p.plus(q.minus(p).normalised().times(radius));
					double ox = p.x;
					double oy = p.y;
													 											
					for (j=i+4;j<len;++j){
						double dx = v[j].x-ox;
						double dy = v[j].y-oy;					
						if (Math.abs(Math.sqrt(dx*dx+dy*dy)-radius)>=0.1d)
							break;			
					}
					if (j>len) break;
					if (j>maxj){
						maxj = j;
						maxr = rr;
						pp = new Vector2D(ox,oy);
					}
				}
				j = maxj;
				radius = maxr;
				xx = v[j-1].x;
				yy = v[j-1].y;
				TrackSegment s = TrackSegment.createTurnSeg(0, pp.x, pp.y, radius, x1, y1, xx, yy,x2,y2);
				if (ts!=null) s.distanceFromLocalOrigin = getDistNextSegment(ts, s);
				ts = s;
				rs.add(ts);
				dist += ts.length;
				if (j>=len) break;
				xx = v[j].x;
				yy = v[j].y;				
			}
			i = j;
		}//end of while

		return rs;
	}





	public static void line(double xx0, double yy0, double xx1, double yy1,XYSeries series)
	{
		int x0 = (int)Math.round(xx0*10);
		int y0 = (int)Math.round(yy0*10); 
		int x1 = (int)Math.round(xx1*10); 
		int y1 = (int)Math.round(yy1*10);
		int dy = y1 - y0;
		int dx = x1 - x0;
		int stepx, stepy;

		if (dy < 0) { dy = -dy;  stepy = -1; } else { stepy = 1; }
		if (dx < 0) { dx = -dx;  stepx = -1; } else { stepx = 1; }
		dy <<= 1;                                                  // dy is now 2*dy
		dx <<= 1;                                                  // dx is now 2*dx

		series.add(x0/10.0d, y0/10.0d);
		if (dx > dy) {
			int fraction = dy - (dx >> 1);                         // same as 2*dy - dx
			while (x0 != x1) {
				if (fraction >= 0) {
					y0 += stepy;
					fraction -= dx;                                // same as fraction -= 2*dx
				}
				x0 += stepx;
				fraction += dy;                                    // same as fraction -= 2*dy
				series.add( x0/10.0d, y0/10.0d);
			}
		} else {
			int fraction = dx - (dy >> 1);
			while (y0 != y1) {
				if (fraction >= 0) {
					x0 += stepx;
					fraction -= dy;
				}
				y0 += stepy;
				fraction += dx;
				series.add( x0/10.0d, y0/10.0d);
			}
		}
	}

	public static void circle(double xx,double yy,double r,XYSeries series) {
		long x = Math.round(xx*10);
		long y = Math.round(yy*10);
		long radius = Math.round(r*10);		
		long discriminant = (5 - radius<<2)>>2 ;
		long i = 0, j = radius ;
		while (i<=j) {
			series.add((x+i)/10.d,(y-j)/10.d) ;
			series.add((x+j)/10.d,(y-i)/10.d) ;
			series.add((x+i)/10.d,(y+j)/10.d) ;
			series.add((x+j)/10.d,(y+i)/10.d) ;
			series.add((x-i)/10.d,(y-j)/10.d) ;
			series.add((x-j)/10.d,(y-i)/10.d) ;
			series.add((x-i)/10.d,(y+j)/10.d) ;
			series.add((x-j)/10.d,(y+i)/10.d) ;
			i++ ;
			if (discriminant<0) {                
				discriminant += (i<<1) + 1 ;
			} else {
				j-- ;
				discriminant += 1+ (i - j)<<1 ;
			}
		}
	}
	
	
	public static void arc(double xx,double yy,double r,double startX,double startY,double arc,XYSeries series) {
		Vector2D center = new Vector2D(xx,yy);
		Vector2D start = new Vector2D(startX,startY).minus(center);		
		double length = Math.abs(arc*r);
		int num = (int)(length*2);
		if (num<1) num=1;
		arc = -arc;
		double step = arc/num;		
		double angle = 0.0d;		
		Vector2D point = null;
		for (int i=0;i<num;++i){
			point = start.rotated(angle).plus(center);
			series.add(point.x,point.y);					
			angle += step;
		}		
	}

	public static TrackSegment cutOff(TrackSegment ts,double dist){
		if (ts.distanceFromLocalOrigin+ts.length<dist)
			return null;
		if (ts.distanceFromLocalOrigin>dist)
			return ts;
		double d = dist-ts.distanceFromLocalOrigin;
		if (ts.type==STRT){
			TrackSegment t = TrackSegment.createStraightSeg(dist, ts.startX, ts.startY-d, ts.endX, ts.endY-d);
		} else {
			double arc = d/ts.radius;
			if (ts.type==LFT) arc = -arc;
			Vector2D center = new Vector2D(ts.centerx,ts.centery);
			Vector2D v = new Vector2D(ts.startX-ts.centerx,ts.startY-ts.centery);
			v = center.plus(v.rotated(-arc));
			Vector2D e = new Vector2D(ts.endX-ts.centerx,ts.endY-ts.centery);
			e = center.plus(e.rotated(-arc));
			return TrackSegment.createTurnSeg(dist, ts.centerx, ts.centery, ts.radius, v.x, v.y, e.x, e.y, center.x, center.y);
		}
		return null;
	}

	public static void drawTrack(ObjectList<TrackSegment> ts,final String title,double dist){			
		XYSeries series = new XYSeries("Curve");
		if (ts==null) return;
		for (TrackSegment t : ts){
			if (t.distanceFromLocalOrigin+t.length<dist)
				continue;
			
			t= TrackSegment.cutOff(t, dist);
			if (t.type==STRT){
				line(t.startX, t.startY, t.endX, t.endY, series);
			} else {
				arc(t.centerx, t.centery, t.radius, t.startX,t.startY,t.arc,series);								
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

	

	public static void drawTrack(ObjectList<TrackSegment> ts,final String title){			
		XYSeries series = new XYSeries("Curve");
		if (ts==null) return;
//		for (int i=0;i<numPointLeft;++i){
//		series.add(leftEgdeX[i], leftEgdeY[i]);
//		}

		for (TrackSegment t : ts){
			if (t.type==STRT){
				line(t.startX, t.startY, t.endX, t.endY, series);
			} else {
				arc(t.centerx, t.centery, t.radius, t.startX,t.startY,t.arc,series);
//				series.add(t.centerx,t.centery);
			}
		}



//		for (int i=0;i<numPointRight;++i){
//		series.add(rightEgdeX[i], rightEgdeY[i]);
//		}

		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );		
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


	public TrackSegment(int type, double centerx, double centery,
			double length, double distanceFromLocalOrigin, double radius,
			double arc, double startX, double startY, double endX, double endY) {
		this.type = type;
		this.centerx = centerx;
		this.centery = centery;
		this.length = length;
		this.distanceFromLocalOrigin = distanceFromLocalOrigin;
		this.radius = radius;
		this.arc = arc;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}



	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}	


	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}



	/**
	 * @return the centerx
	 */
	public double getCenterx() {
		return centerx;
	}



	/**
	 * @param centerx the centerx to set
	 */
	public void setCenterx(double centerx) {
		this.centerx = centerx;
	}



	/**
	 * @return the centery
	 */
	public double getCentery() {
		return centery;
	}



	/**
	 * @param centery the centery to set
	 */
	public void setCentery(double centery) {
		this.centery = centery;
	}



	/**
	 * @return the length
	 */
	public double getLength() {
		return length;
	}



	/**
	 * @param length the length to set
	 */
	public void setLength(double length) {
		this.length = length;
	}



	/**
	 * @return the distanceFromLocalOrigin
	 */
	public double getDistanceFromLocalOrigin() {
		return distanceFromLocalOrigin;
	}



	/**
	 * @param distanceFromLocalOrigin the distanceFromLocalOrigin to set
	 */
	public void setDistanceFromLocalOrigin(double distanceFromLocalOrigin) {
		this.distanceFromLocalOrigin = distanceFromLocalOrigin;
	}



	/**
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}



	/**
	 * @param radius the radius to set
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}



	/**
	 * @return the arc
	 */
	public double getArc() {
		return arc;
	}



	/**
	 * @param arc the arc to set
	 */
	public void setArc(double arc) {
		this.arc = arc;
	}



	/**
	 * @return the startX
	 */
	public double getStartX() {
		return startX;
	}



	/**
	 * @param startX the startX to set
	 */
	public void setStartX(double startX) {
		this.startX = startX;
	}



	/**
	 * @return the startY
	 */
	public double getStartY() {
		return startY;
	}



	/**
	 * @param startY the startY to set
	 */
	public void setStartY(double startY) {
		this.startY = startY;
	}



	/**
	 * @return the endX
	 */
	public double getEndX() {
		return endX;
	}



	/**
	 * @param endX the endX to set
	 */
	public void setEndX(double endX) {
		this.endX = endX;
	}



	/**
	 * @return the endY
	 */
	public double getEndY() {
		return endY;
	}



	/**
	 * @param endY the endY to set
	 */
	public void setEndY(double endY) {
		this.endY = endY;
	}





	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TrackSegment))
			return false;
		final TrackSegment other = (TrackSegment) obj;
		if (Double.doubleToLongBits(arc) != Double.doubleToLongBits(other.arc))
			return false;
		if (Double.doubleToLongBits(centerx) != Double
				.doubleToLongBits(other.centerx))
			return false;
		if (Double.doubleToLongBits(centery) != Double
				.doubleToLongBits(other.centery))
			return false;
		if (Double.doubleToLongBits(distanceFromLocalOrigin) != Double
				.doubleToLongBits(other.distanceFromLocalOrigin))
			return false;
		if (Double.doubleToLongBits(endX) != Double
				.doubleToLongBits(other.endX))
			return false;
		if (Double.doubleToLongBits(endY) != Double
				.doubleToLongBits(other.endY))
			return false;
		if (Double.doubleToLongBits(length) != Double
				.doubleToLongBits(other.length))
			return false;
		if (Double.doubleToLongBits(radius) != Double
				.doubleToLongBits(other.radius))
			return false;
		if (Double.doubleToLongBits(startX) != Double
				.doubleToLongBits(other.startX))
			return false;
		if (Double.doubleToLongBits(startY) != Double
				.doubleToLongBits(other.startY))
			return false;
		if (type != other.type)
			return false;
		return true;
	}


	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	@Override
	public String toString()
	{
		final String TAB = "    ";

		String retValue = "";

		retValue = "TrackSegment ( "
			+ super.toString() + TAB
			+ "type = " + this.type + TAB
			+ "centerx = " + this.centerx + TAB
			+ "centery = " + this.centery + TAB
			+ "length = " + this.length + TAB
			+ "distanceFromLocalOrigin = " + this.distanceFromLocalOrigin + TAB
			+ "radius = " + this.radius + TAB
			+ "arc = " + this.arc + TAB
			+ "startX = " + this.startX + TAB
			+ "startY = " + this.startY + TAB
			+ "endX = " + this.endX + TAB
			+ "endY = " + this.endY + TAB
			+ " )";

		return retValue;
	}
	
	public Vector2D[] getSegIntersection(double x1,double y1,double x2,double y2){
		double[] r = null;
		if (type==STRT){	
			r = new double[3];
			Object rs = Geom.getSegSegIntersection(x1, y1, x2, y2, startX, startY, endX, endY, r);
			if (rs!=null && rs != Geom.PARALLEL){
				return new Vector2D[]{new Vector2D(r[0],r[1])};
			}			
		} else {
			r = Geom.getSegArcIntersection(x1, y1, x2, y2, centerx, centery, radius, arc, startX, startY, endX, endY);
			if (r==null || r.length<2) return null;
			Vector2D[] rs = new Vector2D[r.length/2];
			for (int i = 0;i<rs.length;++i)
				rs[i] = new Vector2D(r[i*2],r[i*2+1]);				
			return rs;
		}
		return null;
	}

	public static ObjectArrayList<TrackSegment> combine(TrackSegment a, TrackSegment b){
		ObjectArrayList<TrackSegment> rs = new ObjectArrayList<TrackSegment>();
		double dist1 = a.distanceFromLocalOrigin;
		double l1 = a.length;
		double dist2 = b.distanceFromLocalOrigin;
		double l2 = b.length;
		 
		if (a.type==b.type){
			if (a.type!=STRT){
				if (dist1+l1<dist2){
					rs.add(a);
					rs.add(b);
					return rs;
				} else if (dist2+l2<dist1){
					rs.add(b);
					rs.add(a);
					return rs;
				}				
			}	
			if (Math.abs(a.radius-b.radius)<=0.1){
				double d = Math.min(dist1, dist2);
				double e = Math.max(dist1+l1, dist2+l2);
				double l = e-d;
				double r = Math.round(a.radius);
				double startX = (dist1<dist2) ? a.startX : b.startX;
				double startY = (dist1<dist2) ? a.startY : b.startY;
				Vector2D v = new Vector2D(startX-a.centerx,startY-a.centery);
				Vector2D center = new Vector2D(a.centerx,a.centery);
				double arc = (r==0) ? 0 : l/r;
				if (a.type==LFT) arc = -arc;
				if (arc!=0) 
					v = center.plus(v.rotated(arc));
				else v = v.plus(new Vector2D(0,l));
				double endX = v.x;
				double endY = v.y;			
				TrackSegment ts = (a.type==STRT) ? createStraightSeg(d, 0, 0, 0, e) : createTurnSeg(d, a.centerx, a.centery, a.radius, startX, startY, endX, endY, a.centerx, a.centery);
				rs.add(ts);
			} else {
				rs.add(a);
//				rs.add(b);
			}
		} else {
			if (dist1>dist2+l2){
				rs.add(b);
				rs.add(a);				
			} else if (dist2>dist1+l1){
				rs.add(a);
				rs.add(b);			
			} else if (dist1+l1>dist2+l2){
				rs.add(a);
			} else {
				rs.add(a);
			}
		}
		
		return rs;
	}

}
