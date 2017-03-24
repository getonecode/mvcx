package guda.mvcx.gen.helper;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.vertx.core.buffer.Buffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * Created by well on 2017/3/24.
 */
public class FreemakerHelper {


    private static Configuration configuration;

    static{
        configuration = new Configuration(Configuration.VERSION_2_3_22);
        configuration.setTemplateLoader(new ClassTemplateLoader(FreemakerHelper.class,"/ftl"));
    }
    private FreemakerHelper(){


    }

    public static String render(String ftlName,Map<String,Object> data) throws IOException {
        Template template = configuration.getTemplate(ftlName);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            template.process(data, new OutputStreamWriter(baos));
            Buffer buffer = Buffer.buffer(baos.toByteArray());
            return buffer.toString();
        }catch(Exception e){

        }
        return null;
    }


}
