package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int USER_MEAL_ID = START_SEQ + 2;
    public static final int ADMIN_MEAL_ID = START_SEQ + 3;

    public static final Meal USER_MEAL = new Meal(USER_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal ADMIN_MEAL = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of( 2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(USER_MEAL);
        updated.setDescription("Обновленная еда");
        updated.setCalories(300);
        return updated;
    }

    public static LocalDate getDate() {
        return LocalDate.of(2020, Month.JANUARY, 31);
    }
}
