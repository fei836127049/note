import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import zhf.Dao.UserMapper;
import zhf.Utils.MybatisUtils;
import zhf.pojo.Users;

/**
 * @author zhf
 * @date 2022/1/20
 */
public class MapperTest {

    @Test
    public void test(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Users userById = mapper.getUserById(1);

        System.out.println(userById);
        sqlSession.close();
    }
}
