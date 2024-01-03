package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.audio.Audio;
import database.monetization.ArtistMoneyStats;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static utils.Constants.MAX_MAP_TOP_RESULT_NUMBER;


/**
 * Utility class containing static methods to be applied on Maps.
 */
public final class MapOperations {
    /* Constructor */
    private MapOperations() {
    }


    /**
     * Sorts a map of < Audio, Integer > type descending by the value.
     * First, it merges Audio objects with the same name into a map
     * of < String, Integer > type.
     * @return Sorted LinkedHashMap of < name, Integer > type, where name is a String.
     */
    public static LinkedHashMap<String, Integer> sortAudioMapByValue(
                                        final Map<Audio, Integer> map) {
        // Merge the values for Audio objects that have the same name.
        Map<String, Integer> mergedEntries = new HashMap<>();

        for (Map.Entry<Audio, Integer> entry : map.entrySet()) {
            String audioName = entry.getKey().getName();
            int value = entry.getValue();

            mergedEntries.merge(audioName, value, Integer::sum);
        }

        return sortStringMapByValue(mergedEntries);
    }


    /**
     * Generic map sorting function descending by the values, for values implementing Comparable
     * @param map Map to be sorted, with String keys and T values.
     * @return A LinkedHashMap containing the sorted keys.
     * @param <T> Value for the Map, implementing Comparable (for example, Integer or Double).
     */
    public static <T extends Comparable<T>> LinkedHashMap<String, T> sortStringMapByValue(
                                                                final Map<String, T> map) {
        // Inspired by the answer of Vitalii Fedorenko
        // from https://stackoverflow.com/questions/2864840/treemap-sort-by-value.
        return map.entrySet().stream()
                .sorted((e1, e2) -> {
                    int valueCompare = e2.getValue().compareTo(e1.getValue());
                    if (valueCompare != 0) {
                        return valueCompare;
                    }
                    return e1.getKey().compareTo(e2.getKey());
                }).collect(Collectors.toMap(Map.Entry::getKey,
                                            Map.Entry::getValue,
                                            (e1, e2) -> e1,
                                            LinkedHashMap::new));
    }


    /**
     * Special function for the Monetization Command, sorting the entries
     * of the map in descending order by the total revenue of an artist.
     * @param map Map of < String, ArtistMoneyStats > type.
     * @return Sorted LinkedHashMap.
     */
    public static LinkedHashMap<String, ArtistMoneyStats> sortMonetization(
                                    final Map<String, ArtistMoneyStats> map) {
        return map.entrySet().stream()
                .sorted((e1, e2) -> {
                    int valueCompare = Double.compare(e2.getValue().getTotalRevenue(),
                            e1.getValue().getTotalRevenue());
                    if (valueCompare != 0) {
                        return valueCompare;
                    }
                    return e1.getKey().compareTo(e2.getKey());
                }).collect(Collectors.toMap(Map.Entry::getKey,
                                            Map.Entry::getValue,
                                            (e1, e2) -> e1,
                                            LinkedHashMap::new));
    }


    /**
     * Used for the Wrapped Commands as a helper to generate ObjectNode instances
     * for printing purposes, selecting only top 5 entries of the respective map
     * (which should be already sorted).
     */
    public static ObjectNode createStringMapObjectNode(final LinkedHashMap<String, Integer> map) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode mapNode = mapper.createObjectNode();

        int cnt = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            mapNode.put(entry.getKey(), entry.getValue());

            cnt++;
            if (cnt == MAX_MAP_TOP_RESULT_NUMBER) {
                break;
            }
        }

        return mapNode;
    }


    /**
     * Used for the Wrapped Command to obtain an ArrayNode containing only top 5 keys
     * of the given map (which should be already sorted).
     */
    public static ArrayNode createLinkedMapKeyArrayNode(final LinkedHashMap<String, Integer> map) {
        ObjectMapper mapper = new ObjectMapper();

        int cnt = 0;
        ArrayNode keyArrayNode = mapper.createArrayNode();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            keyArrayNode.add(entry.getKey());

            cnt++;
            if (cnt == MAX_MAP_TOP_RESULT_NUMBER) {
                break;
            }
        }

        return keyArrayNode;
    }
}
