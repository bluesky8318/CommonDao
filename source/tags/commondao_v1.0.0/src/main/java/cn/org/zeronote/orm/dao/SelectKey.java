/**
 * 
 */
package cn.org.zeronote.orm.dao;

/**
 * 自增主键查询方法
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class SelectKey {

	/**查询结果key*/
	private String keyProperty;
	/**查询语句*/
	private String query;
	
	/**
	 * 
	 */
	public SelectKey() {
	}
	
	/**
	 * 
	 * @param keyProperty
	 * @param query
	 */
	public SelectKey(String keyProperty, String query) {
		this.keyProperty = keyProperty;
		this.query = query;
	}

	/**
	 * @return the keyProperty
	 */
	public String getKeyProperty() {
		return keyProperty;
	}

	/**
	 * @param keyProperty the keyProperty to set
	 */
	public void setKeyProperty(String keyProperty) {
		this.keyProperty = keyProperty;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}
}
