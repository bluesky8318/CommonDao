/**
 * 
 */
package cn.org.zeronote.orm.nosql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import cn.org.zeronote.orm.DataAccessException;
import cn.org.zeronote.orm.ORMTable;
import cn.org.zeronote.orm.PaginationSupport;
import cn.org.zeronote.orm.RowSelection;
import cn.org.zeronote.orm.dao.DefaultCommonDao;


/**
 * 使用Rdis提供缓存支持的DAO
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class RedisWeakCachedCommonDao extends DefaultCommonDao {

	private static Logger logger = LoggerFactory.getLogger(RedisWeakCachedCommonDao.class);
	
	/** jedis pool */
	private JedisPool jedisPool;
	
	/** 缓存时间(单位：秒)，缓存到期后自动删除，默认60秒 */
	private int cacheSeconds = 60;
	/**
	 * 
	 */
	public RedisWeakCachedCommonDao() {
	}

	/**
	 * jedis pool
	 * @param jedisPool the jedisPool to set
	 */
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	/**
	 * 缓存时间(单位：秒)，缓存到期后自动删除
	 * @param cacheSeconds the cacheSeconds to set
	 */
	public void setCacheSeconds(int cacheSeconds) {
		this.cacheSeconds = cacheSeconds;
	}

	/**
	 * 获取redis cache连接
	 * @return
	 */
	private Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			logger.warn("Get redis pool error!", e);
		}
		return jedis;
	}
	
	/**
	 * 生成key值
	 * @param obj
	 * @return
	 */
	private List<String> generaCacheKey(Object obj) {
		ORMTable ormTable = obj.getClass().getAnnotation(ORMTable.class);
		if (ormTable != null) {
			String cachedTableKey = "".equalsIgnoreCase(ormTable.cachedShortAlias()) ? ormTable.tableName() : ormTable.cachedShortAlias();
			String[] cachedKeys = ormTable.cachedKey();
			
			if (cachedKeys.length > 0) {
				List<String> ks = new ArrayList<String>();
				for (int i = 0; i < cachedKeys.length; i++) {
					String cachedCol = cachedKeys[i];
					String[] fk = cachedCol.split("=");
					try {
						Field f = obj.getClass().getDeclaredField(fk[0]);
						f.setAccessible(true);
						ks.add(cachedTableKey + "." + fk[1] + "." + f.get(obj));	// 表别名 + key别名 + key值
					} catch (NoSuchFieldException e) {
						logger.warn("NoSuchFieldException:{};", cachedCol, e);
						continue;
					} catch (SecurityException e) {
						logger.warn("SecurityException:{};", cachedCol, e);
						continue;
					} catch (IllegalArgumentException e) {
						logger.warn("Get field value fail:{};", cachedCol, e);
						continue;
					} catch (IllegalAccessException e) {
						logger.warn("Get field value fail:{};", cachedCol, e);
						continue;
					}
				}
				return ks;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 清除缓存
	 * @param pojos
	 */
	private void deleteCache(Object... pojos) {
		Jedis jedis = getJedis();
		if (jedis != null) {
			try {
				for (Object obj : pojos) {
					List<byte[]> ks = new ArrayList<byte[]>();
					
					List<String> keys = generaCacheKey(obj);
					if (keys != null && !keys.isEmpty()) {
						for (String key : keys) {
							ks.add(key.getBytes());
						}
					}
					// 清除缓存
					jedis.del(ks.toArray(new byte[0][0]));
				}
			} finally {
				jedisPool.returnResource(jedis);
			}
		}
	}
	
	/**
	 * 清除缓存
	 * @param pojos
	 */
	private void updateCache(Object... pojos) {
		Jedis jedis = getJedis();
		if (jedis != null) {
			try {
				for (Object obj : pojos) {
					List<String> keys = generaCacheKey(obj);
					
					if (keys != null && !keys.isEmpty()) {
						// 更新
						for (String key : keys) {
							try {
								setCache(jedis, key, obj);	// key:// 表别名 + key别名 + key值
							} catch (IOException e) {
								logger.warn("Set cache value fail:{};", key, e);
								continue;
							}
						}
					}
				}
			} finally {
				jedisPool.returnResource(jedis);
			}
		}
	}
	
	/**
	 * 读取缓存
	 * @param jedis
	 * @param key
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private Object getCache(Jedis jedis, String key) throws ClassNotFoundException, IOException {
		Object obj = null;
        if (jedis != null && jedis.isConnected()) {
        	byte[] bi = jedis.get(key.getBytes());
        	if (bi != null) {
			    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bi, 0, bi.length));
			    obj = ois.readObject();
			}
        }
        return obj;
    }
    
    /**
     * 设置缓存对象
     * @param jedis
     * @param key
     * @param obj
     * @throws IOException 
     */
    private void setCache(Jedis jedis, String key, Object obj) throws IOException {
        if (jedis != null && jedis.isConnected()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            byte[] bs = bos.toByteArray();
            if (cacheSeconds > 0) {
            	jedis.setex(key.getBytes(), cacheSeconds, bs);
			} else {
				jedis.set(key.getBytes(), bs);
			}
            
        }
    }

	/* (non-Javadoc)
	 * @see cn.org.zeronote.orm.dao.DefaultCommonDao#updateByLogic(java.lang.Object[])
	 */
	@Override
	public int updateByLogic(Object... pojos) throws DataAccessException {
		int r = super.updateByLogic(pojos);
		if (r > 0) {
			// 有更新，才清除缓存，进行同步
			deleteCache(pojos);
		}
		return r;
	}

	/* (non-Javadoc)
	 * @see cn.org.zeronote.orm.dao.DefaultCommonDao#updateByPhysical(java.lang.Object[])
	 */
	@Override
	public int updateByPhysical(Object... pojos) throws DataAccessException {
		int r = super.updateByPhysical(pojos);
		updateCache(pojos);
		return r;
	}

	/* (non-Javadoc)
	 * @see cn.org.zeronote.orm.dao.DefaultCommonDao#deleteByLogic(java.lang.Object[])
	 */
	@Override
	public int deleteByLogic(Object... pojos) throws DataAccessException {
		int r = super.deleteByLogic(pojos);
		deleteCache(pojos);
		return r;
	}

	/* (non-Javadoc)
	 * @see cn.org.zeronote.orm.dao.DefaultCommonDao#deleteByPhysical(java.lang.Object[])
	 */
	@Override
	public int deleteByPhysical(Object... pojos) throws DataAccessException {
		int r = super.deleteByPhysical(pojos);
		deleteCache(pojos);
		return r;
	}

	/* (non-Javadoc)
	 * @see cn.org.zeronote.orm.dao.DefaultCommonDao#queryForPojoOne(java.lang.Class, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T queryForPojoOne(Class<T> pojoType, Map<String, Object> args)
			throws DataAccessException {
		Jedis jedis = getJedis();
		if (jedis != null) {
			try {
				T t = null;
				// 先在redis里找 XXX Annotation可以cache
				ORMTable ormTable = pojoType.getAnnotation(ORMTable.class);
				String cachedTableKey = "".equalsIgnoreCase(ormTable.cachedShortAlias()) ? ormTable.tableName() : ormTable.cachedShortAlias();
				
				String[] cachedKeys = ormTable.cachedKey();
				for (String cachedKey : cachedKeys) {
					String[] ks = cachedKey.split("=");
					Object arg = args.get(ks[0]);
					if (arg != null) {
						// 存在key值，则查找，否则数据库中差
						String key = cachedTableKey + "." + ks[1] + "." + String.valueOf(arg);
						T obj = null;
						try {
							obj = (T) getCache(jedis, key);
						} catch (ClassNotFoundException e) {
							logger.warn("Get from cache fail!", e);
						} catch (IOException e) {
							logger.warn("Get from cache fail!", e);
						}
						if (obj != null) {
							t = obj;
							break;
						}
					}
				}
				// 已有，则，直接返回
				if (t != null) {
					return t;
				} else {
					// 没有再去数据库里取
					t = super.queryForPojoOne(pojoType, args);
					if (t != null) {
						// 将新获取的添加到cache里
						for (String cachedKey : cachedKeys) {
							String[] fk = cachedKey.split("=");
							try {
								Field f = t.getClass().getDeclaredField(fk[0]);
								f.setAccessible(true);
								String key = cachedTableKey + "." + fk[1] + "." + f.get(t);
								setCache(jedis, key, t);
							} catch (Exception e) {
								logger.warn("Set to cache fail!", e);
							}
						}
					}
				}
				return t;
			} finally {
				// 必须释放
				jedisPool.returnResource(jedis);
			}
		} else {
			// 没有redis缓存，直接从数据库中获取
			return super.queryForPojoOne(pojoType, args);
		}
		
	}

	/* (non-Javadoc)
	 * @see cn.org.zeronote.orm.dao.DefaultCommonDao#queryForPojoList(java.lang.Class, java.lang.String, java.lang.Object[])
	 */
	@Override
	public <T> List<T> queryForPojoList(Class<T> pojoType, String col,
			Object[] args, String... requireFields) throws DataAccessException {
		// TODO
		// 先从缓存里查
		// 缓存里没有的再从数据库里查
		// 拼装到一起
		// 按照输入顺序排序
		return super.queryForPojoList(pojoType, col, args);
	}

	/* (non-Javadoc)
	 * @see cn.org.zeronote.orm.dao.DefaultCommonDao#queryForPaginatedPojoList(java.lang.Class, java.util.Map, cn.org.zeronote.orm.RowSelection)
	 */
	@Override
	public <T> PaginationSupport<T> queryForPaginatedPojoList(
			Class<T> pojoType, Map<String, Object> args,
			RowSelection rowSelection) throws DataAccessException {
		// TODO Auto-generated method stub
		return super.queryForPaginatedPojoList(pojoType, args, rowSelection);
	}
	
}
