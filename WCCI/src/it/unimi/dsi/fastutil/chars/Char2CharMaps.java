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

import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.chars.CharCollections;

import it.unimi.dsi.fastutil.chars.CharSets;


import java.util.Map;

/** A class providing static methods and objects that do useful things with type-specific maps.
 *
 * @see it.unimi.dsi.fastutil.Maps
 * @see java.util.Collections
 */

public class Char2CharMaps {

 private Char2CharMaps() {}


 /** An immutable class representing an empty type-specific map.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific map.
	 */

 public static class EmptyMap extends Char2CharFunctions.EmptyFunction implements Char2CharMap , java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected EmptyMap() {}

  public boolean containsValue( final char v ) { return false; }

  public void putAll( final Map<? extends Character, ? extends Character> m ) { throw new UnsupportedOperationException(); }

  @SuppressWarnings("unchecked")
  public ObjectSet<Char2CharMap.Entry > char2CharEntrySet() { return ObjectSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public CharSet keySet() { return CharSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public CharCollection values() { return CharSets.EMPTY_SET; }


  public boolean containsValue( final Object ov ) { return false; }


        private Object readResolve() { return EMPTY_MAP; }

  public Object clone() { return EMPTY_MAP; }

  public boolean isEmpty() { return true; }

  @SuppressWarnings("unchecked")
  public ObjectSet<Map.Entry<Character, Character>> entrySet() { return (ObjectSet)char2CharEntrySet(); }

  public int hashCode() { return 0; }

  public boolean equals( final Object o ) {
   if ( ! ( o instanceof Map ) ) return false;

   return ((Map<?,?>)o).isEmpty();
  }

  public String toString() { return "{}"; }
 }



 /** An empty type-specific map (immutable). It is serializable and cloneable. */

 @SuppressWarnings("unchecked")
 public static final EmptyMap EMPTY_MAP = new EmptyMap();


 /** An immutable class representing a type-specific singleton map.	 
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific map.
	 */

 public static class Singleton extends Char2CharFunctions.Singleton implements Char2CharMap , java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected transient volatile ObjectSet<Char2CharMap.Entry > entries;
  protected transient volatile CharSet keys;
  protected transient volatile CharCollection values;

  protected Singleton( final char key, final char value ) {
   super( key, value );
  }

  public boolean containsValue( final char v ) { return ( (value) == (v) ); }

  public boolean containsValue( final Object ov ) { return ( (((((Character)(ov)).charValue()))) == (value) ); }


  public void putAll( final Map<? extends Character, ? extends Character> m ) { throw new UnsupportedOperationException(); }

  public ObjectSet<Char2CharMap.Entry > char2CharEntrySet() { if ( entries == null ) entries = ObjectSets.singleton( (Char2CharMap.Entry )new SingletonEntry() ); return entries; }
  public CharSet keySet() { if ( keys == null ) keys = CharSets.singleton( key ); return keys; }
  public CharCollection values() { if ( values == null ) values = CharSets.singleton( value ); return values; }

  protected class SingletonEntry implements Char2CharMap.Entry , Map.Entry<Character,Character> {
   public Character getKey() { return (Character.valueOf(Singleton.this.key)); }
   public Character getValue() { return (Character.valueOf(Singleton.this.value)); }


   public char getCharKey() { return Singleton.this.key; }



   public char getCharValue() { return Singleton.this.value; }
   public char setValue( final char value ) { throw new UnsupportedOperationException(); }


   public Character setValue( final Character value ) { throw new UnsupportedOperationException(); }

   public boolean equals( final Object o ) {
    if (!(o instanceof Map.Entry)) return false;
    Map.Entry<?,?> e = (Map.Entry<?,?>)o;

    return ( (Singleton.this.key) == (((((Character)(e.getKey())).charValue()))) ) && ( (Singleton.this.value) == (((((Character)(e.getValue())).charValue()))) );
   }

   public int hashCode() { return (Singleton.this.key) ^ (Singleton.this.value); }
   public String toString() { return Singleton.this.key + "->" + Singleton.this.value; }
  }

  public boolean isEmpty() { return false; }

  @SuppressWarnings("unchecked")
  public ObjectSet<Map.Entry<Character, Character>> entrySet() { return (ObjectSet)char2CharEntrySet(); }

  public int hashCode() { return (key) ^ (value); }

  public boolean equals( final Object o ) {
   if ( o == this ) return true;
   if ( ! ( o instanceof Map ) ) return false;

   Map<?,?> m = (Map<?,?>)o;
   if ( m.size() != 1 ) return false;
   return entrySet().iterator().next().equals( m.entrySet().iterator().next() );
  }

  public String toString() { return "{" + key + "=>" + value + "}"; }
 }

