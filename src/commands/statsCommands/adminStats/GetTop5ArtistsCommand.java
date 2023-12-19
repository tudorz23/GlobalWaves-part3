package commands.statsCommands.adminStats;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.users.Artist;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;

import java.util.ArrayList;
import java.util.Comparator;

import static utils.Constants.MAX_ARTIST_RANK_NUMBER;

public final class GetTop5ArtistsCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final ArrayNode output;

    /* Constructor */
    public GetTop5ArtistsCommand(final Session session, final CommandInput commandInput,
                                final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.output = output;
    }

    @Override
    public void execute() {
        session.setTimestamp(commandInput.getTimestamp());
        PrinterBasic printer = new PrinterBasic(output, commandInput);

        ArrayList<Artist> artists = new ArrayList<>(session.getDatabase().getArtists());

        // Sort artists by the likes number using method reference operator.
        artists.sort(Comparator.comparing(Artist::computeLikeCnt).reversed());

        while (artists.size() > MAX_ARTIST_RANK_NUMBER) {
            artists.remove(artists.size() - 1);
        }

        ArrayList<String> results = new ArrayList<>();
        for (Artist artist : artists) {
            results.add(artist.getUsername());
        }

        printer.printStringResultsStats(results);
    }
}
