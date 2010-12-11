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

package it.unimi.dsi.fastutil.booleans;

import java.util.List;
import java.util.Collection;

/** A class providing static methods and objects that do useful things with type-specific lists.
 *
 * @see java.util.Collections
 */

public class BooleanLists {

 private BooleanLists() {}

 /** An immutable class representing an empty type-specific list.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific list.
	 */

 public static class EmptyList extends BooleanCollections.EmptyCollection implements BooleanList , java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected EmptyList() {}

  public void add( final int index, final boolean k ) { throw new UnsupportedOperationException(); }
  public boolean add( final boolean k ) { throw new UnsupportedOperationException(); }
  public boolean removeBoolean( int i ) { throw new UnsupportedOperationException(); }
  public boolean set( final int index, final boolean k ) { throw new UnsupportedOperationException(); }

  public int indexOf( boolean k ) { return -1; }
  public int lastIndexOf( boolean k ) { return -1; }

  public boolean addAll( Collection<? extends Boolean> c ) { throw new UnsupportedOperationException(); }
  public boolean addAll( int i, Collection<? extends Boolean> c ) { throw new UnsupportedOperationException(); }
  public boolean removeAll( Collection<?> c ) { throw new UnsupportedOperationException(); }

  public Boolean get( int i ) { throw new IndexOutOfBoundsException(); }


  public boolean addAll( BooleanCollection c ) { throw new UnsupportedOperationException(); }
  public boolean addAll( BooleanList c ) { throw new UnsupportedOperationException(); }
  public boolean addAll( int i, BooleanCollection c ) { throw new UnsupportedOperationException(); }
  public boolean addAll( int i, BooleanList c ) { throw new UnsupportedOperationException(); }

  public void add( final int index, final Boolean k ) { throw new UnsupportedOperationException(); }
  public boolean add( final Boolean k ) { throw new UnsupportedOperationException(); }
  public Boolean set( final int index, final Boolean k ) { throw new UnsupportedOperationException(); }
  public boolean getBoolean( int i ) { throw new IndexOutOfBoundsException(); }

  public Boolean remove( int k ) { throw new UnsupportedOperationException(); }

  public int indexOf( Object k ) { return -1; }
  public int lastIndexOf( Object k ) { return -1; }


  //@SuppressWarnings("unchecked")
  //public KEY_ITERATOR KEY_GENERIC iterator( int i ) { if ( i == 0 ) return ITERATORS.EMPTY_ITERATOR; throw new IndexOutOfBoundsException( String.valueOf( i ) ); }

  @Deprecated
  @SuppressWarnings("unchecked")
  public BooleanIterator booleanIterator() { return BooleanIterators.EMPTY_ITERATOR; }

  @SuppressWarnings("unchecked")
  public BooleanListIterator listIterator() { return BooleanIterators.EMPTY_ITERATOR; }

  @SuppressWarnings("unchecked")
  public BooleanListIterator listIterator( int i ) { if ( i == 0 ) return BooleanIterators.EMPTY_ITERATOR; throw new IndexOutOfBoundsException( String.valueOf( i ) ); }

  @Deprecated
  public BooleanListIterator booleanListIterator() { return listIterator(); }

  @Deprecated
  public BooleanListIterator booleanListIterator( int i ) { return listIterator( i ); }

  public BooleanList subList( int from, int to ) { if ( from == 0 && to == 0 ) return this; throw new IndexOutOfBoundsException(); }

  @Deprecated
  public BooleanList booleanSubList( int from, int to ) { return subList( from, to ); }

  public void getElements( int from, boolean[] a, int offset, int length ) { if ( from == 0 && length == 0 && offset >= 0 && offset <= a.length ) return; throw new IndexOutOfBoundsException(); }
  public void removeElements( int from, int to ) { throw new UnsupportedOperationException(); }

  public void addElements( int index, final boolean a[], int offset, int length ) { throw new UnsupportedOperationException(); }
  public void addElements( int index, final boolean a[] ) { throw new UnsupportedOperationException(); }

