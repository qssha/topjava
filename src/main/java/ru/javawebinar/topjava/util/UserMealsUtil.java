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

        //List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        //mealsTo.forEach(System.out::println);

        //List<UserMealWithExcess> mealsToSecond = filteredByOneCycle(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        //mealsToSecond.forEach(System.out::println);

        //List<UserMealWithExcess> mealsToThird = filteredByOneCycleAlternative(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        //mealsToThird.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        //System.out.println(filteredByStreamsSimple(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mealMap = new HashMap<>();
        for (UserMeal meal :
                meals) {
            LocalDate localDate = meal.getDateTime().toLocalDate();
            mealMap.merge(localDate, meal.getCalories(), Integer::sum);
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
            mealMap.merge(localDate, new AbstractMap.SimpleEntry<>(new ArrayList<>(), meal.getCalories()), (prev, current) -> {
                prev.setValue(prev.getValue() + current.getValue());
                return prev;
            });

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

    public static List<UserMealWithExcess> filteredByOneCycleAlternative(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Map.Entry<BooleanWrapper, Integer>> mealMap = new HashMap<>();
        List<UserMealWithExcess> mealWithExcessList = new ArrayList<>();
        for (UserMeal meal :
                meals) {
            LocalDate localDate = meal.getDateTime().toLocalDate();
            mealMap.merge(localDate, new AbstractMap.SimpleEntry<>(new BooleanWrapper(false), meal.getCalories()), (prev, current) -> {
                prev.setValue(prev.getValue() + current.getValue());
                return prev;
            });

            if (!mealMap.get(localDate).getKey().isBool() && mealMap.get(localDate).getValue() > caloriesPerDay) {
                mealMap.get(localDate).getKey().setBool(true);
            }

            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExcessList.add(convertUserMealToUserMealWithExcessWrapper(meal, mealMap.get(localDate).getKey()));
            }
        }
        return mealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(x -> x.getDateTime().toLocalDate(),
                        Collector.of(
                                () -> new AbstractMap.SimpleEntry<>(new ArrayList<UserMeal>(), 0),
                                (sumListEntry, userMeal) -> {
                                    sumListEntry.setValue(sumListEntry.getValue() + userMeal.getCalories());
                                    if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                                        sumListEntry.getKey().add(userMeal);
                                },
                                (part1, part2) -> {
                                    part1.setValue(part1.getValue() + part2.getValue());
                                    part1.getKey().addAll(part2.getKey());
                                    return part1;
                                })))
                .values().stream()
                .collect(Collector.of(
                        ArrayList::new,
                        (userMeals, e) -> {
                            boolean excess = e.getValue() > caloriesPerDay;
                            e.getKey().forEach(x -> userMeals.add(convertUserMealToUserMealWithExcess(x, excess)));
                        },
                        (part1, part2) -> {
                            part1.addAll(part2);
                            return part1;
                        }));
    }

    public static List<UserMealWithExcess> filteredByStreamsSimple(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mealMap = meals.stream()
                .collect(Collectors.groupingBy(x -> x.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(x -> TimeUtil.isBetweenHalfOpen(x.getDateTime().toLocalTime(), startTime, endTime))
                .map(x -> convertUserMealToUserMealWithExcess(x, mealMap.get(x.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static UserMealWithExcess convertUserMealToUserMealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    private static UserMealWithExcess convertUserMealToUserMealWithExcessWrapper(UserMeal meal, BooleanWrapper excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
