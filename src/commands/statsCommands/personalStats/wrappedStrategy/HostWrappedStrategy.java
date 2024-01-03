package commands.statsCommands.personalStats.wrappedStrategy;

import client.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.analytics.HostAnalytics;
import database.users.Host;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.MapOperations;

import java.util.LinkedHashMap;

public final class HostWrappedStrategy implements IWrappedStrategy {
    private final Session session;
    private final CommandInput commandInput;
    private final Host host;
    private final ArrayNode output;

    /* Constructor */
    public HostWrappedStrategy(final Session session, final CommandInput commandInput,
                               final Host host, final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.host = host;
        this.output = output;
    }

    @Override
    public void wrapped() {
        HostAnalytics analytics = host.getHostAnalytics();
        if (analytics.getHostTopFans().isEmpty()) {
            PrinterBasic printer = new PrinterBasic(output, commandInput);
            printer.print("No data to show for host " + host.getUsername());
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", "wrapped");
        commandNode.put("user", host.getUsername());
        commandNode.put("timestamp", session.getTimestamp());

        LinkedHashMap<String, Integer> sortedEpisodes
                            = MapOperations.sortStringMapByValue(analytics.getHostTopEpisodes());

        ObjectNode resultNode = mapper.createObjectNode();
        ObjectNode topEpisodes = MapOperations.createStringMapObjectNode(sortedEpisodes);

        resultNode.set("topEpisodes", topEpisodes);
        resultNode.put("listeners", analytics.getHostTopFans().size());

        commandNode.set("result", resultNode);
        output.add(commandNode);
    }
}
