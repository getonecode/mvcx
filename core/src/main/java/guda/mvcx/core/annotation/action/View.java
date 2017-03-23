package guda.mvcx.core.annotation.action;



import guda.mvcx.core.enums.ViewTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by well on 2017/3/21.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface View {

    ViewTypeEnum type() default ViewTypeEnum.template;
}
