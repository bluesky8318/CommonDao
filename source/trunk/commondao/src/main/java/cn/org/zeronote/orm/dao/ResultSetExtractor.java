/**
 * 
 */
package cn.org.zeronote.orm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.ResultSetHandler;

/**
 * ResultSet整合接口
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public interface ResultSetExtractor<T> extends ResultSetHandler<T> {

	/**
	 * Implementations must implement this method to process the entire ResultSet.
	 * @param rs ResultSet to extract data from. Implementations should
	 * not close this: it will be closed by the calling JdbcTemplate.
	 * @return an arbitrary result object, or <code>null</code> if none
	 * (the extractor will typically be stateful in the latter case).
	 * @throws SQLException if a SQLException is encountered getting column
	 * values or navigating (that is, there's no need to catch SQLException)
	 * @throws DataAccessException in case of custom exceptions
	 */
	T handle(ResultSet rs) throws SQLException;
}
