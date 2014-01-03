/**
 * 
 */
package cn.org.zeronote.orm.dao.dialect;

import javax.sql.DataSource;

import cn.org.zeronote.orm.DataAccessException;
import cn.org.zeronote.orm.PaginationSupport;
import cn.org.zeronote.orm.RowSelection;

/**
 * 分页
 * @author <a href='mailto:lizheng8318@gmail.com'>leon</a>
 *
 */
public interface IPaginatedRepairer {

	/**
	 * 分页查询
	 * @param dataSource
	 * @param sql
	 * @param args
	 * @param pojoType
	 * @param rowSelection
	 * @return
	 * @throws DataAccessException
	 */
	<T> PaginationSupport<T> queryForPaginatedPojoList(DataSource dataSource, String sql, Object[] args, Class<T> pojoType, RowSelection rowSelection) throws DataAccessException;
}
