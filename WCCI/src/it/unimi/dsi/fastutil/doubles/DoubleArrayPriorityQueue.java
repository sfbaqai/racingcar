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

package it.unimi.dsi.fastutil.doubles;







import java.util.NoSuchElementException;

/** A type-specific array-based priority queue.
 *
 * <P>Instances of this class represent a priority queue using a backing
 * array&mdash;all operations are performed directly on the array. The array is
 * enlarged as needed, but it is never shrunk. Use the {@link #trim()} method
 * to reduce its size, if necessary.
 *
 * <P>This implementation is extremely inefficient, but it is difficult to beat
 * when the size of the queue is very small.
 */

public class DoubleArrayPriorityQueue extends AbstractDoublePriorityQueue {

 /** The backing array. */
 @SuppressWarnings("unchecked")
 protected double array[] = DoubleArrays.EMPTY_ARRAY;

 /** The number of elements in this queue. */
 protected int size;

 /** The type-specific comparator used in this queue. */
 protected DoubleComparator c;

 /** Creates a new empty queue with a given capacity and comparator.
	 *
	 * @param capacity the initial capacity of this queue.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 @SuppressWarnings("unchecked")
 public DoubleArrayPriorityQueue( int capacity, DoubleComparator c ) {
  if ( capacity > 0 ) this.array = new double[ capacity ];
  this.c = c;
 }

 /** Creates a new empty queue with a given capacity and using the natural order.
	 *
	 * @param capacity the initial capacity of this queue.
	 */
 public DoubleArrayPriorityQueue( int capacity ) {
  this( capacity, null );
 }

 /** Creates a new empty queue with a given comparator.
	 *
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public DoubleArrayPriorityQueue( DoubleComparator c ) {
  this( 0, c );
 }

 /** Creates a new empty queue using the natural order. 
	 */
 public DoubleArrayPriorityQueue() {
  this( 0, null );
 }

 /** Wraps a given array in a queue using a given comparator.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param a an array.
	 * @param size the number of elements to be included in the queue.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public DoubleArrayPriorityQueue( final double[] a, int size, final DoubleComparator c ) {
  this( c );
  this.array = a;
  this.size = size;
 }


 /** Wraps a given array in a queue using a given comparator.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param a an array.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public DoubleArrayPriorityQueue( final double[] a, final DoubleComparator c ) {
  this( a, a.length, c );
 }

 /** Wraps a given array in a queue using the natural order.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param a an array.
	 * @param size the number of elements to be included in the queue.
	 */
 public DoubleArrayPriorityQueue( final double[] a, int size ) {
  this( a, size, null );
 }


 /** Wraps a given array in a queue using the natural order.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param a an array.
	 */
 public DoubleArrayPriorityQueue( final double[] a ) {
  this( a, a.length );
 }



 /** Returns the index of the smallest element. */

 @SuppressWarnings("unchecked")
 private int findFirst() {
  int i = size;
  int firstIndex = --i;
  double first = array[ firstIndex ];

  if ( c == null ) { while( i-- != 0 ) if ( ( (array[ i ]) < (first) ) ) first = array[ firstIndex = i ]; }
  else while( i-- != 0 ) { if ( c.compare( array[ i ], first ) < 0 ) first = array[ firstIndex = i ]; }

  return firstIndex;
 }

 private void ensureNonEmpty() {
  if ( size == 0 ) throw new NoSuchElementException();
 }

 public void enqueue( double x ) {
  if ( size == array.length ) array = DoubleArrays.grow( array, size + 1 );
  array[ size++ ] = x;
 }

 /** Dequeues an element.
	 *
	 * <P>Note that each call to this method requires a complete scan of the backing array. Please
	 * consider caching its result.
	 */

 public double dequeueDouble() {
  ensureNonEmpty();
  final int first = findFirst();
  final double result = array[ first ];
  System.arraycopy( array, first + 1, array, first, --size - first );



  return result;
 }

 public double firstDouble() {
  ensureNonEmpty();
  return array[ findFirst() ];
 }

 public void changed() {
  ensureNonEmpty();
 }

 public int size() { return size; }

 public void clear() {



  size = 0;
 }

 /** Trims the underlying array so that it has exactly {@link #size()} elements.
	 */

 public void trim() {
  array = DoubleArrays.trim( array, size );
 }

 public DoubleComparator comparator() { return c; }

}
