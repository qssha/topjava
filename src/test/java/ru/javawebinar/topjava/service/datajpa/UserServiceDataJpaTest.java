package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;


import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceTest {

    @Test
    public void getUserWithMeals() {
        User user = service.getUserWithMeals(ADMIN_ID);
        USER_MATCHER.assertMatch(user, ADMIN);
        MEAL_MATCHER.assertMatch(user.getMeals(), ADMIN_MEAL2, ADMIN_MEAL1);
    }
}
