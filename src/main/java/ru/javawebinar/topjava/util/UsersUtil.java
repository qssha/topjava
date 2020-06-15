package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {
    public static final List<User> USERS = Arrays.asList(
            new User(0, "admin", "admin@mail.com", "admin", Role.ADMIN),
            new User(1, "user", "user@mail.com", "user", Role.USER),
            new User(2, "user2", "user2@mail.com", "user", Role.USER)
    );
}
