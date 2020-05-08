CREATE TABLE movie (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  synopsis TEXT NOT NULL
);

CREATE TABLE review (
  id SERIAL PRIMARY KEY,
  movie_id INT NOT NULL,
  author TEXT NOT NULL,
  comment TEXT NOT NULL,
  CONSTRAINT fk_movie_id FOREIGN KEY (movie_id) REFERENCES movie (id)
);

INSERT INTO MOVIE (name, synopsis) VALUES ('Titanic', 'This is not going to end well');

INSERT INTO review (movie_id, author, comment) VALUES (1, 'The Snarky Reviewer', 'Instant classic');
