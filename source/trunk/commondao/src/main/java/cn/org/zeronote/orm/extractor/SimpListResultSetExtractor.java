/**
 * 
 */
package cn.org.zeronote.orm.extractor;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import cn.org.zeronote.orm.dao.ResultSetExtractor;


/**
 * 简单对象装载器
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class SimpListResultSetExtractor<T> implements ResultSetExtractor<List<T>> {
	
	private Class<T> pojoType;
	
	/**
	 * @param cls	class
	 */
	public SimpListResultSetExtractor(Class<T> cls) {
		pojoType = cls;
	}

	@Override
	public List<T> handle(ResultSet rs) throws SQLException {
		List<T> ls = new ArrayList<T>();
		while (rs.next()) {
			ls.add((T) tranValue(rs));
		}
		return ls;
	}
	
	@SuppressWarnings("unchecked")
	private T tranValue(ResultSet rs) throws SQLException {
		return (T) conversionType(rs.getObject(1), rs.getMetaData().getColumnType(1),  pojoType);
	}
	
	/**
	 * 试图将value转换为voClazz类型
	 * @param value
	 * @param type
	 * @param voClazz
	 * @return
	 * @throws SQLException 
	 */
	private Object conversionType(Object value, int type, Class<?> voClazz) throws SQLException {
		if (value == null) {
			// 空值返回null || 0
			if (voClazz != null && (voClazz.equals(int.class) || voClazz.equals(double.class) || voClazz.equals(float.class))) {
				return 0;
			} else {
				return null;
			}
		}
		if (value.getClass().equals(voClazz)) {
			// 类型相同，直接返回
			return value;
		}
		// 根据类型转换表进行转换
		//// 目标类型：boolean
		if (Boolean.class.equals(voClazz) || boolean.class.equals(voClazz)) {
			switch (type) {
			case Types.CHAR:
			case Types.VARCHAR:
				String str = String.valueOf(value);
				if ("0".equalsIgnoreCase(str) 
						|| "N".equalsIgnoreCase(str)
						|| "No".equalsIgnoreCase(str)
						) {
					// 0, N, No表示false
					return false;
				} else {
					// 非0...表示true
					return true;
				}
			default:
				return value;
			}
		}
		
		//// 目标类型：long
		if (Long.class.equals(voClazz) || long.class.equals(voClazz)) {
			switch (type) {
			case Types.DECIMAL:
				if (value instanceof BigDecimal) {
					return Math.round(((BigDecimal)value).doubleValue());
				}
			case Types.NUMERIC:
			case Types.BIGINT:
			case Types.INTEGER:
				if (value instanceof BigDecimal) {
					return Math.round(((BigDecimal)value).doubleValue());
				} else {
					try {
						Double d = Double.valueOf(String.valueOf(value));
						return d.longValue();
					} catch (NumberFormatException e) {
					}
				}
			default:
				return value;
			}
		}

		//// 目标类型：int
		if (Integer.class.equals(voClazz) || int.class.equals(voClazz)) {
			switch (type) {
			case Types.DECIMAL:
				if (value instanceof BigDecimal) {
					return Math.round(((BigDecimal)value).floatValue());
				}
			case Types.NUMERIC:
			case Types.BIGINT:
			case Types.INTEGER:
				if (value instanceof BigDecimal) {
					return Math.round(((BigDecimal)value).floatValue());
				} else {
					try {
						Integer d = Integer.valueOf(String.valueOf(value));
						return d.intValue();
					} catch (NumberFormatException e) {
					}
				}
			default:
				return value;
			}
		}
		
		//// 目标类型：double
		if (Double.class.equals(voClazz) || double.class.equals(voClazz)) {
			switch (type) {
			case Types.DECIMAL:
				if (value instanceof BigDecimal) {
					return ((BigDecimal)value).doubleValue();
				}
			case Types.NUMERIC:
			case Types.BIGINT:
			case Types.INTEGER:
				if (value instanceof BigDecimal) {
					return ((BigDecimal)value).doubleValue();
				} else {
					try {
						Double d = Double.valueOf(String.valueOf(value));
						return d.doubleValue();
					} catch (NumberFormatException e) {
					}
				}
			default:
				return value;
			}
		}
		
		return value;
	}
}
