package functions;

import myinput.MyInput;
import fileio.ActionInputData;
import fileio.Input;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import user.MyUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import static functions.RecommendationBestUnseen.intToBool;
import static sortfunctions.QueryRatingSearch.querySearch;
import static sortfunctions.QueryRatingSearch2.querySearch2;
import static utils.Utils.initializeInput;
import static utils.Utils.initializeUsers;

public final class QueryRating {
    private QueryRating() {
    }
    /**
     * outputs first N videos sorted by their rating
     */
    public static void func(final Input input, final Writer fileWriter, final JSONArray arrayResult,
                            final ActionInputData action, int end) throws IOException {


        MyInput myInput = initializeInput(input);
        String message = "Query result: [";
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

                    // check if video exists in database
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
        // remember only valid actions
        HashMap<Integer, String> validActions = new HashMap<>();

        // compute each rating
        for (int i = 0; i < end; i++) {
            if (myInput.getCommands().get(i).getActionType().equals("command")) {
                if (myInput.getCommands().get(i).getType().equals("rating")) {

                    int isSeason;
                    // current user
                    int index = 0;
                    String thisUsername = myInput.getCommands().get(i).getUsername();
                    for (int j = 0; j < myUsers.size(); j++) {
                        if (myUsers.get(j).getUsername().equals(thisUsername)) {
                            index = j;
                            break;
                        }
                    }
                    MyUser thisUser = myUsers.get(index);

                    isSeason = myInput.getCommands().get(i).getSeasonNumber();

                    if (thisUser.getHistory().containsKey(myInput
                            .getCommands().get(i).getTitle())) {

                        // serial
                        if (isSeason > 0) {
                            thisUser.addToRatingShow(myInput.getCommands().get(i).getTitle(),
                                    myInput.getCommands().get(i).getSeasonNumber());
                            validActions.put(myInput.getCommands().get(i).getActionId(),
                                    myInput.getCommands().get(i).getTitle());
                        } else { // movie
                            if (!thisUser.getRatingsMovie()
                                    .containsKey(myInput.getCommands().get(i).getTitle())) {
                                thisUser.addToRatingMovie(myInput.getCommands().get(i).getTitle(),
                                        myInput.getCommands().get(i).getGrade());
                                validActions.put(myInput.getCommands().get(i).getActionId(),
                                        myInput.getCommands().get(i).getTitle());
                            }
                        }
                    }
                }
            }
        }
        // videos with their rating
        Map<String, Double> videos = new HashMap<>();

        // movie
        double videoRating;
        int numberRatings;

        boolean exists1;
        boolean exists2;
        boolean exists4;

        // serial
        double[] serialRatings;
        double noSeasons = 0.00;
        double[] counters;

        // compute each rating
        for (int i = 0; i < end; i++) {

            int type = 0;
            exists1 = false;
            exists2 = false;

            if (myInput.getCommands().get(i).getActionType().equals("command")) {
                if (myInput.getCommands().get(i).getType().equals("rating")) {

                    if (validActions.containsKey(myInput.getCommands().get(i).getActionId())) {

                        // movie
                        for (int k = 0; k < myInput.getMovies().size(); k++) {
                            if (myInput.getCommands().get(i).getTitle()
                                    .equals(myInput.getMovies().get(k).getTitle())) {
                                exists1 = true;
                                type = 0;
                                break;
                            }
                        }

                        // serial
                        for (int k = 0; k < myInput.getSerials().size(); k++) {
                            if (myInput.getCommands().get(i).getTitle()
                                    .equals(myInput.getSerials().get(k).getTitle())) {
                                exists1 = true;
                                noSeasons = myInput.getSerials().get(k).getNumberSeason();
                                type = 1;
                                break;
                            }
                        }

                        serialRatings = new double[(int) noSeasons];
                        counters = new double[(int) noSeasons];

                        if (type == 1) {
                            serialRatings[myInput.getCommands()
                                    .get(i).getSeasonNumber() - 1] =
                                    myInput.getCommands().get(i).getGrade();

                            counters[myInput.getCommands()
                                    .get(i).getSeasonNumber() - 1] = 1;
                        }

                        for (int k = 0; k < myInput.getUsers().size(); k++) {

                            if (myInput.getCommands().get(i).getUsername()
                                    .equals(myInput.getUsers().get(k).getUsername())) {

                                if (myInput.getUsers().get(k).getHistory().containsKey(
                                        myInput.getCommands().get(i).getTitle())) {
                                    exists2 = true;
                                    break;
                                }
                            }
                        }

                        if (exists1 && exists2) {

                            numberRatings = 1;
                            videoRating = myInput.getCommands().get(i).getGrade();

                            for (int j = i + 1; j < end; j++) {

                                exists4 = false;

                                if (myInput.getCommands().get(j).getActionType()
                                        .equals("command")) {
                                    if (myInput.getCommands().get(j).getType()
                                            .equals("rating")) {

                                        if (myInput.getCommands().get(i).getTitle().equals(
                                                myInput.getCommands().get(j).getTitle()
                                        )) {

                                            for (int k = 0; k < myInput.getUsers().size(); k++) {

                                                if (myInput.getCommands().get(j).getUsername()
                                                        .equals(myInput.getUsers()
                                                                .get(k).getUsername())) {

                                                    if (myInput.getUsers().get(k).getHistory().
                                                            containsKey(
                                                            myInput.getCommands()
                                                                    .get(j).getTitle())) {
                                                        exists4 = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (exists4) {

                                                // movie
                                                if (!intToBool(myInput.getCommands()
                                                        .get(i).getSeasonNumber())) {

                                                    numberRatings++;
                                                    videoRating += myInput.getCommands()
                                                            .get(j).getGrade();
                                                    myInput.removeCommand(myInput
                                                            .getCommands().get(j));
                                                    break;
                                                } else if (intToBool(myInput.getCommands()
                                                        .get(i).getSeasonNumber())) {
                                                    // serial
                                                    serialRatings[myInput.getCommands()
                                                            .get(j).getSeasonNumber() - 1] +=
                                                            myInput.getCommands().get(j).getGrade();
                                                    counters[myInput.getCommands()
                                                            .get(j).getSeasonNumber() - 1]++;
                                                    myInput.removeCommand(myInput
                                                            .getCommands().get(j));
                                                    break;

                                                }
                                            }
                                        }
                                    }
                                }
                                if (myInput.getCommands().size() < end) {
                                    end = myInput.getCommands().size();
                                }
                            }
                            if (type == 1) {

                                for (int k = 0; k < serialRatings.length; k++) {
                                    if (counters[k] != 0.00) {
                                        serialRatings[k] = serialRatings[k] / counters[k];
                                    }
                                }
                                double rating = 0.00;
                                for (double serialRating : serialRatings) {
                                    rating += serialRating;
                                }

                                rating = rating / noSeasons;
                                videos.put(myInput.getCommands().get(i).getTitle(),
                                        rating);
                            } else {
                                videoRating = videoRating / numberRatings;
                                videos.put(myInput.getCommands().get(i).getTitle(),
                                        videoRating);
                            }
                        }
                    }
                }
            }
            if (myInput.getCommands().size() < end) {
                end = myInput.getCommands().size();
            }
        }

        String thisYear = action.getFilters().get(0).get(0);
        String thisGenre = action.getFilters().get(1).get(0);

        // movie
        if (action.getObjectType().equals("movies")) {
            // no filters
            videos.entrySet().removeIf(e -> !functions.IsMovieFilters.func(
                    myInput.getMovies(), e.getKey(), thisGenre, thisYear));
            // delete serials
            videos.entrySet().removeIf(e -> functions.IsSerial.func(
                    myInput.getSerials(), e.getKey()));
        } else { // serial
            // no filters
            videos.entrySet().removeIf(e -> !functions.IsSerialFilters.func(
                    myInput.getSerials(), e.getKey(), thisGenre, thisYear));
            // delete movies
            videos.entrySet().removeIf(e -> functions.IsMovie.func(
                    myInput.getMovies(), e.getKey()));
        }

        HashMap<String, Double> sortedVideos;
        if (action.getSortType().equals("asc")) {
            sortedVideos = querySearch2(videos);
        } else {
            sortedVideos = querySearch(videos);
        }

        if (sortedVideos.isEmpty()) {
            message += "]";
            JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                    message);
            arrayResult.add(jsO);
        } else {
            int cnt = 0;

            if (action.getNumber() >= sortedVideos.size()) {
                for (Map.Entry<String, Double> entry : sortedVideos.entrySet()) {
                    message += entry.getKey() + ", ";
                }
            } else {
                for (Map.Entry<String, Double> entry : sortedVideos.entrySet()) {
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
