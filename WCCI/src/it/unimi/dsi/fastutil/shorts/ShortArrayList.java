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
 * Copyright (C) 2002-2008 Sebastiano Vigna 
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

import it.unimi.dsi.fastutil.Arrays;

import java.util.Collection;
import java.util.Iterator;
import java.util.RandomAccess;
import java.util.NoSuchElementException;



/** A type-specific array-based list; provides some additional methods that use polymorphism to avoid (un)boxing. 
 *
 * <P>This class implements a lightweight, fast, open, optimized,
 * reuse-oriented version of array-based lists. Instances of this class
 * represent a list with an array that is enlarged as needed when new entries
 * are created (by dividing the current length by the golden ratio), but is
 * <em>never</em> made smaller (even on a {@link #clear()}). A family of
 * {@linkplain #trim() trimming methods} lets you control the size of the
 * backing array; this is particularly useful if you reuse instances of this class.
 * Range checks are equivalent to those of {@link java.util}'s classes, but
 * they are delayed as much as possible. The backing array is exposed by the
 * {@link #elements()} method.
 *
 * <p>This class implements the bulk methods <code>removeElements()</code>,
 * <code>addElements()</code> and <code>getElements()</code> using
 * high-performance system calls (e.g., {@link
 * System#arraycopy(Object,int,Object,int,int) System.arraycopy()} instead of
 * expensive loops.
 *
 * @see java.util.ArrayList
 */

public final class ShortArrayList extends AbstractShortList implements RandomAccess, Cloneable, java.io.Serializable {
 public static final long serialVersionUID = -7046029254386353130L;
 /** The initial default capacity of an array list. */
 public static final int DEFAULT_INITIAL_CAPACITY = 16;

 /** The inverse of the golden ratio times 2<sup>16</sup>. */
 protected static final long ONEOVERPHI = 106039;







 /** The backing array. */
 protected transient short a[];

 /** The current actual size of the list (never greater than the backing-array length). */
 protected int size;

 private static final boolean ASSERTS = false;

 /** Creates a new array list using a given array.
	 *
	 * <P>This constructor is only meant to be used by the wrapping methods.
	 *
	 * @param a the array that will be used to back this array list.
	 */

 @SuppressWarnings("unused")
 protected ShortArrayList( final short a[], boolean dummy ) {
  this.a = a;



 }

 /** Creates a new array list with given capacity.
	 *
	 * @param capacity the initial capacity of the array list (may be 0).
	 */

 @SuppressWarnings("unchecked")
 public ShortArrayList( final int capacity ) {
  if ( capacity < 0 ) throw new IllegalArgumentException( "Initial capacity (" + capacity + ") is negative" );

  a = new short[ capacity ];



 }

 /** Creates a new array list with {@link #DEFAULT_INITIAL_CAPACITY} capacity.
	 */

 public ShortArrayList() {
  this( DEFAULT_INITIAL_CAPACITY );
 }

 /** Creates a new array list and fills it with a given collection.
	 *
	 * @param c a collection that will be used to fill the array list.
	 */

 public ShortArrayList( final Collection<? extends Short> c ) {
  this( c.size() );

  size = ShortIterators.unwrap( ShortIterators.asShortIterator( c.iterator() ), a );



 }

 /** Creates a new array list and fills it with a given type-specific collection.
	 *
	 * @param c a type-specific collection that will be used to fill the array list.
	 */

 public ShortArrayList( final ShortCollection c ) {
  this( c.size() );
  size = ShortIterators.unwrap( c.iterator(), a );
 }

 /** Creates a new array list and fills it with a given type-specific list.
	 *
	 * @param l a type-specific list that will be used to fill the array list.
	 */

 public ShortArrayList( final ShortList l ) {
  this( l.size() );
  l.getElements( 0, a, 0, size = l.size() );
 }

 /** Creates a new array list and fills it with the elements of a given array.
	 *
	 * @param a an array whose elements will be used to fill the array list.
	 */

 public ShortArrayList( final short a[] ) {
  this( a, 0, a.length );
 }

 /** Creates a new array list and fills it with the elements of a given array.
	 *
	 * @param a an array whose elements will be used to fill the array list.
	 * @param offset the first element to use.
	 * @param length the number of elements to use.
	 */

