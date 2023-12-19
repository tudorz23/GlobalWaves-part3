package commands.userCommands.hostCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.records.Announcement;
import database.users.Host;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.UserType;

public final class RemoveAnnouncementCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public RemoveAnnouncementCommand(final Session session, final CommandInput commandInput,
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
        Announcement announcement;

        try {
            announcement = host.findAnnouncement(commandInput.getName());
        } catch (IllegalArgumentException exception) {
            printer.print(exception.getMessage());
            return;
        }

        host.removeAnnouncement(announcement);
        printer.print(host.getUsername() + " has successfully deleted the announcement.");
    }
}
