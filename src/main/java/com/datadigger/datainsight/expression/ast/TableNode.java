package com.datadigger.datainsight.expression.ast;

import com.datadigger.datainsight.type.NodeType;

public class TableNode extends Node {
	private static final long serialVersionUID = 1L;

	@Override
	public String getExpressionText() {
		return "^T_" + getTableId() + "^";
	}

	public String getTableId() {
		return getText();
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.TABLE;
	}

}
