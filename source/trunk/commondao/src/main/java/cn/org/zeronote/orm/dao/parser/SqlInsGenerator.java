/**
 * 
 */
package cn.org.zeronote.orm.dao.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.org.zeronote.orm.ORMAutoAssemble;
import cn.org.zeronote.orm.ORMColumn;
import cn.org.zeronote.orm.ORMHash;
import cn.org.zeronote.orm.ORMTable;

/**
 * UPDATE sql 语句生成器
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class SqlInsGenerator implements Generator {
	
	private static Logger logger = LoggerFactory.getLogger(SqlInsGenerator.class);
	
	protected Object pojo;
	
	
	protected String sql;
	protected Object[] argsList;
	
	/**
	 * 
	 */
	public SqlInsGenerator() {
	}
	
	/**
	 * 
	 * @param pojoClazz
	 */
	public SqlInsGenerator(Object pojo) {
		this.pojo = pojo;
	}

	/**
	 * 生成主键依赖的
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	private void generate() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Class<?> pojoClazz = pojo.getClass();
		// TABLE
		ORMAutoAssemble ormaa = pojoClazz.getAnnotation(ORMAutoAssemble.class);
		if (ormaa == null) {
			throw new IllegalAccessException("It not a ORMAutoAssemble Class!");
		}
		
		ORMTable ormTable = pojoClazz.getAnnotation(ORMTable.class);
		if (ormTable == null) {
			throw new IllegalAccessException("It not a ORMTable Class!");
		}
		ORMHash ormHashTable = pojoClazz.getAnnotation(ORMHash.class);
		
		
		String tableName = ormHashTable == null ? ormTable.tableName() : DBUtils.getInstance().getHashTableName(ormHashTable, ormTable, pojo);
		generate(tableName);
		
	}
	
	/**
	 * 生成主键依赖的
	 * @param tableName
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void generate(String tableName) throws IllegalArgumentException, IllegalAccessException {
		Class<?> pojoClazz = pojo.getClass();
		List<Object> params = new ArrayList<Object>();
		List<String> paramKeys = new ArrayList<String>();
		
		StringBuilder ins = new StringBuilder("INSERT INTO ");
		ins.append(tableName).append(" (");
		// INSERT
		Field[] fields = pojoClazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			ORMColumn ormc = field.getAnnotation(ORMColumn.class);
			if (ormc != null) {
				Object val = field.get(pojo);
				if (val != null) {
					// 不是空才去设置
					ins.append(ormc.value()).append(", ");
					params.add(val);
					paramKeys.add("?");
				} else if (!"null".equalsIgnoreCase(ormc.defaultValue())) {
					// 是空，但是有默认值，则设置
					switch (ormc.defaultValueScope()) {
					case ALL:
					case INSERT:
						ins.append(ormc.value()).append(", ");
						Object value = ORMColumn.DEFAULT_DATE.equals(ormc.defaultValue()) ? new Date() : ormc.defaultValue();
						params.add(value);
						paramKeys.add("?");
						// 回写到pojo中
						try {
							if (value != null) {
								field.set(pojo, ValueUtils.conversionType(value, field.getType()));
							}
						} catch (Exception e) {
							logger.error("reset default value error, value:{}; field:{}", new Object[]{value, field, e});
						}
						break;
					default:
						break;
					}
				}
			}
		}
		ins.replace(ins.length() - 2, ins.length(), ") ");
		// VALUES
		ins.append(" VALUES (");
		for (String paramKey : paramKeys) {
			ins.append(paramKey).append(", ");
		}
		ins.replace(ins.length() - 2, ins.length(), ") ");
		this.sql = ins.toString();
		
		// params
		argsList = params.toArray();
	}
	
	/**
	 * @return the sql
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public String getSql() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (sql == null) {
			generate();
		}
		return sql;
	}

	/**
	 * @return the args
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public Object[] getArgs() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (argsList == null) {
			generate();
		}
		return argsList;
	}

}
