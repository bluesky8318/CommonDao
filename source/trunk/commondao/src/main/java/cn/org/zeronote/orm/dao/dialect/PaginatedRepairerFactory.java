/**
 * 
 */
package cn.org.zeronote.orm.dao.dialect;

import cn.org.zeronote.orm.DataAccessException;

/**
 * @author leon
 *
 */
public final class PaginatedRepairerFactory {

	/**
	 * 获取指定实例
	 * @param dbType		db type
	 * @return				paginated repairer
	 */
	public static IPaginatedRepairer getInstance(DBType dbType) {
		switch (dbType) {
		case MSSQL2005PLUS:
			return new MSSqlServerRowNumberPaginatedRepairer();
		case MYSQL:
			return new MySQLPaginatedRepairer();
		case ORACLE:
			return new OraclePaginatedRepairer();
		default:
			throw new DataAccessException("not support dbtype:" + dbType);
		}
	}
}
