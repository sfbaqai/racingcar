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

package it.unimi.dsi.fastutil.doubles;

import java.util.Collection;
import java.util.Set;

/** A class providing static methods and objects that do useful things with type-specific sets.
 *
 * @see java.util.Collections
 */

public class DoubleSets {

 private DoubleSets() {}

 /** An immutable class representing the empty set and implementing a type-specific set interface.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific set.
	 */

 public static class EmptySet extends DoubleCollections.EmptyCollection implements DoubleSet , java.io.Serializable, Cloneable {
  public static final long serialVersionUID = -7046029254386353129L;

  protected EmptySet() {}

  public boolean remove( double ok ) { throw new UnsupportedOperationException(); }
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

 public static class Singleton extends AbstractDoubleSet implements java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final double element;

  protected Singleton( final double element ) {
   this.element = element;
  }

  public boolean add( final double k ) { throw new UnsupportedOperationException(); }

  public boolean contains( final double k ) { return ( (k) == (element) ); }

  public boolean addAll( final Collection<? extends Double> c ) { throw new UnsupportedOperationException(); }
  public boolean removeAll( final Collection<?> c ) { throw new UnsupportedOperationException(); }
  public boolean retainAll( final Collection<?> c ) { throw new UnsupportedOperationException(); }


  /* Slightly optimized w.r.t. the one in ABSTRACT_SET. */

  public double[] toDoubleArray() {
   double a[] = new double[ 1 ];
   a[ 0 ] = element;
   return a;
  }

  public boolean addAll( final DoubleCollection c ) { throw new UnsupportedOperationException(); }
  public boolean removeAll( final DoubleCollection c ) { throw new UnsupportedOperationException(); }
  public boolean retainAll( final DoubleCollection c ) { throw new UnsupportedOperationException(); }

  @SuppressWarnings("unchecked")
  public DoubleListIterator iterator() { return DoubleIterators.singleton( element ); }

  public int size() { return 1; }

  public Object clone() { return this; }
 }



 /** Returns a type-specific immutable set containing only the specified element. The returned set is serializable and cloneable.
	 *
	 * @param element the only element of the returned set.
	 * @return a type-specific immutable set containing just <code>element</code>.
	 */

 public static DoubleSet singleton( final double element ) {
  return new Singleton ( element );
 }





 /** Returns a type-specific immutable set containing only the specified element. The returned set is serializable and cloneable.
	 *
	 * @param element the only element of the returned set.
	 * @return a type-specific immutable set containing just <code>element</code>.
	 */

 public static DoubleSet singleton( final Double element ) {
  return new Singleton ( ((element).doubleValue()) );
 }



 /** A synchronized wrapper class for sets. */

 public static class SynchronizedSet extends DoubleCollections.SynchronizedCollection implements DoubleSet , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected SynchronizedSet( final DoubleSet s, final Object sync ) {
   super( s, sync );
  }

  protected SynchronizedSet( final DoubleSet s ) {
   super( s );
  }

  public boolean remove( final double k ) { synchronized( sync ) { return collection.remove( (Double.valueOf(k)) ); } }
  public boolean equals( final Object o ) { synchronized( sync ) { return collection.equals( o ); } }
  public int hashCode() { synchronized( sync ) { return collection.hashCode(); } }
 }


 /** Returns a synchronized type-specific set backed by the given type-specific set.
	 *
	 * @param s the set to be wrapped in a synchronized set.
	 * @return a synchronized view of the specified set.
	 * @see java.util.Collections#synchronizedSet(Set)
	 */
 public static DoubleSet synchronize( final DoubleSet s ) { return new SynchronizedSet ( s ); }

 /** Returns a synchronized type-specific set backed by the given type-specific set, using an assigned object to synchronize.
	 *
	 * @param s the set to be wrapped in a synchronized set.
	 * @param sync an object that will be used to synchronize the access to the set.
	 * @return a synchronized view of the specified set.
	 * @see java.util.Collections#synchronizedSet(Set)
	 */

 public static DoubleSet synchronize( final DoubleSet s, final Object sync ) { return new SynchronizedSet ( s, sync ); }



 /** An unmodifiable wrapper class for sets. */

 public static class UnmodifiableSet extends DoubleCollections.UnmodifiableCollection implements DoubleSet , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected UnmodifiableSet( final DoubleSet s ) {
   super( s );
  }

  public boolean remove( final double k ) { throw new UnsupportedOperationException(); }
  public boolean equals( final Object o ) { return collection.equals( o ); }
  public int hashCode() { return collection.hashCode(); }
 }


 /** Returns an unmodifiable type-specific set backed by the given type-specific set.
	 *
	 * @param s the set to be wrapped in an unmodifiable set.
	 * @return an unmodifiable view of the specified set.
	 * @see java.util.Collections#unmodifiableSet(Set)
	 */
 public static DoubleSet unmodifiable( final DoubleSet s ) { return new UnmodifiableSet ( s ); }
}
