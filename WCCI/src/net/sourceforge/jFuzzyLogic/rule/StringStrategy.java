/**
 *
 */
package net.sourceforge.jFuzzyLogic.rule;

import gnu.trove.TObjectHashingStrategy;
import it.unimi.dsi.fastutil.chars.CharArrays;

/**
 * @author kokichi3000
 *
 */
public final class StringStrategy implements TObjectHashingStrategy<String> {

	/* (non-Javadoc)
	 * @see gnu.trove.TObjectHashingStrategy#computeHashCode(java.lang.Object)
	 */
	public int computeHashCode(String arg0) {
		// TODO Auto-generated method stub
		 char[] c = arg0.toCharArray();
         // use the shift-add-xor class of string hashing functions
         // cf. Ramakrishna and Zobel, "Performance in Practice of String Hashing Functions"
         int h = 31; // seed chosen at random
         for (int i = 0; i < c.length; i++) { // could skip invariants
             h = h ^ ((h << 5) + (h >> 2) + c[i]); // L=5, R=2 works well for ASCII input
         }
         return h;
	}

	/* (non-Javadoc)
	 * @see gnu.trove.TObjectHashingStrategy#equals(java.lang.Object, java.lang.Object)
	 */
	public boolean equals(String o1, String o2) {
		char[] c1 = o1.toCharArray();
        char[] c2 = o2.toCharArray();

        return CharArrays.equals(c1, c2);
	}

}
