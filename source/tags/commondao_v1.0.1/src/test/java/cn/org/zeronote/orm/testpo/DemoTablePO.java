/**
 * 
 */
package cn.org.zeronote.orm.testpo;

import cn.org.zeronote.orm.ORMAutoAssemble;
import cn.org.zeronote.orm.ORMColumn;
import cn.org.zeronote.orm.ORMTable;

/**
 * 这是一个标准table
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
@ORMAutoAssemble
@ORMTable(tableName = "demo", cachedKey={"id=I"})
public class DemoTablePO {

	@ORMColumn(value = "id", physicalPkFld=true)
	private Long id;
	@ORMColumn(value = "content")
	private String cnt;
	
	/**
	 * 
	 */
	public DemoTablePO() {
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
