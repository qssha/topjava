package ru.javawebinar.topjava.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.repository.UserRepository;

@Service
@Profile("!datajpa")
public class UserGeneralSerice extends UserService {
    public UserGeneralSerice(UserRepository repository) {
        super(repository);
    }
}
