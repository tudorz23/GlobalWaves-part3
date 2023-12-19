package commands.statsCommands.personalStats;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.users.Host;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.stats.PrinterShowPodcasts;

public final class ShowPodcastsCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public ShowPodcastsCommand(final Session session, final CommandInput commandInput,
                               final User user, final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.user = user;
        this.output = output;
    }

    @Override
    public void execute() {
        session.setTimestamp(commandInput.getTimestamp());
        PrinterShowPodcasts printer = new PrinterShowPodcasts((Host) user, session, output);

        printer.print();
    }
}
