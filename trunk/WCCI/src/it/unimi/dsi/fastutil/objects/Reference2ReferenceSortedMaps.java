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

public class Reference2ReferenceSortedMaps {

 private Reference2ReferenceSortedMaps() {}

 /** Returns a comparator for entries based on a given comparator on keys.
	 *
	 * @param comparator a comparator on keys.
	 * @return the associated comparator on entries.
	 */
 public static <K> Comparator<? super Map.Entry<K, ?>> entryComparator( final Comparator <K> comparator ) {
  return new Comparator<Map.Entry<K, ?>>() {
   public int compare( Map.Entry<K, ?> x, Map.Entry<K, ?> y ) {
    return comparator.compare( x.getKey(), y.getKey() );
   }
  };
 }


 /** An immutable class representing an empty type-specific sorted map. 
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific sorted map.
	 */

 public static class EmptySortedMap <K,V> extends Reference2ReferenceMaps.EmptyMap <K,V> implements Reference2ReferenceSortedMap <K,V>, java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected EmptySortedMap() {}

  public Comparator <? super K> comparator() { return null; }

  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Reference2ReferenceMap.Entry <K,V> > reference2ReferenceEntrySet() { return ObjectSortedSets.EMPTY_SET; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<K, V>> entrySet() { return ObjectSortedSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public ReferenceSortedSet <K> keySet() { return ReferenceSortedSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public Reference2ReferenceSortedMap <K,V> subMap( final K from, final K to ) { return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Reference2ReferenceSortedMap <K,V> headMap( final K to ) { return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Reference2ReferenceSortedMap <K,V> tailMap( final K from ) { return EMPTY_MAP; }

  public K firstKey() { throw new NoSuchElementException(); }
  public K lastKey() { throw new NoSuchElementException(); }
 }



 /** An empty type-specific sorted map (immutable). It is serializable and cloneable. */

 @SuppressWarnings("unchecked")
 public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();


 /** An immutable class representing a type-specific singleton sorted map. 
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific sorted map.
	 */

 public static class Singleton <K,V> extends Reference2ReferenceMaps.Singleton <K,V> implements Reference2ReferenceSortedMap <K,V>, java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Comparator <? super K> comparator;

  protected Singleton( final K key, final V value, Comparator <? super K> comparator ) {
   super( key, value );
   this.comparator = comparator;
  }

  protected Singleton( final K key, final V value ) {
   this( key, value, null );
  }

  @SuppressWarnings("unchecked")
  final int compare( final K k1, final K k2 ) {
   return comparator == null ? ( ((Comparable<K>)(k1)).compareTo(k2) ) : comparator.compare( k1, k2 );
  }

  public Comparator <? super K> comparator() { return comparator; }

  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Reference2ReferenceMap.Entry <K,V> > reference2ReferenceEntrySet() { if ( entries == null ) entries = ObjectSortedSets.singleton( (Reference2ReferenceMap.Entry <K,V>)new SingletonEntry(), (Comparator<? super Reference2ReferenceMap.Entry <K,V> >)entryComparator( comparator ) ); return (ObjectSortedSet<Reference2ReferenceMap.Entry <K,V> >)entries; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<K, V>> entrySet() { return (ObjectSortedSet)reference2ReferenceEntrySet(); }

  public ReferenceSortedSet <K> keySet() { if ( keys == null ) keys = ReferenceSortedSets.singleton( key, comparator ); return (ReferenceSortedSet <K>)keys; }

  @SuppressWarnings("unchecked")
  public Reference2ReferenceSortedMap <K,V> subMap( final K from, final K to ) { if ( compare( from, key ) <= 0 && compare( key, to ) < 0 ) return this; return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Reference2ReferenceSortedMap <K,V> headMap( final K to ) { if ( compare( key, to ) < 0 ) return this; return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Reference2ReferenceSortedMap <K,V> tailMap( final K from ) { if ( compare( from, key ) <= 0 ) return this; return EMPTY_MAP; }

  public K firstKey() { return key; }
  public K lastKey() { return key; }
 }

 /** Returns a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <P>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair <code>&lt;key,value></code>.
	 */

 public static <K,V> Reference2ReferenceSortedMap <K,V> singleton( final K key, V value ) {
  return new Singleton <K,V>( (key), (value) );
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

 public static <K,V> Reference2ReferenceSortedMap <K,V> singleton( final K key, V value, Comparator <? super K> comparator ) {
  return new Singleton <K,V>( (key), (value), comparator );
 }
  /** A synchronized wrapper class for sorted maps. */

 public static class SynchronizedSortedMap <K,V> extends Reference2ReferenceMaps.SynchronizedMap <K,V> implements Reference2ReferenceSortedMap <K,V>, java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Reference2ReferenceSortedMap <K,V> sortedMap;

  protected SynchronizedSortedMap( final Reference2ReferenceSortedMap <K,V> m, final Object sync ) {
   super( m, sync );
   sortedMap = m;
  }

  protected SynchronizedSortedMap( final Reference2ReferenceSortedMap <K,V> m ) {
   super( m );
   sortedMap = m;
  }

  public Comparator <? super K> comparator() { synchronized( sync ) { return sortedMap.comparator(); } }

  public ObjectSortedSet<Reference2ReferenceMap.Entry <K,V> > reference2ReferenceEntrySet() { if ( entries == null ) entries = ObjectSortedSets.synchronize( sortedMap.reference2ReferenceEntrySet(), sync ); return (ObjectSortedSet<Reference2ReferenceMap.Entry <K,V> >)entries; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<K, V>> entrySet() { return (ObjectSortedSet)reference2ReferenceEntrySet(); }
  public ReferenceSortedSet <K> keySet() { if ( keys == null ) keys = ReferenceSortedSets.synchronize( sortedMap.keySet(), sync ); return (ReferenceSortedSet <K>)keys; }

  public Reference2ReferenceSortedMap <K,V> subMap( final K from, final K to ) { return new SynchronizedSortedMap <K,V>( sortedMap.subMap( from, to ), sync ); }
  public Reference2ReferenceSortedMap <K,V> headMap( final K to ) { return new SynchronizedSortedMap <K,V>( sortedMap.headMap( to ), sync ); }
  public Reference2ReferenceSortedMap <K,V> tailMap( final K from ) { return new SynchronizedSortedMap <K,V>( sortedMap.tailMap( from ), sync ); }

  public K firstKey() { synchronized( sync ) { return sortedMap.firstKey(); } }
  public K lastKey() { synchronized( sync ) { return sortedMap.lastKey(); } }
 }

 /** Returns a synchronized type-specific sorted map backed by the given type-specific sorted map.
	 *
	 * @param m the sorted map to be wrapped in a synchronized sorted map.
	 * @return a synchronized view of the specified sorted map.
	 * @see java.util.Collections#synchronizedSortedMap(SortedMap)
	 */
 public static <K,V> Reference2ReferenceSortedMap <K,V> synchronize( final Reference2ReferenceSortedMap <K,V> m ) { return new SynchronizedSortedMap <K,V>( m ); }

 /** Returns a synchronized type-specific sorted map backed by the given type-specific sorted map, using an assigned object to synchronize.
	 *
	 * @param m the sorted map to be wrapped in a synchronized sorted map.
	 * @param sync an object that will be used to synchronize the access to the sorted sorted map.
	 * @return a synchronized view of the specified sorted map.
	 * @see java.util.Collections#synchronizedSortedMap(SortedMap)
	 */

 public static <K,V> Reference2ReferenceSortedMap <K,V> synchronize( final Reference2ReferenceSortedMap <K,V> m, final Object sync ) { return new SynchronizedSortedMap <K,V>( m, sync ); }




 /** An unmodifiable wrapper class for sorted maps. */

 public static class UnmodifiableSortedMap <K,V> extends Reference2ReferenceMaps.UnmodifiableMap <K,V> implements Reference2ReferenceSortedMap <K,V>, java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Reference2ReferenceSortedMap <K,V> sortedMap;

  protected UnmodifiableSortedMap( final Reference2ReferenceSortedMap <K,V> m ) {
   super( m );
   sortedMap = m;
  }

  public Comparator <? super K> comparator() { return sortedMap.comparator(); }

  public ObjectSortedSet<Reference2ReferenceMap.Entry <K,V> > reference2ReferenceEntrySet() { if ( entries == null ) entries = ObjectSortedSets.unmodifiable( sortedMap.reference2ReferenceEntrySet() ); return (ObjectSortedSet<Reference2ReferenceMap.Entry <K,V> >)entries; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<K, V>> entrySet() { return (ObjectSortedSet)reference2ReferenceEntrySet(); }
  public ReferenceSortedSet <K> keySet() { if ( keys == null ) keys = ReferenceSortedSets.unmodifiable( sortedMap.keySet() ); return (ReferenceSortedSet <K>)keys; }

  public Reference2ReferenceSortedMap <K,V> subMap( final K from, final K to ) { return new UnmodifiableSortedMap <K,V>( sortedMap.subMap( from, to ) ); }
  public Reference2ReferenceSortedMap <K,V> headMap( final K to ) { return new UnmodifiableSortedMap <K,V>( sortedMap.headMap( to ) ); }
  public Reference2ReferenceSortedMap <K,V> tailMap( final K from ) { return new UnmodifiableSortedMap <K,V>( sortedMap.tailMap( from ) ); }

  public K firstKey() { return sortedMap.firstKey(); }
  public K lastKey() { return sortedMap.lastKey(); }
 }

 /** Returns an unmodifiable type-specific sorted map backed by the given type-specific sorted map.
	 *
	 * @param m the sorted map to be wrapped in an unmodifiable sorted map.
	 * @return an unmodifiable view of the specified sorted map.
	 * @see java.util.Collections#unmodifiableSortedMap(SortedMap)
	 */
 public static <K,V> Reference2ReferenceSortedMap <K,V> unmodifiable( final Reference2ReferenceSortedMap <K,V> m ) { return new UnmodifiableSortedMap <K,V>( m ); }
}
