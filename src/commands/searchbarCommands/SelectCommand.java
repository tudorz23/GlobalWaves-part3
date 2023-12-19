package commands.searchbarCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.audio.Audio;
import database.users.Artist;
import database.users.Host;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.LogStatus;
import utils.enums.SearchableType;
import utils.enums.UserType;

public final class SelectCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public SelectCommand(final Session session, final CommandInput commandInput,
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

        if (user.getLogStatus() == LogStatus.OFFLINE) {
            printer.printOfflineUser();
            return;
        }

        if (user.getSearchResult() == null) {
            printer.print("Please conduct a search before making a selection.");
            return;
        }

        if (commandInput.getItemNumber() > user.getSearchResult().size()) {
            printer.print("The selected ID is too high.");
            return;
        }

        int index = commandInput.getItemNumber() - 1;
        user.setSelection(user.getSearchResult().get(index));

        if (user.getSelection().getSearchableType() == SearchableType.USER) {
            User selection = (User) user.getSelection();

            if (selection.getType() == UserType.ARTIST) {
                user.setCurrPage(((Artist) selection).getOfficialPage());
            } else {
                user.setCurrPage(((Host) selection).getOfficialPage());
            }
            printer.print("Successfully selected " + selection.getUsername() + "'s page.");
            return;
        }

        printer.print("Successfully selected " + ((Audio) user.getSelection()).getName() + ".");
    }
}
