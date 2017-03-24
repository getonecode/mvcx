package guda.mvcx.core.ext.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import guda.mvcx.core.util.EnumUtil;

import java.io.IOException;
import java.util.Map;

/**
 * Created by well on 2017/3/24.
 */
public class EnumToMapDirective implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (params == null || params.size() == 0) {
            throw new TemplateException("params can not be empty", env);
        }
        String clazz= params.get("class").toString();
        String keyMethod= params.get("keyMethod").toString();
        String valueMethod= params.get("valueMethod").toString();
        Map<Object, Object> objectObjectMap = EnumUtil.toMap(clazz, keyMethod, valueMethod);
        env.setVariable("inner_map", env.getObjectWrapper().wrap(objectObjectMap));
        if (body != null) {
            body.render(env.getOut());
        }
    }
}
