package functions;

import fileio.SerialInputData;

import java.util.List;

public final class IsSerialFilters {
    private IsSerialFilters() {
    }
    /**
     * checks if a Serial is in the database with the given filters or no filters at all
     */
    public static boolean func(final List<SerialInputData> serials, final String title,
                               final String genre, final String year) {

        for (SerialInputData serial : serials) {

            if (serial.getTitle().equals(title)) {

                if (genre != null && year != null) {
                    if (serial.getGenres().contains(genre)
                            && serial.getYear() == Integer.parseInt(year)) {
                        return true;
                    }
                } else if (genre != null && year == null) {
                    if (serial.getGenres().contains(genre)) {
                        return true;
                    }
                } else if (genre == null && year != null) {
                    if (serial.getYear() == Integer.parseInt(year)) {
                        return true;
                    }
                } else {
                    return IsSerial.func(serials, title);
                }
            }
        }
        return false;
    }
}
