package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final int USER_MEAL_FIRST_ID = START_SEQ + 2;
    public static final int USER_MEAL_SECOND_ID = START_SEQ + 3;
    public static final int USER_MEAL_THIRD_ID = START_SEQ + 4;

    public static final int ADMIN_MEAL_FIRST_ID = START_SEQ + 5;
    public static final int ADMIN_MEAL_SECOND_ID = START_SEQ + 6;
    public static final int ADMIN_MEAL_THIRD_ID = START_SEQ + 7;
    public static final int ADMIN_MEAL_FOURTH_ID = START_SEQ + 8;

    public static Meal USER_MEAL_FIRST = new Meal(USER_MEAL_FIRST_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static Meal USER_MEAL_SECOND =  new Meal(USER_MEAL_SECOND_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static Meal USER_MEAL_THIRD = new Meal(USER_MEAL_THIRD_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);


    public static Meal ADMIN_MEAL_FIRST = new Meal(ADMIN_MEAL_FIRST_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static Meal ADMIN_MEAL_SECOND = new Meal(ADMIN_MEAL_SECOND_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static Meal ADMIN_MEAL_THIRD = new Meal(ADMIN_MEAL_THIRD_ID, LocalDateTime.of(2020, Month.FEBRUARY, 1, 13, 0), "Обед", 500);
    public static Meal ADMIN_MEAL_FOURTH = new Meal(ADMIN_MEAL_FOURTH_ID, LocalDateTime.of(2020, Month.FEBRUARY, 2, 20, 0), "Ужин", 410);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of( 2020, Month.FEBRUARY, 1, 20, 0), "Ужин", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(USER_MEAL_FIRST);
        updated.setDescription("Обновленная еда");
        updated.setCalories(300);
        return updated;
    }

    public static LocalDate getDate() {
        return LocalDate.of(2020, Month.JANUARY, 31);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
