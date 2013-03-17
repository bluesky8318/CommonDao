/**
 * 
 */
package cn.org.zeronote.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表字段
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ORMColumn {

	/** 日期类型默认值 */
	public final static String DEFAULT_DATE = "DEFAULT_DATE";
	
	/**
	 * 字段名
	 * @return
	 */
	String value();
	
	/**
	 * 是否是物理主键
	 * @return
	 */
	boolean physicalPkFld() default false;
	
	/**
	 * 是否逻辑主键
	 * @return
	 */
	boolean LogicPkFld() default false;
	
	/**
	 * 更新默认值
	 * 拼接方法：
	 * 		如果get(obj)为null，则拼接：value=defaultValue，边界单引号需要在defaultValue中自行设置
	 * @return
	 */
	String defaultValue() default "null";
	
	/**
	 * 是否自增
	 * @return
	 */
	boolean autoIncrement() default false;
	
	/**
	 * 默认值的作用域
	 * @return
	 */
	ORMValueScope defaultValueScope() default ORMValueScope.ALL;
	
	/**
	 * 是否追加
	 * <br/>
	 * 主要针对String 类型，进行文字内容追加
	 * @return
	 */
	boolean append() default false;
}
