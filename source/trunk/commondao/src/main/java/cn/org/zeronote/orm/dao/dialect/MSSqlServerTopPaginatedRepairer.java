/**
 * 
 */
package cn.org.zeronote.orm.dao.dialect;

import javax.sql.DataSource;

import cn.org.zeronote.orm.DataAccessException;
import cn.org.zeronote.orm.PaginationSupport;
import cn.org.zeronote.orm.RowSelection;

/**
 * SQL Server使用top进行分页
 * @author <a href='mailto:lizheng8318@gmail.com'>leon</a>
 *
 */
public class MSSqlServerTopPaginatedRepairer extends AbstractPaginatedRepairer {

	/**
	 * 
	 */
	public MSSqlServerTopPaginatedRepairer() {
	}

	/* (non-Javadoc)
	 * @see cn.org.zeronote.orm.dao.dialect.IPaginatedRepairer#queryForPaginatedPojoList(javax.sql.DataSource, java.lang.String, java.lang.Object[], java.lang.Class, cn.org.zeronote.orm.RowSelection)
	 */
	@Override
	public <T> PaginationSupport<T> queryForPaginatedPojoList(
			DataSource dataSource, String sql, Object[] args,
			Class<T> pojoType, RowSelection rowSelection)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

}
