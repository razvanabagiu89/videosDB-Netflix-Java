package functions;

import fileio.ActionInputData;
import fileio.Input;
import fileio.Writer;
import myinput.MyInput;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import user.MyUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static sortfunctions.MapInteger.integerDescDesc;
import static sortfunctions.MapInteger.integerAscAsc;
import static utils.Utils.initializeInput;
import static utils.Utils.initializeUsers;

public final class QueryMostViewed {
    private QueryMostViewed() {
    }
    /**
     * outputs first N videos that are sorted by their number of views
     */
    public static void func(final Input input, final Writer fileWriter, final JSONArray arrayResult,
                            final ActionInputData action, int end) throws IOException {

        String message = "Query result: [";
        MyInput myInput = initializeInput(input);
        ArrayList<MyUser> myUsers = initializeUsers(input.getUsers());

        // update each user's history
        for (int i = 0; i < end; i++) {
            if (myInput.getCommands().get(i).getActionType().equals("command")) {
                if (myInput.getCommands().get(i).getType().equals("view")) {

                    int index = 0;
                    String thisUsername = myInput.getCommands().get(i).getUsername();
                    for (int j = 0; j < myUsers.size(); j++) {
                        if (myUsers.get(j).getUsername().equals(thisUsername)) {
                            index = j;
                            break;
                        }
                    }
                    MyUser thisUser = myUsers.get(index);

                    // check if movie exists
                    boolean existsVideo1 = IsMovie.func(myInput.getMovies(),
                            myInput.getCommands().get(i).getTitle());
                    boolean existsVideo2 = IsSerial.func(myInput.getSerials(),
                            myInput.getCommands().get(i).getTitle());
                    String thisVideo = myInput.getCommands().get(i).getTitle();

                    if (existsVideo1 || existsVideo2) {

                        if (!thisUser.getHistory().containsKey(thisVideo)) {
                            thisUser.addToHistory(thisVideo);
                        } else {
                            thisUser.incrementToHistory(thisVideo);
                        }
                    }
                }
            }
        }
        HashMap<String, Integer> videos = new HashMap<>();
        String thisYear = action.getFilters().get(0).get(0);
        String thisGenre = action.getFilters().get(1).get(0);

        // cycle all users
        for (int i = 0; i < myUsers.size(); i++) {

            String findMe;
            int findCounter = 0;

            // and their history
            for (Iterator<Map.Entry<String, Integer>> it = myUsers.get(i).getHistory()
                    .entrySet().iterator(); it.hasNext();) {

                Map.Entry<String, Integer> entry = it.next();
                findMe = entry.getKey();

                // get number of views from first one
                findCounter = entry.getValue();


                // check if movie or serial
                boolean isMovieFilters = functions.IsMovieFilters.func(myInput.getMovies(),
                        findMe, thisGenre, thisYear);
                boolean isSerialFilters = functions.IsSerialFilters.func(myInput.getSerials(),
                        findMe, thisGenre, thisYear);

                for (int k = i + 1; k < myUsers.size(); k++) {

                    // get all the remaining number of views from remaining users
                    for (Map.Entry<String, Integer> entry2 : myUsers.get(k).getHistory().
                            entrySet()) {
                        if (entry2.getKey().equals(findMe)) {
                            findCounter += entry2.getValue();
                            break;
                        }
                    }
                    myUsers.get(k).removeFromHistory(findMe);
                }

                // movie/serial
                if (action.getObjectType().equals("movies") && isMovieFilters) {
                    videos.put(findMe, findCounter);
                } else if (action.getObjectType().equals("shows") && isSerialFilters) { // serial
                    videos.put(findMe, findCounter);
                }
            }
        }
        Map<String, Integer> sortedVideos;

        if (action.getSortType().equals("asc")) {
            sortedVideos = integerAscAsc(videos);
        } else {
            sortedVideos = integerDescDesc(videos);
        }

        if (sortedVideos.isEmpty()) {
            message += "]";
            JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                    message);
            arrayResult.add(jsO);
        } else {
            int cnt = 0;

            if (action.getNumber() >= sortedVideos.size()) {
                for (Map.Entry<String, Integer> entry : sortedVideos.entrySet()) {
                    message += entry.getKey() + ", ";
                }
            } else {
                for (Map.Entry<String, Integer> entry : sortedVideos.entrySet()) {
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
