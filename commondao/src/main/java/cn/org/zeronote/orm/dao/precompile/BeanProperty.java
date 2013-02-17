/**
 * 
 */
package cn.org.zeronote.orm.dao.precompile;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean属性
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class BeanProperty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4890540553088645447L;

	/** bean class*/
	private Class<?> clazz;
	/** bean fields */
	private Field[] fields;
	/** 标准在class上的Annotation */
	private List<Annotation> classAnnotations;
	/** 标注在field上的Annotation*/
	private Map<Field, Annotation[]> fieldAnnotations;
	
	/**
	 * 
	 */
	public BeanProperty() {
	}

	/**
	 * @return the clazz
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * @param clazz the clazz to set
	 */
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * @return the fields
	 */
	public Field[] getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	/**
	 * @return the classAnnotations
	 */
	public List<Annotation> getClassAnnotations() {
		if (classAnnotations == null) {
			classAnnotations = new ArrayList<Annotation>();
		}
		return classAnnotations;
	}

	/**
	 * @param classAnnotations the classAnnotations to set
	 */
	public void setClassAnnotations(List<Annotation> classAnnotations) {
		this.classAnnotations = classAnnotations;
	}

	/**
	 * @return the fieldAnnotations
	 */
	public Map<Field, Annotation[]> getFieldAnnotations() {
		if (fieldAnnotations == null) {
			fieldAnnotations = new HashMap<Field, Annotation[]>();
		}
		return fieldAnnotations;
	}

	/**
	 * @param fieldAnnotations the fieldAnnotations to set
	 */
	public void setFieldAnnotations(Map<Field, Annotation[]> fieldAnnotations) {
		this.fieldAnnotations = fieldAnnotations;
	}
}
