/**
 * 
 */
package cn.org.zeronote.orm.dao.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cn.org.zeronote.orm.dao.parser.SqlSelHashGenerator;
import cn.org.zeronote.orm.testpo.DemoHashTable;


/**
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class SqlSelHashGeneratorTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.parser.SqlSelHashGenerator#getSqls()}.
	 * @throws IllegalAccessException 
	 */
	@Test
	public void testGetSqls() throws IllegalAccessException {
		Map<String, Object[]> args = new HashMap<String, Object[]>();
		args.put("id", new Object[]{1,101,2,102,3});
		args.put("cnt", new Object[]{"123"});
		SqlSelHashGenerator sqlGen = new SqlSelHashGenerator(DemoHashTable.class, args);
		String[] sqls = sqlGen.getSqls();
		List<Object[]> rArgs = sqlGen.getArgsObjs();
		for (int i = 0; i < sqls.length; i++) {
			System.out.println(sqls[i]);
			System.out.println(rArgs.get(i).length);
		}
	}

}
