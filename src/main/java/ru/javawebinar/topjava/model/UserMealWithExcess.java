package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.util.BooleanWrapper;

import java.time.LocalDateTime;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final BooleanWrapper excess;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = new BooleanWrapper(excess);
    }

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, BooleanWrapper excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public boolean isExcess() {
        return excess.isBool();
    }

    public void setExcess(boolean excess) {
        this.excess.setBool(excess);
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
