-- Users
INSERT INTO users (name, email, password, role)
SELECT * FROM (VALUES
    ('John Doe', 'johndoe@example.com', '$2a$12$4sZhQV/S16oRgXhOXqKoN.nAzD2TDqsPg.noi9YdbCMVAimnwJ1fe', 'CUSTOMER'),
    ('Jane Smith', 'janesmith@example.com', '$2a$12$4sZhQV/S16oRgXhOXqKoN.nAzD2TDqsPg.noi9YdbCMVAimnwJ1fe', 'CUSTOMER')
) AS new_users
WHERE (SELECT COUNT(*) FROM users) <= 1;

-- Movies
INSERT INTO movies (title, genre, duration, rating, release_year)
SELECT * FROM (VALUES
    ('Inception', 'Science Fiction', 148, 8.8, 2010),
    ('The Godfather', 'Crime', 175, 9.2, 1972),
    ('The Dark Knight', 'Action', 152, 9.0, 2008),
    ('Forrest Gump', 'Drama', 142, 8.8, 1994),
    ('Pulp Fiction', 'Crime', 154, 8.9, 1994)
) AS new_movies
WHERE NOT EXISTS (SELECT 1 FROM movies LIMIT 1);

-- Showtimes
INSERT INTO showtimes (movie_id, theater, start_time, end_time, available_seats)
SELECT m.id, s.theater, s.start_time, s.end_time, s.available_seats
FROM (VALUES
    ('Inception', 'Theater 1', '2025-02-01T18:00:00', '2025-02-01T20:30:00', 100),
    ('The Godfather', 'Theater 2', '2025-02-02T19:00:00', '2025-02-02T21:30:00', 80),
    ('The Dark Knight', 'Theater 3', '2025-02-03T20:00:00', '2025-02-03T22:30:00', 120)
) AS s(title, theater, start_time, end_time, available_seats)
JOIN movies m ON m.title = s.title
WHERE NOT EXISTS (SELECT 1 FROM showtimes LIMIT 1);