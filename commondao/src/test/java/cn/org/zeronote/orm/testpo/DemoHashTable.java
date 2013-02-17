/**
 * 
 */
package cn.org.zeronote.orm.testpo;

import cn.org.zeronote.orm.ORMAutoAssemble;
import cn.org.zeronote.orm.ORMColumn;
import cn.org.zeronote.orm.ORMHash;
import cn.org.zeronote.orm.ORMTable;

/**
 * 这是一个散列的table
 * 
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 * 
 */
@ORMAutoAssemble
@ORMHash(hashSize = 100, hashRefColumn = "id", createSQL = "create table {0} (id bigint(20) NOT NULL AUTO_INCREMENT, content char(20) NOT NULL)")
@ORMTable(tableName = "demohashtable")
public class DemoHashTable {

	@ORMColumn(value = "id", physicalPkFld=true)
	private Long id;
	@ORMColumn(value = "content")
	private String cnt;
	
	/**
	 * 
	 */
	public DemoHashTable() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the cnt
	 */
	public String getCnt() {
		return cnt;
	}

	/**
	 * @param cnt the cnt to set
	 */
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
}
