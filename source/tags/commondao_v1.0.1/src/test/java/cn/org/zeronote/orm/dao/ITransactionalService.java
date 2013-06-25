/**
 * 
 */
package cn.org.zeronote.orm.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional(
			propagation = Propagation.REQUIRED,
			rollbackFor={Exception.class}
			)
	void transcationalSomething() throws Exception;
}
