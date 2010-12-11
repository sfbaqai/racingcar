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

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import it.unimi.dsi.fastutil.chars.AbstractCharIterator;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Map;





/** An abstract class providing basic methods for sorted maps implementing a type-specific interface. */

public abstract class AbstractChar2CharSortedMap extends AbstractChar2CharMap implements Char2CharSortedMap {

 public static final long serialVersionUID = -1773560792952436569L;

 protected AbstractChar2CharSortedMap() {}

 /** Delegates to the corresponding type-specific method. */
 public Char2CharSortedMap headMap( final Character to ) {
  return headMap( ((to).charValue()) );
 }

 /** Delegates to the corresponding type-specific method. */
 public Char2CharSortedMap tailMap( final Character from ) {
  return tailMap( ((from).charValue()) );
 }

 /** Delegates to the corresponding type-specific method. */
 public Char2CharSortedMap subMap( final Character from, final Character to ) {
  return subMap( ((from).charValue()), ((to).charValue()) );
 }


 /** Delegates to the corresponding type-specific method. */
 public Character firstKey() {
  return (Character.valueOf(firstCharKey()));
 }

 /** Delegates to the corresponding type-specific method. */
 public Character lastKey() {
  return (Character.valueOf(lastCharKey()));
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


 public CharSortedSet keySet() {
  return new KeySet();
 }

 /** A wrapper exhibiting the keys of a map. */

 protected class KeySet extends AbstractCharSortedSet {
  public boolean contains( final char k ) { return containsKey( k ); }
  public int size() { return AbstractChar2CharSortedMap.this.size(); }
  public void clear() { AbstractChar2CharSortedMap.this.clear(); }

  public CharComparator comparator() { return AbstractChar2CharSortedMap.this.comparator(); }

  public char firstChar() { return firstCharKey(); }
  public char lastChar() { return lastCharKey(); }

  public CharSortedSet headSet( final char to ) { return headMap( to ).keySet(); }
  public CharSortedSet tailSet( final char from ) { return tailMap( from ).keySet(); }
  public CharSortedSet subSet( final char from, final char to ) { return subMap( from, to ).keySet(); }

  public CharBidirectionalIterator iterator( final char from ) { return new KeySetIterator ( entrySet().iterator( new BasicEntry ( from, ((char)0) ) ) ); }
  public CharBidirectionalIterator iterator() { return new KeySetIterator ( entrySet().iterator() ); }


 }
 /** A wrapper exhibiting a map iterator as an iterator on keys.
	 *
	 * <P>To provide an iterator on keys, just create an instance of this
	 * class using the corresponding iterator on entries.
	 */

 protected static class KeySetIterator extends AbstractCharBidirectionalIterator {
  protected final ObjectBidirectionalIterator<Map.Entry <Character, Character>> i;

  public KeySetIterator( ObjectBidirectionalIterator<Map.Entry <Character, Character>> i ) {
   this.i = i;
  }

  public char nextChar() { return ((i.next().getKey()).charValue()); };
  public char previousChar() { return ((i.previous().getKey()).charValue()); };

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

 public CharCollection values() {
  return new ValuesCollection();
 }

 /** A wrapper exhibiting the values of a map. */
 protected class ValuesCollection extends AbstractCharCollection {
  public CharIterator iterator() { return new ValuesIterator ( entrySet().iterator() ); }
  public boolean contains( final char k ) { return containsValue( k ); }
  public int size() { return AbstractChar2CharSortedMap.this.size(); }
  public void clear() { AbstractChar2CharSortedMap.this.clear(); }

 }

 /** A wrapper exhibiting a map iterator as an iterator on values.
	 *
	 * <P>To provide an iterator on values, just create an instance of this
	 * class using the corresponding iterator on entries.
	 */

 protected static class ValuesIterator extends AbstractCharIterator {
  protected final ObjectBidirectionalIterator<Map.Entry <Character, Character>> i;

  public ValuesIterator( ObjectBidirectionalIterator<Map.Entry <Character, Character>> i ) {
   this.i = i;
  }

  public char nextChar() { return ((i.next().getValue()).charValue()); };
  public boolean hasNext() { return i.hasNext(); }
 }

 @SuppressWarnings("unchecked")
 public ObjectSortedSet<Map.Entry<Character, Character>> entrySet() {
  return (ObjectSortedSet)char2CharEntrySet();
 }
}
