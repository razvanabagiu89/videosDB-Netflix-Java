package sortfunctions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.LinkedHashMap;

public final class MapDouble {
    private MapDouble() {
    }
    /**
     * for maps that contain double as values
     * first criteria value, then name - ascendent
     */
    public static HashMap<String, Double> doubleAscAsc(final HashMap<String, Double> hm) {
        List<Map.Entry<String, Double>> list =
                new LinkedList<>(hm.entrySet());

        list.sort(new Comparator<>() {
            public int compare(final Map.Entry<String, Double> o1,
                               final Map.Entry<String, Double> o2) {
                double value = o1.getValue() - o2.getValue();
                if (value > 0) {
                    return 1;
                } else if (value < 0) {
                    return -1;
                } else if (value == 0.0) {
                    int text = o1.getKey().compareTo(o2.getKey());
                    if (text > 0) {
                        return 1;
                    } else {
                        return -1;
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
    /**
     * for maps that contain double as values
     * first criteria value, then name - descendent
     */
    public static HashMap<String, Double> doubleDescDesc(final HashMap<String, Double> hm) {
        List<Map.Entry<String, Double>> list =
                new LinkedList<>(hm.entrySet());

        list.sort(new Comparator<>() {
            public int compare(final Map.Entry<String, Double> o1,
                               final Map.Entry<String, Double> o2) {
                double value = o1.getValue() - o2.getValue();
                if (value > 0) {
                    return -1;
                } else if (value < 0) {
                    return 1;
                } else if (value == 0.0) {
                    int text = o1.getKey().compareTo(o2.getKey());
                    if (text > 0) {
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
