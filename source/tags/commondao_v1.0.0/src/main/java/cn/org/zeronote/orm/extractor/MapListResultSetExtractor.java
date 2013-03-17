/**
 * 
 */
package cn.org.zeronote.orm.extractor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.org.zeronote.orm.dao.ResultSetExtractor;


/**
 * MapList装载器
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class MapListResultSetExtractor implements ResultSetExtractor<List<Map<String,Object>>> {
	
	/**
	 * 
	 */
	public MapListResultSetExtractor() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.orm.ResultSetExtractor#extractData(java.sql.ResultSet)
	 */
	@Override
	public List<Map<String,Object>> handle(ResultSet rs) throws SQLException {
		ResultSetMetaData rm = rs.getMetaData();
		int ccount = rm.getColumnCount();
		
		List<Map<String,Object>> mapls = new ArrayList<Map<String,Object>>();
		while (rs.next()) {
			Map<String,Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= ccount; i++) {
				String label = rm.getColumnLabel(i);
				Object obj = rs.getObject(label);
				map.put(label, obj);
			}
			mapls.add(map);
		}
		return mapls;
	}

}
