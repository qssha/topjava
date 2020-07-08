package ru.javawebinar.topjava.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

@Service
@Profile("datajpa")
public class MealDataJpaService extends MealService {
    public MealDataJpaService(MealRepository repository) {
        super(repository);
    }

    @Transactional
    public Meal getMealWithUser(int id, int userId) {
        Meal meal = get(id, userId);
        meal.getUser().getName();
        return meal;
    }
}
