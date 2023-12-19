package commands.searchbarCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import commands.searchbarCommands.searchStrategy.*;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterSearch;
import utils.enums.LogStatus;
import utils.enums.PlayerState;
import java.util.ArrayList;

public final class SearchCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public SearchCommand(final Session session, final CommandInput commandInput,
                         final User user, final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.user = user;
        this.output = output;
    }

    @Override
    public void execute() {
        session.setTimestamp(commandInput.getTimestamp());
        PrinterSearch printer = new PrinterSearch(user, session, output);

        if (user.getLogStatus() == LogStatus.OFFLINE) {
            printer.printOfflineUser();
            return;
        }

        if (user.getPlayer() != null && user.getPlayer().getPlayerState() != PlayerState.EMPTY) {
            user.getPlayer().simulateTimePass(session.getTimestamp());
        }

        // Clear the old search result, the previous selection and the player.
        user.setSearchResult(new ArrayList<>());
        user.setSelection(null);
        user.getPlayer().emptyPlayer();

        ISearchStrategy searchStrategy;

        try {
            searchStrategy = getSearchStrategy();
        } catch (IllegalArgumentException illegalArgumentException) {
            return;
        }

        searchStrategy.search();
        printer.print();
    }

    /**
     * Factory method to get the appropriate Strategy object for Search Command.
     * @throws IllegalArgumentException if the searching criteria is invalid.
     */
    private ISearchStrategy getSearchStrategy() {
        switch (commandInput.getType()) {
            case "song" -> {
                return new SearchSongStrategy(session, commandInput, user);
            }
            case "playlist" -> {
                return new SearchPlaylistStrategy(session, commandInput, user);
            }
            case "podcast" -> {
                return new SearchPodcastStrategy(session, commandInput, user);
            }
            case "album" -> {
                return new SearchAlbumStrategy(session, commandInput, user);
            }
            case "artist" -> {
                return new SearchArtistStrategy(session, commandInput, user);
            }
            case "host" -> {
                return new SearchHostStrategy(session, commandInput, user);
            }
            default -> throw new IllegalArgumentException("Invalid search criteria.");
        }
    }
}
