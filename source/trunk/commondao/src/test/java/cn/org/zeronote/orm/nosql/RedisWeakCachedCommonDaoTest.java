/**
 * 
 */
package cn.org.zeronote.orm.nosql;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.org.zeronote.orm.nosql.RedisWeakCachedCommonDao;
import cn.org.zeronote.orm.testpo.UserPO;


/**
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/dao.xml"})
public class RedisWeakCachedCommonDaoTest {

	@Autowired
	@Qualifier("cacheDao")
	RedisWeakCachedCommonDao dao;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.nosql.RedisWeakCachedCommonDao#queryForPojoOne(java.lang.Class, java.util.Map)}.
	 */
	@Test
	public void testQueryForPojoOne() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("id", "123");
		UserPO user = dao.queryForPojoOne(UserPO.class, args);
		System.out.println(user.getId());
	}

	@Test
	public void testDeleteByPhysical() {
		UserPO user = new UserPO();
		user.setId(123L);
		dao.deleteByPhysical(user);
	}
}
