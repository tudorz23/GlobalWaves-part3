package commands.adminCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import commands.adminCommands.deleteUserStrategy.DeleteArtistStrategy;
import commands.adminCommands.deleteUserStrategy.DeleteBasicUserCommand;
import commands.adminCommands.deleteUserStrategy.DeleteHostStrategy;
import commands.adminCommands.deleteUserStrategy.IDeleteUserStrategy;
import database.audio.Playlist;
import database.audio.Song;
import database.users.Artist;
import database.users.BasicUser;
import database.users.Host;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;

public final class DeleteUserCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public DeleteUserCommand(final Session session, final CommandInput commandInput,
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

        session.getDatabase().simulateTimeForEveryone(session.getTimestamp());

        // Check if any user interacts with any of the user's playlists.
        if (checkPlaylistsInteractions()) {
            printer.print(user.getUsername() + " can't be deleted.");
            return;
        }

        IDeleteUserStrategy deleteUserStrategy;
        try {
            deleteUserStrategy = getDeleteUserStrategy();
        } catch (IllegalArgumentException exception) {
            printer.print("No delete strategy for this user.");
            return;
        }

        if (!deleteUserStrategy.deleteUser()) {
            printer.print(user.getUsername() + " can't be deleted.");
            return;
        }

        for (Playlist playlist : user.getPlaylists()) {
            session.getDatabase().removePlaylist(playlist);
        }

        for (Playlist playlist : user.getFollowedPlaylists()) {
            playlist.decrementFollowersCnt();
        }

        for (Song song : user.getLikedSongs()) {
            song.decrementLikeCnt();
        }

        printer.print(user.getUsername() + " was successfully deleted.");
    }


    /**
     * Checks if any user interacts with any of the user's playlists.
     * @return true if it does, false otherwise.
     */
    private boolean checkPlaylistsInteractions() {
        for (Playlist playlist : user.getPlaylists()) {
            for (User iterUser : session.getDatabase().getBasicUsers()) {
                if (iterUser.interactsWithAudio(playlist)) {
                    return true;
                }
            }

            for (User iterUser : session.getDatabase().getArtists()) {
                if (iterUser.interactsWithAudio(playlist)) {
                    return true;
                }
            }

            for (User iterUser : session.getDatabase().getHosts()) {
                if (iterUser.interactsWithAudio(playlist)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Factory method to get the appropriate Strategy object for Delete User Command.
     */
    private IDeleteUserStrategy getDeleteUserStrategy() {
        switch (user.getType()) {
            case ARTIST -> {
                return new DeleteArtistStrategy(session, (Artist) user);
            }
            case HOST -> {
                return new DeleteHostStrategy(session, (Host) user);
            }
            case BASIC_USER -> {
                return new DeleteBasicUserCommand(session, (BasicUser) user);
            }
            // Never reached.
            default -> throw new IllegalArgumentException("Invalid user.");
        }
    }
}
