/**
 * 
 */
package cn.org.zeronote.orm.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.org.zeronote.orm.PaginationSupport;
import cn.org.zeronote.orm.RowSelection;
import cn.org.zeronote.orm.dao.ResultSetExtractor;
import cn.org.zeronote.orm.dao.RowProcessor;



/**
 * Pojo装载器 带分页的
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 * @date 2012-3-29
 *
 * @param <T>
 */
public class PaginationPojoListResultSetExtractor<T> implements ResultSetExtractor<PaginationSupport<T>> {

	private Class<T> pojoType;

	private RowSelection rsl;
	
	private RowProcessor rowProcessor = new BaseRowProcessor();
	/**
	 * 
	 */
	public PaginationPojoListResultSetExtractor(Class<T> cls, RowSelection rsl) {
		this.pojoType = cls;
		this.rsl = rsl;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.orm.ResultSetExtractor#extractData(java.sql.ResultSet)
	 */
	@Override
	public PaginationSupport<T> handle(ResultSet rs) throws SQLException {
		PaginationSupport<T> ps = new PaginationSupport<T>();
		
		long cur = rsl.getStartPage() * rsl.getPageSize() + 1;
		long index = 1;
		long count = 0;
		List<T> ls = new ArrayList<T>();
		while (rs.next()) {
			count++;
			if (cur > index) {
				index++;
				continue;
			}
			if (ls.size() < rsl.getPageSize()) {
				ls.add(rowProcessor.toBean(rs, pojoType));
			}
		}
		ps.setObject(ls);
		ps.setCurrentPage(rsl.getStartPage());
		ps.setPageCount((int) (count / rsl.getPageSize() + (count % rsl.getPageSize() > 0 ? 1 : 0)));
		ps.setPageSize(rsl.getPageSize());
		ps.setTotalCount(count);
		return ps;
	}

}
