/**
 * 
 */
package cn.org.zeronote.orm.generation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class Table {

	/**表名称*/
	private String tableName;
	/**字段*/
	private List<Column> columns;
	
	/**
	 * 
	 */
	public Table() {
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the columns
	 */
	public List<Column> getColumns() {
		if (columns == null) {
			columns = new ArrayList<Column>();
		}
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
}
