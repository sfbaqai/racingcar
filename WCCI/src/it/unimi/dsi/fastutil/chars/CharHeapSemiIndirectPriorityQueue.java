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

import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.AbstractIndirectPriorityQueue;

/** A type-specific heap-based semi-indirect priority queue. 
 *
 * <P>Instances of this class use as reference list a <em>reference array</em>,
 * which must be provided to each constructor. The priority queue is
 * represented using a heap. The heap is enlarged as needed, but it is never
 * shrunk. Use the {@link #trim()} method to reduce its size, if necessary.
 *
 * <P>This implementation allows one to enqueue several time the same index, but
 * you must be careful when calling {@link #changed()}.
 */

public class CharHeapSemiIndirectPriorityQueue extends AbstractIndirectPriorityQueue<Character> {

 /** The reference array. */
 protected char refArray[];

 /** The semi-indirect heap. */
 protected int heap[] = IntArrays.EMPTY_ARRAY;

 /** The number of elements in this queue. */
 protected int size;

 /** The type-specific comparator used in this queue. */
 protected CharComparator c;

 /** Creates a new empty queue without elements with a given capacity and comparator.
	 *
	 * @param refArray the reference array.
	 * @param capacity the initial capacity of this queue.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public CharHeapSemiIndirectPriorityQueue( char[] refArray, int capacity, CharComparator c ) {
  if ( capacity > 0 ) this.heap = new int[ capacity ];
  this.refArray = refArray;
  this.c = c;
 }

 /** Creates a new empty queue with given capacity and using the natural order.
	 *
	 * @param refArray the reference array.
	 * @param capacity the initial capacity of this queue.
	 */
 public CharHeapSemiIndirectPriorityQueue( char[] refArray, int capacity ) {
  this( refArray, capacity, null );
 }

 /** Creates a new empty queue with capacity equal to the length of the reference array and a given comparator.
	 *
	 * @param refArray the reference array.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public CharHeapSemiIndirectPriorityQueue( char[] refArray, CharComparator c ) {
  this( refArray, refArray.length, c );
 }

 /** Creates a new empty queue with capacity equal to the length of the reference array and using the natural order. 
	 * @param refArray the reference array.
	 */
 public CharHeapSemiIndirectPriorityQueue( final char[] refArray ) {
  this( refArray, refArray.length, null );
 }


 /** Wraps a given array in a queue using a given comparator.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The first <code>size</code> element of the array will be rearranged so to form a heap (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param size the number of elements to be included in the queue.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public CharHeapSemiIndirectPriorityQueue( final char[] refArray, final int[] a, int size, final CharComparator c ) {
  this( refArray, 0, c );
  this.heap = a;
  this.size = size;
  CharSemiIndirectHeaps.makeHeap( refArray, a, size, c );
 }


 /** Wraps a given array in a queue using a given comparator.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The elements of the array will be rearranged so to form a heap (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param c the comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public CharHeapSemiIndirectPriorityQueue( final char[] refArray, final int[] a, final CharComparator c ) {
  this( refArray, a, a.length, c );
 }

 /** Wraps a given array in a queue using the natural order.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The first <code>size</code> element of the array will be rearranged so to form a heap (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param size the number of elements to be included in the queue.
	 */
 public CharHeapSemiIndirectPriorityQueue( final char[] refArray, final int[] a, int size ) {
  this( refArray, a, size, null );
 }


 /** Wraps a given array in a queue using the natural order.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The elements of the array will be rearranged so to form a heap (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 */
 public CharHeapSemiIndirectPriorityQueue( final char[] refArray, final int[] a ) {
  this( refArray, a, a.length );
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

 public void enqueue( int x ) {
  ensureElement( x );

  if ( size == heap.length ) heap = IntArrays.grow( heap, size + 1 );

  heap[ size++ ] = x;
  CharSemiIndirectHeaps.upHeap( refArray, heap, size, size - 1, c );
 }

 public int dequeue() {
  if ( size == 0 ) throw new NoSuchElementException();
  final int result = heap[ 0 ];
  heap[ 0 ] = heap[ --size ];
  if ( size != 0 ) CharSemiIndirectHeaps.downHeap( refArray, heap, size, 0, c );
  return result;
 }

 public int first() {
  if ( size == 0 ) throw new NoSuchElementException();
  return heap[ 0 ];
 }

 /** {@inheritDoc}
	 *
	 * <P>The caller <strong>must</strong> guarantee that when this method is called the
	 * index of the first element appears just once in the queue. Failure to do so
	 * will bring the queue in an inconsistent state, and will cause
	 * unpredictable behaviour.
	 */

 public void changed() {
  CharSemiIndirectHeaps.downHeap( refArray, heap, size, 0, c );
 }

 /** Rebuilds this heap in a bottom-up fashion.
	 */

 public void allChanged() {
  CharSemiIndirectHeaps.makeHeap( refArray, heap, size, c );
 }

 public int size() { return size; }

 public void clear() { size = 0; }

 /** Trims the backing array so that it has exactly {@link #size()} elements.
	 */

 public void trim() {
  heap = IntArrays.trim( heap, size );
 }

 public CharComparator comparator() { return c; }

 public int front( final int[] a ) {
  return CharSemiIndirectHeaps.front( refArray, heap, size, a );
 }

 public String toString() {
  StringBuffer s = new StringBuffer();
  s.append( "[" );
  for ( int i = 0; i < size; i++ ) {
   if ( i != 0 ) s.append( ", " );
   s.append( refArray[ heap [ i ] ] );
  }
  s.append( "]" );
  return s.toString();
 }
}
