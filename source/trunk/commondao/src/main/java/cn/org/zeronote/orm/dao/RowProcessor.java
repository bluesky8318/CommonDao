/**
 * 
 */
package cn.org.zeronote.orm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 记录拼装
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public interface RowProcessor {

	/**
	 * Rs的一条记录拼装成javabean
	 * @param <T>	PO类型
	 * @param rs	原始结果集
	 * @param clz	PO列席
	 * @return		拼装后bean
	 * @throws SQLException		数据访问异常
	 */
	<T> T toBean(ResultSet rs, Class<T> clz) throws SQLException;
	
}
