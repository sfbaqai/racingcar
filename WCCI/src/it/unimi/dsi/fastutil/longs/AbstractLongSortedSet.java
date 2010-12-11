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

package it.unimi.dsi.fastutil.longs;

/** An abstract class providing basic methods for sorted sets implementing a type-specific interface. */

public abstract class AbstractLongSortedSet extends AbstractLongSet implements LongSortedSet {

 protected AbstractLongSortedSet() {}


 /** Delegates to the corresponding type-specific method. */
 public LongSortedSet headSet( final Long to ) {
  return headSet( to.longValue() );
 }

 /** Delegates to the corresponding type-specific method. */
 public LongSortedSet tailSet( final Long from ) {
  return tailSet( from.longValue() );
 }

 /** Delegates to the corresponding type-specific method. */
 public LongSortedSet subSet( final Long from, final Long to ) {
  return subSet( from.longValue(), to.longValue() );
 }

 /** Delegates to the corresponding type-specific method. */
 public Long first() {
  return (Long.valueOf(firstLong()));
 }

 /** Delegates to the corresponding type-specific method. */
 public Long last() {
  return (Long.valueOf(lastLong()));
 }


 /** Delegates to the new covariantly stronger generic method. */

 @Deprecated
 public LongBidirectionalIterator longIterator() {
  return iterator();
 }

 public abstract LongBidirectionalIterator iterator();
}
