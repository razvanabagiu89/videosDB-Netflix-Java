package functions;

import myinput.MyInput;
import fileio.ActionInputData;
import fileio.Input;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static sortfunctions.MapInteger.integerAscAsc;
import static sortfunctions.MapInteger.integerDescDesc;
import static utils.Utils.initializeInput;

public final class QueryDuration {
    private QueryDuration() {
    }
    /**
     * outputs first N videos sorted by their total duration
     */
    public static void func(final Input input, final Writer fileWriter, final JSONArray arrayResult,
                            final ActionInputData action) throws IOException {

        String message = "Query result: [";
        MyInput myInput = initializeInput(input);

        String thisYear = action.getFilters().get(0).get(0);
        String thisGenre = action.getFilters().get(1).get(0);

        HashMap<String, Integer> longest = new HashMap<>();

        // movie
        if (action.getObjectType().equals("movies")) {

            // cycle all movies
            for (int i = 0; i < myInput.getMovies().size(); i++) {

                boolean isMovieFilters = functions.IsMovieFilters.func(
                        myInput.getMovies(), myInput.getMovies().get(i).getTitle(),
                        thisGenre, thisYear);

                // if it is a valid movie with given filters
                if (isMovieFilters) {
                    longest.put(myInput.getMovies().get(i).getTitle(),
                            myInput.getMovies().get(i).getDuration());
                }
            }
        } else if (action.getObjectType().equals("shows")) {  //serial

            // cycle all serials
            for (int i = 0; i < myInput.getSerials().size(); i++) {

                boolean isSerialFilters = functions.IsSerialFilters.func(
                        myInput.getSerials(), myInput.getSerials().get(i).getTitle(),
                        thisGenre, thisYear);

                // sum the duration of seasons
                if (isSerialFilters) {
                    int sum = 0;

                    for (int j = 0; j < myInput.getSerials().get(i).getSeasons().size(); j++) {
                        sum += myInput.getSerials().get(i).getSeasons().get(j).getDuration();
                    }
                    longest.put(myInput.getSerials().get(i).getTitle(), sum);
                }
            }
        }
        Map<String, Integer> sortedLongest;

        if (action.getSortType().equals("asc")) {
            sortedLongest = integerAscAsc(longest);
        } else {
            sortedLongest = integerDescDesc(longest);
        }

        if (sortedLongest.isEmpty()) {
            message += "]";
            JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                    message);
            arrayResult.add(jsO);
        } else {
            int cnt = 0;

            if (action.getNumber() >= sortedLongest.size()) {
                for (Map.Entry<String, Integer> entry : sortedLongest.entrySet()) {
                    message += entry.getKey() + ", ";
                }
            } else {
                for (Map.Entry<String, Integer> entry : sortedLongest.entrySet()) {
                    message += entry.getKey() + ", ";
                    cnt++;
                    if (cnt == action.getNumber()) {
                        break;
                    }
                }
            }

            // format message
            String strNew = message.substring(0, message.length() - 1);
            strNew = message.substring(0, message.length() - 2);
            message = strNew;
            message += "]";

            JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                    message);
            arrayResult.add(jsO);
        }
    }
}
