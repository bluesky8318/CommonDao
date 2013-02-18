/**
 * 
 */
package cn.org.zeronote.orm.dao.parser;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cn.org.zeronote.orm.dao.parser.SqlSelGenerator;
import cn.org.zeronote.orm.testpo.DemoTablePO;


/**
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class SqlSelGeneratorTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.parser.SqlSelGenerator#getSql()}.
	 * @throws IllegalAccessException 
	 */
	@Test
	public void testGetSql() throws IllegalAccessException {
		Map<String, Object[]> args = new HashMap<String, Object[]>();
		args.put("id", new Object[]{1,2,3});
		SqlSelGenerator sqlGen = new SqlSelGenerator(DemoTablePO.class, null, args);
		System.out.println(sqlGen.getSql());
		System.out.println(sqlGen.getArgs().length);
	}

}
