package guda.mvcx.core.factory;

import java.util.List;

/**
 * Created by well on 2017/3/27.
 */
public interface AppBeanFactory extends BeanFactory{

    public List<Class> getActionClassList();


}
