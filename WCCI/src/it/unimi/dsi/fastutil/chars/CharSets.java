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

package it.unimi.dsi.fastutil.chars;

import java.util.Collection;
import java.util.Set;

/** A class providing static methods and objects that do useful things with type-specific sets.
 *
 * @see java.util.Collections
 */

public class CharSets {

 private CharSets() {}

 /** An immutable class representing the empty set and implementing a type-specific set interface.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific set.
	 */

 public static class EmptySet extends CharCollections.EmptyCollection implements CharSet , java.io.Serializable, Cloneable {
  public static final long serialVersionUID = -7046029254386353129L;

  protected EmptySet() {}

  public boolean remove( char ok ) { throw new UnsupportedOperationException(); }
  public Object clone() { return EMPTY_SET; }
        private Object readResolve() { return EMPTY_SET; }
 }


 /** An empty set (immutable). It is serializable and cloneable.
	 *
	 * <P>The class of this objects represent an abstract empty set
	 * that is a subset of a (sorted) type-specific set.
	 */
 @SuppressWarnings("unchecked")
 public static final EmptySet EMPTY_SET = new EmptySet();



 /** An immutable class representing a type-specific singleton set.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific set.	 */

 public static class Singleton extends AbstractCharSet implements java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final char element;

  protected Singleton( final char element ) {
   this.element = element;
  }

  public boolean add( final char k ) { throw new UnsupportedOperationException(); }

  public boolean contains( final char k ) { return ( (k) == (element) ); }

  public boolean addAll( final Collection<? extends Character> c ) { throw new UnsupportedOperationException(); }
  public boolean removeAll( final Collection<?> c ) { throw new UnsupportedOperationException(); }
  public boolean retainAll( final Collection<?> c ) { throw new UnsupportedOperationException(); }


  /* Slightly optimized w.r.t. the one in ABSTRACT_SET. */

  public char[] toCharArray() {
   char a[] = new char[ 1 ];
   a[ 0 ] = element;
   return a;
  }

  public boolean addAll( final CharCollection c ) { throw new UnsupportedOperationException(); }
  public boolean removeAll( final CharCollection c ) { throw new UnsupportedOperationException(); }
  public boolean retainAll( final CharCollection c ) { throw new UnsupportedOperationException(); }

  @SuppressWarnings("unchecked")
  public CharListIterator iterator() { return CharIterators.singleton( element ); }

  public int size() { return 1; }

  public Object clone() { return this; }
 }



 /** Returns a type-specific immutable set containing only the specified element. The returned set is serializable and cloneable.
	 *
	 * @param element the only element of the returned set.
	 * @return a type-specific immutable set containing just <code>element</code>.
	 */

 public static CharSet singleton( final char element ) {
  return new Singleton ( element );
 }





 /** Returns a type-specific immutable set containing only the specified element. The returned set is serializable and cloneable.
	 *
	 * @param element the only element of the returned set.
	 * @return a type-specific immutable set containing just <code>element</code>.
	 */

 public static CharSet singleton( final Character element ) {
  return new Singleton ( ((element).charValue()) );
 }



 /** A synchronized wrapper class for sets. */

 public static class SynchronizedSet extends CharCollections.SynchronizedCollection implements CharSet , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected SynchronizedSet( final CharSet s, final Object sync ) {
   super( s, sync );
  }

  protected SynchronizedSet( final CharSet s ) {
   super( s );
  }

  public boolean remove( final char k ) { synchronized( sync ) { return collection.remove( (Character.valueOf(k)) ); } }
  public boolean equals( final Object o ) { synchronized( sync ) { return collection.equals( o ); } }
  public int hashCode() { synchronized( sync ) { return collection.hashCode(); } }
 }


 /** Returns a synchronized type-specific set backed by the given type-specific set.
	 *
	 * @param s the set to be wrapped in a synchronized set.
	 * @return a synchronized view of the specified set.
	 * @see java.util.Collections#synchronizedSet(Set)
	 */
 public static CharSet synchronize( final CharSet s ) { return new SynchronizedSet ( s ); }

 /** Returns a synchronized type-specific set backed by the given type-specific set, using an assigned object to synchronize.
	 *
	 * @param s the set to be wrapped in a synchronized set.
	 * @param sync an object that will be used to synchronize the access to the set.
	 * @return a synchronized view of the specified set.
	 * @see java.util.Collections#synchronizedSet(Set)
	 */

 public static CharSet synchronize( final CharSet s, final Object sync ) { return new SynchronizedSet ( s, sync ); }



 /** An unmodifiable wrapper class for sets. */

 public static class UnmodifiableSet extends CharCollections.UnmodifiableCollection implements CharSet , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected UnmodifiableSet( final CharSet s ) {
   super( s );
  }

  public boolean remove( final char k ) { throw new UnsupportedOperationException(); }
  public boolean equals( final Object o ) { return collection.equals( o ); }
  public int hashCode() { return collection.hashCode(); }
 }


 /** Returns an unmodifiable type-specific set backed by the given type-specific set.
	 *
	 * @param s the set to be wrapped in an unmodifiable set.
	 * @return an unmodifiable view of the specified set.
	 * @see java.util.Collections#unmodifiableSet(Set)
	 */
 public static CharSet unmodifiable( final CharSet s ) { return new UnmodifiableSet ( s ); }
}
