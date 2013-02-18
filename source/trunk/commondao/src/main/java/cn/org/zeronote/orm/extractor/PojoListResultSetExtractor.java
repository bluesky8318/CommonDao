/**
 * 
 */
package cn.org.zeronote.orm.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.org.zeronote.orm.dao.ResultSetExtractor;
import cn.org.zeronote.orm.dao.RowProcessor;


/**
 * Pojo装载器
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class PojoListResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

	private Class<T> pojoType;
	private Set<String> requireFields = new HashSet<String>();
	
	private RowProcessor rowProcessor = new BaseRowProcessor();
	/**
	 * 
	 */
	public PojoListResultSetExtractor(Class<T> cls) {
		this.pojoType = cls;
	}
	
	/**
	 * 包含过滤field
	 * @param cls
	 * @param requireFields
	 */
	public PojoListResultSetExtractor(Class<T> cls, String[] requireFields) {
		this.pojoType = cls;
		if (requireFields != null) {
			for (String f : requireFields) {
				this.requireFields.add(f);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.orm.ResultSetExtractor#extractData(java.sql.ResultSet)
	 */
	@Override
	public List<T> handle(ResultSet rs) throws SQLException {
		List<T> ls = new ArrayList<T>();
		while (rs.next()) {
			ls.add(rowProcessor.toBean(rs, pojoType, requireFields));
		}
		return ls;
	}

}
