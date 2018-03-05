package com.datadigger.datainsight.expression.ast;

import com.datadigger.datainsight.type.NodeType;

import antlr.CommonAST;

public abstract class  Node extends CommonAST {
	@Override
	public Node getNextSibling() {
		// TODO Auto-generated method stub
		return (Node) super.getNextSibling();
	}

	@Override
	public Node getFirstChild() {
		return (Node) super.getFirstChild();
	}

	public abstract String getExpressionText();
	public abstract NodeType getNodeType();
	
	public Node getChild(int index){
		Node rtn = getFirstChild();
		if(index==0){
			return rtn;
		}
		
		for(int i=0;i<index;i++){
			if (rtn!=null)
				rtn = rtn.getNextSibling();
		}
		return rtn;
	}

	public boolean clearEmptyParam() {
		Node child = this.getFirstChild();
		while(child != null) {
			child.clearEmptyParam();
			child = child.getNextSibling();
		}
		return false;
	}
}
