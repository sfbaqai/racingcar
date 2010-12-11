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

package it.unimi.dsi.fastutil.doubles;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;




import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.bytes.ByteArrays;

import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;


import it.unimi.dsi.fastutil.objects.ObjectIterator;



import it.unimi.dsi.fastutil.objects.ObjectArrays;





import java.util.Comparator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
/**  A type-specific linked hash map with with a fast, small-footprint implementation.
 *
 * <P>Instances of this class use a hash table to represent a map. The table is
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
 * <P>This class implements the interface of a sorted map, so to allow easy
 * access of the iteration order: for instance, you can get the first key
 * in iteration order with {@link #firstKey()} without having to create an
 * iterator; however, this class partially violates the {@link java.util.SortedMap}
 * contract because all submap methods throw an exception and {@link
 * #comparator()} returns always <code>null</code>.
 *
 * <P>The iterators provided by the views of this class using are type-specific
 * {@linkplain java.util.ListIterator list iterators}. However, creation of an
 * iterator using a starting point is going to be very expensive, as the chosen
 * starting point must be linearly searched for, unless it is {@link #lastKey()},
 * in which case the iterator is created in constant time.
 *
 * <P>Note that deletions in a linked table require scanning the list until the
 * element to be removed is found. The only exceptions are the first element, the last element,
 * and deletions performed using an iterator.
 *
 * @see Hash
 * @see HashCommon
 */

public class Double2ObjectLinkedOpenHashMap <V> extends AbstractDouble2ObjectSortedMap <V> implements java.io.Serializable, Cloneable, Hash {
 /** The array of keys. */
 protected transient double key[];

 /** The array of values. */
 protected transient V value[];

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

 /** Number of entries in the map. */
 protected int count;


 /** Cached set of entries. */
 protected transient volatile FastSortedEntrySet <V> entries;

 /** Cached set of keys. */
 protected transient volatile DoubleSortedSet keys;
 /** Cached collection of values. */
 protected transient volatile ObjectCollection <V> values;

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
 /** Creates a new hash map.
	 *
	 * The actual table size is the least available prime greater than <code>n</code>/<code>f</code>.
	 *
	 * @param n the expected number of elements in the hash map.
	 * @param f the load factor.
	 * @see Hash#PRIMES
	 */

 @SuppressWarnings("unchecked")
 public Double2ObjectLinkedOpenHashMap( final int n, final float f ) {

  if ( f <= 0 || f > 1 ) throw new IllegalArgumentException( "Load factor must be greater than 0 and smaller than or equal to 1" );
  if ( n < 0 ) throw new IllegalArgumentException( "Hash table size must be nonnegative" );

  int l = Arrays.binarySearch( PRIMES, (int)( n / f ) + 1 );
  if ( l < 0 ) l = -l - 1;

  free = PRIMES[ p = l ];
  this.f = f;
  this.maxFill = (int)( free * f );
  key = new double[ free ];
  value = (V[]) new Object[ free ];
  state = new byte[ free ];

  link = new int[ free ];

 }
 /** Creates a new hash map with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor.
	 *
	 * @param n the expected number of elements in the hash map.
	 */

 public Double2ObjectLinkedOpenHashMap( final int n ) {
  this( n, DEFAULT_LOAD_FACTOR );
 }
 /** Creates a new hash map with {@link Hash#DEFAULT_INITIAL_SIZE} entries
	 * and {@link Hash#DEFAULT_LOAD_FACTOR} as load factor.
	 */

 public Double2ObjectLinkedOpenHashMap() {
  this( DEFAULT_INITIAL_SIZE, DEFAULT_LOAD_FACTOR );
 }
 /** Creates a new hash map copying a given one.
	 *
	 * @param m a {@link Map} to be copied into the new hash map. 
	 * @param f the load factor.
	 */

 public Double2ObjectLinkedOpenHashMap( final Map<? extends Double, ? extends V> m, final float f ) {
  this( m.size(), f );
  putAll( m );
 }
 /** Creates a new hash map with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor copying a given one.
	 *
	 * @param m a {@link Map} to be copied into the new hash map. 
	 */

