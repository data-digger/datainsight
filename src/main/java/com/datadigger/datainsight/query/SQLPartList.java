package com.datadigger.datainsight.query;

import java.util.ArrayList;

import com.datadigger.datainsight.query.format.sql.SqlText;
import com.datadigger.datainsight.type.SQLPartType;



public class SQLPartList extends ArrayList<SQLPart>{
	private SQLPartType type;
	
	public SQLPartList(SQLPartType type){
		super();
		setType(type);
	}
	
	public SQLPartList(){
		super();
		type = SQLPartType.SQLUnknown;
	}
	
	public SQLPartType getType() {
		return type;
	}

	public void setType(SQLPartType type) {
		this.type = type;
	}
	
	public String getSqlStr() {
		return getSqlStr(false, SQLPart.Stage.DoJob);
	}

	public String getSqlStr(boolean doFormat) {
		return getSqlStr(doFormat, SQLPart.Stage.DoJob);
	}

	public String getPrepareSqlStr() {
		return getSqlStr(false, SQLPart.Stage.Prepare);
	}

	public String getCleanupSqlStr() {
		return getSqlStr(false, SQLPart.Stage.Cleanup);
	}

	public String getSqlStr(boolean needFormat, SQLPart.Stage stage) {
		StringBuffer rtn = new StringBuffer();
		for (SQLPart sp : this) {
			if (sp == null) {
				continue;
			}
			if (stage.equals(SQLPart.Stage.Prepare)) {
				rtn.append(sp.getPrepareSqlStr());
			} else if (stage.equals(SQLPart.Stage.Cleanup)) {
				rtn.append(sp.getCleanupSqlStr());
			} else {
				rtn.append(sp.getSqlStr());
			}
		}
		String ret = rtn.toString();
		if (needFormat)
			ret = new SqlText(ret).format();
		return ret;
	}
	
}
