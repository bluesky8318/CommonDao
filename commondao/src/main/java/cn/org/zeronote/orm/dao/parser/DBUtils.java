package cn.org.zeronote.orm.dao.parser;

import java.lang.reflect.Field;

import cn.org.zeronote.orm.ORMHash;
import cn.org.zeronote.orm.ORMTable;

/**
 * 数据库相关工具类
 * <p>
 * 类似计算表名hash值等
 * </p>
 * @author zheng.li
 *
 */
public class DBUtils {

	private static DBUtils instance;
	
	/**
	 * 
	 */
	public DBUtils() {
	}
	
    /**
     * 计算hash值
     * @param id
     * @param size
     * @return
     */
    public String hash(long id, int size) {
        if (size < 1) {
            size = 1;
        }
        long i = id % size;
        while (i < 0) {
            i += size;
        }
        if (i < 10) {
            return "0" + i;
        } else {
            return "" + i;
        }
    }
    
	/**
	 * 获取hash table的表名
	 * @param hash
	 * @param table
	 * @param pojo
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	String getHashTableName(ORMHash hash, ORMTable table, Object pojo) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Class<?> pojoClazz = pojo.getClass();
		String tableName = table.tableName();
		Field f = pojoClazz.getDeclaredField(hash.hashRefColumn());
		f.setAccessible(true);
		Object id = f.get(pojo);
		tableName += ("_" + DBUtils.getInstance().hash(Long.valueOf(String.valueOf(id)), hash.hashSize()));
		return tableName;
	}

	/**
	 * 查找hash table表名
	 * @param hash
	 * @param table
	 * @param colValue
	 * @return
	 */
	String findHashTableName(ORMHash hash, ORMTable table, long colValue) {
		String tableName = table.tableName();
		tableName += ("_" + DBUtils.getInstance().hash(colValue, hash.hashSize()));
		return tableName;
	}
    
	/**
	 * 获取单例实例
	 * @return
	 */
	public static DBUtils getInstance() {
		if (instance == null) {
			instance = new DBUtils();
		}
		return instance;
	}

}