 public Double2ObjectLinkedOpenHashMap( final Map<? extends Double, ? extends V> m ) {
  this( m, DEFAULT_LOAD_FACTOR );
 }
 /** Creates a new hash map copying a given type-specific one.
	 *
	 * @param m a type-specific map to be copied into the new hash map. 
	 * @param f the load factor.
	 */

 public Double2ObjectLinkedOpenHashMap( final Double2ObjectMap <V> m, final float f ) {
  this( m.size(), f );
  putAll( m );
 }
 /** Creates a new hash map with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor copying a given type-specific one.
	 *
	 * @param m a type-specific map to be copied into the new hash map. 
	 */

 public Double2ObjectLinkedOpenHashMap( final Double2ObjectMap <V> m ) {
  this( m, DEFAULT_LOAD_FACTOR );
 }
 /** Creates a new hash map using the elements of two parallel arrays.
	 *
	 * @param k the array of keys of the new hash map.
	 * @param v the array of corresponding values in the new hash map.
	 * @param f the load factor.
	 * @throws IllegalArgumentException if <code>k</code> and <code>v</code> have different lengths.
	 */

 public Double2ObjectLinkedOpenHashMap( final double[] k, final V v[], final float f ) {
  this( k.length, f );
  if ( k.length != v.length ) throw new IllegalArgumentException( "The key array and the value array have different lengths (" + k.length + " and " + v.length + ")" );
  for( int i = 0; i < k.length; i++ ) this.put( k[ i ], v[ i ] );
 }
 /** Creates a new hash map with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor using the elements of two parallel arrays.
	 *
	 * @param k the array of keys of the new hash map.
	 * @param v the array of corresponding values in the new hash map.
	 * @throws IllegalArgumentException if <code>k</code> and <code>v</code> have different lengths.
	 */

