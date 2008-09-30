/**
 * 
 */
package solo;

import java.io.Serializable;

/**
 * @author kokichi3000
 *
 */
public final class State<T,V> implements Serializable,Comparable<State<T,V>>{
	
	
	public int num;
	public T state;
	public State<T,V> prev;
	

	public V prevAction;
	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	
	
	
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "State ( "
	        + super.toString() + TAB
	        + "num = " + this.num + TAB
	        + "state = " + this.state + TAB
	        + "prev = " + ((this.prev==null) ? "" : this.prev.state) + TAB
	        + "prevAction = " + this.prevAction + TAB
	        + " )";
	
	    return retValue;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final State<T,V> other = (State<T,V>) obj;
		if (num != other.num)
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(State<T, V> arg0) {
		// TODO Auto-generated method stub		
		return (num<arg0.num)?-1:(num>arg0.num)?1:0;
	}

	public State(){};
	public State(int num, T state, State<T, V> prev, V prevAction) {		
		this.num = num;
		this.state = state;
		this.prev = prev;
		this.prevAction = prevAction;
	}
	
}
