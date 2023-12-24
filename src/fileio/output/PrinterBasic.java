package fileio.output;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.records.Notification;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.List;

/**
 * The most commonly-used printer for a simple message.
 */
public final class PrinterBasic extends Printer {
    private final CommandInput commandInput;

    /* Constructor */
    public PrinterBasic(final ArrayNode output, final CommandInput commandInput) {
        super(output);
        this.commandInput = commandInput;
    }

    /**
     * Appends a standard output, composed of generic data and a message,
     * to the output ArrayNode.
     */
    public void print(final String message) {
        ObjectNode commandNode = getMetadataNode();
        commandNode.put("message", message);

        output.add(commandNode);
    }


    /**
     * Prints a specific message for a command when the queried user is offline.
     */
    public void printOfflineUser() {
        ObjectNode commandNode = getMetadataNode();
        commandNode.put("message", commandInput.getUsername() + " is offline.");

        output.add(commandNode);
    }


    /**
     * @return ObjectNode containing general daa regarding a command.
     */
    private ObjectNode getMetadataNode() {
        ObjectNode commandNode = mapper.createObjectNode();

        commandNode.put("command", commandInput.getCommand());
        commandNode.put("user", commandInput.getUsername());
        commandNode.put("timestamp", commandInput.getTimestamp());

        return commandNode;
    }


    /**
     * Used for basic Stats Commands, whose output is formed by the
     * command name, the timestamp and a list of Strings, called result.
     * @param stringList the String list.
     */
    public void printStringResultsStats(final ArrayList<String> stringList) {
        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", commandInput.getCommand());
        commandNode.put("timestamp", commandInput.getTimestamp());

        ArrayNode result = mapper.createArrayNode();
        for (String string : stringList) {
            result.add(string);
        }

        commandNode.set("result", result);
        output.add(commandNode);
    }


    /**
     * Used for Get Notifications command.
     * @param notifications List of notifications to print.
     */
    public void printNotifications(List<Notification> notifications) {
        ObjectNode commandNode = getMetadataNode();
        ArrayNode result = mapper.createArrayNode();

        for (Notification notification : notifications) {
            ObjectNode notificationNode = mapper.createObjectNode();

            notificationNode.put("name", notification.name());
            notificationNode.put("description", notification.description());

            result.add(notificationNode);
        }

        commandNode.set("notifications", result);
        output.add(commandNode);
    }
}
