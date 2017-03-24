package guda.mvcx.demo.biz.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import guda.mvcx.demo.biz.UserService;
import guda.mvcx.demo.dao.UserDAO;
import guda.mvcx.demo.dao.model.UserDO;


/**
 * Created by well on 2017/3/20.
 */
@Singleton
public class UserServiceImpl implements UserService {


    @Inject
    private UserDAO userDAO;
    @Override
    public UserDO index() {
        return userDAO.getUser("admin");
    }
}
