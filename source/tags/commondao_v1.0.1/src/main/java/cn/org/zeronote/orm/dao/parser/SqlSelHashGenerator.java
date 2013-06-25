/**
 * 
 */
package cn.org.zeronote.orm.dao.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.org.zeronote.orm.ORMAutoAssemble;
import cn.org.zeronote.orm.ORMHash;
import cn.org.zeronote.orm.ORMTable;

/**
 * Orm sql语句生成
 * <p>针对表散列进行特殊处理，要生成多条sql</p>
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class SqlSelHashGenerator extends SqlSelGenerator{
	
	private List<String> sqls;
	private List<Object[]> argsObjs;
	
	/**
	 * 
	 */
	public SqlSelHashGenerator() {
	}

	/**
	 * 
	 * @param pojoClazz
	 * @param args
	 */
	public SqlSelHashGenerator(Class<?> pojoClazz, String[] requireFields, Map<String, Object[]> args) {
		super(pojoClazz, requireFields, args);
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
		ORMHash ormHashTable = pojoClazz.getAnnotation(ORMHash.class);
		
		String refCol = ormHashTable.hashRefColumn();

		Map<String, Map<String, List<Object>>> tableArgsMap = new HashMap<String, Map<String,List<Object>>>();
		// 装填 散列列
		for (String colKey : argsMap.keySet()) {
			if (colKey.equals(refCol)) {
				// 参照列拆分
				Object[] args = argsMap.get(colKey);
				for (Object colValue : args) {
					String tableName = DBUtils.getInstance().findHashTableName(ormHashTable, ormTable, Long.valueOf(String.valueOf(colValue)));
					if (tableArgsMap.get(tableName) == null) {
						Map<String, List<Object>> m = new HashMap<String, List<Object>>();
						List<Object> l = new ArrayList<Object>();
						l.add(colValue);
						m.put(colKey, l);
						tableArgsMap.put(tableName, m);
					} else {
						tableArgsMap.get(tableName).get(colKey).add(colValue);
					}
				}
				break;
			}
		}
		// 装填其它参数
		for (String colKey : argsMap.keySet()) {
			if (colKey.equals(refCol)) {
				// 这个参数已经拆分完毕
				continue;
			}
			for (Map<String, List<Object>> map : tableArgsMap.values()) {
				map.put(colKey, Arrays.asList(argsMap.get(colKey)));
			}
		}
		
		String[] orders = ormTable.order();
		
		this.sqls = new ArrayList<String>();
		this.argsObjs = new ArrayList<Object[]>();
		for (String tableName : tableArgsMap.keySet()) {
			List<Object> params = new ArrayList<Object>();
			
			Map<String, List<Object>> args = tableArgsMap.get(tableName);
			Map<String, Object[]> rArgs = new HashMap<String, Object[]>();
			for (String arg : args.keySet()) {
				rArgs.put(arg, args.get(arg).toArray());
			}
			this.sqls.add(genSql(params, tableName, rArgs, orders));
			// params
			this.argsObjs.add(params.toArray());
		}
		this.sql = this.sqls.get(0);
		this.args = this.argsObjs.get(0);
	}
	
	/**
	 * 相关参数
	 * @return
	 * @throws IllegalAccessException 
	 */
	public List<Object[]> getArgsObjs() throws IllegalAccessException {
		if (argsObjs == null) {
			generateParam();
		}
		return argsObjs;
	}
	
	/**
	 * Sql语句
	 * @return the sql
	 * @throws IllegalAccessException 
	 */
	public String[] getSqls() throws IllegalAccessException {
		if (sqls == null) {
			generateParam();
		}
		return sqls.toArray(new String[0]);
	}

}
