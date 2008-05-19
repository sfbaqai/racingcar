package net.sourceforge.jFuzzyLogic.rule;

import gnu.trove.TObjectHashingStrategy;
import it.unimi.dsi.fastutil.chars.CharArrays;
import it.unimi.dsi.lang.MutableString;

/**
 * @author kokichi3000
 *
 */
public class MutableStringStrategy implements TObjectHashingStrategy<MutableString> {

	/* (non-Javadoc)
	 * @see gnu.trove.TObjectHashingStrategy#computeHashCode(java.lang.Object)
	 */
	public int computeHashCode(MutableString arg0) {
//		// TODO Auto-generated method stub
		 char[] c = arg0.toCharArray();
         // use the shift-add-xor class of string hashing functions
         // cf. Ramakrishna and Zobel, "Performance in Practice of String Hashing Functions"
         int h = 31; // seed chosen at random
         for (int i = 0; i < c.length; i++) { // could skip invariants
             h = h ^ ((h << 5) + (h >> 2) + c[i]); // L=5, R=2 works well for ASCII input
         }
         return h;
//		return arg0.hashCode();
	}

	/* (non-Javadoc)
	 * @see gnu.trove.TObjectHashingStrategy#equals(java.lang.Object, java.lang.Object)
	 */
	public boolean equals(MutableString o1, MutableString o2) {
		char[] c1 = o1.toCharArray();
        char[] c2 = o2.toCharArray();

        return CharArrays.equals(c1, c2);
	}

}
