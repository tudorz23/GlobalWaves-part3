package fileio.output.stats;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.audio.Album;
import database.users.Artist;
import fileio.output.PrinterComplex;

public class PrinterShowAlbums extends PrinterComplex {
    private final Artist artist;

    /* Constructor */
    public PrinterShowAlbums(final Artist artist, final Session session,
                                final ArrayNode output) {
        super(session, output);
        this.artist = artist;
    }

    /**
     * Appends the ShowAlbums output to the output ArrayNode.
     */
    public void print() {
        ObjectNode commandNode = mapper.createObjectNode();

        commandNode.put("command", "showAlbums");
        commandNode.put("user", artist.getUsername());
        commandNode.put("timestamp", session.getTimestamp());

        ArrayNode result = mapper.createArrayNode();
        for (Album album : artist.getAlbums()) {
            result.add(createAlbumNode(album));
        }

        commandNode.set("result", result);
        output.add(commandNode);
    }
}
