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
		// 将sql语句转换成对应的 查询总数/查询当前页 两个sql
		// 总数sql
		StringBuilder cSql = new StringBuilder("select count(1) from (");
		cSql.append(sql)
			.append(") as mt");
		
		// 分页sql
		StringBuilder nSql = new StringBuilder("select * from (");
		nSql.append("select *, ROW_NUMBER() OVER (ORDER BY ")
			.append(rowSelection.getOrder())
			.append(") as rank from (")
			.append(sql)
			.append(") as mt");
		nSql.append(") as mst where mst.rank between ? and ? ");
		
		return null;
	}

}
