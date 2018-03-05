package com.datadigger.datainsight.expression;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;



public class TextQueryColumnParser {
	private static final Logger log = Logger.getLogger(TextQueryColumnParser.class);
	
	public static List<String[]> parseExpression(String expr) {
		List<String[]> result = new ArrayList<String[]>();
		PushbackReader reader = new PushbackReader(new StringReader(expr), 1024);
		try {
			skipSpace(reader);
			skipWord(reader, "select");
			skipSpace(reader);
			String token = null;
			List<String> tokenList = new ArrayList<String>();
			while((token = nextToken(reader)) != null) {
				if(token.equals(" ") || token.equals("\r") || token.equals("\n") || token.equals("\t"))
					continue;
				if(token.equals(",") && bracketMatch(tokenList)) {
					String[] columnName = getColumnName(tokenList);
					if(columnName != null)
						result.add(columnName);
					tokenList.clear();
				}
				if(token.equalsIgnoreCase("from") && bracketMatch(tokenList)) {
					String[] columnName = getColumnName(tokenList);
					if(columnName != null)
						result.add(columnName);
					if(result.size() == 0)
						result.add(new String[]{"", ""});
					return result;
				}
				tokenList.add(token);
			}
			if(tokenList.size() > 0) {
				String[] columnName = getColumnName(tokenList);
				if(columnName != null)
					result.add(columnName);
			}
			if(result.size() == 0)
				result.add(new String[]{"", ""});
			return result;
		} catch (IOException e) {
			log.warn("Error in parse expression", e);
			return null;
		}
	}

	private static boolean bracketMatch(List<String> tokenList) {
		int count = 0;
		for(String str : tokenList) {
			int index = str.indexOf('(');
			while(index >= 0) {
				count++;
				index = str.indexOf('(', index + 1);
			}
			
			index = str.indexOf(')');
			while(index >= 0) {
				count--;
				index = str.indexOf(')', index + 1);
			}
		}
		return count == 0;
	}

	private static String[] getColumnName(List<String> tokenList) {
		if(tokenList.size() == 0)
			return null;
		String colName = tokenList.get(tokenList.size() - 1);
		if(colName.charAt(0) == '^' && colName.charAt(colName.length() - 1) == '^') {
			String id = colName.substring("^?_".length(), colName.length() - 1);
//			IField field = FreeQueryDAOFactory.getCommonDAO().searchAbstractFieldBO(id);
//			if(field != null) {
//				String alias = StringUtil.isNullOrEmpty(field.getAlias()) ? field.getName() : field.getAlias(); 
//				return new String[] {field.getName(), alias};
//			} else
				return new String[]{colName, colName};
		} else
			return new String[]{colName, colName};
	}

	private static String nextToken(PushbackReader reader) throws IOException {
		int ch = reader.read();
		if(ch < 0)
			return null;
		switch(ch) {
			case ' ':
			case ',':
			case '\r':
			case '\n':
			case '\t':
			case '(':
			case ')':
				return "" + (char)ch;
		}
		reader.unread(ch);
		StringBuilder buf = new StringBuilder();
		while((ch = reader.read()) >= 0) {
			buf.append((char)ch);
			if(ch == '\'' || ch == '"') {
				int quot = -1;
				while((quot = reader.read()) >= 0) {
					buf.append((char)quot);
					if(quot == ch)
						return buf.toString();
				}
				return buf.toString();
			}
			switch(ch) {
				case ' ':
				case ',':
				case '\r':
				case '\n':
				case '\t':
				case '(':
				case ')':
					reader.unread(ch);
					buf.setLength(buf.length() - 1);
					return buf.toString();
			}
		}
		return buf.toString();
	}

	private static boolean skipWord(PushbackReader reader, String word) throws IOException {
		char[] buf = new char[word.length()];
		if(reader.read(buf) < 0)
			return false;
		if(new String(buf).equalsIgnoreCase(word))
			return true;
		else
			reader.unread(buf);
		return false;
	}

	private static void skipSpace(PushbackReader reader) throws IOException {
		int ch = -1;
		while((ch = reader.read()) == ' ' && ch >= 0);
		if(ch >= 0)
			reader.unread(ch);
	}
}
