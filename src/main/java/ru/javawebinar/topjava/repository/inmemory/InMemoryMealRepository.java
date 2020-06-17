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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(x -> save(x, x.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal == null) return null;

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.putIfAbsent(userId, new HashMap<>());
            repository.get(userId).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> {meal.setUserId(userId); return meal;});
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) return false;
        Meal meal = mealMap.get(id);
        if (meal == null || meal.getUserId() != userId) return false;
        return mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) return null;
        Meal meal = mealMap.get(id);
        if (meal == null || meal.getUserId() != userId) return null;
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getMeals(userId, x -> true);
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        return getMeals(userId,
                x -> DateTimeUtil.isBetweenHalfOpen(x.getDate(), startDate, endDate.equals(LocalDate.MAX) ? endDate : endDate.plusDays(1)));
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

