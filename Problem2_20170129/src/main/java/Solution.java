// Copyright (C) 2017 GBesancon

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Solution {
  protected void computeAllSimilarMovies(Movie movie, Set<Movie> movies) {
    if (!movies.contains(movie)) {
      movies.add(movie);
      for (Movie aSimilarMovie : movie.getSimilarMovies()) {
        computeAllSimilarMovies(aSimilarMovie, movies);
      }
    }
  }

  public Set<Movie> getMovieRecommendations(Movie movie, int N) {
    Set<Movie> movies = new HashSet<Movie>();
    computeAllSimilarMovies(movie, movies);
    List<Movie> moviesSortedByRating = new ArrayList<Movie>(movies);
    Collections.sort(
        moviesSortedByRating,
        new Comparator<Movie>() {
          @Override
          public int compare(Movie o1, Movie o2) {
            return Float.compare(o1.getRating(), o2.getRating());
          }
        });
    return new LinkedHashSet<Movie>(moviesSortedByRating.subList(0, N));
  }
}
