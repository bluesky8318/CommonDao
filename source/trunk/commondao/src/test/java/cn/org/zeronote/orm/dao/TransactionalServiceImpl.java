/**
 * 
 */
package cn.org.zeronote.orm.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author leon
 *
 */
@Repository
@Transactional
public class TransactionalServiceImpl implements ITransactionalService {

	@Autowired
	@Qualifier("dbDao")
	private DefaultCommonDao dao;
	
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 事务处理接口
	 * <br>
	 * 当遇到Exception后，事物回滚
	 */
	@Transactional(
			propagation = Propagation.REQUIRED,
			rollbackFor={Exception.class}
			)
	@Override
	public void transcationalSomething() throws Exception {
		dao.update("INSERT INTO T_USER(USER_NAME) VALUES(?)", "谁1");
		dao.update("INSERT INTO T_USER(USER_NAME) VALUES(?)", "谁2");
		throw new Exception("Rollback");
	}

	/**
	 * 事务处理接口
	 * <br>
	 * 当遇到Exception后，事物回滚
	 */
	@Transactional(
			propagation = Propagation.REQUIRED,
			rollbackFor={Exception.class}
			)
	@Override
	public void transcationalSomethingTx() throws Exception {
		jdbcTemplate.update("INSERT INTO T_USER(USER_NAME) VALUES(?)", "谁3");
		jdbcTemplate.update("INSERT INTO T_USER(USER_NAME) VALUES(?)", "谁4");
		throw new Exception("Rollback");
	}

}
