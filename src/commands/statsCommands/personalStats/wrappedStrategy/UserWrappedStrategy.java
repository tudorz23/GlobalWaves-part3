package commands.statsCommands.personalStats.wrappedStrategy;

import client.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.analytics.Analytics;
import database.users.BasicUser;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.MapOperations;

import java.util.LinkedHashMap;

public final class UserWrappedStrategy implements IWrappedStrategy {
    private final Session session;
    private final CommandInput commandInput;
    private final BasicUser user;
    private final ArrayNode output;

    /* Constructor */
     public UserWrappedStrategy(final Session session, final CommandInput commandInput,
                                final BasicUser user, final ArrayNode output) {
         this.session = session;
         this.commandInput = commandInput;
         this.user = user;
         this.output = output;
     }

    @Override
    public void wrapped() {
        Analytics analytics = user.getAnalytics();
        if (analytics.getTopSongs().isEmpty() && analytics.getTopEpisodes().isEmpty()) {
            PrinterBasic printer = new PrinterBasic(output, commandInput);
            printer.print("No data to show for user " + user.getUsername() + ".");
            return;
        }

        LinkedHashMap<String, Integer> sortedArtists
                                = MapOperations.sortStringMapByValue(analytics.getTopArtists());
        LinkedHashMap<String, Integer> sortedGenres
                                = MapOperations.sortStringMapByValue(analytics.getTopGenres());
        LinkedHashMap<String, Integer> sortedSongs
                                = MapOperations.sortAudioMapByValue(analytics.getTopSongs());
        LinkedHashMap<String, Integer> sortedAlbums
                                = MapOperations.sortStringMapByValue(analytics.getTopAlbums());
        LinkedHashMap<String, Integer> sortedEpisodes
                                = MapOperations.sortStringMapByValue(analytics.getTopEpisodes());

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", "wrapped");
        commandNode.put("user", user.getUsername());
        commandNode.put("timestamp", session.getTimestamp());

        ObjectNode resultNode = mapper.createObjectNode();

        ObjectNode topArtists = MapOperations.createStringMapObjectNode(sortedArtists);
        ObjectNode topGenres = MapOperations.createStringMapObjectNode(sortedGenres);
        ObjectNode topSongs = MapOperations.createStringMapObjectNode(sortedSongs);
        ObjectNode topAlbums = MapOperations.createStringMapObjectNode(sortedAlbums);
        ObjectNode topEpisodes = MapOperations.createStringMapObjectNode(sortedEpisodes);

        resultNode.set("topArtists", topArtists);
        resultNode.set("topGenres", topGenres);
        resultNode.set("topSongs", topSongs);
        resultNode.set("topAlbums", topAlbums);
        resultNode.set("topEpisodes", topEpisodes);

        commandNode.set("result", resultNode);
        output.add(commandNode);
    }
}
