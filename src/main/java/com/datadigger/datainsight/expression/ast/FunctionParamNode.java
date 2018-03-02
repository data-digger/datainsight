package com.datadigger.datainsight.expression.ast;

import java.util.ArrayList;
import java.util.List;

import com.datadigger.datainsight.type.NodeType;


public class FunctionParamNode extends Node {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8279573300434298104L;

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
		return NodeType.FUNCTION_PARAM;
	}

	public boolean clearEmptyParam() {
		Node node = this.getFirstChild();
		while(node != null && node instanceof TextNode) {
			node.setText(node.getText().trim());
			if(node.getText().length() != 0)
				break;
			node = node.getNextSibling();
		}
		if(node == null)
			return true;
		this.setFirstChild(node);
		List<Node> children = new ArrayList<Node>();
		node = node.getNextSibling();
		while(node != null) {
			children.add(node);
			node = node.getNextSibling();
		}
		int index = children.size() - 1;
		for(; index >= 0; index--) {
			node = children.remove(index);
			if(node instanceof TextNode) {
				node.setText(node.getText().trim());
				if(node.getText().length() != 0)
					break;
			} else
				break;
		}
		if(index < 0)
			return this.getFirstChild() == null;
		else
			node.setNextSibling(null);
		return false;
	}
}
