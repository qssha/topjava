package ru.javawebinar.topjava.web.meal;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class LocalTimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String source) {
        try {
            return LocalTime.parse(source, DateTimeFormatter.ISO_LOCAL_TIME);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
