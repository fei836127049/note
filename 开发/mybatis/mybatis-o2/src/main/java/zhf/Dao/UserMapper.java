package zhf.Dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import zhf.pojo.Users;

import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.Map;

/**
 * @author zhf
 * @date 2022/1/6
 */
public interface UserMapper {
    //使用注解进行开发
    @Select("select * from user")
    List<Users> getUser();

    //方法存在多个参数，所有参数前面必须加上@Param（“”）注解
    @Select("select * from Users where id = #{id}")
    Users getUserById(@Param("id") int id);
}
