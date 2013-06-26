/**
 * 
 */
package cn.org.zeronote.orm.dao.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.zeronote.orm.DataAccessException;

/**
 * 参数转换生成类
 * <p>
 * 将 :字段名 方式的sql转换为?方式的sql与参数
 * </p>
 * @author leon
 *
 */
public class ParamTransformGenerator implements Generator {

	private static final String PATTERN = ":.+?\\b";
	
	private String oraSql;
	private Map<String, Object> params;
	
	private String sql;
	private Object[] args;
	
	/**
	 * 
	 */
	public ParamTransformGenerator(String sql, Map<String, Object> params) {
		this.oraSql = sql;
		this.params = params;
	}

	/**
	 * 执行生成
	 */
	public void generate() throws DataAccessException {
		// 将 :字段名 方式，转换为?方式
		if (oraSql == null || "".equalsIgnoreCase(oraSql.trim())) {
			throw new DataAccessException("SQL is null");
		}

		List<Object> arglist = new ArrayList<Object>();
		
		Pattern p = Pattern.compile(PATTERN);
		Matcher m = p.matcher(oraSql);
		while (m.find()) {
			String key = m.group();
			if (!params.containsKey(key.substring(1))) {
				// 参数列表中不包含该key
				throw new DataAccessException("Params not contains key~" + key + ";");
			}
			arglist.add(params.get(key.substring(1)));	// 添加参数列表
		}
		
		sql = oraSql.replaceAll(PATTERN, "?");
		args = arglist.toArray();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSql() {
		if (sql == null) {
			generate();
		}
		return sql;
	}

	/**
	 * 
	 * @return
	 */
	public Object[] getArgs() {
		if (args == null) {
			generate();
		}
		return args;
	}
}
