package commands.statsCommands.adminStats;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.audio.Playlist;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.PlaylistVisibility;
import java.util.ArrayList;
import java.util.Comparator;

import static utils.Constants.MAX_PLAYLIST_RANK_NUMBER;

public final class GetTop5PlaylistsCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final ArrayNode output;

    /* Constructor */
    public GetTop5PlaylistsCommand(final Session session, final CommandInput commandInput,
                                   final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.output = output;
    }

    @Override
    public void execute() {
        session.setTimestamp(commandInput.getTimestamp());
        PrinterBasic printer = new PrinterBasic(output, commandInput);

        ArrayList<Playlist> publicPlaylists = new ArrayList<>();
        for (Playlist playlist : session.getDatabase().getPlaylists()) {
            if (playlist.getVisibility() == PlaylistVisibility.PUBLIC) {
                publicPlaylists.add(playlist);
            }
        }

        // Sort playlists by the followers count, decreasingly using method reference operator.
        // Because playlists are added to the Database as they are created, they are
        // automatically sorted by "age" in case of equality for followers.
        publicPlaylists.sort(Comparator.comparing(Playlist::getFollowersCnt).reversed());

        while (publicPlaylists.size() > MAX_PLAYLIST_RANK_NUMBER) {
            publicPlaylists.remove(publicPlaylists.size() - 1);
        }

        ArrayList<String> results = new ArrayList<>();
        for (Playlist playlist : publicPlaylists) {
            results.add(playlist.getName());
        }

        printer.printStringResultsStats(results);
    }
}
