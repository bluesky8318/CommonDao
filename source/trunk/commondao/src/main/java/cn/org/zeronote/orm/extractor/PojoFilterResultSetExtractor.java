/**
 * 
 */
package cn.org.zeronote.orm.extractor;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cn.org.zeronote.orm.ORMAutoAssemble;
import cn.org.zeronote.orm.ORMColumn;
import cn.org.zeronote.orm.dao.ResultSetExtractor;
import cn.org.zeronote.orm.dao.RowProcessor;



/**
 * Pojo装载器
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class PojoFilterResultSetExtractor<T> implements ResultSetExtractor<T> {

	private Class<T> pojoType;
	private boolean next;
	private Map<String, Object> filter;
	
	private RowProcessor rowProcessor = new BaseRowProcessor();
	
	private Boolean ignoreCase;
	
	/**
	 * @param cls	class
	 */
	public PojoFilterResultSetExtractor(Class<T> cls) {
		this(cls, false, new HashMap<String, Object>());
	}
	
	/**
	 * 
	 * @param cls		class
	 * @param next		是否包含下一个
	 * @param filter	过滤内容
	 */
	public PojoFilterResultSetExtractor(Class<T> cls, boolean next, Map<String, Object> filter) {
		this.pojoType = cls;
		this.next = next;
		this.filter = filter;
		
		if (this.filter != null) {
			if (isIgnoreCase()) {
				Map<String, Object> fs = new HashMap<String, Object>();
				for (String key : this.filter.keySet()) {
					fs.put(key.toLowerCase(), this.filter.get(key));
				}
				this.filter = fs;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.orm.ResultSetExtractor#extractData(java.sql.ResultSet)
	 */
	@Override
	public T handle(ResultSet rs) throws SQLException {
		while (rs.next()) {
			T t = toBean(rs);
			try {
				if (check(t)) {
					if (next) {
						if (rs.next()) {
							return toBean(rs);
						} else {
							return null;
						}
					} else {
						return toBean(rs);
					}
				}
			} catch (IllegalArgumentException e) {
				throw new SQLException("filter result set error!", e);
			} catch (IllegalAccessException e) {
				throw new SQLException("filter result set error!", e);
			}
		}
		return null;
	}
	
	private boolean check(T t) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = pojoType.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			ORMColumn ormc = field.getAnnotation(ORMColumn.class);
			if (ormc != null) {
				if (filter.get(ormc.value()) == null) {
					continue;
				}
				if (!field.get(t).equals(filter.get(isIgnoreCase() ? ormc.value().toLowerCase() : ormc.value()))) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * @return the ignoreCase
	 */
	private boolean isIgnoreCase() {
		if (ignoreCase == null) {
			ORMAutoAssemble orma = pojoType.getAnnotation(ORMAutoAssemble.class);
			ignoreCase = orma == null ? false : orma.ignoreCase();	// 是否忽略大小写
		}
		return ignoreCase;
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
