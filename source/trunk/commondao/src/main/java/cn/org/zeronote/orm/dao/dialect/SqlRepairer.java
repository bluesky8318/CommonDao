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
	
	/**自增主键查询*/
	private static Map<DBType, SelectKey> selectKeyMap = new HashMap<DBType, SelectKey>();
	static {
		selectKeyMap.put(DBType.MYSQL, new SelectKey("ID", "SELECT @@IDENTITY AS ID"));
		selectKeyMap.put(DBType.MSSQL2005PLUS, new SelectKey("ID", "SELECT SCOPE_IDENTITY() AS ID"));
		selectKeyMap.put(DBType.ORACLE, null);	// oracle 要使用SEQUENCE来完成自增主键，这里暂时不提供oracle的获取方法，当oracle时，GetSelectKey都返回null，主键不被赋值
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