  public void size( int s ) { throw new UnsupportedOperationException(); }

  public int compareTo( final List<? extends Boolean> o ) {
   if ( o == this ) return 0;
   return ((List<?>)o).isEmpty() ? 0 : -1;
  }

  private Object readResolve() { return EMPTY_LIST; }
  public Object clone() { return EMPTY_LIST; }
 }

 /** An empty list (immutable). It is serializable and cloneable. 
	 *
	 * <P>The class of this objects represent an abstract empty list
	 * that is a sublist of any type of list. Thus, {@link #EMPTY_LIST}
	 * may be assigned to a variable of any (sorted) type-specific list.
	 */

 @SuppressWarnings("unchecked")
 public static final EmptyList EMPTY_LIST = new EmptyList();



 /** An immutable class representing a type-specific singleton list. 
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific list.
	 */

 public static class Singleton extends AbstractBooleanList implements java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  private final boolean element;

  private Singleton( final boolean element ) {
   this.element = element;
  }

  public boolean getBoolean( final int i ) { if ( i == 0 ) return element; throw new IndexOutOfBoundsException(); }
  public boolean removeBoolean( final int i ) { throw new UnsupportedOperationException(); }
  public boolean contains( final boolean k ) { return ( (k) == (element) ); }

  public boolean addAll( final Collection<? extends Boolean> c ) { throw new UnsupportedOperationException(); }
  public boolean addAll( final int i, final Collection <? extends Boolean> c ) { throw new UnsupportedOperationException(); }
  public boolean removeAll( final Collection<?> c ) { throw new UnsupportedOperationException(); }
  public boolean retainAll( final Collection<?> c ) { throw new UnsupportedOperationException(); }

  /* Slightly optimized w.r.t. the one in ABSTRACT_SET. */

  public boolean[] toBooleanArray() {
   boolean a[] = new boolean[ 1 ];
   a[ 0 ] = element;
   return a;
  }

  @SuppressWarnings("unchecked")
  public BooleanListIterator listIterator() { return BooleanIterators.singleton( element ); }

  public BooleanListIterator listIterator( int i ) {
   if ( i > 1 || i < 0 ) throw new IndexOutOfBoundsException();
   BooleanListIterator l = listIterator();
   if ( i == 1 ) l.next();
   return l;
  }

  @SuppressWarnings("unchecked")
  public BooleanList subList( final int from, final int to ) {
   ensureIndex( from );
   ensureIndex( to );
   if ( from > to ) throw new IndexOutOfBoundsException( "Start index (" + from + ") is greater than end index (" + to + ")" );

   if ( from != 0 || to != 1 ) return EMPTY_LIST;
   return this;
  }

  public int size() { return 1; }
  public void size( final int size ) { throw new UnsupportedOperationException(); }
  public void clear() { throw new UnsupportedOperationException(); }

  public Object clone() { return this; }


  public boolean rem( final boolean k ) { throw new UnsupportedOperationException(); }
  public boolean addAll( final BooleanCollection c ) { throw new UnsupportedOperationException(); }
  public boolean addAll( final int i, final BooleanCollection c ) { throw new UnsupportedOperationException(); }


 }

 /** Returns a type-specific immutable list containing only the specified element. The returned list is serializable and cloneable.
	 *
	 * @param element the only element of the returned list.
	 * @return a type-specific immutable list containing just <code>element</code>.
	 */

 public static BooleanList singleton( final boolean element ) { return new Singleton ( element ); }



 /** Returns a type-specific immutable list containing only the specified element. The returned list is serializable and cloneable.
	 *
	 * @param element the only element of the returned list.
	 * @return a type-specific immutable list containing just <code>element</code>.
	 */

 public static BooleanList singleton( final Object element ) { return new Singleton ( ((((Boolean)(element)).booleanValue())) ); }




 /** A synchronized wrapper class for lists. */

 public static class SynchronizedList extends BooleanCollections.SynchronizedCollection implements BooleanList , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final BooleanList list; // Due to the large number of methods that are not in COLLECTION, this is worth caching.

  protected SynchronizedList( final BooleanList l, final Object sync ) {
   super( l, sync );
   this.list = l;
  }

  protected SynchronizedList( final BooleanList l ) {
   super( l );
   this.list = l;
  }

  public boolean getBoolean( final int i ) { synchronized( sync ) { return list.getBoolean( i ); } }
  public boolean set( final int i, final boolean k ) { synchronized( sync ) { return list.set( i, k ); } }
  public void add( final int i, final boolean k ) { synchronized( sync ) { list.add( i, k ); } }
  public boolean removeBoolean( final int i ) { synchronized( sync ) { return list.removeBoolean( i ); } }

  public int indexOf( final boolean k ) { synchronized( sync ) { return list.indexOf( k ); } }
  public int lastIndexOf( final boolean k ) { synchronized( sync ) { return list.lastIndexOf( k ); } }

  public boolean addAll( final int index, final Collection<? extends Boolean> c ) { synchronized( sync ) { return list.addAll( index, c ); } }

  public void getElements( final int from, final boolean a[], final int offset, final int length ) { synchronized( sync ) { list.getElements( from, a, offset, length ); } }
  public void removeElements( final int from, final int to ) { synchronized( sync ) { list.removeElements( from, to ); } }
  public void addElements( int index, final boolean a[], int offset, int length ) { synchronized( sync ) { list.addElements( index, a, offset, length ); } }
  public void addElements( int index, final boolean a[] ) { synchronized( sync ) { list.addElements( index, a ); } }
  public void size( final int size ) { synchronized( sync ) { list.size( size ); } }

  public BooleanListIterator listIterator() { return list.listIterator(); }
  public BooleanListIterator listIterator( final int i ) { return list.listIterator( i ); }

  @Deprecated
  public BooleanListIterator booleanListIterator() { return listIterator(); }

  @Deprecated
  public BooleanListIterator booleanListIterator( final int i ) { return listIterator( i ); }

  public BooleanList subList( final int from, final int to ) { synchronized( sync ) { return synchronize( list.subList( from, to ), sync ); } }

  @Deprecated
  public BooleanList booleanSubList( final int from, final int to ) { return subList( from, to ); }

  public boolean equals( final Object o ) { synchronized( sync ) { return collection.equals( o ); } }
  public int hashCode() { synchronized( sync ) { return collection.hashCode(); } }


  public int compareTo( final List<? extends Boolean> o ) { synchronized( sync ) { return list.compareTo( o ); } }



  public boolean addAll( final int index, final BooleanCollection c ) { synchronized( sync ) { return list.addAll( index, c ); } }
  public boolean addAll( final int index, BooleanList l ) { synchronized( sync ) { return list.addAll( index, l ); } }
  public boolean addAll( BooleanList l ) { synchronized( sync ) { return list.addAll( l ); } }

  public Boolean get( final int i ) { synchronized( sync ) { return list.get( i ); } }
  public void add( final int i, Boolean k ) { synchronized( sync ) { list.add( i, k ); } }
  public Boolean set( final int index, Boolean k ) { synchronized( sync ) { return list.set( index, k ); } }
  public Boolean remove( final int i ) { synchronized( sync ) { return list.remove( i ); } }
  public int indexOf( final Object o ) { synchronized( sync ) { return list.indexOf( o ); } }
  public int lastIndexOf( final Object o ) { synchronized( sync ) { return list.lastIndexOf( o ); } }

 }


 /** Returns a synchronized type-specific list backed by the given type-specific list.
	 *
	 * @param l the list to be wrapped in a synchronized list.
	 * @return a synchronized view of the specified list.
	 * @see java.util.Collections#synchronizedList(List)
	 */
 public static BooleanList synchronize( final BooleanList l ) { return new SynchronizedList ( l ); }

 /** Returns a synchronized type-specific list backed by the given type-specific list, using an assigned object to synchronize.
	 *
	 * @param l the list to be wrapped in a synchronized list.
	 * @param sync an object that will be used to synchronize the access to the list.
	 * @return a synchronized view of the specified list.
	 * @see java.util.Collections#synchronizedList(List)
	 */

 public static BooleanList synchronize( final BooleanList l, final Object sync ) { return new SynchronizedList ( l, sync ); }



 /** An unmodifiable wrapper class for lists. */

 public static class UnmodifiableList extends BooleanCollections.UnmodifiableCollection implements BooleanList , java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final BooleanList list; // Due to the large number of methods that are not in COLLECTION, this is worth caching.

  protected UnmodifiableList( final BooleanList l ) {
   super( l );
   this.list = l;
  }

  public boolean getBoolean( final int i ) { return list.getBoolean( i ); }
  public boolean set( final int i, final boolean k ) { throw new UnsupportedOperationException(); }
  public void add( final int i, final boolean k ) { throw new UnsupportedOperationException(); }
  public boolean removeBoolean( final int i ) { throw new UnsupportedOperationException(); }

  public int indexOf( final boolean k ) { return list.indexOf( k ); }
  public int lastIndexOf( final boolean k ) { return list.lastIndexOf( k ); }

  public boolean addAll( final int index, final Collection<? extends Boolean> c ) { throw new UnsupportedOperationException(); }

  public void getElements( final int from, final boolean a[], final int offset, final int length ) { list.getElements( from, a, offset, length ); }
  public void removeElements( final int from, final int to ) { throw new UnsupportedOperationException(); }
  public void addElements( int index, final boolean a[], int offset, int length ) { throw new UnsupportedOperationException(); }
  public void addElements( int index, final boolean a[] ) { throw new UnsupportedOperationException(); }
  public void size( final int size ) { list.size( size ); }

  public BooleanListIterator listIterator() { return BooleanIterators.unmodifiable( list.listIterator() ); }
  public BooleanListIterator listIterator( final int i ) { return BooleanIterators.unmodifiable( list.listIterator( i ) ); }

  @Deprecated
  public BooleanListIterator booleanListIterator() { return listIterator(); }

  @Deprecated
  public BooleanListIterator booleanListIterator( final int i ) { return listIterator( i ); }

  public BooleanList subList( final int from, final int to ) { return unmodifiable( list.subList( from, to ) ); }

  @Deprecated
  public BooleanList booleanSubList( final int from, final int to ) { return subList( from, to ); }

  public boolean equals( final Object o ) { return collection.equals( o ); }
  public int hashCode() { return collection.hashCode(); }


  public int compareTo( final List<? extends Boolean> o ) { return list.compareTo( o ); }



  public boolean addAll( final int index, final BooleanCollection c ) { throw new UnsupportedOperationException(); }
  public boolean addAll( final BooleanList l ) { throw new UnsupportedOperationException(); }
  public boolean addAll( final int index, final BooleanList l ) { throw new UnsupportedOperationException(); }
  public Boolean get( final int i ) { return list.get( i ); }
  public void add( final int i, Boolean k ) { throw new UnsupportedOperationException(); }
  public Boolean set( final int index, Boolean k ) { throw new UnsupportedOperationException(); }
  public Boolean remove( final int i ) { throw new UnsupportedOperationException(); }
  public int indexOf( final Object o ) { return list.indexOf( o ); }
  public int lastIndexOf( final Object o ) { return list.lastIndexOf( o ); }

 }


 /** Returns an unmodifiable type-specific list backed by the given type-specific list.
	 *
	 * @param l the list to be wrapped in an unmodifiable list.
	 * @return an unmodifiable view of the specified list.
	 * @see java.util.Collections#unmodifiableList(List)
	 */
 public static BooleanList unmodifiable( final BooleanList l ) { return new UnmodifiableList ( l ); }
}
