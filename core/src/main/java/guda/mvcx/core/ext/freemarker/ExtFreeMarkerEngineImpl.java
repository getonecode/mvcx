package guda.mvcx.core.ext.freemarker;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.TemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by well on 2017/3/22.
 */
public class ExtFreeMarkerEngineImpl implements TemplateEngine {

    private final Configuration config;
    private final FileTemplateLoader loader;

    public ExtFreeMarkerEngineImpl(String baseDir){
        if(baseDir==null){
            throw new RuntimeException("template dir cannot null");
        }
        File file=new File(baseDir);
        if(!file.exists()){
            throw new RuntimeException("template dir cannot find:"+baseDir);
        }
        config = new Configuration(Configuration.VERSION_2_3_22);
        try{
            loader=new FileTemplateLoader(file,false);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        config.setTemplateLoader(loader);

    }

    @Override
    public void render(RoutingContext context, String templateFileName, Handler<AsyncResult<Buffer>> handler) {
        try {
            Template template = config.getTemplate(templateFileName);
            Map<String, RoutingContext> variables = new HashMap<>(1);
            variables.put("context", context);
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                template.process(variables, new OutputStreamWriter(baos));
                handler.handle(Future.succeededFuture(Buffer.buffer(baos.toByteArray())));
            }
        } catch (Exception ex) {
            handler.handle(Future.failedFuture(ex));
        }
    }
}
