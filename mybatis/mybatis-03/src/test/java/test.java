import com.Dao.StudentsMapper;
import com.Dao.TeacherMapper;
import com.Pojo.Students;
import com.Pojo.Teacher;
import com.Utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @auther harfe
 * @date 2022/2/12 15:38
 */
public class test {
    @Test
    public void testStudent(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentsMapper mapper = sqlSession.getMapper(StudentsMapper.class);
        List<Students> student = mapper.getStudent();
        for (Students students : student
        ) {
            System.out.println(students);
        }
        sqlSession.close();
    }
    @Test
    public void testStudent2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentsMapper mapper = sqlSession.getMapper(StudentsMapper.class);
        List<Students> student = mapper.getStudent2();
        for (Students students : student
        ) {
            System.out.println(students);
        }
        sqlSession.close();
    }
}
