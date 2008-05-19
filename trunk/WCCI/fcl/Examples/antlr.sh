#!/bin/sh

rm -vf ../src/net/sourceforge/jFuzzyLogic/fcl/FCLLexer.java \
	../src/net/sourceforge/jFuzzyLogic/fcl/FCLLexerTokenTypes.java  \
	../src/net/sourceforge/jFuzzyLogic/fcl/FCLParser.java  \
	../src/net/sourceforge/jFuzzyLogic/fcl/FCLTreeParser.java 

ANTLRJAR=`ls $PWD/../lib/antlr*.jar`
export CLASSPATH=$CLASSPATH:$ANTLRJAR
java antlr.Tool -o ../src/net/sourceforge/jFuzzyLogic/fcl fcl.g

