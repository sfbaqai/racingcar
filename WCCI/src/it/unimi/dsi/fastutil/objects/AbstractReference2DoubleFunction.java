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

/** An abstract class providing basic methods for functions implementing a type-specific interface.
 *
 * <P>Optional operations just throw an {@link
 * UnsupportedOperationException}. Generic versions of accessors delegate to
 * the corresponding type-specific counterparts following the interface rules
 * (they take care of returning <code>null</code> on a missing key).
 *
 * <P>This class handles directly a default return
 * value (including {@linkplain #defaultReturnValue() methods to access
 * it}). Instances of classes inheriting from this class have just to return
 * <code>defRetValue</code> to denote lack of a key in type-specific methods. The value
 * is serialized.
 *
 * <P>Implementing subclasses have just to provide type-specific <code>get()</code>,
 * type-specific <code>containsKey()</code>, and <code>size()</cose> methods.
 *
 */

public abstract class AbstractReference2DoubleFunction <K> implements Reference2DoubleFunction <K>, java.io.Serializable {

 protected AbstractReference2DoubleFunction() {}

 /**
	 * The default return value for <code>get()</code>, <code>put()</code> and
	 * <code>remove()</code>.  
	 */

 protected double defRetValue;

 public void defaultReturnValue( final double rv ) {
  defRetValue = rv;
 }

 public double defaultReturnValue() {
  return defRetValue;
 }

 public double put( K key, double value ) {
  throw new UnsupportedOperationException();
 }

 public double removeDouble( Object key ) {
  throw new UnsupportedOperationException();
 }

 public void clear() {
  throw new UnsupportedOperationException();
 }
 /** Delegates to the corresponding type-specific method, taking care of returning <code>null</code> on a missing key.
	 *
	 * <P>This method must check whether the provided key is in the map using <code>containsKey()</code>. Thus,
	 * it probes the map <em>twice</em>. Implementors of subclasses should override it with a more efficient method.
	 */
 public Double get( final Object ok ) {
  final Object k = (ok);
  return containsKey( k ) ? (Double.valueOf(getDouble( k ))) : null;
 }

 /** Delegates to the corresponding type-specific method, taking care of returning <code>null</code> on a missing key. 
	 *
	 * <P>This method must check whether the provided key is in the map using <code>containsKey()</code>. Thus,
	 * it probes the map <em>twice</em>. Implementors of subclasses should override it with a more efficient method.
	 */
 public Double put( final K ok, final Double ov ) {
  final K k = (ok);
  final double v = put( k, ((ov).doubleValue()) );
  return containsKey( k ) ? (Double.valueOf(v)) : null;
 }

 /** Delegates to the corresponding type-specific method, taking care of returning <code>null</code> on a missing key. 
	 *
	 * <P>This method must check whether the provided key is in the map using <code>containsKey()</code>. Thus,
	 * it probes the map <em>twice</em>. Implementors of subclasses should override it with a more efficient method.
	 */
 public Double remove( final Object ok ) {
  final Object k = (ok);
  final double v = removeDouble( k );
  return containsKey( k ) ? (Double.valueOf(v)) : null;
 }

}
