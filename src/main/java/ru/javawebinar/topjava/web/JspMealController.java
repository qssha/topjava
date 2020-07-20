package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

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

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int id = getId(request);
        mealController.delete(id);
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String createGet(HttpServletRequest request) {
        request.setAttribute("meal",
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        request.setAttribute("action", "create");
        return "mealForm";
    }

    @PostMapping("/create")
    public String createPost(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        mealController.create(meal);
        return "redirect:/meals";
    }

    @GetMapping("/update")
    public String update(HttpServletRequest request) {
        Meal meal = mealController.get(getId(request));
        request.setAttribute("meal", meal);
        request.setAttribute("action", "update");
        return "mealForm";
    }

    @PostMapping("/update")
    public String updatePost(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        mealController.update(meal, getId(request));
        return "redirect:/meals";
    }

    @GetMapping("/filter")
    public String filter(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", mealController.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
