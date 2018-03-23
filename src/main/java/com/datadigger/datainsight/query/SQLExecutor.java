package com.datadigger.datainsight.query;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.datadigger.datainsight.bean.CellData;
import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.domain.DataSource;
import com.datadigger.datainsight.exception.DataDiggerErrorCode;
import com.datadigger.datainsight.exception.DataDiggerException;
import com.datadigger.datainsight.repository.DataSourceRepository;
import com.datadigger.datainsight.util.ConnectionPool;
import com.datadigger.datainsight.util.JdbcUtil;
import com.datadigger.datainsight.util.StrUtil;
import com.datadigger.datainsight.util.ValueType;

public class SQLExecutor {
	private static final Logger log = Logger.getLogger(SQLExecutor.class);
	
	public SQLExecutor() {

	}

	public static GridData execute(DataSource ds, String sql, List<Object> paramList) {
		return execute(ds, sql, Integer.MAX_VALUE, true , paramList);
	}
	
	public static GridData execute(DataSource ds, String sql) {
		return execute(ds, sql, Integer.MAX_VALUE, true , null);
	}

	public static GridData execute(DataSource ds, String sql, boolean format) {
		return execute(ds, sql, Integer.MAX_VALUE, format, null);
	}
	
	public static GridData execute(DataSource ds, String sql, int maxRows) {
		return execute(ds, sql, maxRows, true,null);
	}
	
	
	public static GridData execute(DataSource ds, String sql, int maxRows, boolean format,List<Object> paramList) {
		GridData result = new GridData();

		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		String dbType = ds.getDriverType();
		String charset = ds.getDbCharset();
		try{
			conn = ConnectionPool.getInstance().getConnection(ds);
			
			prep = JdbcUtil.prepareStatement(conn,sql);
			if(paramList != null) {
				for(int i = 0; i < paramList.size(); i++) {
					Object o = paramList.get(i);
					if (o instanceof Integer) {
						prep.setInt(i+1, ((Integer) o).intValue());
					} else if (o instanceof Double ) {
						prep.setDouble(i+1, ((Double) o).doubleValue());
					} else if (o instanceof String ) {
						prep.setString(i+1, (String)o);
					} else if (o instanceof Date ) {
						prep.setDate(i+1, (Date)o);
					}
				}
			}
			rs = prep.executeQuery();
		
			ResultSetMetaData meta = rs.getMetaData();
			List<String> columnsName = new ArrayList<String>();
			for(int i = 1; i <= meta.getColumnCount(); i++) {
				String cName = meta.getColumnLabel(i)!= null ?meta.getColumnLabel(i):meta.getColumnName(i);
				columnsName.add(cName);
			}
			result.setStringHeaders(columnsName);
			List<List<CellData>> data = new ArrayList<List<CellData>>();

			SimpleDateFormat dateFormat = null;
			SimpleDateFormat timeFormat = null;
			SimpleDateFormat datetimeFormat = null;
			DecimalFormat integerFormat = null;
			DecimalFormat decimalFormat = null;
			
			if(format) {
				dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				timeFormat = new SimpleDateFormat("HH:mm:ss");
				datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				integerFormat = new DecimalFormat("#,##0");
				decimalFormat = new DecimalFormat("#,##0.00");
			}

			int rowCount = 0;
			while(rs.next() && ++rowCount <= maxRows) {
				List<CellData> row = new ArrayList<CellData>(meta.getColumnCount());
				data.add(row);
				for(int i = 1; i <= meta.getColumnCount(); i++) {
					CellData cell = new CellData();

					switch (meta.getColumnType(i)) {
						case java.sql.Types.BIT:
						case java.sql.Types.BIGINT:
						case java.sql.Types.TINYINT:
						case java.sql.Types.SMALLINT:
						case java.sql.Types.INTEGER:
							cell.setIntValue(rs.getInt(i));
							if(rs.wasNull())
								cell.setNull(true);
							if(format && !rs.wasNull())
								cell.setDisplayValue(integerFormat.format(cell.getIntValue()));
							break;

						case java.sql.Types.FLOAT:
						case java.sql.Types.DOUBLE:
						case java.sql.Types.REAL:
							cell.setDoubleValue(rs.getDouble(i));
							if(rs.wasNull())
								cell.setNull(true);
							if(format && !rs.wasNull())
								cell.setDisplayValue(decimalFormat.format(cell.getDoubleValue()));
							break;
						case java.sql.Types.DECIMAL:
						case java.sql.Types.NUMERIC:
							if(meta.getScale(i) == 0 && meta.getPrecision(i) < 10) {//û�г������η�Χ
								cell.setIntValue(rs.getInt(i));
								if(rs.wasNull())
									cell.setNull(true);
								if(format && !rs.wasNull())
									cell.setDisplayValue(integerFormat.format(cell.getIntValue()));
								break;
							} else {
								cell.setDoubleValue(rs.getDouble(i));
								if(rs.wasNull())
									cell.setNull(true);
								if(format && !rs.wasNull())
									cell.setDisplayValue(decimalFormat.format(cell.getDoubleValue()));
								break;
							}

						case java.sql.Types.CHAR:
						case java.sql.Types.LONGVARCHAR:
						case java.sql.Types.VARCHAR:
							String oldValue = rs.getString(i);
							if(rs.wasNull())
								cell.setNull(true);
							String newValue = StrUtil.changeCharsetFrom(oldValue, charset);
							cell.setStringValue(newValue);
			
							if(format)
								cell.setDisplayValue(cell.getStringValue());
							break;

						case java.sql.Types.DATE:
							cell.setDateValue(ValueType.DATE, rs.getDate(i));
							if(rs.wasNull())
								cell.setNull(true);
							if(format && cell.getDateValue() != null)
								cell.setDisplayValue(dateFormat.format(cell.getDateValue()));
							break;
						case java.sql.Types.TIME:
							cell.setDateValue(ValueType.TIME, rs.getTime(i));
							if(rs.wasNull())
								cell.setNull(true);
							if(format && cell.getDateValue() != null)
								cell.setDisplayValue(timeFormat.format(cell.getDateValue()));
							break;
						case java.sql.Types.TIMESTAMP:
							cell.setDateValue(ValueType.DATETIME, rs.getTimestamp(i));
							if(rs.wasNull())
								cell.setNull(true);
							if(format && cell.getDateValue() != null)
								cell.setDisplayValue(datetimeFormat.format(cell.getDateValue()));
							break;
						default:
							cell.setStringValue("");
							cell.setDisplayValue("");
							cell.setType(ValueType.UNKNOWN);
					}
					row.add(cell);
				}
			}
			result.setData(data);
			return result;
		} catch(Exception e){
			throw new DataDiggerException(DataDiggerErrorCode.SQL_ERROR, e).setDetail(sql);
		} finally {
			try {
				closeDBObject(rs, prep, null);
			} finally {
				closeDBObject(null, null, conn);
			}
		}
	}
	
