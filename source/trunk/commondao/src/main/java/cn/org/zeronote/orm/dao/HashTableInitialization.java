/**
 * 
 */
package cn.org.zeronote.orm.dao;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.org.zeronote.orm.ICommonDao;
import cn.org.zeronote.orm.ORMHash;
import cn.org.zeronote.orm.ORMTable;
import cn.org.zeronote.orm.dao.parser.DBUtils;


/**
 * HashTable初始化工作
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class HashTableInitialization {
	
	private static Logger logger = LoggerFactory.getLogger(HashTableInitialization.class);

	/** 数据访问 */
	private ICommonDao dao;
	
	/** 需要被初始化的散列表对象(必须加ORMHashTable注解) */
	private Class<?>[] clzss;
	/**
	 * 
	 */
	public HashTableInitialization() {
	}
	
	/**
	 * 初始化指定类
	 * @throws SQLException 
	 */
	public void init() throws SQLException {
		// 初始化表
		if (clzss != null) {
			int inCount = 0;
			for (Class<?> clz : clzss) {
				ORMHash ht = clz.getAnnotation(ORMHash.class);
				if (ht == null) {
					logger.warn("class[{}] not have ORMHashTable Annotation!", clz.toString());
					continue;
				}
				ORMTable table = clz.getAnnotation(ORMTable.class);
				if (table == null) {
					logger.warn("class[{}] not have ORMHashTable Annotation!", clz.toString());
					continue;
				}
				createTable(ht, table);
				inCount++;
			}
			logger.info("{} class be initialized in all[{}]!", inCount, clzss.length);
		} else {
			logger.info("No class be initialized!");
		}
	}
	
	/**
	 * 初始化散列表
	 * @param ht
	 * @throws SQLException 
	 */
	private void createTable(ORMHash ht, ORMTable table) throws SQLException {
		String createSQL = ht.createSQL();
		String tableName = table.tableName();
		int hashSize = ht.hashSize();
		if (hashSize < 1) {
			hashSize = 1;
		}
		
		List<String> alreadyExistTableList = dao.queryForSimpObjectList("show tables like '" + tableName +"%'", new Object[]{}, String.class);
		
		MessageFormat mf = new MessageFormat(createSQL);
		
		String hashTableName = null;
        for (int i = 0; i < hashSize; i++) {
            hashTableName = tableName + "_" + DBUtils.getInstance().hash(i, hashSize);
            if (alreadyExistTableList.contains(hashTableName)) {	// XXX 是否要区分大小写？
				logger.trace("Hash Table[{}] is already exist!", hashTableName);
			} else {
				logger.info("Init hash table:{};", hashTableName);
				dao.update(mf.format(new Object[]{hashTableName}));
				logger.info("Init hash table[{}] finish.", hashTableName);
			}
        }
	}

	/**
	 * @param clzss the clzss to set
	 */
	public void setClzss(Class<?>[] clzss) {
		this.clzss = clzss;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(ICommonDao dao) {
		this.dao = dao;
	}
}
