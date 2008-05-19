//-----------------------------------------------------------------------------
//
// FCL (IEC 1131) lexer & parser implementation
// using antlr (see http://www.antlr.org/)
//
//					Pablo Cingolani pcingola@sinectis.com
//-----------------------------------------------------------------------------


//-----------------------------------------------------------------------------
// Lexer
//-----------------------------------------------------------------------------
class FCLLexer extends Lexer;

options {
	// charVocabulary = '\0'..'\377';
	charVocabulary = '\u0000'..'\u007F';
	exportVocab = FCL;		// call the vocabulary "Pascal"
	testLiterals = false;	// don't automatically test for literals
	k = 4;					// four characters of lookahead
	caseSensitive = false;
	caseSensitiveLiterals = false;
}

tokens {
		ACCU = "accu";
		ACT = "act";
		AND = "and";
		ASSIGN_OPERATOR;
		ASUM = "asum";
		BDIF = "bdif";
		BSUM = "bsum";
		COA = "COA";
		COG = "COG";
		COGS = "COGS";
		DEFAULT = "default";
		DEFUZZIFY = "defuzzify";
		DOT; 
		DOTS;
		END_DEFUZZIFY = "end_defuzzify";
		END_FUNCTION_BLOCK = "end_function_block";
		END_FUZZIFY = "end_fuzzify";
		END_RULEBLOCK = "end_ruleblock";
		END_VAR = "end_var";
		FUNCTION_BLOCK = "function_block"; 
		FUZZIFY = "fuzzify";
		ID = "id";
		IF = "if";
		IS = "is";
		LEFT_PARENTHESIS_STAR;
		LM = "LM";
		MAX = "max";
		METHOD = "method";
		MIN = "min";
		NC = "nc";
		NSUM = "nsum";
		OR = "or";
		PROD = "prod";
		RANGE = "range";
		REAL; 
		RM = "RM";
		RIGHT_PARENTHESIS_STAR;
		RULE= "rule";
		RULEBLOCK = "ruleblock";
		TERM = "term";
		THEN = "then";
		TYPE_REAL = "real";
		VAR_INPUT = "var_input";
		VAR_OUTPUT = "var_output";
		WITH = "with";
}

// Common symbols
COLON : ':';
COMMA : ',';
LEFT_CURLY : '{';
RIGHT_CURLY : '}';
RIGHT_PARENTHESIS: ')' ;
SEMICOLON  : ';' ;

// ':='
ASSIGN_OPERATOR : ':' '=';

// '(' and "(*"
LEFT_PARENTHESIS: 	'('
					( '*'	{ $setType(LEFT_PARENTHESIS_STAR); } )? ;

// '.' and '..'
DOT : 	'.'
		( '.' (' ' | '\t' | '\n' | '\r' '\n')	{ $setType(DOTS); } )?;

// '*' and "*)"
STAR  : '*' 
		( ')'	{ $setType(RIGHT_PARENTHESIS_STAR); }
		|		{ $setType(STAR); }
		);

// Real number (float/double)
REAL   :  ('+' | '-')? ('0'..'9')+ ( '.' ('0'..'9')+ )?;

// White spaces (space, tab, newLine, etc.)
WS    : ( ' ' | '\t')	{ newline(); $setType(Token.SKIP); };
NEWLINE    : ( '\r' '\n' | '\n' )	{ newline(); $setType(Token.SKIP); };

// An identifier.  Note that testLiterals is set to true!  This means
// that after we match the rule, we look in the literals table to see
// if it's a literal or really an identifer
ID
	options { testLiterals = true; }
	:   ('a'..'z') ('a'..'z'|'0'..'9'|'_')*   //pspsps
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

// Variables in/out
var_input : VAR_INPUT^ (var_def)* END_VAR!;
var_output : VAR_OUTPUT^ (var_def)+ END_VAR!;
var_def : ID^ COLON! data_type SEMICOLON! (range)? ;
// var_def : ID^ COLON! data_type SEMICOLON! (var_range)? ;
var_range : LEFT_PARENTHESIS_STAR! RANGE^ LEFT_PARENTHESIS! REAL DOTS! REAL RIGHT_PARENTHESIS! RIGHT_PARENTHESIS_STAR!;

// Fuzzify
fuzzify_block : FUZZIFY^ ID (linguistic_term)* END_FUZZIFY!;
linguistic_term: TERM^ ID ASSIGN_OPERATOR! membership_function SEMICOLON!;
membership_function : singleton | (points)+ ;
singleton : REAL | ID;
points : LEFT_PARENTHESIS^ singleton COMMA! REAL RIGHT_PARENTHESIS!;

// Defuzzify
defuzzify_block : DEFUZZIFY^ ID (defuzzify_item)* END_DEFUZZIFY!;
defuzzify_item : defuzzification_method | default_value | linguistic_term | range | accumulation_method;
range : RANGE^ ASSIGN_OPERATOR LEFT_PARENTHESIS! REAL DOTS! REAL RIGHT_PARENTHESIS!;
defuzzification_method : METHOD^ COLON! (COG|COGS|COA|LM|RM) SEMICOLON!;
default_value : DEFAULT^ ASSIGN_OPERATOR! (REAL | NC) SEMICOLON!;
accumulation_method : ACCU^ COLON! (MAX|BSUM|NSUM) SEMICOLON!;

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
condition : subcondition ((AND^|OR^) subcondition)+;
subcondition : subcondition_bare | subcondition_paren;
subcondition_bare : (NOT^)? ID^ (IS! (NOT)? ID)? ;
subcondition_paren : LEFT_PARENTHESIS^ condition RIGHT_PARENTHESIS!;
conclusion : sub_conclusion (COMMA! sub_conclusion)?;
sub_conclusion : ID^ IS! ID;
with: WITH^ REAL;

// Data type
data_type : TYPE_REAL;

