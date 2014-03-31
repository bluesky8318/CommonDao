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
	 * @param <T>					pojo class
	 * @param dataSource			数据源
	 * @param sql					查询语句
	 * @param args					查询参数
	 * @param pojoType				PO类型
	 * @param rowSelection			分页设置
	 * @return						分页结果集
	 * @throws DataAccessException	数据访问异常
	 */
	<T> PaginationSupport<T> queryForPaginatedPojoList(DataSource dataSource, String sql, Object[] args, Class<T> pojoType, RowSelection rowSelection) throws DataAccessException;
}
