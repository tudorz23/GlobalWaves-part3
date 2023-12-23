package commands.statsCommands.personalStats;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import commands.statsCommands.personalStats.wrappedStrategy.ArtistWrappedStrategy;
import commands.statsCommands.personalStats.wrappedStrategy.HostWrappedStrategy;
import commands.statsCommands.personalStats.wrappedStrategy.IWrappedStrategy;
import commands.statsCommands.personalStats.wrappedStrategy.UserWrappedStrategy;
import database.users.Artist;
import database.users.BasicUser;
import database.users.Host;
import database.users.User;
import fileio.input.CommandInput;

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
}
