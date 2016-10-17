package resources;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Application configuration for tests
 */
@Configuration
@ComponentScan("com.serviceapp")
@EnableJpaRepositories("com.serviceapp.repository")
public class TestConfiguration {

    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false&serverTimezone=UTC";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "htlbcrf";

    private static final String[] ENTITY_PACKAGES = {"com.serviceapp.entity", "com.serviceapp."};

    private static final String HIBERNATE_DIALECT_PROP = "hibernate.dialect";
    private static final String HIBERNATE_DIALECT_VALUE = "org.hibernate.dialect.MySQLDialect";

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(DATABASE_DRIVER);
        driverManagerDataSource.setUrl(DATABASE_URL);
        driverManagerDataSource.setUsername(DATABASE_USER);
        driverManagerDataSource.setPassword(DATABASE_PASSWORD);

        return driverManagerDataSource;
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
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }

}
