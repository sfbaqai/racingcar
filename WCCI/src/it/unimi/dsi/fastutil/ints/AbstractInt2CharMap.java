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

import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharIterator;

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

public abstract class AbstractInt2CharMap extends AbstractInt2CharFunction implements Int2CharMap , java.io.Serializable {

 public static final long serialVersionUID = -4940583368468432370L;

 protected AbstractInt2CharMap() {}


 public boolean containsValue( Object ov ) {
  return containsValue( ((((Character)(ov)).charValue())) );
 }


 /** Checks whether the given value is contained in {@link #values()}. */
 public boolean containsValue( char v ) {
  return values().contains( v );
 }

 /** Checks whether the given value is contained in {@link #keySet()}. */
 public boolean containsKey( int k ) {
  return keySet().contains( k );
 }

 /** Puts all pairs in the given map.
	 * If the map implements the interface of this map,
	 * it uses the faster iterators.
	 *
	 * @param m a map.
	 */
 @SuppressWarnings("unchecked")
 public void putAll(Map<? extends Integer,? extends Character> m) {
  int n = m.size();
  final Iterator<? extends Map.Entry<? extends Integer,? extends Character>> i = m.entrySet().iterator();

  if (m instanceof Int2CharMap) {
   Int2CharMap.Entry e;
   while(n-- != 0) {
    e = (Int2CharMap.Entry )i.next();
    put(e.getIntKey(), e.getCharValue());
   }
  }
  else {
   Map.Entry<? extends Integer,? extends Character> e;
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

 public static class BasicEntry implements Int2CharMap.Entry {
  protected int key;
  protected char value;

  public BasicEntry( final Integer key, final Character value ) {
   this.key = ((key).intValue());
   this.value = ((value).charValue());
  }



  public BasicEntry( final int key, final char value ) {
   this.key = key;
   this.value = value;
  }



  public Integer getKey() {
   return (Integer.valueOf(key));
  }


  public int getIntKey() {
   return key;
  }


  public Character getValue() {
   return (Character.valueOf(value));
  }


  public char getCharValue() {
   return value;
  }


  public char setValue( final char value ) {
   throw new UnsupportedOperationException();
  }



  public Character setValue( final Character value ) {
   return Character.valueOf(setValue(value.charValue()));
  }



  public boolean equals( final Object o ) {
   if (!(o instanceof Map.Entry)) return false;
   Map.Entry<?,?> e = (Map.Entry<?,?>)o;

   return ( (key) == (((((Integer)(e.getKey())).intValue()))) ) && ( (value) == (((((Character)(e.getValue())).charValue()))) );
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


 public IntSet keySet() {
  return new AbstractIntSet () {

    public boolean contains( final int k ) { return containsKey( k ); }

    public int size() { return AbstractInt2CharMap.this.size(); }
    public void clear() { AbstractInt2CharMap.this.clear(); }

    public IntIterator iterator() {
     return new AbstractIntIterator () {
       final ObjectIterator<Map.Entry<Integer,Character>> i = entrySet().iterator();

       public int nextInt() { return ((Int2CharMap.Entry )i.next()).getIntKey(); };

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


 public CharCollection values() {
  return new AbstractCharCollection () {

    public boolean contains( final char k ) { return containsValue( k ); }

    public int size() { return AbstractInt2CharMap.this.size(); }
    public void clear() { AbstractInt2CharMap.this.clear(); }

    public CharIterator iterator() {
     return new AbstractCharIterator () {
       final ObjectIterator<Map.Entry<Integer,Character>> i = entrySet().iterator();

       public char nextChar() { return ((Int2CharMap.Entry )i.next()).getCharValue(); };

       public boolean hasNext() { return i.hasNext(); }
      };
    }
   };
 }


 @SuppressWarnings("unchecked")
 public ObjectSet<Map.Entry<Integer, Character>> entrySet() {
  return (ObjectSet)int2CharEntrySet();
 }



 /** Returns a hash code for this map.
	 *
	 * The hash code of a map is computed by summing the hash codes of its entries.
	 *
	 * @return a hash code for this map.
	 */

 public int hashCode() {
  int h = 0, n = size();
  final ObjectIterator<? extends Map.Entry<Integer,Character>> i = entrySet().iterator();

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
  final ObjectIterator<? extends Map.Entry<Integer,Character>> i = entrySet().iterator();
  int n = size();
  Int2CharMap.Entry e;
  boolean first = true;

  s.append("{");

  while(n-- != 0) {
   if (first) first = false;
   else s.append(", ");

   e = (Int2CharMap.Entry )i.next();




    s.append(String.valueOf(e.getIntKey()));
   s.append("=>");



    s.append(String.valueOf(e.getCharValue()));
  }

  s.append("}");
  return s.toString();
 }


}
