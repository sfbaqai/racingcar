package solo;


public final class FastInt2IntMap{
	/**
	 * 
	 */
	private static final long serialVersionUID = -972293290473205330L;
	final static int INITIAL_SIZE = 50;
	final static int MAX_VALUE = Integer.MAX_VALUE;
	static int DEFAULT_RETURN_VALUE = 0;
	public int BIT_PER_PAGE = 17;	
	public int PAGE_SIZE = 2<<BIT_PER_PAGE;
	public int NUM_PAGE = MAX_VALUE/PAGE_SIZE * 2;
	public int maxV;
	int minV;
	public int START_PAGE = NUM_PAGE >> 1;
	int size = 0;	
	int offset = 0;
	public int[] map = null;
	
	
	public FastInt2IntMap(int minV,int maxV){
//		int range = Math.max(Math.abs(minV), Math.abs(maxV));
		int n = Math.abs(maxV-minV);
//		this.maxV = maxV;
//		this.minV = minV;
//		if (range<PAGE_SIZE){
//			PAGE_SIZE = range;
//		}
//		NUM_PAGE = n/PAGE_SIZE;
//		START_PAGE = NUM_PAGE >> 1;
		map = new int[n];
	}
	
//	public final int getPageID(int key){		
//		int id = key/PAGE_SIZE;
//		offset = (int)((long)key - (long)id*(long)PAGE_SIZE);
//		id += START_PAGE;
//		return id;
//	}
	
	public final void clear() {
		// TODO Auto-generated method stub
		size = 0;
//		for (int i=0;i<NUM_PAGE;++i){
//			if (map[i]!=null) Arrays.fill(map[i], 0);
//		}

	}

	
//	public final boolean containsKey(Object arg0) {
//		// TODO Auto-generated method stub
//		if (!(arg0 instanceof Integer)) return false;
//		int key = ((Integer)arg0).intValue();
//		int pageId = getPageID(key);
//		if (pageId<0 || pageId>=NUM_PAGE) return false;		
////		if (map[pageId]==null || map[pageId][offset]==DEFAULT_RETURN_VALUE) return false;
//		return true;
//	}
//		
//	public final boolean containsKey(int key){
//		// TODO Auto-generated method stub		
//		int pageId = getPageID(key);
//		if (pageId<0 || pageId>=NUM_PAGE) return false;		
////		if (map[pageId]==null || map[pageId][offset]==DEFAULT_RETURN_VALUE) return false;
//		return true;
//	}

		
	public final Integer get(Object arg0) {
		// TODO Auto-generated method stub
		if (!(arg0 instanceof Integer)) return null;
		int key = ((Integer)arg0).intValue();
//		int pageId = getPageID(key);
//		if (pageId<0 || pageId>=NUM_PAGE) return null;		
//		if (map[pageId]==null) return null;
		return map[key];
	}
	
	public final int get(int key) {
		// TODO Auto-generated method stub		
//		int pageId = getPageID(key);
//		if (pageId<0 || pageId>=NUM_PAGE) return DEFAULT_RETURN_VALUE;		
//		if (map[pageId]==null) return DEFAULT_RETURN_VALUE;
		return map[key];
	}
		

	
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return (size==0);
	}
		
	public final Integer put(Integer arg0, Integer arg1) {
		// TODO Auto-generated method stub		
//		int key = arg0.intValue();
//		int val = arg1.intValue();
//		int pageId = getPageID(key);
//		if (pageId<0 || pageId>=NUM_PAGE) return null;		
//		if (map[pageId]==null) {
//			map[pageId] = new int[PAGE_SIZE];
//			if (DEFAULT_RETURN_VALUE!=0) Arrays.fill(map[pageId], DEFAULT_RETURN_VALUE);
//		}
//		 
//		Integer old = map[pageId][offset];
//		if (old==DEFAULT_RETURN_VALUE) size++;
//		map[pageId][offset] = val;
//		return old;
		return null;
	}
	
	public final int put(int key, int val) {
		// TODO Auto-generated method stub				
//		int pageId = getPageID(key);
//		if (pageId<0 || pageId>=NUM_PAGE) return DEFAULT_RETURN_VALUE;			
//		if (map[pageId]==null) {
//			map[pageId] = new int[PAGE_SIZE];
//			if (DEFAULT_RETURN_VALUE!=0) Arrays.fill(map[pageId], DEFAULT_RETURN_VALUE);
//		}
		 
//		int old = map[key];
//		if (old==DEFAULT_RETURN_VALUE) size++;
//		size++;
		map[key] = val;
		return val;
	}

	
	public final Integer remove(Object arg0) {
		// TODO Auto-generated method stub
//		if (!(arg0 instanceof Integer)) return null;
//		int key = ((Integer)arg0).intValue();
//		int pageId = getPageID(key);
//		if (pageId<0 || pageId>NUM_PAGE) return null;		
//		if (map[pageId]==null || map[pageId][offset]==DEFAULT_RETURN_VALUE) return null;		
//		Integer val = map[pageId][offset];
//		map[pageId][offset] = DEFAULT_RETURN_VALUE;
//		size--;
//		return val;
		return null;
	}

	public final int remove(int key) {
		// TODO Auto-generated method stub		
//		int pageId = getPageID(key);
//		if (pageId<0 || pageId>NUM_PAGE) return DEFAULT_RETURN_VALUE;		
//		if (map[pageId]==null || map[pageId][offset]==DEFAULT_RETURN_VALUE) return DEFAULT_RETURN_VALUE;		
//		Integer val = map[pageId][offset];
//		map[pageId][offset] = DEFAULT_RETURN_VALUE;
//		size--;
//		return val;
		return 0;
	}

		
	public final int size() {
		// TODO Auto-generated method stub
		return size;
	}
			

}
