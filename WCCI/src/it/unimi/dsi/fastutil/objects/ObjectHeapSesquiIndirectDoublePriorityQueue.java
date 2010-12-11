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

package it.unimi.dsi.fastutil.objects;


import java.util.Comparator;

import it.unimi.dsi.fastutil.IndirectDoublePriorityQueue;


/** A type-specific heap-based sesqui-indirect double priority queue.
 *
 * <P>Instances of this class are based on a semi-indirect and an indirect
 * heap-based queues. The queues are enlarged as needed, but they are never
 * shrunk. Use the {@link #trim()} method to reduce their size, if necessary.
 *
 * <P>Either comparator may be <code>null</code>, indicating that natural comparison should take place. Of course,
 * it makes little sense having them equal.
 */

public class ObjectHeapSesquiIndirectDoublePriorityQueue <K> extends ObjectHeapSemiIndirectPriorityQueue <K> implements IndirectDoublePriorityQueue <K> {

 /** The secondary indirect queue. */
 protected ObjectHeapIndirectPriorityQueue <K> secondaryQueue;

 /** Creates a new empty queue with a given capacity.
	 *
	 * @param refArray the reference array.
	 * @param capacity the initial capacity of this queue.
	 * @param c the primary comparator used in this queue, or <code>null</code> for the natural order.
	 * @param d the secondary comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public ObjectHeapSesquiIndirectDoublePriorityQueue( K[] refArray, int capacity, Comparator <? super K> c, Comparator <? super K> d ) {
  super( refArray, capacity, c );
  secondaryQueue = new ObjectHeapIndirectPriorityQueue <K>( refArray, capacity, d );
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
 public ObjectHeapSesquiIndirectDoublePriorityQueue( K[] refArray, int capacity, Comparator <? super K> c ) {
  super( refArray, capacity, c );
  secondaryQueue = new ObjectHeapIndirectPriorityQueue <K>( refArray, capacity, c == null ? ObjectComparators.OPPOSITE_COMPARATOR : ObjectComparators.oppositeComparator( c ) );
 }


 /** Creates a new empty queue with a given capacity and natural order as primary comparator.
	 *
	 * <P>This constructor uses as secondary comparator the opposite of the natural order.
	 *
	 * @param refArray the reference array.
	 * @param capacity the initial capacity of this queue.
	 */
 public ObjectHeapSesquiIndirectDoublePriorityQueue( K[] refArray, int capacity ) {
  this( refArray, capacity, null );
 }


 /** Creates a new empty queue with capacity equal to the length of the reference array.
	 *
	 * @param refArray the reference array.
	 * @param c the primary comparator used in this queue, or <code>null</code> for the natural order.
	 * @param d the secondary comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public ObjectHeapSesquiIndirectDoublePriorityQueue( K[] refArray, Comparator <? super K> c, Comparator <? super K> d ) {
  this( refArray, refArray.length, c, d );
 }

 /** Creates a new empty queue with capacity equal to the length of the reference array.
	 *
	 * <P>This constructor uses as secondary comparator the opposite order of <code>c</code>.
	 *
	 * @param refArray the reference array.
	 * @param c the primary comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public ObjectHeapSesquiIndirectDoublePriorityQueue( K[] refArray, Comparator <? super K> c ) {
  this( refArray, refArray.length, c );
 }

 /** Creates a new empty queue with capacity equal to the length of the reference array and natural order as primary comparator.
	 *
	 * <P>This constructor uses as secondary comparator the opposite of the natural order.
	 *
	 * @param refArray the reference array.
	 */
 public ObjectHeapSesquiIndirectDoublePriorityQueue( K[] refArray ) {
  this( refArray, refArray.length, null );
 }


