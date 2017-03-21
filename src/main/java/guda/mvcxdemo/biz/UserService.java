package guda.mvcxdemo.biz;

import com.google.inject.ImplementedBy;
import guda.mvcx.annotation.biz.Biz;
import guda.mvcxdemo.biz.impl.UserServiceImpl;
import guda.mvcxdemo.model.UserDO;

/**
 * Created by well on 2017/3/20.
 */
@Biz
@ImplementedBy(UserServiceImpl.class)
public interface UserService {

    UserDO index();
}
