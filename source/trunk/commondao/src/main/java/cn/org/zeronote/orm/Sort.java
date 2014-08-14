/**
 * 
 */
package cn.org.zeronote.orm;

/**
 * 排序
 * @author <a href='mailto:lizheng8318@gmail.com'>leon</a>
 *
 */
public enum Sort {

	/** 降序 */
	DESC("desc"),
	/** 升序 */
	ASC("asc");
	
	/** 描述字符串 */
	private String value;
	
	/**
	 * 添加输出值
	 * @param value
	 */
	private Sort(String value) {
		this.value = value;
	}
	
	/**
	 * 输出值
	 * @return
	 */
	public String getValue() {
		return value;
	}
}
