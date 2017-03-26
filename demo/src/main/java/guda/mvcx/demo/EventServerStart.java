package guda.mvcx.demo;

import guda.mvcx.core.GuiceBeanFactory;
import guda.mvcx.core.eventbus.HttpConsumerVerticle;
import guda.mvcx.core.eventbus.HttpEventContext;
import guda.mvcx.core.eventbus.ContextMsgConvert;
import guda.mvcx.core.eventbus.EventBusVerticle;
import guda.mvcx.core.util.JsonConfigUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxFactoryImpl;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.VertxFactory;

/**
 * Created by well on 2017/3/25.
 */
public class EventServerStart {

    public static void main(String[] args){
        JsonObject config= JsonConfigUtil.getConfig(ServerStart.class).getJsonObject("dev");
        JsonObject sys = config.getJsonObject("sys");
        sys.forEach(entry->{
            System.getProperties().put(entry.getKey(),entry.getValue());
        });


        GuiceBeanFactory guiceBeanFactory =new GuiceBeanFactory(config);

        VertxFactory factory = new VertxFactoryImpl();
        final Vertx vertx = factory.vertx();

        final DeploymentOptions deploymentOptions = readOpts();
        deploymentOptions.setConfig(config);

        EventBusVerticle eventBusVerticle=new EventBusVerticle(guiceBeanFactory);
        vertx.deployVerticle(eventBusVerticle,deploymentOptions,res -> {
            if (res.succeeded()) {
                System.out.println("Deployment id is: " + res.result());
            } else {
                System.out.println("Deployment failed!");
                res.cause().printStackTrace();
            }
        });

        HttpConsumerVerticle httpConsumerVerticle =new HttpConsumerVerticle(guiceBeanFactory.getFullMatchActionMap(),guiceBeanFactory.getPatternRouteActionList());
        vertx.deployVerticle(httpConsumerVerticle,deploymentOptions,res -> {
            if (res.succeeded()) {
                System.out.println("Deployment id is: " + res.result());
            } else {
                System.out.println("Deployment failed!");
                res.cause().printStackTrace();
            }
        });

        HttpConsumerVerticle httpConsumerVerticle2 =new HttpConsumerVerticle(guiceBeanFactory.getFullMatchActionMap(),guiceBeanFactory.getPatternRouteActionList());
        vertx.deployVerticle(httpConsumerVerticle2,deploymentOptions,res -> {
            if (res.succeeded()) {
                System.out.println("Deployment id is: " + res.result());
            } else {
                System.out.println("Deployment failed!");
                res.cause().printStackTrace();
            }
        });

        vertx.eventBus().registerDefaultCodec(HttpEventContext.class, new ContextMsgConvert());

    }



    public static DeploymentOptions readOpts(){
        final DeploymentOptions options = new DeploymentOptions();
        //options.setHa(false);
        options.setInstances(1);
        //options.setWorker(false);
        //options.setMultiThreaded(false);
        return options;

    }
}
