// $ANTLR 2.7.5 (20050128): "../fcl/fcl.g" -> "FCLParser.java"$
package net.sourceforge.jFuzzyLogic.fcl;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.AST;
import antlr.collections.impl.BitSet;

public class FCLParser extends antlr.LLkParser       implements FCLTokenTypes
 {

protected FCLParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public FCLParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected FCLParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public FCLParser(TokenStream lexer) {
  this(lexer,1);
}

public FCLParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void fcl() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fcl_AST = null;
		
		try {      // for error handling
			{
			int _cnt49=0;
			_loop49:
			do {
				if ((LA(1)==FUNCTION_BLOCK)) {
					function_block();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt49>=1 ) { break _loop49; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt49++;
			} while (true);
			}
			fcl_AST = currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = fcl_AST;
	}
	
	public final void function_block() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST function_block_AST = null;
		
		try {      // for error handling
			AST tmp1_AST = null;
			tmp1_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp1_AST);
			match(FUNCTION_BLOCK);
			{
			switch ( LA(1)) {
			case ID:
			{
				AST tmp2_AST = null;
				tmp2_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp2_AST);
				match(ID);
				break;
			}
			case DEFUZZIFY:
			case END_FUNCTION_BLOCK:
			case FUZZIFY:
			case RULEBLOCK:
			case VAR_INPUT:
			case VAR_OUTPUT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop53:
			do {
				if ((_tokenSet_1.member(LA(1)))) {
					declaration();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop53;
				}
				
			} while (true);
			}
			match(END_FUNCTION_BLOCK);
			function_block_AST = currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		returnAST = function_block_AST;
	}
	
	public final void declaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST declaration_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case VAR_INPUT:
			{
				var_input();
				astFactory.addASTChild(currentAST, returnAST);
				declaration_AST = currentAST.root;
				break;
			}
			case VAR_OUTPUT:
			{
				var_output();
				astFactory.addASTChild(currentAST, returnAST);
				declaration_AST = currentAST.root;
				break;
			}
			case FUZZIFY:
			{
				fuzzify_block();
				astFactory.addASTChild(currentAST, returnAST);
				declaration_AST = currentAST.root;
				break;
			}
			case DEFUZZIFY:
			{
				defuzzify_block();
				astFactory.addASTChild(currentAST, returnAST);
				declaration_AST = currentAST.root;
				break;
			}
			case RULEBLOCK:
			{
				rule_block();
				astFactory.addASTChild(currentAST, returnAST);
				declaration_AST = currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		returnAST = declaration_AST;
	}
	
	public final void var_input() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST var_input_AST = null;
		
		try {      // for error handling
			AST tmp4_AST = null;
			tmp4_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp4_AST);
			match(VAR_INPUT);
			{
			_loop57:
			do {
				if ((LA(1)==ID)) {
					var_def();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop57;
				}
				
			} while (true);
			}
			match(END_VAR);
			var_input_AST = currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		returnAST = var_input_AST;
	}
	
	public final void var_output() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST var_output_AST = null;
		
		try {      // for error handling
			AST tmp6_AST = null;
			tmp6_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp6_AST);
			match(VAR_OUTPUT);
			{
			int _cnt60=0;
			_loop60:
			do {
				if ((LA(1)==ID)) {
					var_def();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt60>=1 ) { break _loop60; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt60++;
			} while (true);
			}
			match(END_VAR);
			var_output_AST = currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		returnAST = var_output_AST;
	}
	
	public final void fuzzify_block() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fuzzify_block_AST = null;
		
		try {      // for error handling
			AST tmp8_AST = null;
			tmp8_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp8_AST);
			match(FUZZIFY);
			AST tmp9_AST = null;
			tmp9_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp9_AST);
			match(ID);
			{
			_loop64:
			do {
				switch ( LA(1)) {
				case TERM:
				{
					linguistic_term();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case RANGE:
				{
					range();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					break _loop64;
				}
				}
			} while (true);
			}
			match(END_FUZZIFY);
			fuzzify_block_AST = currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		returnAST = fuzzify_block_AST;
	}
	
	public final void defuzzify_block() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST defuzzify_block_AST = null;
		
		try {      // for error handling
			AST tmp11_AST = null;
			tmp11_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp11_AST);
			match(DEFUZZIFY);
			AST tmp12_AST = null;
			tmp12_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp12_AST);
			match(ID);
			{
			_loop100:
			do {
				if ((_tokenSet_4.member(LA(1)))) {
					defuzzify_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop100;
				}
				
			} while (true);
			}
			match(END_DEFUZZIFY);
			defuzzify_block_AST = currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		returnAST = defuzzify_block_AST;
	}
	
	public final void rule_block() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST rule_block_AST = null;
		
		try {      // for error handling
			AST tmp14_AST = null;
			tmp14_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp14_AST);
			match(RULEBLOCK);
			AST tmp15_AST = null;
			tmp15_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp15_AST);
			match(ID);
			{
			_loop111:
			do {
				if ((_tokenSet_5.member(LA(1)))) {
					rule_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop111;
				}
				
			} while (true);
			}
			match(END_RULEBLOCK);
			rule_block_AST = currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		returnAST = rule_block_AST;
	}
	
	public final void var_def() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST var_def_AST = null;
		
		try {      // for error handling
			AST tmp17_AST = null;
			tmp17_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp17_AST);
			match(ID);
			match(COLON);
			data_type();
			astFactory.addASTChild(currentAST, returnAST);
			match(SEMICOLON);
			var_def_AST = currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		returnAST = var_def_AST;
	}
	
	public final void data_type() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST data_type_AST = null;
		
		try {      // for error handling
			AST tmp20_AST = null;
			tmp20_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp20_AST);
			match(TYPE_REAL);
			data_type_AST = currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = data_type_AST;
	}
	
	public final void linguistic_term() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST linguistic_term_AST = null;
		
		try {      // for error handling
			AST tmp21_AST = null;
			tmp21_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp21_AST);
			match(TERM);
			AST tmp22_AST = null;
			tmp22_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp22_AST);
			match(ID);
			match(ASSIGN_OPERATOR);
			membership_function();
			astFactory.addASTChild(currentAST, returnAST);
			match(SEMICOLON);
			linguistic_term_AST = currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_8);
		}
		returnAST = linguistic_term_AST;
	}
	
	public final void range() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST range_AST = null;
		
		try {      // for error handling
			AST tmp25_AST = null;
			tmp25_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp25_AST);
			match(RANGE);
			match(ASSIGN_OPERATOR);
			match(LEFT_PARENTHESIS);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			match(DOTS);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			match(RIGHT_PARENTHESIS);
			match(SEMICOLON);
			range_AST = currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_8);
		}
		returnAST = range_AST;
	}
	
	public final void membership_function() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST membership_function_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case FUNCTION:
			{
				function();
				astFactory.addASTChild(currentAST, returnAST);
				membership_function_AST = (AST)currentAST.root;
				break;
			}
			case ID:
			case MINUS:
			case PLUS:
			case REAL:
			{
				singleton();
				astFactory.addASTChild(currentAST, returnAST);
				membership_function_AST = (AST)currentAST.root;
				break;
			}
			case SINGLETONS:
			{
				singletons();
				astFactory.addASTChild(currentAST, returnAST);
				membership_function_AST = (AST)currentAST.root;
				break;
			}
			case LEFT_PARENTHESIS:
			{
				piece_wise_linear();
				astFactory.addASTChild(currentAST, returnAST);
				membership_function_AST = (AST)currentAST.root;
				break;
			}
			case GAUSS:
			{
				gauss();
				astFactory.addASTChild(currentAST, returnAST);
				membership_function_AST = (AST)currentAST.root;
				break;
			}
			case TRIAN:
			{
				trian();
				astFactory.addASTChild(currentAST, returnAST);
				membership_function_AST = (AST)currentAST.root;
				break;
			}
			case TRAPE:
			{
				trape();
				astFactory.addASTChild(currentAST, returnAST);
				membership_function_AST = (AST)currentAST.root;
				break;
			}
			case SIGM:
			{
				sigm();
				astFactory.addASTChild(currentAST, returnAST);
				membership_function_AST = (AST)currentAST.root;
				break;
			}
			case GBELL:
			{
				gbell();
				astFactory.addASTChild(currentAST, returnAST);
				membership_function_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = membership_function_AST;
	}
	
	public final void function() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST function_AST = null;
		
		try {      // for error handling
			AST tmp31_AST = null;
			tmp31_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp31_AST);
			match(FUNCTION);
			fun_pm();
			astFactory.addASTChild(currentAST, returnAST);
			function_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = function_AST;
	}
	
	public final void singleton() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST singleton_AST = null;
		
		try {      // for error handling
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			singleton_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = singleton_AST;
	}
	
	public final void singletons() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST singletons_AST = null;
		
		try {      // for error handling
			AST tmp32_AST = null;
			tmp32_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp32_AST);
			match(SINGLETONS);
			{
			int _cnt76=0;
			_loop76:
			do {
				if ((LA(1)==LEFT_PARENTHESIS)) {
					points();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt76>=1 ) { break _loop76; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt76++;
			} while (true);
			}
			singletons_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = singletons_AST;
	}
	
	public final void piece_wise_linear() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST piece_wise_linear_AST = null;
		
		try {      // for error handling
			{
			int _cnt71=0;
			_loop71:
			do {
				if ((LA(1)==LEFT_PARENTHESIS)) {
					points();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt71>=1 ) { break _loop71; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt71++;
			} while (true);
			}
			piece_wise_linear_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = piece_wise_linear_AST;
	}
	
	public final void gauss() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST gauss_AST = null;
		
		try {      // for error handling
			AST tmp33_AST = null;
			tmp33_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp33_AST);
			match(GAUSS);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			gauss_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = gauss_AST;
	}
	
	public final void trian() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST trian_AST = null;
		
		try {      // for error handling
			AST tmp34_AST = null;
			tmp34_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp34_AST);
			match(TRIAN);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			trian_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = trian_AST;
	}
	
	public final void trape() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST trape_AST = null;
		
		try {      // for error handling
			AST tmp35_AST = null;
			tmp35_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp35_AST);
			match(TRAPE);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			trape_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = trape_AST;
	}
	
	public final void sigm() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST sigm_AST = null;
		
		try {      // for error handling
			AST tmp36_AST = null;
			tmp36_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp36_AST);
			match(SIGM);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			sigm_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = sigm_AST;
	}
	
	public final void gbell() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST gbell_AST = null;
		
		try {      // for error handling
			AST tmp37_AST = null;
			tmp37_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp37_AST);
			match(GBELL);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			gbell_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = gbell_AST;
	}
	
	public final void atom() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST atom_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case ID:
			{
				AST tmp38_AST = null;
				tmp38_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp38_AST);
				match(ID);
				atom_AST = (AST)currentAST.root;
				break;
			}
			case MINUS:
			case PLUS:
			case REAL:
			{
				real();
				astFactory.addASTChild(currentAST, returnAST);
				atom_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_9);
		}
		returnAST = atom_AST;
	}
	
	public final void points() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST points_AST = null;
		
		try {      // for error handling
			AST tmp39_AST = null;
			tmp39_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp39_AST);
			match(LEFT_PARENTHESIS);
			atom();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			AST tmp41_AST = null;
			tmp41_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp41_AST);
			match(REAL);
			match(RIGHT_PARENTHESIS);
			points_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_10);
		}
		returnAST = points_AST;
	}
	
	public final void real() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST real_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case PLUS:
			{
				AST tmp43_AST = null;
				tmp43_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp43_AST);
				match(PLUS);
				break;
			}
			case MINUS:
			{
				AST tmp44_AST = null;
				tmp44_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp44_AST);
				match(MINUS);
				break;
			}
			case REAL:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			AST tmp45_AST = null;
			tmp45_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp45_AST);
			match(REAL);
			real_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_9);
		}
		returnAST = real_AST;
	}
	
	public final void fun_pm() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fun_pm_AST = null;
		
		try {      // for error handling
			fun_md();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop87:
			do {
				if ((LA(1)==MINUS||LA(1)==PLUS)) {
					{
					switch ( LA(1)) {
					case PLUS:
					{
						AST tmp46_AST = null;
						tmp46_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp46_AST);
						match(PLUS);
						break;
					}
					case MINUS:
					{
						AST tmp47_AST = null;
						tmp47_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp47_AST);
						match(MINUS);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					fun_md();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop87;
				}
				
			} while (true);
			}
			fun_pm_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_11);
		}
		returnAST = fun_pm_AST;
	}
	
	public final void fun_md() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fun_md_AST = null;
		
		try {      // for error handling
			fun_mp();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop91:
			do {
				if ((LA(1)==SLASH||LA(1)==STAR)) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						AST tmp48_AST = null;
						tmp48_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp48_AST);
						match(STAR);
						break;
					}
					case SLASH:
					{
						AST tmp49_AST = null;
						tmp49_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp49_AST);
						match(SLASH);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					fun_mp();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop91;
				}
				
			} while (true);
			}
			fun_md_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_12);
		}
		returnAST = fun_md_AST;
	}
	
	public final void fun_mp() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fun_mp_AST = null;
		
		try {      // for error handling
			fun_atom();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop95:
			do {
				if ((LA(1)==HAT||LA(1)==PERCENT)) {
					{
					switch ( LA(1)) {
					case HAT:
					{
						AST tmp50_AST = null;
						tmp50_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp50_AST);
						match(HAT);
						break;
					}
					case PERCENT:
					{
						AST tmp51_AST = null;
						tmp51_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp51_AST);
						match(PERCENT);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					fun_atom();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop95;
				}
				
			} while (true);
			}
			fun_mp_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_13);
		}
		returnAST = fun_mp_AST;
	}
	
	public final void fun_atom() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fun_atom_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case ID:
			case MINUS:
			case PLUS:
			case REAL:
			{
				atom();
				astFactory.addASTChild(currentAST, returnAST);
				fun_atom_AST = (AST)currentAST.root;
				break;
			}
			case ABS:
			case COS:
			case EXP:
			case LN:
			case LOG:
			case SIN:
			case TAN:
			case LEFT_PARENTHESIS:
			{
				{
				switch ( LA(1)) {
				case EXP:
				{
					AST tmp52_AST = null;
					tmp52_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp52_AST);
					match(EXP);
					break;
				}
				case LN:
				{
					AST tmp53_AST = null;
					tmp53_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp53_AST);
					match(LN);
					break;
				}
				case LOG:
				{
					AST tmp54_AST = null;
					tmp54_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp54_AST);
					match(LOG);
					break;
				}
				case SIN:
				{
					AST tmp55_AST = null;
					tmp55_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp55_AST);
					match(SIN);
					break;
				}
				case COS:
				{
					AST tmp56_AST = null;
					tmp56_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp56_AST);
					match(COS);
					break;
				}
				case TAN:
				{
					AST tmp57_AST = null;
					tmp57_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp57_AST);
					match(TAN);
					break;
				}
				case ABS:
				{
					AST tmp58_AST = null;
					tmp58_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp58_AST);
					match(ABS);
					break;
				}
				case LEFT_PARENTHESIS:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(LEFT_PARENTHESIS);
				fun_pm();
				astFactory.addASTChild(currentAST, returnAST);
				match(RIGHT_PARENTHESIS);
				fun_atom_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_14);
		}
		returnAST = fun_atom_AST;
	}
	
	public final void defuzzify_item() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST defuzzify_item_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case METHOD:
			{
				defuzzification_method();
				astFactory.addASTChild(currentAST, returnAST);
				defuzzify_item_AST = (AST)currentAST.root;
				break;
			}
			case DEFAULT:
			{
				default_value();
				astFactory.addASTChild(currentAST, returnAST);
				defuzzify_item_AST = (AST)currentAST.root;
				break;
			}
			case TERM:
			{
				linguistic_term();
				astFactory.addASTChild(currentAST, returnAST);
				defuzzify_item_AST = (AST)currentAST.root;
				break;
			}
			case RANGE:
			{
				range();
				astFactory.addASTChild(currentAST, returnAST);
				defuzzify_item_AST = (AST)currentAST.root;
				break;
			}
			case ACCU:
			{
				accumulation_method();
				astFactory.addASTChild(currentAST, returnAST);
				defuzzify_item_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		returnAST = defuzzify_item_AST;
	}
	
	public final void defuzzification_method() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST defuzzification_method_AST = null;
		
		try {      // for error handling
			AST tmp61_AST = null;
			tmp61_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp61_AST);
			match(METHOD);
			match(COLON);
			{
			switch ( LA(1)) {
			case COG:
			{
				AST tmp63_AST = null;
				tmp63_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp63_AST);
				match(COG);
				break;
			}
			case COGS:
			{
				AST tmp64_AST = null;
				tmp64_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp64_AST);
				match(COGS);
				break;
			}
			case COGF:
			{
				AST tmp65_AST = null;
				tmp65_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp65_AST);
				match(COGF);
				break;
			}
			case COA:
			{
				AST tmp66_AST = null;
				tmp66_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp66_AST);
				match(COA);
				break;
			}
			case LM:
			{
				AST tmp67_AST = null;
				tmp67_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp67_AST);
				match(LM);
				break;
			}
			case RM:
			{
				AST tmp68_AST = null;
				tmp68_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp68_AST);
				match(RM);
				break;
			}
			case MM:
			{
				AST tmp69_AST = null;
				tmp69_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp69_AST);
				match(MM);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(SEMICOLON);
			defuzzification_method_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		returnAST = defuzzification_method_AST;
	}
	
	public final void default_value() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST default_value_AST = null;
		
		try {      // for error handling
			AST tmp71_AST = null;
			tmp71_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp71_AST);
			match(DEFAULT);
			match(ASSIGN_OPERATOR);
			{
			switch ( LA(1)) {
			case REAL:
			{
				AST tmp73_AST = null;
				tmp73_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp73_AST);
				match(REAL);
				break;
			}
			case NC:
			{
				AST tmp74_AST = null;
				tmp74_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp74_AST);
				match(NC);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(SEMICOLON);
			default_value_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		returnAST = default_value_AST;
	}
	
	public final void accumulation_method() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST accumulation_method_AST = null;
		
		try {      // for error handling
			AST tmp76_AST = null;
			tmp76_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp76_AST);
			match(ACCU);
			match(COLON);
			{
			switch ( LA(1)) {
			case MAX:
			{
				AST tmp78_AST = null;
				tmp78_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp78_AST);
				match(MAX);
				break;
			}
			case BSUM:
			{
				AST tmp79_AST = null;
				tmp79_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp79_AST);
				match(BSUM);
				break;
			}
			case NSUM:
			{
				AST tmp80_AST = null;
				tmp80_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp80_AST);
				match(NSUM);
				break;
			}
			case PROBOR:
			{
				AST tmp81_AST = null;
				tmp81_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp81_AST);
				match(PROBOR);
				break;
			}
			case SUM:
			{
				AST tmp82_AST = null;
				tmp82_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp82_AST);
				match(SUM);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(SEMICOLON);
			accumulation_method_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_16);
		}
		returnAST = accumulation_method_AST;
	}
	
	public final void rule_item() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST rule_item_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case AND:
			case OR:
			{
				operator_definition();
				astFactory.addASTChild(currentAST, returnAST);
				rule_item_AST = (AST)currentAST.root;
				break;
			}
			case ACT:
			{
				activation_method();
				astFactory.addASTChild(currentAST, returnAST);
				rule_item_AST = (AST)currentAST.root;
				break;
			}
			case ACCU:
			{
				accumulation_method();
				astFactory.addASTChild(currentAST, returnAST);
				rule_item_AST = (AST)currentAST.root;
				break;
			}
			case RULE:
			{
				rule();
				astFactory.addASTChild(currentAST, returnAST);
				rule_item_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_17);
		}
		returnAST = rule_item_AST;
	}
	
	public final void operator_definition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST operator_definition_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case OR:
			{
				operator_definition_or();
				astFactory.addASTChild(currentAST, returnAST);
				operator_definition_AST = (AST)currentAST.root;
				break;
			}
			case AND:
			{
				operator_definition_and();
				astFactory.addASTChild(currentAST, returnAST);
				operator_definition_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_17);
		}
		returnAST = operator_definition_AST;
	}
	
	public final void activation_method() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST activation_method_AST = null;
		
		try {      // for error handling
			AST tmp84_AST = null;
			tmp84_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp84_AST);
			match(ACT);
			match(COLON);
			{
			switch ( LA(1)) {
			case PROD:
			{
				AST tmp86_AST = null;
				tmp86_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp86_AST);
				match(PROD);
				break;
			}
			case MIN:
			{
				AST tmp87_AST = null;
				tmp87_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp87_AST);
				match(MIN);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(SEMICOLON);
			activation_method_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_17);
		}
		returnAST = activation_method_AST;
	}
	
	public final void rule() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST rule_AST = null;
		
		try {      // for error handling
			AST tmp89_AST = null;
			tmp89_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp89_AST);
			match(RULE);
			rule_name();
			astFactory.addASTChild(currentAST, returnAST);
			match(COLON);
			if_clause();
			astFactory.addASTChild(currentAST, returnAST);
			then_clause();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case WITH:
			{
				with();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMICOLON:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(SEMICOLON);
			rule_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_17);
		}
		returnAST = rule_AST;
	}
	
	public final void operator_definition_or() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST operator_definition_or_AST = null;
		
		try {      // for error handling
			AST tmp92_AST = null;
			tmp92_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp92_AST);
			match(OR);
			match(COLON);
			{
			switch ( LA(1)) {
			case MAX:
			{
				AST tmp94_AST = null;
				tmp94_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp94_AST);
				match(MAX);
				break;
			}
			case ASUM:
			{
				AST tmp95_AST = null;
				tmp95_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp95_AST);
				match(ASUM);
				break;
			}
			case BSUM:
			{
				AST tmp96_AST = null;
				tmp96_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp96_AST);
				match(BSUM);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(SEMICOLON);
			operator_definition_or_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_17);
		}
		returnAST = operator_definition_or_AST;
	}
	
	public final void operator_definition_and() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST operator_definition_and_AST = null;
		
		try {      // for error handling
			AST tmp98_AST = null;
			tmp98_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp98_AST);
			match(AND);
			match(COLON);
			{
			switch ( LA(1)) {
			case MIN:
			{
				AST tmp100_AST = null;
				tmp100_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp100_AST);
				match(MIN);
				break;
			}
			case PROD:
			{
				AST tmp101_AST = null;
				tmp101_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp101_AST);
				match(PROD);
				break;
			}
			case BDIF:
			{
				AST tmp102_AST = null;
				tmp102_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp102_AST);
				match(BDIF);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(SEMICOLON);
			operator_definition_and_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_17);
		}
		returnAST = operator_definition_and_AST;
	}
	
	public final void rule_name() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST rule_name_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case ID:
			{
				AST tmp104_AST = null;
				tmp104_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp104_AST);
				match(ID);
				rule_name_AST = (AST)currentAST.root;
				break;
			}
			case REAL:
			{
				AST tmp105_AST = null;
				tmp105_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp105_AST);
				match(REAL);
				rule_name_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_18);
		}
		returnAST = rule_name_AST;
	}
	
	public final void if_clause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST if_clause_AST = null;
		
		try {      // for error handling
			AST tmp106_AST = null;
			tmp106_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp106_AST);
			match(IF);
			condition();
			astFactory.addASTChild(currentAST, returnAST);
			if_clause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_19);
		}
		returnAST = if_clause_AST;
	}
	
	public final void then_clause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST then_clause_AST = null;
		
		try {      // for error handling
			AST tmp107_AST = null;
			tmp107_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp107_AST);
			match(THEN);
			conclusion();
			astFactory.addASTChild(currentAST, returnAST);
			then_clause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_20);
		}
		returnAST = then_clause_AST;
	}
	
	public final void with() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST with_AST = null;
		
		try {      // for error handling
			AST tmp108_AST = null;
			tmp108_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp108_AST);
			match(WITH);
			AST tmp109_AST = null;
			tmp109_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp109_AST);
			match(REAL);
			with_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = with_AST;
	}
	
	public final void condition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST condition_AST = null;
		
		try {      // for error handling
			subcondition();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop128:
			do {
				if ((LA(1)==AND||LA(1)==OR)) {
					{
					switch ( LA(1)) {
					case AND:
					{
						AST tmp110_AST = null;
						tmp110_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp110_AST);
						match(AND);
						break;
					}
					case OR:
					{
						AST tmp111_AST = null;
						tmp111_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp111_AST);
						match(OR);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					subcondition();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop128;
				}
				
			} while (true);
			}
			condition_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_21);
		}
		returnAST = condition_AST;
	}
	
	public final void conclusion() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST conclusion_AST = null;
		
		try {      // for error handling
			sub_conclusion();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				sub_conclusion();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case WITH:
			case SEMICOLON:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			conclusion_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_20);
		}
		returnAST = conclusion_AST;
	}
	
	public final void subcondition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST subcondition_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case NOT:
			{
				AST tmp113_AST = null;
				tmp113_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp113_AST);
				match(NOT);
				break;
			}
			case ID:
			case LEFT_PARENTHESIS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case ID:
			{
				subcondition_bare();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_PARENTHESIS:
			{
				subcondition_paren();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			subcondition_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_22);
		}
		returnAST = subcondition_AST;
	}
	
	public final void subcondition_bare() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST subcondition_bare_AST = null;
		
		try {      // for error handling
			AST tmp114_AST = null;
			tmp114_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp114_AST);
			match(ID);
			{
			switch ( LA(1)) {
			case IS:
			{
				match(IS);
				{
				switch ( LA(1)) {
				case NOT:
				{
					AST tmp116_AST = null;
					tmp116_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp116_AST);
					match(NOT);
					break;
				}
				case ID:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				AST tmp117_AST = null;
				tmp117_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp117_AST);
				match(ID);
				break;
			}
			case AND:
			case OR:
			case THEN:
			case RIGHT_PARENTHESIS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			subcondition_bare_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_22);
		}
		returnAST = subcondition_bare_AST;
	}
	
	public final void subcondition_paren() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST subcondition_paren_AST = null;
		
		try {      // for error handling
			AST tmp118_AST = null;
			tmp118_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp118_AST);
			match(LEFT_PARENTHESIS);
			condition();
			astFactory.addASTChild(currentAST, returnAST);
			match(RIGHT_PARENTHESIS);
			subcondition_paren_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_22);
		}
		returnAST = subcondition_paren_AST;
	}
	
	public final void sub_conclusion() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST sub_conclusion_AST = null;
		
		try {      // for error handling
			AST tmp120_AST = null;
			tmp120_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp120_AST);
			match(ID);
			match(IS);
			AST tmp122_AST = null;
			tmp122_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp122_AST);
			match(ID);
			sub_conclusion_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_23);
		}
		returnAST = sub_conclusion_AST;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"abs\"",
		"\"accu\"",
		"\"act\"",
		"\"and\"",
		"ASSIGN_OPERATOR",
		"\"asum\"",
		"\"bdif\"",
		"\"bsum\"",
		"\"coa\"",
		"\"cog\"",
		"\"cogs\"",
		"\"cogf\"",
		"\"cos\"",
		"COMMENT",
		"COMMENT_C",
		"COMMENT_SL",
		"\"default\"",
		"\"defuzzify\"",
		"DOT",
		"DOTS",
		"\"end_defuzzify\"",
		"\"end_function_block\"",
		"\"end_fuzzify\"",
		"\"end_ruleblock\"",
		"\"end_var\"",
		"\"exp\"",
		"\"function\"",
		"\"gauss\"",
		"\"gbell\"",
		"\"function_block\"",
		"\"fuzzify\"",
		"\"id\"",
		"\"if\"",
		"\"is\"",
		"\"lm\"",
		"\"ln\"",
		"\"log\"",
		"\"max\"",
		"\"method\"",
		"\"min\"",
		"\"mm\"",
		"\"nc\"",
		"\"not\"",
		"\"nsum\"",
		"\"or\"",
		"\"probor\"",
		"\"prod\"",
		"\"range\"",
		"\"rm\"",
		"\"rule\"",
		"\"ruleblock\"",
		"\"sigm\"",
		"\"sin\"",
		"\"singletons\"",
		"\"sum\"",
		"\"tan\"",
		"\"term\"",
		"\"then\"",
		"\"trape\"",
		"\"trian\"",
		"\"real\"",
		"\"var_input\"",
		"\"var_output\"",
		"\"with\"",
		"COLON",
		"COMMA",
		"HAT",
		"LEFT_CURLY",
		"LEFT_PARENTHESIS",
		"MINUS",
		"PERCENT",
		"PLUS",
		"RIGHT_CURLY",
		"RIGHT_PARENTHESIS",
		"SEMICOLON",
		"SLASH",
		"STAR",
		"WS",
		"NEWLINE",
		"REAL"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 18014415691448320L, 6L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 8589934594L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 18014415725002752L, 6L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 1155177702468091936L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 9288674231451872L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 34628173824L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 0L, 16384L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 1155177702551978016L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 34368126976L, 650848L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 0L, 16640L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 0L, 24576L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 0L, 27136L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 0L, 125440L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 0L, 126528L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 1155177702484869152L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 1164466376850538720L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 9288674365669600L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 0L, 16L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 2305843009213693952L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = { 0L, 16392L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { 2305843009213693952L, 8192L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { 2306124484190404736L, 8192L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = { 0L, 16424L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	
	}
