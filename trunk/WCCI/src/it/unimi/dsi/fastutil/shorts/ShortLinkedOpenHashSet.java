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

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.bytes.ByteArrays;

import java.util.Arrays;
import java.util.Collection;

import java.util.Iterator;

import java.util.NoSuchElementException;






/**  A type-specific linked hash set with with a fast, small-footprint implementation.
 *
 * <P>Instances of this class use a hash table to represent a set. The table is
 * enlarged as needed when new entries are created, but it is <em>never</em> made
 * smaller (even on a {@link #clear()}). A family of {@linkplain #trim() trimming
 * methods} lets you control the size of the table; this is particularly useful
 * if you reuse instances of this class.
 *
 * <P>The enlargement speed is controlled by the <em>growth factor</em>, a
 * positive number. If the growth factor is <var>p</var>, then the table is
 * enlarged each time roughly by a factor 2<sup>p/16</sup>. By default, <var>p</var> is
 * {@link Hash#DEFAULT_GROWTH_FACTOR}, which means that the table is doubled at
 * each enlargement, but one can easily set more or less aggressive policies by
 * calling {@link #growthFactor(int)} (note that the growth factor is <em>not</em> serialized:
 * deserialized tables gets the {@linkplain Hash#DEFAULT_GROWTH_FACTOR default growth factor}).
 *
 * <P>This class implements the interface of a sorted set, so to allow easy
 * access of the iteration order: for instance, you can get the first element
 * in iteration order with {@link #first()} without having to create an
 * iterator; however, this class partially violates the {@link java.util.SortedSet}
 * contract because all subset methods throw an exception and {@link
 * #comparator()} returns always <code>null</code>.
 *
 * <P>The iterators provided by this class are type-specific {@linkplain
 * java.util.ListIterator list iterators}.  However, creation of an iterator
 * using a starting point is going to be very expensive, as the chosen starting
 * point must be linearly searched for, unless it is {@link #last()}, in which
 * case the iterator is created in constant time.
 *
 * <P>Note that deletions in a linked table require scanning the list until the
 * element to be removed is found. The only exceptions are the first element, the last element,
 * and deletions performed using an iterator.
 *
 * @see Hash
 * @see HashCommon
 */

public class ShortLinkedOpenHashSet extends AbstractShortSortedSet implements java.io.Serializable, Cloneable, Hash {
 /** The array of keys. */
 protected transient short key[];

 /** The array of occupancy states. */
 protected transient byte state[];

 /** The acceptable load factor. */
 protected final float f;

 /** Index into the prime list, giving the current table size. */
 protected transient int p;

 /** Threshold after which we rehash. It must be the table size times {@link #f}. */
 protected transient int maxFill;

 /** Number of free entries in the table (may be less than the table size - {@link #count} because of deleted entries). */
 protected transient int free;

 /** Number of entries in the set. */
 protected int count;

 /** The growth factor of the table. The next table size will be <code>{@link Hash#PRIMES}[{@link #p}+growthFactor</code>. */
 protected transient int growthFactor = Hash.DEFAULT_GROWTH_FACTOR;


 /** The index of the first entry in iteration order. It is valid iff {@link #count} is nonzero; otherwise, it contains -1. */
 protected transient int first = -1;
 /** The index of the last entry in iteration order. It is valid iff {@link #count} is nonzero; otherwise, it contains -1. */
 protected transient int last = -1;
 /** For each entry, the next and the previous entry in iteration order
	exclusive-or'd together. It is valid only on {@link Hash#OCCUPIED}
	entries. The first and the last entry contain the actual successor and
	predecessor, respectively, exclusived-or'd with -1. */
 protected transient int link[];







    public static final long serialVersionUID = -7046029254386353129L;

