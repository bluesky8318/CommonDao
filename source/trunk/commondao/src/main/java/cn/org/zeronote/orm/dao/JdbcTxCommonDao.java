/**
 * 
 */
package cn.org.zeronote.orm.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * 支持spring jdbc 事务的commondao实现
 * @author leon
 *
 */
public class JdbcTxCommonDao extends DefaultCommonDao {

	/**
	 * 
	 */
	public JdbcTxCommonDao() {
	}

	/**
	 * 添加支持jdbc datasource Transaction 支持的QueryRunner处理方式
	 */
	@Override
	protected QueryRunner getQueryRunner() {
		
		return new QueryRunner(dataSource){

			/* (non-Javadoc)
			 * @see org.apache.commons.dbutils.AbstractQueryRunner#prepareConnection()
			 */
			@Override
			protected Connection prepareConnection() throws SQLException {
				return DataSourceUtils.getConnection(this.getDataSource());
			}

			/* (non-Javadoc)
			 * @see org.apache.commons.dbutils.AbstractQueryRunner#close(java.sql.Connection)
			 */
			@Override
			protected void close(Connection conn) throws SQLException {
				DataSourceUtils.releaseConnection(conn, getDataSource());
			}
			
		};
	}

	/**
	 * 添加支持jdbc datasource Transaction 支持的Connection获取
	 */
	@Override
	protected Connection getConnection() throws SQLException {
		return DataSourceUtils.getConnection(dataSource);
	}

	/**
	 * 添加支持jdbc datasource Transaction 支持的Connection关闭
	 */
	@Override
	protected void close(Connection conn) throws SQLException {
		DataSourceUtils.releaseConnection(conn, dataSource);
	}
}
