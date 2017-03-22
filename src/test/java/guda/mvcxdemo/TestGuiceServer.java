package guda.mvcxdemo;

import guda.mvcx.AutoVerticle;
import guda.mvcx.GuiceBeanFactory;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxFactoryImpl;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.VertxFactory;

import java.util.Map;

/**
 * Created by well on 2017/3/21.
 */
public class TestGuiceServer {

    public static void main(String[] args){
        JsonObject config=JsonConfigUtil.getConfig().getJsonObject("dev");
        JsonObject sys = config.getJsonObject("sys");
        sys.forEach(entry->{
            System.getProperties().put(entry.getKey(),entry.getValue());
        });

        GuiceBeanFactory guiceBeanFactory =new GuiceBeanFactory(config);



        VertxFactory factory = new VertxFactoryImpl();
        final Vertx vertx = factory.vertx();

        final DeploymentOptions deploymentOptions = readOpts();
        deploymentOptions.setConfig(config);

        AutoVerticle autoVerticle=new AutoVerticle(guiceBeanFactory);
        vertx.deployVerticle(autoVerticle,deploymentOptions,res -> {
            if (res.succeeded()) {
                System.out.println("Deployment id is: " + res.result());
            } else {
                System.out.println("Deployment failed!");
                res.cause().printStackTrace();
            }
        });

    }

    public static DeploymentOptions readOpts(){
        final DeploymentOptions options = new DeploymentOptions();
        options.setHa(false);
        options.setInstances(1);
        options.setWorker(false);
        options.setMultiThreaded(false);
        return options;

    }
}
