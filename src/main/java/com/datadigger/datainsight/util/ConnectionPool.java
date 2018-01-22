package com.datadigger.datainsight.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp.*;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import com.datadigger.datainsight.exception.DataDiggerErrorCode;
import com.datadigger.datainsight.exception.DataDiggerException;



public class ConnectionPool implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ConnectionPool.class);
	
	public static final String REP_POOLNAME = "res";

	
	private static ConnectionPool _instance = null;

	private static PoolingDriver driver = null; 

	static {
		try {
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
		} catch (ClassNotFoundException e) {
			log.error("org.apache.commons.dbcp.PoolingDriver not found");
		} catch (SQLException e) {
			log.error("org.apache.commons.dbcp.PoolingDriver " + e.getMessage());
		}
	}
	
	private ConnectionPool(){
	}
	
	public static ConnectionPool getInstance(){
		if (_instance == null) 
			_instance = new ConnectionPool();
		return _instance;
	}

	public static String getDebugInfo(){
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		try {
			String[] poolNames = driver.getPoolNames();
			for (int i = 0; i < poolNames.length; i++) {
				String tempPoolName = poolNames[i];
				buf.append("[name:").append(tempPoolName);
				buf.append("  Active:").append(driver.getConnectionPool(tempPoolName).getNumActive());
				buf.append("  Idle:").append(driver.getConnectionPool(tempPoolName).getNumIdle());
				buf.append("];");
			}
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		buf.append("}");
		return buf.toString();
	}
	
	public static void clearAll(){
		try {
			String[] poolNames = driver.getPoolNames();
			for (int i = 0; i < poolNames.length; i++) {
				driver.getConnectionPool(poolNames[i]).clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection driverConnect(String poolName) throws SQLException {
		
		if (!log.isTraceEnabled())
			return driver.connect("jdbc:apache:commons:dbcp:" + poolName, null);
		else{
			
			log.trace("Before getconnection():"+ConnectionPool.getDebugInfo());
			final Connection conn = driver.connect("jdbc:apache:commons:dbcp:" + poolName, null);
			log.trace("getconnection():"+conn.getClass()+":"+Integer.toHexString(System.identityHashCode(conn)));
			
			synchronized(_instance){	
				StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		        for (int i=0; i < trace.length; i++)
		        	log.trace("\tat " + trace[i]);
		        log.trace("After getconnection():"+ConnectionPool.getDebugInfo());
			}
			
			return (Connection)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Connection.class}, new InvocationHandler(){
	
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					
					if (method.getName().equals("close")){
						if (log.isTraceEnabled())
							log.trace("Before close():"+ConnectionPool.getDebugInfo());
						Object ret = method.invoke(conn, args);
						
						if (log.isTraceEnabled())
							log.trace("close():"+conn.getClass()+":"+Integer.toHexString(System.identityHashCode(conn)));
						
						if (log.isTraceEnabled()){
							synchronized(_instance){
								StackTraceElement[] trace = Thread.currentThread().getStackTrace();
					            for (int i=0; i < trace.length; i++)
					            	log.trace("\tat " + trace[i]);
					            log.trace("After close():"+ConnectionPool.getDebugInfo());
							}
						}
			            return ret;
					}
					else
						return method.invoke(conn, args);
				}});
			
		}
	}
	

	public Connection getConnection(IConnectionInfo ds) throws Exception {
		
		String poolName = getPoolNameFromDatasource(ds);
		try {
			driver.getConnectionPool(poolName);
			
			return driverConnect(poolName);
			
		} catch (SQLException sql) {
			if(ds.getUrl().startsWith("JNDI:")) {
				InitialContext cxt = new InitialContext();
				DataSource dataSource = (DataSource) cxt.lookup(ds.getUrl().substring("JNDI:".length()));
				return dataSource.getConnection();
			} else {
				createPool(ds);
				return driverConnect(poolName);
			}
		}
	}

	public void closePool(IConnectionInfo ds) throws SQLException {
		String poolName = getPoolNameFromDatasource(ds);
		driver.closePool(poolName);
	}

	public void closeAllPool() throws SQLException {

		String tempPoolName = null;

		String[] poolNames = driver.getPoolNames();
		for (int i = 0; i < poolNames.length; i++) {
			tempPoolName = poolNames[i];
			driver.closePool(tempPoolName);
		}
	}

	public void clearPool(IConnectionInfo ds) throws Exception {
		String poolName = getPoolNameFromDatasource(ds);
		driver.getConnectionPool(poolName).clear();
	}

	public void updatePool(IConnectionInfo ds) throws Exception {
		closePool(ds);
		createPool(ds);
	}

	private void createPool(IConnectionInfo ds) throws Exception {
		String poolName = getPoolNameFromDatasource(ds);

			Driver jdbcDriver = (Driver) Class.forName(ds.getDriver()).newInstance();
			GenericObjectPool connectionPool = new GenericObjectPool(null, ds.getMaxConnection() == 0 ? 1000 : ds.getMaxConnection());
			connectionPool.setMaxIdle(ds.getMaxConnection() == 0 ? 1000 : ds.getMaxConnection());
	
			Properties prop = new Properties();
			prop.setProperty("user", ds.getUser());
			prop.setProperty("password", ds.getPassword());
			final int transactionIsolation = ds.getTransactionIsolation();
			ConnectionFactory connectionFactory = new DriverConnectionFactory(
					jdbcDriver,
					ds.getUrl(),
					prop) {
				public Connection createConnection() throws SQLException {
					Connection conn = super.createConnection();
					if(transactionIsolation >= 0)
						conn.setTransactionIsolation(transactionIsolation);
					return conn;
				}
			};
	
			String validationQuery = ds.getValidationQuery();
			if (validationQuery != null && "".equals(validationQuery.trim()))
				validationQuery = null;
			connectionPool.setTestOnBorrow(validationQuery != null);
			PoolableConnectionFactory poolableConnectionFactory = 
			new PoolableConnectionFactory(
					connectionFactory, connectionPool, null, validationQuery, null, true, -1, null, null);
	
			driver.registerPool(poolName, connectionPool);
	}
	
	private String getPoolNameFromDatasource(IConnectionInfo ds) {
		return ds.getId().trim();

	}

}
