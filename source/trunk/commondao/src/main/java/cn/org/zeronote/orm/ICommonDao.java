/**
 * 
 */
package cn.org.zeronote.orm;

import java.util.List;
import java.util.Map;

import cn.org.zeronote.orm.dao.ResultSetExtractor;


/**
 * 通用数据访问接口
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public interface ICommonDao {

	/**
	 * 执行带命名参数的查询，根据分页设置返回结果集，将结果集按照设定的转换规则转换为Pojo列表。
	 * @param sql
	 * @param args
	 * @param pojoType
	 * @param rowSelection
	 * @return
	 * @throws DataAccessException
	 */
	public <T> PaginationSupport<T> queryForPaginatedPojoList(String sql, Object[] args, Class<T> pojoType,
			RowSelection rowSelection)
			throws DataAccessException;
	
	/**
	 * 执行带命名参数的查询，根据分页设置返回结果集，将结果集按照设定的转换规则转换为Pojo列表。
	 * @param pojoType
	 * @param args
	 * @param rowSelection
	 * @return
	 * @throws DataAccessException
	 */
	public <T> PaginationSupport<T> queryForPaginatedPojoList(Class<T> pojoType, Map<String, Object> args,
			RowSelection rowSelection)
			throws DataAccessException;
	
	/**
	 * 执行带参数的查询得到的结果集转换成一个列表，列表中的每个元素为一个Map，对应原结果集的每条记录。
	 * 结果集的列名对应Map中的key，结果集中的值对应Map中的value。 参数列表由args给出。
	 *
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String,Object>> queryForMapList(String sql, Object... args) throws DataAccessException;
	
	/**
	 * 执行带参数的查询得到的结果集转换成一个列表，列表中的每个元素为一个Map，对应原结果集的每条记录。
	 * <p>
	 * 结果集的列名对应Map中的key，结果集中的值对应Map中的value。
	 * <br>
	 * 参数：
	 * <br>
	 * sql:使用":字段名"方式设置参数，冒号与字段名之间不允许有空白，字段名区分大小写，字段名为参数params的map的key值
	 * <br>
	 * params:以key/value方式存储sql的参数值
	 * </p>
	 *
	 * @param sql
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String,Object>> queryForMapList(String sql, Map<String, Object> params) throws DataAccessException;
	
	/**
	 * 执行带参数查询，将得到的结果集拼装成一个pojo列表，列表中的每一个元素为一个pojo，从原结果集中的记
	 * 录转换而来。转换的规则是将结果集的列名和pojo的属性名进行匹配（不区分大小写，所以pojo中不能有同名
	 * 的多个属性），匹配上则将结果集中的这列的值赋给pojo的对应的属性。
	 * 默认情况下，clob字段会转换为String类型，而不是保持Clob类型，但是blob字段还是保持为Blob类型或 者InputStream类型。
	 * @param <T>
	 * @param sql
	 * @param args
	 * @param pojoType
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> queryForPojoList(String sql, Object[] args, Class<T> pojoType) throws DataAccessException;
	
	/**
	 * 执行带参数查询，将得到的结果集拼装成一个pojo列表，列表中的每一个元素为一个pojo，从原结果集中的记
	 * 录转换而来。转换的规则是将结果集的列名和pojo的属性名进行匹配（不区分大小写，所以pojo中不能有同名
	 * 的多个属性），匹配上则将结果集中的这列的值赋给pojo的对应的属性。
	 * 默认情况下，clob字段会转换为String类型，而不是保持Clob类型，但是blob字段还是保持为Blob类型或 者InputStream类型。
	 * 
	 * <p>
	 * 参数：
	 * <br>
	 * sql:使用":字段名"方式设置参数，冒号与字段名之间不允许有空白，字段名区分大小写，字段名为参数params的map的key值
	 * <br>
	 * params:以key/value方式存储sql的参数值
	 * </p>
	 * 
	 * @param <T>
	 * @param sql
	 * @param params
	 * @param pojoType
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> queryForPojoList(String sql, Map<String, Object> params, Class<T> pojoType) throws DataAccessException;
	
	
	/**
	 * 执行带参数查询，将得到的结果集拼装成一个pojo列表，列表中的每一个元素为一个pojo，从原结果集中的记
	 * 录转换而来。转换的规则是将结果集的列名和pojo的属性名进行匹配（不区分大小写，所以pojo中不能有同名
	 * 的多个属性），匹配上则将结果集中的这列的值赋给pojo的对应的属性。
	 * 默认情况下，clob字段会转换为String类型，而不是保持Clob类型，但是blob字段还是保持为Blob类型或 者InputStream类型。
	 * 
	 * 没有查询参数，使用AutoAssemble自动装配
	 * 
	 * @param <T>
	 * @param pojoType
	 * @param args
	 * @param requireFields	请求中必须填充的pojo field，如果为空，则全部获取
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> queryForPojoList(Class<T> pojoType, Map<String, Object> args, String... requireFields) throws DataAccessException;
	
	/**
	 * 如果查询返回的结果集只有一列一行，可以用这个方法快捷的得到结果集中的对象，对象类型 与传入的类型必须匹配。
	 * @param <T>
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T queryForSimpObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException;
	
	/**
	 * 如果查询返回的结果集只有一列简单类型(int/double/string)，可以用这个方法快捷的得到结果集中的对象，对象类型 与传入的类型必须匹配。
	 * @param <T>
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> queryForSimpObjectList(String sql, Object[] args, Class<T> requiredType) throws DataAccessException;
	
	/**
	 * 执行带参数查询，将得到的结果集拼装成一个pojo列表，列表中的每一个元素为一个pojo，从原结果集中的记
	 * 录转换而来。转换的规则是将结果集的列名和pojo的属性名进行匹配（不区分大小写，所以pojo中不能有同名
	 * 的多个属性），匹配上则将结果集中的这列的值赋给pojo的对应的属性。
	 * 默认情况下，clob字段会转换为String类型，而不是保持Clob类型，但是blob字段还是保持为Blob类型或 者InputStream类型。
	 * 
	 * 没有查询参数，使用AutoAssemble自动装配
	 * 
	 * @param <T>
	 * @param pojoType
	 * @param col		参数列，对应PojoType的成员变量
	 * @param args		该参数的值，会拼接为in方式，或者col=arg0 or col=arg1 方式
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> queryForPojoList(Class<T> pojoType, String col, Object[] args, String... requireFields) throws DataAccessException;
	
	/**
	 * 执行全部查询，查询指定条件的第一条记录
	 * <br>
	 * 针对wasp的特殊要求增加的查询接口
	 * @param <T>
	 * @param pojoType
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T queryForPojoOne(Class<T> pojoType, Map<String, Object> args) throws DataAccessException;
	
	/**
	 * 执行查询回调
	 * @param <T>
	 * @param sql
	 * @param args
	 * @param resultSetExtractor
	 * @return
	 */
	public <T> T queryCallback(String sql, Object[] args, ResultSetExtractor<T> resultSetExtractor)  throws DataAccessException;
	
	/**
	 * 执行带参数的sql，参数列表必须按照参数在sql语句中出现的顺序组织。 参数在sql中用"?"表示
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	public int update(String sql, Object... args) throws DataAccessException;
	
	/**
	 * 更新对象到数据库
	 * <br>
	 * 按照逻辑主键进行更新
	 * @param pojos
	 * @return
	 * @throws DataAccessException
	 */
	public int updateByLogic(Object... pojos) throws DataAccessException;
	
	/**
	 * 更新对象到数据库
	 * <br>
	 * 按照物理主键进行更新
	 * @param pojos
	 * @return
	 * @throws DataAccessException
	 */
	public int updateByPhysical(Object... pojos) throws DataAccessException;
	
	/**
	 * 从数据库中删除对象
	 * <br>
	 * 按照逻辑主键进行更新
	 * @param pojos
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteByLogic(Object... pojos) throws DataAccessException;
	
	/**
	 * 从数据库中删除对象
	 * <br>
	 * 按照物理主键进行更新
	 * @param pojos
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteByPhysical(Object... pojos) throws DataAccessException;
	
	/**
	 * 条件删除
	 * @param pojoType
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	public <T> int delete(Class<T> pojoType, Map<String, Object> args) throws DataAccessException;
	
	/**
	 * 条件删除，条件为逻辑主键
	 * <br>
	 * 仅支持独立主键的情况，不支持联合主键
	 * <br>
	 * 如果是散列表，所用主键必须是散列键
	 * @param pojoType
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	public <T> int deleteByLogic(Class<T> pojoType, Object... args) throws DataAccessException;
	
	/**
	 * 条件删除，条件为物理主键
	 * <br>
	 * 仅支持独立主键的情况，不支持联合主键
	 * <br>
	 * 如果是散列表，所用主键必须是散列键
	 * @param pojoType
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	public <T> int deleteByPhysical(Class<T> pojoType, Object... args) throws DataAccessException;
	
	/**
	 * 将对象插入数据库
	 * <br>
	 * 批量插入操作，不检查主键约束
	 * @param pojos
	 * @return
	 * @throws DataAccessException
	 */
	public int insert(Object... pojos) throws DataAccessException;
	
	/**
	 * 批量执行多个更新操作
	 * @param sql
	 * @param argsList
	 * @return 各个更新操作对应的更新记录数数组
	 * @throws DataAccessException
	 */
	public int[] batchUpdate(String sql, List<Object[]> argsList) throws DataAccessException;
	
	/**
	 * 批量执行多个更新操作
	 * <br>
	 * 按照逻辑主键进行更新
	 * @param <T>
	 * @param pojoType	pojo
	 * @param argsList
	 * @return	各个更新操作对应的更新记录数数组
	 * @throws DataAccessException
	 */
	public <T> int[] batchUpdateByLogic(Class<T> pojoType, T... pojos) throws DataAccessException;
	
	/**
	 * 批量执行多个更新操作
	 * <br>
	 * 按照物理主键进行更新
	 * @param <T>
	 * @param pojoType	pojo
	 * @param argsList
	 * @return	各个更新操作对应的更新记录数数组
	 * @throws DataAccessException
	 */
	public <T> int[] batchUpdateByPhysical(Class<T> pojoType, T... pojos) throws DataAccessException;
}
