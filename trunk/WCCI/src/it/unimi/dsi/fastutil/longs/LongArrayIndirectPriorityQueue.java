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

package it.unimi.dsi.fastutil.longs;





import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.AbstractIndirectPriorityQueue;

import java.util.NoSuchElementException;

/** A type-specific array-based semi-indirect priority queue. 
 *
 * <P>Instances of this class use as reference list a <em>reference array</em>,
 * which must be provided to each constructor, and represent a priority queue
 * using a backing array of integer indices&mdash;all operations are performed
 * directly on the array. The array is enlarged as needed, but it is never
 * shrunk. Use the {@link #trim()} method to reduce its size, if necessary.
 *
 * <P>This implementation is extremely inefficient, but it is difficult to beat
 * when the size of the queue is very small. Moreover, it allows to enqueue several
 * time the same index, without limitations.
 */

public class LongArrayIndirectPriorityQueue extends AbstractIndirectPriorityQueue<Long> {

 /** The reference array. */
 protected long refArray[];

 /** The backing array. */
 protected int array[] = IntArrays.EMPTY_ARRAY;

 /** The number of elements in this queue. */
 protected int size;

 /** The type-specific comparator used in this queue. */
 protected LongComparator c;

 /** Creates a new empty queue without elements with a given capacity and comparator.
	 *
	 * @param refArray the reference array.
	 * @param capacity the initial capacity of this queue.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public LongArrayIndirectPriorityQueue( long[] refArray, int capacity, LongComparator c ) {
  if ( capacity > 0 ) this.array = new int[ capacity ];
  this.refArray = refArray;
  this.c = c;
 }

 /** Creates a new empty queue with given capacity and using the natural order.
	 *
	 * @param refArray the reference array.
	 * @param capacity the initial capacity of this queue.
	 */
 public LongArrayIndirectPriorityQueue( long[] refArray, int capacity ) {
  this( refArray, capacity, null );
 }

 /** Creates a new empty queue with capacity equal to the length of the reference array and a given comparator.
	 *
	 * @param refArray the reference array.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public LongArrayIndirectPriorityQueue( long[] refArray, LongComparator c ) {
  this( refArray, refArray.length, c );
 }

 /** Creates a new empty queue with capacity equal to the length of the reference array and using the natural order. 
	 * @param refArray the reference array.
	 */
 public LongArrayIndirectPriorityQueue( long[] refArray ) {
  this( refArray, refArray.length, null );
 }


 /** Wraps a given array in a queue using a given comparator.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param size the number of elements to be included in the queue.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public LongArrayIndirectPriorityQueue( final long[] refArray, final int[] a, int size, final LongComparator c ) {
  this( refArray, 0, c );
  this.array = a;
  this.size = size;
 }


 /** Wraps a given array in a queue using a given comparator.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public LongArrayIndirectPriorityQueue( final long[] refArray, final int[] a, final LongComparator c ) {
  this( refArray, a, a.length, c );
 }

 /** Wraps a given array in a queue using the natural order.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param size the number of elements to be included in the queue.
	 */
 public LongArrayIndirectPriorityQueue( final long[] refArray, final int[] a, int size ) {
  this( refArray, a, size, null );
 }


 /** Wraps a given array in a queue using the natural order.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 */
 public LongArrayIndirectPriorityQueue( final long[] refArray, final int[] a ) {
  this( refArray, a, a.length );
 }


 /** Returns the index (in {@link #array}) of the smallest element. */

 @SuppressWarnings("unchecked")
 private int findFirst() {
  int i = size;
  int firstIndex = --i;
  long first = refArray[ array[ firstIndex ] ];

  if ( c == null ) while( i-- != 0 ) { if ( ( (refArray[ array[ i ] ]) < (first) ) ) first = refArray[ array[ firstIndex = i ] ]; }
  else while( i-- != 0 ) { if ( c.compare( refArray[ array[ i ] ], first ) < 0 ) first = refArray[ array[ firstIndex = i ] ]; }

  return firstIndex;
 }

