package commands.statsCommands.personalStats.wrappedStrategy;

import client.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.statsCommands.personalStats.WrappedCommand;
import database.analytics.HostAnalytics;
import database.users.Host;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;

import java.util.LinkedHashMap;

public class HostWrappedStrategy implements IWrappedStrategy {
    private final Session session;
    private final CommandInput commandInput;
    private final Host user;
    private final ArrayNode output;

    /* Constructor */
    public HostWrappedStrategy(final Session session, final CommandInput commandInput,
                                 final Host user, final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.user = user;
        this.output = output;
    }

    @Override
    public void wrapped() {
        HostAnalytics analytics = user.getHostAnalytics();
        if (analytics.getHostTopFans().isEmpty()) {
            PrinterBasic printer = new PrinterBasic(output, commandInput);
            printer.print("No data to show for user " + user.getUsername());
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", "wrapped");
        commandNode.put("user", user.getUsername());
        commandNode.put("timestamp", session.getTimestamp());

        LinkedHashMap<String, Integer> sortedEpisodes = WrappedCommand.sortStringMapByValue(analytics.getHostTopEpisodes());

        ObjectNode resultNode = mapper.createObjectNode();
        ObjectNode topEpisodes = WrappedCommand.createStringMapObjectNode(sortedEpisodes);

        resultNode.set("topEpisodes", topEpisodes);
        resultNode.put("listeners", analytics.getHostTopFans().size());

        commandNode.set("result", resultNode);
        output.add(commandNode);
    }
}
