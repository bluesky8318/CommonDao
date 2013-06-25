/**
 * 
 */
package cn.org.zeronote.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ORMAutoAssemble
public @interface ORMTable {

	/**
	 * 指定和该类型对应的数据库表名
	 * @return String
	 */
	String tableName();
	
	/**
	 * 表名别名，用来拼接cachekey的前缀，尽量短，并保证项目中不重复
	 * <p>
	 * 默认为空，这个时候使用<code>tableName</code>来作为cache key前缀
	 * </p>
	 * @return
	 */
	String cachedShortAlias() default "";

	/**
	 * 指定nosql缓存策略key值
	 * <p>格式：field=key;field:对应javabean的成员变量名称，区分大小写;key:该字段的缓存key</p>
	 * @return
	 */
	String[] cachedKey() default {};
	
	/**
	 * 指定查询对应的排序字段
	 * <br>数据库字段名
	 * @return
	 */
	String[] order() default {};
}
