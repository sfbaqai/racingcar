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

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.bytes.ByteArrays;

import java.util.Arrays;
import java.util.Collection;



import java.util.NoSuchElementException;
/**  A hash set with with a fast, small-footprint implementation whose {@linkplain it.unimi.dsi.fastutil.Hash.Strategy hashing strategy}
 * is specified at creation time.
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
 *
 * @see Hash
 * @see HashCommon
 */

public class ObjectOpenCustomHashSet <K> extends AbstractObjectSet <K> implements java.io.Serializable, Cloneable, Hash {
 /** The array of keys. */
 protected transient K key[];

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
 /** The hash strategy of this custom set. */
 protected Strategy<K> strategy;


    public static final long serialVersionUID = -7046029254386353129L;

 private static final boolean ASSERTS = false;


 /** Creates a new hash set.
	 *
	 * The actual table size is the least available prime greater than <code>n</code>/<code>f</code>.
	 *
	 * @param n the expected number of elements in the hash set. 
	 * @param f the load factor.
	 * @param strategy the strategy.
	 * @see Hash#PRIMES
	 */
 @SuppressWarnings("unchecked")
 public ObjectOpenCustomHashSet( final int n, final float f, final Strategy<K> strategy ) {
  this.strategy = strategy;
  if ( f <= 0 || f > 1 ) throw new IllegalArgumentException( "Load factor must be greater than 0 and smaller than or equal to 1" );
  if ( n < 0 ) throw new IllegalArgumentException( "Hash table size must be nonnegative" );

  int l = Arrays.binarySearch( PRIMES, (int)( n / f ) + 1 );
  if ( l < 0 ) l = -l - 1;

  free = PRIMES[ p = l ];
  this.f = f;
  this.maxFill = (int)( free * f );
  key = (K[]) new Object[ free ];
  state = new byte[ free ];



 }



 /** Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor.
	 *
	 * @param n the expected number of elements in the hash set. 
	 * @param strategy the strategy.
	 */

 public ObjectOpenCustomHashSet( final int n, final Strategy<K> strategy ) {
  this( n, DEFAULT_LOAD_FACTOR, strategy );
 }
 /** Creates a new hash set with {@link Hash#DEFAULT_INITIAL_SIZE} elements
	 * and {@link Hash#DEFAULT_LOAD_FACTOR} as load factor.
	 * @param strategy the strategy.
	 */

 public ObjectOpenCustomHashSet( final Strategy<K> strategy ) {
 this( DEFAULT_INITIAL_SIZE, DEFAULT_LOAD_FACTOR, strategy );
 }
 /** Creates a new hash set copying a given collection.
	 *
	 * @param c a {@link Collection} to be copied into the new hash set. 
	 * @param f the load factor.
	 * @param strategy the strategy.
	 */

 public ObjectOpenCustomHashSet( final Collection<? extends K> c, final float f, final Strategy<K> strategy ) {
 this( c.size(), f, strategy );
  addAll( c );
 }
 /** Creates a new hash set  with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor 
	 * copying a given collection.
	 *
	 * @param c a {@link Collection} to be copied into the new hash set. 
	 * @param strategy the strategy.
	 */

 public ObjectOpenCustomHashSet( final Collection<? extends K> c, final Strategy<K> strategy ) {
  this( c, DEFAULT_LOAD_FACTOR, strategy );
 }
 /** Creates a new hash set copying a given type-specific collection.
	 *
	 * @param c a type-specific collection to be copied into the new hash set. 
	 * @param f the load factor.
	 * @param strategy the strategy.
	 */

 public ObjectOpenCustomHashSet( final ObjectCollection <? extends K> c, final float f, Strategy<K> strategy ) {
 this( c.size(), f, strategy );
  addAll( c );
 }
 /** Creates a new hash set  with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor 
	 * copying a given type-specific collection.
	 *
	 * @param c a type-specific collection to be copied into the new hash set. 
	 * @param strategy the strategy.
	 */

 public ObjectOpenCustomHashSet( final ObjectCollection <? extends K> c, final Strategy<K> strategy ) {
  this( c, DEFAULT_LOAD_FACTOR, strategy );
 }
 /** Creates a new hash set using elements provided by a type-specific iterator.
	 *
	 * @param i a type-specific iterator whose elements will fill the set.
	 * @param f the load factor.
	 * @param strategy the strategy.
	 */

 public ObjectOpenCustomHashSet( final ObjectIterator <K> i, final float f, final Strategy<K> strategy ) {
  this( DEFAULT_INITIAL_SIZE, f, strategy );
  while( i.hasNext() ) add( i.next() );
 }
 /** Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor using elements provided by a type-specific iterator.
	 *
	 * @param i a type-specific iterator whose elements will fill the set.
	 * @param strategy the strategy.
	 */

