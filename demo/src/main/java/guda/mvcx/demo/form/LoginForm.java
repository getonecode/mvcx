package guda.mvcx.demo.form;

import guda.mvcx.core.helper.Form;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by well on 2017/3/23.
 */
public class LoginForm extends Form{

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
