package com.datadigger.datainsight.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.datadigger.datainsight.exception.DataDiggerErrorCode;
import com.datadigger.datainsight.exception.DataDiggerException;



public class JdbcUtil {

	private static final Logger log = Logger.getLogger(JdbcUtil.class);

	public static PreparedStatement prepareStatement(Connection dbConn, String sql) {
		log.debug("Exec SQL:" + sql);
		PreparedStatement dbPrep;
		try {
			dbPrep = dbConn.prepareStatement(sql);
		} catch (SQLException e) {
			throw new DataDiggerException(DataDiggerErrorCode.SQL_ERROR, e).setDetail(sql+";"+e.getMessage());
		}
		return dbPrep;
	}

	public static PreparedStatement prepareStatement(Connection dbConn, String sql,
			int resultSetType, int resultSetConcurrency) throws SQLException {
		PreparedStatement dbPrep;
		dbPrep = dbConn.prepareStatement(sql, resultSetType,
				resultSetConcurrency);
		return dbPrep;
	}
}
