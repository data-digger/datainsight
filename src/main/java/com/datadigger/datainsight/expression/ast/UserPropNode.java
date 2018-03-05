package com.datadigger.datainsight.expression.ast;

import com.datadigger.datainsight.type.NodeType;

public class UserPropNode extends Node {
	private static final long serialVersionUID = 1L;

	@Override
	public String getExpressionText() {
		return "^A_" + getPropName() + "^";
	}
	
	public String getPropName() {
		return getText();
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.USER_PROP;
	}

}
