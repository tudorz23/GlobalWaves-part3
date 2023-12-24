package commands.userCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.monetization.Monetization;
import database.records.Merch;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import pages.ArtistPage;
import utils.enums.PageType;

public class BuyMerchCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public BuyMerchCommand(final Session session, final CommandInput commandInput,
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

        if (user.getCurrPage().getType() != PageType.ARTIST_PAGE) {
            printer.print("Cannot buy merch from this page.");
            return;
        }

        ArtistPage page = (ArtistPage) user.getCurrPage();

        Merch merch;
        try {
            merch = page.getMerch(commandInput.getName());
        } catch (IllegalArgumentException exception) {
            printer.print("The merch " + commandInput.getName() + " doesn't exist.");
            return;
        }

        user.getAnalytics().addMerch(merch);

        String artist = page.getOwningUser().getUsername();
        session.getDatabase().getMonetization().addMerchRevenue(artist, (double) merch.price());

        printer.print(user.getUsername() + " has added new merch successfully.");
    }
}
