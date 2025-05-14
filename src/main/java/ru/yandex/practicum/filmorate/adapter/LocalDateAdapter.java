package ru.yandex.practicum.filmorate.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {

        if (localDate == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(localDate.format(formatter));
        }
    }

    @Override
    public LocalDate read(final JsonReader jsonReader) throws IOException {

        String nullOrNot = jsonReader.nextString();
        if ("null".equals(nullOrNot)) {
            return null;
        }
        return LocalDate.parse(nullOrNot, formatter);
    }
}
