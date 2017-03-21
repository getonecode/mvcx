package guda.mvcxdemo.form;

import guda.mvcx.Form;

import javax.validation.constraints.NotNull;

/**
 * Created by well on 2017/3/20.
 */
public class UserForm extends Form{

    @NotNull(message = "用户名不可空")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
