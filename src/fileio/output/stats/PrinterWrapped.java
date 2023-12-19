package fileio.output.stats;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.analytics.ArtistAnalytics;
import database.analytics.HostAnalytics;
import database.users.Artist;
import database.users.Host;
import database.users.User;
import fileio.output.PrinterComplex;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PrinterWrapped extends PrinterComplex {
    private final User user;

    /* Constructor */
    public PrinterWrapped(final User user, final Session session,
                          final ArrayNode output) {
        super(session, output);
        this.user = user;
    }


    public void printBasicUser() {
        ObjectNode commandNode = mapper.createObjectNode();

        commandNode.put("command", "wrapped");
        commandNode.put("user", user.getUsername());
        commandNode.put("timestamp", session.getTimestamp());

        LinkedHashMap<String, Integer> sortedArtists = sortTreeMapByValue(user.getAnalytics().getTopArtists());
        LinkedHashMap<String, Integer> sortedGenres = sortTreeMapByValue(user.getAnalytics().getTopGenres());
        LinkedHashMap<String, Integer> sortedSongs = sortTreeMapByValue(user.getAnalytics().getTopSongs());
        LinkedHashMap<String, Integer> sortedAlbums = sortTreeMapByValue(user.getAnalytics().getTopAlbums());
        LinkedHashMap<String, Integer> sortedPodcasts = sortTreeMapByValue(user.getAnalytics().getTopPodcasts());

        ObjectNode resultNode = mapper.createObjectNode();
        ObjectNode topArtists = createLinkedMapObjectNode(sortedArtists);
        ObjectNode topGenres = createLinkedMapObjectNode(sortedGenres);
        ObjectNode topSongs = createLinkedMapObjectNode(sortedSongs);
        ObjectNode topAlbums = createLinkedMapObjectNode(sortedAlbums);
        ObjectNode topPodcasts = createLinkedMapObjectNode(sortedPodcasts);

        resultNode.set("topArtists", topArtists);
        resultNode.set("topGenres", topGenres);
        resultNode.set("topSongs", topSongs);
        resultNode.set("topAlbums", topAlbums);
        resultNode.set("topPodcasts", topPodcasts);

        commandNode.set("result", resultNode);
        output.add(commandNode);
    }


    public void printArtist() {
        ObjectNode commandNode = mapper.createObjectNode();

        commandNode.put("command", "wrapped");
        commandNode.put("user", user.getUsername());
        commandNode.put("timestamp", session.getTimestamp());

        ArtistAnalytics artistAnalytics = ((Artist) user).getArtistAnalytics();

        LinkedHashMap<String, Integer> sortedAlbums = sortTreeMapByValue(artistAnalytics.getArtistTopAlbums());
        LinkedHashMap<String, Integer> sortedSongs = sortTreeMapByValue(artistAnalytics.getArtistTopSongs());
        LinkedHashMap<String, Integer> sortedFans = sortTreeMapByValue(artistAnalytics.getArtistTopFans());

        ObjectNode resultNode = mapper.createObjectNode();
        ObjectNode topAlbums = createLinkedMapObjectNode(sortedAlbums);
        ObjectNode topSongs = createLinkedMapObjectNode(sortedSongs);
        ArrayNode topFans = createLinkedMapKeyArrayNode(sortedFans);

        resultNode.set("topAlbums", topAlbums);
        resultNode.set("topSongs", topSongs);
        resultNode.set("topFans", topFans);
        resultNode.put("listeners", artistAnalytics.getArtistTopFans().size());

        commandNode.set("result", resultNode);
        output.add(commandNode);
    }


    public void printHost() {
        ObjectNode commandNode = mapper.createObjectNode();

        commandNode.put("command", "wrapped");
        commandNode.put("user", user.getUsername());
        commandNode.put("timestamp", session.getTimestamp());

        HostAnalytics hostAnalytics = ((Host) user).getHostAnalytics();

        LinkedHashMap<String, Integer> sortedEpisodes = sortTreeMapByValue(hostAnalytics.getHostTopEpisodes());

        ObjectNode resultNode = mapper.createObjectNode();
        ObjectNode topEpisodes = createLinkedMapObjectNode(sortedEpisodes);

        resultNode.set("topEpisodes", topEpisodes);
        resultNode.put("listeners", hostAnalytics.getHostTopFans().size());

        commandNode.set("result", resultNode);
        output.add(commandNode);
    }

    public LinkedHashMap<String, Integer> sortTreeMapByValue(Map<String, Integer> treeMap) {
        // https://stackoverflow.com/questions/2864840/treemap-sort-by-value
        LinkedHashMap<String, Integer> sortedMap = treeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return sortedMap;
    }


    private ObjectNode createLinkedMapObjectNode(LinkedHashMap<String, Integer> map) {
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

    private ArrayNode createLinkedMapKeyArrayNode(LinkedHashMap<String, Integer> map) {
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
