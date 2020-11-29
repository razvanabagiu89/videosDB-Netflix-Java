package sortfunctions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.LinkedHashMap;

public final class MapInteger {
    private MapInteger() {
    }
    /**
     * for maps that contain integer as values
     * first criteria value, then name - ascendent
     */
    public static HashMap<String, Integer> integerAscAsc(final HashMap<String, Integer> hm) {
        List<Map.Entry<String, Integer>> list =
                new LinkedList<>(hm.entrySet());

        list.sort(new Comparator<>() {
            public int compare(final Map.Entry<String, Integer> o1,
                               final Map.Entry<String, Integer> o2) {
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

        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
    /**
     * for maps that contain integer as values
     * first criteria value, then name - descendent
     */
    public static HashMap<String, Integer> integerDescDesc(final HashMap<String, Integer> hm) {
        List<Map.Entry<String, Integer>> list =
                new LinkedList<>(hm.entrySet());

        list.sort(new Comparator<>() {
            public int compare(final Map.Entry<String, Integer> o1,
                               final Map.Entry<String, Integer> o2) {
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

        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
