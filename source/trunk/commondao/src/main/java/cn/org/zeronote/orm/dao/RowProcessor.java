/**
 * 
 */
package cn.org.zeronote.orm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * 记录拼装
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public interface RowProcessor {

	/**
	 * Rs的一条记录拼装成javabean
	 * @param <T>
	 * @param rs
	 * @param clz
	 * @return
	 * @throws SQLException
	 */
	<T> T toBean(ResultSet rs, Class<T> clz) throws SQLException;
	
	/**
	 * Rs的一条记录拼装成javabean
	 * <br>
	 * 过滤requireFields
	 * @param rs
	 * @param clz
	 * @param requireFields
	 * @return
	 * @throws SQLException
	 */
	<T> T toBean(ResultSet rs, Class<T> clz, Set<String> requireFields) throws SQLException;
	
}
