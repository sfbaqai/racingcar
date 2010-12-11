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
 * Copyright (C) 2003-2008 Paolo Boldi and Sebastiano Vigna 
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

package it.unimi.dsi.fastutil.floats;





/** A class providing static methods and objects that do useful things with comparators.
 */

public class FloatComparators {

 private FloatComparators() {}

 /** A type-specific comparator mimicking the natural order. */

 @SuppressWarnings("unchecked")
 public static final FloatComparator NATURAL_COMPARATOR = new AbstractFloatComparator() {
   public final int compare( final float a, final float b ) {

    return ( (a) < (b) ? -1 : ( (a) == (b) ? 0 : 1 ) );



   }
  };

 /** A type-specific comparator mimicking the opposite of the natural order. */

 @SuppressWarnings("unchecked")
 public static final FloatComparator OPPOSITE_COMPARATOR = new AbstractFloatComparator() {
   public final int compare( final float a, final float b ) {

    return ( (b) < (a) ? -1 : ( (b) == (a) ? 0 : 1 ) );



   }
  };

 /** Returns a comparator representing the opposite order of the given comparator. 
	 *
	 * @param c a comparator.
	 * @return a comparator representing the opposite order of <code>c</code>.
	 */
 public static FloatComparator oppositeComparator( final FloatComparator c ) {
  return new AbstractFloatComparator () {
    private final FloatComparator comparator = c;
    public final int compare( final float a, final float b ) {
     return - comparator.compare( a, b );
    }
   };
 }
}
