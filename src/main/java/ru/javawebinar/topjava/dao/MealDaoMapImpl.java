package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoMapImpl implements MealDao {
    private static final AtomicInteger idCount = new AtomicInteger(0);
    private static final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    static {
        Meal meal = new Meal(idCount.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        mealMap.put(meal.getMealId(), meal);
        meal = new Meal(idCount.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        mealMap.put(meal.getMealId(), meal);
        meal = new Meal(idCount.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        mealMap.put(meal.getMealId(), meal);
        meal = new Meal(idCount.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        mealMap.put(meal.getMealId(), meal);
        meal = new Meal(idCount.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        mealMap.put(meal.getMealId(), meal);
        meal = new Meal(idCount.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        mealMap.put(meal.getMealId(), meal);
        meal = new Meal(idCount.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
        mealMap.put(meal.getMealId(), meal);
    }

    @Override
    public void addMeal(Meal meal) {
        meal.setMealId(idCount.getAndIncrement());
        mealMap.put(meal.getMealId(), meal);
    }

    @Override
    public void deleteMeal(int mealId) {
        mealMap.remove(mealId);
    }

    @Override
    public void updateMeal(Meal meal) {
        mealMap.put(meal.getMealId(), meal);
    }

    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal getMealById(int mealId) {
        return mealMap.get(mealId);
    }
}
