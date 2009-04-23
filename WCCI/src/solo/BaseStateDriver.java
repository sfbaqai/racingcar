/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.Stack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.io.Serializable;
import java.util.List;

/**
 * @author kokichi3000
 *
 */
public abstract class BaseStateDriver<T,V> implements Serializable{
	private class StackNode implements Serializable{		
		public State<T,V> state;		
		public V action;
		public StackNode(State<T, V> state, V action) {
			super();
			this.state = state;
			this.action = action;
		}
	}
	
	State<T, V> start;
	public int repeatNum =1;
	public MyTreeMap<T, V> map;
	public boolean ignoredExisting = false;
	public boolean storeNewState = true;
	private String name;
	protected State<T, V> current;
	protected V action;
	protected boolean started = false;
	private Stack<StackNode> stack = new ObjectArrayList<StackNode>(); 
	public State<T,V> target;
	public V targetAction;
	public List<State<T,V>> startStates = null;
	public ObjectList<State<T,V>> pathToTarget;
	int index = 0;
	/**
	 * 
	 */
	public BaseStateDriver() {
		// TODO Auto-generated constructor stub
		map = new MyTreeMap<T, V>();
	}
	
	public BaseStateDriver(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
		current = null;
		action = null;
		target = null;
		targetAction = null;
		map = new MyTreeMap<T, V>(name);
	}
	
	public void setRepeatNum(int n){
		repeatNum = n;
	}
	
	public void addStartState(T state){
		State<T, V> s = map.get(state);
		if (s==null) return;
		if (startStates==null) startStates = new ObjectArrayList<State<T,V>>();
		startStates.add(s);
	}
	
	public void save(String name){
		map.save(name);
	}
	
	public void save(String name,State<T, V> state){		
		ObjectList<State<T,V>> path = map.walk(state, null);
		MyTreeMap<T, V> m = new MyTreeMap<T, V>();
		if (path==null) return;
			
		for (State<T, V> s : path){
			if (s!=null) {				
				m.store(s.prev, s.prevAction, s.state);
			}
		}		
		m.save(name);
	}
	
	public void save(String name,T state){		
		ObjectList<State<T,V>> path = map.walk(state);
		MyTreeMap<T, V> m = new MyTreeMap<T, V>();
		if (path==null) return;
			
		for (State<T, V> s : path){
			if (s!=null) {				
				m.store(s.prev, s.prevAction, s.state);
			}
		}
		m.save(name);
	}
	
	public abstract boolean stopCondition(State<T, V> state);
	public boolean shutdownCondition(State<T, V> state){
		return false;
	}
	public abstract ObjectList<V> drive(State<T, V> state);
	public abstract void storeSingleAction(T input,V action,T output);
	public abstract void init();
	
	public abstract V restart();
	public abstract V shutdown();
	
	
	public V wDrive(T state){
		if (state==null || map == null) return null;
		if (!started) {
			started = true;
			targetAction = null;
			if (startStates==null || startStates.isEmpty()) 
				target = null;
			 else target = startStates.remove(0);
			start = target;
			index++;
			init();			
		}
		State<T, V> s = null;
		if (map.isEmpty()){			
			if (storeNewState) {
				storeSingleAction(null, null, state);
				map.store(current,null,state);
			}
			current = new State<T,V>(0,state,null,null);
		} else {
			s = map.get(state);
		
			if (s==null || (current!=null && s.num<=current.num)) {						
				if (storeNewState && (s==null || !current.equals(s)))  
					map.store(current, action, state);
				if (current!=null) {
					storeSingleAction(current.state, action, state);
				} else storeSingleAction(null, null, state);
				current = (current==null) ? new State<T, V>(0,state,null,null):new State<T, V>(current.num+1,state,current,action);
			} else {				
				if (!ignoredExisting && current!=null && current.compareTo(s)>=0){
					storeSingleAction(current.state, action, state);					
					if (stack.isEmpty() && (startStates==null || startStates.isEmpty())&& index==repeatNum && repeatNum>1){							
						return shutdown();
					};			
					if (stack.isEmpty()){
						started = false;						
						return restart();
					}
					StackNode sn = stack.pop();
					target = sn.state;
					targetAction = sn.action;					
					return restart();
				}//*/
				
				current = s;				
			}
		}
		current.state = state;
		
		//System.out.println(current.state+"    ");
		//System.out.println(state);
		if (shutdownCondition(current)) return shutdown();
		if (stopCondition(current)){			
			if (stack.isEmpty() && (startStates==null || startStates.isEmpty()) && index==repeatNum && repeatNum>1){							
				return shutdown();
			};			
			if (stack.isEmpty()){
				started = false;				
				return restart();
			}
			StackNode sn = stack.pop();
			target = sn.state;
			targetAction = sn.action;		
			return restart();
		}
		
		if (target!=null && targetAction!=null && target.equals(current)){
			action = targetAction;
			return targetAction;			
		} else if (target==null || target.compareTo(current)<=0){
			ObjectList<V> l = drive(current);
			StackNode sn;
			for (V v : l) {
				sn = new StackNode(current,v);
				stack.push(sn);
			}
			sn = stack.pop();
			target = sn.state;
			targetAction = sn.action;
			action = targetAction;
			return targetAction;
		} else {			
			int n = (pathToTarget==null) ? 0 : pathToTarget.size()-1;
			if (pathToTarget==null || pathToTarget.isEmpty() || !pathToTarget.get(n).equals(current))				
				pathToTarget = map.walk(target,current);			
			n = (pathToTarget==null) ? 0 : pathToTarget.size()-1;
			if (n>=0){
				State<T, V> tmp = pathToTarget.remove(n);
				action = tmp.prevAction;	
			} else action = null;
			return action;
		}		
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
	    final String TAB = "\n";
	    
	    String retValue = "";
	    
	    
	    retValue = "BaseStateDriver ( "
	        + super.toString() + TAB
	        + "map = " + this.map.keySet() + TAB
	        + "name = " + this.name + TAB
	        + "current = " + this.current + TAB
	        + "action = " + this.action + TAB
	        + "stack = " + this.stack + TAB
	        + "target = " + this.target + TAB
	        + "targetAction = " + this.targetAction + TAB
	        + "pathToTarget = " + this.pathToTarget + TAB
	        + " )";
	
	    return retValue;
	}	
	
	

}
