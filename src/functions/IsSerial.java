package functions;

import fileio.SerialInputData;

import java.util.List;

public final class IsSerial {
    private IsSerial() {
    }
    /**
     * check if a serial is in the given database
     * used in isSerialFilters because of null filters
     */
    public static boolean func(final List<SerialInputData> serials, final String title) {

        for (SerialInputData serial : serials) {
            if (serial.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }
}
