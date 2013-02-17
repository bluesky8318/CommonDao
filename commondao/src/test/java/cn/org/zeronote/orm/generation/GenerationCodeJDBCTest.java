/**
 * 
 */
package cn.org.zeronote.orm.generation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.org.zeronote.orm.generation.GenerationCodeJDBC;

/**
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/dao.xml"})
public class GenerationCodeJDBCTest {

	@Autowired
	GenerationCodeJDBC generationCodeJDBC;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.generation.GenerationCodeJDBC#generate(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGenerate() {
		generationCodeJDBC.generate("com.dajie.modules.plugin.admin.po", "D:/Test/services");
	}

}