 /** Returns a type-specific immutable map containing only the specified pair. The returned map is serializable and cloneable.
	 *
	 * <P>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned map.
	 * @param value the only value of the returned map.
	 * @return a type-specific immutable map containing just the pair <code>&lt;key,value></code>.
	 */

 public static Char2CharMap singleton( final char key, char value ) {
  return new Singleton ( key, value );
 }



 /** Returns a type-specific immutable map containing only the specified pair. The returned map is serializable and cloneable.
	 *
	 * <P>Note that albeit the returned map is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned map.
	 * @param value the only value of the returned map.
	 * @return a type-specific immutable map containing just the pair <code>&lt;key,value></code>.
	 */

 public static Char2CharMap singleton( final Character key, final Character value ) {
  return new Singleton ( ((key).charValue()), ((value).charValue()) );
 }




 /** A synchronized wrapper class for maps. */

 public static class SynchronizedMap extends Char2CharFunctions.SynchronizedFunction implements Char2CharMap , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Char2CharMap map;

  protected transient volatile ObjectSet<Char2CharMap.Entry > entries;
  protected transient volatile CharSet keys;
  protected transient volatile CharCollection values;

  protected SynchronizedMap( final Char2CharMap m, final Object sync ) {
   super( m, sync );
   this.map = m;
  }

  protected SynchronizedMap( final Char2CharMap m ) {
   super( m );
   this.map = m;
  }

  public int size() { synchronized( sync ) { return map.size(); } }
  public boolean containsKey( final char k ) { synchronized( sync ) { return map.containsKey( k ); } }
  public boolean containsValue( final char v ) { synchronized( sync ) { return map.containsValue( v ); } }

  public char defaultReturnValue() { synchronized( sync ) { return map.defaultReturnValue(); } }
  public void defaultReturnValue( final char defRetValue ) { synchronized( sync ) { map.defaultReturnValue( defRetValue ); } }

  public char put( final char k, final char v ) { synchronized( sync ) { return map.put( k, v ); } }

  //public void putAll( final MAP KEY_VALUE_EXTENDS_GENERIC c ) { synchronized( sync ) { map.putAll( c ); } }
  public void putAll( final Map<? extends Character, ? extends Character> m ) { synchronized( sync ) { map.putAll( m ); } }

  public ObjectSet<Char2CharMap.Entry > char2CharEntrySet() { if ( entries == null ) entries = ObjectSets.synchronize( map.char2CharEntrySet(), sync ); return entries; }
  public CharSet keySet() { if ( keys == null ) keys = CharSets.synchronize( map.keySet(), sync ); return keys; }
  public CharCollection values() { if ( values == null ) return CharCollections.synchronize( map.values(), sync ); return values; }

  public void clear() { synchronized( sync ) { map.clear(); } }
  public String toString() { synchronized( sync ) { return map.toString(); } }


  public Character put( final Character k, final Character v ) { synchronized( sync ) { return map.put( k, v ); } }



  public char remove( final char k ) { synchronized( sync ) { return map.remove( k ); } }
  public char get( final char k ) { synchronized( sync ) { return map.get( k ); } }
  public boolean containsKey( final Object ok ) { synchronized( sync ) { return map.containsKey( ok ); } }



