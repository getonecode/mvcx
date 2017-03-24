package guda.mvcx.demo.dao;


import guda.mvcx.demo.dao.model.UserDO;

/**
 * Created by well on 2017/3/20.
 */
public interface UserDAO {

    UserDO getUser(String username);
}
