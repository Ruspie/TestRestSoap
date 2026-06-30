package org.example.testrestsoap.service;

import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.entity.jpa.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SessionFactory sessionFactory;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Session session = sessionFactory.getCurrentSession();

        UserEntity userEntity = session.createQuery(
                        "SELECT u FROM UserEntity u WHERE username = :username", UserEntity.class
                ).setParameter("username", username)
                .uniqueResult();

        if (userEntity == null)
            throw new UsernameNotFoundException("Пользователь с username: " + username + " не найден!");

        return User.withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole())
                .build();
    }

}
