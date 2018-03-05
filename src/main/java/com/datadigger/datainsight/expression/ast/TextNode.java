package com.datadigger.datainsight.expression.ast;

import com.datadigger.datainsight.type.NodeType;

public class TextNode extends Node {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3724910103791582198L;

	@Override
	public String getExpressionText() {
		return getText();
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.TEXT;
	}
}
