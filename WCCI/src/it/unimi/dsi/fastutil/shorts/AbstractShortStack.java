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

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.AbstractStack;

/** An abstract class providing basic methods for implementing a type-specific stack interface.
 *
 * <P>To create a type-specific stack, you need both object methods and
 * primitive-type methods. However, if you inherit from this class you need
 * just one (anyone).
 */

public abstract class AbstractShortStack extends AbstractStack<Short> implements ShortStack {

 protected AbstractShortStack() {}

 /** Delegates to the corresponding type-specific method. */
 public void push( Short o ) {
  push( o.shortValue() );
 }

 /** Delegates to the corresponding type-specific method. */
 public Short pop() {
  return Short.valueOf( popShort() );
 }

 /** Delegates to the corresponding type-specific method. */
 public Short top() {
  return Short.valueOf( topShort() );
 }

 /** Delegates to the corresponding type-specific method. */
 public Short peek( int i ) {
  return Short.valueOf( peekShort( i ) );
 }

 /** Delegates to the corresponding generic method. */
 public void push( short k ) {
  push( Short.valueOf( k ) );
 }

 /** Delegates to the corresponding generic method. */
 public short popShort() {
  return pop().shortValue();
 }

 /** Delegates to the corresponding generic method. */
 public short topShort() {
  return top().shortValue();
 }

 /** Delegates to the corresponding generic method. */
 public short peekShort( int i ) {
  return peek( i ).shortValue();
 }
}