 /** Returns the index (in {@link #array}) of the largest element. */

 @SuppressWarnings("unchecked")
 private int findLast() {
  int i = size;
  int lastIndex = --i;
  long last = refArray[ array[ lastIndex ] ];

  if ( c == null ) { while( i-- != 0 ) if ( ( (last) < (refArray[ array[ i ] ]) ) ) last = refArray[ array[ lastIndex = i ] ]; }
  else { while( i-- != 0 ) if ( c.compare( last, refArray[ array[ i ] ] ) < 0 ) last = refArray[ array[ lastIndex = i ] ]; }

  return lastIndex;
 }

 protected final void ensureNonEmpty() {
  if ( size == 0 ) throw new NoSuchElementException();
 }

 /** Ensures that the given index is a valid reference.
	 *
	 * @param index an index in the reference array.
	 * @throws IndexOutOfBoundsException if the given index is negative or larger than the reference array length.
	 */
 protected void ensureElement( final int index ) {
  if ( index < 0 ) throw new IndexOutOfBoundsException( "Index (" + index + ") is negative" );
  if ( index >= refArray.length ) throw new IndexOutOfBoundsException( "Index (" + index + ") is larger than or equal to reference array size (" + refArray.length + ")" );
 }

 /** Enqueues a new element.
	 *
	 * <P>Note that for efficiency reasons this method will <em>not</em> throw an exception
	 * when <code>x</code> is already in the queue. However, the queue state will become
	 * inconsistent and the following behaviour will not be predictable.
	 */
 public void enqueue( int x ) {
  ensureElement( x );
  if ( size == array.length ) array = IntArrays.grow( array, size + 1 );
  array[ size++ ] = x;
 }

 /** Dequeues an element.
	 *
	 * <P>Note that each call to this method requires a complete scan of the backing array. Please
	 * consider caching its result.
	 */

 public int dequeue() {
  ensureNonEmpty();
  final int first = findFirst();
  final int result = array[ first ];
  if ( --size != 0 ) System.arraycopy( array, first + 1, array, first, size - first );
  return result;
 }

 public int first() {
  ensureNonEmpty();
  return array[ findFirst() ];
 }

 public int last() {
  ensureNonEmpty();
  return array[ findLast() ];
 }

 public void changed() {
  ensureNonEmpty();
 }

 /** {@inheritDoc}
	 *
	 * <P>Note that for efficiency reasons this method will <em>not</em> throw an exception
	 * when <code>index</code> is not in the queue.
	 */

 public void changed( int index ) {
  ensureElement( index );
 }

 public void allChanged() {}

 public void remove( int index ) {
  ensureElement( index );
  final int[] a = array;
  int i = size;
  while( i-- != 0 ) if ( a[ i ] == index ) break;
  if ( i < 0 ) throw new IllegalArgumentException( "Index " + index + " does not belong to the queue" );
  if ( --size != 0 ) System.arraycopy( a, i + 1, a, i, size - i );
 }

 public int front( int[] a ) {
  final long top = refArray[ array[ findFirst() ] ];
  int i = size, c = 0;
  while( i-- != 0 ) if ( ( (top) == (refArray[ array[ i ] ]) ) ) a[ c++ ] = array[ i ];
  return c;
 }

 public int size() { return size; }

 public void clear() { size = 0; }

 /** Trims the backing array so that it has exactly {@link #size()} elements.
	 */

 public void trim() {
  array = IntArrays.trim( array, size );
 }

 public LongComparator comparator() { return c; }

 public String toString() {
  StringBuffer s = new StringBuffer();
  s.append( "[" );
  for ( int i = 0; i < size; i++ ) {
   if ( i != 0 ) s.append( ", " );
   s.append( refArray[ array [ i ] ] );
  }
  s.append( "]" );
  return s.toString();
 }
}
