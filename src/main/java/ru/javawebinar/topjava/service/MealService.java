package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, Integer userId) {
        return repository.save(meal, userId);
    }

    public void delete(int id, Integer userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, Integer userId) {
        return checkNotFoundWithId(repository.get(id , userId), id);
    }

    public Collection<MealTo> getAll(Integer userId, int caloriesPerDay) {
        return MealsUtil.getTos(repository.getAll(userId), caloriesPerDay);
    }

    public Collection<MealTo> getFiltered(Integer userId, int caloriesPerDay, LocalDate startDate, LocalTime startTime, LocalTime endTime, LocalDate endDate) {
        if (startDate == null && startTime == null && endDate == null && endTime == null) return getAll(userId, caloriesPerDay);

        if (startDate == null) startDate = LocalDate.MIN;
        if (startTime == null) startTime = LocalTime.MIN;
        if (endDate == null) endDate = LocalDate.MAX;
        if (endTime == null) endTime = LocalTime.MAX;

        return MealsUtil.getFilteredTos(repository.getFiltered(userId, startDate, endDate),
                caloriesPerDay, startTime, endTime);
    }

    public void update(Meal meal, Integer userId) {
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }
}