package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserDataJpaService;
import ru.javawebinar.topjava.service.UserServiceTest;


import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ActiveProfiles("datajpa")
public class UserServiceDataJpaTest extends UserServiceTest {

    @Autowired
    private UserDataJpaService service;

    @Test
    public void getUserWithMeals() {
        User user = service.getUserWithMeals(ADMIN_ID);
        MEAL_MATCHER.assertMatch(user.getMeals(), ADMIN_MEAL2, ADMIN_MEAL1);
    }
}
