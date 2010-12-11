/* Generic definitions */




/* Assertions (useful to generate conditional code) */
/* Current type and class (and size, if applicable) */
/* Value methods */


/* Interfaces (keys) */
/* Interfaces (values) */
/* Abstract implementations (keys) */
/* Abstract implementations (values) */






/* Static containers (keys) */
/* Static containers (values) */






/* Implementations */
/* Synchronized wrappers */
/* Unmodifiable wrappers */
/* Other wrappers */




/* Methods (keys) */
/* Methods (values) */







/* Methods (keys/values) */

/* Methods that have special names depending on keys (but the special names depend on values) */







/* Equality */
/* Object/Reference-only definitions (keys) */
/* Primitive-type-only definitions (keys) */
/* Object/Reference-only definitions (values) */
/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2003-2008 Sebastiano Vigna 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package it.unimi.dsi.fastutil.shorts;

/** A class providing static methods and objects that do useful things with type-specific priority queues.
 *
 * @see it.unimi.dsi.fastutil.PriorityQueue
 */

public class ShortPriorityQueues {

 private ShortPriorityQueues() {}

 /** A synchronized wrapper class for priority queues. */

 public static class SynchronizedPriorityQueue implements ShortPriorityQueue {

  public static final long serialVersionUID = -7046029254386353129L;

  final protected ShortPriorityQueue q;
  final protected Object sync;

  protected SynchronizedPriorityQueue( final ShortPriorityQueue q, final Object sync ) {
   this.q = q;
   this.sync = sync;
  }

  protected SynchronizedPriorityQueue( final ShortPriorityQueue q ) {
   this.q = q;
   this.sync = this;
  }

  public void enqueue( short x ) { synchronized( sync ) { q.enqueue( x ); } }
  public short dequeueShort() { synchronized( sync ) { return q.dequeueShort(); } }
  public short firstShort() { synchronized( sync ) { return q.firstShort(); } }
  public short lastShort() { synchronized( sync ) { return q.lastShort(); } }
  public boolean isEmpty() { synchronized( sync ) { return q.isEmpty(); } }
  public int size() { synchronized( sync ) { return q.size(); } }
  public void clear() { synchronized( sync ) { q.clear(); } }
  public void changed() { synchronized( sync ) { q.changed(); } }
  public ShortComparator comparator() { synchronized( sync ) { return q.comparator(); } }

  public void enqueue( Short x ) { synchronized( sync ) { q.enqueue( x ); } }
  public Short dequeue() { synchronized( sync ) { return q.dequeue(); } }
  public Short first() { synchronized( sync ) { return q.first(); } }
  public Short last() { synchronized( sync ) { return q.last(); } }


 }


 /** Returns a synchronized type-specific priority queue backed by the specified type-specific priority queue.
	 *
	 * @param q the priority queue to be wrapped in a synchronized priority queue.
	 * @return a synchronized view of the specified priority queue.
	 */
 public static ShortPriorityQueue synchronize( final ShortPriorityQueue q ) { return new SynchronizedPriorityQueue( q ); }

 /** Returns a synchronized type-specific priority queue backed by the specified type-specific priority queue, using an assigned object to synchronize.
	 *
	 * @param q the priority queue to be wrapped in a synchronized priority queue.
	 * @param sync an object that will be used to synchronize the access to the priority queue.
	 * @return a synchronized view of the specified priority queue.
	 */

 public static ShortPriorityQueue synchronize( final ShortPriorityQueue q, final Object sync ) { return new SynchronizedPriorityQueue( q, sync ); }


}
