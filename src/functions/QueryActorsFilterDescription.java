package functions;

import myinput.MyInput;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static utils.Utils.initializeInput;

public final class QueryActorsFilterDescription {

    private QueryActorsFilterDescription() {
    }
    /**
     * outputs all actors that have queried keywords in their description
     */
    public static void func(final Input input, final Writer fileWriter, final JSONArray arrayResult,
                            final ActionInputData action) throws IOException {

        String message = "Query result: [";
        MyInput myInput = initializeInput(input);
        ArrayList<String> actors = new ArrayList<>();

        // cycle all actors
        for (int i = 0; i < myInput.getActors().size(); i++) {

            int isValid = 0;
            ActorInputData thisActor = myInput.getActors().get(i);

            // cycle all keywords
            for (int k = 0; k < action.getFilters().get(2).size(); k++) {

                String thisWord = action.getFilters().get(2).get(k);

                if (thisActor.getCareerDescription().toLowerCase()
                        .contains(" " + thisWord.toLowerCase())) {
                    isValid++;
                } else if (thisActor.getCareerDescription().toLowerCase()
                        .contains("-" + thisWord.toLowerCase())) {
                    isValid++;
                }
            }

            // check if it has all keywords
            if (isValid == action.getFilters().get(2).size()) {
                actors.add(thisActor.getName());
            }
        }

        if (action.getSortType().equals("asc")) {
            Collections.sort(actors);
        } else {
            Collections.sort(actors);
            Collections.reverse(actors);
        }

        if (actors.isEmpty()) {
            message += "]";
        } else {
            for (String actor : actors) {
                message += actor + ", ";
            }

            // format message
            String strNew = message.substring(0, message.length() - 1);
            strNew = message.substring(0, message.length() - 2);
            message = strNew;
            message += "]";
        }

        JSONObject jsO = fileWriter.writeFile(action.getActionId(), "",
                message);
        arrayResult.add(jsO);
    }
}
