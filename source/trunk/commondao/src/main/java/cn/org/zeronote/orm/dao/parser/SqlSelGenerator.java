/**
 * 
 */
package cn.org.zeronote.orm.dao.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.org.zeronote.orm.ORMAutoAssemble;
import cn.org.zeronote.orm.ORMColumn;
import cn.org.zeronote.orm.ORMTable;

/**
 * Orm sql语句生成
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class SqlSelGenerator implements Generator {

	/** orm fields cache*/
	private transient static Map<Class<?>, Map<Field, ORMColumn>> fieldsCache = new HashMap<Class<?>, Map<Field,ORMColumn>>();
	private transient byte[] synObj = new byte[0];
	
	/**ORM pojo class*/
	protected Class<?> pojoClazz;
	protected Map<String, Object[]> argsMap;
	protected Set<String> requireFields;
	
	protected String sql;
	protected Object[] args;
	
	/**
	 * 
	 */
	public SqlSelGenerator() {
	}

	/**
	 * 
	 * @param pojoClazz
	 * @param args
	 */
	public SqlSelGenerator(Class<?> pojoClazz, String[] requireFields, Map<String, Object[]> args) {
		this.pojoClazz = pojoClazz;
		this.argsMap = args;
		this.requireFields = new HashSet<String>();
		if (requireFields != null) {
			for (String f : requireFields) {
				this.requireFields.add(f);
			}
		}
	}
	
	
	
	/**
	 * 生成SQL
	 * @return
	 * @throws IllegalAccessException 
	 */
	protected void generateParam() throws IllegalAccessException {
		ORMAutoAssemble ormaa = pojoClazz.getAnnotation(ORMAutoAssemble.class);
		if (ormaa == null) {
			throw new IllegalAccessException("It not a ORMAutoAssemble Class!");
		}
		
		ORMTable ormTable = pojoClazz.getAnnotation(ORMTable.class);
		if (ormTable == null) {
			throw new IllegalAccessException("It not a ORMTable Class!");
		}
		
		String tableName = ormTable.tableName();
		String[] orders = ormTable.order();
		List<Object> params = new ArrayList<Object>();
		
		this.sql = genSql(params, tableName, this.argsMap, orders);
		
		// params
		this.args = params.toArray();
	}
	
	/**
	 * 生成SQL
	 * @param reArgs
	 * @return
	 */
	protected String genSql(List<Object> params, String tableName, Map<String, Object[]> argsMap, String[] orders) {
		// params to param:params
		StringBuilder sql = new StringBuilder("SELECT ");
		// SELECT
		Field[] fields = pojoClazz.getDeclaredFields();
		for (Field field : fields) {
			if (!requireFields.isEmpty() && !requireFields.contains(field.getName())) {
				// 不为空，则表示需要过滤，则判断是否包含，不包含，找下一个
				continue;
			}
			field.setAccessible(true);
			ORMColumn ormc = getAnnotation(pojoClazz, field);
			if (ormc != null) {
				sql.append(ormc.value()).append(",");
			}
		}
		sql.replace(sql.length() - 1, sql.length(), " ");
		// FROM 
		sql.append("FROM ").append(tableName).append(" ");
		// WHERE
		if (argsMap != null && argsMap.size() > 0) {
			sql.append("WHERE (");
			for (Field field : fields) {
				field.setAccessible(true);
				ORMColumn ormc = getAnnotation(pojoClazz, field);
				if (ormc != null) {
					Object[] val = argsMap.get(field.getName());
					if (val != null) {
						if (val.length > 1) {
							// 多参数
							sql.append("(");
							for (Object ovl : val) {
								sql.append(ormc.value()).append("=? OR ");
								Class<?> tc = field.getDeclaringClass();
								params.add(matchParam(ovl, tc));
							}
							sql.delete(sql.length() - 3, sql.length());
							sql.append(") AND ");
						} else {
							// 单参数
							sql.append(ormc.value()).append("=? AND ");
							Class<?> tc = field.getDeclaringClass();
							params.add(matchParam(val[0], tc));
						}
					}
				}
			}
			sql.replace(sql.length() - 5, sql.length(), ") ");
		}
		
		// ORDER
		if (orders != null && orders.length > 0) {
			sql.append("ORDER BY ");
			for (String order : orders) {
				sql.append(order).append(",");
			}
			sql.delete(sql.length() - 1, sql.length());
		}
		
		// return
		return sql.toString();
	}
	
	// 缓存annotation
	private ORMColumn getAnnotation(Class<?> clz, Field field) {
		Map<Field, ORMColumn> fs = fieldsCache.get(clz);
		if (fs == null) {
			// 加载
			synchronized (synObj) {
				fs = fieldsCache.get(clz);
				if (fs == null) {
					fs = new HashMap<Field, ORMColumn>();
					Field[] fields = pojoClazz.getDeclaredFields();
					for (Field f : fields) {
						f.setAccessible(true);
						ORMColumn ormc = f.getAnnotation(ORMColumn.class);
						fs.put(f, ormc);
					}
					fieldsCache.put(clz, fs);
				}
			}
		}
		return fs.get(field);
	}
	
	/**
	 * 类型匹配转换
	 * 如果val与给定的clz不匹配，则返回精确转换后的val
	 * 转换原则：
	 * String ->Number
	 * Number -> String
	 * @param val
	 * @param clz
	 * @return
	 */
	private Object matchParam(Object val, Class<?> clz) {
		if (val == null) {
			return val;
		}
		if (String.class.equals(clz)) {
			// 目标为String
			return val.toString();
		} else if (Integer.class.equals(clz) || int.class.equals(clz)) {
			// 目标int
			return val instanceof Integer ? val : Integer.parseInt(val.toString());
		} else if (Long.class.equals(clz) || long.class.equals(clz)) {
			// 目标long
			return val instanceof Long ? val : Long.parseLong(val.toString());
		} else if (Double.class.equals(clz) || double.class.equals(clz)) {
			// 目标Double
			return val instanceof Double ? val : Double.parseDouble(val.toString());
		} else if (Float.class.equals(clz) || float.class.equals(clz)) {
			// 目标Float
			return val instanceof Float ? val : Float.parseFloat(val.toString());
		} else {
			// 其它不转换
			return val;
		}
	}
	
	/**
	 * 相关参数
	 * @return
	 * @throws IllegalAccessException 
	 */
	public Object[] getArgs() throws IllegalAccessException {
		if (args == null) {
			generateParam();
		}
		return args == null ? new Object[0] : args;
	}
	
	/**
	 * Sql语句
	 * @return the sql
	 * @throws IllegalAccessException 
	 */
	public String getSql() throws IllegalAccessException {
		if (sql == null) {
			generateParam();
		}
		return sql;
	}

	/**
	 * @return the pojoClazz
	 */
	public Class<?> getPojoClazz() {
		return pojoClazz;
	}

	/**
	 * @param pojoClazz the pojoClazz to set
	 */
	public void setPojoClazz(Class<?> pojoClazz) {
		this.pojoClazz = pojoClazz;
	}
}
