package guda.mvcx.core.ext.freemarker;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * Created by well on 2017/3/24.
 */
public class EqualTemplateMethodModelEx implements TemplateMethodModelEx {

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if(arguments.size()!=2){
            throw new RuntimeException("enum to map param at least 3");
        }
        String val1= String.valueOf(arguments.get(0));
        String val2= String.valueOf(arguments.get(1));

        return val1.equals(val2);
    }
}
