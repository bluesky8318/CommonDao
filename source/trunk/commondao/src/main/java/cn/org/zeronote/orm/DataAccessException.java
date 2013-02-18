/**
 * 
 */
package cn.org.zeronote.orm;

/**
 * 数据访问异常
 * <p>跟spring起个相同的名字</p>
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class DataAccessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5490271921625584904L;

	/**
	 * 
	 */
	public DataAccessException() {
	}

	/**
	 * @param message
	 */
	public DataAccessException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DataAccessException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}