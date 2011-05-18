package solo;



public final class ObjectArrayList<K>{
	K[] elems;
	Object[] a;
	int from = 0; 
	int to;
	int size = 0;
	boolean wrapped = false;
	
	public ObjectArrayList() {
		// TODO Auto-generated constructor stub
		to = 20;		
		a = new Object[to];
	}

	public ObjectArrayList(int n) {
		// TODO Auto-generated constructor stub
		to = n;		
		a = new Object[to];
	} 

	
	public ObjectArrayList(K[] val){
		elems = val;
		to = elems.length;		
		wrapped = true;
	}
	
	public ObjectArrayList(K[] val,int length){
		elems = val;
		to = elems.length;
		size = length;
		wrapped = true;
	}
	
	public ObjectArrayList(K[] val,int from,int to){
		elems = val;
		this.from = from;
		this.to = to;		
		wrapped = true;
	}
	
	public ObjectArrayList(K[] val,int from,int to,int sz){
		elems = val;
		this.from = from;
		this.to = to;		
		this.size = sz;
		wrapped = true;
	}
	
	public final void add(K v){
		if (from+size>to){			
			to = size<<1;
			Object[] tmp = new Object[to];
			if (wrapped)
				System.arraycopy(elems, from, tmp, 0, size);
			else System.arraycopy(a, from, tmp, 0, size);
			wrapped = false;
			from = 0;
		}
		if (wrapped) 
			elems[size++] = v;
		else a[size++] = v;
	}
	
	public final void add(int index,K v){
		if (index>size || index<0) {
			System.err.println("Index "+index+" not valid");
			return;		
		}
		if (from+size>to){			
			to = size<<1;
			Object[] tmp = new Object[to];
			if (wrapped)
				System.arraycopy(elems, from, tmp, 0, size);
			else System.arraycopy(a, from, tmp, 0, size);
			wrapped = false;
			from = 0;
		}
		
		if (index==size) {
			if (wrapped) 
				elems[size++] = v;
			else a[size++] = v;
		} else {
			if (wrapped){
				System.arraycopy(elems, index, elems, index+1, size++-index);
				elems[index] = v;
			} else {
				System.arraycopy(a, index, a, index+1, size++-index);
				a[index] = v;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public final K remove(int index){				
		if (index==size-1) {
			if (wrapped) 
				return elems[--size];
			else return (K)a[--size];
		} else {
			if (wrapped){
				K v = elems[index];
				System.arraycopy(elems, index+1, elems, index, --size-index);
				return v;
			} else {
				Object v = a[index];
				System.arraycopy(a, index+1, a, index, --size-index);
				return (K)v;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public final K[] elements(){
		return (wrapped) ? elems : (K[])a;
	}
	
	public final int size(){
		return size;
	}
	
	public final void size(int sz){
		if (sz<0 || sz>to) return;
		size = sz;
	}
	
	@SuppressWarnings("unchecked")
	public final K get(int index){
		return (wrapped) ? elems[index] : (K)a[index];
	}
	
	public final void clear(){
		size = 0;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final ObjectArrayList<K> subList(int from,int to){
		if (from<this.from || from>=this.size || to<=from || to>this.size) {
			System.err.println("Wrong index");
			return null;
		}
		if (wrapped)
			return new ObjectArrayList<K>(elems, from, to, to-from);
		return new ObjectArrayList(a, from, to, to-from);
	}
	
	public final void addAll(ObjectArrayList<K> ol){
		if (from+size+ol.size()>to){			
			to = (size+ol.size())<<1;
			Object[] tmp = new Object[to];
			if (wrapped)
				System.arraycopy(elems, from, tmp, 0, size);
			else System.arraycopy(a, from, tmp, 0, size);
			wrapped = false;
			from = 0;
		}
		if (wrapped)
			System.arraycopy(ol.elements(), 0, elems, size, ol.size);
		else System.arraycopy(ol.elements(), 0, a, size, ol.size);
		size +=ol.size;
	}
	
	public final void addAll(int index,ObjectArrayList<K> ol){
		if (from+size+ol.size()>to){			
			to = (size+ol.size())<<1;
			Object[] tmp = new Object[to];
			if (wrapped)
				System.arraycopy(elems, from, tmp, 0, size);
			else System.arraycopy(a, from, tmp, 0, size);
			wrapped = false;
			from = 0;
		}
		
		
		if (wrapped && index==size)
			System.arraycopy(ol.elements(), 0, elems, size, ol.size);
		else if (index==size){
			System.arraycopy(ol.elements(), 0, a, size, ol.size);
		} else {
			Object[] tmp = new Object[size-index];
			if (wrapped){ 
				System.arraycopy(elems, index, tmp, 0, size-index);
				System.arraycopy(ol.elements(), 0, elems, index, ol.size);
				System.arraycopy(tmp, 0, elems, index+size, size-index);				
			}else {
				System.arraycopy(a, index, tmp, 0, size-index);
				System.arraycopy(ol.elements(), 0, a, index, ol.size);
				System.arraycopy(tmp, 0, a, index+size, size-index);
			}
		}
		size +=ol.size;
	}
	
	public final void set(int index,K v){
		if (wrapped) 
			elems[index] = v;
		else a[index] = v;
	}
}
