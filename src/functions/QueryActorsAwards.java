package functions;

import myinput.MyInput;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static common.Constants.AWARD;
import static sortfunctions.MapInteger.integerAscAsc;
import static sortfunctions.MapInteger.integerDescDesc;
import static utils.Utils.initializeInput;

public final class QueryActorsAwards {

    private QueryActorsAwards() {
    }

    /**
     * outputs all actors with the queried awards
     */
    public static void func(final Input input, final Writer fileWriter, final JSONArray arrayResult,
                            final ActionInputData action) throws IOException {

        String message = "Query result: [";
        MyInput myInput = initializeInput(input);
        HashMap<String, Integer> actors = new HashMap<>();

        // cycle all actors
        for (int i = 0; i < myInput.getActors().size(); i++) {

            int isValid = 0;
            int noAwards;
            ActorInputData thisActor = myInput.getActors().get(i);

            // cycle all awards
            for (int k = 0; k < action.getFilters().get(AWARD).size(); k++) {

                String thisAward = action.getFilters().get(AWARD).get(k);

                // check each award
                if (thisActor.getAwards().containsKey(Utils.stringToAwards(thisAward))) {
                    isValid++;
                }
            }

            // if actor has all awards
            if (isValid == action.getFilters().get(AWARD).size()) {
                noAwards = thisActor.getAwards().values().stream().reduce(0, Integer::sum);
                actors.put(thisActor.getName(), noAwards);
            }
        }

        Map<String, Integer> sortedActors;
        if (action.getSortType().equals("asc")) {
            sortedActors = integerAscAsc(actors);
        } else {
            sortedActors = integerDescDesc(actors);
        }

        if (sortedActors.isEmpty()) {
            message += "]";
        } else {
            for (Map.Entry<String, Integer> entry : sortedActors.entrySet()) {
                message += entry.getKey() + ", ";
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
