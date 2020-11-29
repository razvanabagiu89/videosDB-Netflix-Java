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

public final class CommandFavourite {
    private CommandFavourite() {
}
    /**
     * function to put a video in a user's favourite video list if it was seen before
     * otherwise, it displays an error message
     */
    public static void func(final Input input, final Writer fileWriter, final JSONArray arrayResult,
                            final ActionInputData action, int end) throws IOException {

        String message = "";
        MyInput myInput = initializeInput(input);
        ArrayList<MyUser> myUsers = initializeUsers(myInput.getUsers());

        // cycling all command views and validating them in each user's history
        for (int i = 0; i < end; i++) {
            if (myInput.getCommands().get(i).getActionType().equals("command")) {
                if (myInput.getCommands().get(i).getType().equals("view")) {

                    // check if given movie is in the database
                    boolean existsVideo1 = IsMovie.func(myInput.getMovies(),
                            myInput.getCommands().get(i).getTitle());
                    boolean existsVideo2 = IsSerial.func(myInput.getSerials(),
                            myInput.getCommands().get(i).getTitle());
                    String thisVideo = myInput.getCommands().get(i).getTitle();


                    // user from the action
                    int index = 0;
                    String thisUsername = myInput.getCommands().get(i).getUsername();
                    for (int j = 0; j < myUsers.size(); j++) {
                        if (myUsers.get(j).getUsername().equals(thisUsername)) {
                            index = j;
                            break;
                        }
                    }
                    MyUser thisUser = myUsers.get(index);

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

        // cycling all command favourites and validating them in each user's fav list
        for (int i = 0; i < end; i++) {
            if (myInput.getCommands().get(i).getActionType().equals("command")) {
                if (myInput.getCommands().get(i).getType().equals("favorite")) {

                    String thisUsername = myInput.getCommands().get(i).getUsername();
                    String thisTitle = myInput.getCommands().get(i).getTitle();

                    for (MyUser myUser : myUsers) {
                        if (myUser.getUsername().equals(thisUsername)) {
                            if (myUser.getHistory().containsKey(thisTitle)) {
                                if (!myUser.getFavoriteMovies().contains(thisTitle)) {
                                    myUser.addToFav(thisTitle);
                                }
                            }
                        }
                    }
                }
            }
        }

        String thisUsername = action.getUsername();
        String thisTitle = action.getTitle();

        for (MyUser myUser : myUsers) {
            if (myUser.getUsername().equals(thisUsername)) {
                if (myUser.getHistory().containsKey(thisTitle)) {

                    if (myUser.containsFav(thisTitle)) {
                        message = "error -> " + thisTitle + " is already "
                                + "in favourite list";
                    } else {
                        message = "success -> " + action.getTitle() + " was added "
                                + "as favourite";
                    }
                } else {
                    message = "error -> " + action.getTitle()
                            + " is not seen";
                }
            }
        }
        JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                message);
        arrayResult.add(jsO);
    }
}
