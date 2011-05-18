package solo;

import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class MyTreeMap<K,V> {
	private Object2ObjectRBTreeMap<K, State<K, V>> map;
	private String name;
	
	@SuppressWarnings("unchecked")
	public MyTreeMap(String name){
		this.name = name;
		ObjectInputStream ois;
		try{
			File file = new File(name);
			FileInputStream inStream = new FileInputStream(file);// generic stream to the file
			ois = new ObjectInputStream(inStream);
			map = (Object2ObjectRBTreeMap<K, State<K, V>>)(ois.readObject());			
			ois.close();
			inStream.close();
		} catch (Exception e){
			e.printStackTrace();
			map = new Object2ObjectRBTreeMap<K, State<K,V>>();
		} 
	}
	
	public final void remove(K obj){
		map.remove(obj);
	}
	
	
	public MyTreeMap(){
		map = new Object2ObjectRBTreeMap<K, State<K,V>>();
	};
	
	
	public final void save(String name){		
		ObjectOutputStream oos;
		try{
			File file = new File(name);
			FileOutputStream outStream = new FileOutputStream(file);// generic stream to the file
			oos = new ObjectOutputStream(outStream);			
			oos.writeObject(map);
			oos.flush();
			oos.close();
			outStream.flush();
			outStream.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	};
	
	public final void save(){		
		save(name);
	};
	
	public final int size(){
		return map.size();
	}
	
	public final State<K, V> get(K obj){
		return map.get(obj);
	}
	
	public final void store(K cur,V action,K rs){
		if (cur==null){
			map.put(rs, new State<K,V>(0,rs,null,null) );
			return;
		}
		State<K,V> c = map.get(cur);		
		if (map.isEmpty()){
			c = new State<K,V>(0,cur,null,null);
			map.put(cur, c);
		}
		if (c==null) return;
		State<K, V> newState = new State<K,V>(c.num+1,rs,c,action);
		map.put(rs, newState);
	}
	
	public final void store(State<K,V> cur,V action,K rs){						
		if (cur==null){
			map.put(rs, new State<K,V>(0,rs,null,null) );
			return;
		}
		State<K, V> newState = new State<K,V>(cur.num+1,rs,cur,action);
		map.put(rs, newState);
	}
	
	public final ObjectSortedSet<K> keySet(){
		return map.keySet();
	}
	
	public final ObjectCollection<State<K,V>> values(){
		return map.values();
	}
	
	
	public final ObjectList<State<K,V>> walk(K target){		
		if (target==null || map.isEmpty() || !map.containsKey(target))
			return null;
		State<K,V> s = map.get(target);
		ObjectList<State<K,V>> rs = new ObjectArrayList<State<K,V>>();
		while (s!=null){
			rs.add(s);
			s=s.prev;
		}
		
		return rs;
	}
	
	public ObjectList<State<K,V>> walk(State<K,V> target,State<K,V> from){		
		if (target==null)
			return null;
		State<K,V> s = target;
		ObjectList<State<K,V>> rs = new ObjectArrayList<State<K,V>>();
		if (from!=null){
			while (s!=null && s.num>from.num ){
				rs.add(s);
				s=s.prev;
			}
		} else {
			while (s!=null && s.num>0 ){
				rs.add(s);
				s=s.prev;
			}
		}
		
		return rs;
	}
	
	public static void main(String[] argv){
		MyTreeMap<Float	, Integer> map = new MyTreeMap<Float, Integer>();
		map.store(new Float(1.5), new Integer(1), new Float(2.5));
		map.store(new Float(1.5), new Integer(2), new Float(3.5));
		map.store(new Float(2.5), new Integer(3), new Float(3.0));
		map.store(new Float(2.5), new Integer(4), new Float(6.5));
		map.store(new Float(6.5), new Integer(2), new Float(8.5));
		System.out.println(map.walk(new Float(8.5)));
		map.save("test");
		MyTreeMap<Float	, Integer> nmap = new MyTreeMap<Float	, Integer>("test");
		System.out.println(nmap.walk(new Float(8.5)));
		nmap.store(new Float(6.5), new Integer(-2), new Float(4.5));
		nmap.save();
		map = new MyTreeMap<Float, Integer>("test");
		System.out.println(map.keySet());
		
	}
	
	public boolean isEmpty(){
		return map.isEmpty();
	}
	
}
