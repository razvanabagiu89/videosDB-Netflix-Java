package functions;

import fileio.ActionInputData;
import fileio.Input;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import user.MyUser;

import java.io.IOException;

public final class RecommendationStandard {
    private RecommendationStandard() {
    }
    /**
     * recommends first unseen video from the database
     */
    public static void func(final Input input, final Writer fileWriter, final JSONArray arrayResult,
                            final ActionInputData action, int end) throws IOException {

        String message = "";
        for (int j = 0; j < input.getUsers().size(); j++) {

            MyUser user1 = new MyUser(input.getUsers().get(j).getUsername(),
                    input.getUsers().get(j).getSubscriptionType(),
                    input.getUsers().get(j).getHistory(),
                    input.getUsers().get(j).getFavoriteMovies());

            if (user1.getUsername().equals(action.getUsername())) {

                // movie
                for (int k = 0; k < input.getMovies().size(); k++) {

                    // if user didn't see it
                    if (!user1.getHistory().containsKey(input.getMovies().get(k).getTitle())) {

                        message = "StandardRecommendation result: "
                                + input.getMovies().get(k).getTitle();
                        break;
                    } else {
                        message = "StandardRecommendation cannot be applied!";
                    }
                }
            }
        }
        JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                message);
        arrayResult.add(jsO);
    }
}
