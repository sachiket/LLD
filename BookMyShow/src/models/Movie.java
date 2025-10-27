package models;

import enums.MovieGenre;

/**
 * Represents a movie in the BookMyShow system
 * Follows encapsulation principle with private fields and public getters
 */
public class Movie {
    private final String movieId;
    private final String title;
    private final String description;
    private final int durationInMinutes;
    private final MovieGenre genre;
    private final String language;
    private final double rating;

    public Movie(String movieId, String title, String description, int durationInMinutes, 
                 MovieGenre genre, String language, double rating) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.durationInMinutes = durationInMinutes;
        this.genre = genre;
        this.language = language;
        this.rating = rating;
    }

    // Getters
    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public MovieGenre getGenre() {
        return genre;
    }

    public String getLanguage() {
        return language;
    }

    public double getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return String.format("Movie{id='%s', title='%s', genre=%s, duration=%d mins, rating=%.1f}", 
                           movieId, title, genre, durationInMinutes, rating);
    }
}