 public ShortArrayList( final short a[], final int offset, final int length ) {
  this( length );
  System.arraycopy( a, offset, this.a, 0, length );
  size = length;
 }

 /** Creates a new array list and fills it with the elements returned by an iterator..
	 *
	 * @param i an iterator whose returned elements will fill the array list.
	 */

 public ShortArrayList( final Iterator<? extends Short> i ) {
  this();
  while( i.hasNext() ) this.add( i.next() );
 }

 /** Creates a new array list and fills it with the elements returned by a type-specific iterator..
	 *
	 * @param i a type-specific iterator whose returned elements will fill the array list.
	 */

 public ShortArrayList( final ShortIterator i ) {
  this();
  while( i.hasNext() ) this.add( i.nextShort() );
 }


 /** Returns the backing array of this list.
	 *
	 * @return the backing array.
	 */

 public final short[] elements() {
  return a;
 }
 /** Wraps a given array into an array list of given size.
	 *
	 * @param a an array to wrap.
	 * @param length the length of the resulting array list.
	 * @return a new array list of the given size, wrapping the given array.
	 */

 public static final ShortArrayList wrap( final short a[], final int length ) {
  if ( length > a.length ) throw new IllegalArgumentException( "The specified length (" + length + ") is greater than the array size (" + a.length + ")" );
  final ShortArrayList l = new ShortArrayList ( a, false );
  l.size = length;
  return l;
 }

 /** Wraps a given array into an array list.
	 *
	 * @param a an array to wrap.
	 * @return a new array list wrapping the given array.
	 */

 public static final ShortArrayList wrap( final short a[] ) {
  return wrap( a, a.length );
 }


 /** Ensures that this array list can contain the given number of entries without resizing.
	 *
	 * @param capacity the new minimum capacity for this array list.
	 */
 @SuppressWarnings("unchecked")
 public final void ensureCapacity( final int capacity ) {

  a = ShortArrays.ensureCapacity( a, capacity, size );
  if ( ASSERTS ) assert size <= a.length;
 }

 /** Grows this array, ensuring that it can contain the given number of entries without resizing,
	 * and in case enlarging it at least by the golden ratio.
	 *
	 * @param capacity the new minimum capacity for this array list.
	 */
 @SuppressWarnings("unchecked")
 private final void grow( final int capacity ) {
  if ( capacity > a.length ) {

   a = ShortArrays.grow( a, capacity, size );
  }
  if ( ASSERTS ) assert size <= a.length;
 }

 public final void add( final int index, final short k ) {
  if (index>a.length)
   ensureIndex( index );
  if (size>=a.length) grow( size + 1 );
  if ( index != size ) System.arraycopy( a, index, a, index + 1, size - index );
  a[ index ] = k;
  size++;
  if ( ASSERTS ) assert size <= a.length;
 }

 public final boolean add( final short k ) {
  if (size>=a.length) grow( size + 1 );
  a[ size++ ] = k;
  if ( ASSERTS ) assert size <= a.length;
  return true;
 }

 public final short getShort( final int index ) {
  if ( index >= size ) throw new IndexOutOfBoundsException( "Index (" + index + ") is greater than or equal to list size (" + size + ")" );
  return a[ index ];
 }

 public final int indexOf( final short k ) {



  for( int i = 0; i < size; i++ ) if ( ( (k) == (a[ i ]) ) ) return i;
  return -1;
 }


 public final int lastIndexOf( final short k ) {



  int i = size;
  while( i-- != 0 ) if ( ( (k) == (a[ i ]) ) ) return i;
  return -1;
 }

 public final short removeShort( final int index ) {
  if ( index >= size ) throw new IndexOutOfBoundsException( "Index (" + index + ") is greater than or equal to list size (" + size + ")" );
  final short old = a[ index ];
  size--;
  if ( index != size ) System.arraycopy( a, index + 1, a, index, size - index );



  if ( ASSERTS ) assert size <= a.length;
  return old;
 }

 public final boolean rem( final short k ) {
  int index = indexOf( k );
  if ( index == -1 ) return false;
  removeShort( index );
  if ( ASSERTS ) assert size <= a.length;
  return true;
 }







