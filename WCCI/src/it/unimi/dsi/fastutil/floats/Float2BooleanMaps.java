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

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.booleans.BooleanCollections;

import it.unimi.dsi.fastutil.booleans.BooleanSets;


import java.util.Map;

/** A class providing static methods and objects that do useful things with type-specific maps.
 *
 * @see it.unimi.dsi.fastutil.Maps
 * @see java.util.Collections
 */

public class Float2BooleanMaps {

 private Float2BooleanMaps() {}


 /** An immutable class representing an empty type-specific map.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific map.
	 */

 public static class EmptyMap extends Float2BooleanFunctions.EmptyFunction implements Float2BooleanMap , java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected EmptyMap() {}

  public boolean containsValue( final boolean v ) { return false; }

  public void putAll( final Map<? extends Float, ? extends Boolean> m ) { throw new UnsupportedOperationException(); }

  @SuppressWarnings("unchecked")
  public ObjectSet<Float2BooleanMap.Entry > float2BooleanEntrySet() { return ObjectSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public FloatSet keySet() { return FloatSets.EMPTY_SET; }

  @SuppressWarnings("unchecked")
  public BooleanCollection values() { return BooleanSets.EMPTY_SET; }


  public boolean containsValue( final Object ov ) { return false; }


        private Object readResolve() { return EMPTY_MAP; }

  public Object clone() { return EMPTY_MAP; }

  public boolean isEmpty() { return true; }

  @SuppressWarnings("unchecked")
  public ObjectSet<Map.Entry<Float, Boolean>> entrySet() { return (ObjectSet)float2BooleanEntrySet(); }

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

 public static class Singleton extends Float2BooleanFunctions.Singleton implements Float2BooleanMap , java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected transient volatile ObjectSet<Float2BooleanMap.Entry > entries;
  protected transient volatile FloatSet keys;
  protected transient volatile BooleanCollection values;

  protected Singleton( final float key, final boolean value ) {
   super( key, value );
  }

  public boolean containsValue( final boolean v ) { return ( (value) == (v) ); }

  public boolean containsValue( final Object ov ) { return ( (((((Boolean)(ov)).booleanValue()))) == (value) ); }


  public void putAll( final Map<? extends Float, ? extends Boolean> m ) { throw new UnsupportedOperationException(); }

  public ObjectSet<Float2BooleanMap.Entry > float2BooleanEntrySet() { if ( entries == null ) entries = ObjectSets.singleton( (Float2BooleanMap.Entry )new SingletonEntry() ); return entries; }
  public FloatSet keySet() { if ( keys == null ) keys = FloatSets.singleton( key ); return keys; }
  public BooleanCollection values() { if ( values == null ) values = BooleanSets.singleton( value ); return values; }

  protected class SingletonEntry implements Float2BooleanMap.Entry , Map.Entry<Float,Boolean> {
   public Float getKey() { return (Float.valueOf(Singleton.this.key)); }
   public Boolean getValue() { return (Boolean.valueOf(Singleton.this.value)); }


   public float getFloatKey() { return Singleton.this.key; }



   public boolean getBooleanValue() { return Singleton.this.value; }
   public boolean setValue( final boolean value ) { throw new UnsupportedOperationException(); }


   public Boolean setValue( final Boolean value ) { throw new UnsupportedOperationException(); }

   public boolean equals( final Object o ) {
    if (!(o instanceof Map.Entry)) return false;
    Map.Entry<?,?> e = (Map.Entry<?,?>)o;

    return ( (Singleton.this.key) == (((((Float)(e.getKey())).floatValue()))) ) && ( (Singleton.this.value) == (((((Boolean)(e.getValue())).booleanValue()))) );
   }

   public int hashCode() { return it.unimi.dsi.fastutil.HashCommon.float2int(Singleton.this.key) ^ (Singleton.this.value ? 1231 : 1237); }
   public String toString() { return Singleton.this.key + "->" + Singleton.this.value; }
  }

  public boolean isEmpty() { return false; }

  @SuppressWarnings("unchecked")
  public ObjectSet<Map.Entry<Float, Boolean>> entrySet() { return (ObjectSet)float2BooleanEntrySet(); }

  public int hashCode() { return it.unimi.dsi.fastutil.HashCommon.float2int(key) ^ (value ? 1231 : 1237); }

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

 public static Float2BooleanMap singleton( final float key, boolean value ) {
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

 public static Float2BooleanMap singleton( final Float key, final Boolean value ) {
  return new Singleton ( ((key).floatValue()), ((value).booleanValue()) );
 }




 /** A synchronized wrapper class for maps. */

 public static class SynchronizedMap extends Float2BooleanFunctions.SynchronizedFunction implements Float2BooleanMap , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Float2BooleanMap map;

  protected transient volatile ObjectSet<Float2BooleanMap.Entry > entries;
  protected transient volatile FloatSet keys;
  protected transient volatile BooleanCollection values;

  protected SynchronizedMap( final Float2BooleanMap m, final Object sync ) {
   super( m, sync );
   this.map = m;
  }

  protected SynchronizedMap( final Float2BooleanMap m ) {
   super( m );
   this.map = m;
  }

  public int size() { synchronized( sync ) { return map.size(); } }
  public boolean containsKey( final float k ) { synchronized( sync ) { return map.containsKey( k ); } }
  public boolean containsValue( final boolean v ) { synchronized( sync ) { return map.containsValue( v ); } }

  public boolean defaultReturnValue() { synchronized( sync ) { return map.defaultReturnValue(); } }
  public void defaultReturnValue( final boolean defRetValue ) { synchronized( sync ) { map.defaultReturnValue( defRetValue ); } }

  public boolean put( final float k, final boolean v ) { synchronized( sync ) { return map.put( k, v ); } }

  //public void putAll( final MAP KEY_VALUE_EXTENDS_GENERIC c ) { synchronized( sync ) { map.putAll( c ); } }
  public void putAll( final Map<? extends Float, ? extends Boolean> m ) { synchronized( sync ) { map.putAll( m ); } }

  public ObjectSet<Float2BooleanMap.Entry > float2BooleanEntrySet() { if ( entries == null ) entries = ObjectSets.synchronize( map.float2BooleanEntrySet(), sync ); return entries; }
  public FloatSet keySet() { if ( keys == null ) keys = FloatSets.synchronize( map.keySet(), sync ); return keys; }
  public BooleanCollection values() { if ( values == null ) return BooleanCollections.synchronize( map.values(), sync ); return values; }

  public void clear() { synchronized( sync ) { map.clear(); } }
  public String toString() { synchronized( sync ) { return map.toString(); } }


  public Boolean put( final Float k, final Boolean v ) { synchronized( sync ) { return map.put( k, v ); } }



  public boolean remove( final float k ) { synchronized( sync ) { return map.remove( k ); } }
  public boolean get( final float k ) { synchronized( sync ) { return map.get( k ); } }
  public boolean containsKey( final Object ok ) { synchronized( sync ) { return map.containsKey( ok ); } }



  public boolean containsValue( final Object ov ) { synchronized( sync ) { return map.containsValue( ov ); } }







  public boolean isEmpty() { synchronized( sync ) { return map.isEmpty(); } }
  public ObjectSet<Map.Entry<Float, Boolean>> entrySet() { synchronized( sync ) { return map.entrySet(); } }

  public int hashCode() { synchronized( sync ) { return map.hashCode(); } }
  public boolean equals( final Object o ) { synchronized( sync ) { return map.equals( o ); } }
 }

 /** Returns a synchronized type-specific map backed by the given type-specific map.
	 *
	 * @param m the map to be wrapped in a synchronized map.
	 * @return a synchronized view of the specified map.
	 * @see java.util.Collections#synchronizedMap(Map)
	 */
 public static Float2BooleanMap synchronize( final Float2BooleanMap m ) { return new SynchronizedMap ( m ); }

 /** Returns a synchronized type-specific map backed by the given type-specific map, using an assigned object to synchronize.
	 *
	 * @param m the map to be wrapped in a synchronized map.
	 * @param sync an object that will be used to synchronize the access to the map.
	 * @return a synchronized view of the specified map.
	 * @see java.util.Collections#synchronizedMap(Map)
	 */

 public static Float2BooleanMap synchronize( final Float2BooleanMap m, final Object sync ) { return new SynchronizedMap ( m, sync ); }



 /** An unmodifiable wrapper class for maps. */

 public static class UnmodifiableMap extends Float2BooleanFunctions.UnmodifiableFunction implements Float2BooleanMap , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Float2BooleanMap map;

  protected transient volatile ObjectSet<Float2BooleanMap.Entry > entries;
  protected transient volatile FloatSet keys;
  protected transient volatile BooleanCollection values;

  protected UnmodifiableMap( final Float2BooleanMap m ) {
   super( m );
   this.map = m;
  }

  public int size() { return map.size(); }
  public boolean containsKey( final float k ) { return map.containsKey( k ); }
  public boolean containsValue( final boolean v ) { return map.containsValue( v ); }

  public boolean defaultReturnValue() { throw new UnsupportedOperationException(); }
  public void defaultReturnValue( final boolean defRetValue ) { throw new UnsupportedOperationException(); }

  public boolean put( final float k, final boolean v ) { throw new UnsupportedOperationException(); }

  //public void putAll( final MAP KEY_VALUE_EXTENDS_GENERIC c ) { throw new UnsupportedOperationException(); }
  public void putAll( final Map<? extends Float, ? extends Boolean> m ) { throw new UnsupportedOperationException(); }

  public ObjectSet<Float2BooleanMap.Entry > float2BooleanEntrySet() { if ( entries == null ) entries = ObjectSets.unmodifiable( map.float2BooleanEntrySet() ); return entries; }
  public FloatSet keySet() { if ( keys == null ) keys = FloatSets.unmodifiable( map.keySet() ); return keys; }
  public BooleanCollection values() { if ( values == null ) return BooleanCollections.unmodifiable( map.values() ); return values; }

  public void clear() { throw new UnsupportedOperationException(); }
  public String toString() { return map.toString(); }


  public Boolean put( final Float k, final Boolean v ) { throw new UnsupportedOperationException(); }



  public boolean remove( final float k ) { throw new UnsupportedOperationException(); }
  public boolean get( final float k ) { return map.get( k ); }
  public boolean containsKey( final Object ok ) { return map.containsKey( ok ); }



  public boolean containsValue( final Object ov ) { return map.containsValue( ov ); }







  public boolean isEmpty() { return map.isEmpty(); }
  public ObjectSet<Map.Entry<Float, Boolean>> entrySet() { return ObjectSets.unmodifiable( map.entrySet() ); }
 }

 /** Returns an unmodifiable type-specific map backed by the given type-specific map.
	 *
	 * @param m the map to be wrapped in an unmodifiable map.
	 * @return an unmodifiable view of the specified map.
	 * @see java.util.Collections#unmodifiableMap(Map)
	 */
 public static Float2BooleanMap unmodifiable( final Float2BooleanMap m ) { return new UnmodifiableMap ( m ); }

}
