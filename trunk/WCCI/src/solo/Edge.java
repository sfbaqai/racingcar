/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import cern.colt.list.DoubleArrayList;

import com.graphbuilder.geom.Geom;

/**
 * @author kokichi3000
 *
 */
public final class Edge {
//	public final static double ANGLEACCURACY =100.0d;
	public final static int NUM_POINTS=150;
	public final static double DELTA = 0.005;
	ObjectArrayList<Vector2D> allPoints = null;
	DoubleArrayList x = null;
	DoubleArrayList y = null;
	DoubleArrayList allLengths = null;	
	Object2IntMap<Vector2D> p2l = null;
	double straightDist = 0;
	double dmin = Double.MAX_VALUE;
	int size =0;	
	double totalLength =0;
	Vector2D center = null;
	double radius = Double.MAX_VALUE;
	Vector2D nextCenter = null;
	double nextRadius = 0;
	int index = 0;
	int nIndex = -1;

	public Edge(){
	}

	public double calculateRadius(){
		if (index<size && index>=-1) return calculateRadius(index);
		return -1;
	}

	public double calculateRadius(double[] initialGuess){
//		int index = y.binarySearch(straightDist);
//		if (index<0) index = -index;
		if (index<size && index>=-1) return calculateRadius(index,initialGuess);
		return -1;
	}

