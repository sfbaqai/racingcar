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

package it.unimi.dsi.fastutil.ints;

/** An abstract class providing basic methods for sorted sets implementing a type-specific interface. */

public abstract class AbstractIntSortedSet extends AbstractIntSet implements IntSortedSet {

 protected AbstractIntSortedSet() {}


 /** Delegates to the corresponding type-specific method. */
 public IntSortedSet headSet( final Integer to ) {
  return headSet( to.intValue() );
 }

 /** Delegates to the corresponding type-specific method. */
 public IntSortedSet tailSet( final Integer from ) {
  return tailSet( from.intValue() );
 }

 /** Delegates to the corresponding type-specific method. */
 public IntSortedSet subSet( final Integer from, final Integer to ) {
  return subSet( from.intValue(), to.intValue() );
 }

 /** Delegates to the corresponding type-specific method. */
 public Integer first() {
  return (Integer.valueOf(firstInt()));
 }

 /** Delegates to the corresponding type-specific method. */
 public Integer last() {
  return (Integer.valueOf(lastInt()));
 }


 /** Delegates to the new covariantly stronger generic method. */

 @Deprecated
 public IntBidirectionalIterator intIterator() {
  return iterator();
 }

 public abstract IntBidirectionalIterator iterator();
}
