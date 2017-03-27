package guda.mvcx.core.factory;

import com.google.inject.*;
import guda.mvcx.core.annotation.action.Action;
import guda.mvcx.core.annotation.biz.Biz;
import io.vertx.core.json.JsonObject;
import org.mybatis.guice.XMLMyBatisModule;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by well on 2017/3/20.
 */
public class GuiceBeanFactory implements AppBeanFactory{

    private Logger log = LoggerFactory.getLogger(getClass());

    private JsonObject config;
    private Injector injector;
    private List<Class> actionClassList = new ArrayList<>();



    public GuiceBeanFactory(JsonObject jsonObject) {
        config = jsonObject;
        setupGuice();

    }


    public void setupGuice() {
        JsonObject dbConfig = config.getJsonObject("db");
        List<Module> moduleList = new ArrayList<>();
        if (dbConfig != null) {
            Module mybatisModule = createMybatisModule(dbConfig);
            if (mybatisModule != null) {
                moduleList.add(mybatisModule);
            }
        }
        String bizPackage = config.getString("biz.package");
        Module bizModule = createBizModule(bizPackage);
        if (bizModule != null) {
            moduleList.add(bizModule);
        }

        String actionPackage = config.getString("action.package");
        Module actionModule = createActionModule(actionPackage);
        if (actionModule != null) {
            moduleList.add(actionModule);
        }

        injector = Guice.createInjector(Stage.PRODUCTION, moduleList);
    }

    private Module createMybatisModule(JsonObject dbConfig) {
        if (dbConfig == null) {
            return null;
        }
        return new XMLMyBatisModule() {
            @Override
            protected void initialize() {
                setEnvironmentId(dbConfig.getString("environment.id"));
            }

        };


    }

    private Module createBizModule(String bizPackage) {
        if (bizPackage == null) {
            return null;
        }
        return new Module() {
            @Override
            public void configure(Binder binder) {
                String[] ps = bizPackage.split(",");
                for (String s : ps) {
                    try {
                        Reflections reflections = new Reflections(s);
                        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Biz.class, true);
                        classes.forEach(clazz -> {
                            if (log.isInfoEnabled()) {
                                log.info("bind class:" + clazz.getName());
                            }
                            binder.bind(clazz);
                        });
                    } catch (Throwable e) {
                        throw new UnsupportedOperationException("can't add biz classes");
                    }
                }
            }
        };
    }

    private Module createActionModule(String actionPackage) {
        if (actionPackage == null) {
            return null;
        }
        return new Module() {
            @Override
            public void configure(Binder binder) {
                String[] ps = actionPackage.split(",");
                for (String s : ps) {
                    try {
                        Reflections reflections = new Reflections(s);
                        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Action.class, true);
                        classes.forEach(clazz -> {
                            if (log.isInfoEnabled()) {
                                log.info("bind class:" + clazz.getName());
                            }
                            binder.bind(clazz);
                            actionClassList.add(clazz);
                        });
                    } catch (Throwable e) {
                        throw new UnsupportedOperationException("can't add action classes");
                    }
                }
            }
        };
    }




    public Injector getInjector() {
        return injector;
    }


    @Override
    public <T> T getBean(Class<T> requireType) {
        if(injector==null){
            return null;
        }
        return injector.getInstance(requireType);
    }



    @Override
    public List<Class> getActionClassList() {
        return actionClassList;
    }
}