 private static final boolean ASSERTS = false;
 /** Creates a new hash set.
	 *
	 * The actual table size is the least available prime greater than <code>n</code>/<code>f</code>.
	 *
	 * @param n the expected number of elements in the hash set. 
	 * @param f the load factor.
	 * @see Hash#PRIMES
	 */
 @SuppressWarnings("unchecked")
 public ShortLinkedOpenHashSet( final int n, final float f ) {

  if ( f <= 0 || f > 1 ) throw new IllegalArgumentException( "Load factor must be greater than 0 and smaller than or equal to 1" );
  if ( n < 0 ) throw new IllegalArgumentException( "Hash table size must be nonnegative" );

  int l = Arrays.binarySearch( PRIMES, (int)( n / f ) + 1 );
  if ( l < 0 ) l = -l - 1;

  free = PRIMES[ p = l ];
  this.f = f;
  this.maxFill = (int)( free * f );
  key = new short[ free ];
  state = new byte[ free ];

  link = new int[ free ];

 }
 /** Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor.
	 *
	 * @param n the expected number of elements in the hash set. 
	 */

 public ShortLinkedOpenHashSet( final int n ) {
  this( n, DEFAULT_LOAD_FACTOR );
 }
 /** Creates a new hash set with {@link Hash#DEFAULT_INITIAL_SIZE} elements
	 * and {@link Hash#DEFAULT_LOAD_FACTOR} as load factor.
	 */

 public ShortLinkedOpenHashSet() {
  this( DEFAULT_INITIAL_SIZE, DEFAULT_LOAD_FACTOR );
 }
 /** Creates a new hash set copying a given collection.
	 *
	 * @param c a {@link Collection} to be copied into the new hash set. 
	 * @param f the load factor.
	 */

 public ShortLinkedOpenHashSet( final Collection<? extends Short> c, final float f ) {
  this( c.size(), f );
  addAll( c );
 }
 /** Creates a new hash set  with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor 
	 * copying a given collection.
	 *
	 * @param c a {@link Collection} to be copied into the new hash set. 
	 */

 public ShortLinkedOpenHashSet( final Collection<? extends Short> c ) {
  this( c, DEFAULT_LOAD_FACTOR );
 }
 /** Creates a new hash set copying a given type-specific collection.
	 *
	 * @param c a type-specific collection to be copied into the new hash set. 
	 * @param f the load factor.
	 */

 public ShortLinkedOpenHashSet( final ShortCollection c, final float f ) {
  this( c.size(), f );
  addAll( c );
 }
 /** Creates a new hash set  with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor 
	 * copying a given type-specific collection.
	 *
	 * @param c a type-specific collection to be copied into the new hash set. 
	 */

 public ShortLinkedOpenHashSet( final ShortCollection c ) {
  this( c, DEFAULT_LOAD_FACTOR );
 }
 /** Creates a new hash set using elements provided by a type-specific iterator.
	 *
	 * @param i a type-specific iterator whose elements will fill the set.
	 * @param f the load factor.
	 */

 public ShortLinkedOpenHashSet( final ShortIterator i, final float f ) {
  this( DEFAULT_INITIAL_SIZE, f );
  while( i.hasNext() ) add( i.nextShort() );
 }
 /** Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor using elements provided by a type-specific iterator.
	 *
	 * @param i a type-specific iterator whose elements will fill the set.
	 */

 public ShortLinkedOpenHashSet( final ShortIterator i ) {
  this( i, DEFAULT_LOAD_FACTOR );
 }
 /** Creates a new hash set using elements provided by an iterator.
	 *
	 * @param i an iterator whose elements will fill the set.
	 * @param f the load factor.
	 */

 public ShortLinkedOpenHashSet( final Iterator<?> i, final float f ) {
  this( ShortIterators.asShortIterator( i ), f );
 }
 /** Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor using elements provided by an iterator.
	 *
	 * @param i an iterator whose elements will fill the set.
	 */

 public ShortLinkedOpenHashSet( final Iterator<?> i ) {
  this( ShortIterators.asShortIterator( i ) );
 }
 /** Creates a new hash set and fills it with the elements of a given array.
	 *
	 * @param a an array whose elements will be used to fill the set.
	 * @param offset the first element to use.
	 * @param length the number of elements to use.
	 * @param f the load factor.
	 */

