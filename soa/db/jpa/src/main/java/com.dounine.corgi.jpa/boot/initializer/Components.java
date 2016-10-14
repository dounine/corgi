package com.dounine.corgi.jpa.boot.initializer;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;

import static com.dounine.corgi.jpa.boot.Constant.SCAN_APP_PACKAGES;

/**
 * Created by huanghuanlai on 16/8/17.
 */
public class Components {

    @Bean
    public DruidDataSource getDruid(Environment env) {
        DruidDataSource dds = new DruidDataSource();
        dds.setDriverClassName(env.getProperty("db.driver"));
        dds.setUrl(env.getProperty("db.url"));
        dds.setUsername(env.getProperty("db.username"));
        dds.setPassword(env.getProperty("db.password"));
        return dds;
    }

    /**
     * 实体管理器
     *
     * @return
     */
    @Bean(name = "entityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean getLCEMF(DruidDataSource druidDataSource) {
        LocalContainerEntityManagerFactoryBean lcemf = new LocalContainerEntityManagerFactoryBean();
        lcemf.setDataSource(druidDataSource);
        lcemf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        lcemf.setPackagesToScan(ArrayUtils.add(scanPackages(), SCAN_APP_PACKAGES));
        return lcemf;
    }

    public String[] scanPackages() {
        return new String[0];
    }

    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        return localContainerEntityManagerFactoryBean.getNativeEntityManagerFactory();
    }

    /**
     * 事务管理器
     *
     * @return
     */
    @Bean(name = "transactionManager")
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
