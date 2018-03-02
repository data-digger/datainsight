package com.datadigger.datainsight.expression.ast;

import com.datadigger.datainsight.type.NodeType;

public class ParamIgnoreAreaNode extends Node {

	@Override
	public String getExpressionText() {
		return getText();
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.PARAM_IGNORE_AREA;
	}
}
