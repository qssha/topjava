package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.service.MealService;

@Controller
public abstract class MealRestController extends BaseMealController {

    public MealRestController(MealService service) {
        super(service);
    }
}