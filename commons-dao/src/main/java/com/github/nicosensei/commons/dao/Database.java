/**
 * 
 */
package com.github.nicosensei.commons.dao;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.mchange.v2.c3p0.ComboPooledDataSource;


/**
 * This class handles connections to a database.
 *
 * The implementation relies on a connection pool. Once acquired through
 * the get() method, a connection must be explicitly returned to the pool
 * by calling the release(Connection) method.
 *
 * THis class is intended to be used statically, and hence cannot be
 * instantiated and is final.
 */
public class Database {

	private static final Logger log = LoggerFactory.getLogger(Database.class);

	/**
	 * The c3p0 pooled datasource backing this implementation.
	 */
	private ComboPooledDataSource dataSource = null;

	private DataSourceTransactionManager transactionManager;

	private List<TransactionStatus> transactions = new ArrayList<TransactionStatus>();

	/**
	 * Makes sure that the class can't be instantiated, as it is designed to be
	 * used statically.
	 */
	protected Database(
			final String driverClassName,
			final String jdbcUrl,
			final String dbUserName,
			final String dbUserPassword,
			final int minPoolSize,
			final int maxPoolSize,
			final int maxPoolStatements,
			final int maxPoolStatementsPerConn,
			final int poolAcquireIncrement,
			final int poolIdleConnTestingPeriod,
			final String poolTestQuery) throws DatabaseInitializationException {

		// Initialize the data source
		dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(driverClassName);
		} catch (PropertyVetoException e) {
			throw new DatabaseInitializationException(
					"Failed to set datasource JDBC driver class '"
							+ driverClassName + "'" + "\n",
							e);
		}
		
		dataSource.setJdbcUrl(jdbcUrl);
		if (!dbUserName.isEmpty()) {
			dataSource.setUser(dbUserName);
		}
		if (!dbUserPassword.isEmpty()) {
			dataSource.setPassword(dbUserPassword);
		}
		// Configure pool size
		dataSource.setMinPoolSize(minPoolSize);
		dataSource.setMaxPoolSize(maxPoolSize);
		dataSource.setAcquireIncrement(poolAcquireIncrement);

		// Configure idle connection testing
		if (poolIdleConnTestingPeriod > 0) {
			dataSource.setIdleConnectionTestPeriod(poolIdleConnTestingPeriod);
			if (!poolTestQuery.isEmpty()) {
				dataSource.setPreferredTestQuery(poolTestQuery);
			}
		}

		// Configure statement pooling
		dataSource.setMaxStatements(maxPoolStatements);
		dataSource.setMaxStatementsPerConnection(maxPoolStatementsPerConn);

		//dataSource.setTestConnectionOnCheckout(true);
		//dataSource.setBreakAfterAcquireFailure(false);
		//dataSource.setAcquireRetryAttempts(10000);
		//dataSource.setAcquireRetryDelay(10);

		// Initialize transaction manager
		transactionManager = new DataSourceTransactionManager(dataSource);

		if (log.isInfoEnabled()) {
			String msg = 
					"Connection pool initialized with the following values:";
			msg += "\n- minPoolSize=" + dataSource.getMinPoolSize();
			msg += "\n- maxPoolSize=" + dataSource.getMaxPoolSize();
			msg += "\n- acquireIncrement=" + dataSource.getAcquireIncrement();
			msg += "\n- maxStatements=" + dataSource.getMaxStatements();
			msg += "\n- maxStatementsPerConnection="
					+ dataSource.getMaxStatementsPerConnection();
			msg += "\n- idleConnTestPeriod="
					+ dataSource.getIdleConnectionTestPeriod();
			msg += "\n- idleConnTestQuery='"
					+ dataSource.getPreferredTestQuery() + "'";
			msg += "\n- idleConnTestOnCheckin="
					+ dataSource.isTestConnectionOnCheckin();
			log.info(msg.toString());
		}
	}

	public final void finalize() {

		if (dataSource == null) {
			log.warn("Datasource has not been initialized.");
			return;
		}

		int incompleteCount = 0;
		for (TransactionStatus ts : transactions) {			
			if (!ts.isCompleted()) {
				incompleteCount++;
				transactionManager.rollback(ts);				
			}
		}
		if (incompleteCount > 0) {
			throw new IllegalStateException("[SEVERE] Resource leak! "
					+ incompleteCount + " incomplete transactions were found and rolled back.");
		}

		// Close the data source
		dataSource.close();
		log.info("Closed the harvest data source.");
	}
	
	public synchronized TransactionStatus beginTransaction() {
		TransactionDefinition def = new DefaultTransactionDefinition();
	    TransactionStatus ts = transactionManager.getTransaction(def);
	    this.transactions.add(ts);
	    return ts;
	}
	
	public synchronized void commit(TransactionStatus ts) {
		transactionManager.commit(ts);
		this.transactions.remove(ts);
	}
	
	public synchronized void rollback(TransactionStatus ts) {
		transactionManager.rollback(ts);
		this.transactions.remove(ts);
	}

	public synchronized NamedParameterJdbcTemplate newTemplate() {
		return new NamedParameterJdbcTemplate(dataSource);
	}

}
