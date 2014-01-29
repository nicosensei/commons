/**
 * 
 */
package com.github.nicosensei.commons.dao;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

/**
 * Base abstract class for generic DAOs. 
 * 
 * Offers utility methods to ease querying and updating the harvest database, and simplified
 * transaction handling.
 * 
 * This base DAO service leverages Spring JDBC's {@link NamedParameterJdbcTemplate} and
 * programmatic transaction management to streamline DAO code, by implementing in one place
 * transaction management and exception handling.
 * 
 * Enabling the "trace" logging level on this class will trigger the logging of SQL requests. 
 * 
 */
public abstract class GenericDao {
	
	/**
	 * Result set extractor expecting a single integer value.
	 */
	protected static final class IntResultExtractor implements ResultSetExtractor<Integer> {
		@Override
		public Integer extractData(ResultSet rs)
				throws SQLException, DataAccessException {
			return (rs.next() ? rs.getInt(1) : null);
		}
	}
	
	/**
	 * Result set extractor expecting a single long value.
	 */
	protected static final class LongResultExtractor implements ResultSetExtractor<Long> {
		@Override
		public Long extractData(ResultSet rs)
				throws SQLException, DataAccessException {
			return (rs.next() ? rs.getLong(1) : null);
		}
	}
	
	/**
	 * Result set extractor expecting a single string value.
	 */
	protected static final class StringResultExtractor implements ResultSetExtractor<String> {
		@Override
		public String extractData(ResultSet rs)
				throws SQLException, DataAccessException {
			return (rs.next() ? rs.getString(1) : null);
		}
	}
	
	/**
	 * Row mapper expecting a single string value per result set row.
	 */
	protected static final class StringValueMapper implements RowMapper<String> {
		
		@Override
		public String mapRow(ResultSet rs, int pos) throws SQLException {
			return rs.getString(1);
		}
		
	}
	
	/**
	 * Row mapper expecting a single long value per result set row.
	 */
	protected static final class LongValueMapper implements RowMapper<Long> {
		
		@Override
		public Long mapRow(ResultSet rs, int pos) throws SQLException {
			return rs.getLong(1);
		}
		
	}
	
	/**
	 * Result set extractor that generates a map of string-long pairs.
	 * Expects at least one string and one long column with configured names
	 * to be present in the result set. 
	 */
	protected static class StringToLongMapExtractor 
	implements ResultSetExtractor<Map<String, Long>> {

		/**
		 * The name of the string column to fetch.
		 */
		private final String stringColumnName;

		/**
		 * The name of the string column to fetch.
		 */
		private final String longColumnName;

		public StringToLongMapExtractor(
				final String stringColumnName, 
				final String longColumnName) {
			super();
			this.stringColumnName = stringColumnName;
			this.longColumnName = longColumnName;
		}

		@Override
		public Map<String, Long> extractData(ResultSet rs)
				throws SQLException, DataAccessException {
			Map<String, Long> string2Long = new HashMap<String, Long>();
			while (rs.next()) {
				string2Long.put(
						rs.getString(stringColumnName), 
						rs.getLong(longColumnName));
			}
			return string2Long;
		}

	}

	/** 
	 * The logger. 
	 */
	private static final Logger log = Logger.getLogger(GenericDao.class);
	
	private final Database database;
	
	private final NamedParameterJdbcTemplate template;	
	
	public GenericDao(Database database) {
		super();
		this.database = database;
		this.template = database.newTemplate();
	}

	protected String getMaxLengthStringValue(
			final Object o,
			final String valueKey, 
			final String value, 
			final int maxLength) {
		if (value.length() > maxLength) {
            log.warn(valueKey + " of " + o
                    + " is longer than the allowed " + maxLength
                    + " characters. The value is truncated to length "
                    + maxLength
                    + ". The untruncated value was: " + value);
            // truncate to length maxLength
            return value.substring(0, maxLength);
        }
        return value;
	}
	
