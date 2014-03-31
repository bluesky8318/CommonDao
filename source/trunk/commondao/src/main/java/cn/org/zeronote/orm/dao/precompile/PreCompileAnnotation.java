/**
 * 
 */
package cn.org.zeronote.orm.dao.precompile;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Annotation预编译
 * <br>
 * 对ORM中的对象进行预编译缓存
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class PreCompileAnnotation {

	/** 针对class做bean属性缓存 */
	private static Map<Class<?>, BeanProperty> annotationCache = new HashMap<Class<?>, BeanProperty>();
	
	/**单例*/
	private static PreCompileAnnotation instance;
	
	/**
	 * 
	 */
	private PreCompileAnnotation() {
	}
	
	/**
	 * class 上的注解
	 * @param <A>					class
	 * @param clz					pojo class
	 * @param annotationClass		annotation
	 * @return						指定的注解
	 */
	@SuppressWarnings("unchecked")
	public <A extends Annotation> A getAnnotation(Class<?> clz, Class<A> annotationClass) {
		BeanProperty bp = getBeanProperty(clz);
		for (Annotation annotation : bp.getClassAnnotations()) {
			if (annotation.equals(annotationClass)) {
				return (A) annotation;
			}
		}
		return null;
	}
	
	/**
	 * class的field上的注解
	 * @param <A>				class
	 * @param clz				pojo class
	 * @param field				pojo field
	 * @param annotationClass	annotation
	 * @return					指定的注解
	 */
	@SuppressWarnings("unchecked")
	public <A extends Annotation> A getAnnotation(Class<?> clz, Field field, Class<A> annotationClass) {
		BeanProperty bp = getBeanProperty(clz);
		Annotation[] ans = bp.getFieldAnnotations().get(field);
		for (Annotation annotation : ans) {
			if (annotation.equals(annotationClass)) {
				return (A) annotation;
			}
		}
		return null;
	}

	/**
	 * Bean属性缓存
	 * @param clz	Pojo class
	 * @return		属性列表
	 */
	private BeanProperty getBeanProperty(Class<?> clz) {
		BeanProperty bp = annotationCache.get(clz);
		if (bp == null) {
			// 没有缓存，去获取
			bp = new BeanProperty();
			// 读取Annotation
			//// header
			Annotation[] ans = clz.getAnnotations();
			for (Annotation an : ans) {
				bp.getClassAnnotations().add(an);
			}
			//// fields
			Field[] fields = clz.getDeclaredFields();
			bp.setFields(fields);
			//// field annotation
			for (Field field : fields) {
				field.setAccessible(true);
				bp.getFieldAnnotations().put(field, field.getAnnotations());
			}
			
			// 编译Annotation，放到缓存中
			annotationCache.put(clz, bp);
		}
		return bp;
	}
	
	/**
	 * 实例
	 * @return	单例
	 */
	public static PreCompileAnnotation getInstance() {
		if (instance == null) {
			instance = new PreCompileAnnotation();
		}
		return instance;
	}
}
