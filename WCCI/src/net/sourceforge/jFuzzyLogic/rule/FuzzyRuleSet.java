package net.sourceforge.jFuzzyLogic.rule;

import gnu.trove.THashMap;
import gnu.trove.TObjectProcedure;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.lang.MutableString;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.defuzzifier.Defuzzifier;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfArea;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfGravity;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfGravityFunctions;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfGravitySingletons;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierLeftMostMax;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierMeanMax;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierRightMostMax;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionFuncion;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionGaussian;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionGenBell;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionGenericSingleton;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionPieceWiseLinear;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionSigmoidal;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionSingleton;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionTrapetzoidal;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionTriangular;
import net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethoNormedSum;
import net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethod;
import net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethodBoundedSum;
import net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethodMax;
import net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethodProbOr;
import net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethodSum;
import net.sourceforge.jFuzzyLogic.ruleConnection.RuleConnectionMethod;
import net.sourceforge.jFuzzyLogic.ruleConnection.RuleConnectionMethodAndBoundedDif;
import net.sourceforge.jFuzzyLogic.ruleConnection.RuleConnectionMethodAndMin;
import net.sourceforge.jFuzzyLogic.ruleConnection.RuleConnectionMethodAndProduct;
import net.sourceforge.jFuzzyLogic.ruleConnection.RuleConnectionMethodOrBoundedSum;
import net.sourceforge.jFuzzyLogic.ruleConnection.RuleConnectionMethodOrMax;
import net.sourceforge.jFuzzyLogic.ruleConnection.RuleConnectionMethodOrProbOr;
import net.sourceforge.jFuzzyLogic.ruleImplication.RuleImplicationMethod;
import net.sourceforge.jFuzzyLogic.ruleImplication.RuleImplicationMethodMin;
import net.sourceforge.jFuzzyLogic.ruleImplication.RuleImplicationMethodProduct;
import antlr.collections.AST;

/**
 * A set of fuzzy rules
 * @author pcingola@users.sourceforge.net
 */
public class FuzzyRuleSet {

	/** Debug mode? */
	public static boolean debug = FIS.debug;

	//-------------------------------------------------------------------------
	// Variables
	//-------------------------------------------------------------------------

	/** Rule implication method */
	RuleImplicationMethod ruleImplicationMethod;
	/** A list of rules */
	ObjectArrayList<FuzzyRule> rules;
	/** Every variable is here (key: VariableName) */
	THashMap<MutableString, Variable> variables;


