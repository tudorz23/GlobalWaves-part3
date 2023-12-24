package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.audio.Audio;
import database.monetization.ArtistMoneyStats;
import database.monetization.Monetization;

import java.util.*;
import java.util.stream.Collectors;

public class MapOperations {
    public static LinkedHashMap<String, Integer> sortAudioMapByValue(Map<Audio, Integer> map) {
        Map<String, Integer> mergedEntries = new HashMap<>();

        for (Map.Entry<Audio, Integer> entry : map.entrySet()) {
            String audioName = entry.getKey().getName();
            int value = entry.getValue();

            mergedEntries.merge(audioName, value, Integer::sum);
        }

        LinkedHashMap<String, Integer> sortedMap = sortStringMapByValue(mergedEntries);
        return sortedMap;
    }


    public static LinkedHashMap<String, Integer> sortStringMapByValue(Map<String, Integer> map) {
        // https://stackoverflow.com/questions/2864840/treemap-sort-by-value
        LinkedHashMap<String, Integer> sortedMap = map.entrySet().stream()
                .sorted((e1, e2) -> {
                    int valueCompare = e2.getValue().compareTo(e1.getValue());
                    if (valueCompare != 0) {
                        return valueCompare;
                    }
                    return e1.getKey().compareTo(e2.getKey());
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return sortedMap;
    }


    public static LinkedHashMap<String, ArtistMoneyStats> sortMonetization(Map<String,
            ArtistMoneyStats> map) {
        LinkedHashMap<String, ArtistMoneyStats> sortedMap = map.entrySet().stream()
                .sorted((e1, e2) -> {
                    int valueCompare = Double.compare(e2.getValue().getTotalRevenue(),
                            e1.getValue().getTotalRevenue());
                    if (valueCompare != 0) {
                        return valueCompare;
                    }
                    return e1.getKey().compareTo(e2.getKey());
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        return sortedMap;
    }


    public static ObjectNode createStringMapObjectNode(LinkedHashMap<String, Integer> map) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode mapNode = mapper.createObjectNode();

        int cnt = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            mapNode.put(entry.getKey(), entry.getValue());

            cnt++;
            if (cnt == 5) {
                break;
            }
        }

        return mapNode;
    }


    public static ArrayNode createLinkedMapKeyArrayNode(LinkedHashMap<String, Integer> map) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode keyArrayNode = mapper.createArrayNode();

        int cnt = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            keyArrayNode.add(entry.getKey());

            cnt++;
            if (cnt == 5) {
                break;
            }
        }

        return keyArrayNode;
    }
}
