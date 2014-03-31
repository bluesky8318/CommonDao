/**
 * 
 */
package cn.org.zeronote.orm.dao.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.NumberUtils;

/**
 * 值转换工具类
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public abstract class ValueUtils {
	
	/**
	 * 
	 */
	public ValueUtils() {
	}

	/**
	 * 将value转换为指定类型的值，转换不成功则返回原始的value
	 * @param value				value
	 * @param targetClazz		target class
	 * @return					re value
	 * @throws ParseException 	解析异常
	 */
	@SuppressWarnings("unchecked")
	public static Object conversionType(Object value, Class<?> targetClazz) throws ParseException {
		if (value == null) {
			return null;
		}
		if (String.class.equals(targetClazz)) {
			// 字符串
			return String.valueOf(value);
		} else if (Date.class.equals(targetClazz)) {
			if (value instanceof Date) {
				return value;
			} else {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return df.parse(String.valueOf(value));
			}
		} else if (Number.class.isAssignableFrom(targetClazz)) {
			// 数值
			return NumberUtils.parseNumber(String.valueOf(value), (Class<Number>)targetClazz);
		}
		return value;
	}
}
