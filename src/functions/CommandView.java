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
import java.util.Map;

import static utils.Utils.initializeInput;
import static utils.Utils.initializeUsers;

public final class CommandView {
    private CommandView() {
    }
    /**
     * function to output if a video was seen or not, and how many times
     */
    public static void func(final Input input, final Writer fileWriter, final JSONArray arrayResult,
                            final ActionInputData action, int end) throws IOException {

        String message = "";
        MyInput myInput = initializeInput(input);
        ArrayList<MyUser> myUsers = initializeUsers(input.getUsers());

        // cycle all previous command view and update history of each user
        for (int i = 0; i < end; i++) {
            if (myInput.getCommands().get(i).getActionType().equals("command")) {
                if (myInput.getCommands().get(i).getType().equals("view")) {

                    String thisVideo = myInput.getCommands().get(i).getTitle();

                    // check if video exists in database
                    boolean existsVideo1 = functions.IsMovie.func(myInput.getMovies(), thisVideo);
                    boolean existsVideo2 = functions.IsSerial.func(myInput.getSerials(), thisVideo);

                    // user from current action
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
        //user from action
        int index = 0;
        String thisUsername = action.getUsername();
        for (int j = 0; j < myUsers.size(); j++) {
            if (myUsers.get(j).getUsername().equals(thisUsername)) {
                index = j;
                break;
            }
        }
        MyUser thisUser = myUsers.get(index);

        String thisVideo = action.getTitle();

        if (!thisUser.getHistory().containsKey(thisVideo)) {
            message = "success -> " + action.getTitle() + " was "
                    + "viewed with total views of 1";
        } else {
            for (Map.Entry<String, Integer> entry : thisUser.getHistory().entrySet()) {
                if (entry.getKey().equals(thisVideo)) {
                    message = "success -> " + action.getTitle() + " was "
                            + "viewed with total views of " + (entry.getValue() + 1);

                }
            }
        }
        JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                message);
        arrayResult.add(jsO);
    }
}
