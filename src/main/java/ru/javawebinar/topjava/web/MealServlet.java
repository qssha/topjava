package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMapImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private final MealDao dao;
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public MealServlet() {
        dao = new MealDaoMapImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String mealTimeString = request.getParameter("dateTime");
        LocalDateTime mealTime = mealTimeString == null ? null : LocalDateTime.parse(mealTimeString, formatter);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String mealId = request.getParameter("mealId");
        Meal meal = new Meal(mealTime, description, calories);

        if (mealId == null || mealId.isEmpty()) {
            dao.addMeal(meal);
        } else {
            meal.setMealId(Integer.parseInt(mealId));
            dao.updateMeal(meal);
        }

        int excess = Integer.MAX_VALUE;
        try {
            excess = Integer.parseInt(request.getParameter("excess"));
        } catch (NumberFormatException e) {
        }

        request.setAttribute("meals", MealsUtil.filteredByStreams(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, excess));
        request.getRequestDispatcher(LIST_MEAL).forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter("action");

        int excess = Integer.MAX_VALUE;
        try {
            excess = Integer.parseInt(request.getParameter("excess"));
        } catch (NumberFormatException e) {
        }

        if (action == null) {
            forward = LIST_MEAL;
            List<MealTo> meals = MealsUtil.filteredByStreams(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, excess);
            request.setAttribute("meals", meals);
            request.getRequestDispatcher(forward).forward(request, response);
        } else if (action.equalsIgnoreCase("delete")){
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            dao.deleteMeal(mealId);
            forward = LIST_MEAL;
            List<MealTo> meals = MealsUtil.filteredByStreams(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, excess);
            request.setAttribute("meals", meals);
            response.sendRedirect("meals");
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = dao.getMealById(mealId);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher(forward).forward(request, response);
        } else {
            forward = INSERT_OR_EDIT;
            request.getRequestDispatcher(forward).forward(request, response);
        }
    }
}
