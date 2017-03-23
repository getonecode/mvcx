package guda.mvcx.demo.dao;


import guda.mvcx.core.annotation.dao.DAO;
import guda.mvcx.demo.model.UserDO;

/**
 * Created by well on 2017/3/20.
 */
@DAO
public interface UserDAO {


    UserDO getUser(String username);
}
