package com.dounine.corgi.jta.component;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * Created by huanghuanlai on 2016/11/16.
 */
public class JtaComponents {
    @Resource
    protected Environment env;

//    @Bean("dataSourceMaster")
//    public AtomikosDataSourceBean dataSourceMaster(){
//        AtomikosDataSourceBean adsb = new AtomikosDataSourceBean();
//        adsb.setPoolSize(3);
//        adsb.setUniqueResourceName("dataSourceMaster");
//        adsb.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
//        Properties properties = new Properties();
//        properties.setProperty("URL",env.getProperty("master.url"));
//        properties.setProperty("user",env.getProperty("master.username"));
//        properties.setProperty("password",env.getProperty("master.password"));
//        adsb.setXaProperties(properties);
//        return adsb;
//    }
//
//    @Bean("dataSourceSlave")
//    public AtomikosDataSourceBean dataSourceSlave(){
//        AtomikosDataSourceBean adsb = new AtomikosDataSourceBean();
//        adsb.setPoolSize(3);
//        adsb.setUniqueResourceName("dataSourceSlave");
//        adsb.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
//        Properties properties = new Properties();
//        properties.setProperty("URL",env.getProperty("slave.url"));
//        properties.setProperty("user",env.getProperty("slave.username"));
//        properties.setProperty("password",env.getProperty("slave.password"));
//        adsb.setXaProperties(properties);
//        return adsb;
//    }
//
//    @Bean("jdbcTemplateMaster")
//    public JdbcTemplate jdbcTemplateReader(@Qualifier("dataSourceMaster")AtomikosDataSourceBean dataSourceBean){
//        JdbcTemplate jdbcTemplate = new JdbcTemplate();
//        jdbcTemplate.setDataSource(dataSourceBean);
//        return jdbcTemplate;
//    }
//
//    @Bean("jdbcTemplateSlave")
//    public JdbcTemplate jdbcTemplateWriter(@Qualifier("dataSourceSlave") AtomikosDataSourceBean dataSourceBean){
//        JdbcTemplate jdbcTemplate = new JdbcTemplate();
//        jdbcTemplate.setDataSource(dataSourceBean);
//        return jdbcTemplate;
//    }

    @Bean(initMethod = "init",destroyMethod = "close")
    public UserTransactionManager userTransactionManager(){
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }

    @Bean
    public UserTransactionImp userTransactionImp(){
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        try {
            userTransactionImp.setTransactionTimeout(300);
        } catch (javax.transaction.SystemException e) {
            e.printStackTrace();
        }
        return userTransactionImp;
    }

    @Bean("transactionManager")
    public PlatformTransactionManager jtaTransactionManager(UserTransactionImp userTransactionImp, UserTransactionManager userTransactionManager){
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setUserTransaction(userTransactionImp);
        jtaTransactionManager.setTransactionManager(userTransactionManager);
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }
}
