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
/* Primitive-type-only definitions (values) */
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

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.NoSuchElementException;

/** A class providing static methods and objects that do useful things with type-specific sorted maps.
 *
 * @see java.util.Collections
 */

public class Int2IntSortedMaps {

 private Int2IntSortedMaps() {}

 /** Returns a comparator for entries based on a given comparator on keys.
	 *
	 * @param comparator a comparator on keys.
	 * @return the associated comparator on entries.
	 */
 public static Comparator<? super Map.Entry<Integer, ?>> entryComparator( final IntComparator comparator ) {
  return new Comparator<Map.Entry<Integer, ?>>() {
   public int compare( Map.Entry<Integer, ?> x, Map.Entry<Integer, ?> y ) {
    return comparator.compare( x.getKey(), y.getKey() );
   }
  };
 }


 /** An immutable class representing an empty type-specific sorted map. 
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific sorted map.
	 */

 public static class EmptySortedMap extends Int2IntMaps.EmptyMap implements Int2IntSortedMap , java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected EmptySortedMap() {}

  public IntComparator comparator() { return null; }

  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Int2IntMap.Entry > int2IntEntrySet() { return ObjectSortedSets.EMPTY_SET; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<Integer, Integer>> entrySet() { return ObjectSortedSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public IntSortedSet keySet() { return IntSortedSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public Int2IntSortedMap subMap( final int from, final int to ) { return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Int2IntSortedMap headMap( final int to ) { return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Int2IntSortedMap tailMap( final int from ) { return EMPTY_MAP; }

  public int firstIntKey() { throw new NoSuchElementException(); }
  public int lastIntKey() { throw new NoSuchElementException(); }


  public Int2IntSortedMap headMap( Integer oto ) { return headMap( ((oto).intValue()) ); }
  public Int2IntSortedMap tailMap( Integer ofrom ) { return tailMap( ((ofrom).intValue()) ); }
  public Int2IntSortedMap subMap( Integer ofrom, Integer oto ) { return subMap( ((ofrom).intValue()), ((oto).intValue()) ); }

  public Integer firstKey() { return (Integer.valueOf(firstIntKey())); }
  public Integer lastKey() { return (Integer.valueOf(lastIntKey())); }


 }



 /** An empty type-specific sorted map (immutable). It is serializable and cloneable. */

 @SuppressWarnings("unchecked")
 public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();


 /** An immutable class representing a type-specific singleton sorted map. 
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific sorted map.
	 */

 public static class Singleton extends Int2IntMaps.Singleton implements Int2IntSortedMap , java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final IntComparator comparator;

  protected Singleton( final int key, final int value, IntComparator comparator ) {
   super( key, value );
   this.comparator = comparator;
  }

  protected Singleton( final int key, final int value ) {
   this( key, value, null );
  }

  @SuppressWarnings("unchecked")
  final int compare( final int k1, final int k2 ) {
   return comparator == null ? ( (k1) < (k2) ? -1 : ( (k1) == (k2) ? 0 : 1 ) ) : comparator.compare( k1, k2 );
  }

  public IntComparator comparator() { return comparator; }

  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Int2IntMap.Entry > int2IntEntrySet() { if ( entries == null ) entries = ObjectSortedSets.singleton( (Int2IntMap.Entry )new SingletonEntry(), (Comparator<? super Int2IntMap.Entry >)entryComparator( comparator ) ); return (ObjectSortedSet<Int2IntMap.Entry >)entries; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<Integer, Integer>> entrySet() { return (ObjectSortedSet)int2IntEntrySet(); }

  public IntSortedSet keySet() { if ( keys == null ) keys = IntSortedSets.singleton( key, comparator ); return (IntSortedSet )keys; }

  @SuppressWarnings("unchecked")
  public Int2IntSortedMap subMap( final int from, final int to ) { if ( compare( from, key ) <= 0 && compare( key, to ) < 0 ) return this; return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Int2IntSortedMap headMap( final int to ) { if ( compare( key, to ) < 0 ) return this; return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Int2IntSortedMap tailMap( final int from ) { if ( compare( from, key ) <= 0 ) return this; return EMPTY_MAP; }

  public int firstIntKey() { return key; }
  public int lastIntKey() { return key; }


  public Int2IntSortedMap headMap( Integer oto ) { return headMap( ((oto).intValue()) ); }
  public Int2IntSortedMap tailMap( Integer ofrom ) { return tailMap( ((ofrom).intValue()) ); }
  public Int2IntSortedMap subMap( Integer ofrom, Integer oto ) { return subMap( ((ofrom).intValue()), ((oto).intValue()) ); }

  public Integer firstKey() { return (Integer.valueOf(firstIntKey())); }
  public Integer lastKey() { return (Integer.valueOf(lastIntKey())); }

 }

 /** Returns a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <P>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair <code>&lt;key,value></code>.
	 */

 public static Int2IntSortedMap singleton( final Integer key, Integer value ) {
  return new Singleton ( ((key).intValue()), ((value).intValue()) );
 }

 /** RETURNS a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <P>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @param comparator the comparator to use in the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair <code>&lt;key,value></code>.
	 */

 public static Int2IntSortedMap singleton( final Integer key, Integer value, IntComparator comparator ) {
  return new Singleton ( ((key).intValue()), ((value).intValue()), comparator );
 }



 /** Returns a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <P>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair <code>&lt;key,value></code>.
	 */

 public static Int2IntSortedMap singleton( final int key, final int value ) {
  return new Singleton ( key, value );
 }

 /** Returns a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <P>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @param comparator the comparator to use in the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair <code>&lt;key,value></code>.
	 */

 public static Int2IntSortedMap singleton( final int key, final int value, IntComparator comparator ) {
  return new Singleton ( key, value, comparator );
 }




  /** A synchronized wrapper class for sorted maps. */

 public static class SynchronizedSortedMap extends Int2IntMaps.SynchronizedMap implements Int2IntSortedMap , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Int2IntSortedMap sortedMap;

  protected SynchronizedSortedMap( final Int2IntSortedMap m, final Object sync ) {
   super( m, sync );
   sortedMap = m;
  }

  protected SynchronizedSortedMap( final Int2IntSortedMap m ) {
   super( m );
   sortedMap = m;
  }

  public IntComparator comparator() { synchronized( sync ) { return sortedMap.comparator(); } }

  public ObjectSortedSet<Int2IntMap.Entry > int2IntEntrySet() { if ( entries == null ) entries = ObjectSortedSets.synchronize( sortedMap.int2IntEntrySet(), sync ); return (ObjectSortedSet<Int2IntMap.Entry >)entries; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<Integer, Integer>> entrySet() { return (ObjectSortedSet)int2IntEntrySet(); }
  public IntSortedSet keySet() { if ( keys == null ) keys = IntSortedSets.synchronize( sortedMap.keySet(), sync ); return (IntSortedSet )keys; }

  public Int2IntSortedMap subMap( final int from, final int to ) { return new SynchronizedSortedMap ( sortedMap.subMap( from, to ), sync ); }
  public Int2IntSortedMap headMap( final int to ) { return new SynchronizedSortedMap ( sortedMap.headMap( to ), sync ); }
  public Int2IntSortedMap tailMap( final int from ) { return new SynchronizedSortedMap ( sortedMap.tailMap( from ), sync ); }

  public int firstIntKey() { synchronized( sync ) { return sortedMap.firstIntKey(); } }
  public int lastIntKey() { synchronized( sync ) { return sortedMap.lastIntKey(); } }


  public Integer firstKey() { synchronized( sync ) { return sortedMap.firstKey(); } }
  public Integer lastKey() { synchronized( sync ) { return sortedMap.lastKey(); } }

  public Int2IntSortedMap subMap( final Integer from, final Integer to ) { return new SynchronizedSortedMap ( sortedMap.subMap( from, to ), sync ); }
  public Int2IntSortedMap headMap( final Integer to ) { return new SynchronizedSortedMap ( sortedMap.headMap( to ), sync ); }
  public Int2IntSortedMap tailMap( final Integer from ) { return new SynchronizedSortedMap ( sortedMap.tailMap( from ), sync ); }



 }

 /** Returns a synchronized type-specific sorted map backed by the given type-specific sorted map.
	 *
	 * @param m the sorted map to be wrapped in a synchronized sorted map.
	 * @return a synchronized view of the specified sorted map.
	 * @see java.util.Collections#synchronizedSortedMap(SortedMap)
	 */
 public static Int2IntSortedMap synchronize( final Int2IntSortedMap m ) { return new SynchronizedSortedMap ( m ); }

 /** Returns a synchronized type-specific sorted map backed by the given type-specific sorted map, using an assigned object to synchronize.
	 *
	 * @param m the sorted map to be wrapped in a synchronized sorted map.
	 * @param sync an object that will be used to synchronize the access to the sorted sorted map.
	 * @return a synchronized view of the specified sorted map.
	 * @see java.util.Collections#synchronizedSortedMap(SortedMap)
	 */

 public static Int2IntSortedMap synchronize( final Int2IntSortedMap m, final Object sync ) { return new SynchronizedSortedMap ( m, sync ); }




 /** An unmodifiable wrapper class for sorted maps. */

 public static class UnmodifiableSortedMap extends Int2IntMaps.UnmodifiableMap implements Int2IntSortedMap , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Int2IntSortedMap sortedMap;

  protected UnmodifiableSortedMap( final Int2IntSortedMap m ) {
   super( m );
   sortedMap = m;
  }

  public IntComparator comparator() { return sortedMap.comparator(); }

  public ObjectSortedSet<Int2IntMap.Entry > int2IntEntrySet() { if ( entries == null ) entries = ObjectSortedSets.unmodifiable( sortedMap.int2IntEntrySet() ); return (ObjectSortedSet<Int2IntMap.Entry >)entries; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<Integer, Integer>> entrySet() { return (ObjectSortedSet)int2IntEntrySet(); }
  public IntSortedSet keySet() { if ( keys == null ) keys = IntSortedSets.unmodifiable( sortedMap.keySet() ); return (IntSortedSet )keys; }

  public Int2IntSortedMap subMap( final int from, final int to ) { return new UnmodifiableSortedMap ( sortedMap.subMap( from, to ) ); }
  public Int2IntSortedMap headMap( final int to ) { return new UnmodifiableSortedMap ( sortedMap.headMap( to ) ); }
  public Int2IntSortedMap tailMap( final int from ) { return new UnmodifiableSortedMap ( sortedMap.tailMap( from ) ); }

  public int firstIntKey() { return sortedMap.firstIntKey(); }
  public int lastIntKey() { return sortedMap.lastIntKey(); }


  public Integer firstKey() { return sortedMap.firstKey(); }
  public Integer lastKey() { return sortedMap.lastKey(); }

  public Int2IntSortedMap subMap( final Integer from, final Integer to ) { return new UnmodifiableSortedMap ( sortedMap.subMap( from, to ) ); }
  public Int2IntSortedMap headMap( final Integer to ) { return new UnmodifiableSortedMap ( sortedMap.headMap( to ) ); }
  public Int2IntSortedMap tailMap( final Integer from ) { return new UnmodifiableSortedMap ( sortedMap.tailMap( from ) ); }



 }

 /** Returns an unmodifiable type-specific sorted map backed by the given type-specific sorted map.
	 *
	 * @param m the sorted map to be wrapped in an unmodifiable sorted map.
	 * @return an unmodifiable view of the specified sorted map.
	 * @see java.util.Collections#unmodifiableSortedMap(SortedMap)
	 */
 public static Int2IntSortedMap unmodifiable( final Int2IntSortedMap m ) { return new UnmodifiableSortedMap ( m ); }
}
