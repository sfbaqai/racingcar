//-----------------------------------------------------------------------------
//
// FCL (IEC 1131) lexer & parser implementation
// using antlr (see http://www.antlr.org/)
//
//					Pablo Cingolani pcingola@tacner.com
//-----------------------------------------------------------------------------


//-----------------------------------------------------------------------------
// Lexer
//-----------------------------------------------------------------------------
header {package net.sourceforge.jFuzzyLogic.fcl;} // Head each java file using this 'package' declaration
class FCLLexer extends Lexer;

options {
	charVocabulary = '\u0000'..'\u007F';	// charVocabulary = '\0'..'\377';
	exportVocab = FCL;						// call the vocabulary "FCL"
	testLiterals = false;					// don't automatically test for literals
	k = 4;									// four characters of lookahead
	caseSensitive = false;					// not case sensitive
	caseSensitiveLiterals = false;			// not case sensitive for literals
}


// Tokens (reserved words)
tokens {
		ABS = "abs";
		ACCU = "accu";
		ACT = "act";
		AND = "and";
		ASSIGN_OPERATOR;
		ASUM = "asum";
		BDIF = "bdif";
		BSUM = "bsum";
		COA = "coa";
		COG = "cog";
		COGS = "cogs";
		COGF = "cogf";
		COS = "cos";
		COMMENT;
		COMMENT_C;
		COMMENT_SL;
		DEFAULT = "default";
		DEFUZZIFY = "defuzzify";
		DOT; 
		DOTS;
		END_DEFUZZIFY = "end_defuzzify";
		END_FUNCTION_BLOCK = "end_function_block";
		END_FUZZIFY = "end_fuzzify";
		END_RULEBLOCK = "end_ruleblock";
		END_VAR = "end_var";
		EXP = "exp";
		FUNCTION = "function";
		GAUSS = "gauss";
		GBELL = "gbell";
		FUNCTION_BLOCK = "function_block"; 
		FUZZIFY = "fuzzify";
		ID = "id";
		IF = "if";
		IS = "is";
		LM = "lm";
		LN = "ln";
		LOG = "log";
		MAX = "max";
		METHOD = "method";
		MIN = "min";
		MM = "mm";
		NC = "nc";
		NOT = "not";
		NSUM = "nsum";
		OR = "or";
		PROBOR = "probor";
		PROD = "prod";
		RANGE = "range";
		RM = "rm";
		RULE= "rule";
		RULEBLOCK = "ruleblock";
		SIGM = "sigm";
		SIN = "sin";
		SINGLETONS = "singletons";
		SUM = "sum";
		TAN = "tan";
		TERM = "term";
		THEN = "then";
		TRAPE = "trape";
		TRIAN = "trian";
		TYPE_REAL = "real";
		VAR_INPUT = "var_input";
		VAR_OUTPUT = "var_output";
		WITH = "with";		
}

// Common symbols
ASSIGN_OPERATOR : ':' '=';
COLON : ':';
COMMA : ',';
HAT  : '^' ;
LEFT_CURLY : '{';
LEFT_PARENTHESIS: '(';
MINUS : '-' ;
PERCENT  : '%' ;
PLUS  : '+' ;
RIGHT_CURLY : '}';
RIGHT_PARENTHESIS: ')' ;
SEMICOLON  : ';' ;
SLASH  : '/' ;
STAR  : '*' ;

// '.' and '..'
DOT : 	'.'
		( '.' (' ' | '\t' | '\n' | '\r' '\n')	{ $setType(DOTS); } )?;

// White spaces (space, tab, newLine, etc.)
WS    : ( ' ' | '\t')	{ $setType(Token.SKIP); };
NEWLINE    : ( '\r' '\n' | '\n' )	{ newline(); $setType(Token.SKIP); };

// Real number (float/double) without any sign
REAL  :  ('0'..'9')+ ( '.' ('0'..'9')+ )? ('e' (PLUS|MINUS)? ('0'..'9')+)?;

// An identifier.  Note that testLiterals is set to true!  This means
// that after we match the rule, we look in the literals table to see
// if it's a literal or really an identifer
ID
	options { testLiterals = true; }
	:   ('a'..'z') ('a'..'z'|'0'..'9'|'_')*   // Ids must start with a letter
	;

// FCL style comments
COMMENT : "(*"
			( options { generateAmbigWarnings=false; }
			:    { LA(2) != ')' }? '*'
			|    '\r' '\n'       {newline();}
			|    '\n'            {newline();}
			|   ~('*' | '\n' | '\r')
			)*
			"*)"
			{$setType(Token.SKIP);}
			;