 public ShortLinkedOpenHashSet( final short[] a, final int offset, final int length, final float f ) {
  this( length < 0 ? 0 : length, f );
  ShortArrays.ensureOffsetLength( a, offset, length );
  for( int i = 0; i < length; i++ ) add( a[ offset + i ] );
 }
 /** Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor and fills it with the elements of a given array.
	 *
	 * @param a an array whose elements will be used to fill the set.
	 * @param offset the first element to use.
	 * @param length the number of elements to use.
	 */

 public ShortLinkedOpenHashSet( final short[] a, final int offset, final int length ) {
  this( a, offset, length, DEFAULT_LOAD_FACTOR );
 }
 /** Creates a new hash set copying the elements of an array.
	 *
	 * @param a an array to be copied into the new hash set. 
	 * @param f the load factor.
	 */

 public ShortLinkedOpenHashSet( final short[] a, final float f ) {
  this( a, 0, a.length, f );
 }
 /** Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor 
	 * copying the elements of an array.
	 *
	 * @param a an array to be copied into the new hash set. 
	 */

 public ShortLinkedOpenHashSet( final short[] a ) {
  this( a, DEFAULT_LOAD_FACTOR );
 }
 /** Sets the growth factor. Subsequent enlargements will increase the table
	 * size roughly by a multiplicative factor of 2<sup>p/16</sup>.
	 * 
	 * @param growthFactor the new growth factor; it must be positive.
	 */

 public void growthFactor( int growthFactor ) {
  if ( growthFactor <= 0 ) throw new IllegalArgumentException( "Illegal growth factor " + growthFactor );
  this.growthFactor = growthFactor;
 }


 /** Gets the growth factor.
	 *
	 * @return the growth factor of this set.
	 * @see #growthFactor(int)
	 */

 public int growthFactor() {
  return growthFactor;
 }


 /*
	 * The following methods implements some basic building blocks used by
	 * all accessors. They are (and should be maintained) identical to those used in HashMap.drv.
	 */

 /** Searches for a key, keeping track of a possible insertion point.
	 *
	 * @param k the key.
	 * @return the index of the correct insertion point, if the key is not found; otherwise,
	 * <var>-i</var>-1, where <var>i</var> is the index of the entry containing the key.
	 */

 protected final int findInsertionPoint( final short k ) {
  final short key[] = this.key;
  final byte state[] = this.state;
  final int n = key.length;

  // First of all, we make the key into a positive integer.



  final int k2i = (k) & 0x7FFFFFFF;

  // The primary hash, a.k.a. starting point.
  int h1 = k2i % n;

  if ( state[ h1 ] == OCCUPIED && ! ( (k) == (key[ h1 ]) ) ) {
   // The secondary hash.
   final int h2 = ( k2i % ( n - 2 ) ) + 1;
   do {
    h1 += h2;
    if ( h1 >= n || h1 < 0 ) h1 -= n;
   } while( state[ h1 ] == OCCUPIED && ! ( (k) == (key[ h1 ]) ) ); // There's always a FREE entry.
  }

  if (state[ h1 ] == FREE) return h1;
  if (state[ h1 ] == OCCUPIED) return -h1-1; // Necessarily, KEY_EQUALS_HASH( k, h, key[ h1 ] ).

  /* Tables without deletions will never use code beyond this point. */

  final int i = h1; // Remember first available bucket for later.

  /** See the comments in the documentation of the interface Hash. */
  if ( ASSERTS ) assert state[ h1 ] == REMOVED;
  if ( ! ( (k) == (key[ h1 ]) ) ) {
   // The secondary hash.
   final int h2 = ( k2i % ( n - 2 ) ) + 1;
   do {
    h1 += h2;
    if ( h1 >= n || h1 < 0 ) h1 -= n;
   } while( state[ h1 ] != FREE && ! ( (k) == (key[ h1 ]) ) );
  }

  return state[ h1 ] == OCCUPIED ? -h1-1 : i; // In the first case, necessarily, KEY_EQUALS_HASH( k, h, key[ h1 ] ).
 }


