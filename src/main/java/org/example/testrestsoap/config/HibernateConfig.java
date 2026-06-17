package org.example.testrestsoap.config;

import org.example.testrestsoap.entity.jpa.AddressEntity;
import org.example.testrestsoap.entity.jpa.CompanyEntity;
import org.example.testrestsoap.entity.jpa.PassportEntity;
import org.example.testrestsoap.entity.jpa.PersonEntity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class HibernateConfig {

    @Bean
    public static SessionFactory buildSessionFactory() {
        try {
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

            Properties settings = new Properties();

            // Настройки подключения к H2 (перевод с префиксами Jakarta)
            settings.put(Environment.JAKARTA_JDBC_DRIVER, "org.h2.Driver");
            settings.put(Environment.JAKARTA_JDBC_URL, "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
            settings.put(Environment.JAKARTA_JDBC_USER, "sa");
            settings.put(Environment.JAKARTA_JDBC_PASSWORD, "");

            // Настройки генерации схем и диалекта
            settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
            settings.put(Environment.HBM2DDL_AUTO, "create-drop");

            // Форматирование и вывод логов в консоль
            settings.put(Environment.SHOW_SQL, "true");
            settings.put(Environment.FORMAT_SQL, "true");
            settings.put(Environment.HIGHLIGHT_SQL, "true");

            configuration.setProperties(settings);

            // Перечисляем все четыре твои сущности
            configuration.addAnnotatedClass(PersonEntity.class);
            configuration.addAnnotatedClass(PassportEntity.class);
            configuration.addAnnotatedClass(AddressEntity.class);
            configuration.addAnnotatedClass(CompanyEntity.class);

            return configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания фабрики сессий Hibernate", e);
        }
    }

}
