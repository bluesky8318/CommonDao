/**
 * 
 */
package cn.org.zeronote.orm.dao.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.org.zeronote.orm.DataAccessException;
import cn.org.zeronote.orm.ORMAutoAssemble;
import cn.org.zeronote.orm.ORMCanUpdate;
import cn.org.zeronote.orm.ORMColumn;
import cn.org.zeronote.orm.ORMHash;
import cn.org.zeronote.orm.ORMTable;

/**
 * UPDATE sql 语句生成器
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class SqlUpdGenerator {
	
	private static Logger logger = LoggerFactory.getLogger(SqlUpdGenerator.class);
	
	/** orm fields cache*/
	private transient static Map<Class<?>, Field> updateCheckCache = new HashMap<Class<?>, Field>();
	private transient byte[] synObj = new byte[0];
	
	private Object pojo;
	
	private String sql;
	private Object[] args;
	
	private boolean usePhysicalPk;
	
	/**
	 * 
	 */
	public SqlUpdGenerator() {
	}
	
	/**
	 * 
	 * @param pojoClazz
	 */
	public SqlUpdGenerator(Object pojo, boolean usePhysicalPk) throws DataAccessException{
		this.pojo = pojo;
		this.usePhysicalPk = usePhysicalPk;
		if (!updateCheckCache.containsKey(pojo.getClass())) {
			synchronized (synObj) {
				if (!updateCheckCache.containsKey(pojo.getClass())) {
					boolean add = false;
					Field[] fields = pojo.getClass().getDeclaredFields();
					for (Field field : fields) {
						ORMCanUpdate ocu = field.getAnnotation(ORMCanUpdate.class);
						if (ocu != null) {
							field.setAccessible(true);
							updateCheckCache.put(pojo.getClass(), field);
							add = true;
							break;
						}
					}
					if (!add) {
						updateCheckCache.put(pojo.getClass(), null);
					}
				}
			}
		}
		Field field = updateCheckCache.get(pojo.getClass());
		if (field != null) {
			field.setAccessible(true);
			try {
				boolean c = field.getBoolean(pojo);
				if (!c) {
					logger.debug("POJO Do not allow update");
					throw new DataAccessException("POJO Do not allow update");
				}
			} catch (IllegalArgumentException e) {
				throw new DataAccessException("read can update field fail, field:" + field.getName(), e);
			} catch (IllegalAccessException e) {
				throw new DataAccessException("read can update field fail, field:" + field.getName(), e);
			}
		}
		
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
	 * 生成sql
	 * @param tableName
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void generate(String tableName) throws IllegalArgumentException, IllegalAccessException {

		Class<?> pojoClazz = pojo.getClass();
		StringBuilder upd = new StringBuilder("UPDATE ");
		List<Object> params = new ArrayList<Object>();
		// TABLE
		upd.append(tableName).append(" ");
		// UPDATE
		upd.append("SET ");
		Field[] fields = pojoClazz.getDeclaredFields();
		List<String> pks = null;
		if (usePhysicalPk) {
			pks = new ArrayList<String>();
			for (Field f : fields) {
				ORMColumn ormc = f.getAnnotation(ORMColumn.class);
				if (ormc != null && ormc.physicalPkFld()) {
					pks.add(ormc.value());
				}
			}
		} else {
			pks = new ArrayList<String>();
			for (Field f : fields) {
				ORMColumn ormc = f.getAnnotation(ORMColumn.class);
				if (ormc != null && ormc.LogicPkFld()) {
					pks.add(ormc.value());
				}
			}
		}
		
		for (Field field : fields) {
			field.setAccessible(true);
			ORMColumn ormc = field.getAnnotation(ORMColumn.class);
			// 主键不允许修改
			if (ormc != null && !pks.contains(ormc.value())) {
				Object val = field.get(pojo);
				if (val != null) {
					// 不是空才去设置
					if (ormc.append()) {
						upd.append(ormc.value()).append("=").append(ormc.value()).append("||?, ");	// XXX 只适合Oracle，需要方言支持
					} else {
						upd.append(ormc.value()).append("=?, ");
					}
					params.add(val);
				} else if (!"".equalsIgnoreCase(ormc.defaultValue())) {
					// 是空，但是有默认值，则设置
					switch (ormc.defaultValueScope()) {
					case ALL:
					case UPDATE:
						if (ormc.append()) {
							upd.append(ormc.value()).append("=").append(ormc.value()).append("||").append(ormc.defaultValue()).append(", ");	// XXX 只适合Oracle，需要方言支持
						} else {
							upd.append(ormc.value()).append("=").append(ormc.defaultValue()).append(", ");
						}
						break;
					default:
						break;
					}
				}
			}
		}
		upd.replace(upd.length() - 2, upd.length(), " ");
		// WHERE
		if (pks != null && pks.size() > 0) {
			upd.append("WHERE ");
			for (String pk : pks) {
				for (Field field : fields) {
					field.setAccessible(true);
					ORMColumn ormc = field.getAnnotation(ORMColumn.class);
					if (ormc != null && ormc.value().equalsIgnoreCase(pk)) {
						// 是主键，作为条件
						upd.append(pk).append("=? AND ");
						Object val = field.get(pojo);
						params.add(val);
					}
				}
			}
			upd.replace(upd.length() - 4, upd.length(), " ");
		}
		this.sql = upd.toString();
		
		// params
		args = params.toArray();
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
		if (args == null) {
			generate();
		}
		return args;
	}

}
