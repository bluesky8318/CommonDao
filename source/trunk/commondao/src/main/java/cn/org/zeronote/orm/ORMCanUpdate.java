/**
 * 
 */
package cn.org.zeronote.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用此字段来标识返回的javabean是否可用于更新操作
 * <br>
 * 当请求的字段数量不是全部时，orm返回的po对象不可用于更新
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ORMCanUpdate {
}
