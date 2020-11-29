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

import static sortfunctions.MapInteger.integerAscAsc;
import static sortfunctions.MapInteger.integerDescDesc;
import static utils.Utils.initializeInput;
import static utils.Utils.initializeUsers;

public final class QueryUser {
    private QueryUser() {
    }
    /**
     * outputs the most active users - users which gave the most ratings
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

        // update each user's rating and validate each rating
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
                        } else { // movie
                            if (!thisUser.getRatingsMovie().containsKey(
                                    myInput.getCommands().get(i).getTitle())) {
                                thisUser.addToRatingMovie(myInput.getCommands().get(i).getTitle(),
                                        myInput.getCommands().get(i).getGrade());
                            }
                        }
                    }
                }
            }
        }
        HashMap<String, Integer> users = new HashMap<>();

        // cycle all users to get ratings
        for (MyUser myUser : myUsers) {

            int nrRatings = myUser.getRatingsMovie().size()
                    + myUser.getRatingsShow().size() / 2;

            if (nrRatings != 0) {
                users.put(myUser.getUsername(), nrRatings);
            }
        }
        Map<String, Integer> sortedUsers;

        if (action.getSortType().equals("asc")) {
            sortedUsers = integerAscAsc(users);
        } else {
            sortedUsers = integerDescDesc(users);
        }

        if (sortedUsers.isEmpty()) {
            message += "]";
            JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                    message);
            arrayResult.add(jsO);
        } else {
            int cnt = 0;

            if (action.getNumber() >= sortedUsers.size()) {
                for (Map.Entry<String, Integer> entry : sortedUsers.entrySet()) {
                    message += entry.getKey() + ", ";
                }
            } else {
                for (Map.Entry<String, Integer> entry : sortedUsers.entrySet()) {
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
