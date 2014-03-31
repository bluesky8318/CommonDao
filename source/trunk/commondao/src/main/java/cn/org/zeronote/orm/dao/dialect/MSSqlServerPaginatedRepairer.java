/**
 * 
 */
package cn.org.zeronote.orm.dao.dialect;

import java.sql.SQLException;

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
 * <br>
 * 基于内存的分页方式，对大数据处理不适用，这里只保留，不使用 
 * <br>
 * 此方法对sqlserver有效，但时常出错，指针异常
 * @author <a href='mailto:lizheng8318@gmail.com'>leon</a>
 *
 */
@Deprecated
public class MSSqlServerPaginatedRepairer extends AbstractPaginatedRepairer {

	private static Logger logger = LoggerFactory.getLogger(MSSqlServerPaginatedRepairer.class);
	
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
	 * @param <T>					pojo class
	 * @param dataSource			数据源
	 * @param sql					查询语句
	 * @param args					查询参数
	 * @param resultSetExtractor	结果集装配
	 * @return						实际结果
	 * @throws DataAccessException	数据访问异常
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
}
