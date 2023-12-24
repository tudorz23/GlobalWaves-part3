package commands.adminCommands;

import client.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.ICommand;
import database.monetization.ArtistMoneyStats;
import database.monetization.Monetization;
import utils.MapOperations;

import java.util.LinkedHashMap;
import java.util.Map;

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

        monetization.getListenedArtists().forEach(
                (key ,value) -> value.setTotalRevenue(value.getMerchRevenue() + value.getSongRevenue())
        );

        LinkedHashMap<String, ArtistMoneyStats> sortedMap = MapOperations
                                            .sortMonetization(monetization.getListenedArtists());

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
            artistNode.put("mostProfitableSong", "N/A");

            resultNode.set(entry.getKey(), artistNode);
        }

        commandNode.set("result", resultNode);
        output.add(commandNode);
    }

    public double roundDouble(Double number) {
        return Math.round(number * 100.0) / 100.0;
    }
}
