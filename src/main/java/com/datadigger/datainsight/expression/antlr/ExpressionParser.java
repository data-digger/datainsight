// $ANTLR 2.7.6 (2005-12-22): "expression.g" -> "ExpressionParser.java"$

	package com.datadigger.datainsight.expression.antlr;

	import antlr.*;
import antlr.collections.AST;
import antlr.collections.impl.ASTArray;
import antlr.collections.impl.BitSet;
import com.datadigger.datainsight.exception.DataDiggerException;

import com.datadigger.datainsight.exception.DataDiggerErrorCode;

public class ExpressionParser extends antlr.LLkParser       implements ExpressionParserTokenTypes
 {

	private String expr;
	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public void reportError(RecognitionException ex) {
		throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARSE_ERROR).setDetail("\nat line:" + ex.getLine() + " column:" + ex.getColumn() + "\n" + this.expr);
	}

protected ExpressionParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public ExpressionParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected ExpressionParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public ExpressionParser(TokenStream lexer) {
  this(lexer,1);
}

public ExpressionParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void parse() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parse_AST = null;
		
		try {      // for error handling
			parseNode();
			astFactory.addASTChild(currentAST, returnAST);
			parse_AST = (AST)currentAST.root;
			parse_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(EXPR)).add(parse_AST));
			currentAST.root = parse_AST;
			currentAST.child = parse_AST!=null &&parse_AST.getFirstChild()!=null ?
				parse_AST.getFirstChild() : parse_AST;
			currentAST.advanceChildToEnd();
			parse_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = parse_AST;
	}
	
	public final void parseNode() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parseNode_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case NEW_LINE:
			case STRING_LITERAL:
			case LPAREN:
			case RPAREN:
			case UNDER:
			case COMMA:
			case DOUBLE_QUOTED:
			case LPARENG:
			case RPARENG:
			case LPARENF:
			case RPARENF:
			{
				textNodes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case CTRL:
			{
				specialNodes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LPARAM_IGNORE_AREA:
			{
				{
				ignoreAreaNodes();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case NEW_LINE:
				case STRING_LITERAL:
				case LPAREN:
				case RPAREN:
				case UNDER:
				case COMMA:
				case DOUBLE_QUOTED:
				case LPARENG:
				case RPARENG:
				case LPARENF:
				case RPARENF:
				{
					textNodes();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case CTRL:
				{
					specialNodes();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case EOF:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				}
				break;
			}
			case EOF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(Token.EOF_TYPE);
			parseNode_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = parseNode_AST;
	}
	
	public final void textNodes() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST textNodes_AST = null;
		AST t_AST = null;
		Token  tl = null;
		AST tl_AST = null;
		
				StringBuilder buf = new StringBuilder();
				int textNodeType = 0;
			
		
		try {      // for error handling
			{
			int _cnt8=0;
			_loop8:
			do {
				switch ( LA(1)) {
				case STRING_LITERAL:
				case LPAREN:
				case RPAREN:
				case UNDER:
				case COMMA:
				case DOUBLE_QUOTED:
				case LPARENG:
				case RPARENG:
				case LPARENF:
				case RPARENF:
				{
					textItem();
					t_AST = (AST)returnAST;
					
									buf.append(t_AST);
								
					break;
				}
				case NEW_LINE:
				{
					tl = LT(1);
					tl_AST = astFactory.create(tl);
					match(NEW_LINE);
					
									if(buf.length() > 0) {
										astFactory.addASTChild(currentAST, (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(TEXTNODE,buf.toString()))));
										buf.setLength(0);
									}
									astFactory.addASTChild(currentAST, (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(TEXTNODE,tl.getText()))));
								
					break;
				}
				default:
				{
					if ( _cnt8>=1 ) { break _loop8; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				}
				_cnt8++;
			} while (true);
			}
			
						if(buf.length() > 0)
							astFactory.addASTChild(currentAST, (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(TEXTNODE,buf.toString()))));
					
			{
			switch ( LA(1)) {
			case LPARAM_IGNORE_AREA:
			{
				{
				ignoreAreaNodes();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case NEW_LINE:
				case STRING_LITERAL:
				case LPAREN:
				case RPAREN:
				case UNDER:
				case COMMA:
				case DOUBLE_QUOTED:
				case LPARENG:
				case RPARENG:
				case LPARENF:
				case RPARENF:
				{
					textNodes();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case CTRL:
				{
					specialNodes();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case EOF:
				case RPARAM_IGNORE_AREA:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				}
				break;
			}
			case CTRL:
			{
				specialNodes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case RPARAM_IGNORE_AREA:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			textNodes_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		returnAST = textNodes_AST;
	}
	
	public final void specialNodes() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST specialNodes_AST = null;
		
		try {      // for error handling
			{
			int _cnt19=0;
			_loop19:
			do {
				if ((LA(1)==CTRL)) {
					specialItem();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt19>=1 ) { break _loop19; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt19++;
			} while (true);
			}
			{
			switch ( LA(1)) {
			case NEW_LINE:
			case STRING_LITERAL:
			case LPAREN:
			case RPAREN:
			case UNDER:
			case COMMA:
			case DOUBLE_QUOTED:
			case LPARENG:
			case RPARENG:
			case LPARENF:
			case RPARENF:
			{
				textNodes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case RPARAM_IGNORE_AREA:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			specialNodes_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		returnAST = specialNodes_AST;
	}
	
	public final void ignoreAreaNodes() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST ignoreAreaNodes_AST = null;
		
		try {      // for error handling
			AST tmp2_AST = null;
			tmp2_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp2_AST);
			match(LPARAM_IGNORE_AREA);
			{
			switch ( LA(1)) {
			case NEW_LINE:
			case STRING_LITERAL:
			case LPAREN:
			case RPAREN:
			case UNDER:
			case COMMA:
			case DOUBLE_QUOTED:
			case LPARENG:
			case RPARENG:
			case LPARENF:
			case RPARENF:
			{
				textNodes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case CTRL:
			{
				specialNodes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RPARAM_IGNORE_AREA);
			ignoreAreaNodes_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		returnAST = ignoreAreaNodes_AST;
	}
	
	public final void textItem() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST textItem_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case STRING_LITERAL:
			{
				AST tmp4_AST = null;
				tmp4_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp4_AST);
				match(STRING_LITERAL);
				break;
			}
			case LPAREN:
			{
				AST tmp5_AST = null;
				tmp5_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp5_AST);
				match(LPAREN);
				break;
			}
			case RPAREN:
			{
				AST tmp6_AST = null;
				tmp6_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp6_AST);
				match(RPAREN);
				break;
			}
			case UNDER:
			{
				AST tmp7_AST = null;
				tmp7_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp7_AST);
				match(UNDER);
				break;
			}
			case COMMA:
			{
				AST tmp8_AST = null;
				tmp8_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp8_AST);
				match(COMMA);
				break;
			}
			case DOUBLE_QUOTED:
			{
				AST tmp9_AST = null;
				tmp9_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp9_AST);
				match(DOUBLE_QUOTED);
				break;
			}
			case LPARENG:
			{
				AST tmp10_AST = null;
				tmp10_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp10_AST);
				match(LPARENG);
				break;
			}
			case RPARENG:
			{
				AST tmp11_AST = null;
				tmp11_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp11_AST);
				match(RPARENG);
				break;
			}
			case LPARENF:
			{
				AST tmp12_AST = null;
				tmp12_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp12_AST);
				match(LPARENF);
				break;
			}
			case RPARENF:
			{
				AST tmp13_AST = null;
				tmp13_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp13_AST);
				match(RPARENF);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			textItem_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		returnAST = textItem_AST;
	}
	
	public final void textItemLine() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST textItemLine_AST = null;
		
		try {      // for error handling
			AST tmp14_AST = null;
			tmp14_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp14_AST);
			match(NEW_LINE);
			textItemLine_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = textItemLine_AST;
	}
	
	public final void specialItem() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST specialItem_AST = null;
		Token  type = null;
		AST type_AST = null;
		AST nodeId_AST = null;
		
		try {      // for error handling
			match(CTRL);
			{
			type = LT(1);
			type_AST = astFactory.create(type);
			match(STRING_LITERAL);
			match(UNDER);
			nodeIdList();
			nodeId_AST = (AST)returnAST;
			{
			switch ( LA(1)) {
			case LPAREN:
			{
				if(type.getText().charAt(0) != 'F')
									throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARSE_ERROR).setDetail("ֻ�к����ſ����в���");
								
				match(LPAREN);
				{
				switch ( LA(1)) {
				case STRING_LITERAL:
				case DOUBLE_QUOTED:
				case CTRL:
				{
					params();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case RPAREN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(RPAREN);
				break;
			}
			case CTRL:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			}
			match(CTRL);
			specialItem_AST = (AST)currentAST.root;
			
						char typeCh = type.getText().charAt(0);
						String nodeIdStr = nodeId_AST.getText();
						switch(typeCh) {
							case 'A':
								specialItem_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(USERPROPNODE,nodeIdStr)).add(specialItem_AST));
								break;
							case 'P':
								specialItem_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(PARAMNODE,nodeIdStr)).add(specialItem_AST));
								break;
							case 'C':
								specialItem_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(COLUMNNODE,nodeIdStr)).add(specialItem_AST));
								break;
							case 'F':
								specialItem_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FUNCNODE,nodeIdStr)).add(specialItem_AST));
								break;
							case 'T':
								specialItem_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TABLENODE,nodeIdStr)).add(specialItem_AST));
								break;
							case 'S':
								specialItem_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(SUBQUERY,nodeIdStr)).add(specialItem_AST));
								break;
							default:
								throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARSE_ERROR).setDetail(type.getText() + " Ϊδ֪����");
						}
					
			currentAST.root = specialItem_AST;
			currentAST.child = specialItem_AST!=null &&specialItem_AST.getFirstChild()!=null ?
				specialItem_AST.getFirstChild() : specialItem_AST;
			currentAST.advanceChildToEnd();
			specialItem_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		returnAST = specialItem_AST;
	}
	
	public final void nodeIdList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST nodeIdList_AST = null;
		Token  s = null;
		AST s_AST = null;
		Token  u = null;
		AST u_AST = null;
		
				StringBuilder buf = new StringBuilder();
			
		
		try {      // for error handling
			{
			int _cnt27=0;
			_loop27:
			do {
				switch ( LA(1)) {
				case STRING_LITERAL:
				{
					s = LT(1);
					s_AST = astFactory.create(s);
					match(STRING_LITERAL);
					buf.append(s.getText());
					break;
				}
				case UNDER:
				{
					u = LT(1);
					u_AST = astFactory.create(u);
					match(UNDER);
					buf.append(u.getText());
					break;
				}
				default:
				{
					if ( _cnt27>=1 ) { break _loop27; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				}
				_cnt27++;
			} while (true);
			}
			nodeIdList_AST = (AST)currentAST.root;
			nodeIdList_AST = (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(TEXTNODE,buf.toString())));
			currentAST.root = nodeIdList_AST;
			currentAST.child = nodeIdList_AST!=null &&nodeIdList_AST.getFirstChild()!=null ?
				nodeIdList_AST.getFirstChild() : nodeIdList_AST;
			currentAST.advanceChildToEnd();
			nodeIdList_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		returnAST = nodeIdList_AST;
	}
	
	public final void params() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST params_AST = null;
		
		try {      // for error handling
			param();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop30:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					param();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop30;
				}
				
			} while (true);
			}
			params_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
		returnAST = params_AST;
	}
	
	public final void param() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST param_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case STRING_LITERAL:
			case CTRL:
			{
				{
				int _cnt34=0;
				_loop34:
				do {
					if ((LA(1)==STRING_LITERAL||LA(1)==CTRL)) {
						paramPart();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						if ( _cnt34>=1 ) { break _loop34; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt34++;
				} while (true);
				}
				break;
			}
			case DOUBLE_QUOTED:
			{
				quotedString();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			param_AST = (AST)currentAST.root;
			param_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FUNCPARAM,"")).add(param_AST));
			currentAST.root = param_AST;
			currentAST.child = param_AST!=null &&param_AST.getFirstChild()!=null ?
				param_AST.getFirstChild() : param_AST;
			currentAST.advanceChildToEnd();
			param_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		returnAST = param_AST;
	}
	
	public final void paramPart() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST paramPart_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case STRING_LITERAL:
			{
				paramText();
				astFactory.addASTChild(currentAST, returnAST);
				paramPart_AST = (AST)currentAST.root;
				break;
			}
			case CTRL:
			{
				specialItem();
				astFactory.addASTChild(currentAST, returnAST);
				paramPart_AST = (AST)currentAST.root;
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
		returnAST = paramPart_AST;
	}
	
	public final void quotedString() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST quotedString_AST = null;
		AST ns_AST = null;
		
				StringBuilder buf = new StringBuilder();
			
		
		try {      // for error handling
			{
			match(DOUBLE_QUOTED);
			buf.append("\"");
			{
			_loop38:
			do {
				if ((_tokenSet_8.member(LA(1)))) {
					nonQuotedString();
					ns_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
					buf.append(ns_AST);
				}
				else {
					break _loop38;
				}
				
			} while (true);
			}
			match(DOUBLE_QUOTED);
			buf.append("\"");
			}
			quotedString_AST = (AST)currentAST.root;
			quotedString_AST = (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(TEXTNODE,buf.toString())));
			currentAST.root = quotedString_AST;
			currentAST.child = quotedString_AST!=null &&quotedString_AST.getFirstChild()!=null ?
				quotedString_AST.getFirstChild() : quotedString_AST;
			currentAST.advanceChildToEnd();
			quotedString_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		returnAST = quotedString_AST;
	}
	
	public final void nonQuotedString() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST nonQuotedString_AST = null;
		
			
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case STRING_LITERAL:
			{
				AST tmp23_AST = null;
				tmp23_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp23_AST);
				match(STRING_LITERAL);
				break;
			}
			case LPAREN:
			{
				AST tmp24_AST = null;
				tmp24_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp24_AST);
				match(LPAREN);
				break;
			}
			case RPAREN:
			{
				AST tmp25_AST = null;
				tmp25_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp25_AST);
				match(RPAREN);
				break;
			}
			case CTRL:
			{
				AST tmp26_AST = null;
				tmp26_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp26_AST);
				match(CTRL);
				break;
			}
			case UNDER:
			{
				AST tmp27_AST = null;
				tmp27_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp27_AST);
				match(UNDER);
				break;
			}
			case COMMA:
			{
				AST tmp28_AST = null;
				tmp28_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp28_AST);
				match(COMMA);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			nonQuotedString_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_9);
		}
		returnAST = nonQuotedString_AST;
	}
	
	public final void paramText() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST paramText_AST = null;
		Token  s = null;
		AST s_AST = null;
		
		try {      // for error handling
			{
			s = LT(1);
			s_AST = astFactory.create(s);
			astFactory.addASTChild(currentAST, s_AST);
			match(STRING_LITERAL);
			}
			paramText_AST = (AST)currentAST.root;
			paramText_AST = (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(TEXTNODE,s.getText())));
			currentAST.root = paramText_AST;
			currentAST.child = paramText_AST!=null &&paramText_AST.getFirstChild()!=null ?
				paramText_AST.getFirstChild() : paramText_AST;
			currentAST.advanceChildToEnd();
			paramText_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = paramText_AST;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"EXPR",
		"TEXTNODE",
		"PARAMNODE",
		"COLUMNNODE",
		"TABLENODE",
		"FUNCNODE",
		"FUNCPARAM",
		"USERPROPNODE",
		"SUBQUERY",
		"LPARAM_IGNORE_AREA",
		"RPARAM_IGNORE_AREA",
		"NEW_LINE",
		"STRING_LITERAL",
		"LPAREN",
		"RPAREN",
		"UNDER",
		"COMMA",
		"DOUBLE_QUOTED",
		"LPARENG",
		"RPARENG",
		"LPARENF",
		"RPARENF",
		"CTRL",
		"ESC"
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
		long[] data = { 16386L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 134201346L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 134209538L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 67239936L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 262144L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 1310720L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 68485120L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 69140480L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 71237632L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	
	}
