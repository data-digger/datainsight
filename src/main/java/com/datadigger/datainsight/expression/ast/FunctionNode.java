package com.datadigger.datainsight.expression.ast;

import com.datadigger.datainsight.type.NodeType;

public class FunctionNode extends Node {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7665678901709452251L;

	@Override
	public String getExpressionText() {
		StringBuilder result = new StringBuilder();
		result.append("^F_").append(getText()).append("(");
		Node child = this.getFirstChild();
		while(child != null) {
			result.append(child.getExpressionText()).append(',');
			child = child.getNextSibling();
		}
		if(result.charAt(result.length() - 1) == ',')
			result.setLength(result.length() - 1);
		result.append(")^");
		return result.toString();
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.FUNCTION;
	}
	
	public boolean clearEmptyParam() {
		Node child = this.getFirstChild();
		Node prev = null;
		while(child != null) {
			if(child.clearEmptyParam()) {
				if(prev == null)
					this.setFirstChild(child.getNextSibling());
				else
					prev.setNextSibling(child.getNextSibling());
			} else
				prev = child;
			child = child.getNextSibling();
		}
		return false;
	}
}
