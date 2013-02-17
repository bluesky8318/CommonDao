/**
 * 
 */
package cn.org.zeronote.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 散列表自动装配声明
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ORMAutoAssemble
public @interface ORMHash {
	
	/**
	 * 散列表数量
	 * <p> 小于等于0，则不做散列处理
	 * @return
	 */
	int hashSize();
	
	/**
	 * 散列参照字段，该列必须是int/long
	 * @return
	 */
	String hashRefColumn();
	
	/**
	 * 建表语句
	 * <p>
	 * 建表语句中的TableName使用{0}替换，建表时会使用<code>tableName()</code>和对应的<code>hashSize()</code>进行替换。
	 * </p>
	 * @return
	 */
	String createSQL();
}
