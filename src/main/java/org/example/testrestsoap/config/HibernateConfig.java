package org.example.testrestsoap.config;

import org.example.testrestsoap.entity.jpa.*;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateConfig {

    @Bean(name = "entityManagerFactory")
    public LocalSessionFactoryBean getSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();

        sessionFactoryBean.setDataSource(dataSource);

        Properties settings = new Properties();
        settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        settings.put(Environment.HBM2DDL_AUTO, "none");
        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.FORMAT_SQL, "true");
        settings.put(Environment.HIGHLIGHT_SQL, "true");

        sessionFactoryBean.setHibernateProperties(settings);

        sessionFactoryBean.setAnnotatedClasses(
                PersonEntity.class,
                PassportEntity.class,
                AddressEntity.class,
                CompanyEntity.class,
                UserEntity.class,
                RefreshTokenEntity.class
        );

        return sessionFactoryBean;
    }

}