	protected String getMaxLengthTextValue(
			final Object o,
			final String valueKey, 
			final String value, 
			final int maxLength) {
	    
        int actualMaxLentgh = maxLength;
        if (value.length() > maxLength) {
            log.warn(
                    "The field '" + valueKey + "' is " + value.length() 
                    + " characters long, which is "
                            +  (value.length() - maxLength) + " longer than the allowed " 
                    + maxLength + " characters. The value is now truncated to "
                    + "length " + maxLength);
            // truncate to length maxLength (if maxLength <= Integer.MAX_VALUE)
            // else truncate to length Integer.MAX_VALUE
            if (maxLength > Integer.MAX_VALUE) {
                log.warn("The maxLength is larger than maxint (" +  Integer.MAX_VALUE
                        + "), which is not allowed. maxLength changed to maxint");
                actualMaxLentgh = Integer.MAX_VALUE;
            }
            return value.substring(0, (int) actualMaxLentgh);
        }        
        return value;
	}
	
	/**
	 * Queries an integer value.
	 * @param paramSql the parameterized SQL request
	 * @param paramMap the parameter map
	 * @return an integer value
	 */
	protected int queryIntValue(
			final String paramSql, 
			final ParameterMap paramMap) {
		return query(paramSql, paramMap, new IntResultExtractor());
	}
	
	/**
	 * Queries an integer value.
	 * @param sql the SQL request
	 * @return an integer value
	 */
	protected int queryIntValue(final String sql) {
		return queryIntValue(sql, ParameterMap.EMPTY);
	}
	
	/**
	 * Queries a long value.
	 * @param paramSql the parameterized SQL request
	 * @param paramMap the parameter map
	 * @return a long value
	 */
	protected long queryLongValue(
			final String paramSql, 
			final ParameterMap paramMap) {
		return query(paramSql, paramMap, new LongResultExtractor());
	}
	
	/**
	 * Queries a long value.
	 * @param sql the SQL request
	 * @return a long value
	 */
	protected long queryLongValue(final String sql) {
		return queryLongValue(sql, ParameterMap.EMPTY);
	}
	
	/**
	 * Queries a string value.
	 * @param paramSql the parameterized SQL request
	 * @param paramMap the parameter map
	 * @return a string value
	 */
	protected String queryStringValue(
			final String paramSql, 
			final ParameterMap paramMap) {
		return query(paramSql, paramMap, new StringResultExtractor());
	}
	
	/**
	 * Queries a string value.
	 * @param sql the SQL request
	 * @return a string value
	 */
	protected String queryStringValue(final String sql) {
		return queryStringValue(sql, ParameterMap.EMPTY);
	}
	
	/**
	 * Queries a list of strings.
	 * @param paramSql the parameterized SQL request
	 * @param paramMap the parameter map
	 * @return a list of strings
	 */
	protected List<String> queryStringList(
			final String paramSql, 
			final ParameterMap paramMap) {
		return query(paramSql, paramMap, new StringValueMapper());
	}
	
	/**
	 * Queries a list of strings.
	 * @param sql the SQL request
	 * @return a list of strings
	 */
	protected List<String> queryStringList(final String sql) {
		return queryStringList(sql, ParameterMap.EMPTY);
	}
	
	/**
	 * Queries a list of longs.
	 * @param paramSql the parameterized SQL request
	 * @param paramMap the parameter map
	 * @return a list of strings
	 */
	protected List<Long> queryLongList(
			final String paramSql, 
			final ParameterMap paramMap) {
		return query(paramSql, paramMap, new LongValueMapper());
	}
	
	/**
	 * Queries a list of longs.
	 * @param sql the SQL request
	 * @return a list of strings
	 */
	protected List<Long> queryLongList(final String sql) {
		return queryLongList(sql, ParameterMap.EMPTY);
	}
	
