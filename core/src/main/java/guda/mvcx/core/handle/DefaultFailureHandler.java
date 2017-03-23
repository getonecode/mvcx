package guda.mvcx.core.handle;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.TemplateEngine;

/**
 * Created by well on 2017/3/23.
 */
public class DefaultFailureHandler implements Handler<RoutingContext> {

    private TemplateEngine templateEngine;

    private String errorFile;

    public DefaultFailureHandler(TemplateEngine engine,String errorFileName){
        templateEngine=engine;
        errorFile=errorFileName;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        templateEngine.render(routingContext, errorFile, res -> {
            if (res.succeeded()) {
                routingContext.response().end(res.result());
            } else {
                routingContext.response().end(DEFAULT_ERROR);
            }
        });
        return;
    }

    public String getErrorFile() {
        return errorFile;
    }

    public void setErrorFile(String errorFile) {
        this.errorFile = errorFile;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    private static final String DEFAULT_ERROR =
            "<html><body><h1>内部错误</h1></body></html>";
}
