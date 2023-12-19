package commands.statsCommands.adminStats;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.audio.Album;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import static utils.Constants.MAX_ALBUM_RANK_NUMBER;
import java.util.ArrayList;
import java.util.Comparator;

public final class GetTop5AlbumsCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final ArrayNode output;

    /* Constructor */
    public GetTop5AlbumsCommand(final Session session, final CommandInput commandInput,
                                final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.output = output;
    }

    @Override
    public void execute() {
        session.setTimestamp(commandInput.getTimestamp());
        PrinterBasic printer = new PrinterBasic(output, commandInput);

        ArrayList<Album> albums = new ArrayList<>(session.getDatabase().getAlbums());

        // Sort albums by the likes number using method reference operator.
        albums.sort(Comparator.comparing(Album::computeLikeCnt).reversed()
                .thenComparing(Album::getName));

        while (albums.size() > MAX_ALBUM_RANK_NUMBER) {
            albums.remove(albums.size() - 1);
        }

        ArrayList<String> results = new ArrayList<>();
        for (Album album : albums) {
            results.add(album.getName());
        }

        printer.printStringResultsStats(results);
    }
}
