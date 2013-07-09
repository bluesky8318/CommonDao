/**
 * 
 */
package cn.org.zeronote.orm.dao;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.org.zeronote.orm.testpo.ActiveDevicePO;
import cn.org.zeronote.orm.testpo.UserInfoPO;
import cn.org.zeronote.orm.testpo.UserNotifyPO;
import cn.org.zeronote.orm.testpo.UserPO;


/**
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/dao.xml"})
public class DefaultCommonDaoTest {

	@Autowired
	@Qualifier("dbDao")
	DefaultCommonDao dao;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#queryForPaginatedPojoList(java.lang.String, java.lang.Object[], java.lang.Class, cn.org.zeronote.orm.RowSelection)}.
	 */
	@Test
	public void testQueryForPaginatedPojoList() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#queryForMapList(java.lang.String, java.lang.Object[])}.
	 */
	@Test
	public void testQueryForMapList() {
		List<Map<String, Object>> ls = dao.queryForMapList("select * from cache_push  where show_time <= ? AND status = ? LIMIT 180;", new Date(), 1);
		for (Map<String, Object> map : ls) {
			System.out.println(map.get("push_id"));
		}
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#queryForMapList(java.lang.String, java.util.Map<String, Object>)}.
	 */
	@Test
	public void testQueryForMapListStringMap() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", 1);
		List<Map<String, Object>> ls = dao.queryForMapList("select * from T_USER  where id = :id", params);
		for (Map<String, Object> map : ls) {
			System.out.println(map.get("USER_NAME"));
		}
	}
	
	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#queryForPojoList(java.lang.String, java.lang.Object[], java.lang.Class)}.
	 */
	@Test
	public void testQueryForPojoListStringObjectArrayClassOfT() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#queryForPojoList(java.lang.Class, java.util.Map)}.
	 */
	@Test
	public void testQueryForPojoListClassOfTMapOfStringObject() {
		Map<String, Object> args = new HashMap<String, Object>();
		List<UserPO> users = dao.queryForPojoList(UserPO.class, args, "id");
		for (UserPO po : users) {
			System.out.println(po.isCanUpdate());
		}
		
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#queryForPojoList(java.lang.Class, java.lang.String, java.lang.Object[])}.
	 */
	@Test
	public void testQueryForPojoListClassOfTStringObjectArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#queryForSimpObject(java.lang.String, java.lang.Object[], java.lang.Class)}.
	 */
	@Test
	public void testQueryForSimpObject() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#queryForSimpObjectList(java.lang.String, java.lang.Object[], java.lang.Class)}.
	 */
	@Test
	public void testQueryForSimpObjectList() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#queryForPojoOne(java.lang.Class, java.util.Map)}.
	 */
	@Test
	public void testQueryForPojoOne() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#queryCallback(java.lang.String, java.lang.Object[], cn.org.zeronote.orm.dao.ResultSetExtractor)}.
	 */
	@Test
	public void testQueryCallback() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#update(java.lang.String, java.lang.Object[])}.
	 */
	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#updateByLogic(java.lang.Object[])}.
	 */
	@Test
	public void testUpdateByLogic() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#updateByPhysical(java.lang.Object[])}.
	 */
	@Test
	public void testUpdateByPhysical() {
	    UserInfoPO pojo = new UserInfoPO();
	    pojo.setPkId(2L);
	    pojo.setEmail("abc@email.com");
		dao.updateByPhysical(pojo);
		System.out.println("fin");
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#deleteByLogic(java.lang.Object[])}.
	 */
	@Test
	public void testDeleteByLogic() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testDelete() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("id", 1L);
		args.put("userId", 1L);
		int i = dao.delete(UserNotifyPO.class, args);
		System.out.println("size:" + i);
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#deleteByPhysical(java.lang.Object[])}.
	 */
	@Test
	public void testDeleteByPhysical() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testDeleteByPhysicalClzObject() {
		int i = dao.deleteByPhysical(UserNotifyPO.class, 1L);
		System.out.println("size:" + i);
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#insert(java.lang.Object[])}.
	 */
	@Test
	public void testInsert() {
		UserNotifyPO poa = new UserNotifyPO();
		poa.setUserId(1L);
		poa.setType(1);
		int i = dao.insert(poa);
		System.out.println("size:" + i);
		
		ActiveDevicePO po = new ActiveDevicePO();
		po.setAgent("test0");
		po.setChannel("test0");
		po.setCreateTime(new Date());
		po.setImei("1230");
		po.setPlatform("test0");
		po.setVendor("test0");
		po.setVersion("10");
		
		ActiveDevicePO po1 = new ActiveDevicePO();
		po1.setId(1L);
		po1.setAgent("test1");
		po1.setChannel("test1");
		po1.setCreateTime(new Date());
		po1.setImei("1231");
		po1.setPlatform("test1");
		po1.setVendor("test1");
		po1.setVersion("11");
		dao.insert(po, po1);
		System.out.println(po1.getId());
		System.out.println(po.getId());
		
		UserPO user = new UserPO();
		user.setId(123455L);
		dao.insert(user);
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#batchUpdate(java.lang.String, java.util.List)}.
	 */
	@Test
	public void testBatchUpdateStringListOfObject() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.DefaultCommonDao#batchUpdateByLogic(java.lang.Class, T[])}.
	 */
	@Test
	public void testBatchUpdateClassOfTTArray() {
		fail("Not yet implemented");
	}

}
