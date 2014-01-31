/**
 * 
 */
package cn.org.zeronote.orm.extractor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import cn.org.zeronote.orm.ORMAutoAssemble;
import cn.org.zeronote.orm.ORMCanUpdate;
import cn.org.zeronote.orm.ORMColumn;
import cn.org.zeronote.orm.dao.RowProcessor;



/**
 * 拼装结果集<p>
 * 如果在JVM参数中配置了db.charset，则可以支持取数据自动转码
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class BaseRowProcessor implements RowProcessor {
	
	private boolean clob2String = true;
	private boolean blob2Obj = false;
	
	/**orm映射大小写缓存*/
	private Map<Class<?>, Boolean> ignoreCaseCache = new HashMap<Class<?>, Boolean>();
	/**fields cache*/
	private Map<Class<?>, Map<Field, ORMColumn>> fieldsCache = new HashMap<Class<?>, Map<Field,ORMColumn>>();
	/**判断是否可更新的字段缓存*/
	private Map<Class<?>, Field> fieldsCanUpdateCache = new HashMap<Class<?>, Field>();
	
	
	/**
	 * 
	 */
	public BaseRowProcessor() {
	}

	/*
	 * (non-Javadoc)
	 * @see cn.org.zeronote.orm.dao.RowProcessor#toBean(java.sql.ResultSet, java.lang.Class, java.util.Set)
	 */
	@Override
	public <T> T toBean(ResultSet rs, Class<T> clz) throws SQLException {
		// 映射读取
		boolean ignoreCase = false;
		if (ignoreCaseCache.containsKey(clz)) {
			ignoreCase = ignoreCaseCache.get(clz);
		} else {
			ORMAutoAssemble orma = clz.getAnnotation(ORMAutoAssemble.class);
			ignoreCase = orma == null ? false : orma.ignoreCase();	// 是否忽略大小写
			ignoreCaseCache.put(clz, ignoreCase);
		}
		
		// 字段映射
		Map<String, Integer[]> clList = new HashMap<String, Integer[]>();
		for (int j = 1; j <= rs.getMetaData().getColumnCount(); j++) {
			Integer[] is = new Integer[2];
			is[0] = j;
			is[1] = rs.getMetaData().getColumnType(j);
			clList.put(ignoreCase ? rs.getMetaData().getColumnLabel(j).toLowerCase() : rs.getMetaData().getColumnLabel(j), is);
		}
		
		Map<Field, ORMColumn> fieldsMap = fieldsCache.get(clz);
		if (fieldsMap == null) {
			fieldsMap = new HashMap<Field, ORMColumn>();
			fieldsCache.put(clz, fieldsMap);
			
			Field[] fields = null;
			// class parent
			Class<?> parentClz = clz;
			while (!Object.class.equals(parentClz.getSuperclass())) {
				parentClz = parentClz.getSuperclass();
				
				fields = parentClz.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					ORMColumn ormc = field.getAnnotation(ORMColumn.class);
					fieldsMap.put(field, ormc);
				}
			}
			// this class
			fields = clz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				ORMColumn ormc = field.getAnnotation(ORMColumn.class);
				fieldsMap.put(field, ormc);
				ORMCanUpdate ocu = field.getAnnotation(ORMCanUpdate.class);
				if (ocu != null) {
					// 只取一个，并且是最后找到的那个，po里只应该存在一个
					fieldsCanUpdateCache.put(clz, field);
				}
			}

		}
		
		// 创建Bean
		T bean = this.newInstance(clz);
		for (Field field : fieldsMap.keySet()) {
			ORMColumn ormc = fieldsMap.get(field);
			String value = ormc == null ? field.getName() : ormc.value();
			value = ignoreCase ? value.toLowerCase() : value;
			if (clList.keySet().contains(value)) {	// metadata中需要有
				try {
					Integer[] is = clList.get(value);
					field.set(bean, conversionType(getValue(rs, is[0], is[1], field.getType()), is[1], field.getType()));
				} catch (IllegalArgumentException e) {
					throw new SQLException("Cannot write value: " + field.getName(), e);
				} catch (IllegalAccessException e) {
					throw new SQLException("Cannot write value: " + field.getName(), e);
				}
			}
		}
		
		return bean;
	}
	
	/**
	 * new instance
	 * @param <T>
	 * @param c
	 * @return
	 * @throws SQLException
	 */
	protected <T> T newInstance(Class<T> c) throws SQLException {
		try {
			return c.newInstance();

		} catch (InstantiationException e) {
			throw new SQLException("Cannot create " + c.getName(), e);

		} catch (IllegalAccessException e) {
			throw new SQLException("Cannot create " + c.getName(), e);
		}
	}
	
	private Object getValue(ResultSet rs, int columnIndex, int columnType, Class<?> propType) throws SQLException{
		Object obj = null;
		switch (columnType) {
		case Types.BLOB:{
			Blob blob = rs.getBlob(columnIndex);
			obj = blob==null?null:
				((blob2Obj || !propType.equals(Blob.class))?getObjectFromInputStream(blob.getBinaryStream()):blob);
			break;
		}			
		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:{
			obj = (blob2Obj || !propType.equals(InputStream.class))?getObjectFromInputStream(rs.getBinaryStream(columnIndex))
					:rs.getObject(columnIndex);
			break;
		}
		case Types.CLOB:{
			Clob clob = rs.getClob(columnIndex);
			if (clob2String || !propType.equals(Clob.class)) {
				obj = clob==null?null:clob.getSubString(1, (int)clob.length());
			} else {
				obj = clob;
			}
			break;
		}
		default:{
			obj = getResultSetValue(rs, columnIndex);
			break;
		}
		}
		return obj;
	}
	
	private Object getObjectFromInputStream(InputStream in) throws SQLException{
		if (in == null){
			return null;
		}
		ObjectInputStream objIn = null;
		try {
			objIn = new ObjectInputStream(new BufferedInputStream(in));
			return objIn.readObject();
		}
		catch(IOException e){
		    throw new SQLException("将Blob类型字段里的对象转换为Java POJO出错", e);
		}
		catch(Exception e){
			throw new SQLException("将Blob类型字段里的对象转换为Java POJO出错");
		}
		finally {
			if (in != null){
				try {
                    in.close();
                }
                catch (IOException e) {
                	throw new SQLException("将Blob类型字段里的对象转换为Java POJO出错", e);
                }
			}
			if (objIn != null) {
				try {
					objIn.close();
				} catch (IOException e) {
					throw new SQLException("将Blob类型字段里的对象转换为Java POJO出错", e);
				}
			}
		}
	}
	
	public Object getResultSetValue(ResultSet rs, int index) throws SQLException {
		Object obj = rs.getObject(index);
		String className = null;
		if (obj != null) {
			className = obj.getClass().getName();
		}
		if (obj instanceof Blob) {
			obj = rs.getBytes(index);
		}
		else if (obj instanceof Clob) {
			obj = rs.getString(index);
		}
		else if (className != null &&
				("oracle.sql.TIMESTAMP".equals(className) ||
				"oracle.sql.TIMESTAMPTZ".equals(className))) {
			obj = rs.getTimestamp(index);
		}
		else if (className != null && className.startsWith("oracle.sql.DATE")) {
			String metaDataClassName = rs.getMetaData().getColumnClassName(index);
			if ("java.sql.Timestamp".equals(metaDataClassName) ||
					"oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
				obj = rs.getTimestamp(index);
			}
			else {
				obj = rs.getDate(index);
			}
		}
		else if (obj != null && obj instanceof java.sql.Date) {
			if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
				obj = rs.getTimestamp(index);
			}
		}
		else if(obj != null && "java.lang.String".equals(className)) {
			obj = (String)obj;
		}
		return obj;
	}
	
	/**
	 * 试图将value转换为voClazz类型
	 * @param value
	 * @param type
	 * @param voClazz
	 * @return
	 */
	private Object conversionType(Object value, int type, Class<?> voClazz) {
		if (value == null) {
			// 空值返回null || 0
			if (voClazz != null && (voClazz.equals(int.class) || voClazz.equals(double.class) || voClazz.equals(float.class))) {
				return 0;
			} else {
				return null;
			}
		}
		if (value.getClass().equals(voClazz)) {
			// 类型相同，直接返回
			return value;
		}
		// 根据类型转换表进行转换
		//// 目标类型：boolean
		if (Boolean.class.equals(voClazz) || boolean.class.equals(voClazz)) {
			switch (type) {
			case Types.CHAR:
			case Types.VARCHAR:
				String str = String.valueOf(value);
				if ("0".equalsIgnoreCase(str) 
						|| "N".equalsIgnoreCase(str)
						|| "No".equalsIgnoreCase(str)
						) {
					// 0, N, No表示false
					return false;
				} else {
					// 非0...表示true
					return true;
				}
			default:
				return value;
			}
		}
		
		//// 目标类型：long
		if (Long.class.equals(voClazz) || long.class.equals(voClazz)) {
			switch (type) {
			case Types.DECIMAL:
				if (value instanceof BigDecimal) {
					return ((BigDecimal)value).longValue();
				}
			case Types.NUMERIC:
			case Types.BIGINT:
			case Types.INTEGER:
				if (value instanceof BigDecimal) {
					return ((BigDecimal)value).longValue();
				} else {
					try {
						Double d = Double.valueOf(String.valueOf(value));
						return d.longValue();
					} catch (NumberFormatException e) {
					}
				}
			default:
				return value;
			}
		}

		//// 目标类型：int
		if (Integer.class.equals(voClazz) || int.class.equals(voClazz)) {
			switch (type) {
			case Types.DECIMAL:
				if (value instanceof BigDecimal) {
					return ((BigDecimal)value).intValue();
				}
			case Types.NUMERIC:
			case Types.BIGINT:
			case Types.INTEGER:
				if (value instanceof BigDecimal) {
					return ((BigDecimal)value).intValue();
				} else {
					try {
						Integer d = Integer.valueOf(String.valueOf(value));
						return d.intValue();
					} catch (NumberFormatException e) {
					}
				}
			default:
				return value;
			}
		}
		
		//// 目标类型：double
		if (Double.class.equals(voClazz) || double.class.equals(voClazz)) {
			switch (type) {
			case Types.DECIMAL:
				if (value instanceof BigDecimal) {
					return ((BigDecimal)value).doubleValue();
				}
			case Types.NUMERIC:
			case Types.BIGINT:
			case Types.INTEGER:
				if (value instanceof BigDecimal) {
					return ((BigDecimal)value).doubleValue();
				} else {
					try {
						Double d = Double.valueOf(String.valueOf(value));
						return d.doubleValue();
					} catch (NumberFormatException e) {
					}
				}
			default:
				return value;
			}
		}
		
		return value;
	}
}
