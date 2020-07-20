package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.web.meal.MealRestController;

@Controller
@RequestMapping("/meals")
public class JspMealController {

    @Autowired
    private MealRestController mealController;

    @GetMapping("")
    public String getAll(Model model) {
        model.addAttribute("meals", mealController.getAll());
        return "meals";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        mealController.delete(id);
        model.addAttribute("meals", mealController.getAll());
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("meals", mealController.getAll());
        return "mealForm";
    }
}