 public ObjectOpenCustomHashSet( final ObjectIterator <K> i, final Strategy<K> strategy ) {
  this( i, DEFAULT_LOAD_FACTOR, strategy );
 }
 /** Creates a new hash set and fills it with the elements of a given array.
	 *
	 * @param a an array whose elements will be used to fill the set.
	 * @param offset the first element to use.
	 * @param length the number of elements to use.
	 * @param f the load factor.
	 * @param strategy the strategy.
	 */

 public ObjectOpenCustomHashSet( final K[] a, final int offset, final int length, final float f, final Strategy<K> strategy ) {
 this( length < 0 ? 0 : length, f, strategy );
  ObjectArrays.ensureOffsetLength( a, offset, length );
  for( int i = 0; i < length; i++ ) add( a[ offset + i ] );
 }
 /** Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor and fills it with the elements of a given array.
	 *
	 * @param a an array whose elements will be used to fill the set.
	 * @param offset the first element to use.
	 * @param length the number of elements to use.
	 * @param strategy the strategy.
	 */

 public ObjectOpenCustomHashSet( final K[] a, final int offset, final int length, final Strategy<K> strategy ) {
  this( a, offset, length, DEFAULT_LOAD_FACTOR, strategy );
 }
 /** Creates a new hash set copying the elements of an array.
	 *
	 * @param a an array to be copied into the new hash set. 
	 * @param f the load factor.
	 * @param strategy the strategy.
	 */

 public ObjectOpenCustomHashSet( final K[] a, final float f, final Strategy<K> strategy ) {
  this( a, 0, a.length, f, strategy );
 }
 /** Creates a new hash set with {@link Hash#DEFAULT_LOAD_FACTOR} as load factor 
	 * copying the elements of an array.
	 *
	 * @param a an array to be copied into the new hash set. 
	 * @param strategy the strategy.
	 */

 public ObjectOpenCustomHashSet( final K[] a, final Strategy<K> strategy ) {
  this( a, DEFAULT_LOAD_FACTOR, strategy );
 }
 /** Returns the hashing strategy.
	 *
	 * @return the hashing strategy of this custom hash set.
	 */

