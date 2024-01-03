package commands.statsCommands.personalStats.wrappedStrategy;

import client.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.analytics.ArtistAnalytics;
import database.users.Artist;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.MapOperations;

import java.util.LinkedHashMap;

public final class ArtistWrappedStrategy implements IWrappedStrategy {
    private final Session session;
    private final CommandInput commandInput;
    private final Artist artist;
    private final ArrayNode output;

    /* Constructor */
    public ArtistWrappedStrategy(final Session session, final CommandInput commandInput,
                                 final Artist artist, final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.artist = artist;
        this.output = output;
    }

    @Override
    public void wrapped() {
        ArtistAnalytics analytics = artist.getArtistAnalytics();
        if (analytics.getArtistTopFans().isEmpty()) {
            PrinterBasic printer = new PrinterBasic(output, commandInput);
            printer.print("No data to show for artist " + artist.getUsername() + ".");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", "wrapped");
        commandNode.put("user", artist.getUsername());
        commandNode.put("timestamp", session.getTimestamp());

        LinkedHashMap<String, Integer> sortedAlbums
                            = MapOperations.sortStringMapByValue(analytics.getArtistTopAlbums());
        LinkedHashMap<String, Integer> sortedSongs
                            = MapOperations.sortAudioMapByValue(analytics.getArtistTopSongs());
        LinkedHashMap<String, Integer> sortedFans
                            = MapOperations.sortStringMapByValue(analytics.getArtistTopFans());

        ObjectNode resultNode = mapper.createObjectNode();

        ObjectNode topAlbums = MapOperations.createStringMapObjectNode(sortedAlbums);
        ObjectNode topSongs = MapOperations.createStringMapObjectNode(sortedSongs);
        ArrayNode topFans = MapOperations.createLinkedMapKeyArrayNode(sortedFans);

        resultNode.set("topAlbums", topAlbums);
        resultNode.set("topSongs", topSongs);
        resultNode.set("topFans", topFans);
        resultNode.put("listeners", analytics.getArtistTopFans().size());

        commandNode.set("result", resultNode);
        output.add(commandNode);
    }
}
