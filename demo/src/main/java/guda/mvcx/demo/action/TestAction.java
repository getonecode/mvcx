package guda.mvcx.demo.action;

import guda.mvcx.core.annotation.action.Action;
import guda.mvcx.core.annotation.action.Req;

/**
 * Created by well on 2017/3/27.
 */
@Action
public class TestAction {

    @Req("test.*")
    public String test(){
        return "test.ftl";
    }

    @Req("abc.*")
    public String testabc(){
        throw new RuntimeException("abc");

    }
}
