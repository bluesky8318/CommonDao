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
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.NumberUtils;

import cn.org.zeronote.orm.DataAccessException;


/**
 * 分页转换器
 * @author <a href='mailto:lizheng8318@gmail.com'>leon</a>
 *
 */
public abstract class AbstractPaginatedRepairer implements IPaginatedRepairer {

	private static Logger logger = LoggerFactory.getLogger(AbstractPaginatedRepairer.class);
	
	/** 查询器 */
	private QueryRunner queryRunner;
	
	/**
	 * 
	 */
	public AbstractPaginatedRepairer() {
	}

	/**
	 * 实际查询
	 * @param <T>					结果类型
	 * @param dataSource			数据源
	 * @param sql					查询语句
	 * @param args					查询参数
	 * @param resultSetExtractor	结果转换器
	 * @return						查询结果
	 * @throws DataAccessException	数据访问异常
	 */
	protected <T> T query(DataSource dataSource, String sql, Object[] args, ResultSetHandler<T> resultSetExtractor) throws DataAccessException {
		QueryRunner qr = getPaginatedQueryRunner(dataSource);
		logger.debug("Query SQL:{}", sql);
		try {
			return qr.query(sql, resultSetExtractor, pearParams(args));
		} catch (SQLException e) {
			throw new DataAccessException("Query error!", e);
		}
	}
	
	/**
	 * 支持游标滚动的查询
	 * @param dataSource	数据源
	 * @return		query runner
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
	 * @param args	转换前参数
	 * @return		转换后参数
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
	
	@SuppressWarnings("unchecked")
	protected Object convertValueToRequiredType(Object value, Class<?> requiredType) {
		if (value == null) {
			return null;
		}
		if (String.class.equals(requiredType)) {
			return String.valueOf(value);
		}
		else if (Number.class.isAssignableFrom(requiredType)) {
			if (value instanceof Number) {
				// Convert original Number to target Number class.
				return NumberUtils.convertNumberToTargetClass(((Number) value), (Class<Number>)requiredType);
			}
			else {
				// Convert stringified value to target Number class.
				return NumberUtils.parseNumber(value.toString(), (Class<Number>)requiredType);
			}
		}
		else {
			throw new IllegalArgumentException(
					"Value [" + value + "] is of type [" + value.getClass().getName() +
					"] and cannot be converted to required type [" + requiredType.getName() + "]");
		}
	}
}
