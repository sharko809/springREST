package com.serviceapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Application configuration class
 */
@Configuration
@ComponentScan("com.serviceapp")
@EnableJpaRepositories("com.serviceapp.repository")
@EnableTransactionManagement
@EnableSpringDataWebSupport
public class ApplicationConfiguration {

    private static final String JNDI_NAME = "java:comp/env/jdbc/moviedb";
    private static final String[] ENTITY_PACKAGES = {"com.serviceapp.entity"};

    private static final String HIBERNATE_DIALECT_PROP = "hibernate.dialect";
    private static final String HIBERNATE_DIALECT_VALUE = "org.hibernate.dialect.MySQLDialect";

    @Bean
    public DataSource dataSource() {
        return new JndiDataSourceLookup().getDataSource(JNDI_NAME);
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator(){
        return new HibernateExceptionTranslator();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setPackagesToScan(ENTITY_PACKAGES);
        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaProperties(hibernateProps());
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    /**
     * Set hibernate properties to <code>Properties</code> object
     *
     * @return <code>Properties</code> object with hibernate properties set
     */
    private Properties hibernateProps() {
        Properties properties = new Properties();
        properties.put(HIBERNATE_DIALECT_PROP, HIBERNATE_DIALECT_VALUE);
        return properties;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }

    @Bean(name = "validator")
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

}
