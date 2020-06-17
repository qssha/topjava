package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal ->
        {if (MealsUtil.MEALS.indexOf(meal) < 3) save(meal, 1);
        else save(meal, 2);});
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal == null) return null;

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.computeIfAbsent(userId, mealMap -> new HashMap<>()).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap == null? null : mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) return false;
        Meal meal = mealMap.get(id);
        return meal != null && mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap == null ? null : mealMap.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getMeals(userId, meal -> true);
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        return getMeals(userId,
                meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate.equals(LocalDate.MAX) ? endDate : endDate.plusDays(1)));
    }

    private List<Meal> getMeals(int userId, Predicate<? super Meal> predicate) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap == null ? Collections.emptyList() :
                repository.get(userId).values().stream()
                        .filter(predicate)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }
}

