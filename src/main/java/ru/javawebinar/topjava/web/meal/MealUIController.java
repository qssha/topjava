package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(MealUIController.REST_AJAX_URL)
public class MealUIController extends AbstractMealController {
    static final String REST_AJAX_URL = "/rest/ajax/profile/meals";

    @Override
    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createWithLocation(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                                   @RequestParam String description,
                                   @RequestParam int calories) {
        super.create(new Meal(dateTime, description, calories));
    }

    @Override
    @GetMapping(value = "/filter")
    public List<MealTo> getBetween(
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalTime startTime,
            @RequestParam @Nullable LocalDate endDate,
            @RequestParam @Nullable LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}
