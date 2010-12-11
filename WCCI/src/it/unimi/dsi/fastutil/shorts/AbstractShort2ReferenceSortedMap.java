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

import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Map;





/** An abstract class providing basic methods for sorted maps implementing a type-specific interface. */

public abstract class AbstractShort2ReferenceSortedMap <V> extends AbstractShort2ReferenceMap <V> implements Short2ReferenceSortedMap <V> {

 public static final long serialVersionUID = -1773560792952436569L;

 protected AbstractShort2ReferenceSortedMap() {}

 /** Delegates to the corresponding type-specific method. */
 public Short2ReferenceSortedMap <V> headMap( final Short to ) {
  return headMap( ((to).shortValue()) );
 }

 /** Delegates to the corresponding type-specific method. */
 public Short2ReferenceSortedMap <V> tailMap( final Short from ) {
  return tailMap( ((from).shortValue()) );
 }

 /** Delegates to the corresponding type-specific method. */
 public Short2ReferenceSortedMap <V> subMap( final Short from, final Short to ) {
  return subMap( ((from).shortValue()), ((to).shortValue()) );
 }


 /** Delegates to the corresponding type-specific method. */
 public Short firstKey() {
  return (Short.valueOf(firstShortKey()));
 }

 /** Delegates to the corresponding type-specific method. */
 public Short lastKey() {
  return (Short.valueOf(lastShortKey()));
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


 public ShortSortedSet keySet() {
  return new KeySet();
 }

 /** A wrapper exhibiting the keys of a map. */

 protected class KeySet extends AbstractShortSortedSet {
  public boolean contains( final short k ) { return containsKey( k ); }
  public int size() { return AbstractShort2ReferenceSortedMap.this.size(); }
  public void clear() { AbstractShort2ReferenceSortedMap.this.clear(); }

  public ShortComparator comparator() { return AbstractShort2ReferenceSortedMap.this.comparator(); }

  public short firstShort() { return firstShortKey(); }
  public short lastShort() { return lastShortKey(); }

  public ShortSortedSet headSet( final short to ) { return headMap( to ).keySet(); }
  public ShortSortedSet tailSet( final short from ) { return tailMap( from ).keySet(); }
  public ShortSortedSet subSet( final short from, final short to ) { return subMap( from, to ).keySet(); }

  public ShortBidirectionalIterator iterator( final short from ) { return new KeySetIterator <V>( entrySet().iterator( new BasicEntry <V>( from, (null) ) ) ); }
  public ShortBidirectionalIterator iterator() { return new KeySetIterator <V>( entrySet().iterator() ); }


 }
 /** A wrapper exhibiting a map iterator as an iterator on keys.
	 *
	 * <P>To provide an iterator on keys, just create an instance of this
	 * class using the corresponding iterator on entries.
	 */

 protected static class KeySetIterator <V> extends AbstractShortBidirectionalIterator {
  protected final ObjectBidirectionalIterator<Map.Entry <Short, V>> i;

  public KeySetIterator( ObjectBidirectionalIterator<Map.Entry <Short, V>> i ) {
   this.i = i;
  }

  public short nextShort() { return ((i.next().getKey()).shortValue()); };
  public short previousShort() { return ((i.previous().getKey()).shortValue()); };

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

 public ReferenceCollection <V> values() {
  return new ValuesCollection();
 }

 /** A wrapper exhibiting the values of a map. */
 protected class ValuesCollection extends AbstractReferenceCollection <V> {
  public ObjectIterator <V> iterator() { return new ValuesIterator <V>( entrySet().iterator() ); }
  public boolean contains( final Object k ) { return containsValue( k ); }
  public int size() { return AbstractShort2ReferenceSortedMap.this.size(); }
  public void clear() { AbstractShort2ReferenceSortedMap.this.clear(); }

 }

 /** A wrapper exhibiting a map iterator as an iterator on values.
	 *
	 * <P>To provide an iterator on values, just create an instance of this
	 * class using the corresponding iterator on entries.
	 */

 protected static class ValuesIterator <V> extends AbstractObjectIterator <V> {
  protected final ObjectBidirectionalIterator<Map.Entry <Short, V>> i;

  public ValuesIterator( ObjectBidirectionalIterator<Map.Entry <Short, V>> i ) {
   this.i = i;
  }

  public V next() { return (i.next().getValue()); };
  public boolean hasNext() { return i.hasNext(); }
 }

 @SuppressWarnings("unchecked")
 public ObjectSortedSet<Map.Entry<Short, V>> entrySet() {
  return (ObjectSortedSet)short2ReferenceEntrySet();
 }
}
