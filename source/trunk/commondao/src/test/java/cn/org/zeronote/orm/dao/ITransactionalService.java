/**
 * 
 */
package cn.org.zeronote.orm.dao;


/**
 * 事务处理测试类
 * @author leon
 *
 */
public interface ITransactionalService {

	/**
	 * 事务处理接口
	 * <br>
	 * 当遇到Exception后，事物回滚
	 */
	void transcationalSomething() throws Exception;
	
	/**
	 * spring jdbcTemplate 事务处理
	 * @throws Exception
	 */
	void transcationalSomethingTx() throws Exception;
}
