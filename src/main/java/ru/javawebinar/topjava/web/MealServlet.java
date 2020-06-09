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
    private static final Logger log = getLogger(MealServlet.class);
    private MealDao dao;
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public void init() {
        dao = new MealDaoMapImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String mealTimeString = request.getParameter("dateTime");
        LocalDateTime mealDateTime = mealTimeString == null ? null : LocalDateTime.parse(mealTimeString, formatter);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String id = request.getParameter("id");
        Meal meal = new Meal(mealDateTime, description, calories);

        if (id == null || id.isEmpty()) {
            dao.add(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            dao.update(meal);
        }

        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter("action");
        int caloriesLimit = 2000;

        if (action == null) {
            forward = LIST_MEAL;
            List<MealTo> meals = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, caloriesLimit);
            request.setAttribute("meals", meals);
            request.getRequestDispatcher(forward).forward(request, response);
        } else {
            switch (action) {
                case "delete":
                    int id = Integer.parseInt(request.getParameter("id"));
                    dao.delete(id);
                    List<MealTo> meals = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, caloriesLimit);
                    request.setAttribute("meals", meals);
                    response.sendRedirect("meals");
                    break;
                case "edit":
                    id = Integer.parseInt(request.getParameter("id"));
                    Meal meal = dao.getById(id);
                    request.setAttribute("meal", meal);
                case "insert":
                    forward = INSERT_OR_EDIT;
                    request.getRequestDispatcher(forward).forward(request, response);
                    break;
                default:
                    forward = LIST_MEAL;
                    meals = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, caloriesLimit);
                    request.setAttribute("meals", meals);
                    request.getRequestDispatcher(forward).forward(request, response);
            }
        }
    }
}
