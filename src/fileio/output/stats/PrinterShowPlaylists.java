package fileio.output.stats;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.audio.Playlist;
import database.users.User;
import fileio.output.PrinterComplex;

public final class PrinterShowPlaylists extends PrinterComplex {
    private final User user;

    /* Constructor */
    public PrinterShowPlaylists(final User user, final Session session,
                                final ArrayNode output) {
        super(session, output);
        this.user = user;
    }

    /**
     * Appends the ShowPlaylists output to the output ArrayNode.
     */
    public void print() {
        ObjectNode commandNode = mapper.createObjectNode();

        commandNode.put("command", "showPlaylists");
        commandNode.put("user", user.getUsername());
        commandNode.put("timestamp", session.getTimestamp());

        ArrayNode result = mapper.createArrayNode();
        for (Playlist playlist : user.getPlaylists()) {
            result.add(createPlaylistNode(playlist));
        }

        commandNode.set("result", result);
        output.add(commandNode);
    }

}