 /** Searches for a key.
	 *
	 * @param k the key.
	 * @return the index of the entry containing the key, or -1 if the key wasn't found.
	 */

 protected final int findKey( final short k ) {
  final short key[] = this.key;
  final byte state[] = this.state;
  final int n = key.length;

  // First of all, we make the key into a positive integer.



  final int k2i = (k) & 0x7FFFFFFF;

  // The primary hash, a.k.a. starting point.
  int h1 = k2i % n;

  /** See the comments in the documentation of the interface Hash. */
  if ( state[ h1 ] != FREE && ! ( (k) == (key[ h1 ]) ) ) {
   // The secondary hash.
   final int h2 = ( k2i % ( n - 2 ) ) + 1;
   do {
    h1 += h2;
    if ( h1 >= n || h1 < 0 ) h1 -= n;
   } while( state[ h1 ] != FREE && ! ( (k) == (key[ h1 ]) ) ); // There's always a FREE entry.
  }

  return state[ h1 ] == OCCUPIED ? h1 : -1; // In the first case, necessarily, KEY_EQUALS_HASH( k, h, key[ h1 ] ).
 }

 public boolean add( final short k ) {
  final int i = findInsertionPoint( k );
  if ( i < 0 ) return false;

  if ( state[ i ] == FREE ) free--;
  state[ i ] = OCCUPIED;
  key[ i ] = k;


  if ( count == 0 ) {
   first = last = i;
   link[ i ] = 0;
  }
  else {
   link[ last ] ^= i ^ -1;
   link[ i ] = last ^ -1;
   last = i;
  }


  if ( ++count >= maxFill ) {
   int newP = Math.min( p + growthFactor, PRIMES.length - 1 );
   // Just to be sure that size changes when p is very small.
   while( PRIMES[ newP ] == PRIMES[ p ] ) newP++;
   rehash( newP ); // Table too filled, let's rehash
  }
  if ( free == 0 ) rehash( p );
  if ( ASSERTS ) checkTable();
  return true;
 }

 @SuppressWarnings("unchecked")
 public boolean remove( final short k ) {
  final int i = findKey( k );
  if ( i < 0 ) return false;
  state[ i ] = REMOVED;
  count--;






  fixPointers( i );


  if ( ASSERTS ) checkTable();
  return true;
 }

 @SuppressWarnings("unchecked")
 public boolean contains( final short k ) {
  return findKey( k ) >= 0;
 }


 /* Removes all elements from this set.
	 *
	 * <P>To increase object reuse, this method does not change the table size.
	 * If you want to reduce the table size, you must use {@link #trim()}.
	 *
	 */

 public void clear() {
  if ( free == state.length ) return;

  free = state.length;
  count = 0;

  ByteArrays.fill( state, FREE );






  first = last = -1;

 }





 /** Modifies the {@link #link} vector so that the given entry is removed.
	 *
	 * <P>If the given entry is the first or the last one, this method will complete
	 * in constant time; otherwise, it will have to search for the given entry.
	 *
	 * @param i the index of an entry. 
	 */
 private void fixPointers( int i ) {
  if ( count == 0 ) {
   first = last = -1;
   return;
  }

  if ( first == i ) {
   first = link[ i ] ^ -1;
   link[ first ] ^= i ^ -1;
   return;
  }

  if ( last == i ) {
   last = link[ i ] ^ -1;
   link[ last ] ^= i ^ -1;
   return;
  }

  int j = first, prev = -1, next;
  while( ( next = link[ j ] ^ prev ) != i ) {
   prev = j;
   j = next;
  }
  link[ j ] ^= link[ i ] ^ i ^ j;
  link[ link[ i ] ^ j ] ^= i ^ j;
 }


 /** Returns the first element of this set in iteration order.
	 *
	 * @return the first element in iteration order.
	 */
 public short firstShort() {
  if ( count == 0 ) throw new NoSuchElementException();
  return key[ first ];
 }


