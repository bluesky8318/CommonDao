package cn.org.zeronote.orm;

import java.io.Serializable;

/**
 * 用于分页查询中描述分页结构（如每页大小，起始页等信息）
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 * @date 2012-3-29
 *
 */
public class RowSelection implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7294877126777648771L;
	
	private static final int DEFAULT_PAGESIZE = 20;
	
	/**开始页码，0表示第一页*/
	private int startPage;
	/**页大小*/
	private int pageSize = DEFAULT_PAGESIZE;
	/**第一行(不含）*/
	private int firstRow;
	/**本页最后一行（含）*/
	private int lastRow;
	
	/**
	 * 构造方法
	 * @param startPage 0代表第一页，依次类推
	 */
	public RowSelection(int startPage){
		this(startPage, DEFAULT_PAGESIZE);
	}
	
	/**
	 * 构造方法
	 * @param startPage 0代表第一页，依次类推
	 * @param pageSize 每页包含的记录数
	 */
	public RowSelection(int startPage, int pageSize) {
		this.startPage = startPage;
		this.pageSize = pageSize;
		this.firstRow = startPage*pageSize;
		this.lastRow = firstRow + pageSize;
	}

	/**
	 * 每页记录数
	 * @return int
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 得到当前表示的是第几页
	 * @return int 以0为基，0表示第一页，依次类推
	 */
	public int getStartPage() {
		return startPage;
	}
	
	/**
	 * 表示本页包含的第一条记录的前一条记录在整个记录集中所处的位置。<br/>
	 * 例如：分页大小为6，页码为4（实际代表第5页），则本页的第一条记录的前一条记录在整个记录集中的位置为：24。<br/>
	 * RowSelection rowSelection = new RowSelection(4, 6);<br/>
	 * assert rowSelection.getFirstRow()==24<br/>
	 * @return int
	 */
	public int getFirstRow(){
		return firstRow;
	}
	
	/**
	 * 表示本页包含的最后一条记录在整个记录集中所处的位置。<br/>
	 * 例如：分页大小为6，页码为4（实际代表第5页），则本页的最后一条记录在整个记录集中的位置为：30。<br/>
	 * RowSelection rowSelection = new RowSelection(4, 6);<br/>
	 * assert rowSelection.getLastRow()==30<br/>
	 * @return int
	 */
	public int getLastRow(){
		return lastRow;
	}
}
