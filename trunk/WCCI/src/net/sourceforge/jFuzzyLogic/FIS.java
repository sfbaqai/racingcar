package net.sourceforge.jFuzzyLogic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import net.sourceforge.jFuzzyLogic.fcl.FCLLexer;
import net.sourceforge.jFuzzyLogic.fcl.FCLParser;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;
import antlr.CommonAST;
import antlr.collections.AST;
import antlr.debug.misc.ASTFrame;

/**
 * Fuzzy inference system
 * A complete inference system contains:
 * 		- input variables
 * 		- output variables
 * 		- fuzzifiers
 * 		- defuzzifiers
 * 		- rules
 *
 * @author pcingola@users.sourceforge.net
 */
public class FIS {

	public static boolean debug = false;

	//-------------------------------------------------------------------------
	// Variables
	//-------------------------------------------------------------------------

	/**
	 * Several ruleSets indexed by name
	 * 	- Each 'RuleSet' contains 'Variables' and 'FuzzyRules'.
	 *	- 'Varibles' contain 'Deffuzyfiers' ans 'LinguisticTerms'
	 * 	- 'LinguisticTerms' contain 'MembershipFunctions'
	 */
	HashMap<String, FuzzyRuleSet> ruleSets;

	//-------------------------------------------------------------------------
	// Static Methods
	//-------------------------------------------------------------------------

	/**
	 * Create a "Fuzzy inference system (FIS)" from an FCL definition string
	 * @param lexer : lexer to use
	 * @param verbose : be verbose?
	 * @return A new FIS (or null on error)
	 */
	private static FIS createFromLexer(FCLLexer lexer, boolean verbose) {
		FIS fis = new FIS();
		try {
			FCLParser parser = new FCLParser(lexer);
			parser.fcl();
			CommonAST parseTree = (CommonAST) parser.getAST();

			// Error loading file?
			if( parseTree == null ) {
				System.err.println("Can't create FIS");
				return null;
			}
			Gpr.debug(verbose, "Tree: " + parseTree.toStringTree());

			// Add every FuzzyRuleSet (there may be more than one in each FCL file)
			for( AST child = parseTree; child != null; child = child.getNextSibling() ) {
				// Create a new ruleset
				FuzzyRuleSet ruleSet = new FuzzyRuleSet();

				// Generate fuzzyRuleSet based on tree
				String ruleSetName = ruleSet.fclTree(child);
				Gpr.debug(debug, "Ruleset Name: '" + ruleSetName + "'");
				fis.addFuzzyRuleSet(ruleSetName, ruleSet);

				// Show tree?
				if( verbose ) {
					ASTFrame astFrame = new ASTFrame("Tree", parseTree);
					astFrame.setVisible(true);
				}
			}
		} catch(Exception e) {
			Gpr.debug("Exception: " + e);
			e.printStackTrace();
			return null;
		}

		return fis;
	}

	/**
	 * Create a "Fuzzy inference system (FIS)" from an FCL definition string
	 * @param fclDefinition : FCL definition
	 * @param verbose : Be verbose?
	 * @return A new FIS or null on error
	 */
	public static FIS createFromString(String fclDefinition, boolean verbose) {
		// Parse string (lexer first, then parser)
		StringReader stringReader = new StringReader(fclDefinition);
		FCLLexer lexer = new FCLLexer(stringReader);
		// Parse tree and create FIS
		return createFromLexer(lexer, verbose);
	}

	/**
	 * Load an FCL file and create a "Fuzzy inference system (FIS)"
	 * @param fileName : FCL file name
	 * @return A new FIS or null on error
	 */
	public static FIS load(String fileName) {
		return load(fileName, false);
	}

	/**
	 * Load an FCL file and create a "Fuzzy inference system (FIS)"
	 * @param fileName : FCL file name
	 * @param verbose : Be verbose?
	 * @return A new FIS or null on error
	 */
	public static FIS load(String fileName, boolean verbose) {
		// Parse file (lexer first, then parser)
		FCLLexer lexer;
		try {
			lexer = new FCLLexer(new FileInputStream(fileName));
		} catch(FileNotFoundException e) {
			System.err.println("File '" + fileName + "' not found ");
			return null;
		}

		// Parse tree and create FIS
		return createFromLexer(lexer, verbose);
	}

	//-------------------------------------------------------------------------
	// Constructors
	//-------------------------------------------------------------------------

	/**
	 * Default constructor
	 */
	public FIS() {
		ruleSets = new HashMap<String, FuzzyRuleSet>();
	}

	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------

	/**
	 * Add a new ruleset
	 * @param ruleSetName : Fuzzy rule set name
	 * @param ruleSet : Fuzzy rule set
	 * @return this
	 */
	public FIS addFuzzyRuleSet(String ruleSetName, FuzzyRuleSet ruleSet) {
		ruleSets.put(ruleSetName, ruleSet);
		return this;
	}

	/**
	 * Get a fuzzy rule set (first I can find, if there are more that one)
	 * @return Fuzzy rule set (or null if not found)
	 */
	public FuzzyRuleSet getFuzzyRuleSet() {
		return getFuzzyRuleSet(null);
	}

	/**
	 * Get a fuzzy rule set
	 * @param ruleSetName : Fuzzy rule set's name
	 * @return Fuzzy rule set (or null if not found)
	 */
	public FuzzyRuleSet getFuzzyRuleSet(String ruleSetName) {
		if( ruleSetName == null ) {
			ruleSetName = ruleSets.keySet().iterator().next();
		}
		return ruleSets.get(ruleSetName);
	}

	@Override
	public String toString() {
		return toString(false);
	}

	private String toString(boolean useFCL) {
		StringBuffer out = new StringBuffer();

		// Sort ruleSets by name
		ArrayList al = new ArrayList(ruleSets.keySet());
		Collections.sort(al);

		// Iterate over each ruleSet and append it to output string
		for( Iterator it = al.iterator(); it.hasNext(); ) {
			String ruleSetName = (String) it.next();
			FuzzyRuleSet ruleSet = getFuzzyRuleSet(ruleSetName);

			// Convert ruleSet to string (using FLC?)
			if( useFCL ) out.append(ruleSet.toStringFCL());
			else out.append(ruleSet.toString());
		}

		return out.toString();
	}

	public String toStringFCL() {
		return toString(true);
	}
}
