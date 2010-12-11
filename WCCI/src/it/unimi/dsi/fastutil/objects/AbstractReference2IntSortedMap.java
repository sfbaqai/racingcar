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

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import it.unimi.dsi.fastutil.ints.AbstractIntIterator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Map;


import java.util.Comparator;


/** An abstract class providing basic methods for sorted maps implementing a type-specific interface. */

public abstract class AbstractReference2IntSortedMap <K> extends AbstractReference2IntMap <K> implements Reference2IntSortedMap <K> {

 public static final long serialVersionUID = -1773560792952436569L;

 protected AbstractReference2IntSortedMap() {}

 /** Delegates to the corresponding type-specific method. */
 public Reference2IntSortedMap <K> headMap( final K to ) {
  return headMap( (to) );
 }

 /** Delegates to the corresponding type-specific method. */
 public Reference2IntSortedMap <K> tailMap( final K from ) {
  return tailMap( (from) );
 }

 /** Delegates to the corresponding type-specific method. */
 public Reference2IntSortedMap <K> subMap( final K from, final K to ) {
  return subMap( (from), (to) );
 }


 /** Delegates to the corresponding type-specific method. */
 public K firstKey() {
  return (firstKey());
 }

 /** Delegates to the corresponding type-specific method. */
 public K lastKey() {
  return (lastKey());
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


 public ReferenceSortedSet <K> keySet() {
  return new KeySet();
 }

 /** A wrapper exhibiting the keys of a map. */

 protected class KeySet extends AbstractReferenceSortedSet <K> {
  public boolean contains( final Object k ) { return containsKey( k ); }
  public int size() { return AbstractReference2IntSortedMap.this.size(); }
  public void clear() { AbstractReference2IntSortedMap.this.clear(); }

  public Comparator <? super K> comparator() { return AbstractReference2IntSortedMap.this.comparator(); }

  public K first() { return firstKey(); }
  public K last() { return lastKey(); }

  public ReferenceSortedSet <K> headSet( final K to ) { return headMap( to ).keySet(); }
  public ReferenceSortedSet <K> tailSet( final K from ) { return tailMap( from ).keySet(); }
  public ReferenceSortedSet <K> subSet( final K from, final K to ) { return subMap( from, to ).keySet(); }

  public ObjectBidirectionalIterator <K> iterator( final K from ) { return new KeySetIterator <K>( entrySet().iterator( new BasicEntry <K>( from, (0) ) ) ); }
  public ObjectBidirectionalIterator <K> iterator() { return new KeySetIterator <K>( entrySet().iterator() ); }


 }
 /** A wrapper exhibiting a map iterator as an iterator on keys.
	 *
	 * <P>To provide an iterator on keys, just create an instance of this
	 * class using the corresponding iterator on entries.
	 */

 protected static class KeySetIterator <K> extends AbstractObjectBidirectionalIterator <K> {
  protected final ObjectBidirectionalIterator<Map.Entry <K, Integer>> i;

  public KeySetIterator( ObjectBidirectionalIterator<Map.Entry <K, Integer>> i ) {
   this.i = i;
  }

  public K next() { return (i.next().getKey()); };
  public K previous() { return (i.previous().getKey()); };

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

 public IntCollection values() {
  return new ValuesCollection();
 }

 /** A wrapper exhibiting the values of a map. */
 protected class ValuesCollection extends AbstractIntCollection {
  public IntIterator iterator() { return new ValuesIterator <K>( entrySet().iterator() ); }
  public boolean contains( final int k ) { return containsValue( k ); }
  public int size() { return AbstractReference2IntSortedMap.this.size(); }
  public void clear() { AbstractReference2IntSortedMap.this.clear(); }

 }

 /** A wrapper exhibiting a map iterator as an iterator on values.
	 *
	 * <P>To provide an iterator on values, just create an instance of this
	 * class using the corresponding iterator on entries.
	 */

 protected static class ValuesIterator <K> extends AbstractIntIterator {
  protected final ObjectBidirectionalIterator<Map.Entry <K, Integer>> i;

  public ValuesIterator( ObjectBidirectionalIterator<Map.Entry <K, Integer>> i ) {
   this.i = i;
  }

  public int nextInt() { return ((i.next().getValue()).intValue()); };
  public boolean hasNext() { return i.hasNext(); }
 }

 @SuppressWarnings("unchecked")
 public ObjectSortedSet<Map.Entry<K, Integer>> entrySet() {
  return (ObjectSortedSet)reference2IntEntrySet();
 }
}
