package commands.userCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.Player;
import database.audio.Song;
import database.audio.SongCollection;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.AudioType;
import utils.enums.LogStatus;
import utils.enums.PlayerState;

public final class LikeSongCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public LikeSongCommand(final Session session, final CommandInput commandInput,
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
            printer.print("Please load a source before liking or unliking.");
            return;
        }

        userPlayer.simulateTimePass(session.getTimestamp());

        if (userPlayer.getPlayerState() == PlayerState.STOPPED) {
            printer.print("Please load a source before liking or unliking.");
            return;
        }

        if (userPlayer.getCurrPlaying().getType() == AudioType.PODCAST) {
            printer.print("Loaded source is not a song.");
            return;
        }

        Song playingSong;
        if (userPlayer.getCurrPlaying().getType() == AudioType.SONG) {
            playingSong = (Song) userPlayer.getCurrPlaying();
        } else {
            SongCollection currSongCollection = (SongCollection) (userPlayer.getCurrPlaying());
            playingSong = currSongCollection.getSongs()
                            .get(currSongCollection.getPlayingSongIndex());
        }

        Song song;
        try {
            song = session.getDatabase().searchSongInDatabase(playingSong);
        } catch (IllegalArgumentException illegalArgumentException) {
            return;
        }

        if (user.getLikedSongs().contains(song)) {
            user.removeLikedSong(song);
            printer.print("Unlike registered successfully.");
            return;
        }

        user.addLikedSong(song);
        printer.print("Like registered successfully.");
    }

}
