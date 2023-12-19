package commands.playerCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.Player;
import database.audio.SongCollection;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.AudioType;
import utils.enums.LogStatus;
import utils.enums.PlayerState;
import java.util.Collections;
import java.util.Random;

public final class ShuffleCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public ShuffleCommand(final Session session, final CommandInput commandInput,
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

        Player userPlayer = user.getPlayer();

        if (userPlayer == null || userPlayer.getPlayerState() == PlayerState.EMPTY) {
            printer.print("Please load a source before using the shuffle function.");
            return;
        }

        userPlayer.simulateTimePass(session.getTimestamp());

        if (userPlayer.getPlayerState() == PlayerState.STOPPED) {
            printer.print("Please load a source before using the shuffle function.");
            return;
        }

        if (userPlayer.getCurrPlaying().getType() != AudioType.PLAYLIST
            && userPlayer.getCurrPlaying().getType() != AudioType.ALBUM) {
            printer.print("The loaded source is not a playlist or an album.");
            return;
        }

        SongCollection currSongCollection = (SongCollection) userPlayer.getCurrPlaying();

        // Reinitialize the shuffle array.
        currSongCollection.initializeShuffleArray();

        if (userPlayer.isShuffle()) {
            userPlayer.setShuffle(false);
            printer.print("Shuffle function deactivated successfully.");
            return;
        }

        Collections.shuffle(currSongCollection.getShuffleArray(),
                            new Random(commandInput.getSeed()));

        userPlayer.setShuffle(true);
        printer.print("Shuffle function activated successfully.");
    }
}
