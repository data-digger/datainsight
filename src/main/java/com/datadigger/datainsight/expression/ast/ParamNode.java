package com.datadigger.datainsight.expression.ast;

import com.datadigger.datainsight.type.NodeType;

public class ParamNode extends Node {

	private boolean ignorable;
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 832658819578686965L;

	public String getParamId(){
		return getText();
	}

	@Override
	public String getExpressionText() {
		return "^P_" + getParamId() + "^";
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.PARAM;
	}

	public void setIgnorable(boolean ignorable) {
		this.ignorable = ignorable;
	}
	
	public boolean isIgnorable() {
		return this.ignorable;
	}
}
