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

package it.unimi.dsi.fastutil.doubles;

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

public class Double2CharSortedMaps {

 private Double2CharSortedMaps() {}

 /** Returns a comparator for entries based on a given comparator on keys.
	 *
	 * @param comparator a comparator on keys.
	 * @return the associated comparator on entries.
	 */
 public static Comparator<? super Map.Entry<Double, ?>> entryComparator( final DoubleComparator comparator ) {
  return new Comparator<Map.Entry<Double, ?>>() {
   public int compare( Map.Entry<Double, ?> x, Map.Entry<Double, ?> y ) {
    return comparator.compare( x.getKey(), y.getKey() );
   }
  };
 }


 /** An immutable class representing an empty type-specific sorted map. 
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific sorted map.
	 */

 public static class EmptySortedMap extends Double2CharMaps.EmptyMap implements Double2CharSortedMap , java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected EmptySortedMap() {}

  public DoubleComparator comparator() { return null; }

  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Double2CharMap.Entry > double2CharEntrySet() { return ObjectSortedSets.EMPTY_SET; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<Double, Character>> entrySet() { return ObjectSortedSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public DoubleSortedSet keySet() { return DoubleSortedSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public Double2CharSortedMap subMap( final double from, final double to ) { return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Double2CharSortedMap headMap( final double to ) { return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Double2CharSortedMap tailMap( final double from ) { return EMPTY_MAP; }

  public double firstDoubleKey() { throw new NoSuchElementException(); }
  public double lastDoubleKey() { throw new NoSuchElementException(); }


  public Double2CharSortedMap headMap( Double oto ) { return headMap( ((oto).doubleValue()) ); }
  public Double2CharSortedMap tailMap( Double ofrom ) { return tailMap( ((ofrom).doubleValue()) ); }
  public Double2CharSortedMap subMap( Double ofrom, Double oto ) { return subMap( ((ofrom).doubleValue()), ((oto).doubleValue()) ); }

  public Double firstKey() { return (Double.valueOf(firstDoubleKey())); }
  public Double lastKey() { return (Double.valueOf(lastDoubleKey())); }


 }



 /** An empty type-specific sorted map (immutable). It is serializable and cloneable. */

 @SuppressWarnings("unchecked")
 public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();


 /** An immutable class representing a type-specific singleton sorted map. 
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific sorted map.
	 */

 public static class Singleton extends Double2CharMaps.Singleton implements Double2CharSortedMap , java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final DoubleComparator comparator;

  protected Singleton( final double key, final char value, DoubleComparator comparator ) {
   super( key, value );
   this.comparator = comparator;
  }

  protected Singleton( final double key, final char value ) {
   this( key, value, null );
  }

  @SuppressWarnings("unchecked")
  final int compare( final double k1, final double k2 ) {
   return comparator == null ? ( (k1) < (k2) ? -1 : ( (k1) == (k2) ? 0 : 1 ) ) : comparator.compare( k1, k2 );
  }

  public DoubleComparator comparator() { return comparator; }

  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Double2CharMap.Entry > double2CharEntrySet() { if ( entries == null ) entries = ObjectSortedSets.singleton( (Double2CharMap.Entry )new SingletonEntry(), (Comparator<? super Double2CharMap.Entry >)entryComparator( comparator ) ); return (ObjectSortedSet<Double2CharMap.Entry >)entries; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<Double, Character>> entrySet() { return (ObjectSortedSet)double2CharEntrySet(); }

  public DoubleSortedSet keySet() { if ( keys == null ) keys = DoubleSortedSets.singleton( key, comparator ); return (DoubleSortedSet )keys; }

  @SuppressWarnings("unchecked")
  public Double2CharSortedMap subMap( final double from, final double to ) { if ( compare( from, key ) <= 0 && compare( key, to ) < 0 ) return this; return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Double2CharSortedMap headMap( final double to ) { if ( compare( key, to ) < 0 ) return this; return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Double2CharSortedMap tailMap( final double from ) { if ( compare( from, key ) <= 0 ) return this; return EMPTY_MAP; }

  public double firstDoubleKey() { return key; }
  public double lastDoubleKey() { return key; }


  public Double2CharSortedMap headMap( Double oto ) { return headMap( ((oto).doubleValue()) ); }
  public Double2CharSortedMap tailMap( Double ofrom ) { return tailMap( ((ofrom).doubleValue()) ); }
  public Double2CharSortedMap subMap( Double ofrom, Double oto ) { return subMap( ((ofrom).doubleValue()), ((oto).doubleValue()) ); }

  public Double firstKey() { return (Double.valueOf(firstDoubleKey())); }
  public Double lastKey() { return (Double.valueOf(lastDoubleKey())); }

 }

 /** Returns a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <P>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair <code>&lt;key,value></code>.
	 */

 public static Double2CharSortedMap singleton( final Double key, Character value ) {
  return new Singleton ( ((key).doubleValue()), ((value).charValue()) );
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

 public static Double2CharSortedMap singleton( final Double key, Character value, DoubleComparator comparator ) {
  return new Singleton ( ((key).doubleValue()), ((value).charValue()), comparator );
 }



 /** Returns a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <P>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair <code>&lt;key,value></code>.
	 */

 public static Double2CharSortedMap singleton( final double key, final char value ) {
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

 public static Double2CharSortedMap singleton( final double key, final char value, DoubleComparator comparator ) {
  return new Singleton ( key, value, comparator );
 }




  /** A synchronized wrapper class for sorted maps. */

 public static class SynchronizedSortedMap extends Double2CharMaps.SynchronizedMap implements Double2CharSortedMap , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Double2CharSortedMap sortedMap;

  protected SynchronizedSortedMap( final Double2CharSortedMap m, final Object sync ) {
   super( m, sync );
   sortedMap = m;
  }

  protected SynchronizedSortedMap( final Double2CharSortedMap m ) {
   super( m );
   sortedMap = m;
  }

  public DoubleComparator comparator() { synchronized( sync ) { return sortedMap.comparator(); } }

  public ObjectSortedSet<Double2CharMap.Entry > double2CharEntrySet() { if ( entries == null ) entries = ObjectSortedSets.synchronize( sortedMap.double2CharEntrySet(), sync ); return (ObjectSortedSet<Double2CharMap.Entry >)entries; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<Double, Character>> entrySet() { return (ObjectSortedSet)double2CharEntrySet(); }
  public DoubleSortedSet keySet() { if ( keys == null ) keys = DoubleSortedSets.synchronize( sortedMap.keySet(), sync ); return (DoubleSortedSet )keys; }

  public Double2CharSortedMap subMap( final double from, final double to ) { return new SynchronizedSortedMap ( sortedMap.subMap( from, to ), sync ); }
  public Double2CharSortedMap headMap( final double to ) { return new SynchronizedSortedMap ( sortedMap.headMap( to ), sync ); }
  public Double2CharSortedMap tailMap( final double from ) { return new SynchronizedSortedMap ( sortedMap.tailMap( from ), sync ); }

  public double firstDoubleKey() { synchronized( sync ) { return sortedMap.firstDoubleKey(); } }
  public double lastDoubleKey() { synchronized( sync ) { return sortedMap.lastDoubleKey(); } }


  public Double firstKey() { synchronized( sync ) { return sortedMap.firstKey(); } }
  public Double lastKey() { synchronized( sync ) { return sortedMap.lastKey(); } }

  public Double2CharSortedMap subMap( final Double from, final Double to ) { return new SynchronizedSortedMap ( sortedMap.subMap( from, to ), sync ); }
  public Double2CharSortedMap headMap( final Double to ) { return new SynchronizedSortedMap ( sortedMap.headMap( to ), sync ); }
  public Double2CharSortedMap tailMap( final Double from ) { return new SynchronizedSortedMap ( sortedMap.tailMap( from ), sync ); }



 }

 /** Returns a synchronized type-specific sorted map backed by the given type-specific sorted map.
	 *
	 * @param m the sorted map to be wrapped in a synchronized sorted map.
	 * @return a synchronized view of the specified sorted map.
	 * @see java.util.Collections#synchronizedSortedMap(SortedMap)
	 */
 public static Double2CharSortedMap synchronize( final Double2CharSortedMap m ) { return new SynchronizedSortedMap ( m ); }

 /** Returns a synchronized type-specific sorted map backed by the given type-specific sorted map, using an assigned object to synchronize.
	 *
	 * @param m the sorted map to be wrapped in a synchronized sorted map.
	 * @param sync an object that will be used to synchronize the access to the sorted sorted map.
	 * @return a synchronized view of the specified sorted map.
	 * @see java.util.Collections#synchronizedSortedMap(SortedMap)
	 */

 public static Double2CharSortedMap synchronize( final Double2CharSortedMap m, final Object sync ) { return new SynchronizedSortedMap ( m, sync ); }




 /** An unmodifiable wrapper class for sorted maps. */

 public static class UnmodifiableSortedMap extends Double2CharMaps.UnmodifiableMap implements Double2CharSortedMap , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Double2CharSortedMap sortedMap;

  protected UnmodifiableSortedMap( final Double2CharSortedMap m ) {
   super( m );
   sortedMap = m;
  }

  public DoubleComparator comparator() { return sortedMap.comparator(); }

  public ObjectSortedSet<Double2CharMap.Entry > double2CharEntrySet() { if ( entries == null ) entries = ObjectSortedSets.unmodifiable( sortedMap.double2CharEntrySet() ); return (ObjectSortedSet<Double2CharMap.Entry >)entries; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<Double, Character>> entrySet() { return (ObjectSortedSet)double2CharEntrySet(); }
  public DoubleSortedSet keySet() { if ( keys == null ) keys = DoubleSortedSets.unmodifiable( sortedMap.keySet() ); return (DoubleSortedSet )keys; }

  public Double2CharSortedMap subMap( final double from, final double to ) { return new UnmodifiableSortedMap ( sortedMap.subMap( from, to ) ); }
  public Double2CharSortedMap headMap( final double to ) { return new UnmodifiableSortedMap ( sortedMap.headMap( to ) ); }
  public Double2CharSortedMap tailMap( final double from ) { return new UnmodifiableSortedMap ( sortedMap.tailMap( from ) ); }

  public double firstDoubleKey() { return sortedMap.firstDoubleKey(); }
  public double lastDoubleKey() { return sortedMap.lastDoubleKey(); }


  public Double firstKey() { return sortedMap.firstKey(); }
  public Double lastKey() { return sortedMap.lastKey(); }

  public Double2CharSortedMap subMap( final Double from, final Double to ) { return new UnmodifiableSortedMap ( sortedMap.subMap( from, to ) ); }
  public Double2CharSortedMap headMap( final Double to ) { return new UnmodifiableSortedMap ( sortedMap.headMap( to ) ); }
  public Double2CharSortedMap tailMap( final Double from ) { return new UnmodifiableSortedMap ( sortedMap.tailMap( from ) ); }



 }

 /** Returns an unmodifiable type-specific sorted map backed by the given type-specific sorted map.
	 *
	 * @param m the sorted map to be wrapped in an unmodifiable sorted map.
	 * @return an unmodifiable view of the specified sorted map.
	 * @see java.util.Collections#unmodifiableSortedMap(SortedMap)
	 */
 public static Double2CharSortedMap unmodifiable( final Double2CharSortedMap m ) { return new UnmodifiableSortedMap ( m ); }
}
