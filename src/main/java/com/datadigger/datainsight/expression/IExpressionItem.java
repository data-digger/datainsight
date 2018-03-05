//$Revision: 1.2 $
package com.datadigger.datainsight.expression;

import com.datadigger.datainsight.query.SQLPart;

public interface IExpressionItem {
	public SQLPart getSQLPart();	
	public String toString();
}
