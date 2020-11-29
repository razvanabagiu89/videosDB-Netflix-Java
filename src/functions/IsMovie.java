package functions;

import fileio.MovieInputData;

import java.util.List;

public final class IsMovie {
    private IsMovie() {
    }
    /**
     * check if movie exists in database
     * used in isMovieFilters because of null filters
     */
    public static boolean func(final List<MovieInputData> movies, final String title) {

        for (MovieInputData movie : movies) {
            if (movie.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }
}
