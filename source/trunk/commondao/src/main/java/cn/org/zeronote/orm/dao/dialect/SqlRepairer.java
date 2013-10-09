/**
 * 
 */
package cn.org.zeronote.orm.dao.dialect;

import java.util.HashMap;
import java.util.Map;

import cn.org.zeronote.orm.dao.SelectKey;

/**
 * 数据库适配
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class SqlRepairer {

	/**
	 * 数据库类型
	 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
	 *
	 */
	public static enum DBType {
		/** mysql */
		MYSQL,
		/** ms sql server */
		MSSQL;
	}
	
	/**自增主键查询*/
	private static Map<DBType, SelectKey> selectKeyMap = new HashMap<DBType, SelectKey>();
	static {
		selectKeyMap.put(DBType.MYSQL, new SelectKey("ID", "SELECT @@IDENTITY AS ID"));
		selectKeyMap.put(DBType.MSSQL, new SelectKey("ID", "SELECT @@IDENTITY AS ID"));
	}
	
	/**
	 * 
	 */
	public SqlRepairer() {
	}

	/**
	 * @return the selectKey
	 */
	public static SelectKey getSelectKey(DBType type) {
		return selectKeyMap.get(type);
	}
}
