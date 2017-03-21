package guda.mvcxdemo.biz.impl;

import com.google.inject.Inject;
import guda.mvcxdemo.biz.UserService;
import guda.mvcxdemo.dao.UserDAO;
import guda.mvcxdemo.model.UserDO;

/**
 * Created by well on 2017/3/20.
 */

public class UserServiceImpl implements UserService {


    @Inject
    private UserDAO userDAO;
    @Override
    public UserDO index() {
        return userDAO.getUser("admin");
    }
}