package commands.searchbarCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.*;
import database.audio.Audio;
import database.audio.Playlist;
import database.audio.Podcast;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.*;

public final class LoadCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public LoadCommand(final Session session, final CommandInput commandInput,
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

        if (user.getSelection() == null) {
            printer.print("Please select a source before attempting to load.");
            return;
        }

        if (user.getSelection().getSearchableType() == SearchableType.USER) {
            // Cannot load an artist.
            printer.print("Cannot load an artist. Please select an audio file.");
            return;
        }

        Audio selection = (Audio) user.getSelection();
        if (selection.getType() == AudioType.PLAYLIST) {
            if (((Playlist) user.getSelection()).getSongs().isEmpty()) {
                printer.print("You can't load an empty audio collection!");
                return;
            }
        }

        Player userPlayer = user.getPlayer();

        // Load the selection into the player.
        loadSelection(userPlayer);

        userPlayer.setPrevTimeInfo(session.getTimestamp());
        userPlayer.setPlayerState(PlayerState.PLAYING);

        if (userPlayer.getCurrPlaying().getType() == AudioType.PLAYLIST
            || userPlayer.getCurrPlaying().getType() == AudioType.ALBUM) {
            userPlayer.setRepeatState(RepeatState.NO_REPEAT_COLLECTION);
        } else {
            userPlayer.setRepeatState(RepeatState.NO_REPEAT);
        }

        user.setSelection(null);
        user.setSearchResult(null);
        printer.print("Playback loaded successfully.");
    }

    /**
     * Loads the selection into the player. Podcast load is done separately
     * For songs, playlists and albums, a deep copy is directly added.
     */
    public void loadSelection(final Player player) {
        Audio selection = (Audio) user.getSelection();

        // If the selection is a podcast, check if it hasn't been listened to before.
        if (selection.getType() == AudioType.PODCAST) {
            loadPodcast(player, (Podcast) selection);
            return;
        }

        // If the selection is a song, a playlist, or an album, add a deep copy of it.
        player.setCurrPlaying(selection.getDeepCopy());
    }

    /**
     * Loads a podcast into the player. If the podcast has been listened to before,
     * that respective object is loaded. Else, a new deep copy is created and loaded.
     * Thus, it is ensured that an old podcast will start from where it was left.
     */
    public void loadPodcast(final Player player, final Podcast podcast) {
        for (Podcast listenedPodcast : user.getListenedPodcasts()) {
            if (listenedPodcast.getName().equals(podcast.getName())) {
                player.setCurrPlaying(listenedPodcast);
                return;
            }
        }

        // If it has not been listened to, add a deep copy of it.
        Podcast deepCopy = podcast.getDeepCopy();
        player.setCurrPlaying(deepCopy);
        user.getListenedPodcasts().add(deepCopy);
    }
}
