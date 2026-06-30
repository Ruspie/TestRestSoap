package org.example.testrestsoap.service;

import org.example.testrestsoap.entity.jpa.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // 1. Внедряем SessionFactory для прямой работы с Hibernate
    private final SessionFactory sessionFactory;

    @Autowired
    public CustomUserDetailsService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // 2. Главный метод интерфейса UserDetailsService
    @Override
    @Transactional(readOnly = true) // Открываем ленивую транзакцию только на чтение
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Получаем текущую Hibernate-сессию из контекста транзакции
        Session session = sessionFactory.getCurrentSession();

        // 3. Выполняем HQL-запрос для поиска пользователя в таблице 'users'
        UserEntity userEntity = session.createQuery(
                "FROM UserEntity WHERE username = :username", UserEntity.class)
            .setParameter("username", username)
            .uniqueResult();

        // 4. Если пользователь не найден в БД, выбрасываем системное исключение
        if (userEntity == null) {
            throw new UsernameNotFoundException("Пользователь не найден с логином: " + username);
        }

        // 5. Маппим (конвертируем) нашу сущность UserEntity в системный UserDetails
        return User.withUsername(userEntity.getUsername())
            .password(userEntity.getPassword()) // Передаем хэшированный BCrypt-пароль
            .authorities(userEntity.getRole())  // Передаем роль (например, "ROLE_USER")
            .build();
    }
}
