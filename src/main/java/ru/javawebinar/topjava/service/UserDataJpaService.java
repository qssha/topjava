package ru.javawebinar.topjava.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

@Service
@Profile("datajpa")
public class UserDataJpaService extends UserService {
    public UserDataJpaService(UserRepository repository) {
        super(repository);
    }

    @Transactional
    public User getUserWithMeals(int id) {
        User user = get(id);
        user.getMeals().size();
        return user;
    }
}