 /** Returns the last element of this set in iteration order.
	 *
	 * @return the last element in iteration order.
	 */
 public short lastShort() {
  if ( count == 0 ) throw new NoSuchElementException();
  return key[ last ];
 }


 public ShortSortedSet tailSet( short from ) { throw new UnsupportedOperationException(); }
 public ShortSortedSet headSet( short to ) { throw new UnsupportedOperationException(); }
 public ShortSortedSet subSet( short from, short to ) { throw new UnsupportedOperationException(); }

 public ShortComparator comparator() { return null; }



 /** A list iterator over a linked set.
	 *
	 * <P>This class provides a list iterator over a linked hash set. The empty constructor runs in 
	 * constant time. The one-argoument constructor needs to search for the given element, but it is 
	 * optimized for the case of {@link java.util.SortedSet#last()}, in which case runs in constant time, too.
	 */
 private class SetIterator extends AbstractShortListIterator {
  /** The entry that will be returned by the next call to {@link java.util.ListIterator#previous()} (or <code>null</code> if no previous entry exists). */
  int prev = -1;
  /** The entry that will be returned by the next call to {@link java.util.ListIterator#next()} (or <code>null</code> if no next entry exists). */
  int next = -1;
  /** The last entry that was returned (or -1 if we did not iterate or used {@link #remove()}). */
  int curr = -1;
  /** The current index (in the sense of a {@link java.util.ListIterator}). Note that this value is not meaningful when this {@link SetIterator} has been created using the nonempty constructor.*/
  int index = 0;

  SetIterator() {
   next = first;
  }

  SetIterator( short from ) {
   if ( ( (key[ last ]) == (from) ) ) {
    prev = last;
    index = count;
   }
   else {
    if ( ! contains( from ) ) throw new IllegalArgumentException( "The key " + from + " does not belong to this set." );
    next = first;
    short k;
    do k = nextShort(); while( ! ( (k) == (from) ) );
    curr = -1;
   }
  }

  public boolean hasNext() { return next != -1; }
  public boolean hasPrevious() { return prev != -1; }

  public short nextShort() {
   if ( ! hasNext() ) throw new NoSuchElementException();

   curr = next;
   next = link[ curr ] ^ prev;
   prev = curr;

   index++;

   return key[ curr ];
  }

  public short previousShort() {
   if ( ! hasPrevious() ) throw new NoSuchElementException();

   curr = prev;
   prev = link[ curr ] ^ next;
   next = curr;

   index--;

   return key[ curr ];
  }

  public int nextIndex() {
   return index;
  }

  public int previousIndex() {
   return index - 1;
  }


  @SuppressWarnings("unchecked")
  public void remove() {
   if ( curr == -1 ) throw new IllegalStateException();
   state[ curr ] = REMOVED;



   if ( curr == prev ) {
    /* If the last operation was a next(), we are removing an entry that preceeds
				   the current index, and thus we must decrement it. */
    index--;
    prev = link[ curr ] ^ next;
   }
   else next = link[ curr ] ^ prev; // curr == next

   count--;
   /* Now we manually fix the pointers. Because of our knowledge of next
			   and prev, this is going to be faster than calling fixPointers(). */
   if ( prev == -1 ) first = next;
   else link[ prev ] ^= curr ^ next;
   if ( next == -1 ) last = prev;
   else link[ next ] ^= curr ^ prev;
   curr = -1;
  }
 }


 /** Returns a type-specific list iterator on the elements in this set, starting from a given element of the set.
	 *
	 * <P>This method provides an iterator positioned immediately after the
	 * given element. That is, the next call to <code>previous()</code> will
	 * return <code>from</code>, whereas the next call to <code>next()</code>
	 * will return the element immediately after <code>from</code>. This
	 * allows to call <code>iterator(last())</code> and obtain an iterator starting
	 * from the end of the iteration order.
	 *
	 * <P>Because of the way linking is implemented, generating an iterator using this method
	 * requires constant time only if the argument is <code>last()</code>. In all other cases,
	 * a linear search for the given element will be necessary.
	 *
	 * <P>Note that this method returns a bidirectional iterator, which, however, can be safely cast to 
	 * a type-specific list iterator.
	 *
	 * @param from an element to start from.
	 * @return a type-specific list iterator starting at the given element.
	 * @throws IllegalArgumentException if <code>from</code> does not belong to the set.
	 */
 public ShortBidirectionalIterator iterator( short from ) {
  return new SetIterator( from );
 }

