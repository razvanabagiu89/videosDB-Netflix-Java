package sortfunctions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.LinkedHashMap;

public final class QueryRatingSearch2 {
    private QueryRatingSearch2() {
    }
    /**
     * sort for the query rating
     */
    public static HashMap<String, Double> querySearch2(final Map<String, Double> hm) {
        List<Map.Entry<String, Double>> list =
                new LinkedList<>(hm.entrySet());

        list.sort(new Comparator<>() {
            public int compare(final Map.Entry<String, Double> o1,
                               final Map.Entry<String, Double> o2) {
                double value = o1.getValue() - o2.getValue();
                //asc
                if (value > 0) {
                    return 1;
                } else if (value < 0) {
                    return -1;
                } else if (value == 0.0) {
                    //asc
                    int text = o1.getKey().compareTo(o2.getKey());
                    if (text < 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
                return -1;
            }
        });

        HashMap<String, Double> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
