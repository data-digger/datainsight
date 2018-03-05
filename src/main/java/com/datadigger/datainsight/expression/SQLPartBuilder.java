package com.datadigger.datainsight.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.datadigger.datainsight.param.DateValue;
import com.datadigger.datainsight.param.NumValue;
import com.datadigger.datainsight.param.ParamValue;
import com.datadigger.datainsight.param.StrValue;
import com.datadigger.datainsight.query.SQLPart;
import com.datadigger.datainsight.type.SQLPartType;
import com.datadigger.datainsight.util.ValueType;




public class SQLPartBuilder {
	public static SQLPart getSQLPart(ParamValue paramValue) {
		if (paramValue instanceof DateValue)
			return getDateSQLPart((DateValue) paramValue);
		//else if (paramValue instanceof MultiValue)
		//	return getMultiSQLPart((MultiValue) paramValue);
		else if (paramValue instanceof NumValue)
			return getNumSQLPart((NumValue)paramValue);
		else if(paramValue instanceof StrValue)
			return getStrSQLPart((StrValue)paramValue);
		return null;
	}

	private static SQLPart getStrSQLPart(StrValue paramValue) {
		return new SQLPart(SQLPartType.SQLSTR, getMappingDateTime(paramValue.getValue()));
	}

	private static SQLPart getNumSQLPart(NumValue paramValue) {
		if (paramValue.getValueType() == ValueType.INTEGER)
			return new SQLPart(SQLPartType.SQLNUM, Integer.toString((int) paramValue.getNumber()));
		else if (paramValue.getValueType() == ValueType.DOUBLE)
			return new SQLPart(SQLPartType.SQLNUM, Double.toString(paramValue.getNumber()));
		else
			return null;
	}

//	private static SQLPart getMultiSQLPart(MultiValue paramValue) {
//		if (paramValue.getValueType() == ValueType.INTEGER)
//			return new SQLPart(SQLPartType.SQLSTR, paramValue.getValue());
//		else
//			return new SQLPart(SQLPartType.SQLSTR, "'" + StringUtil.replace(paramValue.getValue(), ",", "','")
//					+ "'");
//	}

	private static String getMappingDateTime(String dtStr) {
		String result = dtStr;
		if (result != null) {
			Matcher matcher = Pattern.compile("^\\d{2}:\\d{2}$|\\s\\d{2}:\\d{2}$").matcher(dtStr);
			if (matcher.find())
				result = result + ":00";
		}

		return result;
	};

	private static SQLPart getDateSQLPart(DateValue paramValue) {
		String sql = null;
		switch (paramValue.getDBType()) {
			case ORACLE:
			case ORACLE_OCI:
				sql = "to_date('" + paramValue.getValue() + "', 'yyyy-MM-dd HH24:MI')";
				break;
			case DB2:
			case DB2_400:
			case DB2_V9:
				sql = '\'' + getMappingDateTime(paramValue.getValue()) + '\'';
				break;
			case ACCESS:
				sql = "#" + paramValue.getValue() + "#";
				break;
			case TERADATA:
			case TERADATA_V12:
				switch (paramValue.getValueType()) {
					case DATE:
						sql = "cast('" + paramValue.getValue() + "' as date format 'YYYY-MM-DD')";
						break;
					case DATETIME:
						sql = "cast('" + paramValue.getValue()
								+ "' as timestamp format 'YYYY-MM-DDBHH:MI')";
						break;
					case TIME:
						sql = "cast('" + paramValue.getValue() + "' as time format 'HH:MI')";
						break;
				}
				break;
			default:
				sql = '\'' + paramValue.getValue() + '\'';
				break;
		}
		return new SQLPart(SQLPartType.SQLSTR, sql);
	}
}