 public ShortBidirectionalIterator iterator() {
  return new SetIterator();
 }
 /** Rehashes this set without changing the table size.
	 *
	 * <P>This method should be called when the set underwent numerous
	 * deletions and insertions.  In this case, free entries become rare, and
	 * unsuccessful searches require probing <em>all</em> entries.  For
	 * reasonable load factors this method is linear in the number of entries.
	 * You will need as much additional free memory as that occupied by the
	 * table.
	 *
	 * <P>If you need to reduce the table siza to fit exactly
	 * this set, you must use {@link #trim()}.
	 *
	 * @return true if there was enough memory to rehash the set, false otherwise.
	 * @see #trim()
	 */

 public boolean rehash() {
  try {
   rehash( p );
  }
  catch( OutOfMemoryError cantDoIt ) { return false; }
  return true;
 }


 /** Rehashes this set, making the table as small as possible.
	 * 
	 * <P>This method rehashes the table to the smallest size satisfying the
	 * load factor. It can be used when the set will not be changed anymore, so
	 * to optimize access speed (by collecting deleted entries) and size.
	 *
	 * <P>If the table size is already the minimum possible, this method
	 * does nothing. If you want to guarantee rehashing, use {@link #rehash()}.
	 *
	 * @return true if there was enough memory to trim the set.
	 * @see #trim(int)
	 * @see #rehash()
	 */

 public boolean trim() {
  int l = Arrays.binarySearch( PRIMES, (int)( count / f ) + 1 );
  if ( l < 0 ) l = -l - 1;
  if ( l >= p ) return true;
  try {
   rehash( l );
  }
  catch(OutOfMemoryError cantDoIt) { return false; }
  return true;
 }

 /** Rehashes this set if the table is too large.
	 * 
	 * <P>Let <var>N</var> be the smallest table size that can hold
	 * <code>max(n,{@link #size()})</code> entries, still satisfying the load factor. If the current
	 * table size is smaller than or equal to <var>N</var>, this method does
	 * nothing. Otherwise, it rehashes this set in a table of size
	 * <var>N</var>.
	 *
	 * <P>This method is useful when reusing sets.  {@linkplain #clear() Clearing a
	 * set} leaves the table size untouched. If you are reusing a set
	 * many times, you can call this method with a typical
	 * size to avoid keeping around a very large table just
	 * because of a few large transient sets.
	 *
	 * @param n the threshold for the trimming.
	 * @return true if there was enough memory to trim the set.
	 * @see #trim()
	 * @see #rehash()
	 */

 public boolean trim( final int n ) {
  int l = Arrays.binarySearch( PRIMES, (int)( Math.min( Integer.MAX_VALUE - 1, Math.max( n, count ) / f ) ) + 1 );
  if ( l < 0 ) l = -l - 1;
  if ( p <= l ) return true;
  try {
   rehash( l );
  }
  catch( OutOfMemoryError cantDoIt ) { return false; }
  return true;
 }

 /** Resizes the set.
	 *
	 * <P>This method implements the basic rehashing strategy, and may be
	 * overriden by subclasses implementing different rehashing strategies (e.g.,
	 * disk-based rehashing). However, you should not override this method
	 * unless you understand the internal workings of this class.
	 *
	 * @param newP the new size as an index in {@link Hash#PRIMES}.
	 */

