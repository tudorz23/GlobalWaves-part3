package commands.userCommands.pageCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import pages.Page;

public final class NextPageCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public NextPageCommand(final Session session, final CommandInput commandInput,
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

        if (user.getAnalytics().forwardPageHistoryIsEmpty()) {
            printer.print("There are no pages left to go forward.");
            return;
        }

        Page nextPage = user.getAnalytics().popForwardPageHistory();

        user.getAnalytics().pushPageHistory(user.getCurrPage());
        user.setCurrPage(nextPage);

        printer.print("The user " +  user.getUsername()
                + " has navigated successfully to the next page.");
    }
}
