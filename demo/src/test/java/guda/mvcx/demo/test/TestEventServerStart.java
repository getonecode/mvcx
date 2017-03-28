package guda.mvcx.demo.test;

import guda.mvcx.core.eventbus.EventBusVerticle;
import guda.mvcx.core.eventbus.context.AppContext;
import guda.mvcx.core.eventbus.msg.HttpEventMsg;
import guda.mvcx.core.eventbus.msg.HttpMsgConvert;
import guda.mvcx.core.util.JsonConfigUtil;
import guda.mvcx.demo.ServerStart;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxFactoryImpl;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.VertxFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by well on 2017/3/27.
 */
public class TestEventServerStart {

    private static Logger log = LoggerFactory.getLogger(TestEventServerStart.class);

    public static void main(String[] args) {
        JsonObject config = JsonConfigUtil.getConfig(ServerStart.class).getJsonObject("dev");
        JsonObject sys = config.getJsonObject("sys");
        sys.forEach(entry -> {
            System.getProperties().put(entry.getKey(), entry.getValue());
        });

        AppContext appContext = AppContext.create(config);

        VertxFactory factory = new VertxFactoryImpl();
        final Vertx vertx = factory.vertx();

        final DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setConfig(config);

        EventBusVerticle eventBusVerticle = new EventBusVerticle(appContext);
        vertx.deployVerticle(eventBusVerticle, deploymentOptions, res -> {
            if (res.succeeded()) {
                log.info("Deployment main eventbusVerticle id is: " + res.result());
            } else {
                log.info("Deployment failed!");
                res.cause().printStackTrace();
            }
        });

        int instanceCount = config.getInteger("instance.biz.count", 3);
        vertx.deployVerticle("guda.mvcx.core.eventbus.HttpConsumerVerticle", readConsumeOpts(instanceCount), res -> {
            if (res.succeeded()) {
                log.info("Deployment id is: " + res.result());
            } else {
                log.info("Deployment failed!");
                res.cause().printStackTrace();
            }
        });

        vertx.eventBus().registerDefaultCodec(HttpEventMsg.class, new HttpMsgConvert());

    }


    public static DeploymentOptions readConsumeOpts(int instanceCount) {
        final DeploymentOptions options = new DeploymentOptions();
        options.setInstances(instanceCount);
        options.setWorker(true);
        options.setMultiThreaded(true);
        return options;

    }
}