 /** Wraps a given array in a queue using the given comparators.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The first <code>size</code> element of the array will be rearranged so to form a heap, and
	 * moreover the array will be cloned and wrapped in a secondary queue (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param size the number of elements to be included in the queue.
	 * @param c the primary comparator used in this queue, or <code>null</code> for the natural order.
	 * @param d the secondary comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public ObjectHeapSesquiIndirectDoublePriorityQueue( final K[] refArray, final int[] a, int size, final Comparator <? super K> c, final Comparator <? super K> d ) {
  super( refArray, a, size, c );
  this.secondaryQueue = new ObjectHeapIndirectPriorityQueue <K>( refArray, a.clone(), size, d );
 }

 /** Wraps a given array in a queue using the given comparators.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The first elements of the array will be rearranged so to form a heap, and
	 * moreover the array will be cloned and wrapped in a secondary queue (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param c the primary comparator used in this queue, or <code>null</code> for the natural order.
	 * @param d the secondary comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public ObjectHeapSesquiIndirectDoublePriorityQueue( final K[] refArray, final int[] a, final Comparator <? super K> c, final Comparator <? super K> d ) {
  this( refArray, a, a.length, c, d );
 }


 /** Wraps a given array in a queue using a given comparator.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The first <code>size</code> element of the array will be rearranged so to form a heap, and
	 * moreover the array will be cloned and wrapped in a secondary queue (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * <P>This constructor uses as secondary comparator the opposite order of <code>c</code>.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param size the number of elements to be included in the queue.
	 * @param c the primary comparator used in this queue, or <code>null</code> for the natural order.
	 */
 @SuppressWarnings("unchecked")
 public ObjectHeapSesquiIndirectDoublePriorityQueue( final K[] refArray, final int[] a, int size, final Comparator <? super K> c ) {
  this( refArray, a, size, c, c == null ? ObjectComparators.OPPOSITE_COMPARATOR : ObjectComparators.oppositeComparator( c ) );
 }


 /** Wraps a given array in a queue using a given comparator.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The elements of the array will be rearranged so to form a heap, and
	 * moreover the array will be cloned and wrapped in a secondary queue (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * <P>This constructor uses as secondary comparator the opposite order of <code>c</code>.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param c the primary comparator used in this queue, or <code>null</code> for the natural order.
	 */
 public ObjectHeapSesquiIndirectDoublePriorityQueue( final K[] refArray, final int[] a, final Comparator <? super K> c ) {
  this( refArray, a, a.length, c );
 }

 /** Wraps a given array in a queue using the natural order.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The first <code>size</code> element of the array will be rearranged so to form a heap, and
	 * moreover the array will be cloned and wrapped in a secondary queue (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * <P>This constructor uses as secondary comparator the opposite of the natural order.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 * @param size the number of elements to be included in the queue.
	 */
 public ObjectHeapSesquiIndirectDoublePriorityQueue( final K[] refArray, final int[] a, int size ) {
  this( refArray, a, size, null );
 }


 /** Wraps a given array in a queue using the natural order.
	 *
	 * <P>The queue returned by this method will be backed by the given array.
	 * The elements of the array will be rearranged so to form a heap, and
	 * moreover the array will be cloned and wrapped in a secondary queue (this is
	 * more efficient than enqueing the elements of <code>a</code> one by one).
	 *
	 * <P>This constructor uses as secondary comparator the opposite of the natural order.
	 *
	 * @param refArray the reference array.
	 * @param a an array of indices into <code>refArray</code>.
	 */
 public ObjectHeapSesquiIndirectDoublePriorityQueue( final K[] refArray, final int[] a ) {
  this( refArray, a, a.length );
 }

 public void enqueue( int x ) {
  secondaryQueue.enqueue( x );
  super.enqueue( x );
 }

 public int dequeue() {
  final int result = super.dequeue();
  secondaryQueue.remove( result );
  return result;
 }

 public int secondaryFirst() {
  return secondaryQueue.first();
 }

 public int secondaryLast() { throw new UnsupportedOperationException(); }

 public int secondaryFront( final int[] a ) {
  return secondaryQueue.front( a );
 }

 public void changed() {
  secondaryQueue.changed( heap[ 0 ] );
  super.changed();
 }

 public void allChanged() {
  secondaryQueue.allChanged();
  super.allChanged();
 }

 public void clear() {
  super.clear();
  secondaryQueue.clear();
 }

 /** Trims the underlying queues so they have exactly {@link #size()} elements.
	 */

 public void trim() {
  super.trim();
  secondaryQueue.trim();
 }

 /** Returns the secondary comparator of this queue.
	 *
	 * @return the secondary comparator of this queue.
	 * @see #secondaryFirst()
	 */
 public Comparator <? super K> secondaryComparator() { return secondaryQueue.comparator(); }
}
