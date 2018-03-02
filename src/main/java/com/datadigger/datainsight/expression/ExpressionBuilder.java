package com.datadigger.datainsight.expression;

import java.io.StringReader;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.datadigger.datainsight.exception.DataDiggerErrorCode;
import com.datadigger.datainsight.exception.DataDiggerException;
import com.datadigger.datainsight.expression.antlr.*;
import com.datadigger.datainsight.expression.ast.ExpressionNode;

public class ExpressionBuilder {
	private ExpressionBuilder() {
	}
	
	public static ExpressionNode parseExpression(String expr) {
        ExpressionLexer lexer = new ExpressionLexer(new StringReader(expr));
        ExpressionParser parser = new ExpressionParser(lexer);
        parser.setASTFactory(new ExpressionASTFactory());
        parser.setExpr(expr);
        try {
			parser.parse();
		} catch (RecognitionException e) {
			throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARSE_ERROR).setDetail(e.getLine() + ":" + e.getColumn());
		} catch (TokenStreamException e) {
			throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARSE_ERROR);
		}
		ExpressionNode result = (ExpressionNode) parser.getAST();
		result.clearEmptyParam();
		return result;
	}
}
