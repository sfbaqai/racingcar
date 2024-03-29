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

import it.unimi.dsi.fastutil.AbstractStack;

/** An abstract class providing basic methods for implementing a type-specific stack interface.
 *
 * <P>To create a type-specific stack, you need both object methods and
 * primitive-type methods. However, if you inherit from this class you need
 * just one (anyone).
 */

public abstract class AbstractDoubleStack extends AbstractStack<Double> implements DoubleStack {

 protected AbstractDoubleStack() {}

 /** Delegates to the corresponding type-specific method. */
 public void push( Double o ) {
  push( o.doubleValue() );
 }

 /** Delegates to the corresponding type-specific method. */
 public Double pop() {
  return Double.valueOf( popDouble() );
 }

 /** Delegates to the corresponding type-specific method. */
 public Double top() {
  return Double.valueOf( topDouble() );
 }

 /** Delegates to the corresponding type-specific method. */
 public Double peek( int i ) {
  return Double.valueOf( peekDouble( i ) );
 }

 /** Delegates to the corresponding generic method. */
 public void push( double k ) {
  push( Double.valueOf( k ) );
 }

 /** Delegates to the corresponding generic method. */
 public double popDouble() {
  return pop().doubleValue();
 }

 /** Delegates to the corresponding generic method. */
 public double topDouble() {
  return top().doubleValue();
 }

 /** Delegates to the corresponding generic method. */
 public double peekDouble( int i ) {
  return peek( i ).doubleValue();
 }
}
