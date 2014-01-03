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

import cn.org.zeronote.orm.DataAccessException;
import cn.org.zeronote.orm.PaginationSupport;
import cn.org.zeronote.orm.RowSelection;
import cn.org.zeronote.orm.extractor.PaginationPojoListResultSetExtractor;

/**
 * MS Sql Server 分页查询器
 * @author <a href='mailto:lizheng8318@gmail.com'>leon</a>
 *
 */
public class MSSqlServerPaginatedRepairer implements IPaginatedRepairer {

	private static Logger logger = LoggerFactory.getLogger(MSSqlServerPaginatedRepairer.class);
	
	/** 查询器 */
	private QueryRunner queryRunner;
	
	/**
	 * 
	 */
	public MSSqlServerPaginatedRepairer() {
	}

	/* (non-Javadoc)
	 * @see cn.org.zeronote.orm.dao.dialect.IPaginatedRepairer#queryForPaginatedPojoList(java.lang.String, java.lang.Object[], java.lang.Class, cn.org.zeronote.orm.RowSelection)
	 */
	@Override
	public <T> PaginationSupport<T> queryForPaginatedPojoList(DataSource dataSource, String sql,
			Object[] args, Class<T> pojoType, RowSelection rowSelection)
			throws DataAccessException {
		return (PaginationSupport<T>) queryPaginated(dataSource, sql, args, new PaginationPojoListResultSetExtractor<T>(pojoType, rowSelection));
	}

	
	/**
	 * 分页查询
	 * 与正常查询不一致，需要更多的操作
	 * @param sql
	 * @param args
	 * @param resultSetExtractor
	 * @return
	 * @throws DataAccessException
	 */
	protected <T> T queryPaginated(DataSource dataSource, String sql, Object[] args, ResultSetHandler<T> resultSetExtractor) throws DataAccessException {
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
	 * @return
	 */
	protected QueryRunner getPaginatedQueryRunner(DataSource dataSource) {
		if (queryRunner == null) {
			queryRunner = new QueryRunner(dataSource) {

				@Override
				protected PreparedStatement prepareStatement(Connection conn,
						String sql) throws SQLException {
					return conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
	private Object[] pearParams(Object[] args) {
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
