package commands.userCommands.hostCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.audio.Podcast;
import database.users.Host;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.UserType;

public final class AddPodcastCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public AddPodcastCommand(final Session session, final CommandInput commandInput,
                             final User user, final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.user = user;
        this.output = output;
    }

    @Override
    public void execute() {
        session.setTimestamp(commandInput.getTimestamp());
        PrinterBasic printer = new PrinterBasic(output, commandInput);

        if (user.getType() != UserType.HOST) {
            printer.print(user.getUsername() + " is not a host.");
            return;
        }

        Host host = (Host) user;

        Podcast newPodcast;
        try {
            newPodcast = host.addPodcast(commandInput);
        } catch (IllegalArgumentException illegalArgumentException) {
            printer.print(illegalArgumentException.getMessage());
            return;
        }

        session.getDatabase().addPodcast(newPodcast);
        printer.print(user.getUsername() + " has added new podcast successfully.");
    }
}
