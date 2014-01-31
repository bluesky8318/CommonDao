/**
 * 
 */
package cn.org.zeronote.orm.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.NumberUtils;

import cn.org.zeronote.orm.DataAccessException;
import cn.org.zeronote.orm.ICommonDao;
import cn.org.zeronote.orm.ORMAutoAssemble;
import cn.org.zeronote.orm.ORMHash;
import cn.org.zeronote.orm.PaginationSupport;
import cn.org.zeronote.orm.RowSelection;
import cn.org.zeronote.orm.dao.dialect.DBType;
import cn.org.zeronote.orm.dao.dialect.PaginatedRepairerFactory;
import cn.org.zeronote.orm.dao.dialect.SqlRepairer;
import cn.org.zeronote.orm.dao.parser.ParamTransformGenerator;
import cn.org.zeronote.orm.dao.parser.SqlDelGenerator;
import cn.org.zeronote.orm.dao.parser.SqlInsGenerator;
import cn.org.zeronote.orm.dao.parser.SqlSelGenerator;
import cn.org.zeronote.orm.dao.parser.SqlSelHashGenerator;
import cn.org.zeronote.orm.dao.parser.SqlUpdGenerator;
import cn.org.zeronote.orm.extractor.IdentityFinder;
import cn.org.zeronote.orm.extractor.MapListResultSetExtractor;
import cn.org.zeronote.orm.extractor.PojoListResultSetExtractor;
import cn.org.zeronote.orm.extractor.PojoResultSetExtractor;
import cn.org.zeronote.orm.extractor.SimpListResultSetExtractor;



