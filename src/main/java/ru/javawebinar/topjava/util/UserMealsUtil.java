package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        List<UserMealWithExcess> mealsToSecond = filteredByOneCycle(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToSecond.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mealMap = new HashMap<>();
        for (UserMeal meal :
                meals) {
            LocalDate localDate = meal.getDateTime().toLocalDate();
            mealMap.putIfAbsent(localDate, 0);
            mealMap.put(localDate, mealMap.get(localDate) + meal.getCalories());
        }

        List<UserMealWithExcess> mealWithExcessList = new ArrayList<>();
        for (UserMeal meal :
                meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExcessList.add(convertUserMealToUserMealWithExcess(meal, mealMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }

        return mealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByOneCycle(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Map.Entry<List<UserMealWithExcess>, Integer>> mealMap = new HashMap<>();
        List<UserMealWithExcess> mealWithExcessList = new ArrayList<>();
        for (UserMeal meal :
                meals) {
            LocalDate localDate = meal.getDateTime().toLocalDate();
            mealMap.putIfAbsent(localDate, new AbstractMap.SimpleEntry<>(new ArrayList<>(), 0));
            mealMap.get(localDate).setValue(mealMap.get(localDate).getValue() + meal.getCalories());

            if (mealMap.get(localDate).getValue() > caloriesPerDay &&
                    !mealMap.get(localDate).getKey().isEmpty() && !mealMap.get(localDate).getKey().get(0).isExcess()) {
                mealMap.get(localDate).getKey().forEach(x -> x.setExcess(true));
            }

            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                UserMealWithExcess userMealWithExcess = convertUserMealToUserMealWithExcess(meal, mealMap.get(localDate).getValue() > caloriesPerDay);
                mealMap.get(localDate).getKey().add(userMealWithExcess);
                mealWithExcessList.add(userMealWithExcess);
            }
        }
        return mealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(x -> x.getDateTime().toLocalDate(),
                        Collector.of(
                                () -> new AbstractMap.SimpleEntry<>(new ArrayList<>(), 0),
                                (entry, e) -> {entry.setValue(entry.getValue() + e.getCalories());
                                    if (TimeUtil.isBetweenHalfOpen(e.getDateTime().toLocalTime(), startTime, endTime)) entry.getKey().add(e);},
                                (part1, part2) -> {part1.setValue(part1.getValue() + part2.getValue()); part1.getKey().addAll(part2.getKey()); return part1;})))
                .values().stream()
                .collect(Collector.of(
                        () -> new ArrayList<UserMealWithExcess>(),
                        (userMeals, e) -> {boolean excess = e.getValue() > caloriesPerDay; e.getKey().forEach(x -> userMeals.add(convertUserMealToUserMealWithExcess((UserMeal) x, excess)));},
                        (part1, part2) -> {part1.addAll(part2); return part1;}));
    }

    private static UserMealWithExcess convertUserMealToUserMealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
