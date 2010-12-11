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
 * Copyright (C) 2007-2008 Sebastiano Vigna 
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

/** A simple, brute-force implementation of a set based on a backing array.
 *
 * <p>The main purpose of this
 * implementation is that of wrapping cleanly the brute-force approach to the storage of a very 
 * small number of items: just put them into an array and scan linearly to find an item.
 */

public class ShortArraySet extends AbstractShortSet implements java.io.Serializable, Cloneable {

 private static final long serialVersionUID = 1L;
 /** The backing array (valid up to {@link #size}, excluded). */
 private transient short[] a;
 /** The number of valid entries in {@link #a}. */
 private int size;

 /** Creates a new array set using the given backing array.
	 * 
	 * @param a the backing array.
	 */
 public ShortArraySet( final short[] a ) {
  this.a = a;
 }

 /** Creates a new empty array set.
	 */
 public ShortArraySet() {
  this.a = ShortArrays.EMPTY_ARRAY;
 }

 /** Creates a new empty array set of given initial capacity.
	 * 
	 * @param capacity the initial capacity.
	 */
 public ShortArraySet( final int capacity ) {
  this.a = new short[ capacity ];
 }

 /** Creates a new array set copying the contents of a given set.
	 */
 public ShortArraySet( ShortSet s ) {
  this( s.size () );
  addAll( s );
 }

 /** Creates a new array set using the given backing array and the given number of elements of the array.
	 *
	 * <p>It is responsibility of the caller that the first <code>size</code> elements of <code>a</code> are distinct
	 * (of course, distinct by identity, not by {@link Object#equals(Object)}).
	 * 
	 * @param a the backing array.
	 * @param size the number of valid elements in <code>a</code>.
	 */
 public ShortArraySet( final short[] a, final int size ) {
  this.a = a;
  this.size = size;
  if ( size > a.length ) throw new IllegalArgumentException( "The provided size (" + size + ") is larger than or equal to the array size (" + a.length + ")" );
 }

 private int findKey( final short o ) {
  for( int i = size; i-- != 0; ) if ( ( (a[ i ]) == (o) ) ) return i;
  return -1;
 }

 @Override
 @SuppressWarnings("unchecked")
 public ShortIterator iterator() {
  return ShortIterators.wrap( a, 0, size );
 }

 @SuppressWarnings("unchecked")
 public boolean contains( final short k ) {
  return findKey( k ) != -1;
 }

 public int size() {
  return size;
 }

 @Override
 @SuppressWarnings("unchecked")
 public boolean remove( final short k ) {
  final int pos = findKey( k );
  if ( pos == -1 ) return false;
  final int tail = size - pos - 1;
  for( int i = 0; i < tail; i++ ) a[ pos + i ] = a[ pos + i + 1 ];
  size--;



  return true;
 }

 @Override
 public boolean add( final short k ) {
  final int pos = findKey( k );
  if ( pos != -1 ) return false;
  if ( size == a.length ) {
   final short[] b = new short[ size == 0 ? 2 : size * 2 ];
   for( int i = size; i-- != 0; ) b[ i ] = a[ i ];
   a = b;
  }
  a[ size++ ] = k;
  return true;
 }

 @Override
 public void clear() {



  size = 0;
 }

 @Override
 public boolean isEmpty() {
  return size == 0;
 }

 /** Returns a deep copy of this set. 
	 *
	 * <P>This method performs a deep copy of this hash set; the data stored in the
	 * set, however, is not cloned. Note that this makes a difference only for object keys.
	 *
	 *  @return a deep copy of this set.
	 */

 @SuppressWarnings("unchecked")
 public Object clone() {
  ShortArraySet c;
  try {
   c = (ShortArraySet )super.clone();
  }
  catch(CloneNotSupportedException cantHappen) {
   throw new InternalError();
  }
  c.a = a.clone();
  return c;
 }

 private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
  s.defaultWriteObject();
  for( int i = 0; i < size; i++ ) s.writeShort( a[ i ] );
 }


 @SuppressWarnings("unchecked")
 private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
  s.defaultReadObject();
  a = new short[ size ];
  for( int i = 0; i < size; i++ ) a[ i ] = s.readShort();
 }

}
