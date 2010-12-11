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

/** A class providing static methods and objects that do useful things with type-specific functions.
 *
 * @see it.unimi.dsi.fastutil.Function
 * @see java.util.Collections
 */

public class Int2IntFunctions {

 private Int2IntFunctions() {}


 /** An immutable class representing an empty type-specific function.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific function.
	 */

 public static class EmptyFunction extends AbstractInt2IntFunction implements java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected EmptyFunction() {}

  public int get( final int k ) { return (0); }

  public boolean containsKey( final int k ) { return false; }

  public int defaultReturnValue() { return (0); }
  public void defaultReturnValue( final int defRetValue ) { throw new UnsupportedOperationException(); }


  public Integer get( final Object k ) { return null; }


  public int size() { return 0; }
  public void clear() {}

  private Object readResolve() { return EMPTY_FUNCTION; }

  public Object clone() { return EMPTY_FUNCTION; }
 }



 /** An empty type-specific function (immutable). It is serializable and cloneable. */

 @SuppressWarnings("unchecked")
 public static final EmptyFunction EMPTY_FUNCTION = new EmptyFunction();


 /** An immutable class representing a type-specific singleton function.	 
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific function.
	 */

 public static class Singleton extends AbstractInt2IntFunction implements java.io.Serializable, Cloneable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final int key;
  protected final int value;

  protected Singleton( final int key, final int value ) {
   this.key = key;
   this.value = value;
  }

  public boolean containsKey( final int k ) { return ( (key) == (k) ); }

  public int get( final int k ) { if ( ( (key) == (k) ) ) return value; return defRetValue; }

  public int size() { return 1; }

  public Object clone() { return this; }
 }

 /** Returns a type-specific immutable function containing only the specified pair. The returned function is serializable and cloneable.
	 *
	 * <P>Note that albeit the returned function is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned function.
	 * @param value the only value of the returned function.
	 * @return a type-specific immutable function containing just the pair <code>&lt;key,value></code>.
	 */

 public static Int2IntFunction singleton( final int key, int value ) {
  return new Singleton ( key, value );
 }



 /** Returns a type-specific immutable function containing only the specified pair. The returned function is serializable and cloneable.
	 *
	 * <P>Note that albeit the returned function is immutable, its default return value may be changed.
	 *
	 * @param key the only key of the returned function.
	 * @param value the only value of the returned function.
	 * @return a type-specific immutable function containing just the pair <code>&lt;key,value></code>.
	 */

 public static Int2IntFunction singleton( final Integer key, final Integer value ) {
  return new Singleton ( ((key).intValue()), ((value).intValue()) );
 }




 /** A synchronized wrapper class for functions. */

 public static class SynchronizedFunction extends AbstractInt2IntFunction implements java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Int2IntFunction function;
  protected final Object sync;

  protected SynchronizedFunction( final Int2IntFunction f, final Object sync ) {
   if ( f == null ) throw new NullPointerException();
   this.function = f;
   this.sync = sync;
  }

  protected SynchronizedFunction( final Int2IntFunction f ) {
   if ( f == null ) throw new NullPointerException();
   this.function = f;
   this.sync = this;
  }

  public int size() { synchronized( sync ) { return function.size(); } }
  public boolean containsKey( final int k ) { synchronized( sync ) { return function.containsKey( k ); } }

  public int defaultReturnValue() { synchronized( sync ) { return function.defaultReturnValue(); } }
  public void defaultReturnValue( final int defRetValue ) { synchronized( sync ) { function.defaultReturnValue( defRetValue ); } }

  public int put( final int k, final int v ) { synchronized( sync ) { return function.put( k, v ); } }

  public void clear() { synchronized( sync ) { function.clear(); } }
  public String toString() { synchronized( sync ) { return function.toString(); } }


  public Integer put( final Integer k, final Integer v ) { synchronized( sync ) { return function.put( k, v ); } }
  public Integer get( final Object k ) { synchronized( sync ) { return function.get( k ); } }
  public Integer remove( final Object k ) { synchronized( sync ) { return function.remove( k ); } }



  public int remove( final int k ) { synchronized( sync ) { return function.remove( k ); } }
  public int get( final int k ) { synchronized( sync ) { return function.get( k ); } }
  public boolean containsKey( final Object ok ) { synchronized( sync ) { return function.containsKey( ok ); } }







 }

 /** Returns a synchronized type-specific function backed by the given type-specific function.
	 *
	 * @param f the function to be wrapped in a synchronized function.
	 * @return a synchronized view of the specified function.
	 * @see java.util.Collections#synchronizedMap(java.util.Map)
	 */
 public static Int2IntFunction synchronize( final Int2IntFunction f ) { return new SynchronizedFunction ( f ); }

 /** Returns a synchronized type-specific function backed by the given type-specific function, using an assigned object to synchronize.
	 *
	 * @param f the function to be wrapped in a synchronized function.
	 * @param sync an object that will be used to synchronize the access to the function.
	 * @return a synchronized view of the specified function.
	 * @see java.util.Collections#synchronizedMap(java.util.Map)
	 */

 public static Int2IntFunction synchronize( final Int2IntFunction f, final Object sync ) { return new SynchronizedFunction ( f, sync ); }



 /** An unmodifiable wrapper class for functions. */

 public static class UnmodifiableFunction extends AbstractInt2IntFunction implements java.io.Serializable {

  public static final long serialVersionUID = -7046029254386353129L;

  protected final Int2IntFunction function;

  protected UnmodifiableFunction( final Int2IntFunction f ) {
   if ( f == null ) throw new NullPointerException();
   this.function = f;
  }

  public int size() { return function.size(); }
  public boolean containsKey( final int k ) { return function.containsKey( k ); }

  public int defaultReturnValue() { return defaultReturnValue(); }
  public void defaultReturnValue( final int defRetValue ) { throw new UnsupportedOperationException(); }

  public int put( final int k, final int v ) { throw new UnsupportedOperationException(); }

  public void clear() { throw new UnsupportedOperationException(); }
  public String toString() { return function.toString(); }


  public int remove( final int k ) { throw new UnsupportedOperationException(); }
  public int get( final int k ) { return function.get( k ); }
  public boolean containsKey( final Object ok ) { return function.containsKey( ok ); }







 }

 /** Returns an unmodifiable type-specific function backed by the given type-specific function.
	 *
	 * @param f the function to be wrapped in an unmodifiable function.
	 * @return an unmodifiable view of the specified function.
	 * @see java.util.Collections#unmodifiableMap(java.util.Map)
	 */
 public static Int2IntFunction unmodifiable( final Int2IntFunction f ) { return new UnmodifiableFunction ( f ); }
}