	//-------------------------------------------------------------------------
	// Static Method
	//-------------------------------------------------------------------------

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		FuzzyRuleSet.debug = debug;
	}

	//-------------------------------------------------------------------------
	// Constructors
	//-------------------------------------------------------------------------

	public FuzzyRuleSet() {
		rules = new ObjectArrayList<FuzzyRule>();
		ruleImplicationMethod = new RuleImplicationMethodMin(); // Default implication method: Min
	}

	/**
	 * Add a rule to this ruleSet
	 * @param fuzzyRule : Rule to add
	 * @return this
	 */
	public FuzzyRuleSet add(FuzzyRule fuzzyRule) {
		rules.add(fuzzyRule);
		return this;
	}

	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------

	/** Show a chart for each variable in this ruleSet */
	public void chart() {
		for( Iterator<Variable> it = variablesIterator(); it.hasNext(); ) {
			Variable var = it.next();
			var.chart(true);
		}
	}

	/**
	 * Evaluate fuzzy rule set
	 */
	public void evaluate() {

		THashMap<MutableString, Variable> variables = this.variables;
		ObjectArrayList<Variable> outputs = new ObjectArrayList<Variable>();
		for( Iterator<Variable> it = variables.values().iterator(); it.hasNext(); ) {
			Variable var = it.next();
			if( var.defuzzifier!=null ) {
				var.reset();
				outputs.add(var);
				var.preCalculate();
			}

		}

		//variables.forEachValue(new EvalProc(0));

		// Second: Apply each rule
		for( FuzzyRule fuzzyRule : rules )
			fuzzyRule.evaluate(ruleImplicationMethod);

		// Thrid: Defuzzify each consequent varaible
		for( Variable var : outputs ){
			var.defuzzify();
		}
	}

	class EvalProc implements TObjectProcedure<Variable>{
		double time;

		public EvalProc(double time){
			this.time = time;
		}

		public boolean execute(Variable var) {
			if (var.defuzzifier!=null)
				var.defuzzifier.reset();

			var.setTime(time);

			return true;
		}
	};

	public void evaluate(double startTime,double endTime,double step){
		THashMap<MutableString, Variable> variables = this.variables;
		ObjectArrayList<Variable> outputs = new ObjectArrayList<Variable>();
		for( Iterator<Variable> it = variables.values().iterator(); it.hasNext(); ) {
			Variable var = it.next();
			if( var.defuzzifier!=null ) {
				var.reset();
				outputs.add(var);
				var.preCalculate();
			}
		}

		double[] sum = new double[outputs.size()];

		int count=0;
		for(double time=startTime;time<=endTime;time+=step){
			count++;

			variables.forEachValue(new EvalProc(time));

			// Second: Apply each rule
			for( FuzzyRule fuzzyRule : rules )
				fuzzyRule.evaluate(ruleImplicationMethod);

			// Thrid: Defuzzify each consequent varaible
			int index =0;
			for( Variable var : outputs ) {
				var.defuzzify();
				sum[index++] += var.getLatestDefuzzifiedValue();
			}
		}

		for (int i=0;i<sum.length;++i){
			outputs.get(i).setLatestDefuzzifiedValue(sum[i]/count);
		}
	}

	/**
	 * Buils rule set based on FCL tree (parsed from an FCL file)
	 * @param tree : Tree to use
	 * @return : RuleSet's name (or "" if no name)
	 */
	public String fclTree(AST tree) {
		// Init variables and rules
		variables = new THashMap<MutableString, Variable>(11,new MutableStringStrategy());
		rules = new ObjectArrayList<FuzzyRule>();

		boolean firstChild = true;
		String ruleSetName = "";

		AST child = tree.getFirstChild(); // Descend one level (from root)

		// Add every item
		for( ; child != null; child = child.getNextSibling() ) {
			String leaveName = child.getText();
			Gpr.debug(debug, "Parsing: " + leaveName + "\tline: " + child.getLine() + ", column: " + child.getColumn());
			if( leaveName.equalsIgnoreCase("VAR_INPUT") ) fclTreeVariables(child);
			else if( leaveName.equalsIgnoreCase("VAR_OUTPUT") ) fclTreeVariables(child);
			else if( leaveName.equalsIgnoreCase("FUZZIFY") ) fclTreeFuzzify(child);
			else if( leaveName.equalsIgnoreCase("DEFUZZIFY") ) fclTreeDefuzzify(child);
			else if( leaveName.equalsIgnoreCase("RULEBLOCK") ) fclTreeRuleBlock(child);
			else {
				if( firstChild ) ruleSetName = leaveName;
				else throw new RuntimeException("Unknown item '" + leaveName + "'");
			}
			firstChild = false;
		}
		return ruleSetName;
	}

	/**
	 * Parse a tree for "Defuzzify" item
	 * @param tree : Tree to parse
	 * @return Variable (old or created)
	 */
	private Variable fclTreeDefuzzify(AST tree) {
		String ruleAggregationMethodType = "SUM";
		String defuzzificationMethodType = "COG";

		AST child = tree.getFirstChild();
		MutableString varName = new MutableString(child.getText());
		Gpr.debug(debug, "Parsing: " + varName);

		// Get variable (or create a new one)
		Variable variable = getVariable(varName);
		if( variable == null ) {
			variable = new Variable(varName);
			variables.put(varName, variable);
			Gpr.debug("Variable '" + varName + "' does not exist => Creating it");
		}

		//---
		// Explore each sibling in this level
		//---
		for( child = child.getNextSibling(); child != null; child = child.getNextSibling() ) {
			String leaveName = child.getText();
			Gpr.debug(debug, "Parsing: " + leaveName);

			if( leaveName.equalsIgnoreCase("TERM") ) {
				// Linguistic term
				LinguisticTerm linguisticTerm = fclTreeFuzzifyTerm(child, variable);
				variable.add(linguisticTerm);
			} else if( leaveName.equalsIgnoreCase("ACCU") ) {
				// Aggregation (or accumulation) method
				ruleAggregationMethodType = child.getFirstChild().getText();
			} else if( leaveName.equalsIgnoreCase("METHOD") ) {
				// Defuzzification method
				defuzzificationMethodType = child.getFirstChild().getText();
			} else if( leaveName.equalsIgnoreCase("DEFAULT") ) {
				// Default value
				String defaultValueStr = child.getFirstChild().getText();
				if( defaultValueStr.equalsIgnoreCase("NC") ) variable.setDefaultValue(Double.NaN); // Set it to "No Change"?
				else variable.setDefaultValue(parseDouble(child.getFirstChild())); // Set value
			} else if( leaveName.equalsIgnoreCase("RANGE") ) {
				// Range values (universe min / max)
				double universeMin = parseDouble(child.getFirstChild());
				double universeMax = parseDouble(child.getFirstChild().getNextSibling());
				if( universeMax <= universeMin ) throw new RuntimeException("Range's min is grater than range's max! RANGE := ( " + universeMin + " .. " + universeMax + " );");
				variable.setUniverseMax(universeMax);
				variable.setUniverseMin(universeMin);
			} else throw new RuntimeException("Unknown/Unimplemented item '" + leaveName + "'");
		}

		//---
		// Aggregation (or accumulation) method
		//---
		RuleAggregationMethod ruleAggregationMethod;
		if( ruleAggregationMethodType.equalsIgnoreCase("MAX") ) ruleAggregationMethod = new RuleAggregationMethodMax();
		else if( ruleAggregationMethodType.equalsIgnoreCase("BSUM") ) ruleAggregationMethod = new RuleAggregationMethodBoundedSum();
		else if( ruleAggregationMethodType.equalsIgnoreCase("NSUM") ) ruleAggregationMethod = new RuleAggregationMethoNormedSum();
		else if( ruleAggregationMethodType.equalsIgnoreCase("PROBOR") ) ruleAggregationMethod = new RuleAggregationMethodProbOr();
		else if( ruleAggregationMethodType.equalsIgnoreCase("SUM") ) ruleAggregationMethod = new RuleAggregationMethodSum();
		else throw new RuntimeException("Unknown/Unimplemented Rule aggregation method '" + ruleAggregationMethodType + "'");
		// Set variable's rule aggregation method
		variable.setRuleAggregationMethod(ruleAggregationMethod);

		//---
		// Defuzzification method
		//---
		Defuzzifier defuzzifier;
		if( defuzzificationMethodType.equalsIgnoreCase("COG") ) defuzzifier = new DefuzzifierCenterOfGravity(variable);
		else if( defuzzificationMethodType.equalsIgnoreCase("COGS") ) defuzzifier = new DefuzzifierCenterOfGravitySingletons(variable);
		else if( defuzzificationMethodType.equalsIgnoreCase("COGF") ) defuzzifier = new DefuzzifierCenterOfGravityFunctions(variable);
		else if( defuzzificationMethodType.equalsIgnoreCase("COA") ) defuzzifier = new DefuzzifierCenterOfArea(variable);
		else if( defuzzificationMethodType.equalsIgnoreCase("LM") ) defuzzifier = new DefuzzifierLeftMostMax(variable);
		else if( defuzzificationMethodType.equalsIgnoreCase("RM") ) defuzzifier = new DefuzzifierRightMostMax(variable);
		else if( defuzzificationMethodType.equalsIgnoreCase("MM") ) defuzzifier = new DefuzzifierMeanMax(variable);
		else throw new RuntimeException("Unknown/Unimplemented Rule defuzzification method '" + defuzzificationMethodType + "'");
		// Set variable's defuzzifier
		variable.setDefuzzifier(defuzzifier);

		return variable;
	}

	/**
	 * Parse a tree for "Fuzzify" item
	 * @param tree : Tree to parse
	 * @return Variable (old or created)
	 */
	private Variable fclTreeFuzzify(AST tree) {
		AST child = tree.getFirstChild();
		MutableString varName = new MutableString(child.getText());
		Gpr.debug(debug, "Parsing: " + varName);

		// Get variable (or create a new one)
		Variable variable = getVariable(varName);
		if( variable == null ) {
			variable = new Variable(varName);
			variables.put(varName, variable);
			Gpr.debug("Variable '" + varName + "' does not exist => Creating it");
		}

		// Explore each sibling in this level
		for( child = child.getNextSibling(); child != null; child = child.getNextSibling() ) {
			String leaveName = child.getText();
			Gpr.debug(debug, "Parsing: " + leaveName);

			if( leaveName.equalsIgnoreCase("TERM") ) {
				LinguisticTerm linguisticTerm = fclTreeFuzzifyTerm(child, variable);
				variable.add(linguisticTerm);
			} else if( leaveName.equalsIgnoreCase("RANGE") ) {
				// Range values (universe min / max)
				double universeMin = parseDouble(child.getFirstChild());
				double universeMax = parseDouble(child.getFirstChild().getNextSibling());
				if( universeMax <= universeMin ) throw new RuntimeException("Range's min is grater than range's max! RANGE := ( " + universeMin + " .. " + universeMax + " );");
				variable.setUniverseMax(universeMax);
				variable.setUniverseMin(universeMin);
			}else throw new RuntimeException("Unknown/Unimplemented item '" + leaveName + "'");
		}

		return variable;
	}

	/**
	 * Parse a tree for "Term" item
	 * @param tree : Tree to parse
	 * @return A new LinguisticTerm
	 */
	private LinguisticTerm fclTreeFuzzifyTerm(AST tree, Variable variable) {
		AST child = tree.getFirstChild();
		MutableString termName = new MutableString(child.getText());
		Gpr.debug(debug, "Parsing: " + termName);

		child = child.getNextSibling();
		MutableString leaveName = new MutableString(child.getText());
		Gpr.debug(debug, "Parsing: leaveName = " + leaveName);

		MembershipFunction membershipFunction = null;
		if( leaveName.equalsIgnoreCase("(") ) { // Piece-wise linear
			membershipFunction = fclTreeFuzzifyTermPieceWiseLinear(child);
		} else if( leaveName.equalsIgnoreCase("GAUSS") ) {
			membershipFunction = fclTreeFuzzifyTermGauss(child);
		} else if( leaveName.equalsIgnoreCase("TRIAN") ) {
			membershipFunction = fclTreeFuzzifyTermTriangular(child);
		} else if( leaveName.equalsIgnoreCase("GBELL") ) {
			membershipFunction = fclTreeFuzzifyTermGenBell(child);
		} else if( leaveName.equalsIgnoreCase("TRAPE") ) {
			membershipFunction = fclTreeFuzzifyTermTrapetzoidal(child);
		} else if( leaveName.equalsIgnoreCase("SIGM") ) {
			membershipFunction = fclTreeFuzzifyTermSigmoidal(child);
		} else if( leaveName.equalsIgnoreCase("SINGLETONS") ) {
			membershipFunction = fclTreeFuzzifyTermSingletons(child);
		} else if( leaveName.equalsIgnoreCase("FUNCTION") ) {
			membershipFunction = fclTreeFuzzifyTermFunction(child);
		} else if( leaveName.equalsIgnoreCase("-") ) { // Single point  (negative number) => Singleton
			membershipFunction = fclTreeFuzzifyTermSingleton(child);
		} else if( leaveName.equalsIgnoreCase("+") ) { // Single point (positive number) => Singleton
			membershipFunction = fclTreeFuzzifyTermSingleton(child);
		} else { // Single point (positive number) => Singleton
			membershipFunction = fclTreeFuzzifyTermSingleton(child);
		}
		LinguisticTerm linguisticTerm = new LinguisticTerm(termName, membershipFunction);

		// Create linguistic term
		return linguisticTerm;
	}

	/**
	 * Parse a tree for trapetzoidal memebership function
	 * @param tree : Tree to parse
	 * @return A new membership function
	 */
	private MembershipFunction fclTreeFuzzifyTermFunction(AST tree) {
		Gpr.debug(debug, "Function: " + tree.toStringTree());
		return new MembershipFunctionFuncion(this, tree.getFirstChild());
	}

	/**
	 * Parse a tree for gaussian memebership function
	 * @param tree : Tree to parse
	 * @return A new membership function
	 */
	private MembershipFunction fclTreeFuzzifyTermGauss(AST tree) {
		AST child = tree.getFirstChild();
		double mean = parseDouble(child);
		double stdev = parseDouble(child.getNextSibling());
		MembershipFunction membershipFunction = new MembershipFunctionGaussian(mean, stdev);
		return membershipFunction;
	}

	/**
	 * Parse a tree for generilized bell memebership function
	 * @param tree : Tree to parse
	 * @return A new membership function
	 */
	private MembershipFunction fclTreeFuzzifyTermGenBell(AST tree) {
		AST child = tree.getFirstChild();
		double a = parseDouble(child);
		double b = parseDouble(child.getNextSibling());
		double mean = parseDouble(child.getNextSibling().getNextSibling());
		MembershipFunction membershipFunction = new MembershipFunctionGenBell(a, b, mean);
		return membershipFunction;
	}

	/**
	 * Parse a tree for piece-wice linear memebership function
	 * @param tree : Tree to parse
	 * @return A new membership function
	 */
	private MembershipFunction fclTreeFuzzifyTermPieceWiseLinear(AST tree) {
		AST child = tree;
		Gpr.debug(debug, "Parsing: " + child.getText());

		// Count number of points
		int numPoints;
		for( numPoints = 0; child != null; child = child.getNextSibling() ) {
			String leaveName = child.getText();
			if( leaveName.equalsIgnoreCase("(") ) numPoints++;
		}

		// Parse multiple points (for piece-wise linear)
		return fclTreeFuzzifyTermPieceWiseLinearPoints(tree, numPoints);
	}

	/**
	 * Parse a tree for piece-wice linear memebership function series of points
	 * @param tree : Tree to parse
	 * @param numberOfPoints : Number of points in this function
	 * @return A new membership function
	 */
	private MembershipFunction fclTreeFuzzifyTermPieceWiseLinearPoints(AST tree, int numberOfPoints) {
		AST child = tree;
		Gpr.debug(debug, "Parsing: " + child.getText());

		double x[] = new double[numberOfPoints];
		double y[] = new double[numberOfPoints];
		for( int i = 0; child != null; child = child.getNextSibling(), i++ ) {
			String leaveName = child.getText();
			Gpr.debug(debug, "Sub-Parsing: " + leaveName);

			// It's a set of points? => Defines a piece-wise linear membership function
			if( leaveName.equalsIgnoreCase("(") ) {
				AST point = child.getFirstChild();
				x[i] = parseDouble(point); // Parse and add each point
				y[i] = parseDouble(point.getNextSibling());
				Gpr.debug(debug, "Parsed point " + i + " x=" + x[i] + ", y=" + y[i]);
			} else throw new RuntimeException("Unknown (or unimplemented) option : " + leaveName);
		}
		return new MembershipFunctionPieceWiseLinear(x, y);
	}

	/**
	 * Parse a tree for sigmoidal memebership function
	 * @param tree : Tree to parse
	 * @return A new membership function
	 */
	private MembershipFunction fclTreeFuzzifyTermSigmoidal(AST tree) {
		AST child = tree.getFirstChild();
		double gain = parseDouble(child);
		double t0 = parseDouble(child.getNextSibling());
		MembershipFunction membershipFunction = new MembershipFunctionSigmoidal(gain, t0);
		return membershipFunction;
	}

	/**
	 * Parse a tree for piece-wice linear memebership function item
	 * @param tree : Tree to parse
	 * @return A new membership function
	 */
	private MembershipFunction fclTreeFuzzifyTermSingleton(AST tree) {
		double singleTonValueX = parseDouble(tree);
		MembershipFunction membershipFunction = new MembershipFunctionSingleton(singleTonValueX, 1);
		return membershipFunction;
	}

	/**
	 * Parse a tree for singletons memebership function
	 * @param tree : Tree to parse
	 * @return A new membership function
	 */
	private MembershipFunction fclTreeFuzzifyTermSingletons(AST tree) {
		AST child = tree.getFirstChild();
		Gpr.debug(debug, "Parsing: " + tree.getText());

		// Count number of points
		int numPoints;
		for( numPoints = 0; child != null; child = child.getNextSibling() ) {
			String leaveName = child.getText();
			if( leaveName.equalsIgnoreCase("(") ) numPoints++;
			Gpr.debug("leaveName : " + leaveName + "\tnumPoints: " + numPoints);
		}

		// Parse multiple points (for piece-wise linear)
		return fclTreeFuzzifyTermSingletonsPoints(tree.getFirstChild(), numPoints);
	}

	/**
	 * Parse a tree for singletons memebership function series of points
	 * @param tree : Tree to parse
	 * @param numberOfPoints : Number of points in this function
	 * @return A new membership function
	 */
	private MembershipFunction fclTreeFuzzifyTermSingletonsPoints(AST tree, int numberOfPoints) {
		AST child = tree;
		Gpr.debug(debug, "Parsing: " + child.getText());

		double x[] = new double[numberOfPoints];
		double y[] = new double[numberOfPoints];
		for( int i = 0; child != null; child = child.getNextSibling(), i++ ) {
			String leaveName = child.getText();
			Gpr.debug(debug, "Sub-Parsing: " + leaveName);

			// It's a set of points? => Defines a piece-wise linear membership function
			if( leaveName.equalsIgnoreCase("(") ) {
				AST point = child.getFirstChild();
				x[i] = parseDouble(point); // Parse and add each point
				y[i] = parseDouble(point.getNextSibling());
				Gpr.debug(debug, "Parsed point " + i + " x=" + x[i] + ", y=" + y[i]);
			} else throw new RuntimeException("Unknown (or unimplemented) option : " + leaveName);
		}
		return new MembershipFunctionGenericSingleton(x, y);
	}

	/**
	 * Parse a tree for trapetzoidal memebership function
	 * @param tree : Tree to parse
	 * @return A new membership function
	 */
	private MembershipFunction fclTreeFuzzifyTermTrapetzoidal(AST tree) {
		AST child = tree.getFirstChild();
		double min = parseDouble(child);
		double midLow = parseDouble(child.getNextSibling());
		double midHigh = parseDouble(child.getNextSibling().getNextSibling());
		double max = parseDouble(child.getNextSibling().getNextSibling().getNextSibling());
		MembershipFunction membershipFunction = new MembershipFunctionTrapetzoidal(min, midLow, midHigh, max);
		return membershipFunction;
	}

	/**
	 * Parse a tree for traingular memebership function
	 * @param tree : Tree to parse
	 * @return A new membership function
	 */
	private MembershipFunction fclTreeFuzzifyTermTriangular(AST tree) {
		AST child = tree.getFirstChild();
		double min = parseDouble(child);
		double mid = parseDouble(child.getNextSibling());
		double max = parseDouble(child.getNextSibling().getNextSibling());
		MembershipFunction membershipFunction = new MembershipFunctionTriangular(min, mid, max);
		return membershipFunction;
	}

	private void fclTreeRuleBlock(AST tree) {
		boolean rulesAdded = false;
		AST child = tree.getFirstChild();
		String ruleBlockName = child.getText();
		Gpr.debug(debug, "Parsing: " + ruleBlockName);

		// Use 'default' methods
		RuleConnectionMethod and = new RuleConnectionMethodAndMin(), or = new RuleConnectionMethodOrMax();

		// Explore each sibling in this level
		for( child = child.getNextSibling(); child != null; child = child.getNextSibling() ) {
			String leaveName = child.getText();
			Gpr.debug(debug, "Parsing: " + leaveName);

			if( leaveName.equalsIgnoreCase("AND") ) {
				//---
				// Which 'AND' method to use? (Note: We alse set 'OR' method accordingly to fullfill DeMorgan's law
				//---
				if( rulesAdded ) throw new RuntimeException("AND / OR methods must be defined prior to RULE definition");
				String type = child.getFirstChild().getText();
				if( type.equalsIgnoreCase("MIN") ) {
					and = new RuleConnectionMethodAndMin();
					or = new RuleConnectionMethodOrMax();
				} else if( type.equalsIgnoreCase("PROD") ) {
					and = new RuleConnectionMethodAndProduct();
					or = new RuleConnectionMethodOrProbOr();
				} else if( type.equalsIgnoreCase("BDIF") ) {
					and = new RuleConnectionMethodAndBoundedDif();
					or = new RuleConnectionMethodOrBoundedSum();
				} else throw new RuntimeException("Unknown (or unimplemented) 'AND' method: " + type);
			} else if( leaveName.equalsIgnoreCase("OR") ) {
				//---
				// Which 'AND' method to use? (Note: We alse set 'OR' method accordingly to fullfill DeMorgan's law
				//---
				if( rulesAdded ) throw new RuntimeException("AND / OR methods must be defined prior to RULE definition");
				String type = tree.getFirstChild().getText();
				if( type.equalsIgnoreCase("MAX") ) {
					or = new RuleConnectionMethodOrMax();
					and = new RuleConnectionMethodAndMin();
				} else if( type.equalsIgnoreCase("ASUM") ) {
					or = new RuleConnectionMethodOrProbOr();
					and = new RuleConnectionMethodAndProduct();
				} else if( type.equalsIgnoreCase("BSUM") ) {
					or = new RuleConnectionMethodOrBoundedSum();
					and = new RuleConnectionMethodAndBoundedDif();
				} else throw new RuntimeException("Unknown (or unimplemented) 'OR' method: " + type);
			} else if( leaveName.equalsIgnoreCase("ACT") ) fclTreeRuleBlockActivation(child);
			else if( leaveName.equalsIgnoreCase("RULE") ) {
				rulesAdded = true;
				fclTreeRuleBlockRule(child, and, or);
			} else throw new RuntimeException("Unknown (or unimplemented) ruleblock item : " + leaveName);
		}
	}

	/**
	 * Parse rule Implication Method (or rule activation method)
	 * @param tree : Tree to parse
	 */
	private void fclTreeRuleBlockActivation(AST tree) {
		String type = tree.getFirstChild().getText();
		Gpr.debug(debug, "Parsing: " + type);

		if( type.equalsIgnoreCase("MIN") ) ruleImplicationMethod = new RuleImplicationMethodMin();
		else if( type.equalsIgnoreCase("PROD") ) ruleImplicationMethod = new RuleImplicationMethodProduct();
		else throw new RuntimeException("Unknown (or unimplemented) 'ACT' method: " + type);
	}

	/**
	 * Parse rule Implication Method (or rule activation method)
	 * @param tree : Tree to parse
	 */
	private void fclTreeRuleBlockRule(AST tree, RuleConnectionMethod and, RuleConnectionMethod or) {
		AST child = tree.getFirstChild();
		Gpr.debug(debug, "Parsing: " + child.getText());

		FuzzyRule fuzzyRule = new FuzzyRule(child.getText());

		for( child = child.getNextSibling(); child != null; child = child.getNextSibling() ) {
			String type = child.getText();
			Gpr.debug(debug, "Parsing: " + type);

			if( type.equalsIgnoreCase("IF") ) fuzzyRule.setAntecedents((FuzzyRuleExpression) fclTreeRuleBlockRuleIf(child.getFirstChild(), and, or));
			else if( type.equalsIgnoreCase("THEN") ) fclTreeRuleBlockRuleThen(child, fuzzyRule);
			else if( type.equalsIgnoreCase("WITH") ) fclTreeRuleBlockRuleWith(child, fuzzyRule);
			else throw new RuntimeException("Unknown (or unimplemented) rule block item: " + type);
		}

		add(fuzzyRule);
	}

	/**
	 * Parse rule 'IF' (or rule's weight)
	 * @param tree : Tree to parse
	 */
	private FuzzyRuleExpression fclTreeRuleBlockRuleIf(AST tree, RuleConnectionMethod and, RuleConnectionMethod or) {
		AST child = tree;
		String ifConnector = child.getText();
		Gpr.debug(debug, "Parsing: " + ifConnector);

		// Create a new expresion
		FuzzyRuleExpression fuzzyRuleExpression = new FuzzyRuleExpression();

		if( ifConnector.equalsIgnoreCase("AND") ) {
			fuzzyRuleExpression.setRuleConnectionMethod(and);
			// Recurse on term1
			fuzzyRuleExpression.setTerm1(fclTreeRuleBlockRuleIf(child.getFirstChild(), and, or));
			// Recurse on term2
			fuzzyRuleExpression.setTerm2(fclTreeRuleBlockRuleIf(child.getFirstChild().getNextSibling(), and, or));
		} else if( ifConnector.equalsIgnoreCase("OR") ) {
			fuzzyRuleExpression.setRuleConnectionMethod(or);
			// Recurse on term2
			fuzzyRuleExpression.setTerm1(fclTreeRuleBlockRuleIf(child.getFirstChild(), and, or));
			// Recurse on term2
			fuzzyRuleExpression.setTerm2(fclTreeRuleBlockRuleIf(child.getFirstChild().getNextSibling(), and, or));
		} else if( ifConnector.equalsIgnoreCase("NOT") ) {
			fuzzyRuleExpression.setNegated(true);
			fuzzyRuleExpression.setTerm1(fclTreeRuleBlockRuleIf(child.getFirstChild(), and, or));
		} else if( ifConnector.equalsIgnoreCase("(") ) {
			fuzzyRuleExpression.setTerm1(fclTreeRuleBlockRuleIf(child.getFirstChild(), and, or));
		} else {
			// It's a "(Variable IS linguisticTerm)" clause
			// or "(Variable IS NOT linguisticTerm)" clause
			MutableString varName = new MutableString(child.getText());
			MutableString lingTerm = new MutableString(child.getFirstChild().getText());
			boolean negate = false;
			if( lingTerm.equalsIgnoreCase("NOT") ) {
				lingTerm = new MutableString(child.getFirstChild().getNextSibling().getText());
				negate = true;
			}
			Variable variable = getVariable(varName);
			FuzzyRuleTerm fuzzyRuleTerm = new FuzzyRuleTerm(variable, lingTerm, negate);
			fuzzyRuleExpression.add(fuzzyRuleTerm);
		}
		return fuzzyRuleExpression;
	}

	/**
	 * Parse rule 'THEN' (or rule's weight)
	 * @param tree : Tree to parse
	 */
	private void fclTreeRuleBlockRuleThen(AST tree, FuzzyRule fuzzyRule) {
		for( AST child = tree.getFirstChild(); child != null; child = child.getNextSibling() ) {
			MutableString thenVariable = new MutableString(child.getText());
			Gpr.debug(debug, "Parsing: " + thenVariable);

			MutableString thenValue = new MutableString(child.getFirstChild().getText());
			Variable variable = getVariable(thenVariable);
			if( variable == null ) throw new RuntimeException("Variable " + thenVariable + " does not exist");
			fuzzyRule.addConsequent(variable, thenValue, false);
		}
	}

	/**
	 * Parse rule 'WITH' (or rule's weight)
	 * @param tree : Tree to parse
	 */
	private void fclTreeRuleBlockRuleWith(AST tree, FuzzyRule fuzzyRule) {
		Gpr.debug(debug, "Parsing: " + tree.getFirstChild().getText());
		fuzzyRule.setWeight(parseDouble(tree.getFirstChild()));
	}

	/**
	 * Parse a tree for "Variable" item (either input or output variables)
	 * @param tree
	 */
	private void fclTreeVariables(AST tree) {
		for( AST child = tree.getFirstChild(); child != null; child = child.getNextSibling() ) {
			MutableString varName = new MutableString(child.getText());
			Variable variable = new Variable(varName);
			Gpr.debug(debug, "Parsing: " + varName);
//			AST range=null;
//			for (range=child.getFirstChild(); range!=null;range=range.getNextSibling())
//				if (range.getText().equalsIgnoreCase("RANGE")) break;
//
//			if (range!=null) {
//				AST rchild = range.getFirstChild();
//				double umin=Double.parseDouble(rchild.getText());
//				rchild = rchild.getNextSibling();
//				double umax=Double.parseDouble(rchild.getText());
//				variable.setUniverseMin(umin);
//				variable.setUniverseMax(umax);
//			}

			if( varibleExists(variable.getName()) ) Gpr.debug("Warning: Variable '" + variable.getName() + "' duplicated");
			else variables.put(varName, variable); // OK? => Add variable
		}
	}

	public RuleImplicationMethod getRuleImplicationMethod() {
		return ruleImplicationMethod;
	}

	public List getRules() {
		return rules;
	}

	/** Get Variable by name */
	public Variable getVariable(MutableString variableName) {
		return variables.get(variableName);
	}

	/** Get Variable by name */
	public Variable getVariable(String variableName) {
		return variables.get(new MutableString(variableName));
	}

	public THashMap<MutableString, Variable> getVariables() {
		return variables;
	}

	/** Parse a double number */
	public double parseDouble(AST tree) {
		double sign = +1.0;
		double number = 0;

		//		if( tree == null ) {
		//			Gpr.debug("NULL tree!");
		//			return 0;
		//		}

		if( tree.getText().equals("-") ) {
			// Negative sign
			sign = -1.0;
			number = Gpr.parseDoubleSafe(tree.getFirstChild().getText());
		} else if( tree.getText().equals("+") ) {
			// Positive sign
			sign = +1.0;
			number = Gpr.parseDoubleSafe(tree.getFirstChild().getText());
		} else {
			// No sign
			number = Gpr.parseDoubleSafe(tree.getText());
		}

		return sign * number;
	}

	/**
	 * Reset ruleset (should be done prior to each inference)
	 * Also create 'variables' list (if needed)
	 */
	public void reset() {
		boolean addToVariables = false;
		THashMap<Variable, Variable> resetted = new THashMap<Variable, Variable>();

		// Create a list of consequent variables if not already created
		// (all variables that must be defuzzified)
		if( variables == null ) {
			variables = new THashMap<MutableString, Variable>(7,new MutableStringStrategy());
			addToVariables = true;
		}

		//---
		// Reset every consequent variable on every rule
		//---
		for( Iterator it = rules.iterator(); it.hasNext(); ) {
			FuzzyRule fr = (FuzzyRule) it.next();
			// Reset rule's degree of support
			fr.setDegreeOfSupport(0);

			//---
			// Reset every consequent variable (and add it to variables list if needed)
			//---
			List llc = fr.getConsequents();
			for( Iterator itc = llc.iterator(); itc.hasNext(); ) {
				FuzzyRuleTerm term = (FuzzyRuleTerm) itc.next();
				Variable var = term.getVariable();
				// Not already resetted?
				if( resetted.get(var) == null ) {
					// Sanity check
					if( var.getDefuzzifier() == null ) throw new RuntimeException("Defuzzifier not setted for output variable '" + var.getName() + "'");
					// Reset variable
					var.reset();
					// Mark it as 'resetted' so we don't reset it again
					resetted.put(var, var);
					// Add this variable to variables's list (if not already added)
					if( addToVariables && (!varibleExists(var.getName())) ) variables.put(var.getName(), var);
				}
			}

			//---
			// Reset every antecedent's variable  (and add it to variables list if needed)
			//---
			for( Iterator itc = fr.getAntecedents().iteratorVariables(); itc.hasNext(); ) {
				Variable var = (Variable) itc.next();
				// Not already resetted?
				if( resetted.get(var) == null ) {
					// Reset variable
					var.reset();
					// Mark it as 'resetted' so we don't reset it again
					resetted.put(var, var);
					// Add this variable to variables's list (if not already added)
					if( addToVariables && (!varibleExists(var.getName())) ) variables.put(var.getName(), var);
				}
			}
		}
	}

	public void setRuleImplicationMethod(RuleImplicationMethod ruleImplicationMethod) {
		this.ruleImplicationMethod = ruleImplicationMethod;
	}

	public void setRules(ObjectArrayList<FuzzyRule> rules) {
		this.rules = rules;
	}

	/**
	 * Set a variable
	 * @param variableName : Variable's name
	 * @param value : variable's value to be setted
	 * @return this
	 */
	public FuzzyRuleSet setVariable(MutableString variableName, double value) {
		Variable var = getVariable(variableName);
		if( var == null ) throw new RuntimeException("No such variable: '" + variableName + "'");
		var.setValue(value);
		return this;
	}

	/**
	 * Set a variable
	 * @param variableName : Variable's name
	 * @param value : variable's value to be setted
	 * @return this
	 */
	public FuzzyRuleSet setVariable(String variableName, double value) {
		Variable var = getVariable(new MutableString(variableName));
		if( var == null ) throw new RuntimeException("No such variable: '" + variableName + "'");
		var.setValue(value);
		return this;
	}

	public void setVariables(THashMap<MutableString, Variable> consequentVariables) {
		this.variables = consequentVariables;
	}

	public String toString() {
		String str = "Implication method: " + ruleImplicationMethod.getName() + "\n";

		// Show variables (sorted by name)
		for( Iterator<Variable> it = variablesIteratorSorted(); it.hasNext(); ) {
			Variable var = it.next();
			str += var.toString() + "\n";
		}

		// Show rules
		for( Iterator it = rules.iterator(); it.hasNext(); ) {
			FuzzyRule fr = (FuzzyRule) it.next();
			str += fr.toString() + "\n";
		}
		return str;
	}

	public String toStringFCL() {
		String varsIn = "", varsOut = "", fuzzifiers = "", defuzzifiers = "", ruleBlock = "", operator = "";

		// Show variables (sorted by name)
		for( Iterator<Variable> it = variablesIteratorSorted(); it.hasNext(); ) {
			Variable var = it.next();
			if( var.getDefuzzifier() == null ) {
				// Add input variables
				varsIn += "\t" + var.getName() + " : REAL;\n";

				// Add fuzzyfiers
				fuzzifiers += "FUZZIFY " + var.getName() + "\n";
				for( Iterator<MutableString> itlt = var.iteratorLinguisticTermNamesSorted(); itlt.hasNext(); ) {
					MutableString ltName = itlt.next();
					LinguisticTerm linguisticTerm = var.getLinguisticTerm(ltName);
					fuzzifiers += "\t" + linguisticTerm.toStringFCL() + "\n";
				}
				fuzzifiers += "END_FUZZIFY\n\n";

			} else {
				// Add outputt variables
				varsOut += "\t" + var.getName() + " : REAL;\n";

				// Add defuzzyfiers
				defuzzifiers += "DEFUZZIFY " + var.getName() + "\n";
				for( Iterator<MutableString> itlt = var.iteratorLinguisticTermNamesSorted(); itlt.hasNext(); ) {
					MutableString ltName = itlt.next();
					LinguisticTerm linguisticTerm = var.getLinguisticTerm(ltName);
					defuzzifiers += "\t" + linguisticTerm.toStringFCL() + "\n";
				}
				defuzzifiers += "\t" + var.getRuleAggregationMethod().toStringFCL() + "\n";
				defuzzifiers += "\t" + var.getDefuzzifier().toStringFCL() + "\n";
				defuzzifiers += "\tDEFAULT := " + (Double.isNaN(var.getDefaultValue()) ? "NC" : Double.toString(var.getDefaultValue())) + ";\n";
				var.estimateUniverse();
				defuzzifiers += "\tRANGE := (" + var.getUniverseMin() + " .. " + var.getUniverseMax() + ");\n";
				defuzzifiers += "END_DEFUZZIFY\n\n";
			}
		}

		varsIn = "VAR_INPUT\n" + varsIn + "END_VAR\n\n";
		varsOut = "VAR_OUTPUT\n" + varsOut + "END_VAR\n\n";

		// Show rules
		int ruleNum = 1;
		for( Iterator it = rules.iterator(); it.hasNext(); ruleNum++ ) {
			FuzzyRule fr = (FuzzyRule) it.next();
			ruleBlock += "\tRULE " + ruleNum + " : " + fr.toStringFCL() + "\n";
			if( fr.getAntecedents().getRuleConnectionMethod() != null ) {
				operator = fr.getAntecedents().getRuleConnectionMethod().toStringFCL();
			}
		}

		ruleBlock = "RULEBLOCK Rules\n" //
				+ "\t" + ruleImplicationMethod.toStringFCL() + "\n"//
				+ "\t" + operator + "\n" //
				+ ruleBlock //
				+ "END_RULEBLOCK\n\n";

		return "FUNCTION_BLOCK fbName\n\n" + varsIn + varsOut + fuzzifiers + defuzzifiers + ruleBlock + "END_FUNCTION_BLOCK\n";
	}

	/** Get an iterator for variables */
	public Iterator<Variable> variablesIterator() {
		return variables.values().iterator();
	}

	/** Get an iterator for variables (sorted by name) */
	public Iterator<Variable> variablesIteratorSorted() {
		LinkedList ll = new LinkedList(variables.values());
		Collections.sort(ll);
		return ll.iterator();
	}

	/** Does this variable exist in this ruleset? */
	public boolean varibleExists(MutableString variableName) {
		if( variables == null ) return false;
		if( getVariable(variableName) != null ) return true;
		return false;
	}

}
