package guda.mvcxdemo.form;

/**
 * Created by well on 2017/3/20.
 */
public class FormTest {

    public static void main(String[] args){
        UserForm userForm = new UserForm();
        userForm.validate();
        System.out.println(userForm.getErrorResult().get("userName"));
    }
}
