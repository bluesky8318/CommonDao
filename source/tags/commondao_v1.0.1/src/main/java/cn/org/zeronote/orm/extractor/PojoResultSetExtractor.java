/**
 * 
 */
package cn.org.zeronote.orm.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.org.zeronote.orm.dao.ResultSetExtractor;
import cn.org.zeronote.orm.dao.RowProcessor;


/**
 * Pojo装载器
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class PojoResultSetExtractor<T> implements ResultSetExtractor<T> {

	private Class<T> pojoType;
	private boolean next;
	
	private RowProcessor rowProcessor = new BaseRowProcessor();
	/**
	 * 
	 */
	public PojoResultSetExtractor(Class<T> cls) {
		this(cls, false);
	}
	
	public PojoResultSetExtractor(Class<T> cls, boolean next) {
		this.pojoType = cls;
		this.next = next;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.orm.ResultSetExtractor#extractData(java.sql.ResultSet)
	 */
	@Override
	public T handle(ResultSet rs) throws SQLException {
		if (rs.next()) {
			if (next) {
				if (rs.next()) {
					return toBean(rs);
				} else {
					return null;
				}
			} else {
				return toBean(rs);
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 拼装结果集
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private T toBean(ResultSet rs) throws SQLException {
		return rowProcessor.toBean(rs, pojoType);
	}
}
