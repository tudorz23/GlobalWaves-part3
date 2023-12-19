package commands.userCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import pages.Page;
import pages.PageFactory;
import utils.enums.UserType;

public final class ChangePageCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public ChangePageCommand(final Session session, final CommandInput commandInput,
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
            printer.print(user.getUsername() + " not a basic user, can't change pages like this.");
            return;
        }

        PageFactory pageFactory = new PageFactory(user);

        Page newPage;
        try {
            newPage = pageFactory.getPage(commandInput);
        } catch (IllegalArgumentException exception) {
            printer.print(user.getUsername() + " is trying to access a non-existent page.");
            return;
        }

        user.setCurrPage(newPage);
        printer.print(user.getUsername() + " accessed " + commandInput.getNextPage()
                + " successfully.");
    }
}
