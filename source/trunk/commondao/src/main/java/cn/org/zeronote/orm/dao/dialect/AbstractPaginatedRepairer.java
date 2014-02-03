/**
 * 
 */
package cn.org.zeronote.orm.dao.dialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;


/**
 * 分页转换器
 * @author <a href='mailto:lizheng8318@gmail.com'>leon</a>
 *
 */
public abstract class AbstractPaginatedRepairer implements IPaginatedRepairer {

	/** 查询器 */
	private QueryRunner queryRunner;
	
	/**
	 * 
	 */
	public AbstractPaginatedRepairer() {
	}

	/**
	 * 支持游标滚动的查询
	 * @return
	 */
	protected QueryRunner getPaginatedQueryRunner(DataSource dataSource) {
		if (queryRunner == null) {
			queryRunner = new QueryRunner(dataSource) {

				@Override
				protected PreparedStatement prepareStatement(Connection conn,
						String sql) throws SQLException {
					return conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				}
				
			};
		}
		return queryRunner;
	}
	
	/**
	 * 处理数据类型
	 * @param args
	 * @return
	 */
	protected Object[] pearParams(Object[] args) {
		Object[] nArgs = new Object[args.length];
		for (int i = 0; i < nArgs.length; i++) {
			Object o = args[i];
			if (o instanceof Date) {
				// 日期类型
				Date d = (Date) o;
				o = new java.sql.Timestamp(d.getTime());
			}
			nArgs[i] = o;
		}
		return nArgs;
	}
	
}
