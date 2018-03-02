package com.datadigger.datainsight.query;

import com.datadigger.datainsight.type.SQLPartType;

public class SQLPart {
	private SQLPartType type;

	private String sqlStr;
	private String prepareSqlStr = "";
	private String cleanupSqlStr = "";
	
	public SQLPart(SQLPartType type, String sqlStr) {
		if (type==null)
			type = SQLPartType.SQLUnknown;
		setType(type);
		setSqlStr(sqlStr);
	}

	public String getSqlStr() {
		return sqlStr;
	}

	public void setSqlStr(String sqlStr) {
		this.sqlStr = sqlStr;
	}

	public SQLPartType getType() {
		return type;
	}

	public void setType(SQLPartType type) {
		this.type = type;
	}

	public String getPrepareSqlStr() {
		return prepareSqlStr;
	}

	public void setPrepareSqlStr(String prepareSqlStr) {
		this.prepareSqlStr = prepareSqlStr;
	}

	public String getCleanupSqlStr() {
		return cleanupSqlStr;
	}

	public void setCleanupSqlStr(String cleanupSqlStr) {
		this.cleanupSqlStr = cleanupSqlStr;
	}
	
	public static enum Stage {
		Prepare, DoJob, Cleanup
	}
}