 public Double2ObjectLinkedOpenHashMap( final double[] k, final V v[] ) {
  this( k, v, DEFAULT_LOAD_FACTOR );
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
	 * all accessors.  They are (and should be maintained) identical to those used in HashSet.drv.
	 */

 /** Searches for a key, keeping track of a possible insertion point.
	 *
	 * @param k the key.
	 * @return the index of the correct insertion point, if the key is not found; otherwise,
	 * <var>-i</var>-1, where <var>i</var> is the index of the entry containing the key.
	 */

 protected final int findInsertionPoint( final double k ) {
  final double key[] = this.key;
  final byte state[] = this.state;
  final int n = key.length;

  // First of all, we make the key into a positive integer.



  final int k2i = it.unimi.dsi.fastutil.HashCommon.double2int(k) & 0x7FFFFFFF;

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

 protected final int findKey( final double k ) {
  final double key[] = this.key;
  final byte state[] = this.state;
  final int n = key.length;

  // First of all, we make the key into a positive integer.



  final int k2i = it.unimi.dsi.fastutil.HashCommon.double2int(k) & 0x7FFFFFFF;

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



 public V put(final double k, final V v) {
  final int i = findInsertionPoint( k );

  if (i < 0) {
   final V oldValue = value[-i-1];
   value[-i-1] = v;
   return oldValue;
  }

  if ( state[i] == FREE ) free--;
  state[i] = OCCUPIED;
  key[i] = k;
  value[i] = v;


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
  return defRetValue;
 }





 public V put(final Double ok, final V ov) {
  final V v = (ov);
  final double k = ((ok).doubleValue());

  final int i = findInsertionPoint( k );

  if (i < 0) {
   final V oldValue = value[-i-1];
   value[-i-1] = v;
   return (oldValue);
  }

  if ( state[i] == FREE ) free--;
  state[i] = OCCUPIED;
  key[i] = k;
  value[i] = v;


  if ( count == 0 ) {
   first = last = i;
   link[ i ] = 0;
  }
  else {
   link[ last ] ^= i ^ -1;
   link[ i ] = last ^ -1;
   last = i;
  }


  if ( ++count >= maxFill ) rehash( Math.min(p+16, PRIMES.length-1) ); // Table too filled, let's rehash
  if ( free == 0 ) rehash( p );
  if ( ASSERTS ) checkTable();
  return (this.defRetValue);
 }







 public boolean containsValue( final Object v ) {
  final V value[] = this.value;
  final byte state[] = this.state;

  int i = 0, j = count;

  while(j-- != 0) {
   while(state[ i ] != OCCUPIED ) i++;
   if ( ( (value[ i ]) == null ? (v) == null : (value[ i ]).equals(v) ) ) return true;
   i++;
  }
  return false;
 }

 /* Removes all elements from this map.
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

  // We null all object entries so that the garbage collector can do its work.




  ObjectArrays.fill( value, null );



  first = last = -1;

 }

 /** The entry class for a hash map does not record key and value, but
	 * rather the position in the hash table of the corresponding entry. This
	 * is necessary so that calls to {@link java.util.Map.Entry#setValue(Object)} are reflected in
	 * the map */

 private final class MapEntry implements Double2ObjectMap.Entry <V>, Map.Entry<Double, V> {
  private int index;

  MapEntry( final int index ) {
   this.index = index;
  }

  public Double getKey() {
   return (Double.valueOf(key[ index ]));
  }


  public double getDoubleKey() {
      return key[ index ];
  }


  public V getValue() {
   return (value[ index ]);
  }







  public V setValue( final V v ) {
   final V oldValue = value[ index ];
   value[ index ] = v;
   return oldValue;
  }
  @SuppressWarnings("unchecked")
  public boolean equals( final Object o ) {
   if (!(o instanceof Map.Entry)) return false;
   Map.Entry<Double, V> e = (Map.Entry<Double, V>)o;

   return ( (key[ index ]) == (((e.getKey()).doubleValue())) ) && ( (value[ index ]) == null ? ((e.getValue())) == null : (value[ index ]).equals((e.getValue())) );
  }

  public int hashCode() {
   return it.unimi.dsi.fastutil.HashCommon.double2int(key[ index ]) ^ ( (value[ index ]) == null ? 0 : (value[ index ]).hashCode() );
  }


  public String toString() {
   return key[ index ] + "->" + value[ index ];
  }
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


 /** Returns the first key of this map in iteration order.
	 *
	 * @return the first key in iteration order.
	 */
 public double firstDoubleKey() {
  if ( count == 0 ) throw new NoSuchElementException();
  return key[ first ];
 }


 /** Returns the last key of this map in iteration order.
	 *
	 * @return the last key in iteration order.
	 */
 public double lastDoubleKey() {
  if ( count == 0 ) throw new NoSuchElementException();
  return key[ last ];
 }

 public DoubleComparator comparator() { return null; }

 public Double2ObjectSortedMap <V> tailMap( double from ) { throw new UnsupportedOperationException(); }
 public Double2ObjectSortedMap <V> headMap( double to ) { throw new UnsupportedOperationException(); }
 public Double2ObjectSortedMap <V> subMap( double from, double to ) { throw new UnsupportedOperationException(); }



 /** A list iterator over a linked map.
	 *
	 * <P>This class provides a list iterator over a linked hash map. The empty constructor runs in 
	 * constant time. The one-argoument constructor needs to search for the given key, but it is 
	 * optimized for the case of {@link java.util.SortedMap#lastKey()}, in which case runs in constant time, too.
	 */

 private class MapIterator {
  /** The entry that will be returned by the next call to {@link java.util.ListIterator#previous()} (or <code>null</code> if no previous entry exists). */
  int prev = -1;
  /** The entry that will be returned by the next call to {@link java.util.ListIterator#next()} (or <code>null</code> if no next entry exists). */
  int next = -1;
  /** The last entry that was returned (or -1 if we did not iterate or used {@link java.util.Iterator#remove()}). */
  int curr = -1;
  /** The current index (in the sense of a {@link java.util.ListIterator}). Note that this value is not meaningful when this iterator has been created using the nonempty constructor.*/
  int index = 0;

  MapIterator() {
   next = first;
  }

  MapIterator( final double from ) {
   if ( ( (key[ last ]) == (from) ) ) {
    prev = last;
    index = count;
   }
   else {
    if ( ! Double2ObjectLinkedOpenHashMap.this.containsKey( from ) ) throw new IllegalArgumentException( "The key " + from + " does not belong to this set." );
    next = first;
    int e;
    do e = nextEntry(); while( ! ( (key[ e ]) == (from) ) );
    curr = -1;
   }
  }

  public boolean hasNext() { return next != -1; }
  public boolean hasPrevious() { return prev != -1; }

  public int nextIndex() {
   return index;
  }

  public int previousIndex() {
   return index - 1;
  }

  public int nextEntry() {
   if ( ! hasNext() ) return size();

   curr = next;
   next = link[ curr ] ^ prev;
   prev = curr;

   index++;

   return curr;
  }

  public int previousEntry() {
   if ( ! hasPrevious() ) return -1;

   curr = prev;
   prev = link[ curr ] ^ next;
   next = curr;

   index--;

   return curr;
  }

  @SuppressWarnings("unchecked")
  public void remove() {
   if ( curr == -1 ) throw new IllegalStateException();
   state[ curr ] = REMOVED;






   value[ curr ] = null;


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

  public int skip( final int n ) {
   int i = n;
   while( i-- != 0 && hasNext() ) nextEntry();
   return n - i - 1;
  }

  public int back( final int n ) {
   int i = n;
   while( i-- != 0 && hasPrevious() ) previousEntry();
   return n - i - 1;
  }
 }

 private class EntryIterator extends MapIterator implements ObjectListIterator<Double2ObjectMap.Entry <V> > {
  public EntryIterator() {}

  public EntryIterator( double from ) {
   super( from );
  }

  public MapEntry next() {
   return new MapEntry( nextEntry() );
  }

  public MapEntry previous() {
   return new MapEntry( previousEntry() );
  }

  public void set( Double2ObjectMap.Entry <V> ok ) { throw new UnsupportedOperationException(); }
  public void add( Double2ObjectMap.Entry <V> ok ) { throw new UnsupportedOperationException(); }
 }

 private class FastEntryIterator extends MapIterator implements ObjectListIterator<Double2ObjectMap.Entry <V> > {
  final BasicEntry <V> entry = new BasicEntry <V> ( (0), (null) );

  public FastEntryIterator() {}

  public FastEntryIterator( double from ) {
   super( from );
  }

  public BasicEntry <V> next() {
   final int e = nextEntry();
   entry.key = key[ e ];
   entry.value = value[ e ];
   return entry;
  }

  public BasicEntry <V> previous() {
   final int e = previousEntry();
   entry.key = key[ e ];
   entry.value = value[ e ];
   return entry;
  }

  public void set( Double2ObjectMap.Entry <V> ok ) { throw new UnsupportedOperationException(); }
  public void add( Double2ObjectMap.Entry <V> ok ) { throw new UnsupportedOperationException(); }
 }
 @SuppressWarnings("unchecked")
 public boolean containsKey( double k ) {
  return findKey( k ) >= 0;
 }

 public int size() {
  return count;
 }

 public boolean isEmpty() {
  return count == 0;
 }

 @SuppressWarnings("unchecked")
 public V get(final double k) {
  final int i = findKey( k);

  return i < 0 ? defRetValue : value[i];
 }

 @SuppressWarnings("unchecked")
 public V remove(final double k) {
  final int i = findKey( k );
  if (i < 0) return defRetValue;

  state[i] = REMOVED;
  count--;





  final V v = value[i];
  value[i] = null;



  fixPointers( i );


  return v;



 }




 public V get(final Double ok) {
  final int i = findKey(((ok).doubleValue()));

  return i < 0 ? (this.defRetValue) : (V)(value[i]);
 }



 @SuppressWarnings("unchecked")
 public V remove( final Object ok ) {
  final int i = findKey( ((((Double)(ok)).doubleValue())) );
  if (i < 0) return (this.defRetValue);

  state[i] = REMOVED;
  count--;






  final V v = value[i];
  value[i] = null;



  fixPointers( i );


  if ( ASSERTS ) checkTable();


  return v;



 }
 private final class MapEntrySet extends AbstractObjectSortedSet<Double2ObjectMap.Entry <V> > implements FastSortedEntrySet <V> {

  public ObjectBidirectionalIterator<Double2ObjectMap.Entry <V> > iterator() {
   return new EntryIterator();
  }

  public Comparator<? super Double2ObjectMap.Entry <V> > comparator() { return null; }
  public ObjectSortedSet<Double2ObjectMap.Entry <V> > subSet( Double2ObjectMap.Entry <V> fromElement, Double2ObjectMap.Entry <V> toElement) { throw new UnsupportedOperationException(); }
  public ObjectSortedSet<Double2ObjectMap.Entry <V> > headSet( Double2ObjectMap.Entry <V> toElement ) { throw new UnsupportedOperationException(); }
  public ObjectSortedSet<Double2ObjectMap.Entry <V> > tailSet( Double2ObjectMap.Entry <V> fromElement ) { throw new UnsupportedOperationException(); }

  public Double2ObjectMap.Entry <V> first() {
   if ( count == 0 ) throw new NoSuchElementException();
   return new MapEntry( Double2ObjectLinkedOpenHashMap.this.first );
  }

  public Double2ObjectMap.Entry <V> last() {
   if ( count == 0 ) throw new NoSuchElementException();
   return new MapEntry( Double2ObjectLinkedOpenHashMap.this.last );
  }
  @SuppressWarnings("unchecked")
  public boolean contains( final Object o ) {
   if (!(o instanceof Map.Entry)) return false;
   final Map.Entry<Double, V> e = (Map.Entry<Double, V>)o;
   final int i = findKey( ((e.getKey()).doubleValue()) );
   return i >= 0 && ( (value[ i ]) == null ? ((e.getValue())) == null : (value[ i ]).equals((e.getValue())) );
  }

  @SuppressWarnings("unchecked")
  public boolean remove( final Object o ) {
   if (!(o instanceof Map.Entry)) return false;
   final Map.Entry<Double, V> e = (Map.Entry<Double, V>)o;
   final int i = findKey( ((e.getKey()).doubleValue()) );
   if ( i >= 0 ) Double2ObjectLinkedOpenHashMap.this.remove( e.getKey() );
   return i >= 0;
  }

  public int size() {
   return count;
  }

  public void clear() {
   Double2ObjectLinkedOpenHashMap.this.clear();
  }


  public ObjectBidirectionalIterator<Double2ObjectMap.Entry <V> > iterator( final Double2ObjectMap.Entry <V> from ) {
   return new EntryIterator( ((from.getKey()).doubleValue()) );
  }

  public ObjectBidirectionalIterator<Double2ObjectMap.Entry <V> > fastIterator() {
   return new FastEntryIterator();
  }

  public ObjectBidirectionalIterator<Double2ObjectMap.Entry <V> > fastIterator( final Double2ObjectMap.Entry <V> from ) {
   return new FastEntryIterator( ((from.getKey()).doubleValue()) );
  }


 }



 public FastSortedEntrySet <V> double2ObjectEntrySet() {
  if ( entries == null ) entries = new MapEntrySet();




  return entries;
 }


 /** An iterator on keys.
	 *
	 * <P>We simply override the {@link java.util.ListIterator#next()}/{@link java.util.ListIterator#previous()} methods
	 * (and possibly their type-specific counterparts) so that they return keys
	 * instead of entries.
	 */


 private final class KeyIterator extends MapIterator implements DoubleListIterator {
  public KeyIterator( final double k ) { super( k ); }
  public double previousDouble() { return key[ previousEntry() ]; }
  public void set( double k ) { throw new UnsupportedOperationException(); }
  public void add( double k ) { throw new UnsupportedOperationException(); }

  public Double previous() { return (Double.valueOf(key[ previousEntry() ])); }
  public void set( Double ok ) { throw new UnsupportedOperationException(); }
  public void add( Double ok ) { throw new UnsupportedOperationException(); }






  public KeyIterator() { super(); }
  public double nextDouble() { return key[ nextEntry() ]; }

  public Double next() { return (Double.valueOf(key[ nextEntry() ])); }

 }




 private final class KeySet extends AbstractDoubleSortedSet {

  public DoubleBidirectionalIterator iterator( final double from ) {
   return new KeyIterator( from );
  }

  public DoubleBidirectionalIterator iterator() {
   return new KeyIterator();
  }
  public int size() {
   return count;
  }

  public boolean contains( double k ) {
   return containsKey( k );
  }

  public boolean remove( double k ) {
   int oldCount = count;
   Double2ObjectLinkedOpenHashMap.this.remove( k );
   return count != oldCount;
  }

  public void clear() {
   Double2ObjectLinkedOpenHashMap.this.clear();
  }



  public double firstDouble() {
   if ( count == 0 ) throw new NoSuchElementException();
   return key[ first ];
  }

  public double lastDouble() {
   if ( count == 0 ) throw new NoSuchElementException();
   return key[ last ];
  }

  public DoubleComparator comparator() { return null; }

  final public DoubleSortedSet tailSet( double from ) { throw new UnsupportedOperationException(); }
  final public DoubleSortedSet headSet( double to ) { throw new UnsupportedOperationException(); }
  final public DoubleSortedSet subSet( double from, double to ) { throw new UnsupportedOperationException(); }

 }



 public DoubleSortedSet keySet() {



  if ( keys == null ) keys = new KeySet();
  return keys;
 }


 /** An iterator on values.
	 *
	 * <P>We simply override the {@link java.util.ListIterator#next()}/{@link java.util.ListIterator#previous()} methods
	 * (and possibly their type-specific counterparts) so that they return values
	 * instead of entries.
	 */


 private final class ValueIterator extends MapIterator implements ObjectListIterator <V> {
  public V previous() { return value[ previousEntry() ]; }






  public void set( V v ) { throw new UnsupportedOperationException(); }
  public void add( V v ) { throw new UnsupportedOperationException(); }




  public ValueIterator() { super(); }
  public V next() { return value[ nextEntry() ]; }



 }

 public ObjectCollection <V> values() {
  if ( values == null ) values = new AbstractObjectCollection <V>() {

    public ObjectIterator <V> iterator() {
     return new ValueIterator();
    }

    public int size() {
     return count;
    }

    public boolean contains( Object v ) {
     return containsValue( v );
    }

    public void clear() {
     Double2ObjectLinkedOpenHashMap.this.clear();
    }
   };

  return values;
 }


 /** Rehashes this map without changing the table size.
	 * <P>This method should be called when the map underwent numerous deletions and insertions.
	 * In this case, free entries become rare, and unsuccessful searches
	 * require probing <em>all</em> entries. For reasonable load factors this method is linear in the number of entries.
	 * You will need as much additional free memory as
	 * that occupied by the table.
	 *
	 * <P>If you need to reduce the table siza to fit exactly
	 * this map, you must use {@link #trim()}.
	 *
	 * @return <code>true</code> if there was enough memory to rehash the map, <code>false</code> otherwise.
	 * @see #trim()
	 */

 public boolean rehash() {
  try {
   rehash(p);
  }
  catch(OutOfMemoryError cantDoIt) { return false; }
  return true;
 }


 /** Rehashes the map, making the table as small as possible.
	 * 
	 * <P>This method rehashes to the smallest size satisfying
	 * the load factor. It can be used when the map will not be
	 * changed anymore, so to optimize access speed (by collecting
	 * deleted entries) and size.
	 *
	 * <P>If the table size is already the minimum possible, this method
	 * does nothing. If you want to guarantee rehashing, use {@link #rehash()}.
	 *
	 * @return true if there was enough memory to trim the map.
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


 /** Rehashes this map if the table is too large.
	 * 
	 * <P>Let <var>N</var> be the smallest table size that can hold
	 * <code>max(n,{@link #size()})</code> entries, still satisfying the load factor. If the current
	 * table size is smaller than or equal to <var>N</var>, this method does
	 * nothing. Otherwise, it rehashes this map in a table of size
	 * <var>N</var>.
	 *
	 * <P>This method is useful when reusing maps.  {@linkplain #clear() Clearing a
	 * map} leaves the table size untouched. If you are reusing a map
	 * many times, you can call this method with a typical
	 * size to avoid keeping around a very large table just
	 * because of a few large transient maps.
	 *
	 * @param n the threshold for the trimming.
	 * @return true if there was enough memory to trim the map.
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

 /** Resizes the map.
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





  double k;
  V v;

  final int newN = PRIMES[newP];
  final double key[] = this.key, newKey[] = new double[newN];
  final V value[] = this.value, newValue[] = (V[]) new Object[newN];
  final byte newState[] = new byte[newN];

  final int link[] = this.link, newLink[] = new int[ newN ];
  first = -1;

  while(j-- != 0) {





   k = key[i];
   v = value[i];
   k2i = it.unimi.dsi.fastutil.HashCommon.double2int(k) & 0x7FFFFFFF;

   h1 = k2i % newN;
   h2 = (k2i % (newN - 2)) + 1;

   if ( newState[h1] != FREE ) {
    h2 = (k2i % (newN - 2)) + 1;
    do {
     h1 += h2;
     if ( h1 >= newN || h1 < 0 ) h1 -= newN;
    } while( newState[h1] != FREE );
   }

   newState[h1] = OCCUPIED;
   newKey[h1] = k;
   newValue[h1] = v;


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
  this.value = newValue;
  this.state = newState;

  this.link = newLink;
  this.last = newPrev;
  if ( newPrev != -1 ) newLink[ newPrev ] ^= -1;

 }


 /** Returns a deep copy of this map. 
	 *
	 * <P>This method performs a deep copy of this hash map; the data stored in the
	 * map, however, is not cloned. Note that this makes a difference only for object keys.
	 *
	 *  @return a deep copy of this map.
	 */

 @SuppressWarnings("unchecked")
 public Object clone() {
  Double2ObjectLinkedOpenHashMap c;
  try {
   c = (Double2ObjectLinkedOpenHashMap)super.clone();
  }
  catch(CloneNotSupportedException cantHappen) {
   throw new InternalError();
  }

  c.keys = null;
  c.values = null;
  c.entries = null;

  c.key = key.clone();
  c.value = value.clone();
  c.state = state.clone();

  c.link = link.clone();




  return c;
 }


 /** Returns a hash code for this map.
	 *
	 * This method overrides the generic method provided by the superclass. 
	 * Since <code>equals()</code> is not overriden, it is important
	 * that the value returned by this method is the same value as
	 * the one returned by the overriden method.
	 *
	 * @return a hash code for this map.
	 */

 public int hashCode() {
  int h = 0, t, i = 0, j = count;
  while( j-- != 0 ) {
   while( state[ i ] != OCCUPIED ) i++;
   t = 0;



    t = it.unimi.dsi.fastutil.HashCommon.double2int(key[ i ]);

   if ( this != value[ i ] )

    t ^= ( (value[ i ]) == null ? 0 : (value[ i ]).hashCode() );
   h += t;
   i++;
  }
  return h;
 }



 private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
  final double key[] = this.key;
  final V value[] = this.value;
  final MapIterator i = new MapIterator();
  int e, j = count;

  s.defaultWriteObject();

  while( j-- != 0 ) {
   e = i.nextEntry();
   s.writeDouble( key[ e ] );
   s.writeObject( value[ e ] );
  }
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

  final double key[] = this.key = new double[ n ];
  final V value[] = this.value = (V[]) new Object[ n ];
  final byte state[] = this.state = new byte[ n ];

  final int link[] = this.link = new int[ n ];
  int prev = -1;
  first = last = -1;


  int i, k2i, h1, h2;
  double k;
  V v;

  i = count;
  while( i-- != 0 ) {

   k = s.readDouble();
   v = (V) s.readObject();
   k2i = it.unimi.dsi.fastutil.HashCommon.double2int(k) & 0x7FFFFFFF;

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
   value[ h1 ] = v;


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
