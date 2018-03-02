package com.datadigger.datainsight.expression;

import java.io.Serializable;

import com.datadigger.datainsight.expression.ast.TextNode;
import com.datadigger.datainsight.query.SQLPart;
import com.datadigger.datainsight.type.SQLPartType;



public class TextExpressionItem implements IExpressionItem,Serializable {

	TextNode node;
	public String toString(){
		return getSQLPart().getSqlStr();
	}

	public SQLPart getSQLPart() {
		SQLPart sqlPart = new SQLPart(SQLPartType.SQLSTR, node.getText());
		return sqlPart;
	}
	
	public TextExpressionItem(){
		
	}
	
	public TextExpressionItem(TextNode node){
		this.node = node;
	}
	
	public TextExpressionItem(String txt){
		TextNode tmpNode = new TextNode();
		tmpNode.setText(txt);
		this.node = tmpNode;
	}
		
	public TextNode getTextNode(){
		return node;
	}

}
