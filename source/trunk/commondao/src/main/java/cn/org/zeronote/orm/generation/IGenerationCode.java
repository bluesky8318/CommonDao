/**
 * 
 */
package cn.org.zeronote.orm.generation;

/**
 * 生成代码
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public interface IGenerationCode {

	/**
	 * 生成代码（全库）
	 * @param pack	指定生成代码的包名，格式：cn.org.zeronote.po
	 * @param outputFolder	指定生成代码保存的根路径
	 * @param useDefault	是否使用默认值
	 */
	void generate(String pack, String outputFolder, boolean useDefault);
	
	/**
	 * 生成代码，指定前缀
	 * @param pack	指定生成代码的包名，格式：cn.org.zeronote.po
	 * @param outputFolder	指定生成代码保存的根路径
	 * @param prefix	前缀
	 * @param useDefault 是否使用默认值
	 */
	void generatePrefix(String prefix, String pack, String outputFolder, boolean useDefault);
	
	/**
	 * 生成代码，指定表名
	 * @param pack	指定生成代码的包名，格式：cn.org.zeronote.po
	 * @param outputFolder	指定生成代码保存的根路径
	 * @param tableName	table name
	 * @param useDefault	是否使用默认值
	 */
	void generateTable(String tableName, String pack, String outputFolder, boolean useDefault);
}
