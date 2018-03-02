header {
	package com.freequery.expression.antlr;

	import com.freequery.*;
}

class ExpressionParser extends Parser;

options {
	buildAST = true;
}

tokens {
	EXPR;
	TEXTNODE;
	PARAMNODE;
	COLUMNNODE;
	TABLENODE;
	FUNCNODE;
	FUNCPARAM;
	USERPROPNODE;
	SUBQUERY;
	LPARAM_IGNORE_AREA;
	RPARAM_IGNORE_AREA;
}

{
	private String expr;
	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public void reportError(RecognitionException ex) {
		throw new FreeQueryException(QueryErrorCode.EXPR_PARSE_ERROR).setDetail(ex.getMessage() + "\nat line:" + ex.getLine() + " column:" + ex.getColumn() + "\n" + this.expr);
	}
}

parse
	:	parseNode
	{ #parse = #([EXPR], #parse);}
	;

parseNode
	:
	(
		textNodes
	|	specialNodes
	)?
	EOF!
	;

textNodes
	{
		StringBuilder buf = new StringBuilder();
		int textNodeType = 0;
	}
	:	(	t:textItem! {
				buf.append(#t);
			} 
		|	tl:NEW_LINE! {
				if(buf.length() > 0) {
					astFactory.addASTChild(currentAST, #([TEXTNODE, buf.toString()]));
					buf.setLength(0);
				}
				astFactory.addASTChild(currentAST, #([TEXTNODE, #tl.getText()]));
			}
		)+
		{
			if(buf.length() > 0)
				astFactory.addASTChild(currentAST, #([TEXTNODE, buf.toString()]));
		}
		(	(
				ignoreAreaNodes	(textNodes | specialNodes)?
			)
		 | 
			specialNodes)?
	;

textItem
	:	(
		STRING_LITERAL
	|	LPAREN
	|	RPAREN
	|	UNDER
	|	COMMA
	|	DOUBLE_QUOTED
	|	LPARENG
	|	RPARENG
	|	LPARENF
	|	RPARENF
	)
	;
	
textItemLine
	:	NEW_LINE
	;


ignoreAreaNodes
	:
		LPARAM_IGNORE_AREA^
		(textNodes | specialNodes)
		RPARAM_IGNORE_AREA!
	;

specialNodes
	:	(specialItem)+ (textNodes)?
	;

specialItem
	:	CTRL! 
		(
			type:STRING_LITERAL!
			UNDER!
			nodeId:nodeIdList!
			(
				{ if(type.getText().charAt(0) != 'F')
					throw new FreeQueryException(QueryErrorCode.EXPR_PARSE_ERROR).setDetail("Only functions can have params");
				}
				LPAREN!
					(params)?
				RPAREN!

			)?
		)
		CTRL!
		{
			char typeCh = type.getText().charAt(0);
			String nodeIdStr = #nodeId.getText();
			switch(typeCh) {
				case 'A':
					#specialItem = #([USERPROPNODE, nodeIdStr], #specialItem);
					break;
				case 'P':
					#specialItem = #([PARAMNODE, nodeIdStr], #specialItem);
					break;
				case 'C':
					#specialItem = #([COLUMNNODE, nodeIdStr], #specialItem);
					break;
				case 'F':
					#specialItem = #([FUNCNODE, nodeIdStr], #specialItem);
					break;
				case 'T':
					#specialItem = #([TABLENODE, nodeIdStr], #specialItem);
					break;
				case 'S':
					#specialItem = #([SUBQUERY, nodeIdStr], #specialItem);
					break;
				default:
					throw new FreeQueryException(QueryErrorCode.EXPR_PARSE_ERROR).setDetail(type.getText() + " unknown");
			}
		}
	;

nodeIdList
	{
		StringBuilder buf = new StringBuilder();
	}
	:	(
		s:STRING_LITERAL! {buf.append(s.getText());}
	|	u:UNDER! {buf.append(u.getText());}
	)+ { #nodeIdList = #([TEXTNODE, buf.toString()]);}
	;

params
	:	param (COMMA! param)*
	;

param
	:
	(	
		(paramPart)+
	|	quotedString
	)
	{ #param = #([FUNCPARAM, ""], #param);}
	;

quotedString
	{
		StringBuilder buf = new StringBuilder();
	}
	:	
	(	DOUBLE_QUOTED! {buf.append("\"");}
			(ns:nonQuotedString {buf.append(#ns);})*
		DOUBLE_QUOTED! {buf.append("\"");}
	) { #quotedString = #([TEXTNODE, buf.toString()]);}
	;

paramPart
	:	paramText
	|	specialItem
	;

paramText
	:
	(
		s:STRING_LITERAL
	) { #paramText = #([TEXTNODE, #s.getText()]);}
	;

nonQuotedString
	{
	}
	:	
	(
		STRING_LITERAL
	|	LPAREN
	|	RPAREN
	|	CTRL
	|	UNDER
	|	COMMA
	)
	;

class ExpressionLexer extends Lexer;

options {
	k = 2;
	charVocabulary = '\u0003'..'\uFFFE';
	testLiterals = false;
}


LPAREN	:	'(';
RPAREN	:	')';
CTRL	:	'^';
UNDER	:	'_';
COMMA	:	',';
DOUBLE_QUOTED:	'"';

protected
ESC		:	'\\'
		(	CTRL
		|	'\\'
		|	'"'
		)
		;
LPARENG	:	'{';
RPARENG	:	'}';
LPARENF	:	'[';
RPARENF	:	']';


LPARAM_IGNORE_AREA	
			:	"{["
			;
RPARAM_IGNORE_AREA
			:	 "]}"
			;

STRING_LITERAL
	:	(ESC | ~('^' | '(' | ')' | '\\' | '_' | ',' | '"' | '\r' | '\n' | '{' | '[' | '}' | ']'))+
	;

NEW_LINE
	:	('\r' | '\n' | "\n\r")
	;