/**
 * 
 */
package cn.org.zeronote.orm.dao.dialect;

import javax.sql.DataSource;

import cn.org.zeronote.orm.DataAccessException;
import cn.org.zeronote.orm.PaginationSupport;
import cn.org.zeronote.orm.RowSelection;

/**
 * oracle 分页查询
 * @author <a href='mailto:lizheng8318@gmail.com'>leon</a>
 *
 */
public class OraclePaginatedRepairer implements IPaginatedRepairer {

	/**
	 * 
	 */
	public OraclePaginatedRepairer() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see cn.org.zeronote.orm.dao.dialect.IPaginatedRepairer#queryForPaginatedPojoList(javax.sql.DataSource, java.lang.String, java.lang.Object[], java.lang.Class, cn.org.zeronote.orm.RowSelection)
	 */
	@Override
	public <T> PaginationSupport<T> queryForPaginatedPojoList(
			DataSource dataSource, String sql, Object[] args,
			Class<T> pojoType, RowSelection rowSelection)
			throws DataAccessException {
		// 调整sql语句，添加rownum进行分页
		
		return null;
	}

}