 public short set( final int index, final short k ) {
  if ( index >= size ) throw new IndexOutOfBoundsException( "Index (" + index + ") is greater than or equal to list size (" + size + ")" );
  short old = a[ index ];
  a[ index ] = k;
  return old;
 }

 public final void clear() {



  size = 0;
  if ( ASSERTS ) assert size <= a.length;
 }

 public final int size() {
  return size;
 }

 public final void size( final int size ) {
  if ( size > a.length ) ensureCapacity( size );
  /*if ( size > this.size ) ARRAYS.fill( a, this.size, size, KEY_NULL );
#if #keys(reference)
		else ARRAYS.fill( a, size, this.size, KEY_NULL );
#endif 
//*/
  this.size = size;
 }

 public final boolean isEmpty() {
  return size == 0;
 }

 /** Trims this array list so that the capacity is equal to the size. 
	 *
	 * @see java.util.ArrayList#trimToSize()
	 */
 public final void trim() {
  trim( 0 );
 }

 /** Trims the backing array if it is too large.
	 * 
	 * If the current array length is smaller than or equal to
	 * <code>n</code>, this method does nothing. Otherwise, it trims the
	 * array length to the maximum between <code>n</code> and {@link #size()}.
	 *
	 * <P>This method is useful when reusing lists.  {@linkplain #clear() Clearing a
	 * list} leaves the array length untouched. If you are reusing a list
	 * many times, you can call this method with a typical
	 * size to avoid keeping around a very large array just
	 * because of a few large transient lists.
	 *
	 * @param n the threshold for the trimming.
	 */

 @SuppressWarnings("unchecked")
 public final void trim( final int n ) {
  if ( n >= a.length || size == a.length ) return;
  final short t[] = new short[ Math.max( n, size ) ];
  System.arraycopy( a, 0, t, 0, size );
  a = t;
  if ( ASSERTS ) assert size <= a.length;
 }


    /** Copies element of this type-specific list into the given array using optimized system calls.
	 *
	 * @param from the start index (inclusive).
	 * @param a the destination array.
	 * @param offset the offset into the destination array where to store the first element copied.
	 * @param length the number of elements to be copied.
	 */

 public final void getElements( final int from, final short[] a, final int offset, final int length ) {
  ShortArrays.ensureOffsetLength( a, offset, length );
  System.arraycopy( this.a, from, a, offset, length );
 }

 /** Removes elements of this type-specific list using optimized system calls.
	 *
	 * @param from the start index (inclusive).
	 * @param to the end index (exclusive).
	 */
 public final void removeElements( final int from, final int to ) {
  Arrays.ensureFromTo( size, from, to );
  System.arraycopy( a, to, a, from, size - to );
  size -= ( to - from );




 }


 /** Adds elements to this type-specific list using optimized system calls.
	 *
	 * @param index the index at which to add elements.
	 * @param a the array containing the elements.
	 * @param offset the offset of the first element to add.
	 * @param length the number of elements to add.
	 */
 public final void addElements( final int index, final short a[], final int offset, final int length ) {
  if (index>=this.a.length) ensureIndex( index );
  ShortArrays.ensureOffsetLength( a, offset, length );
  if (size+length>this.a.length) grow( size + length );
  System.arraycopy( this.a, index, this.a, index + length, size - index );
  System.arraycopy( a, offset, this.a, index, length );
  size += length;
 }





 public final short[] toShortArray( short a[] ) {
  if ( a == null || a.length < size ) a = new short[ size ];
  System.arraycopy( this.a, 0, a, 0, size );
  return a;
 }

 public final boolean addAll( int index, final ShortCollection c ) {
  if (index>=a.length) ensureIndex( index );
  int n = c.size();
  if ( n == 0 ) return false;
  if (size+n>a.length) grow( size + n );
  if ( index != size ) System.arraycopy( a, index, a, index + n, size - index );
  final ShortIterator i = c.iterator();
  size += n;
  while( n-- != 0 ) a[ index++ ] = i.nextShort();
  if ( ASSERTS ) assert size <= a.length;
  return true;
 }

