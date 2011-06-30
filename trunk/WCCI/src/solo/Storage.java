package solo;

import java.util.Arrays;

public final class Storage {
	private static final int MAX_SIZE = 2<<15;
	public static final int[][] allMaps = new int[MAX_SIZE][];
	public static final int[][] allAppear = new int[MAX_SIZE][];
	public static final int[][] allTypes = new int[MAX_SIZE][];
	public static final int[][] allRadius = new int[MAX_SIZE][];
	public static final int[][] allMidIndx = new int[MAX_SIZE][];
//	private static int[][] allMap_N = new int[MAX_SIZE][];
	public static int[] occupied = new int[MAX_SIZE];
	public static int numMap = 128;
	static {
		int MAP_SZ = Segment.MAX_RADIUS<<1;
		int APPEAR_SZ = 200;
		for (int i = numMap-1;i>=0;--i){
			allMaps[i] = new int[MAP_SZ];
			allAppear[i] = new int[APPEAR_SZ];
			allRadius[i] = new int[APPEAR_SZ];
			allTypes[i] = new int[APPEAR_SZ];
			allMidIndx[i] = new int[APPEAR_SZ];
		}		
	}
	
	public int SIZE_N = 0;
	public final int[] totalRad_N = new int[MAX_SIZE];	
	public final int[] appearRad_N = new int[MAX_SIZE];
	public final int[] maxRad = new int[MAX_SIZE];	
	public int[] mapIndx = new int[MAX_SIZE];	
	public int[] allIndex = new int[MAX_SIZE];
	public int numIndex = 0;
	public int which = 0;
	public Storage(){
		Arrays.fill(mapIndx, -1);
	}
	
	public void setWhich(int wh){
		which = wh;
	}
	
	public void setRowLen(int n){
		SIZE_N = 1;
		int i = 2;
		while (i<=n) {
			i<<=1;
			SIZE_N++;
		}
	}
	
	public void clearAll(){
		for (int i = numIndex-1;i>=0;--i){
			int j = allIndex[i];
			int sz = appearRad_N[j];
			int mapIndx = this.mapIndx[j];
			int[] map = allMaps[mapIndx];
			int[] appearRad = allAppear[mapIndx];
			for (int k = sz-1;k>=0;--k){
				int rr = appearRad[k];
				map[rr] = 0;
				appearRad[k] = 0;
			}
			sz = totalRad_N[j];
			int[] midIndx = allMidIndx[mapIndx];
			int[] aRadius = allRadius[mapIndx];
			int[] aTypes = allTypes[mapIndx];
			for (int k = sz-1;k>=0;--k){
				int indx = midIndx[k];
				aRadius[indx] = 0;
				aTypes[indx] = -3;
				midIndx[k] = 0;
			}
			appearRad_N[j] = 0;
			totalRad_N[j] = 0;
			maxRad[j] = 0;
			allIndex[i] = 0;
			occupied[mapIndx] = 0;
			this.mapIndx[j] = -1;
		}
		numIndex = 0;
	}
	
	public void store(int startIndx,int midIndx,int endIndx,int tp,double rad){
		if (SIZE_N==0) return;
		int j = (startIndx<<SIZE_N)+endIndx;
		int indx = mapIndx[j];		
		if (indx<0){
			for (int i = 0;i<MAX_SIZE;++i){
				if (occupied[i]==0){
					indx = i;
					occupied[i] = 1;					
					break;
				}
			}
			mapIndx[j] = indx;
			allIndex[numIndex++] = j;
		}
		int[] map = allMaps[indx];
		int[] appear = allAppear[indx];
		int[] aRads = allRadius[indx];
		int[] aMidIndx = allMidIndx[indx];
		int[] aTypes = allTypes[indx];
		if (map==null){
			map = new int[Segment.MAX_RADIUS<<1];
			appear = new int[100];
			aRads = new int[100];
			aMidIndx = new int[100];
			aTypes = new int[100];
			allAppear[numMap] = appear;
			allRadius[numMap] = aRads;
			allMidIndx[numMap] = aMidIndx;
			allTypes[numMap] = aTypes;
			allMaps[numMap++] = map;
		}
		int total_N = totalRad_N[j];
		midIndx-=startIndx;
		if (midIndx<0 || midIndx>=aRads.length || aRads[midIndx]!=0) return;
		if (tp==0){
			for (int i=total_N-1;i>=0;--i)
				if (aMidIndx[i]==midIndx) return;
		}
		aTypes[midIndx] = tp;
		aMidIndx[total_N++] = midIndx;
		if (tp!=Segment.UNKNOWN){
			int er = (tp==0) ? 0 : (int)Math.round(rad+which*tp*CircleDriver2.tW);
			if (er>=Segment.MAX_RADIUS-1) {
				er = 0;
				tp = 0;
			}
			if (tp==1) er+=Segment.MAX_RADIUS;
			if (er>=0 && map[er]==0)
				appear[ appearRad_N[j]++ ] = er;			
			
			if (er>=0) map[er]++;
			aRads[midIndx] = er;
			int mr = maxRad[j];					
			if (mr!=er && er>=0 && mr>=0 && map[mr]<=map[er]) maxRad[j] = er;	
		}
		totalRad_N[j] = total_N;
	}
	
	public int getRadiusFrom2Point(int startIndx,int endIndx){
		int j = (startIndx<<SIZE_N)+endIndx;
		int indx = mapIndx[j];
		if (indx<0) return -1;
		int[] map = allMaps[indx];
		if (map==null) return -1;		
		int[] aTypes = allTypes[indx];
		if (aTypes[0]==Segment.UNKNOWN) return 0;
		int[] aRads = allRadius[indx];
		int[] aMidIndx = allMidIndx[indx];
		if (aRads[0]==0){
			int total_N = totalRad_N[j];						
			for (int i=total_N-1;i>=0;--i)
				if (aMidIndx[i]==0) return 0;
			return -1;
		} 
		return aRads[0];
	}
	
