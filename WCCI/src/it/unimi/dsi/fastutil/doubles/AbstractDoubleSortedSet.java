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
 * Copyright (C) 2003-2008 Sebastiano Vigna 
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

/** An abstract class providing basic methods for sorted sets implementing a type-specific interface. */

public abstract class AbstractDoubleSortedSet extends AbstractDoubleSet implements DoubleSortedSet {

 protected AbstractDoubleSortedSet() {}


 /** Delegates to the corresponding type-specific method. */
 public DoubleSortedSet headSet( final Double to ) {
  return headSet( to.doubleValue() );
 }

 /** Delegates to the corresponding type-specific method. */
 public DoubleSortedSet tailSet( final Double from ) {
  return tailSet( from.doubleValue() );
 }

 /** Delegates to the corresponding type-specific method. */
 public DoubleSortedSet subSet( final Double from, final Double to ) {
  return subSet( from.doubleValue(), to.doubleValue() );
 }

 /** Delegates to the corresponding type-specific method. */
 public Double first() {
  return (Double.valueOf(firstDouble()));
 }

 /** Delegates to the corresponding type-specific method. */
 public Double last() {
  return (Double.valueOf(lastDouble()));
 }


 /** Delegates to the new covariantly stronger generic method. */

 @Deprecated
 public DoubleBidirectionalIterator doubleIterator() {
  return iterator();
 }

 public abstract DoubleBidirectionalIterator iterator();
}
