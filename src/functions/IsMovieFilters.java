package functions;

import fileio.MovieInputData;

import java.util.List;

public final class IsMovieFilters {
    private IsMovieFilters() {
    }
    /**
     * check if movie exists in database with the given filters or none
     */
    public static boolean func(final List<MovieInputData> movies, final String title,
                               final String genre, final String year) {

        for (MovieInputData movie : movies) {

            if (movie.getTitle().equals(title)) {

                if (genre != null && year != null) {
                    if (movie.getGenres().contains(genre)
                            && movie.getYear() == Integer.parseInt(year)) {
                        return true;
                    }
                } else if (genre != null && year == null) {
                    if (movie.getGenres().contains(genre)) {
                        return true;
                    }
                } else if (genre == null && year != null) {
                    if (movie.getYear() == Integer.parseInt(year)) {
                        return true;
                    }
                } else {
                    return IsMovie.func(movies, title);
                }
            }
        }
        return false;
    }
}
