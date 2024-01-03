package commands.statsCommands.personalStats;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.records.Merch;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;

import java.util.ArrayList;
import java.util.List;

public final class SeeMerchCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public SeeMerchCommand(final Session session, final CommandInput commandInput,
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

        List<String> result = new ArrayList<>();

        for (Merch merch : user.getAnalytics().getMerchCollection()) {
            result.add(merch.name());
        }

        printer.printMerch(result);
    }
}
