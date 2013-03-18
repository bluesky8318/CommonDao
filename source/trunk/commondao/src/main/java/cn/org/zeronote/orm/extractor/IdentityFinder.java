/**
 * 
 */
package cn.org.zeronote.orm.extractor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.util.NumberUtils;

import cn.org.zeronote.orm.ORMColumn;
import cn.org.zeronote.orm.dao.SelectKey;


/**
 * 自增主键查找器
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class IdentityFinder {

	private Connection conn;
	private SelectKey selectKey;
	
	/**
	 * 
	 */
	public IdentityFinder(Connection conn, SelectKey selectKey) {
		this.conn = conn;
		this.selectKey = selectKey;
	}
	
	/**
	 * 查找自增主键并填充到队列中
	 * @param objs
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException 
	 */
	public void find(Object... objs) throws IllegalArgumentException, IllegalAccessException, SQLException {
		Class<?> clz = objs[0].getClass();
		Field[] fields = clz.getDeclaredFields();
		for (Field field : fields) {
			ORMColumn oc = field.getAnnotation(ORMColumn.class);
			if (oc != null && oc.physicalPkFld() && oc.autoIncrement()) {
				// 找到最后一个自增id
				QueryRunner qr = new QueryRunner();
				Object identityVal = qr.query(conn, selectKey.getQuery(), new ScalarHandler<Object>());
				Long identity = NumberUtils.parseNumber(identityVal.toString(), Long.class);
				
				field.setAccessible(true);
				// 填充到所有当中
				for (int i = objs.length - 1; i >= 0; i--) {
					if (field.get(objs[i]) == null) {	// 判null，要求主键都是包装类
						// 类型转换
						@SuppressWarnings("unchecked")
						Object val = NumberUtils.convertNumberToTargetClass(identity, ((Class<Number>)field.getType()));
						field.set(objs[i], val);
						identity--;
					}
				}
				break;
			}
		}
	}

}
