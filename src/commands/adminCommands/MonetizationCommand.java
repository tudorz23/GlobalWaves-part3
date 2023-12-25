package commands.adminCommands;

import client.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.ICommand;
import database.audio.Song;
import database.monetization.ArtistMoneyStats;
import database.monetization.Monetization;
import utils.MapOperations;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MonetizationCommand implements ICommand {
    private final Session session;
    private final ArrayNode output;

    /* Constructor */
    public MonetizationCommand(Session session, final ArrayNode output) {
        this.session = session;
        this.output = output;
    }

    @Override
    public void execute() {
        session.getDatabase().simulateTimeForEveryone(session.getTimestamp());
        Monetization monetization = session.getDatabase().getMonetization();

        LinkedHashMap<String, ArtistMoneyStats> sortedMap = MapOperations
                                            .sortMonetization(monetization.getMonetizedArtists());

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", "endProgram");

        ObjectNode resultNode = mapper.createObjectNode();

        int cnt = 0;
        for (Map.Entry<String, ArtistMoneyStats> entry : sortedMap.entrySet()) {
            cnt++;

            ObjectNode artistNode = mapper.createObjectNode();
            artistNode.put("songRevenue", roundDouble(entry.getValue().getSongRevenue()));
            artistNode.put("merchRevenue", roundDouble(entry.getValue().getMerchRevenue()));
            artistNode.put("ranking", cnt);

            String mostProfitableSong = getMostProfitableSong(entry.getValue().getPayingSongs());
            if (mostProfitableSong == null) {
                artistNode.put("mostProfitableSong", "N/A");
            } else {
                artistNode.put("mostProfitableSong", mostProfitableSong);
            }

            resultNode.set(entry.getKey(), artistNode);
        }

        commandNode.set("result", resultNode);
        output.add(commandNode);
    }


    /**
     * Truncates a double variable, keeping only its first two decimals.
     */
    private double roundDouble(Double number) {
        return Math.round(number * 100.0) / 100.0;
    }


    /**
     * Computes the most profitable song of an artist.
     * @param payingSongs Map containing pairs of type < Song, revenue >.
     * @return Name of the most profitable song, if the map is not empty
     * and null, otherwise.
     */
    public String getMostProfitableSong(Map<Song, Double> payingSongs) {
        if (payingSongs.isEmpty()) {
            return null;
        }

        // Firstly, merge the values for the Songs that have the same names.
        Map<String, Double> mergedPayingSongs = new HashMap<>();

        for (Map.Entry<Song, Double> entry : payingSongs.entrySet()) {
            String songName = entry.getKey().getName();
            double value = entry.getValue();

            mergedPayingSongs.merge(songName, value, Double::sum);
        }

        // Sort the merged map.
        LinkedHashMap<String, Double> sortedSongs =
                                MapOperations.sortStringMapByValue(mergedPayingSongs);

        String mostProfitableSong;

        return sortedSongs.entrySet().stream().findFirst().get().getKey();
    }
}
