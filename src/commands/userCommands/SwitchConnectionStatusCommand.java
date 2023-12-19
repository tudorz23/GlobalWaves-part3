package commands.userCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.Player;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.LogStatus;
import utils.enums.UserType;

public final class SwitchConnectionStatusCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public SwitchConnectionStatusCommand(final Session session, final CommandInput commandInput,
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

        if (user.getType() != UserType.BASIC_USER) {
            printer.print(user.getUsername() + " is not a normal user.");
            return;
        }

        Player userPlayer = user.getPlayer();

        if (user.getLogStatus() == LogStatus.OFFLINE) {
            user.setLogStatus(LogStatus.ONLINE);
            userPlayer.setPrevTimeInfo(session.getTimestamp());

            printer.print(user.getUsername() + " has changed status successfully.");
            return;
        }

        userPlayer.simulateTimePass(session.getTimestamp());
        user.setLogStatus(LogStatus.OFFLINE);

        printer.print(user.getUsername() + " has changed status successfully.");
    }
}