	/**
	 * Generically queries the DB for a result.
	 * @param paramSql the parameterized SQL request
	 * @param paramMap the map of parameters and values
	 * @param extractor the desired {@link ResultSetExtractor} implementation
	 * @return the object built by the extractor
	 */
	protected <T> T query(
			final String paramSql, 
			final ParameterMap paramMap,
			final ResultSetExtractor<T> extractor) {
		
		if (log.isTraceEnabled()) {
			log.trace("[DB QUERY] " + paramSql + " " + paramMap);
		}
		return template.query(paramSql, paramMap, extractor);
	}
	
	/**
	 * Generically queries the DB for a result.
	 * @param sql the SQL request
	 * @param extractor the desired {@link ResultSetExtractor} implementation
	 * @return the object built by the extractor
	 */
	protected <T> T query(
			final String sql,
			final ResultSetExtractor<T> extractor) {
		return query(sql, ParameterMap.EMPTY, extractor);
	}
	
	/**
	 * Generically queries the DB for a list of custom objects.
	 * @param paramSql the parameterized SQL request
	 * @param paramMap the map of parameters and values
	 * @param mapper the desired {@link RowMapper} implementation to build the list elements
	 * @return the object built by the extractor
	 */
	protected <T> List<T> query(
			final String paramSql, 
			final ParameterMap paramMap,
			final RowMapper<T> mapper) {
		if (log.isTraceEnabled()) {
			log.trace("[DB QUERY] " + paramSql + " " + paramMap);
		}
		return template.query(paramSql, paramMap, mapper);
	}
	
	/**
	 * Generically queries the DB for a list of custom objects.
	 * @param sql the parameterized SQL request
	 * @param mapper the desired {@link RowMapper} implementation to build the list elements
	 * @return the object built by the extractor
	 */
	protected <T> List<T> query(
			final String sql,
			final RowMapper<T> mapper) {
		return query(sql, ParameterMap.EMPTY, mapper);
	}
	
