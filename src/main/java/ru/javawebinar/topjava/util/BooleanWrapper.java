package ru.javawebinar.topjava.util;

public class BooleanWrapper {
    private boolean bool;

    public BooleanWrapper(boolean bool) {
        this.bool = bool;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    @Override
    public String toString() {
        return bool ? "true" : "false";
    }
}
