package ru.yandex.practicum.filmorate.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class DurationAdapter extends TypeAdapter<Long> {

    @Override
    public void write(final JsonWriter jsonWriter, final Long duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(duration);
    }

    @Override
    public Long read(final JsonReader jsonReader) throws IOException {

        final long minutes = jsonReader.nextLong();
        if (minutes == 0) {
            return null;
        }
        return minutes;
    }
}