	/**
	 * Executes a DB update and returns the generated key 
	 * for the given column name.
	 * @param paramSql the parameterized SQL request
	 * @param paramMap the map of parameters and values
	 * @param idColumnName the name of the key column
	 * @return the generated ID
	 * @throws UnknownID if no matching ID was generated
	 */
	protected Long executeUpdate(
			final String paramSql, 
			final ParameterMap paramMap,
			final String idColumnName) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		if (log.isTraceEnabled()) {
			log.trace("[DB UPDATE] " + paramSql + " " + paramMap);
		}
		template.update(paramSql, new MapSqlParameterSource(paramMap), keyHolder);		
		Long generatedId = (Long) keyHolder.getKeys().get(idColumnName);
		if (generatedId == null) {
			throw new DataRetrievalFailureException(
					"No generated id for column '" + idColumnName + "' when executing"
					+ " update." + "\n\tParameterized SQL: " + paramSql 
					+ "\n\tParameters source: " + paramMap);
		}
		return generatedId;
	}
	
	/**
     * Executes a method as a transaction.
     * @param methodName the method name
     * @param params the method parameters, as Class/Object (type/value) pairs
     * @return the returned object.
     * @throws TransactionException if something went wrong.
     */
    protected final Object executeTransaction(
            final String methodName,
            final Object... params) {

    	TransactionStatus trans = database.beginTransaction();
        Class<? extends GenericDao> daoClass = getClass();

        int pCount = params.length;
        List<Class<?>> paramClasses = new ArrayList<Class<?>>();
        List<Object> paramValues = new ArrayList<Object>();
        for (int i = 0; i < pCount - 1 ; i += 2) {
            paramClasses.add((Class<?>) params[i]);
            paramValues.add(params[i + 1]);
        }

        try {
            Method m = daoClass.getDeclaredMethod(
            		methodName, 
            		(Class<?>[]) paramClasses.toArray(new Class<?>[paramClasses.size()]));
            boolean isNotAccessible = !m.isAccessible();
            if (isNotAccessible) {
                m.setAccessible(true);
            }
            
            Object ret = m.invoke(
            		this, 
            		(Object[]) paramValues.toArray(new Object[paramValues.size()]));
            if (isNotAccessible) {
                m.setAccessible(false);
            }
            
            database.commit(trans);
            if (log.isDebugEnabled()) {
    			log.debug("Transaction was committed");
    		}
            
            return ret;
        } catch (final SecurityException e) {
            throw new DatabaseError("SecurityException when invoking method " 
            		+ methodName + " failed.", e);
        } catch (final NoSuchMethodException e) {
        	throw new DatabaseError("No such method " + getClass() 
        			+ "#" + methodName, e);
        } catch (final Throwable t) {
        	database.rollback(trans);
        	String message = "Transactional invocation of method " + methodName + " failed."
        			+ "\nTransaction was rolled back"; 
    		log.error(message, t);    		
    		throw new DatabaseError(message, t);
        }
    }
	
	/**
	 * Executes a DB update.
	 * @param paramSql the parameterized SQL request
	 * @param paramMap the map of parameters and values
	 * @return the update count
	 */
	protected int executeUpdate(
			final String paramSql, 
			ParameterMap paramMap) {
		if (log.isTraceEnabled()) {
			log.trace("[DB UPDATE] " + paramSql + " " + paramMap);
		}
		return template.update(paramSql, paramMap);
	}
	
	/**
	 * Executes a DB update.
	 * @param sql the SQL request
	 * @return the update count
	 */
	protected int executeUpdate(final String sql) {
		return executeUpdate(sql, ParameterMap.EMPTY);
	}
	
	/**
	 * Executes a DB update.
	 * @param paramSql the parameterized SQL request
	 * @param paramMap the map of parameters and values
	 * @param genKeyHolder a mutable map that will contain generated keys.
	 * @return the update count
	 */
	protected int executeUpdateGetGeneratedKeys(
			final String paramSql, 
			final ParameterMap paramMap,
			Map<String, Object> genKeyHolder) {
		if (log.isTraceEnabled()) {
			log.trace("[DB UPDATE] " + paramSql + " " + paramMap);
		}		
		GeneratedKeyHolder keys = new GeneratedKeyHolder();
		int updateCount =  template.update(
				paramSql, 
				new MapSqlParameterSource(paramMap), 
				keys);
		
		genKeyHolder.clear();
		genKeyHolder.putAll(keys.getKeys());		
		
		return updateCount;
	}
	
	/** 
	 * Translate a "normal" glob (with * and .) into SQL syntax.
	 *
	 * @param glob A shell-like glob string (must not be null)
	 * @return A string that implements glob in SQL "LIKE" constructs.
	 */
	protected String makeSQLGlob(String glob) {
		return glob.replace("*", "%").replace("?", "_");
	}
	
	/**
	 * Extracts a date from the result set, preserving null values.
	 * @param rs the result set
	 * @param columnName the column name
	 * @return a date instance or null if the value is null in the result set
	 * @throws SQLException if the extraction failed (column name not present)
	 */
	protected Date getDateKeepNull(ResultSet rs, String columnName) throws SQLException {
		Timestamp ts = rs.getTimestamp(columnName);
		return rs.wasNull() ? null : new Date(ts.getTime());
	}
	
	/**
	 * Extracts a long from the result set, preserving null values.
	 * @param rs the result set
	 * @param columnName the column name
	 * @return a {@link Long} instance or null if the value is null in the result set
	 * @throws SQLException if the extraction failed (column name not present)
	 */
	protected Long getLongKeepNull(ResultSet rs, String columnName) throws SQLException {
		long l = rs.getLong(columnName);
		return rs.wasNull() ? null : l;
	}
	
	/**
	 * Extracts a long from the result set, preserving null values.
	 * @param rs the result set
	 * @param columnName the column name
	 * @return a {@link Long} instance or null if the value is null in the result set
	 * @throws SQLException if the extraction failed (column name not present)
	 */
	protected Integer getIntegerKeepNull(ResultSet rs, String columnName) throws SQLException {
		int l = rs.getInt(columnName);
		return rs.wasNull() ? null : l;
	}

}
