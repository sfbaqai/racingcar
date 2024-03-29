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

package it.unimi.dsi.fastutil.objects;


import java.util.Comparator;


/** A class providing static methods and objects that do useful things with comparators.
 */

public class ObjectComparators {

 private ObjectComparators() {}

 /** A type-specific comparator mimicking the natural order. */

 @SuppressWarnings("unchecked")
 public static final Comparator NATURAL_COMPARATOR = new Comparator() {
   public final int compare( final Object a, final Object b ) {



    return ((Comparable)a).compareTo(b);

   }
  };

 /** A type-specific comparator mimicking the opposite of the natural order. */

 @SuppressWarnings("unchecked")
 public static final Comparator OPPOSITE_COMPARATOR = new Comparator() {
   public final int compare( final Object a, final Object b ) {



    return ((Comparable)b).compareTo(a);

   }
  };

 /** Returns a comparator representing the opposite order of the given comparator. 
	 *
	 * @param c a comparator.
	 * @return a comparator representing the opposite order of <code>c</code>.
	 */
 public static <K> Comparator <K> oppositeComparator( final Comparator <K> c ) {
  return new Comparator <K>() {
    private final Comparator <K> comparator = c;
    public final int compare( final K a, final K b ) {
     return - comparator.compare( a, b );
    }
   };
 }
}
