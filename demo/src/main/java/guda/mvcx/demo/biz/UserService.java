package guda.mvcx.demo.biz;

import com.google.inject.ImplementedBy;
import guda.mvcx.core.annotation.biz.Biz;
import guda.mvcx.demo.biz.impl.UserServiceImpl;
import guda.mvcx.demo.model.UserDO;


/**
 * Created by well on 2017/3/20.
 */
@Biz
@ImplementedBy(UserServiceImpl.class)
public interface UserService {

    UserDO index();
}
