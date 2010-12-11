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

import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Map;





/** An abstract class providing basic methods for sorted maps implementing a type-specific interface. */

public abstract class AbstractInt2DoubleSortedMap extends AbstractInt2DoubleMap implements Int2DoubleSortedMap {

 public static final long serialVersionUID = -1773560792952436569L;

 protected AbstractInt2DoubleSortedMap() {}

 /** Delegates to the corresponding type-specific method. */
 public Int2DoubleSortedMap headMap( final Integer to ) {
  return headMap( ((to).intValue()) );
 }

 /** Delegates to the corresponding type-specific method. */
 public Int2DoubleSortedMap tailMap( final Integer from ) {
  return tailMap( ((from).intValue()) );
 }

 /** Delegates to the corresponding type-specific method. */
 public Int2DoubleSortedMap subMap( final Integer from, final Integer to ) {
  return subMap( ((from).intValue()), ((to).intValue()) );
 }


 /** Delegates to the corresponding type-specific method. */
 public Integer firstKey() {
  return (Integer.valueOf(firstIntKey()));
 }

 /** Delegates to the corresponding type-specific method. */
 public Integer lastKey() {
  return (Integer.valueOf(lastIntKey()));
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


 public IntSortedSet keySet() {
  return new KeySet();
 }

 /** A wrapper exhibiting the keys of a map. */

 protected class KeySet extends AbstractIntSortedSet {
  public boolean contains( final int k ) { return containsKey( k ); }
  public int size() { return AbstractInt2DoubleSortedMap.this.size(); }
  public void clear() { AbstractInt2DoubleSortedMap.this.clear(); }

  public IntComparator comparator() { return AbstractInt2DoubleSortedMap.this.comparator(); }

  public int firstInt() { return firstIntKey(); }
  public int lastInt() { return lastIntKey(); }

  public IntSortedSet headSet( final int to ) { return headMap( to ).keySet(); }
  public IntSortedSet tailSet( final int from ) { return tailMap( from ).keySet(); }
  public IntSortedSet subSet( final int from, final int to ) { return subMap( from, to ).keySet(); }

  public IntBidirectionalIterator iterator( final int from ) { return new KeySetIterator ( entrySet().iterator( new BasicEntry ( from, (0) ) ) ); }
  public IntBidirectionalIterator iterator() { return new KeySetIterator ( entrySet().iterator() ); }


 }
 /** A wrapper exhibiting a map iterator as an iterator on keys.
	 *
	 * <P>To provide an iterator on keys, just create an instance of this
	 * class using the corresponding iterator on entries.
	 */

 protected static class KeySetIterator extends AbstractIntBidirectionalIterator {
  protected final ObjectBidirectionalIterator<Map.Entry <Integer, Double>> i;

  public KeySetIterator( ObjectBidirectionalIterator<Map.Entry <Integer, Double>> i ) {
   this.i = i;
  }

  public int nextInt() { return ((i.next().getKey()).intValue()); };
  public int previousInt() { return ((i.previous().getKey()).intValue()); };

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

 public DoubleCollection values() {
  return new ValuesCollection();
 }

 /** A wrapper exhibiting the values of a map. */
 protected class ValuesCollection extends AbstractDoubleCollection {
  public DoubleIterator iterator() { return new ValuesIterator ( entrySet().iterator() ); }
  public boolean contains( final double k ) { return containsValue( k ); }
  public int size() { return AbstractInt2DoubleSortedMap.this.size(); }
  public void clear() { AbstractInt2DoubleSortedMap.this.clear(); }

 }

 /** A wrapper exhibiting a map iterator as an iterator on values.
	 *
	 * <P>To provide an iterator on values, just create an instance of this
	 * class using the corresponding iterator on entries.
	 */

 protected static class ValuesIterator extends AbstractDoubleIterator {
  protected final ObjectBidirectionalIterator<Map.Entry <Integer, Double>> i;

  public ValuesIterator( ObjectBidirectionalIterator<Map.Entry <Integer, Double>> i ) {
   this.i = i;
  }

  public double nextDouble() { return ((i.next().getValue()).doubleValue()); };
  public boolean hasNext() { return i.hasNext(); }
 }

 @SuppressWarnings("unchecked")
 public ObjectSortedSet<Map.Entry<Integer, Double>> entrySet() {
  return (ObjectSortedSet)int2DoubleEntrySet();
 }
}
