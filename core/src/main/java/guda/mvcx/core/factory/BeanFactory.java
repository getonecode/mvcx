package guda.mvcx.core.factory;

/**
 * Created by well on 2017/3/27.
 */
public interface BeanFactory {


    <T> T getBean(Class<T> requireType) ;


}
