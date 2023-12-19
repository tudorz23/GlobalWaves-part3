package fileio.output;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.Searchable;
import database.audio.Audio;
import database.users.User;
import utils.enums.SearchableType;

public final class PrinterSearch extends PrinterComplex {
    private final User user;

    /* Constructor */
    public PrinterSearch(final User user, final Session session,
                         final ArrayNode output) {
        super(session, output);
        this.user = user;
    }

    /**
     * Appends the Search output to the output ArrayNode.
     */
    public void print() {
        ObjectNode commandNode = getMetadataNode();

        String message = "Search returned " + user.getSearchResult().size() + " results";
        commandNode.put("message", message);

        ArrayNode results = mapper.createArrayNode();
        for (Searchable searchable : user.getSearchResult()) {
            if (searchable.getSearchableType() == SearchableType.USER) {
                results.add(((User) searchable).getUsername());
            } else {
                results.add(((Audio) searchable).getName());
            }
        }

        commandNode.set("results", results);
        output.add(commandNode);
    }


    /**
     * Prints standard details regarding an offline user.
     */
    public void printOfflineUser() {
        ObjectNode commandNode = getMetadataNode();
        commandNode.put("message", user.getUsername() + " is offline.");

        ArrayNode results = mapper.createArrayNode();
        commandNode.set("results", results);
        output.add(commandNode);
    }


    /**
     * Helper for printing common details of a command.
     * @return ObjectNode containing the data.
     */
    private ObjectNode getMetadataNode() {
        ObjectNode commandNode = mapper.createObjectNode();

        commandNode.put("command", "search");
        commandNode.put("user", user.getUsername());
        commandNode.put("timestamp", session.getTimestamp());

        return commandNode;
    }
}
