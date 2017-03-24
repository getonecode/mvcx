package guda.mvcx.gen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by well on 2017/3/28.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GenField {

    boolean ignore() default false;

    String cn();

    int order() default 0;

    boolean inSearchForm() default false;

    boolean canNull() default false;
}