 @SuppressWarnings("unchecked")
 protected void rehash( final int newP ) {

  int i = first, j = count, prev = -1, newPrev = -1, t, k2i, h1, h2;





  //System.err.println("Rehashing to size " +  PRIMES[newP] + "...");

  short k;

  final int newN = PRIMES[ newP ];
  final short key[] = this.key, newKey[] = new short[ newN ];
  final byte newState[] = new byte[ newN ];

  final int link[] = this.link, newLink[] = new int[ newN ];
  first = -1;


  while( j-- != 0 ) {





   k = key[ i ];
   k2i = (k) & 0x7FFFFFFF;

   h1 = k2i % newN;

   if ( newState[ h1 ] != FREE ) {
    h2 = ( k2i % ( newN - 2 ) ) + 1;
    do {
     h1 += h2;
     if ( h1 >= newN || h1 < 0 ) h1 -= newN;
    } while( newState[ h1 ] != FREE );
   }

   newState[ h1 ] = OCCUPIED;
   newKey[ h1 ] = k;


   t = i;
   i = link[ i ] ^ prev;
   prev = t;

   if ( first != -1 ) {
    newLink[ newPrev ] ^= h1;
    newLink[ h1 ] = newPrev;
    newPrev = h1;
   }
   else {
    newPrev = first = h1;
    newLink[ h1 ] = -1;
   }



  }

  p = newP;
  free = newN - count;
  maxFill = (int)( newN * f );
  this.key = newKey;
  this.state = newState;

  this.link = newLink;
  this.last = newPrev;
  if ( newPrev != -1 ) newLink[ newPrev ] ^= -1;

 }

 public int size() {
  return count;
 }

 public boolean isEmpty() {
  return count == 0;
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
  ShortLinkedOpenHashSet c;
  try {
   c = (ShortLinkedOpenHashSet )super.clone();
  }
  catch(CloneNotSupportedException cantHappen) {
   throw new InternalError();
  }
  c.key = key.clone();
  c.state = state.clone();

  c.link = link.clone();




  return c;
 }

 /** Returns a hash code for this set.
	 *
	 * This method overrides the generic method provided by the superclass. 
	 * Since <code>equals()</code> is not overriden, it is important
	 * that the value returned by this method is the same value as
	 * the one returned by the overriden method.
	 *
	 * @return a hash code for this set.
	 */


 public int hashCode() {
  int h = 0, i = 0, j = count;
  while( j-- != 0 ) {
   while( state[ i ] != OCCUPIED ) i++;



    h += (key[ i ]);
   i++;
  }
  return h;
 }


 private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
  final ShortIterator i = iterator();
  int j = count;
  s.defaultWriteObject();
  while( j-- != 0 ) s.writeShort( i.nextShort() );
 }


 @SuppressWarnings("unchecked")
 private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
  s.defaultReadObject();
  // We restore the default growth factor.
  growthFactor = Hash.DEFAULT_GROWTH_FACTOR;
  // Note that we DO NOT USE the stored p. See CHANGES.
  p = Arrays.binarySearch( PRIMES, (int)( count / f ) + 1 );
  if ( p < 0 ) p = -p - 1;

  final int n = PRIMES[ p ];
  maxFill = (int)( n * f );
  free = n - count;;

  final short key[] = this.key = new short[ n ];
  final byte state[] = this.state = new byte[ n ];

  final int link[] = this.link = new int[ n ];
  int prev = -1;
  first = last = -1;


  int i, k2i, h1, h2;
  short k;

  i = count;
  while( i-- != 0 ) {

   k = s.readShort();
   k2i = (k) & 0x7FFFFFFF;

   h1 = k2i % n;

   if ( state[ h1 ] != FREE ) {
    h2 = ( k2i % ( n - 2 ) ) + 1;
    do {
     h1 += h2;
     if ( h1 >= n || h1 < 0 ) h1 -= n;
    } while( state[ h1 ] != FREE );
   }

   state[ h1 ] = OCCUPIED;
   key[ h1 ] = k;


   if ( first != -1 ) {
    link[ prev ] ^= h1;
    link[ h1 ] = prev;
    prev = h1;
   }
   else {
    prev = first = h1;
    link[ h1 ] = -1;
   }

  }


  last = prev;
  if ( prev != -1 ) link[ prev ] ^= -1;


  if ( ASSERTS ) checkTable();
 }
 private void checkTable() {}
}
