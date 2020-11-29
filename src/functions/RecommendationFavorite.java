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

public final class RecommendationFavorite {
    private RecommendationFavorite() {
    }
    /**
     * recommends the video with the most ocurrences in all users favourite list
     */
    public static void func(final Input input, final Writer fileWriter, final JSONArray arrayResult,
                            final ActionInputData action, int end) throws IOException {

        String message;
        MyInput myInput = initializeInput(input);
        ArrayList<MyUser> myUsers = initializeUsers(input.getUsers());

        // update history
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

                    // check if video is in database
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
        // update all favourite lists with valid videos only
        for (int i = 0; i < end; i++) {

            if (myInput.getCommands().get(i).getActionType().equals("command")
                    && myInput.getCommands().get(i).getType().equals("favorite")) {

                boolean existsVideo1 = IsMovie.func(myInput.getMovies(),
                        myInput.getCommands().get(i).getTitle());
                boolean existsVideo2 = IsSerial.func(myInput.getSerials(),
                        myInput.getCommands().get(i).getTitle());

                if (existsVideo1 || existsVideo2) {

                    for (int j = 0; j < myInput.getUsers().size(); j++) {

                        if (myInput.getUsers().get(j).getUsername().equals(
                                myInput.getCommands().get(i).getUsername())) {

                            MyUser user1 = new MyUser(myInput.getUsers().get(j).getUsername(),
                                    myInput.getUsers().get(j).getSubscriptionType(),
                                    myInput.getUsers().get(j).getHistory(),
                                    myInput.getUsers().get(j).getFavoriteMovies());

                            if (user1.getHistory().containsKey(myInput.getCommands()
                                    .get(i).getTitle())) {
                                int index = myUsers.indexOf(user1);
                                if (index < 0) {
                                    break;
                                }
                                myUsers.get(index).addToFav(myInput.getCommands()
                                        .get(i).getTitle());
                                break;
                            }
                        }
                    }
                }
            }
        }
        String maxTitle = "";
        int findMax = -1;
        String usernameNeed;
        usernameNeed = action.getUsername();
        MyUser userFIN = null;

        for (MyUser myUser : myUsers) {
            if (myUser.getUsername().equals(usernameNeed)) {
                userFIN = myUser;
                break;
            }
        }
        // cycle all users
        for (int i = 0; i < myUsers.size(); i++) {

            String findMe;
            int findCounter;

            // and their favourite list
            for (int j = 0; j < myUsers.get(i).getFavoriteMovies().size(); j++) {
                findMe = myUsers.get(i).getFavoriteMovies().get(j);
                findCounter = 1;

                // don't count the action user
                if (userFIN.getHistory().containsKey(findMe)) {
                    continue;
                }

                for (int k = i + 1; k < myUsers.size(); k++) {

                    if (myUsers.get(k).containsFav(findMe)) {
                        findCounter++;
                        myUsers.get(k).removeFav(findMe);
                    }
                }
                if (findMax < findCounter) {
                    findMax = findCounter;
                    maxTitle = findMe;
                }
            }
        }
        if (maxTitle.equals("")) {
            message = "FavoriteRecommendation cannot be applied!";
        } else {
            message = "FavoriteRecommendation result: " + maxTitle;
        }
        JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                message);
        arrayResult.add(jsO);
    }
}
