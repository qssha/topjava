package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

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

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<String, Map.Entry<List<UserMeal>, Integer>> mealMap = new HashMap<>();
        for (UserMeal meal :
                meals) {
            String yearMonthDay = meal.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE);
            mealMap.putIfAbsent(yearMonthDay, new AbstractMap.SimpleEntry<>(new ArrayList<>(), 0));
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                mealMap.get(yearMonthDay).getKey().add(meal);
            mealMap.get(yearMonthDay).setValue(mealMap.get(yearMonthDay).getValue() + meal.getCalories());
        }

        List<UserMealWithExcess> mealWithExcessList = new ArrayList<>();
        for (Map.Entry<String, Map.Entry<List<UserMeal>, Integer>> entry:
        mealMap.entrySet()) {
            boolean excess = entry.getValue().getValue() > caloriesPerDay;
            for (UserMeal meal :
                    entry.getValue().getKey()) {
                mealWithExcessList.add(new UserMealWithExcess(meal, excess));
            }
        }

        return mealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(x -> x.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        Collector.of(
                                () -> new AbstractMap.SimpleEntry<>(new ArrayList<>(), 0),
                                (entry, e) -> {entry.setValue(entry.getValue() + e.getCalories());
                                    if (TimeUtil.isBetweenHalfOpen(e.getDateTime().toLocalTime(), startTime, endTime)) entry.getKey().add(e);},
                                (part1, part2) -> {part1.setValue(part1.getValue() + part2.getValue()); part1.getKey().addAll(part2.getKey()); return part1;})))
                .values().stream()
                .collect(Collector.of(
                        () -> new ArrayList<UserMealWithExcess>(),
                        (userMeals, e) -> { boolean excess = e.getValue() > caloriesPerDay; e.getKey().forEach(x -> userMeals.add(new UserMealWithExcess((UserMeal) x, excess)));},
                        (part1, part2) -> {part1.addAll(part2); return part1;}));
    }
}
