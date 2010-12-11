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

package it.unimi.dsi.fastutil.chars;







import java.util.NoSuchElementException;


/** A type-specific heap-based priority queue.
 *
 * <P>Instances of this class represent a priority queue using a heap. The heap is enlarged as needed, but
 * it is never shrunk. Use the {@link #trim()} method to reduce its size, if necessary.
 */

public class CharHeapPriorityQueue extends AbstractCharPriorityQueue {

 /** The heap array. */
 @SuppressWarnings("unchecked")
 protected char[] heap = CharArrays.EMPTY_ARRAY;

 /** The number of elements in this queue. */
 protected int size;

 /** The type-specific comparator used in this queue. */
 protected CharComparator c;

 /** Creates a new empty queue with a given capacity and comparator.
	 *
	 * @param capacity the initial capacity of this queue.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 @SuppressWarnings("unchecked")
 public CharHeapPriorityQueue( int capacity, CharComparator c ) {
  if ( capacity > 0 ) this.heap = new char[ capacity ];
  this.c = c;
 }

 /** Creates a new empty queue with a given capacity and using the natural order.
	 *
	 * @param capacity the initial capacity of this queue.
	 */
 public CharHeapPriorityQueue( int capacity ) {
  this( capacity, null );
 }

 /** Creates a new empty queue with a given comparator.
	 *
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public CharHeapPriorityQueue( CharComparator c ) {
  this( 0, c );
 }

 /** Creates a new empty queue using the natural order. 
	 */
 public CharHeapPriorityQueue() {
  this( 0, null );
 }

 /** Wraps a given array in a queue using a given comparator.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The first <code>size</code> element of the array will be rearranged so to form a heap (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * @param a an array.
	 * @param size the number of elements to be included in the queue.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public CharHeapPriorityQueue( final char[] a, int size, final CharComparator c ) {
  this( c );
  this.heap = a;
  this.size = size;
  CharHeaps.makeHeap( a, size, c );
 }


 /** Wraps a given array in a queue using a given comparator.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The elements of the array will be rearranged so to form a heap (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * @param a an array.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public CharHeapPriorityQueue( final char[] a, final CharComparator c ) {
  this( a, a.length, c );
 }

 /** Wraps a given array in a queue using the natural order.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The first <code>size</code> element of the array will be rearranged so to form a heap (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * @param a an array.
	 * @param size the number of elements to be included in the queue.
	 */
 public CharHeapPriorityQueue( final char[] a, int size ) {
  this( a, size, null );
 }


 /** Wraps a given array in a queue using the natural order.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The elements of the array will be rearranged so to form a heap (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * @param a an array.
	 */
 public CharHeapPriorityQueue( final char[] a ) {
  this( a, a.length );
 }

 public void enqueue( char x ) {
  if ( size == heap.length ) heap = CharArrays.grow( heap, size + 1 );

  heap[ size++ ] = x;
  CharHeaps.upHeap( heap, size, size - 1, c );
 }

 public char dequeueChar() {
  if ( size == 0 ) throw new NoSuchElementException();

  final char result = heap[ 0 ];
  heap[ 0 ] = heap[ --size ];



  if ( size != 0 ) CharHeaps.downHeap( heap, size, 0, c );
  return result;
 }

 public char firstChar() {
  if ( size == 0 ) throw new NoSuchElementException();
  return heap[ 0 ];
 }

 public void changed() {
  CharHeaps.downHeap( heap, size, 0, c );
 }

 public int size() { return size; }

 public void clear() {



  size = 0;
 }

 /** Trims the underlying heap array so that it has exactly {@link #size()} elements.
	 */

 public void trim() {
  heap = CharArrays.trim( heap, size );
 }

 public CharComparator comparator() { return c; }
}
