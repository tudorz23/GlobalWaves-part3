package commands.statsCommands.personalStats.wrappedStrategy;

import client.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.analytics.Analytics;
import database.audio.Audio;
import database.users.BasicUser;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.MapOperations;

import java.util.LinkedHashMap;

public class UserWrappedStrategy implements IWrappedStrategy {
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
        if (analytics.getTopSongs().isEmpty() && analytics.getTopPodcasts().isEmpty()) {
            PrinterBasic printer = new PrinterBasic(output, commandInput);
            printer.print("No data to show for user " + user.getUsername() + ".");
            return;
        }

        LinkedHashMap<String, Integer> sortedArtists = MapOperations.sortStringMapByValue(user.getAnalytics().getTopArtists());
        LinkedHashMap<String, Integer> sortedGenres = MapOperations.sortStringMapByValue(user.getAnalytics().getTopGenres());
        LinkedHashMap<String, Integer> sortedSongs = MapOperations.sortAudioMapByValue(user.getAnalytics().getTopSongs());
        LinkedHashMap<String, Integer> sortedAlbums = MapOperations.sortStringMapByValue(user.getAnalytics().getTopAlbums());
        LinkedHashMap<String, Integer> sortedPodcasts = MapOperations.sortAudioMapByValue(user.getAnalytics().getTopPodcasts());

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
        ObjectNode topPodcasts = MapOperations.createStringMapObjectNode(sortedPodcasts);

        resultNode.set("topArtists", topArtists);
        resultNode.set("topGenres", topGenres);
        resultNode.set("topSongs", topSongs);
        resultNode.set("topAlbums", topAlbums);
        resultNode.set("topPodcasts", topPodcasts);

        commandNode.set("result", resultNode);
        output.add(commandNode);
    }

}