 public Strategy<K> strategy() {
  return strategy;
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

 protected final int findInsertionPoint( final K k ) {
  final K key[] = this.key;
  final byte state[] = this.state;
  final int n = key.length;

  // First of all, we make the key into a positive integer.

  final int h, k2i = ( h = ( strategy.hashCode(k) ) ) & 0x7FFFFFFF;



  // The primary hash, a.k.a. starting point.
  int h1 = k2i % n;

  if ( state[ h1 ] == OCCUPIED && ! ( (key[ h1 ]) != HashCommon.REMOVED && (h) == strategy.hashCode(key[ h1 ]) && strategy.equals((k), (key[ h1 ])) ) ) {
   // The secondary hash.
   final int h2 = ( k2i % ( n - 2 ) ) + 1;
   do {
    h1 += h2;
    if ( h1 >= n || h1 < 0 ) h1 -= n;
   } while( state[ h1 ] == OCCUPIED && ! ( (key[ h1 ]) != HashCommon.REMOVED && (h) == strategy.hashCode(key[ h1 ]) && strategy.equals((k), (key[ h1 ])) ) ); // There's always a FREE entry.
  }

  if (state[ h1 ] == FREE) return h1;
  if (state[ h1 ] == OCCUPIED) return -h1-1; // Necessarily, KEY_EQUALS_HASH( k, h, key[ h1 ] ).

  /* Tables without deletions will never use code beyond this point. */

  final int i = h1; // Remember first available bucket for later.

  /** See the comments in the documentation of the interface Hash. */
  if ( ASSERTS ) assert state[ h1 ] == REMOVED;
  if ( ! ( (key[ h1 ]) != HashCommon.REMOVED && (h) == strategy.hashCode(key[ h1 ]) && strategy.equals((k), (key[ h1 ])) ) ) {
   // The secondary hash.
   final int h2 = ( k2i % ( n - 2 ) ) + 1;
   do {
    h1 += h2;
    if ( h1 >= n || h1 < 0 ) h1 -= n;
   } while( state[ h1 ] != FREE && ! ( (key[ h1 ]) != HashCommon.REMOVED && (h) == strategy.hashCode(key[ h1 ]) && strategy.equals((k), (key[ h1 ])) ) );
  }

  return state[ h1 ] == OCCUPIED ? -h1-1 : i; // In the first case, necessarily, KEY_EQUALS_HASH( k, h, key[ h1 ] ).
 }


 /** Searches for a key.
	 *
	 * @param k the key.
	 * @return the index of the entry containing the key, or -1 if the key wasn't found.
	 */

 protected final int findKey( final K k ) {
  final K key[] = this.key;
  final byte state[] = this.state;
  final int n = key.length;

  // First of all, we make the key into a positive integer.

  final int h, k2i = ( h = ( strategy.hashCode(k) ) ) & 0x7FFFFFFF;



  // The primary hash, a.k.a. starting point.
  int h1 = k2i % n;

  /** See the comments in the documentation of the interface Hash. */
  if ( state[ h1 ] != FREE && ! ( (key[ h1 ]) != HashCommon.REMOVED && (h) == strategy.hashCode(key[ h1 ]) && strategy.equals((k), (key[ h1 ])) ) ) {
   // The secondary hash.
   final int h2 = ( k2i % ( n - 2 ) ) + 1;
   do {
    h1 += h2;
    if ( h1 >= n || h1 < 0 ) h1 -= n;
   } while( state[ h1 ] != FREE && ! ( (key[ h1 ]) != HashCommon.REMOVED && (h) == strategy.hashCode(key[ h1 ]) && strategy.equals((k), (key[ h1 ])) ) ); // There's always a FREE entry.
  }

  return state[ h1 ] == OCCUPIED ? h1 : -1; // In the first case, necessarily, KEY_EQUALS_HASH( k, h, key[ h1 ] ).
 }

 public boolean add( final K k ) {
  final int i = findInsertionPoint( k );
  if ( i < 0 ) return false;

  if ( state[ i ] == FREE ) free--;
  state[ i ] = OCCUPIED;
  key[ i ] = k;
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
 public boolean remove( final Object k ) {
  final int i = findKey( (K) k );
  if ( i < 0 ) return false;
  state[ i ] = REMOVED;
  count--;


  key[ i ] = (K) HashCommon.REMOVED;






  if ( ASSERTS ) checkTable();
  return true;
 }

 @SuppressWarnings("unchecked")
 public boolean contains( final Object k ) {
  return findKey( (K) k ) >= 0;
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


  ObjectArrays.fill( key, null );





 }
 /** An iterator over a hash set. */

 private class SetIterator extends AbstractObjectIterator <K> {
  /** The index of the next entry to be returned. */
  int pos = 0;
  /** The index of the last entry that has been returned. */
  int last = -1;
  /** A downward counter measuring how many entries have been returned. */
  int c = count;

  {
   final byte state[] = ObjectOpenCustomHashSet.this.state;
   final int n = state.length;

   if ( c != 0 ) while( pos < n && state[pos] != OCCUPIED ) pos++;
  }

  public boolean hasNext() {
   return c != 0 && pos < ObjectOpenCustomHashSet.this.state.length;
  }

  public K next() {
   K retVal;
   final byte state[] = ObjectOpenCustomHashSet.this.state;
   final int n = state.length;

   if ( ! hasNext() ) throw new NoSuchElementException();
   retVal = key[ last = pos ];
   if ( --c != 0 ) do pos++; while( pos < n && state[ pos ] != OCCUPIED );

   return retVal;
  }

  @SuppressWarnings("unchecked")
  public void remove() {
   if ( last == -1 || state[ last ] != OCCUPIED ) throw new IllegalStateException();
   state[last] = REMOVED;

   key[ last ] = (K) HashCommon.REMOVED;

   count--;
  }
 }

 public ObjectIterator <K> iterator() {
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



  int i = 0, j = count, k2i, h1, h2;
  final byte state[] = this.state;


  //System.err.println("Rehashing to size " +  PRIMES[newP] + "...");

  K k;

  final int newN = PRIMES[ newP ];
  final K key[] = this.key, newKey[] = (K[]) new Object[ newN ];
  final byte newState[] = new byte[ newN ];





  while( j-- != 0 ) {


   while( state[ i ] != OCCUPIED ) i++;


   k = key[ i ];
   k2i = ( strategy.hashCode(k) ) & 0x7FFFFFFF;

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
   i++;

  }

  p = newP;
  free = newN - count;
  maxFill = (int)( newN * f );
  this.key = newKey;
  this.state = newState;





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
  ObjectOpenCustomHashSet <K> c;
  try {
   c = (ObjectOpenCustomHashSet <K>)super.clone();
  }
  catch(CloneNotSupportedException cantHappen) {
   throw new InternalError();
  }
  c.key = key.clone();
  c.state = state.clone();




  c.strategy = strategy;

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

   if ( this != key[ i ] )

    h += ( strategy.hashCode(key[ i ]) );
   i++;
  }
  return h;
 }


 private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
  final ObjectIterator <K> i = iterator();
  int j = count;
  s.defaultWriteObject();
  while( j-- != 0 ) s.writeObject( i.next() );
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

  final K key[] = this.key = (K[]) new Object[ n ];
  final byte state[] = this.state = new byte[ n ];






  int i, k2i, h1, h2;
  K k;

  i = count;
  while( i-- != 0 ) {

   k = (K) s.readObject();
   k2i = ( strategy.hashCode(k) ) & 0x7FFFFFFF;

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
  }






  if ( ASSERTS ) checkTable();
 }
 private void checkTable() {}
}
