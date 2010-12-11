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
 * Copyright (C) 2003-2008 Paolo Boldi and Sebastiano Vigna 
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

package it.unimi.dsi.fastutil.ints;

import java.util.NoSuchElementException;

import it.unimi.dsi.fastutil.PriorityQueue;

/** A type-specific {@link PriorityQueue}; provides some additional methods that use polymorphism to avoid (un)boxing. 
 *
 * <P>Additionally, this interface strengthens {@link #comparator()}.
 */

public interface IntPriorityQueue extends PriorityQueue<Integer> {

 /** Enqueues a new element.
	 *
	 * @param x the element to enqueue..
	 */

 void enqueue( int x );

 /** Dequeues the first element from the queue.
	 *
	 * @return the dequeued element.
	 * @throws NoSuchElementException if the queue is empty.
	 */

 int dequeueInt();

 /** Returns the front element of the queue.
	 *
	 * @return the front element.
	 * @throws NoSuchElementException if the queue is empty.
	 */

 int firstInt();

 /** Returns the rear element of the queue, that is, the element the would be dequeued last (optional operation).
	 *
	 * @return the rear element.
	 * @throws NoSuchElementException if the queue is empty.
	 */

 int lastInt();

 /** Returns the comparator associated with this sorted set, or null if it uses its elements' natural ordering.
	 *
	 * <P>Note that this specification strengthens the one given in {@link PriorityQueue#comparator()}.
	 *
	 * @see PriorityQueue#comparator()
	 */

 IntComparator comparator();
}
