package commands.statsCommands.personalStats;

import client.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.ICommand;
import commands.statsCommands.personalStats.wrappedStrategy.ArtistWrappedStrategy;
import commands.statsCommands.personalStats.wrappedStrategy.HostWrappedStrategy;
import commands.statsCommands.personalStats.wrappedStrategy.IWrappedStrategy;
import commands.statsCommands.personalStats.wrappedStrategy.UserWrappedStrategy;
import database.audio.Audio;
import database.users.Artist;
import database.users.BasicUser;
import database.users.Host;
import database.users.User;
import fileio.input.CommandInput;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class WrappedCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public WrappedCommand(final Session session, final CommandInput commandInput,
                          final User user, final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.user = user;
        this.output = output;
    }

    @Override
    public void execute() {
        session.setTimestamp(commandInput.getTimestamp());

        session.getDatabase().simulateTimeForEveryone(session.getTimestamp());

        IWrappedStrategy wrappedStrategy;

        switch (user.getType()) {
            case BASIC_USER -> {
                wrappedStrategy = new UserWrappedStrategy(session, commandInput, (BasicUser) user, output);
            }
            case ARTIST -> {
                wrappedStrategy = new ArtistWrappedStrategy(session, commandInput, (Artist) user, output);
            }
            case HOST -> {
                wrappedStrategy = new HostWrappedStrategy(session, commandInput, (Host) user, output);
            }
            default -> {
                return;
            }
        }

        wrappedStrategy.wrapped();
    }


    public static LinkedHashMap<Audio, Integer> sortAudioMapByValue(Map<Audio, Integer> map) {
        // https://stackoverflow.com/questions/2864840/treemap-sort-by-value
        LinkedHashMap<Audio, Integer> sortedMap = map.entrySet().stream()
                .sorted((e1, e2) -> {
                    int valueCompare = e2.getValue().compareTo(e1.getValue());
                    if (valueCompare != 0) {
                        return valueCompare;
                    }
                    return e1.getKey().getName().compareTo(e2.getKey().getName());
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return sortedMap;
    }


    public static LinkedHashMap<String, Integer> sortStringMapByValue(Map<String, Integer> map) {
        // https://stackoverflow.com/questions/2864840/treemap-sort-by-value
        LinkedHashMap<String, Integer> sortedMap = map.entrySet().stream()
                .sorted((e1, e2) -> {
                    int valueCompare = e2.getValue().compareTo(e1.getValue());
                    if (valueCompare != 0) {
                        return valueCompare;
                    }
                    return e1.getKey().compareTo(e2.getKey());
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return sortedMap;
    }


    public static ObjectNode createStringMapObjectNode(LinkedHashMap<String, Integer> map) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode mapNode = mapper.createObjectNode();

        int cnt = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            mapNode.put(entry.getKey(), entry.getValue());

            cnt++;
            if (cnt == 5) {
                break;
            }
        }

        return mapNode;
    }


    public static ObjectNode createAudioMapObjectNode(LinkedHashMap<Audio, Integer> map) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode mapNode = mapper.createObjectNode();

        int cnt = 0;
        for (Map.Entry<Audio, Integer> entry : map.entrySet()) {
            mapNode.put(entry.getKey().getName(), entry.getValue());

            cnt++;
            if (cnt == 5) {
                break;
            }
        }

        return mapNode;
    }


    public static ArrayNode createLinkedMapKeyArrayNode(LinkedHashMap<String, Integer> map) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode keyArrayNode = mapper.createArrayNode();

        int cnt = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            keyArrayNode.add(entry.getKey());

            cnt++;
            if (cnt == 5) {
                break;
            }
        }

        return keyArrayNode;
    }
}
