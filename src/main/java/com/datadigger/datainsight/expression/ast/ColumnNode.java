package com.datadigger.datainsight.expression.ast;

import com.datadigger.datainsight.type.NodeType;

public class ColumnNode extends Node {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7663990847841383464L;

	public String getColumnId(){
		return getText();
	}

	@Override
	public String getExpressionText() {
		return "^C_" + getColumnId() + "^";
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.COLUMN;
	}
}
