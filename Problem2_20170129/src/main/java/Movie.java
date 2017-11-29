import java.util.List;

public interface Movie {
	int getId();

	float getRating();

	List<Movie> getSimilarMovies();
}
