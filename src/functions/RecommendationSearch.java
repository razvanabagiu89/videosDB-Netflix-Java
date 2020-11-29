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
import java.util.Iterator;

import static functions.RecommendationBestUnseen.intToBool;
import static sortfunctions.SortSearch.sortSearch;
import static utils.Utils.initializeInput;
import static utils.Utils.initializeUsers;

public final class RecommendationSearch {
    private RecommendationSearch() {
    }
    /**
     * recommends all videos unseen by a user with the genre given
     */
    public static void func(final Input input, final Writer fileWriter,
                            final JSONArray arrayResult, final ActionInputData action,
                            int end) throws IOException {

        MyInput myInput = initializeInput(input);
        String message = "SearchRecommendation cannot be applied!";
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

                    // check if video exists
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
                            //daca il contine deja doar ii incrementam
                        }
                    }
                }
            }
        }

        // each video with its rating
        HashMap<String, Double> videos = new HashMap<>();
        // movie
        double videoRating;
        int numberRatings;

        boolean exists1;
        boolean exists2;
        boolean exists4;

        //serial
        double[] serialRatings;
        double noSeasons = 0.00;
        double[] counters;


        // compute rating
        for (int i = 0; i < end; i++) {

            int type = 0;
            exists1 = false;
            exists2 = false;

            if (myInput.getCommands().get(i).getActionType().equals("command")) {
                if (myInput.getCommands().get(i).getType().equals("rating")) {

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
                        serialRatings[myInput.getCommands().get(i).getSeasonNumber() - 1] =
                                myInput.getCommands().get(i).getGrade();

                        counters[myInput.getCommands().get(i).getSeasonNumber() - 1] = 1;
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

                        for (int j = i + 1; j < myInput.getCommands().size(); j++) {

                            exists4 = false;

                            if (myInput.getCommands().get(j).getActionType().equals("command")) {
                                if (myInput.getCommands().get(j).getType().equals("rating")) {

                                    if (myInput.getCommands().get(i).getTitle().equals(
                                            myInput.getCommands().get(j).getTitle()
                                    )) {

                                        for (int k = 0; k < myInput.getUsers().size(); k++) {

                                            if (myInput.getCommands().get(j).getUsername()
                                                    .equals(myInput.getUsers()
                                                            .get(k).getUsername())) {

                                                if (myInput.getUsers().get(k).getHistory()
                                                        .containsKey(myInput.getCommands()
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
                                                myInput.removeCommand(myInput.
                                                        getCommands().get(j));  // serial
                                            } else if (intToBool(myInput.getCommands()
                                                    .get(i).getSeasonNumber())) {

                                                serialRatings[myInput.getCommands()
                                                        .get(j).getSeasonNumber() - 1] +=
                                                        myInput.getCommands().get(j).getGrade();
                                                counters[myInput.getCommands().
                                                        get(j).getSeasonNumber() - 1]++;
                                                myInput.removeCommand(myInput
                                                        .getCommands().get(j));
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
            if (myInput.getCommands().size() < end) {
                end = myInput.getCommands().size();
            }
        }

        // movie
        for (int i = 0; i < myInput.getMovies().size(); i++) {
            if (!videos.containsKey(myInput.getMovies().get(i).getTitle())) {
                videos.put(myInput.getMovies().get(i).getTitle(), 0.0);
            }
        }

        //serial
        for (int i = 0; i < myInput.getSerials().size(); i++) {
            if (!videos.containsKey(myInput.getSerials().get(i).getTitle())) {
                videos.put(myInput.getSerials().get(i).getTitle(), 0.0);
            }
        }

        String thisGenre = action.getGenre();

        for (Iterator<Map.Entry<String, Double>> it = videos
                .entrySet().iterator(); it.hasNext();) {

            Map.Entry<String, Double> entry = it.next();

            // check if it's not given genre, then remove
            // movie
            for (int i = 0; i < myInput.getMovies().size(); i++) {

                if (myInput.getMovies().get(i).getTitle().equals(entry.getKey())) {

                    if (!myInput.getMovies().get(i).getGenres().contains(thisGenre)) {
                        it.remove();
                        break;
                    }
                }
            }

            // serial
            for (int i = 0; i < myInput.getSerials().size(); i++) {

                if (myInput.getSerials().get(i).getTitle().equals(entry.getKey())) {

                    if (!myInput.getSerials().get(i).getGenres().contains(thisGenre)) {
                        it.remove();
                        break;
                    }
                }
            }
        }
        Map<String, Double> sortedVideos = sortSearch(videos);

        // action user
        int index = 0;
        String thisUsername = action.getUsername();
        for (int i = 0; i < myUsers.size(); i++) {
            if (myUsers.get(i).getUsername().equals(thisUsername)) {
                index = i;
                break;
            }
        }
        MyUser thisUser = myUsers.get(index);
        if (!thisUser.getSubscriptionType().equals("PREMIUM")) {
            JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                    message);
            arrayResult.add(jsO);
        }
        message = "SearchRecommendation result: [";

        for (Map.Entry<String, Double> entry : sortedVideos.entrySet()) {

            if (!thisUser.getHistory().containsKey(entry.getKey())) {
                message += entry.getKey() + ", ";
            }
        }

        // format message
        String strNew = message.substring(0, message.length() - 1);
        strNew = message.substring(0, message.length() - 2);
        message = strNew;
        message += "]";

        if (message.equals("SearchRecommendation result:]")) {
            message = "SearchRecommendation cannot be applied!";
        }

        JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                message);
        arrayResult.add(jsO);
    }
}
