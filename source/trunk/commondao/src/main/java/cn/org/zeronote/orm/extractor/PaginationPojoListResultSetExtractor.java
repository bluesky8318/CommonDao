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
 * 
 * Pojo装载器 带分页的
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 * @param <T>	PO类型
 */
public class PaginationPojoListResultSetExtractor<T> implements ResultSetExtractor<PaginationSupport<T>> {

	private Class<T> pojoType;

	private RowSelection rsl;
	
	private RowProcessor rowProcessor = new BaseRowProcessor();
	
	/**
	 * @param cls	class
	 * @param rsl	分页设置
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
		
		int cur = rsl.getStartPage() * rsl.getPageSize() + 1;	// 计算第一行行号
		
		boolean atLast = false;	// 游标是否在最后一行后面了（结果集是否被取完）
		// 光标放置到第一行之前
		if (cur > 1) {
			if (!rs.absolute(cur - 1)) {
				// 试图将光标移动到最后一行之后，也就是说，该页内容，超出范围
				atLast = true;
			}
		}
		
		List<T> ls = new ArrayList<T>();
		int count = 0;
		if (atLast) {
			// 计算总数，当前页签内容为空
			count = -1;
		} else {
			atLast = true;
			while (rs.next()) {
				ls.add(rowProcessor.toBean(rs, pojoType));
				if (ls.size() >= rsl.getPageSize()) {
					// 获取完一页，退出
					atLast = false;
					break;
				}
			}
		}
		
		// 总数
		if (atLast) {
			if (count == -1) {
				count = 0;
			} else {
				count = cur - 1 + ls.size();
			}
		} else {
			// 使用游标获取总数
			if (rs.last()) {
				count = rs.getRow();	// 行号从1开始，最后一行正好是总数
			} else {
				// 不存在任何行
				count = 0;
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
