package ru.javawebinar.topjava.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.repository.MealRepository;

@Service
@Profile("!datajpa")
public class MealGeneralService extends MealService {
    public MealGeneralService(MealRepository repository) {
        super(repository);
    }
}
