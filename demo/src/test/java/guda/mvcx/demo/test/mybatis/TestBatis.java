package guda.mvcx.demo.test.mybatis;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;

import guda.mvcx.core.annotation.biz.Biz;
import guda.mvcx.core.annotation.dao.DAO;
import guda.mvcx.demo.biz.UserService;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.druid.DruidDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;
import org.reflections.Reflections;

import java.util.Properties;
import java.util.Set;

/**
 * Created by well on 2017/3/20.
 */
public class TestBatis {

    public static void main(String[] args) {
        Properties myBatisProperties = new Properties();
        myBatisProperties.setProperty("mybatis.environment.id", "test");

        myBatisProperties.setProperty("JDBC.driverClassName", "com.mysql.jdbc.Driver");
        myBatisProperties.setProperty("JDBC.host", "127.0.0.1");
        myBatisProperties.setProperty("JDBC.port", "3306");
        myBatisProperties.setProperty("JDBC.schema", "demo");
        myBatisProperties.setProperty("JDBC.username", "demo_user");
        myBatisProperties.setProperty("JDBC.password", "demo_pwd");
        myBatisProperties.setProperty("JDBC.autoCommit", "false");

        Injector injector = Guice.createInjector(new Module[]{new MyBatisModule() {
            @Override
            protected void initialize() {
                install(JdbcHelper.MySQL);
                bindDataSourceProviderType(DruidDataSourceProvider.class);
                bindTransactionFactoryType(JdbcTransactionFactory.class);
                Names.bindProperties(binder(), myBatisProperties);
                addMapperClasses();
                //bind(UserService.class).to(UserServiceImpl.class);
            }


            private void addMapperClasses() {
                try {
                    Reflections reflections = new Reflections("guda.vertxdemo");
                    Set<Class<?>> classes = reflections.getTypesAnnotatedWith(DAO.class);
                    addMapperClasses(classes);
                } catch (Throwable e) {
                    throw new UnsupportedOperationException("can't add dao classes");
                }
            }
        }, new Module() {

            @Override
            public void configure(Binder binder) {
                try {
                    Reflections reflections = new Reflections("guda.vertxdemo");
                    Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Biz.class);
                    classes.forEach(clazz -> {
                        binder.bind(clazz);
                    });
                } catch (Throwable e) {
                    throw new UnsupportedOperationException("can't add biz classes");
                }
            }
        }});


        UserService instance = injector.getInstance(UserService.class);
        System.out.println(instance.index().getUsername());
    }


}
