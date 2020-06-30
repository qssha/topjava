package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDateTime.of;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static TestMatcher<Meal> MEAL_MATCHER = TestMatcher.usingFieldsComparator();

    public static final int NOT_FOUND = 10;
    public static final int MEAL1_ID = START_SEQ + 2;
    public static final int ADMIN_MEAL_ID = START_SEQ + 9;

    public static final Meal MEAL1 = new Meal(MEAL1_ID, of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL2 = new Meal(MEAL1_ID + 1, of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL3 = new Meal(MEAL1_ID + 2, of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL4 = new Meal(MEAL1_ID + 3, of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal MEAL5 = new Meal(MEAL1_ID + 4, of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 500);
    public static final Meal MEAL6 = new Meal(MEAL1_ID + 5, of(2020, Month.JANUARY, 31, 13, 0), "Обед", 1000);
    public static final Meal MEAL7 = new Meal(MEAL1_ID + 6, of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 510);
    public static final Meal ADMIN_MEAL1 = new Meal(ADMIN_MEAL_ID, of(2020, Month.JANUARY, 31, 14, 0), "Админ ланч", 510);
    public static final Meal ADMIN_MEAL2 = new Meal(ADMIN_MEAL_ID + 1, of(2020, Month.JANUARY, 31, 21, 0), "Админ ужин", 1500);

    static {
        MEAL1.setUser(UserTestData.USER);
        MEAL2.setUser(UserTestData.USER);
        MEAL3.setUser(UserTestData.USER);
        MEAL4.setUser(UserTestData.USER);
        MEAL5.setUser(UserTestData.USER);
        MEAL6.setUser(UserTestData.USER);
        MEAL7.setUser(UserTestData.USER);
        ADMIN_MEAL1.setUser(UserTestData.ADMIN);
        ADMIN_MEAL2.setUser(UserTestData.ADMIN);
    }

    public static final List<Meal> MEALS = Arrays.asList(MEAL7, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);

    public static Meal getNew() {
        Meal meal = new Meal(null, of(2020, Month.FEBRUARY, 1, 18, 0), "Созданный ужин", 300);
        meal.setUser(UserTestData.USER);
        return meal;
    }

    public static Meal getUpdated() {
        Meal meal = new Meal(MEAL1_ID, MEAL1.getDateTime(), "Обновленный завтрак", 200);
        meal.setUser(UserTestData.USER);
        return meal;
    }
}
