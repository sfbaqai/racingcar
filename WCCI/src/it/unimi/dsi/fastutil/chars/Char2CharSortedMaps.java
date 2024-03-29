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

package it.unimi.dsi.fastutil.chars;

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

public class Char2CharSortedMaps {

 private Char2CharSortedMaps() {}

 /** Returns a comparator for entries based on a given comparator on keys.
	 *
	 * @param comparator a comparator on keys.
	 * @return the associated comparator on entries.
	 */
 public static Comparator<? super Map.Entry<Character, ?>> entryComparator( final CharComparator comparator ) {
  return new Comparator<Map.Entry<Character, ?>>() {
   public int compare( Map.Entry<Character, ?> x, Map.Entry<Character, ?> y ) {
    return comparator.compare( x.getKey(), y.getKey() );
   }
  };
 }


 /** An immutable class representing an empty type-specific sorted map. 
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific sorted map.
	 */

 public static class EmptySortedMap extends Char2CharMaps.EmptyMap implements Char2CharSortedMap , java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected EmptySortedMap() {}

  public CharComparator comparator() { return null; }

  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Char2CharMap.Entry > char2CharEntrySet() { return ObjectSortedSets.EMPTY_SET; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<Character, Character>> entrySet() { return ObjectSortedSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public CharSortedSet keySet() { return CharSortedSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public Char2CharSortedMap subMap( final char from, final char to ) { return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Char2CharSortedMap headMap( final char to ) { return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Char2CharSortedMap tailMap( final char from ) { return EMPTY_MAP; }

  public char firstCharKey() { throw new NoSuchElementException(); }
  public char lastCharKey() { throw new NoSuchElementException(); }


  public Char2CharSortedMap headMap( Character oto ) { return headMap( ((oto).charValue()) ); }
  public Char2CharSortedMap tailMap( Character ofrom ) { return tailMap( ((ofrom).charValue()) ); }
  public Char2CharSortedMap subMap( Character ofrom, Character oto ) { return subMap( ((ofrom).charValue()), ((oto).charValue()) ); }

  public Character firstKey() { return (Character.valueOf(firstCharKey())); }
  public Character lastKey() { return (Character.valueOf(lastCharKey())); }


 }



 /** An empty type-specific sorted map (immutable). It is serializable and cloneable. */

 @SuppressWarnings("unchecked")
 public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();


 /** An immutable class representing a type-specific singleton sorted map. 
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific sorted map.
	 */

 public static class Singleton extends Char2CharMaps.Singleton implements Char2CharSortedMap , java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final CharComparator comparator;

  protected Singleton( final char key, final char value, CharComparator comparator ) {
   super( key, value );
   this.comparator = comparator;
  }

  protected Singleton( final char key, final char value ) {
   this( key, value, null );
  }

  @SuppressWarnings("unchecked")
  final int compare( final char k1, final char k2 ) {
   return comparator == null ? ( (k1) < (k2) ? -1 : ( (k1) == (k2) ? 0 : 1 ) ) : comparator.compare( k1, k2 );
  }

  public CharComparator comparator() { return comparator; }

  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Char2CharMap.Entry > char2CharEntrySet() { if ( entries == null ) entries = ObjectSortedSets.singleton( (Char2CharMap.Entry )new SingletonEntry(), (Comparator<? super Char2CharMap.Entry >)entryComparator( comparator ) ); return (ObjectSortedSet<Char2CharMap.Entry >)entries; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<Character, Character>> entrySet() { return (ObjectSortedSet)char2CharEntrySet(); }

  public CharSortedSet keySet() { if ( keys == null ) keys = CharSortedSets.singleton( key, comparator ); return (CharSortedSet )keys; }

  @SuppressWarnings("unchecked")
  public Char2CharSortedMap subMap( final char from, final char to ) { if ( compare( from, key ) <= 0 && compare( key, to ) < 0 ) return this; return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Char2CharSortedMap headMap( final char to ) { if ( compare( key, to ) < 0 ) return this; return EMPTY_MAP; }

  @SuppressWarnings("unchecked")
  public Char2CharSortedMap tailMap( final char from ) { if ( compare( from, key ) <= 0 ) return this; return EMPTY_MAP; }

  public char firstCharKey() { return key; }
  public char lastCharKey() { return key; }


  public Char2CharSortedMap headMap( Character oto ) { return headMap( ((oto).charValue()) ); }
  public Char2CharSortedMap tailMap( Character ofrom ) { return tailMap( ((ofrom).charValue()) ); }
  public Char2CharSortedMap subMap( Character ofrom, Character oto ) { return subMap( ((ofrom).charValue()), ((oto).charValue()) ); }

  public Character firstKey() { return (Character.valueOf(firstCharKey())); }
  public Character lastKey() { return (Character.valueOf(lastCharKey())); }

 }

 /** Returns a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <P>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair <code>&lt;key,value></code>.
	 */

 public static Char2CharSortedMap singleton( final Character key, Character value ) {
  return new Singleton ( ((key).charValue()), ((value).charValue()) );
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

 public static Char2CharSortedMap singleton( final Character key, Character value, CharComparator comparator ) {
  return new Singleton ( ((key).charValue()), ((value).charValue()), comparator );
 }



 /** Returns a type-specific immutable sorted map containing only the specified pair. The returned sorted map is serializable and cloneable.
	 *
	 * <P>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned sorted map.
	 * @param value the only value of the returned sorted map.
	 * @return a type-specific immutable sorted map containing just the pair <code>&lt;key,value></code>.
	 */

 public static Char2CharSortedMap singleton( final char key, final char value ) {
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

 public static Char2CharSortedMap singleton( final char key, final char value, CharComparator comparator ) {
  return new Singleton ( key, value, comparator );
 }




  /** A synchronized wrapper class for sorted maps. */

 public static class SynchronizedSortedMap extends Char2CharMaps.SynchronizedMap implements Char2CharSortedMap , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Char2CharSortedMap sortedMap;

  protected SynchronizedSortedMap( final Char2CharSortedMap m, final Object sync ) {
   super( m, sync );
   sortedMap = m;
  }

  protected SynchronizedSortedMap( final Char2CharSortedMap m ) {
   super( m );
   sortedMap = m;
  }

  public CharComparator comparator() { synchronized( sync ) { return sortedMap.comparator(); } }

  public ObjectSortedSet<Char2CharMap.Entry > char2CharEntrySet() { if ( entries == null ) entries = ObjectSortedSets.synchronize( sortedMap.char2CharEntrySet(), sync ); return (ObjectSortedSet<Char2CharMap.Entry >)entries; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<Character, Character>> entrySet() { return (ObjectSortedSet)char2CharEntrySet(); }
  public CharSortedSet keySet() { if ( keys == null ) keys = CharSortedSets.synchronize( sortedMap.keySet(), sync ); return (CharSortedSet )keys; }

  public Char2CharSortedMap subMap( final char from, final char to ) { return new SynchronizedSortedMap ( sortedMap.subMap( from, to ), sync ); }
  public Char2CharSortedMap headMap( final char to ) { return new SynchronizedSortedMap ( sortedMap.headMap( to ), sync ); }
  public Char2CharSortedMap tailMap( final char from ) { return new SynchronizedSortedMap ( sortedMap.tailMap( from ), sync ); }

  public char firstCharKey() { synchronized( sync ) { return sortedMap.firstCharKey(); } }
  public char lastCharKey() { synchronized( sync ) { return sortedMap.lastCharKey(); } }


  public Character firstKey() { synchronized( sync ) { return sortedMap.firstKey(); } }
  public Character lastKey() { synchronized( sync ) { return sortedMap.lastKey(); } }

  public Char2CharSortedMap subMap( final Character from, final Character to ) { return new SynchronizedSortedMap ( sortedMap.subMap( from, to ), sync ); }
  public Char2CharSortedMap headMap( final Character to ) { return new SynchronizedSortedMap ( sortedMap.headMap( to ), sync ); }
  public Char2CharSortedMap tailMap( final Character from ) { return new SynchronizedSortedMap ( sortedMap.tailMap( from ), sync ); }



 }

 /** Returns a synchronized type-specific sorted map backed by the given type-specific sorted map.
	 *
	 * @param m the sorted map to be wrapped in a synchronized sorted map.
	 * @return a synchronized view of the specified sorted map.
	 * @see java.util.Collections#synchronizedSortedMap(SortedMap)
	 */
 public static Char2CharSortedMap synchronize( final Char2CharSortedMap m ) { return new SynchronizedSortedMap ( m ); }

 /** Returns a synchronized type-specific sorted map backed by the given type-specific sorted map, using an assigned object to synchronize.
	 *
	 * @param m the sorted map to be wrapped in a synchronized sorted map.
	 * @param sync an object that will be used to synchronize the access to the sorted sorted map.
	 * @return a synchronized view of the specified sorted map.
	 * @see java.util.Collections#synchronizedSortedMap(SortedMap)
	 */

 public static Char2CharSortedMap synchronize( final Char2CharSortedMap m, final Object sync ) { return new SynchronizedSortedMap ( m, sync ); }




 /** An unmodifiable wrapper class for sorted maps. */

 public static class UnmodifiableSortedMap extends Char2CharMaps.UnmodifiableMap implements Char2CharSortedMap , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Char2CharSortedMap sortedMap;

  protected UnmodifiableSortedMap( final Char2CharSortedMap m ) {
   super( m );
   sortedMap = m;
  }

  public CharComparator comparator() { return sortedMap.comparator(); }

  public ObjectSortedSet<Char2CharMap.Entry > char2CharEntrySet() { if ( entries == null ) entries = ObjectSortedSets.unmodifiable( sortedMap.char2CharEntrySet() ); return (ObjectSortedSet<Char2CharMap.Entry >)entries; }
  @SuppressWarnings("unchecked")
  public ObjectSortedSet<Map.Entry<Character, Character>> entrySet() { return (ObjectSortedSet)char2CharEntrySet(); }
  public CharSortedSet keySet() { if ( keys == null ) keys = CharSortedSets.unmodifiable( sortedMap.keySet() ); return (CharSortedSet )keys; }

  public Char2CharSortedMap subMap( final char from, final char to ) { return new UnmodifiableSortedMap ( sortedMap.subMap( from, to ) ); }
  public Char2CharSortedMap headMap( final char to ) { return new UnmodifiableSortedMap ( sortedMap.headMap( to ) ); }
  public Char2CharSortedMap tailMap( final char from ) { return new UnmodifiableSortedMap ( sortedMap.tailMap( from ) ); }

  public char firstCharKey() { return sortedMap.firstCharKey(); }
  public char lastCharKey() { return sortedMap.lastCharKey(); }


  public Character firstKey() { return sortedMap.firstKey(); }
  public Character lastKey() { return sortedMap.lastKey(); }

  public Char2CharSortedMap subMap( final Character from, final Character to ) { return new UnmodifiableSortedMap ( sortedMap.subMap( from, to ) ); }
  public Char2CharSortedMap headMap( final Character to ) { return new UnmodifiableSortedMap ( sortedMap.headMap( to ) ); }
  public Char2CharSortedMap tailMap( final Character from ) { return new UnmodifiableSortedMap ( sortedMap.tailMap( from ) ); }



 }

 /** Returns an unmodifiable type-specific sorted map backed by the given type-specific sorted map.
	 *
	 * @param m the sorted map to be wrapped in an unmodifiable sorted map.
	 * @return an unmodifiable view of the specified sorted map.
	 * @see java.util.Collections#unmodifiableSortedMap(SortedMap)
	 */
 public static Char2CharSortedMap unmodifiable( final Char2CharSortedMap m ) { return new UnmodifiableSortedMap ( m ); }
}
