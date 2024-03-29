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

import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import it.unimi.dsi.fastutil.bytes.AbstractByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Map;





/** An abstract class providing basic methods for sorted maps implementing a type-specific interface. */

public abstract class AbstractFloat2ByteSortedMap extends AbstractFloat2ByteMap implements Float2ByteSortedMap {

 public static final long serialVersionUID = -1773560792952436569L;

 protected AbstractFloat2ByteSortedMap() {}

 /** Delegates to the corresponding type-specific method. */
 public Float2ByteSortedMap headMap( final Float to ) {
  return headMap( ((to).floatValue()) );
 }

 /** Delegates to the corresponding type-specific method. */
 public Float2ByteSortedMap tailMap( final Float from ) {
  return tailMap( ((from).floatValue()) );
 }

 /** Delegates to the corresponding type-specific method. */
 public Float2ByteSortedMap subMap( final Float from, final Float to ) {
  return subMap( ((from).floatValue()), ((to).floatValue()) );
 }


 /** Delegates to the corresponding type-specific method. */
 public Float firstKey() {
  return (Float.valueOf(firstFloatKey()));
 }

 /** Delegates to the corresponding type-specific method. */
 public Float lastKey() {
  return (Float.valueOf(lastFloatKey()));
 }



 /** Returns a type-specific-sorted-set view of the keys of this map.
	 *
	 * <P>The view is backed by the sorted set returned by {@link #entrySet()}. Note that
	 * <em>no attempt is made at caching the result of this method</em>, as this would
	 * require adding some attributes that lightweight implementations would
	 * not need. Subclasses may easily override this policy by calling
	 * this method and caching the result, but implementors are encouraged to
	 * write more efficient ad-hoc implementations.
	 *
	 * @return a sorted set view of the keys of this map; it may be safely cast to a type-specific interface.
	 */


 public FloatSortedSet keySet() {
  return new KeySet();
 }

 /** A wrapper exhibiting the keys of a map. */

 protected class KeySet extends AbstractFloatSortedSet {
  public boolean contains( final float k ) { return containsKey( k ); }
  public int size() { return AbstractFloat2ByteSortedMap.this.size(); }
  public void clear() { AbstractFloat2ByteSortedMap.this.clear(); }

  public FloatComparator comparator() { return AbstractFloat2ByteSortedMap.this.comparator(); }

  public float firstFloat() { return firstFloatKey(); }
  public float lastFloat() { return lastFloatKey(); }

  public FloatSortedSet headSet( final float to ) { return headMap( to ).keySet(); }
  public FloatSortedSet tailSet( final float from ) { return tailMap( from ).keySet(); }
  public FloatSortedSet subSet( final float from, final float to ) { return subMap( from, to ).keySet(); }

  public FloatBidirectionalIterator iterator( final float from ) { return new KeySetIterator ( entrySet().iterator( new BasicEntry ( from, ((byte)0) ) ) ); }
  public FloatBidirectionalIterator iterator() { return new KeySetIterator ( entrySet().iterator() ); }


 }
 /** A wrapper exhibiting a map iterator as an iterator on keys.
	 *
	 * <P>To provide an iterator on keys, just create an instance of this
	 * class using the corresponding iterator on entries.
	 */

 protected static class KeySetIterator extends AbstractFloatBidirectionalIterator {
  protected final ObjectBidirectionalIterator<Map.Entry <Float, Byte>> i;

  public KeySetIterator( ObjectBidirectionalIterator<Map.Entry <Float, Byte>> i ) {
   this.i = i;
  }

  public float nextFloat() { return ((i.next().getKey()).floatValue()); };
  public float previousFloat() { return ((i.previous().getKey()).floatValue()); };

  public boolean hasNext() { return i.hasNext(); }
  public boolean hasPrevious() { return i.hasPrevious(); }
 }



 /** Returns a type-specific collection view of the values contained in this map.
	 *
	 * <P>The view is backed by the sorted set returned by {@link #entrySet()}. Note that
	 * <em>no attempt is made at caching the result of this method</em>, as this would
	 * require adding some attributes that lightweight implementations would
	 * not need. Subclasses may easily override this policy by calling
	 * this method and caching the result, but implementors are encouraged to
	 * write more efficient ad-hoc implementations.
	 *
	 * @return a type-specific collection view of the values contained in this map.
	 */

 public ByteCollection values() {
  return new ValuesCollection();
 }

 /** A wrapper exhibiting the values of a map. */
 protected class ValuesCollection extends AbstractByteCollection {
  public ByteIterator iterator() { return new ValuesIterator ( entrySet().iterator() ); }
  public boolean contains( final byte k ) { return containsValue( k ); }
  public int size() { return AbstractFloat2ByteSortedMap.this.size(); }
  public void clear() { AbstractFloat2ByteSortedMap.this.clear(); }

 }

 /** A wrapper exhibiting a map iterator as an iterator on values.
	 *
	 * <P>To provide an iterator on values, just create an instance of this
	 * class using the corresponding iterator on entries.
	 */

 protected static class ValuesIterator extends AbstractByteIterator {
  protected final ObjectBidirectionalIterator<Map.Entry <Float, Byte>> i;

  public ValuesIterator( ObjectBidirectionalIterator<Map.Entry <Float, Byte>> i ) {
   this.i = i;
  }

  public byte nextByte() { return ((i.next().getValue()).byteValue()); };
  public boolean hasNext() { return i.hasNext(); }
 }

 @SuppressWarnings("unchecked")
 public ObjectSortedSet<Map.Entry<Float, Byte>> entrySet() {
  return (ObjectSortedSet)float2ByteEntrySet();
 }
}
