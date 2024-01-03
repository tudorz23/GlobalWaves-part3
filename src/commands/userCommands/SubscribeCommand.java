package commands.userCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.users.ContentCreator;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.PageType;

public final class SubscribeCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public SubscribeCommand(final Session session, final CommandInput commandInput,
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

        if (user.getCurrPage().getType() != PageType.ARTIST_PAGE
            && user.getCurrPage().getType() != PageType.HOST_PAGE) {
            printer.print("To subscribe you need to be on the page of an artist or host.");
            return;
        }

        ContentCreator contentCreator = (ContentCreator) user.getCurrPage().getOwningUser();

        if (user.getAnalytics().isSubscribedTo(contentCreator)) {
            user.getAnalytics().unsubscribeFrom(contentCreator);
            contentCreator.removeObserver(user);
            printer.print(user.getUsername() + " unsubscribed from " + contentCreator.getUsername()
                            + " successfully.");
            return;
        }

        user.getAnalytics().subscribeTo(contentCreator);
        contentCreator.addObserver(user);
        printer.print(user.getUsername() + " subscribed to " + contentCreator.getUsername()
                + " successfully.");
    }
}
