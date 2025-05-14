package ru.yandex.practicum.filmorate.managerAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.practicum.filmorate.adapter.DurationAdapter;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;

import java.time.LocalDate;

public class ManagerAdapter {

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Long.class, new DurationAdapter());
        return gsonBuilder.create();

    }
}
