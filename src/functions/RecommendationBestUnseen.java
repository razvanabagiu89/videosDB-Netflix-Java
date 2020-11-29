package functions;

import myinput.MyInput;
import fileio.ActionInputData;
import fileio.Input;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import user.MyUser;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static utils.Utils.initializeInput;

public final class RecommendationBestUnseen {
    private RecommendationBestUnseen() {
    }
    /**
     * converts an integer to a boolean
     */
    public static boolean intToBool(final int intValue) {
        boolean boolValue;

        boolValue = intValue >= 1;
        return boolValue;
    }
    /**
     * outputs the best video based on ratings that the user hasn't seen
     */
    public static void func(final Input input, final Writer fileWriter, final JSONArray arrayResult,
                            final ActionInputData action, int end) throws IOException {

        MyInput myInput = initializeInput(input);
        String message = "BestRatedUnseenRecommendation cannot be applied!";

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

        // cycle all ratings
        for (int i = 0; i < end; i++) {

            int type = 0;
            exists1 = false;
            exists2 = false;

            if (myInput.getCommands().get(i).getActionType().equals("command")) {
                if (myInput.getCommands().get(i).getType().equals("rating")) {

                    // movie
                    for (int k = 0; k < myInput.getMovies().size(); k++) {
                        if (myInput.getCommands().get(i).getTitle().equals(myInput.getMovies()
                                .get(k).getTitle())) {
                            exists1 = true;
                            type = 0;
                            break;
                        }
                    }

                    // serial
                    for (int k = 0; k < myInput.getSerials().size(); k++) {
                        if (myInput.getCommands().get(i).getTitle().equals(myInput.getSerials()
                                .get(k).getTitle())) {
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

                        if (myInput.getCommands().get(i).getUsername().equals(myInput.getUsers()
                                .get(k).getUsername())) {

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

                            if (myInput.getCommands().get(j).getActionType().equals("command")) {
                                if (myInput.getCommands().get(j).getType().equals("rating")) {

                                    if (myInput.getCommands().get(i).getTitle().equals(
                                            myInput.getCommands().get(j).getTitle()
                                    )) {

                                        for (int k = 0; k < myInput.getUsers().size(); k++) {

                                            if (myInput.getCommands().get(j).getUsername().equals(
                                                    myInput.getUsers().get(k).getUsername())) {

                                                if (myInput.getUsers().get(k).getHistory()
                                                        .containsKey(myInput.getCommands().
                                                                get(j).getTitle())) {
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
                                                myInput.removeCommand(myInput.getCommands().get(j));
                                                break;
                                            } else if (intToBool(myInput.getCommands() // serial
                                                    .get(i).getSeasonNumber())) {

                                                serialRatings[myInput.getCommands()
                                                        .get(j).getSeasonNumber() - 1] +=
                                                        myInput.getCommands().get(j).getGrade();
                                                counters[myInput.getCommands().get(j)
                                                        .getSeasonNumber() - 1]++;
                                                myInput.removeCommand(myInput.getCommands().get(j));
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (type == 1) {

                            for (int k = 0; k < serialRatings.length; k++) {
                                if (counters[k] != 0.00) {
                                    serialRatings[k] = serialRatings[k] / counters[k];
                                }
                            }
                            double ratings = 0.00;
                            for (double serialRating : serialRatings) {
                                ratings += serialRating;
                            }

                            ratings = ratings / noSeasons;
                            videos.put(myInput.getCommands().get(i).getTitle(),
                                    ratings);
                        } else {
                            videoRating = videoRating / numberRatings;
                            videos.put(myInput.getCommands().get(i).getTitle(),
                                    videoRating);
                        }
                    }
                }
            }
        }
        Map<String, Double> sortedVideos = videos
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        // cycle all users
        for (int j = 0; j < myInput.getUsers().size(); j++) {

            MyUser user1 = new MyUser(myInput.getUsers().get(j).getUsername(),
                    myInput.getUsers().get(j).getSubscriptionType(),
                    myInput.getUsers().get(j).getHistory(),
                    myInput.getUsers().get(j).getFavoriteMovies());

            if (user1.getUsername().equals(action.getUsername())) {

                for (Map.Entry<String, Double> entry : sortedVideos.entrySet()) {

                    // recommend
                    if (!user1.getHistory().containsKey(entry.getKey())) {

                        if (!Double.isNaN(entry.getValue())) {
                            message = "BestRatedUnseenRecommendation result: " + entry.getKey();
                            break;
                        }
                    } else {  // if all recommended are already seen, recommend from database
                        for (int k = 0; k < myInput.getMovies().size(); k++) {
                            if (!user1.getHistory().containsKey(myInput
                                    .getMovies().get(k).getTitle())) {

                                message = "BestRatedUnseenRecommendation result: "
                                        + myInput.getMovies().get(k).getTitle();
                                break;
                            }
                        }
                    }
                }
            }
        }
        JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                message);
        arrayResult.add(jsO);
    }
}