	public void toSegment(Vector2D[] v,int startIndx,int midIndx,int endIndx,Segment s){
		int j = (startIndx<<SIZE_N)+endIndx;
		int indx = mapIndx[j];
		int[] map = allMaps[indx];
		if (map==null) return;		
		
		int[] aTypes = allTypes[indx];
		midIndx-=startIndx;
		int tp = aTypes[midIndx];
		if (tp==Segment.UNKNOWN) {
			s.type = Segment.UNKNOWN;
			return;
		}
		int[] aRads = allRadius[indx];
		s.startIndex = startIndx;
		s.endIndex = endIndx;
		s.num = endIndx+1-startIndx;
		if (s.start==null) 
			s.start = new Vector2D(v[startIndx]);
		else s.start.copy(v[startIndx]);
		
		if (s.end==null) 
			s.end = new Vector2D(v[endIndx]);
		else s.end.copy(v[endIndx]);
		
		int rad = aRads[midIndx];
		if (tp==1) rad-=Segment.MAX_RADIUS;
		s.radius = rad - which*tp*CircleDriver2.tW;		
		s.type = tp;
		if (tp!=0){
			if (s.center==null) s.center = new Vector2D();
			Segment.circle(s.start, s.end, tp, s.radius,s.center);
		}
	}
	
	public void toSegment(Vector2D[] v,int startIndx,int endIndx,Segment s){
		int j = (startIndx<<SIZE_N)+endIndx;
		int indx = mapIndx[j];
		int[] map = allMaps[indx];
		if (map==null) return;		
		
		int[] aTypes = allTypes[indx];		
		int[] aRads = allRadius[indx];
		int[] aMidIndx = allMidIndx[indx];
		s.startIndex = startIndx;
		s.endIndex = endIndx;
		s.num = endIndx+1-startIndx;
		if (s.start==null) 
			s.start = new Vector2D(v[startIndx]);
		else s.start.copy(v[startIndx]);
		
		if (s.end==null) 
			s.end = new Vector2D(v[endIndx]);
		else s.end.copy(v[endIndx]);
		
		int rad = maxRad[j];
		int total_N = totalRad_N[j];
		int tp = -2;
		for (int i=total_N-1;i>=0;--i){
			int midIndx = aMidIndx[i];
			if (aRads[midIndx]==rad) {
				tp = aTypes[midIndx];
				break;
			}
		}
		
		if (tp==1) rad-=Segment.MAX_RADIUS;
		s.radius = rad - which*tp*CircleDriver2.tW;		
		s.type = tp;
		if (tp!=0){
			if (s.center==null) s.center = new Vector2D();
			Segment.circle(s.start, s.end, tp, s.radius,s.center);
		}
	}
	
	public int getType(Vector2D[] v,int startIndx,int endIndx){
		int j = (startIndx<<SIZE_N)+endIndx;
		int indx = mapIndx[j];
		int[] map = (indx<0) ? null : allMaps[indx];
		if (map==null) return -2;		
		
		int[] aTypes = allTypes[indx];		
		int[] aRads = allRadius[indx];	
		int[] aMidIndx = allMidIndx[indx];
		
		int rad = maxRad[j];
		int total_N = totalRad_N[j];
		int tp = -2;
		for (int i=total_N-1;i>=0;--i){
			int midIndx = aMidIndx[i];
			if (aRads[midIndx]==rad) {
				tp = aTypes[midIndx];
				break;
			}
		}
		return tp;
	}
	
	public int getMaxRad(int startIndx,int endIndx){
		int j = (startIndx<<SIZE_N)+endIndx;
		return maxRad[j];
	}
	
	public boolean isChecked(int startIndx,int endIndx){
		int j = (startIndx<<SIZE_N)+endIndx;
		int total = totalRad_N[j];
		if (total>=endIndx-startIndx-1) return true;		
		return false;
	}
	
	public boolean isFound(int startIndx,int endIndx){
		int j = (startIndx<<SIZE_N)+endIndx;
		int indx = mapIndx[j];
		if (indx>=0 && appearRad_N[j]>0){
			int mr = maxRad[j];
			int[] map = allMaps[indx];
			if (map!=null && (map[mr]>2 || map[mr]>1 && endIndx-startIndx<3)) return true;
			int[] aTypes = allTypes[indx];
			int[] aRads = allRadius[indx];
			int tp = aTypes[0];
			if (tp>Segment.UNKNOWN){
				int rad = aRads[0];
				if (map[rad]>1) return true;
			}
		}
		return false;
	}
	
	public int getTotalRad_N(int startIndx,int endIndx){
		int j = (startIndx<<SIZE_N)+endIndx;
		return totalRad_N[j];
	}
	
	public int getAppearRad_N(int startIndx,int endIndx){
		int j = (startIndx<<SIZE_N)+endIndx;
		return appearRad_N[j];
	}
	
	public int[] getMap(int startIndx,int endIndx){
		int j = (startIndx<<SIZE_N)+endIndx;
		int indx = mapIndx[j];
		return (indx<0) ? null : allMaps[indx];
	}
	
	public int[] getAppear(int startIndx,int endIndx){
		int j = (startIndx<<SIZE_N)+endIndx;
		int indx = mapIndx[j];
		return (indx<0) ? null : allAppear[indx];
	}
	
	public int[] getMap(int j){
		return allMaps[j];
	}
	
	public int[] getAppear(int j){
		return allAppear[j];
	}
}