	public boolean isStraightLine(Vector2D p1,Vector2D p2,Vector2D p3){
		double[] r = new double[3];
		boolean isCirle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r);
		if (!isCirle || r[2]>=100000) return true;
		return false;
	}

	public boolean isPointOnEdge(Vector2D hh){
		double d = 0;
		if (dmin==Double.MAX_VALUE){
			d = calculateRadius();
			if (d==-1) return false;
		} else d = dmin;
		Vector2D p1 = allPoints.get(size-1);
		Vector2D p2 = allPoints.get(size-2);
		Vector2D p3 = allPoints.get(size-3);
		if (d<=0.0001){//exactly correct guess
			double dd = (radius!=Double.MAX_VALUE) ? center.distance(hh)-radius : Geom.ptLineDistSq(p1.x, p1.y, p2.x, p2.y, hh.x, hh.y, null);
			if (radius!=Double.MAX_VALUE) dd*=dd;
			if (dd<=0.01) return true;
		}

		if (isStraightLine(p1, p2, hh))	return true;
		double[] r = new double[3];
		boolean isCircle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r);
		if (isCircle){
			double dd = hh.distance(new Vector2D(r[0],r[1]))-Math.sqrt(r[2]);
			if (dd>=-1) return true;			
		}

		return false;//false means UNKNOWN
	}

	public double radiusNextSeg(double[] r){
		if (r!=null && r.length>=3 && nextCenter!=null){
			r[0] = nextCenter.x;
			r[1] = nextCenter.y;
			r[2] = nextRadius*nextRadius;
		}
		return nextRadius;
	}

	public double calculateRadius(int index,double[] initialGuess){ 
		if (size<index+2)
			return -1;
		double x0 = allPoints.get(0).x;
		if (index+4==size){			
			Vector2D p1 = allPoints.get(index+1);
			Vector2D p2 = allPoints.get(index+2);
			Vector2D p3 = allPoints.get(index+3);				
			double[] r = new double[3];
			boolean isCircle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r);
			if (isCircle){
				r[2] = Math.sqrt(r[2]);
//				Geom.getCircle2(p1, p2, new Vector2D(x0,0), new Vector2D(x0,1), r);
//				r[2] = Math.sqrt(r[2]);
				double lx = r[0]-r[2]-x0;
				double rx = r[0]+r[2]-x0;
				if (lx>0.5 || rx<-0.5 || (lx*rx<0 && (lx<-0.5 && rx>0.5))){
					Geom.getCircle2(p1, p3, new Vector2D(x0,0), new Vector2D(x0,1), r);					
					r[2] = Math.sqrt(r[2]);
				}
				dmin = 0;
				radius = r[2];
				center = new Vector2D(r[0],r[1]);
				
			}
			return dmin;
		}
		
		if (index+4>size){
			dmin = 0;
			center = new Vector2D(initialGuess[0],initialGuess[1]);
			radius = initialGuess[2];
			int n = 0;
			for (int i=index+1;i<size;++i){
				n++;
				Vector2D p = allPoints.get(i);
				double tmp = p.distance(center)-radius; 
				dmin += tmp*tmp;				
			}
			dmin /= n;
			return dmin;	
		}
		
		CircleFitter cf = new CircleFitter(initialGuess.clone(),allPoints.elements(),index+1,index+3);

		try{
			cf.fit();
			double r = cf.getEstimatedRadius();
			double d = 0;
			int n = 0;
			Vector2D o = cf.getEstimatedCenter();
			this.center = o;
			this.radius = r;
			for (int i=index+3;i<size;++i){
				n++;
				Vector2D p = allPoints.get(i);
				double tmp = p.distance(o)-r; 				
				if (tmp>=1e-4){
					nIndex = i;
					break;
				}
			}

				if (nIndex!=-1){
//					if (nIndex>index+3 && nIndex<size){
//						cf = new CircleFitter(initialGuess.clone(),allPoints.elements(),index+1,nIndex-1);
//						cf.fit();
//						this.center = cf.getEstimatedCenter();
//						this.radius = cf.getEstimatedRadius();						
//					} else {
//						this.center = new Vector2D(initialGuess[0],initialGuess[1]);
//						this.radius = initialGuess[2];
//					}					
					
					if (nIndex<size-2){
						cf = new CircleFitter(initialGuess,allPoints.elements(),nIndex,size-1);
						cf.fit();
						this.nextCenter = cf.getEstimatedCenter();
						this.nextRadius = cf.getEstimatedRadius();
					}
				}
								
		} catch (Exception e){
			e.printStackTrace();
			dmin = Double.MAX_VALUE;
		}

		if (dmin<Double.MAX_VALUE)
			return dmin;

		return -1;
	}
	
	/*public void calculateRadiusWhileInTurn(Segment seg,double tW,double toMiddle,double dist){
		double r = seg.radius;
		double rad = 0;
		int i=0;
		for (i=0;i<size;++i)
			if (allPoints.get(i).y>=0)
				break;
		Vector2D p1 = allPoints.get(i);
		Vector2D p2 = allPoints.get(i+1);
		Vector2D p3 = allPoints.get(i+2);
					
		double[] rr = new double[3];
		boolean isCircle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, rr);
		if (isCircle){
			rr[2] = Math.sqrt(rr[2]);
		}
		CircleFitter cf = new CircleFitter(rr.clone(),allPoints.elements(),i,i+3);		
		try{
			cf.fit();	
			radius = (int)Math.round(cf.getEstimatedRadius());
			rad = (int)Math.round(cf.getEstimatedRadius()+tW); 
			double sign = (cf.getEstimatedCenterX()<0) ? -1 : 1;
			if (rad != seg.radius){
				int nr = seg.map.get(rad); 
				if (nr==seg.map.defaultReturnValue()) nr = 0;
				nr++;
				seg.map.put(rad, nr);
				if  (nr<=seg.map.get(r)) rad = r;
			}
			center = new Vector2D(sign*r+toMiddle,0);				
		} catch (Exception e) {
			// TODO: handle exception
		}		
		Vector2D end = null;
		for (i=0;i<size;++i){
//			System.out.println(allPoints.get(i).distance(center)+tW-rad);
			if (Math.abs(allPoints.get(i).distance(center)+tW-rad)>=0.0001d) break;
		}
		if (i==0){
			index=-1;
			nIndex = -1;
			return;
		}
		end = allPoints.get(i-1);
		end = center.plus(end.minus(center).normalised().times(rad));		
		TrackSegment ts = TrackSegment.createTurnSeg(dist, center.x, center.y, rad, 0, 0, end.x, end.y, center.x, center.y);
		seg.combine(ts);
		index = -1;
		nIndex = i;
		
		if (nIndex<size-2){
			p1 = allPoints.get(size-3);
			p2 = allPoints.get(size-2);
			p3 = allPoints.get(size-1);
			isCircle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, rr);
			if (isCircle){
				rr[2] = Math.sqrt(rr[2]);
			}
			cf = new CircleFitter(rr.clone(),allPoints.elements(),nIndex,size-1);
			cf.fit();
			this.nextCenter = cf.getEstimatedCenter();
			this.nextRadius = cf.getEstimatedRadius();
		}
	}
	
	
	public Segment calculateRadiusWhileNotInTurn(Segment seg,double tW,double toMiddle,double dist){
		double r = (seg!=null) ? seg.radius : -1;
		int rad = 0;
		int i=0;		
		double x0 = allPoints.get(0).x;
		double[] rr = new double[3];
		if (index+2>=size) return null;
		Vector2D p1 = allPoints.get(index+1);
		Vector2D p2 = allPoints.get(index+2);
		
		Vector2D p = null;
		if (index+3==size){			
			p = allPoints.get(size-1);
			Geom.getCircle2(p1, p2, new Vector2D(x0,0), new Vector2D(x0,1), rr);					
			rr[2] = Math.sqrt(rr[2]);
			rad = (int)Math.round(rr[2]+tW);
			radius = (int)Math.round(rr[2]);
			center = new Vector2D(rr[0],rr[1]);

		} else {			
			Vector2D p3 = allPoints.get(index+3);
			if (index+4==size){
				p = allPoints.get(size-1);
				boolean isCircle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, rr);
				if (isCircle){
					rr[2] = Math.sqrt(rr[2]);
					double lx = rr[0]-rr[2]-x0;
					double rx = rr[0]+rr[2]-x0;
					if (lx>0.5 || rx<-0.5 || (lx*rx<0 && (lx<-0.5 && rx>0.5))){
						Geom.getCircle2(p1, p3, new Vector2D(x0,0), new Vector2D(x0,1), rr);					
						rr[2] = Math.sqrt(rr[2]);
					}
					rad = (int)Math.round(rr[2]+tW);
					radius = (int)Math.round(rr[2]);
					center = new Vector2D(rr[0],rr[1]);

				}
			} else {											
				boolean isCircle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, rr);
				if (isCircle){
					rr[2] = Math.sqrt(rr[2]);
				}
				CircleFitter cf = new CircleFitter(rr,allPoints.elements(),index+1,index+4);		
				try{
					cf.fit();	
					rad = (int)Math.round(cf.getEstimatedRadius()+tW); 
					double xx = cf.getEstimatedCenterX();
					double r0 = cf.getEstimatedRadius();
					double lx = xx-r0-x0;
					double rx = xx+r0-x0;
					if (lx>0.5 || rx<-0.5 || (lx*rx<0 && (lx<-0.5 && rx>0.5))){
						Geom.getCircle2(p1, p3, new Vector2D(x0,0), new Vector2D(x0,1), rr);					
						rr[2] = Math.sqrt(rr[2]);
						rad = (int)Math.round(rr[2]+tW);
						xx = rr[0];				
					}					
					
				} catch (Exception e) {
					// TODO: handle exception
				}
												
				center = new Vector2D(rr[0],rr[1]);
				radius = rr[2];
				for (i=index+4;i<size;++i){			
					if (Math.abs(allPoints.get(i).distance(center)-radius)>=0.0001d) break;
				}
				
				p = allPoints.get(i-1);						
				nIndex = i;					
				if (nIndex<size-3){
					p1 = allPoints.get(size-3);
					p2 = allPoints.get(size-2);
					p3 = allPoints.get(size-1);
					isCircle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, rr);
					if (isCircle){
						rr[2] = Math.sqrt(rr[2]);
					}
					cf = new CircleFitter(rr.clone(),allPoints.elements(),nIndex,size-1);
					cf.fit();
					this.nextCenter = cf.getEstimatedCenter();
					this.nextRadius = cf.getEstimatedRadius();
				}
			}
		}
		
		
		if (p!=null) p = center.plus(p.minus(center).normalised().times(rad));
		double l = center.y;					
		Vector2D start = allPoints.get(index+1);
		
		double angle =0;
		if (start!=null){
			start = center.plus(start.minus(center).normalised().times(rad));
			double xx = (center.x==0) ? 0 : -center.x;
			angle= Math.abs(Vector2D.angle(start.minus(center),new Vector2D(xx,0)));
			l += rad*angle;
		}
		
		if (p!=null && start!=null){
			TrackSegment ts = TrackSegment.createTurnSeg(dist+l, center.x, center.y, rad, start.x, start.y, p.x, p.y, center.x, center.y);
			if (rad>0 && seg!=null) 
				seg.combine(ts);
			else return new Segment(ts);
		}
		return seg;
	}//*/
	
	
	public double reCalculateRadius(){		
			double[] r = new double[3];			
			r[0] = center.x;
			r[1] = center.y;
			r[2] = radius;
			int sz = (nIndex>=4) ? nIndex : size;
			if (center!=null){
				CircleFitter cf = new CircleFitter(r,allPoints.elements(),index+1,sz-1);
				if (sz-index<=4) return -1;
				try{
					cf.fit();
					dmin = cf.getLMAObject().chi2Goodness();	
					center.x = r[0];
					center.y = r[1];
					radius = r[2];
					int n = 0;
					double d = 0;
					for (int i=index+1;i<size;++i){
						n++;
						Vector2D p = allPoints.get(i);
						double tmp = p.distance(center)-radius; 
						
						if (n>3 && (d+tmp*tmp)/(n-3)>=1e-6){
							nIndex = i;
							n--;
							break;
						}
						d += tmp*tmp;
					}
					dmin = d/(n-3);										
					if (nIndex!=-1 && nIndex<=size-3){
						Vector2D p1 = allPoints.get(nIndex);
						Vector2D p2 = allPoints.get((nIndex+size-1)/2);
						Vector2D p3 = allPoints.get(size-1);
						boolean isCircle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r);
						if (isCircle){
							r[2] = Math.sqrt(r[2]);
							nextRadius = r[2];
							nextCenter = new Vector2D(r[0],r[1]);
						}
					}									
				} catch (Exception e) {
					// TODO: handle exception
				}
				return dmin;
			}
			return -1;
	}
		


	public double calculateRadius(int index){
		double[] r = new double[3];			
		
		if (center!=null){
			int n = 0;
			double d = 0;
			for (int i=index+1;i<size;++i){
				n++;
				Vector2D p = allPoints.get(i);
				double tmp = p.distance(center)-radius; 
				
				if (n>3 && (d+tmp*tmp)/(n-3)>=1e-6){
					nIndex = i;
					n--;
					break;
				}
				d += tmp*tmp;
			}
			dmin = d/(n-3);										
			if (nIndex!=-1 && nIndex<=size-3){
				Vector2D p1 = allPoints.get(nIndex);
				Vector2D p2 = allPoints.get((nIndex+size-1)/2);
				Vector2D p3 = allPoints.get(size-1);
				boolean isCircle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r);
				if (isCircle){
					r[2] = Math.sqrt(r[2]);
					nextRadius = r[2];
					nextCenter = new Vector2D(r[0],r[1]);
				}
			}
			return dmin;
		}
		if (straightDist<allPoints.get(size-1).y){
			double x0 = allPoints.get(0).x;
			if (size>index+3){
				Vector2D p1 = allPoints.get(index+1);
				Vector2D p2 = allPoints.get(index+2);
				Vector2D p3 = allPoints.get(index+3);				
	
				boolean isCircle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r);
				if (isCircle){								
					r[2] = Math.sqrt(r[2]);
					
					double lx = r[0]-r[2]-x0;
					double rx = r[0]+r[2]-x0;
					if (lx>0.5 || rx<-0.5 || (lx*rx<0 && (lx<-0.5 && rx>0.5))){
						Geom.getCircle2(p1, p3, new Vector2D(x0,0), new Vector2D(x0,1), r);					
						r[2] = Math.sqrt(r[2]);
					}
					
					
					
					int n = 0;
					double d = 0;
					Vector2D o = new Vector2D(r[0],r[1]);
					for (int i=index+1;i<size;++i){
						n++;
						Vector2D p = allPoints.get(i);
						double tmp = p.distance(o)-r[2]; 
						
						if (n>3 && (d+tmp*tmp)/(n-3)>=1e-6){
							nIndex = i;
							n--;
							break;
						}
						d += tmp*tmp;
					}
					dmin = d/(n-3);							
					center = new Vector2D(r[0],r[1]);
					radius = r[2];
					
					if (nIndex!=-1 && nIndex<=size-3){
						p1 = allPoints.get(size-2);
						p2 = allPoints.get(size-3);
						p3 = allPoints.get(size-1);
						isCircle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r);
						if (isCircle){
							r[2] = Math.sqrt(r[2]);
							nextRadius = r[2];
							nextCenter = new Vector2D(r[0],r[1]);
						}
					}
					return dmin;
				}
			} else if (size>index+2){
				Vector2D p1 = allPoints.get(index+1);
				Vector2D p2 = allPoints.get(size-1);
				Geom.getCircle2(p1, p2, new Vector2D(x0,0), new Vector2D(x0,1), r);
				r[2] = Math.sqrt(r[2]);
				dmin = 0;
				center = new Vector2D(r[0],r[1]);
				radius = r[2];
				return dmin;
			}
		}		
		return -1;
	}

	public Edge(double[] xx,double[] yy){
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		size = xx.length;
		int sz = Math.max(size, NUM_POINTS);

		double[] aL = new double[sz];
		Vector2D[] aP = new Vector2D[sz];
		p2l = new Object2IntOpenHashMap<Vector2D>(sz);
		straightDist = 0;

		Vector2D prev = new Vector2D(xx[0],yy[0]);
		double x0 = xx[0];
		double len = 0;
		int j = 0;
		for (int i=0;i<size;++i){
			double x = xx[i];
			double y = yy[i];
			if (i>0 && y<yy[i-1])
				continue;
			Vector2D p =new Vector2D(x,y);
			aP[j] = p;
			len += p.distance(prev);
			aL[j] = len;
			p2l.put(p, j);						
			prev = p;
			if (x<=x0+DELTA && x>=x0-DELTA) {
				if (straightDist<y) straightDist = y;
				index = j;
			}
			j++;
		}
		this.size = j;
		allPoints = ObjectArrayList.wrap(aP);		
		allLengths = new DoubleArrayList(aL);
		totalLength = len;		
		allLengths.setSize(size);
		allPoints.size(size);
		x.setSize(size);
		y.setSize(size);
		if (index==0) index = -1;
//		calculateRadius(index);
	}

	public Edge(double[] xx,double[] yy,int size){
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);

		int sz = Math.max(size, NUM_POINTS);
		double[] aL = new double[sz];
		Vector2D[] aP = new Vector2D[sz];		
		p2l = new Object2IntOpenHashMap<Vector2D>(sz);
		straightDist = 0;		
		Vector2D prev = new Vector2D(xx[0],yy[0]);
		double len = 0;
		double x0 = xx[0];
		int j = 0;
		for (int i=0;i<size;++i){
			double x = xx[i];
			double y = yy[i];
			if (j>0 && y<prev.y)
				continue;
			Vector2D p =new Vector2D(x,y);
			aP[j] = p;
			len += p.distance(prev);
			aL[j] = len;
			p2l.put(p, j);						
			prev = p;
			if (x<=x0+DELTA && x>=x0-DELTA) {
				if (straightDist<y) straightDist = y;
				index = j;
			}
			j++;
		}
		size = j;		
		this.size = size;
		totalLength = len;
		allPoints = ObjectArrayList.wrap(aP,size);		
		allLengths = new DoubleArrayList(aL);
		x.setSize(size);
		y.setSize(size);
		allLengths.setSize(size);
		if (index==0) index = -1;
