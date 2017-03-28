package guda.mvcx.demo;

import guda.mvcx.core.AutoVerticle;
import guda.mvcx.core.eventbus.context.AppContext;
import guda.mvcx.core.eventbus.context.AppContextImpl;
import guda.mvcx.core.factory.GuiceBeanFactory;
import guda.mvcx.core.util.JsonConfigUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.impl.Args;
import io.vertx.core.impl.VertxFactoryImpl;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.VertxFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by well on 2017/3/24.
 */
public class ServerStart {

    public static void main(String[] sargs){

        Args args = new Args(sargs);
        String confArg = args.map.get("-conf");
        if(confArg==null){
            confArg="dev";
        }

        System.out.println("server start use conf:" + confArg);

        JsonObject config= JsonConfigUtil.getConfig(ServerStart.class).getJsonObject(confArg);
        JsonObject sys = config.getJsonObject("sys");
        sys.forEach(entry->{
            System.getProperties().put(entry.getKey(),entry.getValue());
        });

        AppContext appContext=AppContext.create(config);

        VertxFactory factory = new VertxFactoryImpl();
        final Vertx vertx = factory.vertx();

        final DeploymentOptions deploymentOptions = readOpts();
        deploymentOptions.setConfig(config);

        AutoVerticle autoVerticle=new AutoVerticle(appContext);
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
        //options.setHa(false);
        options.setInstances(1);
        //options.setWorker(false);
        //options.setMultiThreaded(false);
        return options;

    }
}
