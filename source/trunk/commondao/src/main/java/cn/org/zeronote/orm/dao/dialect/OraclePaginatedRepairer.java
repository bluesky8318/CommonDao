/**
 * 
 */
package cn.org.zeronote.orm.dao.dialect;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.org.zeronote.orm.DataAccessException;
import cn.org.zeronote.orm.PaginationSupport;
import cn.org.zeronote.orm.RowSelection;
import cn.org.zeronote.orm.extractor.PojoListResultSetExtractor;

/**
 * oracle 分页查询
 * @author <a href='mailto:lizheng8318@gmail.com'>leon</a>
 *
 */
public class OraclePaginatedRepairer extends AbstractPaginatedRepairer {

	/**
	 * 
	 */
	public OraclePaginatedRepairer() {
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
			.append(") mt");
		
		// 分页sql
		StringBuilder nSql = new StringBuilder("select * from (");
		nSql.append("select ROWNUM rw, mt.* from (")
			.append(sql)
			.append(" ORDER BY ")
			.append(rowSelection.getOrder())
			.append(") mt ");
		nSql.append(") mst where mst.rw between ? and ? ");
		
		PaginationSupport<T> ps = new PaginationSupport<T>();
		// 计算count
		BigDecimal count = query(dataSource, cSql.toString(), args, new ScalarHandler<BigDecimal>());
		ps.setTotalCount(count.intValue());
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
