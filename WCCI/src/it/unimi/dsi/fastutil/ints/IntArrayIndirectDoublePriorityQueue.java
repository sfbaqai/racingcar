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






/** A type-specific array-based indirect double priority queue.
 *
 * <P>Instances of this class are based on a single array. This implementation
 * is extremely inefficient, but it is difficult to beat when the size of the
 * queue is very small.  The array is enlarged as needed, but it is never
 * shrunk. Use the {@link #trim()} method to reduce its size, if necessary.
 *
 * <P>Either comparator may be <code>null</code>, indicating that natural comparison should take place. Of course,
 * it makes little sense having them equal.
 */

public class IntArrayIndirectDoublePriorityQueue extends IntArrayIndirectPriorityQueue implements IntIndirectDoublePriorityQueue {

 /** The secondary comparator. */
 protected IntComparator secondaryComparator;

 /** Creates a new empty queue with a given capacity.
	 *
	 * @param refArray the reference array.
	 * @param capacity the initial capacity of this queue.
	 * @param c the primary comparator used in this queue, or <code>null</code> for the natural order.
	 * @param d the secondary comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public IntArrayIndirectDoublePriorityQueue( int[] refArray, int capacity, IntComparator c, IntComparator d ) {
  super( refArray, capacity, c );
  secondaryComparator = d;
 }


 /** Creates a new empty queue with a given capacity.
	 *
	 * <P>This constructor uses as secondary comparator the opposite order of <code>c</code>.
	 *
	 * @param refArray the reference array.
	 * @param capacity the initial capacity of this queue.
	 * @param c the primary comparator used in this queue, or <code>null</code> for the natural order.
	 */
 @SuppressWarnings("unchecked")
 public IntArrayIndirectDoublePriorityQueue( int[] refArray, int capacity, IntComparator c ) {
  super( refArray, capacity, c == null ? IntComparators.OPPOSITE_COMPARATOR : IntComparators.oppositeComparator( c ) );
 }


 /** Creates a new empty queue with a given capacity and natural order as primary comparator.
	 *
	 * <P>This constructor uses as secondary comparator the opposite of the natural order.
	 *
	 * @param refArray the reference array.
	 * @param capacity the initial capacity of this queue.
	 */
 public IntArrayIndirectDoublePriorityQueue( int[] refArray, int capacity ) {
  this( refArray, capacity, null );
 }


 /** Creates a new empty queue with capacity equal to the length of the reference array.
	 *
	 * @param refArray the reference array.
	 * @param c the primary comparator used in this queue, or <code>null</code> for the natural order.
	 * @param d the secondary comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public IntArrayIndirectDoublePriorityQueue( int[] refArray, IntComparator c, IntComparator d ) {
  this( refArray, refArray.length, c, d );
 }

 /** Creates a new empty queue with capacity equal to the length of the reference array.
	 *
	 * <P>This constructor uses as secondary comparator the opposite order of <code>c</code>.
	 *
	 * @param refArray the reference array.
	 * @param c the primary comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public IntArrayIndirectDoublePriorityQueue( int[] refArray, IntComparator c ) {
  this( refArray, refArray.length, c );
 }

 /** Creates a new empty queue with capacity equal to the length of the reference array and natural order as primary comparator.
	 *
	 * <P>This constructor uses as secondary comparator the opposite of the natural order.
	 *
	 * @param refArray the reference array.
	 */
 public IntArrayIndirectDoublePriorityQueue( int[] refArray ) {
  this( refArray, refArray.length, null );
 }


 /** Wraps a given array in a queue using the given comparators.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param size the number of elements to be included in the queue.
	 * @param c the primary comparator used in this queue, or <code>null</code> for the natural order.
	 * @param d the secondary comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public IntArrayIndirectDoublePriorityQueue( final int[] refArray, final int[] a, int size, final IntComparator c, final IntComparator d ) {
  this( refArray, 0, c, d );
  this.array = a;
  this.size = size;
 }

 /** Wraps a given array in a queue using the given comparators.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param c the primary comparator used in this queue, or <code>null</code> for the natural order.
	 * @param d the secondary comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public IntArrayIndirectDoublePriorityQueue( final int[] refArray, final int[] a, final IntComparator c, final IntComparator d ) {
  this( refArray, a, a.length, c, d );
 }


 /** Wraps a given array in a queue using a given comparator and its opposite.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param size the number of elements to be included in the queue.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public IntArrayIndirectDoublePriorityQueue( final int[] refArray, final int[] a, int size, final IntComparator c ) {
  this( refArray, 0, c );
  this.array = a;
  this.size = size;
 }


 /** Wraps a given array in a queue using a given comparator and its opposite.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public IntArrayIndirectDoublePriorityQueue( final int[] refArray, final int[] a, final IntComparator c ) {
  this( refArray, a, a.length, c );
 }

 /** Wraps a given array in a queue using the natural order and its opposite.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param size the number of elements to be included in the queue.
	 */
 public IntArrayIndirectDoublePriorityQueue( final int[] refArray, final int[] a, int size ) {
  this( refArray, a, size, null );
 }


 /** Wraps a given array in a queue using the natural order and its opposite.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 */
 public IntArrayIndirectDoublePriorityQueue( final int[] refArray, final int[] a ) {
  this( refArray, a, a.length );
 }

 /** Returns the index (in {@link #array}) of the smallest element w.r.t. the {@linkplain #secondaryComparator secondary comparator}. */

 @SuppressWarnings("unchecked")
 private int findSecondaryFirst() {
  int i = size;
  int firstIndex = --i;
  int first = refArray[ array[ firstIndex ] ];

  if ( secondaryComparator == null ) while( i-- != 0 ) { if ( ( (refArray[ array[ i ] ]) < (first) ) ) first = refArray[ array[ firstIndex = i ] ]; }
  else while( i-- != 0 ) { if ( secondaryComparator.compare( refArray[ array[ i ] ], first ) < 0 ) first = refArray[ array[ firstIndex = i ] ]; }

  return firstIndex;
 }

 @SuppressWarnings("unchecked")
 private int findSecondaryLast() {
  int i = size;
  int lastIndex = --i;
  int last = refArray[ array[ lastIndex ] ];

  if ( secondaryComparator == null ) while( i-- != 0 ) { if ( ( (last) < (refArray[ array[ i ] ]) ) ) last = refArray[ array[ lastIndex = i ] ]; }
  else while( i-- != 0 ) { if ( secondaryComparator.compare( last, refArray[ array[ i ] ] ) < 0 ) last = refArray[ array[ lastIndex = i ] ]; }

  return lastIndex;
 }


 public int secondaryFirst() {
  return array[ findSecondaryFirst() ];
 }


 public int secondaryLast() {
  return array[ findSecondaryLast() ];
 }

 public int secondaryFront( int[] a ) {
  final int secondaryTop = refArray[ array[ findSecondaryFirst() ] ];
  int i = size, c = 0;
  while( i-- != 0 ) if ( ( (secondaryTop) == (refArray[ array[ i ] ]) ) ) a[ c++ ] = array[ i ];
  return c;
 }

 public void changed( int i ) {}

 /** Returns the secondary comparator of this queue.
	 *
	 * @return the secondary comparator of this queue.
	 * @see #secondaryFirst()
	 */
 public IntComparator secondaryComparator() { return secondaryComparator; }
}
