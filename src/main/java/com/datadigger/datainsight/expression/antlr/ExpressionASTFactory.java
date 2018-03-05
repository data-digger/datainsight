package com.datadigger.datainsight.expression.antlr;

import com.datadigger.datainsight.expression.ast.*;

import antlr.ASTFactory;
import antlr.CommonAST;
import static com.datadigger.datainsight.expression.antlr.ExpressionParserTokenTypes.*;

public class ExpressionASTFactory extends ASTFactory {

	@Override
	public Class getASTNodeType(int tokenType) {
		switch(tokenType) {
			case EXPR:
				return ExpressionNode.class;
			case TEXTNODE:
				return TextNode.class;
			case PARAMNODE:
				return ParamNode.class;
			case FUNCNODE:
				return FunctionNode.class;
			case COLUMNNODE:
				return ColumnNode.class;
			case FUNCPARAM:
				return FunctionParamNode.class;
			case TABLENODE:
				return TableNode.class;
			case USERPROPNODE:
				return UserPropNode.class;
			case SUBQUERY:
				return SubQueryNode.class;
			case LPARAM_IGNORE_AREA:
				return ParamIgnoreAreaNode.class;
			default:					
				return CommonAST.class;
		}
	}

}
