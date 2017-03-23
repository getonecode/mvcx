package guda.mvcx.demo.dao;


import guda.mvcx.core.annotation.dao.DAO;
import guda.mvcx.demo.model.UserDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by well on 2017/3/20.
 */
@DAO
public interface UserDAO {


    @Select("SELECT * FROM user WHERE username = #{username}")
    UserDO getUser(@Param("username") String username);
}
