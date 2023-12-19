package commands.statsCommands.adminStats;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.LogStatus;

import java.util.ArrayList;

public final class GetOnlineUsersCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final ArrayNode output;

    /* Constructor */
    public GetOnlineUsersCommand(final Session session, final CommandInput commandInput,
                               final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.output = output;
    }

    @Override
    public void execute() {
        session.setTimestamp(commandInput.getTimestamp());
        PrinterBasic printer = new PrinterBasic(output, commandInput);

        ArrayList<String> result = new ArrayList<>();
        for (User user : session.getDatabase().getBasicUsers()) {
            if (user.getLogStatus() == LogStatus.ONLINE) {
                result.add(user.getUsername());
            }
        }

        printer.printStringResultsStats(result);
    }
}
