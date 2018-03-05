package com.datadigger.datainsight.expression.ast;

import com.datadigger.datainsight.query.SQLPart;
import com.datadigger.datainsight.type.NodeType;

public class ExpressionNode extends Node {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3970829098302016999L;

	public java.util.List<SQLPart> getSqlPartList(){
		return null;
	}

	@Override
	public String getExpressionText() {
		StringBuilder result = new StringBuilder();
		Node child = this.getFirstChild();
		while(child != null) {
			result.append(child.getExpressionText());
			child = child.getNextSibling();
		}
		return result.toString();
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.EXPRESSION;
	}
}
