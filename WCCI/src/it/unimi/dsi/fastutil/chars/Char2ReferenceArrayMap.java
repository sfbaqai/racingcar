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
/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2007-2008 Sebastiano Vigna 
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

import java.util.Map;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.ReferenceCollections;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

/** A simple, brute-force implementation of a map based on two parallel backing arrays. 
 * 
 * <p>The main purpose of this
 * implementation is that of wrapping cleanly the brute-force approach to the storage of a very 
 * small number of pairs: just put them into two parallel arrays and scan linearly to find an item.
 */

public class Char2ReferenceArrayMap <V> extends AbstractChar2ReferenceMap <V> implements java.io.Serializable, Cloneable {

 private static final long serialVersionUID = 1L;
 /** The keys (valid up to {@link #size}, excluded). */
 private transient char[] key;
 /** The values (parallel to {@link #key}). */
 private transient Object[] value;
 /** The number of valid entries in {@link #key} and {@link #value}. */
 private int size;

 /** Creates a new empty array map with given key and value backing arrays.
	 * 
	 * @param key the key array.
	 * @param value the value array (it <em>must</em> have the same length as <code>key</code>).
	 */
 public Char2ReferenceArrayMap( final char[] key, final Object[] value ) {
  this.key = key;
  this.value = value;
  if( key.length != value.length ) throw new IllegalArgumentException( "Keys and values have different lengths (" + key.length + ", " + value.length + ")" );
 }

 /** Creates a new empty array map.
	 */
 public Char2ReferenceArrayMap() {
  this.key = CharArrays.EMPTY_ARRAY;
  this.value = ObjectArrays.EMPTY_ARRAY;
 }

 /** Creates a new empty array map of given capacity.
	 *
	 * @param capacity the initial capacity.
	 */
 public Char2ReferenceArrayMap( final int capacity ) {
  this.key = new char[ capacity ];
  this.value = new Object[ capacity ];
 }

 /** Creates a new empty array map copying the entries of a given map.
	 *
	 * @param m a map.
	 */
 public Char2ReferenceArrayMap( final Char2ReferenceMap <V> m ) {
  this( m.size() );
  putAll( m );
 }

 /** Creates a new array map with given key and value backing arrays, using the given number of elements.
	 * 
	 * @param key the key array.
	 * @param value the value array (it <em>must</em> have the same length as <code>key</code>).
	 * @param size the number of valid elements in <code>key</code> and <code>value</code>.
	 */
 public Char2ReferenceArrayMap( final char[] key, final Object[] value, final int size ) {
  this.key = key;
  this.value = value;
  this.size = size;
  if( key.length != value.length ) throw new IllegalArgumentException( "Keys and values have different lengths (" + key.length + ", " + value.length + ")" );
  if ( size > key.length ) throw new IllegalArgumentException( "The provided size (" + size + ") is larger than or equal to the backing-arrays size (" + key.length + ")" );
 }

 private final class EntrySet extends AbstractObjectSet<Char2ReferenceMap.Entry <V> > implements FastEntrySet <V> {

  @Override
  public ObjectIterator<Char2ReferenceMap.Entry <V> > iterator() {
   return new AbstractObjectIterator<Char2ReferenceMap.Entry <V> >() {
    int next = 0;

    public boolean hasNext() {
     return next < size;
    }

    @SuppressWarnings("unchecked")
    public Entry next() {
     if ( ! hasNext() ) throw new NoSuchElementException();
     return new AbstractChar2ReferenceMap.BasicEntry <V>( key[ next ], (V) value[ next++ ] );
    }

   };
  }

  public ObjectIterator<Char2ReferenceMap.Entry <V> > fastIterator() {
   return new AbstractObjectIterator<Char2ReferenceMap.Entry <V> >() {
    int next = 0;
    final BasicEntry <V> entry = new BasicEntry <V> ( ((char)0), (null) );

    public boolean hasNext() {
     return next < size;
    }

    @SuppressWarnings("unchecked")
    public Entry next() {
     if ( ! hasNext() ) throw new NoSuchElementException();
     entry.key = key[ next ];
     entry.value = (V) value[ next++ ];
     return entry;
    }

   };
  }

  public int size() {
   return size;
  }

