package commands.userCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.audio.Song;
import database.monetization.Monetization;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.PremiumState;

import java.util.HashMap;
import java.util.Map;

import static utils.Constants.PREMIUM_FEE;

public class CancelPremiumCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public CancelPremiumCommand(final Session session, final CommandInput commandInput,
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

        if (user.getPremiumState() == PremiumState.FREE) {
            printer.print(user.getUsername() + " is not a premium user.");
            return;
        }

        user.getPlayer().simulateTimePass(session.getTimestamp());

        Map<Song, Integer> listenedAsPremium = user.getPlayer().getListenedAsPremium();
        if (listenedAsPremium.isEmpty()) {
            return;
        }

        Map<Song, Double> songMonetization = session.getDatabase()
                .computeSongMonetization(listenedAsPremium, PREMIUM_FEE);

        session.getDatabase().updateArtistMonetization(songMonetization);

        user.setPremiumState(PremiumState.FREE);
        printer.print(user.getUsername() + " cancelled the subscription successfully.");
    }
}
