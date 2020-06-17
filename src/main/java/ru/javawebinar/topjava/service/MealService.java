package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id , userId), id);
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        return MealsUtil.getTos(repository.getAll(userId), caloriesPerDay);
    }

    public List<MealTo> getFiltered(int userId, int caloriesPerDay, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        if (startDate == null && startTime == null && endDate == null && endTime == null) return getAll(userId, caloriesPerDay);

        if (startDate == null) startDate = LocalDate.MIN;
        if (startTime == null) startTime = LocalTime.MIN;
        if (endDate == null) endDate = LocalDate.MAX;
        if (endTime == null) endTime = LocalTime.MAX;

        return MealsUtil.getFilteredTos(repository.getFiltered(userId, startDate, endDate),
                caloriesPerDay, startTime, endTime);
    }

    public void update(Meal meal, int userId) {
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }
}