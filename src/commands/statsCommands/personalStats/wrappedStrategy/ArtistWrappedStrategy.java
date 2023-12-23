package commands.statsCommands.personalStats.wrappedStrategy;

import client.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.analytics.ArtistAnalytics;
import database.audio.Audio;
import database.users.Artist;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.MapOperations;

import java.util.LinkedHashMap;

public class ArtistWrappedStrategy implements IWrappedStrategy {
    private final Session session;
    private final CommandInput commandInput;
    private final Artist user;
    private final ArrayNode output;

    /* Constructor */
    public ArtistWrappedStrategy(final Session session, final CommandInput commandInput,
                               final Artist user, final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.user = user;
        this.output = output;
    }

    @Override
    public void wrapped() {
        ArtistAnalytics analytics = user.getArtistAnalytics();
        if (analytics.getArtistTopFans().isEmpty()) {
            PrinterBasic printer = new PrinterBasic(output, commandInput);
            printer.print("No data to show for artist " + user.getUsername() + ".");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", "wrapped");
        commandNode.put("user", user.getUsername());
        commandNode.put("timestamp", session.getTimestamp());

        LinkedHashMap<String, Integer> sortedAlbums = MapOperations.sortStringMapByValue(analytics.getArtistTopAlbums());
        LinkedHashMap<Audio, Integer> sortedSongs = MapOperations.sortAudioMapByValue(analytics.getArtistTopSongs());
        LinkedHashMap<String, Integer> sortedFans = MapOperations.sortStringMapByValue(analytics.getArtistTopFans());

        ObjectNode resultNode = mapper.createObjectNode();
        ObjectNode topAlbums = MapOperations.createStringMapObjectNode(sortedAlbums);
        ObjectNode topSongs = MapOperations.createAudioMapObjectNode(sortedSongs);
        ArrayNode topFans = MapOperations.createLinkedMapKeyArrayNode(sortedFans);

        resultNode.set("topAlbums", topAlbums);
        resultNode.set("topSongs", topSongs);
        resultNode.set("topFans", topFans);
        resultNode.put("listeners", analytics.getArtistTopFans().size());

        commandNode.set("result", resultNode);
        output.add(commandNode);
    }
}
