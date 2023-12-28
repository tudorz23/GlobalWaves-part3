package commands.adminCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.audio.Song;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.AudioType;
import utils.enums.PlayerState;
import utils.enums.PremiumState;

import java.util.Map;

public class AdBreakCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public AdBreakCommand(final Session session, final CommandInput commandInput,
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

        user.getPlayer().simulateTimePass(session.getTimestamp());

        if (user.getPlayer().getPlayerState() == PlayerState.STOPPED
                || user.getPlayer().getPlayerState() == PlayerState.EMPTY) {
            printer.print(user.getUsername() + " is not playing any music.");
            return;
        }

        if (user.getPlayer().getCurrPlaying().getType() == AudioType.PODCAST) {
            printer.print(user.getUsername() + " is not playing any music.");
            return;
        }

        if (user.getPremiumState() == PremiumState.PREMIUM) {
            printer.print(user.getUsername() + " is a premium user.");
            return;
        }


//        Map<Song, Integer> listenedBetweenAds = user.getPlayer().getListenedBetweenAds();
//        if (!listenedBetweenAds.isEmpty()) {
//        Map<Song, Double> songMonetization = session.getDatabase()
//                .computeSongMonetization(listenedBetweenAds, commandInput.getPrice());
//
//        session.getDatabase().updateArtistMonetization(songMonetization);
//        }
//
//        user.getPlayer().initListenedBetweenAds();
        user.getPlayer().setLastAdPrice(commandInput.getPrice());
        user.getPlayer().setAdIsNext(true);

        printer.print("Ad inserted successfully.");
    }
}