	protected static void closeDBObject(ResultSet rs, Statement stat, Connection dbConn) {
		try {
			if(rs != null)
				rs.close();
		} catch (SQLException e) {
		}
		try {
			if(stat != null)
				stat.close();
		} catch (SQLException e) {
		}
		try {
			if(dbConn != null)
				dbConn.close();
		} catch (SQLException e) {
		}
	}
	
	private static GridData getResultSet(ResultSet rs, int maxRows, boolean format) throws SQLException {
		GridData result = new GridData();
		ResultSetMetaData meta = rs.getMetaData();
		List<String> columnsName = new ArrayList<String>();
		int columnCount = meta.getColumnCount();
		for(int i = 1; i <= columnCount; i++) {
			columnsName.add(meta.getColumnName(i));
		}
		result.setStringHeaders(columnsName);
		List<List<CellData>> data = new ArrayList<List<CellData>>();

		SimpleDateFormat dateFormat = null;
		SimpleDateFormat timeFormat = null;
		SimpleDateFormat datetimeFormat = null;
		DecimalFormat integerFormat = null;
		DecimalFormat decimalFormat = null;
		
		if(format) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			timeFormat = new SimpleDateFormat("HH:mm:ss");
			datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			integerFormat = new DecimalFormat("#,##0");
			decimalFormat = new DecimalFormat("#,##0.00");
		}
		int rowCount = 0;
		while(rs.next() && ++rowCount <= maxRows) {
			List<CellData> row = new ArrayList<CellData>(columnCount);
			data.add(row);
			for(int i = 1; i <= columnCount; i++) {
				CellData cell = new CellData();

				switch (meta.getColumnType(i)) {
					case java.sql.Types.BIT:
					case java.sql.Types.BIGINT:
					case java.sql.Types.TINYINT:
					case java.sql.Types.SMALLINT:
					case java.sql.Types.INTEGER:
						cell.setIntValue(rs.getInt(i));
						if(rs.wasNull())
							cell.setNull(true);
						if(format && !rs.wasNull())
							cell.setDisplayValue(integerFormat.format(cell.getIntValue()));
						break;

					case java.sql.Types.FLOAT:
					case java.sql.Types.DOUBLE:
					case java.sql.Types.REAL:
						cell.setDoubleValue(rs.getDouble(i));
						if (rs.wasNull()){
							cell.setDoubleValue(Double.NaN);
							cell.setNull(true);
						}
						if(format && !rs.wasNull())
							cell.setDisplayValue(decimalFormat.format(cell.getDoubleValue()));
						break;
					case java.sql.Types.DECIMAL:
					case java.sql.Types.NUMERIC:
						//ORACLE�������⣬�����DECIMAL���ͣ�getScale���Ƿ���0���ᱻ�жϳ�����
//						if(meta.getScale(i) == 0 && meta.getPrecision(i) < 10) {//û�г������η�Χ
//							cell.setIntValue(rs.getInt(i));
//							if(format && !rs.wasNull())
//								cell.setDisplayValue(integerFormat.format(cell.getIntValue()));
//							break;
//						} else {
							cell.setDoubleValue(rs.getDouble(i));
							if (rs.wasNull()) {
								cell.setDoubleValue(Double.NaN);
								cell.setNull(true);
							}
							if(format && !rs.wasNull())
								cell.setDisplayValue(decimalFormat.format(cell.getDoubleValue()));
							break;
//						}
					case java.sql.Types.CHAR:
					case java.sql.Types.LONGVARCHAR:
					case java.sql.Types.VARCHAR:
						cell.setStringValue(rs.getString(i));
						if(rs.wasNull())
							cell.setNull(true);
						if(format)
							cell.setDisplayValue(cell.getStringValue());
						break;

					case java.sql.Types.DATE:
						cell.setDateValue(ValueType.DATE, rs.getDate(i));
						if(rs.wasNull())
							cell.setNull(true);
						if(format && cell.getDateValue() != null)
							cell.setDisplayValue(dateFormat.format(cell.getDateValue()));
						break;
					case java.sql.Types.TIME:
						cell.setDateValue(ValueType.TIME, rs.getTime(i));
						if(rs.wasNull())
							cell.setNull(true);
						if(format && cell.getDateValue() != null)
							cell.setDisplayValue(timeFormat.format(cell.getDateValue()));
						break;
					case java.sql.Types.TIMESTAMP:
						cell.setDateValue(ValueType.DATETIME, rs.getTimestamp(i));
						if(rs.wasNull())
							cell.setNull(true);
						if(format && cell.getDateValue() != null)
							cell.setDisplayValue(datetimeFormat.format(cell.getDateValue()));
						break;

					default:
						cell.setStringValue("");
						cell.setDisplayValue("");
						cell.setType(ValueType.UNKNOWN);
				}
				row.add(cell);
			}
		}
		result.setData(data);
		return result;
	}
	
	public static GridData executePrepareStatement(DataSource ds, String sql, Object... params) {
		return executePrepareStatement(ds, true, sql, params);
	}

	public static GridData executePrepareStatement(DataSource ds, boolean format, String sql, Object... params) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try{
			if(log.isDebugEnabled()){
				StringBuffer strSQLInfo = new StringBuffer();
				strSQLInfo.append("SQL:").append(sql).append("; SQLParams:[");
				for(int i=0;i<params.length;i++){
					strSQLInfo.append(params[i]);
					if (i==params.length-1)
						strSQLInfo.append("]");
					else
						strSQLInfo.append(",");
				}
				
				log.debug(strSQLInfo.toString());
			}
			conn = ConnectionPool.getInstance().getConnection(ds);
			prep = JdbcUtil.prepareStatement(conn,sql);
			for(int i = 0; i < params.length; i++) {
				Object param = params[i];
				if(param == null) {
					prep.setNull(i + 1, Types.VARCHAR);
				} else if(param instanceof String) {
					prep.setString(i + 1, param.toString());
				} else if(param instanceof Double) {
					prep.setDouble(i + 1, ((Double)param).doubleValue());
				} else if(param instanceof Integer) {
					prep.setInt(i + 1, ((Integer)param).intValue());
				} else if(param instanceof java.util.Date) {
					if(param instanceof Date)
						prep.setDate(i + 1, (Date)param);
					else if(param instanceof Time)
						prep.setTime(i + 1, (Time)param);
					else if(param instanceof Timestamp)
						prep.setTimestamp(i + 1, (Timestamp)param);
					else
						prep.setTimestamp(i + 1, new Timestamp(((java.util.Date)param).getTime()));
				}
			}
			rs = prep.executeQuery();
			return getResultSet(rs, Integer.MAX_VALUE, format);
		} catch(Exception e){
			throw new DataDiggerException(DataDiggerErrorCode.SQL_ERROR, e).setDetail(sql);
		} finally {
			closeDBObject(rs, prep, conn);
		}
	}

	public static int executeUpdatePrepareStatement(DataSource ds, String sql, Object... params) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try{
			if(log.isDebugEnabled()){
				StringBuffer strSQLInfo = new StringBuffer();
				strSQLInfo.append("SQL:").append(sql).append("; SQLParams:[");
				for(int i=0;i<params.length;i++){
					strSQLInfo.append(params[i]);
					if (i==params.length-1)
						strSQLInfo.append("]");
					else
						strSQLInfo.append(",");
				}
				
				log.debug(strSQLInfo.toString());
			}
			conn = ConnectionPool.getInstance().getConnection(ds);
			prep = JdbcUtil.prepareStatement(conn,sql);
			for(int i = 0; i < params.length; i++) {
				Object param = params[i];
				if(param == null) {
					prep.setNull(i + 1, Types.VARCHAR);
				} else if(param instanceof String) {
					prep.setString(i + 1, param.toString());
				} else if(param instanceof Double) {
					if(Double.isInfinite((Double)param) || Double.isNaN((Double)param))
						prep.setNull(i+1, Types.DOUBLE);
					else
						prep.setDouble(i + 1, ((Double)param).doubleValue());
				} else if(param instanceof Integer) {
					prep.setInt(i + 1, ((Integer)param).intValue());
				} else if(param instanceof java.util.Date) {
					if(param instanceof Date)
						prep.setDate(i + 1, (Date)param);
					else if(param instanceof Time)
						prep.setTime(i + 1, (Time)param);
					else if(param instanceof Timestamp)
						prep.setTimestamp(i + 1, (Timestamp)param);
					else
						prep.setTimestamp(i + 1, new Timestamp(((java.util.Date)param).getTime()));
				}
			}
			return prep.executeUpdate();
		} catch(Exception e){
			throw new DataDiggerException(DataDiggerErrorCode.SQL_ERROR, e).setDetail(sql);
		} finally {
			closeDBObject(rs, prep, conn);
		}
	}
	
	public static int executeUpdate(DataSource ds, String sql) {

		Connection conn = null;
		Statement stat = null;
		try{
			log.debug("SQL:" + sql);
			conn = ConnectionPool.getInstance().getConnection(ds);
			stat = conn.createStatement();
			return stat.executeUpdate(sql);
		} catch(Exception e){
			throw new DataDiggerException(DataDiggerErrorCode.SQL_ERROR, e).setDetail(sql);
		} finally {
			closeDBObject(null, stat, conn);
		}
	}
}
