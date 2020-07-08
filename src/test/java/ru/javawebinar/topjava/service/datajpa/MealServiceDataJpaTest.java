package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealDataJpaService;
import ru.javawebinar.topjava.service.MealServiceTest;

import static ru.javawebinar.topjava.MealTestData.ADMIN_MEAL_ID;
import static ru.javawebinar.topjava.UserTestData.*;


@ActiveProfiles("datajpa")
public class MealServiceDataJpaTest extends MealServiceTest {

    @Autowired
    private MealDataJpaService service;

    @Test
    public void getMealWithUser() {
        Meal meal = service.getMealWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        USER_MATCHER.assertMatch(meal.getUser(), ADMIN);
    }
}
