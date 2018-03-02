package com.datadigger.datainsight.query.format.sql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlText {

	private String inputText;

	public SqlText(String sqlText) {
		inputText = sqlText;
	}

	public String format() {
		if (inputText == null)
			return null;

		String[] sqls = inputText.split(";");
		StringBuilder buf = new StringBuilder();
		int count = sqls.length;
		for (int i = 0; i < count; i++) {
//			buf.append(new org.hibernate.pretty.Formatter(sqls[i]).format());
			buf.append(sqls[i]);
			if (i < count - 1)
				buf.append(";");
		}
		String result = buf.toString();
		// ϵͳ�������⴦����^function()^�ṹ,�Ѻ����еĻس����滻��
		result = getAdjustedFormatedSqlString(result);

		return result;
	}

	// ��ȡ�ַ������ض��ַ��ĸ���
	private int getCharacterNumber(String str, String regex) {
		int count = 0;
		Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(str);
		while (matcher.find())
			count++;
		return count;
	}

	// ϵͳ�������⴦����^function()^�ṹ,�Ѻ����еĻس����滻��
	private String getAdjustedFormatedSqlString(String formatedSql) {
		String[] arr = formatedSql.split("\n");
		StringBuilder result = new StringBuilder();
		StringBuilder tempBuf = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (tempBuf.length() == 0) {
				int index = arr[i].indexOf("^F");
				if (index != -1) {
					int bracket_l = getCharacterNumber(arr[i], "\\(");
					if (bracket_l != 0) {
						int bracket_r = getCharacterNumber(arr[i], "\\)");
						if (bracket_l != bracket_r) {
							tempBuf = new StringBuilder(arr[i]);
							continue;
						}
					}
				}
				result.append(arr[i]).append("\n");
			} else {
				tempBuf.append(" ").append(arr[i].trim());
				int bracket_l = getCharacterNumber(tempBuf.toString(), "\\(");
				int bracket_r = getCharacterNumber(tempBuf.toString(), "\\)");
				if (bracket_l != bracket_r && i < arr.length - 1) {
					continue;
				} else {
					result.append(tempBuf).append("\n");
					tempBuf = new StringBuilder();
				}
			}
		}
		return result.toString();
	}

}