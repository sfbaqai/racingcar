package solo;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import cern.colt.Swapper;
import cern.colt.list.ObjectArrayList;

public class FastMap<K,V> implements SortedMap<K, V> {
	final static int INITIAL_SIZE = 50;
	final static int MAX_VALUE = Integer.MAX_VALUE;
	public int BIT_PER_PAGE = 17;
	public int NUM_PAGE = MAX_VALUE/(2 << (BIT_PER_PAGE-1)); 
	public int PAGE_SIZE = 2<<BIT_PER_PAGE;
	int START_PAGE = NUM_PAGE >> 1;
	Object minKey = null;
	Object maxKey = null;
	ObjectArrayList keys = null;
	ObjectArrayList values = null;
	Object[] k;
	Object[] v;	
	@SuppressWarnings("rawtypes")
	Comparator c =null;
	int size = 0;
	int len = 0;
	Object[][] map = null;
	boolean sorted = true;
	public final Swapper swapper = new Swapper(){
		@Override
		public void swap(int i, int j) {
			// TODO Auto-generated method stub
			Object tmp = k[i];
			k[i] = k[j];
			k[j] = tmp;
			Object vv = v[i];
			v[i] = v[j];
			v[j] = vv;
		}
	};
	
	
	public FastMap() {
		map = new Object[NUM_PAGE][];		
//		keys = new ObjectArrayList(INITIAL_SIZE);
//		values = new ObjectArrayList(INITIAL_SIZE);
		// TODO Auto-generated constructor stub
	}
	
	public FastMap(int initsize) {
		map = new Object[NUM_PAGE][];		
//		keys = new ObjectArrayList(initsize);
//		values = new ObjectArrayList(initsize);
		// TODO Auto-generated constructor stub
	}
	
	//this constructor will not copy the array but use as backing array
	public FastMap(K[] ks,V[] vs){
		map = new Object[NUM_PAGE][];
//		keys = new ObjectArrayList(ks);
//		values = new ObjectArrayList(vs);
		size = ks.length;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final static int compare(Object a, Object b,Comparator c){
		if (c!=null) return c.compare(a, b);
		if (a instanceof Comparable)
			return ((Comparable) a).compareTo(b);
		if (b instanceof Comparable)
			return -((Comparable) b).compareTo(a);
		return 0;
	}

	public void setBitsPerPage(int v){
		if (v!=BIT_PER_PAGE){
			BIT_PER_PAGE = v;
			NUM_PAGE = MAX_VALUE/(2 << (BIT_PER_PAGE-1));
			START_PAGE = NUM_PAGE >> 1;
			map = new Object[NUM_PAGE][];			
		}
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		size = 0;
	}

	@Override
	public final boolean containsKey(Object arg0) {
		// TODO Auto-generated method stub
		int hash = arg0.hashCode();		
		int id = hash >> BIT_PER_PAGE;
		int offset = hash - (int)(id<<BIT_PER_PAGE);
		id += START_PAGE;
		return (map!=null && map[id]!=null && map[id][offset]!=null);
	}

	@Override
	public boolean containsValue(Object arg0) {
		// TODO Auto-generated method stub		
		return values.contains(arg0, false);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final V get(Object key) {
		// TODO Auto-generated method stub
		if (size==0 || map==null) return null;
		int hash = key.hashCode();		
		int id = hash >> BIT_PER_PAGE;		
		int offset = hash - (int)(id<<BIT_PER_PAGE);
		id += START_PAGE;
		if (map[id]!=null)
			 return (V)map[id][offset];
		
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return (size==0);
	}

	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final V put(K key, V val) {
		// TODO Auto-generated method stub
		
		int hash = key.hashCode();		
		int id = hash >> BIT_PER_PAGE;
		int offset = hash - (int)(id<<BIT_PER_PAGE);
		id += START_PAGE;
		if (map[id]==null) 
			map[id] = new Object[PAGE_SIZE];
		V old = (V)map[id][offset];
		if (old==null) size++;
		map[id][offset] = val;
		return old;
			
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public V remove(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparator<? super K> comparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public K firstKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap<K, V> headMap(K arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public K lastKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap<K, V> subMap(K arg0, K arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap<K, V> tailMap(K arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
