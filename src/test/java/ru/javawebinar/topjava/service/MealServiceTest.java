package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    private MealRepository repository;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_FIRST_ID, USER_ID);
        assertThat(meal).isEqualToComparingFieldByField(USER_MEAL_FIRST);
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_FIRST_ID, USER_ID);
        assertNull(repository.get(USER_MEAL_FIRST_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> expectedMeals = Arrays.asList(ADMIN_MEAL_SECOND, ADMIN_MEAL_FIRST);
        List<Meal> actualMeals = service.getBetweenInclusive(null, getDate(), ADMIN_ID);
        assertThat(expectedMeals.size()).isEqualTo(actualMeals.size());
        for (int i = 0; i < expectedMeals.size(); i++) {
            assertThat(expectedMeals.get(i)).isEqualToComparingFieldByField(actualMeals.get(i));
        }
    }

    @Test
    public void getAll() {
        List<Meal> expectedMeals = Arrays.asList(USER_MEAL_THIRD, USER_MEAL_SECOND, USER_MEAL_FIRST);
        List<Meal> actualMeals = service.getAll(USER_ID);
        assertThat(expectedMeals.size()).isEqualTo(actualMeals.size());
        for (int i = 0; i < expectedMeals.size(); i++) {
            assertThat(expectedMeals.get(i)).isEqualToComparingFieldByField(actualMeals.get(i));
        }
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertThat(service.get(USER_MEAL_FIRST_ID, USER_ID)).isEqualToComparingFieldByField(updated);
    }

    @Test
    public void create() {
        Meal meal = getNew();
        Meal createdMeal = service.create(meal, USER_ID);
        assertThat(createdMeal).isEqualToComparingFieldByField(meal);
    }

    @Test
    public void getWrongMeal() {
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_FIRST_ID, ADMIN_ID));
    }

    @Test
    public void deleteWrongMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_MEAL_FIRST_ID, ADMIN_ID));
    }

    @Test
    public void updateWrongMeal() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(), ADMIN_ID));
    }
}