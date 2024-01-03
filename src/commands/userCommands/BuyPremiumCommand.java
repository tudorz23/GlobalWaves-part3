package commands.userCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.PremiumState;

public final class BuyPremiumCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public BuyPremiumCommand(final Session session, final CommandInput commandInput,
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

        if (user.getPremiumState() == PremiumState.PREMIUM) {
            printer.print(user.getUsername() + " is already a premium user.");
            return;
        }

        user.getPlayer().simulateTimePass(session.getTimestamp());

        user.getPlayer().initListenedAsPremium();
        user.getPlayer().setAdIsNext(false);

        user.setPremiumState(PremiumState.PREMIUM);
        printer.print(user.getUsername() + " bought the subscription successfully.");
    }
}