/**
 * 默认ICommonDao实现
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class DefaultCommonDao implements ICommonDao {

	private static Logger logger = LoggerFactory.getLogger(DefaultCommonDao.class);
	
	private Object synObj = new Object();
	
	/**数据源*/
	protected DataSource dataSource;
	
	/**数据库类型*/
	protected DBType dbType;
	
	
	private QueryRunner queryRunner;
	
	/**
	 * 
	 */
	public DefaultCommonDao() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.ailk.keel.core.database.common.ICommonDao#queryForPaginatedPojoList(java.lang.String, java.lang.Object[], java.lang.Class, com.ailk.keel.core.database.common.RowSelection)
	 */
	@Override
	public <T> PaginationSupport<T> queryForPaginatedPojoList(String sql,
			Object[] args, Class<T> pojoType, RowSelection rowSelection)
			throws DataAccessException {
		// 分页查询
		return PaginatedRepairerFactory.getInstance(dbType).queryForPaginatedPojoList(dataSource, sql, args, pojoType, rowSelection);
	}
	
	/*
	 * (non-Javadoc)
	 * @see cn.org.zeronote.orm.ICommonDao#queryForPaginatedPojoList(java.lang.Class, java.util.Map, cn.org.zeronote.orm.RowSelection)
	 */
	@Override
	public <T> PaginationSupport<T> queryForPaginatedPojoList(
			Class<T> pojoType, Map<String, Object> args,
			RowSelection rowSelection) throws DataAccessException {
		ORMHash orma = pojoType.getAnnotation(ORMHash.class);
		if (orma != null) {
			// 散列表，暂不支持分页查询
			throw new DataAccessException("pagination select not support hash table");
		}
		
		Map<String, Object[]> nArgs = new HashMap<String, Object[]>();
		for (String key : args.keySet()) {
			nArgs.put(key, new Object[]{args.get(key)});
		}
		SqlSelGenerator sqlGenerator = new SqlSelGenerator(pojoType, nArgs);
		try {
			String sql = sqlGenerator.getSql();
			Object[] argsObj = sqlGenerator.getArgs();
			return queryForPaginatedPojoList(sql, argsObj, pojoType, rowSelection);
		} catch (IllegalAccessException e) {
			logger.error("init sql error!", e);
			throw new DataAccessException("init sql error!", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.ICommonDao#queryForMapList(java.lang.String, java.lang.Object[])
	 */
	@Override
	public List<Map<String, Object>> queryForMapList(String sql, Object... args)
			throws DataAccessException {
		return (List<Map<String, Object>>) query(sql, args, new MapListResultSetExtractor());
	}
	
	/*
	 * (non-Javadoc)
	 * @see cn.org.zeronote.orm.ICommonDao#queryForMapList(java.lang.String, java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> queryForMapList(String sql, Map<String, Object> params) 
			throws DataAccessException {
		logger.debug("Query Ora SQL:{}", sql);
		// SQL变换
		ParamTransformGenerator generator = new ParamTransformGenerator(sql, params);
		generator.generate();
		String sql2 = generator.getSql();	// 将:字段名方式，整理成?方式
		Object[] args = generator.getArgs();
		return queryForMapList(sql2, args);
	}

	/* (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.ICommonDao#queryForPojoList(java.lang.String, java.lang.Object[], java.lang.Class)
	 */
	@Override
	public <T> List<T> queryForPojoList(String sql, Object[] args,
			Class<T> pojoType) throws DataAccessException {
		return (List<T>) query(sql, args, new PojoListResultSetExtractor<T>(pojoType));
	}

	/*
	 * (non-Javadoc)
	 * @see cn.org.zeronote.orm.ICommonDao#queryForPojoList(java.lang.String, java.util.Map, java.lang.Class)
	 */
	@Override
	public <T> List<T> queryForPojoList(String sql, Map<String, Object> params,
			Class<T> pojoType) throws DataAccessException {
		logger.debug("Query Ora SQL:{}", sql);
		// SQL变换
		ParamTransformGenerator generator = new ParamTransformGenerator(sql, params);
		generator.generate();
		String sql2 = generator.getSql();	// 将:字段名方式，整理成?方式
		Object[] args = generator.getArgs();
		return queryForPojoList(sql2, args, pojoType);
	}
	
	/* (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.ICommonDao#queryForPojoList(java.lang.Class, java.lang.Object[])
	 */
	@Override
	public <T> List<T> queryForPojoList(Class<T> pojoType, Map<String, Object> args)
			throws DataAccessException {
		Map<String, Object[]> argsMap = new HashMap<String, Object[]>();
		for (String key : args.keySet()) {
			argsMap.put(key, new Object[]{args.get(key)});
		}
		
		return queryForPojoList0(pojoType, argsMap);
	}
	
	/*
	 * (non-Javadoc)
	 * @see cn.org.zeronote.orm.ICommonDao#queryForPojoList(java.lang.Class, java.lang.String, java.lang.Object[])
	 */
	@Override
	public <T> List<T> queryForPojoList(Class<T> pojoType, String col, Object... args) throws DataAccessException {
		Map<String, Object[]> argsMap = new HashMap<String, Object[]>();
		argsMap.put(col, args);
		
		return queryForPojoList0(pojoType, argsMap);
	}

	/**
	 * 实现查询
	 * @param pojoType
	 * @param argsMap
	 * @return
	 */
	private <T> List<T> queryForPojoList0(Class<T> pojoType, Map<String, Object[]> argsMap) {
		String[] sqls = null;
		List<Object[]> argsObjs = null;
		if (pojoType.getAnnotation(ORMAutoAssemble.class) != null && pojoType.getAnnotation(ORMHash.class) != null) {
			// 处理散列表，分SQL查询
			SqlSelHashGenerator sqlGenerator = new SqlSelHashGenerator(pojoType, argsMap);
			try {
				sqls = sqlGenerator.getSqls();
				argsObjs = sqlGenerator.getArgsObjs();
			} catch (IllegalAccessException e) {
				logger.error("init query sql error!", e);
				throw new DataAccessException("init query sql error!", e);
			}
		} else {
			SqlSelGenerator sqlGenerator = new SqlSelGenerator(pojoType, argsMap);
			try {
				sqls = new String[]{sqlGenerator.getSql()};
				argsObjs = new ArrayList<Object[]>();
				argsObjs.add(sqlGenerator.getArgs());
			} catch (IllegalAccessException e) {
				logger.error("init query sql error!", e);
				throw new DataAccessException("init query sql error!", e);
			}
		}
		
		
		List<T> res = new ArrayList<T>();
		for (int i = 0; i < sqls.length; i++) {
			String sql = sqls[i];
			Object[] argsObj = argsObjs.get(i);
			List<T> ls = (List<T>) query(sql, argsObj, new PojoListResultSetExtractor<T>(pojoType));
			if (ls != null && !ls.isEmpty()) {
				res.addAll(ls);
			}
		}
		return res;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.ICommonDao#queryForSimpObject(java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T queryForSimpObject(String sql, Object[] args, Class<T> requiredType)
			throws DataAccessException {
		Object o = query(sql, args, new ScalarHandler<Object>());
		return (T) convertValueToRequiredType(o, requiredType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ailk.keel.core.database.common.ICommonDao#queryForSimpObjectList(java.lang.String, java.lang.Object[], java.lang.Class)
	 */
	@Override
	public <T> List<T> queryForSimpObjectList(String sql, Object[] args,
			Class<T> requiredType) throws DataAccessException {
		List<T> o = query(sql, args, new SimpListResultSetExtractor<T>(requiredType));
		return o;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.ICommonDao#queryForPojoOne(java.lang.Class, java.util.Map)
	 */
	@Override
	public <T> T queryForPojoOne(Class<T> pojoType, Map<String, Object> args)
			throws DataAccessException {
		Map<String, Object[]> argsMap = new HashMap<String, Object[]>();
		for (String key : args.keySet()) {
			argsMap.put(key, new Object[]{args.get(key)});
		}
		SqlSelGenerator sqlGenerator = new SqlSelGenerator(pojoType, argsMap);
		String sql;
		Object[] objArgs;
		try {
			sql = sqlGenerator.getSql();
			objArgs = sqlGenerator.getArgs();
		} catch (IllegalAccessException e) {
			logger.error("init query sql error!", e);
			throw new DataAccessException("init query sql error!", e);
		}
		return (T) query(sql, objArgs, new PojoResultSetExtractor<T>(pojoType));
	}

	/*
	 * (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.ICommonDao#queryCallback(java.lang.String, java.lang.Object[], com.ailk.aiip.apps.wasp.collect.common.orm.ResultSetExtractor)
	 */
	@Override
	public <T> T queryCallback(String sql, Object[] args, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException {
		return query(sql, args, resultSetExtractor);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.ICommonDao#update(java.lang.String, java.lang.Object[])
	 */
	@Override
	public int update(String sql, Object... args) throws DataAccessException {
		return execute(sql, args);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.ICommonDao#update(java.lang.Object[])
	 */
	@Override
	public int updateByLogic(Object... pojos) throws DataAccessException {
		int r = 0;
		for (Object obj : pojos) {
			SqlUpdGenerator sqlGenerator = new SqlUpdGenerator(obj, false);
			try {
				r += execute(sqlGenerator.getSql(), sqlGenerator.getArgs());
			} catch (IllegalArgumentException e) {
				logger.error("init update sql error! exec number : {}", r, e);
				throw new DataAccessException("init update sql error!", e);
			} catch (IllegalAccessException e) {
				logger.error("init update sql error! exec number : {}", r, e);
				throw new DataAccessException("init update sql error!", e);
			} catch (NoSuchFieldException e) {
				logger.error("init update sql error! exec number : {}", r, e);
				throw new DataAccessException("init update sql error!", e);
			} catch (SecurityException e) {
				logger.error("init update sql error! exec number : {}", r, e);
				throw new DataAccessException("init update sql error!", e);
			} catch (ParseException e) {
			    logger.error("init update sql error! exec number : {}", r, e);
                throw new DataAccessException("init update sql error!", e);
            }
		}
		return r;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ailk.keel.core.database.common.ICommonDao#updateByPhysical(java.lang.Object[])
	 */
	@Override
	public int updateByPhysical(Object... pojos) throws DataAccessException {
		int r = 0;
		for (Object obj : pojos) {
			SqlUpdGenerator sqlGenerator = new SqlUpdGenerator(obj, true);
			try {
				r += execute(sqlGenerator.getSql(), sqlGenerator.getArgs());
			} catch (IllegalArgumentException e) {
				logger.error("init update sql error! exec number : {}", r, e);
				throw new DataAccessException("init update sql error!", e);
			} catch (IllegalAccessException e) {
				logger.error("init update sql error! exec number : {}", r, e);
				throw new DataAccessException("init update sql error!", e);
			} catch (NoSuchFieldException e) {
				logger.error("init update sql error! exec number : {}", r, e);
				throw new DataAccessException("init update sql error!", e);
			} catch (SecurityException e) {
				logger.error("init update sql error! exec number : {}", r, e);
				throw new DataAccessException("init update sql error!", e);
			} catch (ParseException e) {
			    logger.error("init update sql error! exec number : {}", r, e);
                throw new DataAccessException("init update sql error!", e);
            }
		}
		return r;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ailk.keel.core.database.common.ICommonDao#deleteByLogic(java.lang.Object[])
	 */
	@Override
	public int deleteByLogic(Object... pojos) throws DataAccessException {
		// XXX 考虑批量
		int r = 0;
		for (Object obj : pojos) {
			
			try {
				SqlDelGenerator sqlGenerator = new SqlDelGenerator(obj, false);
				r += execute(sqlGenerator.getSql(), sqlGenerator.getArgs());
			} catch (IllegalArgumentException e) {
				logger.error("init delete sql error! exec number : {}", r, e);
				throw new DataAccessException("init delete sql error!", e);
			} catch (IllegalAccessException e) {
				logger.error("init delete sql error! exec number : {}", r, e);
				throw new DataAccessException("init delete sql error!", e);
			} catch (NoSuchFieldException e) {
				logger.error("init delete sql error! exec number : {}", r, e);
				throw new DataAccessException("init delete sql error!", e);
			} catch (SecurityException e) {
				logger.error("init delete sql error! exec number : {}", r, e);
				throw new DataAccessException("init delete sql error!", e);
			} catch (SQLException e) {
				logger.error("init delete sql error! exec number : {}", r, e);
				throw new DataAccessException("init delete sql error!", e);
			}
		}
		return r;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ailk.keel.core.database.common.ICommonDao#deleteByPhysical(java.lang.Object[])
	 */
	@Override
	public int deleteByPhysical(Object... pojos) throws DataAccessException {
		// XXX 考虑批量
		int r = 0;
		for (Object obj : pojos) {
			try {
				SqlDelGenerator sqlGenerator = new SqlDelGenerator(obj, true);
				r += execute(sqlGenerator.getSql(), sqlGenerator.getArgs());
			} catch (IllegalArgumentException e) {
				logger.error("init delete sql error! exec number : {}", r, e);
				throw new DataAccessException("init delete sql error!", e);
			} catch (IllegalAccessException e) {
				logger.error("init delete sql error! exec number : {}", r, e);
				throw new DataAccessException("init delete sql error!", e);
			} catch (NoSuchFieldException e) {
				logger.error("init delete sql error! exec number : {}", r, e);
				throw new DataAccessException("init delete sql error!", e);
			} catch (SecurityException e) {
				logger.error("init delete sql error! exec number : {}", r, e);
				throw new DataAccessException("init delete sql error!", e);
			} catch (SQLException e) {
				logger.error("init delete sql error! exec number : {}", r, e);
				throw new DataAccessException("init delete sql error!", e);
			}
		}
		return r;
	}

	/*
	 * (non-Javadoc)
	 * @see cn.org.zeronote.orm.ICommonDao#delete(java.lang.Class, java.util.Map)
	 */
	@Override
	public <T> int delete(Class<T> pojoType, Map<String, Object> args)
			throws DataAccessException {
		SqlDelGenerator sqlGenerator = new SqlDelGenerator(pojoType, args);
		try {
			int r = execute(sqlGenerator.getSql(), sqlGenerator.getArgs());
			return r;
		} catch (IllegalArgumentException e) {
			logger.error("init delete sql error!", e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (IllegalAccessException e) {
			logger.error("init delete sql error!", e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (NoSuchFieldException e) {
			logger.error("init delete sql error!", e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (SecurityException e) {
			logger.error("init delete sql error!", e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (SQLException e) {
			logger.error("init delete sql error!", e);
			throw new DataAccessException("init delete sql error!", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see cn.org.zeronote.orm.ICommonDao#deleteByLogic(java.lang.Class, java.lang.Object[])
	 */
	@Override
	public <T> int deleteByLogic(Class<T> pojoType, Object... args)
			throws DataAccessException {
		int r = 0;
		Map<String, List<Object[]>> sqlAndArgsList = new HashMap<String, List<Object[]>>();
		
		try {
			for (Object arg : args) {
				SqlDelGenerator sqlGenerator = new SqlDelGenerator(pojoType, arg, false);
				String sql = sqlGenerator.getSql();
				if (sqlAndArgsList.containsKey(sql)) {
					sqlAndArgsList.get(sql).add(sqlGenerator.getArgs());
				} else {
					List<Object[]> argsList = new ArrayList<Object[]>();
					argsList.add(sqlGenerator.getArgs());
					sqlAndArgsList.put(sql, argsList);
				}
			}
		} catch (IllegalArgumentException e) {
			logger.error("init delete sql error! exec number : {}", r, e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (IllegalAccessException e) {
			logger.error("init delete sql error! exec number : {}", r, e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (NoSuchFieldException e) {
			logger.error("init delete sql error! exec number : {}", r, e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (SecurityException e) {
			logger.error("init delete sql error! exec number : {}", r, e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (SQLException e) {
			logger.error("init delete sql error! exec number : {}", r, e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (InstantiationException e) {
			throw new DataAccessException("init delete sql error!", e);
		}
		for (String sql : sqlAndArgsList.keySet()) {
			int[] ii = batchUpdate(sql, sqlAndArgsList.get(sql));
			for (int i : ii) {
				r += i;
			}
		}
		return r;
	}

	/*
	 * (non-Javadoc)
	 * @see cn.org.zeronote.orm.ICommonDao#deleteByPhysical(java.lang.Class, java.lang.Object[])
	 */
	@Override
	public <T> int deleteByPhysical(Class<T> pojoType, Object... args)
			throws DataAccessException {
		int r = 0;
		Map<String, List<Object[]>> sqlAndArgsList = new HashMap<String, List<Object[]>>();
		
		try {
			for (Object arg : args) {
				SqlDelGenerator sqlGenerator = new SqlDelGenerator(pojoType, arg, true);
				String sql = sqlGenerator.getSql();
				if (sqlAndArgsList.containsKey(sql)) {
					sqlAndArgsList.get(sql).add(sqlGenerator.getArgs());
				} else {
					List<Object[]> argsList = new ArrayList<Object[]>();
					argsList.add(sqlGenerator.getArgs());
					sqlAndArgsList.put(sql, argsList);
				}
			}
		} catch (IllegalArgumentException e) {
			logger.error("init delete sql error! exec number : {}", r, e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (IllegalAccessException e) {
			logger.error("init delete sql error! exec number : {}", r, e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (NoSuchFieldException e) {
			logger.error("init delete sql error! exec number : {}", r, e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (SecurityException e) {
			logger.error("init delete sql error! exec number : {}", r, e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (SQLException e) {
			logger.error("init delete sql error! exec number : {}", r, e);
			throw new DataAccessException("init delete sql error!", e);
		} catch (InstantiationException e) {
			throw new DataAccessException("init delete sql error!", e);
		}
		for (String sql : sqlAndArgsList.keySet()) {
			int[] ii = batchUpdate(sql, sqlAndArgsList.get(sql));
			for (int i : ii) {
				r += i;
			}
		}
		return r;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ailk.keel.core.database.common.ICommonDao#insert(java.lang.Object[])
	 */
	@Override
	public int insert(Object... pojos) throws DataAccessException {
		int r = 0;
		Map<String, List<Object[]>> sqlAndArgsList = new HashMap<String, List<Object[]>>();
		Map<String, List<Object>> sqlPojosMapping = new HashMap<String, List<Object>>();
		try {
			for (Object obj : pojos) {
				SqlInsGenerator sqlGenerator = new SqlInsGenerator(obj);
				String sql = sqlGenerator.getSql();
				if (sqlAndArgsList.containsKey(sql)) {
					sqlAndArgsList.get(sql).add(sqlGenerator.getArgs());
					sqlPojosMapping.get(sql).add(obj);
				} else {
					List<Object[]> argsList = new ArrayList<Object[]>();
					argsList.add(sqlGenerator.getArgs());
					sqlAndArgsList.put(sql, argsList);
					
					List<Object> objsList = new ArrayList<Object>();
					objsList.add(obj);
					sqlPojosMapping.put(sql, objsList);
				}
			}
		} catch (IllegalArgumentException e) {
			logger.error("init insert sql error! exec number : {}", r, e);
			throw new DataAccessException("init insert sql error!", e);
		} catch (IllegalAccessException e) {
			logger.error("init insert sql error! exec number : {}", r, e);
			throw new DataAccessException("init insert sql error!", e);
		} catch (NoSuchFieldException e) {
			logger.error("init insert sql error! exec number : {}", r, e);
			throw new DataAccessException("init insert sql error!", e);
		} catch (SecurityException e) {
			logger.error("init insert sql error! exec number : {}", r, e);
			throw new DataAccessException("init insert sql error!", e);
		}
		// 这里使用同一个Connection以保证主键获取的成功
		Connection conn;
		try {
			conn = getConnection();
		} catch (SQLException e) {
			logger.error("Get connection from datasource error", e);
			throw new DataAccessException("Get connection from datasource error!", e);
		}
		try {
			for (String sql : sqlAndArgsList.keySet()) {
				int[] ii = batchUpdate(conn, sql, sqlAndArgsList.get(sql));
				SelectKey selectKey = SqlRepairer.getSelectKey(dbType);
				if (selectKey != null) {
					List<Object> objs = sqlPojosMapping.get(sql);
					if (objs != null && !objs.isEmpty()) {
						// 自增主键配置
						IdentityFinder identityFinder = new IdentityFinder(conn, selectKey);
						try {
							identityFinder.find(objs.toArray());
						} catch (IllegalArgumentException e) {
							logger.error("find identity!", e);
							throw new DataAccessException("find identity error!", e);
						} catch (IllegalAccessException e) {
							logger.error("find identity!", e);
							throw new DataAccessException("find identity error!", e);
						} catch (SQLException e) {
							logger.error("find identity!", e);
							throw new DataAccessException("find identity error!", e);
						}
					}
				}
				for (int i : ii) {
					r += i;
				}
			}
		} finally {
			try {
				close(conn);
			} catch (SQLException e) {
				throw new DataAccessException("close connection error!", e);
			}
		}
		return r;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.ICommonDao#batchUpdate(java.lang.String, java.util.List)
	 */
	@Override
	public int[] batchUpdate(String sql, List<Object[]> argsList)
			throws DataAccessException {
		Object[][] params = new Object[argsList.size()][];
		for (int i = 0; i < params.length; i++) {
			params[i] = pearParams(argsList.get(i));
		}
		QueryRunner qr = getQueryRunner();
		logger.trace("BatchUpdate SQL:{}, args size:{}", sql, argsList.size());
		try {
			return qr.batch(sql, params);
		} catch (SQLException e) {
			throw new DataAccessException("BatchUpdate error! sql:" + sql + ";", e);
		}
	}
	
	/**
	 * 指定connection的batchupdate，为了支持获取主键
	 * @param conn
	 * @param sql
	 * @param argsList
	 * @return
	 * @throws DataAccessException
	 */
	private int[] batchUpdate(Connection conn, String sql, List<Object[]> argsList)
			throws DataAccessException {
		Object[][] params = new Object[argsList.size()][];
		for (int i = 0; i < params.length; i++) {
			params[i] = pearParams(argsList.get(i));
		}
		QueryRunner qr = getQueryRunner();
		logger.trace("BatchUpdate SQL:{}, args size:{}", sql, argsList.size());
		try {
			return qr.batch(conn, sql, params);
		} catch (SQLException e) {
			throw new DataAccessException("BatchUpdate error! sql:" + sql + ";", e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ailk.aiip.apps.wasp.collect.common.ICommonDao#batchUpdate(java.lang.Class, java.lang.Object[])
	 */
	@Override
	public <T> int[] batchUpdateByLogic(Class<T> pojoType, T... pojos)
			throws DataAccessException {
		if (pojos == null || pojos.length == 0) {
			return new int[0];
		}
		try {
			String sql = null;
			List<Object[]> argsList = new ArrayList<Object[]>();
			for (T t : pojos) {
				if ( pojoType.equals(t.getClass())) {
					SqlUpdGenerator sqlGenerator = new SqlUpdGenerator(t, false);
					sql = sqlGenerator.getSql();
					argsList.add(sqlGenerator.getArgs());
				} else {
					throw new DataAccessException("Update : The type of inconsistency!");
				}
			}
			if (sql != null) {
				return batchUpdate(sql, argsList);
			} else {
				logger.trace("Not exist sql!");
				return new int[0];
			}
			
		} catch (IllegalArgumentException e) {
			logger.error("init update sql error!", e);
			throw new DataAccessException("init update sql error!", e);
		} catch (IllegalAccessException e) {
			logger.error("init update sql error!", e);
			throw new DataAccessException("init update sql error!", e);
		} catch (NoSuchFieldException e) {
			logger.error("init update sql error!", e);
			throw new DataAccessException("init update sql error!", e);
		} catch (SecurityException e) {
			logger.error("init update sql error!", e);
			throw new DataAccessException("init update sql error!", e);
		} catch (ParseException e) {
		    logger.error("init update sql error!", e);
            throw new DataAccessException("init update sql error!", e);
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see cn.org.zeronote.orm.ICommonDao#batchUpdateByPhysical(java.lang.Class, T[])
	 */
	@Override
	public <T> int[] batchUpdateByPhysical(Class<T> pojoType, T... pojos)
			throws DataAccessException {
		if (pojos == null || pojos.length == 0) {
			return new int[0];
		}
		try {
			String sql = null;
			List<Object[]> argsList = new ArrayList<Object[]>();
			for (T t : pojos) {
				if ( pojoType.equals(t.getClass())) {
					SqlUpdGenerator sqlGenerator = new SqlUpdGenerator(t, true);
					sql = sqlGenerator.getSql();
					argsList.add(sqlGenerator.getArgs());
				} else {
					throw new DataAccessException("Update : The type of inconsistency!");
				}
			}
			if (sql != null) {
				return batchUpdate(sql, argsList);
			} else {
				logger.trace("Not exist sql!");
				return new int[0];
			}
			
		} catch (IllegalArgumentException e) {
			logger.error("init update sql error!", e);
			throw new DataAccessException("init update sql error!", e);
		} catch (IllegalAccessException e) {
			logger.error("init update sql error!", e);
			throw new DataAccessException("init update sql error!", e);
		} catch (NoSuchFieldException e) {
			logger.error("init update sql error!", e);
			throw new DataAccessException("init update sql error!", e);
		} catch (SecurityException e) {
			logger.error("init update sql error!", e);
			throw new DataAccessException("init update sql error!", e);
		} catch (ParseException e) {
		    logger.error("init update sql error!", e);
            throw new DataAccessException("init update sql error!", e);
        }
	}
	
	/**
	 * 实际查询
	 * @param sql
	 * @param args
	 * @param resultSetExtractor
	 * @return
	 * @throws DataAccessException
	 */
	protected <T> T query(String sql, Object[] args, ResultSetHandler<T> resultSetExtractor) throws DataAccessException {
		QueryRunner qr = getQueryRunner();
		logger.debug("Query SQL:{}", sql);
		try {
			return qr.query(sql, resultSetExtractor, pearParams(args));
		} catch (SQLException e) {
			throw new DataAccessException("Query error!", e);
		}
	}
	
	/**
	 * update操作执行
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException 
	 */
	protected int execute(String sql, Object[] args) throws DataAccessException {
		QueryRunner qr = getQueryRunner();
		logger.debug("Update SQL:{}", sql);
		try {
			return qr.update(sql, pearParams(args));
		} catch (SQLException e) {
			throw new DataAccessException("update sql error! sql:" + sql + ";", e);
		}
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

	/**
	 * 获取拼装QueryRunner
	 * @return the queryRunner
	 */
	protected QueryRunner getQueryRunner() {
		if (queryRunner == null) {
			queryRunner = new QueryRunner(dataSource);
		}
		return queryRunner;
	}
	
	/**
	 * 获取连接
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		return this.dataSource.getConnection();
	}
	
	/**
	 * 关闭连接
	 * @param conn
	 * @throws SQLException
	 */
	protected void close(Connection conn) throws SQLException {
		if (conn != null && !conn.isClosed()) {
			conn.close();
		}
	}

	/**
	 * set datasource
	 * @param dataSource the dataSource to set
	 * @throws DataAccessException 
	 * @throws SQLException 
	 */
	public void setDataSource(DataSource dataSource) throws SQLException {
		synchronized (synObj) {
			this.dataSource = dataSource;
		}
	}
	
	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(DBType dbType) {
		this.dbType = dbType;
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
