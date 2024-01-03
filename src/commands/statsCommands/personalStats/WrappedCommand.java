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
import utils.enums.UserType;

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
        try {
            wrappedStrategy = getWrappedStrategy(user.getType());
        } catch (IllegalArgumentException exception) {
            return;
        }

        wrappedStrategy.wrapped();
    }


    /**
     * Factory method to get the appropriate Strategy object for Wrapped Command.
     * @throws IllegalArgumentException if the user type is invalid.
     */
    private IWrappedStrategy getWrappedStrategy(final UserType userType) {
        switch (userType) {
            case BASIC_USER -> {
                return new UserWrappedStrategy(session, commandInput, (BasicUser) user, output);
            }
            case ARTIST -> {
                return new ArtistWrappedStrategy(session, commandInput, (Artist) user, output);
            }
            case HOST -> {
                return new HostWrappedStrategy(session, commandInput, (Host) user, output);
            }
            default -> {
                throw new IllegalArgumentException("Invalid Wrapped Strategy requested.");
            }
        }
    }
}
