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
	 * 生成代码
	 * @param pack	指定生成代码的包名，格式：com.dajie.po
	 * @param outputFolder	指定生成代码保存的根路径
	 */
	void generate(String pack, String outputFolder);
}
