package com.datadigger.datainsight.expression.ast;

import com.datadigger.datainsight.type.NodeType;

public class SubQueryNode extends Node {
	private static final long serialVersionUID = 1L;

	public String getSubQueryId() {
		return getText();
	}
	
	@Override
	public String getExpressionText() {
		return "^S_" + getSubQueryId() + "^";
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.SUBQUERY;
	}

}
