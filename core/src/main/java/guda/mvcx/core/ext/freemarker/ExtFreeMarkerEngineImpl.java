package guda.mvcx.core.ext.freemarker;

import freemarker.cache.FileTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import guda.mvcx.core.util.JsonConfigUtil;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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

    public ExtFreeMarkerEngineImpl(JsonObject jsonConfig) {
        String baseDir = jsonConfig.getString(JsonConfigUtil.templateDirKey);
        if (baseDir == null) {
            throw new RuntimeException("template dir cannot null");
        }
        File file = new File(baseDir);
        if (!file.exists()) {
            throw new RuntimeException("template dir cannot find:" + baseDir);
        }
        config = new Configuration(Configuration.VERSION_2_3_22);
        try {
            loader = new FileTemplateLoader(file, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        config.setTemplateLoader(loader);

        config.setSharedVariable("equal", new EqualTemplateMethodModelEx());
        config.setSharedVariable("enum2map", new EnumToMapDirective());

        JsonObject server = jsonConfig.getJsonObject(JsonConfigUtil.serverKey);
        if (server != null) {
            server.forEach(host -> {
                try {
                    config.setSharedVariable(host.getKey(), host.getValue());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });

        }
        JsonObject tools = jsonConfig.getJsonObject(JsonConfigUtil.freemarkerToolKey);

        if(tools!=null){
            BeansWrapperBuilder builder = new BeansWrapperBuilder(Configuration.VERSION_2_3_21);
            builder.setUseModelCache(true);
            builder.setExposeFields(true);
            BeansWrapper beansWrapper = builder.build();
            tools.forEach(tool->{
                TemplateHashModel templateHashModel = useStaticPackage(beansWrapper, String.valueOf(tool.getValue()));
                if(templateHashModel!=null){
                    config.setSharedVariable(tool.getKey(), templateHashModel);
                }

            });


        }




    }

    public static TemplateHashModel useStaticPackage(BeansWrapper beansWrapper, String packageName) {
        try {
            TemplateHashModel staticModels = beansWrapper.getStaticModels();
            TemplateHashModel fileStatics = (TemplateHashModel) staticModels.get(packageName);
            return fileStatics;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
