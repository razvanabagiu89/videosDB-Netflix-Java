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

import static utils.Utils.initializeInput;
import static utils.Utils.initializeUsers;

public final class CommandRating {
    private CommandRating() {
    }
    /**
     * function to output what rating received a video and by what user
     * if user didn't see the video, it won't count (error message)
     */
    public static void func(final Input input, final Writer fileWriter, final JSONArray arrayResult,
                            final ActionInputData action, int end) throws IOException {

        String message;
        MyInput myInput = initializeInput(input);
        ArrayList<MyUser> myUsers = initializeUsers(input.getUsers());

        // cycling all command views and validating them in each user's history
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

        // cycling command rating to validate each rating
        for (int i = 0; i < end; i++) {
            if (myInput.getCommands().get(i).getActionType().equals("command")) {
                if (myInput.getCommands().get(i).getType().equals("rating")) {

                    int isSeason;
                    // user from action
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

                    // check if it was seen
                    if (thisUser.getHistory().containsKey(myInput
                            .getCommands().get(i).getTitle())) {

                        // serial
                        if (isSeason > 0) {
                            thisUser.addToRatingShow(
                                    myInput.getCommands().get(i).getTitle(),
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

        String thisUsername = action.getUsername();
        int index = 0;
        for (int i = 0; i < myUsers.size(); i++) {
            if (myUsers.get(i).getUsername().equals(thisUsername)) {
                index = i;
                break;
            }
        }
        MyUser thisUser = myUsers.get(index);
        int isSeason = action.getSeasonNumber(); // serial or movie


        if (thisUser.getHistory().containsKey(action.getTitle())) {

            //serial
            if (isSeason > 0) {

                if (thisUser.containsShow(action.getTitle(), action.getSeasonNumber())) {
                    message = "error -> " + action.getTitle() + " has been already rated";

                } else {
                    message = "success -> " + action.getTitle()
                            + " was rated with " + action.getGrade()
                            + " by " + thisUser.getUsername();
                }
            } else { // movie

                if (!thisUser.getRatingsMovie().containsKey(action.getTitle())) {

                    message = "success -> " + action.getTitle()
                            + " was rated with " + action.getGrade()
                            + " by " + thisUser.getUsername();
                } else {
                    message = "error -> " + action.getTitle() + " has been already rated";
                }
            }
        } else {
            message = "error -> " + action.getTitle()
                    + " is not seen";
        }
        JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                message);
        arrayResult.add(jsO);
    }
}
