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

import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntIterator;

import it.unimi.dsi.fastutil.objects.ObjectSet;


import it.unimi.dsi.fastutil.objects.ObjectIterator;


import java.util.Iterator;
import java.util.Map;

/** An abstract class providing basic methods for maps implementing a type-specific interface.
 *
 * <P>Optional operations just throw an {@link
 * UnsupportedOperationException}. Generic versions of accessors delegate to
 * the corresponding type-specific counterparts following the interface rules
 * (they take care of returning <code>null</code> on a missing key).
 *
 * <P>As a further help, this class provides a {@link BasicEntry BasicEntry} inner class
 * that implements a type-specific version of {@link java.util.Map.Entry}; it
 * is particularly useful for those classes that do not implement their own
 * entries (e.g., most immutable maps).
 */

public abstract class AbstractShort2IntMap extends AbstractShort2IntFunction implements Short2IntMap , java.io.Serializable {

 public static final long serialVersionUID = -4940583368468432370L;

 protected AbstractShort2IntMap() {}


 public boolean containsValue( Object ov ) {
  return containsValue( ((((Integer)(ov)).intValue())) );
 }


 /** Checks whether the given value is contained in {@link #values()}. */
 public boolean containsValue( int v ) {
  return values().contains( v );
 }

 /** Checks whether the given value is contained in {@link #keySet()}. */
 public boolean containsKey( short k ) {
  return keySet().contains( k );
 }

 /** Puts all pairs in the given map.
	 * If the map implements the interface of this map,
	 * it uses the faster iterators.
	 *
	 * @param m a map.
	 */
 @SuppressWarnings("unchecked")
 public void putAll(Map<? extends Short,? extends Integer> m) {
  int n = m.size();
  final Iterator<? extends Map.Entry<? extends Short,? extends Integer>> i = m.entrySet().iterator();

  if (m instanceof Short2IntMap) {
   Short2IntMap.Entry e;
   while(n-- != 0) {
    e = (Short2IntMap.Entry )i.next();
    put(e.getShortKey(), e.getIntValue());
   }
  }
  else {
   Map.Entry<? extends Short,? extends Integer> e;
   while(n-- != 0) {
    e = i.next();
    put(e.getKey(), e.getValue());
   }
  }
 }

 public boolean isEmpty() {
  return size() == 0;
 }

 /** This class provides a basic but complete type-specific entry class for all those maps implementations
	 * that do not have entries on their own (e.g., most immutable maps). 
	 *
	 * <P>This class does not implement {@link java.util.Map.Entry#setValue(Object) setValue()}, as the modification
	 * would not be reflected in the base map.
	 */

 public static class BasicEntry implements Short2IntMap.Entry {
  protected short key;
  protected int value;

  public BasicEntry( final Short key, final Integer value ) {
   this.key = ((key).shortValue());
   this.value = ((value).intValue());
  }



  public BasicEntry( final short key, final int value ) {
   this.key = key;
   this.value = value;
  }



  public Short getKey() {
   return (Short.valueOf(key));
  }


  public short getShortKey() {
   return key;
  }


  public Integer getValue() {
   return (Integer.valueOf(value));
  }


  public int getIntValue() {
   return value;
  }


  public int setValue( final int value ) {
   throw new UnsupportedOperationException();
  }



  public Integer setValue( final Integer value ) {
   return Integer.valueOf(setValue(value.intValue()));
  }



  public boolean equals( final Object o ) {
   if (!(o instanceof Map.Entry)) return false;
   Map.Entry<?,?> e = (Map.Entry<?,?>)o;

   return ( (key) == (((((Short)(e.getKey())).shortValue()))) ) && ( (value) == (((((Integer)(e.getValue())).intValue()))) );
  }

  public int hashCode() {
   return (key) ^ (value);
  }


  public String toString() {
   return key + "->" + value;
  }
 }


 /** Returns a type-specific-set view of the keys of this map.
	 *
	 * <P>The view is backed by the set returned by {@link #entrySet()}. Note that
	 * <em>no attempt is made at caching the result of this method</em>, as this would
	 * require adding some attributes that lightweight implementations would
	 * not need. Subclasses may easily override this policy by calling
	 * this method and caching the result, but implementors are encouraged to
	 * write more efficient ad-hoc implementations.
	 *
	 * @return a set view of the keys of this map; it may be safely cast to a type-specific interface.
	 */


 public ShortSet keySet() {
  return new AbstractShortSet () {

    public boolean contains( final short k ) { return containsKey( k ); }

    public int size() { return AbstractShort2IntMap.this.size(); }
    public void clear() { AbstractShort2IntMap.this.clear(); }

    public ShortIterator iterator() {
     return new AbstractShortIterator () {
       final ObjectIterator<Map.Entry<Short,Integer>> i = entrySet().iterator();

       public short nextShort() { return ((Short2IntMap.Entry )i.next()).getShortKey(); };

       public boolean hasNext() { return i.hasNext(); }
      };
    }
   };
 }

 /** Returns a type-specific-set view of the values of this map.
	 *
	 * <P>The view is backed by the set returned by {@link #entrySet()}. Note that
	 * <em>no attempt is made at caching the result of this method</em>, as this would
	 * require adding some attributes that lightweight implementations would
	 * not need. Subclasses may easily override this policy by calling
	 * this method and caching the result, but implementors are encouraged to
	 * write more efficient ad-hoc implementations.
	 *
	 * @return a set view of the values of this map; it may be safely cast to a type-specific interface.
	 */


 public IntCollection values() {
  return new AbstractIntCollection () {

    public boolean contains( final int k ) { return containsValue( k ); }

    public int size() { return AbstractShort2IntMap.this.size(); }
    public void clear() { AbstractShort2IntMap.this.clear(); }

    public IntIterator iterator() {
     return new AbstractIntIterator () {
       final ObjectIterator<Map.Entry<Short,Integer>> i = entrySet().iterator();

       public int nextInt() { return ((Short2IntMap.Entry )i.next()).getIntValue(); };

       public boolean hasNext() { return i.hasNext(); }
      };
    }
   };
 }


 @SuppressWarnings("unchecked")
 public ObjectSet<Map.Entry<Short, Integer>> entrySet() {
  return (ObjectSet)short2IntEntrySet();
 }



 /** Returns a hash code for this map.
	 *
	 * The hash code of a map is computed by summing the hash codes of its entries.
	 *
	 * @return a hash code for this map.
	 */

 public int hashCode() {
  int h = 0, n = size();
  final ObjectIterator<? extends Map.Entry<Short,Integer>> i = entrySet().iterator();

  while( n-- != 0 ) h += i.next().hashCode();
  return h;
 }

 public boolean equals(Object o) {
  if ( o == this ) return true;
  if ( ! ( o instanceof Map ) ) return false;

  Map<?,?> m = (Map<?,?>)o;
  if ( m.size() != size() ) return false;
  return entrySet().containsAll( m.entrySet() );
 }


 public String toString() {
  final StringBuilder s = new StringBuilder();
  final ObjectIterator<? extends Map.Entry<Short,Integer>> i = entrySet().iterator();
  int n = size();
  Short2IntMap.Entry e;
  boolean first = true;

  s.append("{");

  while(n-- != 0) {
   if (first) first = false;
   else s.append(", ");

   e = (Short2IntMap.Entry )i.next();




    s.append(String.valueOf(e.getShortKey()));
   s.append("=>");



    s.append(String.valueOf(e.getIntValue()));
  }

  s.append("}");
  return s.toString();
 }


}
