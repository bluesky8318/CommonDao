/**
 * 
 */
package cn.org.zeronote.orm.dao.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cn.org.zeronote.orm.testpo.UserPO;

/**
 * @author <a href='mailto:lizheng8318@gmail.com'>lizheng</a>
 *
 */
public class SqlUpdGeneratorTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test method for {@link cn.org.zeronote.orm.dao.parser.SqlUpdGenerator#getSql()}.
     * @throws NoSuchFieldException 
     * @throws IllegalAccessException 
     * @throws SecurityException 
     * @throws IllegalArgumentException 
     */
    @Test
    public void testGetSql() throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        UserPO po = new UserPO();
        po.setId(1L);
        SqlUpdGenerator generator = new SqlUpdGenerator(po, true);
        System.out.println(generator.getSql());
        Object[] args = generator.getArgs();
        for (Object arg : args) {
            System.out.print(arg + ",");
        }
        
    }

}
