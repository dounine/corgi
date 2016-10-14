package com.dounine.corgi.jpa.boot.initializer;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.dounine.corgi.jpa.utils.EntityPackageUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;

import static com.dounine.corgi.jpa.boot.Constant.SCAN_APP_PACKAGES;

/**
 * Created by huanghuanlai on 16/8/17.
 */
public class Components{

    @Autowired
    PropertiesFactoryBean pfb;

    @Bean
    public PropertiesFactoryBean getProperties(){
        PropertiesFactoryBean pfb = new PropertiesFactoryBean();
        pfb.setLocation(new ClassPathResource("config.properties"));
        pfb.setFileEncoding("utf-8");
        return pfb;
    }

    @Bean
    public DruidDataSource getDruid(PropertiesFactoryBean pfb){
        DruidDataSource dds = new DruidDataSource();
        try {
            dds.setDriverClassName(pfb.getObject().getProperty("db.driver"));
            dds.setUrl(pfb.getObject().getProperty("db.url"));
            dds.setUsername(pfb.getObject().getProperty("db.username"));
            dds.setPassword(pfb.getObject().getProperty("db.password"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dds;
    }

    /**
     * 实体管理器
     * @return
     */
    @Bean(name = "entityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean getLCEMF(DruidDataSource druidDataSource){
        LocalContainerEntityManagerFactoryBean lcemf = new LocalContainerEntityManagerFactoryBean();
        lcemf.setDataSource(druidDataSource);
        lcemf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        lcemf.setPackagesToScan(ArrayUtils.add(scanPackages(),SCAN_APP_PACKAGES));
        return lcemf;
    }

    public String[] scanPackages(){
        return new String[0];
    }

    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean){
        return localContainerEntityManagerFactoryBean.getNativeEntityManagerFactory();
    }

    /**
     * 事务管理器
     * @return
     */
    @Bean(name = "transactionManager")
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

}
