/**
 * 
 */
package cn.org.zeronote.orm;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据支持
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class PaginationSupport<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6691202480299936564L;

	public static final int PAGESIZE = 20;	
	
	/**数据对象*/
	private List<T> object;
	/**当前页，0代表第一页*/
	private int currentPage = 0;
	/**页数*/
	private int pageCount = 0;
	/**页大小*/
	private int pageSize = PAGESIZE;
	/**总行数*/
	private long totalCount;
	
	/**
	 * 
	 */
	public PaginationSupport() {
	}

	/**
	 * @return the object
	 */
	public List<T> getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(List<T> object) {
		this.object = object;
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the totalCount
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
}