 public final boolean addAll( final int index, final ShortList l ) {
  if (index>=a.length) ensureIndex( index );
  final int n = l.size();
  if ( n == 0 ) return false;
  if (size+n>a.length) grow( size + n );
  if ( index != size ) System.arraycopy( a, index, a, index + n, size - index );
  l.getElements( 0, a, index, n );
  size += n;
  if ( ASSERTS ) assert size <= a.length;
  return true;
 }



 public final ShortListIterator listIterator( final int index ) {
  if (index>=a.length) ensureIndex( index );

  return new AbstractShortListIterator () {
    int pos = index, last = -1;

    public boolean hasNext() { return pos < size; }
    public boolean hasPrevious() { return pos > 0; }
    public short nextShort() { if ( ! hasNext() ) throw new NoSuchElementException(); return a[ last = pos++ ]; }
    public short previousShort() { if ( ! hasPrevious() ) throw new NoSuchElementException(); return a[ last = --pos ]; }
    public int nextIndex() { return pos; }
    public int previousIndex() { return pos - 1; }
    public void add( short k ) {
     if ( last == -1 ) throw new IllegalStateException();
     ShortArrayList.this.add( pos++, k );
     last = -1;
    }
    public void set( short k ) {
     if ( last == -1 ) throw new IllegalStateException();
     ShortArrayList.this.set( last, k );
    }
    public void remove() {
     if ( last == -1 ) throw new IllegalStateException();
     ShortArrayList.this.removeShort( last );
     /* If the last operation was a next(), we are removing an element *before* us, and we must decrease pos correspondingly. */
     if ( last < pos ) pos--;
     last = -1;
    }
   };
 }


 @SuppressWarnings("unchecked")
 public final Object clone() {
  ShortArrayList c = new ShortArrayList( size );
  System.arraycopy( a, 0, c.a, 0, size );
  c.size = size;
  return c;
 }







    /** Compares this type-specific array list to another one.
	 *
	 * <P>This method exists only for sake of efficiency. The implementation
	 * inherited from the abstract implementation would already work.
	 *
	 * @param l a type-specific array list.
     * @return true if the argument contains the same elements of this type-specific array list.
	 */
 public final boolean equals( final ShortArrayList l ) {
  if ( l == this ) return true;
  int s = size();
  if ( s != l.size() ) return false;
  final short[] a1 = a;
  final short[] a2 = l.a;




  while( s-- != 0 ) if ( a1[ s ] != a2[ s ] ) return false;

  return true;
 }




    /** Compares this list to another object. If the
     * argument is a {@link java.util.List}, this method performs a lexicographical comparison; otherwise,
     * it throws a <code>ClassCastException</code>.
     *
	 * <P>This method exists only for sake of efficiency. The implementation
	 * inherited from the abstract implementation would already work.
	 *
     * @param l an list.
     * @return if the argument is a {@link java.util.List}, a negative integer,
     * zero, or a positive integer as this list is lexicographically less than, equal
     * to, or greater than the argument.
     * @throws ClassCastException if the argument is not a list.
     */
 @SuppressWarnings("unchecked")
 public final int compareTo( final ShortArrayList l ) {
  final int s1 = size(), s2 = l.size();
  final short a1[] = a, a2[] = l.a;
  int r, i;

  for( i = 0; i < s1 && i < s2; i++ ) if ( ( r = ( (a1[ i ]) < (a2[ i ]) ? -1 : ( (a1[ i ]) == (a2[ i ]) ? 0 : 1 ) ) ) != 0 ) return r;

  return i < s2 ? -1 : ( i < s1 ? 1 : 0 );
 }



 private final void writeObject( java.io.ObjectOutputStream s ) throws java.io.IOException {
  s.defaultWriteObject();
  for( int i = 0; i < size; i++ ) s.writeShort( a[ i ] );
 }

 @SuppressWarnings("unchecked")
 private final void readObject( java.io.ObjectInputStream s ) throws java.io.IOException, ClassNotFoundException {
  s.defaultReadObject();
  a = new short[ size ];
  for( int i = 0; i < size; i++ ) a[ i ] = s.readShort();
 }
}