//		calculateRadius(index);
	}

	public Edge(Vector2D[] v,int size){		
		int sz = Math.max(size, NUM_POINTS);

		double[] xx = new double[sz];
		double[] yy = new double[sz];
		double[] aL = new double[sz];		
		p2l = new Object2IntOpenHashMap<Vector2D>(sz);						
		allPoints = ObjectArrayList.wrap(v,size);


		Vector2D prev = v[0];
		double x0 = prev.x;
		double len = 0;
		straightDist = 0;		
		int j = 0;
		for (int i=0;i<size;++i){
			Vector2D p = v[i];
			double x = p.x;
			double y = p.y;			
			xx[i] = x;
			yy[i] = y;
			if (j>0 && y<prev.y)
				continue;
			len += p.distance(prev);
			aL[j] = len;						
			p2l.put(p, j);									
			prev = p;
			if (x<=x0+DELTA && x>=x0-DELTA) {
				if (straightDist<y) straightDist = y;
				index = j;
			}
			j++;
		}
		size = j;		
		this.size = size;
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		allLengths = new DoubleArrayList(aL);
		totalLength = len;
		x.setSize(size);
		y.setSize(size);
		allLengths.setSize(size);
		allPoints.size(size);
		if (index==0) index = -1;
//		calculateRadius(index);
	}

	public Edge(Vector2D[] v){
		size = v.length;
		int sz = Math.max(size, NUM_POINTS);

		double[] xx = new double[sz];
		double[] yy = new double[sz];
		double[] aL = new double[sz];		
		p2l = new Object2IntOpenHashMap<Vector2D>(sz);					
		allPoints = ObjectArrayList.wrap(v,size);

		straightDist = 0;
		Vector2D prev = v[0];
		double len = 0;
		double x0 = prev.x;
		int j = 0;
		for (int i=0;i<size;++i){
			Vector2D p = v[i];
			double x = p.x;
			double y = p.y;			
			xx[i] = x;
			yy[i] = y;
			if (j>0 && y<prev.y)
				continue;
			len += p.distance(prev);
			aL[j] = len;						
			p2l.put(p, j);									
			prev = p;
			if (x<=x0+DELTA && x>=x0-DELTA) {
				if (straightDist<y) straightDist = y;
				index = j;
			}
			j++;
		}	
		this.size =j;
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		allLengths = new DoubleArrayList(aL);
		totalLength = len;
		x.setSize(size);
		y.setSize(size);
		allLengths.setSize(size);
		if (index==0) index = -1;
//		calculateRadius(index);
	}

	public final Vector2D getHighestPoint(){
		return allPoints.get(size-1);
	}

	public final Vector2D getLowestPoint(){
		return allPoints.get(0);
	}

	public final Vector2D locatePointAtLength(double length){
		if (allPoints==null || allLengths==null || size<2 || length<0) return null;		
		int index = allLengths.binarySearch(length);				
		Vector2D[] allPoints = this.allPoints.elements();
		if (index>=0)
			return new Vector2D(allPoints[index]);


		if (index<0) index = -index+1;
		Vector2D t = null;
		Vector2D p = null;
		if (index>=size){
			t = allPoints[size-1].minus(allPoints[size-2]).normalized();
			p = allPoints[size-1];
			return p.plus(t.times(length-totalLength));
		} else {
			t = allPoints[index].minus(allPoints[index-1]).normalized();
			p = allPoints[index-1];
		}

		return p.plus(t.times(length-allLengths.getQuick(index-1)));
	}

	public final Vector2D estimatePointOnEdge(double length,Vector2D hP){
		if (size<2) return null;
		Vector2D lastPoint = allPoints.get(size-1);			
		if (length<totalLength || hP==null || hP.equals(lastPoint))
			return locatePointAtLength(length);
		double d = length-totalLength;		

		Vector2D t = hP.minus(lastPoint).normalized();		
		return lastPoint.plus(t.times(d));		
	}

	public boolean isStraight(Vector2D[] points){
		if (points==null || points.length<2) return false;
		int len = points.length;
		if (len==2) return true;
		Vector2D v0 = points[0];
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (int i=1;i<len;++i){
			Vector2D v = points[i];
			double a = v.minus(v0).angle();
			if (a<min) min = a;
			if (a>max) max = a;
		}
		return (Math.abs(max-min) < 0.1);
	}

	public final int turn(){
		double sumx = 0;
		double[] xx = x.elements();
		for (int i=0;i<size;++i) sumx +=xx[i];
		double mean = sumx/size;
		double highestx = xx[size-1];		
		if (highestx>mean+DELTA)
			return MyDriver.TURNRIGHT;
		if (highestx<mean-DELTA)
			return MyDriver.TURNLEFT;			

		return MyDriver.STRAIGHT;
	}


	public final void removeLastPoint(){
		size--;
		Vector2D lastPoint = allPoints.get(size);		
		if (straightDist==lastPoint.y ) straightDist=(size>=1) ? allPoints.get(size-1).y : 0;
		p2l.remove(lastPoint);
		allPoints.remove(size);
		x.remove(size);
		y.remove(size);
		allLengths.remove(size);		
	}

	public final void append(Vector2D p){
		Vector2D lastPoint = allPoints.get(size-1);
		size++;
		this.x.add(p.x);
		this.y.add(p.y);		
		this.allPoints.add(p);
		totalLength += p.distance(lastPoint);		
		this.allLengths.add(totalLength);
		this.p2l.put(p, size-1);
		double x0 = x.getQuick(0);
		if (straightDist==lastPoint.y && straightDist<p.y && p.x<=x0+DELTA && p.x>=x0-DELTA) {
			straightDist=p.y;
			index++;
		}
//		if (index<size) calculateRadius(index);
	}

	public final Vector2D get(int index){
		if (index<0 || index>=size)
			return null;		
		return allPoints.get(index);
	}

	public final static void drawEdge(Edge edge,final String title){			
		XYSeries series = new XYSeries("Curve");

		for (int i=0;i<edge.size;++i){
			Vector2D v = edge.get(i);
			series.add(v.x,v.y);
		}

		if (edge.center!=null){
//			if (edge.radius<550) TrackSegment.circle(edge.center.x, edge.center.y, edge.radius, series);
			series.add(edge.center.x, edge.center.y);
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

	public final static void drawEdge(Edge edge,XYSeries series){					

		for (int i=0;i<edge.size;++i){
			Vector2D v = edge.get(i);
			series.add(v.x,v.y);
		}

		if (edge.center!=null){
			TrackSegment.circle(edge.center.x, edge.center.y, edge.radius, series);
		}
		series.add(edge.center.x, edge.center.y);

	}


}