// 'C' style comments
COMMENT_C : "/*"
			( options { generateAmbigWarnings=false; }
			:    { LA(2) != '/' }? '*'
			|    '\r' '\n'       {newline();}
			|    '\n'            {newline();}
			|   ~('*' | '\n' | '\r')
			)*
			"*/"
			{$setType(Token.SKIP);}
			;

// 'C' style single line comments
COMMENT_SL : "//"
			(~'\n')* '\n'
			{ $setType(Token.SKIP); newline(); }
			;


//-----------------------------------------------------------------------------
// Parser
//-----------------------------------------------------------------------------
class FCLParser extends Parser;
options { buildAST=true; }

// FCL file may contain several funcion blocks
fcl : (function_block)+;

// Function block
function_block : FUNCTION_BLOCK^ (ID)? (declaration)* END_FUNCTION_BLOCK! ; 
declaration : var_input | var_output | fuzzify_block | defuzzify_block | rule_block;

// Variables input and output
var_input : VAR_INPUT^ (var_def)* END_VAR!;
var_output : VAR_OUTPUT^ (var_def)+ END_VAR!;
var_def : ID^ COLON! data_type SEMICOLON! (range)? ;

// Fuzzify
fuzzify_block : FUZZIFY^ ID (linguistic_term)* END_FUZZIFY!;
linguistic_term: TERM^ ID ASSIGN_OPERATOR! membership_function SEMICOLON!;
membership_function : function | singleton | singletons | piece_wise_linear | gauss | trian | trape | sigm | gbell ;
gauss: GAUSS^ atom atom;
gbell: GBELL^ atom atom atom;
piece_wise_linear: (points)+ ;
sigm: SIGM^ atom atom;
singleton : atom;
singletons: SINGLETONS^ (points)+ ;
trape: TRAPE^ atom atom atom atom;
trian: TRIAN^ atom atom atom;
points : LEFT_PARENTHESIS^ atom COMMA! REAL RIGHT_PARENTHESIS!;
atom : ID | real;
real: (PLUS^|MINUS^)? REAL;

// Functions (for singletons)
function: FUNCTION^ fun_pm;
fun_pm: fun_md ((PLUS^ | MINUS^ ) fun_md)*;						// Function plus or minus
fun_md: fun_mp ((STAR^ | SLASH^) fun_mp)*;						// Function multiply or divide
fun_mp : fun_atom ((HAT^ | PERCENT^) fun_atom)*;				// Function modulus or power
fun_atom : atom | (EXP^|LN^|LOG^|SIN^|COS^|TAN^|ABS^)? LEFT_PARENTHESIS! fun_pm RIGHT_PARENTHESIS!;	// Atom and parenthesis

// Defuzzify
defuzzify_block : DEFUZZIFY^ ID (defuzzify_item)* END_DEFUZZIFY!;
defuzzify_item : defuzzification_method | default_value | linguistic_term | range | accumulation_method;
range : RANGE^ ASSIGN_OPERATOR! LEFT_PARENTHESIS! REAL DOTS! REAL RIGHT_PARENTHESIS! SEMICOLON!;
defuzzification_method : METHOD^ COLON! (COG|COGS|COGF|COA|LM|RM|MM) SEMICOLON!;
default_value : DEFAULT^ ASSIGN_OPERATOR! (REAL | NC) SEMICOLON!;
accumulation_method : ACCU^ COLON! (MAX|BSUM|NSUM|PROBOR|SUM) SEMICOLON!;

// Ruleblock
rule_block : RULEBLOCK^ ID (rule_item)* END_RULEBLOCK!;
rule_item : operator_definition | activation_method | accumulation_method | rule;
operator_definition : operator_definition_or | operator_definition_and;
operator_definition_or : OR^ COLON! (MAX|ASUM|BSUM) SEMICOLON!;
operator_definition_and : AND^ COLON! (MIN|PROD|BDIF) SEMICOLON!;
activation_method : ACT^ COLON! (PROD|MIN) SEMICOLON!;
rule : RULE^ rule_name COLON! if_clause then_clause (with)? SEMICOLON! ;
rule_name : ID | REAL;
if_clause : IF^ condition;
then_clause : THEN^ conclusion;
condition : subcondition ((AND^|OR^) subcondition)*;
subcondition : (NOT^)? (subcondition_bare | subcondition_paren);
subcondition_bare : ID^ (IS! (NOT)? ID)? ;
subcondition_paren : LEFT_PARENTHESIS^ condition RIGHT_PARENTHESIS!;
conclusion : sub_conclusion (COMMA! sub_conclusion)?;
sub_conclusion : ID^ IS! ID;
with: WITH^ REAL;

// Data type
data_type : TYPE_REAL;
