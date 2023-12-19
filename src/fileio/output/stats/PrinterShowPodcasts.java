package fileio.output.stats;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.audio.Podcast;
import database.users.Host;
import fileio.output.PrinterComplex;

public class PrinterShowPodcasts extends PrinterComplex {
    private final Host host;

    /* Constructor */
    public PrinterShowPodcasts(final Host host, final Session session,
                               final ArrayNode output) {
        super(session, output);
        this.host = host;
    }

    /**
     * Appends the ShowPodcasts output to the output ArrayNode.
     */
    public void print() {
        ObjectNode commandNode = mapper.createObjectNode();

        commandNode.put("command", "showPodcasts");
        commandNode.put("user", host.getUsername());
        commandNode.put("timestamp", session.getTimestamp());

        ArrayNode result = mapper.createArrayNode();
        for (Podcast podcast : host.getPodcasts()) {
            result.add(createPodcastNode(podcast));
        }

        commandNode.set("result", result);
        output.add(commandNode);
    }
}
