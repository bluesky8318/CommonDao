/**
 * 
 */
package cn.org.zeronote.orm.generation;

/**
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class Column {

	/**字段名*/
	private String name;
	/**类型*/
	private Class<?> clz;
	/**是否物理主键*/
	private boolean primaryKey;
	/**是否可为空*/
	private boolean nullable;
	
	/** 是否自增 */
	private boolean autoIncrement;
	
	/** 默认值 */
	private String def;
	
	/**
	 * 
	 */
	public Column() {
	}
	
	/**
	 * @return the autoIncrement
	 */
	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	/**
	 * @param autoIncrement the autoIncrement to set
	 */
	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	/**
	 * @return the primaryKey
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	/**
	 * @param primaryKey the primaryKey to set
	 */
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the clz
	 */
	public Class<?> getClz() {
		return clz;
	}
	/**
	 * @param clz the clz to set
	 */
	public void setClz(Class<?> clz) {
		this.clz = clz;
	}

	/**
	 * @return the nullable
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * @param nullable the nullable to set
	 */
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * @return the def
	 */
	public String getDef() {
		return def;
	}

	/**
	 * @param def the def to set
	 */
	public void setDef(String def) {
		this.def = def;
	}
}
