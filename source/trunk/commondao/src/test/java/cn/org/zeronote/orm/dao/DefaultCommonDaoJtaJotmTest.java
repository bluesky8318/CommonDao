/**
 * 
 */
package cn.org.zeronote.orm.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/dao_jta_jotm.xml"})
public class DefaultCommonDaoJtaJotmTest {

	@Autowired
	@Qualifier("dbDao")
	DefaultCommonDao dao;
	
	
	@Autowired
	ITransactionalService transactionalService;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * 测试事务处理
	 */
	@Test
	public void testTransactional() {
		try {
			transactionalService.transcationalSomething();
		} catch (Exception e) {
			System.out.println("rollback:"+e.getMessage());
			e.printStackTrace();
		}
	}
}
