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

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.booleans.BooleanCollection;

import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

import java.util.Map;
import java.util.SortedMap;





/** A type-specific {@link SortedMap}; provides some additional methods that use polymorphism to avoid (un)boxing.
 *
 * <P>Additionally, this interface strengthens {@link #entrySet()},
 * {@link #keySet()}, {@link #values()},
 * {@link #comparator()}, {@link SortedMap#subMap(Object,Object)}, {@link SortedMap#headMap(Object)} and {@link SortedMap#tailMap(Object)}.
 *
 * @see SortedMap
 */

public interface Long2BooleanSortedMap extends Long2BooleanMap , SortedMap<Long, Boolean> {

 /** A sorted entry set providing fast iteration.
	 *
	 * <p>In some cases (e.g., hash-based classes) iteration over an entry set requires the creation
	 * of a large number of entry objects. Some <code>fastutil</code>
	 * maps might return {@linkplain #entrySet() entry set} objects of type {@link FastSortedEntrySet FastSortedEntrySet}: in this case, {@link #fastIterator() fastIterator()}
	 * will return an iterator that is guaranteed not to create a large number of objects, <em>possibly
	 * by returning always the same entry</em> (of course, mutated).
	 */
 public interface FastSortedEntrySet extends ObjectSortedSet<Long2BooleanMap.Entry >, FastEntrySet {
  /** Returns a fast iterator over this sorted entry set; the iterator might return always the same entry object, suitably mutated.
		 *
		 * @return a fast iterator over this sorted entry set; the iterator might return always the same entry object, suitably mutated.
		 */
  public ObjectBidirectionalIterator<Long2BooleanMap.Entry > fastIterator( Long2BooleanMap.Entry from );
 }

 /** Returns a sorted-set view of the mappings contained in this map.
	 *  Note that this specification strengthens the one given in the
	 *  corresponding type-specific unsorted map.
	 *
	 * @return a sorted-set view of the mappings contained in this map.
	 * @see Map#entrySet()
	 */

 ObjectSortedSet<Map.Entry<Long, Boolean>> entrySet();

 /** Returns a type-specific sorted-set view of the mappings contained in this map.
	 * Note that this specification strengthens the one given in the
	 * corresponding type-specific unsorted map.
	 *
	 * @return a type-specific sorted-set view of the mappings contained in this map.
	 * @see #entrySet()
	 */

 ObjectSortedSet<Long2BooleanMap.Entry > long2BooleanEntrySet();

 /** Returns a sorted-set view of the keys contained in this map.
	 *  Note that this specification strengthens the one given in the
	 *  corresponding type-specific unsorted map.
	 *
	 * @return a sorted-set view of the keys contained in this map.
	 * @see Map#keySet()
	 */

 LongSortedSet keySet();

 /** Returns a set view of the values contained in this map.
	 * <P>Note that this specification strengthens the one given in {@link Map#values()},
	 * which was already strengthened in the corresponding type-specific class,
	 * but was weakened by the fact that this interface extends {@link SortedMap}.
	 *
	 * @return a set view of the values contained in this map.
	 * @see Map#values()
	 */

 BooleanCollection values();
 /** Returns the comparator associated with this sorted set, or null if it uses its keys' natural ordering.
	 *
	 *  <P>Note that this specification strengthens the one given in {@link SortedMap#comparator()}.
	 *
	 * @see SortedMap#comparator()
	 */
 LongComparator comparator();

 /** Returns a view of the portion of this sorted map whose keys range from <code>fromKey</code>, inclusive, to <code>toKey</code>, exclusive.
	 *
	 *  <P>Note that this specification strengthens the one given in {@link SortedMap#subMap(Object,Object)}.
	 *
	 * @see SortedMap#subMap(Object,Object)
	 */
 Long2BooleanSortedMap subMap(Long fromKey, Long toKey);

 /** Returns a view of the portion of this sorted map whose keys are strictly less than <code>toKey</code>.
	 *
	 *  <P>Note that this specification strengthens the one given in {@link SortedMap#headMap(Object)}.
	 *
	 * @see SortedMap#headMap(Object)
	 */
 Long2BooleanSortedMap headMap(Long toKey);

 /** Returns a view of the portion of this sorted map whose keys are greater than or equal to <code>fromKey</code>.
	 *
	 *  <P>Note that this specification strengthens the one given in {@link SortedMap#tailMap(Object)}.
	 *
	 * @see SortedMap#tailMap(Object)
	 */
 Long2BooleanSortedMap tailMap(Long fromKey);


 /**  Returns a view of the portion of this sorted map whose keys range from <code>fromKey</code>, inclusive, to <code>toKey</code>, exclusive.
	 * @see SortedMap#subMap(Object,Object)
	 */
 Long2BooleanSortedMap subMap(long fromKey, long toKey);

 /** Returns a view of the portion of this sorted map whose keys are strictly less than <code>toKey</code>.
	 * @see SortedMap#headMap(Object)
	 */
 Long2BooleanSortedMap headMap(long toKey);

 /** Returns a view of the portion of this sorted map whose keys are greater than or equal to <code>fromKey</code>.
	 * @see SortedMap#tailMap(Object)
	 */
 Long2BooleanSortedMap tailMap(long fromKey);

 /**
	 * @see SortedMap#firstKey()
	 */
 long firstLongKey();

 /**
	 * @see SortedMap#lastKey()
	 */
 long lastLongKey();

}