  public boolean containsValue( final Object ov ) { synchronized( sync ) { return map.containsValue( ov ); } }







  public boolean isEmpty() { synchronized( sync ) { return map.isEmpty(); } }
  public ObjectSet<Map.Entry<Character, Character>> entrySet() { synchronized( sync ) { return map.entrySet(); } }

  public int hashCode() { synchronized( sync ) { return map.hashCode(); } }
  public boolean equals( final Object o ) { synchronized( sync ) { return map.equals( o ); } }
 }

 /** Returns a synchronized type-specific map backed by the given type-specific map.
	 *
	 * @param m the map to be wrapped in a synchronized map.
	 * @return a synchronized view of the specified map.
	 * @see java.util.Collections#synchronizedMap(Map)
	 */
 public static Char2CharMap synchronize( final Char2CharMap m ) { return new SynchronizedMap ( m ); }

 /** Returns a synchronized type-specific map backed by the given type-specific map, using an assigned object to synchronize.
	 *
	 * @param m the map to be wrapped in a synchronized map.
	 * @param sync an object that will be used to synchronize the access to the map.
	 * @return a synchronized view of the specified map.
	 * @see java.util.Collections#synchronizedMap(Map)
	 */

 public static Char2CharMap synchronize( final Char2CharMap m, final Object sync ) { return new SynchronizedMap ( m, sync ); }



 /** An unmodifiable wrapper class for maps. */

 public static class UnmodifiableMap extends Char2CharFunctions.UnmodifiableFunction implements Char2CharMap , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Char2CharMap map;

  protected transient volatile ObjectSet<Char2CharMap.Entry > entries;
  protected transient volatile CharSet keys;
  protected transient volatile CharCollection values;

  protected UnmodifiableMap( final Char2CharMap m ) {
   super( m );
   this.map = m;
  }

  public int size() { return map.size(); }
  public boolean containsKey( final char k ) { return map.containsKey( k ); }
  public boolean containsValue( final char v ) { return map.containsValue( v ); }

  public char defaultReturnValue() { throw new UnsupportedOperationException(); }
  public void defaultReturnValue( final char defRetValue ) { throw new UnsupportedOperationException(); }

  public char put( final char k, final char v ) { throw new UnsupportedOperationException(); }

  //public void putAll( final MAP KEY_VALUE_EXTENDS_GENERIC c ) { throw new UnsupportedOperationException(); }
  public void putAll( final Map<? extends Character, ? extends Character> m ) { throw new UnsupportedOperationException(); }

  public ObjectSet<Char2CharMap.Entry > char2CharEntrySet() { if ( entries == null ) entries = ObjectSets.unmodifiable( map.char2CharEntrySet() ); return entries; }
  public CharSet keySet() { if ( keys == null ) keys = CharSets.unmodifiable( map.keySet() ); return keys; }
  public CharCollection values() { if ( values == null ) return CharCollections.unmodifiable( map.values() ); return values; }

  public void clear() { throw new UnsupportedOperationException(); }
  public String toString() { return map.toString(); }


  public Character put( final Character k, final Character v ) { throw new UnsupportedOperationException(); }



  public char remove( final char k ) { throw new UnsupportedOperationException(); }
  public char get( final char k ) { return map.get( k ); }
  public boolean containsKey( final Object ok ) { return map.containsKey( ok ); }



  public boolean containsValue( final Object ov ) { return map.containsValue( ov ); }







  public boolean isEmpty() { return map.isEmpty(); }
  public ObjectSet<Map.Entry<Character, Character>> entrySet() { return ObjectSets.unmodifiable( map.entrySet() ); }
 }

 /** Returns an unmodifiable type-specific map backed by the given type-specific map.
	 *
	 * @param m the map to be wrapped in an unmodifiable map.
	 * @return an unmodifiable view of the specified map.
	 * @see java.util.Collections#unmodifiableMap(Map)
	 */
 public static Char2CharMap unmodifiable( final Char2CharMap m ) { return new UnmodifiableMap ( m ); }

}
