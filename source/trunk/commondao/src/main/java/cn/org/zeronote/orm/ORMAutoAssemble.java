/**
 * 
 */
package cn.org.zeronote.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动装配声明
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ORMAutoAssemble {
	
	/**
	 * 字段是否忽略大小写，默认区分
	 * @return
	 */
	boolean ignoreCase() default false;
}
