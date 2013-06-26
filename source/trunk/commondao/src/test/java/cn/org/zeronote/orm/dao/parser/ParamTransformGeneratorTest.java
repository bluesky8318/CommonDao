/**
 * 
 */
package cn.org.zeronote.orm.dao.parser;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author leon
 *
 */
public class ParamTransformGeneratorTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.parser.ParamTransformGenerator#generate()}.
	 */
	@Test
	public void testGenerate() {
		String sql = " select * from abc where a=:test1 and b like :test2 an p = :test1";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("test1", 1);
		params.put("test2", "%t");
		ParamTransformGenerator generator = new ParamTransformGenerator(sql, params);
		generator.generate();
		Assert.assertTrue(" select * from abc where a=? and b like ? an p = ?".equals(generator.getSql()));
		Assert.assertTrue(generator.getArgs().length == 3);
	}

}
