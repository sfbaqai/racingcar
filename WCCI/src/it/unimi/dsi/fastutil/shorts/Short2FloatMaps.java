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

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.floats.FloatCollections;

import it.unimi.dsi.fastutil.floats.FloatSets;


import java.util.Map;

/** A class providing static methods and objects that do useful things with type-specific maps.
 *
 * @see it.unimi.dsi.fastutil.Maps
 * @see java.util.Collections
 */

public class Short2FloatMaps {

 private Short2FloatMaps() {}


 /** An immutable class representing an empty type-specific map.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific map.
	 */

 public static class EmptyMap extends Short2FloatFunctions.EmptyFunction implements Short2FloatMap , java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected EmptyMap() {}

  public boolean containsValue( final float v ) { return false; }

  public void putAll( final Map<? extends Short, ? extends Float> m ) { throw new UnsupportedOperationException(); }

  @SuppressWarnings("unchecked")
  public ObjectSet<Short2FloatMap.Entry > short2FloatEntrySet() { return ObjectSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public ShortSet keySet() { return ShortSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public FloatCollection values() { return FloatSets.EMPTY_SET; }


  public boolean containsValue( final Object ov ) { return false; }


        private Object readResolve() { return EMPTY_MAP; }

  public Object clone() { return EMPTY_MAP; }

  public boolean isEmpty() { return true; }

  @SuppressWarnings("unchecked")
  public ObjectSet<Map.Entry<Short, Float>> entrySet() { return (ObjectSet)short2FloatEntrySet(); }

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

 public static class Singleton extends Short2FloatFunctions.Singleton implements Short2FloatMap , java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected transient volatile ObjectSet<Short2FloatMap.Entry > entries;
  protected transient volatile ShortSet keys;
  protected transient volatile FloatCollection values;

  protected Singleton( final short key, final float value ) {
   super( key, value );
  }

  public boolean containsValue( final float v ) { return ( (value) == (v) ); }

  public boolean containsValue( final Object ov ) { return ( (((((Float)(ov)).floatValue()))) == (value) ); }


  public void putAll( final Map<? extends Short, ? extends Float> m ) { throw new UnsupportedOperationException(); }

  public ObjectSet<Short2FloatMap.Entry > short2FloatEntrySet() { if ( entries == null ) entries = ObjectSets.singleton( (Short2FloatMap.Entry )new SingletonEntry() ); return entries; }
  public ShortSet keySet() { if ( keys == null ) keys = ShortSets.singleton( key ); return keys; }
  public FloatCollection values() { if ( values == null ) values = FloatSets.singleton( value ); return values; }

  protected class SingletonEntry implements Short2FloatMap.Entry , Map.Entry<Short,Float> {
   public Short getKey() { return (Short.valueOf(Singleton.this.key)); }
   public Float getValue() { return (Float.valueOf(Singleton.this.value)); }


   public short getShortKey() { return Singleton.this.key; }



   public float getFloatValue() { return Singleton.this.value; }
   public float setValue( final float value ) { throw new UnsupportedOperationException(); }


   public Float setValue( final Float value ) { throw new UnsupportedOperationException(); }

   public boolean equals( final Object o ) {
    if (!(o instanceof Map.Entry)) return false;
    Map.Entry<?,?> e = (Map.Entry<?,?>)o;

    return ( (Singleton.this.key) == (((((Short)(e.getKey())).shortValue()))) ) && ( (Singleton.this.value) == (((((Float)(e.getValue())).floatValue()))) );
   }

   public int hashCode() { return (Singleton.this.key) ^ it.unimi.dsi.fastutil.HashCommon.float2int(Singleton.this.value); }
   public String toString() { return Singleton.this.key + "->" + Singleton.this.value; }
  }

  public boolean isEmpty() { return false; }

  @SuppressWarnings("unchecked")
  public ObjectSet<Map.Entry<Short, Float>> entrySet() { return (ObjectSet)short2FloatEntrySet(); }

  public int hashCode() { return (key) ^ it.unimi.dsi.fastutil.HashCommon.float2int(value); }

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

 public static Short2FloatMap singleton( final short key, float value ) {
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

 public static Short2FloatMap singleton( final Short key, final Float value ) {
  return new Singleton ( ((key).shortValue()), ((value).floatValue()) );
 }




 /** A synchronized wrapper class for maps. */

 public static class SynchronizedMap extends Short2FloatFunctions.SynchronizedFunction implements Short2FloatMap , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Short2FloatMap map;

  protected transient volatile ObjectSet<Short2FloatMap.Entry > entries;
  protected transient volatile ShortSet keys;
  protected transient volatile FloatCollection values;

  protected SynchronizedMap( final Short2FloatMap m, final Object sync ) {
   super( m, sync );
   this.map = m;
  }

  protected SynchronizedMap( final Short2FloatMap m ) {
   super( m );
   this.map = m;
  }

  public int size() { synchronized( sync ) { return map.size(); } }
  public boolean containsKey( final short k ) { synchronized( sync ) { return map.containsKey( k ); } }
  public boolean containsValue( final float v ) { synchronized( sync ) { return map.containsValue( v ); } }

  public float defaultReturnValue() { synchronized( sync ) { return map.defaultReturnValue(); } }
  public void defaultReturnValue( final float defRetValue ) { synchronized( sync ) { map.defaultReturnValue( defRetValue ); } }

  public float put( final short k, final float v ) { synchronized( sync ) { return map.put( k, v ); } }

  //public void putAll( final MAP KEY_VALUE_EXTENDS_GENERIC c ) { synchronized( sync ) { map.putAll( c ); } }
  public void putAll( final Map<? extends Short, ? extends Float> m ) { synchronized( sync ) { map.putAll( m ); } }

  public ObjectSet<Short2FloatMap.Entry > short2FloatEntrySet() { if ( entries == null ) entries = ObjectSets.synchronize( map.short2FloatEntrySet(), sync ); return entries; }
  public ShortSet keySet() { if ( keys == null ) keys = ShortSets.synchronize( map.keySet(), sync ); return keys; }
  public FloatCollection values() { if ( values == null ) return FloatCollections.synchronize( map.values(), sync ); return values; }

  public void clear() { synchronized( sync ) { map.clear(); } }
  public String toString() { synchronized( sync ) { return map.toString(); } }


  public Float put( final Short k, final Float v ) { synchronized( sync ) { return map.put( k, v ); } }



  public float remove( final short k ) { synchronized( sync ) { return map.remove( k ); } }
  public float get( final short k ) { synchronized( sync ) { return map.get( k ); } }
  public boolean containsKey( final Object ok ) { synchronized( sync ) { return map.containsKey( ok ); } }



  public boolean containsValue( final Object ov ) { synchronized( sync ) { return map.containsValue( ov ); } }







  public boolean isEmpty() { synchronized( sync ) { return map.isEmpty(); } }
  public ObjectSet<Map.Entry<Short, Float>> entrySet() { synchronized( sync ) { return map.entrySet(); } }

  public int hashCode() { synchronized( sync ) { return map.hashCode(); } }
  public boolean equals( final Object o ) { synchronized( sync ) { return map.equals( o ); } }
 }

 /** Returns a synchronized type-specific map backed by the given type-specific map.
	 *
	 * @param m the map to be wrapped in a synchronized map.
	 * @return a synchronized view of the specified map.
	 * @see java.util.Collections#synchronizedMap(Map)
	 */
 public static Short2FloatMap synchronize( final Short2FloatMap m ) { return new SynchronizedMap ( m ); }

 /** Returns a synchronized type-specific map backed by the given type-specific map, using an assigned object to synchronize.
	 *
	 * @param m the map to be wrapped in a synchronized map.
	 * @param sync an object that will be used to synchronize the access to the map.
	 * @return a synchronized view of the specified map.
	 * @see java.util.Collections#synchronizedMap(Map)
	 */

 public static Short2FloatMap synchronize( final Short2FloatMap m, final Object sync ) { return new SynchronizedMap ( m, sync ); }



 /** An unmodifiable wrapper class for maps. */

 public static class UnmodifiableMap extends Short2FloatFunctions.UnmodifiableFunction implements Short2FloatMap , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Short2FloatMap map;

  protected transient volatile ObjectSet<Short2FloatMap.Entry > entries;
  protected transient volatile ShortSet keys;
  protected transient volatile FloatCollection values;

  protected UnmodifiableMap( final Short2FloatMap m ) {
   super( m );
   this.map = m;
  }

  public int size() { return map.size(); }
  public boolean containsKey( final short k ) { return map.containsKey( k ); }
  public boolean containsValue( final float v ) { return map.containsValue( v ); }

  public float defaultReturnValue() { throw new UnsupportedOperationException(); }
  public void defaultReturnValue( final float defRetValue ) { throw new UnsupportedOperationException(); }

  public float put( final short k, final float v ) { throw new UnsupportedOperationException(); }

  //public void putAll( final MAP KEY_VALUE_EXTENDS_GENERIC c ) { throw new UnsupportedOperationException(); }
  public void putAll( final Map<? extends Short, ? extends Float> m ) { throw new UnsupportedOperationException(); }

  public ObjectSet<Short2FloatMap.Entry > short2FloatEntrySet() { if ( entries == null ) entries = ObjectSets.unmodifiable( map.short2FloatEntrySet() ); return entries; }
  public ShortSet keySet() { if ( keys == null ) keys = ShortSets.unmodifiable( map.keySet() ); return keys; }
  public FloatCollection values() { if ( values == null ) return FloatCollections.unmodifiable( map.values() ); return values; }

  public void clear() { throw new UnsupportedOperationException(); }
  public String toString() { return map.toString(); }


  public Float put( final Short k, final Float v ) { throw new UnsupportedOperationException(); }



  public float remove( final short k ) { throw new UnsupportedOperationException(); }
  public float get( final short k ) { return map.get( k ); }
  public boolean containsKey( final Object ok ) { return map.containsKey( ok ); }



  public boolean containsValue( final Object ov ) { return map.containsValue( ov ); }







  public boolean isEmpty() { return map.isEmpty(); }
  public ObjectSet<Map.Entry<Short, Float>> entrySet() { return ObjectSets.unmodifiable( map.entrySet() ); }
 }

 /** Returns an unmodifiable type-specific map backed by the given type-specific map.
	 *
	 * @param m the map to be wrapped in an unmodifiable map.
	 * @return an unmodifiable view of the specified map.
	 * @see java.util.Collections#unmodifiableMap(Map)
	 */
 public static Short2FloatMap unmodifiable( final Short2FloatMap m ) { return new UnmodifiableMap ( m ); }

}
