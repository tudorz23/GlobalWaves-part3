package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.audio.Audio;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MapOperations {
    public static LinkedHashMap<Audio, Integer> sortAudioMapByValue(Map<Audio, Integer> map) {
        // https://stackoverflow.com/questions/2864840/treemap-sort-by-value
        LinkedHashMap<Audio, Integer> sortedMap = map.entrySet().stream()
                .sorted((e1, e2) -> {
                    int valueCompare = e2.getValue().compareTo(e1.getValue());
                    if (valueCompare != 0) {
                        return valueCompare;
                    }
                    return e1.getKey().getName().compareTo(e2.getKey().getName());
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

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


    public static ObjectNode createAudioMapObjectNode(LinkedHashMap<Audio, Integer> map) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode mapNode = mapper.createObjectNode();

        int cnt = 0;
        for (Map.Entry<Audio, Integer> entry : map.entrySet()) {
            mapNode.put(entry.getKey().getName(), entry.getValue());

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
