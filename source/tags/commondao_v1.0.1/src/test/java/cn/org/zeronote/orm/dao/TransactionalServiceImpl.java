/**
 * 
 */
package cn.org.zeronote.orm.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;


/**
 * @author leon
 *
 */
@Repository
public class TransactionalServiceImpl implements ITransactionalService {

	@Autowired
	@Qualifier("dbDao")
	private DefaultCommonDao dao;
	
	/* (non-Javadoc)
	 * @see cn.org.zeronote.orm.dao.ITransactionalService#transcationalSomething()
	 */
	@Override
	public void transcationalSomething() throws Exception {
		dao.update("INSERT INTO T_USER(USER_NAME) VALUES(?)", "谁1");
		dao.update("INSERT INTO T_USER(USER_NAME) VALUES(?)", "谁1");
		throw new Exception("Rollback");
	}

}
