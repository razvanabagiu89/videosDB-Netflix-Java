package functions;

import myinput.MyInput;
import fileio.ActionInputData;
import fileio.Input;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import user.MyUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static utils.Utils.initializeInput;
import static utils.Utils.initializeUsers;

public final class RecommendationPopular {

    private RecommendationPopular() {
    }

    /**
     * recommends the first unseen video from the most popular genre
     * popularity is computed by ratings
     */
    public static void func(final Input input, final Writer fileWriter, final JSONArray arrayResult,
                            final ActionInputData action, final int end) throws IOException {


        MyInput myInput = initializeInput(input);
        String message = "PopularRecommendation cannot be applied!";
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
        HashMap<String, Integer> popularGenres = new HashMap<>();

        // cycle all users
        for (MyUser myUser : myUsers) {

            // and their history
            for (Map.Entry<String, Integer> entry : myUser.getHistory().entrySet()) {

                String thisTitle = entry.getKey();

                // movie
                for (int j = 0; j < myInput.getMovies().size(); j++) {
                    if (myInput.getMovies().get(j).getTitle().equals(thisTitle)) {

                        for (int k = 0; k < myInput.getMovies().get(j).getGenres().size(); k++) {

                            String thisGenre = myInput.getMovies().get(j).getGenres().get(k);

                            if (!popularGenres.containsKey(thisGenre)) {
                                popularGenres.put(thisGenre, entry.getValue());
                            } else {
                                int compareMe = 0;
                                for (Map.Entry<String, Integer> popularG
                                        :popularGenres.entrySet()) {
                                    if (popularG.getKey().equals(thisGenre)) {

                                        compareMe = popularG.getValue();
                                        break;
                                    }
                                }
                                popularGenres.put(thisGenre, entry.getValue() + compareMe);
                            }
                        }
                    }
                }

                // serial
                for (int j = 0; j < myInput.getSerials().size(); j++) {
                    if (myInput.getSerials().get(j).getTitle().equals(thisTitle)) {

                        for (int k = 0; k < myInput.getSerials().get(j).getGenres().size(); k++) {

                            String thisGenre = myInput.getSerials().get(j).getGenres().get(k);

                            if (!popularGenres.containsKey(thisGenre)) {
                                popularGenres.put(thisGenre, entry.getValue());
                            } else {
                                int compareMe = 0;
                                for (Map.Entry<String, Integer> popularG
                                        : popularGenres.entrySet()) {
                                    if (popularG.getKey().equals(thisGenre)) {
                                        compareMe = popularG.getValue();
                                        break;
                                    }
                                }
                                popularGenres.put(thisGenre, entry.getValue() + compareMe);
                            }
                        }
                    }
                }
            }
        }
        Map<String, Integer> sortedGenres =
                popularGenres.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));
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
        } else {

            // check if movie or serial
            boolean okM = false;
            boolean okS = false;

            for (Map.Entry<String, Integer> entry : sortedGenres.entrySet()) {

                String thisGenre2 = entry.getKey();
                String thisTitle;

                //movies
                for (int i = 0; i < myInput.getMovies().size(); i++) {

                    //genres
                    for (int j = 0; j < myInput.getMovies().get(i).getGenres().size(); j++) {

                        if (myInput.getMovies().get(i).getGenres().get(j).equals(thisGenre2)) {

                            thisTitle = myInput.getMovies().get(i).getTitle();

                            if (!thisUser.getHistory().containsKey(thisTitle)) {
                                message = "PopularRecommendation result: " + thisTitle;
                                okM = true;
                                break;
                            }
                        }
                    }
                    if (okM) {
                        break;
                    }
                }

                if (!okM) {
                    //serials
                    for (int i = 0; i < myInput.getSerials().size(); i++) {

                        //genres
                        for (int j = 0; j < myInput.getSerials().get(i).getGenres().size(); j++) {

                            if (myInput.getSerials().get(i).getGenres().get(j).equals(thisGenre2)) {

                                thisTitle = myInput.getSerials().get(i).getTitle();

                                if (!thisUser.getHistory().containsKey(thisTitle)) {
                                    message = "PopularRecommendation result: " + thisTitle;
                                    okS = true;
                                    break;
                                }
                            }
                        }
                        if (okS) {
                            break;
                        }
                    }
                }
                if (okM || okS) {
                    break;
                }
            }
            JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                    message);
            arrayResult.add(jsO);
        }
    }
}
