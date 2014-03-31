/**
 * 
 */
package cn.org.zeronote.orm.dao.dialect;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.org.zeronote.orm.DataAccessException;
import cn.org.zeronote.orm.PaginationSupport;
import cn.org.zeronote.orm.RowSelection;
import cn.org.zeronote.orm.extractor.PojoListResultSetExtractor;

/**
 * MySQL 分页查询器
 * @author <a href='mailto:lizheng8318@gmail.com'>leon</a>
 *
 */
public class MySQLPaginatedRepairer extends AbstractPaginatedRepairer {

	/**
	 * 
	 */
	public MySQLPaginatedRepairer() {
	}

	/*
	 * (non-Javadoc)
	 * @see cn.org.zeronote.orm.dao.dialect.IPaginatedRepairer#queryForPaginatedPojoList(java.lang.String, java.lang.Object[], java.lang.Class, cn.org.zeronote.orm.RowSelection)
	 */
	@Override
	public <T> PaginationSupport<T> queryForPaginatedPojoList(DataSource dataSource, String sql,
			Object[] args, Class<T> pojoType, RowSelection rowSelection)
			throws DataAccessException {
		StringBuilder cSql = new StringBuilder("select count(1) from (");
		cSql.append(sql)
			.append(") mt");
		
		// 分页sql
		StringBuilder nSql = new StringBuilder(sql);
		nSql.append(" ORDER BY ")
			.append(rowSelection.getOrder())
			.append(" limit ?, ?");
		
		PaginationSupport<T> ps = new PaginationSupport<T>();
		// 计算count
		Long count = query(dataSource, cSql.toString(), args, new ScalarHandler<Long>());
		ps.setTotalCount(count);
		ps.setPageSize(rowSelection.getPageSize());
		ps.setPageCount((int) (ps.getTotalCount() / ps.getPageSize() + (ps.getTotalCount() % ps.getPageSize() == 0 ? 0 : 1 )));
		
		int ccr = rowSelection.getStartPage() * rowSelection.getPageSize();
		if (ps.getTotalCount() <= ccr) {
			// 请求的当前页，没有数据
			ps.setCurrentPage(rowSelection.getStartPage());
			ps.setObject(new ArrayList<T>());
		} else {
			// 读取当前页数据
			ps.setCurrentPage(rowSelection.getStartPage());
			// 拼接参数，between的两端
			Object[] nArgs = new Object[args.length + 2];
			for (int i = 0; i < args.length; i++) {
				nArgs[i] = args[i];
			}
			nArgs[args.length] = ccr + 1;
			nArgs[args.length + 1] = ccr + rowSelection.getPageSize();
			
			List<T> ls = query(dataSource, nSql.toString(), nArgs, new PojoListResultSetExtractor<T>(pojoType));
			ps.setObject(ls);
		}
		
		return ps;
	}

}
