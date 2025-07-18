DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS rating_films CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS user_friends CASCADE;
DROP TABLE IF EXISTS user_likes CASCADE;
DROP TABLE IF EXISTS film_genres CASCADE;

-- таблица жанров фильмов
CREATE TABLE IF NOT EXISTS genres (
		genres_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
		name VARCHAR(50) NOT NULL
);

		-- таблица MPA рейтинга
CREATE TABLE IF NOT EXISTS rating_films (
		rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
		rating_name VARCHAR(100)
);

		-- Таблица фильмов
CREATE TABLE IF NOT EXISTS films (
		id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
		name VARCHAR(100) NOT NULL,
description VARCHAR(200) NOT NULL,
release_date DATE NOT NULL,
duration INTEGER NOT NULL,
rating_id INTEGER REFERENCES rating_films (rating_id) ON DELETE CASCADE
);

		-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
		id INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
		name VARCHAR(50),
login VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL,
birthday DATE NOT NULL
);

		-- Таблица для хранения связей "друзья"
CREATE TABLE IF NOT EXISTS user_friends (
		user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
friend_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
approval BOOLEAN
);

		-- Таблица для лайков фильмов
CREATE TABLE IF NOT EXISTS user_likes (
		user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
film_id INTEGER REFERENCES films (id) ON DELETE CASCADE,
PRIMARY KEY (user_id, film_id)
);

		-- Связующая таблица для жанров
CREATE TABLE IF NOT EXISTS film_genres (
		film_id INTEGER REFERENCES films (id) ON DELETE CASCADE,
genre_id INTEGER REFERENCES genres (genres_id) ON DELETE CASCADE,
PRIMARY KEY (film_id, genre_id)
);