  @SuppressWarnings("unchecked")
  public boolean contains( Object o ) {
   if ( ! ( o instanceof Map.Entry ) ) return false;
   Map.Entry e = (Map.Entry)o;
   return Char2ReferenceArrayMap.this.containsKey( e.getKey() ) && ( (Char2ReferenceArrayMap.this.get( e.getKey() )) == (e.getValue()) );
  }

 }

 public FastEntrySet <V> char2ReferenceEntrySet() {
  return new EntrySet();
 }

 private int findKey( final char k ) {
  final char[] key = this.key;
  for( int i = size; i-- != 0; ) if ( ( (key[ i ]) == (k) ) ) return i;
  return -1;
 }

 @SuppressWarnings("unchecked")

 public V get( final char k ) {



  final char[] key = this.key;
  for( int i = size; i-- != 0; ) if ( ( (key[ i ]) == (k) ) ) return (V) value[ i ];
  return defRetValue;
 }

 public int size() {
  return size;
 }

 @Override
 public void clear() {

  for( int i = size; i-- != 0; ) {




   value[ i ] = null;

  }

  size = 0;
 }

 @Override
 public boolean containsKey( final char k ) {
  return findKey( k ) != -1;
 }

 @Override
 @SuppressWarnings("unchecked")
 public boolean containsValue( Object v ) {
  for( int i = size; i-- != 0; ) if ( ( (value[ i ]) == (v) ) ) return true;
  return false;
 }

 @Override
 public boolean isEmpty() {
  return size == 0;
 }

 @Override
 @SuppressWarnings("unchecked")
 public V put( char k, V v ) {
  final int oldKey = findKey( k );
  if ( oldKey != -1 ) {
   final V oldValue = (V) value[ oldKey ];
   value[ oldKey ] = v;
   return oldValue;
  }
  if ( size == key.length ) {
   final char[] newKey = new char[ size == 0 ? 2 : size * 2 ];
   final Object[] newValue = new Object[ size == 0 ? 2 : size * 2 ];
   for( int i = size; i-- != 0; ) {
    newKey[ i ] = key[ i ];
    newValue[ i ] = value[ i ];
   }
   key = newKey;
   value = newValue;
  }
  key[ size ] = k;
  value[ size ] = v;
  size++;
  return defRetValue;
 }

 @Override
 @SuppressWarnings("unchecked")


 public V remove( final char k ) {



  final int oldPos = findKey( k );
  if ( oldPos == -1 ) return defRetValue;
  final V oldValue = (V) value[ oldPos ];
  final int tail = size - oldPos - 1;
  for( int i = 0; i < tail; i++ ) {
   key[ oldPos + i ] = key[ oldPos + i + 1 ];
   value[ oldPos + i ] = value[ oldPos + i + 1 ];
  }
  size--;




  value[ size ] = null;

  return oldValue;
 }

 @Override

 @SuppressWarnings("unchecked")
 public CharSet keySet() {
  return new CharArraySet ( key, size );
 }

 @Override
 public ReferenceCollection <V> values() {
  return ReferenceCollections.unmodifiable( new ReferenceArraySet <V>( value, size ) );
 }

 /** Returns a deep copy of this map. 
	 *
	 * <P>This method performs a deep copy of this hash map; the data stored in the
	 * map, however, is not cloned. Note that this makes a difference only for object keys.
	 *
	 *  @return a deep copy of this map.
	 */

 @SuppressWarnings("unchecked")
 public Object clone() {
  Char2ReferenceArrayMap <V> c;
  try {
   c = (Char2ReferenceArrayMap <V>)super.clone();
  }
  catch(CloneNotSupportedException cantHappen) {
   throw new InternalError();
  }
  c.key = key.clone();
  c.value = value.clone();
  return c;
 }

 private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
  s.defaultWriteObject();
  for( int i = 0; i < size; i++ ) {
   s.writeChar( key[ i ] );
   s.writeObject( value[ i ] );
  }
 }

 @SuppressWarnings("unchecked")
 private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
  s.defaultReadObject();
  key = new char[ size ];
  value = new Object[ size ];
  for( int i = 0; i < size; i++ ) {
   key[ i ] = s.readChar();
   value[ i ] = s.readObject();
  }
 }
}
