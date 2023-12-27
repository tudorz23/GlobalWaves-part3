package commands.userCommands.pageCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import pages.Page;

public class PreviousPageCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public PreviousPageCommand(final Session session, final CommandInput commandInput,
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

        if (user.getPlayer() != null) {
            user.getPlayer().simulateTimePass(session.getTimestamp());
        }

        if (user.getAnalytics().pageHistoryIsEmpty()) {
            printer.print("There are no pages left to go back.");
            return;
        }

        Page prevPage = user.getAnalytics().popPageHistory();

        user.getAnalytics().pushForwardPageHistory(user.getCurrPage());
        user.setCurrPage(prevPage);

        printer.print("The user " +  user.getUsername()
                    + " has navigated successfully to the previous page.");
    }
}
