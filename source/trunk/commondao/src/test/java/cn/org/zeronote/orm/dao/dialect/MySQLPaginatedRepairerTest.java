/**
 * 
 */
package cn.org.zeronote.orm.dao.dialect;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.org.zeronote.orm.PaginationSupport;
import cn.org.zeronote.orm.RowSelection;

/**
 * @author <a href='mailto:lizheng8318@gmail.com'>leon</a>
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/test_cn.org.zeronote.orm.MySQLPaginatedRepairer.xml"})
public class MySQLPaginatedRepairerTest {

	public static final class TestPO {
		private long id;
		private String case_title;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getCase_title() {
			return case_title;
		}
		public void setCase_title(String case_title) {
			this.case_title = case_title;
		}
	}
	@Autowired
	private MySQLPaginatedRepairer instance;
	
	@Autowired
	private DataSource dataSource;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link cn.org.zeronote.orm.dao.dialect.MySQLPaginatedRepairer#queryForPaginatedPojoList(javax.sql.DataSource, java.lang.String, java.lang.Object[], java.lang.Class, cn.org.zeronote.orm.RowSelection)}.
	 */
	@Test
	public void testQueryForPaginatedPojoList() {
		String sql = "select * from t_litigation_record";
		Object[] args = {};
		Class<?> pojoType = TestPO.class;
		RowSelection rowSelection = new RowSelection(0, 10, "id");
		PaginationSupport<?> ps = instance.queryForPaginatedPojoList(dataSource, sql, args, pojoType, rowSelection);
		System.out.println(ps.getObject().size());
	}

}
