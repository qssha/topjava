package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(x -> save(x, x.getUserId()));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (!meal.getUserId().equals(userId)) return null;

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (!meal.getUserId().equals(userId)) return null;
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, Integer userId) {
        if (repository.get(id) == null || !repository.get(id).getUserId().equals(userId)) return false;
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, Integer userId) {
        if (repository.get(id) == null || !repository.get(id).getUserId().equals(userId)) return null;
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return repository.values().stream()
                .filter(x -> x.getUserId().equals(userId))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getFiltered(Integer userId, LocalDate startDate, LocalDate endDate) {
        return getAll(userId).stream()
                .filter(x -> DateTimeUtil.isBetweenHalfOpen(x.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }
}

