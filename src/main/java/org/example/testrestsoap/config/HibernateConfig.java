//package org.example.testrestsoap.config;
//
//import org.example.testrestsoap.entity.jpa.AddressEntity;
//import org.example.testrestsoap.entity.jpa.CompanyEntity;
//import org.example.testrestsoap.entity.jpa.PassportEntity;
//import org.example.testrestsoap.entity.jpa.PersonEntity;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Environment;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Configuration
//public class HibernateConfig {
//
//    @Bean(name = "entityManagerFactory")
//    public LocalSessionFactoryBean entityManagerFactory(DataSource dataSource) {
//        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
//
//        // Передаем спринговый DataSource (в нем будут жить и JdbcTemplate, и Hibernate)
//        sessionFactoryBean.setDataSource(dataSource);
//
//        // Настройки поведения самого Hibernate
//        Properties settings = new Properties();
//        settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
//        settings.put(Environment.HBM2DDL_AUTO, "none");
//        settings.put(Environment.SHOW_SQL, "true");
//        settings.put(Environment.FORMAT_SQL, "true");
//        settings.put(Environment.HIGHLIGHT_SQL, "true");
//        sessionFactoryBean.setHibernateProperties(settings);
//
//        // Регистрируем Entity-классы
//        sessionFactoryBean.setAnnotatedClasses(
//            PersonEntity.class,
//            PassportEntity.class,
//            AddressEntity.class,
//            CompanyEntity.class
//        );
//
//        return sessionFactoryBean;
//    }
//}
