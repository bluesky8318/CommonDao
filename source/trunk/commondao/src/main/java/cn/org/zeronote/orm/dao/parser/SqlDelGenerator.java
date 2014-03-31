/**
 * 
 */
package cn.org.zeronote.orm.dao.parser;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.org.zeronote.orm.ORMAutoAssemble;
import cn.org.zeronote.orm.ORMColumn;
import cn.org.zeronote.orm.ORMHash;
import cn.org.zeronote.orm.ORMTable;

/**
 * DELETE sql 语句生成器
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class SqlDelGenerator implements Generator {

	private Class<?> pojoClazz;
	private Map<String, Object> delArgs;
	
	private String sql;
	private Object[] args;
	
	
	/**
	 * 条件删除，无需自己创建删除对象
	 * @param pojoType		POJO class
	 * @param args			params
	 */
	public SqlDelGenerator(Class<?> pojoType, Map<String, Object> args) {
		this.pojoClazz = pojoType;
		this.delArgs = args;
	}

	/**
	 * 主键删除
	 * @param pojo							pojo
	 * @param usePhysicalPk					是否物理主键
	 * @throws IllegalArgumentException		参数异常
	 * @throws IllegalAccessException		参数异常
	 */
	public SqlDelGenerator(Object pojo, boolean usePhysicalPk) throws IllegalArgumentException, IllegalAccessException {
		this.pojoClazz = pojo.getClass();
		this.delArgs = fieldArgsMap(pojo, usePhysicalPk);
	}
	
	/**
	 * 主键删除
	 * @param pojoType						pojo
	 * @param pkVal							主键
	 * @param usePhysicalPk					是否物理主键
	 * @throws IllegalAccessException 		参数异常
	 * @throws InstantiationException 		参数异常
	 */
	public SqlDelGenerator(Class<?> pojoType, Object pkVal, boolean usePhysicalPk) throws InstantiationException, IllegalAccessException {
		this.pojoClazz = pojoType;
		this.delArgs = fieldArgsMap(pojoType, pkVal, usePhysicalPk);
	}

	/**
	 * 拼装删除参数
	 * @param pojoType						pojo
	 * @param pkVal							主键
	 * @param usePhysicalPk					是否物理主键
	 * @return								参数Map
	 * @throws IllegalArgumentException		参数异常
	 * @throws IllegalAccessException		参数异常
	 */
	private Map<String, Object> fieldArgsMap(Class<?> pojoType, Object pkVal, boolean usePhysicalPk) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> delArgs = new HashMap<String, Object>();
		Field[] fields = pojoType.getDeclaredFields();
		for (Field f : fields) {
			ORMColumn ormc = f.getAnnotation(ORMColumn.class);
			if (ormc != null) {
				if (usePhysicalPk) {
					if (ormc.physicalPkFld()) {
						f.setAccessible(true);
						delArgs.put(f.getName(), pkVal);
					}
				} else {
					if (ormc.LogicPkFld()) {
						f.setAccessible(true);
						delArgs.put(f.getName(), pkVal);
					}
				}
				
			}
		}
		return delArgs;
	}
	
	/**
	 * 拼装删除参数
	 * @param pojo							pojo
	 * @param usePhysicalPk					是否物理主键
	 * @return								参数Map
	 * @throws IllegalArgumentException		参数异常
	 * @throws IllegalAccessException		参数异常
	 */
	private Map<String, Object> fieldArgsMap(Object pojo, boolean usePhysicalPk) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> delArgs = new HashMap<String, Object>();
		Field[] fields = pojo.getClass().getDeclaredFields();
		for (Field f : fields) {
			ORMColumn ormc = f.getAnnotation(ORMColumn.class);
			if (ormc != null) {
				if (usePhysicalPk) {
					if (ormc.physicalPkFld()) {
						f.setAccessible(true);
						delArgs.put(f.getName(), f.get(pojo));
					}
				} else {
					if (ormc.LogicPkFld()) {
						f.setAccessible(true);
						delArgs.put(f.getName(), f.get(pojo));
					}
				}
				
			}
		}
		return delArgs;
	}
	
	/**
	 * 生成SQL
	 * @throws IllegalAccessException 		exception
	 * @throws IllegalArgumentException 	exception
	 * @throws SQLException 				exception
	 * @throws SecurityException 			exception
	 * @throws NoSuchFieldException 		exception
	 */
	private void generate() throws IllegalArgumentException, IllegalAccessException, SQLException, NoSuchFieldException, SecurityException {
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
		
		String tableName = null;
		if (ormHashTable == null) {
			tableName = ormTable.tableName();
		} else {
			String hashKey = this.pojoClazz.getDeclaredField(ormHashTable.hashRefColumn()).getName();
			long hashCol = Long.valueOf(String.valueOf(this.delArgs.get(hashKey)));
			tableName = DBUtils.getInstance().findHashTableName(ormHashTable, ormTable, hashCol);
		}
		
		generate(tableName);
	}
	
	/**
	 * 生成主键依赖的
	 * @param tableName						表名
	 * @throws IllegalAccessException 		exception
	 * @throws IllegalArgumentException 	exception
	 * @throws SQLException 				exception
	 */
	private void generate(String tableName) throws IllegalArgumentException, IllegalAccessException, SQLException {
		StringBuilder del = new StringBuilder("DELETE FROM ");
		List<Object> params = new ArrayList<Object>();
		
		del.append(tableName).append(" ");
		// DELETE-WHERE
		Field[] fields = pojoClazz.getDeclaredFields();
		
		// WHERE
		if (delArgs != null && !delArgs.isEmpty()) {
			del.append("WHERE ");
			for (String pk : delArgs.keySet()) {
				for (Field field : fields) {
					field.setAccessible(true);
					ORMColumn ormc = field.getAnnotation(ORMColumn.class);
					if (ormc != null && field.getName().equalsIgnoreCase(pk)) {
						// 是主键，作为条件
						del.append(ormc.value()).append("=? AND ");
						Object val = delArgs.get(pk);
						params.add(val);
					}
				}
			}
			del.replace(del.length() - 4, del.length(), " ");
		} else {
			// 主键为空
			throw new SQLException("del args key is null!");
		}
		this.sql = del.toString();
		
		// params
		args = params.toArray();
	}
	
	/**
	 * 生成sql
	 * @return the sql
	 * @throws IllegalAccessException 		exception
	 * @throws IllegalArgumentException 	exception
	 * @throws SQLException 				exception
	 * @throws SecurityException 			exception
	 * @throws NoSuchFieldException 		exception
	 */
	public String getSql() throws IllegalArgumentException, IllegalAccessException, SQLException, NoSuchFieldException, SecurityException {
		if (sql == null) {
			generate();
		}
		return sql;
	}

	/**
	 * 生成sql对应的参数
	 * @return the args
	 * @throws IllegalAccessException		exception 
	 * @throws IllegalArgumentException 	exception
	 * @throws SQLException 				exception
	 * @throws SecurityException 			exception
	 * @throws NoSuchFieldException 		exception
	 */
	public Object[] getArgs() throws IllegalArgumentException, IllegalAccessException, SQLException, NoSuchFieldException, SecurityException {
		if (args == null) {
			generate();
		}
		return args;
	}

}
